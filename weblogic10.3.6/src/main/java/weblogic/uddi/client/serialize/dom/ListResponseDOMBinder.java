package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Element;
import weblogic.uddi.client.structures.response.ListResponse;

public class ListResponseDOMBinder {
   public static void fromDOM(ListResponse var0, Element var1) {
      ResponseDOMBinder.fromDOM(var0, var1);
      String var2 = "false";
      if (var1.hasAttribute("truncated")) {
         var2 = var1.getAttribute("truncated");
      }

      var0.setTruncated(var2.equals("true"));
   }
}
