package weblogic.wsee.reliability.faults;

import weblogic.wsee.reliability.WsrmConstants;

public class SequenceClosedFaultMsg extends SequenceFaultMsg {
   public static final SequenceFaultMsgType TYPE = new SequenceFaultMsgType();

   public SequenceClosedFaultMsg(WsrmConstants.RMVersion var1) {
      super(var1, WsrmConstants.FaultCode.SENDER, "SequenceClosed", "The Sequence is closed and cannot accept new messages.", TYPE);
   }
}
