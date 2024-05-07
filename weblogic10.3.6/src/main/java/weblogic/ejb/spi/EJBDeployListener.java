package weblogic.ejb.spi;

import javax.naming.Context;
import weblogic.application.ApplicationContext;
import weblogic.management.configuration.EJBComponentMBean;

public interface EJBDeployListener {
   void init(ApplicationContext var1, EJBComponentMBean var2) throws EJBDeploymentException;

   void prepare(DeploymentInfo var1, EjbDescriptorBean var2, ApplicationContext var3) throws EJBDeploymentException;

   void unprepare();

   void activate(EjbDescriptorBean var1, ClassLoader var2, Context var3) throws EJBDeploymentException;

   void deactivate();
}
