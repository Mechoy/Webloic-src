package weblogic.wsee.security.wssc.v13.faults;

import weblogic.wsee.security.wssc.base.faults.BadContextTokenExceptionBase;

public class BadContextTokenException extends BadContextTokenExceptionBase {
   public BadContextTokenException(String var1) {
      super(var1, "wsc", "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512");
   }
}
