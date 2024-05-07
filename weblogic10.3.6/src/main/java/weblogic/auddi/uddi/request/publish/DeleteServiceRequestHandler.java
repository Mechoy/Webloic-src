package weblogic.auddi.uddi.request.publish;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.datastructure.AuthInfo;
import weblogic.auddi.uddi.datastructure.AuthInfoHandler;
import weblogic.auddi.uddi.datastructure.ServiceKey;
import weblogic.auddi.uddi.datastructure.ServiceKeyHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;

public class DeleteServiceRequestHandler extends UDDIXMLHandler {
   private UDDIRequest uddirequest;

   public Object create(Node var1) throws UDDIException {
      DeleteServiceRequest var2 = new DeleteServiceRequest();
      var2.setAPI(false);
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      NodeList var5 = var1.getChildNodes();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            if (var5.item(var6).getNodeName().equals("authInfo")) {
               AuthInfoHandler var7 = (AuthInfoHandler)var3.makeHandler("authInfo");
               AuthInfo var8 = (AuthInfo)var7.create(var5.item(var6));
               var2.setAuthInfo(var8);
            }

            if (var5.item(var6).getNodeName().equals("serviceKey")) {
               if (var2.getAuthInfo() == null) {
                  throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "authInfo"));
               }

               ServiceKeyHandler var9 = (ServiceKeyHandler)var3.makeHandler("serviceKey");
               ServiceKey var10 = (ServiceKey)var9.create(var5.item(var6));
               var2.addServiceKey(var10);
            }
         }
      }

      return var2;
   }
}
