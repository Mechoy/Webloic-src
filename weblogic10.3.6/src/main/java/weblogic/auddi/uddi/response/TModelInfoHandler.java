package weblogic.auddi.uddi.response;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.datastructure.TModelKeyHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class TModelInfoHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;
   private TModelInfo tModelInfo = null;

   public Object create(Node var1) throws UDDIException {
      this.tModelInfo = new TModelInfo();
      maker = UDDIXMLHandlerMaker.getInstance();
      Element var2 = (Element)var1;
      if (var2.getAttributeNode("tModelKey") != null) {
         Attr var3 = var2.getAttributeNode("tModelKey");
         TModelKeyHandler var4 = (TModelKeyHandler)maker.makeHandler("tModelKey");
         TModelKey var5 = (TModelKey)var4.create(var3);
         this.tModelInfo.setKey(var5);
      }

      NodeList var6 = var1.getChildNodes();
      if (var6 != null) {
         for(int var7 = 0; var7 < var6.getLength(); ++var7) {
            if (var6.item(var7).getNodeName().equals("name")) {
               this.tModelInfo.setName(var6.item(var7).getChildNodes().item(0).getNodeValue());
            }
         }
      }

      return this.tModelInfo;
   }
}
