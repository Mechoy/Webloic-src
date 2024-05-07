package weblogic.xml.security.specs;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.NSOutputStream;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.wsse.BinarySecurityToken;
import weblogic.xml.security.wsse.Token;
import weblogic.xml.security.wsse.v200207.BinarySecurityTokenImpl;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public class EncryptionSpec extends OperationSpec {
   private String encryptionMethod;
   private String keyWrappingMethod;
   private X509Certificate cert;
   private BinarySecurityToken bst;
   private static final String DEFAULT_ENCRYPTION_METHOD = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc";
   private static final String DEFAULT_KEYWRAPPING_METHOD = "http://www.w3.org/2001/04/xmlenc#rsa-1_5";
   private static final EncryptionSpec DEFAULT_SPEC = new EncryptionSpec("http://www.w3.org/2001/04/xmlenc#tripledes-cbc", "http://www.w3.org/2001/04/xmlenc#rsa-1_5", (X509Certificate)null, true);

   public EncryptionSpec(String var1, X509Certificate var2) {
      this(var1, "http://www.w3.org/2001/04/xmlenc#rsa-1_5", var2, false);
   }

   public EncryptionSpec(String var1, X509Certificate var2, boolean var3) {
      this(var1, "http://www.w3.org/2001/04/xmlenc#rsa-1_5", var2, var3);
   }

   public EncryptionSpec(String var1, String var2, X509Certificate var3, boolean var4) {
      this.encryptionMethod = null;
      this.keyWrappingMethod = null;
      this.cert = null;
      this.bst = null;
      this.encryptionMethod = var1;
      this.setKeyWrappingMethod(var2);
      this.setCertificate(var3);
      this.setEntireBody(var4);
   }

   public EncryptionSpec(XMLInputStream var1, String var2) throws XMLStreamException {
      this.encryptionMethod = null;
      this.keyWrappingMethod = null;
      this.cert = null;
      this.bst = null;
      this.fromXMLInternal(var1, var2);
   }

   public X509Certificate getCertificate() {
      return this.cert;
   }

   public void setCertificate(X509Certificate var1) {
      this.cert = var1;
      if (var1 != null) {
         this.bst = new BinarySecurityTokenImpl(var1, (PrivateKey)null);
      } else {
         this.bst = null;
      }

   }

   public Token getToken() {
      return this.bst;
   }

   public String getEncryptionMethod() {
      return this.encryptionMethod;
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      if (!(var1 instanceof NSOutputStream)) {
         NamespaceAwareXOS var4 = null;
         var1 = var4 = new NamespaceAwareXOS((XMLOutputStream)var1);
         var4.addPrefix("http://www.openuri.org/2002/11/wsse/spec", "spec");
      }

      Attribute[] var8 = new Attribute[]{ElementFactory.createAttribute(var2, "EncryptionMethod", this.encryptionMethod), ElementFactory.createAttribute(var2, "KeyWrappingMethod", this.getKeyWrappingMethod()), ElementFactory.createAttribute(var2, "EncryptBody", this.entireBody() ? "true" : "false")};
      StreamUtils.addStart((XMLOutputStream)var1, var2, "EncryptionSpec", var8, var3);
      int var5 = var3 + 2;
      if (this.bst != null) {
         this.bst.toXML((XMLOutputStream)var1);
      }

      Iterator var6 = this.headerList.iterator();

      ElementIdentifier var7;
      while(var6.hasNext()) {
         var7 = (ElementIdentifier)var6.next();
         var7.toXML((XMLOutputStream)var1, var2, var5);
      }

      var6 = this.bodyList.iterator();

      while(var6.hasNext()) {
         var7 = (ElementIdentifier)var6.next();
         var7.toXML((XMLOutputStream)var1, var2, var5);
      }

      var6 = this.unrestrictedList.iterator();

      while(var6.hasNext()) {
         var7 = (ElementIdentifier)var6.next();
         var7.toXML((XMLOutputStream)var1, var2, var5);
      }

      StreamUtils.addEnd((XMLOutputStream)var1, var2, "EncryptionSpec", var3);
   }

   protected void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)StreamUtils.getElement(var1, var2, "EncryptionSpec");
      if (var3 == null) {
         throw new XMLStreamException("Expected EncryptionSpec, got " + var3);
      } else {
         this.encryptionMethod = StreamUtils.getAttribute(var3, "EncryptionMethod");
         StreamUtils.requiredAttr(this.encryptionMethod, "EncryptionSpec", "EncryptionMethod");
         this.keyWrappingMethod = StreamUtils.getAttribute(var3, "KeyWrappingMethod");
         this.setEntireBody("true".equals(StreamUtils.getAttribute(var3, "EncryptBody")));
         XMLEvent var4 = StreamUtils.peekElement(var1);
         if (var4.isStartElement() && StreamUtils.matches(var4, "BinarySecurityToken", WSSEConstants.WSSE_URI)) {
            this.bst = new BinarySecurityTokenImpl(var1, WSSEConstants.WSSE_URI);
            this.cert = this.bst.getCertificate();
            var4 = StreamUtils.peekElement(var1);
         }

         for(; !var4.isEndElement(); var4 = StreamUtils.peekElement(var1)) {
            if (var4.isStartElement() && StreamUtils.matches(var4, "ElementIdentifier", var2)) {
               ElementIdentifier var5 = new ElementIdentifier(var1, var2);
               String var6 = var5.getRestriction();
               if (var6 == null) {
                  this.addUnrestrictedType(var5);
               } else if ("body".equals(var6)) {
                  this.addBodyElement(var5);
               } else {
                  this.addHeaderElement(var5);
               }
            } else {
               StreamUtils.discard(var1);
            }
         }

         StreamUtils.closeScope(var1, var2, "EncryptionSpec");
      }
   }

   public static EncryptionSpec getDefaultSpec() {
      return DEFAULT_SPEC;
   }

   public String toString() {
      return "weblogic.xml.security.specs.EncryptionSpec{encryptionMethod='" + this.encryptionMethod + "'" + ", cert=" + this.cert + ", bst=" + this.bst + "}";
   }

   public String getKeyWrappingMethod() {
      return this.keyWrappingMethod == null ? "http://www.w3.org/2001/04/xmlenc#rsa-1_5" : this.keyWrappingMethod;
   }

   public void setKeyWrappingMethod(String var1) {
      if (var1 != null && !"http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p".equals(var1) && !"http://www.w3.org/2001/04/xmlenc#rsa-1_5".equals(var1)) {
         throw new IllegalArgumentException(var1 + " is not a supported key wrapping" + " algorithm");
      } else {
         this.keyWrappingMethod = var1;
      }
   }
}
