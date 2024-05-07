package weblogic.management.j2ee.internal;

import java.security.AccessController;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.QueryExp;
import weblogic.management.j2ee.J2EEApplicationMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class J2EEApplicationMBeanImpl extends J2EEDeployedObjectMBeanImpl implements J2EEApplicationMBean {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String domain;

   public J2EEApplicationMBeanImpl(String var1, String var2, ApplicationInfo var3) {
      super(var1, var2, var3);
   }

   public String[] getmodules() {
      QueryExp var1 = new QueryExp() {
         public boolean apply(ObjectName var1) {
            return JMOTypesHelper.moduleList.contains(var1.getKeyProperty("j2eeType"));
         }

         public void setMBeanServer(MBeanServer var1) {
         }
      };
      return this.queryNames(var1);
   }

   public String getserver() {
      return this.serverName;
   }

   static {
      domain = ManagementService.getRuntimeAccess(kernelId).getDomainName();
   }
}
