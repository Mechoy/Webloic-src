package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.Email;

public class EmailDOMBinder {
   public static Email fromDOM(Element var0) {
      return var0.hasAttribute("useType") ? new Email(var0.getAttribute("useType"), TextDOMBinder.fromDOM(var0)) : new Email(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(Email var0, Document var1) {
      Element var2 = TextDOMBinder.toDOM("email", var0.getValue(), var1);
      if (var0.getUseType() != null) {
         var2.setAttribute("useType", var0.getUseType());
      }

      return var2;
   }
}
