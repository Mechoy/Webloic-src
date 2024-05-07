package weblogic.auddi.uddi.response;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.Operator;
import weblogic.auddi.uddi.datastructure.OperatorHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class ServiceListResponseHandler extends UDDIXMLHandler {
   private ServiceListResponse serviceListResponse = null;
   private static UDDIXMLHandlerMaker maker = null;

   public Object create(Node var1) throws UDDIException {
      this.serviceListResponse = new ServiceListResponse();
      maker = UDDIXMLHandlerMaker.getInstance();
      Element var2 = (Element)var1;
      Attr var3;
      if (var2.getAttributeNode("truncated") != null) {
         var3 = var2.getAttributeNode("truncated");
         String var4 = var3.getNodeValue();
         this.serviceListResponse.setTruncated(Boolean.valueOf(var4));
      }

      if (var2.getAttributeNode("operator") != null) {
         var3 = var2.getAttributeNode("operator");
         maker = UDDIXMLHandlerMaker.getInstance();
         OperatorHandler var8 = (OperatorHandler)maker.makeHandler("operator");
         Operator var5 = (Operator)var8.create(var3);
         this.serviceListResponse.setOperator(var5);
      }

      NodeList var7 = var1.getChildNodes();
      if (var7 != null) {
         for(int var9 = 0; var9 < var7.getLength(); ++var9) {
            if (var7.item(var9).getNodeName().equals("serviceInfos")) {
               ServiceInfosHandler var10 = (ServiceInfosHandler)maker.makeHandler("serviceInfos");
               ServiceInfos var6 = (ServiceInfos)var10.create(var7.item(var9));
               this.serviceListResponse.setServiceInfos(var6);
            }
         }
      }

      return this.serviceListResponse;
   }
}
