package weblogic.auddi.uddi.response;

import org.w3c.dom.Node;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;

public class ErrorResponseHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      return new ErrorResponse(var1);
   }
}
