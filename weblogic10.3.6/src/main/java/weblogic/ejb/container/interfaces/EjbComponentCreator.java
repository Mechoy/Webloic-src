package weblogic.ejb.container.interfaces;

import weblogic.management.DeploymentException;

public interface EjbComponentCreator {
   void initialize(DeploymentInfo var1, ClassLoader var2) throws DeploymentException;

   Object getBean(String var1, Class var2, boolean var3) throws IllegalAccessException, InstantiationException;

   void invokePostConstruct(Object var1);

   Object assembleEJB3Proxy(Object var1, String var2);
}
