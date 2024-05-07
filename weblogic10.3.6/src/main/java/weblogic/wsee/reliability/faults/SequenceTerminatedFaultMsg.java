package weblogic.wsee.reliability.faults;

import weblogic.wsee.reliability.WsrmConstants;

public class SequenceTerminatedFaultMsg extends SequenceFaultMsg {
   public static final SequenceFaultMsgType TYPE = new SequenceFaultMsgType();

   public SequenceTerminatedFaultMsg(WsrmConstants.RMVersion var1) {
      this(var1, true);
   }

   public SequenceTerminatedFaultMsg(WsrmConstants.RMVersion var1, boolean var2) {
      super(var1, var2 ? WsrmConstants.FaultCode.SENDER : WsrmConstants.FaultCode.RECEIVER, "SequenceTerminated", "The Sequence has been terminated due to an unrecoverable error.", TYPE);
   }
}
