package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.request.Request;

public class RequestDOMBinder {
   public static Element toDOM(String var0, Request var1, Document var2) {
      Element var3 = var2.createElement(var0);
      if (var1.getGeneric() != null) {
         var3.setAttribute("generic", var1.getGeneric());
      }

      if (var1.getXMLns() != null) {
         var3.setAttribute("xmlns", var1.getXMLns());
      }

      return var3;
   }
}
