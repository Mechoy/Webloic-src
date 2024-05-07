package weblogic.wsee.reliability.handshake;

import weblogic.wsee.reliability.WsrmConstants;

public final class TerminateSequenceResponseMsg extends EndOfLifeSequenceResponseMsg {
   public TerminateSequenceResponseMsg(WsrmConstants.RMVersion var1, String var2) {
      super(var1, WsrmConstants.Element.TERMINATE_SEQUENCE_RESPONSE.getElementName(), var2);
   }

   public TerminateSequenceResponseMsg(WsrmConstants.RMVersion var1) {
      this(var1, (String)null);
   }
}
