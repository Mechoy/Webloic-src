package weblogic.application.internal.flow;

import java.util.HashMap;
import weblogic.application.ApplicationContextInternal;
import weblogic.j2ee.descriptor.wl.ApplicationParamBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.management.DeploymentException;

public final class ApplicationParamFlow extends BaseFlow {
   public ApplicationParamFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      WeblogicApplicationBean var1 = this.appCtx.getWLApplicationDD();
      if (var1 != null) {
         ApplicationParamBean[] var2 = var1.getApplicationParams();
         if (var2 != null && var2.length != 0) {
            HashMap var3 = new HashMap(var2.length);

            for(int var4 = 0; var4 < var2.length; ++var4) {
               var3.put(var2[var4].getParamName(), var2[var4].getParamValue());
            }

            this.appCtx.setApplicationParameters(var3);
         }
      }
   }
}
