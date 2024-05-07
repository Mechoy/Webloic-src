package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.HostingRedirector;

public class HostingRedirectorDOMBinder {
   public static HostingRedirector fromDOM(Element var0) {
      HostingRedirector var1 = new HostingRedirector();
      if (var0.hasAttribute("bindingKey")) {
         var1.setBindingKey(var0.getAttribute("bindingKey"));
      }

      return var1;
   }

   public static Element toDOM(HostingRedirector var0, Document var1) {
      Element var2 = var1.createElement("hostingRedirector");
      if (var0.getBindingKey() != null) {
         var2.setAttribute("bindingKey", var0.getBindingKey());
      }

      return var2;
   }
}
