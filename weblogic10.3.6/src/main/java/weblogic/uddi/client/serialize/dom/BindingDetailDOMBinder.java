package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.response.BindingDetail;

public class BindingDetailDOMBinder {
   public static BindingDetail fromDOM(Element var0) {
      BindingDetail var1 = new BindingDetail();
      ListResponseDOMBinder.fromDOM(var1, var0);
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("bindingTemplate");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(BindingTemplateDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setBindingTemplateVector(var2);
      return var1;
   }
}
