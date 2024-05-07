package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.DeploymentInfo;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.deploy.DeploymentCompatibilityEventHandler;
import weblogic.management.deploy.DeploymentCompatibilityEventManager;

/** @deprecated */
public interface ApplicationMBean extends ConfigurationMBean, DeploymentCompatibilityEventManager {
   String TYPE_EAR = DeploymentInfo.TYPE_EAR;
   String TYPE_EXPLODED_EAR = DeploymentInfo.TYPE_EXPLODED_EAR;
   String TYPE_COMPONENT = DeploymentInfo.TYPE_COMPONENT;
   String TYPE_EXPLODED_COMPONENT = DeploymentInfo.TYPE_EXPLODED_COMPONENT;
   String TYPE_UNKNOWN = DeploymentInfo.TYPE_UNKNOWN;
   boolean TWOPHASE_DEFAULT = true;
   String NO_STAGE = "nostage";
   String STAGE = "stage";
   String EXTERNAL_STAGE = "external_stage";
   String DEFAULT_STAGE = null;
   int DEPLOYMENT_TIMEOUT = 3600000;

   String getPath();

   void setPath(String var1) throws ManagementException, InvalidAttributeValueException;

   ComponentMBean[] getComponents();

   boolean addComponent(ComponentMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean removeComponent(ComponentMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   void setComponents(ComponentMBean[] var1);

   WebAppComponentMBean createWebAppComponent(String var1);

   void destroyWebAppComponent(WebAppComponentMBean var1);

   WebAppComponentMBean lookupWebAppComponent(String var1);

   WebAppComponentMBean[] getWebAppComponents();

   boolean isEar();

   String getFullPath();

   boolean isInternalApp();

   void setInternalApp(boolean var1);

   String getStagingPath();

   String[] getStagedTargets();

   void addStagedTarget(String var1);

   void removeStagedTarget(String var1);

   void setStagedTargets(String[] var1);

   void unstageTargets(String[] var1);

   String getStagingMode();

   void setStagingMode(String var1) throws ManagementException;

   boolean stagingEnabled(String var1);

   boolean staged(String var1);

   /** @deprecated */
   boolean useStagingDirectory(String var1);

   void sendAppLevelNotification(String var1, String var2, String var3);

   void sendModuleNotification(String var1, String var2, String var3, String var4, String var5, String var6, long var7);

   /** @deprecated */
   boolean isTwoPhase();

   /** @deprecated */
   void setTwoPhase(boolean var1);

   int getLoadOrder();

   void setLoadOrder(int var1);

   String getDeploymentType();

   void setDeploymentType(String var1);

   /** @deprecated */
   int getDeploymentTimeout();

   /** @deprecated */
   void setDeploymentTimeout(int var1);

   String getAltDescriptorPath();

   void setAltDescriptorPath(String var1);

   String getAltWLSDescriptorPath();

   void setAltWLSDescriptorPath(String var1);

   void refreshDDsIfNeeded(String[] var1, String[] var2);

   /** @deprecated */
   boolean isDeployed();

   /** @deprecated */
   void setDeployed(boolean var1);

   EJBComponentMBean createEJBComponent(String var1);

   void destroyEJBComponent(EJBComponentMBean var1);

   EJBComponentMBean lookupEJBComponent(String var1);

   EJBComponentMBean[] getEJBComponents();

   ConnectorComponentMBean createConnectorComponent(String var1);

   void destroyConnectorComponent(ConnectorComponentMBean var1);

   ConnectorComponentMBean lookupConnectorComponent(String var1);

   ConnectorComponentMBean[] getConnectorComponents();

   WebServiceComponentMBean createWebServiceComponent(String var1);

   void destroyWebServiceComponent(WebServiceComponentMBean var1);

   ComponentMBean createDummyComponent(String var1);

   WebServiceComponentMBean lookupWebServiceComponent(String var1);

   WebServiceComponentMBean[] getWebServiceComponents();

   JDBCPoolComponentMBean createJDBCPoolComponent(String var1);

   void destroyJDBCPoolComponent(JDBCPoolComponentMBean var1);

   JDBCPoolComponentMBean lookupJDBCPoolComponent(String var1);

   JDBCPoolComponentMBean[] getJDBCPoolComponents();

   /** @deprecated */
   int getInternalType();

   void setAppDeployment(AppDeploymentMBean var1);

   AppDeploymentMBean getAppDeployment();

   AppDeploymentMBean returnDeployableUnit();

   void setDelegationEnabled(boolean var1);

   boolean isDelegationEnabled();

   void addHandler(DeploymentCompatibilityEventHandler var1);
}
