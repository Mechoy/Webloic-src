package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.AddressLine;

public class AddressLineDOMBinder {
   public static AddressLine fromDOM(Element var0) {
      return new AddressLine(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(AddressLine var0, Document var1) {
      return TextDOMBinder.toDOM("addressLine", var0.getValue(), var1);
   }
}
