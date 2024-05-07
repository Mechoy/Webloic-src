package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.BusinessService;
import weblogic.uddi.client.structures.datatypes.BusinessServices;

public class BusinessServicesDOMBinder {
   public static BusinessServices fromDOM(Element var0) {
      BusinessServices var1 = new BusinessServices();
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("businessService");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(BusinessServiceDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setBusinessServiceVector(var2);
      return var1;
   }

   public static Element toDOM(BusinessServices var0, Document var1) {
      Element var2 = var1.createElement("businessServices");
      Vector var3 = var0.getBusinessServiceVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(BusinessServiceDOMBinder.toDOM((BusinessService)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
