package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.BusinessKey;

public class BusinessKeyDOMBinder {
   public static BusinessKey fromDOM(Element var0) {
      return new BusinessKey(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(BusinessKey var0, Document var1) {
      return TextDOMBinder.toDOM("businessKey", var0.getValue(), var1);
   }
}
