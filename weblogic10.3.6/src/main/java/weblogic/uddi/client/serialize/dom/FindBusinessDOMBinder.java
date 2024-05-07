package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.request.FindBusiness;

public class FindBusinessDOMBinder {
   public static Element toDOM(FindBusiness var0, Document var1) {
      Element var2 = FindRequestDOMBinder.toDOM("find_business", var0, var1);
      if (var0.getName() != null) {
         var2.appendChild(NameDOMBinder.toDOM(var0.getName(), var1));
      }

      if (var0.getIdentifierBag() != null) {
         var2.appendChild(IdentifierBagDOMBinder.toDOM(var0.getIdentifierBag(), var1));
      }

      if (var0.getCategoryBag() != null) {
         var2.appendChild(CategoryBagDOMBinder.toDOM(var0.getCategoryBag(), var1));
      }

      if (var0.getTModelBag() != null) {
         var2.appendChild(TModelBagDOMBinder.toDOM(var0.getTModelBag(), var1));
      }

      if (var0.getDiscoveryURLs() != null) {
         var2.appendChild(DiscoveryURLsDOMBinder.toDOM(var0.getDiscoveryURLs(), var1));
      }

      return var2;
   }
}
