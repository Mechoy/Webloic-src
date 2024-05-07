package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.request.UpdateRequest;

public class UpdateRequestDOMBinder {
   public static Element toDOM(String var0, UpdateRequest var1, Document var2) {
      Element var3 = RequestDOMBinder.toDOM(var0, var1, var2);
      if (var1.getAuthInfo() != null) {
         var3.appendChild(AuthInfoDOMBinder.toDOM(var1.getAuthInfo(), var2));
      }

      return var3;
   }
}
