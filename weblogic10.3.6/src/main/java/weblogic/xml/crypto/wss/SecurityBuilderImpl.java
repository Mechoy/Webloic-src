package weblogic.xml.crypto.wss;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorException;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.dsig.ReferenceUtils;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignature;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfoFactory;
import weblogic.xml.crypto.encrypt.EncryptionAlgorithm;
import weblogic.xml.crypto.encrypt.ReferenceList;
import weblogic.xml.crypto.encrypt.api.CipherReference;
import weblogic.xml.crypto.encrypt.api.DataReference;
import weblogic.xml.crypto.encrypt.api.EncryptedData;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.EncryptionProperties;
import weblogic.xml.crypto.encrypt.api.TBEKey;
import weblogic.xml.crypto.encrypt.api.TBEXML;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.encrypt.api.dom.DOMEncryptContext;
import weblogic.xml.crypto.encrypt.api.dom.DOMTBEXML;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.api.Security;
import weblogic.xml.crypto.wss.api.Timestamp;
import weblogic.xml.crypto.wss.api.WSSecurityFactory;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class SecurityBuilderImpl implements SecurityBuilder {
   protected static final String STR_ID_PREFIX = "str";
   protected WSSecurityContext securityCtx;
   protected Map namespaces;
   protected Set idQNames;
   private Timestamp timestamp;
   private List msgTokens = new ArrayList();
   private List ctxTokens = new ArrayList();
   protected Security security;
   private Map refs = new HashMap();
   private WSSecurityFactory sf = WSSecurityFactory.getInstance();

   public SecurityBuilderImpl(WSSecurityContext var1, Element var2) {
      this.securityCtx = var1;
      this.namespaces = DOMUtils.getNSMap(var2);
      this.idQNames = var1.getIdQNames();
   }

   public SecurityBuilderImpl(WSSecurityContext var1) {
      this.securityCtx = var1;
      this.namespaces = var1.getNamespaces();
      this.idQNames = var1.getIdQNames();
   }

   public XMLSignatureFactory getXMLSignatureFactory() {
      return this.securityCtx.getSignatureFactory();
   }

   private KeyInfoFactory getKeyInfoFactory() {
      return this.getXMLSignatureFactory().getKeyInfoFactory();
   }

   public XMLEncryptionFactory getXMLEncryptionFactory() {
      return this.securityCtx.getEncryptionFactory();
   }

   protected String getExisitingUri(Element var1) {
      return DOMUtils.getExistingId(var1, this.idQNames);
   }

   public String assignUri(Element var1) throws WSSecurityException {
      if (var1.isSameNode(var1.getOwnerDocument().getDocumentElement())) {
         return "";
      } else {
         String var2;
         if (!"http://www.w3.org/2000/09/xmldsig#".equals(var1.getNamespaceURI()) && !"http://www.w3.org/2001/04/xmlenc#".equals(var1.getNamespaceURI())) {
            var2 = this.getExisitingUri(var1);
            if (var2 != null && var2.length() > 0) {
               return this.getUri(var2);
            } else {
               String var3 = (String)this.namespaces.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
               String var4 = WSSConstants.WSU_ID_QNAME.getNamespaceURI();
               String var5 = this.getUri(DOMUtils.assignId(var1, WSSConstants.WSU_ID_QNAME, var3, this.idQNames));
               DOMUtils.declareNamespace(var1, var4, var3);
               return var5;
            }
         } else {
            var2 = null;
            if (var1.hasAttributeNS("", "Id")) {
               var2 = var1.getAttributeNS("", "Id");
            } else if (var1.hasAttributeNS((String)null, "Id")) {
               var2 = var1.getAttributeNS((String)null, "Id");
            } else {
               var2 = DOMUtils.generateId(var1.getLocalName());
               var1.setAttributeNS("", "Id", var2);
            }

            return this.getUri(var2);
         }
      }
   }

   public Reference createReference(String var1, String var2, DigestMethod var3, List var4, boolean var5, ContextHandler var6) throws WSSecurityException {
      return this.createReferenceInternal(var1, (List)null, var2, var3, var4, var5, var6);
   }

   protected Reference createReferenceInternal(String var1, List var2, String var3, DigestMethod var4, List var5, boolean var6, ContextHandler var7) throws WSSecurityException {
      String var8 = null;
      Object var9 = null;
      SecurityToken var10 = this.getSecurityToken(var1, var3, Purpose.SIGN, var7);
      if (var10 == null) {
         return null;
      } else {
         SecurityToken var11 = this.getPreviousToken(var10);
         if (var11 != null) {
            var10 = var11;
         }

         String var12 = var10.getId();
         boolean var13 = var1 != null && (var1.startsWith("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile") || var1.startsWith("http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile"));
         if ((var6 || var11 != null) && var12 != null && !var13) {
            var8 = this.getUri(var12);
            var9 = var10;
         } else {
            SecurityTokenReference var14 = this.getSTR(var1, var2, var10, false);
            String var15 = this.getSTRId(var14);
            this.securityCtx.addSTR(var15, var14);
            var8 = this.getUri(var15);
            var5.add(0, STRTransform.getInstance());
            var9 = var14;
         }

         try {
            if (var6) {
               this.addTokenToMessage(var10, var7);
            }
         } catch (MarshalException var16) {
            throw new WSSecurityException("Failed to add target token for Reference.", var16);
         }

         Reference var17 = this.getXMLSignatureFactory().newReference(var8, var4, ReferenceUtils.getTransforms(var5), (String)null, (String)null);
         this.refs.put(var17, var9);
         return var17;
      }
   }

   public Reference createSTRReference(SecurityToken var1, DigestMethod var2, List var3, boolean var4) throws WSSecurityException {
      SecurityTokenReference var5 = this.getSTR(var1.getValueType(), var1, var4);
      String var6 = this.getSTRId(var5);
      this.securityCtx.addSTR(var6, var5);
      String var7 = this.getUri(var6);
      if (var3 == null) {
         var3 = new ArrayList();
      }

      ((List)var3).add(0, STRTransform.getInstance());
      Reference var8 = this.getXMLSignatureFactory().newReference(var7, var2, ReferenceUtils.getTransforms((List)var3), (String)null, (String)null);
      return var8;
   }

   protected String getUri(String var1) {
      return "#" + var1;
   }

   private String getSTRId(SecurityTokenReference var1) {
      String var2 = var1.getId();
      if (var2 == null) {
         var2 = getId("str");
         var1.setId(var2);
      }

      return var2;
   }

   public boolean addTimestamp(short var1, ContextHandler var2) throws WSSecurityException, MarshalException {
      if (this.timestamp != null) {
         throw new WSSecurityException("Timestamp already added to SecurityBuilder.");
      } else {
         WSSecurityFactory var10001 = this.sf;
         this.timestamp = WSSecurityFactory.newTimestamp((String)null, true, var1);
         this.add(this.timestamp, var2);
         return true;
      }
   }

   public SecurityToken createSecurityToken(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException, MarshalException {
      SecurityToken var5 = this.getSecurityToken(var1, var2, var3, var4);
      SecurityToken var6 = this.getPreviousToken(var5);
      if (var6 != null) {
         var5 = var6;
      }

      this.addToken(false, var5, var4);
      return var5;
   }

   public SecurityToken addSecurityToken(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException, MarshalException {
      SecurityToken var5 = this.getSecurityToken(var1, var2, var3, var4);
      SecurityToken var6 = this.getPreviousToken(var5);
      if (var6 != null) {
         var5 = var6;
      }

      this.addTokenToMessage(var5, var4);
      if (Purpose.IDENTITY.equals(var3)) {
         this.addIdToken(var5);
      }

      return var5;
   }

   public Node addSignature(SignedInfo var1, String var2, String var3, boolean var4, ContextHandler var5) throws WSSecurityException, MarshalException {
      return this.addSignatureInternal(var1, var2, (List)null, var3, var4, var5);
   }

   protected Node addSignatureInternal(SignedInfo var1, String var2, List var3, String var4, boolean var5, ContextHandler var6) throws WSSecurityException, MarshalException {
      SecurityToken var7 = this.getSecurityToken(var2, var4, Purpose.SIGN, var6);
      if (var7 == null) {
         return null;
      } else {
         SecurityToken var8 = this.getPreviousToken(var7);
         if (var8 != null) {
            var7 = var8;
         }

         SecurityTokenReference var9 = this.getSTR(var2, var3, var7, var5);
         if (var9 == null) {
            throw new WSSecurityException("Failed to create reference for token: " + var7);
         } else {
            return this.addSignatureWithToken(var1, var9, var5, var6);
         }
      }
   }

   public Node addSignature(SignedInfo var1, Reference var2, ContextHandler var3) throws WSSecurityException, MarshalException {
      return this.addSignatureInternal(var1, var2, (List)null, var3);
   }

   protected Node addSignatureInternal(SignedInfo var1, Reference var2, List var3, ContextHandler var4) throws WSSecurityException, MarshalException {
      Object var5 = this.refs.get(var2);
      SecurityToken var6 = null;
      SecurityTokenReference var7 = null;
      if (var5 instanceof SecurityToken) {
         var6 = (SecurityToken)var5;
         String var8 = var6.getValueType();
         var7 = this.getSTR(var8, var3, var6, true);
      } else {
         var7 = (SecurityTokenReference)var5;
         var6 = var7.getSecurityToken();
      }

      return this.addSignatureWithToken(var1, var7, false, var4);
   }

   protected Node addSignatureWithToken(SignedInfo var1, SecurityTokenReference var2, Boolean var3, ContextHandler var4) throws MarshalException, WSSecurityException {
      SecurityToken var5 = var2.getSecurityToken();
      KeyProvider var6 = this.getKeyProvider(var5);
      ArrayList var7 = new ArrayList();
      var7.add(var2);
      KeyInfo var8 = this.getXMLSignatureFactory().getKeyInfoFactory().newKeyInfo(var7);
      XMLSignature var9 = this.getXMLSignatureFactory().newXMLSignature(var1, var8);
      Node var10 = this.addSignature(var9, var6, var4);
      if (var3 != null) {
         this.addToken(var3, var5, var4);
      }

      return var10;
   }

   protected void addToken(boolean var1, SecurityToken var2, ContextHandler var3) throws WSSecurityException, MarshalException {
      if (var1) {
         this.addTokenToMessage(var2, var3);
      } else {
         this.addTokenToContext(var2);
      }

   }

   public boolean addEncryption(List var1, EncryptionMethod var2, EncryptionMethod var3, String var4, String var5, boolean var6, ContextHandler var7) throws WSSecurityException, MarshalException {
      return this.addEncryptionInternal(var1, var2, var3, var4, (List)null, var5, var6, var7);
   }

   protected boolean addEncryptionInternal(List var1, EncryptionMethod var2, EncryptionMethod var3, String var4, List var5, String var6, boolean var7, ContextHandler var8) throws WSSecurityException, MarshalException {
      if (var1 != null && var1.size() != 0) {
         boolean var9 = var2 != null;
         SecurityToken var10 = this.getToken(var4, var6, var8);
         SecurityTokenReference var11 = null;
         if (var9) {
            var11 = this.createKeyIdSTRInternal(var4, var5, var10, var7);
         } else {
            int var12;
            if ((var12 = this.msgTokens.indexOf(var10)) > -1) {
               var10 = (SecurityToken)this.msgTokens.get(var12);
            }

            var11 = this.getSTR(var4, var5, var10, var7);
         }

         if (var11 == null) {
            throw new WSSecurityException("Failed to create reference for token: " + var10);
         } else {
            KeyInfo var19 = this.getKeyInfo(var11);
            KeyProvider var13 = this.getKeyProvider(var4, var10);
            KeySelector var14 = this.securityCtx.getKeySelector();
            Key var15 = this.selectKey(var14, var3);
            if (var15 == null) {
               var15 = this.generateKey(var3);
            }

            DOMEncryptContext var16 = new DOMEncryptContext(var15);
            List var17 = this.encryptData(var1, var16, var3, var19, !var9, var8);
            if (var9) {
               Key var18 = this.getKey(var14, var13, var2);
               this.addEncryptedKey(var15, var18, var2, var19, var17, var7, var10, var8);
            } else {
               this.addReferenceList(var17, var10, var8, var4, var7);
            }

            return true;
         }
      } else {
         throw new WSSecurityException("List of TBE must not be null or empty.");
      }
   }

   private Key selectKey(KeySelector var1, EncryptionMethod var2) {
      KeySelectorResult var3 = null;
      Key var4 = null;

      try {
         var3 = var1.select((KeyInfo)null, KeySelector.Purpose.ENCRYPT, var2, (XMLCryptoContext)null);
         if (var3 != null) {
            var4 = var3.getKey();
         }
      } catch (KeySelectorException var6) {
      }

      return var4;
   }

   protected Key generateKey(EncryptionMethod var1) throws WSSecurityException {
      try {
         Key var2 = ((EncryptionAlgorithm)var1).generateKey();
         return var2;
      } catch (XMLEncryptionException var4) {
         throw new WSSecurityException("Failed to generate key for algorithm " + var1.getAlgorithm());
      }
   }

   protected KeyProvider getKeyProvider(String var1, SecurityToken var2) throws WSSecurityException {
      SecurityTokenHandler var3 = this.securityCtx.getRequiredTokenHandler(var1);
      KeyProvider var4 = var3.getKeyProvider(var2, this.securityCtx.getMessageContext());
      if (var4 != null) {
         this.securityCtx.addKeyProvider(var4);
      }

      return var4;
   }

   protected KeyInfo getKeyInfo(SecurityTokenReference var1) {
      ArrayList var2 = new ArrayList();
      var2.add(var1);
      KeyInfo var3 = this.getKeyInfoFactory().newKeyInfo(var2);
      return var3;
   }

   protected SecurityToken getToken(String var1, String var2, ContextHandler var3) throws WSSecurityException {
      SecurityTokenContextHandler var4 = getSecurityTokenContextHandler(var3);
      this.copySubject(var4);
      Object var5 = this.getCredential(var1, var2, var4, Purpose.ENCRYPT);
      SecurityToken var6 = null;
      if (var5 != null) {
         SecurityTokenHandler var7 = this.securityCtx.getRequiredTokenHandler(var1);
         var6 = var7.getSecurityToken(var1, var5, var3);
      } else {
         var6 = this.getToken(var1, var2, var3, Purpose.ENCRYPT);
         if (var6 == null) {
            throw new WSSecurityException("Failed to get token for tokenType: " + var1);
         }
      }

      return var6;
   }

   protected List encryptData(List var1, DOMEncryptContext var2, EncryptionMethod var3, KeyInfo var4, boolean var5, ContextHandler var6) throws MarshalException, WSSecurityException {
      XMLEncryptionFactory var7 = this.getXMLEncryptionFactory();
      ArrayList var8 = new ArrayList();
      Iterator var9 = var1.iterator();

      while(var9.hasNext()) {
         TBEXML var10 = (TBEXML)var9.next();
         String var11 = getId();
         EncryptedData var12 = var7.newEncryptedData(var10, var3, var5 ? var4 : null, (EncryptionProperties)null, var11, (CipherReference)null);
         this.encrypt(var12, var2, var6);
         DataReference var13 = this.getXMLEncryptionFactory().newDataReference("#" + var11, (List)null);
         var8.add(var13);
      }

      return var8;
   }

   protected EncryptedKey addEncryptedKey(Key var1, Key var2, EncryptionMethod var3, KeyInfo var4, List var5, String var6, boolean var7, SecurityToken var8, ContextHandler var9) throws WSSecurityException, MarshalException {
      TBEKey var10 = new TBEKey(var1);
      EncryptedKey var11 = this.getEncryptionFactory().newEncryptedKey(var10, var3, var4, (EncryptionProperties)null, var5, var6, (String)null, (String)null, (CipherReference)null);
      DOMEncryptContext var12 = new DOMEncryptContext(var2);
      this.addEncryptedKey(var11, var12, var9);
      this.msgTokens.add(var11);
      this.addToken(var7, var8, var9);
      return var11;
   }

   protected void addEncryptedKey(Key var1, Key var2, EncryptionMethod var3, KeyInfo var4, List var5, boolean var6, SecurityToken var7, ContextHandler var8) throws WSSecurityException, MarshalException {
      this.addEncryptedKey(var1, var2, var3, var4, var5, getId(), var6, var7, var8);
   }

   protected Key getKey(KeySelector var1, KeyProvider var2, EncryptionMethod var3) throws WSSecurityException {
      KeySelectorResult var4 = null;

      try {
         if (var2 != null) {
            var4 = var2.getKey(var3.getAlgorithm(), KeySelector.Purpose.ENCRYPT);
         } else {
            var4 = var1.select((KeyInfo)null, KeySelector.Purpose.ENCRYPT, var3, (XMLCryptoContext)null);
         }
      } catch (KeySelectorException var6) {
         throw new WSSecurityException(var6);
      }

      if (var4 == null) {
         throw new WSSecurityException("Failed to select key for algorithm " + var3.getAlgorithm());
      } else {
         Key var5 = var4.getKey();
         return var5;
      }
   }

   protected XMLEncryptionFactory getEncryptionFactory() {
      return this.securityCtx.getEncryptionFactory();
   }

   private void copySubject(SecurityTokenContextHandler var1) {
      MessageContext var2 = this.securityCtx.getMessageContext();
      if (var2 != null) {
         AuthenticatedSubject var3 = (AuthenticatedSubject)var2.getProperty("weblogic.wsee.wss.subject");
         var1.addContextElement("weblogic.wsee.wss.subject", var3);
      }

   }

   private void encrypt(EncryptedData var1, DOMEncryptContext var2, ContextHandler var3) throws MarshalException, WSSecurityException {
      DOMTBEXML var4 = (DOMTBEXML)var1.getTBE();

      try {
         Node var5 = null;
         Node var6 = null;
         Node var7 = null;
         Node var8 = null;
         int var9 = var4.getNodeList().getLength();
         String var10 = var4.getType();
         if ("http://www.w3.org/2001/04/xmlenc#Element".equals(var10)) {
            var5 = var4.getNodeList().item(0);
            var7 = var5.getNextSibling();
            var8 = var5.getParentNode();
         }

         var1.encrypt(var2);
         if ("http://www.w3.org/2001/04/xmlenc#Element".equals(var10)) {
            if (var7 != null) {
               var6 = var7.getPreviousSibling();
            } else {
               var6 = var8.getLastChild();
            }

            this.updateContext(var5, var6, var3);
            if (this.isHeader(var6)) {
               this.processEncryptedHeader(var5, var6);
            }
         }

      } catch (XMLEncryptionException var11) {
         throw new WSSecurityException(var11);
      }
   }

   private boolean isHeader(Node var1) {
      return DOMUtils.is(var1.getParentNode(), "http://schemas.xmlsoap.org/soap/envelope/", "Header") || DOMUtils.is(var1.getParentNode(), "http://www.w3.org/2003/05/soap-envelope", "Header");
   }

   protected void processEncryptedHeader(Node var1, Node var2) {
   }

   protected void updateContext(Node var1, Node var2, ContextHandler var3) {
      SecurityTokenContextHandler var4 = (SecurityTokenContextHandler)var3;
      Node var5 = (Node)var4.getValue("weblogic.wsee.security.first_token_node");
      if (var5 != null && var5.equals(var1)) {
         var4.addContextElement("weblogic.wsee.security.first_token_node", var2);
      }

      Node var6 = (Node)var4.getValue("weblogic.wsee.security.last_token_node");
      if (var6 != null && var6.equals(var1)) {
         var4.addContextElement("weblogic.wsee.security.last_token_node", var2);
      }

   }

   protected void addReferenceList(List var1, SecurityToken var2, ContextHandler var3) throws MarshalException, WSSecurityException {
      this.addReferenceList(var1, var2, var3, (String)null, true);
   }

   private void addReferenceList(List var1, SecurityToken var2, ContextHandler var3, String var4, boolean var5) throws MarshalException, WSSecurityException {
      if (var4 != null) {
         if (SCTUtils.isSCTokenTypeURI(var4)) {
            if (var5) {
               this.addTokenToMessage(var2, var3);
            }
         } else {
            this.addTokenToMessage(var2, var3);
         }
      } else {
         this.addTokenToMessage(var2, var3);
      }

      this.add(new ReferenceList(var1), var3);
   }

   private SecurityToken getSecurityToken(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException {
      SecurityTokenContextHandler var5 = getSecurityTokenContextHandler(var4);
      Object var6 = this.getCredential(var1, var2, var5, var3);
      if (var6 == null) {
         return null;
      } else {
         SecurityToken var7 = this.getSecurityToken(var1, var6, var5);
         return var7;
      }
   }

   private KeyProvider getKeyProvider(SecurityToken var1) throws WSSecurityException {
      SecurityTokenHandler var2 = this.securityCtx.getRequiredTokenHandler(var1.getValueType());
      return var2.getKeyProvider(var1, this.securityCtx.getMessageContext());
   }

   private Object getCredential(String var1, String var2, SecurityTokenContextHandler var3, Purpose var4) {
      LogUtils.logWss("Trying to get credential for token type " + var1 + " and purpose " + var4 + " from credential provider.");
      CredentialProvider var5 = this.getCredentialProvider(var1);
      if (var5 == null) {
         LogUtils.logWss("No credential provider found for token type " + var1);
         return null;
      } else {
         Object var6 = var5.getCredential(var1, var2, var3, var4);
         if (var6 != null) {
            LogUtils.logWss("Got credential for token type " + var1 + " and purpose " + var4 + " from credential provider " + var5);
         } else {
            LogUtils.logWss("No credential for token type " + var1 + " and purpose " + var4 + " from credential provider " + var5);
         }

         return var6;
      }
   }

   protected CredentialProvider getCredentialProvider(String var1) {
      return this.securityCtx.getCredentialProvider(var1);
   }

   private SecurityToken getSecurityToken(String var1, Object var2, SecurityTokenContextHandler var3) throws WSSecurityException {
      SecurityTokenHandler var4 = this.securityCtx.getRequiredTokenHandler(var1);
      SecurityToken var5 = var4.getSecurityToken(var1, var2, var3);
      return var5;
   }

   private SecurityToken getToken(String var1, String var2, ContextHandler var3, Purpose var4) throws WSSecurityException {
      LogUtils.logWss("Trying to get token for token type " + var1 + " and purpose " + var4 + " from token handler.");
      SecurityTokenHandler var5 = this.securityCtx.getRequiredTokenHandler(var1);
      SecurityToken var6 = var5.getSecurityToken(var1, var2, var4, var3);
      if (var6 != null) {
         LogUtils.logWss("Got token for token type " + var1 + " and purpose " + var4 + " from token handler" + var5);
      } else {
         LogUtils.logWss("Did not get token for token type " + var1 + " and purpose " + var4 + " from token handler" + var5);
      }

      return var6;
   }

   protected SecurityTokenReference createDirectSTR(String var1, SecurityToken var2) throws WSSecurityException {
      SecurityTokenHandler var3 = this.securityCtx.getRequiredTokenHandler(var1);
      SecurityTokenReference var4 = var3.getSTR(WSSConstants.REFERENCE_QNAME, var1, var2);
      if (var4 == null) {
         LogUtils.logWss("Returned STR was null, returning null to caller");
         return null;
      } else {
         String var5 = var4.getReferenceURI();
         if (var5 == null) {
            String var6 = var2.getId();
            if (var6 == null) {
               var6 = getId();
               var2.setId(var6);
            }

            var4.setReferenceURI("#" + var6);
         }

         LogUtils.logWss("Token's ID is: " + var2.getId());
         LogUtils.logWss("STR's ID is: " + var4.getId());
         LogUtils.logWss("STR's reference URI is: " + var4.getReferenceURI());
         return var4;
      }
   }

   protected SecurityTokenReference getSTR(String var1, List var2, SecurityToken var3, boolean var4) throws WSSecurityException {
      return this.getSTR(var1, var3, var4);
   }

   protected SecurityTokenReference getSTR(String var1, SecurityToken var2, boolean var3) throws WSSecurityException {
      SecurityTokenReference var4 = null;
      if (var3) {
         var4 = this.createDirectSTR(var1, var2);
      }

      if (var4 == null) {
         var4 = this.createKeyIdSTR(var1, var2);
      }

      if (var4 != null && var4.getId() == null) {
         var4.setId(getId("str"));
      }

      return var4;
   }

   protected SecurityTokenReference createKeyIdSTRInternal(String var1, List var2, SecurityToken var3, boolean var4) throws WSSecurityException {
      return this.createKeyIdSTR(var1, var3);
   }

   protected SecurityTokenReference createKeyIdSTR(String var1, SecurityToken var2) throws WSSecurityException {
      SecurityTokenHandler var3 = this.securityCtx.getRequiredTokenHandler(var1);
      SecurityTokenReference var4 = var3.getSTR(WSSConstants.KEY_IDENTIFIER_QNAME, var1, var2);
      return var4;
   }

   private String assignId(SecurityToken var1) {
      String var2 = var1.getId();
      if (var2 == null) {
         var2 = getId();
         var1.setId(var2);
      }

      return var2;
   }

   protected static String getId(String var0) {
      return DOMUtils.generateId(var0);
   }

   private static String getId() {
      return DOMUtils.generateId();
   }

   private void add(XMLStructure var1, ContextHandler var2) throws MarshalException, WSSecurityException {
      if (this.security == null) {
         this.createSecurity(this.securityCtx);
      }

      this.security.add((XMLStructure)var1, (XMLCryptoContext)null, var2);
   }

   private Node addSignature(XMLSignature var1, KeyProvider var2, ContextHandler var3) throws MarshalException, WSSecurityException {
      if (this.security == null) {
         this.createSecurity(this.securityCtx);
      }

      return this.security.add(var1, var2, var3);
   }

   protected void addEncryptedKey(EncryptedKey var1, DOMEncryptContext var2, ContextHandler var3) throws MarshalException, WSSecurityException {
      if (this.security == null) {
         this.createSecurity(this.securityCtx);
      }

      this.security.add((XMLStructure)var1, (XMLCryptoContext)var2, var3);
   }

   protected void createSecurity(WSSecurityContext var1) throws MarshalException {
      WSSecurityFactory.getInstance();
      this.security = WSSecurityFactory.newSecurity(var1);
   }

   protected void addTokenToMessage(SecurityToken var1, ContextHandler var2) throws WSSecurityException, MarshalException {
      if (var1 != null) {
         int var3 = this.msgTokens.indexOf(var1);
         if (var3 < 0) {
            this.msgTokens.add(var1);
            this.assignId(var1);
            this.add(var1, var2);
            if (var1 instanceof BinarySecurityToken) {
               this.moveToTop(var1);
            }
         } else {
            this.moveToTop((SecurityToken)this.msgTokens.get(var3));
         }
      }

   }

   protected void addTokenToContext(SecurityToken var1) {
      if (!this.ctxTokens.contains(var1) && !this.msgTokens.contains(var1)) {
         this.securityCtx.addSecurityToken(var1);
      }

   }

   private void addIdToken(SecurityToken var1) {
      this.securityCtx.addIdToken(var1);
   }

   private SecurityToken getPreviousToken(SecurityToken var1) {
      int var2 = this.msgTokens.indexOf(var1);
      return var2 >= 0 ? (SecurityToken)this.msgTokens.get(var2) : null;
   }

   protected void moveToTop(SecurityToken var1) {
      Node var2 = this.securityCtx.getNode(var1);
      if (null != var2) {
         Element var3 = this.securityCtx.getSecurityElement();
         if (null != var3) {
            Node var4 = var3.getFirstChild();
            if (!var4.equals(var2)) {
               var3.removeChild(var2);
               var3.insertBefore(var2, var4);
            }
         }
      }
   }

   private static SecurityTokenContextHandler getSecurityTokenContextHandler(ContextHandler var0) throws WSSecurityException {
      if (!(var0 instanceof SecurityTokenContextHandler)) {
         throw new WSSecurityException(var0 + " is not a SecurityTokenContextHandler");
      } else {
         SecurityTokenContextHandler var1 = (SecurityTokenContextHandler)var0;
         return var1;
      }
   }
}
