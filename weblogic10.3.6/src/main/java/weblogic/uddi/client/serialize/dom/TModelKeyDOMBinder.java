package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.TModelKey;

public class TModelKeyDOMBinder {
   public static TModelKey fromDOM(Element var0) {
      return new TModelKey(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(TModelKey var0, Document var1) {
      return TextDOMBinder.toDOM("tModelKey", var0.getValue(), var1);
   }
}
