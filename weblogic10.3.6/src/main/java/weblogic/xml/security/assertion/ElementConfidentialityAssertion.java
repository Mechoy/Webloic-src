package weblogic.xml.security.assertion;

import javax.xml.namespace.QName;
import weblogic.xml.security.specs.ElementIdentifier;

/** @deprecated */
public class ElementConfidentialityAssertion extends ConfidentialityAssertion implements ElementAssertion {
   private final QName qname;
   private final String localName;
   private final String namespace;
   private final String restriction;

   public ElementConfidentialityAssertion(String var1, String var2, String var3) {
      super((String)null);
      this.localName = var1;
      this.namespace = var2;
      this.restriction = var3;
      StringBuffer var4 = new StringBuffer();
      if (var3 != null) {
         var4.append(var3).append(":");
      }

      if ("body".equals(var3) && var1 == null) {
         var4.append("body");
         this.qname = null;
      } else {
         if (var1 == null) {
            throw new AssertionError("must provide a localName");
         }

         this.qname = new QName(var2, var1);
         var4.append(this.qname.toString());
      }

      this.id = var4.toString();
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

   public String getRestriction() {
      return this.restriction;
   }

   public final boolean isAssertionType(String var1) {
      return "http://www.bea.com/servers/xml/security/assertion/TypeConfidentiality".equals(var1);
   }

   public final String getAssertionType() {
      return "http://www.bea.com/servers/xml/security/assertion/TypeConfidentiality";
   }

   public final int getAssertionTypeCode() {
      return 4;
   }

   public final boolean satisfies(ElementIdentifier var1) {
      return ElementAssertionUtils.satisfies(this.localName, this.namespace, this.restriction, var1);
   }

   public final String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("(").append("ElementConfidentialityAssertion");
      var1.append(" ").append(this.qname);
      if (this.restriction != null) {
         var1.append("\n    :restriction \"").append(this.restriction).append("\"");
      }

      var1.append(")");
      return var1.toString();
   }
}
