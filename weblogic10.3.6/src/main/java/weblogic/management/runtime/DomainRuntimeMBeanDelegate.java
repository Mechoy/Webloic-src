package weblogic.management.runtime;

import java.security.AccessController;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RegistrationManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class DomainRuntimeMBeanDelegate extends RuntimeMBeanDelegate {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public DomainRuntimeMBeanDelegate(String var1) throws ManagementException {
      super(var1, ManagementService.getDomainAccess(kernelId).getDomainRuntime());
   }

   public DomainRuntimeMBeanDelegate(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }

   public DomainRuntimeMBeanDelegate(String var1, boolean var2) throws ManagementException {
      super(var1, ManagementService.getDomainAccess(kernelId).getDomainRuntime(), var2);
   }

   public DomainRuntimeMBeanDelegate() throws ManagementException {
      this(ManagementService.getRuntimeAccess(kernelId).getDomainName());
   }

   public DomainRuntimeMBeanDelegate(String var1, RuntimeMBean var2, boolean var3) throws ManagementException {
      super(var1, var2, var3);
   }

   public DomainRuntimeMBeanDelegate(String var1, RuntimeMBean var2, boolean var3, String var4) throws ManagementException {
      super(var1, var2, var3, var4);
   }

   public RegistrationManager getRegistrationManager() {
      return (RegistrationManager)(this.parent != null ? this.parent.getRegistrationManager() : ManagementService.getDomainAccess(kernelId));
   }
}
