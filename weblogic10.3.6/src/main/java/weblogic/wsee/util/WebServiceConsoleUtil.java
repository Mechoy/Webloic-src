package weblogic.wsee.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import weblogic.jws.CallbackMethod;

public class WebServiceConsoleUtil {
   public static ArrayList<WebServiceMethod> getCallbackMethods(String var0) throws ClassNotFoundException {
      return getMethods(var0, CallbackMethod.class);
   }

   private static ArrayList<WebServiceMethod> getMethods(String var0, Class var1) throws ClassNotFoundException {
      Class var2 = Thread.currentThread().getContextClassLoader().loadClass(var0);
      Method[] var3 = var2.getMethods();
      ArrayList var4 = new ArrayList();
      Method[] var5 = var3;
      int var6 = var3.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Method var8 = var5[var7];
         if (var8.getAnnotation(var1) != null) {
            String var9 = var8.getName();
            ArrayList var10 = new ArrayList();
            Class[] var11 = var8.getParameterTypes();
            Class[] var12 = var11;
            int var13 = var11.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               Class var15 = var12[var14];
               var10.add(var15.getCanonicalName());
            }

            var4.add(new WebServiceMethod(var9, var10));
         }
      }

      return var4;
   }

   public static class WebServiceMethod {
      private String name;
      private ArrayList<String> paramTypes;

      public WebServiceMethod(String var1, ArrayList<String> var2) {
         this.name = var1;
         this.paramTypes = var2;
      }

      public String getName() {
         return this.name;
      }

      public ArrayList<String> getParamTypes() {
         return this.paramTypes;
      }
   }
}
