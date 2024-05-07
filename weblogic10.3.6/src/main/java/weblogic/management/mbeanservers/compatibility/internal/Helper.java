package weblogic.management.mbeanservers.compatibility.internal;

import java.util.ArrayList;
import java.util.List;
import javax.management.MBeanParameterInfo;

public class Helper {
   public static Class[] getInterfaces(Class var0) {
      ArrayList var1 = new ArrayList();
      addInterfacesRecusively(var0, var1);
      Class[] var2 = new Class[var1.size()];
      return (Class[])((Class[])var1.toArray(var2));
   }

   public static boolean isAssignableFrom(Object[] var0, MBeanParameterInfo[] var1) throws ClassNotFoundException {
      if (var0 == null) {
         return var1.length == 0;
      } else if (var0.length != var1.length) {
         return false;
      } else {
         Class[] var2 = new Class[var0.length];
         Class[] var3 = new Class[var1.length];

         int var4;
         for(var4 = 0; var4 < var0.length; ++var4) {
            var2[var4] = var0[var4] == null ? null : var0[var4].getClass();
         }

         for(var4 = 0; var4 < var0.length; ++var4) {
            String var5 = var1[var4].getType();
            var3[var4] = var5 == null ? Void.class : TypesHelper.wrapClass(TypesHelper.findClass(var5));
         }

         for(var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4] != null && !var3[var4].isAssignableFrom(var2[var4])) {
               return false;
            }
         }

         return true;
      }
   }

   private static void addInterfacesRecusively(Class var0, List var1) {
      Class[] var2 = var0.getInterfaces();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (!var1.contains(var2[var3])) {
            var1.add(var2[var3]);
         }

         addInterfacesRecusively(var2[var3], var1);
      }

      Class var4 = var0.getSuperclass();
      if (var4 != null) {
         addInterfacesRecusively(var4, var1);
      }

   }
}
