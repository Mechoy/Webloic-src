package weblogic.wsee.reliability.faults;

import org.w3c.dom.Element;
import weblogic.wsee.reliability.WsrmConstants;

public class WSRMRequiredFaultMsg extends SequenceFaultMsg {
   public static final SequenceFaultMsgType TYPE = new SequenceFaultMsgType();

   public WSRMRequiredFaultMsg(WsrmConstants.RMVersion var1) {
      super(var1, WsrmConstants.FaultCode.SENDER, "WSRMRequired", "The RM Destination requires the use of WSRM.", TYPE);
   }

   public void writeDetail(Element var1) throws SequenceFaultException {
   }

   public void readDetail(Element var1) throws SequenceFaultException {
   }
}
