package weblogic.application;

import java.io.File;
import java.util.Iterator;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.BasicDeploymentMBean;

public abstract class DeploymentManager {
   private static final String IMPL_CLASS = "weblogic.application.internal.DeploymentManagerImpl";
   private static final DeploymentManager theOne;

   public static DeploymentManager getDeploymentManager() {
      return theOne;
   }

   public abstract Deployment createDeployment(BasicDeploymentMBean var1, File var2) throws DeploymentException;

   public abstract Deployment findDeployment(BasicDeploymentMBean var1);

   public abstract Deployment findDeployment(String var1);

   public abstract Deployment removeDeployment(BasicDeploymentMBean var1);

   public abstract Deployment removeDeployment(String var1);

   public abstract Iterator getDeployments();

   public abstract MBeanFactory getMBeanFactory();

   public abstract void addModuleListener(ModuleListener var1);

   public abstract void removeModuleListener(ModuleListener var1);

   public abstract Iterator getModuleListeners();

   static {
      try {
         theOne = (DeploymentManager)Class.forName("weblogic.application.internal.DeploymentManagerImpl").newInstance();
      } catch (Exception var1) {
         throw new AssertionError(var1);
      }
   }
}
