package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.response.TModelList;

public class TModelListDOMBinder {
   public static TModelList fromDOM(Element var0) {
      TModelList var1 = new TModelList();
      ListResponseDOMBinder.fromDOM(var1, var0);
      NodeList var2 = var0.getElementsByTagName("tModelInfos");
      if (var2.getLength() > 0) {
         var1.setTModelInfos(TModelInfosDOMBinder.fromDOM((Element)var2.item(0)));
      }

      return var1;
   }
}
