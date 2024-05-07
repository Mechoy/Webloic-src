package weblogic.xml.crypto.wss;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.crypto.dsig.DsigConstants;
import weblogic.xml.crypto.dsig.KeyInfoObjectFactory;
import weblogic.xml.crypto.dsig.api.keyinfo.X509IssuerSerial;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.api.KeyIdentifier;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.crypto.wss11.internal.WSS11Constants;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeySTR;
import weblogic.xml.dom.Builder;
import weblogic.xml.dom.ElementNode;
import weblogic.xml.dom.marshal.MarshalException;
import weblogic.xml.security.utils.Utils;

public class SecurityTokenReferenceImpl implements SecurityTokenReference, KeyInfoObjectFactory {
   private static final String ID_PREFIX = "str";
   private static final QName TOKEN_TYPE_QNAME = new QName("http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd", "TokenType");
   private static final ConcurrentHashMap factories = new ConcurrentHashMap();
   private String id;
   private QName STRType;
   private String valueType;
   private String uri;
   private KeyIdentifier keyId;
   private SecurityToken token;

   public SecurityTokenReferenceImpl() {
   }

   public SecurityTokenReferenceImpl(QName var1, String var2, SecurityToken var3) {
      this.STRType = var1;
      this.valueType = var2;
      this.token = var3;
      this.id = DOMUtils.generateId("str");
   }

   private static final void initFactories() {
      BinarySecurityTokenReference.init();
      UsernameTokenReference.init();
      EncryptedKeySTR.init();
   }

   public static void register(SecurityTokenHandler var0) {
      String[] var1 = var0.getValueTypes();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         factories.put(var1[var2], var0);
      }

      QName[] var4 = var0.getQNames();

      for(int var3 = 0; var3 < var4.length; ++var3) {
         factories.put(var4[var3], var0);
      }

   }

   public String getValueType() {
      return this.valueType;
   }

   public QName getSTRType() {
      return this.STRType;
   }

   public void setSTRType(QName var1) {
      this.STRType = var1;
   }

   public X509IssuerSerial getIssuerSerial() {
      return null;
   }

   public void setValueType(String var1) {
      this.valueType = var1;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public String getUsage() {
      return null;
   }

   public KeyIdentifier getKeyIdentifier() {
      return this.keyId;
   }

   public void setKeyIdentifier(KeyIdentifier var1) {
      this.keyId = var1;
   }

   public String getReferenceURI() {
      return this.uri;
   }

   public void setReferenceURI(String var1) {
      this.uri = var1;
   }

   public SecurityToken getSecurityToken() {
      return this.token;
   }

   public void marshal(Element var1, Node var2, Map var3) throws MarshalException {
      if (this.STRType.equals(WSSConstants.REFERENCE_QNAME)) {
         this.marshalDirectRef(var1, var2, var3, this.uri);
      } else if (this.STRType.equals(WSSConstants.KEY_IDENTIFIER_QNAME)) {
         this.marshalKeyIdRef(var1, var2, var3, this.keyId);
      } else if (this.STRType.equals(WSSConstants.EMBEDDED_QNAME)) {
         this.marshalEmbeddedRef(var1, var2, var3, this.token);
      }

   }

   public void unmarshal(Node var1) throws MarshalException {
      this.unmarshalInternal(var1);
      Element var2 = DOMUtils.getFirstElement(var1);
      this.STRType = DOMUtils.getQName(var2);
      if (this.STRType.equals(WSSConstants.REFERENCE_QNAME)) {
         this.unmarshalDirectRef(var2);
      } else if (this.STRType.equals(WSSConstants.KEY_IDENTIFIER_QNAME)) {
         this.unmarshalKeyIdRef(var2);
      } else {
         if (!this.STRType.equals(WSSConstants.EMBEDDED_QNAME)) {
            throw new MarshalException("Unrecognized child element in " + WSSConstants.STR_QNAME);
         }

         this.unmarshalEmbeddedRef(var2);
      }

   }

   protected void unmarshalInternal(Node var1) {
      this.id = DOMUtils.getAttributeValue((Element)var1, WSSConstants.WSU_ID_QNAME);
   }

   public static SecurityTokenReference createAndUnmarshal(Node var0) throws MarshalException {
      SecurityTokenHandler var1 = null;
      Object var2 = null;
      Element var3 = (Element)var0;
      String var4 = DOMUtils.getAttributeValue(var3, TOKEN_TYPE_QNAME);
      if (var4 != null) {
         var1 = (SecurityTokenHandler)factories.get(var4);
      }

      if (var1 != null) {
         return unmarshalSTR(var1, var0);
      } else {
         Element var5 = DOMUtils.getFirstElement(var3);
         QName var6 = DOMUtils.getQName(var5);
         if (!WSSConstants.REFERENCE_QNAME.equals(var6) && !WSSConstants.KEY_IDENTIFIER_QNAME.equals(var6)) {
            if (WSSConstants.EMBEDDED_QNAME.equals(var6)) {
               Element var12 = DOMUtils.getFirstElement(var5);
               QName var13 = DOMUtils.getQName(var12);
               var1 = (SecurityTokenHandler)factories.get(var13);
            } else {
               if (DsigConstants.KEYNAME_QNAME.equals(var6)) {
                  throw new MarshalException("Failed to unmarshal " + WSSConstants.STR_QNAME + ", " + DsigConstants.KEYNAME_QNAME + " not supported.");
               }

               var1 = (SecurityTokenHandler)factories.get(var6);
               if (var1 == null) {
                  throw new MarshalException("Failed to unmarshal " + WSSConstants.STR_QNAME + ", " + var6 + " not supported.");
               }
            }
         } else {
            String var7 = DOMUtils.getAttributeValue(var5, WSSConstants.VALUE_TYPE_QNAME);
            var1 = (SecurityTokenHandler)factories.get(var7);
            if (var1 == null && WSSConstants.KEY_IDENTIFIER_QNAME.equals(var6)) {
               throw new MarshalException("Failed to unmarshal " + WSSConstants.STR_QNAME + ", no SecurityTokenReference factory " + "found for " + var6 + " " + WSSConstants.VALUE_TYPE_QNAME + ": " + var7);
            }

            if (WSSConstants.REFERENCE_QNAME.equals(var6)) {
               String var8 = DOMUtils.getAttributeValue(var5, WSSConstants.URI_QNAME);
               WSSecurityContext var9 = WSSecurityContext.getCurrentContext();
               if (var9 != null && var1 == null) {
                  Element var10 = var9.getElementById(var8.startsWith("#") ? var8.substring(1) : var8);
                  if (var10 != null) {
                     QName var11 = DOMUtils.getQName(var10);
                     var1 = (SecurityTokenHandler)factories.get(var11);
                  }
               }
            }
         }

         return unmarshalSTR(var1, var0);
      }
   }

   private static SecurityTokenReference unmarshalSTR(SecurityTokenHandler var0, Node var1) throws MarshalException {
      Object var2;
      if (var0 == null) {
         var2 = new SecurityTokenReferenceImpl();
      } else {
         var2 = var0.newSecurityTokenReference(var1);
      }

      ((SecurityTokenReference)var2).unmarshal(var1);
      return (SecurityTokenReference)var2;
   }

   public QName getQName() {
      return WSSConstants.STR_QNAME;
   }

   public Object newKeyInfoObject(XMLStreamReader var1) throws weblogic.xml.crypto.api.MarshalException {
      SecurityTokenReference var2 = null;

      try {
         ElementNode var3 = new ElementNode();
         Builder.read(var3, var1);
         var2 = createAndUnmarshal(var3);
         return var2;
      } catch (MarshalException var4) {
         throw new weblogic.xml.crypto.api.MarshalException(var4);
      } catch (XMLStreamException var5) {
         throw new weblogic.xml.crypto.api.MarshalException(var5);
      }
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   protected Element marshalInternal(Element var1, Node var2, Map var3) {
      String var4 = getPrefix(var3, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
      String var5 = getPrefix(var3, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu");
      Element var6 = DOMUtils.createElement(var1, WSSConstants.STR_QNAME, var4);
      DOMUtils.declareNamespace(var6, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
      if (this.id != null) {
         DOMUtils.addPrefixedAttribute(var6, WSSConstants.WSU_ID_QNAME, var5, this.id);
         DOMUtils.declareNamespace(var6, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", var5);
      }

      this.marshalAttributes(var6, var3);
      String var7 = (String)var3.get(this.STRType.getNamespaceURI());
      if (var7 == null) {
         var3.put(this.STRType.getNamespaceURI(), "strtype");
      }

      Element var8 = DOMUtils.createAndAddElement(var6, this.STRType, var7);
      if (var2 != null) {
         var1.insertBefore(var6, var2);
      } else {
         var1.appendChild(var6);
      }

      return var8;
   }

   protected void marshalAttributes(Element var1, Map var2) {
      String var3 = null;
      if (this.token != null) {
         var3 = this.token.getValueType();
      }

      if (var3 != null) {
         String var4 = getPrefix(var2, WSS11Constants.TOKEN_TYPE_QNAME.getNamespaceURI(), "wsse11");
         DOMUtils.addPrefixedAttribute(var1, WSS11Constants.TOKEN_TYPE_QNAME, var4, var3);
         DOMUtils.declareNamespace(var1, WSS11Constants.TOKEN_TYPE_QNAME.getNamespaceURI(), var4);
      }

   }

   protected static String getPrefix(Map var0, String var1, String var2) {
      String var3 = (String)var0.get(var1);
      if (var3 == null) {
         var0.put(var1, var2);
         var3 = var2;
      }

      return var3;
   }

   public Node marshalDirectRef(Element var1, Node var2, Map var3, String var4) {
      Element var5 = this.marshalInternal(var1, var2, var3);
      DOMUtils.addAttribute(var5, WSSConstants.VALUE_TYPE_QNAME, var3, this.valueType);
      DOMUtils.addAttribute(var5, WSSConstants.URI_QNAME, var3, var4);
      return var5.getParentNode();
   }

   public Node marshalEmbeddedRef(Element var1, Node var2, Map var3, SecurityToken var4) throws MarshalException {
      Element var5 = this.marshalInternal(var1, var2, var3);
      var4.marshal(var5, (Node)null, var3);
      return var5.getParentNode();
   }

   public Node marshalKeyIdRef(Element var1, Node var2, Map var3, KeyIdentifier var4) throws MarshalException {
      Element var5 = this.marshalInternal(var1, var2, var3);
      DOMUtils.addAttribute(var5, WSSConstants.VALUE_TYPE_QNAME, var3, this.valueType);
      String var6 = var4.getEncodingType();
      DOMUtils.addAttribute(var5, WSSConstants.ENCODING_TYPE_QNAME, var3, var6);
      DOMUtils.addText(var5, Utils.toBase64(var4.getIdentifier()));
      return var5.getParentNode();
   }

   public void unmarshalDirectRef(Element var1) {
      this.uri = DOMUtils.getAttributeValue(var1, WSSConstants.URI_QNAME);
      this.valueType = DOMUtils.getAttributeValue(var1, WSSConstants.VALUE_TYPE_QNAME);
      if (this.valueType == null) {
         WSSecurityContext var2 = WSSecurityContext.getCurrentContext();
         if (var2 != null) {
            Element var3 = var2.getElementById(this.uri.startsWith("#") ? this.uri.substring(1) : this.uri);
            if (var3 != null) {
               SecurityToken var4 = var2.getToken(var3);
               if (var4 != null) {
                  this.valueType = var4.getValueType();
               }
            }
         }
      }

   }

   public void unmarshalKeyIdRef(Element var1) throws MarshalException {
      this.valueType = DOMUtils.getAttributeValue(var1, WSSConstants.VALUE_TYPE_QNAME);
      String var2 = DOMUtils.getAttributeValue(var1, WSSConstants.ENCODING_TYPE_QNAME);
      if (var2 != null && !var2.equals("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary")) {
         throw new MarshalException("Unsupported " + WSSConstants.ENCODING_TYPE_QNAME + ": " + var2);
      } else {
         this.keyId = new KeyIdentifierImpl(Utils.base64(DOMUtils.getText(var1)));
      }
   }

   public void unmarshalEmbeddedRef(Element var1) throws MarshalException {
   }

   public static Node getStrNode(SecurityTokenReference var0) throws weblogic.xml.crypto.api.MarshalException {
      Document var1 = null;

      try {
         var1 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      } catch (ParserConfigurationException var6) {
         throw new weblogic.xml.crypto.api.MarshalException("Failed to write element SecurityTokenReference", var6);
      } catch (FactoryConfigurationError var7) {
         throw new weblogic.xml.crypto.api.MarshalException("Failed to write element SecurityTokenReference", var7);
      }

      Element var2 = var1.createElementNS("foo", "bar");
      var1.appendChild(var2);
      HashMap var3 = new HashMap();
      var3.put("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
      var3.put("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu");

      try {
         var0.marshal(var2, (Node)null, var3);
      } catch (MarshalException var5) {
         throw new weblogic.xml.crypto.api.MarshalException("Failed to write element SecurityTokenReference");
      }

      Node var4 = var2.getLastChild();
      var2.removeChild(var4);
      return var4;
   }

   static {
      initFactories();
   }
}
