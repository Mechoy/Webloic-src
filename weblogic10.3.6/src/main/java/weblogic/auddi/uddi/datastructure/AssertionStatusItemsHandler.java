package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class AssertionStatusItemsHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      AssertionStatusItems var2 = null;
      Object var3 = null;
      AssertionStatusItemHandler var4 = null;
      var2 = new AssertionStatusItems();
      var4 = (AssertionStatusItemHandler)((UDDIXMLHandlerMaker)var3).makeHandler("assertionStatusItem");
      NodeList var5 = var1.getChildNodes();
      int var6 = var5.getLength();

      for(int var7 = 0; var7 < var6; ++var7) {
         var2.add((AssertionStatusItem)var4.create(var5.item(var7)));
      }

      return var2;
   }
}
