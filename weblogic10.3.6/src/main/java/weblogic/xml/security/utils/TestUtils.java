package weblogic.xml.security.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import weblogic.xml.security.keyinfo.KeyInfo;
import weblogic.xml.security.keyinfo.KeyPurpose;
import weblogic.xml.security.keyinfo.KeyResolver;
import weblogic.xml.security.keyinfo.KeyResult;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public class TestUtils {
   private static byte[] DES_KEY_BYTES = new byte[]{-56, -113, -119, -43, -3, -23, -71, -128, 4, 70, 50, 28, 79, -85, -33, -125, -92, 98, -74, 98, -105, -14, 112, -12};
   public static final byte[] AES_128_KEY_BYTES = new byte[]{-45, 95, -78, -71, 13, -95, -72, -12, -75, -7, 11, -12, 44, 127, -77, 105};
   private static final byte[] AES_192_KEY_BYTES = new byte[]{34, 87, -18, 75, -115, 11, -67, 43, 85, 83, 67, 35, -15, -29, -21, -84, 97, -43, -124, 6, -8, -13, 47, -66};
   private static final byte[] AES_256_KEY_BYTES = new byte[]{102, 22, 120, -65, 116, 101, -63, 57, 66, 16, -22, 72, -84, 119, -53, 41, 92, -119, 56, 16, -19, 16, -109, -114, 64, 54, -83, -1, -116, 81, -43, -80};

   public static XMLInputStream createXMLInputStreamFromString(String var0) throws XMLStreamException {
      StringBufferInputStream var1 = new StringBufferInputStream(var0);
      return createXMLInputStream(var1);
   }

   public static XMLInputStream createXMLInputStreamFromFile(String var0) throws IOException {
      FileInputStream var1 = new FileInputStream(var0);
      return createXMLInputStream(var1);
   }

   public static XMLInputStream createXMLInputStream(InputStream var0) throws XMLStreamException {
      XMLInputStreamFactory var1 = XMLInputStreamFactory.newInstance();
      XMLInputStream var2 = var1.newInputStream(var0);
      var2.next();
      return var2;
   }

   public static XMLOutputStream createXMLOutputStream(OutputStream var0) throws XMLStreamException {
      XMLOutputStreamFactory var1 = XMLOutputStreamFactory.newInstance();
      return var1.newOutputStream(var0);
   }

   public static SecretKey getDESKey() throws Exception {
      return getDESKey(DES_KEY_BYTES);
   }

   public static SecretKey getDESKey(byte[] var0) throws Exception {
      DESedeKeySpec var1 = new DESedeKeySpec(var0);
      SecretKeyFactory var2 = SecretKeyFactory.getInstance("DESEDE");
      return var2.generateSecret(var1);
   }

   public static SecretKey getAES128Key() throws Exception {
      return getAESKey(AES_128_KEY_BYTES);
   }

   public static SecretKey getAES192Key() throws Exception {
      return getAESKey(AES_192_KEY_BYTES);
   }

   public static SecretKey getAES256Key() throws Exception {
      return getAESKey(AES_256_KEY_BYTES);
   }

   public static SecretKey getAESKey(byte[] var0) throws Exception {
      return new SecretKeySpec(var0, "AES");
   }

   public static KeyResolver getDESKeyResolver() {
      return new KeyResolver() {
         public KeyResult resolveKey(KeyPurpose var1, String var2, KeyInfo var3) {
            try {
               return new KeyResult(TestUtils.getDESKey());
            } catch (Exception var5) {
               throw new AssertionError(var5);
            }
         }
      };
   }

   public static XMLName getAttrQNameValue(QNameAttribute var0) {
      return var0.getQNameValue();
   }
}
