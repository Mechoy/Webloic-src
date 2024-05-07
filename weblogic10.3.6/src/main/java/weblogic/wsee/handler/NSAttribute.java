package weblogic.wsee.handler;

import java.util.Map;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.stream.events.Name;

public class NSAttribute implements Attribute {
   private StartElement element;
   private Attribute attr;
   private Map namespaceMap;

   public NSAttribute(StartElement var1, Attribute var2) {
      this.attr = var2;
      this.namespaceMap = var1.getNamespaceMap();
      this.element = var1;
   }

   public XMLName getName() {
      return this.attr.getName();
   }

   public String getType() {
      return this.attr.getType();
   }

   public String getValue() {
      return this.attr.getValue();
   }

   public XMLName getSchemaType() {
      return null;
   }

   public XMLName getValueAsXMLName() throws XMLStreamException {
      String var1 = this.getValue();
      String var2 = null;
      String var3 = null;
      String var4 = null;
      int var5 = var1.indexOf(58);
      if (var5 < 0) {
         var4 = this.element.getNamespaceUri("");
         return var4 == null ? new Name(var1) : new Name(var4, var1);
      } else {
         var2 = var1.substring(0, var5);
         var3 = var1.substring(var5 + 1);
         var4 = (String)this.namespaceMap.get(var2);
         if (var4 == null) {
            throw new XMLStreamException("Attribute QName value \"" + var1 + "\" does not map to a prefix that is in scope");
         } else {
            return new Name(var4, var3, var2);
         }
      }
   }
}
