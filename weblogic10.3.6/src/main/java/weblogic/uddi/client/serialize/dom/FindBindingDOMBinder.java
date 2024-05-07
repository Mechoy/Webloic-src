package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.request.FindBinding;

public class FindBindingDOMBinder {
   public static Element toDOM(FindBinding var0, Document var1) {
      Element var2 = FindRequestDOMBinder.toDOM("find_binding", var0, var1);
      if (var0.getServiceKey() != null) {
         var2.setAttribute("serviceKey", var0.getServiceKey());
      }

      if (var0.getTModelBag() != null) {
         var2.appendChild(TModelBagDOMBinder.toDOM(var0.getTModelBag(), var1));
      }

      return var2;
   }
}
