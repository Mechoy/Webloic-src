package weblogic.management.configuration;

/** @deprecated */
public interface JMSStoreMBean extends ConfigurationMBean {
   JMSServerMBean getJMSServer();

   void setJMSServer(JMSServerMBean var1);
}
