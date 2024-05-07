package weblogic.security.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import weblogic.management.DomainDir;
import weblogic.security.Salt;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.security.internal.encryption.JSafeEncryptionServiceImpl;
import weblogic.security.service.PrivilegedActions;

public final class SerializedSystemIni {
   private static final String PW = "0xccb97558940b82637c8bec3c770f86fa3a391a56";
   private static final int VERSION = 2;
   private static final String FILE = "SerializedSystemIni.dat";
   private static final int SALT_LENGTH = 4;
   private static final boolean DEBUG = false;
   private static final int UPDATE_VERSION = 1;
   private static SerializedSystemIni theInstance = null;
   private byte[] salt = null;
   private byte[] encryptedSecretKey = null;
   private byte[] encryptedAESSecretKey = null;
   private static AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static EncryptionService encryptionService = null;

   private static void debug(String var0) {
   }

   private void generateSalt() {
      this.salt = Salt.getRandomBytes(4);
   }

   private void generateEncryptedSecretKey() {
      this.encryptedSecretKey = JSafeEncryptionServiceImpl.getFactory().createEncryptedSecretKey(this.salt, "0xccb97558940b82637c8bec3c770f86fa3a391a56");
   }

   private void generateAESEncryptedSecretKey() {
      this.encryptedAESSecretKey = JSafeEncryptionServiceImpl.getFactory().createAESEncryptedSecretKey(this.salt, "0xccb97558940b82637c8bec3c770f86fa3a391a56");
   }

   private byte[] readBytes(InputStream var1) throws IOException {
      int var2 = var1.read();
      if (var2 < 0) {
         throw new IOException("SerializedSystemIni is empty");
      } else {
         byte[] var3 = new byte[var2];
         int var4 = 0;

         int var6;
         for(boolean var5 = false; var4 < var2; var4 += var6) {
            var6 = var1.read(var3, var4, var2 - var4);
            if (var6 == -1) {
               break;
            }
         }

         return var3;
      }
   }

   private void write(String var1, boolean var2) {
      if (var2) {
         ensureDomainSecurityDirExists();
      }

      FileWriter var3 = new FileWriter() {
         public void write(OutputStream var1) throws IOException {
            var1.write(SerializedSystemIni.this.salt.length);
            var1.write(SerializedSystemIni.this.salt);
            var1.write(2);
            var1.write(SerializedSystemIni.this.encryptedSecretKey.length);
            var1.write(SerializedSystemIni.this.encryptedSecretKey);
            var1.write(SerializedSystemIni.this.encryptedAESSecretKey.length);
            var1.write(SerializedSystemIni.this.encryptedAESSecretKey);
         }
      };
      FileUtils.replace(var1, var3);
   }

   private SerializedSystemIni(String var1) {
      File var2 = new File(var1);
      if (!var2.exists()) {
         this.generateSalt();
         this.generateEncryptedSecretKey();
         this.generateAESEncryptedSecretKey();
         this.write(var1, true);
      } else {
         try {
            FileInputStream var3 = new FileInputStream(var2);

            try {
               this.salt = this.readBytes(var3);
               int var4 = var3.read();
               if (var4 != -1) {
                  this.encryptedSecretKey = this.readBytes(var3);
                  if (var4 >= 2) {
                     this.encryptedAESSecretKey = this.readBytes(var3);
                  }
               }
            } catch (IOException var14) {
               throw new SerializedSystemIniException(SecurityLogger.getIniCorruptFile(var1), var14);
            } finally {
               try {
                  var3.close();
               } catch (IOException var13) {
                  throw new SerializedSystemIniException(SecurityLogger.getIniCouldNotClose(var1), var13);
               }
            }

            if (this.encryptedSecretKey == null) {
               this.generateEncryptedSecretKey();
               this.generateAESEncryptedSecretKey();
               this.write(var1, true);
            }
         } catch (FileNotFoundException var16) {
            throw new SerializedSystemIniException(SecurityLogger.getIniErrorOpeningFile(var1), var16);
         }
      }

   }

   private SerializedSystemIni(String var1, int var2) {
      File var3 = new File(var1);
      if (!var3.exists()) {
         throw new SerializedSystemIniException(SecurityLogger.getIniErrorOpeningFile(var1));
      } else {
         try {
            FileInputStream var4 = new FileInputStream(var3);

            try {
               this.salt = this.readBytes(var4);
               int var5 = var4.read();
               if (var5 != var2) {
                  throw new SerializedSystemIniException(SecurityLogger.getIniVersionMismatch("" + var5, "" + var2));
               }

               this.encryptedSecretKey = this.readBytes(var4);
            } catch (IOException var15) {
               throw new SerializedSystemIniException(SecurityLogger.getIniCorruptFile(var1), var15);
            } finally {
               try {
                  var4.close();
               } catch (IOException var14) {
                  throw new SerializedSystemIniException(SecurityLogger.getIniCouldNotClose(var1), var14);
               }
            }

         } catch (FileNotFoundException var17) {
            throw new SerializedSystemIniException(SecurityLogger.getIniErrorOpeningFile(var1), var17);
         }
      }
   }

   private byte[] getTheSalt() {
      return this.salt;
   }

   private byte[] getTheEncryptedSecretKey() {
      return this.encryptedSecretKey;
   }

   private byte[] getTheAESEncryptedSecretKey() {
      return this.encryptedAESSecretKey;
   }

   private static synchronized void init() {
      if (theInstance == null) {
         theInstance = new SerializedSystemIni(DomainDir.getPathRelativeSecurityDir("SerializedSystemIni.dat"));
      }

   }

   public static boolean exists() {
      File var0 = new File(DomainDir.getPathRelativeSecurityDir("SerializedSystemIni.dat"));
      return var0.exists();
   }

   public static String getPath() {
      return DomainDir.getPathRelativeSecurityDir("SerializedSystemIni.dat");
   }

   public static byte[] getSalt(String var0) {
      return (new SerializedSystemIni(var0)).getTheSalt();
   }

   public static byte[] getSalt() {
      init();
      return theInstance.getTheSalt();
   }

   public static byte[] getEncryptedSecretKey() {
      init();
      return theInstance.getTheEncryptedSecretKey();
   }

   public static byte[] getEncryptedAESSecretKey() {
      init();
      return theInstance.getTheAESEncryptedSecretKey();
   }

   static EncryptionService getEncryptionService(byte[] var0, byte[] var1, byte[] var2) {
      return JSafeEncryptionServiceImpl.getFactory().getEncryptionService(var0, "0xccb97558940b82637c8bec3c770f86fa3a391a56", var1, var2);
   }

   public static EncryptionService getExistingEncryptionService() {
      String var0 = DomainDir.getRootDir();
      String var1 = var0 + File.separator + "security" + File.separator + "SerializedSystemIni.dat";
      File var2 = new File(var1);
      if (!var2.exists()) {
         String var3 = var0 + File.separator + "SerializedSystemIni.dat";
         File var4 = new File(var3);
         if (!var4.exists()) {
            return null;
         }

         var1 = var3;
      }

      SerializedSystemIni var5 = new SerializedSystemIni(var1);
      return getEncryptionService(var5.getTheSalt(), var5.getTheEncryptedSecretKey(), var5.getTheAESEncryptedSecretKey());
   }

   public static EncryptionService getEncryptionService(String var0) {
      String var1 = var0 + File.separator + "security" + File.separator + "SerializedSystemIni.dat";
      File var2 = new File(var1);
      if (!var2.exists()) {
         String var3 = var0 + File.separator + "SerializedSystemIni.dat";
         File var4 = new File(var3);
         if (var4.exists()) {
            var1 = var3;
         }
      }

      SerializedSystemIni var5 = new SerializedSystemIni(var1);
      return getEncryptionService(var5.getTheSalt(), var5.getTheEncryptedSecretKey(), var5.getTheAESEncryptedSecretKey());
   }

   private static void ensureDomainSecurityDirExists() {
      File var0 = new File(DomainDir.getSecurityDir());
      if (!var0.exists()) {
         try {
            var0.mkdir();
         } catch (SecurityException var2) {
         }
      }

   }

   static void upgradeSSI() {
      File var0 = new File(DomainDir.getPathRelativeSecurityDir("SerializedSystemIni.dat"));
      if (!var0.exists()) {
         File var1 = new File(DomainDir.getPathRelativeRootDir("SerializedSystemIni.dat"));
         if (var1.exists()) {
            ensureDomainSecurityDirExists();

            try {
               weblogic.utils.FileUtils.copy(var1, var0);
               if (var1.canWrite()) {
                  var1.delete();
               }
            } catch (IOException var3) {
            }

         }
      }
   }

   public static EncryptionService getEncryptionService() {
      if (encryptionService == null) {
         encryptionService = getEncryptionService(DomainDir.getRootDir());
      }

      return encryptionService;
   }

   public static void addAESKey() {
      updateDatFileWithAESKey(DomainDir.getPathRelativeSecurityDir("SerializedSystemIni.dat"));
   }

   public static void updateDatFileWithAESKey(String var0) {
      SerializedSystemIni var1 = new SerializedSystemIni(var0, 1);
      var1.generateAESEncryptedSecretKey();
      var1.write(var0, false);
   }

   public static void rollbackAESKey() {
      updateDatFileRollbackAESKey(DomainDir.getPathRelativeSecurityDir("SerializedSystemIni.dat"));
   }

   public static void updateDatFileRollbackAESKey(String var0) {
      SerializedSystemIni var1 = new SerializedSystemIni(var0, 2);
      final byte[] var2 = var1.getTheSalt();
      final byte[] var3 = var1.getTheEncryptedSecretKey();
      FileWriter var4 = new FileWriter() {
         public void write(OutputStream var1) throws IOException {
            var1.write(var2.length);
            var1.write(var2);
            var1.write(1);
            var1.write(var3.length);
            var1.write(var3);
         }
      };
      FileUtils.replace(var0, var4);
   }
}
