package weblogic.xml.crypto.wss;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.wsee.security.configuration.TimestampConfiguration;
import weblogic.xml.crypto.NodeURIDereferencer;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.api.dom.DOMIdMap;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.common.keyinfo.KeyResolver;
import weblogic.xml.crypto.common.keyinfo.KeySelectorResultImpl;
import weblogic.xml.crypto.dom.DOMIdMapImpl;
import weblogic.xml.crypto.dsig.XMLSignatureFactoryImpl;
import weblogic.xml.crypto.dsig.XMLSignatureImpl;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.XMLSignature;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.api.BinarySecurityTokenType;
import weblogic.xml.crypto.wss.api.Timestamp;
import weblogic.xml.crypto.wss.api.WSSecurityFactory;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.dom.marshal.MarshalException;

public class WSSecurityContext implements DOMIdMap, XMLCryptoContext, WSSecurityInfo, Serializable {
   private static final long serialVersionUID = -4276590520722987533L;
   public static final String WS_SECURITY_CONTEXT = "weblogic.xml.crypto.wss.WSSecurityContext";
   public static final String MESSAGE_CONTEXT = "javax.xml.rpc.handler.MessageContext";
   public static final String CREDENTIAL_PROVIDER_LIST = "weblogic.wsee.security.wss.CredentialProviderList";
   public static final String SERVER_CERTFILE = "weblogic.xml.crypto.wss.provider.ServerCertfile";
   public static final String SERVER_KEYFILE = "weblogic.xml.crypto.wss.provider.ServerKeyfile";
   public static final String TRUST_MANAGER = "weblogic.wsee.security.wss.TrustManager";
   public static final String END_POINT_URL = "weblogic.wsee.security.wss.end_point_url";
   private static final String[] SAML_TOKEN_TYPE_URIS = new String[]{"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID", "http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile-1.0#SAMLAssertionID"};
   public static final String INCLUSIVE_NS_PREFIX_LIST = "com.bea.weblogic.xml.crypto.dsig.IncluisveNSPrefixList";
   private static final ThreadLocal context = new ThreadLocal() {
      protected Object initialValue() {
         return new ArrayList();
      }
   };
   private transient Node parent;
   private transient Element securityElement;
   private transient Node nextSibling;
   private transient DOMIdMap idMap;
   private Set idQNames = new HashSet();
   private transient URIDereferencer uriDereferencer;
   private transient Map namespaces;
   private Map properties;
   private transient List securityTokens;
   private transient List cachedTokens;
   private transient List idTokens;
   private transient Map tokenMap;
   private transient Map nodeMap;
   private transient Map strMap;
   private transient Map strIdMap;
   private transient List signatures;
   private transient List encryptions;
   private transient Timestamp timestamp;
   private transient TimestampHandler timestampHandler;
   private transient List keyProviders;
   private transient KeyResolver keySelector;
   private transient List tokenOrderList;
   private transient Map tokenHandlers;
   private transient Map credentialProviders;
   private transient Map BSTTypes;
   private transient XMLEncryptionFactory encryptionFactory;
   private transient XMLSignatureFactory signatureFactory;
   private transient WSSecurityFactory securityFactory;

   protected void init() {
      this.tokenHandlers = new LinkedHashMap();
      this.setTokenHandler(new BinarySecurityTokenHandler());
      this.setTokenHandler(new UsernameTokenHandler());
      this.setTimestampHandler(new TimestampConfiguration());
      this.credentialProviders = new LinkedHashMap();
      this.BSTTypes = new HashMap();
      this.keyProviders = new ArrayList();
      this.idTokens = new ArrayList();
      this.tokenMap = new HashMap();
      this.nodeMap = new HashMap();
      this.strMap = new HashMap();
      this.strIdMap = new HashMap();
      this.signatures = new ArrayList();
      this.encryptions = new ArrayList();
      this.securityTokens = new ArrayList();
      this.cachedTokens = new ArrayList();
      this.setBSTType(new X509V3BSTType());
      this.signatureFactory = new XMLSignatureFactoryImpl();

      try {
         this.encryptionFactory = XMLEncryptionFactory.getInstance();
      } catch (XMLEncryptionException var2) {
      }

      this.securityFactory = WSSecurityFactory.getInstance();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.init();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      this.properties.remove("javax.xml.rpc.handler.MessageContext");
      var1.defaultWriteObject();
   }

   public WSSecurityContext(SOAPMessageContext var1) throws SOAPException {
      this.idQNames.addAll(WSSConstants.BUILTIN_ID_QNAMES);
      this.properties = new HashMap();
      this.securityTokens = new ArrayList();
      this.cachedTokens = new ArrayList();
      this.idTokens = new ArrayList();
      this.tokenMap = new HashMap();
      this.nodeMap = new HashMap();
      this.strMap = new HashMap();
      this.strIdMap = new HashMap();
      this.signatures = new ArrayList();
      this.encryptions = new ArrayList();
      this.keyProviders = new ArrayList();
      this.init();
      this.init(var1);
   }

   public WSSecurityContext(Node var1) {
      this.idQNames.addAll(WSSConstants.BUILTIN_ID_QNAMES);
      this.properties = new HashMap();
      this.securityTokens = new ArrayList();
      this.cachedTokens = new ArrayList();
      this.idTokens = new ArrayList();
      this.tokenMap = new HashMap();
      this.nodeMap = new HashMap();
      this.strMap = new HashMap();
      this.strIdMap = new HashMap();
      this.signatures = new ArrayList();
      this.encryptions = new ArrayList();
      this.keyProviders = new ArrayList();
      this.init();
      this.init(var1.getParentNode(), (Node)null, (Set)null, (Map)null);
      this.securityElement = (Element)var1;
   }

   public WSSecurityContext(Node var1, Node var2, Set var3, Map var4) {
      this.idQNames.addAll(WSSConstants.BUILTIN_ID_QNAMES);
      this.properties = new HashMap();
      this.securityTokens = new ArrayList();
      this.cachedTokens = new ArrayList();
      this.idTokens = new ArrayList();
      this.tokenMap = new HashMap();
      this.nodeMap = new HashMap();
      this.strMap = new HashMap();
      this.strIdMap = new HashMap();
      this.signatures = new ArrayList();
      this.encryptions = new ArrayList();
      this.keyProviders = new ArrayList();
      this.init();
      this.init(var1, var2, var3, var4);
   }

   public Element getElementById(String var1) {
      return this.idMap.getElementById(var1);
   }

   public void setIdAttributeNS(Element var1, String var2, String var3) {
      this.idMap.setIdAttributeNS(var1, var2, var3);
   }

   public Node getNode() {
      return this.parent;
   }

   public Node getNextSibling() {
      return this.nextSibling;
   }

   public void setSecurityElement(Element var1) {
      this.securityElement = var1;
   }

   public Element getSecurityElement() {
      return this.securityElement;
   }

   public URIDereferencer getURIDereferencer() {
      return this.uriDereferencer;
   }

   public void setBaseURI(String var1) {
   }

   public void setKeySelector(KeySelector var1) {
   }

   public void setURIDereferencer(URIDereferencer var1) {
      this.uriDereferencer = var1;
   }

   public Map getNamespaces() {
      return this.namespaces;
   }

   public void addIdQNames(Set var1) {
      var1.addAll(var1);
   }

   public Set getIdQNames() {
      return this.idQNames;
   }

   public Object setProperty(String var1, Object var2) {
      return this.properties.put(var1, var2);
   }

   public Object getProperty(String var1) {
      return this.properties.get(var1);
   }

   public void addSecurityToken(SecurityToken var1) {
      if (!this.securityTokens.contains(var1)) {
         this.securityTokens.add(var1);
      }

   }

   public List getSecurityTokens(String var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.securityTokens.iterator();

      SecurityToken var4;
      while(var3.hasNext()) {
         var4 = (SecurityToken)var3.next();
         if (var4.getValueType().equals(var1)) {
            var2.add(var4);
         }
      }

      var3 = this.cachedTokens.iterator();

      while(var3.hasNext()) {
         var4 = (SecurityToken)var3.next();
         if (var4.getValueType().equals(var1)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public List getSecurityTokens() {
      ArrayList var1 = new ArrayList();
      var1.addAll(this.securityTokens);
      var1.addAll(this.cachedTokens);
      return var1;
   }

   protected List getCurrentTokens() {
      return this.securityTokens;
   }

   public void addToken(SecurityToken var1, Element var2) {
      this.tokenMap.put(var2, var1);
      this.nodeMap.put(var1, var2);
   }

   public SecurityToken getToken(Element var1) {
      return (SecurityToken)this.tokenMap.get(var1);
   }

   public Node getNode(SecurityToken var1) {
      return (Node)this.nodeMap.get(var1);
   }

   public void addSTR(SecurityTokenReference var1, SecurityToken var2) {
      this.strMap.put(var2, var1);
      this.addSecurityToken(var2);
   }

   public void addKeyInfo(String var1, SecurityToken var2) {
      this.strMap.put(var2, var1);
   }

   public void addSTR(String var1, SecurityTokenReference var2) {
      this.strIdMap.put(var1, var2);
   }

   public SecurityTokenReference getSTR(String var1) {
      return (SecurityTokenReference)this.strIdMap.get(var1);
   }

   public Object getSTR(SecurityToken var1) {
      return this.strMap.get(var1);
   }

   public void setTimestamp(Timestamp var1) {
      this.timestamp = var1;
   }

   public Timestamp getTimestamp() {
      return this.timestamp;
   }

   public void addSignature(XMLSignature var1) {
      this.signatures.add(var1);
   }

   public List getSignatures() {
      return this.signatures;
   }

   public void addEncryption(Encryption var1) {
      this.encryptions.add(var1);
   }

   public List getEncryptions() {
      return this.encryptions;
   }

   public void addKeyProvider(KeyProvider var1) {
      this.keyProviders.add(0, var1);
      if (this.keySelector != null) {
         this.keySelector.addKeyProvider(var1);
      }

   }

   private KeyProvider[] getKeyProviders() {
      KeyProvider[] var1 = new KeyProvider[this.keyProviders.size()];
      this.keyProviders.toArray(var1);
      return var1;
   }

   public String getBaseURI() {
      return null;
   }

   public KeySelector getKeySelector() {
      if (this.keySelector == null) {
         this.keySelector = new KeyResolver(this.getKeyProviders());
      }

      return this.keySelector;
   }

   public void setTokenHandler(SecurityTokenHandler var1) {
      if (null == var1) {
         log("Skip a null security token handeler");
      } else {
         this.fillMap(this.tokenHandlers, var1.getValueTypes(), var1);
         this.fillMap(this.tokenHandlers, var1.getQNames(), var1);
         SecurityTokenReferenceImpl.register(var1);
      }
   }

   public SecurityTokenHandler getRequiredTokenHandler(Object var1) throws WSSecurityException {
      SecurityTokenHandler var2 = (SecurityTokenHandler)this.tokenHandlers.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         throw new WSSecurityException("No token handler found for " + var1, WSSConstants.UNSUPPORTED_TOKEN);
      }
   }

   public SecurityTokenHandler getTokenHandler(Object var1) throws WSSecurityException {
      return (SecurityTokenHandler)this.tokenHandlers.get(var1);
   }

   public Iterator getTokenHandlers() {
      return this.tokenHandlers.values().iterator();
   }

   public CredentialProvider getCredentialProvider(Object var1) {
      return (CredentialProvider)this.credentialProviders.get(var1);
   }

   public CredentialProvider getRequiredCredentialProvider(Object var1) throws WSSecurityException {
      CredentialProvider var2 = (CredentialProvider)this.credentialProviders.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         throw new WSSecurityException("No credential provider found for " + var1);
      }
   }

   public void setCredentialProvider(CredentialProvider var1) {
      this.fillMap(this.credentialProviders, var1.getValueTypes(), var1);
   }

   public Map getCredentialProviders() {
      return this.credentialProviders;
   }

   public void setBSTType(BinarySecurityTokenType var1) {
      this.BSTTypes.put(var1.getValueType(), var1);
   }

   public BinarySecurityTokenType getBSTType(String var1) {
      return (BinarySecurityTokenType)this.BSTTypes.get(var1);
   }

   public XMLEncryptionFactory getEncryptionFactory() {
      return this.encryptionFactory;
   }

   public void setEncryptionFactory(XMLEncryptionFactory var1) {
      this.encryptionFactory = var1;
   }

   public XMLSignatureFactory getSignatureFactory() {
      return this.signatureFactory;
   }

   public void setSignatureFactory(XMLSignatureFactory var1) {
      this.signatureFactory = var1;
   }

   public void setSecurityFactory(WSSecurityFactory var1) {
      this.securityFactory = var1;
   }

   public WSSecurityFactory getSecurityFactory() {
      return this.securityFactory;
   }

   private void fillMap(Map var1, Object[] var2, Object var3) {
      for(int var4 = 0; var4 < var2.length; ++var4) {
         var1.put(var2[var4], var3);
      }

   }

   public MessageContext getMessageContext() {
      return (MessageContext)this.properties.get("javax.xml.rpc.handler.MessageContext");
   }

   public static WSSecurityContext getSecurityContext(MessageContext var0) {
      return null == var0 ? null : (WSSecurityContext)var0.getProperty("weblogic.xml.crypto.wss.WSSecurityContext");
   }

   public static Map getCredentialProviders(MessageContext var0) {
      Map var1 = null;
      WSSecurityContext var2 = getSecurityContext(var0);
      if (var2 != null) {
         var1 = var2.getCredentialProviders();
      }

      return var1;
   }

   public void init(SOAPMessageContext var1) throws SOAPException {
      Object var2 = var1.getMessage().getSOAPHeader();
      if (var2 == null) {
         var2 = createSOAPHeader(var1);
      }

      this.init((Node)var2, (Node)null, (Set)null, (Map)null);
      this.setProperty("javax.xml.rpc.handler.MessageContext", var1);
      var1.setProperty("weblogic.xml.crypto.wss.WSSecurityContext", this);
   }

   public void init(Node var1, Node var2, Set var3, Map var4) {
      if (var1 == null) {
         throw new NullPointerException("Parent node of Security element must not be null.");
      } else {
         this.parent = var1;
         this.nextSibling = var2;
         if (var4 != null) {
            this.namespaces = var4;
         } else {
            this.namespaces = DOMUtils.getNSMap(var1);
         }

         this.addWSSNamespaces(this.namespaces);
         if (var3 != null) {
            this.idQNames.addAll(var3);
         }

         Document var5 = var1.getOwnerDocument();
         this.uriDereferencer = new NodeURIDereferencer(var5, DOMUtils.getNSMap(var1));
         this.idMap = new DOMIdMapImpl(var5, this.idQNames, this.namespaces);
      }
   }

   private void addWSSNamespaces(Map var1) {
      addNamespace(var1, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu");
      addNamespace(var1, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu");
   }

   private static void addNamespace(Map var0, String var1, String var2) {
      if (var0.get(var1) == null) {
         var0.put(var1, var2);
      }

   }

   private static Node createSOAPHeader(SOAPMessageContext var0) throws SOAPException {
      return var0.getMessage().getSOAPPart().getEnvelope().addHeader();
   }

   public List<BinarySecurityToken> getBinarySecurityTokens() {
      if (null != this.tokenMap && !this.tokenMap.isEmpty()) {
         Collection var1 = this.tokenMap.values();
         ArrayList var2 = new ArrayList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (var4 instanceof BinarySecurityToken) {
               var2.add((BinarySecurityToken)var4);
            }
         }

         if (var2.isEmpty()) {
            log("No BST tokens");
            return null;
         } else {
            log("Found BinarySecurityToken, return with size =" + var2.size());
            return var2;
         }
      } else {
         return null;
      }
   }

   public List getIdTokens() {
      return this.idTokens;
   }

   public void addIdToken(SecurityToken var1) {
      if (!this.idTokens.contains(var1)) {
         this.idTokens.add(var1);
      }

   }

   public AuthenticatedSubject getSubject() throws WSSecurityException {
      AuthenticatedSubject var1 = null;
      SOAPMessageContext var2 = (SOAPMessageContext)this.getProperty("javax.xml.rpc.handler.MessageContext");
      List var3 = this.getIdTokens();
      log("Number of identity tokens on context: " + var3.size());
      if (var3.size() > 0) {
         Iterator var10 = this.getIdTokens().iterator();

         SecurityToken var12;
         do {
            if (!var10.hasNext()) {
               throw new WSSecurityException("Failed to get subject from identity token.", WSSConstants.FAILURE_AUTH);
            }

            var12 = (SecurityToken)var10.next();
            log("Trying to get subject from identity token, type: " + var12.getValueType());
            var1 = this.getSubject(var12, var2);
         } while(var1 == null);

         log("Got subject from identity token, type: " + var12.getValueType());
         return var1;
      } else {
         for(int var4 = 0; var4 < SAML_TOKEN_TYPE_URIS.length; ++var4) {
            String var5 = SAML_TOKEN_TYPE_URIS[var4];
            List var6 = this.getSecurityTokens(var5);
            log("Number of SAML tokens (" + var5 + ") on context: " + var6.size());
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               SecurityToken var8 = (SecurityToken)var7.next();
               var1 = this.getSubject(var8, var2);
               if (var1 != null) {
                  log("Got subject from non-identity token, type: " + var8.getValueType());
                  return var1;
               }
            }
         }

         List var9 = this.getSecurityTokens("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
         var9.addAll(this.getSecurityTokens("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1"));
         Iterator var11 = var9.iterator();

         SecurityToken var13;
         do {
            if (!var11.hasNext()) {
               return var1;
            }

            var13 = (SecurityToken)var11.next();
            var1 = this.getSubject(var13, var2);
         } while(var1 == null);

         log("Got subject from non-identity token, type: " + var13.getValueType());
         return var1;
      }
   }

   private List getTokenOrder() {
      if (this.tokenOrderList == null) {
         ArrayList var1 = new ArrayList();
         Iterator var2 = this.tokenHandlers.values().iterator();

         while(var2.hasNext()) {
            SecurityTokenHandler var3 = (SecurityTokenHandler)var2.next();
            String[] var4 = var3.getValueTypes();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               var1.add(var4[var5]);
            }
         }

         this.tokenOrderList = var1;
      }

      return this.tokenOrderList;
   }

   private AuthenticatedSubject getSubject(SecurityToken var1, SOAPMessageContext var2) throws WSSecurityException {
      AuthenticatedSubject var3 = null;
      SecurityTokenHandler var4 = this.getRequiredTokenHandler(var1.getValueType());
      Subject var5 = var4.getSubject(var1, var2);
      if (var5 != null) {
         var3 = AuthenticatedSubject.getFromSubject(var5);
      }

      return var3;
   }

   public List getSignatures(SecurityToken var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.signatures.iterator();

      while(var3.hasNext()) {
         XMLSignatureImpl var4 = (XMLSignatureImpl)var3.next();
         SecurityToken var5 = ((KeySelectorResultImpl)((KeySelectorResultImpl)var4.getSignatureValidateResult().getKeySelectorResult())).getSecurityToken();
         if (var1.equals(var5)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public List getEncryptions(SecurityToken var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < this.getEncryptions().size(); ++var3) {
         Encryption var4 = (Encryption)this.getEncryptions().get(var3);
         SecurityToken var5 = ((KeySelectorResultImpl)((KeySelectorResultImpl)var4.getKeySelectorResult())).getSecurityToken();
         if (var1.equals(var5)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public XMLSignature getSignature(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      Iterator var3 = this.signatures.iterator();

      label47:
      while(var3.hasNext()) {
         XMLSignature var4 = (XMLSignature)var3.next();
         List var5 = var4.getSignedInfo().getReferences();
         Iterator var6 = var5.iterator();

         while(true) {
            Element var10;
            do {
               String var8;
               do {
                  if (!var6.hasNext()) {
                     continue label47;
                  }

                  Reference var7 = (Reference)var6.next();
                  var8 = var7.getURI();
               } while(var8.length() < 2);

               String var9 = var8.substring(1);
               if (var9.equals(var1.getId())) {
                  return var4;
               }

               var10 = this.getElementById(var9);
            } while(var10 == null);

            SecurityTokenReference var11 = null;

            try {
               var11 = SecurityTokenReferenceImpl.createAndUnmarshal(var10);
            } catch (MarshalException var13) {
               continue;
            }

            SecurityTokenHandler var12 = this.getRequiredTokenHandler(var11.getValueType());
            if (var1.equals(var12.getSecurityToken(var11, var2))) {
               return var4;
            }
         }
      }

      return null;
   }

   public void reset() {
      if (this.idTokens != null) {
         this.idTokens.clear();
      }

      if (this.tokenMap != null) {
         this.tokenMap.clear();
      }

      if (this.nodeMap != null) {
         this.nodeMap.clear();
      }

      if (this.strMap != null) {
         this.strMap.clear();
      }

      if (this.strIdMap != null) {
         this.strIdMap.clear();
      }

      if (this.securityTokens != null && this.cachedTokens != null) {
         this.cachedTokens.addAll(this.securityTokens);
         this.securityTokens.clear();
      }

      if (this.signatures != null) {
         this.signatures.clear();
      }

   }

   public void setCredentialProviders(List var1) {
      this.credentialProviders.clear();
      this.addCredentialProviders(var1);
   }

   public void addCredentialProviders(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         CredentialProvider var3 = (CredentialProvider)var2.next();
         this.addCredentialProvider(var3);
      }

   }

   public void addCredentialProvider(CredentialProvider var1) {
      String[] var2 = var1.getValueTypes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         String var4 = var2[var3];
         if (!this.credentialProviders.containsKey(var4)) {
            this.credentialProviders.put(var4, var1);
         } else {
            this.credentialProviders.put(var4, this.merge((CredentialProvider)this.credentialProviders.get(var4), var1, var4));
         }
      }

   }

   private CredentialProvider merge(CredentialProvider var1, CredentialProvider var2, String var3) {
      return new WrapperCredentialProvider(var1, var2, var3);
   }

   public TimestampHandler getTimestampHandler() {
      return this.timestampHandler;
   }

   public void setTimestampHandler(TimestampHandler var1) {
      this.timestampHandler = var1;
   }

   private static void log(String var0) {
      LogUtils.logWss(var0);
   }

   public static void pushContext(WSSecurityContext var0) {
      ((List)context.get()).add(var0);
   }

   public static void popContext() {
      List var0 = (List)context.get();
      if (!var0.isEmpty()) {
         var0.remove(var0.size() - 1);
      }

   }

   public static WSSecurityContext getCurrentContext() {
      List var0 = (List)context.get();
      return (WSSecurityContext)(var0.isEmpty() ? null : var0.get(var0.size() - 1));
   }
}
