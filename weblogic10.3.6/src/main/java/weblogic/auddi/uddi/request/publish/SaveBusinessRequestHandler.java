package weblogic.auddi.uddi.request.publish;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.datastructure.AuthInfo;
import weblogic.auddi.uddi.datastructure.AuthInfoHandler;
import weblogic.auddi.uddi.datastructure.BusinessEntity;
import weblogic.auddi.uddi.datastructure.BusinessEntityHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;
import weblogic.auddi.util.Logger;

public class SaveBusinessRequestHandler extends UDDIXMLHandler {
   private UDDIRequest uddirequest;

   public Object create(Node var1) throws UDDIException {
      Logger.trace("+SaveBusinessRequestHandler.create()");
      SaveBusinessRequest var2 = new SaveBusinessRequest();
      var2.setAPI(false);
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      NodeList var5 = var1.getChildNodes();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            String var7 = var5.item(var6).getNodeName();
            Logger.trace("name: " + var7);
            if (var7.equals("authInfo")) {
               AuthInfoHandler var8 = (AuthInfoHandler)var3.makeHandler("authInfo");
               AuthInfo var9 = (AuthInfo)var8.create(var5.item(var6));
               var2.setAuthInfo(var9);
            } else if (var7.equals("businessEntity")) {
               if (var2.getAuthInfo() == null) {
                  throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "authInfo"));
               }

               BusinessEntityHandler var10 = (BusinessEntityHandler)var3.makeHandler("businessEntity");
               BusinessEntity var11 = (BusinessEntity)var10.create(var5.item(var6));
               var2.addBusinessEntity(var11);
            }
         }
      }

      Logger.trace("-SaveBusinessRequestHandler.create()");
      return var2;
   }
}
