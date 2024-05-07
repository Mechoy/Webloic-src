package weblogic.application.internal;

import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.ModuleFactory;
import weblogic.j2ee.descriptor.ModuleBean;

public final class AppClientModuleFactory implements ModuleFactory {
   public Module createModule(ModuleBean var1) throws ModuleException {
      String var2 = var1.getJava();
      return var2 != null ? new AppClientModule(var2) : null;
   }
}
