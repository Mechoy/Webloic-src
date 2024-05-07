package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.PersonName;

public class PersonNameDOMBinder {
   public static PersonName fromDOM(Element var0) {
      return new PersonName(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(PersonName var0, Document var1) {
      return TextDOMBinder.toDOM("personName", var0.getValue(), var1);
   }
}
