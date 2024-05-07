package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class DiscoveryURLsHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      UDDIXMLHandlerMaker var2 = UDDIXMLHandlerMaker.getInstance();
      DiscoveryURLs var3 = new DiscoveryURLs();
      DiscoveryURLHandler var4 = (DiscoveryURLHandler)var2.makeHandler("discoveryURL");
      NodeList var5 = var1.getChildNodes();
      int var6 = var5.getLength();

      for(int var7 = 0; var7 < var6; ++var7) {
         if (var5.item(var7).getNodeType() != 8) {
            DiscoveryURL var8 = (DiscoveryURL)var4.create(var5.item(var7));
            var3.add(var8);
         }
      }

      return var3;
   }
}
