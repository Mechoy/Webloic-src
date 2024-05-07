package weblogic.xml.crypto;

import java.util.Iterator;
import java.util.Set;
import weblogic.xml.crypto.api.NodeSetData;

public class NodeSetDataImpl implements NodeSetData {
   private Set nodes;

   public NodeSetDataImpl(Set var1) {
      this.nodes = var1;
   }

   public boolean contains(Object var1) {
      return this.nodes.contains(var1);
   }

   public Iterator iterator() {
      return this.nodes.iterator();
   }
}
