package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.InvalidKeyPassedException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class BusinessKeyHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      BusinessKey var2 = null;
      if (var1.getNodeType() == 2) {
         String var5 = var1.getNodeValue();
         if (var5 != null && !var5.equals("")) {
            var2 = new BusinessKey(var5);
         }

         return var2;
      } else {
         Element var3 = (Element)var1;
         Node var4 = var3.getChildNodes().item(0);
         if (var4 == null) {
            throw new InvalidKeyPassedException(UDDIMessages.get("error.invalidKeyPassed.empty", "businessKey"));
         } else {
            var2 = new BusinessKey(var4.getNodeValue());
            return var2;
         }
      }
   }
}
