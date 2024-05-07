package weblogic.wsee.reliability.faults;

import weblogic.wsee.reliability.WsrmConstants;

public class UnknownSequenceFaultMsg extends SequenceFaultMsg {
   public static final SequenceFaultMsgType TYPE = new SequenceFaultMsgType();

   public UnknownSequenceFaultMsg(WsrmConstants.RMVersion var1) {
      super(var1, WsrmConstants.FaultCode.SENDER, "UnknownSequence", "The value of wsrm:Identifier is not a known Sequence identifier.", TYPE);
   }
}
