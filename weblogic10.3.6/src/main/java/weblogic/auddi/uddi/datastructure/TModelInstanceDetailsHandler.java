package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class TModelInstanceDetailsHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;
   private TModelInstanceDetails tModelInstanceDetails = null;

   public Object create(Node var1) throws UDDIException {
      this.tModelInstanceDetails = new TModelInstanceDetails();
      maker = UDDIXMLHandlerMaker.getInstance();
      NodeList var2 = var1.getChildNodes();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.getLength(); ++var3) {
            if (var2.item(var3).getNodeType() != 8 && var2.item(var3).getNodeName().equals("tModelInstanceInfo")) {
               TModelInstanceInfoHandler var4 = (TModelInstanceInfoHandler)maker.makeHandler("tModelInstanceInfo");
               TModelInstanceInfo var5 = (TModelInstanceInfo)var4.create(var2.item(var3));
               this.tModelInstanceDetails.add(var5);
            }
         }
      }

      return this.tModelInstanceDetails;
   }
}
