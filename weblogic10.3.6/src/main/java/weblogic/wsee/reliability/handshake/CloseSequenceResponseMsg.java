package weblogic.wsee.reliability.handshake;

import weblogic.wsee.reliability.WsrmConstants;

public final class CloseSequenceResponseMsg extends EndOfLifeSequenceResponseMsg {
   public CloseSequenceResponseMsg(WsrmConstants.RMVersion var1, String var2) {
      super(var1, WsrmConstants.Element.CLOSE_SEQUENCE_RESPONSE.getElementName(), var2);
   }

   public CloseSequenceResponseMsg(WsrmConstants.RMVersion var1) {
      this(var1, (String)null);
   }
}
