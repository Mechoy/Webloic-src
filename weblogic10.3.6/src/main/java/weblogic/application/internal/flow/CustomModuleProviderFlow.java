package weblogic.application.internal.flow;

import java.util.Map;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.Flow;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.management.DeploymentException;

public final class CustomModuleProviderFlow extends BaseFlow implements Flow {
   public CustomModuleProviderFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      WeblogicExtensionBean var1 = this.appCtx.getWLExtensionDD();
      Map var2 = CustomModuleHelper.initFactories(var1, this.appCtx.getAppClassLoader());
      if (var2 != null) {
         this.appCtx.setCustomModuleFactories(var2);
      }
   }
}
