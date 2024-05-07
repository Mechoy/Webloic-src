package weblogic.ejb.container.interfaces;

import javax.ejb.EnterpriseBean;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.spi.WLDeploymentException;

public interface PoolIntf {
   EnterpriseBean getBean() throws InternalException;

   EnterpriseBean getBean(long var1) throws InternalException;

   void releaseBean(EnterpriseBean var1);

   void destroyBean(EnterpriseBean var1);

   void setMaxBeansInFreePool(int var1);

   void updateMaxBeansInFreePool(int var1);

   void updateIdleTimeoutSeconds(int var1);

   void setInitialBeansInFreePool(int var1);

   void cleanup();

   void createInitialBeans() throws WLDeploymentException;

   void reset();

   void reInitializePool();
}
