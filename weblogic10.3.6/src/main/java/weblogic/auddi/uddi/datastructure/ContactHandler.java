package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class ContactHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      UDDIXMLHandlerMaker var7 = UDDIXMLHandlerMaker.getInstance();
      Element var8 = (Element)var1;
      Element var9 = (Element)var8.getElementsByTagName("personName").item(0);
      NodeList var10 = var9.getChildNodes();
      Contact var2;
      if (var10.getLength() > 0) {
         var2 = new Contact(var10.item(0).getNodeValue());
      } else {
         var2 = new Contact("");
      }

      Attr var11 = var8.getAttributeNode("useType");
      if (var11 != null && var2 != null) {
         String var12 = var11.getValue();
         var2.setUseType(var12);
      }

      if (var2 != null) {
         NodeList var15 = var1.getChildNodes();
         int var13 = var15.getLength();

         for(int var14 = 0; var14 < var13; ++var14) {
            if (var15.item(var14).getNodeType() != 8) {
               if (var15.item(var14).getNodeName().equals("phone")) {
                  PhoneHandler var3 = (PhoneHandler)var7.makeHandler("phone");
                  var2.addPhone((Phone)var3.create(var15.item(var14)));
               } else if (var15.item(var14).getNodeName().equals("email")) {
                  EmailHandler var4 = (EmailHandler)var7.makeHandler("email");
                  var2.addEmail((Email)var4.create(var15.item(var14)));
               } else if (var15.item(var14).getNodeName().equals("address")) {
                  AddressHandler var5 = (AddressHandler)var7.makeHandler("address");
                  var2.addAddress((Address)var5.create(var15.item(var14)));
               } else if (var15.item(var14).getNodeName().equals("description")) {
                  DescriptionHandler var6 = (DescriptionHandler)var7.makeHandler("description");
                  var2.addDescription((Description)var6.create(var15.item(var14)));
               }
            }
         }
      }

      return var2;
   }
}
