package weblogic.xml.crypto.dsig;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Mac;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.api.SignatureMethod;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.spec.SignatureMethodParameterSpec;
import weblogic.xml.security.utils.Utils;

public class SignatureMethodHMAC extends SignatureMethodImpl implements SignatureMethodFactory, SignatureMethod, WLSignatureMethod, XMLStructure, WLXMLStructure {
   private static final String OUTPUTLENGTH_ELEMENT = "HMACOutputLength";
   private static final String ALGORITHM_ID_SHA1 = "HmacSHA1";
   private final String uri;
   private final String algorithmID;
   private final Mac algorithm;
   private SignatureMethodParameterSpec signatureMethodParameterSpec;
   private int outputLength = 160;

   public SignatureMethodHMAC(String var1, String var2) throws NoSuchAlgorithmException {
      this.uri = var1;
      this.algorithmID = var2;
      this.algorithm = Mac.getInstance("HmacSHA1");
   }

   static void init() {
      try {
         register(new SignatureMethodHMAC("http://www.w3.org/2000/09/xmldsig#hmac-sha1", "HmacSHA1"));
      } catch (NoSuchAlgorithmException var1) {
      }

   }

   public String getAlgorithm() {
      return this.uri;
   }

   public AlgorithmParameterSpec getParameterSpec() {
      return this.signatureMethodParameterSpec;
   }

   public String getURI() {
      return this.uri;
   }

   public SignatureMethod newSignatureMethod() {
      try {
         return new SignatureMethodHMAC(this.uri, this.algorithmID);
      } catch (NoSuchAlgorithmException var2) {
         throw new AssertionError(var2);
      }
   }

   public boolean isFeatureSupported(String var1) {
      return false;
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

   public boolean verify(Key var1, byte[] var2, String var3) throws XMLSignatureException {
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

   public void readParameters(XMLStreamReader var1) throws MarshalException {
      String var2 = null;

      try {
         var1.nextTag();
         if (var1.getName().equals(new QName("http://www.w3.org/2000/09/xmldsig#", "HMACOutputLength"))) {
            var1.next();
            var2 = var1.getText();
            if (var2 != null) {
               this.outputLength = Integer.parseInt(var2);
               if (this.outputLength != 160) {
                  throw new MarshalException("Invalid HMACOutputLength: " + var2);
               }
            }
         }
      } catch (NumberFormatException var4) {
         throw new MarshalException("Invalid HMACOutputLength: " + var2);
      } catch (XMLStreamException var5) {
         throw new MarshalException("Failed to read HMACOutputLength.");
      }
   }

   public void writeParameters(XMLStreamWriter var1) throws MarshalException {
   }
}
