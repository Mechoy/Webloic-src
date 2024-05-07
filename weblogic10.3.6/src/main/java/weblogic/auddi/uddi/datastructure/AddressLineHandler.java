package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class AddressLineHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      AddressLine var2 = null;
      Element var3 = (Element)var1;
      String var4 = null;
      NodeList var5 = var3.getChildNodes();
      if (var5.getLength() > 0) {
         var4 = var5.item(0).getNodeValue();
      }

      if (var4 == null) {
         var4 = "";
      }

      var2 = new AddressLine(var4);
      if (var2 != null) {
         Attr var6 = var3.getAttributeNode("keyName");
         if (var6 != null) {
            var2.setKeyName(var3.getAttribute("keyName"));
         }

         Attr var7 = var3.getAttributeNode("keyValue");
         if (var7 != null) {
            var2.setKeyValue(var3.getAttribute("keyValue"));
         }
      }

      return var2;
   }
}
