package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.Description;

public class DescriptionDOMBinder {
   public static Description fromDOM(Element var0) {
      return new Description(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(Description var0, Document var1) {
      return TextDOMBinder.toDOM("description", var0.getValue(), var1);
   }
}
