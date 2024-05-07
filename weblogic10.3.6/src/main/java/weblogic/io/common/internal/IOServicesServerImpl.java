package weblogic.io.common.internal;

import weblogic.common.T3Client;
import weblogic.common.T3Exception;
import weblogic.common.T3ServicesDef;
import weblogic.io.common.IOServicesDef;
import weblogic.io.common.T3File;
import weblogic.io.common.T3FileSystem;

public final class IOServicesServerImpl extends IOServicesImpl implements IOServicesDef {
   public IOServicesServerImpl(T3ServicesDef var1) throws T3Exception {
      super(var1, (T3Client)null);
   }

   public T3FileSystem getFileSystem(String var1) throws T3Exception {
      return (T3FileSystem)(var1 != null && !var1.equals("") ? this.getLocalFileSystem(var1) : this.localFileSystem);
   }

   public T3File getFile(String var1) throws T3Exception {
      String var2 = null;
      if (var1 != null && var1.length() > 2) {
         char var3 = var1.charAt(0);
         int var4 = var1.indexOf(var3, 2);
         if ((var3 == '/' || var3 == '\\') && var1.charAt(1) == var3 && var4 != -1) {
            var2 = var1.substring(2, var4);
            var1 = var1.substring(var4 + 1);
         }
      }

      return this.getLocalFileSystem(var2).getFile(var1);
   }

   private T3FileSystemLocal getLocalFileSystem(String var1) throws T3Exception {
      return new T3FileSystemLocalMountPoint(var1);
   }
}
