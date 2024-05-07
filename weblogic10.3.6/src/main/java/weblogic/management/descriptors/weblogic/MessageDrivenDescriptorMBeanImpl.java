package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class MessageDrivenDescriptorMBeanImpl extends XMLElementMBeanDelegate implements MessageDrivenDescriptorMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_providerURL = false;
   private String providerURL;
   private boolean isSet_pool = false;
   private PoolMBean pool;
   private boolean isSet_jmsPollingIntervalSeconds = false;
   private int jmsPollingIntervalSeconds = 10;
   private boolean isSet_jmsClientID = false;
   private String jmsClientID;
   private boolean isSet_initialContextFactory = false;
   private String initialContextFactory = "weblogic.jndi.WLInitialContextFactory";
   private boolean isSet_destinationJNDIName = false;
   private String destinationJNDIName;
   private boolean isSet_connectionFactoryJNDIName = false;
   private String connectionFactoryJNDIName = "weblogic.jms.MessageDrivenBeanConnectionFactory";

   public String getProviderURL() {
      return this.providerURL;
   }

   public void setProviderURL(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.providerURL;
      this.providerURL = var1;
      this.isSet_providerURL = var1 != null;
      this.checkChange("providerURL", var2, this.providerURL);
   }

   public PoolMBean getPool() {
      return this.pool;
   }

   public void setPool(PoolMBean var1) {
      PoolMBean var2 = this.pool;
      this.pool = var1;
      this.isSet_pool = var1 != null;
      this.checkChange("pool", var2, this.pool);
   }

   public int getJMSPollingIntervalSeconds() {
      return this.jmsPollingIntervalSeconds;
   }

   public void setJMSPollingIntervalSeconds(int var1) {
      int var2 = this.jmsPollingIntervalSeconds;
      this.jmsPollingIntervalSeconds = var1;
      this.isSet_jmsPollingIntervalSeconds = var1 != -1;
      this.checkChange("jmsPollingIntervalSeconds", var2, this.jmsPollingIntervalSeconds);
   }

   public String getJMSClientID() {
      return this.jmsClientID;
   }

   public void setJMSClientID(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.jmsClientID;
      this.jmsClientID = var1;
      this.isSet_jmsClientID = var1 != null;
      this.checkChange("jmsClientID", var2, this.jmsClientID);
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

   public String getDestinationJNDIName() {
      return this.destinationJNDIName;
   }

   public void setDestinationJNDIName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.destinationJNDIName;
      this.destinationJNDIName = var1;
      this.isSet_destinationJNDIName = var1 != null;
      this.checkChange("destinationJNDIName", var2, this.destinationJNDIName);
   }

   public String getConnectionFactoryJNDIName() {
      return this.connectionFactoryJNDIName;
   }

   public void setConnectionFactoryJNDIName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.connectionFactoryJNDIName;
      this.connectionFactoryJNDIName = var1;
      this.isSet_connectionFactoryJNDIName = var1 != null;
      this.checkChange("connectionFactoryJNDIName", var2, this.connectionFactoryJNDIName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<message-driven-descriptor");
      var2.append(">\n");
      if (null != this.getPool()) {
         var2.append(this.getPool().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getDestinationJNDIName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<destination-jndi-name>").append(this.getDestinationJNDIName()).append("</destination-jndi-name>\n");
      }

      if ((this.isSet_initialContextFactory || !"weblogic.jndi.WLInitialContextFactory".equals(this.getInitialContextFactory())) && null != this.getInitialContextFactory()) {
         var2.append(ToXML.indent(var1 + 2)).append("<initial-context-factory>").append(this.getInitialContextFactory()).append("</initial-context-factory>\n");
      }

      if (null != this.getProviderURL()) {
         var2.append(ToXML.indent(var1 + 2)).append("<provider-url>").append(this.getProviderURL()).append("</provider-url>\n");
      }

      if ((this.isSet_connectionFactoryJNDIName || !"weblogic.jms.MessageDrivenBeanConnectionFactory".equals(this.getConnectionFactoryJNDIName())) && null != this.getConnectionFactoryJNDIName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<connection-factory-jndi-name>").append(this.getConnectionFactoryJNDIName()).append("</connection-factory-jndi-name>\n");
      }

      if (this.isSet_jmsPollingIntervalSeconds || 10 != this.getJMSPollingIntervalSeconds()) {
         var2.append(ToXML.indent(var1 + 2)).append("<jms-polling-interval-seconds>").append(this.getJMSPollingIntervalSeconds()).append("</jms-polling-interval-seconds>\n");
      }

      if (null != this.getJMSClientID()) {
         var2.append(ToXML.indent(var1 + 2)).append("<jms-client-id>").append(this.getJMSClientID()).append("</jms-client-id>\n");
      }

      var2.append(ToXML.indent(var1)).append("</message-driven-descriptor>\n");
      return var2.toString();
   }
}
