package weblogic.jdbc.module;

import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JDBCConnectionPoolMBean;
import weblogic.management.configuration.JDBCDataSourceMBean;
import weblogic.management.configuration.JDBCMultiPoolMBean;
import weblogic.management.configuration.JDBCStoreMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.JDBCTxDataSourceMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;

public class JDBCConfigurationProcessor implements ConfigurationProcessor {
   JDBCDeploymentHelper helper = new JDBCDeploymentHelper();

   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      JDBCSystemResourceMBean var2 = null;

      try {
         JDBCConnectionPoolMBean[] var4 = var1.getJDBCConnectionPools();
         int var3;
         if (var4 != null) {
            for(var3 = 0; var3 < var4.length; ++var3) {
               var2 = this.helper.createJDBCSystemResource(var4[var3], 1, var1);
               var2.setTargets(var4[var3].getTargets());
               this.fixJDBCConnectionPoolReferences(var1, var4[var3], var2);
               var1.destroyJDBCConnectionPool(var4[var3]);
            }
         }

         JDBCMultiPoolMBean[] var5 = var1.getJDBCMultiPools();
         if (var5 != null) {
            for(var3 = 0; var3 < var5.length; ++var3) {
               var2 = this.helper.createJDBCSystemResource(var5[var3], 2, var1);
               var2.setTargets(var5[var3].getTargets());
               var1.destroyJDBCMultiPool(var5[var3]);
            }
         }

         JDBCDataSourceMBean[] var6 = var1.getJDBCDataSources();
         if (var6 != null) {
            for(var3 = 0; var3 < var6.length; ++var3) {
               var2 = this.helper.createJDBCSystemResource(var6[var3], 3, var1);
               var2.setTargets(var6[var3].getTargets());
               var1.destroyJDBCDataSource(var6[var3]);
            }
         }

         JDBCTxDataSourceMBean[] var7 = var1.getJDBCTxDataSources();
         if (var6 != null) {
            for(var3 = 0; var3 < var7.length; ++var3) {
               var2 = this.helper.createJDBCSystemResource(var7[var3], 4, var1);
               var2.setTargets(var7[var3].getTargets());
               var1.destroyJDBCTxDataSource(var7[var3]);
            }
         }

      } catch (Exception var8) {
         throw new UpdateException(var8.getMessage());
      }
   }

   private void fixJDBCConnectionPoolReferences(DomainMBean var1, JDBCConnectionPoolMBean var2, JDBCSystemResourceMBean var3) {
      JDBCStoreMBean[] var4 = var1.getJDBCStores();
      if (var4 != null) {
         JDBCConnectionPoolMBean var5 = null;

         for(int var6 = 0; var6 < var4.length; ++var6) {
            if ((var5 = var4[var6].getConnectionPool()) != null && var5.getName().equals(var2.getName())) {
               try {
                  var4[var6].setDataSource(var3);
                  var4[var6].setConnectionPool((JDBCConnectionPoolMBean)null);
               } catch (Exception var8) {
               }
            }
         }
      }

   }
}
