package weblogic.wsee.reliability2.tube.processors;

import com.sun.xml.ws.api.message.Packet;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;

public class LastMessageProcessor extends MessageProcessor {
   private static final Logger LOGGER = Logger.getLogger(LastMessageProcessor.class.getName());

   public LastMessageProcessor(WsrmTubeUtils var1) {
      super(var1);
   }

   public boolean processOutbound(Packet var1, WsrmConstants.RMVersion var2, SequenceIdFactory var3, String var4) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Processing outbound LastMessage");
      }

      return true;
   }

   public void handleInbound(Packet var1, WsrmConstants.RMVersion var2, WsrmTubeUtils.InboundMessageResult var3) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Processing inbound LastMessage");
      }

      var3.message = this._tubeUtil.processInboundSequenceMessage(var1).message;
   }
}
