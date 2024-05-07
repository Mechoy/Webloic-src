package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.ServiceKey;

public class ServiceKeyDOMBinder {
   public static ServiceKey fromDOM(Element var0) {
      return new ServiceKey(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(ServiceKey var0, Document var1) {
      return TextDOMBinder.toDOM("serviceKey", var0.getValue(), var1);
   }
}
