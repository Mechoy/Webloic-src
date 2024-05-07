package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.response.BusinessList;

public class BusinessListDOMBinder {
   public static BusinessList fromDOM(Element var0) {
      BusinessList var1 = new BusinessList();
      ListResponseDOMBinder.fromDOM(var1, var0);
      NodeList var2 = var0.getElementsByTagName("businessInfos");
      if (var2.getLength() > 0) {
         var1.setBusinessInfos(BusinessInfosDOMBinder.fromDOM((Element)var2.item(0)));
      }

      return var1;
   }
}
