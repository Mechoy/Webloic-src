package weblogic.cluster.singleton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.JDBCDriverParamsBean;
import weblogic.jdbc.common.internal.JDBCMBeanConverter;
import weblogic.management.DistributedManagementException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.NodeManagerMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;

public class ConfigMigrationProcessor implements ConfigurationProcessor {
   private static final String MIG_TARGET_TYPE = "MigratableTarget";
   private static final HashMap migTar2ServerMap = new HashMap();
   private static final String CONNECTION_POOL_NAME = "_autogen_SingletonConnPool";
   private static final String JDBC_SYSTEM_RESOURCE = "_autogen_SingletonJDBCSystemResource";

   public static final ConfigMigrationProcessor getInstance() {
      return ConfigMigrationProcessor.SingletonMaker.config;
   }

   private ConfigMigrationProcessor() {
   }

   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      try {
         this.changeJMSServerTargets(var1);
         this.changeJTAMigratableTargets(var1);
      } catch (Exception var3) {
         throw new UpdateException(var3);
      }
   }

   public void setConnectionPoolProperties(String var1, String var2, String var3, String var4, String var5, DomainMBean var6) throws InvalidAttributeValueException, DistributedManagementException {
      JDBCSystemResourceMBean var7 = null;
      ClusterMBean[] var8 = var6.getClusters();

      for(int var9 = 0; var9 < var8.length; ++var9) {
         if (this.isMigratableCluster(var8[var9])) {
            var7 = var8[var9].getDataSourceForAutomaticMigration();
            String var10 = var8[var9].getName() + "_autogen_SingletonJDBCSystemResource";
            if (var7 == null) {
               var7 = var6.createJDBCSystemResource(var10, var10 + "-jdbc.xml");
            }

            JDBCDataSourceBean var11 = var7.getJDBCResource();
            var11.setName(var10);
            JDBCDriverParamsBean var12 = var11.getJDBCDriverParams();
            var12.setDriverName(var3);
            var12.setPassword(var2);
            var12.setUrl(var4);
            Properties var13 = new Properties();
            var13.put("user", var1);
            String[] var14 = new String[]{"_autogen_SingletonConnPool"};
            var11.getJDBCDataSourceParams().setJNDINames(var14);
            JDBCMBeanConverter.setDriverProperties(var11, var13);
            var7.setTargets(new TargetMBean[]{var8[var9]});
            var8[var9].setDataSourceForAutomaticMigration(var7);
         }
      }

   }

   public void createMachine(DomainMBean var1, String var2, String var3, String var4, String var5, String var6) throws InvalidAttributeValueException {
      MachineMBean[] var7 = var1.getMachines();

      for(int var8 = 0; var8 < var7.length; ++var8) {
         if (var7[var8].getName().equals(var2)) {
            return;
         }
      }

      MachineMBean var10 = var1.createMachine(var2);
      NodeManagerMBean var9 = var10.getNodeManager();
      var9.setListenAddress(var3);
      var9.setListenPort(Integer.parseInt(var4));
      if (var5.length() > 0) {
         var9.setNMType(var5);
      }

      var1.lookupServer(var6).setMachine(var10);
   }

   public void setCandidateMachinesForAutomaticMigration(DomainMBean var1) {
      ClusterMBean[] var2 = var1.getClusters();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (this.isMigratableCluster(var2[var3])) {
            this.setCandidateMachines(var2[var3]);
         }
      }

   }

   private void setCandidateMachines(ClusterMBean var1) {
      ServerMBean[] var2 = var1.getServers();
      HashSet var3 = new HashSet();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         MachineMBean var5 = var2[var4].getMachine();
         if (var5 != null) {
            var3.add(var5);
         }
      }

      MachineMBean[] var6 = new MachineMBean[var3.size()];
      var1.setCandidateMachinesForMigratableServers((MachineMBean[])((MachineMBean[])var3.toArray(var6)));
   }

   private boolean isMigratableCluster(ClusterMBean var1) {
      ServerMBean[] var2 = var1.getServers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].isAutoMigrationEnabled()) {
            return true;
         }
      }

      return false;
   }

   private void changeJMSServerTargets(DomainMBean var1) throws Exception {
      JMSServerMBean[] var2 = var1.getJMSServers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         TargetMBean[] var4 = var2[var3].getTargets();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = var4[var5].getType();
            if (var6.equals("MigratableTarget")) {
               MigratableTargetMBean var7 = (MigratableTargetMBean)var4[var5];
               TargetMBean[] var8 = new TargetMBean[]{var7.getUserPreferredServer()};
               var2[var3].setTargets(var8);
            }
         }
      }

      this.deleteMigratableTargets(var1);
   }

   private void deleteMigratableTargets(DomainMBean var1) {
      ClusterMBean[] var2 = var1.getClusters();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         MigratableTargetMBean[] var4 = var2[var3].getMigratableTargets();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            this.addMigratableTargetToMap(var4[var5]);
            var1.destroyMigratableTarget(var4[var5]);
         }
      }

   }

   private void changeJTAMigratableTargets(DomainMBean var1) throws Exception {
      ClusterMBean[] var2 = var1.getClusters();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         ServerMBean[] var4 = var2[var3].getServers();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            JTAMigratableTargetMBean var6 = var4[var5].getJTAMigratableTarget();
            if (!var4[var5].isAutoMigrationEnabled()) {
               var4[var5].setAutoMigrationEnabled(true);
            }

            this.addMigratableTargetToMap(var6);
            var1.destroyMigratableTarget(var6);
         }
      }

   }

   private void addMigratableTargetToMap(MigratableTargetMBean var1) {
      ServerMBean var2 = var1.getUserPreferredServer();
      migTar2ServerMap.put(var1.getName(), var2.getName());
   }

   static HashMap getMTToServerMapping() {
      return migTar2ServerMap;
   }

   // $FF: synthetic method
   ConfigMigrationProcessor(Object var1) {
      this();
   }

   private static class SingletonMaker {
      private static final ConfigMigrationProcessor config = new ConfigMigrationProcessor();
   }
}
