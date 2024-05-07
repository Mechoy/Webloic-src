package weblogic.servlet.internal;

import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.ModuleFactory;
import weblogic.j2ee.descriptor.ModuleBean;

public class WebAppModuleFactory implements ModuleFactory {
   public Module createModule(ModuleBean var1) throws ModuleException {
      if (var1.getWeb() != null) {
         String var2 = var1.getWeb().getContextRoot();
         if ("".equals(var2)) {
            var2 = var1.getWeb().getWebUri();
         }

         return new WebAppModule(var1.getWeb().getWebUri(), var2);
      } else {
         return null;
      }
   }
}
