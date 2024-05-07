package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class BusinessServicesHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      UDDIXMLHandlerMaker var4 = UDDIXMLHandlerMaker.getInstance();
      BusinessServices var2 = new BusinessServices();
      BusinessServiceHandler var3 = (BusinessServiceHandler)var4.makeHandler("businessService");
      NodeList var5 = var1.getChildNodes();
      int var6 = var5.getLength();

      for(int var7 = 0; var7 < var6; ++var7) {
         if (var5.item(var7).getNodeType() != 8) {
            var2.add((BusinessService)var3.create(var5.item(var7)));
         }
      }

      return var2;
   }
}
