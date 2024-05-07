package weblogic.wsee.security.wssc.v13.faults;

import weblogic.wsee.security.wssc.base.faults.UnknownDerivationSourceExceptionBase;

public class UnknownDerivationSourceException extends UnknownDerivationSourceExceptionBase {
   public UnknownDerivationSourceException(String var1) {
      super(var1, "wsc", "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512");
   }
}
