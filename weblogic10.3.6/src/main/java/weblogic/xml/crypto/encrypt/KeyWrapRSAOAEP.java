package weblogic.xml.crypto.encrypt;

import com.bea.security.utils.random.SecureRandomData;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.DigestMethodImpl;
import weblogic.xml.crypto.dsig.WLDigestMethod;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.spec.RSAOAEPParameterSpec;
import weblogic.xml.crypto.utils.StaxUtils;

final class KeyWrapRSAOAEP extends KeyWrap implements WLEncryptionMethodFactory {
   private static final String ALGORITHM_ID = "RSA/NoPad";
   public static final String TAG_OAEP_PARAMS = "OAEPparams";
   private static final byte[] EMPTY_PARAMS = new byte[0];

   private KeyWrapRSAOAEP() {
      super("http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p", (Integer)null, (AlgorithmParameterSpec)null);
   }

   private KeyWrapRSAOAEP(Integer var1, AlgorithmParameterSpec var2) {
      super("http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p", var1, var2);
   }

   public static void init() {
      WLEncryptionMethod.register(new KeyWrapRSAOAEP());
   }

   protected void writeParameters(XMLStreamWriter var1) throws XMLStreamException {
      byte[] var2 = getOAEPParams(this.getParams());
      if (var2 != null && var2.length > 0) {
         String var3 = Utils.base64(var2);
         StaxUtils.writeElement(var1, "http://www.w3.org/2001/04/xmlenc#", "OAEPparams", var3);
      }

      try {
         WLDigestMethod var6 = getDigestMethod(this.getParams());
         ((WLXMLStructure)var6).write(var1);
      } catch (XMLEncryptionException var4) {
         throw new XMLStreamException(var4);
      } catch (MarshalException var5) {
         throw new XMLStreamException(var5);
      }
   }

   public static byte[] getOAEPParams(RSAOAEPParameterSpec var0) {
      if (var0 == null) {
         return EMPTY_PARAMS;
      } else {
         byte[] var1 = var0.getOAEPParams();
         return var1 != null ? var1 : EMPTY_PARAMS;
      }
   }

   private RSAOAEPParameterSpec getParams() {
      return (RSAOAEPParameterSpec)this.params;
   }

   public WLEncryptionMethod getEncryptionMethod(AlgorithmParameterSpec var1, Integer var2) {
      return this.getKeyWrap(var1, var2);
   }

   public KeyWrap getKeyWrap(AlgorithmParameterSpec var1, Integer var2) {
      return var1 == null && var2 == null ? this : new KeyWrapRSAOAEP(var2, var1);
   }

   public EncryptionAlgorithm getEncryptionAlgorithm(AlgorithmParameterSpec var1, Integer var2) {
      throw new UnsupportedOperationException("Algorithm " + this.getAlgorithm() + " cannot be used for bulk encryption");
   }

   public AlgorithmParameterSpec readParameters(XMLStreamReader var1) throws MarshalException {
      String var2;
      try {
         var2 = StaxUtils.getElementValue(var1, "http://www.w3.org/2001/04/xmlenc#", "OAEPparams");
      } catch (XMLStreamException var10) {
         throw new MarshalException(var10);
      }

      byte[] var3 = var2 != null ? Utils.base64(var2) : EMPTY_PARAMS;
      WLDigestMethod var4;
      if (var1.isStartElement()) {
         String var5 = var1.getLocalName();
         if (!"DigestMethod".equals(var5)) {
            throw new MarshalException("Unrecognized Element: " + var5);
         }

         try {
            var4 = DigestMethodImpl.newDigestMethod(var1);
         } catch (NoSuchAlgorithmException var7) {
            throw new MarshalException(var7);
         } catch (MarshalException var8) {
            throw new MarshalException(var8);
         } catch (XMLStreamException var9) {
            throw new MarshalException(var9);
         }
      } else {
         var4 = null;
      }

      return new RSAOAEPParameterSpec(var4, var3);
   }

   public byte[] decrypt(Key var1, byte[] var2) throws XMLEncryptionException {
      if (!(var1 instanceof RSAPrivateKey)) {
         throw new XMLEncryptionException("Invalid key supplied to: " + this.getAlgorithm() + ", expecting RSAPrivateKey");
      } else {
         RSAPrivateKey var3 = (RSAPrivateKey)var1;
         CipherWrapper var4 = CipherWrapper.getInstance("RSA/NoPad", 2, var1);
         byte[] var5 = var4.decrypt(var2);
         int var6 = var3.getModulus().bitLength() / 8;
         byte[] var7 = new byte[var6 - 1];
         int var8 = Math.min(var5.length, var7.length);
         int var9 = var5.length - var8;
         int var10 = var7.length - var8;
         System.arraycopy(var5, var9, var7, var10, var8);
         WLDigestMethod var11 = getDigestMethod(this.getParams());
         MessageDigest var12 = var11.getMessageDigest();
         return oaepDecode(var12, var7, getOAEPParams(this.getParams()));
      }
   }

   private static WLDigestMethod getDigestMethod(RSAOAEPParameterSpec var0) throws XMLEncryptionException {
      if (var0 == null) {
         return getDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1");
      } else {
         WLDigestMethod var1 = (WLDigestMethod)var0.getDigestMethod();
         return var1 != null ? var1 : getDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1");
      }
   }

   public byte[] encrypt(Key var1, byte[] var2) throws XMLEncryptionException {
      if (!(var1 instanceof RSAPublicKey)) {
         throw new XMLEncryptionException("Invalid key supplied to: " + this.getAlgorithm() + ", expecting RSAPublicKey");
      } else {
         RSAPublicKey var3 = (RSAPublicKey)var1;
         CipherWrapper var4 = CipherWrapper.getInstance("RSA/NoPad", 1, var1);
         int var5 = var3.getModulus().bitLength() / 8;
         WLDigestMethod var6 = getDigestMethod(this.getParams());
         MessageDigest var7 = var6.getMessageDigest();
         byte[] var8 = oaepEncode(var7, var2, getOAEPParams(this.getParams()), var5 - 1);
         byte[] var9 = new byte[var8.length + 1];
         System.arraycopy(var8, 0, var9, 1, var8.length);
         return var4.encrypt(var9);
      }
   }

   static byte[] oaepDecode(MessageDigest var0, byte[] var1, byte[] var2) throws XMLEncryptionException {
      int var3 = var0.getDigestLength();
      if (var1.length < 2 * var3 + 1) {
         throw new XMLEncryptionException("decoding error");
      } else {
         byte[] var4 = new byte[var3];
         System.arraycopy(var1, 0, var4, 0, var3);
         byte[] var5 = new byte[var1.length - var3];
         System.arraycopy(var1, var3, var5, 0, var1.length - var3);
         byte[] var6 = new byte[var3];
         WLDigestMethod var7 = getDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1");
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
               throw new XMLEncryptionException("decoding error");
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
            throw new XMLEncryptionException("decoding error");
         } else {
            byte[] var15 = new byte[var5.length - var11];
            System.arraycopy(var5, var11, var15, 0, var15.length);
            return var15;
         }
      }
   }

   static byte[] oaepEncode(MessageDigest var0, byte[] var1, byte[] var2, int var3) throws XMLEncryptionException {
      int var4 = var0.getDigestLength();
      if (var1.length > var3 - 2 * var4 - 1) {
         throw new XMLEncryptionException("Export strength certificates not supported");
      } else {
         byte[] var5 = var0.digest(var2);
         byte[] var6 = new byte[var3 - var4];
         System.arraycopy(var5, 0, var6, 0, var4);
         int var7 = var6.length - var1.length;
         System.arraycopy(var1, 0, var6, var7, var1.length);
         var6[var7 - 1] = 1;
         byte[] var8 = SecureRandomData.getInstance().getRandomBytes(var4);
         byte[] var9 = new byte[var6.length];
         WLDigestMethod var10 = getDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1");
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

      for(int var7 = 0; var7 < var6; ++var7) {
         var0.update(var1);
         var0.update(i2osp(var7));
         byte[] var8 = var0.digest();
         int var9 = Math.min(var4, var3 - var5);
         System.arraycopy(var8, 0, var2, var5, var9);
         var5 += var9;
      }

   }

   private static WLDigestMethod getDigestMethod(String var0) throws XMLEncryptionException {
      try {
         return DigestMethodImpl.newDigestMethod(var0);
      } catch (NoSuchAlgorithmException var2) {
         throw new XMLEncryptionException(var2);
      }
   }

   static byte[] i2osp(int var0) {
      byte[] var1 = new byte[]{(byte)(var0 >>> 24), (byte)(var0 >>> 16), (byte)(var0 >>> 8), (byte)(var0 >>> 0)};
      return var1;
   }
}
