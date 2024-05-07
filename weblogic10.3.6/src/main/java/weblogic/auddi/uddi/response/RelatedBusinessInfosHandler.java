package weblogic.auddi.uddi.response;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class RelatedBusinessInfosHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;
   private RelatedBusinessInfos relatedBusinessInfos = null;

   public Object create(Node var1) throws UDDIException {
      this.relatedBusinessInfos = new RelatedBusinessInfos();
      maker = UDDIXMLHandlerMaker.getInstance();
      NodeList var2 = var1.getChildNodes();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.getLength(); ++var3) {
            if (var2.item(var3).getNodeName().equals("relatedBusinessInfo")) {
               RelatedBusinessInfoHandler var4 = (RelatedBusinessInfoHandler)maker.makeHandler("relatedBusinessInfo");
               RelatedBusinessInfo var5 = (RelatedBusinessInfo)var4.create(var2.item(var3));
               this.relatedBusinessInfos.add(var5);
            }
         }
      }

      return this.relatedBusinessInfos;
   }
}
