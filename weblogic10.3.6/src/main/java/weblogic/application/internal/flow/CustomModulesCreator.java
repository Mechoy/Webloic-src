package weblogic.application.internal.flow;

import java.util.ArrayList;
import weblogic.application.Module;
import weblogic.application.internal.FlowContext;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.management.DeploymentException;

public class CustomModulesCreator implements ModulesCreator {
   public Module[] create(FlowContext var1) throws DeploymentException {
      WeblogicExtensionBean var2 = var1.getWLExtensionDD();
      if (var2 == null) {
         return new Module[0];
      } else {
         ArrayList var3 = new ArrayList();
         CustomModuleHelper.createCustomModules(var2.getCustomModules(), var3, var1.getCustomModuleFactories());
         return (Module[])var3.toArray(new Module[var3.size()]);
      }
   }
}
