package weblogic.xml.crypto.api.dom;

import org.w3c.dom.Node;
import weblogic.xml.crypto.api.XMLStructure;

public class DOMStructure implements XMLStructure {
   private Node node;

   public DOMStructure(Node var1) {
      this.node = var1;
   }

   public Node getNode() {
      return this.node;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }
}
