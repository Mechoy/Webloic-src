package weblogic.wsee.reliability2.tube.processors;

import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.handshake.TerminateSequenceResponseMsg;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.reliability2.sequence.SourceOfferSequence;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;

public class TerminateSequenceResponseProcessor extends MessageProcessor {
   private static final Logger LOGGER = Logger.getLogger(TerminateSequenceResponseProcessor.class.getName());

   public TerminateSequenceResponseProcessor(WsrmTubeUtils var1) {
      super(var1);
   }

   public boolean processOutbound(Packet var1, WsrmConstants.RMVersion var2, SequenceIdFactory var3, String var4) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Processing outbound TerminateSequenceResponse, currently no-op");
      }

      return true;
   }

   public void handleInbound(Packet var1, WsrmConstants.RMVersion var2, WsrmTubeUtils.InboundMessageResult var3) {
      Message var4 = null;
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Handling inbound TerminateSequenceResponse, parsing message");
      }

      Message var5 = var1.getMessage();

      try {
         TerminateSequenceResponseMsg var7 = new TerminateSequenceResponseMsg(var2);
         var7.readMsg(var5.readAsSOAPMessage());
         SourceSequence var6 = (SourceSequence)WsrmTubeUtils.doInitialSetupForAction(WsrmConstants.Element.TERMINATE_SEQUENCE_RESPONSE.getElementName(), true, var7.getSequenceId(), var2, var1);
         var3.seq = var6;
         this._tubeUtil.setInboundSequence(var1, var6);
         internalTerminateSequenceResponse(var6);
      } catch (Exception var8) {
         var4 = WsrmTubeUtils.handleInboundException(var8, this._addrVersion, this._soapVersion);
      }

      var3.message = var4;
   }

   public static void forceAnonymousTerminateSequenceResponse(SourceOfferSequence var0) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Forcing terminate on anonymous source offer sequence " + var0.getId());
      }

      internalTerminateSequenceResponse(var0);
   }

   private static void internalTerminateSequenceResponse(SourceSequence var0) throws WsrmException {
      SequenceState var1 = var0.getState();
      synchronized(var0) {
         if (!var1.isValidTransition(SequenceState.TERMINATED)) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Attempted to illegally change state of sequence " + var0.getId() + " to TERMINATED from " + var1);
            }

            return;
         }

         var0.setState(SequenceState.TERMINATED);
         SourceSequenceManager.getInstance().removeSequence(var0);
      }

      TerminateSequenceProcessor.checkForAnonymousOfferSequenceTermination(var0);
   }
}
