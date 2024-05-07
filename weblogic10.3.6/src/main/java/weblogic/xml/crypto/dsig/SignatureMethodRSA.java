package weblogic.xml.crypto.dsig;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.api.SignatureMethod;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.spec.SignatureMethodParameterSpec;
import weblogic.xml.security.utils.Utils;

class SignatureMethodRSA extends SignatureMethodImpl implements SignatureMethodFactory, SignatureMethod, WLSignatureMethod, XMLStructure, WLXMLStructure {
   private static final String ALGORITHM_ID_SHA1 = "SHA1withRSA";
   private final String uri;
   private final String algorithmID;
   private final Signature algorithm;
   private SignatureMethodParameterSpec signatureMethodParameterSpec;

   private SignatureMethodRSA(String var1, String var2) throws NoSuchAlgorithmException {
      this.uri = var1;
      this.algorithmID = var2;
      this.algorithm = Signature.getInstance("SHA1withRSA");
   }

   static void init() {
      try {
         register(new SignatureMethodRSA("http://www.w3.org/2000/09/xmldsig#rsa-sha1", "SHA1withRSA"));
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
         return new SignatureMethodRSA(this.uri, this.algorithmID);
      } catch (NoSuchAlgorithmException var2) {
         throw new AssertionError(var2);
      }
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public String sign(Key var1, byte[] var2) throws XMLSignatureException {
      if (!(var1 instanceof PrivateKey)) {
         throw new XMLSignatureException(this.getURI() + " signing requires private key");
      } else {
         try {
            this.algorithm.initSign((PrivateKey)var1);
            this.algorithm.update(var2);
            byte[] var3 = this.algorithm.sign();
            return Utils.base64(var3);
         } catch (InvalidKeyException var4) {
            throw new XMLSignatureException(var4);
         } catch (SignatureException var5) {
            throw new XMLSignatureException(var5);
         }
      }
   }

   public boolean verify(Key var1, byte[] var2, String var3) throws XMLSignatureException {
      if (!(var1 instanceof PublicKey)) {
         throw new XMLSignatureException(this.getURI() + " verification requires public key");
      } else {
         try {
            this.algorithm.initVerify((PublicKey)var1);
            this.algorithm.update(var2);
            byte[] var4 = Utils.base64(var3);
            return this.algorithm.verify(var4);
         } catch (InvalidKeyException var5) {
            throw new XMLSignatureException(var5);
         } catch (SignatureException var6) {
            return false;
         }
      }
   }

   public void readParameters(XMLStreamReader var1) throws MarshalException {
   }

   public void writeParameters(XMLStreamWriter var1) throws MarshalException {
   }
}
