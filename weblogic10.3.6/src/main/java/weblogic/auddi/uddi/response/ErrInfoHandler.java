package weblogic.auddi.uddi.response;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class ErrInfoHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      ErrInfo var2 = new ErrInfo();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      if (var4.getAttributeNode("errCode") != null) {
         Attr var5 = var4.getAttributeNode("errCode");
         String var6 = var5.getNodeValue();
         if (var6 != null) {
            var2.setErrCode(var6);
         }
      }

      NodeList var7 = var1.getChildNodes();
      if (var7 != null && var7.getLength() > 0) {
         var2.setErrMsg(var7.item(0).getNodeValue());
      }

      return var2;
   }
}
