package weblogic.xml.security.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import weblogic.utils.encoders.BASE64Decoder;
import weblogic.utils.encoders.BASE64Encoder;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.xml.security.signature.DSIGConstants;
import weblogic.xml.stream.XMLName;
import weblogic.xml.util.WhitespaceUtils;

public class Utils {
   private static final int DEFAULT_NONCE_SIZE = 20;
   private static int DEFAULT_ID_SIZE = 18;
   private static final String SUBJECT_KEY_IDENTIFIER_OID = "2.5.29.14";
   private static CertificateFactory certFactory;
   private static SecureRandom rng;
   private static String UTF_8 = "UTF-8";
   private static final String SECURE_RANDOM = "SecureRandom";
   private static final String SUN_RNG_TYPE = "SHA1PRNG";
   private static final String IBM_RNG_TYPE = "IBMSecureRandom";
   public static final String ENCODING_PKIPATH = "PkiPath";
   public static final String ENCODING_PKCS7 = "PKCS7";

   public static String toCryptoBinary(BigInteger var0) {
      if (var0 == null) {
         return null;
      } else {
         byte[] var1 = var0.toByteArray();
         int var2 = 0;

         for(int var3 = 0; var3 < var1.length && var1[var3] == 0; ++var3) {
            ++var2;
         }

         byte[] var5 = new byte[var1.length - var2];
         System.arraycopy(var1, var2, var5, 0, var5.length);
         BASE64Encoder var4 = new BASE64Encoder();
         return var4.encodeBuffer(var5);
      }
   }

   public static BigInteger fromCryptoBinary(String var0) {
      var0 = WhitespaceUtils.removeAllWhitespaces(var0);
      BASE64Decoder var1 = new BASE64Decoder();
      Object var2 = null;

      byte[] var5;
      try {
         var5 = var1.decodeBuffer(var0);
      } catch (IOException var4) {
         throw new AssertionError(var4);
      }

      return new BigInteger(1, var5);
   }

   public static String base64(byte[] var0) {
      return (new BASE64Encoder()).encodeBuffer(var0);
   }

   public static byte[] base64(String var0) {
      var0 = WhitespaceUtils.removeAllWhitespaces(var0);

      try {
         return (new BASE64Decoder()).decodeBuffer(var0);
      } catch (IOException var2) {
         throw new AssertionError(var2);
      }
   }

   public static String toBase64(byte[] var0) {
      return (new BASE64Encoder()).encodeBuffer(var0);
   }

   public static String toBase64(X509Certificate var0) throws CertificateException {
      return toBase64(var0.getEncoded());
   }

   public static CertificateFactory getCertFactory() {
      if (certFactory == null) {
         try {
            certFactory = CertificateFactory.getInstance("X.509");
         } catch (Exception var1) {
            throw new AssertionError(var1);
         }
      }

      return certFactory;
   }

   public static X509Certificate certFromBase64(String var0) throws CertificateException {
      byte[] var1 = base64(var0);
      UnsyncByteArrayInputStream var2 = new UnsyncByteArrayInputStream(var1);
      return (X509Certificate)getCertFactory().generateCertificate(var2);
   }

   public static SecureRandom getRNG() {
      if (rng == null) {
         SecureRandom var0;
         try {
            Set var1 = Security.getAlgorithms("SecureRandom");
            if (var1.contains("SHA1PRNG")) {
               var0 = SecureRandom.getInstance("SHA1PRNG");
            } else if (var1.contains("IBMSecureRandom")) {
               var0 = SecureRandom.getInstance("IBMSecureRandom");
            } else {
               if (var1.isEmpty()) {
                  throw new AssertionError("No SecureRandom provider available");
               }

               var0 = SecureRandom.getInstance((String)var1.iterator().next());
            }
         } catch (NoSuchAlgorithmException var2) {
            throw new AssertionError(var2);
         }

         rng = var0;
      }

      return rng;
   }

   public static String generateNonce() {
      return generateBase64Nonce(20);
   }

   public static byte[] passwordDigest(String var0, String var1, String var2) throws NoSuchAlgorithmException {
      return passwordDigest(base64(var0), var1, var2);
   }

   public static byte[] passwordDigest(byte[] var0, String var1, String var2) throws NoSuchAlgorithmException {
      MessageDigest var3 = null;
      var3 = MessageDigest.getInstance("SHA-1");
      var3.update(var0);

      try {
         var3.update(var1.getBytes(UTF_8));
         var3.update(var2.getBytes(UTF_8));
      } catch (UnsupportedEncodingException var5) {
         throw new AssertionError("Unable to create password digest - UTF-8 encoding required but unavailable: " + var5.getMessage());
      }

      byte[] var4 = var3.digest();
      return var4;
   }

   public static String generateBase64Nonce(int var0) {
      byte[] var1 = generateNonce(var0);
      return (new BASE64Encoder()).encodeBuffer(var1);
   }

   public static String generateXPointerId(int var0) {
      String var1 = generateBase64Nonce(var0);
      StringBuffer var2 = new StringBuffer(var1.length());

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         char var4 = var1.charAt(var3);
         if (var4 == '=' || var4 == '+' || var4 == '/') {
            var4 = '_';
         }

         var2.append(var4);
      }

      return var2.toString();
   }

   public static byte[] generateNonce(int var0) {
      byte[] var1 = new byte[var0];
      getRNG().nextBytes(var1);
      return var1;
   }

   public static String generateId() {
      return generateId("Unknown");
   }

   public static String generateId(String var0) {
      return generateId(var0, DEFAULT_ID_SIZE);
   }

   public static String generateId(String var0, int var1) {
      if (!DSIGConstants.VERBOSE) {
         var0 = "Id";
      }

      return var0 + "-" + generateXPointerId(var1);
   }

   public static final byte[] getSubjectKeyIdentifier(X509Certificate var0) {
      byte[] var1 = var0.getExtensionValue("2.5.29.14");
      byte[] var2;
      if (var1 != null) {
         int var3 = getSkidContentLength(var1);
         int var4 = var1.length - var3;
         var2 = new byte[var3];

         for(int var5 = 0; var5 < var3; ++var5) {
            var2[var5] = var1[var5 + var4];
         }
      } else {
         var2 = null;
      }

      return var2;
   }

   private static final int getSkidContentLength(byte[] var0) {
      return var0.length - 4;
   }

   public static final QName getQName(XMLName var0) {
      return new QName(var0.getNamespaceUri(), var0.getLocalName());
   }

   public static CertPath generateCertPath(X509Certificate[] var0) {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         X509Certificate var3 = var0[var2];
         var1.add(var3);
      }

      return generateCertPath((List)var1);
   }

   public static CertPath generateCertPath(List var0) {
      try {
         CertPath var1 = getCertFactory().generateCertPath(var0);
         return var1;
      } catch (CertificateException var3) {
         throw new IllegalArgumentException("Unable to create CertPath from certificates: " + var3);
      }
   }
}
