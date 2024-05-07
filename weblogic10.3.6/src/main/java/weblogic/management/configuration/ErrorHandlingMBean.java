package weblogic.management.configuration;

public interface ErrorHandlingMBean extends ConfigurationMBean {
   String getPolicy();

   void setPolicy(String var1);

   String getLogPolicy();

   void setLogPolicy(String var1);
}
