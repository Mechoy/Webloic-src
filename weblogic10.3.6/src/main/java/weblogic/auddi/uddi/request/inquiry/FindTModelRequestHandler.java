package weblogic.auddi.uddi.request.inquiry;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.CategoryBag;
import weblogic.auddi.uddi.datastructure.CategoryBagHandler;
import weblogic.auddi.uddi.datastructure.IdentifierBag;
import weblogic.auddi.uddi.datastructure.IdentifierBagHandler;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.NameHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;

public class FindTModelRequestHandler extends UDDIXMLHandler {
   private UDDIRequest uddirequest;

   public Object create(Node var1) throws UDDIException {
      FindTModelRequest var2 = new FindTModelRequest();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      if (var4.getAttributeNode("maxRows") != null) {
         Attr var5 = var4.getAttributeNode("maxRows");
         String var6 = var5.getNodeValue();
         var2.setMaxRows(var6);
      }

      NodeList var9 = var1.getChildNodes();
      if (var9 != null) {
         for(int var10 = 0; var10 < var9.getLength(); ++var10) {
            if (var9.item(var10).getNodeName().equals("findQualifiers")) {
               FindQualifiersHandler var7 = (FindQualifiersHandler)var3.makeHandler("findQualifiers");
               FindQualifiers var8 = (FindQualifiers)var7.create(var9.item(var10));
               var2.setFindQualifiers(var8);
            }

            if (var9.item(var10).getNodeName().equals("name")) {
               NameHandler var11 = (NameHandler)var3.makeHandler("name");
               Name var14 = (Name)var11.create(var9.item(var10));
               if (var14 != null) {
                  var2.addName(var14);
               }
            }

            if (var9.item(var10).getNodeName().equals("categoryBag")) {
               CategoryBagHandler var12 = (CategoryBagHandler)var3.makeHandler("categoryBag");
               CategoryBag var15 = (CategoryBag)var12.create(var9.item(var10));
               var2.setCategoryBag(var15);
            }

            if (var9.item(var10).getNodeName().equals("identifierBag")) {
               IdentifierBagHandler var13 = (IdentifierBagHandler)var3.makeHandler("identifierBag");
               IdentifierBag var16 = (IdentifierBag)var13.create(var9.item(var10));
               var2.setIdentifierBag(var16);
            }
         }
      }

      return var2;
   }
}
