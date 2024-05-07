package weblogic.wsee.security.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import weblogic.utils.encoders.BASE64Encoder;

public class CertUtils {
   public static void dumpCert(String var0, String var1, String var2, String var3, OutputStream var4) throws Exception {
      List var5 = getCertificate(var0, var1, var2, "JKS");
      String var6 = getBase64EncodedCert((X509Certificate)var5.get(0));
      var4.write("-----BEGIN CERTIFICATE-----".getBytes());
      var4.write(var6.getBytes());
      var4.write("-----END CERTIFICATE-----".getBytes());
   }

   public static void dumpPrivateKey(String var0, String var1, String var2, String var3, OutputStream var4) throws Exception {
      List var5 = getCertificate(var0, var1, var2, "JKS");
      String var6 = getBase64EncodedCert((X509Certificate)var5.get(0));
      var4.write("-----BEGIN PRIVATE KEY-----".getBytes());
      var4.write(var6.getBytes());
      var4.write("-----END PRIVATE KEY-----".getBytes());
   }

   public static List getCertificate(String var0, String var1, String var2, String var3) {
      if (var0 != null && !var0.equals("")) {
         if (var3 != null && !var3.equals("")) {
            if (var2 != null && !var2.equals("")) {
               try {
                  KeyStore var4 = KeyStore.getInstance(var3);
                  FileInputStream var5 = new FileInputStream(var0);
                  if (var1 == null) {
                     var4.load(var5, (char[])null);
                  } else {
                     var4.load(var5, var1.toCharArray());
                  }

                  Certificate[] var6 = var4.getCertificateChain(var2);
                  if (var6 == null) {
                     throw new SecurityException("Can not find public key for alias: \"" + var2 + "\"");
                  } else {
                     return Arrays.asList(var6);
                  }
               } catch (FileNotFoundException var7) {
                  throw new SecurityException(var7);
               } catch (NoSuchAlgorithmException var8) {
                  throw new SecurityException(var8);
               } catch (IOException var9) {
                  throw new SecurityException(var9);
               } catch (CertificateException var10) {
                  throw new SecurityException(var10);
               } catch (KeyStoreException var11) {
                  throw new SecurityException(var11);
               }
            } else {
               throw new SecurityException("certAlias is either null or empty");
            }
         } else {
            throw new SecurityException("keyStoreType is either null or empty");
         }
      } else {
         throw new SecurityException("keyStoreFilename is either null or empty string");
      }
   }

   public static Enumeration getAliases(String var0, String var1, String var2) {
      if (var0 != null && !var0.equals("")) {
         if (var2 != null && !var2.equals("")) {
            try {
               KeyStore var3 = KeyStore.getInstance(var2);
               FileInputStream var4 = new FileInputStream(var0);
               if (var1 == null) {
                  var3.load(var4, (char[])null);
               } else {
                  var3.load(var4, var1.toCharArray());
               }

               return var3.aliases();
            } catch (FileNotFoundException var5) {
               throw new SecurityException(var5);
            } catch (NoSuchAlgorithmException var6) {
               throw new SecurityException(var6);
            } catch (IOException var7) {
               throw new SecurityException(var7);
            } catch (CertificateException var8) {
               throw new SecurityException(var8);
            } catch (KeyStoreException var9) {
               throw new SecurityException(var9);
            }
         } else {
            throw new SecurityException("keyStoreType is either null or empty");
         }
      } else {
         throw new SecurityException("keyStoreFilename is either null or empty string");
      }
   }

   public static String getBase64EncodedCert(Certificate var0) throws CertificateEncodingException {
      return (new BASE64Encoder()).encodeBuffer(var0.getEncoded());
   }

   public static PrivateKey getPrivateKey(String var0, String var1, String var2, String var3, String var4) throws KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException {
      KeyStore var5 = KeyStore.getInstance(var3);
      var5.load(new FileInputStream(var2), var4.toCharArray());
      PrivateKey var6 = (PrivateKey)var5.getKey(var0, var1.toCharArray());
      return var6;
   }

   public static PrivateKey getPKCS8PrivateKey(String var0) throws Exception {
      if (var0 == null) {
         return null;
      } else {
         FileInputStream var1 = new FileInputStream(var0);
         return getPKCS8PrivateKey((InputStream)var1);
      }
   }

   public static PrivateKey getPKCS8PrivateKey(InputStream var0) throws Exception {
      if (null == var0) {
         throw new IllegalArgumentException("Null inputstream");
      } else {
         DataInputStream var1 = new DataInputStream(var0);

         PrivateKey var6;
         try {
            byte[] var2 = new byte[var1.available()];
            var1.readFully(var2);
            PKCS8EncodedKeySpec var3 = new PKCS8EncodedKeySpec(var2);
            KeyFactory var4 = KeyFactory.getInstance("RSA");
            PrivateKey var5 = var4.generatePrivate(var3);
            var6 = var5;
         } finally {
            var1.close();
         }

         return var6;
      }
   }

   public static X509Certificate getCertificate(String var0) throws Exception {
      if (var0 == null) {
         return null;
      } else {
         FileInputStream var1 = new FileInputStream(var0);
         return getCertificate((InputStream)var1);
      }
   }

   public static X509Certificate getCertificate(InputStream var0) throws Exception {
      if (null == var0) {
         throw new IllegalArgumentException("Null inputstream");
      } else {
         DataInputStream var1 = new DataInputStream(var0);

         X509Certificate var4;
         try {
            byte[] var2 = new byte[var1.available()];
            var1.readFully(var2);
            ByteArrayInputStream var3 = new ByteArrayInputStream(var2);
            var4 = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(var3);
         } finally {
            var1.close();
         }

         return var4;
      }
   }

   public static String getSubjectCN(X509Certificate var0) {
      if (null == var0) {
         return null;
      } else {
         String var1 = var0.getSubjectDN().getName();
         int var2 = var1.indexOf("CN=");
         if (var2 == -1) {
            return null;
         } else {
            String var3 = var1.substring(var2 + 3);
            var2 = var3.indexOf(",");
            return var2 == -1 ? var3.trim() : var3.substring(0, var2).trim();
         }
      }
   }
}
