package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.InvalidKeyPassedException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class TModelKeyHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      TModelKey var2 = null;
      if (var1 == null) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "tModelKey"));
      } else if (var1.getNodeType() == 2) {
         String var5 = var1.getNodeValue();
         if (var5 != null && !var5.equals("")) {
            var2 = new TModelKey(var5);
         }

         return var2;
      } else {
         Element var3 = (Element)var1;
         Node var4 = var3.getChildNodes().item(0);
         if (var4 == null) {
            throw new InvalidKeyPassedException(UDDIMessages.get("error.invalidKeyPassed.empty", "tModelKey"));
         } else {
            var2 = new TModelKey(var4.getNodeValue());
            return var2;
         }
      }
   }
}
