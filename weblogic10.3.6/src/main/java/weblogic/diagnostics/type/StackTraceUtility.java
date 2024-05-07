package weblogic.diagnostics.type;

import weblogic.utils.StackTraceUtils;

public class StackTraceUtility {
   public static int getMatchingFrames(Exception var0, String var1) {
      int var2 = 0;
      StackTraceElement[] var3 = var0.getStackTrace();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         StackTraceElement var5 = var3[var4];
         String var6 = var5.getClassName();
         if (!var6.startsWith(var1)) {
            break;
         }

         ++var2;
      }

      return var2;
   }

   public static String removeFrames(Exception var0, int var1) {
      if (var1 > 0) {
         StackTraceElement[] var2 = var0.getStackTrace();
         StackTraceElement[] var3 = new StackTraceElement[var2.length - var1];
         System.arraycopy(var2, var1, var3, 0, var3.length);
         var0.setStackTrace(var3);
      }

      return StackTraceUtils.throwable2StackTrace(var0);
   }
}
