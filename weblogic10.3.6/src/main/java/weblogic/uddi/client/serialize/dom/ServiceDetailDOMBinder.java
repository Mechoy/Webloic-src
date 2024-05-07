package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.response.ServiceDetail;

public class ServiceDetailDOMBinder {
   public static ServiceDetail fromDOM(Element var0) {
      ServiceDetail var1 = new ServiceDetail();
      ListResponseDOMBinder.fromDOM(var1, var0);
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("businessService");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(BusinessServiceDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setBusinessServiceVector(var2);
      return var1;
   }
}
