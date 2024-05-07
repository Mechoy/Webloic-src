package weblogic.xml.security.utils;

import java.util.Map;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.AttributeIterator;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.events.ElementEvent;

public class StartEventDelegate extends ElementEvent implements StartElement {
   private StartElement se = null;
   private Map namespaceMap = null;

   public StartEventDelegate(StartElement var1, Map var2) {
      super(2, var1.getName());
      this.se = var1;
      this.namespaceMap = var2;
   }

   public AttributeIterator getAttributes() {
      return this.se.getAttributes();
   }

   public AttributeIterator getNamespaces() {
      return this.se.getNamespaces();
   }

   public AttributeIterator getAttributesAndNamespaces() {
      return this.se.getAttributesAndNamespaces();
   }

   public Attribute getAttributeByName(XMLName var1) {
      return this.se.getAttributeByName(var1);
   }

   public String getNamespaceUri(String var1) {
      return (String)this.namespaceMap.get(var1);
   }

   public Map getNamespaceMap() {
      return this.namespaceMap;
   }
}
