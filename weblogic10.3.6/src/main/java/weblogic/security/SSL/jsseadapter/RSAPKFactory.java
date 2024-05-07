package weblogic.security.SSL.jsseadapter;

import com.rsa.jsafe.JSAFE_PrivateKey;
import com.rsa.jsafe.JSAFE_SecretKey;
import com.rsa.jsafe.JSAFE_SymmetricCipher;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RSAPKFactory {
   private static final Hashtable<String, String> SUPPORTED_PK_TYPES = new Hashtable();
   private static final String ENC_PKCS8_RSA_PK_BEGIN_HEADER = "-----BEGIN ENCRYPTED PRIVATE KEY-----";
   private static final String ENC_PKCS8_RSA_PK_END_HEADER = "-----END ENCRYPTED PRIVATE KEY-----";
   private static final String UNENC_PKCS1_RSA_PK_BEGIN_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
   private static final String UNENC_PKCS1_RSA_PK_END_HEADER = "-----END RSA PRIVATE KEY-----";
   private static final String UNENC_PKCS8_RSA_PK_BEGIN_HEADER = "-----BEGIN PRIVATE KEY-----";
   private static final String UNENC_PKCS8_RSA_PK_END_HEADER = "-----END PRIVATE KEY-----";

   static PrivateKey getPrivateKey(InputStream var0, char[] var1) throws KeyManagementException {
      byte[] var2 = null;
      String var3 = null;
      PrivateKey var4 = null;
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "Private key input stream: {0}", var0);
      }

      try {
         var3 = new String(JaSSLSupport.readFully(var0));
      } catch (IOException var10) {
         if (JaLogger.isLoggable(Level.SEVERE)) {
            JaLogger.log(Level.SEVERE, JaLogger.Component.SSLCONTEXT, "Failed to read key material from the input stream: " + var10.getMessage());
         }
      }

      String var5 = null;
      if (var3 != null && var3.length() > 0) {
         Iterator var6 = SUPPORTED_PK_TYPES.entrySet().iterator();

         try {
            Map.Entry var7;
            while(var6.hasNext()) {
               var7 = (Map.Entry)var6.next();
               Matcher var8 = Pattern.compile((String)var7.getValue(), 32).matcher(var3);
               if (var8.find()) {
                  var2 = JaSSLSupport.decodeData(var8.group(1));
                  var5 = (String)var7.getKey();
                  break;
               }
            }

            if (var2 != null) {
               var7 = null;
               KeyFactory var11 = KeyFactory.getInstance("RSA");
               if ("-----BEGIN RSA PRIVATE KEY-----".equalsIgnoreCase(var5)) {
                  RSAPrivateCrtKeySpec var12 = getRSAKeySpec(var2);
                  var4 = var11.generatePrivate(var12);
               } else if ("-----BEGIN PRIVATE KEY-----".equalsIgnoreCase(var5)) {
                  PKCS8EncodedKeySpec var13 = new PKCS8EncodedKeySpec(var2);
                  var4 = var11.generatePrivate(var13);
               } else if ("-----BEGIN ENCRYPTED PRIVATE KEY-----".equalsIgnoreCase(var5)) {
                  var4 = getEncryptedPKCS8PrivateKey(var2, var1);
               }
            }
         } catch (Exception var9) {
            if (JaLogger.isLoggable(Level.SEVERE)) {
               JaLogger.log(Level.SEVERE, JaLogger.Component.SSLCONTEXT, "Failed to create the private key: " + var9.getMessage());
            }

            throw new KeyManagementException(var9);
         }
      }

      return var4;
   }

   private static RSAPrivateCrtKeySpec getRSAKeySpec(byte[] var0) throws IOException {
      DERDecoder var1 = new DERDecoder(var0);
      ASN1Object var2 = var1.readObject();
      if (var2.getType() != 16) {
         throw new IOException("Unexpected type; not a sequence");
      } else {
         var1 = var2.getDecoder();
         var1.readObject();
         BigInteger var3 = var1.readObject().getBigInteger();
         BigInteger var4 = var1.readObject().getBigInteger();
         BigInteger var5 = var1.readObject().getBigInteger();
         BigInteger var6 = var1.readObject().getBigInteger();
         BigInteger var7 = var1.readObject().getBigInteger();
         BigInteger var8 = var1.readObject().getBigInteger();
         BigInteger var9 = var1.readObject().getBigInteger();
         BigInteger var10 = var1.readObject().getBigInteger();
         RSAPrivateCrtKeySpec var11 = new RSAPrivateCrtKeySpec(var3, var4, var5, var6, var7, var8, var9, var10);
         return var11;
      }
   }

   private static PrivateKey getEncryptedPKCS8PrivateKey(byte[] var0, char[] var1) throws Exception {
      JSAFE_SymmetricCipher var2 = JSAFE_SymmetricCipher.getInstance(var0, 0, "Java");
      JSAFE_SecretKey var3 = var2.getBlankKey();
      var3.setPassword(var1, 0, var1.length);
      var2.decryptInit(var3, (SecureRandom)null);
      JSAFE_PrivateKey var4 = var2.unwrapPrivateKey(var0, 0, var0.length, true);
      return new JSAFE_PrivateKeyWrapper(var4);
   }

   static {
      SUPPORTED_PK_TYPES.put("-----BEGIN ENCRYPTED PRIVATE KEY-----", "-----BEGIN ENCRYPTED PRIVATE KEY-----(.+?)-----END ENCRYPTED PRIVATE KEY-----");
      SUPPORTED_PK_TYPES.put("-----BEGIN RSA PRIVATE KEY-----", "-----BEGIN RSA PRIVATE KEY-----(.+?)-----END RSA PRIVATE KEY-----");
      SUPPORTED_PK_TYPES.put("-----BEGIN PRIVATE KEY-----", "-----BEGIN PRIVATE KEY-----(.+?)-----END PRIVATE KEY-----");
   }
}
