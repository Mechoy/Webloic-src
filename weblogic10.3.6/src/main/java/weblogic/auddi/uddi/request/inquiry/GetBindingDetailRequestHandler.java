package weblogic.auddi.uddi.request.inquiry;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BindingKey;
import weblogic.auddi.uddi.datastructure.BindingKeyHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;

public class GetBindingDetailRequestHandler extends UDDIXMLHandler {
   private UDDIRequest uddirequest;

   public Object create(Node var1) throws UDDIException {
      GetBindingDetailRequest var2 = new GetBindingDetailRequest();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      NodeList var5 = var1.getChildNodes();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            if (var5.item(var6).getNodeName().equals("bindingKey")) {
               BindingKeyHandler var7 = (BindingKeyHandler)var3.makeHandler("bindingKey");
               BindingKey var8 = (BindingKey)var7.create(var5.item(var6));
               var2.addBindingKey(var8);
            }
         }
      }

      return var2;
   }
}
