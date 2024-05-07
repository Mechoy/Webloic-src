package weblogic.auddi.uddi.response;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BindingTemplate;
import weblogic.auddi.uddi.datastructure.BindingTemplateHandler;
import weblogic.auddi.uddi.datastructure.BindingTemplates;
import weblogic.auddi.uddi.datastructure.BindingTemplatesHandler;
import weblogic.auddi.uddi.datastructure.Operator;
import weblogic.auddi.uddi.datastructure.OperatorHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class BindingDetailResponseHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;

   public Object create(Node var1) throws UDDIException {
      BindingDetailResponse var2 = new BindingDetailResponse();
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
         OperatorHandler var9 = (OperatorHandler)maker.makeHandler("operator");
         Operator var6 = (Operator)var9.create(var4);
         var2.setOperator(var6);
      }

      NodeList var8 = var1.getChildNodes();
      if (var8 != null) {
         for(int var10 = 0; var10 < var8.getLength(); ++var10) {
            if (var8.item(var10).getNodeName().equals("bindingTemplates")) {
               BindingTemplatesHandler var11 = (BindingTemplatesHandler)maker.makeHandler("bindingTemplates");
               BindingTemplates var7 = (BindingTemplates)var11.create(var8.item(var10));
               var2.setBindingTemplates(var7);
            } else if (var8.item(var10).getNodeName().equals("bindingTemplate")) {
               BindingTemplateHandler var12 = (BindingTemplateHandler)maker.makeHandler("bindingTemplate");
               BindingTemplate var13 = (BindingTemplate)var12.create(var8.item(var10));
               var2.addBindingTemplate(var13);
            }
         }
      }

      return var2;
   }
}
