package weblogic.jms.backend;

import weblogic.messaging.kernel.UOWCallback;
import weblogic.messaging.kernel.UOWCallbackCaller;
import weblogic.messaging.kernel.UOWCallbackFactory;

public class BEUOWCallbackFactory implements UOWCallbackFactory {
   public UOWCallback create(UOWCallbackCaller var1, String var2) {
      return new BEUOWCallback(var1, var2);
   }
}
