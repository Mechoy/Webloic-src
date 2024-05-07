package weblogic.application.internal.flow;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Set;
import weblogic.application.AppClassLoaderManager;
import weblogic.application.Module;
import weblogic.application.ModuleManager;
import weblogic.application.ModuleNotFoundException;
import weblogic.application.internal.AppClassLoaderManagerImpl;
import weblogic.application.internal.Flow;
import weblogic.application.internal.FlowContext;
import weblogic.application.utils.EarUtils;
import weblogic.deploy.container.DeploymentContext;
import weblogic.kernel.Kernel;
import weblogic.management.DeploymentException;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class TailModuleRedeployFlow extends BaseFlow implements Flow {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean PRODUCTION_MODE_ENABLED;
   private final ModuleManager moduleManager;

   public TailModuleRedeployFlow(FlowContext var1) {
      super(var1);
      this.moduleManager = var1.getModuleManager();
   }

   public void start(String[] var1) throws DeploymentException {
      Module[] var2 = this.appCtx.getStartingModules();
      this.appCtx.setStartingModules(new Module[0]);
      Module[] var3 = this.appCtx.getApplicationModules();
      Module[] var4 = new Module[var3.length + var2.length];
      System.arraycopy(var3, 0, var4, 0, var3.length);
      System.arraycopy(var2, 0, var4, var3.length, var2.length);
      this.appCtx.setApplicationModules(var4);
   }

   public void stop(String[] var1) throws DeploymentException {
      Module[] var2 = this.appCtx.getApplicationModules();
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         for(int var5 = 0; var5 < var1.length; ++var5) {
            if (var2[var4].getId().equals(var1[var5])) {
               var3.add(var2[var4]);
               break;
            }
         }
      }

      this.appCtx.setStoppingModules((Module[])((Module[])var3.toArray(new Module[var3.size()])));
   }

   public void validateRedeploy(DeploymentContext var1) throws DeploymentException {
      String[] var2 = var1.getUpdatedResourceURIs();
      if (!var1.isAppStaged()) {
         this.validateModuleUris(var2);
      }

      this.validateClassLoaderStructure(var2);
   }

   private String[] getOldUris(String[] var1) {
      ModuleManager var2 = this.appCtx.getModuleManager();
      ArrayList var3 = new ArrayList(Arrays.asList(var1));
      ListIterator var4 = var3.listIterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if (var2.isNewUri(var5)) {
            var4.remove();
         }
      }

      return (String[])((String[])var3.toArray(new String[0]));
   }

   private void validateModuleUris(String[] var1) throws ModuleNotFoundException {
      ModuleManager var2 = this.appCtx.getModuleManager();
      if (!var2.validateModuleIds(var1)) {
         var1 = EarUtils.toModuleIds(this.appCtx, var1);
         if (!var2.validateModuleIds(var1)) {
            throw new ModuleNotFoundException(var2.getInvalidModuleIds(var1));
         }
      }

   }

   private void validateClassLoaderStructure(String[] var1) throws DeploymentException {
      String[] var2 = this.getOldUris(var1);
      AppClassLoaderManagerImpl var3 = (AppClassLoaderManagerImpl)AppClassLoaderManager.getAppClassLoaderManager();
      if (!PRODUCTION_MODE_ENABLED) {
         Set var4 = var3.updatePartialDeploySet(this.appCtx, var2);
         if (var4.size() > var2.length) {
            var4.addAll(Arrays.asList(var1));
            this.appCtx.setPartialRedeployURIs((String[])((String[])var4.toArray(new String[var4.size()])));
         }
      } else {
         var3.checkPartialRedeploy(this.appCtx, var2);
      }

   }

   static {
      PRODUCTION_MODE_ENABLED = !Kernel.isServer() || ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().isProductionModeEnabled();
   }
}
