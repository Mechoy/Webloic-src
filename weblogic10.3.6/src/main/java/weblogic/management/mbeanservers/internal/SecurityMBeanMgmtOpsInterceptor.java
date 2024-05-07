package weblogic.management.mbeanservers.internal;

import java.io.IOException;
import java.security.AccessController;
import javax.management.Descriptor;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.modelmbean.ModelMBeanInfo;
import weblogic.descriptor.DescriptorClassLoader;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.jmx.mbeanserver.WLSMBeanServerInterceptorBase;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class SecurityMBeanMgmtOpsInterceptor extends WLSMBeanServerInterceptorBase {
   private static final String REALM_MBEAN = "weblogic.management.security.RealmMBean";
   private static final String SECURITY_STORE_MBEAN = "weblogic.management.security.RDBMSSecurityStoreMBean";
   private static final String ULM_MBEAN = "weblogic.management.security.authentication.UserLockoutManagerMBean";
   private static final String PROVIDER_MBEAN = "weblogic.management.security.ProviderMBean";
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMX");
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final int EDIT_MBS = 1;
   public static final int DOMAIN_RUNTIME_MBS = 2;
   private int mbsType;

   public SecurityMBeanMgmtOpsInterceptor(int var1) {
      this.mbsType = var1;
   }

   public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
      if (!this.isInvokeAllowed(var1, var2)) {
         String var5 = ManagementTextTextFormatter.getInstance().getMgmtOperationsIllegal();
         if (this.mbsType == 2) {
            var5 = ManagementTextTextFormatter.getInstance().getMgmtOperationsIllegalDomainRuntime();
         }

         throw new MBeanException(new RuntimeException(var5), var5);
      } else {
         return super.invoke(var1, var2, var3, var4);
      }
   }

   private boolean isInvokeAllowed(ObjectName var1, String var2) throws InstanceNotFoundException, IOException, MBeanException, ReflectionException {
      try {
         if (this.mbsType == 2 && !ManagementService.getRuntimeAccess(kernelId).getServerRuntime().isRestartRequired()) {
            return true;
         } else {
            MBeanInfo var3 = super.getMBeanInfo(var1);
            if (!(var3 instanceof ModelMBeanInfo)) {
               return true;
            } else {
               ModelMBeanInfo var4 = (ModelMBeanInfo)var3;
               String var5 = var4.getClassName();
               Class var6 = DescriptorClassLoader.loadClass(var5);
               if (this.isSecurityMBean(var6)) {
                  if (var4.getOperation(var2) == null || var4.getOperation(var2).getImpact() == 0) {
                     return true;
                  }

                  Descriptor var7 = var4.getOperation(var2).getDescriptor();
                  Boolean var8 = (Boolean)var7.getFieldValue("com.bea.allowSecurityOperations");
                  if (var8 != null && var8) {
                     return true;
                  }

                  String var9 = (String)var7.getFieldValue("com.bea.collectionRole");
                  if (var9 != null) {
                     if (debug.isDebugEnabled()) {
                        debug.debug("This operation " + var2 + " is a Management operation " + "on MBean " + var1 + " and will be prevented.");
                     }

                     return false;
                  }
               }

               return true;
            }
         }
      } catch (IntrospectionException var10) {
         if (debug.isDebugEnabled()) {
            debug.debug("IntrospectionException thrown while checking the mgmt operations.", var10);
         }

         return true;
      } catch (ClassNotFoundException var11) {
         if (debug.isDebugEnabled()) {
            debug.debug("ClassNotFoundException thrown while checking the mgmt operations.", var11);
         }

         return true;
      }
   }

   private boolean isSecurityMBean(Class var1) throws ClassNotFoundException {
      if (!Class.forName("weblogic.management.security.RealmMBean").isAssignableFrom(var1) && !Class.forName("weblogic.management.security.ProviderMBean").isAssignableFrom(var1) && !Class.forName("weblogic.management.security.authentication.UserLockoutManagerMBean").isAssignableFrom(var1) && !Class.forName("weblogic.management.security.RDBMSSecurityStoreMBean").isAssignableFrom(var1)) {
         return false;
      } else {
         if (debug.isDebugEnabled()) {
            debug.debug("Invoking a management operation on a security mbean.");
         }

         return true;
      }
   }
}
