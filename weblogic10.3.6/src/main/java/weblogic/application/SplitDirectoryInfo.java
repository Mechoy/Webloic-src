package weblogic.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class SplitDirectoryInfo {
   private final SplitDirParser parser;

   public SplitDirectoryInfo(File var1, File var2) throws IOException {
      this.parser = new SplitDirParser(var1, var2);
      this.parser.load();
   }

   public File[] getRootDirectories() {
      return this.getSrcDir() != null && this.getSrcDir().equals(this.getDestDir()) ? new File[]{this.getSrcDir()} : new File[]{this.getSrcDir(), this.getDestDir()};
   }

   public File getSrcDir() {
      return this.parser.srcDir;
   }

   public File getDestDir() {
      return this.parser.destDir;
   }

   public Map getUriLinks() {
      return this.parser.uriLinks;
   }

   public Iterator getAppClasses() {
      return this.parser.appClasses.iterator();
   }

   public String[] getWebAppClasses(String var1) {
      List var2 = (List)this.parser.webClasses.get(var1);
      return var2 == null ? new String[0] : (String[])((String[])var2.toArray(new String[var2.size()]));
   }

   public String getExtraClasspath() {
      StringBuffer var1 = new StringBuffer();
      String var2 = "";
      Iterator var3 = this.parser.appClasses.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var1.append(var2);
         var2 = File.pathSeparator;
         var1.append(var4);
      }

      return var1.toString();
   }

   private static class SplitDirParser extends Properties {
      private final File destDir;
      private final File propsFile;
      private File srcDir;
      private String srcPath;
      private final Map uriLinks = new HashMap();
      private final List appClasses = new ArrayList();
      private final Map webClasses = new HashMap();

      SplitDirParser(File var1, File var2) {
         this.destDir = var1;
         this.propsFile = var2;
      }

      private File resolveSrcDir() throws IOException {
         if (this.srcPath == null) {
            return this.destDir;
         } else {
            File var1 = new File(this.srcPath);
            if (!var1.isAbsolute()) {
               var1 = new File(this.destDir, var1.getPath());
            }

            if (!var1.exists()) {
               throw new IOException("Unable to find the source directory: " + var1.getAbsolutePath() + ".  This directory is referenced in the file: " + this.propsFile.getAbsolutePath() + ".  A possible solution is to rebuild your application.");
            } else if (!var1.isDirectory()) {
               throw new IOException("The source directory: " + var1.getAbsolutePath() + " exists, but it is not a directory.  This directory is " + "referenced in the file: " + this.propsFile.getAbsolutePath());
            } else {
               return var1;
            }
         }
      }

      public Object put(Object var1, Object var2) {
         String var3 = (String)var1;
         String var4 = (String)var2;
         if ("bea.srcdir".equals(var1)) {
            this.srcPath = var4;
         } else if (!var4.startsWith("APP-INF/lib/") && !"APP-INF/classes".equals(var4)) {
            if (var4.indexOf("WEB-INF/classes") <= -1 && var4.indexOf("WEB-INF/lib/") <= -1) {
               Object var8 = (List)this.uriLinks.get(var2);
               if (var8 == null) {
                  var8 = new ArrayList();
               }

               ((List)var8).add(new File(var3));
               this.uriLinks.put(var2, var8);
            } else {
               int var5 = var4.replace(File.separatorChar, '/').indexOf(47);
               String var6 = var5 == -1 ? var4 : var4.substring(0, var5);
               Object var7 = (List)this.webClasses.get(var6);
               if (var7 == null) {
                  var7 = new ArrayList();
                  this.webClasses.put(var6, var7);
               }

               ((List)var7).add(var3);
            }
         } else {
            this.appClasses.add(var1);
         }

         return super.put(var1, var2);
      }

      public void load() throws IOException {
         FileInputStream var1 = null;

         try {
            var1 = new FileInputStream(this.propsFile);
            this.load(var1);
         } finally {
            if (var1 != null) {
               var1.close();
            }

         }

         this.srcDir = this.resolveSrcDir();
      }
   }
}
