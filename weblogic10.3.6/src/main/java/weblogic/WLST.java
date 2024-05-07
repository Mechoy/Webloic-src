package weblogic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import weblogic.descriptor.DescriptorClassLoader;

public class WLST {
   public static void main(String[] var0) {
      ClassLoader var1 = DescriptorClassLoader.getClassLoader();
      Thread.currentThread().setContextClassLoader(var1);
      String var2 = System.getProperty("wlst.debug.init", "false");
      boolean var3 = false;
      if (var2.equals("true")) {
         var3 = true;
      }

      try {
         Class var4 = var1.loadClass("weblogic.management.scripting.WLST");
         Method var8 = var4.getMethod("main", String[].class);
         var8.invoke(var4, var0);
      } catch (InvocationTargetException var6) {
         Throwable var5 = var6.getTargetException();
         if (var5 != null) {
            System.err.println("Problem invoking WLST - " + var5);
            if (var3) {
               var5.printStackTrace();
            }
         } else {
            System.err.println("Problem invoking WLST - " + var6);
            if (var3) {
               var6.printStackTrace();
            }
         }

         System.exit(1);
      } catch (Throwable var7) {
         System.err.println("Problem invoking WLST - " + var7);
         if (var3) {
            var7.printStackTrace();
         }

         System.exit(1);
      }

   }
}
