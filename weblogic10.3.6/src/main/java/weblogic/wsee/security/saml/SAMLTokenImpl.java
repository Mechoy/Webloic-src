package weblogic.wsee.security.saml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.security.auth.Subject;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import weblogic.xml.crypto.wss.SecurityTokenImpl;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.dom.marshal.MarshalException;

public class SAMLTokenImpl extends SecurityTokenImpl implements SAMLToken {
   private static final long serialVersionUID = 5452212188047058232L;
   private SAMLCredentialImpl samlCredential;
   private Subject subject;

   public SAMLTokenImpl() {
   }

   public SAMLTokenImpl(Object var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Can not create SAML token with null credential. Please check the configuration.");
      } else {
         this.samlCredential = (SAMLCredentialImpl)var1;
      }
   }

   public SAMLTokenImpl(String var1, Object var2) {
      this(var2);
      this.samlCredential.setTokenType(var1);
   }

   public Object getCredential() {
      return this.samlCredential;
   }

   public String getValueType() {
      return this.samlCredential.getTokenType();
   }

   public String getId() {
      return this.getAssertionID();
   }

   public boolean isSaml2() {
      return this.samlCredential.isSaml2();
   }

   public void setId(String var1) {
      throw new UnsupportedOperationException("Can't set ID on SAML Assertion");
   }

   public PrivateKey getPrivateKey() {
      return this.samlCredential.getPrivateKey();
   }

   public PublicKey getPublicKey() {
      Key var1 = this.samlCredential.getHolderOfKey();
      return var1 instanceof PublicKey ? (PublicKey)var1 : null;
   }

   public Key getSecretKey() {
      if (null == this.samlCredential.getSymmetircKey() && null == this.samlCredential.getHolderOfKey()) {
         return null;
      } else if (null != this.samlCredential.getSymmetircKey()) {
         return this.samlCredential.getSymmetircKey();
      } else {
         Key var1 = this.samlCredential.getHolderOfKey();
         return !(var1 instanceof PublicKey) && !(var1 instanceof PrivateKey) ? var1 : null;
      }
   }

   public X509Certificate getHolderOfCert() {
      return this.samlCredential.getX509Cert();
   }

   public Subject getSubject() {
      return this.subject;
   }

   public void setSubject(Subject var1) {
      this.subject = var1;
   }

   public void marshal(Element var1, Node var2, Map var3) throws MarshalException {
      Object var4 = this.samlCredential.getCredential();
      Node var5;
      if (var4 instanceof String) {
         DocumentBuilder var6 = null;

         try {
            var6 = CSSUtils.getParser();
            Document var7 = var6.parse(new ByteArrayInputStream(((String)var4).getBytes()));
            var5 = var1.getOwnerDocument().importNode(var7.getFirstChild(), true);
         } catch (SAXException var13) {
            throw new MarshalException(var13);
         } catch (IOException var14) {
            throw new MarshalException(var14);
         } finally {
            if (var6 != null) {
               CSSUtils.returnParser(var6);
            }

         }
      } else {
         if (!(var4 instanceof Element)) {
            throw new MarshalException("do not know how to marshal: " + var4);
         }

         Element var16 = (Element)var4;
         var5 = var1.getOwnerDocument().importNode(var16, true);
      }

      if (var2 != null) {
         var1.insertBefore(var5, var2);
      } else {
         var1.appendChild(var5);
      }

   }

   public void unmarshal(Node var1) throws MarshalException {
      try {
         this.samlCredential = new SAMLCredentialImpl(var1);
      } catch (WSSecurityException var3) {
         this.samlCredential = null;
         throw new MarshalException(var3);
      }
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public String getAssertionID() {
      return this.samlCredential.getAssertionID();
   }

   public boolean isHolderOfKey() {
      return this.samlCredential.isHolderOfKey();
   }

   public boolean equals(Object var1) {
      return var1 != null && var1 instanceof SAMLToken ? ((SAMLToken)var1).getAssertionID().equals(this.getAssertionID()) : false;
   }
}
