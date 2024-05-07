package weblogic.marathon.fs;

import java.io.File;

class DelTree implements Runnable {
   private File root;

   private void assertion(boolean var1) {
      if (!var1) {
         throw new Error("assertion failure");
      }
   }

   public DelTree(File var1) {
      this.root = var1;
   }

   public static void main(String[] var0) {
      (new DelTree(new File(var0[0].replace('/', File.separatorChar)))).run();
   }

   public void run() {
      if (this.root.exists()) {
         this.recurse(this.root);
      }
   }

   private void recurse(File var1) {
      this.assertion(var1.isDirectory());
      String[] var2 = var1.list();

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         File var4 = new File(var1, var2[var3]);
         if (var4.isDirectory()) {
            this.recurse(var4);
         } else if (!var4.delete()) {
            throw new Error("cannot delete: " + var4.getAbsolutePath());
         }
      }

      if (!var1.delete()) {
         throw new Error("cannot delete: " + var1.getAbsolutePath());
      }
   }
}
