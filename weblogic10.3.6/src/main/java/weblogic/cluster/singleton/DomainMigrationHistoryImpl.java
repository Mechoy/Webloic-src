package weblogic.cluster.singleton;

import java.util.ArrayList;
import java.util.List;
import weblogic.cluster.ClusterLogger;
import weblogic.management.ManagementException;
import weblogic.management.runtime.MigrationDataRuntimeMBean;
import weblogic.management.runtime.ServiceMigrationDataRuntimeMBean;

public class DomainMigrationHistoryImpl implements DomainMigrationHistory {
   public static final DomainMigrationHistoryImpl THE_ONE = new DomainMigrationHistoryImpl();
   private List serverMigrationList = new ArrayList();
   private List serviceMigrationList = new ArrayList();

   private DomainMigrationHistoryImpl() {
   }

   public synchronized void update(MigrationData var1) {
      int var3;
      if (var1.getMigrationType().equals("server")) {
         MigrationDataRuntimeMBean[] var2 = this.getMigrationDataRuntimes();
         if (var2 != null) {
            for(var3 = 0; var3 < var2.length; ++var3) {
               if (var2[var3].getServerName().equals(var1.getServerName()) && var2[var3].getMigrationStartTime() == var1.getMigrationStartTime()) {
                  ((MigrationDataRuntimeMBeanImpl)var2[var3]).update(var1);
                  return;
               }
            }
         }

         try {
            this.serverMigrationList.add(new MigrationDataRuntimeMBeanImpl(var1));
         } catch (ManagementException var6) {
            ClusterLogger.logErrorUpdatingMigrationRuntimeInfo(var6);
         }
      } else if (var1.getMigrationType().equals("service")) {
         ServiceMigrationDataRuntimeMBean[] var7 = this.getServiceMigrationDataRuntimes();
         if (var7 != null) {
            for(var3 = 0; var3 < var7.length; ++var3) {
               if (var7[var3].getServerName().equals(var1.getServerName()) && var7[var3].getMigrationStartTime() == var1.getMigrationStartTime()) {
                  ((ServiceMigrationDataRuntimeMBeanImpl)var7[var3]).update(var1);
                  return;
               }
            }
         }

         try {
            this.serviceMigrationList.add(new ServiceMigrationDataRuntimeMBeanImpl(var1));
         } catch (ManagementException var4) {
            ClusterLogger.logErrorUpdatingMigrationRuntimeInfo(var4);
         } catch (IllegalArgumentException var5) {
            ClusterLogger.logErrorUpdatingMigrationRuntimeInfo(var5);
         }
      } else {
         ClusterLogger.logUnknownMigrationDataType(var1.getMigrationType());
      }

   }

   public synchronized MigrationDataRuntimeMBean[] getMigrationDataRuntimes() {
      if (this.serverMigrationList.size() == 0) {
         return null;
      } else {
         MigrationDataRuntimeMBean[] var1 = new MigrationDataRuntimeMBean[this.serverMigrationList.size()];
         this.serverMigrationList.toArray(var1);
         return var1;
      }
   }

   public synchronized ServiceMigrationDataRuntimeMBean[] getServiceMigrationDataRuntimes() {
      if (this.serviceMigrationList.size() == 0) {
         return null;
      } else {
         ServiceMigrationDataRuntimeMBean[] var1 = new ServiceMigrationDataRuntimeMBean[this.serviceMigrationList.size()];
         this.serviceMigrationList.toArray(var1);
         return var1;
      }
   }
}
