package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.request.GetRegisteredInfo;

public class GetRegisteredInfoDOMBinder {
   public static Element toDOM(GetRegisteredInfo var0, Document var1) {
      Element var2 = UpdateRequestDOMBinder.toDOM("get_registeredInfo", var0, var1);
      return var2;
   }
}
