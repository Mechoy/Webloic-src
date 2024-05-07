package weblogic.auddi.uddi.response;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.AuthInfo;
import weblogic.auddi.uddi.datastructure.AuthInfoHandler;
import weblogic.auddi.uddi.datastructure.Operator;
import weblogic.auddi.uddi.datastructure.OperatorHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class AuthTokenResponseHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;

   public Object create(Node var1) throws UDDIException {
      AuthTokenResponse var2 = new AuthTokenResponse();
      maker = UDDIXMLHandlerMaker.getInstance();
      Element var3 = (Element)var1;
      if (var3.getAttributeNode("operator") != null) {
         Attr var4 = var3.getAttributeNode("operator");
         maker = UDDIXMLHandlerMaker.getInstance();
         OperatorHandler var5 = (OperatorHandler)maker.makeHandler("operator");
         Operator var6 = (Operator)var5.create(var4);
         var2.setOperator(var6);
      }

      NodeList var8 = var1.getChildNodes();
      if (var8 != null) {
         for(int var9 = 0; var9 < var8.getLength(); ++var9) {
            if (var8.item(var9).getNodeName().equals("authInfo")) {
               AuthInfoHandler var10 = (AuthInfoHandler)maker.makeHandler("authInfo");
               AuthInfo var7 = (AuthInfo)var10.create(var8.item(var9));
               var2.setAuthInfo(var7);
            }
         }
      }

      return var2;
   }
}
