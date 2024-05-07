package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.AuthInfo;

public class AuthInfoDOMBinder {
   public static AuthInfo fromDOM(Element var0) {
      return new AuthInfo(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(AuthInfo var0, Document var1) {
      return TextDOMBinder.toDOM("authInfo", var0.getValue(), var1);
   }
}
