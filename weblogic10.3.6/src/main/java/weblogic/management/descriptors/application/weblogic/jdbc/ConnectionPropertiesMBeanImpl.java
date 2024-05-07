package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ConnectionPropertiesMBeanImpl extends XMLElementMBeanDelegate implements ConnectionPropertiesMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_url = false;
   private String url;
   private boolean isSet_driverClassName = false;
   private String driverClassName;
   private boolean isSet_password = false;
   private String password;
   private boolean isSet_userName = false;
   private String userName;
   private boolean isSet_connectionParams = false;
   private ConnectionParamsMBean connectionParams;

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.url;
      this.url = var1;
      this.isSet_url = var1 != null;
      this.checkChange("url", var2, this.url);
   }

   public String getDriverClassName() {
      return this.driverClassName;
   }

   public void setDriverClassName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.driverClassName;
      this.driverClassName = var1;
      this.isSet_driverClassName = var1 != null;
      this.checkChange("driverClassName", var2, this.driverClassName);
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.password;
      this.password = var1;
      this.isSet_password = var1 != null;
      this.checkChange("password", var2, this.password);
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.userName;
      this.userName = var1;
      this.isSet_userName = var1 != null;
      this.checkChange("userName", var2, this.userName);
   }

   public ConnectionParamsMBean getConnectionParams() {
      return this.connectionParams;
   }

   public void setConnectionParams(ConnectionParamsMBean var1) {
      ConnectionParamsMBean var2 = this.connectionParams;
      this.connectionParams = var1;
      this.isSet_connectionParams = var1 != null;
      this.checkChange("connectionParams", var2, this.connectionParams);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<connection-properties");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</connection-properties>\n");
      return var2.toString();
   }
}
