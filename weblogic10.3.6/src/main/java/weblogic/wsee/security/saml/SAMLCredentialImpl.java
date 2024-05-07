package weblogic.wsee.security.saml;

import java.io.CharArrayWriter;
import java.security.Key;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.security.utils.SAMLAssertionInfo;
import weblogic.security.utils.SAMLAssertionInfoFactory;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.common.keyinfo.EncryptedKeyProvider;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.api.keyinfo.X509Data;
import weblogic.xml.crypto.dsig.keyinfo.KeyInfoImpl;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.wss.SecurityImpl;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.dom.DOMStreamReader;
import weblogic.xml.dom.DOMUtils;

public class SAMLCredentialImpl implements SAMLCredential {
   private static final boolean verbose = Verbose.isVerbose(SAMLCredentialImpl.class);
   private static final boolean debug = false;
   private static final boolean DEBUG_SX_INTEROP_ISSUED_TOKEN = false;
   private Key symmetircKey;
   private PrivateKey privateKey;
   private X509Certificate cert;
   private Key holderOfKey;
   private boolean isHolderOfKey;
   private Element encryptedKey;
   private EncryptedKeyProvider encryptedKeyProvider;
   private String tokenType;
   private String version;
   private boolean isSaml2;
   private Object credential;
   private SAMLAssertionInfo samlAsst;
   private SecurityTokenReference securityTokenReference;
   private SAMLAttributeStatementData attributes;

   protected SAMLCredentialImpl() throws WSSecurityException {
      this.symmetircKey = null;
      this.privateKey = null;
      this.cert = null;
      this.holderOfKey = null;
      this.isHolderOfKey = false;
      this.encryptedKey = null;
      this.encryptedKeyProvider = null;
      this.tokenType = null;
      this.version = null;
      this.isSaml2 = false;
      this.credential = null;
      this.samlAsst = null;
      this.attributes = null;
   }

   public SAMLCredentialImpl(Node var1) throws WSSecurityException {
      this((String)null, var1);
   }

   public SAMLCredentialImpl(String var1, Object var2) throws WSSecurityException {
      this(var1, var2, (PrivateKey)null);
   }

   public void verbose() {
      if (verbose) {
         Verbose.log((Object)("Assertion ID: " + this.samlAsst.getId()));
         Verbose.log((Object)("Assertion CM: " + this.samlAsst.getSubjectConfirmationMethod()));
         Verbose.log((Object)("Assertion Subject: " + this.samlAsst.getSubjectName()));
         Verbose.log((Object)("Assertion Version: " + this.samlAsst.getVersion()));
      }

   }

   public SAMLCredentialImpl(String var1, Object var2, PrivateKey var3) throws WSSecurityException {
      this.symmetircKey = null;
      this.privateKey = null;
      this.cert = null;
      this.holderOfKey = null;
      this.isHolderOfKey = false;
      this.encryptedKey = null;
      this.encryptedKeyProvider = null;
      this.tokenType = null;
      this.version = null;
      this.isSaml2 = false;
      this.credential = null;
      this.samlAsst = null;
      this.attributes = null;
      if (verbose) {
         Verbose.log((Object)("tokenType: " + var1 + ", cred: " + var2 + ", privkey: " + var3));
      }

      if (verbose) {
         Verbose.log((Object)("Class of cred is: " + var2.getClass().toString()));
      }

      Element var5 = null;
      DocumentBuilder var6 = null;

      try {
         if (verbose) {
            Verbose.log((Object)"Instantiating SAMLAssertionInfoFactory");
         }

         SAMLAssertionInfoFactory var7 = SAMLAssertionInfoFactory.getInstance();
         if (var2 instanceof Node) {
            var6 = CSSUtils.getParser();
            Document var8 = var6.newDocument();
            Node var9 = var8.importNode((Node)var2, true);
            var8.appendChild(var9);

            try {
               if (verbose) {
                  Verbose.log((Object)"Getting SAMLAssertionInfo from DOM Element of CSS");
               }

               this.samlAsst = var7.getSAMLAssertionInfo((Element)var9);
               var5 = SAMLAssertionInfoImpl.getSamlAssertionSignatureKeyInfoElement((Element)var9);
            } catch (Exception var16) {
               if (verbose) {
                  Verbose.log((Object)("Got erroron on SAMLAssertionInfo from DOM Element of CSS, msg =" + var16.getMessage()));
               }
            }
         } else {
            if (!(var2 instanceof String)) {
               throw new WSSecurityException("Invalid SAML token", WSSConstants.FAILURE_TOKEN_INVALID);
            }

            if (verbose) {
               Verbose.log((Object)"Getting SAMLAssertionInfo from String XML");
            }

            this.samlAsst = var7.getSAMLAssertionInfo((String)var2);
         }

         if (this.samlAsst == null) {
            throw new WSSecurityException("Invalid SAML token when samlAsst= null", WSSConstants.FAILURE_TOKEN_INVALID);
         }
      } catch (Exception var17) {
         throw new WSSecurityException("Invalid SAML token on CCS?" + var17.getMessage(), var17, WSSConstants.FAILURE_TOKEN_INVALID);
      } finally {
         if (var6 != null) {
            CSSUtils.returnParser(var6);
         }

      }

      if (verbose) {
         Verbose.log((Object)"Got SAMLAssertionInfo");
      }

      this.setVersion(this.samlAsst.getVersion());
      if (var1 == null) {
         if (this.isSaml2()) {
            var1 = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0";
         } else {
            var1 = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1";
         }
      }

      this.setTokenType(var1);
      this.setPrivateKey(var3);
      this.setCredential(var2);
      this.initHolderOfKey(this.samlAsst, var5);
   }

   private static String toXMLString(Node var0) throws TransformerException {
      CharArrayWriter var1 = new CharArrayWriter();
      Transformer var2 = null;
      var2 = TransformerFactory.newInstance().newTransformer();
      var2.transform(new DOMSource(var0), new StreamResult(var1));
      var1.flush();
      return var1.toString();
   }

   private void setVersion(String var1) {
      this.version = var1;
      if (var1.equals("2.0")) {
         this.isSaml2 = true;
      } else {
         this.isSaml2 = false;
      }

   }

   public SAMLAttributeStatementData getAttributes() {
      return this.attributes;
   }

   public void setAttributes(SAMLAttributeStatementData var1) {
      this.attributes = var1;
   }

   public String getVersion() {
      return this.version;
   }

   public boolean isSaml2() {
      return this.isSaml2;
   }

   public String getAssertionID() {
      return this.samlAsst.getId();
   }

   public String getTokenType() {
      return this.tokenType;
   }

   public void setTokenType(String var1) {
      this.tokenType = var1;
   }

   public void setCredential(Object var1) {
      this.credential = var1;
   }

   public Object getCredential() {
      return this.credential;
   }

   public void setPrivateKey(PrivateKey var1) {
      this.privateKey = var1;
   }

   public PrivateKey getPrivateKey() {
      return this.privateKey;
   }

   public boolean isHolderOfKey() {
      return this.isHolderOfKey;
   }

   public Key getHolderOfKey() {
      return this.holderOfKey;
   }

   public Key getSymmetircKey() {
      return this.symmetircKey;
   }

   public void setSymmetircKey(Key var1) {
      this.symmetircKey = var1;
   }

   public Element getEncryptedKey() {
      return this.encryptedKey;
   }

   public EncryptedKeyProvider getEncryptedKeyProvider() {
      return this.encryptedKeyProvider;
   }

   public void setEncryptedKeyProvider(EncryptedKeyProvider var1) {
      this.encryptedKeyProvider = var1;
   }

   public X509Certificate getX509Cert() {
      return this.cert;
   }

   private void initHolderOfKey(SAMLAssertionInfo var1, Element var2) throws WSSecurityException {
      String var3 = var1.getSubjectConfirmationMethod();
      this.isHolderOfKey = this.isSaml2() && "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key".equals(var3) || !this.isSaml2() && "urn:oasis:names:tc:SAML:1.0:cm:holder-of-key".equals(var3);
      this.holderOfKey = null;
      Element var4 = null;
      if (!this.isHolderOfKey) {
         if (null != var2) {
            var4 = var2;
         }
      } else {
         var4 = var1.getSubjectKeyInfo();
      }

      if (var4 != null) {
         this.encryptedKey = DOMUtils.getFirstElement(var4, SecurityImpl.ENCRYPTED_KEY_QNAME);
         if (this.encryptedKey != null) {
            if (verbose) {
               Verbose.log((Object)("Got Encrypted Key =" + this.encryptedKey));
            }

            return;
         }

         try {
            DOMStreamReader var5 = new DOMStreamReader(var4);
            KeyInfoImpl var6 = new KeyInfoImpl();
            var6.read(var5);
            this.setKeyObjectFromKeyInfo(var6);
         } catch (XMLStreamException var7) {
            throw new WSSecurityException("Invalid SAML token on XML " + var7.getMessage(), var7, WSSConstants.FAILURE_TOKEN_INVALID);
         } catch (MarshalException var8) {
            throw new WSSecurityException("Invalid SAML token  on Marshal " + var8.getMessage(), var8, WSSConstants.FAILURE_TOKEN_INVALID);
         }
      }

   }

   private Key setKeyObjectFromKeyInfo(KeyInfo var1) {
      List var2 = var1.getContent();
      Iterator var3 = var2.iterator();

      while(true) {
         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (var4 instanceof X509Data) {
               X509Data var5 = (X509Data)var4;
               Iterator var6 = var5.getContent().iterator();

               while(var6.hasNext()) {
                  var4 = var6.next();
                  if (var4 instanceof X509Certificate) {
                     this.cert = (X509Certificate)var4;
                     this.holderOfKey = ((X509Certificate)var4).getPublicKey();
                  }
               }
            } else if (var4 instanceof SecurityTokenReference) {
               this.securityTokenReference = (SecurityTokenReference)var4;
            } else if (var4 instanceof EncryptedKey && verbose) {
               Verbose.log((Object)"Got another EncryptedKey object here???? ");
            }
         }

         return null;
      }
   }

   public SecurityTokenReference getSecurityTokenReference() {
      return this.securityTokenReference;
   }
}
