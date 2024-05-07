package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.response.AuthToken;

public class AuthTokenDOMBinder {
   public static AuthToken fromDOM(Element var0) {
      AuthToken var1 = new AuthToken();
      ResponseDOMBinder.fromDOM(var1, var0);
      NodeList var2 = var0.getElementsByTagName("authInfo");
      if (var2.getLength() > 0) {
         var1.setAuthInfo(AuthInfoDOMBinder.fromDOM((Element)var2.item(0)));
      }

      return var1;
   }
}
