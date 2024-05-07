package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.TooManyOptionsException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class TModelExtHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      TModelExt var2 = new TModelExt();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      Attr var5;
      if (var4.getAttributeNode("tModelKey") != null) {
         var5 = var4.getAttributeNode("tModelKey");
         TModelKeyHandler var6 = (TModelKeyHandler)var3.makeHandler("tModelKey");
         TModelKey var7 = (TModelKey)var6.create(var5);
         var2.setTModelKey(var7);
      }

      if (var4.getAttributeNode("operator") != null) {
         var5 = var4.getAttributeNode("operator");
         OperatorHandler var11 = (OperatorHandler)var3.makeHandler("operator");
         Operator var13 = (Operator)var11.create(var5);
         var2.setOperator(var13);
      }

      if (var4.getAttributeNode("authorizedName") != null) {
         var2.setAuthorizedName(var4.getAttribute("authorizedName"));
      }

      NodeList var10 = var1.getChildNodes();
      if (var10 != null) {
         boolean var12 = false;

         for(int var14 = 0; var14 < var10.getLength(); ++var14) {
            if (var10.item(var14).getNodeType() != 8) {
               if (var10.item(var14).getNodeName().equals("name")) {
                  if (var12) {
                     throw new TooManyOptionsException(UDDIMessages.get("error.tooManyOptions.name", "1"));
                  }

                  var12 = true;
                  NameHandler var8 = new NameHandler();
                  Name var9 = (Name)var8.create(var10.item(var14));
                  var2.setName(var9);
               }

               if (var10.item(var14).getNodeName().equals("description")) {
                  DescriptionHandler var15 = new DescriptionHandler();
                  Description var18 = (Description)var15.create(var10.item(var14));
                  var2.addDescription(var18);
               }

               if (var10.item(var14).getNodeName().equals("overviewDoc")) {
                  OverviewDocHandler var16 = new OverviewDocHandler();
                  OverviewDoc var20 = (OverviewDoc)var16.create(var10.item(var14));
                  var2.setOverviewDoc(var20);
               }

               if (var10.item(var14).getNodeName().equals("identifierBag")) {
                  IdentifierBagHandler var17 = new IdentifierBagHandler();
                  IdentifierBag var22 = (IdentifierBag)var17.create(var10.item(var14));
                  var2.setIdentifierBag(var22);
               }

               if (var10.item(var14).getNodeName().equals("categoryBag")) {
                  CategoryBagHandler var19 = new CategoryBagHandler();
                  CategoryBag var23 = (CategoryBag)var19.create(var10.item(var14));
                  var2.setCategoryBag(var23);
               }

               if (var10.item(var14).getNodeName().equals("isHidden")) {
                  String var21 = var10.item(var14).getChildNodes().item(0).getNodeValue();
                  boolean var24 = Boolean.valueOf(var21);
                  var2.setHidden(var24);
               }
            }
         }
      }

      return var2;
   }
}
