package weblogic.io.common.internal;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import weblogic.common.T3Exception;
import weblogic.io.common.T3File;
import weblogic.io.common.T3FileInputStream;
import weblogic.io.common.T3FileOutputStream;

public final class T3FileLocal extends File implements T3File {
   private static final long serialVersionUID = 5975049963833859248L;
   private static final boolean debug = false;
   private File javaFile;
   private String pathFromMP;

   public T3FileLocal(String var1) {
      super(var1);
      this.pathFromMP = var1;

      try {
         this.javaFile = new File(this.getCanonicalPath());
      } catch (IOException var3) {
         this.javaFile = new File(var1);
      }

   }

   public T3FileLocal(String var1, String var2) {
      super(var1, var2);

      try {
         this.javaFile = new File(this.getCanonicalPath());
      } catch (IOException var4) {
         this.javaFile = new File(var1, var2);
      }

   }

   private File getTryFile() {
      Object var1;
      if (this.javaFile == null) {
         var1 = this;
      } else {
         var1 = this.javaFile;
      }

      return (File)var1;
   }

   public T3FileInputStream getFileInputStream() throws T3Exception {
      return new T3FileInputStreamLocal(this);
   }

   public T3FileInputStream getFileInputStream(int var1, int var2) throws T3Exception {
      return new T3FileInputStreamLocal(this);
   }

   public T3FileOutputStream getFileOutputStream() throws T3Exception {
      return new T3FileOutputStreamLocal(this);
   }

   public T3FileOutputStream getFileOutputStream(int var1, int var2) throws T3Exception {
      return new T3FileOutputStreamLocal(this);
   }

   public T3File extend(String var1) {
      return new T3FileLocal(this.pathFromMP + File.separator + var1);
   }

   public String getName() {
      return this.getTryFile().getName();
   }

   public String getPath() {
      return this.pathFromMP;
   }

   public String getAbsolutePath() {
      return this.getTryFile().getAbsolutePath();
   }

   public String getParent() {
      return super.getParent();
   }

   public boolean exists() {
      return this.getTryFile().exists();
   }

   public boolean canWrite() {
      return this.getTryFile().canWrite();
   }

   public boolean canRead() {
      return this.getTryFile().canRead();
   }

   public boolean isFile() {
      return this.getTryFile().isFile();
   }

   public boolean isDirectory() {
      return this.getTryFile().isDirectory();
   }

   public long lastModified() {
      return this.getTryFile().lastModified();
   }

   public long length() {
      return this.getTryFile().length();
   }

   public boolean mkdir() {
      return this.getTryFile().mkdir();
   }

   public boolean mkdirs() {
      return this.getTryFile().mkdirs();
   }

   public String[] list() {
      return this.getTryFile().list();
   }

   public String[] list(FilenameFilter var1) {
      return this.getTryFile().list(var1);
   }

   public boolean delete() {
      return this.getTryFile().delete();
   }

   public int hashCode() {
      return this.getTryFile().hashCode();
   }

   public String toString() {
      return this.getTryFile().toString();
   }

   public boolean renameTo(T3File var1) {
      return var1 instanceof T3FileLocal ? this.getTryFile().renameTo(((T3FileLocal)var1).javaFile) : false;
   }

   public boolean equals(Object var1) {
      return var1 instanceof T3FileLocal ? this.getTryFile().equals(((T3FileLocal)var1).javaFile) : false;
   }
}
