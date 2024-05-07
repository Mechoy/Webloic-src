package weblogic.auddi.uddi.request.inquiry;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.datastructure.TModelKeyHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;

public class GetTModelDetailRequestHandler extends UDDIXMLHandler {
   private UDDIRequest uddirequest;

   public Object create(Node var1) throws UDDIException {
      GetTModelDetailRequest var2 = new GetTModelDetailRequest();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      NodeList var5 = var1.getChildNodes();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            if (var5.item(var6).getNodeName().equals("tModelKey")) {
               TModelKeyHandler var7 = (TModelKeyHandler)var3.makeHandler("tModelKey");
               TModelKey var8 = (TModelKey)var7.create(var5.item(var6));
               var2.addTModelKey(var8);
            }
         }
      }

      return var2;
   }
}
