package weblogic.auddi.uddi.response;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class ResultHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker;
   private Result result = null;

   public Object create(Node var1) throws UDDIException {
      this.result = new Result();
      maker = UDDIXMLHandlerMaker.getInstance();
      Element var2 = (Element)var1;
      if (var2.getAttributeNode("keyType") != null) {
      }

      if (var2.getAttributeNode("errno") != null) {
         Attr var3 = var2.getAttributeNode("errno");
         this.result.setErrno(var3.getNodeValue());
      }

      NodeList var7 = var1.getChildNodes();
      if (var7 != null) {
         for(int var4 = 0; var4 < var7.getLength(); ++var4) {
            if (var7.item(var4).getNodeName().equals("errInfo")) {
               ErrInfoHandler var5 = (ErrInfoHandler)maker.makeHandler("errInfo");
               ErrInfo var6 = (ErrInfo)var5.create(var7.item(var4));
               this.result.setErrInfo(var6);
            }
         }
      }

      return this.result;
   }
}
