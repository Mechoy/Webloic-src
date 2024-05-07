package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.request.FindService;

public class FindServiceDOMBinder {
   public static Element toDOM(FindService var0, Document var1) {
      Element var2 = FindRequestDOMBinder.toDOM("find_service", var0, var1);
      if (var0.getBusinessKey() != null) {
         var2.setAttribute("businessKey", var0.getBusinessKey());
      }

      if (var0.getName() != null) {
         var2.appendChild(NameDOMBinder.toDOM(var0.getName(), var1));
      }

      if (var0.getCategoryBag() != null) {
         var2.appendChild(CategoryBagDOMBinder.toDOM(var0.getCategoryBag(), var1));
      }

      if (var0.getTModelBag() != null) {
         var2.appendChild(TModelBagDOMBinder.toDOM(var0.getTModelBag(), var1));
      }

      return var2;
   }
}
