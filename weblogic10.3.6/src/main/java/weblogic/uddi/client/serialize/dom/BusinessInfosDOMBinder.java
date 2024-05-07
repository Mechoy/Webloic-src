package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.BusinessInfo;
import weblogic.uddi.client.structures.datatypes.BusinessInfos;

public class BusinessInfosDOMBinder {
   public static BusinessInfos fromDOM(Element var0) {
      BusinessInfos var1 = new BusinessInfos();
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("businessInfo");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(BusinessInfoDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setBusinessInfoVector(var2);
      return var1;
   }

   public static Element toDOM(BusinessInfos var0, Document var1) {
      Element var2 = var1.createElement("businessInfos");
      Vector var3 = var0.getBusinessInfoVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(BusinessInfoDOMBinder.toDOM((BusinessInfo)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
