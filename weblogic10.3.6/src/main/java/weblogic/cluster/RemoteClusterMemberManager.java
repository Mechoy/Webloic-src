package weblogic.cluster;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.extensions.server.RemoteDomainSecurityHelper;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.StopTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;

public final class RemoteClusterMemberManager implements NakedTimerListener, StopTimerListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final ClusterMBean cluster;
   private final Environment env;
   private final ArrayList listeners;
   private final int leaseRenewInterval;
   private long version;
   private boolean loggedMessageOnce;
   private RemoteClusterHealthChecker healthChecker;
   private final TimerManager timerFactory;
   private ArrayList currentCandidates;

   public static RemoteClusterMemberManager getInstance() {
      return RemoteClusterMemberManager.SingletonMaker.singleton;
   }

   private RemoteClusterMemberManager() {
      this.cluster = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      this.env = new Environment();
      this.listeners = new ArrayList();
      if (this.cluster != null) {
         this.leaseRenewInterval = this.cluster.getHealthCheckIntervalMillis();
         this.timerFactory = TimerManagerFactory.getTimerManagerFactory().getTimerManager("RemoteClusterMemberManager");
         this.timerFactory.schedule(this, (long)this.leaseRenewInterval, (long)this.leaseRenewInterval);
      } else {
         this.timerFactory = null;
         this.leaseRenewInterval = 0;
      }

   }

   public synchronized void addRemoteClusterMemberListener(RemoteClusterMembersChangeListener var1) {
      this.listeners.add(var1);
      if (this.currentCandidates != null) {
         ArrayList var2 = new ArrayList(1);
         var2.add(var1);
         this.fireMembershipChangeEvent(var2, this.currentCandidates);
      }

   }

   public void timerStopped(Timer var1) {
   }

   public void timerExpired(Timer var1) {
      ArrayList var2 = null;
      RemoteClusterHealthChecker var3 = this.getHealthChecker();
      ArrayList var4 = null;

      try {
         if (var3 == null) {
            return;
         }

         synchronized(this) {
            var4 = (ArrayList)this.listeners.clone();
         }

         var2 = var3.checkClusterMembership(this.version);
         if (var2 != null) {
            this.currentCandidates = var2;
            this.version = 0L;

            for(int var5 = 0; var5 < var2.size(); ++var5) {
               ClusterMemberInfo var6 = (ClusterMemberInfo)var2.get(var5);
               this.version += (long)var6.identity().hashCode();
            }

            this.fireMembershipChangeEvent(var4, var2);
         }
      } catch (RemoteException var8) {
         this.currentCandidates = null;
         this.healthChecker = null;
         this.version = 0L;
         this.fireMembershipChangeEvent(var4, new ArrayList());
      }

   }

   private void fireMembershipChangeEvent(ArrayList var1, ArrayList var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         RemoteClusterMembersChangeListener var4 = (RemoteClusterMembersChangeListener)var1.get(var3);
         var4.remoteClusterMembersChanged(var2);
      }

   }

   private RemoteClusterHealthChecker getHealthChecker() {
      if (this.healthChecker != null) {
         return this.healthChecker;
      } else {
         String var1 = this.cluster.getRemoteClusterAddress();
         if (var1 == null) {
            return null;
         } else if (!ServerHelper.isURLValid(var1)) {
            if (!this.loggedMessageOnce) {
               ClusterLogger.logIncorrectRemoteClusterAddress(var1);
               this.loggedMessageOnce = true;
            }

            return null;
         } else {
            try {
               AuthenticatedSubject var2 = RemoteDomainSecurityHelper.getSubject(var1);
               if (var2 == null) {
                  var2 = SubjectUtils.getAnonymousSubject();
                  this.healthChecker = this.lookup(var2, var1);
               } else {
                  this.healthChecker = this.lookup(var2, var1);
                  if (this.healthChecker != null) {
                     this.healthChecker = new SecureClusterHealthChecker(this.healthChecker, var2);
                  }
               }

               return this.healthChecker;
            } catch (IOException var3) {
               return null;
            }
         }
      }
   }

   private RemoteClusterHealthChecker lookup(AuthenticatedSubject var1, final String var2) throws RemoteException {
      try {
         Object var3 = SecurityManager.runAs(kernelId, var1, new PrivilegedExceptionAction() {
            public Object run() {
               Context var1 = null;

               Object var3;
               try {
                  RemoteClusterMemberManager.this.env.setProviderUrl(var2);
                  RemoteClusterMemberManager.this.env.setRequestTimeout((long)(RemoteClusterMemberManager.this.cluster.getHealthCheckIntervalMillis() * RemoteClusterMemberManager.this.cluster.getIdlePeriodsUntilTimeout()));
                  var1 = RemoteClusterMemberManager.this.env.getInitialContext();
                  RemoteClusterHealthChecker var2x = (RemoteClusterHealthChecker)var1.lookup("weblogic/cluster/RemoteClusterHealthChecker");
                  return var2x;
               } catch (NamingException var13) {
                  var3 = null;
               } finally {
                  try {
                     if (var1 != null) {
                        var1.close();
                     }
                  } catch (NamingException var12) {
                  }

               }

               return var3;
            }
         });
         if (var3 != null) {
            if (var3 instanceof RemoteException) {
               throw (RemoteException)var3;
            } else {
               return (RemoteClusterHealthChecker)var3;
            }
         } else {
            return null;
         }
      } catch (PrivilegedActionException var4) {
         throw new AssertionError(var4);
      }
   }

   // $FF: synthetic method
   RemoteClusterMemberManager(Object var1) {
      this();
   }

   private static class SecureClusterHealthChecker implements RemoteClusterHealthChecker {
      private final RemoteClusterHealthChecker delegate;
      private final AuthenticatedSubject subject;

      private SecureClusterHealthChecker(RemoteClusterHealthChecker var1, AuthenticatedSubject var2) {
         this.delegate = var1;
         this.subject = var2;
      }

      public ArrayList checkClusterMembership(final long var1) throws RemoteException {
         if (this.subject == null) {
            return this.delegate.checkClusterMembership(var1);
         } else {
            try {
               Object var3 = SecurityManager.runAs(RemoteClusterMemberManager.kernelId, this.subject, new PrivilegedExceptionAction() {
                  public Object run() {
                     try {
                        return SecureClusterHealthChecker.this.delegate.checkClusterMembership(var1);
                     } catch (RemoteException var2) {
                        return var2;
                     }
                  }
               });
               if (var3 instanceof RemoteException) {
                  throw (RemoteException)var3;
               } else {
                  return (ArrayList)var3;
               }
            } catch (PrivilegedActionException var4) {
               throw new AssertionError(var4);
            }
         }
      }

      // $FF: synthetic method
      SecureClusterHealthChecker(RemoteClusterHealthChecker var1, AuthenticatedSubject var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class SingletonMaker {
      private static final RemoteClusterMemberManager singleton = new RemoteClusterMemberManager();
   }
}
