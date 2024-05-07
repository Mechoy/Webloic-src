package weblogic.ejb.container.interfaces;

import javax.naming.Name;
import weblogic.ejb.spi.WLDeploymentException;

public interface BaseEJBHomeIntf extends weblogic.ejb.spi.BaseEJBHomeIntf {
   void setup(BeanInfo var1, BaseEJBHomeIntf var2, BeanManager var3) throws WLDeploymentException;

   void pushEnvironment();

   void popEnvironment();

   BeanInfo getBeanInfo();

   void setBeanInfo(BeanInfo var1);

   DeploymentInfo getDeploymentInfo();

   void setDeploymentInfo(DeploymentInfo var1);

   Name getJNDIName();

   String getIsIdenticalKey();

   boolean usesBeanManagedTx();

   BeanManager getBeanManager();

   boolean isDeployed();

   void setIsDeployed(boolean var1);

   void undeploy();

   boolean isCallerInRole(String var1);

   String getDisplayName();
}
