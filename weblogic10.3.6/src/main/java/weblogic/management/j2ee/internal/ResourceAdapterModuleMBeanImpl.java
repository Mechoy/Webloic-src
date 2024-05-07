package weblogic.management.j2ee.internal;

import java.security.AccessController;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.management.j2ee.ResourceAdapterModuleMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class ResourceAdapterModuleMBeanImpl extends J2EEModuleMBeanImpl implements ResourceAdapterModuleMBean {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String domain;

   public ResourceAdapterModuleMBeanImpl(String var1, String var2, String var3, ApplicationInfo var4) {
      super(var1, var2, var3, var4);
   }

   public String[] getresourceAdapters() {
      String var1 = domain + ":j2eeType=" + "ResourceAdapter" + "," + "J2EEServer" + "=" + JMOTypesHelper.getKeyValue(this.name, "J2EEServer") + "," + "ResourceAdapterModule" + "=" + JMOTypesHelper.getKeyValue(this.name, "name") + ",*";

      try {
         ObjectName var2 = new ObjectName(var1);
         return this.queryNames(var2);
      } catch (MalformedObjectNameException var3) {
         throw new AssertionError(" Malformed ObjectName" + var3);
      }
   }

   static {
      domain = ManagementService.getRuntimeAccess(kernelId).getDomainName();
   }
}
