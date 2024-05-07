package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.request.ValidateCategorization;

public class ValidateCategorizationDOMBinder {
   public static Element toDOM(ValidateCategorization var0, Document var1) {
      Element var2 = RequestDOMBinder.toDOM("validate_categorization", var0, var1);
      if (var0.getTModelKey() != null) {
         var2.appendChild(TModelKeyDOMBinder.toDOM(var0.getTModelKey(), var1));
      }

      if (var0.getKeyValue() != null) {
         var2.appendChild(KeyValueDOMBinder.toDOM(var0.getKeyValue(), var1));
      }

      if (var0.getBusinessEntity() != null) {
         var2.appendChild(BusinessEntityDOMBinder.toDOM(var0.getBusinessEntity(), var1));
      }

      if (var0.getBusinessService() != null) {
         var2.appendChild(BusinessServiceDOMBinder.toDOM(var0.getBusinessService(), var1));
      }

      if (var0.getTModel() != null) {
         var2.appendChild(TModelDOMBinder.toDOM(var0.getTModel(), var1));
      }

      return var2;
   }
}
