package weblogic.auddi.uddi.response;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class TModelInfosHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;
   private TModelInfos tModelInfos = null;

   public Object create(Node var1) throws UDDIException {
      this.tModelInfos = new TModelInfos();
      maker = UDDIXMLHandlerMaker.getInstance();
      NodeList var2 = var1.getChildNodes();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.getLength(); ++var3) {
            if (var2.item(var3).getNodeName().equals("tModelInfo")) {
               TModelInfoHandler var4 = (TModelInfoHandler)maker.makeHandler("tModelInfo");
               TModelInfo var5 = (TModelInfo)var4.create(var2.item(var3));
               this.tModelInfos.add(var5);
            }
         }
      }

      return this.tModelInfos;
   }
}
