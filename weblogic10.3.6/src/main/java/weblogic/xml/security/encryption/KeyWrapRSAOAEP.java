package weblogic.xml.security.encryption;

import com.bea.security.utils.random.SecureRandomData;
import java.security.Key;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import weblogic.xml.security.signature.DSIGConstants;
import weblogic.xml.security.signature.DSIGReader;
import weblogic.xml.security.signature.DigestMethod;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.XMLSecurityException;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class KeyWrapRSAOAEP extends KeyWrap implements KeyWrapFactory, XMLEncConstants, DSIGConstants {
   public static final String URI = "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p";
   private static final String ALGORITHM_ID = "RSA/NoPad";
   private static final String KEY_FACTORY_ID = "RSA";
   private final String uri;
   private byte[] oaepParams = new byte[0];
   private DigestMethod digestMethod;

   private KeyWrapRSAOAEP(String var1) {
      this.uri = var1;
   }

   public String getURI() {
      return this.uri;
   }

   public String getAlgorithm() {
      return "RSA";
   }

   public static void init() {
      EncryptionMethod.register(new KeyWrapRSAOAEP("http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p"));
   }

   public byte[] getOAEPParams() {
      return this.oaepParams;
   }

   public void setOAEPParams(byte[] var1) {
      this.oaepParams = var1;
   }

   public void setDigestMethod(String var1) throws XMLSecurityException {
      this.digestMethod = DigestMethod.get(var1);
   }

   protected void toXMLInternal(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      if (this.oaepParams != null && this.oaepParams.length > 0) {
         String var4 = Utils.base64(this.oaepParams);
         StreamUtils.addElement(var1, var2, "OAEPparams", var4, var3 + 2);
         this.digestMethod.toXML(var1, var2, var3 + 2);
      }

   }

   public void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      String var3 = StreamUtils.getValue(var1, var2, "OAEPparams");
      if (var3 != null) {
         this.oaepParams = Utils.base64(var3);
      }

      this.digestMethod = (DigestMethod)DSIGReader.read(var1, 2);
   }

   public EncryptionMethod newEncryptionMethod() {
      return new KeyWrapRSAOAEP("http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p");
   }

   public KeyWrap newKeyWrap() {
      return new KeyWrapRSAOAEP("http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p");
   }

   public byte[] unwrap(Key var1, byte[] var2) throws EncryptionException {
      if (!(var1 instanceof RSAPrivateKey)) {
         throw new EncryptionException("Invalid key supplied to: " + this.getURI() + ", expecting RSAPrivateKey");
      } else {
         RSAPrivateKey var3 = (RSAPrivateKey)var1;
         CipherWrapper var4 = CipherWrapper.getInstance("RSA/NoPad", 2, var1);
         byte[] var5 = var4.decrypt(var2);
         int var6 = (var3.getModulus().bitLength() + 7) / 8;
         byte[] var7 = new byte[var6 - 1];
         int var8 = Math.min(var5.length, var7.length);
         int var9 = var5.length - var8;
         int var10 = var7.length - var8;
         System.arraycopy(var5, var9, var7, var10, var8);
         if (this.digestMethod == null) {
            this.digestMethod = getDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1");
         }

         MessageDigest var11 = this.digestMethod.getMessageDigest();
         return oaepDecode(var11, var7, this.oaepParams);
      }
   }

   public byte[] wrap(Key var1, byte[] var2) throws EncryptionException {
      if (!(var1 instanceof RSAPublicKey)) {
         throw new EncryptionException("Invalid key supplied to: " + this.getURI() + ", expecting RSAPublicKey");
      } else {
         RSAPublicKey var3 = (RSAPublicKey)var1;
         CipherWrapper var4 = CipherWrapper.getInstance("RSA/NoPad", 1, var1);
         int var5 = (var3.getModulus().bitLength() + 7) / 8;
         if (this.digestMethod == null) {
            this.digestMethod = getDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1");
         }

         MessageDigest var6 = this.digestMethod.getMessageDigest();
         byte[] var7 = oaepEncode(var6, var2, this.oaepParams, var5 - 1);
         byte[] var8 = new byte[var7.length + 1];
         System.arraycopy(var7, 0, var8, 1, var7.length);
         return var4.encrypt(var8);
      }
   }

   static byte[] oaepDecode(MessageDigest var0, byte[] var1, byte[] var2) throws EncryptionException {
      int var3 = var0.getDigestLength();
      if (var1.length < 2 * var3 + 1) {
         throw new EncryptionException("decoding error");
      } else {
         byte[] var4 = new byte[var3];
         System.arraycopy(var1, 0, var4, 0, var3);
         byte[] var5 = new byte[var1.length - var3];
         System.arraycopy(var1, var3, var5, 0, var1.length - var3);
         byte[] var6 = new byte[var3];
         DigestMethod var7 = getDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1");
         MessageDigest var8 = var7.getMessageDigest();
         mgf1(var8, var5, var6);

         for(int var9 = 0; var9 < var3; ++var9) {
            var4[var9] ^= var6[var9];
         }

         var8.reset();
         byte[] var13 = new byte[var1.length - var3];
         mgf1(var8, var4, var13);

         for(int var10 = 0; var10 < var5.length; ++var10) {
            var5[var10] ^= var13[var10];
         }

         var0.update(var2);
         byte[] var14 = var0.digest();

         int var11;
         for(var11 = 0; var11 < var14.length; ++var11) {
            if (var14[var11] != var5[var11]) {
               throw new EncryptionException("decoding error");
            }
         }

         var11 = -1;

         for(int var12 = var3; var12 < var5.length; ++var12) {
            if (var5[var12] == 1) {
               var11 = var12 + 1;
               break;
            }
         }

         if (var11 == -1) {
            throw new EncryptionException("decoding error");
         } else {
            byte[] var15 = new byte[var5.length - var11];
            System.arraycopy(var5, var11, var15, 0, var15.length);
            return var15;
         }
      }
   }

   static byte[] oaepEncode(MessageDigest var0, byte[] var1, byte[] var2, int var3) throws EncryptionException {
      int var4 = var0.getDigestLength();
      if (var1.length > var3 - 2 * var4 - 1) {
         throw new EncryptionException("Export strength certificates not supported");
      } else {
         byte[] var5 = var0.digest(var2);
         byte[] var6 = new byte[var3 - var4];
         System.arraycopy(var5, 0, var6, 0, var4);
         int var7 = var6.length - var1.length;
         System.arraycopy(var1, 0, var6, var7, var1.length);
         var6[var7 - 1] = 1;
         byte[] var8 = SecureRandomData.getInstance().getRandomBytes(var4);
         byte[] var9 = new byte[var6.length];
         DigestMethod var10 = getDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1");
         MessageDigest var11 = var10.getMessageDigest();
         mgf1(var11, var8, var9);

         for(int var12 = 0; var12 < var6.length; ++var12) {
            var9[var12] ^= var6[var12];
         }

         byte[] var14 = new byte[var4];
         var11.reset();
         mgf1(var11, var9, var14);

         for(int var13 = 0; var13 < var14.length; ++var13) {
            var14[var13] ^= var8[var13];
         }

         byte[] var15 = new byte[var14.length + var6.length];
         System.arraycopy(var14, 0, var15, 0, var14.length);
         System.arraycopy(var9, 0, var15, var14.length, var9.length);
         return var15;
      }
   }

   static void mgf1(MessageDigest var0, byte[] var1, byte[] var2) {
      int var3 = var2.length;
      int var4 = var0.getDigestLength();
      int var5 = 0;
      int var6 = var3 / var4 + (1 % var4 > 0 ? 1 : 0);
      byte[] var7 = new byte[var1.length + 4];

      for(int var8 = 0; var8 < var6; ++var8) {
         var0.update(var1);
         var0.update(i2osp(var8));
         byte[] var9 = var0.digest();
         int var10 = Math.min(var4, var3 - var5);
         System.arraycopy(var9, 0, var2, var5, var10);
         var5 += var10;
      }

   }

   private static DigestMethod getDigestMethod(String var0) throws EncryptionException {
      try {
         return DigestMethod.get(var0);
      } catch (XMLSecurityException var2) {
         throw new EncryptionException("DigestMethod: " + var0 + " not available");
      }
   }

   static byte[] i2osp(int var0) {
      byte[] var1 = new byte[]{(byte)(var0 >>> 24), (byte)(var0 >>> 16), (byte)(var0 >>> 8), (byte)(var0 >>> 0)};
      return var1;
   }
}
