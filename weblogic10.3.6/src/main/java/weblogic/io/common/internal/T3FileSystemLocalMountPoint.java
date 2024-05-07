package weblogic.io.common.internal;

import java.security.AccessController;
import weblogic.common.T3Exception;
import weblogic.io.common.T3File;
import weblogic.io.common.T3FileInputStream;
import weblogic.io.common.T3FileOutputStream;
import weblogic.management.configuration.FileT3MBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class T3FileSystemLocalMountPoint extends T3FileSystemLocal {
   private String fileSystemName = null;
   private String mountPoint = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static String getFileSystemName(String var0) {
      if (var0 != null && var0.length() > 2) {
         char var1 = var0.charAt(0);
         int var2 = var0.indexOf(92, 2);
         int var3 = var0.indexOf(47, 2);
         if ((var1 == '/' || var1 == '\\') && var0.charAt(1) == var1) {
            if (var2 != -1) {
               return var0.substring(2, var2);
            } else {
               return var3 != -1 ? var0.substring(2, var3) : "";
            }
         } else {
            return "";
         }
      } else {
         return "";
      }
   }

   public static String getFileName(String var0) {
      if (var0 != null && var0.length() > 2) {
         char var1 = var0.charAt(0);
         int var2 = var0.indexOf(92, 2);
         int var3 = var0.indexOf(47, 2);
         if ((var1 == '/' || var1 == '\\') && var0.charAt(1) == var1) {
            if (var2 != -1) {
               return var0.substring(var2 + 1);
            } else {
               return var3 != -1 ? var0.substring(var3 + 1) : var0;
            }
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public T3FileSystemLocalMountPoint(String var1) throws T3Exception {
      FileT3MBean var2 = null;
      this.fileSystemName = var1;
      var2 = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupFileT3(this.fileSystemName);
      if (var2 == null) {
         throw new T3Exception("Unknown file system " + this.fileSystemName);
      } else {
         this.mountPoint = var2.getPath() + this.separator();
         if (!FileService.getFileService().isFileSystemMounted(var1)) {
            throw new T3Exception("Unknown file system " + this.fileSystemName);
         }
      }
   }

   public T3File getFile(String var1) {
      return new T3FileLocal(this.mountPoint + var1);
   }

   public T3File getFile(String var1, String var2) {
      return this.getFile(this.mountPoint + var1 + this.separator() + var2);
   }

   public String getName() {
      return this.fileSystemName;
   }

   public T3FileInputStream getFileInputStream(String var1) throws T3Exception {
      var1 = this.stripLeadingSlash(var1);
      return this.getFile(var1).getFileInputStream();
   }

   public T3FileInputStream getFileInputStream(String var1, int var2, int var3) throws T3Exception {
      var1 = this.stripLeadingSlash(var1);
      return this.getFile(var1).getFileInputStream(var2, var3);
   }

   public T3FileOutputStream getFileOutputStream(String var1) throws T3Exception {
      var1 = this.stripLeadingSlash(var1);
      return this.getFile(var1).getFileOutputStream();
   }

   public T3FileOutputStream getFileOutputStream(String var1, int var2, int var3) throws T3Exception {
      var1 = this.stripLeadingSlash(var1);
      return this.getFile(var1).getFileOutputStream(var2, var3);
   }

   private String stripLeadingSlash(String var1) {
      return var1.startsWith(this.pathSeparator()) ? var1.substring(1) : var1;
   }
}
