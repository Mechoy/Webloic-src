package weblogic.wsee.reliability.faults;

import weblogic.wsee.reliability.WsrmConstants;

public class SequenceRefusedFaultMsg extends SequenceFaultMsg {
   public static final SequenceFaultMsgType TYPE = new SequenceFaultMsgType();

   public SequenceRefusedFaultMsg(WsrmConstants.RMVersion var1) {
      super(var1, WsrmConstants.FaultCode.SENDER, "CreateSequenceRefused", "The requested Sequence has been refused by the RM Destination.", TYPE);
   }
}
