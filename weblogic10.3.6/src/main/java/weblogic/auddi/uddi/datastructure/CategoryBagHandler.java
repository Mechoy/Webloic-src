package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class CategoryBagHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      CategoryBag var2 = new CategoryBag();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      KeyedReferenceHandler var4 = (KeyedReferenceHandler)var3.makeHandler("keyedReference");
      NodeList var5 = var1.getChildNodes();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            if (var5.item(var6).getNodeName().equals("keyedReference") && var5.item(var6).getNodeType() != 8) {
               KeyedReferenceHandler var7 = (KeyedReferenceHandler)var3.makeHandler("keyedReference");
               KeyedReference var8 = (KeyedReference)var7.create(var5.item(var6));
               var2.add(var8);
            }
         }
      }

      return var2;
   }
}
