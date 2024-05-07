package weblogic.servlet.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import weblogic.utils.classloaders.FileSource;
import weblogic.utils.classloaders.NullSource;
import weblogic.utils.classloaders.Source;
import weblogic.utils.classloaders.URLSource;
import weblogic.utils.classloaders.ZipSource;

public class WarSource implements Source {
   final Source delegate;
   private final long length;
   private final boolean isDirectory;
   private final long lastChecked = System.currentTimeMillis();
   private final long lastModified;
   private final String lastModifiedString;
   private final boolean fromArchive;
   private final boolean fromLibrary;
   private String contentType;
   private String fileName = null;

   public WarSource(Source var1) {
      boolean var2 = false;
      boolean var3 = false;
      if (var1 instanceof War.LibrarySource) {
         War.LibrarySource var4 = (War.LibrarySource)var1;
         var2 = var4.isFromArchive();
         var1 = var4.getSource();
         var3 = true;
      }

      this.delegate = var1;
      this.fromArchive = var2;
      this.fromLibrary = var3;
      this.length = var1.length();
      this.lastModified = var1.lastModified();
      this.lastModifiedString = ResponseHeaders.getDateString(this.lastModified);
      this.isDirectory = isDirectory(var1);
   }

   public Source getDelegateSource() {
      return this.delegate;
   }

   public long getLastChecked() {
      return this.lastChecked;
   }

   public byte[] getBytes() throws IOException {
      return this.delegate.getBytes();
   }

   public long length() {
      return this.length;
   }

   public long lastModified() {
      return this.lastModified;
   }

   public InputStream getInputStream() throws IOException {
      return this.delegate.getInputStream();
   }

   public URL getURL() {
      return this.delegate.getURL();
   }

   public URL getCodeSourceURL() {
      return this.delegate.getCodeSourceURL();
   }

   public String getLastModifiedAsString() {
      return this.lastModifiedString;
   }

   public boolean isDirectory() {
      return this.isDirectory;
   }

   public boolean isFile() {
      if (this.delegate instanceof FileSource) {
         return true;
      } else if (this.delegate instanceof ZipSource) {
         return false;
      } else if (this.delegate instanceof WarSource) {
         return ((WarSource)this.delegate).isFile();
      } else if (this.delegate instanceof URLSource) {
         return true;
      } else if (this.delegate instanceof MDSSource) {
         return false;
      } else {
         throw new UnsupportedOperationException("Can't isFile on: '" + this.delegate + "'");
      }
   }

   public String getFileName() {
      if (this.fileName != null) {
         return this.fileName;
      } else {
         if (this.delegate instanceof FileSource) {
            this.fileName = ((FileSource)this.delegate).getFile().getAbsolutePath();
         } else if (!(this.delegate instanceof ZipSource) && !(this.delegate instanceof URLSource)) {
            if (this.delegate instanceof MDSSource) {
               this.fileName = ((MDSSource)this.delegate).getProviderURI();
            } else {
               if (!(this.delegate instanceof WarSource)) {
                  throw new UnsupportedOperationException("Can't getFileName on: '" + this.delegate + "'");
               }

               this.fileName = ((WarSource)this.delegate).getFileName();
            }
         } else {
            this.fileName = this.delegate.getURL().getFile();
         }

         return this.fileName;
      }
   }

   public boolean isFromArchive() {
      return this.fromArchive;
   }

   public boolean isFromLibrary() {
      return this.fromLibrary;
   }

   public String getContentType(WebAppServletContext var1) {
      if (this.contentType != null) {
         return this.contentType;
      } else {
         this.contentType = var1.getMimeType(this.getName());
         return this.contentType;
      }
   }

   public String getName() {
      if (this.delegate instanceof FileSource) {
         return ((FileSource)this.delegate).getFile().getName();
      } else {
         String var1;
         if (this.delegate instanceof ZipSource) {
            var1 = ((ZipSource)this.delegate).getEntry().getName();
            return this.getNameFromPath(var1);
         } else if (this.delegate instanceof MDSSource) {
            var1 = ((MDSSource)this.delegate).getProviderURI();
            return this.getNameFromPath(var1);
         } else if (this.delegate instanceof WarSource) {
            return ((WarSource)this.delegate).getName();
         } else if (this.delegate instanceof URLSource) {
            return ((URLSource)this.delegate).getURL().getFile();
         } else {
            throw new UnsupportedOperationException("Can't getName on: '" + this.delegate + "'");
         }
      }
   }

   private String getNameFromPath(String var1) {
      if (var1.charAt(var1.length() - 1) == '/') {
         var1 = var1.substring(0, var1.length() - 1);
      }

      int var2 = var1.lastIndexOf("/");
      return var2 != -1 ? var1.substring(var2 + 1) : var1;
   }

   public WarSource[] listSources() {
      if (this.delegate instanceof FileSource) {
         return getFileSourceListing((FileSource)this.delegate);
      } else if (this.delegate instanceof ZipSource) {
         return getZipSourceListing((ZipSource)this.delegate);
      } else if (this.delegate instanceof WarSource) {
         return ((WarSource)this.delegate).listSources();
      } else if (this.delegate instanceof MDSSource) {
         return null;
      } else if (this.delegate instanceof URLSource) {
         return null;
      } else {
         throw new UnsupportedOperationException("Can't getDirectoryListing on: " + this.delegate + "'");
      }
   }

   private static WarSource[] getFileSourceListing(FileSource var0) {
      if (!var0.getFile().isDirectory()) {
         return null;
      } else {
         File[] var1 = var0.getFile().listFiles();
         WarSource[] var2 = new WarSource[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = new WarSource(new FileSource(var1[var3]));
         }

         return var2;
      }
   }

   private static WarSource[] getZipSourceListing(ZipSource var0) {
      String var1 = var0.getEntry().getName();
      if ("/".equals(var1)) {
         var1 = "";
      }

      ArrayList var2 = new ArrayList();
      Enumeration var3 = var0.getFile().entries();

      while(true) {
         ZipEntry var4;
         String var5;
         int var6;
         do {
            do {
               do {
                  if (!var3.hasMoreElements()) {
                     return (WarSource[])((WarSource[])var2.toArray(new WarSource[var2.size()]));
                  }

                  var4 = (ZipEntry)var3.nextElement();
                  var5 = var4.getName();
               } while(var5.equals(var1));
            } while(!var5.startsWith(var1));

            var6 = var5.indexOf("/", var1.length());
         } while(var6 > 0 && var6 < var5.length() - 1);

         var2.add(new WarSource(new ZipSource(var0.getFile(), var4)));
      }
   }

   private static boolean isDirectory(Source var0) {
      if (var0 == null) {
         return false;
      } else if (var0 instanceof FileSource) {
         return ((FileSource)var0).getFile().isDirectory();
      } else if (var0 instanceof ZipSource) {
         ZipSource var1 = (ZipSource)var0;
         if (var1.getEntry().isDirectory()) {
            return true;
         } else {
            ZipFile var2 = var1.getFile();
            ZipEntry var3 = var2.getEntry(var1.getEntry().toString() + '/');
            return var3 != null;
         }
      } else if (var0 instanceof WarSource) {
         return ((WarSource)var0).isDirectory();
      } else if (!(var0 instanceof NullSource) && !(var0 instanceof URLSource) && !(var0 instanceof MDSSource)) {
         throw new UnsupportedOperationException("Can't isDirectory on: '" + var0 + "'");
      } else {
         return false;
      }
   }
}
