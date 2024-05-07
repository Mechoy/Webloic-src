package weblogic.application.internal.flow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationFactoryManager;
import weblogic.application.Module;
import weblogic.application.ModuleFactory;
import weblogic.application.ModuleManager;
import weblogic.application.ModuleNotFoundException;
import weblogic.application.ParentModule;
import weblogic.application.WeblogicModuleFactory;
import weblogic.application.internal.AppDDHolder;
import weblogic.application.internal.Flow;
import weblogic.application.internal.FlowContext;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.IOUtils;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.container.DeploymentContext;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.WebBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class CreateModulesFlow extends ModuleFilterFlow implements Flow {
   private static final ApplicationFactoryManager afm = ApplicationFactoryManager.getApplicationFactoryManager();

   public CreateModulesFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   private boolean containsModule(String var1) {
      ModuleManager var2 = this.appCtx.getModuleManager();
      if (var2.findModuleWithId(var1) != null) {
         return true;
      } else {
         ApplicationBean var3 = this.appCtx.getApplicationDD();
         ModuleBean[] var4 = var3.getModules();
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               WebBean var6 = var4[var5].getWeb();
               if (var6 != null && var1.equals(var6.getWebUri())) {
                  return true;
               }
            }
         }

         WeblogicApplicationBean var8 = this.appCtx.getWLApplicationDD();
         if (var8 != null) {
            WeblogicModuleBean[] var9 = var8.getModules();
            if (var9 != null) {
               for(int var7 = 0; var7 < var9.length; ++var7) {
                  if (var1.equals(var9[var7].getName())) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   private void validateTargets() throws DeploymentException {
      SubDeploymentMBean[] var1 = this.appCtx.getBasicDeploymentMBean().getSubDeployments();
      if (var1 != null && var1.length != 0) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (!this.containsModule(var1[var2].getName())) {
               throw new DeploymentException("The application " + this.appCtx.getApplicationId() + " contains a SubDeploymentMBean with a name " + var1[var2].getName() + " however there is no module in the application with that" + " URI or context-root.");
            }
         }

      }
   }

   public void prepare() throws DeploymentException {
      this.appCtx.setApplicationModules(this.createModules(this.appCtx));
      this.validateTargets();
   }

   public void validateRedeploy(DeploymentContext var1) throws DeploymentException {
      AppDDHolder var2 = this.appCtx.getProposedPartialRedeployDDs();
      if (var2 == null) {
         throw new AssertionError("Could not process proposed application deployment descriptors");
      } else {
         this.appCtx.setAdditionalModuleUris(this.getModuleUriMappings(var2));
      }
   }

   private Map getModuleUriMappings(AppDDHolder var1) throws DeploymentException {
      Object var2 = Collections.EMPTY_MAP;
      if (var1.getApplicationBean() == null) {
         return (Map)var2;
      } else {
         ModuleBean[] var3 = var1.getApplicationBean().getModules();
         if (var3.length > 0) {
            var2 = new HashMap(var3.length);
         }

         for(int var4 = 0; var4 < var3.length; ++var4) {
            ((Map)var2).put(EarUtils.getModuleURI(var3[var4]), EarUtils.reallyGetModuleURI(var3[var4]));
         }

         if (var1.getWLApplicationBean() == null) {
            return (Map)var2;
         } else {
            WeblogicModuleBean[] var8 = var1.getWLApplicationBean().getModules();

            for(int var5 = 0; var5 < var8.length; ++var5) {
               String var6 = this.getModuleId(var8[var5]);
               String var7 = this.getModuleURI(var8[var5]);
               ((Map)var2).put(var6, var7);
            }

            return (Map)var2;
         }
      }
   }

   private Module[] createModules(FlowContext var1) throws DeploymentException {
      ArrayList var2 = new ArrayList();
      CustomModulesCreator var3 = new CustomModulesCreator();
      Module[] var4 = var3.create(var1);
      var2.addAll(Arrays.asList(var4));
      WebLogicModulesCreator var8 = new WebLogicModulesCreator();
      Module[] var5 = var8.create(var1);
      var2.addAll(Arrays.asList(var5));
      JeeModulesCreator var9 = new JeeModulesCreator();
      Module[] var6 = var9.create(var1);
      var2.addAll(Arrays.asList(var6));
      WebLogicExtensionModulesCreator var10 = new WebLogicExtensionModulesCreator();
      Module[] var7 = var10.create(var1);
      var2.addAll(Arrays.asList(var7));
      return this.createWrappedModules(var2);
   }

   private Module createModuleFromFactories(ModuleBean var1) throws DeploymentException {
      Iterator var2 = afm.getModuleFactories();

      Module var4;
      do {
         if (!var2.hasNext()) {
            throw new DeploymentException("Unable to create module " + EarUtils.getModuleURI(var1));
         }

         ModuleFactory var3 = (ModuleFactory)var2.next();
         var4 = var3.createModule(var1);
      } while(var4 == null);

      return this.createAndAddScopedCustomModules(var4);
   }

   private Module createModuleFromFactories(WeblogicModuleBean var1) throws DeploymentException {
      Iterator var2 = afm.getWeblogicModuleFactories();

      Module var4;
      do {
         if (!var2.hasNext()) {
            throw new DeploymentException("Unable to find module for " + var1.getClass().getName());
         }

         WeblogicModuleFactory var3 = (WeblogicModuleFactory)var2.next();
         var4 = var3.createModule(var1);
      } while(var4 == null);

      return var4;
   }

   public void start(String[] var1) throws DeploymentException {
      Module[] var2 = new Module[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         ModuleBean var4 = this.findStandardModuleDescriptor(var1[var3]);
         if (var4 != null) {
            var2[var3] = this.createModuleFromFactories(var4);
         } else {
            WeblogicModuleBean var5 = this.findWLSModuleDescriptor(var1[var3]);
            if (var5 == null) {
               throw new ModuleNotFoundException("Trying to start uri " + var1[var3] + " which does not currently exist in the application " + "and was not declared in the META-INF/application.xml or " + "META-INF/weblogic-application.xml.  If you were attempting " + "to redeploy a web module, please ensure you specified the " + "context-root rather than the web-uri.");
            }

            var2[var3] = this.createModuleFromFactories(var5);
         }
      }

      this.appCtx.setStartingModules(this.createWrappedModules(Arrays.asList(var2)));
   }

   private ModuleBean findStandardModuleDescriptor(String var1) {
      return this.findModuleDescriptor(this.appCtx.getApplicationDD().getModules(), var1);
   }

   private WeblogicModuleBean findWLSModuleDescriptor(String var1) {
      WeblogicApplicationBean var2 = this.appCtx.getWLApplicationDD();
      return var2 != null ? this.findModuleDescriptor(var2.getModules(), var1) : null;
   }

   private ModuleBean findModuleDescriptor(ModuleBean[] var1, String var2) {
      if (var1 == null) {
         return null;
      } else {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (EarUtils.getModuleURI(var1[var3]).equals(var2)) {
               return var1[var3];
            }
         }

         return null;
      }
   }

   private WeblogicModuleBean findModuleDescriptor(WeblogicModuleBean[] var1, String var2) {
      if (var1 == null) {
         return null;
      } else {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (this.getModuleId(var1[var3]).equals(var2)) {
               return var1[var3];
            }
         }

         return null;
      }
   }

   private String getModuleId(WeblogicModuleBean var1) {
      return WebLogicModuleType.MODULETYPE_JDBC.equalsIgnoreCase(var1.getType()) && var1.getName() != null ? var1.getName() : this.getModuleURI(var1);
   }

   private String getModuleURI(WeblogicModuleBean var1) {
      String var2 = var1.getPath();
      if (var2 == null) {
         throw new AssertionError("WeblogicModuleBean contains no module URI");
      } else {
         return var2;
      }
   }

   private Module createAndAddScopedCustomModules(Module var1) throws DeploymentException {
      if (!(var1 instanceof ParentModule)) {
         return var1;
      } else {
         String var2 = ((ParentModule)var1).getDescriptorURI();
         ModuleBean var3 = this.findStandardModuleDescriptor(var1.getId());
         if (var3 == null) {
            return var1;
         } else {
            String var4 = EarUtils.reallyGetModuleURI(var3);
            VirtualJarFile var5 = null;

            Module var7;
            try {
               var5 = VirtualJarFactory.createVirtualJar(this.appCtx.getEar().getModuleRoots(var4));
               ScopedModuleDriver var6 = new ScopedModuleDriver(var1, this.appCtx, var4, var5, var2);
               return var6;
            } catch (IOException var11) {
               var7 = var1;
            } finally {
               IOUtils.forceClose(var5);
            }

            return var7;
         }
      }
   }
}
