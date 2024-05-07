package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.response.RegisteredInfo;

public class RegisteredInfoDOMBinder {
   public static RegisteredInfo fromDOM(Element var0) {
      RegisteredInfo var1 = new RegisteredInfo();
      ListResponseDOMBinder.fromDOM(var1, var0);
      NodeList var2 = var0.getElementsByTagName("businessInfos");
      if (var2.getLength() > 0) {
         var1.setBusinessInfos(BusinessInfosDOMBinder.fromDOM((Element)var2.item(0)));
      }

      var2 = var0.getElementsByTagName("tModelInfos");
      if (var2.getLength() > 0) {
         var1.setTModelInfos(TModelInfosDOMBinder.fromDOM((Element)var2.item(0)));
      }

      return var1;
   }
}
