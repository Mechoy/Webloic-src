package weblogic.wsee.reliability.faults;

import weblogic.wsee.reliability.WsrmConstants;

public class SecurityMismatchFaultException extends SequenceFaultException {
   private static final long serialVersionUID = 1L;

   public SecurityMismatchFaultException(String var1, WsrmConstants.RMVersion var2, boolean var3) {
      super((SequenceFaultMsg)(new SecurityMismatchFaultMsg(var1, var2, var3)));
   }

   public boolean isSsl() {
      return ((SecurityMismatchFaultMsg)this.getMsg()).isSsl();
   }

   public boolean isSct() {
      return ((SecurityMismatchFaultMsg)this.getMsg()).isSct();
   }
}
