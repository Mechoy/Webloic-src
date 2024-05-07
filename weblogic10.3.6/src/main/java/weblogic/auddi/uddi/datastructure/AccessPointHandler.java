package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.ValueNotAllowedException;

public class AccessPointHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      AccessPoint var2 = null;
      Object var3 = null;
      String var4 = null;
      String var5 = null;
      Node var6 = var1.getAttributes().getNamedItem("URLType");
      if (var6 == null) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "URLType"));
      } else {
         var4 = var6.getNodeValue();
         Node var7 = var1.getChildNodes().item(0);
         if (var7 == null) {
            throw new ValueNotAllowedException(UDDIMessages.get("error.valueNotAllowed.empty", "accessPoint"));
         } else {
            var5 = var7.getNodeValue();
            var2 = new AccessPoint(var4, var5);
            return var2;
         }
      }
   }
}
