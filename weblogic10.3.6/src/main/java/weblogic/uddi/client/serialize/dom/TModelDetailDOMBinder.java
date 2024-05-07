package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.response.TModelDetail;

public class TModelDetailDOMBinder {
   public static TModelDetail fromDOM(Element var0) {
      TModelDetail var1 = new TModelDetail();
      ListResponseDOMBinder.fromDOM(var1, var0);
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("tModel");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(TModelDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setTModelVector(var2);
      return var1;
   }
}
