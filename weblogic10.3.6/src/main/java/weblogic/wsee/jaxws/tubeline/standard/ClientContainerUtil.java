package weblogic.wsee.jaxws.tubeline.standard;

import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.j2ee.ComponentRuntimeMBeanImpl;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.wsee.util.StringUtil;

public class ClientContainerUtil {
   public static ComponentRuntimeMBeanImpl getContainingComponentRuntime() {
      ComponentRuntimeMBeanImpl var0 = null;
      String var1 = ApplicationAccess.getApplicationAccess().getModuleName(Thread.currentThread().getContextClassLoader());
      ApplicationContextInternal var2 = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
      if (!StringUtil.isEmpty(var1) && var2 != null) {
         String var3 = "/" + var1;
         if (var3.endsWith(".war") || var3.endsWith(".jar")) {
            var3 = var3.substring(0, var3.length() - 4);
         }

         J2EEApplicationRuntimeMBeanImpl var4 = var2.getRuntime();
         ComponentRuntimeMBean[] var5 = var4.getComponentRuntimes();
         ComponentRuntimeMBean[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            ComponentRuntimeMBean var9 = var6[var8];
            if (var9.getModuleId().equals(var3)) {
               var0 = (ComponentRuntimeMBeanImpl)var9;
               break;
            }

            if (var9 instanceof WebAppComponentRuntimeMBean && ((WebAppComponentRuntimeMBean)var9).getModuleURI().equals(var1)) {
               var0 = (ComponentRuntimeMBeanImpl)var9;
               break;
            }
         }

         if (var0 == null && var5.length == 1) {
            var0 = (ComponentRuntimeMBeanImpl)var5[0];
         }
      }

      return var0;
   }

   public static ComponentRuntimeMBeanImpl getContainingComponentRuntimeByModuleName(String var0) {
      if (StringUtil.isEmpty(var0)) {
         var0 = ApplicationAccess.getApplicationAccess().getCurrentModuleName();
      }

      if (StringUtil.isEmpty(var0)) {
         var0 = ApplicationAccess.getApplicationAccess().getModuleName(Thread.currentThread().getContextClassLoader());
      }

      ApplicationRuntimeMBean var1 = getContainingApplicationRuntime();
      ComponentRuntimeMBeanImpl var2 = null;
      if (var1 != null) {
         ComponentRuntimeMBean[] var3 = var1.getComponentRuntimes();
         ComponentRuntimeMBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            ComponentRuntimeMBean var7 = var4[var6];
            if (var7.getModuleId().equals(var0)) {
               var2 = (ComponentRuntimeMBeanImpl)var7;
               break;
            }
         }
      }

      if (var2 == null) {
         var2 = getContainingComponentRuntime();
      }

      return var2;
   }

   public static ApplicationRuntimeMBean getContainingApplicationRuntime() {
      ClassLoader var0 = Thread.currentThread().getContextClassLoader();
      if (var0 instanceof GenericClassLoader) {
         ApplicationContextInternal var1 = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
         if (var1 == null) {
            return null;
         } else {
            J2EEApplicationRuntimeMBeanImpl var2 = var1.getRuntime();
            return var2;
         }
      } else {
         return null;
      }
   }

   public static String getContainingModuleName() {
      String var0 = ApplicationAccess.getApplicationAccess().getCurrentModuleName();
      if (StringUtil.isEmpty(var0)) {
         var0 = ApplicationAccess.getApplicationAccess().getModuleName(Thread.currentThread().getContextClassLoader());
      }

      return var0;
   }
}
