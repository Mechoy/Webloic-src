package weblogic.xml.crypto.encrypt;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;
import java.util.Collections;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.AlgorithmMethod;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.KeyInfoObjectFactory;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.keyinfo.KeyInfoObjectBase;
import weblogic.xml.crypto.encrypt.api.CipherReference;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.EncryptionProperties;
import weblogic.xml.crypto.encrypt.api.TBE;
import weblogic.xml.crypto.encrypt.api.TBEKey;
import weblogic.xml.crypto.encrypt.api.XMLDecryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.utils.StaxUtils;

public class WLEncryptedKey extends WLEncryptedType implements EncryptedKey, KeyInfoObjectFactory {
   public static final String TAG_ENCRYPTED_KEY = "EncryptedKey";
   public static final String TAG_CARRIED_KEY_NAME = "CarriedKeyName";
   public static final String ATTR_RECIPIENT = "Recipient";
   private List referenceList;
   private String recipient;
   private String carriedKeyName;
   private KeyWrap encryptionMethod;
   private TBEKey tbeKey;

   public WLEncryptedKey(TBEKey var1, EncryptionMethod var2, KeyInfo var3, EncryptionProperties var4, List var5, String var6, String var7, String var8, CipherReference var9) {
      super(var3, var4, var6, (WLCipherReference)var9);
      this.tbeKey = var1;
      if (var2 instanceof KeyWrap) {
         this.encryptionMethod = (KeyWrap)var2;
         this.referenceList = Collections.unmodifiableList(var5);
         this.carriedKeyName = var7;
         this.recipient = var8;
      } else {
         throw new IllegalArgumentException("Provided EncryptionMethod (" + var2.getAlgorithm() + ") is not a keywrap algorithm," + " as required.");
      }
   }

   WLEncryptedKey() {
   }

   public TBE getTBE() {
      return this.tbeKey;
   }

   public EncryptionMethod getEncryptionMethod() {
      return this.encryptionMethod;
   }

   protected void setEncryptionMethod(WLEncryptionMethod var1) throws XMLEncryptionException {
      if (!(var1 instanceof KeyWrap)) {
         throw new XMLEncryptionException(var1.getAlgorithm() + " cannot be used as a key wrap");
      } else {
         this.encryptionMethod = (KeyWrap)var1;
      }
   }

   void setRecipient(String var1) {
      this.recipient = var1;
   }

   public String getRecipient() {
      return this.recipient;
   }

   void setCarriedKeyName(String var1) {
      this.carriedKeyName = var1;
   }

   public String getCarriedKeyName() {
      return this.carriedKeyName;
   }

   public List getReferenceList() {
      return this.referenceList;
   }

   void setReferenceList(List var1) {
      this.referenceList = Collections.unmodifiableList(var1);
   }

   public Key decryptKey(XMLDecryptContext var1) throws XMLEncryptionException {
      return new SecretKeySpec(this.decryptBytes(var1), (String)null);
   }

   public Key decryptKey(XMLDecryptContext var1, AlgorithmMethod var2) throws XMLEncryptionException {
      if (!(var2 instanceof EncryptionAlgorithm)) {
         throw new XMLEncryptionException(var2.getAlgorithm() + " cannot be used as a block cipher.");
      } else {
         byte[] var3 = this.decryptBytes(var1);
         return ((EncryptionAlgorithm)var2).createKey(var3);
      }
   }

   private byte[] decryptBytes(XMLDecryptContext var1) throws XMLEncryptionException {
      Key var2 = this.getKey(KeySelector.Purpose.DECRYPT, var1);
      if (var2 == null) {
         throw new XMLEncryptionException("Unable to select key from context");
      } else {
         WLCipherData var3 = (WLCipherData)this.getCipherData();
         byte[] var4 = var3.getCipherBytes();
         byte[] var5 = this.encryptionMethod.decrypt(var2, var4);
         return var5;
      }
   }

   public InputStream decrypt(XMLDecryptContext var1) throws XMLEncryptionException {
      return new ByteArrayInputStream(this.decryptBytes(var1));
   }

   public void encrypt(XMLEncryptContext var1) throws XMLEncryptionException, MarshalException {
      Key var2 = this.getKey(KeySelector.Purpose.ENCRYPT, var1);
      if (var2 == null) {
         throw new XMLEncryptionException("Unable to select key from context");
      } else {
         byte[] var3 = this.tbeKey.getKey().getEncoded();
         byte[] var4 = this.encryptionMethod.encrypt(var2, var3);
         WLCipherData var5 = (WLCipherData)this.getCipherData();
         var5.setCipherText(new ByteArrayInputStream(var4));
         this.marshal(var1);
         this.tbeKey = null;
      }
   }

   public String getLocalName() {
      return "EncryptedKey";
   }

   public String getNamespace() {
      return "http://www.w3.org/2001/04/xmlenc#";
   }

   protected void readChildren(XMLStreamReader var1) throws MarshalException {
      try {
         this.referenceList = ReferenceList.read(var1);
         this.carriedKeyName = StaxUtils.getElementValue(var1, "http://www.w3.org/2001/04/xmlenc#", "CarriedKeyName");
      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }
   }

   protected void readAttributes(XMLStreamReader var1) {
      this.recipient = StaxUtils.getAttributeValue("http://www.w3.org/2001/04/xmlenc#", "Recipient", var1);
   }

   protected void writeAttributes(XMLStreamWriter var1) throws MarshalException {
      if (this.recipient != null) {
         try {
            var1.writeAttribute("Recipient", this.recipient);
         } catch (XMLStreamException var3) {
            throw new MarshalException(var3);
         }
      }

   }

   protected void writeChildren(XMLStreamWriter var1) throws MarshalException {
      try {
         if (this.referenceList != null && this.referenceList.size() > 0) {
            ReferenceList.write(var1, this.referenceList);
         }

         if (this.carriedKeyName != null) {
            var1.writeStartElement(this.getNamespace(), "CarriedKeyName");
            var1.writeCharacters(this.carriedKeyName);
            var1.writeEndElement();
         }

      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }
   }

   public String childrenToString() {
      return ", encryptionMethod=" + this.encryptionMethod + ", carriedKeyName='" + this.carriedKeyName + "'" + ", recipient='" + this.recipient + "'" + ", referenceList=" + this.referenceList;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public static void init() {
      KeyInfoObjectBase.register(new WLEncryptedKey());
   }

   public QName getQName() {
      return new QName("http://www.w3.org/2001/04/xmlenc#", "EncryptedKey");
   }

   public Object newKeyInfoObject(XMLStreamReader var1) throws MarshalException {
      WLEncryptedKey var2 = new WLEncryptedKey();
      var2.readKeyInfo(var1);
      return var2;
   }
}
