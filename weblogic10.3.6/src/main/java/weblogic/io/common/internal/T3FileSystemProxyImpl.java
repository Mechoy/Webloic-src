package weblogic.io.common.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import weblogic.common.T3Exception;
import weblogic.common.T3MiscLogger;
import weblogic.common.T3ServicesDef;
import weblogic.t3.srvr.T3Srvr;

public class T3FileSystemProxyImpl implements T3FileSystemProxy {
   private String fileSystemName;
   private String prefix;
   private String canonicalPrefix;
   private T3ServicesDef svc;

   public String getName() {
      return this.fileSystemName;
   }

   public T3FileSystemProxyImpl(String var1, String var2) {
      this.fileSystemName = var1;
      this.svc = T3Srvr.getT3Srvr().getT3Services();
      this.prefix = var2;

      try {
         this.canonicalPrefix = this.validatePath(var2);
      } catch (IOException var4) {
         this.canonicalPrefix = null;
         T3MiscLogger.logBadCreate(var2, var4);
         return;
      }

      T3MiscLogger.logCreate(var2);
   }

   private String validatePath(String var1) throws IOException {
      String var2;
      File var3;
      try {
         var3 = new File(var1);
         var2 = var3.getCanonicalPath();
      } catch (IOException var5) {
         throw new IOException("Problem with path " + var1 + ", " + var5);
      }

      if (!var3.isDirectory()) {
         throw new IOException("Problem with path " + var2 + ", it is NOT a directory");
      } else {
         return var2;
      }
   }

   private String getCanonicalPrefix() throws IOException {
      if (this.canonicalPrefix == null) {
         throw new IOException();
      } else {
         return this.canonicalPrefix;
      }
   }

   private File getActualFile(String var1) throws IllegalArgumentException {
      File var2 = new File(this.prefix + File.separator + var1);

      try {
         String var3 = var2.getCanonicalPath();
         if (!var3.startsWith(this.getCanonicalPrefix())) {
            throw new IllegalArgumentException("Remote file name " + var1 + " references above the mount point");
         }
      } catch (IOException var4) {
      }

      return var2;
   }

   private File getAbsoluteFile(String var1) throws IllegalArgumentException {
      return this.getActualFile(var1);
   }

   public boolean absoluteExists(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.exists();
   }

   public boolean isAbsoluteDirectory(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.isDirectory();
   }

   public String separator() {
      return File.separator;
   }

   public String pathSeparator() {
      return File.pathSeparator;
   }

   public OneWayInputServer createInputStream(OneWayInputClient var1, String var2, int var3, int var4) throws T3Exception {
      File var5 = this.getAbsoluteFile(var2);
      FileInputStream var6 = null;

      try {
         var6 = new FileInputStream(var5);
      } catch (FileNotFoundException var8) {
         T3MiscLogger.logFindRemote(var5.getPath(), var8);
         throw new T3Exception("Unable to find remote file " + var2);
      }

      T3RemoteInputStreamProxy var7 = new T3RemoteInputStreamProxy(this.fileSystemName + ":" + var2, var6, var3, var4, var1);
      return var7;
   }

   public OneWayOutputServer createOutputStream(OneWayOutputClient var1, String var2, int var3) throws T3Exception {
      File var4 = this.getAbsoluteFile(var2);
      FileOutputStream var5 = null;

      try {
         var5 = new FileOutputStream(var4);
      } catch (IOException var7) {
         T3MiscLogger.logOpenRemote(var4.getPath(), var7);
         throw new T3Exception("Unable to open remote file " + var2);
      }

      T3RemoteOutputStreamProxy var6 = new T3RemoteOutputStreamProxy(this.fileSystemName + ":" + var2, var5, var3, var1);
      return var6;
   }

   public String getName(String var1) {
      if (var1 != null && !var1.equals("")) {
         File var2 = this.getAbsoluteFile(var1);
         return var2.getName();
      } else {
         return this.fileSystemName;
      }
   }

   public String getCanonicalPath(String var1) throws IOException {
      File var2 = this.getAbsoluteFile(var1);
      String var3 = var2.getCanonicalPath();
      int var4 = var3.indexOf(this.getCanonicalPrefix());
      if (var4 != -1) {
         return this.getCanonicalPrefix().length() >= var3.length() ? "" : var3.substring(this.getCanonicalPrefix().length() + 1);
      } else {
         throw new IOException("Invalid path name");
      }
   }

   public String getParent(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      String var3 = var2.getParent();
      return var3 != null && var3.length() > this.prefix.length() ? var3.substring(this.prefix.length() + 1) : null;
   }

   public boolean exists(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.exists();
   }

   public boolean canWrite(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.canWrite();
   }

   public boolean canRead(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.canRead();
   }

   public boolean isFile(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.isFile();
   }

   public boolean isDirectory(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.isDirectory();
   }

   public long lastModified(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.lastModified();
   }

   public long length(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.length();
   }

   public boolean mkdir(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.mkdir();
   }

   public boolean mkdirs(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.mkdirs();
   }

   public String[] list(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.list();
   }

   public String[] list(String var1, FilenameFilter var2) {
      File var3 = this.getAbsoluteFile(var1);
      return var3.list(var2);
   }

   public boolean delete(String var1) {
      File var2 = this.getAbsoluteFile(var1);
      return var2.delete();
   }

   public boolean renameTo(String var1, String var2) {
      File var3 = this.getAbsoluteFile(var1);
      File var4 = this.getAbsoluteFile(var2);
      return var3.renameTo(var4);
   }
}
