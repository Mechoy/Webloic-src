package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.Description;
import weblogic.uddi.client.structures.datatypes.OverviewDoc;

public class OverviewDocDOMBinder {
   public static OverviewDoc fromDOM(Element var0) {
      OverviewDoc var1 = new OverviewDoc();
      Vector var4 = new Vector();
      NodeList var2 = var0.getElementsByTagName("description");
      int var3 = var2.getLength();

      for(int var5 = 0; var5 < var3; ++var5) {
         if (((Element)var2.item(var5)).getParentNode().equals(var0)) {
            var4.add(DescriptionDOMBinder.fromDOM((Element)var2.item(var5)));
         }
      }

      var1.setDescriptionVector(var4);
      var2 = var0.getElementsByTagName("overviewURL");
      if (var2.getLength() > 0) {
         var1.setOverviewURL(OverviewURL_DOMBinder.fromDOM((Element)var2.item(0)));
      }

      return var1;
   }

   public static Element toDOM(OverviewDoc var0, Document var1) {
      Element var2 = var1.createElement("overviewDoc");
      Vector var3 = var0.getDescriptionVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(DescriptionDOMBinder.toDOM((Description)var3.elementAt(var4), var1));
      }

      if (var0.getOverviewURL() != null) {
         var2.appendChild(OverviewURL_DOMBinder.toDOM(var0.getOverviewURL(), var1));
      }

      return var2;
   }
}
