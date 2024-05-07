package weblogic.wsee.reliability.faults;

import weblogic.wsee.reliability.WsrmConstants;

public class LastMessageNumExceededFaultMsg extends SequenceFaultMsg {
   public static final SequenceFaultMsgType TYPE = new SequenceFaultMsgType();

   public LastMessageNumExceededFaultMsg(WsrmConstants.RMVersion var1) {
      super(var1, WsrmConstants.FaultCode.SENDER, "LastMessageNumberExceeded", "The value for wsrm:MessageNumber exceeds the value of the MessageNumber accompanying a LastMessage element in this Sequence.", TYPE);
   }
}
