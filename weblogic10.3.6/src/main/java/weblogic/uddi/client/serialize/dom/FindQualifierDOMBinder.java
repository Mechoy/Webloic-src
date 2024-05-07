package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.FindQualifier;

public class FindQualifierDOMBinder {
   public static FindQualifier fromDOM(Element var0) {
      return new FindQualifier(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(FindQualifier var0, Document var1) {
      return TextDOMBinder.toDOM("findQualifier", var0.getValue(), var1);
   }
}
