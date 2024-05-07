package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class PhoneHandler extends UDDIXMLHandler {
   private Phone phone;

   public Object create(Node var1) throws UDDIException {
      Element var2 = (Element)var1;
      NodeList var3 = var2.getChildNodes();
      if (var3.getLength() > 0) {
         this.phone = new Phone(var3.item(0).getNodeValue());
      } else {
         this.phone = new Phone("");
      }

      Attr var4 = var2.getAttributeNode("useType");
      if (var4 != null && this.phone != null) {
         String var5 = var4.getValue();
         this.phone.setUseType(var5);
      }

      return this.phone;
   }
}
