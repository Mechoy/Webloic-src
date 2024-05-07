package weblogic.xml.security.wsse.v200207;

import java.math.BigInteger;
import java.security.Key;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import weblogic.xml.security.SecurityProcessingException;
import weblogic.xml.security.keyinfo.X509IssuerSerial;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.NSOutputStream;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.Utils;
import weblogic.xml.security.wsse.KeyIdentifier;
import weblogic.xml.security.wsse.SecurityTokenReference;
import weblogic.xml.security.wsse.Token;
import weblogic.xml.security.wsse.internal.BaseToken;
import weblogic.xml.security.wsu.v200207.WSUConstants;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class SecurityTokenReferenceImpl extends BaseToken implements WSSEConstants, SecurityTokenReference {
   private String reference = null;
   private KeyIdentifier identifier = null;
   private X509IssuerSerial issuerSerial = null;
   private Token token = null;
   private String id = null;

   public SecurityTokenReferenceImpl(Token var1) {
      this.token = var1;
   }

   public SecurityTokenReferenceImpl(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public final boolean references(Token var1) {
      return var1 != null ? var1.equals(this.token) : false;
   }

   public final String getReference() {
      return this.reference;
   }

   public final void setReference(String var1) {
      this.reference = var1;
   }

   public KeyIdentifier getKeyIdentifier() {
      return this.identifier;
   }

   public X509IssuerSerial getX509IssuerSerial() {
      return this.issuerSerial;
   }

   public void setId(String var1) {
      if (var1 != null) {
         throw new AssertionError("Id for BinarySecurityTokenImpl already set");
      } else {
         this.id = var1;
      }
   }

   public String getId() {
      if (this.id == null) {
         this.id = Utils.generateId("SecurityTokenReference");
      }

      return this.id;
   }

   public void setToken(Token var1) {
      if (this.token != null) {
         throw new AssertionError("Token has already been set for this reference");
      } else {
         this.token = var1;
      }
   }

   public Token getToken() {
      return this.token;
   }

   public PrivateKey getPrivateKey() {
      return this.token == null ? null : this.token.getPrivateKey();
   }

   public Key getSecretKey() {
      return this.token == null ? null : this.token.getSecretKey();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("SecurityTokenReferenceImpl:").append("\n        Id: ").append(this.id).append("\n Reference: ").append(this.reference).append("\n KeyIdentifier: ").append(this.identifier);
      return var1.toString();
   }

   public void toXML(XMLOutputStream var1) throws XMLStreamException {
      this.toXML(var1, WSSE_URI, 0);
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      NamespaceAwareXOS var4;
      if (!(var1 instanceof NSOutputStream)) {
         var4 = new NamespaceAwareXOS((XMLOutputStream)var1);
         var4.addPrefix(WSSE_URI, "wsse");
         var1 = var4;
      }

      var4 = null;
      if (this.id != null) {
         Attribute[] var8 = new Attribute[]{ElementFactory.createAttribute(WSUConstants.WSU_URI, "Id", this.getId())};
         StreamUtils.addStart((XMLOutputStream)var1, var2, "SecurityTokenReference", var8, var3);
      } else {
         StreamUtils.addStart((XMLOutputStream)var1, var2, (String)"SecurityTokenReference", var3);
      }

      if (this.reference != null) {
         this.referenceToXML((XMLOutputStream)var1, var2, var3);
      } else {
         this.identifier = this.token.getKeyIdentifier();
         if (this.identifier != null) {
            this.identifierToXML((XMLOutputStream)var1, var2, var3);
         } else {
            X509Certificate var5 = this.token.getCertificate();
            Principal var6 = var5.getIssuerDN();
            BigInteger var7 = var5.getSerialNumber();
            if (var6 == null || var7 == null) {
               throw new SecurityProcessingException("unable to create token reference for " + this.token);
            }

            this.issuerSerial = new X509IssuerSerial(var6.getName(), var7);
            this.issuerSerialToXML((XMLOutputStream)var1, var3);
         }
      }

      StreamUtils.addEnd((XMLOutputStream)var1, var2, "SecurityTokenReference", var3);
   }

   private void issuerSerialToXML(XMLOutputStream var1, int var2) throws XMLStreamException {
      this.issuerSerial.toXML(var1, "http://www.w3.org/2000/09/xmldsig#", var2);
   }

   private void identifierToXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      this.identifier.toXML(var1, var2, var3);
   }

   private void referenceToXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      Attribute[] var4 = new Attribute[]{ElementFactory.createAttribute((String)null, "URI", this.reference)};
      StreamUtils.addStart(var1, var2, "Reference", var4, var3);
      StreamUtils.addEnd(var1, var2, "Reference", var3);
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)StreamUtils.getElement(var1, var2, "SecurityTokenReference");
      this.id = StreamUtils.getAttribute(var3, "Id");
      XMLEvent var4 = var1.peek();

      while(true) {
         while(!var4.isEndElement()) {
            if (!var4.isStartElement()) {
               var1.next();
               var4 = var1.peek();
            } else {
               if (StreamUtils.matches(var4, "Reference", var2) && this.reference == null) {
                  this.parseReference(var1, var2);
               } else if (StreamUtils.matches(var4, "KeyIdentifier", var2)) {
                  this.identifier = new KeyIdentifierImpl(var1, var2);
               } else if (StreamUtils.matches(var4, "X509IssuerSerial", "http://www.w3.org/2000/09/xmldsig#")) {
                  this.issuerSerial = X509IssuerSerial.fromXML(var1, "http://www.w3.org/2000/09/xmldsig#");
               } else {
                  StreamUtils.discard(var1);
               }

               var4 = var1.peek();
            }
         }

         StreamUtils.closeScope(var1, var2, "SecurityTokenReference");
         return;
      }
   }

   private void parseReference(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)StreamUtils.getElement(var1, var2, "Reference");
      this.reference = StreamUtils.getAttribute(var3, "URI");
      StreamUtils.closeScope(var1, var2, "Reference");
   }
}
