package weblogic.xml.security.encryption;

import java.security.Key;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.xml.security.keyinfo.KeyInfo;
import weblogic.xml.security.keyinfo.KeyPurpose;
import weblogic.xml.security.keyinfo.KeyResolver;
import weblogic.xml.security.keyinfo.KeyResolverException;
import weblogic.xml.security.keyinfo.KeyResult;
import weblogic.xml.security.signature.DSIGReader;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class EncryptedType implements XMLEncConstants {
   public static final String TYPE_ELEMENT_URI = "http://www.w3.org/2001/04/xmlenc#Element";
   public static final String TYPE_CONTENT_URI = "http://www.w3.org/2001/04/xmlenc#Content";
   public static final int TYPE_ELEMENT = 1;
   public static final int TYPE_CONTENT = 2;
   public static final int TYPE_OTHER = 3;
   KeyInfo keyInfo;
   CipherData cipherData;
   String id;
   int type;
   String mimeType;
   String encoding;
   private KeyResolver keyResolver;
   private Key key;
   protected Map nsMap;

   abstract EncryptionMethod getEncryptionMethodInternal();

   public abstract void setEncryptionMethod(EncryptionMethod var1) throws EncryptionException;

   public void setEncryptionMethod(String var1) throws EncryptionException {
      this.setEncryptionMethod(EncryptionMethod.get(var1));
   }

   public String getEncryptionMethod() {
      EncryptionMethod var1 = this.getEncryptionMethodInternal();
      return var1 == null ? null : var1.getURI();
   }

   public void setType(String var1) {
      if ("http://www.w3.org/2001/04/xmlenc#Element".equals(var1)) {
         this.type = 1;
      } else if ("http://www.w3.org/2001/04/xmlenc#Content".equals(var1)) {
         this.type = 2;
      } else {
         this.type = 3;
      }

   }

   public void setType(int var1) {
      this.type = var1;
   }

   public int getType() {
      return this.type;
   }

   public String getTypeURI() {
      switch (this.type) {
         case 1:
            return "http://www.w3.org/2001/04/xmlenc#Element";
         case 2:
            return "http://www.w3.org/2001/04/xmlenc#Content";
         default:
            return null;
      }
   }

   public void setKeyResolver(KeyResolver var1) {
      this.keyResolver = var1;
   }

   public KeyResolver getKeyResolver() {
      return this.keyResolver;
   }

   KeyResult resolveKey(KeyPurpose var1, String var2, KeyInfo var3) throws EncryptionException, KeyResolverException {
      if (var3 != null) {
         Iterator var4 = var3.getEncryptedKeys();
         if (var4.hasNext()) {
            EncryptedKey var5 = (EncryptedKey)var4.next();
            var5.setKeyResolver(this.keyResolver);
            Key var6 = var5.getWrappedKey(this.getEncryptionMethodInternal());
            return new KeyResult(var6);
         }
      }

      if (this.keyResolver != null) {
         return this.keyResolver.resolveKey(var1, this.getEncryptionMethod(), var3);
      } else {
         throw new EncryptionException("Cannot resolve key for: " + var3);
      }
   }

   public KeyInfo getKeyInfo() {
      return this.keyInfo;
   }

   public void setKeyInfo(KeyInfo var1) {
      this.keyInfo = var1;
   }

   public CipherData getCipherData() {
      if (this.cipherData == null) {
         this.cipherData = new CipherValue();
      }

      return this.cipherData;
   }

   public void setCipherData(CipherData var1) {
      this.cipherData = var1;
   }

   public void clear() {
      this.cipherData.clear();
   }

   public void setKey(Key var1) {
      this.key = var1;
   }

   public void setKey(EncryptedKey var1) {
      this.setKey(var1.getWrappedKey());
   }

   public Key getKey() {
      return this.key;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public String getId() {
      return this.id;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("EncryptedType").append(":\n");
      this.toString(var1);
      return var1.toString();
   }

   void toString(StringBuffer var1) {
      var1.append("  ").append(this.getEncryptionMethod()).append("\n").append("  ").append(this.keyInfo).append("\n").append("  ").append(this.cipherData);
   }

   void writeCommon(String var1, String var2, XMLOutputStream var3, int var4) throws XMLStreamException {
      Attribute[] var5 = new Attribute[]{StreamUtils.createAttribute("Id", this.id), StreamUtils.createAttribute("Type", this.getTypeURI()), StreamUtils.createAttribute("MimeType", this.mimeType), StreamUtils.createAttribute("Encoding", this.encoding)};
      if (this instanceof EncryptedData && this.getType() == 1) {
         StreamUtils.addStart(var3, var1, var2, var5);
      } else {
         StreamUtils.addStart(var3, var1, var2, var5, var4);
      }

      EncryptionMethod var6 = this.getEncryptionMethodInternal();
      if (var6 != null) {
         var6.toXML(var3, var1, var4 + 2);
      }

      if (this.keyInfo != null) {
         this.keyInfo.toXML(var3, var4 + 2);
      }

      this.cipherData.toXML(var3, var1, var4 + 2);
   }

   StartElement readCommon(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)var1.next();
      this.nsMap = new HashMap();
      this.nsMap.putAll(var3.getNamespaceMap());
      this.id = StreamUtils.getAttribute(var3, "Id");
      this.setType(StreamUtils.getAttribute(var3, "Type"));
      this.mimeType = StreamUtils.getAttribute(var3, "MimeType");
      this.encoding = StreamUtils.getAttribute(var3, "Encoding");
      EncryptionMethod var4 = (EncryptionMethod)XMLEncReader.read(var1, 3);
      this.setEncryptionMethod(var4);
      this.keyInfo = (KeyInfo)DSIGReader.read(var1, 4);
      this.cipherData = CipherData.fromXML(var1, var2);
      StreamUtils.required(this.cipherData, "EncryptedType", "CipherData");
      return var3;
   }
}
