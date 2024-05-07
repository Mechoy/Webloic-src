package weblogic.marathon.fs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StdFS extends FS {
   private Map memEntries = new HashMap();

   protected StdFS(File var1, FS var2, String var3) {
      super(var1, var2, var3);
   }

   public void close() {
   }

   public Entry getEntry(String var1) throws IOException {
      var1 = var1.replace('/', File.separatorChar);
      Entry var2 = (Entry)this.memEntries.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         File var3 = new File(this.getRoot(), var1);
         if (!var3.exists()) {
            throw new FileNotFoundException(var3.getAbsolutePath());
         } else {
            return new FileEntry(var1, var3);
         }
      }
   }

   public URL getURL(String var1) throws IOException {
      if (!this.exists(var1)) {
         return null;
      } else {
         var1 = var1.replace('/', File.separatorChar);
         File var2 = new File(this.getRoot(), var1);
         return new URL("file:" + var2.getAbsolutePath());
      }
   }

   public boolean exists(String var1) {
      var1 = var1.replace('/', File.separatorChar);
      if (this.memEntries.get(var1) != null) {
         return true;
      } else {
         File var2 = new File(this.getRoot(), var1);
         return var2.exists();
      }
   }

   public FS mountNested(String var1) throws IOException {
      FS var2 = (FS)this.children.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         File var3 = new File(this.getRoot(), var1);
         return mountInteral(var3, this);
      }
   }

   public void save() throws IOException {
      Map var1 = this.memEntries;
      this.memEntries = new HashMap();
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         MemoryEntry var4 = (MemoryEntry)var1.get(var3);
         File var5 = new File(this.getRoot(), var4.getPath());
         var5.getParentFile().mkdirs();
         FileOutputStream var6 = new FileOutputStream(var5);

         try {
            var6.write(var4.data);
         } finally {
            var6.close();
         }
      }

   }

   public void put(String var1, byte[] var2) throws IOException {
      var1 = var1.replace('/', File.separatorChar);
      this.memEntries.put(var1, new MemoryEntry(var1, var2));
   }

   public Entry getRootEntry() throws IOException {
      return new FileEntry("", this.getRoot());
   }
}
