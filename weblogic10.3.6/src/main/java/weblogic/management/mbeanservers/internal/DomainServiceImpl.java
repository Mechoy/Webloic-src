package weblogic.management.mbeanservers.internal;

import java.security.AccessController;
import weblogic.management.mbeanservers.Service;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class DomainServiceImpl extends ServiceImpl {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public DomainServiceImpl(String var1, String var2, Service var3) {
      this(var1, var2, var3, true);
   }

   public DomainServiceImpl(String var1, String var2, Service var3, boolean var4) {
      super(var1, var2, var3);
      if (var4) {
         this.register();
      }

   }

   public void register() {
      ManagementService.getDomainAccess(kernelId).registerService(this);
   }

   public void unregister() {
      ManagementService.getDomainAccess(kernelId).unregisterService(this);
   }
}
