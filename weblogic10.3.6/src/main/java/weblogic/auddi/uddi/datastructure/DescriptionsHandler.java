package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class DescriptionsHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      UDDIXMLHandlerMaker var2 = UDDIXMLHandlerMaker.getInstance();
      Descriptions var3 = new Descriptions();
      DescriptionHandler var4 = (DescriptionHandler)var2.makeHandler("description");
      NodeList var5 = var1.getChildNodes();
      int var6 = var5.getLength();

      for(int var7 = 0; var7 < var6; ++var7) {
         if (var5.item(var7).getNodeType() != 8) {
            Description var8 = (Description)var4.create(var5.item(var7));
            var3.add(var8);
         }
      }

      return var3;
   }
}
