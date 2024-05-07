package weblogic.security.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import weblogic.management.provider.ManagementService;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class KeyStoreUtils {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static File getFile(String var0) {
      if (var0 != null && var0.length() != 0) {
         File var1 = new File(var0);
         if (var1.exists()) {
            return var1;
         } else {
            var1 = new File(ManagementService.getRuntimeAccess(kernelId).getServer().getRootDirectory(), var0);
            return var1.exists() ? var1 : null;
         }
      } else {
         throw new IllegalArgumentException(SecurityLogger.getLocationNullOrEmpty());
      }
   }

   public static KeyStore load(File var0, String var1, String var2) {
      char[] var3 = var1 != null && var1.length() != 0 ? var1.toCharArray() : null;
      return load(var0, var3, var2);
   }

   public static KeyStore load(File var0, char[] var1, String var2) {
      if (var0 == null) {
         throw new IllegalArgumentException(SecurityLogger.getNullFile());
      } else {
         KeyStore var3 = null;

         try {
            var3 = KeyStore.getInstance(var2);
         } catch (KeyStoreException var23) {
            SecurityLogger.logLoadKeyStoreKeyStoreException(var2, var23.toString());
            return null;
         }

         FileInputStream var4 = null;

         try {
            var4 = new FileInputStream(var0);
         } catch (FileNotFoundException var22) {
            SecurityLogger.logLoadKeyStoreFileNotFoundException(var0.getAbsolutePath(), var22.toString());
            return null;
         }

         Object var6;
         try {
            var3.load(var4, var1);
            return var3;
         } catch (CertificateException var24) {
            SecurityLogger.logLoadKeyStoreCertificateException(var0.getAbsolutePath(), var2, var24.toString());
            var6 = null;
            return (KeyStore)var6;
         } catch (NoSuchAlgorithmException var25) {
            SecurityLogger.logLoadKeyStoreNoSuchAlgorithmException(var0.getAbsolutePath(), var2, var25.toString());
            var6 = null;
            return (KeyStore)var6;
         } catch (IOException var26) {
            SecurityLogger.logLoadKeyStoreIOException(var0.getAbsolutePath(), var2, var26.toString());
            var6 = null;
         } finally {
            try {
               var4.close();
            } catch (IOException var21) {
            }

         }

         return (KeyStore)var6;
      }
   }

   public static KeyStore load(File var0, String var1, String var2, String var3) {
      char[] var4 = var1 != null && var1.length() != 0 ? var1.toCharArray() : null;
      return load(var0, var4, var2, var3);
   }

   public static KeyStore load(File var0, char[] var1, String var2, String var3) {
      if (var0 == null) {
         throw new IllegalArgumentException(SecurityLogger.getNullFile());
      } else {
         KeyStore var4 = null;

         try {
            var4 = KeyStore.getInstance(var2, var3);
         } catch (NoSuchProviderException var25) {
            SecurityLogger.logLoadKeyStoreException(var2, var3, var25.toString());
            return null;
         } catch (KeyStoreException var26) {
            SecurityLogger.logLoadKeyStoreException(var2, var3, var26.toString());
            return null;
         }

         FileInputStream var5 = null;

         try {
            var5 = new FileInputStream(var0);
         } catch (FileNotFoundException var24) {
            SecurityLogger.logLoadKeyStoreFileNotFoundException(var0.getAbsolutePath(), var24.toString());
            return null;
         }

         Object var7;
         try {
            var4.load(var5, var1);
            return var4;
         } catch (CertificateException var27) {
            SecurityLogger.logLoadKeyStoreCertificateException(var0.getAbsolutePath(), var2, var27.toString());
            var7 = null;
            return (KeyStore)var7;
         } catch (NoSuchAlgorithmException var28) {
            SecurityLogger.logLoadKeyStoreNoSuchAlgorithmException(var0.getAbsolutePath(), var2, var28.toString());
            var7 = null;
            return (KeyStore)var7;
         } catch (IOException var29) {
            SecurityLogger.logLoadKeyStoreIOException(var0.getAbsolutePath(), var2, var29.toString());
            var7 = null;
         } finally {
            try {
               var5.close();
            } catch (IOException var23) {
            }

         }

         return (KeyStore)var7;
      }
   }

   public static boolean store(KeyStore var0, File var1, String var2) {
      if (var0 == null) {
         throw new IllegalArgumentException(SecurityLogger.getNullKeystore());
      } else if (var1 == null) {
         throw new IllegalArgumentException(SecurityLogger.getNullFile());
      } else if (var2 != null && var2.length() != 0) {
         FileOutputStream var3 = null;

         try {
            var3 = new FileOutputStream(var1);
         } catch (FileNotFoundException var23) {
            SecurityLogger.logStoreKeyStoreFileNotFoundException(var1.getAbsolutePath(), var23.toString());
            return false;
         }

         boolean var6;
         try {
            Object var4 = null;

            try {
               var0.store(var3, var2.toCharArray());
               return true;
            } catch (KeyStoreException var24) {
               SecurityLogger.logStoreKeyStoreKeyStoreException(var1.getAbsolutePath(), var0.getType(), var24.toString());
               var6 = false;
               return var6;
            } catch (CertificateException var25) {
               SecurityLogger.logStoreKeyStoreCertificateException(var1.getAbsolutePath(), var0.getType(), var25.toString());
               var6 = false;
            } catch (NoSuchAlgorithmException var26) {
               SecurityLogger.logStoreKeyStoreNoSuchAlgorithmException(var1.getAbsolutePath(), var0.getType(), var26.toString());
               var6 = false;
               return var6;
            } catch (IOException var27) {
               SecurityLogger.logStoreKeyStoreIOException(var1.getAbsolutePath(), var0.getType(), var27.toString());
               var6 = false;
               return var6;
            }
         } finally {
            try {
               var3.close();
            } catch (IOException var22) {
            }

         }

         return var6;
      } else {
         throw new IllegalArgumentException(SecurityLogger.getNullOrEmptyPassPhrase());
      }
   }
}
