package weblogic.wsee.reliability2.tube.processors;

import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.SOAPException;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.faults.IllegalRMVersionFaultException;
import weblogic.wsee.reliability.handshake.CloseSequenceMsg;
import weblogic.wsee.reliability.handshake.CloseSequenceResponseMsg;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.WsrmHeader;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.compat.Rpc2WsUtil;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.sequence.DestinationSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequenceManager;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;

public class CloseSequenceProcessor extends MessageProcessor {
   private static final Logger LOGGER = Logger.getLogger(CloseSequenceProcessor.class.getName());

   public CloseSequenceProcessor(WsrmTubeUtils var1) {
      super(var1);
   }

   public boolean processOutbound(Packet var1, WsrmConstants.RMVersion var2, SequenceIdFactory var3, String var4) throws WsrmException {
      CloseSequenceMsg var5 = new CloseSequenceMsg(var2);

      try {
         var5.readMsg(var1.getMessage().readAsSOAPMessage());
      } catch (SOAPException var7) {
         throw new WsrmException(var7.toString(), var7);
      }

      SourceSequence var6 = (SourceSequence)WsrmTubeUtils.doInitialSetupForAction(WsrmConstants.Action.CLOSE_SEQUENCE.getActionURI(var2), true, var5.getSequenceId(), var2, (Packet)null);
      if (var2 == WsrmConstants.RMVersion.RM_10) {
         throw new IllegalRMVersionFaultException(var6.getId(), var2, var6.getRmVersion());
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** Processed outbound CloseSequence on source sequence " + var6.getId());
         }

         this._tubeUtil.setOutboundSequence(var1, var6);
         if (var6.setState(SequenceState.CLOSING)) {
            SourceSequenceManager.getInstance().updateSequence(var6);
            return true;
         } else {
            return false;
         }
      }
   }

   public void handleInbound(Packet var1, WsrmConstants.RMVersion var2, WsrmTubeUtils.InboundMessageResult var3) {
      Message var4 = null;
      Message var5 = var1.getMessage();

      try {
         CloseSequenceMsg var7 = new CloseSequenceMsg(var2);
         var7.readMsg(var5.readAsSOAPMessage());
         DestinationSequence var6 = (DestinationSequence)WsrmTubeUtils.doInitialSetupForAction(WsrmConstants.Element.CLOSE_SEQUENCE.getElementName(), false, var7.getSequenceId(), var2, var1);
         var3.seq = var6;
         SequenceState var8 = var6.getState();
         synchronized(var6) {
            if (!var8.isValidTransition(SequenceState.CLOSING)) {
               var3.message = var4;
               return;
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("** Closing destination sequence " + var6.getId());
            }

            var6.setState(SequenceState.CLOSING);
            var6.setState(SequenceState.CLOSED);
            var6.addPiggybackAckHeader();
            DestinationSequenceManager.getInstance().updateSequence(var6);
         }

         CloseSequenceResponseMsg var9 = this.createCloseSequenceResponseMessage(var6);
         var4 = Rpc2WsUtil.createMessageFromHandshakeMessage(var9, this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
         this._tubeUtil.setInboundSequence(var1, var6);
         this._tubeUtil.setOutboundSequence(var1, var6);
         List var10 = var6.getAndClearPiggybackHeaders();

         WsrmHeader var12;
         for(Iterator var11 = var10.iterator(); var11.hasNext(); var4.getHeaders().add(var12)) {
            var12 = (WsrmHeader)var11.next();
            if ("SequenceAcknowledgement".equals(var12.getLocalPart())) {
               ((AcknowledgementHeader)var12).setFinal(true);
            }
         }
      } catch (Exception var14) {
         var4 = WsrmTubeUtils.handleInboundException(var14, this._addrVersion, this._soapVersion);
      }

      var3.message = var4;
   }

   private CloseSequenceResponseMsg createCloseSequenceResponseMessage(DestinationSequence var1) throws WsrmException {
      CloseSequenceResponseMsg var2 = new CloseSequenceResponseMsg(var1.getRmVersion(), var1.getId());
      var2.setMustUnderstand(true);
      return var2;
   }
}
