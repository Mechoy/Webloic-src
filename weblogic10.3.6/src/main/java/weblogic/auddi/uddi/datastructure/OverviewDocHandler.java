package weblogic.auddi.uddi.datastructure;

import java.net.URL;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class OverviewDocHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      OverviewDoc var2 = new OverviewDoc();
      Element var3 = (Element)var1;
      NodeList var4 = var1.getChildNodes();
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.getLength(); ++var5) {
            if (var4.item(var5).getNodeType() != 8) {
               if (var4.item(var5).getNodeName().equals("description")) {
                  DescriptionHandler var6 = new DescriptionHandler();
                  Description var7 = (Description)var6.create(var4.item(var5));
                  var2.addDescription(var7);
               }

               if (var4.item(var5).getNodeName().equals("overviewURL") && var4.item(var5).getChildNodes().item(0) != null && !var4.item(var5).getChildNodes().item(0).getNodeValue().trim().equals("")) {
                  OverviewURLHandler var8 = new OverviewURLHandler();
                  URL var9 = (URL)var8.create(var4.item(var5));
                  var2.setOverviewURL(var9);
               }
            }
         }
      }

      return var2;
   }
}
