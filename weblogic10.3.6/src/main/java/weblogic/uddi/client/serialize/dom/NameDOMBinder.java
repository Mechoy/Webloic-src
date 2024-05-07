package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.Name;

public class NameDOMBinder {
   public static Name fromDOM(Element var0) {
      return new Name(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(Name var0, Document var1) {
      return TextDOMBinder.toDOM("name", var0.getValue(), var1);
   }
}
