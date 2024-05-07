package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.KeyValue;

public class KeyValueDOMBinder {
   public static KeyValue fromDOM(Element var0) {
      return new KeyValue(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(KeyValue var0, Document var1) {
      return TextDOMBinder.toDOM("keyValue", var0.getValue(), var1);
   }
}
