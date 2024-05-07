package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.util.Logger;

public class BusinessEntityHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      Logger.trace("+BusinessEntityHandler.create()");
      UDDIXMLHandlerMaker var2 = UDDIXMLHandlerMaker.getInstance();
      NodeList var4 = var1.getChildNodes();
      int var5 = var4.getLength();
      Element var6 = (Element)var1;
      Attr var7 = var6.getAttributeNode("businessKey");
      BusinessEntity var3;
      if (var6.getAttributeNode("businessKey") != null) {
         BusinessKeyHandler var8 = (BusinessKeyHandler)var2.makeHandler("businessKey");
         BusinessKey var9 = (BusinessKey)var8.create(var7);
         var3 = new BusinessEntity(var9);
      } else {
         var3 = new BusinessEntity();
      }

      if (var6.getAttributeNode("operator") != null) {
         var3.setOperator(var6.getAttribute("operator"));
      }

      if (var6.getAttributeNode("authorizedName") != null) {
         var3.setAuthorizedName(var6.getAttribute("authorizedName"));
      }

      for(int var12 = 0; var12 < var5; ++var12) {
         if (var4.item(var12).getNodeType() != 8) {
            String var13 = var4.item(var12).getNodeName();
            Logger.trace("name: " + var13);
            if (var13.equals("discoveryURLs")) {
               DiscoveryURLsHandler var10 = (DiscoveryURLsHandler)var2.makeHandler("discoveryURLs");
               DiscoveryURLs var11 = (DiscoveryURLs)var10.create(var4.item(var12));
               var3.setDiscoveryURLs(var11);
            } else if (var13.equals("name")) {
               NameHandler var14 = (NameHandler)var2.makeHandler("name");
               Name var17 = (Name)var14.create(var4.item(var12));
               var3.addName(var17);
            } else if (var13.equals("contacts")) {
               ContactsHandler var15 = (ContactsHandler)var2.makeHandler("contacts");
               Contacts var19 = (Contacts)var15.create(var4.item(var12));
               var3.setContacts(var19);
            } else if (var13.equals("businessServices")) {
               BusinessServicesHandler var16 = (BusinessServicesHandler)var2.makeHandler("businessServices");
               BusinessServices var21 = (BusinessServices)var16.create(var4.item(var12));
               var3.setBusinessServices(var21);
            } else if (var13.equals("description")) {
               DescriptionHandler var18 = (DescriptionHandler)var2.makeHandler("description");
               Description var23 = (Description)var18.create(var4.item(var12));
               var3.addDescription(var23);
            } else if (var13.equals("identifierBag")) {
               IdentifierBagHandler var20 = (IdentifierBagHandler)var2.makeHandler("identifierBag");
               IdentifierBag var24 = (IdentifierBag)var20.create(var4.item(var12));
               var3.setIdentifierBag(var24);
            } else if (var13.equals("categoryBag")) {
               CategoryBagHandler var22 = (CategoryBagHandler)var2.makeHandler("categoryBag");
               CategoryBag var25 = (CategoryBag)var22.create(var4.item(var12));
               var3.setCategoryBag(var25);
            }
         }
      }

      Logger.trace("-BusinessEntityHandler.create()");
      return var3;
   }
}
