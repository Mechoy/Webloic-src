package weblogic.management.j2ee.internal;

import java.security.AccessController;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.QueryExp;
import weblogic.management.j2ee.J2EEServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class J2EEServerMBeanImpl extends J2EEManagedObjectMBeanImpl implements J2EEServerMBean {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String serverVersion;
   private static final String serverVendor = "Oracle";
   private static final String domain;
   private static String serverName;

   public J2EEServerMBeanImpl(String var1) {
      super(var1);
   }

   public String getserverVersion() {
      return serverVersion;
   }

   public String getserverVendor() {
      return "Oracle";
   }

   public String[] getjavaVMs() {
      try {
         String var1 = domain + ":j2eeType=" + "JVM" + "," + "J2EEServer" + "=\"" + this.getServerName() + "\",*";
         ObjectName var2 = new ObjectName(var1);
         return this.queryNames(var2);
      } catch (MalformedObjectNameException var3) {
         throw new AssertionError(" Malformed ObjectName" + var3);
      }
   }

   public String[] getdeployedObjects() {
      QueryExp var1 = new QueryExp() {
         public boolean apply(ObjectName var1) {
            String var2 = var1.getKeyProperty("j2eeType");
            if (var2.equals("J2EEApplication")) {
               return true;
            } else if (!var2.equals("WebModule") && !var2.equals("ResourceAdapterModule") && !var2.equals("EJBModule")) {
               return false;
            } else {
               return var1.getKeyProperty("J2EEApplication").equals("null");
            }
         }

         public void setMBeanServer(MBeanServer var1) {
         }
      };
      return this.queryNames(var1);
   }

   public String[] getresources() {
      QueryExp var1 = new QueryExp() {
         public boolean apply(ObjectName var1) {
            return JMOTypesHelper.resourceList.contains(var1.getKeyProperty("j2eeType"));
         }

         public void setMBeanServer(MBeanServer var1) {
         }
      };
      return this.queryNames(var1);
   }

   private String getServerName() {
      if (serverName != null) {
         return serverName;
      } else {
         try {
            ObjectName var1 = new ObjectName(this.name);
            serverName = var1.getKeyProperty("name");
         } catch (MalformedObjectNameException var2) {
            throw new AssertionError("Malformed ObjectName detected");
         }

         return serverName;
      }
   }

   static {
      serverVersion = ManagementService.getRuntimeAccess(kernelId).getServer().getServerVersion();
      domain = ManagementService.getRuntimeAccess(kernelId).getDomainName();
      serverName = null;
   }
}
