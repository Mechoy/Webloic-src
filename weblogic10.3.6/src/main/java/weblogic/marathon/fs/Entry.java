package weblogic.marathon.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class Entry {
   protected String path;

   public abstract Entry[] list();

   public abstract InputStream getInputStream() throws IOException;

   protected Entry(String var1) {
      var1 = var1.replace(File.separatorChar, '/');
      this.path = var1;
   }

   public String getPath() {
      return this.path;
   }

   public String getName() {
      String var1 = this.getPath();
      int var2 = var1.lastIndexOf(47);
      if (var2 == var1.length() - 1) {
         var2 = var1.lastIndexOf(47, var2 - 1);
      }

      return var2 >= 0 ? var1.substring(var2 + 1) : var1;
   }

   public abstract long getTime();
}
