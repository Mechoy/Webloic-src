package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.request.DiscardAuthToken;

public class DiscardAuthTokenDOMBinder {
   public static Element toDOM(DiscardAuthToken var0, Document var1) {
      Element var2 = UpdateRequestDOMBinder.toDOM("discard_authToken", var0, var1);
      return var2;
   }
}
