package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.InvalidCompletionStatusException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class CompletionStatusHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      Element var2 = (Element)var1;
      CompletionStatus var3 = null;
      if (var2.getChildNodes().item(0) != null) {
         var3 = new CompletionStatus(var2.getChildNodes().item(0).getNodeValue());
         return var3;
      } else {
         throw new InvalidCompletionStatusException(UDDIMessages.get("error.invalidCompletionStatus.status", "empty"));
      }
   }
}
