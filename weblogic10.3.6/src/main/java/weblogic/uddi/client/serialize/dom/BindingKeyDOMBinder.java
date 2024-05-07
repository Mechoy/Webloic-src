package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.BindingKey;

public class BindingKeyDOMBinder {
   public static BindingKey fromDOM(Element var0) {
      return new BindingKey(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(BindingKey var0, Document var1) {
      return TextDOMBinder.toDOM("bindingKey", var0.getValue(), var1);
   }
}
