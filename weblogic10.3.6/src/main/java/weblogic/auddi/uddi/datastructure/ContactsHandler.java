package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class ContactsHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      UDDIXMLHandlerMaker var2 = UDDIXMLHandlerMaker.getInstance();
      Contacts var3 = new Contacts();
      ContactHandler var4 = (ContactHandler)var2.makeHandler("contact");
      NodeList var5 = var1.getChildNodes();
      int var6 = var5.getLength();

      for(int var7 = 0; var7 < var6; ++var7) {
         if (var5.item(var7).getNodeType() != 8) {
            var3.add((Contact)var4.create(var5.item(var7)));
         }
      }

      return var3;
   }
}
