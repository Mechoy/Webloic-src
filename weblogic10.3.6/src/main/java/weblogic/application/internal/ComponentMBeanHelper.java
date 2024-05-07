package weblogic.application.internal;

import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;

final class ComponentMBeanHelper {
   private ComponentMBeanHelper() {
   }

   static ComponentMBean findComponentMBeanByName(ApplicationMBean var0, String var1, Class var2) {
      return findComponentMBean(var0.getComponents(), var1, var2, false);
   }

   static ComponentMBean findComponentMBeanByURI(ComponentMBean[] var0, String var1, Class var2) {
      return findComponentMBean(var0, var1, var2, true);
   }

   static ComponentMBean findComponentMBeanByURI(ApplicationMBean var0, String var1, Class var2) {
      return var0 == null ? null : findComponentMBean(var0.getComponents(), var1, var2, true);
   }

   private static ComponentMBean findComponentMBean(ComponentMBean[] var0, String var1, Class var2, boolean var3) {
      if (var2 == null) {
         throw new IllegalArgumentException("type cannot be null");
      } else {
         if (var0 != null) {
            for(int var4 = 0; var4 < var0.length; ++var4) {
               String var5 = getName(var0[var4], var3);
               if (var1.equals(var5) && var2.isAssignableFrom(var0[var4].getClass())) {
                  return var0[var4];
               }
            }
         }

         return null;
      }
   }

   private static String getName(ComponentMBean var0, boolean var1) {
      return var1 ? var0.getURI() : var0.getName();
   }
}
