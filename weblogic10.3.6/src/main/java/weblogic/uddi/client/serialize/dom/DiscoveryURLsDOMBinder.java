package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.DiscoveryURL;
import weblogic.uddi.client.structures.datatypes.DiscoveryURLs;

public class DiscoveryURLsDOMBinder {
   public static DiscoveryURLs fromDOM(Element var0) {
      DiscoveryURLs var1 = new DiscoveryURLs();
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("discoveryURL");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(DiscoveryURL_DOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setDiscoveryURLVector(var2);
      return var1;
   }

   public static Element toDOM(DiscoveryURLs var0, Document var1) {
      Element var2 = var1.createElement("discoveryURLs");
      Vector var3 = var0.getDiscoveryURLVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(DiscoveryURL_DOMBinder.toDOM((DiscoveryURL)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
