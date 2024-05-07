package weblogic.application.utils;

import com.bea.util.jam.JClass;
import com.bea.util.jam.JamService;
import com.bea.util.jam.JamServiceFactory;
import com.bea.util.jam.JamServiceParams;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.apache.openjpa.lib.meta.ClassAnnotationMetaDataFilter;
import org.apache.openjpa.lib.meta.MetaDataFilter;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.jars.VirtualJarFile;

public class AnnotationDetector {
   private final MetaDataFilter filter;
   private static final DebugCategory debug = Debug.getCategory("weblogic.application.annotationdetector.debug");
   private final Class[] annotations;

   public AnnotationDetector(Class[] var1) {
      this.annotations = var1;
      this.filter = new ClassAnnotationMetaDataFilter(var1);
   }

   public AnnotationDetector(Class var1) {
      this.annotations = new Class[]{var1};
      this.filter = new ClassAnnotationMetaDataFilter(var1);
   }

   public boolean isAnnotated(ZipFile var1) throws IOException {
      Enumeration var2 = var1 == null ? null : var1.entries();
      if (var2 == null) {
         return false;
      } else {
         Resource var3 = new Resource(var1);

         while(var2.hasMoreElements()) {
            ZipEntry var4 = (ZipEntry)var2.nextElement();
            if (!var4.isDirectory()) {
               if (debug.isEnabled()) {
                  Debug.say("Scanning annotations in archive " + var1.getName() + " entry " + var4.getName());
               }

               var3.setEntry(var4);
               if (this.filter.matches(var3)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean isAnnotated(VirtualJarFile var1) throws IOException {
      Iterator var2 = var1 == null ? null : var1.entries();
      return var2 == null ? false : this.isAnnotated(var1, var2);
   }

   public boolean isAnnotated(VirtualJarFile var1, Iterator var2) throws IOException {
      Resource var3 = new Resource(var1);

      do {
         if (!var2.hasNext()) {
            return false;
         }

         ZipEntry var4 = (ZipEntry)var2.next();
         if (debug.isEnabled()) {
            Debug.say("Scanning annotations in archive " + var1.getName() + " entry " + var4.getName());
         }

         var3.setEntry(var4);
      } while(!this.filter.matches(var3));

      return true;
   }

   public boolean isAnnotated(File var1) throws IOException {
      if (debug.isEnabled()) {
         Debug.say("Recursively scanning annotations in directory " + var1.getName());
      }

      return this.scan(var1, this.filter, new FileResource());
   }

   private boolean scan(File var1, MetaDataFilter var2, FileResource var3) throws IOException {
      if (debug.isEnabled()) {
         Debug.say("Scanning annotations in file " + var1.getName());
      }

      var3.setFile(var1);
      if (var2.matches(var3)) {
         return true;
      } else {
         File[] var4 = var1.listFiles();
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               if (this.scan(var4[var5], var2, var3)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean hasAnnotatedSources(File var1) throws IOException {
      JamServiceFactory var2 = JamServiceFactory.getInstance();
      JamServiceParams var3 = var2.createServiceParams();
      this.includeSources(var1, var3);
      JamService var4 = var2.createService(var3);
      JClass[] var5 = var4.getAllClasses();
      if (var5 == null) {
         return false;
      } else {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (this.hasAnnotation(var5[var6])) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean hasAnnotation(JClass var1) throws IOException {
      for(int var2 = 0; var2 < this.annotations.length; ++var2) {
         if (var1.getAnnotation(this.annotations[var2]) != null) {
            return true;
         }
      }

      return false;
   }

   private void includeSources(File var1, JamServiceParams var2) {
      File[] var3 = var1.listFiles();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (this.isJavaSource(var3[var4])) {
               var2.includeSourceFile(var3[var4]);
            } else {
               this.includeSources(var3[var4], var2);
            }
         }

      }
   }

   private boolean isJavaSource(File var1) {
      return var1.getName().endsWith(".java");
   }

   public boolean isAnnotated(ZipInputStream var1) throws IOException {
      Resource var2 = new Resource(var1);
      ZipEntry var3 = null;

      while((var3 = var1.getNextEntry()) != null) {
         try {
            if (!var3.isDirectory()) {
               var2.setEntry(var3);
               if (this.filter.matches(var2)) {
                  boolean var4 = true;
                  return var4;
               }
            }
         } finally {
            var1.closeEntry();
         }
      }

      return false;
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         System.out.println("Usage AnnotationScanner <file>");
         System.exit(-1);
      }

      AnnotationDetector var1 = new AnnotationDetector(new Class[]{Class.forName("javax.ejb.MessageDriven"), Class.forName("javax.ejb.Stateful"), Class.forName("javax.ejb.Stateless")});
      System.out.println("Do we have annotations in the module? " + var1.isAnnotated(new ZipFile(var0[0])));
   }

   public static class FileResource implements MetaDataFilter.Resource {
      private File _file = null;

      public void setFile(File var1) {
         this._file = var1;
      }

      public String getName() {
         return this._file.getName();
      }

      public byte[] getContent() throws IOException {
         long var1 = this._file.length();
         FileInputStream var3 = new FileInputStream(this._file);

         byte[] var17;
         try {
            byte[] var4;
            int var7;
            if (var1 > 0L) {
               var4 = new byte[(int)var1];
               int var18 = 0;
               var7 = (int)var1;

               int var16;
               while((var16 = var3.read(var4, var18, var7 - var18)) > 0) {
                  var18 += var16;
                  if (var18 == var7) {
                     break;
                  }
               }
            } else {
               ByteArrayOutputStream var5 = new ByteArrayOutputStream();
               byte[] var6 = new byte[1024];

               while((var7 = var3.read(var6)) != -1) {
                  var5.write(var6, 0, var7);
               }

               var4 = var5.toByteArray();
            }

            var17 = var4;
         } finally {
            try {
               var3.close();
            } catch (IOException var14) {
            }

         }

         return var17;
      }
   }

   private static class Resource implements MetaDataFilter.Resource {
      private final ZipFile _file;
      private final VirtualJarFile _vjf;
      private final ZipInputStream zip;
      private ZipEntry _entry = null;

      public Resource(ZipFile var1) {
         this._file = var1;
         this._vjf = null;
         this.zip = null;
      }

      public Resource(VirtualJarFile var1) {
         this._file = null;
         this._vjf = var1;
         this.zip = null;
      }

      public Resource(ZipInputStream var1) {
         this.zip = var1;
         this._file = null;
         this._vjf = null;
      }

      public void setEntry(ZipEntry var1) {
         this._entry = var1;
      }

      public String getName() {
         return this._entry.getName();
      }

      public byte[] getContent() throws IOException {
         long var1 = this._entry.getSize();
         if (var1 == 0L) {
            return new byte[0];
         } else {
            Object var3 = null;
            Object var4 = null;

            byte[] var16;
            try {
               if (this._file != null) {
                  var4 = this._file.getInputStream(this._entry);
               } else if (this._vjf != null) {
                  var4 = this._vjf.getInputStream(this._entry);
               } else if (this.zip != null) {
                  var4 = this.zip;
               }

               int var7;
               if (var1 < 0L) {
                  ByteArrayOutputStream var5 = new ByteArrayOutputStream();
                  byte[] var6 = new byte[1024];

                  while((var7 = ((InputStream)var4).read(var6)) != -1) {
                     var5.write(var6, 0, var7);
                  }

                  var16 = var5.toByteArray();
               } else {
                  var16 = new byte[(int)var1];
                  int var18 = 0;
                  var7 = (int)var1;

                  int var17;
                  while((var17 = ((InputStream)var4).read(var16, var18, var7 - var18)) > 0) {
                     var18 += var17;
                     if (var18 == var7) {
                        break;
                     }
                  }
               }
            } finally {
               if (this.zip == null && var4 != null) {
                  try {
                     ((InputStream)var4).close();
                  } catch (IOException var14) {
                  }
               }

            }

            return var16;
         }
      }
   }
}
