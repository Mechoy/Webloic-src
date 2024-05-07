package weblogic.wsee.reliability2.tube.processors;

import com.sun.xml.ws.api.message.Packet;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;

public class AckProcessor extends MessageProcessor {
   public AckProcessor(WsrmTubeUtils var1) {
      super(var1);
   }

   public boolean processOutbound(Packet var1, WsrmConstants.RMVersion var2, SequenceIdFactory var3, String var4) throws WsrmException {
      return true;
   }

   public void handleInbound(Packet var1, WsrmConstants.RMVersion var2, WsrmTubeUtils.InboundMessageResult var3) throws WsrmException {
   }
}
