package weblogic.auddi.uddi.request.inquiry;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class FindQualifiersHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      FindQualifiers var2 = new FindQualifiers();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      NodeList var4 = var1.getChildNodes();
      if (var4 != null) {
         int var5 = var4.getLength();

         for(int var6 = 0; var6 < var5; ++var6) {
            if (var4.item(var6).getNodeName().equals("findQualifier")) {
               FindQualifierHandler var7 = (FindQualifierHandler)var3.makeHandler("findQualifier");
               FindQualifier var8 = (FindQualifier)var7.create(var4.item(var6));
               var2.add(var8);
            }
         }
      }

      return var2;
   }
}
