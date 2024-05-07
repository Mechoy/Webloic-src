package weblogic.io.common.internal;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import weblogic.common.T3Exception;
import weblogic.common.T3ServicesDef;
import weblogic.io.common.T3File;
import weblogic.io.common.T3FileInputStream;
import weblogic.io.common.T3FileOutputStream;

public final class T3FileRemote extends File implements T3File {
   private static final long serialVersionUID = 5169544571923577797L;
   private static final int DEFAULT_BUFFERSIZE = 102400;
   private static final int DEFAULT_READ_AHEAD = 1;
   private static final int DEFAULT_WRITE_BEHIND = 1;
   private T3ServicesDef svc;
   private T3FileSystemProxy rfs;
   private String path;
   private String separator;
   private char separatorChar;
   private String pathSeparator;
   private char pathSeparatorChar;

   public T3FileRemote(T3ServicesDef var1, T3FileSystemProxy var2, String var3) {
      super(var3);
      this.svc = var1;
      this.rfs = var2;
      this.path = var3;
      this.separator = var2.separator();
      this.pathSeparator = var2.pathSeparator();
      this.separatorChar = this.separator.charAt(0);
      this.pathSeparatorChar = this.pathSeparator.charAt(0);
   }

   public T3FileInputStream getFileInputStream() throws T3Exception {
      return new T3FileInputStreamRemote(this.rfs, this, 102400, 1);
   }

   public T3FileInputStream getFileInputStream(int var1, int var2) throws T3Exception {
      return new T3FileInputStreamRemote(this.rfs, this, var1, var2);
   }

   public T3FileOutputStream getFileOutputStream() throws T3Exception {
      return new T3FileOutputStreamRemote(this.rfs, this, 102400, 1);
   }

   public T3FileOutputStream getFileOutputStream(int var1, int var2) throws T3Exception {
      return new T3FileOutputStreamRemote(this.rfs, this, var1, var2);
   }

   public T3File extend(String var1) {
      return new T3FileRemote(this.svc, this.rfs, this.path + this.separator + var1);
   }

   public String getName() {
      return this.rfs.getName(this.path);
   }

   public String getPath() {
      return this.path;
   }

   public String getAbsolutePath() {
      return this.path;
   }

   public String getCanonicalPath() throws IOException {
      return this.rfs.getCanonicalPath(this.path);
   }

   public String getParent() {
      String var1;
      try {
         String var2 = this.getCanonicalPath();
         var1 = this.rfs.getParent(var2);
      } catch (IOException var3) {
         var1 = null;
      }

      return var1;
   }

   public boolean exists() {
      return this.rfs.exists(this.path) || this.rfs.absoluteExists(this.path);
   }

   public boolean canWrite() {
      return this.rfs.canWrite(this.path);
   }

   public boolean canRead() {
      return this.rfs.canRead(this.path);
   }

   public boolean isFile() {
      return this.rfs.isFile(this.path);
   }

   public boolean isDirectory() {
      return this.rfs.isDirectory(this.path) || this.rfs.isAbsoluteDirectory(this.path);
   }

   public boolean isAbsolute() {
      return true;
   }

   public long lastModified() {
      return this.rfs.lastModified(this.path);
   }

   public long length() {
      return this.rfs.length(this.path);
   }

   public boolean mkdir() {
      return this.rfs.mkdir(this.path);
   }

   public boolean mkdirs() {
      return this.rfs.mkdirs(this.path);
   }

   public String[] list() {
      return this.rfs.list(this.path);
   }

   public String[] list(FilenameFilter var1) {
      return this.rfs.list(this.path, var1);
   }

   public boolean delete() {
      return this.rfs.delete(this.path);
   }

   public String toString() {
      return this.path;
   }

   public int hashCode() {
      return this.path.hashCode();
   }

   public boolean equals(Object var1) {
      return var1 instanceof T3FileRemote && this.rfs.equals(((T3FileRemote)var1).rfs) ? this.path.equals(((T3FileRemote)var1).getPath()) : false;
   }

   public boolean renameTo(T3File var1) {
      boolean var2 = false;
      if (var1 instanceof T3FileRemote && this.rfs.equals(((T3FileRemote)var1).rfs)) {
         var2 = this.rfs.renameTo(this.path, ((T3FileRemote)var1).getPath());
      }

      return var2;
   }
}
