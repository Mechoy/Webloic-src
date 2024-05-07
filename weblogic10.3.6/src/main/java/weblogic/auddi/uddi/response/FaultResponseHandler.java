package weblogic.auddi.uddi.response;

import org.w3c.dom.Node;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;

public class FaultResponseHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      FaultResponse var2 = new FaultResponse(var1);
      return var2;
   }
}
