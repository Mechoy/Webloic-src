package weblogic.io.common.internal;

import java.io.File;
import weblogic.common.T3Exception;
import weblogic.io.common.T3File;
import weblogic.io.common.T3FileInputStream;
import weblogic.io.common.T3FileOutputStream;
import weblogic.io.common.T3FileSystem;

public class T3FileSystemLocal implements T3FileSystem {
   public String separator() {
      return File.separator;
   }

   public String pathSeparator() {
      return File.pathSeparator;
   }

   public T3File getFile(String var1) {
      return new T3FileLocal(var1);
   }

   public T3File getFile(String var1, String var2) {
      return this.getFile(var1 + this.separator() + var2);
   }

   public String getName() {
      return "";
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
