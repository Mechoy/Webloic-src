package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class JMSSendDestinationMBeanImpl extends XMLElementMBeanDelegate implements JMSSendDestinationMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_providerUrl = false;
   private String providerUrl;
   private boolean isSet_connectionFactory = false;
   private String connectionFactory;
   private boolean isSet_componentName = false;
   private String componentName;
   private boolean isSet_initialContextFactory = false;
   private String initialContextFactory;
   private boolean isSet_jndiName = false;
   private JNDINameMBean jndiName;

   public String getProviderUrl() {
      return this.providerUrl;
   }

   public void setProviderUrl(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.providerUrl;
      this.providerUrl = var1;
      this.isSet_providerUrl = var1 != null;
      this.checkChange("providerUrl", var2, this.providerUrl);
   }

   public String getConnectionFactory() {
      return this.connectionFactory;
   }

   public void setConnectionFactory(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.connectionFactory;
      this.connectionFactory = var1;
      this.isSet_connectionFactory = var1 != null;
      this.checkChange("connectionFactory", var2, this.connectionFactory);
   }

   public String getComponentName() {
      return this.componentName;
   }

   public void setComponentName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.componentName;
      this.componentName = var1;
      this.isSet_componentName = var1 != null;
      this.checkChange("componentName", var2, this.componentName);
   }

   public String getInitialContextFactory() {
      return this.initialContextFactory;
   }

   public void setInitialContextFactory(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.initialContextFactory;
      this.initialContextFactory = var1;
      this.isSet_initialContextFactory = var1 != null;
      this.checkChange("initialContextFactory", var2, this.initialContextFactory);
   }

   public JNDINameMBean getJNDIName() {
      return this.jndiName;
   }

   public void setJNDIName(JNDINameMBean var1) {
      JNDINameMBean var2 = this.jndiName;
      this.jndiName = var1;
      this.isSet_jndiName = var1 != null;
      this.checkChange("jndiName", var2, this.jndiName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<jms-send-destination");
      if (this.isSet_componentName) {
         var2.append(" name=\"").append(String.valueOf(this.getComponentName())).append("\"");
      }

      if (this.isSet_providerUrl) {
         var2.append(" provider-url=\"").append(String.valueOf(this.getProviderUrl())).append("\"");
      }

      if (this.isSet_initialContextFactory) {
         var2.append(" initial-context-factory=\"").append(String.valueOf(this.getInitialContextFactory())).append("\"");
      }

      if (this.isSet_connectionFactory) {
         var2.append(" connection-factory=\"").append(String.valueOf(this.getConnectionFactory())).append("\"");
      }

      var2.append(">\n");
      if (null != this.getJNDIName()) {
         var2.append(this.getJNDIName().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</jms-send-destination>\n");
      return var2.toString();
   }
}
