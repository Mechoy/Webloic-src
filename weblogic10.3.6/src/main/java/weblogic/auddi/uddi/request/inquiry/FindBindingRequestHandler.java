package weblogic.auddi.uddi.request.inquiry;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.ServiceKey;
import weblogic.auddi.uddi.datastructure.TModelBag;
import weblogic.auddi.uddi.datastructure.TModelBagHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;

public class FindBindingRequestHandler extends UDDIXMLHandler {
   private UDDIRequest uddirequest;

   public Object create(Node var1) throws UDDIException {
      FindBindingRequest var2 = new FindBindingRequest();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      Attr var5;
      if (var4.getAttributeNode("serviceKey") != null) {
         var5 = var4.getAttributeNode("serviceKey");
         ServiceKey var6 = null;
         if (var5.getNodeValue().trim().equals("")) {
            var6 = new ServiceKey("00000000-0000-0000-0000-000000000000");
         } else {
            var6 = new ServiceKey(var5.getNodeValue());
         }

         var2.setServiceKey(var6);
      }

      if (var4.getAttributeNode("maxRows") != null) {
         var5 = var4.getAttributeNode("maxRows");
         String var10 = var5.getNodeValue();
         var2.setMaxRows(var10);
      }

      NodeList var9 = var1.getChildNodes();
      if (var9 != null) {
         for(int var12 = 0; var12 < var9.getLength(); ++var12) {
            if (var9.item(var12).getNodeName().equals("findQualifiers")) {
               FindQualifiersHandler var7 = (FindQualifiersHandler)var3.makeHandler("findQualifiers");
               FindQualifiers var8 = (FindQualifiers)var7.create(var9.item(var12));
               var2.setFindQualifiers(var8);
            }

            if (var9.item(var12).getNodeName().equals("tModelBag")) {
               TModelBagHandler var11 = (TModelBagHandler)var3.makeHandler("tModelBag");
               TModelBag var13 = (TModelBag)var11.create(var9.item(var12));
               var2.setTModelBag(var13);
            }
         }
      }

      return var2;
   }
}
