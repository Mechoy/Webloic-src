package weblogic.auddi.uddi.request.publish;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;

public class GetAuthTokenRequestHandler extends UDDIXMLHandler {
   private UDDIRequest uddirequest;

   public Object create(Node var1) throws UDDIException {
      GetAuthTokenRequest var2 = new GetAuthTokenRequest();
      var2.setAPI(false);
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      Attr var5;
      String var6;
      if (var4.getAttributeNode("userID") != null) {
         var5 = var4.getAttributeNode("userID");
         var6 = var5.getNodeValue();
         var2.setUserId(var6);
      }

      if (var4.getAttributeNode("cred") != null) {
         var5 = var4.getAttributeNode("cred");
         var6 = var5.getNodeValue();
         var2.setCred(var6);
      }

      return var2;
   }
}
