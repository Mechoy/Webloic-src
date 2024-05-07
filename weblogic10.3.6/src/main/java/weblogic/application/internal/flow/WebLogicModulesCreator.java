package weblogic.application.internal.flow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import weblogic.application.ApplicationFactoryManager;
import weblogic.application.Module;
import weblogic.application.WebLogicApplicationModuleFactory;
import weblogic.application.WeblogicModuleFactory;
import weblogic.application.internal.FlowContext;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;
import weblogic.management.DeploymentException;

public class WebLogicModulesCreator implements ModulesCreator {
   private static final ApplicationFactoryManager afm = ApplicationFactoryManager.getApplicationFactoryManager();

   public Module[] create(FlowContext var1) throws DeploymentException {
      WeblogicApplicationBean var2 = var1.getWLApplicationDD();
      if (var2 == null) {
         return new Module[0];
      } else {
         ArrayList var3 = new ArrayList();
         this.createModules(var2.getModules(), var3);
         this.createOldStyleWLDDModules(var2, var3);
         return (Module[])var3.toArray(new Module[var3.size()]);
      }
   }

   private void createModules(WeblogicModuleBean[] var1, List<Module> var2) throws DeploymentException {
      if (var1 != null && var1.length != 0) {
         WeblogicModuleBean[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            WeblogicModuleBean var6 = var3[var5];
            Module var7 = this.createModuleFromFactories(var6);
            var2.add(var7);
         }

      }
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

   private void createOldStyleWLDDModules(WeblogicApplicationBean var1, List<Module> var2) throws DeploymentException {
      Iterator var3 = afm.getWLAppModuleFactories();

      while(var3.hasNext()) {
         WebLogicApplicationModuleFactory var4 = (WebLogicApplicationModuleFactory)var3.next();
         Module[] var5 = var4.createModule(var1);
         if (var5 != null && var5.length > 0) {
            var2.addAll(Arrays.asList(var5));
         }
      }

   }
}
