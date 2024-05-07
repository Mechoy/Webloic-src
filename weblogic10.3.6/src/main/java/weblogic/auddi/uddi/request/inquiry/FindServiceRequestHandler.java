package weblogic.auddi.uddi.request.inquiry;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.CategoryBag;
import weblogic.auddi.uddi.datastructure.CategoryBagHandler;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.SearchNameHandler;
import weblogic.auddi.uddi.datastructure.TModelBag;
import weblogic.auddi.uddi.datastructure.TModelBagHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;

public class FindServiceRequestHandler extends UDDIXMLHandler {
   private UDDIRequest uddirequest;

   public Object create(Node var1) throws UDDIException {
      FindServiceRequest var2 = new FindServiceRequest();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      Attr var5;
      if (var4.getAttributeNode("businessKey") != null) {
         var5 = var4.getAttributeNode("businessKey");
         BusinessKey var6 = null;
         if (!var5.getNodeValue().trim().equals("") && !var5.getNodeValue().trim().equals("*")) {
            var6 = new BusinessKey(var5.getNodeValue());
         } else {
            var6 = new BusinessKey("00000000-0000-0000-0000-000000000000");
         }

         var2.setBusinessKey(var6);
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

            if (var9.item(var12).getNodeName().equals("name")) {
               SearchNameHandler var11 = (SearchNameHandler)var3.makeHandler("searchname");
               Name var15 = (Name)var11.create(var9.item(var12));
               if (var15 != null) {
                  var2.addName(var15);
               }
            }

            if (var9.item(var12).getNodeName().equals("categoryBag")) {
               CategoryBagHandler var13 = (CategoryBagHandler)var3.makeHandler("categoryBag");
               CategoryBag var16 = (CategoryBag)var13.create(var9.item(var12));
               var2.setCategoryBag(var16);
            }

            if (var9.item(var12).getNodeName().equals("tModelBag")) {
               TModelBagHandler var14 = (TModelBagHandler)var3.makeHandler("tModelBag");
               TModelBag var17 = (TModelBag)var14.create(var9.item(var12));
               var2.setTModelBag(var17);
            }
         }
      }

      return var2;
   }
}
