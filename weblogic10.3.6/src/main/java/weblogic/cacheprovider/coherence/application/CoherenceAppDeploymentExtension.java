package weblogic.cacheprovider.coherence.application;

import weblogic.application.AppDeploymentExtension;
import weblogic.application.ApplicationContextInternal;
import weblogic.cacheprovider.CacheProviderLogger;
import weblogic.cacheprovider.coherence.CoherenceClusterManager;
import weblogic.j2ee.descriptor.wl.CoherenceClusterRefBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.management.DeploymentException;
import weblogic.utils.classloaders.GenericClassLoader;

public class CoherenceAppDeploymentExtension implements AppDeploymentExtension {
   public String getName() {
      return this.getClass().getName();
   }

   public void init(ApplicationContextInternal var1) {
   }

   public void prepare(ApplicationContextInternal var1) throws DeploymentException {
      if (var1.isEar()) {
         try {
            GenericClassLoader var2 = var1.getAppClassLoader();
            CoherenceClusterRefBean var3 = this.getCoherenceClusterRefBean(var1);
            CoherenceClusterManager var4 = CoherenceClusterManager.getInstance();
            boolean var5 = var4.isCoherenceAvailable(var2);
            String var6 = null;
            if (var3 != null) {
               var6 = var3.getCoherenceClusterName();
               if (!var5) {
                  throw new DeploymentException("Missing Coherence jars or WebLogic Coherence Integration jars");
               }
            }

            if (var5) {
               var4.registerApplicationRuntimeMBean(var2, var1.getRuntime());
               var4.configureClusterService(var2, var6);
               var4.addCacheConfiguration(var2);
            }

         } catch (DeploymentException var7) {
            throw var7;
         } catch (Exception var8) {
            throw new DeploymentException(var8.getMessage(), var8);
         }
      }
   }

   public void activate(ApplicationContextInternal var1) throws DeploymentException {
   }

   public void deactivate(ApplicationContextInternal var1) throws DeploymentException {
   }

   public void unprepare(ApplicationContextInternal var1) throws DeploymentException {
      if (var1.isEar()) {
         try {
            GenericClassLoader var2 = var1.getAppClassLoader();
            CoherenceClusterManager var3 = CoherenceClusterManager.getInstance();
            if (var3.isCoherenceAvailable(var2)) {
               var3.releaseCacheConfiguration(var2);
               var3.shutdownClusterService(var2);
               var3.unRegisterApplicationRuntimeMBean(var2, var1.getRuntime());
            }
         } catch (Exception var4) {
            CacheProviderLogger.logFailedToUnprepare(var1.getApplicationId(), var4);
         }

      }
   }

   private CoherenceClusterRefBean getCoherenceClusterRefBean(ApplicationContextInternal var1) {
      CoherenceClusterRefBean var2 = null;
      WeblogicApplicationBean var3 = var1.getWLApplicationDD();
      if (var3 != null) {
         var2 = var3.getCoherenceClusterRef();
      }

      return var2;
   }
}
