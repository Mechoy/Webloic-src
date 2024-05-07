package weblogic.servlet.cluster;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Set;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.management.configuration.ClusterMBean;
import weblogic.rmi.extensions.server.RemoteDomainSecurityHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.servlet.cluster.wan.BatchedSessionState;
import weblogic.servlet.cluster.wan.PersistenceServiceInternal;
import weblogic.servlet.cluster.wan.ServiceUnavailableException;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;

public class LinkLivenessChecker implements TimerListener {
   private static final long SUSPEND_TIMEOUT = 5000L;
   private final String remoteClusterURL;
   private PersistenceServiceInternal service;
   private boolean timerStopped;
   private final TimerManager timerManager;
   private final ClusterMBean clusterMBean;
   private boolean requiresValidSubject;

   LinkLivenessChecker(String var1, ClusterMBean var2) {
      this(var1, var2, false);
   }

   LinkLivenessChecker(String var1, ClusterMBean var2, boolean var3) {
      this.timerStopped = true;
      this.remoteClusterURL = var1;
      this.clusterMBean = var2;
      this.requiresValidSubject = var3;
      this.timerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("commLinkManager");
   }

   PersistenceServiceInternal getRemotePersistenceService() {
      return this.service;
   }

   void stop() {
      synchronized(this.timerManager) {
         if (!this.timerStopped) {
            try {
               this.timerManager.waitForSuspend(5000L);
            } catch (InterruptedException var4) {
            }

            this.timerStopped = true;
         }
      }
   }

   void resume() {
      synchronized(this.timerManager) {
         if (this.timerStopped) {
            this.timerManager.resume();
            this.timerManager.schedule(this, (long)this.clusterMBean.getInterClusterCommLinkHealthCheckInterval());
            this.timerStopped = false;
         }

      }
   }

   private void lookupRemotePersistenceService() {
      int var2;
      try {
         this.service = LinkLivenessChecker.SecurePersistenceServiceImpl.getInstance(this.remoteClusterURL, this.requiresValidSubject);
         if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
            String var1 = this.service != null ? "Successfully looked up" : "Failed to look up";
            WANReplicationDetailsDebugLogger.debug(var1 + " Persistence Service from the remote cluster " + this.service);
         }

         if (this.service == null) {
            int var5 = this.clusterMBean.getInterClusterCommLinkHealthCheckInterval();
            this.timerManager.schedule(this, (long)var5);
         }
      } catch (NamingException var3) {
         if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
            WANReplicationDetailsDebugLogger.debug("Failed to get initial context " + this.remoteClusterURL);
         }

         var2 = this.clusterMBean.getInterClusterCommLinkHealthCheckInterval();
         this.timerManager.schedule(this, (long)var2);
      } catch (IOException var4) {
         if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
            WANReplicationDetailsDebugLogger.debug("Failed to get initial context " + this.remoteClusterURL);
         }

         var2 = this.clusterMBean.getInterClusterCommLinkHealthCheckInterval();
         this.timerManager.schedule(this, (long)var2);
      }

   }

   public void timerExpired(Timer var1) {
      this.lookupRemotePersistenceService();
   }

   private static class SecurePersistenceServiceImpl implements PersistenceServiceInternal {
      static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      private final PersistenceServiceInternal delegate;
      private final String remoteURL;
      private final AuthenticatedSubject subject;

      static PersistenceServiceInternal getInstance(final String var0, boolean var1) throws NamingException, IOException {
         AuthenticatedSubject var2 = RemoteDomainSecurityHelper.getSubject(var0);
         if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
            WANReplicationDetailsDebugLogger.debug("SecurePersistenceServiceImpl.getInstance  subject for " + var0 + " is " + var2);
         }

         if (var2 == null) {
            return var1 ? null : lookup(var0);
         } else {
            PrivilegedExceptionAction var3 = new PrivilegedExceptionAction() {
               public Object run() {
                  try {
                     return LinkLivenessChecker.SecurePersistenceServiceImpl.lookup(var0);
                  } catch (NamingException var2) {
                     return var2;
                  }
               }
            };

            try {
               Object var4 = SecurityManager.runAs(KERNEL_ID, var2, var3);
               if (var4 instanceof NamingException) {
                  throw (NamingException)var4;
               } else {
                  return new SecurePersistenceServiceImpl((PersistenceServiceInternal)var4, var0, var2);
               }
            } catch (PrivilegedActionException var5) {
               throw new AssertionError(var5);
            }
         }
      }

      private static PersistenceServiceInternal lookup(String var0) throws NamingException {
         Environment var1 = new Environment();
         var1.setProviderUrl(var0);
         Context var2 = var1.getInitialContext();
         return (PersistenceServiceInternal)var2.lookup("weblogic/servlet/wan/persistenceservice");
      }

      private SecurePersistenceServiceImpl(PersistenceServiceInternal var1, String var2, AuthenticatedSubject var3) {
         this.delegate = var1;
         this.remoteURL = var2;
         this.subject = var3;
      }

      public void persistState(final BatchedSessionState var1) throws ServiceUnavailableException, RemoteException {
         if (this.subject == null) {
            this.delegate.persistState(var1);
         } else {
            execute(new PrivilegedExceptionAction() {
               public Object run() {
                  try {
                     SecurePersistenceServiceImpl.this.delegate.persistState(var1);
                     return null;
                  } catch (RemoteException var2) {
                     return var2;
                  }
               }
            }, this.subject);
         }

      }

      public void invalidateSessions(final Set var1) throws RemoteException {
         if (this.subject == null) {
            this.delegate.invalidateSessions(var1);
         } else {
            execute(new PrivilegedExceptionAction() {
               public Object run() {
                  try {
                     SecurePersistenceServiceImpl.this.delegate.invalidateSessions(var1);
                     return null;
                  } catch (RemoteException var2) {
                     return var2;
                  }
               }
            }, this.subject);
         }

      }

      private static void execute(PrivilegedExceptionAction var0, AuthenticatedSubject var1) throws RemoteException {
         try {
            RemoteException var2 = (RemoteException)SecurityManager.runAs(KERNEL_ID, var1, var0);
            if (var2 != null) {
               throw var2;
            }
         } catch (PrivilegedActionException var3) {
            throw new AssertionError(var3);
         }
      }

      private static AuthenticatedSubject getSubject(String var0) {
         try {
            return RemoteDomainSecurityHelper.getSubject(var0);
         } catch (IOException var2) {
            return null;
         }
      }
   }
}
