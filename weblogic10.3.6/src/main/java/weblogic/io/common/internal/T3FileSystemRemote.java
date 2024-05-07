package weblogic.io.common.internal;

import weblogic.common.T3Exception;
import weblogic.common.T3ServicesDef;
import weblogic.io.common.T3File;
import weblogic.io.common.T3FileInputStream;
import weblogic.io.common.T3FileOutputStream;
import weblogic.io.common.T3FileSystem;

public final class T3FileSystemRemote implements T3FileSystem {
   private T3FileSystemProxy rfs;
   private T3ServicesDef svc;
   private String separator = null;
   private String pathSeparator = null;

   public T3FileSystemRemote(T3ServicesDef var1, T3FileSystemProxy var2) {
      this.svc = var1;
      this.rfs = var2;
   }

   public String separator() {
      if (this.separator == null) {
         this.separator = this.rfs.separator();
      }

      return this.separator;
   }

   public String pathSeparator() {
      if (this.pathSeparator == null) {
         this.pathSeparator = this.rfs.pathSeparator();
      }

      return this.pathSeparator;
   }

   public String getName() {
      return this.rfs.getName();
   }

   public T3File getFile(String var1) {
      return new T3FileRemote(this.svc, this.rfs, var1);
   }

   public T3File getFile(String var1, String var2) {
      return this.getFile(var1 + this.separator() + var2);
   }

   public T3FileInputStream getFileInputStream(String var1) throws T3Exception {
      return this.getFile(var1).getFileInputStream();
   }

   public T3FileInputStream getFileInputStream(String var1, int var2, int var3) throws T3Exception {
      return this.getFile(var1).getFileInputStream(var2, var3);
   }

   public T3FileOutputStream getFileOutputStream(String var1) throws T3Exception {
      return this.getFile(var1).getFileOutputStream();
   }

   public T3FileOutputStream getFileOutputStream(String var1, int var2, int var3) throws T3Exception {
      return this.getFile(var1).getFileOutputStream(var2, var3);
   }
}
