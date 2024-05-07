package weblogic.auddi.uddi.response;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.BusinessKeyHandler;
import weblogic.auddi.uddi.datastructure.Description;
import weblogic.auddi.uddi.datastructure.DescriptionHandler;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.NameHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class RelatedBusinessInfoHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;
   private RelatedBusinessInfo relatedBusinessInfo = null;

   public Object create(Node var1) throws UDDIException {
      this.relatedBusinessInfo = new RelatedBusinessInfo();
      maker = UDDIXMLHandlerMaker.getInstance();
      Element var2 = (Element)var1;
      NodeList var3 = var1.getChildNodes();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.getLength(); ++var4) {
            if (var3.item(var4).getNodeName().equals("businessKey")) {
               BusinessKeyHandler var5 = (BusinessKeyHandler)maker.makeHandler("businessKey");
               BusinessKey var6 = (BusinessKey)var5.create(var3.item(var4));
               this.relatedBusinessInfo.setBusinessKey(var6);
            }

            if (var3.item(var4).getNodeName().equals("name")) {
               NameHandler var7 = (NameHandler)maker.makeHandler("name");
               Name var10 = (Name)var7.create(var3.item(var4));
               this.relatedBusinessInfo.addName(var10);
            }

            if (var3.item(var4).getNodeName().equals("description")) {
               DescriptionHandler var8 = (DescriptionHandler)maker.makeHandler("description");
               Description var11 = (Description)var8.create(var3.item(var4));
               this.relatedBusinessInfo.addDescription(var11);
            }

            if (var3.item(var4).getNodeName().equals("sharedRelationships")) {
               SharedRelationshipsHandler var9 = (SharedRelationshipsHandler)maker.makeHandler("sharedRelationships");
               SharedRelationships var12 = (SharedRelationships)var9.create(var3.item(var4));
               this.relatedBusinessInfo.addSharedRelationships(var12);
            }
         }
      }

      return this.relatedBusinessInfo;
   }
}
