package weblogic.auddi.uddi.request.inquiry;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.ServiceKey;
import weblogic.auddi.uddi.datastructure.ServiceKeyHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;

public class GetServiceDetailRequestHandler extends UDDIXMLHandler {
   private UDDIRequest uddirequest;

   public Object create(Node var1) throws UDDIException {
      GetServiceDetailRequest var2 = new GetServiceDetailRequest();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      NodeList var5 = var1.getChildNodes();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            if (var5.item(var6).getNodeName().equals("serviceKey")) {
               ServiceKeyHandler var7 = (ServiceKeyHandler)var3.makeHandler("serviceKey");
               ServiceKey var8 = (ServiceKey)var7.create(var5.item(var6));
               var2.addServiceKey(var8);
            }
         }
      }

      return var2;
   }
}
