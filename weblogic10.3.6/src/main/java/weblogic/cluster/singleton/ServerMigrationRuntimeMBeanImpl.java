package weblogic.cluster.singleton;

import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import weblogic.cluster.ClusterLogger;
import weblogic.jndi.Environment;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ClusterRuntimeMBean;
import weblogic.management.runtime.MigrationDataRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServerMigrationRuntimeMBean;
import weblogic.protocol.URLManager;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.work.WorkManagerFactory;

public final class ServerMigrationRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ServerMigrationRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static ServerMigrationRuntimeMBeanImpl singleton;
   private List migrationList = new ArrayList();

   private ServerMigrationRuntimeMBeanImpl(RuntimeMBean var1) throws ManagementException {
      super("ServerMigrationRuntime", var1, true);
   }

   static synchronized void initialize() throws ManagementException {
      ClusterRuntimeMBean var0 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getClusterRuntime();
      if (var0 != null) {
         if (singleton == null) {
            singleton = new ServerMigrationRuntimeMBeanImpl(var0);
         }

      }
   }

   public static synchronized ServerMigrationRuntimeMBeanImpl getInstance() {
      Debug.assertion(singleton != null, "Cannot use ServerMigrationRuntimeMBeanImpl without initialization");
      return singleton;
   }

   public boolean isClusterMaster() {
      return MigratableServerService.theOne().isClusterMaster();
   }

   public String getClusterMasterName() throws ManagementException {
      try {
         return MigratableServerService.theOne().findClusterMaster();
      } catch (LeasingException var2) {
         throw new ManagementException("Unable to determine ClusterMaster due to " + var2.getMessage());
      }
   }

   public MigrationDataRuntimeMBean[] getMigrationData() {
      if (this.migrationList.size() == 0) {
         return null;
      } else {
         MigrationDataRuntimeMBean[] var1 = new MigrationDataRuntimeMBean[this.migrationList.size()];
         this.migrationList.toArray(var1);
         return var1;
      }
   }

   void migrationCompleted(MigratableServerState var1) {
      MigrationDataRuntimeMBean[] var2 = this.getMigrationData();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].getServerName().equals(var1.getServer().getName()) && var2[var3].getStatus() == 1) {
               MigrationDataImpl var4 = new MigrationDataImpl(var1, 0, var2[var3].getMigrationStartTime());
               ((MigrationDataRuntimeMBeanImpl)var2[var3]).update(var4);
               this.updateAdminServer(var4);
               return;
            }
         }
      }

   }

   void migrationStarted(MigratableServerState var1) {
      MigrationDataRuntimeMBean[] var2 = this.getMigrationData();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].getServerName().equals(var1.getServer().getName()) && var2[var3].getStatus() == 1) {
               MigrationDataImpl var4 = new MigrationDataImpl(var1, 1, var2[var3].getMigrationStartTime());
               ((MigrationDataRuntimeMBeanImpl)var2[var3]).update(var4);
               this.updateAdminServer(var4);
               return;
            }
         }
      }

      try {
         MigrationDataImpl var6 = new MigrationDataImpl(var1, 1, System.currentTimeMillis());
         this.updateAdminServer(var6);
         this.migrationList.add(new MigrationDataRuntimeMBeanImpl(this, var6));
      } catch (ManagementException var5) {
         ClusterLogger.logErrorReportingMigrationRuntimeInfo(var5);
      }

   }

   private void updateAdminServer(final MigrationData var1) {
      try {
         String var2 = ManagementService.getRuntimeAccess(kernelId).getAdminServerName();
         if (var2 == null) {
            return;
         }

         final DomainMigrationHistory var3 = this.getDomainMigrationHistoryRemote(var2);
         if (var3 == null) {
            return;
         }

         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               var3.update(var1);
            }
         });
      } catch (RemoteRuntimeException var4) {
      }

   }

   private DomainMigrationHistory getDomainMigrationHistoryRemote(String var1) {
      try {
         Environment var2 = new Environment();
         var2.setProviderUrl(URLManager.findAdministrationURL(var1));
         DomainMigrationHistory var3 = (DomainMigrationHistory)PortableRemoteObject.narrow(var2.getInitialReference(DomainMigrationHistoryImpl.class), DomainMigrationHistory.class);
         return var3;
      } catch (UnknownHostException var4) {
         return null;
      } catch (NamingException var5) {
         ClusterLogger.logErrorReportingMigrationRuntimeInfo(var5);
         return null;
      }
   }

   private static final class MigrationDataImpl implements MigrationData {
      private final String serverName;
      private final String machineMigratedFrom;
      private final String machineMigratedTo;
      private final String clusterMasterName;
      private final String clusterName;
      private int status;
      private long endTime;
      private long startTime;

      MigrationDataImpl(MigratableServerState var1, int var2, long var3) {
         this.serverName = var1.getServer().getName();
         if (var1.getPreviousMachine() != null) {
            this.machineMigratedFrom = var1.getPreviousMachine().getName();
         } else {
            this.machineMigratedFrom = null;
         }

         this.machineMigratedTo = var1.getCurrentMachine().getName();
         this.clusterMasterName = ManagementService.getRuntimeAccess(ServerMigrationRuntimeMBeanImpl.kernelId).getServerName();
         this.clusterName = ManagementService.getRuntimeAccess(ServerMigrationRuntimeMBeanImpl.kernelId).getServerRuntime().getClusterRuntime().getName();
         this.startTime = var3;
         this.status = var2;
         if (var2 == 2 || var2 == 0) {
            this.endTime = System.currentTimeMillis();
         }

      }

      public String getServerName() {
         return this.serverName;
      }

      public int getStatus() {
         return this.status;
      }

      public String getMachineMigratedFrom() {
         return this.machineMigratedFrom;
      }

      public String getMachineMigratedTo() {
         return this.machineMigratedTo;
      }

      public long getMigrationStartTime() {
         return this.startTime;
      }

      public long getMigrationEndTime() {
         return this.endTime;
      }

      public String getClusterName() {
         return this.clusterName;
      }

      public String getClusterMasterName() {
         return this.clusterMasterName;
      }

      public String getMigrationType() {
         return "server";
      }
   }
}
