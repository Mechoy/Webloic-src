package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class EmailHandler extends UDDIXMLHandler {
   private Email email;

   public Object create(Node var1) throws UDDIException {
      Element var2 = (Element)var1;
      Object var3 = null;
      NodeList var4 = var2.getChildNodes();
      if (var4.getLength() > 0) {
         this.email = new Email(var4.item(0).getNodeValue());
      } else {
         this.email = new Email("");
      }

      Attr var5 = var2.getAttributeNode("useType");
      if (var5 != null && this.email != null) {
         String var6 = var5.getValue();
         this.email.setUseType(var6);
      }

      return this.email;
   }
}
