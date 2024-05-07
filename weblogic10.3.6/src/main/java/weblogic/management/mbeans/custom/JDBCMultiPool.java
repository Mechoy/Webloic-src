package weblogic.management.mbeans.custom;

import java.util.StringTokenizer;
import javax.management.InvalidAttributeValueException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JDBCConnectionPoolMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;

public final class JDBCMultiPool extends JDBCConfigurationMBeanCustomizer {
   private transient JDBCConnectionPoolMBean[] poolList;
   private String algorithm = "High-Availability";
   private String connectionPoolFailoverCallbackHandler = null;
   private boolean failoverRequestIfBusy = false;
   private int healthCheckFrequencySeconds = 300;

   public JDBCMultiPool(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public JDBCConnectionPoolMBean[] getPoolList() {
      if (this.delegate == null) {
         return this.poolList == null ? new JDBCConnectionPoolMBean[0] : this.poolList;
      } else {
         String var1 = this.delegate.getJDBCResource().getJDBCDataSourceParams().getDataSourceList();
         if (var1 == null) {
            return new JDBCConnectionPoolMBean[0];
         } else {
            StringTokenizer var2 = new StringTokenizer(var1, ",");
            JDBCConnectionPoolMBean[] var3 = new JDBCConnectionPoolMBean[var2.countTokens()];
            int var4 = 0;
            DomainMBean var5 = (DomainMBean)this.getMbean().getParent();
            JDBCConnectionPoolMBean[] var6 = var5.getJDBCConnectionPools();

            while(true) {
               while(var2.hasMoreTokens()) {
                  String var7 = var2.nextToken();

                  for(int var8 = 0; var8 < var6.length; ++var8) {
                     JDBCConnectionPoolMBean var9 = var6[var8];
                     if (var9 != null && var7.equals(var9.getName())) {
                        var3[var4++] = var9;
                        break;
                     }
                  }
               }

               if (var4 == 0) {
                  return new JDBCConnectionPoolMBean[0];
               }

               return var3;
            }
         }
      }
   }

   public void setPoolList(JDBCConnectionPoolMBean[] var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         String var2 = null;
         if (var1 != null) {
            int var3 = var1.length;
            if (var3 > 0) {
               StringBuffer var4 = new StringBuffer();

               for(int var5 = 0; var5 < var3; ++var5) {
                  var4.append(var1[var5].getName());
                  var4.append(",");
               }

               var4.deleteCharAt(var4.lastIndexOf(","));
               var2 = var4.toString();
            }
         }

         this.delegate.getJDBCResource().getJDBCDataSourceParams().setDataSourceList(var2);
      } else {
         this.poolList = var1;
      }

   }

   public void setAlgorithmType(String var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         if ("High-Availability".equals(var1)) {
            this.delegate.getJDBCResource().getJDBCDataSourceParams().setAlgorithmType("Failover");
         } else {
            this.delegate.getJDBCResource().getJDBCDataSourceParams().setAlgorithmType(var1);
         }
      } else {
         this.algorithm = var1;
      }

   }

   public String getAlgorithmType() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCDataSourceParams().getAlgorithmType() : this.algorithm;
   }

   public String getConnectionPoolFailoverCallbackHandler() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCDataSourceParams().getConnectionPoolFailoverCallbackHandler() : this.connectionPoolFailoverCallbackHandler;
   }

   public void setConnectionPoolFailoverCallbackHandler(String var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCDataSourceParams().setConnectionPoolFailoverCallbackHandler(var1);
      } else {
         this.connectionPoolFailoverCallbackHandler = var1;
      }

   }

   public boolean getFailoverRequestIfBusy() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCDataSourceParams().isFailoverRequestIfBusy() : this.failoverRequestIfBusy;
   }

   public void setFailoverRequestIfBusy(boolean var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCDataSourceParams().setFailoverRequestIfBusy(var1);
      } else {
         this.failoverRequestIfBusy = var1;
      }

   }

   public void setHealthCheckFrequencySeconds(int var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setTestFrequencySeconds(var1);
      } else {
         this.healthCheckFrequencySeconds = var1;
      }

   }

   public int getHealthCheckFrequencySeconds() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getTestFrequencySeconds() : this.healthCheckFrequencySeconds;
   }
}
