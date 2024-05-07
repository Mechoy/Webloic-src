package weblogic.management.configuration;

public interface RestfulManagementServicesMBean extends ConfigurationMBean {
   boolean isEnabled();

   void setEnabled(boolean var1);
}
