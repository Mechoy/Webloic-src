package weblogic.application.internal.flow;

import java.io.File;
import weblogic.application.AppClassLoaderManager;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.SingleModuleFileManager;
import weblogic.application.internal.AppClassLoaderManagerImpl;
import weblogic.application.utils.AppFileOverrideUtils;
import weblogic.management.DeploymentException;
import weblogic.spring.monitoring.instrumentation.SpringInstrumentationUtils;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.GenericClassLoader;

public final class SingleModuleClassLoaderFlow extends BaseFlow {
   private static final AppClassLoaderManagerImpl appClassLoaderManager = (AppClassLoaderManagerImpl)AppClassLoaderManager.getAppClassLoaderManager();

   public SingleModuleClassLoaderFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      GenericClassLoader var1 = this.appCtx.getAppClassLoader();
      AppFileOverrideUtils.addFinderIfRequired(this.appCtx.getAppDeploymentMBean(), var1);
      var1.setAnnotation(new Annotation(this.appCtx.getBasicDeploymentMBean().getName()));
      File var2 = new File(this.appCtx.getStagingPath());
      this.appCtx.setApplicationPaths(new File[]{var2});
      this.appCtx.setApplicationFileManager(new SingleModuleFileManager(var2));
      SpringInstrumentationUtils.addSpringInstrumentor(var1);
   }

   public void unprepare() {
      appClassLoaderManager.removeApplicationLoader(this.appCtx.getApplicationId());
      GenericClassLoader var1 = this.appCtx.getAppClassLoader();
      var1.close();
   }
}
