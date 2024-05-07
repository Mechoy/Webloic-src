package weblogic.management.security.internal.compatibility;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XMLAttributeList {
   NamedNodeMap map;

   XMLAttributeList(Node var1) {
      this.map = var1.getAttributes();
   }

   public int getLength() {
      return this.map.getLength();
   }

   public String getName(int var1) {
      return this.map.item(var1).getNodeName();
   }

   public String getType(int var1) {
      return "CDATA";
   }

   public String getType(String var1) {
      return "CDATA";
   }

   public String getValue(int var1) {
      return this.map.item(var1).getNodeValue();
   }

   public String getValue(String var1) {
      Node var2 = this.map.getNamedItem(var1);
      return var2 != null ? var2.getNodeValue() : null;
   }
}
