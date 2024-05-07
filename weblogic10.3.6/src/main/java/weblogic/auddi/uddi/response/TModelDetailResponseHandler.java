package weblogic.auddi.uddi.response;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.Operator;
import weblogic.auddi.uddi.datastructure.OperatorHandler;
import weblogic.auddi.uddi.datastructure.TModel;
import weblogic.auddi.uddi.datastructure.TModelHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class TModelDetailResponseHandler extends UDDIXMLHandler {
   private TModelDetailResponse tModelDetailResponse = null;
   private static UDDIXMLHandlerMaker maker = null;

   public Object create(Node var1) throws UDDIException {
      this.tModelDetailResponse = new TModelDetailResponse();
      maker = UDDIXMLHandlerMaker.getInstance();
      Element var2 = (Element)var1;
      Attr var3;
      if (var2.getAttributeNode("truncated") != null) {
         var3 = var2.getAttributeNode("truncated");
         String var4 = var3.getNodeValue();
         this.tModelDetailResponse.setTruncated(Boolean.valueOf(var4));
      }

      if (var2.getAttributeNode("operator") != null) {
         var3 = var2.getAttributeNode("operator");
         OperatorHandler var9 = (OperatorHandler)maker.makeHandler("operator");
         Operator var5 = (Operator)var9.create(var3);
         this.tModelDetailResponse.setOperator(var5);
      }

      NodeList var8 = var1.getChildNodes();
      if (var8 != null) {
         for(int var10 = 0; var10 < var8.getLength(); ++var10) {
            if (var8.item(var10).getNodeName().equals("tModel")) {
               TModelHandler var11 = (TModelHandler)maker.makeHandler("tModel");
               TModel var6 = (TModel)var11.create(var8.item(var10));
               Element var7 = (Element)var8.item(var10);
               if (var7.getAttributeNode("authorizedName") != null) {
                  var6.setAuthorizedName(var7.getAttribute("authorizedName"));
               }

               this.tModelDetailResponse.addTModel(var6);
            }
         }
      }

      return this.tModelDetailResponse;
   }
}
