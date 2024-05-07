package weblogic.wsee.callback;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;

public class CallbackHandler extends GenericHandler {
   public QName[] getHeaders() {
      return CallbackConstants.CALLBACK_HEADERS;
   }
}
