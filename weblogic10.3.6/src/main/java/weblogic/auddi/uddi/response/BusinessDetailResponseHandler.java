package weblogic.auddi.uddi.response;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessEntity;
import weblogic.auddi.uddi.datastructure.BusinessEntityHandler;
import weblogic.auddi.uddi.datastructure.Operator;
import weblogic.auddi.uddi.datastructure.OperatorHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class BusinessDetailResponseHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;

   public Object create(Node var1) throws UDDIException {
      BusinessDetailResponse var2 = new BusinessDetailResponse();
      maker = UDDIXMLHandlerMaker.getInstance();
      Element var3 = (Element)var1;
      Attr var4;
      if (var3.getAttributeNode("truncated") != null) {
         var4 = var3.getAttributeNode("truncated");
         String var5 = var4.getNodeValue();
         var2.setTruncated(Boolean.valueOf(var5));
      }

      if (var3.getAttributeNode("operator") != null) {
         var4 = var3.getAttributeNode("operator");
         maker = UDDIXMLHandlerMaker.getInstance();
         OperatorHandler var10 = (OperatorHandler)maker.makeHandler("operator");
         Operator var6 = (Operator)var10.create(var4);
         var2.setOperator(var6);
      }

      NodeList var9 = var1.getChildNodes();
      if (var9 != null) {
         for(int var11 = 0; var11 < var9.getLength(); ++var11) {
            if (var9.item(var11).getNodeName().equals("businessEntity")) {
               BusinessEntityHandler var12 = (BusinessEntityHandler)maker.makeHandler("businessEntity");
               BusinessEntity var7 = (BusinessEntity)var12.create(var9.item(var11));
               Element var8 = (Element)var9.item(var11);
               if (var8.getAttributeNode("authorizedName") != null) {
                  var7.setAuthorizedName(var8.getAttribute("authorizedName"));
               }

               var2.addBusinessEntity(var7);
            }
         }
      }

      return var2;
   }
}
