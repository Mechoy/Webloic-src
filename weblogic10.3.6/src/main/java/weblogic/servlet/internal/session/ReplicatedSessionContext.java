package weblogic.servlet.internal.session;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import weblogic.cluster.replication.NotFoundException;
import weblogic.cluster.replication.ROID;
import weblogic.cluster.replication.ReplicationManager;
import weblogic.cluster.replication.ReplicationServices;
import weblogic.logging.Loggable;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.URLManager;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.internal.HttpServer;
import weblogic.servlet.internal.MembershipControllerImpl;
import weblogic.servlet.internal.NakedTimerListenerBase;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.timers.Timer;
import weblogic.utils.Debug;

public class ReplicatedSessionContext extends SessionContext {
   private static final boolean DEBUG = false;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final ReplicationServices repserv = ReplicationManager.services();
   private final HttpServer httpServer = this.getServletContext().getServer();
   private LATUpdateTrigger latTrigger;

   public ReplicatedSessionContext(WebAppServletContext var1, SessionConfigManager var2) {
      super(var1, var2);
   }

   public void startTimers() {
      super.startTimers();
      this.latTrigger = new LATUpdateTrigger();
   }

   public String getPersistentStoreType() {
      return "replicated";
   }

   ROID getSecondarySession(String var1) {
      return this.httpServer.getReplicator().getSecondary(var1);
   }

   void removeSecondarySession(String var1) {
      this.httpServer.getReplicator().removeSecondary(var1, this.getServletContext().getContextPath());
   }

   public void sync(HttpSession var1) {
      ReplicatedSessionData var2 = (ReplicatedSessionData)var1;
      synchronized(var2) {
         var2.decrementActiveRequestCount();
         if (DEBUG_SESSIONS.isDebugEnabled()) {
            DEBUG_SESSIONS.debug("synchronized on " + var2.getROID() + " and session is inUse: " + var2.sessionInUse() + " and active request count is: " + var2.getActiveRequestCount());
         }

         if (var2.sessionInUse()) {
            return;
         }

         if (DEBUG_SESSIONS.isDebugEnabled()) {
            DEBUG_SESSIONS.debug("Going ahead with replication for " + var2.getROID());
         }

         if (var2.isValid()) {
            var2.syncSession();
         }
      }

      if (var2.getActiveRequestCount() < 0) {
         throw new AssertionError("Reference Count value set below 0, value is" + var2.getActiveRequestCount());
      }
   }

   public ROID getROID(String var1) {
      ROID var2 = (ROID)this.getOpenSession(var1);
      if (var2 == null) {
         var2 = this.getSecondarySession(var1);
      }

      return var2;
   }

   public void updateSecondaryLAT(ROID var1, long var2) {
      ReplicatedSessionData var4 = this.getSecondarySession(var1);
      if (var4 != null && var4.getLAT() < var2) {
         if (DEBUG_SESSIONS.isDebugEnabled()) {
            DEBUG_SESSIONS.debug("updating LAT for " + var1 + " lat=" + var2 + " prev=" + var4.getLAT());
         }

         var4.setLastAccessedTime(var2);
      }

   }

   void killSecondary(ReplicatedSessionData var1) {
      var1.unregisterSecondary();
      this.removeSecondarySession(var1.id);
   }

   boolean hasSecondarySessionExpired(ReplicatedSessionData var1, long var2) {
      long var4 = Long.MAX_VALUE;
      int var6 = var1.getMaxInactiveInterval() * 2;
      var4 = var2 - (long)var6 * 1000L;
      return var6 >= 0 && var1.getLAT() < var4;
   }

   public void destroy(boolean var1) {
      if (this.latTrigger != null) {
         this.latTrigger.stop();
      }

      super.destroy(var1);
      if (!var1) {
         if (!this.configMgr.isSaveSessionsOnRedeployEnabled()) {
            String[] var2 = this.getIdsInternal();
            if (var2 != null && var2.length > 0) {
               SessionCleanupAction var3 = new SessionCleanupAction(var2);
               Throwable var4 = (Throwable)SecurityServiceManager.runAs(kernelId, SubjectUtils.getAnonymousSubject(), var3);
               if (var4 != null) {
                  HTTPSessionLogger.logUnexpectedErrorCleaningUpSessions(this.getServletContext().getLogContext(), var4);
               }
            }

         }
      }
   }

   void unregisterExpiredSessions(ArrayList var1) {
      if (!var1.isEmpty()) {
         ROID[] var2 = new ROID[var1.size()];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = ((ReplicatedSessionData)var1.get(var3)).getROID();
         }

         this.getReplicationServices().unregister((ROID[])var2, this.getServletContext().getContextPath());
      }
   }

   public HttpSession getNewSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      this.checkSessionCount();
      ReplicatedSessionData var4 = new ReplicatedSessionData(var1, this);
      var4.setMonitoringId();
      return var4;
   }

   private ReplicatedSessionData lookupRO(String var1, String var2, ServerIdentity var3, ROID var4) {
      ROID var5 = null;

      try {
         if (var3 == null) {
            return null;
         } else {
            ReplicatedSessionData var6 = (ReplicatedSessionData)this.getReplicationServices().registerLocally(var3, var4, this.getServletContext().getContextPath());
            var5 = (ROID)this.getOpenSession(var2);
            if (var6 != null) {
               if (this.isDebugEnabled()) {
                  Loggable var7 = HTTPSessionLogger.logRetrievedROIDFromSecondaryLoggable(var5.toString(), var3.toString(), "", var2);
                  DEBUG_SESSIONS.debug(var7.getMessage());
               }

               if (DEBUG_SESSIONS.isDebugEnabled()) {
                  DEBUG_SESSIONS.debug("looked up the roid = " + var5 + " from host " + var3.toString());
               }
            }

            return var6;
         }
      } catch (RemoteException var8) {
         HTTPSessionLogger.logErrorGettingSession(var1, var8);
         return null;
      }
   }

   private SessionData getPrimarySessionForTrigger(String var1) throws NotFoundException {
      if (var1 == null) {
         return null;
      } else {
         ROID var2 = (ROID)this.getOpenSession(var1);
         return var2 == null ? null : (ReplicatedSessionData)this.getReplicationServices().invalidationLookup(var2, this.getServletContext().getContextPath());
      }
   }

   public boolean isPrimaryOrSecondary(String var1) {
      RSID var2 = new RSID(var1, MembershipControllerImpl.getInstance().getClusterMembers());
      if (var2.id == null) {
         return true;
      } else {
         ServerIdentity var3 = this.getCurrentClusterMember();
         if (var2.getPrimary() == null && var2.getSecondary() == null) {
            return true;
         } else {
            return var3.equals(var2.getPrimary()) || var3.equals(var2.getSecondary());
         }
      }
   }

   public SessionData getSessionInternalForAuthentication(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      return this.getSessionInternal(var1, var2, var3, true);
   }

   public SessionData getSessionInternal(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      return this.getSessionInternal(var1, var2, var3, false);
   }

   private SessionData getSessionInternal(String var1, ServletRequestImpl var2, ServletResponseImpl var3, boolean var4) {
      try {
         if (var2 == null) {
            return this.getPrimarySessionForTrigger(RSID.getID(var1));
         }

         return this.lookupSession(var1, var2, var3, var4);
      } catch (NotFoundException var6) {
      } catch (RuntimeException var7) {
         HTTPSessionLogger.logErrorGettingSession(var1, var7);
      }

      return null;
   }

   public boolean hasSession(String var1) {
      if (this.getSessionInternal(var1, (ServletRequestImpl)null, (ServletResponseImpl)null) != null) {
         return true;
      } else {
         return this.lookupSession(var1, (ServletRequestImpl)null, (ServletResponseImpl)null, false) != null;
      }
   }

   private SessionData lookupSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3, boolean var4) {
      try {
         RSID var5 = new RSID(var1, MembershipControllerImpl.getInstance().getClusterMembers());
         if (var5.id == null) {
            return null;
         }

         ServerIdentity var6 = this.getCurrentClusterMember();
         String var7 = null;
         String var8 = null;
         if (var5.getPrimary() != null && !var6.equals(var5.getPrimary())) {
            var7 = URLManager.findURL(var5.getPrimary(), this.httpServer.getReplicationChannel());
         }

         if (var5.getSecondary() != null && !var6.equals(var5.getSecondary())) {
            var8 = URLManager.findURL(var5.getSecondary(), this.httpServer.getReplicationChannel());
         }

         boolean var9 = false;
         String var10 = null;
         ROID var11 = (ROID)this.getOpenSession(var5.id);
         if (var11 == null && var6.equals(var5.getSecondary())) {
            var11 = this.getSecondarySession(var5.id);
         }

         boolean var12 = !var6.equals(var5.getSecondary()) && !var6.equals(var5.getPrimary());
         if (var7 != null && this.getPersistentStoreType() != "async-replication-across-cluster" && !var4) {
            HTTPSessionLogger.logSessionAccessFromNonPrimary(var5.id, var6.toString(), var5.getPrimary() != null ? var5.getPrimary().toString() : "NONE", var2 != null ? var2.getRequestURL().toString() : "NONE");
         }

         if (var12) {
            ROID var13 = null;
            ReplicatedSessionData var14 = null;
            RemoteException var15 = null;
            if (var7 != null) {
               try {
                  var13 = this.httpServer.getReplicator().lookupROID(var5.id, var7, this.getConfigMgr().getCookieName(), this.getConfigMgr().getCookiePath(), var12);
               } catch (RemoteException var19) {
                  var15 = var19;
               }

               if (var13 != null) {
                  var11 = var13;
                  var10 = "primary-server: " + var7;
                  var14 = this.lookupRO(var1, var5.id, var5.getPrimary(), var13);
                  if (var14 != null) {
                     var9 = true;
                  }
               }
            }

            if (var13 == null && var8 != null) {
               try {
                  var13 = this.httpServer.getReplicator().lookupROID(var5.id, var8, this.getConfigMgr().getCookieName(), this.getConfigMgr().getCookiePath(), var12);
               } catch (RemoteException var18) {
                  var15 = var18;
               }

               if (var13 != null) {
                  var11 = var13;
                  var10 = "secondary-server: " + var8;
                  var14 = this.lookupRO(var1, var5.id, var5.getSecondary(), var13);
                  if (var14 != null) {
                     var9 = true;
                  }
               }
            }

            if (var11 == null && var15 != null) {
               HTTPSessionLogger.logErrorGettingSession(var1, var15);
            }
         }

         if (var11 == null) {
            return null;
         }

         if (var10 == null) {
            var10 = "current-server";
         }

         ReplicatedSessionData var23 = (ReplicatedSessionData)this.getReplicationServices().lookup(var11, this.getServletContext().getContextPath());
         if (var23 == null) {
            return null;
         }

         if (!var23.id.equals(var5.id)) {
            Debug.say("Incoming sessionid: " + var1 + "\nrsid: " + var5 + "\nrsid.id: " + var5.id + "\nroid: " + var11 + "\nprimary: " + var5.getPrimary() + "\nsecondary: " + var5.getSecondary() + "\ncurrentServer: " + var6.hashCode() + "\ncurrent context path: " + this.getServletContext().getContextPath() + "\norigin: " + var10 + "\nsession.id: " + var23.id + "\nsession.ROID: " + var23.getROID() + "\nsession.ContextPath: " + var23.getContextPath());
            throw new AssertionError("Lookup returned replicated session object with a different sessionid");
         }

         synchronized(var23) {
            if (!var23.isValid()) {
               return null;
            }

            var23.setSessionContext(this);
            var23.updateVersionIfNeeded(this);
            if (var9) {
               var23.setLastAccessedTime(System.currentTimeMillis());
               var23.notifyActivated(new HttpSessionEvent(var23));
            }

            if (!var23.isValidForceCheck()) {
               return null;
            }

            if (var2 != null) {
               var23.incrementActiveRequestCount();
            }

            var23.reinitializeSecondary();
         }

         return var23;
      } catch (NotFoundException var21) {
      } catch (RuntimeException var22) {
         HTTPSessionLogger.logErrorGettingSession(var1, var22);
      }

      return null;
   }

   public Object getOpenSession(String var1) {
      return this.httpServer.getReplicator().getPrimary(var1);
   }

   public void removeSession(String var1, ROID var2) {
      super.removeSession(var1);
      this.httpServer.getReplicator().removePrimary(var1, this.getServletContext().getContextPath());
   }

   public int getCurrOpenSessionsCount() {
      return this.getOpenSessions().size();
   }

   boolean invalidateSession(SessionData var1, boolean var2, boolean var3) {
      String var4 = var1.id;
      ReplicatedSessionData var5 = (ReplicatedSessionData)var1;
      var5.remove(var2, var3);
      this.removeSession(var4, var5.getROID());
      this.decrementOpenSessionsCount();
      SessionData.invalidateProcessedSession(var1);
      return true;
   }

   private ReplicatedSessionData getSecondarySession(ROID var1) {
      try {
         return (ReplicatedSessionData)this.getReplicationServices().invalidationLookup(var1, this.getServletContext().getContextPath());
      } catch (NotFoundException var3) {
         return null;
      }
   }

   protected void invalidateOrphanedSessions() {
      long var1 = System.currentTimeMillis();
      ROID[] var3 = this.httpServer.getReplicator().getSecondaryIds();
      int var4 = var3.length;
      int var5 = 0;

      for(int var6 = 0; var6 < var4; ++var6) {
         ROID var7 = var3[var6];

         try {
            ReplicatedSessionData var8 = this.getSecondarySession(var7);
            if (var8 != null && !var8.sessionInUse() && this.hasSecondarySessionExpired(var8, var1)) {
               this.killSecondary(var8);
               if (this.isDebugEnabled()) {
                  Loggable var9 = HTTPSessionLogger.logTimerInvalidatedSessionLoggable("secondary:" + var7, this.getServletContext().getContextPath());
                  DEBUG_SESSIONS.debug(var9.getMessage());
               }

               ++var5;
            }
         } catch (ThreadDeath var10) {
            throw var10;
         } catch (Throwable var11) {
            HTTPSessionLogger.logUnexpectedTimeoutError(var11);
         }
      }

      if (DEBUG_SESSIONS.isDebugEnabled() && var4 > 0) {
         DEBUG_SESSIONS.debug("Total secondary sessions invalidated = " + var5 + " out of total secondary sessions = " + var4 + " in " + this.getServletContext().getContextPath());
      }

   }

   public int getNonPersistedSessionCount() {
      Hashtable var1 = this.getOpenSessions();
      if (var1.size() < 1) {
         return 0;
      } else {
         int var2 = 0;
         Enumeration var3 = var1.elements();

         while(var3.hasMoreElements()) {
            ReplicatedSessionData var4 = (ReplicatedSessionData)var3.nextElement();
            ROID var5 = var4.getROID();

            try {
               if (this.getReplicationServices().getSecondaryInfo(var5) == null) {
                  ++var2;
               }
            } catch (NotFoundException var7) {
            }
         }

         return var2;
      }
   }

   protected ReplicationServices getReplicationServices() {
      return repserv;
   }

   public String lookupAppVersionIdForSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      HttpSession var4 = var2.getSession(false);
      return var4 != null && var4 instanceof ReplicatedSessionData ? ((ReplicatedSessionData)var4).getVersionId() : null;
   }

   LATUpdateTrigger getTimer() {
      return this.latTrigger;
   }

   private class SessionCleanupAction implements PrivilegedAction {
      private final String[] ids;

      SessionCleanupAction(String[] var2) {
         this.ids = var2;
      }

      public Object run() {
         try {
            for(int var1 = 0; var1 < this.ids.length; ++var1) {
               SessionData var2 = null;
               if (this.ids[var1] == null) {
                  break;
               }

               try {
                  var2 = ReplicatedSessionContext.this.getPrimarySessionForTrigger(this.ids[var1]);
               } catch (NotFoundException var4) {
               }

               if (var2 != null) {
                  ReplicatedSessionContext.this.invalidateSession(var2, false);
               }
            }

            return null;
         } catch (Throwable var5) {
            return var5;
         }
      }
   }

   class LATUpdateTrigger extends NakedTimerListenerBase {
      private static final int DEFAULT_INTERVAL = 120;
      private static final int MAX_INTERVAL = 900;
      private static final int MIN_INTERVAL = 60;
      private final Hashtable allBatchJobs;
      private Timer timer;
      private boolean stopRequested;
      private int triggerInterval;
      private int latestBatchJobInterval;

      private LATUpdateTrigger() {
         super("LATUpdateTrigger", ReplicatedSessionContext.this.getServletContext());
         this.allBatchJobs = new Hashtable();
         this.stopRequested = false;
         this.triggerInterval = 120;
         this.latestBatchJobInterval = 120;
         this.latestBatchJobInterval = 120;
         this.computeBatchInterval(-1);
         this.start();
      }

      private void start() {
         this.stopRequested = false;
         this.triggerInterval = this.latestBatchJobInterval;
         this.timer = this.timerManager.schedule(this, 0L, (long)(this.triggerInterval * 1000));
      }

      private void stop() {
         this.stopRequested = true;
         this.timer.cancel();
         this.timerManager.stop();
      }

      private void bounce() {
         this.timer.cancel();
         this.start();
      }

      void registerLAT(ROID var1, long var2, int var4) {
         ServerIdentity var5 = this.getSecondary(var1);
         if (var5 != null) {
            Hashtable var6 = (Hashtable)this.allBatchJobs.get(var5);
            if (var6 == null) {
               synchronized(this.allBatchJobs) {
                  var6 = (Hashtable)this.allBatchJobs.get(var5);
                  if (var6 == null) {
                     var6 = new Hashtable();
                     this.allBatchJobs.put(var5, var6);
                  }
               }
            }

            var6.put(var1, new Long((long)var4));
            this.computeBatchInterval(var4);
         }
      }

      private ServerIdentity getSecondary(ROID var1) {
         try {
            return (ServerIdentity)ReplicatedSessionContext.this.getReplicationServices().getSecondaryInfo(var1);
         } catch (NotFoundException var3) {
            return null;
         }
      }

      void unregisterLAT(ROID var1) {
         ServerIdentity var2 = this.getSecondary(var1);
         if (var2 != null) {
            Hashtable var3 = (Hashtable)this.allBatchJobs.get(var2);
            if (var3 != null) {
               var3.remove(var1);
            }
         }
      }

      private void computeBatchInterval(int var1) {
         if (var1 > 0 && var1 / 3 < this.latestBatchJobInterval) {
            this.latestBatchJobInterval = var1 / 3;
         }

         int var2 = ReplicatedSessionContext.this.configMgr.getInvalidationIntervalSecs();
         if (var2 / 2 < this.latestBatchJobInterval) {
            this.latestBatchJobInterval = var2 / 2;
         }

         if (this.latestBatchJobInterval < 60) {
            this.latestBatchJobInterval = 60;
         } else if (this.latestBatchJobInterval > 900) {
            this.latestBatchJobInterval = 900;
         }

      }

      public void timerExpired(Timer var1) {
         if (ReplicatedSessionContext.this.getServletContext().isStarted()) {
            final int var2 = this.allBatchJobs.size();
            if (var2 < 1) {
               this.latestBatchJobInterval = 120;
            } else {
               SecurityServiceManager.runAs(ReplicatedSessionContext.kernelId, SubjectUtils.getAnonymousSubject(), new PrivilegedAction() {
                  public Object run() {
                     try {
                        LATUpdateTrigger.this.runTrigger(var2);
                     } catch (Throwable var2x) {
                        HTTPSessionLogger.logLATUpdateError(ReplicatedSessionContext.this.getServletContext().getContextPath(), var2x);
                     }

                     return null;
                  }
               });
            }
         }
      }

      private void runTrigger(int var1) {
         try {
            if (SessionContext.DEBUG_SESSIONS.isDebugEnabled()) {
               SessionContext.DEBUG_SESSIONS.debug("LATUpdateTrigger: total jobs: " + var1 + " current interval=" + this.latestBatchJobInterval);
            }

            Hashtable var2 = null;
            synchronized(this.allBatchJobs) {
               var2 = (Hashtable)this.allBatchJobs.clone();
               this.allBatchJobs.clear();
            }

            var1 = var2.size();
            if (var1 >= 1) {
               Iterator var3 = var2.values().iterator();

               while(var3.hasNext()) {
                  this.sendBatchJob((Hashtable)var3.next());
               }

               return;
            }
         } finally {
            if (!this.stopRequested && this.triggerInterval != this.latestBatchJobInterval) {
               this.bounce();
            }

         }

      }

      private void sendBatchJob(Hashtable var1) {
         int var2 = var1.size();
         if (var2 >= 1) {
            ROID[] var3 = new ROID[var2];
            long[] var4 = new long[var2];
            String var5 = null;
            Enumeration var6 = var1.keys();

            for(int var7 = 0; var6.hasMoreElements() && var7 < var2; ++var7) {
               ROID var8 = (ROID)var6.nextElement();
               if (var5 == null) {
                  try {
                     ServerIdentity var9 = (ServerIdentity)ReplicatedSessionContext.this.getReplicationServices().getSecondaryInfo(var8);
                     if (var9 != null) {
                        var5 = URLManager.findURL(var9, ReplicatedSessionContext.this.httpServer.getReplicationChannel());
                     }
                  } catch (NotFoundException var10) {
                  }
               }

               var3[var7] = var8;
               var4[var7] = (Long)var1.get(var8);
            }

            if (SessionContext.DEBUG_SESSIONS.isDebugEnabled()) {
               SessionContext.DEBUG_SESSIONS.debug("LATUpdateTrigger: sending a batch of size = " + var2 + " to secondary url : " + var5);
            }

            if (var5 != null) {
               ReplicatedSessionContext.this.httpServer.getReplicator().updateROIDLastAccessTimes(var5, var3, var4, ReplicatedSessionContext.this.getServletContext().getContextPath());
            }

            var1.clear();
         }
      }

      // $FF: synthetic method
      LATUpdateTrigger(Object var2) {
         this();
      }
   }
}
