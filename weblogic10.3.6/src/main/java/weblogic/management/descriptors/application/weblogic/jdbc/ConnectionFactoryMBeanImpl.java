package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ConnectionFactoryMBeanImpl extends XMLElementMBeanDelegate implements ConnectionFactoryMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_factoryName = false;
   private String factoryName;
   private boolean isSet_connectionProperties = false;
   private ConnectionPropertiesMBean connectionProperties;

   public String getFactoryName() {
      return this.factoryName;
   }

   public void setFactoryName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.factoryName;
      this.factoryName = var1;
      this.isSet_factoryName = var1 != null;
      this.checkChange("factoryName", var2, this.factoryName);
   }

   public ConnectionPropertiesMBean getConnectionProperties() {
      return this.connectionProperties;
   }

   public void setConnectionProperties(ConnectionPropertiesMBean var1) {
      ConnectionPropertiesMBean var2 = this.connectionProperties;
      this.connectionProperties = var1;
      this.isSet_connectionProperties = var1 != null;
      this.checkChange("connectionProperties", var2, this.connectionProperties);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<connection-factory");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</connection-factory>\n");
      return var2.toString();
   }
}
