package weblogic.xml.crypto.dom;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.URIDereferenceUtils;
import weblogic.xml.crypto.api.dom.DOMIdMap;

public class DOMIdMapImpl implements DOMIdMap {
   private Node node;
   private Set idAttributeQNames;
   private Map namespaces;
   private Map idElements = new HashMap();

   public DOMIdMapImpl(Node var1, Set var2, Map var3) {
      this.node = var1;
      this.idAttributeQNames = var2;
      this.namespaces = var3;
   }

   public Element getElementById(String var1) {
      Element var2 = (Element)this.idElements.get(var1);
      if (var2 == null) {
         var2 = (Element)URIDereferenceUtils.findNodeById(var1, this.idAttributeQNames, this.node);
      }

      return var2;
   }

   public void setIdAttributeNS(Element var1, String var2, String var3) {
      this.idElements.put(var1.getAttributeNS(var2, var3), var1);
   }

   private String getPrefix(String var1) {
      return var1 != null && var1.length() > 0 ? var1 + ":" : "";
   }
}
