package weblogic.xml.crypto.wss;

import java.io.Serializable;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.crypto.utils.CertUtils;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.api.BinarySecurityTokenType;
import weblogic.xml.crypto.wss.api.Encoding;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityTokenPolicyInfo;
import weblogic.xml.dom.marshal.MarshalException;

public class BinarySecurityTokenImpl extends SecurityTokenImpl implements BinarySecurityToken, SecurityTokenPolicyInfo, Serializable {
   private static final long serialVersionUID = -2889267257952778022L;
   private static String defaultEncoding = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary";
   private static String ID_PREFIX = "bst";
   private static final ConcurrentHashMap types = new ConcurrentHashMap();
   private static final ConcurrentHashMap encodings = new ConcurrentHashMap();
   private String id;
   private Object credentials;
   private byte[] decodedValue;
   private String encodedValue;
   private String valueType;
   private String encodingType;
   private BinarySecurityTokenType type;
   private boolean validated;

   public BinarySecurityTokenImpl() {
      this.encodingType = defaultEncoding;
      this.validated = false;
      this.type = (BinarySecurityTokenType)types.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
      if (null != this.type && null != this.type.getValueType()) {
         this.valueType = this.type.getValueType();
      } else {
         this.valueType = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3";
      }

   }

   public BinarySecurityTokenImpl(String var1, Object var2, ContextHandler var3) {
      this.encodingType = defaultEncoding;
      this.validated = false;
      if (var2 == null) {
         throw new IllegalArgumentException("Credential must not be null.");
      } else {
         this.valueType = var1;
         this.type = (BinarySecurityTokenType)types.get(var1);
         if (this.type == null) {
            this.type = (BinarySecurityTokenType)types.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
            this.valueType = this.type.getValueType();
         }

         this.credentials = var2;
         this.id = DOMUtils.generateId(ID_PREFIX);
      }
   }

   private static final void initTypes() {
      register((BinarySecurityTokenType)(new X509V3BSTType()));
      register((BinarySecurityTokenType)(new X509V1BSTType()));
      register((BinarySecurityTokenType)X509CertPath.getPKCS7());
      register((BinarySecurityTokenType)X509CertPath.getPKIPath());
   }

   public static void initEncodings() {
      encodings.put("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary", new Base64Encoding());
   }

   public static void register(Encoding var0) {
      encodings.put(var0.getURI(), var0);
   }

   public static void register(BinarySecurityTokenType var0) {
      types.put(var0.getValueType(), var0);
   }

   public static BinarySecurityTokenType getBSTType(String var0) {
      return (BinarySecurityTokenType)types.get(var0);
   }

   public String getValueType() {
      return this.valueType;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public PrivateKey getPrivateKey() {
      return this.type.getPrivateKey(this.getCredential());
   }

   public PublicKey getPublicKey() {
      return this.type.getPublicKey(this.getCredential());
   }

   public Key getSecretKey() {
      return this.type.getSecretKey(this.getCredential());
   }

   public X509Certificate getCertificate() {
      return this.type.getCertificate(this.getCredential());
   }

   public Object getCredential() {
      if (this.credentials == null) {
         this.credentials = this.type.getCredentials(this.decodedValue);
      }

      return this.credentials;
   }

   public String getEncodedValue() throws WSSecurityException {
      if (this.encodedValue == null) {
         this.encodedValue = ((Encoding)encodings.get(this.getEncodingType())).encode(this.getDecodedValue());
      }

      return this.encodedValue;
   }

   public byte[] getDecodedValue() throws WSSecurityException {
      if (this.decodedValue == null) {
         this.decodedValue = ((BinarySecurityTokenType)types.get(this.valueType)).getUnencodedValue(this.credentials);
      }

      return this.decodedValue;
   }

   public String getEncodingType() {
      return this.encodingType;
   }

   public void marshal(Element var1, Node var2, Map var3) throws MarshalException {
      BSTUtils.marshalToken(this, var1, var3, var2, true);
   }

   public void unmarshal(Node var1) throws MarshalException {
      Element var2 = (Element)var1;
      this.setId(DOMUtils.getAttributeValue(var2, WSSConstants.WSU_ID_QNAME));
      this.valueType = DOMUtils.getAttributeValue(var2, WSSConstants.VALUE_TYPE_QNAME);
      if (this.valueType == null) {
         throw new MarshalException("Attribute " + WSSConstants.VALUE_TYPE_QNAME + " required in " + WSSConstants.BST_QNAME);
      } else {
         this.type = (BinarySecurityTokenType)types.get(this.valueType);
         String var3 = DOMUtils.getAttributeValue(var2, WSSConstants.ENCODING_TYPE_QNAME);
         if (var3 != null) {
            this.encodingType = var3;
         }

         this.encodedValue = DOMUtils.getText((Element)var1);
         this.decodedValue = ((Encoding)encodings.get(this.encodingType)).decode(this.encodedValue);
         this.validated = false;
      }
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public boolean supports(Purpose var1) {
      return true;
   }

   public Element getSecurityTokenAssertion(Element var1, Purpose var2, ContextHandler var3) throws WSSecurityConfigurationException {
      if (Purpose.IDENTITY.equals(var2)) {
         Boolean var4 = (Boolean)var3.getValue("UseX509ForIdentity");
         if (var4 == null || !var4) {
            return null;
         }
      }

      String var15 = this.getTrustedCANames();
      String var5 = DOMUtils.getPrefix("http://www.bea.com/wls90/security/policy", var1);
      Element var6 = DOMUtils.createAndAddElement(var1, WSSConstants.POLICY_TOKEN_QNAME, var5);
      DOMUtils.addAttribute(var6, WSSConstants.POLICY_TOKEN_TYPE_QNAME, this.getValueType());
      Element var7;
      if (Purpose.SIGN.equals(var2)) {
         DOMUtils.addAttribute(var6, WSSConstants.POLICY_INCLUDE_IN_MESSAGE_QNAME, "true");
         if (var15 != null) {
            var7 = DOMUtils.createAndAddElement(var6, WSSConstants.POLICY_TOKEN_ISSUER, var5);
            DOMUtils.addText(var7, var15);
         }
      } else if (Purpose.ENCRYPT.equals(var2)) {
         var7 = DOMUtils.createAndAddElement(var1, new QName("http://www.bea.com/wls90/security/policy", "SecurityTokenReference"), var5);
         Element var8 = DOMUtils.createAndAddElement(var7, new QName("http://www.bea.com/wls90/security/policy", "Embedded"), var5);
         List var9 = null;

         try {
            HashMap var10 = new HashMap();
            var10.put("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
            var10.put("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu");
            var9 = (List)var3.getValue("com.bea.contextelement.wsee.credentialProviders");
            if (var9 == null) {
               throw new WSSecurityConfigurationException("com.bea.contextelement.wsee.credentialProviders can not be found from the context handler");
            }

            Iterator var11 = var9.iterator();

            while(var11.hasNext()) {
               CredentialProvider var12 = (CredentialProvider)var11.next();
               Object var13 = var12.getCredential(this.getValueType(), (String)null, var3, var2);
               if (var13 != null) {
                  this.credentials = var13;
                  break;
               }
            }

            if (this.credentials == null) {
               throw new WSSecurityConfigurationException("Can not resolve credentials for encryption during customizing the abstract policy. \nPlease check the credential providers " + var9);
            }

            this.marshal(var8, (Node)null, var10);
         } catch (MarshalException var14) {
            throw new WSSecurityConfigurationException(var14.getMessage());
         }
      }

      return var6;
   }

   private String getTrustedCANames() throws WSSecurityConfigurationException {
      X509Certificate[] var1 = CertUtils.getTrustedCAs();
      if (var1.length == 0) {
         return null;
      } else {
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            X509Certificate var4 = var1[var3];
            String var5 = var4.getSubjectX500Principal().getName();
            var2.append(var5);
            var2.append(',');
         }

         return var2.toString();
      }
   }

   public String toString() {
      return this.getValueType() + " [id: " + this.getId() + ", cert: [" + this.getCertificate() + "]]";
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof BinarySecurityToken) {
         BinarySecurityToken var2 = (BinarySecurityToken)var1;
         return var2.getCertificate().equals(this.getCertificate());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.getCertificate().hashCode();
   }

   public void setValidated(boolean var1) {
      this.validated = var1;
   }

   public boolean isValidated() {
      return this.validated;
   }

   static {
      initEncodings();
      initTypes();
   }
}
