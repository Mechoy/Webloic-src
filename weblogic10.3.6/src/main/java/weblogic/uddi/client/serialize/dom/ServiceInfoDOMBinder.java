package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.ServiceInfo;

public class ServiceInfoDOMBinder {
   public static ServiceInfo fromDOM(Element var0) {
      ServiceInfo var3 = new ServiceInfo();
      if (var0.hasAttribute("businessKey")) {
         var3.setBusinessKey(var0.getAttribute("businessKey"));
      }

      if (var0.hasAttribute("serviceKey")) {
         var3.setServiceKey(var0.getAttribute("serviceKey"));
      }

      NodeList var1 = var0.getElementsByTagName("name");
      if (var1.getLength() > 0) {
         var3.setName(NameDOMBinder.fromDOM((Element)var1.item(0)));
      }

      return var3;
   }

   public static Element toDOM(ServiceInfo var0, Document var1) {
      Element var2 = var1.createElement("serviceInfo");
      if (var0.getBusinessKey() != null) {
         var2.setAttribute("businessKey", var0.getBusinessKey());
      }

      if (var0.getServiceKey() != null) {
         var2.setAttribute("serviceKey", var0.getServiceKey());
      }

      if (var0.getName() != null) {
         var2.appendChild(NameDOMBinder.toDOM(var0.getName(), var1));
      }

      return var2;
   }
}
