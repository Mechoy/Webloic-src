package weblogic.application.internal.flow;

import java.io.File;
import java.security.AccessController;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.PathUtils;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.FileUtils;

public final class DescriptorCacheDirFlow extends BaseFlow {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String serverName;

   public DescriptorCacheDirFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() {
      this.appCtx.setDescriptorCacheDir(PathUtils.generateDescriptorCacheDir(serverName, this.appCtx.getApplicationId(), this.appCtx.isInternalApp()));
   }

   public void remove() {
      File var1 = this.appCtx.getDescriptorCacheDir();
      if (var1 != null) {
         FileUtils.remove(var1);
      }

   }

   static {
      serverName = ManagementService.getRuntimeAccess(kernelId).getServerName();
   }
}
