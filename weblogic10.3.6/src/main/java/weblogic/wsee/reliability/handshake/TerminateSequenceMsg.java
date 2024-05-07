package weblogic.wsee.reliability.handshake;

import weblogic.wsee.reliability.WsrmConstants;

public final class TerminateSequenceMsg extends EndOfLifeSequenceMsg {
   public TerminateSequenceMsg(WsrmConstants.RMVersion var1, String var2) {
      super(var1, WsrmConstants.Element.TERMINATE_SEQUENCE.getElementName(), var2);
   }

   public TerminateSequenceMsg(WsrmConstants.RMVersion var1) {
      this(var1, (String)null);
   }
}
