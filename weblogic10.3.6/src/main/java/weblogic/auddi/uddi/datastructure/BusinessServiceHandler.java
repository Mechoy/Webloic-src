package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class BusinessServiceHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      UDDIXMLHandlerMaker var2 = null;
      BusinessService var3 = null;
      var3 = new BusinessService();
      var2 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      Attr var5;
      if (var4.getAttributeNode("businessKey") != null) {
         var5 = var4.getAttributeNode("businessKey");
         BusinessKeyHandler var6 = (BusinessKeyHandler)var2.makeHandler("businessKey");
         BusinessKey var7 = (BusinessKey)var6.create(var5);
         var3.setBusinessKey(var7);
      }

      if (var4.getAttributeNode("serviceKey") != null) {
         var5 = var4.getAttributeNode("serviceKey");
         ServiceKeyHandler var10 = (ServiceKeyHandler)var2.makeHandler("serviceKey");
         ServiceKey var12 = (ServiceKey)var10.create(var5);
         var3.setServiceKey(var12);
      }

      NodeList var9 = var1.getChildNodes();
      if (var9 != null) {
         for(int var11 = 0; var11 < var9.getLength(); ++var11) {
            if (var9.item(var11).getNodeType() != 8) {
               if (var9.item(var11).getNodeName().equals("name")) {
                  NameHandler var13 = (NameHandler)var2.makeHandler("name");
                  Name var8 = (Name)var13.create(var9.item(var11));
                  var3.addName(var8);
               }

               if (var9.item(var11).getNodeName().equals("description")) {
                  DescriptionHandler var14 = (DescriptionHandler)var2.makeHandler("description");
                  Description var15 = (Description)var14.create(var9.item(var11));
                  var3.addDescription(var15);
               }

               if (var9.item(var11).getNodeName().equals("bindingTemplates")) {
                  BindingTemplatesHandler var16 = (BindingTemplatesHandler)var2.makeHandler("bindingTemplates");
                  BindingTemplates var17 = (BindingTemplates)var16.create(var9.item(var11));
                  var3.setBindingTemplates(var17);
               }

               if (var9.item(var11).getNodeName().equals("categoryBag")) {
                  CategoryBagHandler var18 = (CategoryBagHandler)var2.makeHandler("categoryBag");
                  CategoryBag var19 = (CategoryBag)var18.create(var9.item(var11));
                  var3.setCategoryBag(var19);
               }
            }
         }
      }

      return var3;
   }
}
