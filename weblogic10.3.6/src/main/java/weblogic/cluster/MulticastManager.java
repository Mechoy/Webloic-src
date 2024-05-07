package weblogic.cluster;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.WLObjectInputStream;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.health.HealthMonitorService;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerChannelManager;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.t3.srvr.SetUIDRendezvous;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.utils.io.UnsyncByteArrayOutputStream;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;
import weblogic.work.WorkManagerImpl;

public class MulticastManager extends WorkAdapter implements NakedTimerListener, MulticastSessionIDConstants {
   static final int HEARTBEAT_PERIOD_MILLIS = 10000;
   static final int MAX_FRAGMENT_SIZE = Kernel.getConfig().getMTUSize() - 100;
   static final int ONE_KILO_BYTE = 1024;
   private static final int MAX_RETRY_DELAY = 10000;
   private static final int RETRY_DELAY_INCREMENT = 1000;
   private int currentDelay = 0;
   private final TimerManager timerManager;
   private static final String osName = initOSNameProp();
   private static final boolean isLinux;
   private static final AuthenticatedSubject kernelId;
   private static MulticastManager theMulticastManager;
   private boolean serverResumed = false;
   private FragmentSocket sock;
   private byte[] fragmentBuffer;
   private boolean shutdown;
   private ArrayList senders;
   private ArrayList currentHeartbeatItems;
   private ArrayList suspendedSendersLastSeqHBI;
   private MulticastSender heartbeatSender;
   private long messagesLostCount;
   private long resendRequestsCount;
   private int localDomainNameHash;
   private int localClusterNameHash;
   private String localDomainName;
   private String localClusterName;
   private long numForeignFragementsDropped;
   private boolean canReceiveOwnMessages = false;
   private long sendStartTimestamp = 0L;
   private final int port;
   private final String multicastAddress;
   private final String clusterName;
   private final boolean isUnicast;

   public static MulticastManager theOne() {
      return theMulticastManager;
   }

   static void initialize(String var0, String var1, int var2, byte var3, long var4) throws IOException, UnknownHostException {
      theMulticastManager = new MulticastManager(var0, var1, var2, var3, var4);
   }

   private MulticastManager(String var1, String var2, int var3, byte var4, long var5) throws IOException, UnknownHostException {
      ClusterMBean var7 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      this.isUnicast = "unicast".equals(var7.getClusterMessagingMode());
      int var8 = var7.getMulticastBufferSize();
      if (var8 > 0) {
         var8 *= 1024;
      } else {
         var8 = 32768;
      }

      this.sock = FragmentSocketWrapper.getInstance(var1, var2, var3, var4, var5, var8);
      this.fragmentBuffer = new byte['耀'];
      this.shutdown = false;
      this.senders = new ArrayList();
      this.currentHeartbeatItems = new ArrayList();
      this.suspendedSendersLastSeqHBI = new ArrayList();

      for(int var9 = 0; var9 <= 3; ++var9) {
         this.senders.add((Object)null);
      }

      this.heartbeatSender = this.createSender(0, (RecoverListener)null, -1, false);
      this.messagesLostCount = 0L;
      this.resendRequestsCount = 0L;
      this.localDomainName = ManagementService.getRuntimeAccess(kernelId).getDomain().getName();
      this.localDomainNameHash = this.hashCode(this.localDomainName);
      this.localClusterName = var7.getName();
      this.localClusterNameHash = this.hashCode(this.localClusterName);
      this.numForeignFragementsDropped = 0L;
      this.multicastAddress = var1;
      this.port = var3;
      this.clusterName = var7.getName();
      this.timerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("Heartbeat", WorkManagerFactory.getInstance().getSystem());
   }

   void startListening() throws IOException {
      PrivilegedAction var1 = new PrivilegedAction() {
         public Object run() {
            try {
               MulticastManager.this.sock.start();
               return null;
            } catch (IOException var2) {
               return var2;
            }
         }
      };
      IOException var2 = null;
      if (this.port > 1024) {
         var2 = (IOException)var1.run();
      } else {
         var2 = (IOException)SetUIDRendezvous.doPrivileged(var1);
      }

      if (var2 != null) {
         throw var2;
      } else {
         WorkManagerImpl.executeDaemonTask("weblogic.cluster.MessageReceiver", 5, this);
      }
   }

   void startHeartbeat() {
      if (this.timerManager.isSuspended()) {
         this.timerManager.resume();
      } else {
         this.timerManager.scheduleAtFixedRate(this, 0L, 10000L);
      }

   }

   synchronized void suspendNonAdminMulticastSessions() {
      Iterator var1 = this.senders.iterator();

      while(var1.hasNext()) {
         MulticastSender var2 = (MulticastSender)var1.next();
         if (!var2.isAdminSender() && var2.isRetryEnabled()) {
            var2.suspend();
            LastSeqNumHBI var3 = new LastSeqNumHBI(var2.getSessionID(), 0L, false);
            int var4 = this.currentHeartbeatItems.indexOf(var3);
            if (var4 >= 0) {
               Serializable var5 = (Serializable)this.currentHeartbeatItems.get(var4);
               if (var5 != null) {
                  this.removeItem(var5);
                  this.suspendedSendersLastSeqHBI.add(var5);
               }
            }
         }
      }

   }

   synchronized void resumeNonAdminMulticastSessions() {
      Iterator var1 = this.suspendedSendersLastSeqHBI.iterator();

      while(var1.hasNext()) {
         LastSeqNumHBI var2 = (LastSeqNumHBI)var1.next();
         this.addItem(var2);
      }

      this.suspendedSendersLastSeqHBI.clear();
      Iterator var4 = this.senders.iterator();

      while(var4.hasNext()) {
         MulticastSender var3 = (MulticastSender)var4.next();
         if (!var3.isAdminSender()) {
            var3.resume();
         }
      }

   }

   void stopHeartbeat() {
      if (!this.timerManager.isSuspended()) {
         try {
            this.heartbeatSender.send(new ShutdownMessage());
         } catch (IOException var2) {
         }

         this.timerManager.suspend();
      }

   }

   void handleMissedOwnMessages() {
      long var1 = System.currentTimeMillis() - this.sendStartTimestamp;
      ClusterMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      long var4 = (long)(10000 * (var3.getIdlePeriodsUntilTimeout() + 1));
      if (var1 > var4) {
         ClusterLogger.logMessageCannotReceiveOwnMessages(ManagementService.getRuntimeAccess(kernelId).getServer().getName());
         HealthMonitorService.subsystemFailed("Cluster", "Unable to receive self generated multicast messages");
      }

   }

   void stopListening() {
      this.shutdown = true;
      this.sock.shutdown();
   }

   MulticastSender createSender(int var1, RecoverListener var2, int var3, boolean var4) {
      return this.createSender(var1, var2, var3, var4, false);
   }

   MulticastSender createSender(int var1, RecoverListener var2, int var3, boolean var4, boolean var5) {
      synchronized(this.senders) {
         MulticastSender var7 = null;
         if (var1 == -1) {
            var7 = new MulticastSender(this.senders.size(), this.sock, var2, var3, var4, var5);
            this.senders.add(var7);
         } else {
            var7 = (MulticastSender)this.senders.get(var1);
            if (var7 == null) {
               var7 = new MulticastSender(var1, this.sock, var2, var3, var4, var5);
               this.senders.set(var7.getSessionID(), var7);
            }
         }

         return var7;
      }
   }

   MulticastSender findSender(int var1) {
      synchronized(this.senders) {
         return (MulticastSender)this.senders.get(var1);
      }
   }

   public synchronized void addItem(Serializable var1) {
      this.currentHeartbeatItems.add(var1);
   }

   synchronized void removeItem(Serializable var1) {
      this.currentHeartbeatItems.remove(var1);
   }

   synchronized void replaceItem(Serializable var1) {
      this.removeItem(var1);
      this.addItem(var1);
   }

   void flagStartedSending() {
      this.sendStartTimestamp = System.currentTimeMillis();
   }

   long getSendStartTimestamp() {
      return this.sendStartTimestamp;
   }

   boolean getCanReceiveOwnMessages() {
      return this.canReceiveOwnMessages;
   }

   void receiveHeartbeat(final HostID var1, final HeartbeatMessage var2) {
      SecurityServiceManager.runAs(kernelId, kernelId, new PrivilegedAction() {
         public Object run() {
            Iterator var1x;
            if (ClusterHeartbeatsDebugLogger.isDebugEnabled()) {
               ClusterHeartbeatsDebugLogger.debug("Received " + var2 + " from " + var1);
               var1x = var2.items.iterator();

               while(var1x.hasNext()) {
                  ClusterHeartbeatsDebugLogger.debug("  " + var1x.next());
               }
            }

            var1x = var2.items.iterator();

            while(true) {
               while(var1x.hasNext()) {
                  Object var2x = var1x.next();
                  if (var2x instanceof GroupMessage) {
                     ((GroupMessage)var2x).execute(var1);
                  } else if (var2x instanceof LastSeqNumHBI) {
                     LastSeqNumHBI var10 = (LastSeqNumHBI)var2x;
                     if (MulticastManager.this.serverResumed || var10.senderNum < 2 || var10.senderNum == 3) {
                        RemoteMemberInfo var11 = MemberManager.theOne().findOrCreate(var1);

                        try {
                           MulticastReceiver var5 = var11.findOrCreateReceiver(var10.senderNum, var10.useHTTPForSD);
                           var5.processLastSeqNum(var10.lastSeqNum);
                        } finally {
                           MemberManager.theOne().done(var11);
                        }
                     }
                  } else if (var2x instanceof NAKHBI) {
                     NAKHBI var3 = (NAKHBI)var2x;
                     if (var3.memID.isLocal()) {
                        MulticastSender var4 = MulticastManager.this.findSender(var3.senderNum);
                        var4.processNAK(var3.seqNum, var3.fragNum, var3.serverVersion);
                     }
                  }
               }

               return null;
            }
         }
      });
   }

   public void run() {
      while(!this.shutdown) {
         HostID var1 = null;

         try {
            this.sock.receive(this.fragmentBuffer);
            WLObjectInputStream var2 = getInputStream(this.fragmentBuffer);
            int var3 = var2.readInt();
            int var4 = var2.readInt();
            var1 = (HostID)var2.readObjectWL();
            if (this.isFragmentFromForeignCluster(var3, var4)) {
               ++this.numForeignFragementsDropped;
               if (!this.isUnicast) {
                  if (var3 != this.localDomainNameHash) {
                     if (!isLinux) {
                        ClusterLogger.logMultipleDomainsCannotUseSameMulticastAddress2(this.localDomainName);
                     }
                  } else {
                     ClusterLogger.logMultipleClustersCannotUseSameMulticastAddress2(this.localClusterName);
                  }
               }

               if (ClusterDebugLogger.isDebugEnabled()) {
                  ClusterDebugLogger.debug("dropped fragment from foreign domain/cluster domainhash=" + var3 + " clusterhash=" + var4 + " id=" + var1);
               }
            } else if (var1.isLocal()) {
               this.canReceiveOwnMessages = true;
            } else if (acceptMessageVersion(var2)) {
               MemberManager.theOne().resetTimeout(var1);
               byte[] var5 = null;
               if (ClusterService.getClusterService().multicastDataEncryptionEnabled()) {
                  var5 = (byte[])((byte[])var2.readObject());
               }

               byte[] var6 = (byte[])((byte[])var2.readObject());
               byte[] var7 = var6;
               if (ClusterService.getClusterService().multicastDataEncryptionEnabled()) {
                  if (!EncryptionHelper.verify(var6, var5)) {
                     ClusterLogger.logMessageDigestInvalid(var1.objectToString());
                     return;
                  }

                  var7 = EncryptionHelper.decrypt(var6, kernelId);
               }

               var2 = getInputStream(var7);
               int var8 = var2.readInt();
               long var9 = var2.readLong();
               int var11 = var2.readInt();
               int var12 = var2.readInt();
               int var13 = var2.readInt();
               boolean var14 = var2.readBoolean();
               boolean var15 = var2.readBoolean();
               boolean var16 = var2.readBoolean();
               byte[] var17 = var2.readBytes();
               if (this.serverResumed || var8 < 2 || var8 == 3) {
                  RemoteMemberInfo var18 = MemberManager.theOne().findOrCreate(var1);

                  try {
                     MulticastReceiver var19 = var18.findOrCreateReceiver(var8, var16);
                     var19.dispatch(var9, var11, var12, var13, var14, var15, var17);
                  } finally {
                     MemberManager.theOne().done(var18);
                  }
               }

               this.currentDelay = 0;
            }
         } catch (InterruptedIOException var30) {
         } catch (EOFException var31) {
            ClusterLogger.logMulticastAddressCollision(this.clusterName, this.multicastAddress, this.port + "");
         } catch (OptionalDataException var32) {
            ClusterLogger.logMulticastAddressCollision(this.clusterName, this.multicastAddress, this.port + "");
         } catch (StreamCorruptedException var33) {
            if (var1 != null && ClusterService.getClusterService().multicastDataEncryptionEnabled()) {
               ClusterLogger.logMessageDigestInvalid(var1.objectToString());
            } else if (this.isUnicast) {
               ClusterExtensionLogger.logUnicastReceiveError(var33);
            } else {
               ClusterLogger.logMulticastReceiveError(var33);
            }
         } catch (IOException var34) {
            if (!this.shutdown) {
               if (this.isUnicast) {
                  ClusterExtensionLogger.logUnicastReceiveError(var34);
               } else {
                  ClusterLogger.logMulticastReceiveError(var34);
               }
            }

            this.delay();
         } catch (Throwable var35) {
            if (!this.shutdown) {
               if (this.isUnicast) {
                  ClusterExtensionLogger.logUnicastReceiveError(var35);
               } else {
                  ClusterLogger.logMulticastReceiveError(var35);
               }
            }
         }
      }

   }

   private static boolean acceptMessageVersion(WLObjectInputStream var0) {
      try {
         String var1 = var0.readString();
         if (UpgradeUtils.getInstance().acceptVersion(var1)) {
            if (ClusterDebugLogger.isDebugEnabled()) {
               ClusterDebugLogger.debug("[UPGRADE] accepting a group message as the remote version is compatible. message version is " + var1);
            }

            return true;
         } else {
            if (ClusterDebugLogger.isDebugEnabled()) {
               ClusterDebugLogger.debug("[UPGRADE] DROPPING a group message as the remote version is NOT compatible. message version is " + var1);
            }

            return false;
         }
      } catch (IOException var2) {
         if (ClusterDebugLogger.isDebugEnabled()) {
            ClusterDebugLogger.debug("[UPGRADE] remote version is not available in group message!" + StackTraceUtils.throwable2StackTrace(var2));
         }

         return true;
      }
   }

   private void delay() {
      this.currentDelay = Math.min(this.currentDelay + 1000, 10000);

      try {
         Thread.sleep((long)this.currentDelay);
      } catch (InterruptedException var2) {
      }

   }

   MulticastSession getSender(int var1) {
      return (MulticastSession)this.senders.get(var1);
   }

   void forceSuspend() {
      this.serverResumed = false;
   }

   void resume() {
      this.serverResumed = true;
   }

   private boolean isFragmentFromForeignCluster(int var1, int var2) {
      return this.localDomainNameHash != var1 || this.localClusterNameHash != var2;
   }

   public int hashCode(String var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         var2 = 31 * var2 + var1.charAt(var3);
      }

      return var2;
   }

   static WLObjectInputStream getInputStream(byte[] var0) throws IOException {
      UnsyncByteArrayInputStream var1 = new UnsyncByteArrayInputStream(var0);
      WLObjectInputStream var2 = new WLObjectInputStream(var1);
      var2.setReplacer(new MulticastReplacer(LocalServerIdentity.getIdentity()));
      return var2;
   }

   static WLObjectOutputStream getOutputStream(UnsyncByteArrayOutputStream var0) throws IOException {
      return UpgradeUtils.getInstance().getOutputStream(var0, ServerChannelManager.findDefaultLocalServerChannel());
   }

   static WLObjectOutputStream getOutputStream(UnsyncByteArrayOutputStream var0, PeerInfo var1) throws IOException {
      return UpgradeUtils.getInstance().getOutputStream(var0, ServerChannelManager.findDefaultLocalServerChannel(), var1);
   }

   boolean isUnicastMessagingMode() {
      return this.isUnicast;
   }

   long getFragmentsSentCount() {
      return this.sock.getFragmentsSentCount();
   }

   long getFragmentsReceivedCount() {
      return this.sock.getFragmentsReceivedCount();
   }

   long getMulticastMessagesLostCount() {
      return this.messagesLostCount;
   }

   long getResendRequestsCount() {
      return this.resendRequestsCount;
   }

   void incrementMulticastMessagesLostCount(long var1) {
      this.messagesLostCount += var1;
   }

   void incrementResendRequestsCount() {
      ++this.resendRequestsCount;
   }

   long getForeignFragmentsDroppedCount() {
      return this.numForeignFragementsDropped;
   }

   public String toString() {
      return "Read Multicast Msg Fragment";
   }

   public void setPacketDelay(long var1) {
      this.sock.setPacketDelay(var1);
   }

   public void timerExpired(Timer var1) {
      try {
         HeartbeatMessage var2;
         synchronized(this) {
            var2 = new HeartbeatMessage((ArrayList)this.currentHeartbeatItems.clone());
         }

         this.heartbeatSender.send(var2);
         if (ClusterHeartbeatsDebugLogger.isDebugEnabled()) {
            ClusterHeartbeatsDebugLogger.debug("Sent " + var2);
            Iterator var3 = var2.items.iterator();

            while(var3.hasNext()) {
               ClusterHeartbeatsDebugLogger.debug("  " + var3.next());
            }
         }
      } catch (IOException var6) {
         ClusterLogger.logMulticastSendError(var6);
      }

      MemberManager.theOne().checkTimeouts();
   }

   private static String initOSNameProp() {
      String var0 = "UNKNOWN";

      try {
         var0 = System.getProperty("os.name", "UNKNOWN").toLowerCase(Locale.ENGLISH);
      } catch (SecurityException var2) {
      }

      return var0;
   }

   void dumpDiagnosticImageData(XMLStreamWriter var1) throws XMLStreamException, IOException {
      var1.writeStartElement("MulticastManager");
      MemberManager var2 = MemberManager.theOne();
      if (var2 != null) {
         var2.dumpDiagnosticImageData(var1);
      }

      var1.writeEndElement();
   }

   static {
      isLinux = "linux".equals(osName);
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      theMulticastManager = null;
   }
}
