package weblogic;

import weblogic.management.commandline.AdminMain;

public class Admin {
   public static void main(String[] var0) {
      try {
         useAdminMain(var0);
      } catch (Throwable var2) {
         if (isVerbose(var0)) {
            var2.printStackTrace();
         }

         System.exit(1);
      }

   }

   private static boolean isVerbose(String[] var0) {
      if (var0 == null) {
         return false;
      } else {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            if (var0[var1].equals("-verbose")) {
               return true;
            }
         }

         return false;
      }
   }

   private static void useAdminMain(String[] var0) throws Throwable {
      AdminMain.main(var0);
   }
}
