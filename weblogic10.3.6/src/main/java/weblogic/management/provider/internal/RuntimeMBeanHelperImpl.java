package weblogic.management.provider.internal;

import java.security.AccessController;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RegistrationManager;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanHelper;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

class RuntimeMBeanHelperImpl implements RuntimeMBeanHelper {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public String getServerName() {
      return ManagementService.getRuntimeAccess(kernelId).getServerName();
   }

   public RuntimeMBean getDefaultParent() {
      return ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
   }

   public RegistrationManager getRegistrationManager() {
      return ManagementService.getRuntimeAccess(kernelId);
   }

   public boolean isParentRequired(RuntimeMBean var1) {
      return !(var1 instanceof ServerRuntimeMBean) && !(var1 instanceof DomainRuntimeMBean);
   }

   public boolean isParentRequired(String var1) {
      return !var1.equals("ServerRuntime") && !var1.equals("DomainRuntime");
   }
}
