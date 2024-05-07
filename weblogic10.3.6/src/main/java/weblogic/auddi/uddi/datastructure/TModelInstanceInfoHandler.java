package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class TModelInstanceInfoHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      TModelInstanceInfo var2 = new TModelInstanceInfo();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      if (var4.getAttributeNode("tModelKey") != null) {
         Attr var5 = var4.getAttributeNode("tModelKey");
         TModelKeyHandler var6 = (TModelKeyHandler)var3.makeHandler("tModelKey");
         TModelKey var7 = (TModelKey)var6.create(var5);
         if (var7 == null) {
            throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "tModelKey"));
         }

         var2.setTModelKey(var7);
      }

      NodeList var9 = var1.getChildNodes();
      if (var9 != null) {
         for(int var10 = 0; var10 < var9.getLength(); ++var10) {
            if (var9.item(var10).getNodeType() != 8) {
               if (var9.item(var10).getNodeName().equals("instanceDetails")) {
                  InstanceDetailsHandler var11 = (InstanceDetailsHandler)var3.makeHandler("instanceDetails");
                  InstanceDetails var8 = (InstanceDetails)var11.create(var9.item(var10));
                  var2.setInstanceDetails(var8);
               }

               if (var9.item(var10).getNodeName().equals("description")) {
                  DescriptionHandler var12 = (DescriptionHandler)var3.makeHandler("description");
                  Description var13 = (Description)var12.create(var9.item(var10));
                  var2.addDescription(var13);
               }
            }
         }
      }

      return var2;
   }
}
