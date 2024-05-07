package weblogic.wsee.async;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;

public class AsyncHandler extends GenericHandler {
   public QName[] getHeaders() {
      return new QName[0];
   }
}
