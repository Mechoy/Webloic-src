package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Element;
import weblogic.uddi.client.structures.response.Response;

public class ResponseDOMBinder {
   public static void fromDOM(Response var0, Element var1) {
      if (var1.hasAttribute("generic")) {
         var0.setGeneric(var1.getAttribute("generic"));
      }

      if (var1.hasAttribute("xmlns")) {
         var0.setOperator(var1.getAttribute("xmlns"));
      }

   }
}
