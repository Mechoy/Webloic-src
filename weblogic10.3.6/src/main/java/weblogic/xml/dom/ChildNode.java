package weblogic.xml.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ChildNode extends NodeImpl {
   ChildNode() {
   }

   public final Node removeChild(Node var1) throws DOMException {
      throw new DOMException((short)3, "This node type does not support the requested operation");
   }

   public final Node replaceChild(Node var1, Node var2) throws DOMException {
      throw new DOMException((short)3, "This node type does not support the requested operation");
   }

   public final Node insertBefore(Node var1, Node var2) throws DOMException {
      throw new DOMException((short)3, "This node type does not support the requested operation");
   }

   public final NodeList getChildNodes() {
      return Util.EMPTY_NODELIST;
   }

   public final boolean hasChildNodes() {
      return false;
   }
}
