package weblogic.auddi.uddi.request.inquiry;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.BusinessKeyHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;

public class GetBusinessDetailRequestHandler extends UDDIXMLHandler {
   private UDDIRequest uddirequest;

   public Object create(Node var1) throws UDDIException {
      GetBusinessDetailRequest var2 = new GetBusinessDetailRequest();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      NodeList var5 = var1.getChildNodes();
      boolean var6 = false;
      if (var5 != null) {
         for(int var7 = 0; var7 < var5.getLength(); ++var7) {
            if (var5.item(var7).getNodeName().equals("businessKey")) {
               var6 = true;
               BusinessKeyHandler var8 = (BusinessKeyHandler)var3.makeHandler("businessKey");
               BusinessKey var9 = (BusinessKey)var8.create(var5.item(var7));
               var2.addBusinessKey(var9);
            }
         }
      }

      if (!var6) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "businessKey"));
      } else {
         return var2;
      }
   }
}
