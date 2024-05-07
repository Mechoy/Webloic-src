package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class TModelBagHandler extends UDDIXMLHandler {
   private UDDIXMLHandlerMaker maker;

   public Object create(Node var1) throws UDDIException {
      TModelBag var2 = new TModelBag();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      NodeList var4 = var1.getChildNodes();
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.getLength(); ++var5) {
            if (var4.item(var5).getNodeType() != 8 && var4.item(var5).getNodeName().equals("tModelKey")) {
               TModelKeyHandler var6 = (TModelKeyHandler)var3.makeHandler("tModelKey");
               Element var7 = (Element)var4.item(var5);
               Node var8 = var7.getChildNodes().item(0);
               if (var8 != null) {
                  TModelKey var9 = (TModelKey)var6.create(var4.item(var5));
                  var2.add(var9);
               }
            }
         }
      }

      return var2;
   }
}
