package weblogic.xml.security.signature;

import java.security.Key;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import weblogic.utils.Debug;
import weblogic.utils.io.UnsyncByteArrayOutputStream;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

class SignedInfo implements DSIGConstants {
   private CanonicalizationMethod c14nMethod;
   private SignatureMethod signatureMethod;
   private final Map references = new HashMap();
   private boolean signatureValidated = false;
   private XMLInputStream signedInfoStream;
   private Map namespaces = new HashMap();
   private int indent;

   SignedInfo() {
      try {
         this.c14nMethod = CanonicalizationMethod.get("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
      } catch (XMLSignatureException var2) {
         throw new AssertionError(var2);
      }
   }

   SignedInfo(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   void addReference(Reference var1) {
      Reference var2 = (Reference)this.references.get(var1.getURI());
      if (var2 != null) {
         var1.copyDigestValue(var2);
      }

      this.references.put(var1.getURI(), var1);
   }

   Iterator getReferences() {
      return this.references.values().iterator();
   }

   void setCanonicalizationMethod(String var1) throws XMLSignatureException {
      this.setCanonicalizationMethod(CanonicalizationMethod.get(var1));
   }

   void setCanonicalizationMethod(CanonicalizationMethod var1) {
      this.c14nMethod = var1;
   }

   void setSignatureMethod(SignatureMethod var1) {
      this.signatureMethod = var1;
   }

   String getSignatureMethodURI() {
      return this.signatureMethod == null ? null : this.signatureMethod.getURI();
   }

   private void digest() throws SignatureGenerationException {
      Iterator var1 = this.getReferences();

      while(var1.hasNext()) {
         Reference var2 = (Reference)var1.next();

         try {
            var2.digest();
         } catch (InvalidReferenceException var4) {
            throw new SignatureGenerationException(var4);
         }
      }

   }

   String sign(Key var1) throws XMLSignatureException {
      if (this.signatureMethod == null) {
         this.signatureMethod = SignatureMethod.get(var1);
      }

      this.digest();
      return this.signatureMethod.sign(var1, this.c14n());
   }

   private byte[] c14n() throws SignatureGenerationException {
      UnsyncByteArrayOutputStream var1 = new UnsyncByteArrayOutputStream();
      XMLOutputStream var2 = this.c14nMethod.canonicalize(var1, this.namespaces);
      NamespaceAwareXOS var3 = new NamespaceAwareXOS(var2);
      var3.addPrefix("http://www.w3.org/2000/09/xmldsig#", "dsig");

      try {
         this.toXML(var3, "http://www.w3.org/2000/09/xmldsig#", -this.indent);
         var2.close(true);
      } catch (XMLStreamException var5) {
         throw new SignatureGenerationException("canonicalization error", var5);
      }

      byte[] var4 = var1.toByteArray();
      if (VERBOSE) {
         System.out.println("<!-- -- Begin c14ned SignedInfo - Sign ----->");
         System.out.println(new String(var4));
         System.out.println("<!-----  End c14ned SignedInfo - Sign  -- -->");
      }

      return var4;
   }

   private byte[] c14n(XMLInputStream var1) throws SignatureValidationException {
      UnsyncByteArrayOutputStream var2 = new UnsyncByteArrayOutputStream();
      XMLOutputStream var3 = this.c14nMethod.canonicalize(var2, this.namespaces);

      try {
         var3.add(var1);
         var3.close(true);
      } catch (XMLStreamException var5) {
         throw new SignatureValidationException("canonicalization error", var5);
      }

      byte[] var4 = var2.toByteArray();
      if (VERBOSE) {
         System.out.println("<!-- -- Begin c14ned SignedInfo - Verify ----->");
         System.out.println(new String(var4));
         System.out.println("<!-----  End c14ned SignedInfo - Verify  -- -->");
      }

      return var4;
   }

   void validate(Key var1, String var2) throws XMLSignatureException {
      if (!this.signatureValidated) {
         this.validateSignature(var1, var2);
      }

      this.validateReferences();
   }

   void validateSignature(Key var1, String var2) throws XMLSignatureException {
      byte[] var3 = this.c14n(this.signedInfoStream);
      if (!this.signatureMethod.verify(var1, var3, var2)) {
         throw new SignatureValidationException("signature failed to verify");
      } else {
         this.signatureValidated = true;
      }
   }

   void validateReferences() throws XMLSignatureException {
      if (!this.signatureValidated) {
         throw new SignatureValidationException("Must validate signature before validating references.");
      } else {
         LinkedList var1 = null;
         Iterator var2 = this.getReferences();

         while(var2.hasNext()) {
            Reference var3 = (Reference)var2.next();

            try {
               if (VERBOSE) {
                  Debug.say(" +++ validating reference : " + var3);
               }

               var3.validate();
               if (VERBOSE) {
                  Debug.say(" +++ validated reference : " + var3);
               }
            } catch (InvalidReferenceException var5) {
               if (VERBOSE) {
                  var5.printStackTrace();
               }

               if (var1 == null) {
                  var1 = new LinkedList();
               }

               var1.add(var5);
            }
         }

         if (var1 != null) {
            String var6 = "One or more references failed to validate";
            InvalidReferenceException[] var4 = new InvalidReferenceException[var1.size()];
            var4 = (InvalidReferenceException[])((InvalidReferenceException[])var1.toArray(var4));
            throw new ReferenceValidationException(var6, var4);
         }
      }
   }

   void setIndent(int var1) {
      this.indent = var1;
   }

   void setNamespaces(Map var1) {
      this.namespaces.clear();
      this.namespaces.putAll(var1);
      if (VERBOSE) {
         Debug.say(" +++ Setting namespaces (" + var1 + ")");
      }

   }

   void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      if (var3 < 0) {
         var3 = -var3;
         StreamUtils.addStart(var1, var2, "SignedInfo");
      } else {
         StreamUtils.addStart(var1, var2, "SignedInfo", var3);
      }

      if (this.c14nMethod != null) {
         this.c14nMethod.toXML(var1, var2, var3 + 2);
      }

      if (this.signatureMethod != null) {
         this.signatureMethod.toXML(var1, var2, var3 + 2);
      }

      Iterator var4 = this.getReferences();

      while(var4.hasNext()) {
         Reference var5 = (Reference)var4.next();
         var5.toXML(var1, var2, var3 + 2);
      }

      StreamUtils.addEnd(var1, var2, "SignedInfo", var3);
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      this.signedInfoStream = var1.getSubStream();
      StartElement var3 = (StartElement)var1.next();
      this.setNamespaces(var3.getNamespaceMap());
      this.c14nMethod = (CanonicalizationMethod)DSIGReader.read(var1, 1);
      this.signatureMethod = (SignatureMethod)DSIGReader.read(var1, 9);

      for(Reference var4 = (Reference)DSIGReader.read(var1, 6); var4 != null; var4 = (Reference)DSIGReader.read(var1, 6)) {
         this.references.put(var4.getURI(), var4);
      }

      StreamUtils.closeScope(var1, var2, "SignedInfo");
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n  <CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments\"></CanonicalizationMethod>\n  <SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#dsa-sha1\"></SignatureMethod>\n  <Reference URI=\"file:///d|/weblogic/dev/sandbox/billjg/dsig/alpha.txt\">\n    <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>\n    <DigestValue>gCVvOanTCGUKyQ2b6acqlWJFRXQ=</DigestValue>\n  </Reference>\n</SignedInfo>\n");
      SignedInfo var2 = (SignedInfo)DSIGReader.read(var1, 10);
      System.out.println(var2);
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2000/09/xmldsig#", 0);
      var3.flush();
   }
}
