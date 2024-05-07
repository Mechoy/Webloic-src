package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JDBCConnectionPoolMBean;
import weblogic.management.configuration.JDBCStoreMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.JMSJDBCStoreMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public class JMSJDBCStore extends ConfigurationMBeanCustomizer {
   private transient JDBCStoreMBean newBean;
   private transient JDBCConnectionPoolMBean connectionPool;
   private String prefixName;
   private String createTableDDLFile;

   public JMSJDBCStore(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void setDelegatedBean(JDBCStoreMBean var1) {
      this.newBean = var1;
   }

   public static void copy(JMSJDBCStoreMBean var0, JDBCStoreMBean var1) {
      try {
         if (var0.getConnectionPool() != null) {
            var1.setConnectionPool(var0.getConnectionPool());
         }

         if (var0.getPrefixName() != null) {
            var1.setPrefixName(var0.getPrefixName());
         }

         if (var0.getCreateTableDDLFile() != null) {
            var1.setCreateTableDDLFile(var0.getCreateTableDDLFile());
         }
      } catch (InvalidAttributeValueException var3) {
      }

   }

   public void setConnectionPool(JDBCConnectionPoolMBean var1) throws InvalidAttributeValueException {
      if (this.newBean == null) {
         this.connectionPool = var1;
      } else {
         JDBCSystemResourceMBean var2 = this.lookupDataSource((DomainMBean)this.newBean.getParent(), var1.getName());
         if (var2 == null) {
            throw new InvalidAttributeValueException("The data source " + var1.getName() + " cannot be found");
         }

         this.newBean.setDataSource(var2);
      }

   }

   public JDBCConnectionPoolMBean getConnectionPool() {
      if (this.newBean == null) {
         return this.connectionPool;
      } else if (this.newBean.getConnectionPool() != null) {
         return this.newBean.getConnectionPool();
      } else {
         JDBCSystemResourceMBean var1 = this.newBean.getDataSource();
         if (var1 != null && var1.getJDBCResource() != null) {
            String var2 = var1.getJDBCResource().getName();
            DomainMBean var3 = (DomainMBean)this.getMbean().getParent();
            return var3.lookupJDBCConnectionPool(var2);
         } else {
            return null;
         }
      }
   }

   public void setPrefixName(String var1) throws InvalidAttributeValueException {
      if (this.newBean != null) {
         this.newBean.setPrefixName(var1);
      } else {
         this.prefixName = var1;
      }

   }

   public String getPrefixName() {
      return this.newBean != null ? this.newBean.getPrefixName() : this.prefixName;
   }

   public void setCreateTableDDLFile(String var1) throws InvalidAttributeValueException {
      if (this.newBean != null) {
         this.newBean.setCreateTableDDLFile(var1);
      } else {
         this.createTableDDLFile = var1;
      }

   }

   public String getCreateTableDDLFile() {
      return this.newBean != null ? this.newBean.getCreateTableDDLFile() : this.createTableDDLFile;
   }

   private JDBCSystemResourceMBean lookupDataSource(DomainMBean var1, String var2) {
      JDBCSystemResourceMBean[] var3 = var1.getJDBCSystemResources();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            JDBCSystemResourceMBean var5 = var3[var4];
            JDBCDataSourceBean var6 = var5.getJDBCResource();
            if (var6 != null && var2.equals(var6.getName())) {
               return var5;
            }
         }
      }

      return null;
   }
}
