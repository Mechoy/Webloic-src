package weblogic.application.internal.flow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.application.ApplicationFactoryManager;
import weblogic.application.ExtensionModuleFactory;
import weblogic.application.Module;
import weblogic.application.internal.FlowContext;
import weblogic.management.DeploymentException;

public class WebLogicExtensionModulesCreator implements ModulesCreator {
   private static final ApplicationFactoryManager afm = ApplicationFactoryManager.getApplicationFactoryManager();

   public Module[] create(FlowContext var1) throws DeploymentException {
      ArrayList var2 = new ArrayList();
      this.createWebLogicExtensionModules(var1, var2);
      return (Module[])var2.toArray(new Module[var2.size()]);
   }

   private void createWebLogicExtensionModules(FlowContext var1, List<Module> var2) {
      Iterator var3 = afm.getWebLogicExtenstionModuleFactories();

      while(var3.hasNext()) {
         ExtensionModuleFactory var4 = (ExtensionModuleFactory)var3.next();
         Module var5 = var4.createModule(var1);
         if (var5 != null) {
            var2.add(var5);
         }
      }

   }
}
