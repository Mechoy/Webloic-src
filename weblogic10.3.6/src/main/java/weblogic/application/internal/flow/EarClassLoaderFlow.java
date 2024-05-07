package weblogic.application.internal.flow;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import weblogic.application.AppClassLoaderManager;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationFileManager;
import weblogic.application.SplitDirectoryInfo;
import weblogic.application.internal.AppClassLoaderManagerImpl;
import weblogic.application.io.Ear;
import weblogic.application.io.JarCopyFilter;
import weblogic.application.utils.AppFileOverrideUtils;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.application.utils.PathUtils;
import weblogic.management.DeploymentException;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.spring.monitoring.instrumentation.SpringInstrumentationUtils;
import weblogic.utils.classloaders.GenericClassLoader;

public final class EarClassLoaderFlow extends BaseFlow {
   private static final AppClassLoaderManagerImpl appClassLoaderManager = (AppClassLoaderManagerImpl)AppClassLoaderManager.getAppClassLoaderManager();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String serverName;

   public EarClassLoaderFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      try {
         File var1 = PathUtils.getAppTempDir(serverName, ApplicationVersionUtils.replaceDelimiter(this.appCtx.getApplicationId(), '_'));
         Ear var2 = this.createEar(var1);
         this.appCtx.setEar(var2);
         AppFileOverrideUtils.addFinderIfRequired(this.appCtx.getAppDeploymentMBean(), this.appCtx.getAppClassLoader());
         this.appCtx.getAppClassLoader().addClassFinder(var2.getClassFinder());
         SpringInstrumentationUtils.addSpringInstrumentor(this.appCtx.getAppClassLoader());
      } catch (IOException var3) {
         throw new DeploymentException(var3);
      }
   }

   public void unprepare() throws DeploymentException {
      appClassLoaderManager.removeApplicationLoader(this.appCtx.getApplicationId());
      GenericClassLoader var1 = this.appCtx.getAppClassLoader();
      var1.close();
   }

   public void remove() {
      Ear var1 = this.appCtx.getEar();
      if (var1 != null) {
         var1.remove();
      }

   }

   private Ear createEar(File var1) throws IOException {
      File var2 = new File(this.appCtx.getStagingPath());
      if (!var2.exists()) {
         throw new IOException("Could not read application at " + var2.getAbsolutePath());
      } else if (!var2.isDirectory()) {
         return this.archivedEar(var1, var2);
      } else {
         File var3 = new File(var2, ".beabuild.txt");
         if (var3.exists()) {
            this.appCtx.setSplitDir();
            return this.splitDirectory(var1, var2, var3);
         } else {
            return this.explodedEAR(var1, var2);
         }
      }
   }

   private Ear archivedEar(File var1, File var2) throws IOException {
      this.appCtx.setApplicationPaths(new File[]{var1});
      this.appCtx.setApplicationFileManager(ApplicationFileManager.newInstance(var1));
      return new Ear(this.appCtx.getApplicationId(), var1, var2);
   }

   private Ear splitDirectory(File var1, File var2, File var3) throws IOException {
      SplitDirectoryInfo var4 = new SplitDirectoryInfo(var2, var3);
      this.appCtx.setApplicationPaths(var4.getRootDirectories());
      this.appCtx.setApplicationFileManager(ApplicationFileManager.newInstance(var4));
      this.appCtx.setSplitDirectoryInfo(var4);
      return new Ear(this.appCtx.getApplicationId(), var1, var4);
   }

   private Ear explodedEAR(File var1, File var2) throws IOException {
      this.appCtx.setApplicationPaths(new File[]{var2});
      this.appCtx.setApplicationFileManager(ApplicationFileManager.newInstance(var2));
      return this.appCtx.isInternalApp() ? new Ear(this.appCtx.getApplicationId(), var1, new File[]{var2}, JarCopyFilter.NOCOPY_FILTER) : new Ear(this.appCtx.getApplicationId(), var1, new File[]{var2});
   }

   static {
      serverName = ManagementService.getRuntimeAccess(kernelId).getServerName();
   }
}
