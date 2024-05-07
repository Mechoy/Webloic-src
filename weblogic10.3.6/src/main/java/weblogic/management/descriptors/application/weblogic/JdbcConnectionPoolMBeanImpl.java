package weblogic.management.descriptors.application.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.descriptors.application.weblogic.jdbc.ConnectionFactoryMBean;
import weblogic.management.descriptors.application.weblogic.jdbc.DriverParamsMBean;
import weblogic.management.descriptors.application.weblogic.jdbc.PoolParamsMBean;
import weblogic.management.tools.ToXML;

public class JdbcConnectionPoolMBeanImpl extends XMLElementMBeanDelegate implements JdbcConnectionPoolMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_connectionFactory = false;
   private ConnectionFactoryMBean connectionFactory;
   private boolean isSet_aclName = false;
   private String aclName;
   private boolean isSet_poolParams = false;
   private PoolParamsMBean poolParams;
   private boolean isSet_dataSourceName = false;
   private String dataSourceName;
   private boolean isSet_driverParams = false;
   private DriverParamsMBean driverParams;

   public ConnectionFactoryMBean getConnectionFactory() {
      return this.connectionFactory;
   }

   public void setConnectionFactory(ConnectionFactoryMBean var1) {
      ConnectionFactoryMBean var2 = this.connectionFactory;
      this.connectionFactory = var1;
      this.isSet_connectionFactory = var1 != null;
      this.checkChange("connectionFactory", var2, this.connectionFactory);
   }

   public String getAclName() {
      return this.aclName;
   }

   public void setAclName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.aclName;
      this.aclName = var1;
      this.isSet_aclName = var1 != null;
      this.checkChange("aclName", var2, this.aclName);
   }

   public PoolParamsMBean getPoolParams() {
      return this.poolParams;
   }

   public void setPoolParams(PoolParamsMBean var1) {
      PoolParamsMBean var2 = this.poolParams;
      this.poolParams = var1;
      this.isSet_poolParams = var1 != null;
      this.checkChange("poolParams", var2, this.poolParams);
   }

   public String getDataSourceName() {
      return this.dataSourceName;
   }

   public void setDataSourceName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.dataSourceName;
      this.dataSourceName = var1;
      this.isSet_dataSourceName = var1 != null;
      this.checkChange("dataSourceName", var2, this.dataSourceName);
   }

   public DriverParamsMBean getDriverParams() {
      return this.driverParams;
   }

   public void setDriverParams(DriverParamsMBean var1) {
      DriverParamsMBean var2 = this.driverParams;
      this.driverParams = var1;
      this.isSet_driverParams = var1 != null;
      this.checkChange("driverParams", var2, this.driverParams);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<jdbc-connection-pool");
      var2.append(">\n");
      if (null != this.getDataSourceName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<data-source-name>").append(this.getDataSourceName()).append("</data-source-name>\n");
      }

      if (null != this.getConnectionFactory()) {
         var2.append(this.getConnectionFactory().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getPoolParams()) {
         var2.append(this.getPoolParams().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getDriverParams()) {
         var2.append(this.getDriverParams().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getAclName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<acl-name>").append(this.getAclName()).append("</acl-name>\n");
      }

      var2.append(ToXML.indent(var1)).append("</jdbc-connection-pool>\n");
      return var2.toString();
   }
}
