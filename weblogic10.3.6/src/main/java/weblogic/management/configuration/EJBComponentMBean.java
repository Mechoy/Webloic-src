package weblogic.management.configuration;

import weblogic.management.runtime.EJBComponentRuntimeMBean;

/** @deprecated */
public interface EJBComponentMBean extends ComponentMBean, EJBContainerMBean {
   EJBComponentRuntimeMBean getEJBComponentRuntime();

   void setEJBComponentRuntime(EJBComponentRuntimeMBean var1);
}
