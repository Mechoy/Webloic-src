package weblogic.xml.security.signature;

import java.security.Key;
import java.util.Iterator;
import java.util.Map;
import weblogic.xml.security.keyinfo.KeyInfo;
import weblogic.xml.security.keyinfo.KeyInfoValidationException;
import weblogic.xml.security.keyinfo.KeyPurpose;
import weblogic.xml.security.keyinfo.KeyResolver;
import weblogic.xml.security.keyinfo.KeyResolverException;
import weblogic.xml.security.keyinfo.KeyResult;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public final class Signature implements DSIGConstants {
   private SignedInfo signedInfo;
   private KeyInfo keyInfo;
   private String signatureValue;
   private KeyResult validatingKey = null;
   private XMLInputStream objectSubstream;

   public Signature() {
      this.signedInfo = new SignedInfo();
   }

   Signature(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public void setSignatureMethod(String var1) throws XMLSignatureException {
      this.signedInfo.setSignatureMethod(SignatureMethod.get(var1));
   }

   public String getSignatureMethod() {
      return this.signedInfo.getSignatureMethodURI();
   }

   public void setCanonicalizationMethod(String var1) throws XMLSignatureException {
      this.signedInfo.setCanonicalizationMethod(var1);
   }

   public void setCanonicalizationMethod(CanonicalizationMethod var1) {
      this.signedInfo.setCanonicalizationMethod(var1);
   }

   public void setKeyInfo(KeyInfo var1) {
      this.keyInfo = var1;
   }

   public KeyInfo getKeyInfo() throws KeyInfoValidationException {
      if (this.keyInfo != null) {
         this.keyInfo.validate();
      }

      return this.keyInfo;
   }

   public void addReference(Reference var1) {
      this.signedInfo.addReference(var1);
   }

   public Iterator getReferences() {
      return this.signedInfo.getReferences();
   }

   public void sign(Key var1) throws XMLSignatureException {
      this.signatureValue = this.signedInfo.sign(var1);
   }

   public void validate(Key var1) throws XMLSignatureException {
      this.signedInfo.validate(var1, this.signatureValue);
   }

   private void validateSignature(KeyResult var1) throws XMLSignatureException {
      this.signedInfo.validateSignature(var1.getKey(), this.signatureValue);
      this.validatingKey = var1;
   }

   public void validateSignature(KeyResolver var1) throws XMLSignatureException {
      try {
         this.validateSignature(var1.resolveKey(KeyPurpose.VERIFY, this.getSignatureMethod(), this.getKeyInfo()));
      } catch (KeyResolverException var3) {
         throw new SignatureValidationException("Could not resolve key for signature " + this, var3);
      } catch (KeyInfoValidationException var4) {
         throw new SignatureValidationException("Could not valid keyInfo for signature " + this, var4);
      }
   }

   public void validateReferences() throws XMLSignatureException {
      this.signedInfo.validateReferences();
   }

   public KeyResult getValidatingKey() {
      return this.validatingKey;
   }

   void setIndent(int var1) {
      this.signedInfo.setIndent(var1 + 2);
   }

   void setNamespaces(Map var1) {
      this.signedInfo.setNamespaces(var1);
      Iterator var2 = this.getReferences();

      while(var2.hasNext()) {
         Reference var3 = (Reference)var2.next();
         if (var3 instanceof InternalReference) {
            ((InternalReference)var3).setNamespaces(var1);
         }
      }

   }

   private EnvelopingReference getEnvelopingReference() {
      Iterator var1 = this.getReferences();

      Object var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = var1.next();
      } while(!(var2 instanceof EnvelopingReference));

      return (EnvelopingReference)var2;
   }

   XMLInputStream getEmbeddedObject() {
      return this.objectSubstream;
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      StreamUtils.addStart(var1, var2, "Signature");
      this.signedInfo.toXML(var1, var2, var3 + 2);
      StreamUtils.addElement(var1, var2, "SignatureValue", (String)this.signatureValue, var3 + 2, 2);
      if (this.keyInfo != null) {
         this.keyInfo.toXML(var1, var2, var3 + 2);
      }

      EnvelopingReference var4 = this.getEnvelopingReference();
      if (var4 != null) {
         var4.writeEnveloped(var1, var2);
      }

      StreamUtils.addEnd(var1, var2, "Signature", var3);
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      var1.next();
      this.signedInfo = (SignedInfo)DSIGReader.read(var1, 10);
      StreamUtils.required(this.signedInfo, "Signature", "SignedInfo");
      this.signatureValue = StreamUtils.getValue(var1, var2, "SignatureValue");
      StreamUtils.required(this.signatureValue, "Signature", "SignatureValue");
      this.keyInfo = (KeyInfo)DSIGReader.read(var1, 4);
      if (StreamUtils.peekElement(var1, var2, "Object")) {
         this.objectSubstream = var1.getSubStream();
         var1.skipElement();
      }

      while(StreamUtils.peekElement(var1, var2, "Object")) {
         var1.skipElement();
      }

      StreamUtils.closeScope(var1, var2, "Signature");
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n  <SignedInfo>\n    <CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"></CanonicalizationMethod>\n    <SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#dsa-sha1\"></SignatureMethod>\n    <Reference URI=\"file:///d|/weblogic/dev/sandbox/billjg/dsig/alpha.txt\">\n      <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>      <DigestValue>gCVvOanTCGUKyQ2b6acqlWJFRXQ=</DigestValue>\n    </Reference>\n  </SignedInfo>\n  <SignatureValue>\n    TIthi3ukKCZ5sRkDlcijVAuHAX4kHq24NXGbS4Hk+RTbOxAjxKdVqw==\n  </SignatureValue>\n  <KeyInfo>\n    <X509Data>\n      <X509Certificate>\nMIIDJTCCAuMCBD1ROTgwCwYHKoZIzjgEAwUAMHgxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpOZXcgSmVyc2V5MRcwFQYDVQQHEw5MaWJlcnR5IENvcm5lcjEMMAoGA1UEChMDQkVBMRQwEgYDVQQLEwtFbmdpbmVlcmluZzEXMBUGA1UEAxMOQmlsbCBHYWxsYWdoZXIwHhcNMDIwODA3MTUxNDAwWhcNMDIxMTA1MTUxNDAwWjB4MQswCQYDVQQGEwJVUzETMBEGA1UECBMKTmV3IEplcnNleTEXMBUGA1UEBxMOTGliZXJ0eSBDb3JuZXIxDDAKBgNVBAoTA0JFQTEUMBIGA1UECxMLRW5naW5lZXJpbmcxFzAVBgNVBAMTDkJpbGwgR2FsbGFnaGVyMIIBuDCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYUAAoGBANOS8gWryNFVnLtJDhGmCL/O9yKducC6kz1HJ3+yYhsvL8nlsojxCpy4kJoNOqTx4iUssInrw6xA7eI8E8QWENAh8IzynOClENm8lZHZNRrLxoqNYNUEnfcyYNLlQsnOoRFoG+Z1qpHIlSqIOkljregQmGQQUZqJZgyniRdAUV11MAsGByqGSM44BAMFAAMvADAsAhREg+kPv4wa3OG2ruOADwOv+VWrewIUQVUbn8cAEImOJ/0iChsTiKV86q0=\n      </X509Certificate>\n    </X509Data>\n  </KeyInfo>\n</Signature>\n");
      Signature var2 = (Signature)DSIGReader.read(var1, 8);
      System.out.println(var2);
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2000/09/xmldsig#", 0);
      var3.flush();
   }
}
