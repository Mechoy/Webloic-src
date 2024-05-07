package weblogic.wsee.reliability.handshake;

import weblogic.wsee.reliability.WsrmConstants;

public final class CloseSequenceMsg extends EndOfLifeSequenceMsg {
   public CloseSequenceMsg(WsrmConstants.RMVersion var1, String var2) {
      super(var1, WsrmConstants.Element.CLOSE_SEQUENCE.getElementName(), var2);
   }

   public CloseSequenceMsg(WsrmConstants.RMVersion var1) {
      this(var1, (String)null);
   }
}
