package weblogic.xml.crypto.encrypt;

import java.io.InputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorException;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.keyinfo.KeyInfoImpl;
import weblogic.xml.crypto.encrypt.api.CipherData;
import weblogic.xml.crypto.encrypt.api.EncryptedType;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.EncryptionProperties;
import weblogic.xml.crypto.encrypt.api.TBE;
import weblogic.xml.crypto.encrypt.api.XMLDecryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.dom.DOMEncryptContext;
import weblogic.xml.crypto.encrypt.api.dom.DOMTBEXML;
import weblogic.xml.crypto.utils.StaxUtils;
import weblogic.xml.crypto.wss11.internal.WSS11Constants;
import weblogic.xml.dom.DOMStreamWriter;
import weblogic.xml.stax.OutputConfigurationContext;

public abstract class WLEncryptedType implements WLXMLStructure, EncryptedType {
   public static final String ATTR_ENCODING = "Encoding";
   public static final String ATTR_ID = "Id";
   public static final String ATTR_MIME_TYPE = "MimeType";
   public static final String ATTR_TYPE = "Type";
   public static final int TYPE_ELEMENT = 1;
   public static final int TYPE_CONTENT = 2;
   public static final int TYPE_OTHER = 3;
   private KeyInfo keyInfo;
   private WLCipherData cipherData;
   private EncryptionProperties encryptionProperties;
   private String id;
   private String type;
   private String mimeType;
   private String encoding;
   private static final String XMLDSIG_KEYINFO = "KeyInfo";
   private KeySelectorResult keyResult;
   protected static final OutputConfigurationContext WRITER_CONFIG_CTX = new OutputConfigurationContext();

   protected WLEncryptedType(KeyInfo var1, EncryptionProperties var2, String var3, WLCipherReference var4) {
      this.keyInfo = var1;
      this.encryptionProperties = var2;
      this.id = var3;
      if (var4 != null) {
         this.cipherData = WLCipherData.newInstance(var4);
      } else {
         this.cipherData = WLCipherData.newInstance();
      }

   }

   protected WLEncryptedType() {
   }

   public abstract String getLocalName();

   public abstract String getNamespace();

   protected abstract void readChildren(XMLStreamReader var1) throws MarshalException;

   protected abstract void readAttributes(XMLStreamReader var1) throws MarshalException;

   protected abstract void writeAttributes(XMLStreamWriter var1) throws MarshalException;

   protected abstract void writeChildren(XMLStreamWriter var1) throws MarshalException;

   protected abstract String childrenToString();

   public abstract EncryptionMethod getEncryptionMethod();

   protected abstract void setEncryptionMethod(WLEncryptionMethod var1) throws XMLEncryptionException;

   public InputStream getCipherText() {
      return this.cipherData.getCipherText();
   }

   public EncryptionProperties getEncryptionProperties() {
      return this.encryptionProperties;
   }

   public abstract TBE getTBE();

   public KeyInfo getKeyInfo() {
      return this.keyInfo;
   }

   public void setKeyInfo(KeyInfo var1) {
      try {
         this.keyInfo = var1;
      } catch (ClassCastException var3) {
         throw new IllegalArgumentException("Unknown KeyInfo type " + var1);
      }
   }

   public CipherData getCipherData() {
      return this.cipherData;
   }

   protected void setCipherData(WLCipherData var1) {
      this.cipherData = var1;
   }

   public void clear() {
      this.cipherData.clear();
   }

   protected void setEncryptionProperties(EncryptionProperties var1) {
      this.encryptionProperties = var1;
   }

   protected void setMimeType(String var1) {
      this.mimeType = var1;
   }

   protected void setEncoding(String var1) {
      this.encoding = var1;
   }

   public String getType() {
      return this.type;
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public String getEncoding() {
      return this.encoding;
   }

   protected void setId(String var1) {
      this.id = var1;
   }

   public String getId() {
      return this.id;
   }

   public String toString() {
      TBE var1 = this.getTBE();
      String var2 = "weblogic.xml.crypto.encrypt.api.EncryptedType{keyInfo=" + this.keyInfo + ", cipherData=" + this.cipherData + ", id='" + this.id + "'";
      if (var1 != null) {
         var2 = var2 + ", mimeType='" + this.getTBE().getMimeType() + "'" + ", encoding='" + this.getTBE().getEncoding() + "'";
      } else {
         var2 = var2 + ", mimeType='" + this.mimeType + "'" + ", encoding='" + this.encoding + "'";
      }

      var2 = var2 + this.childrenToString() + "}";
      return var2;
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      this.readKeyInfo(var1);

      try {
         while(var1.hasNext() && !var1.isStartElement() && !var1.isEndElement()) {
            var1.next();
         }

      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }
   }

   public void readKeyInfo(XMLStreamReader var1) throws MarshalException {
      try {
         StaxUtils.findStart(var1, "http://www.w3.org/2001/04/xmlenc#", this.getLocalName(), true);

         for(int var2 = 0; var2 < var1.getAttributeCount(); ++var2) {
            String var3 = var1.getAttributeNamespace(var2);
            if (var3 != null && var3.length() != 0 && !var3.equals("http://www.w3.org/2001/04/xmlenc#")) {
               throw new MarshalException("XML Encryption does not allow non-native attributes: " + var3 + ":" + var1.getAttributeLocalName(var2));
            }

            String var4 = var1.getAttributeLocalName(var2);
            String var5 = var1.getAttributeValue(var2);
            if ("Type".equals(var4)) {
               this.setType(var5);
            } else if ("Id".equals(var4)) {
               if (var3 != null || this.id == null) {
                  this.id = var5;
               }
            } else if ("MimeType".equals(var4)) {
               this.mimeType = var5;
            } else if ("Encoding".equals(var4)) {
               this.encoding = var5;
            }
         }

         this.readAttributes(var1);
         var1.next();
         if (StaxUtils.findStart(var1, "http://www.w3.org/2001/04/xmlenc#", "EncryptionMethod")) {
            try {
               this.setEncryptionMethod(WLEncryptionMethod.newInstance(var1));
            } catch (XMLEncryptionException var6) {
               throw new MarshalException(var6);
            }
         }

         if (StaxUtils.findStart(var1, "http://www.w3.org/2000/09/xmldsig#", "KeyInfo")) {
            this.keyInfo = new KeyInfoImpl();
            ((KeyInfoImpl)this.keyInfo).read(var1);
            var1.nextTag();
         }

         this.cipherData = WLCipherData.newInstance(var1);
         this.readChildren(var1);
         StaxUtils.findEnd(var1, this.getNamespace(), this.getLocalName());
      } catch (XMLStreamException var7) {
         throw new MarshalException(var7);
      }
   }

   protected void setType(String var1) {
      this.type = var1;
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement(this.getNamespace(), this.getLocalName());
         if (this.id != null) {
            var1.writeAttribute("Id", this.id);
         }

         String var2 = this.getType();
         if (var2 != null) {
            var1.writeAttribute("Type", var2);
         }

         String var3 = this.getMimeType();
         if (var3 != null) {
            var1.writeAttribute("MimeType", var3);
         }

         if (!"http://www.w3.org/2001/04/xmlenc#Element".equals(var2) && !"http://www.w3.org/2001/04/xmlenc#Content".equals(var2)) {
            String var4 = this.getEncoding();
            if (var4 != null) {
               var1.writeAttribute("Encoding", var4);
            }

            this.writeAttributes(var1);
         }

         WLEncryptionMethod var6 = (WLEncryptionMethod)this.getEncryptionMethod();
         if (var6 != null) {
            var6.write(var1);
         }

         if (this.keyInfo != null) {
            ((KeyInfoImpl)this.keyInfo).write(var1);
         }

         this.cipherData.write(var1);
         this.writeChildren(var1);
         var1.writeEndElement();
         this.clear();
      } catch (XMLStreamException var5) {
         throw new MarshalException(var5);
      }
   }

   public abstract InputStream decrypt(XMLDecryptContext var1) throws XMLEncryptionException;

   public abstract void encrypt(XMLEncryptContext var1) throws XMLEncryptionException, MarshalException;

   protected Key getKey(KeySelector.Purpose var1, XMLCryptoContext var2) throws XMLEncryptionException {
      if (this.keyResult == null) {
         try {
            KeySelector var3 = var2.getKeySelector();
            if (var3 == null) {
               throw new XMLEncryptionException("No KeySelector available on context");
            }

            KeySelectorResult var4 = var3.select(this.getKeyInfo(), var1, this.getEncryptionMethod(), var2);
            if (var4 == null) {
               throw new XMLEncryptionException("Unable to resolve encryption key for " + this);
            }

            this.keyResult = var4;
            var2.setProperty("weblogic.xml.crypto.ksr", var4);
         } catch (KeySelectorException var5) {
            throw new XMLEncryptionException(var5);
         }
      }

      return this.keyResult.getKey();
   }

   public static EncryptedType newInstance(XMLStreamReader var0) throws MarshalException {
      int var1 = var0.getEventType();
      if (var1 != 1) {
         throw new MarshalException("Did not receive valid XML for an EncryptedType");
      } else {
         if (var0.getName().equals(WSS11Constants.ENC_HEADER_QNAME)) {
            try {
               do {
                  var0.nextTag();
               } while(var0.getEventType() != 1);
            } catch (XMLStreamException var5) {
               throw new MarshalException(var5);
            }
         }

         String var2 = var0.getLocalName();
         String var3 = var0.getNamespaceURI();
         if (!var3.equals("http://www.w3.org/2001/04/xmlenc#")) {
            throw new MarshalException("Unsupported namespace (" + var3 + ") for EncryptedType");
         } else if ("EncryptedData".equals(var2)) {
            WLEncryptedData var6 = new WLEncryptedData();
            var6.read(var0);
            return var6;
         } else if ("EncryptedKey".equals(var2)) {
            WLEncryptedKey var4 = new WLEncryptedKey();
            var4.read(var0);
            return var4;
         } else {
            throw new MarshalException("Unknown EncryptedType; " + var2);
         }
      }
   }

   protected void marshal(XMLEncryptContext var1) throws MarshalException {
      if (var1 instanceof DOMEncryptContext) {
         DOMEncryptContext var2 = (DOMEncryptContext)var1;
         Node var3 = var2.getParent();
         Node var4;
         List var5;
         if (var3 != null) {
            var4 = var2.getNextSibling();
            var5 = null;
         } else {
            try {
               DOMTBEXML var6 = (DOMTBEXML)this.getTBE();
               NodeList var7 = var6.getNodeList();
               var5 = copyNodes(var7);
               var3 = var7.item(0).getParentNode();
               var4 = var7.item(var7.getLength() - 1);
            } catch (ClassCastException var10) {
               throw new MarshalException("Unable to identify destination for marhaling");
            }
         }

         Document var11 = var3.getOwnerDocument();
         DOMStreamWriter var12 = new DOMStreamWriter(var11, var3, var4);
         var12.setConfigurationContext(WLEncryptedData.WRITER_CONFIG_CTX);
         this.write(var12);
         if (var5 != null) {
            for(int var8 = 0; var8 < var5.size(); ++var8) {
               Node var9 = (Node)var5.get(var8);
               var3.removeChild(var9);
            }
         }
      }

   }

   private static List copyNodes(NodeList var0) {
      ArrayList var1 = new ArrayList(var0.getLength());

      for(int var2 = 0; var2 < var0.getLength(); ++var2) {
         Node var3 = var0.item(var2);
         var1.add(var3);
      }

      return var1;
   }

   static {
      WRITER_CONFIG_CTX.setPrefixDefaulting(true);
   }
}
