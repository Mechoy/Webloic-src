package weblogic.xml.security.signature;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.Utils;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class SignatureMethodHMAC extends SignatureMethod implements SignatureMethodFactory, DSIGConstants {
   public static final String URI_SHA1 = "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
   private static final String ALGORITHM_ID_SHA1 = "HmacSHA1";
   private final String uri;
   private final String algorithmID;
   private final Mac algorithm;
   private int outputLength = 160;

   public SignatureMethodHMAC(String var1, String var2) throws NoSuchAlgorithmException {
      this.uri = var1;
      this.algorithmID = var2;
      this.algorithm = Mac.getInstance("HmacSHA1");
   }

   public String getURI() {
      return this.uri;
   }

   static void init() {
      try {
         SignatureMethod.register(new SignatureMethodHMAC("http://www.w3.org/2000/09/xmldsig#hmac-sha1", "HmacSHA1"));
      } catch (NoSuchAlgorithmException var1) {
      }

   }

   public SignatureMethod newSignatureMethod() {
      try {
         return new SignatureMethodHMAC(this.uri, this.algorithmID);
      } catch (NoSuchAlgorithmException var2) {
         throw new AssertionError(var2);
      }
   }

   public String sign(Key var1, byte[] var2) throws XMLSignatureException {
      try {
         this.algorithm.init(var1);
         this.algorithm.update(var2);
         byte[] var3 = new byte[this.outputLength / 8];
         byte[] var4 = this.algorithm.doFinal();
         System.arraycopy(var4, 0, var3, 0, var3.length);
         return Utils.base64(var3);
      } catch (InvalidKeyException var5) {
         throw new XMLSignatureException("Invalid key", var5);
      }
   }

   protected boolean verify(Key var1, byte[] var2, String var3) throws XMLSignatureException {
      try {
         this.algorithm.init(var1);
         this.algorithm.update(var2);
         byte[] var4 = this.algorithm.doFinal();
         byte[] var5 = Utils.base64(var3);
         int var6 = this.outputLength / 8;

         for(int var7 = 0; var7 < var6; ++var7) {
            if (var4[var7] != var5[var7]) {
               return false;
            }
         }

         return true;
      } catch (InvalidKeyException var8) {
         throw new XMLSignatureException("Invalid key", var8);
      }
   }

   protected void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      String var3 = StreamUtils.getValue(var1, var2, "HMACOutputLength");
      if (var3 != null) {
         try {
            this.outputLength = Integer.parseInt(var3);
         } catch (NumberFormatException var5) {
            throw new XMLStreamException("Invalid HMACOutputLength: " + var3);
         }
      }
   }
}
