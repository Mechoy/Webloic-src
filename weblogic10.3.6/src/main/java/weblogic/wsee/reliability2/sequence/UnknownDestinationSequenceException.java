package weblogic.wsee.reliability2.sequence;

import weblogic.wsee.reliability.WsrmConstants;

public class UnknownDestinationSequenceException extends UnknownSequenceException {
   private static final long serialVersionUID = 1L;

   public UnknownDestinationSequenceException(UnknownSequenceException var1) {
      this(var1.getRmVersion(), var1.getSequenceId());
   }

   public UnknownDestinationSequenceException(WsrmConstants.RMVersion var1, String var2) {
      this(var1, var2, false);
   }

   public UnknownDestinationSequenceException(WsrmConstants.RMVersion var1, String var2, boolean var3) {
      super(!var3 ? null : "No source sequence found with ID (interpreted as sequence ID or CreateSequence msg ID): " + var2, var1, false, var2);
   }
}
