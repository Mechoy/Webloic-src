package weblogic.management.j2ee.internal;

import java.security.AccessController;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.management.j2ee.JDBCResourceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class JDBCResourceMBeanImpl extends J2EEResourceMBeanImpl implements JDBCResourceMBean {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String domain;
   private String serverName = null;

   public JDBCResourceMBeanImpl(String var1, String var2) {
      super(var1);
      this.serverName = var2;
   }

   public String[] getjdbcDataSources() {
      try {
         String var1 = domain + ":j2eeType=" + "JDBCDataSource" + "," + "J2EEServer" + "=" + this.serverName + ",*";
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
