package weblogic.xml.security.signature;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import weblogic.xml.security.utils.Utils;

class SignatureMethodDSA extends SignatureMethod implements SignatureMethodFactory, DSIGConstants {
   public static final String URI_SHA1 = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
   private static final String ALGORITHM_ID_SHA1 = "SHA1withDSA";
   private final String uri;
   private final String algorithmID;
   private final java.security.Signature algorithm;

   private SignatureMethodDSA(String var1, String var2) throws NoSuchAlgorithmException {
      this.uri = var1;
      this.algorithmID = var2;
      this.algorithm = java.security.Signature.getInstance("SHA1withDSA");
   }

   public String getURI() {
      return this.uri;
   }

   static void init() {
      try {
         SignatureMethod.register(new SignatureMethodDSA("http://www.w3.org/2000/09/xmldsig#dsa-sha1", "SHA1withDSA"));
      } catch (NoSuchAlgorithmException var1) {
      }

   }

   public SignatureMethod newSignatureMethod() {
      try {
         return new SignatureMethodDSA(this.uri, this.algorithmID);
      } catch (NoSuchAlgorithmException var2) {
         throw new AssertionError(var2);
      }
   }

   protected String sign(Key var1, byte[] var2) throws XMLSignatureException {
      if (!(var1 instanceof PrivateKey)) {
         throw new XMLSignatureException(this.getURI() + " signing requires private key");
      } else {
         try {
            this.algorithm.initSign((PrivateKey)var1);
            this.algorithm.update(var2);
            byte[] var3 = this.algorithm.sign();
            var3 = this.convertJCAtoDSIG(var3);
            return Utils.base64(var3);
         } catch (InvalidKeyException var4) {
            throw new XMLSignatureException(var4);
         } catch (SignatureException var5) {
            throw new XMLSignatureException(var5);
         }
      }
   }

   protected boolean verify(Key var1, byte[] var2, String var3) throws XMLSignatureException {
      if (!(var1 instanceof PublicKey)) {
         throw new XMLSignatureException(this.getURI() + " verification requires public key");
      } else {
         try {
            this.algorithm.initVerify((PublicKey)var1);
            this.algorithm.update(var2);
            byte[] var4 = Utils.base64(var3);
            var4 = this.convertDSIGtoJCA(var4);
            return this.algorithm.verify(var4);
         } catch (InvalidKeyException var5) {
            throw new XMLSignatureException(var5);
         } catch (SignatureException var6) {
            return false;
         }
      }
   }

   protected byte[] convertDSIGtoJCA(byte[] var1) throws XMLSignatureException {
      if (var1.length != 40) {
         throw new XMLSignatureException("Invalid signature");
      } else {
         int var2 = 6 + var1.length;
         int var3 = (var1[0] & 128) == 128 ? 1 : 0;
         int var4 = (var1[20] & 128) == 128 ? 1 : 0;
         var2 += var3 + var4;
         byte[] var5 = new byte[var2];
         var5[0] = 48;
         var5[1] = (byte)(4 + var1.length + var3 + var4);
         var5[2] = 2;
         var5[3] = (byte)(20 + var3);
         System.arraycopy(var1, 0, var5, 4 + var3, 20);
         var5[24 + var3] = 2;
         var5[25 + var3] = (byte)(20 + var4);
         System.arraycopy(var1, 20, var5, 26 + var3 + var4, 20);
         return var5;
      }
   }

   protected byte[] convertJCAtoDSIG(byte[] var1) {
      byte[] var2 = new byte[40];
      byte var3 = var1[3];
      byte var4 = var1[var3 + 5];
      I2OSP(var1, 4, var3, var2, 0, 20);
      I2OSP(var1, var3 + 6, var4, var2, 20, 20);
      return var2;
   }

   private static void I2OSP(byte[] var0, int var1, int var2, byte[] var3, int var4, int var5) {
      if (var2 < var5) {
         int var6 = var4 + (var5 - var2);

         for(int var7 = var4; var7 < var6; ++var7) {
            var3[var7] = 0;
         }

         System.arraycopy(var0, var1, var3, var6, var2);
      } else {
         System.arraycopy(var0, var1 + (var2 - var5), var3, var4, var5);
      }

   }
}
