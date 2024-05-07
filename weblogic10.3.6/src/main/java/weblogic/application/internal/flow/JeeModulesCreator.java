package weblogic.application.internal.flow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.application.ApplicationFactoryManager;
import weblogic.application.Module;
import weblogic.application.ModuleFactory;
import weblogic.application.ParentModule;
import weblogic.application.internal.FlowContext;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.IOUtils;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.management.DeploymentException;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class JeeModulesCreator implements ModulesCreator {
   private static final ApplicationFactoryManager afm = ApplicationFactoryManager.getApplicationFactoryManager();

   public Module[] create(FlowContext var1) throws DeploymentException {
      ApplicationBean var2 = var1.getApplicationDD();
      if (var2 == null) {
         return new Module[0];
      } else {
         ArrayList var3 = new ArrayList();
         this.createModules(var1, var2.getModules(), var3);
         return (Module[])var3.toArray(new Module[var3.size()]);
      }
   }

   private void createModules(FlowContext var1, ModuleBean[] var2, List<Module> var3) throws DeploymentException {
      if (var2 != null && var2.length != 0) {
         ModuleBean[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            ModuleBean var7 = var4[var6];
            Module var8 = this.createModuleFromFactories(var1, var7);
            var3.add(var8);
         }

      }
   }

   private Module createModuleFromFactories(FlowContext var1, ModuleBean var2) throws DeploymentException {
      Iterator var3 = afm.getModuleFactories();

      Module var5;
      do {
         if (!var3.hasNext()) {
            throw new DeploymentException("Unable to create module " + EarUtils.getModuleURI(var2));
         }

         ModuleFactory var4 = (ModuleFactory)var3.next();
         var5 = var4.createModule(var2);
      } while(var5 == null);

      return this.createAndAddScopedCustomModules(var1, var5);
   }

   private Module createAndAddScopedCustomModules(FlowContext var1, Module var2) throws DeploymentException {
      if (!(var2 instanceof ParentModule)) {
         return var2;
      } else {
         String var3 = ((ParentModule)var2).getDescriptorURI();
         ModuleBean var4 = this.findStandardModuleDescriptor(var1, var2.getId());
         if (var4 == null) {
            return var2;
         } else {
            String var5 = EarUtils.reallyGetModuleURI(var4);
            VirtualJarFile var6 = null;

            Module var8;
            try {
               var6 = VirtualJarFactory.createVirtualJar(var1.getEar().getModuleRoots(var5));
               ScopedModuleDriver var7 = new ScopedModuleDriver(var2, var1, var5, var6, var3);
               return var7;
            } catch (IOException var12) {
               var8 = var2;
            } finally {
               IOUtils.forceClose(var6);
            }

            return var8;
         }
      }
   }

   private ModuleBean findStandardModuleDescriptor(FlowContext var1, String var2) {
      return this.findModuleDescriptor(var1.getApplicationDD().getModules(), var2);
   }

   private ModuleBean findModuleDescriptor(ModuleBean[] var1, String var2) {
      if (var1 == null) {
         return null;
      } else {
         ModuleBean[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ModuleBean var6 = var3[var5];
            if (EarUtils.getModuleURI(var6).equals(var2)) {
               return var6;
            }
         }

         return null;
      }
   }
}
