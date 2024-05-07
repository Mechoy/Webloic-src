package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.AccessPoint;

public class AccessPointDOMBinder {
   public static AccessPoint fromDOM(Element var0) {
      return var0.hasAttribute("URLType") ? new AccessPoint(TextDOMBinder.fromDOM(var0), var0.getAttribute("URLType")) : new AccessPoint("", TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(AccessPoint var0, Document var1) {
      Element var2 = TextDOMBinder.toDOM("accessPoint", var0.getValue(), var1);
      if (var0.getURLType() != null) {
         var2.setAttribute("URLType", var0.getURLType());
      }

      return var2;
   }
}
