package weblogic.auddi.uddi.request.publish;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.datastructure.AuthInfo;
import weblogic.auddi.uddi.datastructure.AuthInfoHandler;
import weblogic.auddi.uddi.datastructure.TModel;
import weblogic.auddi.uddi.datastructure.TModelHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;

public class SaveTModelRequestHandler extends UDDIXMLHandler {
   private UDDIRequest uddirequest;

   public Object create(Node var1) throws UDDIException {
      SaveTModelRequest var2 = new SaveTModelRequest();
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
            } else if (var5.item(var6).getNodeName().equals("tModel")) {
               if (var2.getAuthInfo() == null) {
                  throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "authInfo"));
               }

               TModelHandler var9 = (TModelHandler)var3.makeHandler("tModel");
               TModel var10 = (TModel)var9.create(var5.item(var6));
               var2.addTModel(var10);
            }
         }
      }

      return var2;
   }
}
