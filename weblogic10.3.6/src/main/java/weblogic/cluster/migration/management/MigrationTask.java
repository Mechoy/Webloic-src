package weblogic.cluster.migration.management;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.cluster.migration.MigrationManager;
import weblogic.cluster.migration.RemoteMigratableServiceCoordinator;
import weblogic.cluster.migration.RemoteMigrationControl;
import weblogic.cluster.singleton.SingletonMonitorRemote;
import weblogic.jndi.Environment;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.management.runtime.MigrationException;
import weblogic.management.runtime.MigrationTaskRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.TaskRuntimeMBean;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public class MigrationTask extends RuntimeMBeanDelegate implements MigrationTaskRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final int SECONDS_TO_WAIT_FOR_MIGRATION = 30;
   private static int num = 0;
   private MigratableTargetMBean mt;
   private ServerMBean destination;
   private boolean jta;
   private int status = 0;
   private Exception ex;
   private long startTime;
   private long endTime;
   private StringBuffer log;
   private boolean isSourceServerDown;
   private boolean isDestinationServerDown;
   private String[] statusTextLUT = null;
   private boolean sysTask = false;
   private final MigratableServiceCoordinatorRuntime runtime;
   private ServerMBean currentServer;
   private final ManagementTextTextFormatter texter = ManagementTextTextFormatter.getInstance();
   private final String currentServerURL;
   private final String destinationServerURL;
   private final String currentServerName;
   private final String destinationServerName;

   public MigrationTask(MigratableTargetMBean var1, ServerMBean var2, boolean var3, boolean var4, boolean var5, MigratableServiceCoordinatorRuntime var6) throws ManagementException {
      super(ManagementTextTextFormatter.getInstance().getMigrationTaskTitle(var1 != null ? var1.getName() : "(null)", var2 != null ? var2.getName() : "(null)", var3 ? "JTA " : "") + "-" + num++, var6);
      this.mt = var1;
      this.destination = var2;
      this.jta = var3;
      this.isSourceServerDown = var4 || var3;
      this.isDestinationServerDown = var5;
      this.log = new StringBuffer(4096);
      this.runtime = var6;
      if (var3) {
         try {
            DomainMBean var7 = ManagementService.getRuntimeAccess(kernelId).getDomain();
            String var8 = URLManager.findAdministrationURL(var7.getAdminServerName());
            RemoteMigratableServiceCoordinator var9 = this.getServiceCoordinator(var8);
            String var10 = var9.getCurrentLocationOfJTA(var1.getName());
            this.currentServer = var7.lookupServer(var10);
            if (this.currentServer == null) {
               this.currentServer = var1.getUserPreferredServer();
            }

            Debug.assertion(this.currentServer != null);
            this.currentServerURL = this.getServerAdminURL(this.currentServer.getName());
            this.currentServerName = this.currentServer.getName();
         } catch (Exception var11) {
            throw new ManagementException(var11);
         }
      } else {
         if (this.isTargetAutoMigratable(var1)) {
            this.currentServer = var1.getHostingServer();
            if (this.currentServer == null) {
               this.isSourceServerDown = true;
            }
         } else {
            this.currentServer = var1.getUserPreferredServer();
            Debug.assertion(this.currentServer != null);
         }

         if (this.currentServer != null) {
            this.currentServerURL = this.getServerAdminURL(this.currentServer.getName());
            this.currentServerName = this.currentServer.getName();
         } else {
            this.currentServerURL = null;
            this.currentServerName = null;
         }

         if (this.currentServerURL == null) {
            this.isSourceServerDown = true;
         }
      }

      this.destinationServerURL = this.getServerAdminURL(var2.getName());
      this.destinationServerName = var2.getName();
      this.statusTextLUT = new String[]{ManagementTextTextFormatter.getInstance().getMigrationTaskStatusInProgress(), ManagementTextTextFormatter.getInstance().getMigrationTaskStatusDone(), ManagementTextTextFormatter.getInstance().getMigrationTaskStatusFailed(), ManagementTextTextFormatter.getInstance().getMigrationTaskStatusQIsTheSourceServerDown(), ManagementTextTextFormatter.getInstance().getMigrationTaskStatusQIsTheDestinationServerDown(), ManagementTextTextFormatter.getInstance().getMigrationTaskStatusCanceled()};
   }

   public boolean isSystemTask() {
      return this.sysTask;
   }

   private RemoteMigratableServiceCoordinator getServiceCoordinator(String var1) throws MigrationException {
      Context var2 = null;

      try {
         Environment var3 = new Environment();
         var3.setProviderUrl(var1);
         var2 = var3.getInitialContext();
         return (RemoteMigratableServiceCoordinator)var2.lookup("weblogic.cluster.migration.migratableServiceCoordinator");
      } catch (NamingException var4) {
         throw new MigrationException("Unexpected naming exception", var4);
      }
   }

   public String getDescription() {
      return ManagementTextTextFormatter.getInstance().getMigrationTaskTitle(this.mt != null ? this.mt.getName() : "(null)", this.destination != null ? this.destinationServerName : "(null)", this.jta ? "JTA " : "");
   }

   public String getStatus() {
      return this.statusTextLUT[this.status];
   }

   public int getStatusCode() {
      return this.status;
   }

   public synchronized boolean isRunning() {
      return this.status == 0;
   }

   public synchronized boolean isTerminal() {
      return this.status == 5 || this.status == 2 || this.status == 1;
   }

   public boolean isWaitingForUser() {
      return this.status == 3 || this.status == 4;
   }

   public long getBeginTime() {
      return this.startTime;
   }

   public long getEndTime() {
      return this.endTime;
   }

   public synchronized Exception getError() {
      return this.ex;
   }

   public ServerMBean getSourceServer() {
      return this.mt.getUserPreferredServer();
   }

   public ServerMBean getDestinationServer() {
      return this.destination;
   }

   public MigratableTargetMBean getMigratableTarget() {
      return this.mt;
   }

   public boolean isJTA() {
      return this.jta;
   }

   public TaskRuntimeMBean[] getSubTasks() {
      return null;
   }

   public TaskRuntimeMBean getParentTask() {
      return null;
   }

   public void setSystemTask(boolean var1) {
      this.sysTask = var1;
   }

   public void run() {
      WorkAdapter var1 = new WorkAdapter() {
         public void run() {
            MigrationTask.this.status = 0;
            MigrationTask.this.startTime = System.currentTimeMillis();
            SecurityServiceManager.runAs(MigrationTask.kernelId, MigrationTask.kernelId, new PrivilegedAction() {
               public Object run() {
                  boolean var23 = false;

                  label190: {
                     label191: {
                        try {
                           var23 = true;
                           synchronized(MigrationManager.singleton()) {
                              while(MigrationManager.singleton().isMigrating()) {
                                 MigrationManager.singleton().wait(1000L);
                              }

                              MigrationManager.singleton().setMigrating(true);
                              MigrationTask.this.migrate();
                           }

                           synchronized(MigrationTask.this) {
                              MigrationTask.this.status = 1;
                              var23 = false;
                              break label190;
                           }
                        } catch (RuntimeException var32) {
                           RuntimeException var35 = var32;
                           synchronized(MigrationTask.this) {
                              MigrationTask.this.ex = var35;
                              MigrationTask.this.status = 2;
                           }

                           MigrationTask.this.logLine(var32.toString());
                           MigrationTask.this.logLine(StackTraceUtils.throwable2StackTrace(var32));
                           var23 = false;
                           break label191;
                        } catch (Exception var33) {
                           Exception var1 = var33;
                           synchronized(MigrationTask.this) {
                              MigrationTask.this.ex = var1;
                              MigrationTask.this.status = 2;
                           }

                           MigrationTask.this.logLine(var33.toString());
                           MigrationTask.this.logLine(StackTraceUtils.throwable2StackTrace(var33));
                           var23 = false;
                        } finally {
                           if (var23) {
                              MigrationTask.this.endTime = System.currentTimeMillis();
                              synchronized(MigrationManager.singleton()) {
                                 MigrationManager.singleton().setMigrating(false);
                              }
                           }
                        }

                        MigrationTask.this.endTime = System.currentTimeMillis();
                        synchronized(MigrationManager.singleton()) {
                           MigrationManager.singleton().setMigrating(false);
                           return null;
                        }
                     }

                     MigrationTask.this.endTime = System.currentTimeMillis();
                     synchronized(MigrationManager.singleton()) {
                        MigrationManager.singleton().setMigrating(false);
                        return null;
                     }
                  }

                  MigrationTask.this.endTime = System.currentTimeMillis();
                  synchronized(MigrationManager.singleton()) {
                     MigrationManager.singleton().setMigrating(false);
                  }

                  return null;
               }
            });
         }

         public String toString() {
            return MigrationTask.this.getDescription();
         }
      };
      WorkManagerFactory.getInstance().getSystem().schedule(var1);
   }

   public synchronized void cancel() throws Exception {
      if (this.status != 3 && this.status != 4) {
         throw new Exception(ManagementTextTextFormatter.getInstance().getMigrationTaskCannotCancelHere());
      } else {
         this.status = 5;
         this.notifyAll();
      }
   }

   public void printLog(PrintWriter var1) {
      var1.print(this.log.toString());
      var1.flush();
   }

   public synchronized void continueWithSourceServerDown(boolean var1) {
      Debug.assertion(this.status == 3);
      this.isSourceServerDown = var1 || this.jta;
      this.status = 0;
      this.notifyAll();
   }

   public synchronized void continueWithDestinationServerDown(boolean var1) {
      Debug.assertion(this.status == 4);
      this.isDestinationServerDown = var1;
      this.status = 0;
      this.notifyAll();
   }

   private void validateConfiguration() throws MigrationException {
      this.check(this.mt.getAllCandidateServers().length > 0, this.texter.getMigrationTaskErrorCandidateServerMustNotBeEmpty());
      if (!this.jta) {
         if (this.isTargetAutoMigratable(this.mt)) {
            if (this.currentServer != null) {
               this.check(!this.currentServer.getName().equals(this.destination.getName()), this.texter.getMigrationTaskErrorDestinationMustNotBeCurrentlyActiveServer());
            }
         } else {
            this.check(!this.mt.isManualActiveOn(this.destination), this.texter.getMigrationTaskErrorDestinationMustNotBeCurrentlyActiveServer());
         }
      }

      this.check(this.isDestinationServerMemberOfCandidateServers(this.destinationServerName), this.texter.getMigrationTaskErrorDestinationMustBeMemberOfCandidiateServers());
   }

   private boolean isDestinationServerMemberOfCandidateServers(String var1) {
      ServerMBean[] var2 = this.mt.getAllCandidateServers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return true;
         }
      }

      return false;
   }

   private String getServerAdminURL(String var1) {
      try {
         return URLManager.findAdministrationURL(var1);
      } catch (UnknownHostException var3) {
         return null;
      }
   }

   private void migrate() throws MigrationException {
      try {
         this.validateConfiguration();
         if (this.jta) {
            this.logLine(this.texter.getMigrationTaskLoglineJTAMigrationStarted(this.mt.getName(), this.destinationServerName));
         } else {
            this.logLine(this.texter.getMigrationTaskLoglineMigrationStarted(this.mt.getName(), this.destinationServerName));
         }

         this.logLine("Destination URL " + this.destinationServerURL + "\t" + this.isDestinationServerDown);
         this.logLine("Source URL " + this.currentServerURL + "\t" + this.isSourceServerDown);
         if (this.destinationServerURL == null && !this.isDestinationServerDown) {
            this.failWith(this.texter.getMigrationTaskUserStopDestinationNotReachable(this.destinationServerName));
         }

         if (this.currentServerURL == null && !this.isSourceServerDown) {
            this.failWith(this.texter.getMigrationTaskLoglineUnableToConnectToCurrentServer(this.currentServerName));
         }

         if (this.jta) {
            this.checkIfRunningServerTlogIsBeingMigrated(this.currentServerURL);
         }

         if (this.isTargetAutoMigratable(this.mt)) {
            ClusterMBean var1 = this.mt.getCluster();
            SingletonMonitorRemote var2 = this.getSingletonMonitor(var1);
            if (var2 == null) {
               throw new MigrationException("Cluster still not started. Please try after sometime.");
            }

            var2.migrate(this.mt.getName(), this.destinationServerName);
            if (!this.mt.getUserPreferredServer().getName().equals(this.destinationServerName)) {
               this.runtime.changeMigratableTargetsConfiguration(this.mt.getName(), this.destinationServerName);
            }
         } else {
            this.deactivateMigratableTarget();
            this.activateMigratableTarget();
            if (!this.jta) {
               this.runtime.changeMigratableTargetsConfiguration(this.mt.getName(), this.destinationServerName);
            } else {
               this.runtime.updateState(this.mt.getName(), this.destination.getName());
            }
         }

         this.logLine(this.texter.getActivationSucceeded());
      } catch (Exception var3) {
         this.failWith(var3.toString());
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else if (var3 instanceof MigrationException) {
            throw (MigrationException)var3;
         } else {
            throw new MigrationException(var3);
         }
      }
   }

   private void deactivateMigratableTarget() throws MigrationException {
      if (this.currentServerURL == null) {
         Debug.say("Cannot reach " + this.currentServerName + " to deactivate MT " + this.mt.getName());
      }

      if (this.currentServerURL != null) {
         Environment var1 = new Environment();
         Context var2 = null;
         RemoteMigrationControl var3 = null;

         try {
            var1.setProviderUrl(this.currentServerURL);
            this.logLine(this.texter.getMigrationTaskLoglineTryingToConnectToCurrentServer(this.currentServerURL));
            var2 = var1.getInitialContext();
            var3 = (RemoteMigrationControl)var2.lookup("weblogic.cluster.migrationControl");
            this.logLine(this.texter.getMigrationTaskLoglineConnectedSuccessfulyToCurrentServer(this.currentServerURL));
            this.logLine(this.texter.getTryingToDeactivateMigratableTarget(this.mt.getName()));
            var3.deactivateTarget(this.mt.getName(), this.destinationServerName);
            this.logLine(this.texter.getDeactivationSucceeded());
         } catch (NamingException var15) {
            String var5 = this.texter.getMigrationTaskLoglineUnableToConnectToCurrentServer(this.currentServerURL);
            this.logLine(var5);
            throw new MigrationException(var5);
         } catch (weblogic.cluster.migration.MigrationException var16) {
            this.logLine(this.texter.getServiceNotDeactivatedOnCurrentHostingServer(this.currentServerName));
            throw new MigrationException(this.texter.getServiceWasNotDeactivatedOnCurrentHostingServer(this.currentServerName, var16.toString()));
         } catch (RemoteException var17) {
            this.logLine(this.texter.getLostConnectionToCurrentHostingServerDeactivation(this.currentServerName));
            throw new MigrationException(this.texter.getLostConnectionToCurrentHostingServerDeactivationEx(this.currentServerName, var17.toString()));
         } finally {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (NamingException var14) {
               }
            }

         }
      } else if (!this.isSourceServerDown) {
         throw new MigrationException(this.texter.getServiceWasNotDeactivatedOnCurrentHostingServer(this.currentServerName, "Current server is unreachable"));
      }

   }

   private void activateMigratableTarget() throws MigrationException {
      if (this.destinationServerURL == null) {
         Debug.say("Cannot reach " + this.destinationServerName + " to activate MT " + this.mt.getName());
      }

      if (this.destinationServerURL != null) {
         Environment var1 = new Environment();
         Context var2 = null;
         RemoteMigrationControl var3 = null;

         try {
            var1.setProviderUrl(this.destinationServerURL);
            this.logLine(this.texter.getTyingToConnectDestinationServer(this.destinationServerURL));
            var2 = var1.getInitialContext();
            this.logLine(this.texter.getConnectedSuccessfulyToDestinationServer(this.destinationServerURL));
            var3 = (RemoteMigrationControl)var2.lookup("weblogic.cluster.migrationControl");
            this.logLine(this.texter.getTryingToActivateMigratableTarget(this.mt.getName()));
            var3.activateTarget(this.mt.getName());
            EditAccess var4 = ManagementServiceRestricted.getEditAccess(kernelId);
            boolean var5 = false;
            if (var4.getEditor() != null && !var4.isEditorExclusive()) {
               var5 = true;
            }

            if (!this.isDestinationServerDown && !this.jta && !var5) {
               for(int var6 = 0; var6 < 30 && var3.getMigratableState(this.mt.getName()) == 0; ++var6) {
                  try {
                     Thread.sleep(1000L);
                  } catch (InterruptedException var18) {
                  }

                  if (var6 == 29) {
                     throw new weblogic.cluster.migration.MigrationException("Could not determine state of Migratable services on destination server. Services may not be active.");
                  }
               }
            }
         } catch (NamingException var19) {
            this.logLine(this.texter.getUnableToConnectToDestinationServer(this.destinationServerName, this.destinationServerURL));
            throw new MigrationException(this.texter.getUnableToConnectToDestinationServer(this.destinationServerName, this.destinationServerURL));
         } catch (weblogic.cluster.migration.MigrationException var20) {
            this.logLine(this.texter.getMigratableServiceWasNotActivatedOnDestination(this.destinationServerName));
            throw new MigrationException(this.texter.getMigratableServiceWasNotActivatedOnDestinationEx(this.destinationServerName, var20.toString()));
         } catch (RemoteException var21) {
            this.logLine(this.texter.getLostConnectToDestinationServer(this.destinationServerName));
            throw new MigrationException(this.texter.getLostConnectToDestinationServerEx(this.destinationServerName, var21.toString()));
         } finally {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (NamingException var17) {
               }
            }

         }
      } else if (!this.isDestinationServerDown) {
         throw new MigrationException(this.texter.getMigratableServiceWasNotActivatedOnDestination(this.destinationServerName));
      }

   }

   private void checkIfRunningServerTlogIsBeingMigrated(String var1) throws MigrationException {
      if (this.currentServerName.equals(this.mt.getName()) && this.currentServerURL != null) {
         this.logLine(this.texter.getMigrationTaskLoglineCannotMigrateTransactionRecoveryService(this.currentServerName, var1));
         throw new MigrationException(this.texter.getMigrationTaskLoglineMigrationTaskLoglineCannotMigrateTransactionRecoveryServiceForTheCurrentServiceHost(this.currentServerName));
      }
   }

   private void check(boolean var1, String var2) throws MigrationException {
      if (!var1) {
         this.failWith(var2);
      }

   }

   private void failWith(String var1) throws MigrationException {
      this.logLine(var1);
      this.status = 2;
      throw new MigrationException(var1);
   }

   private void logLine(String var1) {
      ManagementLogger.logMigrationTaskProgressInfo(this.getDescription(), var1);
      this.log.append(var1 + "\n");
   }

   private boolean isTargetAutoMigratable(MigratableTargetMBean var1) {
      return !var1.getMigrationPolicy().equals("manual");
   }

   private SingletonMonitorRemote getSingletonMonitor(ClusterMBean var1) {
      ServerMBean[] var2 = var1.getServers();
      SingletonMonitorRemote var3 = null;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var3 = this.getSingletonMonitorRemote(var2[var4].getName());
         if (var3 != null) {
            return var3;
         }
      }

      return var3;
   }

   private SingletonMonitorRemote getSingletonMonitorRemote(String var1) {
      Environment var2 = new Environment();
      Context var3 = null;

      SingletonMonitorRemote var5;
      try {
         String var4 = URLManager.findAdministrationURL(var1);
         if (var4 != null) {
            var2.setProviderUrl(var4);
            var3 = var2.getInitialContext();
            var5 = (SingletonMonitorRemote)var3.lookup("weblogic/cluster/singleton/SingletonMonitorRemote");
            return var5;
         }

         var5 = null;
      } catch (NamingException var18) {
         var5 = null;
         return var5;
      } catch (UnknownHostException var19) {
         var5 = null;
         return var5;
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (NamingException var17) {
            }
         }

      }

      return var5;
   }
}
