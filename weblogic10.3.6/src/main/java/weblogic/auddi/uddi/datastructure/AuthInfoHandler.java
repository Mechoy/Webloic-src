package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class AuthInfoHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      Element var2 = (Element)var1;
      Node var3 = var2.getChildNodes().item(0);
      if (var3 == null) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.authInfo.empty"));
      } else {
         AuthInfo var4 = new AuthInfo(var3.getNodeValue());
         return var4;
      }
   }
}
