package weblogic.t3.srvr;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.security.AccessController;
import java.util.Hashtable;
import weblogic.common.T3Exception;
import weblogic.common.internal.T3ClientParams;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerChannel;
import weblogic.rjvm.MsgAbbrevOutputStream;
import weblogic.rjvm.PeerGoneEvent;
import weblogic.rjvm.PeerGoneListener;
import weblogic.rjvm.RJVM;
import weblogic.rjvm.RJVMManager;
import weblogic.rjvm.RemoteInvokable;
import weblogic.rjvm.RemoteRequest;
import weblogic.rmi.internal.OIDManager;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.time.common.internal.TimeEventGenerator;
import weblogic.utils.AssertionError;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public final class ClientContext extends ExecutionContext implements RemoteClientContext, RemoteInvokable, Runnable, Scavengable, PeerGoneListener {
   private AuthenticatedUser user;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DebugLogger debugConnection = DebugLogger.getDebugLogger("DebugConnection");
   private static Hashtable cliConTable = new Hashtable();
   private static final String NAME_SEPARATOR = ".";
   private static int nextIncarnation = 0;
   private static Hashtable ccNameTable = new Hashtable();
   private static Object tableLock = new Object();
   private RJVM rjvm = null;
   private String ccName;
   private byte QOS;
   private int idleCallbackID;
   private ClientMsg cm;
   private int ccID = -1;
   private long disconnectNoticed;
   private boolean hardDisconnectOccurred;
   private long lastWorkPerformed;
   private int workQueueDepth;
   private Object workQueueDepthLock = new Object();
   private boolean dead;
   private static int defHDTM = 0;
   private static int defSDTM = 0;
   private static int defISDTM = -1;

   private static void addEntry(String var0, ClientContext var1) {
      cliConTable.put(var0, var1);
   }

   private static void removeEntry(String var0) {
      cliConTable.remove(var0);
   }

   private static ClientContext findContext(String var0) {
      return (ClientContext)cliConTable.get(var0);
   }

   private static String makeWSIDSuffix() {
      String var0 = nextIncarnation + "." + TimeEventGenerator.getLaunch().getTime();
      ++nextIncarnation;
      return var0;
   }

   public static ClientContext getClientContext(RJVM var0, String var1, UserInfo var2, int var3, byte var4) throws T3Exception {
      synchronized(tableLock) {
         ClientContext var6;
         if (var1 != null && !var1.equals("")) {
            if (isWSID(var1)) {
               var6 = findContext(var1);
               if (var6 == null) {
                  throw new T3Exception("Attempt to connect to workspace: '" + var1 + "' which doesn't exist");
               }
            } else {
               String var7 = var2.getName() + "." + var1;
               var6 = (ClientContext)ccNameTable.get(var7);
               if (var6 == null) {
                  var6 = new ClientContext(var7, var1, var4);
                  ccNameTable.put(var7, var6);
               }
            }
         } else {
            var6 = new ClientContext((String)null, (String)null, var4);
         }

         var6.bind(var0, var3);
         return var6;
      }
   }

   public int hashCode() {
      return this.ccID;
   }

   public boolean equals(Object var1) {
      return var1 == this;
   }

   public AuthenticatedUser getUser() {
      return this.user;
   }

   public AuthenticatedSubject getSubject() {
      return SecurityServiceManager.getSealedSubjectFromWire(kernelId, this.getUser());
   }

   RJVM getRJVM() {
      return this.rjvm;
   }

   private ClientContext(String var1, String var2, byte var3) throws T3Exception {
      super(makeWSIDSuffix());
      this.ccName = var1;
      this.QOS = var3;
      this.user = this.getCurrentSubject();
      this.cm = new ClientMsg();
      this.cm.wsName = var2;
      this.cm.serverName = ManagementService.getRuntimeAccess(kernelId).getServer().getName();
      this.disconnectNoticed = 0L;
      this.hardDisconnectOccurred = false;
      this.lastWorkPerformed = TimeEventGenerator.getCurrentMins() + 1L;
      this.workQueueDepth = 0;
      this.dead = false;
      addEntry(this.getID(), this);
      Scavenger.addScavengable(this.getID(), this);
      this.ccID = OIDManager.getInstance().getNextObjectID();
      RJVMManager.getLocalRJVM().getFinder().put(this.ccID, this);
   }

   private boolean isBound() {
      return this.rjvm != null;
   }

   private void bind(RJVM var1, int var2) throws T3Exception {
      if (this.isBound()) {
         throw new T3Exception("Attempt to bind to ClientContext: '" + this + "' that is already bound");
      } else if (this.dead) {
         throw new T3Exception("Attempt to bind to ClientContext: '" + this + "' that is dead");
      } else {
         this.rjvm = var1;
         this.idleCallbackID = var2;
         this.disconnectNoticed = 0L;
         this.hardDisconnectOccurred = false;
         var1.addPeerGoneListener(this);
      }
   }

   private void unbind() {
      if (!this.isBound()) {
         T3SrvrLogger.logAttemptUnbindUnboundClientContext(this.toString());
      } else {
         this.rjvm.removePeerGoneListener(this);
         this.rjvm = null;
         if (this.disconnectNoticed == 0L) {
            this.disconnectNoticed = TimeEventGenerator.getCurrentMins();
         }

      }
   }

   public String toString() {
      return "ClientContext - id: '" + this.getID() + "', bound: '" + this.isBound() + "', dead: '" + this.dead + "'";
   }

   void incWorkQueueDepth() {
      synchronized(this.workQueueDepthLock) {
         ++this.workQueueDepth;
      }

      this.lastWorkPerformed = TimeEventGenerator.getCurrentMins();
   }

   void decWorkQueueDepth() {
      synchronized(this.workQueueDepthLock) {
         --this.workQueueDepth;
      }
   }

   private void enqueueWork(final Runnable var1) {
      if (this.dead) {
         throw new AssertionError("Connection: '" + this + "' attempted to enqueue work: '" + var1 + "' when it is dead.");
      } else {
         this.incWorkQueueDepth();
         if (SubjectUtils.doesUserHaveAnyAdminRoles(this.getSubject())) {
            WorkManagerFactory.getInstance().getSystem().schedule(var1);
         } else {
            WorkManagerFactory.getInstance().getDefault().schedule(new WorkAdapter() {
               public void run() {
                  try {
                     var1.run();
                  } catch (Exception var2) {
                     throw new RuntimeException(var2);
                  }
               }
            });
         }

      }
   }

   private boolean checkWorkQueueDepth(int var1) {
      if (this.workQueueDepth == var1) {
         return true;
      } else {
         if (this.workQueueDepth > var1) {
            if (Kernel.DEBUG && debugConnection.isDebugEnabled()) {
               T3SrvrLogger.logCCHasPendingExecuteRequests(this.toString(), this.workQueueDepth - var1);
            }
         } else if (this.workQueueDepth < 0) {
            if (Kernel.DEBUG && debugConnection.isDebugEnabled()) {
               T3SrvrLogger.logCCHasNegativeWorkQueueDepth(this.toString(), this.workQueueDepth);
            }

            this.workQueueDepth = 0;
         }

         return false;
      }
   }

   public void invoke(RemoteRequest var1) throws RemoteException {
      String var2;
      Object var3;
      try {
         var2 = var1.readAbbrevString();
         var3 = var1.readObjectWL();
      } catch (IOException var5) {
         throw new UnmarshalException("", var5);
      } catch (ClassNotFoundException var6) {
         throw new UnmarshalException("", var6);
      }

      if ("XZZdisconnectZZX".equals(var2)) {
         this.requestKill();
      } else {
         this.enqueueWork(new ClientRequest(var2, var3, this, var1));
      }

   }

   public void setHardDisconnectTimeoutMins(int var1) {
      this.cm.hardDisconnectTimeoutMins = var1;
   }

   public void setSoftDisconnectTimeoutMins(int var1) {
      this.cm.softDisconnectTimeoutMins = var1;
   }

   public void setIdleDisconnectTimeoutMins(int var1) {
      this.cm.idleSoftDisconnectTimeoutMins = var1;
   }

   public void setVerbose(boolean var1) {
      this.cm.verbose = var1;
   }

   public T3ClientParams getParams() {
      T3ClientParams var1 = new T3ClientParams();
      var1.verbose = Kernel.DEBUG && debugConnection.isDebugEnabled();
      var1.QOS = this.QOS;
      var1.hardDisconnectTimeoutMins = this.cm.hardDisconnectTimeoutMins;
      var1.softDisconnectTimeoutMins = this.cm.softDisconnectTimeoutMins;
      var1.idleSoftDisconnectTimeoutMins = this.cm.idleSoftDisconnectTimeoutMins;
      var1.serverName = this.cm.serverName;
      var1.wsName = this.cm.wsName;
      var1.wsID = this.getID();
      var1.ccID = this.ccID;
      var1.rcc = this;
      var1.user = this.getCurrentSubject();
      return var1;
   }

   private AuthenticatedUser getCurrentSubject() {
      return SecurityServiceManager.getCurrentSubject(kernelId);
   }

   private void sendUnsolicitedData(int var1, Object var2) throws IOException {
      if (this.isBound()) {
         try {
            MsgAbbrevOutputStream var3 = this.rjvm.getRequestStream((ServerChannel)null);
            var3.writeObjectWL(var2);
            var3.sendOneWay(var1, this.QOS);
         } catch (IOException var4) {
            T3SrvrLogger.logFailedSendingUnsolicitedMessage(var2.toString(), var4);
            throw var4;
         }
      }
   }

   public void peerGone(PeerGoneEvent var1) {
      if (!this.dead) {
         if (Kernel.DEBUG && debugConnection.isDebugEnabled()) {
            T3SrvrLogger.logConnectionUnexpectedlyLostHardDisconnect(this.toString(), var1.getReason());
         }

         this.hardDisconnectOccurred = true;
         this.requestKill();
      }
   }

   public void scavenge(int var1) throws IOException {
      synchronized(tableLock) {
         if (this.dead) {
            return;
         }

         if (!this.isBound()) {
            this.dieIfTimedOut(0);
            return;
         }
      }

      if (this.checkIdleDisconnectTimeout() && this.checkWorkQueueDepth(0)) {
         if (Kernel.DEBUG && debugConnection.isDebugEnabled()) {
            T3SrvrLogger.logTimingOutClientContextOnIdle(this.toString());
         }

         this.cm.cmd = 8;
         this.cm.reason = "Removing client because of idle disconnect timeout";
         this.sendUnsolicitedData(this.idleCallbackID, this.cm);
         this.requestKill();
      }

   }

   private void requestKill() {
      synchronized(tableLock) {
         if (!this.isBound()) {
            if (Kernel.DEBUG && debugConnection.isDebugEnabled()) {
               T3SrvrLogger.logIgnoringCCDeathRequest(this.toString());
            }

         } else {
            this.unbind();
            if (Kernel.DEBUG && debugConnection.isDebugEnabled()) {
               T3SrvrLogger.logSchedulingClientContextDeath(this.toString());
            }

            this.enqueueWork(this);
         }
      }
   }

   public synchronized void run() {
      synchronized(tableLock) {
         if (!this.dead) {
            if (!this.isBound()) {
               this.dieIfTimedOut(1);
            }

            this.decWorkQueueDepth();
         }
      }
   }

   private void dieIfTimedOut(int var1) {
      if ((this.hardDisconnectOccurred && this.checkHardDisconnectTimeout() || !this.hardDisconnectOccurred && this.checkSoftDisconnectTimeout()) && this.checkWorkQueueDepth(var1)) {
         if (this.hardDisconnectOccurred) {
            if (Kernel.DEBUG && debugConnection.isDebugEnabled()) {
               T3SrvrLogger.logRemovingClientContextHardDisconnect(this.toString());
            }
         } else if (Kernel.DEBUG && debugConnection.isDebugEnabled()) {
            T3SrvrLogger.logRemovingClientContextSoftDisconnect(this.toString());
         }

         this.dead = true;
         if (this.ccName != null) {
            ccNameTable.remove(this.ccName);
         }

         removeEntry(this.getID());
         Scavenger.removeScavengable(this.getID());
         RJVMManager.getLocalRJVM().getFinder().remove(this.ccID);
         this.clear();
      }

   }

   private boolean checkSoftDisconnectTimeout() {
      int var1 = this.cm.softDisconnectTimeoutMins;
      if (var1 == -2) {
         var1 = defSDTM;
      }

      if (var1 == -1) {
         return false;
      } else {
         int var2 = TimeEventGenerator.deltaMins(this.disconnectNoticed);
         if (var2 >= var1) {
            return true;
         } else {
            if (Kernel.DEBUG && debugConnection.isDebugEnabled()) {
               T3SrvrLogger.logSoftDisconnectPendingMins(var1);
            }

            return false;
         }
      }
   }

   private boolean checkHardDisconnectTimeout() {
      int var1 = this.cm.hardDisconnectTimeoutMins;
      if (var1 == -2) {
         var1 = defHDTM;
      }

      if (var1 == -1) {
         return false;
      } else {
         int var2 = TimeEventGenerator.deltaMins(this.disconnectNoticed);
         if (var2 >= var1) {
            return true;
         } else {
            if (Kernel.DEBUG && debugConnection.isDebugEnabled()) {
               T3SrvrLogger.logHardDisconnectPendingMins(var1);
            }

            return false;
         }
      }
   }

   private boolean checkIdleDisconnectTimeout() {
      int var1 = this.cm.idleSoftDisconnectTimeoutMins;
      if (var1 == -2) {
         var1 = defISDTM;
      }

      if (var1 == -1) {
         return false;
      } else {
         int var2 = TimeEventGenerator.deltaMins(this.lastWorkPerformed);
         if (var2 >= var1) {
            return true;
         } else {
            if (Kernel.DEBUG && debugConnection.isDebugEnabled()) {
               T3SrvrLogger.logIdleDisconnectPendingMins(var1);
            }

            return false;
         }
      }
   }
}
