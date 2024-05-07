package weblogic.xml.crypto.wss;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.saml.SAML2Constants;
import weblogic.wsee.security.saml.SAMLConstants;
import weblogic.xml.crypto.NodeURIDereferencer;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.common.keyinfo.EncryptedKeyProvider;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.common.keyinfo.KeyResolver;
import weblogic.xml.crypto.dom.WLDOMSignContextImpl;
import weblogic.xml.crypto.dom.WLDOMValidateContextImpl;
import weblogic.xml.crypto.dsig.DsigConstants;
import weblogic.xml.crypto.dsig.XMLSignatureImpl;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.XMLSignature;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.encrypt.ReferenceList;
import weblogic.xml.crypto.encrypt.api.DataReference;
import weblogic.xml.crypto.encrypt.api.EncryptedData;
import weblogic.xml.crypto.encrypt.api.KeyReference;
import weblogic.xml.crypto.encrypt.api.ReferenceType;
import weblogic.xml.crypto.encrypt.api.XMLEncryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.encrypt.api.dom.DOMDecryptContext;
import weblogic.xml.crypto.encrypt.api.dom.DOMEncryptContext;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.api.Security;
import weblogic.xml.crypto.wss.api.Timestamp;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeyToken;
import weblogic.xml.dom.DOMStreamReader;
import weblogic.xml.dom.DOMStreamWriter;
import weblogic.xml.dom.marshal.WLDOMStructure;

public class SecurityImpl implements Security, WLDOMStructure {
   public static final QName ENCRYPTED_KEY_QNAME = new QName("http://www.w3.org/2001/04/xmlenc#", "EncryptedKey");
   public static final QName ENCRYPTED_DATA_QNAME = new QName("http://www.w3.org/2001/04/xmlenc#", "EncryptedData");
   public static final QName REFERENCE_LIST_QNAME = new QName("http://www.w3.org/2001/04/xmlenc#", "ReferenceList");
   private static final String TRUE = "1";
   private static final String LOCAL_URI_PREFIX = "#";
   private static final String SIGNATURE_LOCALNAME = "Signature";
   private WSSecurityContext securityCtx;
   protected Element security;
   protected Map namespaces;
   protected Map elementHandlers = new HashMap();
   public static final String VERBOSE_PROPERTY = "weblogic.xml.crypto.wss.verbose";
   public static final boolean VERBOSE = Boolean.getBoolean("weblogic.xml.crypto.wss.verbose");

   public SecurityImpl() {
   }

   public SecurityImpl(WSSecurityContext var1) {
      this.securityCtx = var1;
   }

   public QName getActor() {
      return null;
   }

   private void processAndMarshal(ContextHandler var1) throws MarshalException {
      if (this.securityCtx == null) {
         this.securityCtx = (WSSecurityContext)var1.getValue("com.bea.contextelement.xml.SecurityInfo");
      }

      try {
         this.marshal((Element)this.securityCtx.getNode(), this.securityCtx.getNextSibling(), this.securityCtx.getNamespaces());
      } catch (weblogic.xml.dom.marshal.MarshalException var3) {
         throw new MarshalException(var3);
      }
   }

   public void add(XMLStructure var1, XMLCryptoContext var2, ContextHandler var3) throws WSSecurityException, MarshalException {
      if (this.security == null) {
         this.processAndMarshal(var3);
      }

      this.processAndMarshal(var1, var2, var3);
   }

   public Node add(XMLSignature var1, KeyProvider var2, ContextHandler var3) throws WSSecurityException, MarshalException {
      if (this.security == null) {
         this.processAndMarshal(var3);
      }

      Node var4 = this.processAndMarshalSignature(var1, var2, var3);
      if (isEndoringEncryptSignature(var3)) {
         DOMUtils.assignId((Element)var4, new QName("", "Id"), "", this.securityCtx.getIdQNames());
         ((SecurityTokenContextHandler)var3).addContextElement("weblogic.wsee.security.signature_node", var4);
      }

      return var4;
   }

   protected void processAndMarshal(XMLStructure var1, XMLCryptoContext var2, ContextHandler var3) throws WSSecurityException, MarshalException {
      if (var1 instanceof Timestamp) {
         this.processAndMarshalTimestamp((Timestamp)var1, var3);
      } else if (var1 instanceof EncryptedKeyToken) {
         EncryptedKeyToken var4 = (EncryptedKeyToken)var1;
         DOMEncryptContext var5 = var4.getDOMEncryptContext();
         if (var5 != null) {
            this.processAndMarshalEncryptedKey(var4.getEncryptedKey(), var5, var3);
         } else {
            this.processAndMarshalSecurityToken(var4, var3);
         }
      } else if (var1 instanceof SecurityToken) {
         this.processAndMarshalSecurityToken((SecurityToken)var1, var3);
      } else if (var1 instanceof EncryptedKey) {
         this.processAndMarshalEncryptedKey((EncryptedKey)var1, (XMLEncryptContext)var2, var3);
      } else if (var1 instanceof SecurityTokenReference) {
         this.processAndMarshalSTR((SecurityTokenReference)var1, var3);
      } else if (var1 instanceof ReferenceList) {
         this.processAndMarshalReferenceList((ReferenceList)var1, var3);
      }

   }

   private void processAndMarshalReferenceList(ReferenceList var1, ContextHandler var2) throws MarshalException {
      Node var3 = this.findReferenceInsertBeforeNode(this.security, var2);
      Document var4 = this.security.getOwnerDocument();
      DOMStreamWriter var5 = new DOMStreamWriter(var4, this.security);
      List var6 = var1.getReferences();
      if (var6 != null && var6.size() > 0) {
         ReferenceList.write(var5, var6);
      }

      Node var7 = this.security.getLastChild();
      if (var3 != null) {
         this.security.removeChild(var7);
         this.security.insertBefore(var7, var3);
      }

   }

   private void processAndMarshalSTR(SecurityTokenReference var1, ContextHandler var2) throws MarshalException {
      try {
         Node var3 = this.findInsertBeforeNode(this.security, var2, false);
         var1.marshal(this.security, var3, this.namespaces);
         getInsertedNode(this.security, var2, var3, false);
      } catch (weblogic.xml.dom.marshal.MarshalException var4) {
         throw new MarshalException(var4);
      }
   }

   public void marshal(Element var1, Node var2, Map var3) throws weblogic.xml.dom.marshal.MarshalException {
      this.namespaces = var3;
      String var4 = var1 != null ? var1.getNamespaceURI() : null;
      boolean var5 = "http://www.w3.org/2003/05/soap-envelope".equals(var4);
      String var6 = (String)var3.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
      if (var6 == null) {
         var6 = "wsse";
      }

      try {
         this.security = getSecurityHeader(var1, getRoleAttrName((SOAPMessageContext)null), getRole((SOAPMessageContext)null));
      } catch (WSSecurityException var8) {
         throw new weblogic.xml.dom.marshal.MarshalException(var8);
      }

      if (this.security == null) {
         this.security = DOMUtils.createAndAddElement(var1, WSSConstants.SECURITY_QNAME, var6);
         declareNamespaces(this.security, var3);
         setMustUnderstand(this.security, var3, var5);
      }

      if (var2 != null) {
         var1.insertBefore(this.security, var2);
      } else {
         var1.appendChild(this.security);
      }

      if (this.securityCtx == null) {
         this.securityCtx = new WSSecurityContext(var1, (Node)null, (Set)null, (Map)null);
      }

      this.securityCtx.setSecurityElement(this.security);
   }

   public static void setMustUnderstand(Element var0, Map var1, boolean var2) {
      String var3;
      if (var2) {
         var3 = (String)var1.get("http://www.w3.org/2003/05/soap-envelope");
         QName var4 = new QName("http://www.w3.org/2003/05/soap-envelope", "mustUnderstand");
         DOMUtils.addPrefixedAttribute(var0, var4, var3, "true");
      } else {
         var3 = (String)var1.get("http://schemas.xmlsoap.org/soap/envelope/");
         DOMUtils.addPrefixedAttribute(var0, WSSConstants.MUST_UNDERSTAND_QNAME, var3, "1");
      }

   }

   private void processAndMarshalTimestamp(Timestamp var1, ContextHandler var2) throws MarshalException {
      Node var3;
      if (isTimestampFirst(var2)) {
         var3 = this.security.getFirstChild();
      } else {
         var3 = this.security.getLastChild();
      }

      try {
         var1.marshal(this.security, var3, this.namespaces);
      } catch (weblogic.xml.dom.marshal.MarshalException var5) {
         throw new MarshalException(var5);
      }

      if (isTimestampFirst(var2)) {
         setFirstTokenNode(var2, this.security.getFirstChild());
      }

   }

   private void processAndMarshalSecurityToken(SecurityToken var1, ContextHandler var2) throws MarshalException, WSSecurityException {
      SecurityTokenHandler var3 = this.securityCtx.getRequiredTokenHandler(var1.getValueType());
      KeyProvider var4 = var3.getKeyProvider(var1, this.securityCtx.getMessageContext());
      if (var4 != null) {
         this.securityCtx.addKeyProvider(var4);
      }

      LogUtils.logWss("Adding KeyProvider (outbound) to WSSecurityContext: " + var4 + "\nfor token (type: " + var1.getValueType() + ") ", var1);

      try {
         Node var5 = this.findInsertBeforeNode(this.security, var2, true);
         var1.marshal(this.security, var5, this.namespaces);
         Element var6 = (Element)getInsertedNode(this.security, var2, var5, true);
         this.securityCtx.addSecurityToken(var1);
         this.securityCtx.addToken(var1, var6);
      } catch (weblogic.xml.dom.marshal.MarshalException var7) {
         throw new MarshalException(var7);
      }
   }

   private Node processAndMarshalSignature(XMLSignature var1, KeyProvider var2, ContextHandler var3) throws WSSecurityException, MarshalException {
      Node var4 = this.findSignatureInsertBeforeNode(this.security, var1, var3);
      WLDOMSignContextImpl var5 = new WLDOMSignContextImpl((Key)null, this.security, var4);
      KeyResolver var6 = new KeyResolver(new KeyProvider[]{var2});
      var5.setKeySelector(var6);

      try {
         var5.setProperty("weblogic.xml.crypto.wss.WSSecurityContext", this.securityCtx);
         if (isEndoringEncryptSignature(var3)) {
            Element var7 = (Element)var3.getValue("weblogic.wsee.security.signature_node");
            if (null != var7) {
               var5.setProperty("weblogic.wsee.security.signature_node", var7);
            }
         }

         var1.sign(var5);
         this.securityCtx.addSignature(var1);
         return (Element)getInsertedNode(this.security, var3, var4, false);
      } catch (XMLSignatureException var8) {
         throw new WSSecurityException("Failed to process signature." + var8.getMessage(), var8);
      }
   }

   private void processAndMarshalEncryptedKey(EncryptedKey var1, XMLEncryptContext var2, ContextHandler var3) throws WSSecurityException, MarshalException {
      try {
         Node var4 = this.findInsertBeforeNode(this.security, var3, true);
         ((DOMEncryptContext)var2).setParent(this.security);
         ((DOMEncryptContext)var2).setNextSibling(var4);
         var1.encrypt(var2);
         getInsertedNode(this.security, var3, var4, true);
      } catch (XMLEncryptionException var5) {
         throw new WSSecurityException(var5);
      }
   }

   public void unmarshal(Node var1) throws weblogic.xml.dom.marshal.MarshalException {
      if (var1 == null) {
         throw new weblogic.xml.dom.marshal.MarshalException("Node to unmarshal security object from must not be null.");
      } else {
         if (this.securityCtx == null) {
            this.securityCtx = new WSSecurityContext(var1);
         }

         this.unmarshalInternal(var1);
      }
   }

   public void unmarshal(WSSecurityContext var1) throws weblogic.xml.dom.marshal.MarshalException {
      if (var1 == null) {
         throw new weblogic.xml.dom.marshal.MarshalException("Context to unmarshal security object from must not be null.");
      } else {
         this.securityCtx = var1;
         Element var2 = this.getSecurityHeader(var1);
         if (var2 != null) {
            this.unmarshalInternal(var2);
         }

      }
   }

   public void unmarshal(SOAPMessageContext var1) throws weblogic.xml.dom.marshal.MarshalException {
      if (var1 == null) {
         throw new weblogic.xml.dom.marshal.MarshalException("Context to unmarshal security object from must not be null.");
      } else {
         this.securityCtx = WSSecurityContext.getSecurityContext(var1);
         WSSecurityContext.pushContext(this.securityCtx);

         try {
            Element var2 = getSecurityHeader(var1);
            if (var2 != null) {
               this.unmarshalInternal(var2);
            }
         } catch (SOAPException var7) {
            throw new weblogic.xml.dom.marshal.MarshalException(var7);
         } catch (WSSecurityException var8) {
            throw new weblogic.xml.dom.marshal.MarshalException(var8);
         } finally {
            WSSecurityContext.popContext();
         }

      }
   }

   private void unmarshalInternal(Node var1) throws weblogic.xml.dom.marshal.MarshalException {
      QName var2 = DOMUtils.getQName(var1);
      if (!var2.equals(WSSConstants.SECURITY_QNAME)) {
         throw new weblogic.xml.dom.marshal.MarshalException("QName " + var2 + " of node to unmarshal Security object from does not match " + WSSConstants.SECURITY_QNAME);
      } else {
         this.securityCtx.setSecurityElement((Element)var1);
         Element var3 = DOMUtils.getFirstElement(var1);

         try {
            this.unmarshalChildren(var3, var1);
            this.validateSecurityTokens(this.securityCtx);
            this.validateHandlers(this.securityCtx);
            if (var1.getParentNode() != null) {
               var1.getParentNode().removeChild(var1);
            }

         } catch (WSSecurityException var5) {
            throw new weblogic.xml.dom.marshal.MarshalException(var5);
         } catch (MarshalException var6) {
            throw new weblogic.xml.dom.marshal.MarshalException(var6);
         }
      }
   }

   protected void unmarshalChildren(Element var1, Node var2) throws weblogic.xml.dom.marshal.MarshalException, WSSecurityException, MarshalException {
      Element var3 = null;
      Element var4 = var1;

      Object var5;
      QName var8;
      for(var5 = null; var4 != null; var4 = DOMUtils.getNextElement(var4)) {
         QName var6 = DOMUtils.getQName(var4);
         if (var6.equals(WSSConstants.BST_QNAME)) {
            this.unmarshalAndProcessSecurityToken(var4, var6, this.securityCtx);
         } else if (this.isSAMLQName(var6)) {
            Element var7 = DOMUtils.getFirstElement(var4);
            if (var7 != null) {
               var8 = DOMUtils.getQName(var7);
               if (!var8.equals(ENCRYPTED_DATA_QNAME)) {
                  this.unmarshalAndProcessSecurityToken(var4, var6, this.securityCtx);
               }
            }
         } else if (var6.equals(ENCRYPTED_KEY_QNAME) && var5 == null) {
         }
      }

      var4 = var1;
      boolean var11 = true;

      while(true) {
         while(var4 != null) {
            QName var12 = DOMUtils.getQName(var4);
            if (var12.equals(WSSConstants.TIMESTAMP_QNAME)) {
               this.unmarshalAndProcessTimestamp(var4, this.securityCtx);
            } else if (var12.equals(WSSConstants.UNT_QNAME)) {
               this.unmarshalAndProcessSecurityToken(var4, var12, this.securityCtx);
            } else if (var12.equals(DsigConstants.SIGNATURE_QNAME)) {
               try {
                  var8 = null;
                  this.unmarshalAndProcessSignature(var4, this.securityCtx);
               } catch (WSSecurityException var10) {
                  if (null == var5) {
                     throw var10;
                  }

                  LogUtils.logDsig("Got error on " + var10.getMessage() + "Try again with Encrypted Key!");
                  this.unmarshalAndProcessSignature(var4, this.securityCtx, (KeySelector)var5);
               }
            } else if (var12.equals(REFERENCE_LIST_QNAME)) {
               var11 = false;
               List var13 = this.unmarshalReferenceList(var4);
               this.processReferenceList(var13, this.securityCtx);
            } else if (var12.equals(ENCRYPTED_KEY_QNAME)) {
               var11 = false;
               this.unmarshalAndProcessEncryptedKey(var4, this.securityCtx);
            } else {
               if (var12.equals(ENCRYPTED_DATA_QNAME)) {
                  var11 = false;
                  this.unmarshalAndProcessEncryptedData(var4, this.securityCtx);
                  if (var3 != null) {
                     var4 = (Element)var3.getNextSibling();
                  } else {
                     var4 = DOMUtils.getFirstElement(var2);
                  }
                  continue;
               }

               if (var12.equals(WSSConstants.STR_QNAME)) {
                  var11 = false;
               } else if (this.elementHandlers.containsKey(var12)) {
                  var11 = false;
                  SecurityHeaderElementHandler var14 = (SecurityHeaderElementHandler)this.elementHandlers.get(var12);
                  var14.process(var4, this.securityCtx);
               } else {
                  var11 = false;
                  this.unmarshalAndProcessSecurityToken(var4, var12, this.securityCtx);
               }
            }

            var3 = var4;
            var4 = DOMUtils.getNextElement(var4);
         }

         return;
      }
   }

   private boolean isSAMLQName(QName var1) {
      int var2;
      for(var2 = 0; var2 < SAMLConstants.SAML_ASST_QNAMES.length; ++var2) {
         if (var1.equals(SAMLConstants.SAML_ASST_QNAMES[var2])) {
            return true;
         }
      }

      for(var2 = 0; var2 < SAML2Constants.SAML2_ASST_QNAMES.length; ++var2) {
         if (var1.equals(SAML2Constants.SAML2_ASST_QNAMES[var2])) {
            return true;
         }
      }

      return false;
   }

   private void validateSecurityTokens(WSSecurityContext var1) throws WSSecurityException {
      List var2 = var1.getCurrentTokens();
      Iterator var3 = var2.iterator();

      SecurityTokenValidateResult var8;
      do {
         if (!var3.hasNext()) {
            return;
         }

         SecurityToken var4 = (SecurityToken)var3.next();
         String var5 = var4.getValueType();
         SecurityTokenHandler var6 = var1.getRequiredTokenHandler(var5);
         MessageContext var7 = var1.getMessageContext();
         var8 = var6.validateProcessed(var4, var7);
      } while(var8.status());

      throw new WSSecurityException("Security token failed to validate.", var8, WSSConstants.FAILURE_TOKEN_INVALID);
   }

   private Timestamp unmarshalAndProcessTimestamp(Element var1, WSSecurityContext var2) throws weblogic.xml.dom.marshal.MarshalException {
      TimestampImpl var3 = new TimestampImpl();
      var3.unmarshal(var1);
      var2.setTimestamp(var3);
      return var3;
   }

   private SecurityToken unmarshalAndProcessSecurityToken(Node var1, QName var2, WSSecurityContext var3) throws weblogic.xml.dom.marshal.MarshalException, WSSecurityException {
      SecurityTokenHandler var4 = var3.getRequiredTokenHandler(var2);
      if (var4 == null) {
         throw new weblogic.xml.dom.marshal.MarshalException("Unsupported security token " + var2);
      } else {
         SecurityToken var5 = null;

         try {
            var5 = var4.newSecurityToken(var1);
         } catch (MarshalException var8) {
            Object var7 = var8;
            if (var8.getCause() instanceof WSSecurityException) {
               var7 = var8.getCause();
            }

            throw new weblogic.xml.dom.marshal.MarshalException("Failed to unmarshal " + var2, (Throwable)var7);
         }

         SecurityTokenValidateResult var6 = var4.validateUnmarshalled(var5, var3.getMessageContext());
         if (!var6.status()) {
            throw new WSSecurityException("Security token failed to validate.", var6, WSSConstants.FAILURE_TOKEN_INVALID);
         } else {
            var3.addSecurityToken(var5);
            var3.addToken(var5, (Element)var1);
            LogUtils.logWss("Adding token to WSSecurityContext: ", var5);
            KeyProvider var9 = var4.getKeyProvider(var5, var3.getMessageContext());
            if (var9 != null) {
               var3.addKeyProvider(var9);
            }

            LogUtils.logWss("Adding KeyProvider (inbound) to WSSecurityContext : " + var9 + "\nfor token (type: " + var5.getValueType() + ") ", var5);
            return var5;
         }
      }
   }

   private XMLSignature unmarshalAndProcessSignature(Node var1, WSSecurityContext var2) throws WSSecurityException {
      return this.unmarshalAndProcessSignature(var1, var2, var2.getKeySelector());
   }

   private void p(String var1) {
   }

   private XMLSignature unmarshalAndProcessSignature(Node var1, WSSecurityContext var2, KeySelector var3) throws WSSecurityException {
      if (null == var3) {
         var3 = var2.getKeySelector();
      }

      WLDOMValidateContextImpl var4 = new WLDOMValidateContextImpl(var3, var1);
      URIDereferencer var5 = var2.getURIDereferencer();
      if (var5 != null) {
         var4.setURIDereferencer(var5);
      }

      Set var6 = var2.getIdQNames();
      if (var6 != null) {
         var4.setProperty("weblogic.xml.crypto.idqnames", var6);
      }

      MessageContext var7 = var2.getMessageContext();
      var4.setProperty("javax.xml.rpc.handler.MessageContext", var7);
      var4.setProperty("weblogic.xml.crypto.wss.WSSecurityContext", var2);
      XMLSignature var8 = null;

      try {
         XMLSignatureFactory var9 = var2.getSignatureFactory();
         var8 = var9.unmarshalXMLSignature(var4);
         boolean var10 = var8.validate(var4);
         if (!var10) {
            throw new WSSecurityException("Signature failed to validate.", ((XMLSignatureImpl)var8).getSignatureValidateResult().toFaultString(), WSSConstants.FAILURE_VERIFY_OR_DECRYPT);
         }
      } catch (MarshalException var11) {
         throw new WSSecurityException("Failed to unmarshal signature.", var11);
      } catch (XMLSignatureException var12) {
         throw new WSSecurityException("Failed to validate signature.", var12);
      } catch (Throwable var13) {
         throw new WSSecurityException(var13.getMessage());
      }

      this.p("\n\n +++ ADD Signature to SecurityCOntext");
      var2.addSignature(var8);
      this.p("\n\n +++ ADD Signature to SecurityCOntext DONE");
      return var8;
   }

   private void processReferenceList(List var1, WSSecurityContext var2) throws WSSecurityException, MarshalException {
      Iterator var3 = var1.iterator();

      while(true) {
         ReferenceType var4;
         String var6;
         Element var7;
         label38:
         do {
            while(var3.hasNext()) {
               var4 = (ReferenceType)var3.next();
               String var5 = var4.getURI();
               var6 = var4.getType();
               var7 = var2.getElementById(var5.substring(1));
               if (var7 == null) {
                  throw new WSSecurityException("Failed to resolve DataReference.", WSSConstants.FAILURE_INVALID);
               }

               if (!"DataReference".equals(var6) && !(var4 instanceof DataReference)) {
                  continue label38;
               }

               KeySelector var8 = var2.getKeySelector();
               DOMDecryptContext var9 = new DOMDecryptContext(var8, var7);
               XMLEncryptionFactory var10 = var2.getEncryptionFactory();

               try {
                  EncryptedData var11 = (EncryptedData)var10.unmarshalEncryptedType(var9);
                  Element var12 = (Element)var7.getParentNode();
                  Node var13 = var7.getNextSibling();
                  Node var14 = var7.getPreviousSibling();
                  this.decrypt(var11, var7, var9);
                  List var15 = this.getInsertedNodes(var12, var14, var13);
                  KeySelectorResult var16 = (KeySelectorResult)var9.getProperty("weblogic.xml.crypto.ksr");
                  Encryption var17 = new Encryption(var11, (EncryptedKey)null, var15, var16);
                  var2.addEncryption(var17);
               } catch (XMLEncryptionException var18) {
                  throw new WSSecurityException(var18, WSSConstants.FAILURE_VERIFY_OR_DECRYPT);
               }
            }

            return;
         } while(!"KeyReference".equals(var6) && !(var4 instanceof KeyReference));

         this.unmarshalAndProcessEncryptedKey(var7, var2);
      }
   }

   protected boolean isHeader(Node var1) {
      return false;
   }

   protected void handleEncryptedHeader(Node var1) {
   }

   private KeyResolver unmarshalEncryptedKeyOnly(Element var1, WSSecurityContext var2) throws WSSecurityException, MarshalException {
      KeySelector var3 = var2.getKeySelector();
      DOMDecryptContext var4 = new DOMDecryptContext(var3, var1);
      MessageContext var5 = var2.getMessageContext();
      var4.setProperty("javax.xml.rpc.handler.MessageContext", var5);
      var4.setProperty("weblogic.xml.crypto.wss.WSSecurityContext", var2);
      XMLEncryptionFactory var6 = var2.getEncryptionFactory();
      EncryptedKey var7 = (EncryptedKey)var6.unmarshalEncryptedType(var4);
      EncryptedKeyProvider var8 = this.handleEncryptedKey(var7, var4, var2);
      KeyResolver var9 = new KeyResolver();
      ((KeyResolver)var9).addKeyProvider(var8);
      return (KeyResolver)var9;
   }

   private void unmarshalAndProcessEncryptedKey(Element var1, WSSecurityContext var2) throws WSSecurityException, MarshalException {
      KeySelector var3 = var2.getKeySelector();
      DOMDecryptContext var4 = new DOMDecryptContext(var3, var1);
      MessageContext var5 = var2.getMessageContext();
      var4.setProperty("javax.xml.rpc.handler.MessageContext", var5);
      var4.setProperty("weblogic.xml.crypto.wss.WSSecurityContext", var2);
      XMLEncryptionFactory var6 = var2.getEncryptionFactory();
      EncryptedKey var7 = (EncryptedKey)var6.unmarshalEncryptedType(var4);
      EncryptedKeyProvider var8 = this.handleEncryptedKey(var7, var4, var2);
      KeyResolver var9 = new KeyResolver();
      ((KeyResolver)var9).addKeyProvider(var8);
      List var10 = var7.getReferenceList();
      if (var10 != null) {
         Iterator var11 = var10.iterator();

         while(var11.hasNext()) {
            DataReference var12 = (DataReference)var11.next();

            try {
               Element var13 = var2.getElementById(var12.getURI().substring(1));
               if (var13 == null) {
                  throw new WSSecurityException("Failed to resolve DataReference.", WSSConstants.FAILURE_INVALID);
               }

               DOMDecryptContext var14 = new DOMDecryptContext(var9, var13);
               var14.setProperty("javax.xml.rpc.handler.MessageContext", var5);
               EncryptedData var15 = (EncryptedData)var6.unmarshalEncryptedType(var14);
               Element var16 = (Element)var13.getParentNode();
               Node var17 = var13.getNextSibling();
               Node var18 = var13.getPreviousSibling();
               this.decrypt(var15, var13, var14);
               List var19 = this.getInsertedNodes(var16, var18, var17);
               KeySelectorResult var20 = (KeySelectorResult)var14.getProperty("weblogic.xml.crypto.ksr");
               Encryption var21 = new Encryption(var15, var7, var19, var20);
               var2.addEncryption(var21);
            } catch (XMLEncryptionException var22) {
               throw new WSSecurityException(var22, WSSConstants.FAILURE_VERIFY_OR_DECRYPT);
            }
         }
      }

   }

   private void decrypt(EncryptedData var1, Element var2, DOMDecryptContext var3) throws XMLEncryptionException {
      if (this.isHeader(var2)) {
         this.handleEncryptedHeader(var2);
      }

      var1.decryptAndReplace(var3);
      NodeURIDereferencer.resetParsedFlag(this.securityCtx);
   }

   protected EncryptedKeyProvider handleEncryptedKey(EncryptedKey var1, DOMDecryptContext var2, WSSecurityContext var3) throws WSSecurityException {
      EncryptedKeyProvider var4 = null;

      try {
         var4 = new EncryptedKeyProvider(var1, var2);
      } catch (XMLEncryptionException var6) {
         throw new WSSecurityException(var6, WSSConstants.FAILURE_INVALID);
      }

      var3.addKeyProvider(var4);
      return var4;
   }

   private void unmarshalAndProcessEncryptedData(Element var1, WSSecurityContext var2) throws WSSecurityException {
      KeySelector var3 = var2.getKeySelector();
      DOMDecryptContext var4 = new DOMDecryptContext(var3, var1);
      XMLEncryptionFactory var5 = var2.getEncryptionFactory();

      try {
         EncryptedData var6 = (EncryptedData)var5.unmarshalEncryptedType(var4);
         var6.decryptAndReplace(var4);
      } catch (MarshalException var7) {
         throw new WSSecurityException("Failed to unmarsahl encrypted data.", var7, WSSConstants.FAILURE_INVALID);
      } catch (XMLEncryptionException var8) {
         throw new WSSecurityException("Failed to decrypt encrypted data.", var8, WSSConstants.FAILURE_VERIFY_OR_DECRYPT);
      }
   }

   private List unmarshalReferenceList(Node var1) throws MarshalException {
      try {
         DOMStreamReader var2 = new DOMStreamReader(var1);
         return ReferenceList.read(var2, false);
      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }
   }

   private static void declareNamespaces(Element var0, Map var1) {
      declareNamespace(var0, var1, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
      declareNamespace(var0, var1, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu");
   }

   private static void declareNamespace(Element var0, Map var1, String var2, String var3) {
      if (var1.get(var2) == null) {
         var1.put(var2, var3);
         DOMUtils.declareNamespace(var0, var2, var3);
      }

   }

   private static Element getSecurityHeader(SOAPMessageContext var0) throws SOAPException, WSSecurityException {
      String var1 = getRole(var0);
      String var2 = getRoleAttrName(var0);
      SOAPHeader var3 = var0.getMessage().getSOAPHeader();
      return getSecurityHeader(var3, var2, var1);
   }

   private Element getSecurityHeader(WSSecurityContext var1) {
      return (Element)var1.getNode();
   }

   private static Element getSecurityHeader(Element var0, String var1, String var2) throws WSSecurityException {
      NodeList var3 = var0.getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");
      Element var4 = null;

      for(int var5 = 0; var3.getLength() > var5; ++var5) {
         Element var6 = (Element)var3.item(var5);
         if (isForRole(var6, var1, var2)) {
            if (var4 != null) {
               throw new WSSecurityException("Found more than one Security header for role " + var2, WSSConstants.FAILURE_INVALID);
            }

            var4 = var6;
         }
      }

      return var4;
   }

   private static boolean isForRole(Element var0, String var1, String var2) {
      Attr var3 = var0.getAttributeNodeNS("soapenv", var1);
      if ((!isNext(var2) || var3 != null) && !isNext(var3.getValue())) {
         return var3.getValue() != null && var3.getValue().equals(var2);
      } else {
         return true;
      }
   }

   private static String getRole(SOAPMessageContext var0) {
      return null;
   }

   private static String getRoleAttrName(SOAPMessageContext var0) {
      return "actor";
   }

   private static boolean isNext(String var0) {
      if (var0 != null && var0.length() != 0) {
         return var0.equals("http://schemas.xmlsoap.org/soap/actor/next");
      } else {
         return true;
      }
   }

   private List getInsertedNodes(Element var1, Node var2, Node var3) {
      ArrayList var4 = new ArrayList();
      Node var5 = null;
      if (var2 != null) {
         var5 = var2.getNextSibling();
      } else {
         var5 = var1.getFirstChild();
      }

      while(var5 != null && var5 != var3) {
         var4.add(var5);
         var5 = var5.getNextSibling();
      }

      return var4;
   }

   private static boolean isTimestampFirst(ContextHandler var0) {
      return Boolean.parseBoolean((String)var0.getValue("weblogic.wsee.security.timestamp_first")) || isStrictLayout(var0);
   }

   private static Node getLastTokenNode(ContextHandler var0) {
      return (Node)var0.getValue("weblogic.wsee.security.last_token_node");
   }

   private static void setLastTokenNode(ContextHandler var0, Node var1) {
      ((SecurityTokenContextHandler)var0).addContextElement("weblogic.wsee.security.last_token_node", var1);
   }

   private static Node getFirstTokenNode(ContextHandler var0) {
      return (Node)var0.getValue("weblogic.wsee.security.first_token_node");
   }

   private static void setFirstTokenNode(ContextHandler var0, Node var1) {
      ((SecurityTokenContextHandler)var0).addContextElement("weblogic.wsee.security.first_token_node", var1);
   }

   private static boolean needToSetAsFirstToken(ContextHandler var0) {
      String var1 = (String)var0.getValue("weblogic.wsee.security.move_node_to_top");
      return var1 != null ? Boolean.parseBoolean(var1) : false;
   }

   private static boolean isStrictLayout(ContextHandler var0) {
      Boolean var1 = (Boolean)var0.getValue("weblogic.wsee.security.strict_layout");
      return var1 != null ? var1 : false;
   }

   public static boolean isEndoringEncryptSignature(ContextHandler var0) {
      Boolean var1 = (Boolean)var0.getValue("weblogic.wsee.security.endorse_signature_encrypt_signature");
      return var1 != null ? var1 : false;
   }

   public static boolean isEncryptBeforeSign(ContextHandler var0) {
      Boolean var1 = (Boolean)var0.getValue("weblogic.wsee.security.encrypt_sign");
      return var1 != null ? var1 : false;
   }

   private static Node getInsertedNode(Element var0, ContextHandler var1, Node var2, boolean var3) {
      Node var4;
      if (var2 == null) {
         Node var5 = getFirstTokenNode(var1);
         if (var5 == null) {
            var4 = var0.getFirstChild();
         } else {
            var4 = var5.getNextSibling();
         }
      } else {
         var4 = var2.getPreviousSibling();
      }

      if (var3) {
         setLastTokenNode(var1, var4);
      }

      if (needToSetAsFirstToken(var1)) {
         setFirstTokenNode(var1, var4);
      }

      return var4;
   }

   protected Node findSignatureInsertBeforeNode(Element var1, XMLSignature var2, ContextHandler var3) {
      Node var4 = this.findInsertBeforeNode(var1, var3, false);
      List var5;
      if (isStrictLayout(var3)) {
         var5 = var2.getSignedInfo().getReferences();
         ArrayList var6 = new ArrayList();
         Iterator var7 = var5.iterator();

         while(var7.hasNext()) {
            Reference var8 = (Reference)var7.next();
            if (var8.getURI().startsWith("#")) {
               var6.add(var8.getURI().substring(1));
            }
         }

         Node var10 = this.findLastChildNodeById(var6, this.securityCtx.getIdQNames(), var1, var4);
         if (var10 != null) {
            var4 = var10.getNextSibling();
         }
      }

      Node var9;
      if (isEncryptBeforeSign(var3)) {
         for(var9 = var4 == null ? var1.getLastChild() : var4; null != var9; var9 = var9.getPreviousSibling()) {
            if (ENCRYPTED_KEY_QNAME.getLocalPart().equals(var9.getLocalName()) || REFERENCE_LIST_QNAME.getLocalPart().equals(var9.getLocalName())) {
               return var9;
            }
         }
      }

      if (isEndoringEncryptSignature(var3)) {
         var5 = null;
         if (null != var4) {
            if (!ENCRYPTED_KEY_QNAME.getLocalPart().equals(var4.getLocalName()) && !REFERENCE_LIST_QNAME.getLocalPart().equals(var4.getLocalName())) {
               return var4;
            }

            var9 = var4.getNextSibling();
         } else {
            var9 = getLastTokenNode(var3);
         }

         while(null != var9) {
            if (!ENCRYPTED_KEY_QNAME.getLocalPart().equals(var9.getLocalName()) && !REFERENCE_LIST_QNAME.getLocalPart().equals(var9.getLocalName())) {
               return var9;
            }

            var9 = var9.getNextSibling();
         }
      }

      return var4;
   }

   protected Node findReferenceInsertBeforeNode(Element var1, ContextHandler var2) {
      Node var3 = this.findInsertBeforeNode(var1, var2, false);
      Node var4 = var3;
      if (!isEncryptBeforeSign(var2)) {
         while(var4 != null && !var4.getLocalName().equals("Signature")) {
            var4 = var4.getPreviousSibling();
         }
      }

      if (var4 != null) {
         var3 = var4;
      }

      return var3;
   }

   protected Node findInsertBeforeNode(Element var1, ContextHandler var2, boolean var3) {
      Node var4 = var1.getLastChild();
      if (var4 == null) {
         return null;
      } else if (needToSetAsFirstToken(var2)) {
         return var1.getFirstChild();
      } else {
         boolean var5 = isTimestampFirst(var2);
         Node var6 = getLastTokenNode(var2);
         Node var7 = getFirstTokenNode(var2);
         Node var8;
         if (var3) {
            if (var6 == null) {
               var8 = var1.getFirstChild();
               return var5 && var8 != null && WSSConstants.TIMESTAMP_QNAME.getLocalPart().equals(var8.getLocalName()) ? var8.getNextSibling() : var8;
            } else {
               return var7 == null ? var6 : var7.getNextSibling();
            }
         } else {
            var8 = null;
            if (var6 == null) {
               if (var5 && WSSConstants.TIMESTAMP_QNAME.getLocalPart().equals(var4.getLocalName())) {
                  return null;
               }

               var8 = var4;
            } else {
               var8 = var6.getNextSibling();
            }

            return var8;
         }
      }
   }

   private Node findLastChildNodeById(List var1, Set var2, Node var3, Node var4) {
      for(Node var5 = var3.getLastChild(); var5 != null; var5 = var5.getPreviousSibling()) {
         if (this.matchesId(var1, var2, var5)) {
            return var5;
         }

         if (var5 == var4) {
            return null;
         }
      }

      return null;
   }

   private boolean matchesId(List var1, Set var2, Node var3) {
      if (!var3.hasAttributes()) {
         return false;
      } else {
         NamedNodeMap var4 = var3.getAttributes();
         int var5 = var4.getLength();
         Iterator var6 = var1.iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();

            for(int var8 = 0; var8 < var5; ++var8) {
               Node var9 = var4.item(var8);
               if (var7.equals(var9.getNodeValue())) {
                  Iterator var10 = var2.iterator();

                  while(var10.hasNext()) {
                     QName var11 = (QName)var10.next();
                     if (this.namespacesMatch(var9, var11) && var9.getLocalName().equals(var11.getLocalPart())) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      }
   }

   private boolean namespacesMatch(Node var1, QName var2) {
      String var3 = var1.getNamespaceURI();
      String var4 = var2.getNamespaceURI();
      if (this.isEmptyNamespace(var3) && this.isEmptyNamespace(var4)) {
         return true;
      } else {
         return var3 != null && var3.equals(var4);
      }
   }

   private boolean isEmptyNamespace(String var1) {
      return var1 == null || "".equals(var1);
   }

   public void register(SecurityHeaderElementHandler var1) {
      this.elementHandlers.put(var1.getQName(), var1);
   }

   private void validateHandlers(WSSecurityContext var1) throws WSSecurityException {
      Iterator var2 = this.elementHandlers.keySet().iterator();

      while(var2.hasNext()) {
         QName var3 = (QName)var2.next();
         ((SecurityHeaderElementHandler)this.elementHandlers.get(var3)).validate(var1);
      }

   }

   /** @deprecated */
   public interface SecurityHeaderElementHandler {
      QName getQName();

      void process(Node var1, WSSecurityContext var2) throws MarshalException;

      void validate(WSSecurityContext var1) throws WSSecurityException;
   }
}
