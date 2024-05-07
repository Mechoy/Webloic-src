package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class IdentifierBagHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      UDDIXMLHandlerMaker var2 = UDDIXMLHandlerMaker.getInstance();
      IdentifierBag var3 = new IdentifierBag();
      NodeList var4 = var1.getChildNodes();
      KeyedReferenceHandler var5 = (KeyedReferenceHandler)var2.makeHandler("keyedReference");
      int var6 = var4.getLength();

      for(int var7 = 0; var7 < var6; ++var7) {
         if (var4.item(var7).getNodeType() != 8) {
            KeyedReference var8 = (KeyedReference)var5.create(var4.item(var7));
            var3.add(var8);
         }
      }

      return var3;
   }
}
