package weblogic.xml.security.encryption;

import java.security.Key;
import java.security.PublicKey;
import weblogic.xml.security.keyinfo.KeyPurpose;
import weblogic.xml.security.keyinfo.KeyResolverException;
import weblogic.xml.security.keyinfo.KeyResult;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class EncryptedKey extends EncryptedType {
   private ReferenceList referenceList;
   private String recipient;
   private String carriedKeyName;
   private KeyWrap encryptionMethod;
   private Key wrappedKey;

   public EncryptedKey(PublicKey var1) throws EncryptionException {
      this(var1, (String)"http://www.w3.org/2001/04/xmlenc#tripledes-cbc", "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p");
   }

   public EncryptedKey(Key var1, String var2, String var3) throws EncryptionException {
      EncryptionMethod var4 = EncryptionMethod.get(var2);
      if (!(var4 instanceof EncryptionAlgorithm)) {
         throw new EncryptionException(var2 + " cannot be used as a block cipher");
      } else {
         this.setKey(var1);
         this.setEncryptionMethod(var3);
         this.setWrappedKey(((EncryptionAlgorithm)var4).generateKey());
      }
   }

   public EncryptedKey(Key var1, Key var2, String var3) throws EncryptionException {
      this.setKey(var1);
      this.setEncryptionMethod(var3);
      this.setWrappedKey(var2);
   }

   public EncryptedKey(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public void setEncryptionMethod(EncryptionMethod var1) throws EncryptionException {
      if (!(var1 instanceof KeyWrap)) {
         throw new EncryptionException(var1.getURI() + " cannot be used as a key wrap");
      } else {
         this.encryptionMethod = (KeyWrap)var1;
      }
   }

   EncryptionMethod getEncryptionMethodInternal() {
      return this.encryptionMethod;
   }

   public void setRecipient(String var1) {
      this.recipient = var1;
   }

   public String getRecipient() {
      return this.recipient;
   }

   public void setCarriedKeyName(String var1) {
      this.carriedKeyName = var1;
   }

   public String getCarriedKeyName() {
      return this.carriedKeyName;
   }

   public ReferenceList getReferenceList() {
      return this.referenceList;
   }

   public void setReferenceList(ReferenceList var1) {
      this.referenceList = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("EncryptedKey").append(": \n");
      super.toString(var1);
      return var1.toString();
   }

   public Key getWrappedKey() {
      return this.wrappedKey;
   }

   public Key getWrappedKey(EncryptionMethod var1) throws EncryptionException {
      if (this.wrappedKey == null) {
         this.wrappedKey = getWrappedKey(var1, this.getWrappedKeyBytes());
      }

      return this.wrappedKey;
   }

   public static Key getWrappedKey(EncryptionMethod var0, byte[] var1) throws EncryptionException {
      if (!(var0 instanceof EncryptionAlgorithm)) {
         throw new EncryptionException(var0.getURI() + " cannot be used as a block cipher.");
      } else {
         EncryptionAlgorithm var2 = (EncryptionAlgorithm)var0;
         return var2.createKey(var1);
      }
   }

   protected final byte[] getWrappedKeyBytes() throws EncryptionException {
      Key var1 = this.getKey();
      if (var1 == null) {
         KeyResult var2 = null;

         try {
            var2 = this.resolveKey(KeyPurpose.DECRYPT, this.encryptionMethod.getURI(), this.getKeyInfo());
         } catch (KeyResolverException var4) {
            throw new EncryptionException("Unable to resolve key wrapping key", var4);
         }

         var1 = var2.getKey();
      }

      byte[] var5 = this.encryptionMethod.unwrap(var1, this.getCipherData().getCipherBytes());
      return var5;
   }

   public void setWrappedKey(Key var1) throws EncryptionException {
      this.wrappedKey = var1;
      Key var2 = this.getKey();
      if (var2 == null) {
         throw new EncryptionException("encrypting key not set");
      } else {
         byte[] var3 = var1.getEncoded();
         if (this.encryptionMethod == null) {
            this.encryptionMethod = EncryptionMethod.getKeyWrap(var2.getAlgorithm());
         }

         byte[] var4 = this.encryptionMethod.wrap(var2, var3);
         this.getCipherData().setCipherBytes(var4);
      }
   }

   public void toXML(XMLOutputStream var1, int var2) throws XMLStreamException {
      this.toXML(var1, "http://www.w3.org/2001/04/xmlenc#", var2);
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      super.writeCommon(var2, "EncryptedKey", var1, var3);
      if (this.referenceList != null) {
         this.referenceList.toXML(var1, var2, var3 + 2);
      }

      if (this.carriedKeyName != null) {
         StreamUtils.addElement(var1, var2, "CarriedKeyName", this.carriedKeyName, var3 + 2);
      }

      StreamUtils.addEnd(var1, var2, "EncryptedKey", var3);
   }

   void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = super.readCommon(var1, var2);
      this.recipient = StreamUtils.getAttribute(var3, "Recipient");
      this.referenceList = ReferenceList.fromXML(var1, var2);
      this.carriedKeyName = StreamUtils.getValue(var1, var2, "CarriedKeyName");
      StreamUtils.closeScope(var1, var2, "EncryptedKey");
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<EncryptedKey Id=\"EK\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">\n  <EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#kw-tripledes\"/>\n  <ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n    <ds:KeyName>my-tripledes-key</ds:KeyName>\n  </ds:KeyInfo>\n  <CipherData>\n    <CipherValue>HgVuHoXxBQWD9fvi0gt9TanywZ5lJokM/12fcMG6gRoMjsCPulH+4A==</CipherValue>\n  </CipherData>\n</EncryptedKey>\n");
      EncryptedKey var2 = new EncryptedKey(var1, "http://www.w3.org/2001/04/xmlenc#");
      var2.setKeyResolver(TestUtils.getDESKeyResolver());
      System.out.println(var2);
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2001/04/xmlenc#", 0);
      var3.flush();
   }
}
