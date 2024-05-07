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
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServiceMigrationDataRuntimeMBean;
import weblogic.management.runtime.ServiceMigrationRuntimeMBean;
import weblogic.protocol.URLManager;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.work.WorkManagerFactory;

public final class ServiceMigrationRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ServiceMigrationRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static ServiceMigrationRuntimeMBeanImpl singleton;
   private List migrationList = new ArrayList();

   private ServiceMigrationRuntimeMBeanImpl(RuntimeMBean var1) throws ManagementException {
      super("ServiceMigrationRuntime", var1, true);
   }

   static synchronized void initialize() throws ManagementException {
      ClusterRuntimeMBean var0 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getClusterRuntime();
      if (var0 != null) {
         if (singleton == null) {
            singleton = new ServiceMigrationRuntimeMBeanImpl(var0);
         }

      }
   }

   public static synchronized ServiceMigrationRuntimeMBeanImpl getInstance() {
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

   public ServiceMigrationDataRuntimeMBean[] getMigrationData() {
      if (this.migrationList.size() == 0) {
         return null;
      } else {
         ServiceMigrationDataRuntimeMBean[] var1 = new ServiceMigrationDataRuntimeMBean[this.migrationList.size()];
         this.migrationList.toArray(var1);
         return var1;
      }
   }

   synchronized void migrationCompleted(String var1, String var2, String var3) {
      ServiceMigrationDataRuntimeMBean[] var4 = this.getMigrationData();
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getServerName().equals(var1) && var4[var5].getStatus() == 1) {
               MigrationDataImpl var6 = new MigrationDataImpl(var1, var2, var3, 0, var4[var5].getMigrationStartTime());
               ((ServiceMigrationDataRuntimeMBeanImpl)var4[var5]).update(var6);
               this.updateAdminServer(var6);
               return;
            }
         }
      }

   }

   synchronized void migrationStarted(String var1, String var2, String var3) {
      ServiceMigrationDataRuntimeMBean[] var4 = this.getMigrationData();
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getServerName().equals(var1) && var4[var5].getStatus() == 1) {
               MigrationDataImpl var6 = new MigrationDataImpl(var1, var2, var3, 1, var4[var5].getMigrationStartTime());
               ((ServiceMigrationDataRuntimeMBeanImpl)var4[var5]).update(var6);
               this.updateAdminServer(var6);
               return;
            }
         }
      }

      try {
         MigrationDataImpl var9 = new MigrationDataImpl(var1, var2, var3, 1, System.currentTimeMillis());
         this.updateAdminServer(var9);
         this.migrationList.add(new ServiceMigrationDataRuntimeMBeanImpl(this, var9));
      } catch (ManagementException var7) {
         ClusterLogger.logErrorReportingMigrationRuntimeInfo(var7);
      } catch (IllegalArgumentException var8) {
         ClusterLogger.logErrorReportingMigrationRuntimeInfo(var8);
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

      MigrationDataImpl(String var1, String var2, String var3, int var4, long var5) {
         this.serverName = var1;
         this.machineMigratedFrom = var2;
         this.machineMigratedTo = var3;
         this.clusterMasterName = ManagementService.getRuntimeAccess(ServiceMigrationRuntimeMBeanImpl.kernelId).getServerName();
         this.clusterName = ManagementService.getRuntimeAccess(ServiceMigrationRuntimeMBeanImpl.kernelId).getServerRuntime().getClusterRuntime().getName();
         this.startTime = var5;
         this.status = var4;
         if (var4 == 2 || var4 == 0) {
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
         return "service";
      }

      public String toString() {
         return this.getServerName() + " " + this.getMachineMigratedFrom() + " " + this.getMachineMigratedTo() + " " + this.getMigrationStartTime() + " " + this.getMigrationType();
      }
   }
}
