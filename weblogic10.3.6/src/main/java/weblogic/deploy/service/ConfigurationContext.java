package weblogic.deploy.service;

import java.util.Map;

public interface ConfigurationContext {
   String PROPOSED_CONFIGURATION_ID = "PROPOSED_CONFIGURATION";
   String DESC_DIFF_BEAN_UPDATE_ID = "beanUpdateDescriptorDiffId";
   String EXT_DESC_DIFF_BEAN_UPDATE_ID = "externalDescritorDiffId";

   DeploymentRequest getDeploymentRequest();

   void addContextComponent(String var1, Object var2);

   Object getContextComponent(String var1);

   Map getContextComponents();
}
