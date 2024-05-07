package weblogic.application.internal.flow;

import java.net.URL;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.cluster.singleton.SingletonService;
import weblogic.cluster.singleton.SingletonServicesManager;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.wl.SingletonServiceBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.work.InheritableThreadContext;

public final class SingletonServicesFlow extends BaseFlow {
   private SingletonServicesManager manager = SingletonServicesManager.getInstance();
   private SingletonServiceBean[] serviceBeans;
   private SingletonService[] services;
   private InheritableThreadContext context;

   public SingletonServicesFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      WeblogicApplicationBean var1 = this.appCtx.getWLApplicationDD();
      if (var1 != null) {
         this.serviceBeans = var1.getSingletonServices();
         if (this.serviceBeans != null && this.serviceBeans.length != 0) {
            this.services = new SingletonService[this.serviceBeans.length];

            for(int var2 = 0; var2 < this.serviceBeans.length; ++var2) {
               String var3 = this.serviceBeans[var2].getClassName();
               String var4 = this.serviceBeans[var2].getSingletonUri();
               GenericClassLoader var5 = this.appCtx.getAppClassLoader();
               if (var4 != null) {
                  this.addListenerJarToLoader(var5, var4);
               }

               this.services[var2] = SingletonServicesManager.constructSingletonService(var3, var5);
               this.context = InheritableThreadContext.getContext();
            }

         }
      }
   }

   public void activate() throws DeploymentException {
      if (this.serviceBeans != null) {
         String var1 = null;
         AppDeploymentMBean var2 = this.appCtx.getAppDeploymentMBean();
         if (var2 != null) {
            var1 = ApplicationVersionUtils.getVersionId(var2.getApplicationIdentifier());
         }

         for(int var3 = 0; var3 < this.serviceBeans.length; ++var3) {
            try {
               this.manager.addConfiguredService(SingletonServicesManager.Util.getAppscopedSingletonServiceName(this.serviceBeans[var3].getName(), var1), this.services[var3], this.context);
            } catch (IllegalArgumentException var5) {
               throw new DeploymentException(var5);
            }
         }

      }
   }

   public void deactivate() throws DeploymentException {
      if (this.serviceBeans != null) {
         String var1 = null;
         AppDeploymentMBean var2 = this.appCtx.getAppDeploymentMBean();
         if (var2 != null) {
            var1 = ApplicationVersionUtils.getVersionId(var2.getApplicationIdentifier());
         }

         for(int var3 = 0; var3 < this.serviceBeans.length; ++var3) {
            this.manager.remove(SingletonServicesManager.Util.getAppscopedSingletonServiceName(this.serviceBeans[var3].getName(), var1));
         }

      }
   }

   public void unprepare() throws DeploymentException {
   }

   private void addListenerJarToLoader(GenericClassLoader var1, String var2) throws DeploymentException {
      URL var3 = var1.getResource(this.appCtx.getApplicationId() + "#" + var2);
      if (var3 == null) {
         Loggable var4 = J2EELogger.logUnabletoFindSingletonJarLoggable(this.appCtx.getApplicationId(), var2);
         throw new DeploymentException(var4.getMessage());
      } else {
         var1.addClassFinder(new ClasspathClassFinder2(var3.getFile()));
      }
   }
}
