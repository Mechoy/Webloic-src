package weblogic.wsee.security.policy;

import com.bea.xml.XmlObject;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XBeanUtils {
   public static Element getElement(XmlObject var0) {
      Node var1 = var0.newDomNode().getFirstChild();

      assert var1.getNodeType() == 1;

      return (Element)var1;
   }

   public static DocumentFragment getXMLBeanChildren(XmlObject var0) {
      Node var1 = var0.newDomNode().getFirstChild();
      NodeList var2 = var1.getChildNodes();
      DocumentFragment var3 = var1.getOwnerDocument().createDocumentFragment();

      for(int var4 = 0; var4 < var2.getLength(); ++var4) {
         var3.appendChild(var2.item(var4));
      }

      return var3;
   }
}
