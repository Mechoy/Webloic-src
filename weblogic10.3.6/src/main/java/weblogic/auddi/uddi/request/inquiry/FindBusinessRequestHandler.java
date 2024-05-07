package weblogic.auddi.uddi.request.inquiry;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.CategoryBag;
import weblogic.auddi.uddi.datastructure.CategoryBagHandler;
import weblogic.auddi.uddi.datastructure.DiscoveryURLs;
import weblogic.auddi.uddi.datastructure.DiscoveryURLsHandler;
import weblogic.auddi.uddi.datastructure.IdentifierBag;
import weblogic.auddi.uddi.datastructure.IdentifierBagHandler;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.SearchNameHandler;
import weblogic.auddi.uddi.datastructure.TModelBag;
import weblogic.auddi.uddi.datastructure.TModelBagHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class FindBusinessRequestHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      FindBusinessRequest var2 = new FindBusinessRequest();
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
               SearchNameHandler var11 = (SearchNameHandler)var3.makeHandler("searchname");
               Name var14 = (Name)var11.create(var9.item(var10));
               if (var14 != null) {
                  var2.addName(var14);
               }
            }

            if (var9.item(var10).getNodeName().equals("identifierBag")) {
               IdentifierBagHandler var12 = (IdentifierBagHandler)var3.makeHandler("identifierBag");
               IdentifierBag var16 = (IdentifierBag)var12.create(var9.item(var10));
               var2.setIdentifierBag(var16);
            }

            if (var9.item(var10).getNodeName().equals("categoryBag")) {
               CategoryBagHandler var13 = (CategoryBagHandler)var3.makeHandler("categoryBag");
               CategoryBag var18 = (CategoryBag)var13.create(var9.item(var10));
               var2.setCategoryBag(var18);
            }

            if (var9.item(var10).getNodeName().equals("tModelBag")) {
               TModelBagHandler var15 = (TModelBagHandler)var3.makeHandler("tModelBag");
               TModelBag var19 = (TModelBag)var15.create(var9.item(var10));
               var2.setTModelBag(var19);
            }

            if (var9.item(var10).getNodeName().equals("discoveryURLs")) {
               DiscoveryURLsHandler var17 = (DiscoveryURLsHandler)var3.makeHandler("discoveryURLs");
               DiscoveryURLs var20 = (DiscoveryURLs)var17.create(var9.item(var10));
               var2.setDiscoveryURLs(var20);
            }
         }
      }

      return var2;
   }
}
