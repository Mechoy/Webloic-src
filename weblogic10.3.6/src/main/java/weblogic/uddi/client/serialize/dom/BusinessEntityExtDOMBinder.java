package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.BusinessEntityExt;

public class BusinessEntityExtDOMBinder {
   public static BusinessEntityExt fromDOM(Element var0) {
      BusinessEntityExt var1 = new BusinessEntityExt();
      NodeList var2 = var0.getElementsByTagName("businessEntity");
      if (var2.getLength() > 0) {
         var1.setBusinessEntity(BusinessEntityDOMBinder.fromDOM((Element)var2.item(0)));
      }

      return var1;
   }

   public static Element toDOM(BusinessEntityExt var0, Document var1) {
      Element var2 = var1.createElement("businessEntityExt");
      if (var0.getBusinessEntity() != null) {
         var2.appendChild(BusinessEntityDOMBinder.toDOM(var0.getBusinessEntity(), var1));
      }

      return var2;
   }
}
