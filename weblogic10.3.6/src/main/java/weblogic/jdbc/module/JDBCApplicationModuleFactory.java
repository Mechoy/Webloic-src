package weblogic.jdbc.module;

import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.WebLogicApplicationModuleFactory;
import weblogic.j2ee.descriptor.wl.JDBCConnectionPoolBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;

public class JDBCApplicationModuleFactory implements WebLogicApplicationModuleFactory {
   public Module[] createModule(WeblogicApplicationBean var1) throws ModuleException {
      JDBCConnectionPoolBean[] var2 = var1.getJDBCConnectionPools();
      if (var2 == null) {
         return null;
      } else {
         Module[] var3 = new Module[var2.length];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4] = new JDBCModule(var2[var4], var2[var4].getDataSourceJNDIName());
         }

         return var3;
      }
   }
}
