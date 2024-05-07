package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.response.ServiceList;

public class ServiceListDOMBinder {
   public static ServiceList fromDOM(Element var0) {
      ServiceList var1 = new ServiceList();
      ListResponseDOMBinder.fromDOM(var1, var0);
      NodeList var2 = var0.getElementsByTagName("serviceInfos");
      if (var2.getLength() > 0) {
         var1.setServiceInfos(ServiceInfosDOMBinder.fromDOM((Element)var2.item(0)));
      }

      return var1;
   }
}
