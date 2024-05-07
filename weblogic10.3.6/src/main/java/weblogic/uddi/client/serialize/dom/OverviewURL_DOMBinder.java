package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.OverviewURL;

public class OverviewURL_DOMBinder {
   public static OverviewURL fromDOM(Element var0) {
      return new OverviewURL(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(OverviewURL var0, Document var1) {
      return TextDOMBinder.toDOM("overviewURL", var0.getValue(), var1);
   }
}
