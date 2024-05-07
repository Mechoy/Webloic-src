package weblogic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import weblogic.utils.ArrayUtils;

public class GetMessage {
   public static void main(String[] var0) {
      Object var2 = null;

      try {
         Class var1 = Class.forName("weblogic.i18ntools.CatInfo");

         try {
            Class[] var3 = new Class[]{String[].class};
            Method var4 = var1.getMethod("main", var3);

            try {
               ArrayList var5 = new ArrayList();
               ArrayUtils.addAll(var5, var0);
               var5.add("__name__");
               var5.add("weblogic.GetMessage");
               Object[] var6 = var5.toArray();
               String[] var7 = new String[var6.length];
               System.arraycopy(var6, 0, var7, 0, var6.length);
               Object[] var8 = new Object[]{var7};
               var4.invoke((Object)null, var8);
            } catch (IllegalArgumentException var17) {
               System.err.println(var17.toString());
               var17.printStackTrace();
            } catch (InvocationTargetException var18) {
               System.err.println(var18.toString());
            }
         } catch (NoSuchMethodException var19) {
            System.err.println(var19.toString());
         } catch (SecurityException var20) {
            System.err.println(var20.toString());
         }
      } catch (ClassNotFoundException var21) {
         var2 = var21;
      } catch (IllegalAccessException var22) {
         var2 = var22;
      } finally {
         if (var2 != null) {
            System.err.println(((Throwable)var2).toString());
         }

      }

   }
}
