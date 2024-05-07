package weblogic.marathon.fs;

import java.util.zip.ZipEntry;

class ZU {
   static void p(String var0) {
      System.err.println("[ZU]: " + var0);
   }

   static boolean isRootEntry(String var0) {
      int var1 = var0.length();
      int var2 = var0.indexOf(47);
      return var2 == -1 || var2 == var1 - 1;
   }

   static boolean isRootEntry(ZipEntry var0) {
      return isRootEntry(var0.getName());
   }

   static String fixPath(String var0) {
      int var1 = var0.length();
      return var1 != 0 && var0.charAt(var1 - 1) == '/' ? var0.substring(0, var1 - 1) : var0;
   }

   static boolean isDirectChild(String var0, String var1) {
      if (!var1.startsWith(var0)) {
         return false;
      } else if (var1.equals(var0)) {
         return false;
      } else if (var1.length() <= var0.length()) {
         return false;
      } else {
         int var2 = var0.length();
         String var3 = var1.substring(var2);
         return isRootEntry(var3);
      }
   }

   static boolean containsEntry(Entry var0, Entry var1) {
      if (var1 != null && var1 instanceof MemoryEntry) {
         return false;
      } else {
         Entry[] var2 = var0.list();

         for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            if (var1.getPath().equals(var2[var3].getPath())) {
               if (!var1.getClass().equals(var2[var3].getClass())) {
                  (new StringBuilder()).append(var1.getPath()).append(" old=").append(var2[var3].getClass().getName()).append(" new=").append(var1.getClass().getName()).toString();
               }

               return true;
            }
         }

         return false;
      }
   }
}
