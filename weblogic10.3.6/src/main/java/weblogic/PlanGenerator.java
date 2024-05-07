package weblogic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlanGenerator {
   public static void main(String[] var0) throws Throwable {
      Object var2 = null;

      try {
         Class var1 = Class.forName("weblogic.deploy.api.tools.PlanGenerator");

         try {
            Class[] var3 = new Class[]{String[].class};
            Method var4 = var1.getMethod("main", var3);

            try {
               Object[] var5 = new Object[]{var0};
               var4.invoke((Object)null, var5);
            } catch (IllegalArgumentException var14) {
               System.err.println(var14.toString());
            } catch (InvocationTargetException var15) {
               if (var15.getCause() != null) {
                  throw var15.getCause();
               }

               throw var15;
            }
         } catch (NoSuchMethodException var16) {
            System.err.println(var16.toString());
         } catch (SecurityException var17) {
            System.err.println(var17.toString());
         }
      } catch (ClassNotFoundException var18) {
         var2 = var18;
      } catch (IllegalAccessException var19) {
         var2 = var19;
      } finally {
         if (var2 != null) {
            System.err.println(((Throwable)var2).toString());
         }

      }

   }
}
