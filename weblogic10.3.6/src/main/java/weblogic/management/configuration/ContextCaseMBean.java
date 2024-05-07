package weblogic.management.configuration;

public interface ContextCaseMBean extends DeploymentMBean {
   String getUserName();

   void setUserName(String var1);

   String getGroupName();

   void setGroupName(String var1);

   String getRequestClassName();

   void setRequestClassName(String var1);
}
