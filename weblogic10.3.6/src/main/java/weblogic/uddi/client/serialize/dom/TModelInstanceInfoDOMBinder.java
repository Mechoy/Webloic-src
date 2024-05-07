package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.Description;
import weblogic.uddi.client.structures.datatypes.TModelInstanceInfo;

public class TModelInstanceInfoDOMBinder {
   public static TModelInstanceInfo fromDOM(Element var0) {
      TModelInstanceInfo var3 = new TModelInstanceInfo();
      if (var0.hasAttribute("tModelKey")) {
         var3.setTModelKey(var0.getAttribute("tModelKey"));
      }

      Vector var4 = new Vector();
      NodeList var1 = var0.getElementsByTagName("description");
      int var2 = var1.getLength();

      for(int var5 = 0; var5 < var2; ++var5) {
         if (((Element)var1.item(var5)).getParentNode().equals(var0)) {
            var4.add(DescriptionDOMBinder.fromDOM((Element)var1.item(var5)));
         }
      }

      var3.setDescriptionVector(var4);
      var1 = var0.getElementsByTagName("instanceDetails");
      if (var1.getLength() > 0) {
         var3.setInstanceDetails(InstanceDetailsDOMBinder.fromDOM((Element)var1.item(0)));
      }

      return var3;
   }

   public static Element toDOM(TModelInstanceInfo var0, Document var1) {
      Element var2 = var1.createElement("tModelInstanceInfo");
      if (var0.getTModelKey() != null) {
         var2.setAttribute("tModelKey", var0.getTModelKey());
      }

      Vector var3 = var0.getDescriptionVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(DescriptionDOMBinder.toDOM((Description)var3.elementAt(var4), var1));
      }

      if (var0.getInstanceDetails() != null) {
         var2.appendChild(InstanceDetailsDOMBinder.toDOM(var0.getInstanceDetails(), var1));
      }

      return var2;
   }
}
