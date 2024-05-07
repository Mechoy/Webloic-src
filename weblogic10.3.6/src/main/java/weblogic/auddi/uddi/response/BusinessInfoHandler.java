package weblogic.auddi.uddi.response;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.BusinessKeyHandler;
import weblogic.auddi.uddi.datastructure.Description;
import weblogic.auddi.uddi.datastructure.DescriptionHandler;
import weblogic.auddi.uddi.datastructure.Descriptions;
import weblogic.auddi.uddi.datastructure.DescriptionsHandler;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.NameHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class BusinessInfoHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;
   private BusinessInfo businessInfo = null;

   public Object create(Node var1) throws UDDIException {
      this.businessInfo = new BusinessInfo();
      maker = UDDIXMLHandlerMaker.getInstance();
      Element var2 = (Element)var1;
      if (var2.getAttributeNode("businessKey") != null) {
         Attr var3 = var2.getAttributeNode("businessKey");
         BusinessKeyHandler var4 = (BusinessKeyHandler)maker.makeHandler("businessKey");
         BusinessKey var5 = (BusinessKey)var4.create(var3);
         this.businessInfo.setKey(var5);
      }

      NodeList var7 = var1.getChildNodes();
      if (var7 != null) {
         for(int var8 = 0; var8 < var7.getLength(); ++var8) {
            if (var7.item(var8).getNodeName().equals("name")) {
               NameHandler var9 = (NameHandler)maker.makeHandler("name");
               Name var6 = (Name)var9.create(var7.item(var8));
               this.businessInfo.addName(var6);
            } else if (var7.item(var8).getNodeName().equals("serviceInfos")) {
               ServiceInfosHandler var10 = (ServiceInfosHandler)maker.makeHandler("serviceInfos");
               ServiceInfos var12 = (ServiceInfos)var10.create(var7.item(var8));
               this.businessInfo.setServiceInfos(var12);
            } else if (var7.item(var8).getNodeName().equals("descriptions")) {
               DescriptionsHandler var11 = (DescriptionsHandler)maker.makeHandler("descriptions");
               Descriptions var14 = (Descriptions)var11.create(var7.item(var8));
               this.businessInfo.setDescriptions(var14);
            } else if (var7.item(var8).getNodeName().equals("description")) {
               DescriptionHandler var13 = (DescriptionHandler)maker.makeHandler("description");
               Description var15 = (Description)var13.create(var7.item(var8));
               this.businessInfo.addDescription(var15);
            }
         }
      }

      return this.businessInfo;
   }
}
