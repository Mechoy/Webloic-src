package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

public interface DeploymentConfigurationMBean extends ConfigurationMBean {
   int getMaxAppVersions();

   void setMaxAppVersions(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean isRemoteDeployerEJBEnabled();

   void setRemoteDeployerEJBEnabled(boolean var1);

   boolean isRestageOnlyOnRedeploy();

   void setRestageOnlyOnRedeploy(boolean var1);
}
