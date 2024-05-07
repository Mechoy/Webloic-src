package weblogic.wsee.reliability2.tube.processors;

import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.SOAPException;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.handshake.TerminateSequenceMsg;
import weblogic.wsee.reliability.handshake.TerminateSequenceResponseMsg;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.compat.Rpc2WsUtil;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.sequence.DestinationOfferSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequenceManager;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.reliability2.sequence.SourceOfferSequence;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.reliability2.sequence.UnknownSequenceException;
import weblogic.wsee.reliability2.store.SenderDispatchFactory;
import weblogic.wsee.reliability2.tube.DispatchFactory;
import weblogic.wsee.reliability2.tube.Sender;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;

public class TerminateSequenceProcessor extends MessageProcessor {
   private static final Logger LOGGER = Logger.getLogger(TerminateSequenceProcessor.class.getName());

   public TerminateSequenceProcessor(WsrmTubeUtils var1) {
      super(var1);
   }

   public boolean processOutbound(Packet var1, WsrmConstants.RMVersion var2, SequenceIdFactory var3, String var4) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Processing outbound TerminateSequence, parsing message");
      }

      TerminateSequenceMsg var5 = new TerminateSequenceMsg(var2);

      try {
         var5.readMsg(var1.getMessage().readAsSOAPMessage());
      } catch (SOAPException var8) {
         throw new WsrmException(var8.toString(), var8);
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Processing outbound TerminateSequence for source seq: " + var5.getSequenceId());
      }

      SourceSequence var6;
      try {
         var6 = (SourceSequence)WsrmTubeUtils.doInitialSetupForAction(WsrmConstants.Action.TERMINATE_SEQUENCE.getActionURI(var2), true, var5.getSequenceId(), var2, (Packet)null);
      } catch (UnknownSequenceException var9) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Aborting outbound TerminateSequence message for seq " + var5.getSequenceId() + " due to: " + var9.toString());
         }

         return false;
      }

      if (var6.getOffer() != null) {
         DestinationOfferSequence var7 = var6.getOffer();
         var7.forceDeliverPendingAcks();
      }

      boolean var10;
      if (var2.isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
         var10 = var6.setState(SequenceState.TERMINATING);
         if (var10) {
            SourceSequenceManager.getInstance().updateSequence(var6);
         }
      } else {
         var6.setState(SequenceState.TERMINATING);
         var10 = var6.setState(SequenceState.TERMINATED);
         if (var10) {
            SourceSequenceManager.getInstance().removeSequence(var6);
         }
      }

      this._tubeUtil.setOutboundSequence(var1, var6);
      this._tubeUtil.removeClientCurrentSequenceId(var6.getCreatingClientInstanceId());
      return var10;
   }

   public static void checkForAnonymousOfferSequenceTermination(SourceSequence var0) throws WsrmException {
      if (var0.getOffer() != null) {
         DestinationOfferSequence var1 = var0.getOffer();
         if (var0.getAcksToEpr() == null || var0.getAcksToEpr().isAnonymous()) {
            forceAnonymousOfferTerminateSequence(var1, var1.getRmVersion(), var1.getMaxMessageNum());
            var0.setOffer((DestinationOfferSequence)null);
         }

      }
   }

   public void handleInbound(Packet var1, WsrmConstants.RMVersion var2, WsrmTubeUtils.InboundMessageResult var3) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Handling inbound TerminateSequence, parsing message");
      }

      Message var4 = null;
      Message var5 = var1.getMessage();

      try {
         TerminateSequenceMsg var7 = new TerminateSequenceMsg(var2);
         var7.readMsg(var5.readAsSOAPMessage());

         DestinationSequence var6;
         try {
            var6 = (DestinationSequence)WsrmTubeUtils.doInitialSetupForAction(WsrmConstants.Element.TERMINATE_SEQUENCE.getElementName(), false, var7.getSequenceId(), var2, (Packet)null);
         } catch (UnknownSequenceException var12) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Ignoring TerminateSequence request for " + var7.getSequenceId() + " because it couldn't be found");
            }

            var3.handled = true;
            return;
         }

         var3.seq = var6;
         internalTerminateSequence(var6, var2, var7.getLastMsgNumber());
         this._tubeUtil.setInboundSequence(var1, var6);
         if (var6.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
            TerminateSequenceResponseMsg var8 = this.createTerminateSequenceResponseMessage(var6);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Creating outbound response TerminateSequenceResponse for sequence " + var6 + ": " + var6.getId());
            }

            var4 = Rpc2WsUtil.createMessageFromHandshakeMessage(var8, this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
            this._tubeUtil.setOutboundSequence(var1, var6);
            var6.addFinalAckToMessage(var4);
         } else if (var6.getOffer() != null && (var6.getOffer().getEndpointEpr() == null || var6.getOffer().getEndpointEpr().isAnonymous())) {
            SourceOfferSequence var14 = var6.getOffer();
            TerminateSequenceMsg var9 = WsrmTubeUtils.createTerminateSequenceMessage(var14);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Creating outbound response TerminateSequence for sequence " + var14 + ": " + var14.getId());
            }

            try {
               var4 = Rpc2WsUtil.createMessageFromHandshakeMessage(var9, var6.getAddressingVersion(), var6.getSoapVersion());
            } catch (Exception var11) {
               throw new WsrmException(var11.toString(), var11);
            }

            this._tubeUtil.setOutboundSequence(var1, var14);
         }
      } catch (Exception var13) {
         var4 = WsrmTubeUtils.handleInboundException(var13, this._addrVersion, this._soapVersion);
      }

      var3.message = var4;
   }

   private TerminateSequenceResponseMsg createTerminateSequenceResponseMessage(DestinationSequence var1) throws WsrmException {
      TerminateSequenceResponseMsg var2 = new TerminateSequenceResponseMsg(var1.getRmVersion(), var1.getId());
      var2.setMustUnderstand(true);
      return var2;
   }

   public static void forceAnonymousOfferTerminateSequence(DestinationOfferSequence var0, WsrmConstants.RMVersion var1, long var2) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** FORCING terminate on anonymous destination offer sequence " + var0.getId());
      }

      internalTerminateSequence(var0, var1, var2);
   }

   public static void forceTerminateSequence(DestinationSequence var0, WsrmConstants.RMVersion var1, long var2) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** FORCING terminate on destination sequence " + var0.getId());
      }

      internalTerminateSequence(var0, var1, var2);
   }

   private static void internalTerminateSequence(DestinationSequence var0, WsrmConstants.RMVersion var1, long var2) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Terminating destination sequence " + var0.getId());
      }

      boolean var4 = false;

      try {
         SequenceState var5 = var0.getState();
         synchronized(var0) {
            if (var5.isValidTransition(SequenceState.TERMINATING)) {
               var4 = true;
               var0.setState(SequenceState.TERMINATING);
               if (var1.isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
                  var0.setFinalMessageNum(var2);
               }

               if (var0.getOffer() != null) {
                  SourceOfferSequence var7 = var0.getOffer();
                  if (var0.hasFinalMessageNum()) {
                     long var8 = var0.calculateFinalResponseMessageNum();
                     if (var8 >= 0L) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                           LOGGER.fine("*** Final message on request sequence " + var0.getId() + " with sequence number: " + var0.getFinalMessageNum() + " was mapped to offer sequence " + var7.getId() + " sequence number " + var8 + ". Enabling auto-terminate.");
                        }

                        enableAutoTerminateForOfferSequence(var7, var8);
                     } else if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("*** Final message on request sequence " + var0.getId() + " with sequence number: " + var0.getFinalMessageNum() + " could not be mapped to a sequence number on offer sequence " + var7.getId());
                     }
                  }
               }

               var0.setState(SequenceState.TERMINATED);
               DestinationSequenceManager.getInstance().updateSequence(var0);
               var4 = false;
               DestinationSequenceManager.getInstance().removeSequence(var0);
               if (var0 instanceof DestinationOfferSequence) {
                  SourceSequence var17 = ((DestinationOfferSequence)var0).getMainSequence();
                  if (var17 != null) {
                     var17.setOffer((DestinationOfferSequence)null);
                     return;
                  }
               }

               return;
            }
         }
      } finally {
         if (var4) {
            var0.setState(SequenceState.CREATED);
            DestinationSequenceManager.getInstance().updateSequence(var0);
         }

      }

   }

   private static void enableAutoTerminateForOfferSequence(SourceOfferSequence var0, long var1) throws WsrmException {
      if (var0.getRmVersion() == WsrmConstants.RMVersion.RM_10) {
         if (!var0.hasFinalMessageNum()) {
            sendEmptyLastMessage(var0);
         } else {
            setSentFinalMessage(var0, var0.getFinalMessageNum());
         }
      } else {
         setSentFinalMessage(var0, var1);
      }

   }

   public static void sendEmptyLastMessage(SourceSequence var0) throws WsrmException {
      WSEndpointReference var1 = var0.getEndpointEpr();
      if (var1.isAnonymous()) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** Bypassing requested send of empty last message on anonymous source sequence " + var0.getId());
         }

      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** Sending empty last message on source sequence " + var0.getId());
         }

         String var3 = WsrmConstants.Action.LAST_MESSAGE.getActionURI(var0.getRmVersion());

         Message var2;
         try {
            var2 = WsUtil.createEmptyMessage(var3, var0.getAddressingVersion(), var0.getSoapVersion());
         } catch (Exception var13) {
            var2 = WsUtil.createMessageFromThrowable(var13, var0.getAddressingVersion(), var0.getSoapVersion());
         }

         SequenceHeader var4 = new SequenceHeader(var0.getRmVersion(), var0.getDestinationId(), 0L, true);
         long var5 = var0.getNextMessageNum(var4);
         var2.getHeaders().add(var4);
         var0.setFinalMessageNum(var5);
         SenderDispatchFactory.Key var7 = var0.getSenderDispatchKey();
         if (var7 == null) {
            WsrmException var14 = new WsrmException("No dispatch key set for sequence '" + var0 + "' cannot send empty last message");
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.warning(var14.toString());
            }

            throw var14;
         } else {
            DispatchFactory var8 = var7.resolve();
            Sender var9 = new Sender(var8);
            WSEndpointReference var10 = WsrmTubeUtils.calculateReplyToForSourceSequence(var0);

            try {
               Map var11 = SourceSequenceManager.getInstance().getSenderInvokeProperties((SourceSequence)var0, (Packet)null);
               var11.put("weblogic.wsee.reliability2.FinalMessageFlag", true);
               var9.send(var2, var3, var1, var10, var11);
            } catch (Exception var12) {
               throw new WsrmException(var12.toString(), var12);
            }
         }
      }
   }

   public static void setSentFinalMessage(SourceSequence var0, long var1) throws WsrmException {
      var0.setFinalMessageNum(var1);
      checkForSequenceCompletion(var0);
   }

   public static void checkForSequenceCompletion(SourceSequence var0) throws WsrmException {
      if (var0.hasFinalMessageNum()) {
         boolean var1 = SourceSequenceManager.getInstance().syncUpWithAcks(var0);
         if (var1) {
            SourceSequenceManager.getInstance().updateSequence(var0);
         }

         if (var0.isComplete()) {
            DispatchFactory var2 = var0.getSenderDispatchKey().resolve();
            WsrmTubeUtils.sendTerminateSequence(var0, var2);
         }
      }

   }
}
