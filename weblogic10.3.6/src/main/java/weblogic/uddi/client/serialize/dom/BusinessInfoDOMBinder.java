package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.BusinessInfo;
import weblogic.uddi.client.structures.datatypes.Description;

public class BusinessInfoDOMBinder {
   public static BusinessInfo fromDOM(Element var0) {
      BusinessInfo var3 = new BusinessInfo();
      if (var0.hasAttribute("businessKey")) {
         var3.setBusinessKey(var0.getAttribute("businessKey"));
      }

      NodeList var1 = var0.getElementsByTagName("name");
      if (var1.getLength() > 0) {
         var3.setName(NameDOMBinder.fromDOM((Element)var1.item(0)));
      }

      Vector var4 = new Vector();
      var1 = var0.getElementsByTagName("description");
      int var2 = var1.getLength();

      for(int var5 = 0; var5 < var2; ++var5) {
         if (((Element)var1.item(var5)).getParentNode().equals(var0)) {
            var4.add(DescriptionDOMBinder.fromDOM((Element)var1.item(var5)));
         }
      }

      var3.setDescriptionVector(var4);
      var1 = var0.getElementsByTagName("serviceInfos");
      if (var1.getLength() > 0) {
         var3.setServiceInfos(ServiceInfosDOMBinder.fromDOM((Element)var1.item(0)));
      }

      return var3;
   }

   public static Element toDOM(BusinessInfo var0, Document var1) {
      Element var2 = var1.createElement("businessInfo");
      if (var0.getBusinessKey() != null) {
         var2.setAttribute("businessKey", var0.getBusinessKey());
      }

      if (var0.getName() != null) {
         var2.appendChild(NameDOMBinder.toDOM(var0.getName(), var1));
      }

      Vector var3 = var0.getDescriptionVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(DescriptionDOMBinder.toDOM((Description)var3.elementAt(var4), var1));
      }

      if (var0.getServiceInfos() != null) {
         var2.appendChild(ServiceInfosDOMBinder.toDOM(var0.getServiceInfos(), var1));
      }

      return var2;
   }
}
