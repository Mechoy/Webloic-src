package weblogic.xml.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Tools {
   static Debug.DebugFacility dbg = null;

   public static Debug.DebugFacility getDebug() {
      return dbg;
   }

   public static boolean isSubclass(Class var0, Class var1) {
      if (var0.equals(var1)) {
         return true;
      } else {
         Class var2 = var0.getSuperclass();
         return var2 == null ? false : isSubclass(var2, var1);
      }
   }

   public static boolean isSubtype(Class var0, Class var1) {
      if (var0.equals(var1)) {
         return true;
      } else {
         Class var2 = var0.getSuperclass();
         if (var2 != null && isSubtype(var2, var1)) {
            return true;
         } else {
            Class[] var3 = var0.getInterfaces();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (isSubtype(var3[var4], var1)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public static boolean isInstance(Object var0, Class var1) {
      if (var0 == null) {
         return true;
      } else {
         Class var2 = var0.getClass();
         return isSubtype(var2, var1);
      }
   }

   public static boolean isInstanceProper(Object var0, Class var1) {
      if (var0 == null) {
         return true;
      } else {
         Class var2 = var0.getClass();
         return isSubclass(var2, var1);
      }
   }

   private static void dumpClass(Class var0) {
      System.out.println("  Members from " + var0.toString() + ":");
      Constructor[] var1 = var0.getDeclaredConstructors();
      int var2 = var1.length;
      int var3;
      if (var2 != 0) {
         System.out.println("    Constructors:");

         for(var3 = 0; var3 < var2; ++var3) {
            Constructor var4 = var1[var3];
            System.out.println("      " + var4.toString());
         }
      }

      Field[] var5 = var0.getDeclaredFields();
      var2 = var5.length;
      if (var2 != 0) {
         System.out.println("    Fields:");

         for(var3 = 0; var3 < var2; ++var3) {
            Field var8 = var5[var3];
            System.out.println("      " + var8.toString());
         }
      }

      Method[] var6 = var0.getDeclaredMethods();
      var2 = var6.length;
      if (var2 != 0) {
         System.out.println("    Methods:");

         for(var3 = 0; var3 < var2; ++var3) {
            Method var9 = var6[var3];
            System.out.println("      " + var9.toString());
         }
      }

      Class var7 = var0.getSuperclass();
      if (var7 != null) {
         dumpClassI(var7);
      }

   }

   public static void dumpClassI(Class var0) {
      System.out.println("Members for " + var0.toString());
      dumpClassI(var0);
      System.out.println();
   }

   public static String getEntityDescriptor(String var0, String var1) {
      return getEntityDescriptor(var0, var1, (String)null);
   }

   public static String getEntityDescriptor(String var0, String var1, String var2) {
      String var3 = "Public ID = \"" + var0 + "\", System ID = \"" + var1 + "\"";
      if (var2 != null && var2.length() > 0) {
         var3 = var3 + ", Root tag = \"" + var2 + "\"";
      }

      return var3;
   }

   static {
      Debug.DebugSpec var0 = Debug.getDebugSpec();
      var0.name = "xml.utils";
      dbg = Debug.makeDebugFacility(var0);
   }

   public interface ILinkableException {
      Exception getPrevious();
   }
}
