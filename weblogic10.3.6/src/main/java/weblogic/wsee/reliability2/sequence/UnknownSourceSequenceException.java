package weblogic.wsee.reliability2.sequence;

import weblogic.wsee.reliability.WsrmConstants;

public class UnknownSourceSequenceException extends UnknownSequenceException {
   private static final long serialVersionUID = 1L;

   public UnknownSourceSequenceException(WsrmConstants.RMVersion var1, String var2) {
      super(var1, true, var2);
   }

   public UnknownSourceSequenceException(UnknownSequenceException var1) {
      this(var1.getRmVersion(), var1.getSequenceId());
   }
}
