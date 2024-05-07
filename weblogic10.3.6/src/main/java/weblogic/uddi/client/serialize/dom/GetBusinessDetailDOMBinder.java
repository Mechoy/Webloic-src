package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.BusinessKey;
import weblogic.uddi.client.structures.request.GetBusinessDetail;

public class GetBusinessDetailDOMBinder {
   public static Element toDOM(GetBusinessDetail var0, Document var1) {
      Element var2 = RequestDOMBinder.toDOM("get_businessDetail", var0, var1);
      Vector var3 = var0.getBusinessKeyVector();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.size(); ++var4) {
            var2.appendChild(BusinessKeyDOMBinder.toDOM((BusinessKey)var3.elementAt(var4), var1));
         }
      }

      return var2;
   }
}
