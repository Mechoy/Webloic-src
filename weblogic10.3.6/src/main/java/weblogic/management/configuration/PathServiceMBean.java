package weblogic.management.configuration;

public interface PathServiceMBean extends DeploymentMBean {
   PersistentStoreMBean getPersistentStore();

   void setPersistentStore(PersistentStoreMBean var1);
}
