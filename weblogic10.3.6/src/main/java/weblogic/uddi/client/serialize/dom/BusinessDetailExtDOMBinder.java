package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.response.BusinessDetailExt;

public class BusinessDetailExtDOMBinder {
   public static BusinessDetailExt fromDOM(Element var0) {
      BusinessDetailExt var1 = new BusinessDetailExt();
      ListResponseDOMBinder.fromDOM(var1, var0);
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("businessEntityExt");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(BusinessEntityExtDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setBusinessEntityExtVector(var2);
      return var1;
   }
}
