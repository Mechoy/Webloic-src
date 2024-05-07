package weblogic.xml.security.specs;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import weblogic.xml.security.SecurityConfigurationException;
import weblogic.xml.security.SecurityProcessingException;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.NSOutputStream;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.wsse.Security;
import weblogic.xml.security.wsse.SecurityElementFactory;
import weblogic.xml.security.wsse.Token;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputOutputStream;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.xmlnode.XMLNode;

/** @deprecated */
public class SecuritySpec implements SpecConstants {
   private String role;
   private String id;
   private String namespace;
   private static final String DEFAULT_NAMESPACE;
   public static final String DEFAULT_SPEC_ID = "default-spec";
   private BinarySecurityTokenSpec binarySecurityTokenSpec;
   private UsernameTokenSpec usernameTokenSpec;
   private SignatureSpec signatureSpec;
   private EncryptionSpec encryptionSpec;
   private static final boolean DEBUG;
   private static final SecurityElementFactory factory;

   public SecuritySpec() {
      this((String)null, DEFAULT_NAMESPACE, (String)null);
   }

   public void setRole(String var1) {
      this.role = var1;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public void setNamespace(String var1) {
      this.namespace = var1;
   }

   public SecuritySpec(String var1, String var2) {
      this(var1, var2, (String)null);
   }

   public SecuritySpec(String var1, String var2, String var3) {
      this.id = "default-spec";
      this.binarySecurityTokenSpec = null;
      this.usernameTokenSpec = null;
      this.signatureSpec = null;
      this.encryptionSpec = null;
      this.id = var1;
      this.namespace = var2;
      this.role = var3;
   }

   public SecuritySpec(XMLInputStream var1, String var2) throws XMLStreamException {
      this.id = "default-spec";
      this.binarySecurityTokenSpec = null;
      this.usernameTokenSpec = null;
      this.signatureSpec = null;
      this.encryptionSpec = null;
      this.fromXMLInternal(var1, var2);
   }

   public SecuritySpec(XMLInputStream var1) throws XMLStreamException {
      this.id = "default-spec";
      this.binarySecurityTokenSpec = null;
      this.usernameTokenSpec = null;
      this.signatureSpec = null;
      this.encryptionSpec = null;
      this.fromXMLInternal(var1, "http://www.openuri.org/2002/11/wsse/spec");
   }

   public SignatureSpec setSignatureSpec(String var1, String var2) {
      return this.setSignatureSpec(var1, var2, false);
   }

   public SignatureSpec setSignatureSpec(String var1, String var2, boolean var3) {
      return this.setSignatureSpec(new SignatureSpec(var1, var2, var3));
   }

   public SignatureSpec setSignatureSpec(SignatureSpec var1) {
      return this.signatureSpec = var1;
   }

   public UsernameTokenSpec setUsernameTokenSpec(String var1) {
      return this.setUsernameTokenSpec(var1, (String)null);
   }

   public UsernameTokenSpec setUsernameTokenSpec(String var1, String var2) {
      return this.setUsernameTokenSpec(new UsernameTokenSpec(var1, var2));
   }

   public UsernameTokenSpec setUsernameTokenSpec(UsernameTokenSpec var1) {
      return this.usernameTokenSpec = var1;
   }

   public BinarySecurityTokenSpec setBinarySecurityTokenSpec(String var1) {
      return this.setBinarySecurityTokenSpec(new BinarySecurityTokenSpec(var1, WSSEConstants.ENCODING_BASE64));
   }

   public BinarySecurityTokenSpec setBinarySecurityTokenSpec(BinarySecurityTokenSpec var1) {
      return this.binarySecurityTokenSpec = var1;
   }

   public EncryptionSpec setEncryptionSpec(String var1, X509Certificate var2) {
      return this.setEncryptionSpec(var1, var2, false);
   }

   public EncryptionSpec setEncryptionSpec(String var1, X509Certificate var2, boolean var3) {
      return this.setEncryptionSpec(new EncryptionSpec(var1, var2, var3));
   }

   public EncryptionSpec setEncryptionSpec(EncryptionSpec var1) {
      return this.encryptionSpec = var1;
   }

   public String toString() {
      return "weblogic.xml.security.specs.SecuritySpec{role='" + this.role + "'" + ", id='" + this.id + "'" + ", namespace='" + this.namespace + "'" + ", binarySecurityTokenSpec=" + this.binarySecurityTokenSpec + ", usernameTokenSpec=" + this.usernameTokenSpec + ", signatureSpec=" + this.signatureSpec + ", encryptionSpec=" + this.encryptionSpec + "}";
   }

   public String getRole() {
      return this.role;
   }

   public String getId() {
      return this.id;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public BinarySecurityTokenSpec getBinarySecurityTokenSpec() {
      return this.binarySecurityTokenSpec;
   }

   public UsernameTokenSpec getUsernameTokenSpec() {
      return this.usernameTokenSpec;
   }

   public void setEnablePasswordAuth(boolean var1) {
      if (var1) {
         this.setUsernameTokenSpec(UsernameTokenSpec.getDefaultSpec());
      } else {
         this.setUsernameTokenSpec((UsernameTokenSpec)null);
      }

   }

   public boolean getEnablePasswordAuth() {
      return this.getUsernameTokenSpec() != null;
   }

   public SignatureSpec getSignatureSpec() {
      return this.signatureSpec;
   }

   public EncryptionSpec getEncryptionSpec() {
      return this.encryptionSpec;
   }

   public void toXML(XMLOutputStream var1) throws XMLStreamException {
      this.toXML(var1, "http://www.openuri.org/2002/11/wsse/spec", 0);
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      if (!(var1 instanceof NSOutputStream)) {
         NamespaceAwareXOS var4;
         var1 = var4 = new NamespaceAwareXOS((XMLOutputStream)var1);
         var4.addPrefix("http://www.openuri.org/2002/11/wsse/spec", "spec");
      }

      ArrayList var8 = new ArrayList(3);
      if (this.role != null) {
         XMLName var5 = ElementFactory.createXMLName((String)null, "Role", this.getSoapEnvPrefix());
         var8.add(ElementFactory.createAttribute(var5, this.role));
      }

      if (this.namespace != null) {
         var8.add(ElementFactory.createAttribute("http://www.openuri.org/2002/11/wsse/spec", "Namespace", this.namespace));
      }

      if (this.id != null) {
         var8.add(ElementFactory.createAttribute("http://www.openuri.org/2002/11/wsse/spec", "Id", this.id));
      }

      Attribute[] var7 = new Attribute[var8.size()];
      var8.toArray(var7);
      int var6 = var3 + 2;
      StreamUtils.addStart((XMLOutputStream)var1, "http://www.openuri.org/2002/11/wsse/spec", "SecuritySpec", var7, var3);
      if (this.usernameTokenSpec != null) {
         this.usernameTokenSpec.toXML((XMLOutputStream)var1, var2, var6);
      }

      if (this.binarySecurityTokenSpec != null) {
         this.binarySecurityTokenSpec.toXML((XMLOutputStream)var1, var2, var6);
      }

      if (this.signatureSpec != null) {
         this.signatureSpec.toXML((XMLOutputStream)var1, var2, var6);
      }

      if (this.encryptionSpec != null) {
         this.encryptionSpec.toXML((XMLOutputStream)var1, var2, var6);
      }

      StreamUtils.addEnd((XMLOutputStream)var1, var2, "SecuritySpec");
   }

   public XMLNode getXMLNode() throws IOException {
      XMLOutputStreamFactory var1 = XMLOutputStreamFactory.newInstance();
      XMLInputOutputStream var2 = var1.newInputOutputStream();
      this.toXML(var2);
      XMLNode var3 = new XMLNode();
      var3.read(var2);
      var3.addNamespace("spec", "http://www.openuri.org/2002/11/wsse/spec");
      var3.setName("SecuritySpec", "spec", "http://www.openuri.org/2002/11/wsse/spec");
      return var3;
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)StreamUtils.getElement(var1, var2, "SecuritySpec");
      if (var3 == null) {
         throw new XMLStreamException("Did not receive expected SecuritySpec");
      } else {
         this.id = StreamUtils.getAttribute(var3, "Id");
         this.namespace = StreamUtils.getAttribute(var3, "Namespace");
         if (this.namespace == null) {
            this.namespace = DEFAULT_NAMESPACE;
         }

         this.role = StreamUtils.getAttribute(var3, "Role");
         XMLEvent var4 = StreamUtils.peekElement(var1);
         if (var4.isStartElement() && StreamUtils.matches(var4, "UsernameTokenSpec", "http://www.openuri.org/2002/11/wsse/spec")) {
            this.usernameTokenSpec = new UsernameTokenSpec(var1, var2);
            var4 = StreamUtils.peekElement(var1);
         }

         if (var4.isStartElement() && StreamUtils.matches(var4, "BinarySecurityTokenSpec", "http://www.openuri.org/2002/11/wsse/spec")) {
            this.binarySecurityTokenSpec = new BinarySecurityTokenSpec(var1, var2);
            var4 = StreamUtils.peekElement(var1);
         }

         if (var4.isStartElement() && StreamUtils.matches(var4, "SignatureSpec", "http://www.openuri.org/2002/11/wsse/spec")) {
            this.signatureSpec = new SignatureSpec(var1, var2);
            var4 = StreamUtils.peekElement(var1);
         }

         if (var4.isStartElement() && StreamUtils.matches(var4, "EncryptionSpec", "http://www.openuri.org/2002/11/wsse/spec")) {
            this.encryptionSpec = new EncryptionSpec(var1, var2);
            var4 = StreamUtils.peekElement(var1);
         }

         StreamUtils.closeScope(var1, var2, "SecuritySpec");
      }
   }

   private String getSoapEnvPrefix() {
      return "env";
   }

   public final Security createSecurity(String var1, TimestampConfig var2, String var3, String var4, X509Certificate var5, PrivateKey var6, X509Certificate var7, SecurityElementFactory var8) {
      Security var9 = var8.createSecurity(var1);
      if (var2.generateTimestamp() && this.signatureSpec != null) {
         if (var2.includeExpiry()) {
            var9.addTimestamp(var2.getValidityPeriod());
         } else {
            var9.addTimestamp();
         }
      }

      if (this.usernameTokenSpec != null) {
         if (var3 == null) {
            throw new SecurityConfigurationException("UsernameToken not provided, but required by service");
         }

         Token var12 = var8.createToken(var3, var4, this.usernameTokenSpec.getPasswordType());
         var9.addToken(var12);
      }

      Token var10;
      if (var6 != null && var7 != null) {
         var10 = var8.createToken(var7, var6);
      } else {
         var10 = null;
      }

      if (this.signatureSpec != null) {
         if (var10 == null) {
            throw new SecurityConfigurationException("Service requires signed requests, but no Token was provided");
         }

         try {
            var9.addSignature(var10, this.signatureSpec);
         } catch (SecurityProcessingException var13) {
            if (DEBUG) {
               var13.printStackTrace();
            }

            throw new SecurityConfigurationException("Unable to add signature to request", var13);
         }
      }

      if (this.binarySecurityTokenSpec != null) {
         if (var10 == null) {
            throw new SecurityConfigurationException("Token not provided, but required by service");
         }

         var9.addToken(var10);
      }

      if (this.encryptionSpec != null) {
         if (var5 == null) {
            throw new SecurityConfigurationException("Service requires encryption but no encryption key was was available for recipient");
         }

         Token var11 = var8.createToken((X509Certificate)var5, (PrivateKey)null);

         try {
            var9.addEncryption(var11, this.encryptionSpec);
         } catch (SecurityProcessingException var14) {
            if (DEBUG) {
               var14.printStackTrace();
            }

            throw new SecurityConfigurationException("Failed adding encryption to request", var14);
         }
      }

      return var9;
   }

   static {
      DEFAULT_NAMESPACE = WSSEConstants.WSSE_URI;
      DEBUG = Security.WSSE_VERBOSE;
      factory = SecurityElementFactory.getDefaultFactory();
   }
}
