package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class AddressHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      Address var2 = null;
      UDDIXMLHandlerMaker var3 = null;
      AddressLineHandler var4 = null;
      AddressLine var5 = null;
      var3 = UDDIXMLHandlerMaker.getInstance();
      var2 = new Address();
      Element var6 = (Element)var1;
      Attr var7 = var6.getAttributeNode("useType");
      if (var7 != null) {
         var2.setUseType(var7.getNodeValue());
      }

      Attr var8 = var6.getAttributeNode("sortCode");
      if (var8 != null) {
         var2.setSortCode(var8.getNodeValue());
      }

      Attr var9 = var6.getAttributeNode("tModelKey");
      if (var9 != null) {
         var2.setTModelKey(new TModelKey(var9.getNodeValue()));
      }

      NodeList var10 = var1.getChildNodes();
      int var11 = var10.getLength();
      if (var11 > 0) {
         var4 = (AddressLineHandler)var3.makeHandler("addressLine");
      }

      for(int var12 = 0; var12 < var11; ++var12) {
         if (var10.item(var12).getNodeType() != 8) {
            var5 = (AddressLine)var4.create(var10.item(var12));
            var2.addAddressLine(var5);
         }
      }

      return var2;
   }
}
