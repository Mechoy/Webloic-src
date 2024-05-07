package weblogic.xml.security.specs;

import java.util.ArrayList;
import javax.xml.namespace.QName;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public class ElementIdentifier extends SpecBase {
   private String localPart;
   private String namespace;
   private String restriction;
   private XMLName name;

   public ElementIdentifier(String var1, String var2) {
      this(var1, var2, (String)null);
   }

   public ElementIdentifier(QName var1, String var2) {
      this(var1.getNamespaceURI(), var1.getLocalPart(), var2);
   }

   public ElementIdentifier(QName var1) {
      this((QName)var1, (String)null);
   }

   public ElementIdentifier(String var1, String var2, String var3) {
      this.localPart = null;
      this.namespace = null;
      this.restriction = null;
      this.name = null;
      if (var2 == null) {
         throw new IllegalArgumentException("Type cannot be null");
      } else if (!validRestriction(var3)) {
         throw new IllegalArgumentException("Unsupported restriction: " + var3);
      } else {
         this.localPart = var2;
         this.namespace = var1;
         this.restriction = var3;
      }
   }

   public ElementIdentifier(XMLInputStream var1, String var2) throws XMLStreamException {
      this.localPart = null;
      this.namespace = null;
      this.restriction = null;
      this.name = null;
      this.fromXMLInternal(var1, var2);
   }

   public XMLName getXMLName() {
      if (this.name == null) {
         this.name = ElementFactory.createXMLName(this.namespace, this.localPart);
      }

      return this.name;
   }

   public String getRestriction() {
      return this.restriction;
   }

   public String getLocalName() {
      return this.localPart;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      ArrayList var4 = new ArrayList(3);
      if (this.localPart != null) {
         var4.add(ElementFactory.createAttribute(var2, "LocalPart", this.localPart));
      }

      if (this.namespace != null) {
         var4.add(ElementFactory.createAttribute(var2, "Namespace", this.namespace));
      }

      if (this.restriction != null) {
         var4.add(ElementFactory.createAttribute(var2, "Restriction", this.restriction));
      }

      Attribute[] var5 = new Attribute[var4.size()];
      var4.toArray(var5);
      StreamUtils.addStart(var1, var2, "ElementIdentifier", var5, var3);
      StreamUtils.addEnd(var1, var2, "ElementIdentifier", var3);
   }

   public static final boolean validRestriction(String var0) {
      return var0 == null || "body".equals(var0) || "header".equals(var0);
   }

   protected void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)StreamUtils.getElement(var1, var2, "ElementIdentifier");
      if (var3 == null) {
         throw new XMLStreamException("Expected ElementIdentifier, got " + var3);
      } else {
         this.localPart = StreamUtils.getAttribute(var3, "LocalPart");
         StreamUtils.requiredAttr(this.localPart, "ElementIdentifier", "LocalPart");
         this.namespace = StreamUtils.getAttribute(var3, "Namespace");
         this.restriction = StreamUtils.getAttribute(var3, "Restriction");
         StreamUtils.closeScope(var1, var2, "ElementIdentifier");
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ElementIdentifier)) {
         return false;
      } else {
         ElementIdentifier var2;
         label44: {
            var2 = (ElementIdentifier)var1;
            if (this.localPart != null) {
               if (this.localPart.equals(var2.localPart)) {
                  break label44;
               }
            } else if (var2.localPart == null) {
               break label44;
            }

            return false;
         }

         if (this.namespace != null) {
            if (!this.namespace.equals(var2.namespace)) {
               return false;
            }
         } else if (var2.namespace != null) {
            return false;
         }

         if (this.restriction != null) {
            if (!this.restriction.equals(var2.restriction)) {
               return false;
            }
         } else if (var2.restriction != null) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int var1 = this.localPart != null ? this.localPart.hashCode() : 0;
      var1 = 29 * var1 + (this.namespace != null ? this.namespace.hashCode() : 0);
      var1 = 29 * var1 + (this.restriction != null ? this.restriction.hashCode() : 0);
      return var1;
   }

   public String toString() {
      return "ElementIdentifier type=" + this.localPart + " namespace=" + this.namespace + " restriction=" + this.restriction;
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<ElementIdentifier xmlns=\"http://www.openuri.org/2002/11/wsse/spec\"\n      LocalPart=\"fooBarBaz\"\n      Restriction=\"body\"\n      Namespace=\"wsse:Base64Binary\" />");
      ElementIdentifier var2 = new ElementIdentifier(var1, "http://www.openuri.org/2002/11/wsse/spec");
      System.out.println(var2);
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3);
      var3.flush();
   }
}
