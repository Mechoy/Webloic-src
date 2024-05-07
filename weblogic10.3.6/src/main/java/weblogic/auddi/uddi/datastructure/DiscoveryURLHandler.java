package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class DiscoveryURLHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      Element var2 = (Element)var1;
      Node var3 = var1.getAttributes().getNamedItem("useType");
      String var4 = null;
      if (var3 == null) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "useType"));
      } else {
         var4 = var3.getNodeValue();
         String var5 = "";
         if (var2.getChildNodes().item(0) != null) {
            var5 = var2.getChildNodes().item(0).getNodeValue().trim();
         }

         DiscoveryURL var6 = new DiscoveryURL(var4, var5);
         return var6;
      }
   }
}
