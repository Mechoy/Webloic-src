package weblogic.ejb.container.deployer;

import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.ModuleFactory;
import weblogic.j2ee.descriptor.ModuleBean;

public final class EJBModuleFactory implements ModuleFactory {
   public Module createModule(ModuleBean var1) throws ModuleException {
      return var1.getEjb() != null ? new EJBModule(var1.getEjb()) : null;
   }
}
