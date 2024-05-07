package weblogic.wsee.reliability2.tube.processors;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Packet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.headers.AckRequestedHeader;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.headers.WsrmHeaderFactory;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;

public class AckRequestedProcessor extends MessageProcessor {
   private static final Logger LOGGER = Logger.getLogger(AckRequestedProcessor.class.getName());

   public AckRequestedProcessor(WsrmTubeUtils var1) {
      super(var1);
   }

   public boolean processOutbound(Packet var1, WsrmConstants.RMVersion var2, SequenceIdFactory var3, String var4) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Handling outbound AckRequested, currently no-op");
      }

      return true;
   }

   public void handleInbound(Packet var1, WsrmConstants.RMVersion var2, WsrmTubeUtils.InboundMessageResult var3) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Handling inbound AckRequested, parsing message");
      }

      QName var4 = WsrmConstants.Element.ACK_REQUESTED.getQName(var2);
      Header var5 = var1.getMessage().getHeaders().get(var4, true);
      if (var5 != null) {
         String var6 = null;
         if (this._binding.getAddressingVersion() != null) {
            var6 = var1.getMessage().getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
         }

         AckRequestedHeader var7 = (AckRequestedHeader)WsrmHeaderFactory.getInstance().createWsrmHeaderFromHeader(AckRequestedHeader.class, var5);
         this._tubeUtil.handleAckRequestedHeader(var7, var6);
      }

      var3.message = null;
   }
}
