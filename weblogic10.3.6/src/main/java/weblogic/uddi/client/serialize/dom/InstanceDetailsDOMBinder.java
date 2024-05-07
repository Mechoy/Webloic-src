package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.Description;
import weblogic.uddi.client.structures.datatypes.InstanceDetails;

public class InstanceDetailsDOMBinder {
   public static InstanceDetails fromDOM(Element var0) {
      InstanceDetails var3 = new InstanceDetails();
      Vector var4 = new Vector();
      NodeList var1 = var0.getElementsByTagName("description");
      int var2 = var1.getLength();

      for(int var5 = 0; var5 < var2; ++var5) {
         if (((Element)var1.item(var5)).getParentNode().equals(var0)) {
            var4.add(DescriptionDOMBinder.fromDOM((Element)var1.item(var5)));
         }
      }

      var3.setDescriptionVector(var4);
      var1 = var0.getElementsByTagName("overviewDoc");
      if (var1.getLength() > 0) {
         var3.setOverviewDoc(OverviewDocDOMBinder.fromDOM((Element)var1.item(0)));
      }

      var1 = var0.getElementsByTagName("instanceParms");
      if (var1.getLength() > 0) {
         var3.setInstanceParms(InstanceParmsDOMBinder.fromDOM((Element)var1.item(0)));
      }

      return var3;
   }

   public static Element toDOM(InstanceDetails var0, Document var1) {
      Element var2 = var1.createElement("instanceDetails");
      Vector var3 = var0.getDescriptionVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(DescriptionDOMBinder.toDOM((Description)var3.elementAt(var4), var1));
      }

      if (var0.getOverviewDoc() != null) {
         var2.appendChild(OverviewDocDOMBinder.toDOM(var0.getOverviewDoc(), var1));
      }

      if (var0.getInstanceParms() != null) {
         var2.appendChild(InstanceParmsDOMBinder.toDOM(var0.getInstanceParms(), var1));
      }

      return var2;
   }
}
