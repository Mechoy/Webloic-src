package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class BusinessEntityExtHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      UDDIXMLHandlerMaker var2 = null;
      BusinessEntityExt var3 = null;
      var3 = new BusinessEntityExt();
      var2 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      NodeList var5 = var1.getChildNodes();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            if (var5.item(var6).getNodeType() != 8 && var5.item(var6).getNodeName().equals("businessEntity")) {
               BusinessEntityHandler var7 = (BusinessEntityHandler)var2.makeHandler("businessEntity");
               BusinessEntity var8 = (BusinessEntity)var7.create(var5.item(var6));
               var3.setBusinessEntity(var8);
            }
         }
      }

      return var3;
   }
}
