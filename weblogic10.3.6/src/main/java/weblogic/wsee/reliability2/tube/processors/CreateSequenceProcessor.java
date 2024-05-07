package weblogic.wsee.reliability2.tube.processors;

import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.soap.SOAPException;
import weblogic.kernel.KernelStatus;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.jaxws.cluster.spi.PhysicalStoreNameHeader;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.mc.api.McFeature;
import weblogic.wsee.monitoring.WseeWsrmRuntimeMBeanImpl;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmSecurityContext;
import weblogic.wsee.reliability.faults.CreateSequenceRefusedFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultException;
import weblogic.wsee.reliability.faults.WsrmFaultException;
import weblogic.wsee.reliability.handshake.CreateSequenceMsg;
import weblogic.wsee.reliability.handshake.CreateSequenceResponseMsg;
import weblogic.wsee.reliability.handshake.SequenceAccept;
import weblogic.wsee.reliability.handshake.SequenceOffer;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEvent;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEventListenerRegistry;
import weblogic.wsee.reliability2.compat.Rpc2WsUtil;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.property.WsrmConfig;
import weblogic.wsee.reliability2.sequence.DestinationSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequenceManager;
import weblogic.wsee.reliability2.sequence.DuplicateSequenceException;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.reliability2.sequence.SourceOfferSequence;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.reliability2.store.SenderDispatchFactory;
import weblogic.wsee.reliability2.tube.DispatchFactoryResolver;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;
import weblogic.wsee.security.wssc.sct.SCCredential;

public class CreateSequenceProcessor extends MessageProcessor {
   private static final Logger LOGGER = Logger.getLogger(CreateSequenceProcessor.class.getName());
   private final Object _inboundLock = "CreateSequenceInboundLock";

   public CreateSequenceProcessor(WsrmTubeUtils var1) {
      super(var1);
   }

   public boolean processOutbound(Packet var1, WsrmConstants.RMVersion var2, SequenceIdFactory var3, String var4) throws WsrmException {
      SequenceIdFactory.Info var5 = var3.getSequenceId(var4, false, var1);
      String var6 = var5.id;
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Processing outbound CreateSequence on source sequence " + var5.id);
      }

      WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.CLIENT_CREATE_SEQ);
      SourceSequence var7 = SourceSequenceManager.getInstance().getSequence(var2, var6, false);
      synchronized(var7) {
         String var9 = var1.getMessage().getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
         if (var7.getCreateSequenceMsgId() != null && !var7.getCreateSequenceMsgId().equals(var9)) {
            String var10 = "Trying to send CreateSequence more than once for a single sequence. Msg Id = " + var9 + " originalMsgId = " + var7.getCreateSequenceMsgId() + " seq " + var7;
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine(var10);
            }

            throw new WsrmException(var10);
         }

         if (var7.getCreateSequenceMsgId() == null) {
            var7.setCreateSequenceMsgId(var9);
            SourceSequenceManager.getInstance().updateSequence(var7);
         }
      }

      this._tubeUtil.setOutboundSequenceId(var1, var6);
      return true;
   }

   public void handleInbound(Packet var1, WsrmConstants.RMVersion var2, WsrmTubeUtils.InboundMessageResult var3) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Handling inbound CreateSequence, parsing message");
      }

      WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.RECV_CREATE_SEQ);

      Message var4;
      try {
         Message var5 = var1.getMessage();
         String var6 = this._tubeUtil.getMessageID(var5);
         CreateSequenceMsg var8 = new CreateSequenceMsg(var2);

         try {
            var8.readMsg(var5.readAsSOAPMessage());
         } catch (SOAPException var30) {
            throw new WsrmException(var30.toString(), var30);
         }

         DestinationSequence var7;
         synchronized(this._inboundLock) {
            var7 = (DestinationSequence)DestinationSequenceManager.getInstance().getSequence(var2, var6);
            var3.seq = var7;
            if (var7 != null) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("** Got duplicate CreateSequence for destination sequence " + var7.getId());
               }

               var3.message = this.createCreateSequenceResponse(var7);
               return;
            }

            SourceOfferSequence var10 = null;
            String var11 = var8.getOffer() != null ? var8.getOffer().getSequenceId() : null;
            if (var11 != null) {
               var10 = (SourceOfferSequence)SourceSequenceManager.getInstance().getSequence(var2, var11);
            }

            if (var10 != null) {
               var7 = var10.getMainSequence();
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("** Got duplicate CreateSequence for offer sequence " + var10.getId() + " and destination sequence " + var7.getId());
               }

               if (var7 != null) {
                  var3.message = this.createCreateSequenceResponse(var7);
               } else {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("** Got duplicate CreateSequence for offer sequence " + var10.getId() + " but destination sequence is found null");
                  }

                  String var35 = "Duplicate offer sequence id " + var10.getId() + " detected in the CreateSequence message.";
                  CreateSequenceRefusedFaultMsg var36 = new CreateSequenceRefusedFaultMsg(new Exception(var35), var2);
                  var36.setReason(var35);
                  WsrmFaultException var37 = new WsrmFaultException(var36);
                  var4 = WsrmTubeUtils.handleInboundException(var37, this._addrVersion, this._soapVersion);
                  var3.message = var4;
               }

               return;
            }

            WsrmConfig.Destination var12 = WsrmConfig.getDestination(this._tubeUtil.getServerContext(), var1, false);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("CreateSequenceProcessor: " + var12.toString());
            }

            WsrmConfig.Source var13 = WsrmConfig.getSource((ServerTubeAssemblerContext)this._tubeUtil.getServerContext(), (ClientTubeAssemblerContext)null, var1, true);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Captured RM source config for later use in async responses");
               LOGGER.log(Level.FINE, var13.toString());
            }

            boolean var14 = var12.isNonBufferedDestination();
            if (!var14) {
               WSEndpointReference var15 = var1.getMessage().getHeaders().getReplyTo(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
               boolean var16 = this._binding.isFeatureEnabled(McFeature.class);
               if (var15 != null && !var15.isAnonymous()) {
                  if (var16) {
                     if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("DestinationSequence turning off buffering (even though it was requested via configuration) for CreateSequence with msg ID '" + var6 + "' because McFeature is enabled");
                     }

                     var14 = true;
                  }
               } else {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("DestinationSequence turning off buffering (even though it was requested via configuration) for CreateSequence with msg ID '" + var6 + "' because its ReplyTo was anonymous");
                  }

                  var14 = true;
               }
            }

            boolean var38 = WsrmTubeUtils.isSSLRequest(var1);
            byte[] var39 = WsrmTubeUtils.getSSLSessionId(var1);
            X509Certificate[] var17 = WsrmTubeUtils.getSSLCertChain(var1);
            WsrmSecurityContext var18 = this._tubeUtil.getWsrmSecurityContext(var12.getRmAssertion(), var1);
            if (var18.isSecureWithSSL()) {
               if (KernelStatus.isServer() && !var38) {
                  throw new SequenceFaultException("SSL Required on CreateSequence");
               }

               var18.setSSLSessionId(var39);
               var18.setSSLCertChain(var17);
            } else if (var18.isSecure()) {
               SCCredential var19 = WsrmTubeUtils.getSCCredential(var1);
               var18.setSCCredential(var19);
            }

            var7 = new DestinationSequence((String)null, var12.getPersistenceConfig().getLogicalStoreName(), var2, this._tubeUtil.getBinding().getAddressingVersion(), this._tubeUtil.getBinding().getSOAPVersion(), var18, var14);
            var3.seq = var7;
            DispatchFactoryResolver.ServerSideKey var40 = new DispatchFactoryResolver.ServerSideKey(var1.endpoint.getEndpointId());
            var7.setDispatchKey(var40);

            try {
               var7.setDeliveryAssurance(var12.getDeliveryAssurance());
            } catch (Exception var29) {
               WseeRmLogger.logUnexpectedException(var29.toString(), var29);
            }

            try {
               var7.setIdleTimeout(DatatypeFactory.newInstance().newDuration(var12.getInactivityTimeout()));
               var7.setExpires(DatatypeFactory.newInstance().newDuration(var12.getSequenceExpiration()));
               var7.setAckInterval(DatatypeFactory.newInstance().newDuration(var12.getAcknowledgementInterval()));
            } catch (Exception var28) {
               WseeRmLogger.logUnexpectedException(var28.toString(), var28);
            }

            SourceOfferSequence var20 = this.populateDestinationSequenceFromCreateSequence(var1, var8, var2, var7, var13);
            var18.update(var1);

            try {
               if (var20 != null) {
               }

               var7.setState(SequenceState.CREATING);
               var7.setState(SequenceState.CREATED);
               DestinationSequenceManager.getInstance().addSequence(var7);
               String var21 = var7.getId();
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("** Created NEW destination sequence " + var21 + (var20 != null ? " associated with offer seq " + var20.getId() : ""));
               }

               String var22 = var7.getPhysicalStoreName();
               PhysicalStoreNameHeader var23 = new PhysicalStoreNameHeader(var22);
               HeaderList var24 = new HeaderList();
               var24.add(var23);
               WSEndpointReference var25 = new WSEndpointReference(var7.getHostEpr(), var24);
               var7.setHostEpr(var25);
               DestinationSequenceManager.getInstance().updateSequence(var7);
               if (var20 != null) {
                  var20.setMainSequenceId(var7.getId());
                  var20.takeEprsFromSequence(var7);
                  if (var20.getSecurityContext().isSecureWithSSL()) {
                     var20.getSecurityContext().setSSLSessionId(var39);
                     var20.getSecurityContext().setSSLCertChain(var17);
                  }

                  var20.setState(SequenceState.CREATING);
                  var20.setState(SequenceState.CREATED);
                  SourceSequenceManager.getInstance().addSequence(var20);
               }

               if (var18.isSecureWithSSL()) {
                  var18.setSSLSessionId(var39);
                  var18.setSSLCertChain(var17);
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Stored SSL security: " + WsrmTubeUtils.dumpSecuritySSL(var39, var17) + ") for new sequence: " + var7);
                  }
               } else if (var18.isSecure()) {
                  SCCredential var26 = WsrmTubeUtils.getSCCredential(var1);
                  var18.setSCCredential(var26);
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Stored SCCredential (" + WsrmTubeUtils.dumpSecuritySCT(var26) + ") for new sequence: " + var7);
                  }
               }

               this._tubeUtil.setInboundSequence(var1, var7);
               this._tubeUtil.setOutboundSequence(var1, var7);

               try {
                  WseeWsrmRuntimeMBeanImpl var41 = WsrmTubeUtils.getWsrmRuntimeFromPacket(var1);
                  if (var41 != null) {
                     var41.addSequenceId(var7.getId());
                     if (var7.getOffer() != null) {
                        var41.addSequenceId(var7.getOffer().getId());
                     }
                  }
               } catch (Exception var31) {
                  if (LOGGER.isLoggable(Level.WARNING)) {
                     LOGGER.log(Level.WARNING, var31.toString(), var31);
                  }

                  WseeRmLogger.logUnexpectedException(var31.toString(), var31);
               }
            } catch (DuplicateSequenceException var32) {
               WseeRmLogger.logUnexpectedException(var32.toString(), var32);
            }
         }

         var4 = this.createCreateSequenceResponse(var7);
      } catch (WsrmException var34) {
         var4 = WsrmTubeUtils.handleInboundException(var34, this._addrVersion, this._soapVersion);
      }

      var3.message = var4;
   }

   private Message createCreateSequenceResponse(DestinationSequence var1) throws WsrmException {
      CreateSequenceResponseMsg var2 = new CreateSequenceResponseMsg(var1.getRmVersion());
      var2.setSequenceId(var1.getId());
      var2.setExpires(var1.getExpires());
      var2.setIncompleteSequenceBehavior(var1.getIncompleteSequenceBehavior());
      if (var1.getOffer() != null) {
         SequenceAccept var3 = new SequenceAccept(var1.getRmVersion());
         EndpointReference var4 = WsUtil.createWseeEPRFromWsEPR(var1.getOffer().getAcksToEpr());
         var3.setAcksTo(var4);
         var2.setAccept(var3);
      }

      try {
         Message var6 = Rpc2WsUtil.createMessageFromHandshakeMessage(var2, this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
         return var6;
      } catch (SOAPException var5) {
         throw new WsrmException(var5.toString(), var5);
      }
   }

   private SourceOfferSequence populateDestinationSequenceFromCreateSequence(Packet var1, CreateSequenceMsg var2, WsrmConstants.RMVersion var3, DestinationSequence var4, WsrmConfig.Source var5) throws WsrmException {
      Message var6 = var1.getMessage();
      String var7 = var6.getHeaders().getMessageID(this._addrVersion, this._soapVersion);
      var4.setCreateSequenceMsgId(var7);
      EndpointReference var8 = var2.getAcksTo();
      WSEndpointReference var9 = WsUtil.createWsEPRFromWseeEPR(var8);
      var4.setAcksToEpr(var9);
      WSEndpointReference var10 = WsrmTubeUtils.getEndpointReferenceFromIncomingPacket(var1, this._tubeUtil.getEndpoint());
      var4.setHostEpr(var10);
      Duration var11 = var2.getExpires();
      if (var11 != null) {
         var4.setExpires(var11);
      }

      if (var4.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
         var4.setIncompleteSequenceBehavior(WsrmConstants.IncompleteSequenceBehavior.NoDiscard);
      }

      SourceOfferSequence var12 = null;
      if (var2.getOffer() != null) {
         SequenceOffer var13 = var2.getOffer();
         WsrmSecurityContext var14 = this._tubeUtil.getWsrmSecurityContext(var5.getRmAssertion(), var1);
         boolean var15 = var5.isNonBufferedSource();
         WSEndpointReference var16;
         if (var13.getEndpoint() != null) {
            EndpointReference var17 = var13.getEndpoint();
            var16 = WsUtil.createWsEPRFromWseeEPR(var17);
         } else {
            var16 = var6.getHeaders().getReplyTo(this._addrVersion, this._soapVersion);
         }

         if (var16 == null || var16.isAnonymous()) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Bypassing buffering on offer sequence " + var13.getSequenceId() + " because it has a null endpoint/to address");
            }

            var15 = true;
         }

         var12 = new SourceOfferSequence(var13.getSequenceId(), var5.getPersistenceConfig().getLogicalStoreName(), var3, var4.getAddressingVersion(), var4.getSoapVersion(), var14, var4, var15);
         var12.setEndpointEpr(var16);
         var12.takeEprsFromSequence(var4);
         var12.setDeliveryAssurance(var5.getDeliveryAssurance());

         try {
            var12.setIdleTimeout(DatatypeFactory.newInstance().newDuration(var5.getInactivityTimeout()));
            var12.setExpires(DatatypeFactory.newInstance().newDuration(var5.getSequenceExpiration()));
            var12.setBaseRetransmissionInterval(DatatypeFactory.newInstance().newDuration(var5.getBaseRetransmissionInterval()));
            var12.setExponentialBackoffEnabled(var5.getRetransmissionExponentialBackoff());
         } catch (Exception var19) {
            WseeRmLogger.logUnexpectedException(var19.toString(), var19);
         }

         if (var13.getExpires() != null) {
            try {
               Duration var20 = DatatypeFactory.newInstance().newDuration(var13.getExpires());
               var12.setExpires(var20);
            } catch (Exception var18) {
               throw new WsrmException(var18.toString(), var18);
            }
         }

         if (var13.getIncompleteSequenceBehavior() != null) {
            var12.setIncompleteSequenceBehavior(var13.getIncompleteSequenceBehavior());
         }

         SenderDispatchFactory.ServerSideKey var21 = new SenderDispatchFactory.ServerSideKey(var1.endpoint.getEndpointId());
         var12.setSenderDispatchKey(var21);
         var4.setOffer(var12);
      }

      return var12;
   }
}
