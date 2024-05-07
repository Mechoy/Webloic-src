package weblogic.marathon.fs;

import java.io.File;
import java.io.IOException;
import java.net.URL;

class NestedZipFS extends FS {
   ZipFS parentFS;
   String path;
   Entry root;

   NestedZipFS(File var1, ZipFS var2, String var3, Entry var4) {
      super(var1, var2, var3);
      this.path = var3;
      this.parentFS = var2;
      this.root = new NestedZipEntry(var4, var3);
      if (var3.endsWith("/")) {
         this.path = var3.substring(0, var3.length() - 1);
      }

   }

   static String constructDelegatePath(String var0, String var1) {
      return var1.startsWith("/") ? var0 + var1 : var0 + "/" + var1;
   }

   public URL getURL(String var1) throws IOException {
      String var2 = constructDelegatePath(this.path, var1);
      return this.parentFS.getURL(var2);
   }

   public Entry getEntry(String var1) throws IOException {
      String var2 = constructDelegatePath(this.path, var1);
      return this.parentFS.getEntry(var2);
   }

   public boolean exists(String var1) {
      String var2 = constructDelegatePath(this.path, var1);
      return this.parentFS.exists(var2);
   }

   public FS mountNested(String var1) throws IOException {
      if (!this.exists(var1)) {
         throw new IOException("no such path: \"" + var1 + "\"");
      } else {
         String var2 = constructDelegatePath(this.path, var1);
         return this.parentFS.mountNested(var2);
      }
   }

   public void put(String var1, byte[] var2) throws IOException {
      String var3 = constructDelegatePath(this.path, var1);
      this.parentFS.put(var3, var2);
   }

   public Entry getRootEntry() throws IOException {
      return this.root;
   }

   public void save() throws IOException {
      if (!this.parentFS.inSave) {
         this.parentFS.save();
      }

   }

   public void close() throws IOException {
      this.parentFS.close();
   }
}
