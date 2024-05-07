package weblogic.xml.crypto.wss;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.security.UsernameAndPassword;
import weblogic.security.WSUserPasswordDigest;
import weblogic.security.service.ContextHandler;
import weblogic.security.utils.PasswordDigestUtils;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.api.Encoding;
import weblogic.xml.crypto.wss.api.UsernameToken;
import weblogic.xml.crypto.wss.policy.ClaimsBuilder;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenPolicyInfo;
import weblogic.xml.dom.marshal.MarshalException;
import weblogic.xml.schema.types.XSDDateTime;
import weblogic.xml.security.utils.Utils;

public class UsernameTokenImpl extends SecurityTokenImpl implements UsernameToken, SecurityTokenPolicyInfo, WSUserPasswordDigest, Serializable {
   private static final long serialVersionUID = 9185634049782890759L;
   private static final String[] valueTypes = new String[]{"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken"};
   private static final String defaultNonceEncodingType = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary";
   private static final String defaultPasswordType = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
   private static final boolean defaultUsePassword = true;
   private static final String DIGEST_ALGORITHM = "SHA-1";
   private static final String ASCII = "US-ASCII";
   private static final String UTF_8 = "UTF-8";
   private static final String ID_PREFIX = "unt";
   private static final String POLICY_URI = "http://www.bea.com/wls90/security/policy";
   private static final QName POLICY_USE_PASSWD = new QName("http://www.bea.com/wls90/security/policy", "UsePassword");
   private static final QName POLICY_PASSWD_TYPE = new QName("Type");
   public static final QName POLICY_PASSWD_ATTR = new QName("Attribute");
   private static final ConcurrentHashMap encodings = new ConcurrentHashMap();
   private static final short USERNAME = 0;
   private static final short PWD = 1;
   private static final short NONCE = 2;
   private static final short CREATED = 3;
   private String id;
   private String username;
   private transient char[] password;
   private transient String passwordDigest;
   private transient byte[] decodedPwdDigest;
   private String passwordType;
   private boolean usePassword;
   private String passwordId;
   private byte[] nonce;
   private String encodedNonce;
   private String nonceEncodingType;
   private boolean useNonce;
   private Calendar created;
   private String serializedCreated;
   private boolean useCreated;
   private transient UsernameAndPassword credential;

   public UsernameTokenImpl() {
      this.passwordType = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
      this.usePassword = true;
      this.nonceEncodingType = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary";
   }

   private UsernameTokenImpl(UsernameAndPassword var1) throws WSSecurityException {
      this(var1, (ContextHandler)null);
   }

   public UsernameTokenImpl(UsernameAndPassword var1, ContextHandler var2) throws WSSecurityException {
      super(var1);
      this.passwordType = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
      this.usePassword = true;
      this.nonceEncodingType = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary";
      this.username = var1.getUsername();
      this.password = var1.getPassword();
      Node var3 = (Node)var2.getValue("weblogic.xml.crypto.wss.policy.Claims");
      if (var3 != null && ClaimsBuilder.getClaimFromElt(var3, POLICY_USE_PASSWD) != null) {
         this.passwordType = ClaimsBuilder.getClaimFromAttr(var3, POLICY_USE_PASSWD, POLICY_PASSWD_TYPE);
      } else {
         this.usePassword = false;
         this.passwordType = null;
      }

      if (this.usePassword && this.password == null) {
         throw new WSSecurityException("No password provided for Password Type for UsernameToken: " + this.passwordType, WSSConstants.FAILURE_TOKEN_INVALID);
      } else if (this.usePassword && this.password != null && !"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest".equals(this.passwordType) && !"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText".equals(this.passwordType)) {
         throw new WSSecurityException("Invalid Password Type for UsernameToken: " + this.passwordType, WSSConstants.FAILURE_TOKEN_INVALID);
      } else {
         if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest".equals(this.passwordType)) {
            this.useNonce = true;
            this.useCreated = true;
         } else if (var3 != null) {
            String var4 = ClaimsBuilder.getClaimFromAttr(var3, POLICY_USE_PASSWD, POLICY_PASSWD_ATTR);
            if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#NonceCreate".equals(var4)) {
               this.useNonce = true;
               this.useCreated = true;
            } else if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#Nonce".equals(var4)) {
               this.useNonce = true;
            } else if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#Create".equals(var4)) {
               this.useCreated = true;
            }
         }

         this.id = DOMUtils.generateId("unt");
      }
   }

   public static void initEncodings() {
      Base64Encoding var0 = new Base64Encoding();
      register(var0);
   }

   public static void register(Encoding var0) {
      encodings.put(var0.getURI(), var0);
   }

   public String getValueType() {
      return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken";
   }

   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public PrivateKey getPrivateKey() {
      return null;
   }

   public PublicKey getPublicKey() {
      return null;
   }

   public Key getSecretKey() {
      return null;
   }

   public Object getCredential() {
      if (this.credential == null) {
         UsernameAndPassword var1 = new UsernameAndPassword(this.username, this.password);
         this.credential = var1;
      }

      return this.credential;
   }

   public String getUsername() {
      return this.username;
   }

   public byte[] getDecodedPasswordDigest() {
      return this.decodedPwdDigest;
   }

   public byte[] getDecodedNonce() {
      return this.nonce;
   }

   public byte[] getPassword() {
      return this.password != null ? (new String(this.password)).getBytes() : null;
   }

   public String getPasswordType() {
      return this.passwordType;
   }

   public String getEncodedNonce() {
      return this.encodedNonce;
   }

   public String getNonceEncodingType() {
      return this.nonceEncodingType;
   }

   public Calendar getCreated() {
      return this.created;
   }

   public String getCreatedString() {
      return this.serializedCreated;
   }

   public SecurityToken getSecurityToken(String var1, Object var2, ContextHandler var3) throws WSSecurityException {
      return new UsernameTokenImpl((UsernameAndPassword)var2, var3);
   }

   public void marshal(Element var1, Node var2, Map var3) throws MarshalException {
      Map var4 = DOMUtils.getNamespaceMap(var1);
      String var5 = (String)var3.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
      String var6 = (String)var3.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
      Element var7 = DOMUtils.createElement(var1, WSSConstants.UNT_QNAME, var5);
      if (this.id != null) {
         DOMUtils.addPrefixedAttribute(var7, WSSConstants.WSU_ID_QNAME, var6, this.id);
         DOMUtils.declareNamespace(var7, WSSConstants.WSU_ID_QNAME.getNamespaceURI(), var6, var4);
      }

      Element var8 = DOMUtils.createAndAddElement(var7, WSSConstants.USERNAME_QNAME, var5);
      DOMUtils.addText(var8, this.username);
      Element var9;
      if (this.usePassword) {
         var9 = DOMUtils.createAndAddElement(var7, WSSConstants.PASSWORD_QNAME, var5);
         if (this.passwordId != null) {
            DOMUtils.addPrefixedAttribute(var9, WSSConstants.WSU_ID_QNAME, var6, this.passwordId);
            DOMUtils.declareNamespace(var9, WSSConstants.WSU_ID_QNAME.getNamespaceURI(), var6, var4);
         }

         DOMUtils.addAttribute(var9, WSSConstants.TYPE_QNAME, this.passwordType);
         if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest".equals(this.passwordType)) {
            this.createPasswordDigest();
            DOMUtils.addText(var9, this.passwordDigest);
         } else {
            if (this.useCreated) {
               this.createCreated();
            }

            if (this.useNonce) {
               this.createNonce();
            }

            DOMUtils.addText(var9, new String(this.password));
         }
      }

      if (this.useNonce) {
         var9 = DOMUtils.createAndAddElement(var7, WSSConstants.NONCE_QNAME, var5);
         DOMUtils.addAttribute(var9, WSSConstants.ENCODING_TYPE_QNAME, this.nonceEncodingType);
         DOMUtils.addText(var9, this.encodedNonce);
      }

      if (this.useCreated) {
         var9 = DOMUtils.createAndAddElement(var7, WSSConstants.CREATED_QNAME, var6);
         DOMUtils.addText(var9, XSDDateTime.getXml(this.created));
      }

      if (var2 != null) {
         var1.insertBefore(var7, var2);
      } else {
         var1.appendChild(var7);
      }

   }

   public void unmarshal(Node var1) throws MarshalException {
      Element var2 = (Element)var1;
      this.id = DOMUtils.getAttributeValue(var2, WSSConstants.WSU_ID_QNAME);
      Element var3 = DOMUtils.getFirstElement(var2);
      if (var3 == null) {
         throw new MarshalException("UsernameToken is empty.");
      } else {
         DOMUtils.require(var3, WSSConstants.USERNAME_QNAME);
         if (null == var3.getFirstChild()) {
            throw new MarshalException("UsernameToken username is empty.");
         } else {
            this.username = DOMUtils.getText(var3);

            for(Element var4 = DOMUtils.getNextElement(var3); var4 != null; var4 = DOMUtils.getNextElement(var4)) {
               if (DOMUtils.is(var4, WSSConstants.PASSWORD_QNAME)) {
                  this.unmarshalPassword(var4);
               } else if (DOMUtils.is(var4, WSSConstants.NONCE_QNAME)) {
                  this.unmarshalNonce(var4);
               } else {
                  if (!DOMUtils.is(var4, WSSConstants.CREATED_QNAME)) {
                     throw new MarshalException("Unsupported child element " + DOMUtils.getQName(var4) + " in " + "UsernameToken");
                  }

                  this.unmarshalCreated(var4);
               }
            }

         }
      }
   }

   private void createCreated() {
      this.created = TimestampImpl.getCalendar();
      this.serializedCreated = XSDDateTime.getXml(this.created);
   }

   private void createNonce() {
      this.nonce = Utils.generateNonce(32);
      this.encodedNonce = ((Encoding)encodings.get(this.nonceEncodingType)).encode(this.nonce);
   }

   private void createPasswordDigest() {
      try {
         if (this.useCreated) {
            this.createCreated();
         }

         if (this.useNonce) {
            this.createNonce();
         }

         byte[] var1 = PasswordDigestUtils.passwordDigest(this.nonce, this.getCreatedString(), (new String(this.password)).getBytes("UTF-8"));
         Encoding var2 = (Encoding)encodings.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
         this.passwordDigest = var2.encode(var1);
      } catch (NoSuchAlgorithmException var3) {
      } catch (UnsupportedEncodingException var4) {
      }

   }

   private void unmarshalCreated(Element var1) throws MarshalException {
      if (null != var1.getFirstChild() && null != DOMUtils.getText(var1)) {
         if (this.serializedCreated != null) {
            throw new MarshalException("Only one Created element allowed in UsernameToken.");
         } else {
            this.serializedCreated = DOMUtils.getText(var1);
            this.created = XSDDateTime.convertXml(this.serializedCreated);
         }
      } else {
         throw new MarshalException("Invalid Createde.");
      }
   }

   private void unmarshalNonce(Element var1) throws MarshalException {
      if (this.encodedNonce != null) {
         throw new MarshalException("Only one Nonce element allowed in UsernameToken.");
      } else if (null != var1.getFirstChild() && null != DOMUtils.getText(var1)) {
         this.nonceEncodingType = DOMUtils.getAttributeValue(var1, WSSConstants.ENCODING_TYPE_QNAME, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
         this.encodedNonce = DOMUtils.getText(var1);
         this.nonce = ((Encoding)encodings.get(this.nonceEncodingType)).decode(this.encodedNonce);
      } else {
         throw new MarshalException("Invalid Nonce.");
      }
   }

   private void unmarshalPassword(Element var1) throws MarshalException {
      if (this.password == null && this.passwordDigest == null) {
         if (null != var1.getFirstChild() && null != DOMUtils.getText(var1)) {
            this.passwordId = DOMUtils.getAttributeValue(var1, WSSConstants.WSU_ID_QNAME);
            String var2 = DOMUtils.getAttributeValue(var1, WSSConstants.TYPE_QNAME);
            if (var2 != null) {
               this.passwordType = var2;
            }

            if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText".equals(this.passwordType)) {
               this.password = DOMUtils.getText(var1).toCharArray();
            } else {
               if (!"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest".equals(this.passwordType)) {
                  throw new MarshalException("Invalid Password Type.");
               }

               this.passwordDigest = DOMUtils.getText(var1);
               this.decodedPwdDigest = Utils.base64(this.passwordDigest);
            }

         } else {
            throw new MarshalException("Invalid Password.");
         }
      } else {
         throw new MarshalException("Only one Password element allowed in UsernameToken.");
      }
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public boolean supports(Purpose var1) {
      return Purpose.IDENTITY.equals(var1);
   }

   public Element getSecurityTokenAssertion(Element var1, Purpose var2, ContextHandler var3) throws WSSecurityConfigurationException {
      String var4 = DOMUtils.getPrefix("http://www.bea.com/wls90/security/policy", var1);
      Element var5 = DOMUtils.createAndAddElement(var1, WSSConstants.POLICY_TOKEN_QNAME, var4);
      DOMUtils.addAttribute(var5, WSSConstants.POLICY_TOKEN_TYPE_QNAME, valueTypes[0]);
      Element var6 = DOMUtils.createAndAddElement(var5, WSSConstants.POLICY_USE_PASSWD_QNAME, var4);
      boolean var7 = false;
      List var8 = (List)var3.getValue("com.bea.contextelement.wsee.tokenHandlers");
      if (var8 != null) {
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            SecurityTokenHandler var10 = (SecurityTokenHandler)var9.next();
            if (var10 instanceof UsernameTokenHandler) {
               var7 = ((UsernameTokenHandler)var10).isPasswordDigestSupported();
               break;
            }
         }
      }

      if (var7) {
         DOMUtils.addAttribute(var6, WSSConstants.POLICY_USE_PASSWD_TYPE_QNAME, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest");
      } else {
         DOMUtils.addAttribute(var6, WSSConstants.POLICY_USE_PASSWD_TYPE_QNAME, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
      }

      return var5;
   }

   public String getNonce() {
      return this.getEncodedNonce();
   }

   public long getCreatedTimeInMillis() {
      return this.getCreated().getTimeInMillis();
   }

   public String getPasswordDigest() {
      return this.passwordDigest;
   }

   static {
      initEncodings();
   }
}
