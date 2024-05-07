package weblogic.wsee.reliability.faults;

import weblogic.wsee.reliability.WsrmConstants;

public class MessageNumRolloverFaultMsg extends SequenceFaultMsg {
   public static final SequenceFaultMsgType TYPE = new SequenceFaultMsgType();

   public MessageNumRolloverFaultMsg(WsrmConstants.RMVersion var1) {
      super(var1, WsrmConstants.FaultCode.SENDER, "MessageNumberRollover", "The maximum value for wsrm:MessageNumber has been exceeded.", TYPE);
   }
}
