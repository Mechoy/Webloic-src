package weblogic.wsee.reliability2.tube.processors;

import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.SOAPException;
import weblogic.kernel.Kernel;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.faults.SequenceFaultException;
import weblogic.wsee.reliability.handshake.CreateSequenceResponseMsg;
import weblogic.wsee.reliability.handshake.SequenceAccept;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEvent;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEventListenerRegistry;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.sequence.DestinationOfferSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequenceManager;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.reliability2.sequence.SourceOfferSequence;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;

public class CreateSequenceResponseProcessor extends MessageProcessor {
   private static final Logger LOGGER = Logger.getLogger(CreateSequenceResponseProcessor.class.getName());

   public CreateSequenceResponseProcessor(WsrmTubeUtils var1) {
      super(var1);
   }

   public boolean processOutbound(Packet var1, WsrmConstants.RMVersion var2, SequenceIdFactory var3, String var4) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Processing outbound CreateSequenceResponse");
      }

      return true;
   }

   public void handleInbound(Packet var1, WsrmConstants.RMVersion var2, WsrmTubeUtils.InboundMessageResult var3) {
      Message var4 = null;
      Message var5 = var1.getMessage();
      WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.CLIENT_CREATE_SEQ_RES);

      try {
         String var6 = var5.getHeaders().getRelatesTo(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
         SourceSequence var7 = SourceSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), var6, false);
         String var8 = var7.getId();
         var7 = (SourceSequence)WsrmTubeUtils.doInitialSetupForAction(WsrmConstants.Element.CREATE_SEQUENCE_RESPONSE.getElementName(), true, var8, var2, (Packet)null);
         var3.seq = var7;
         byte[] var9 = WsrmTubeUtils.getSSLSessionId(var1);
         X509Certificate[] var10 = WsrmTubeUtils.getSSLCertChain(var1);
         if (var7.getSecurityContext().isSecureWithSSL()) {
            if (Kernel.isServer() && var9 == null) {
               throw new SequenceFaultException("SSL Required on CreateSequenceResponse for seq: " + var7.getId());
            }
         } else if (var7.getSecurityContext().isSecure()) {
            WsrmTubeUtils.validateSecurityOnInboundPacket(var7, var1);
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** Completing handshake with CreateSequenceResponse on source sequence " + var7.getId());
         }

         synchronized(var7) {
            boolean var12 = var7 instanceof SourceOfferSequence && ((SourceOfferSequence)var7).isHandshaked() || !(var7 instanceof SourceOfferSequence);
            if (var12 && var7.getDestinationId() != null) {
               var3.message = var4;
               return;
            }

            this.populateSourceSequenceFromCreateSequenceResponse(var5, var2, var7);
            if (var7.getSecurityContext().isSecureWithSSL()) {
               var7.getSecurityContext().setSSLSessionId(var9);
               var7.getSecurityContext().setSSLCertChain(var10);
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("** Populated/created source sequence. Source ID: " + var7.getId() + " Dest ID: " + var7.getDestinationId());
            }

            if (var7.getOffer() != null) {
               if (var7.getOffer().getSecurityContext().isSecureWithSSL()) {
                  var7.getOffer().getSecurityContext().setSSLSessionId(var9);
                  var7.getOffer().getSecurityContext().setSSLCertChain(var10);
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Updated SSLSessionID for offer seq '" + var7.getOffer().getId() + "' to: " + WsrmTubeUtils.dumpSSLSessionId(var9));
                  }
               }

               var7.getOffer().setState(SequenceState.CREATING);
               var7.getOffer().setState(SequenceState.CREATED);
               DestinationSequenceManager.getInstance().updateSequence((DestinationSequence)var7.getOffer());
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("** Populated/created source sequence with offer. Source ID: " + var7.getId() + " Dest ID: " + var7.getDestinationId() + " Offer ID: " + var7.getOffer().getId());
               }
            }

            SourceSequenceManager.getInstance().updateSequence(var7);
            var7.setState(SequenceState.CREATED);
            SourceSequenceManager.getInstance().updateSequence(var7);
            WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.CLIENT_SEQUENCE_CREATED);
         }
      } catch (WsrmException var15) {
         var4 = WsrmTubeUtils.handleInboundException(var15, this._addrVersion, this._soapVersion);
      }

      var3.message = var4;
   }

   private void populateSourceSequenceFromCreateSequenceResponse(Message var1, WsrmConstants.RMVersion var2, SourceSequence var3) throws WsrmException {
      Header var4 = var1.getHeaders().get(var3.getAddressingVersion().fromTag, false);
      if (var4 != null) {
         try {
            WSEndpointReference var5 = var4.readAsEPR(var3.getAddressingVersion());
            var3.setEndpointEpr(var5);
         } catch (Exception var10) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, "Unable to read From header on CreateSequenceResponse: " + var10.toString(), var10);
            }
         }
      }

      CreateSequenceResponseMsg var11 = new CreateSequenceResponseMsg(var2);

      try {
         var11.readMsg(var1.readAsSOAPMessage());
      } catch (SOAPException var9) {
         throw new WsrmException(var9.toString(), var9);
      }

      var3.setDestinationId(var11.getSequenceId());
      if (var11.getExpires() != null) {
         var3.setExpires(var11.getExpires());
      }

      if (var11.getIncompleteSequenceBehavior() != null) {
         var11.setIncompleteSequenceBehavior(var11.getIncompleteSequenceBehavior());
      }

      if (var11.getAccept() != null) {
         SequenceAccept var6 = var11.getAccept();
         DestinationOfferSequence var7 = var3.getOffer();
         if (var7 == null) {
            throw new WsrmException(WseeRmLogger.logGotAcceptWithNoOfferLoggable(var3.getDestinationId()).getMessage());
         }

         WSEndpointReference var8 = WsUtil.createWsEPRFromWseeEPR(var6.getAcksTo());
         var7.setAcksToEpr(var8);
      } else {
         var3.setOffer((DestinationOfferSequence)null);
      }

   }
}
