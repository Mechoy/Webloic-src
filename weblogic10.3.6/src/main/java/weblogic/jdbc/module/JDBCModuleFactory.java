package weblogic.jdbc.module;

import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.WeblogicModuleFactory;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;

public class JDBCModuleFactory implements WeblogicModuleFactory {
   public Module createModule(WeblogicModuleBean var1) throws ModuleException {
      return "JDBC".equals(var1.getType()) ? new JDBCModule(var1) : null;
   }
}
