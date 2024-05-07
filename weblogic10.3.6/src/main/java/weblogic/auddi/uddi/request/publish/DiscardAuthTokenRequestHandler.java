package weblogic.auddi.uddi.request.publish;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.datastructure.AuthInfo;
import weblogic.auddi.uddi.datastructure.AuthInfoHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;
import weblogic.auddi.util.Logger;

public class DiscardAuthTokenRequestHandler extends UDDIXMLHandler {
   private UDDIRequest uddirequest;

   public Object create(Node var1) throws UDDIException {
      DiscardAuthTokenRequest var2 = new DiscardAuthTokenRequest();
      var2.setAPI(false);
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      NodeList var5 = var1.getChildNodes();
      Logger.debug("children: " + var5);
      if (var5 != null) {
         Logger.debug("children.getLength(): " + var5.getLength());

         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            if (var5.item(var6).getNodeName().equals("authInfo")) {
               AuthInfoHandler var7 = (AuthInfoHandler)var3.makeHandler("authInfo");
               AuthInfo var8 = (AuthInfo)var7.create(var5.item(var6));
               var2.setAuthInfo(var8);
            }
         }

         if (var2.getAuthInfo() == null) {
            throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "authInfo"));
         }
      }

      return var2;
   }
}
