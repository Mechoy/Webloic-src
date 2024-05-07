package weblogic.marathon.fs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.application.SplitDirectoryInfo;

public final class SplitDirectoryFS extends FS {
   private final File srcDir;
   private final File outDir;
   private final FS srcFS;
   private final FS outFS;
   private Map links;

   protected SplitDirectoryFS(File var1, FS var2, String var3) throws IOException {
      super(var1, var2, var3);
      this.outDir = var1;
      File var4 = new File(var1, ".beabuild.txt");
      SplitDirectoryInfo var5 = new SplitDirectoryInfo(var1, var4);
      this.srcDir = var5.getSrcDir();
      this.srcFS = new StdFS(this.srcDir, var2, var3);
      this.outFS = new StdFS(var1, var2, var3);
      this.links = var5.getUriLinks();
   }

   private SplitDirectoryFS(File var1, File var2, FS var3, String var4) throws IOException {
      super(var2, var3, var4);
      this.srcDir = var1;
      this.outDir = var2;
      this.srcFS = new StdFS(var1, var3, var4);
      this.outFS = new StdFS(var2, var3, var4);
   }

   public URL getURL(String var1) throws IOException {
      String var2 = var1.replace('/', File.separatorChar);
      if (this.srcFS.exists(var2)) {
         return (new File(this.srcDir, var2)).toURL();
      } else {
         return this.outFS.exists(var2) ? (new File(this.outDir, var2)).toURL() : null;
      }
   }

   public Entry getEntry(String var1) throws IOException {
      if (this.srcFS.exists(var1)) {
         return this.srcFS.getEntry(var1);
      } else if (this.outFS.exists(var1)) {
         return this.outFS.getEntry(var1);
      } else {
         throw new FileNotFoundException(var1);
      }
   }

   public boolean exists(String var1) {
      return this.srcFS.exists(var1) || this.outFS.exists(var1);
   }

   public FS mountNested(String var1) throws IOException {
      File var2 = this.srcDir;
      File var3 = this.outDir;
      String var4 = var1.replace('/', File.separatorChar);
      if (!this.exists(var4)) {
         String var5 = this.getLinkedUri(var4);
         if (var5 != null) {
            return new SplitDirectoryFS(new File(var4), new File(var4), this, var5);
         } else {
            throw new FileNotFoundException(var4);
         }
      } else {
         return new SplitDirectoryFS(new File(var2, var4), new File(var3, var4), this, var1);
      }
   }

   private String getLinkedUri(String var1) {
      if (this.links == null) {
         return null;
      } else {
         File var2 = new File(var1);
         if (!var2.exists()) {
            return null;
         } else {
            Iterator var3 = this.links.keySet().iterator();

            String var4;
            List var5;
            do {
               if (!var3.hasNext()) {
                  return null;
               }

               var4 = (String)var3.next();
               var5 = (List)this.links.get(var4);
            } while(var5 == null || !var5.contains(var2));

            return var4;
         }
      }
   }

   public void put(String var1, byte[] var2) throws IOException {
      if (this.srcFS.exists(var1)) {
         this.srcFS.put(var1, var2);
      } else if (this.outFS.exists(var1)) {
         this.outFS.put(var1, var2);
      } else {
         this.srcFS.put(var1, var2);
      }

   }

   public Entry getRootEntry() throws IOException {
      return this.outFS.getRootEntry();
   }

   public void save() throws IOException {
      this.srcFS.save();
      this.outFS.save();
   }

   public void close() throws IOException {
      IOException var1 = null;

      try {
         this.srcFS.close();
      } catch (IOException var4) {
         var1 = var4;
      }

      try {
         this.outFS.close();
      } catch (IOException var3) {
         var1 = var3;
      }

      if (var1 != null) {
         throw var1;
      }
   }
}
