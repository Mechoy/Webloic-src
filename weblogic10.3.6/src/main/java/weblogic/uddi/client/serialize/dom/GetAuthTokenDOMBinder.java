package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.request.GetAuthToken;

public class GetAuthTokenDOMBinder {
   public static Element toDOM(GetAuthToken var0, Document var1) {
      Element var2 = RequestDOMBinder.toDOM("get_authToken", var0, var1);
      if (var0.getUserID() != null) {
         var2.setAttribute("userID", var0.getUserID());
      }

      if (var0.getCred() != null) {
         var2.setAttribute("cred", var0.getCred());
      }

      return var2;
   }
}
