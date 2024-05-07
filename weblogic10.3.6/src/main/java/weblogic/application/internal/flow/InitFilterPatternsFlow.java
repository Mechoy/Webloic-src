package weblogic.application.internal.flow;

import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.ClassLoaderUtils;
import weblogic.management.DeploymentException;

public class InitFilterPatternsFlow extends BaseFlow {
   public InitFilterPatternsFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      ClassLoaderUtils.initFilterPatterns(this.appCtx.getWLApplicationDD() != null ? this.appCtx.getWLApplicationDD().getPreferApplicationPackages() : null, this.appCtx.getWLApplicationDD() != null ? this.appCtx.getWLApplicationDD().getPreferApplicationResources() : null, this.appCtx.getAppClassLoader());
   }
}
