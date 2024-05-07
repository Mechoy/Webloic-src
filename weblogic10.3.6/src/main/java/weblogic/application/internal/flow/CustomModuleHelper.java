package weblogic.application.internal.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weblogic.application.CustomModuleFactory;
import weblogic.application.Module;
import weblogic.application.internal.CustomModuleContextImpl;
import weblogic.j2ee.descriptor.wl.CustomModuleBean;
import weblogic.j2ee.descriptor.wl.ModuleProviderBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.management.DeploymentException;
import weblogic.utils.classloaders.GenericClassLoader;

public final class CustomModuleHelper {
   public static Map<String, CustomModuleFactory> initFactories(WeblogicExtensionBean var0, GenericClassLoader var1) throws DeploymentException {
      return initFactories(var0, var1, (String)null, (String)null);
   }

   public static Map<String, CustomModuleFactory> initFactories(WeblogicExtensionBean var0, GenericClassLoader var1, String var2, String var3) throws DeploymentException {
      if (var0 == null) {
         return null;
      } else {
         ModuleProviderBean[] var4 = var0.getModuleProviders();
         if (var4 != null && var4.length != 0) {
            HashMap var5 = new HashMap(var4.length);

            for(int var6 = 0; var6 < var4.length; ++var6) {
               CustomModuleFactory var7 = loadModuleFactory(var4[var6].getModuleFactoryClassName(), var1);
               var7.init(new CustomModuleContextImpl(var4[var6], var2, var3));
               var5.put(var4[var6].getName(), var7);
            }

            return var5;
         } else {
            return null;
         }
      }
   }

   private static CustomModuleFactory loadModuleFactory(String var0, GenericClassLoader var1) throws DeploymentException {
      try {
         Class var2 = Class.forName(var0, false, var1);
         if (!CustomModuleFactory.class.isAssignableFrom(var2)) {
            throw new DeploymentException("Your module-provider's module-factory-class " + var0 + " does not implement " + "weblogic.application.CustomModuleFactory");
         } else {
            return (CustomModuleFactory)var2.newInstance();
         }
      } catch (ClassNotFoundException var3) {
         throw new DeploymentException("Unable to load your custom module provider's module-factory-class " + var0);
      } catch (InstantiationException var4) {
         throw new DeploymentException(var4);
      } catch (IllegalAccessException var5) {
         throw new DeploymentException(var5);
      }
   }

   public static Module[] createScopedCustomModules(Module var0, String var1, WeblogicExtensionBean var2, GenericClassLoader var3) throws DeploymentException {
      Map var4 = initFactories(var2, var3, var0.getId(), var1);
      ArrayList var5 = new ArrayList();
      createCustomModules(var2.getCustomModules(), var5, var4);
      return (Module[])((Module[])var5.toArray(new Module[var5.size()]));
   }

   public static void createCustomModules(CustomModuleBean[] var0, List<Module> var1, Map<String, CustomModuleFactory> var2) throws DeploymentException {
      if (var0 != null && var0.length != 0) {
         for(int var3 = 0; var3 < var0.length; ++var3) {
            CustomModuleFactory var4 = (CustomModuleFactory)var2.get(var0[var3].getProviderName());
            if (var4 == null) {
               throw new DeploymentException("The custom module with the uri " + var0[var3].getUri() + " specified a provider-name of " + var0[var3].getProviderName() + ". However, there was no module-provider " + "with this name in your weblogic-extension.xml.");
            }

            Module var5 = var4.createModule(var0[var3]);
            var1.add(var5);
         }

      }
   }
}
