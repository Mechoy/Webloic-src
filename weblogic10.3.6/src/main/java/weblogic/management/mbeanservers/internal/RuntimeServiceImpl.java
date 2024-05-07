package weblogic.management.mbeanservers.internal;

import java.security.AccessController;
import weblogic.management.mbeanservers.Service;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class RuntimeServiceImpl extends ServiceImpl {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public RuntimeServiceImpl(String var1, String var2, Service var3) {
      this(var1, var2, var3, (String)null, true);
   }

   public RuntimeServiceImpl(String var1, String var2, Service var3, boolean var4) {
      this(var1, var2, var3, (String)null, true);
   }

   public RuntimeServiceImpl(String var1, String var2, Service var3, String var4, boolean var5) {
      super(var1, var2, var3, var4);
      if (var5) {
         this.register();
      }

   }

   public void register() {
      ManagementService.getRuntimeAccess(kernelId).registerService(this);
   }

   public void unregister() {
      ManagementService.getRuntimeAccess(kernelId).unregisterService(this);
   }
}
