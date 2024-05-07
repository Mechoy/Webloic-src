package weblogic.connector.deploy;

import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.ModuleFactory;
import weblogic.j2ee.descriptor.ModuleBean;

public class ConnectorModuleFactory implements ModuleFactory {
   public Module createModule(ModuleBean var1) throws ModuleException {
      return var1.getConnector() != null ? new ConnectorModule(var1.getConnector()) : null;
   }
}
