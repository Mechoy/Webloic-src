package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class InstanceDetailsHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      InstanceDetails var2 = new InstanceDetails();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      NodeList var4 = var1.getChildNodes();
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.getLength(); ++var5) {
            if (var4.item(var5).getNodeType() != 8) {
               if (var4.item(var5).getNodeName().equals("description")) {
                  DescriptionHandler var6 = (DescriptionHandler)var3.makeHandler("description");
                  Description var7 = (Description)var6.create(var4.item(var5));
                  var2.addDescription(var7);
               }

               if (var4.item(var5).getNodeName().equals("overviewDoc")) {
                  OverviewDocHandler var8 = (OverviewDocHandler)var3.makeHandler("overviewDoc");
                  OverviewDoc var10 = (OverviewDoc)var8.create(var4.item(var5));
                  var2.setOverviewDoc(var10);
               }

               if (var4.item(var5).getNodeName().equals("instanceParms")) {
                  InstanceParmsHandler var9 = (InstanceParmsHandler)var3.makeHandler("instanceParms");
                  InstanceParms var11 = (InstanceParms)var9.create(var4.item(var5));
                  var2.setInstanceParms(var11);
               }
            }
         }
      }

      return var2;
   }
}
