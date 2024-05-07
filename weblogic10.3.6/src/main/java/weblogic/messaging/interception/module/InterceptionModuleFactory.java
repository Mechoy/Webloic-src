package weblogic.messaging.interception.module;

import javax.enterprise.deploy.shared.ModuleType;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.WeblogicModuleFactory;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;

public class InterceptionModuleFactory implements WeblogicModuleFactory {
   public Module createModule(WeblogicModuleBean var1) throws ModuleException {
      return "Interception".equals(var1.getType()) ? new InterceptionModule(var1.getPath()) : null;
   }

   public Module createModule(ModuleType var1) throws ModuleException {
      return null;
   }
}
