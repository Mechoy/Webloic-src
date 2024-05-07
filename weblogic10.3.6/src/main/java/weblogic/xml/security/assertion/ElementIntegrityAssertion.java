package weblogic.xml.security.assertion;

import java.security.cert.X509Certificate;
import javax.xml.namespace.QName;
import weblogic.xml.security.specs.ElementIdentifier;
import weblogic.xml.stream.XMLName;

/** @deprecated */
public class ElementIntegrityAssertion extends IntegrityAssertion implements ElementAssertion {
   private final QName qname;
   private final String localName;
   private final String namespace;
   private final String restriction;

   public ElementIntegrityAssertion(String var1, X509Certificate var2, String var3, String var4, String var5) {
      super(var1, createIdString(var4, var3, var5), var2);
      this.localName = var3;
      this.namespace = var4;
      this.restriction = var5;
      if ("body".equals(var5) && var3 == null) {
         this.qname = null;
      } else {
         if (var3 == null || var4 == null) {
            throw new AssertionError("must provide a fully qualified type name");
         }

         this.qname = new QName(var4, var3);
      }

   }

   private static String createIdString(String var0, String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      if (var2 != null) {
         var3.append(var2).append(":");
      }

      if ("body".equals(var2) && var1 == null) {
         var3.append("body");
      } else {
         if (var0 != null) {
            var3.append("[").append(var0).append("]");
         }

         var3.append(var1);
      }

      String var4 = var3.toString();
      return var4;
   }

   public ElementIntegrityAssertion(String var1, X509Certificate var2, XMLName var3, String var4) {
      this(var1, var2, var3.getLocalName(), var3.getNamespaceUri(), var4);
   }

   public String getPolicyString() {
      return "Integrity{" + this.id + "}";
   }

   public String getRestriction() {
      return this.restriction;
   }

   public QName getElementName() {
      return this.qname;
   }

   public String getElementLocalName() {
      return this.localName;
   }

   public String getElementNamespace() {
      return this.namespace;
   }

   public String getId() {
      return this.id;
   }

   public final boolean isAssertionType(String var1) {
      return "http://www.bea.com/servers/xml/security/assertion/TypeIntegrity".equals(var1);
   }

   public final String getAssertionType() {
      return "http://www.bea.com/servers/xml/security/assertion/TypeIntegrity";
   }

   public final int getAssertionTypeCode() {
      return 2;
   }

   public final boolean satisfies(ElementIdentifier var1) {
      return ElementAssertionUtils.satisfies(this.localName, this.namespace, this.restriction, var1);
   }

   public final String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("(").append("ElementIntegrityAssertion");
      var1.append(" ").append(this.qname);
      var1.append("\n    :signatureMethod \"").append(this.getSignatureMethod()).append("\"");
      var1.append("\n    :repudiable \"").append(this.repudiable()).append("\"");
      if (this.restriction != null) {
         var1.append("\n    :restriction \"").append(this.restriction).append("\"");
      }

      X509Certificate var2 = this.getCertificate();
      if (var2 != null) {
         var1.append("\n    :certificate \"").append(var2.getSubjectDN()).append("\"");
      }

      var1.append(")");
      return var1.toString();
   }
}
