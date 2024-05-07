package weblogic.wsee.reliability2.store;

import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.Fiber;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Response;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.soap.SOAPFaultException;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.persistence.PersistentMessage;
import weblogic.wsee.jaxws.persistence.PersistentMessageFactory;
import weblogic.wsee.jaxws.tubeline.TubelineSpliceFactory;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability2.api.ReliabilityErrorContext;
import weblogic.wsee.reliability2.api.ReliabilityErrorListener;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEvent;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEventListenerRegistry;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.exception.WsrmExceptionUtil;
import weblogic.wsee.reliability2.headers.WsrmHeaderFactory;
import weblogic.wsee.reliability2.property.WsrmPropertyBag;
import weblogic.wsee.reliability2.sequence.SourceMessageInfo;
import weblogic.wsee.reliability2.sequence.SourceOfferSequence;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.reliability2.tube.DispatchFactory;
import weblogic.wsee.reliability2.tube.DispatchFactoryNotReadyException;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;
import weblogic.wsee.sender.api.ConversationStatusCallback;
import weblogic.wsee.sender.api.ConversationStatusCallbackNotReadyException;
import weblogic.wsee.sender.api.PermanentSendException;
import weblogic.wsee.sender.api.SendException;
import weblogic.wsee.sender.api.SendRequest;
import weblogic.wsee.sender.api.Sender;
import weblogic.wsee.sender.api.SenderNotReadyException;
import weblogic.wsee.sender.api.SendingServiceException;
import weblogic.wsee.util.AccessException;

public class SourceSequenceSender implements Sender {
   private static final Logger LOGGER = Logger.getLogger(SourceSequenceSender.class.getName());
   private SourceSequenceSenderFactory _factory;
   private SourceSequence _seq;
   private Sender.ConversationCallback _callback;
   private ConversationStatusCallback _statusCallback;
   private int _skipCreateSequenceSendCount = 0;

   public SourceSequenceSender(SourceSequenceSenderFactory var1, SourceSequence var2) {
      if (var2 == null) {
         throw new IllegalStateException("Null sequence passed to SourceSequenceSender!!");
      } else {
         this._factory = var1;
         this._seq = var2;
         this._statusCallback = new StatusCallback();
      }
   }

   public void setConversationCallback(Sender.ConversationCallback var1) {
      this.checkClosed();
      this._callback = var1;
   }

   public ConversationStatusCallback getConversationStatusCallback() {
      this.checkClosed();
      return this._statusCallback;
   }

   private void checkClosed() {
      if (this._seq == null) {
         throw new IllegalStateException("Attempt to use closed SourceSequenceSender!!");
      }
   }

   void notifySequenceCreated() {
      this._callback.conversationReady();
   }

   void notifySequenceClosed() {
   }

   public synchronized Sender.SendResult send(SendRequest var1) throws SendException {
      this.checkClosed();

      try {
         boolean var3 = this._seq instanceof SourceOfferSequence;
         TubelineSpliceFactory.DispatchFactory var4 = this.getSenderDispatchKey().resolveForSplice();
         DispatchFactory var5 = this.getSenderDispatchKey().resolve();
         this.checkSequenceCreated(var5);
         if (var3) {
            String var6 = this._seq.getDestinationId();
            if (var6 != null) {
               System.setProperty("pseudo.offer.seq.id.for.testing", var6);
            }
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Sending request " + var1 + " on sender conversation name: " + this._seq.getId());
         }

         if (var3) {
            WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.SERV_OUT_BEFORE_SEND_RESPONSE);
         } else {
            WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.CLIENT_OUT_BEFORE_SEND_REQUEST);
         }

         PersistentMessage var12 = (PersistentMessage)var1.getPayload();
         Packet var7 = WsrmTubeUtils.createPacketFromPersistentMessage(var12, this._seq.getAddressingVersion(), this._seq.getSoapVersion());
         WsrmPropertyBag var8 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var7);
         this.updatePacket(this._seq, var8, var7);
         Sender.SendResult var2 = this.sendRequestAndHandleResponse(var1, var3, var4, var12, var7);
         return var2;
      } catch (SenderNotReadyException var9) {
         throw var9;
      } catch (SendException var10) {
         throw var10;
      } catch (Exception var11) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, var11.toString(), var11);
         }

         throw new SendException(var11.toString(), var11);
      }
   }

   private Sender.SendResult sendRequestAndHandleResponse(SendRequest var1, boolean var2, TubelineSpliceFactory.DispatchFactory var3, PersistentMessage var4, Packet var5) throws SendException {
      Sender.SendResult var6 = Sender.SendResult.SUCCESS;
      Dispatch var7;
      if (var2) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Getting RESPONSE Dispatch to dispatch response message with msgId " + var1.getMessageId() + " msgNum " + var1.getSequenceNumber() + " conversation " + var1.getConversationName());
         }

         var7 = var3.createResponseDispatch(this._seq.getEndpointEpr(), Packet.class, Mode.MESSAGE);
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Getting Dispatch to dispatch message with msgId " + var1.getMessageId() + " msgNum " + var1.getSequenceNumber() + " conversation " + var1.getConversationName());
         }

         TubelineSpliceFactory.ClientDispatchFactory var8 = (TubelineSpliceFactory.ClientDispatchFactory)var3;
         var7 = var8.createPostSpliceDispatch(this._seq.getEndpointEpr(), Packet.class, Mode.MESSAGE);
      }

      try {
         WsrmTubeUtils.addPiggybackHeaders(var5, this._seq.getEndpointEpr().getAddress(), this._seq.getAddressingVersion(), this._seq.getSoapVersion());
      } catch (Exception var12) {
         throw new SendException(var12.toString(), var12);
      }

      weblogic.wsee.reliability2.tube.Sender.setInvokePropertiesOntoDispatch(SourceSequenceManager.getInstance().getSenderInvokeProperties(this._seq, var5), var7);
      var7.getRequestContext().put("weblogic.wsee.jaxws.async.PersistentContext", var4.getContext().getContextPropertyMap());
      String var13 = var5.getMessage().getHeaders().getAction(this._seq.getAddressingVersion(), this._seq.getSoapVersion());
      var5.soapAction = var13;
      var7.getRequestContext().put("javax.xml.ws.soap.http.soapaction.uri", var13);
      SourceSequenceSendRequest var9 = (SourceSequenceSendRequest)var1;
      SourceMessageInfo var10 = var9.getMsgInfo();
      SourceMessageInfo.ClientInvokeInfo var11 = var10.getClientInvokeInfo();
      if (!var2 && var11 != null && var11.impliesSuspendedRequestFiber()) {
         var6 = this.sendRequestAndHandleResponseForSuspendedFiber(var1, var5, var9, var10, var11, var7);
      } else if (!var2) {
         var6 = this.sendRequestAndHandleResponseForAsyncClientHandler(var1, var5, var9, var7, var6);
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Dispatching one-way response msgId " + var1.getMessageId() + " msgNum " + var1.getSequenceNumber() + " conversation " + var1.getConversationName());
         }

         var6 = this.dispatchServiceSideResponseAndCaptureErrors(var7, var9.getMsgInfo(), var5);
      }

      return var6;
   }

   private Sender.SendResult sendRequestAndHandleResponseForAsyncClientHandler(SendRequest var1, Packet var2, final SourceSequenceSendRequest var3, Dispatch<Packet> var4, Sender.SendResult var5) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Sending A-sync (AsyncClientHandlerFeature) msgId " + var1.getMessageId() + " msgNum " + var1.getSequenceNumber() + " conversation " + var1.getConversationName());
      }

      try {
         var4.getRequestContext().put("weblogic.wsee.jaxws.client.async.SendErrorOnlyAsyncHandler", Boolean.TRUE);
         var4.invokeAsync(var2, new AsyncHandler<Packet>() {
            public void handleResponse(Response<Packet> var1) {
               try {
                  Packet var4 = (Packet)var1.get();
                  if (var4 != null && var4.getMessage() != null) {
                     SourceSequenceSender.this.dispatchAsyncTransportResponse(var4);
                  }

                  SourceSequenceSender.this._callback.sendSucceeded(var3.getSequenceNumber());
               } catch (Throwable var3x) {
                  Throwable var2 = SourceSequenceSender.checkForPermanentSendFailure(var3x);
                  SourceSequenceSender.this._callback.sendFailed(var3.getMsgInfo().getMessageNum(), var2);
               }

            }
         });
         var5 = Sender.SendResult.IN_PROCESS;
      } catch (Throwable var7) {
         Throwable var6 = checkForPermanentSendFailure(var7);
         this._callback.sendFailed(var3.getMsgInfo().getMessageNum(), var6);
      }

      return var5;
   }

   private void dispatchAsyncTransportResponse(Packet var1) throws PermanentSendException {
      final String var2 = var1.getMessage().getHeaders().getMessageID(this._seq.getAddressingVersion(), this._seq.getSoapVersion());
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Dispatching MC Transport response for msg ID: " + var2);
      }

      TubelineSpliceFactory.DispatchFactory var3 = this.getSenderDispatchKey().resolveForSplice();
      Dispatch var4 = var3.createResponseDispatch(this._seq.getEndpointEpr(), Packet.class, Mode.MESSAGE);
      this.copyPacketPropsToDispatch(var4, var1);
      var4.invokeAsync(var1, new AsyncHandler<Packet>() {
         public void handleResponse(Response<Packet> var1) {
            if (SourceSequenceSender.LOGGER.isLoggable(Level.FINE)) {
               SourceSequenceSender.LOGGER.fine("Got indication that pre-splice portion of response processing is complete for msg ID: " + var2);
            }

         }
      });
   }

   private Sender.SendResult sendRequestAndHandleResponseForSuspendedFiber(final SendRequest var1, Packet var2, final SourceSequenceSendRequest var3, SourceMessageInfo var4, SourceMessageInfo.ClientInvokeInfo var5, Dispatch<Packet> var6) throws PermanentSendException {
      if (var5.getSuspendedRequestFiber() == null) {
         throw new PermanentSendException("Attempt to send a request from a suspended fiber AFTER having received a response or permanent failure for that request. The msgId is " + var4.getMessageId());
      } else {
         Sender.SendResult var7;
         Packet var8;
         if (var5.isSyncMEP() && !var5.isOneWay()) {
            try {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Sending SYNC msgId " + var1.getMessageId() + " msgNum " + var1.getSequenceNumber() + " conversation " + var1.getConversationName());
               }

               var8 = (Packet)var6.invoke(var2);
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Received SYNC response for request msgId " + var1.getMessageId() + " msgNum " + var1.getSequenceNumber() + " conversation " + var1.getConversationName());
               }

               this.handleResponseForSuspendedFiber(var3.getMsgInfo(), var8);
               var7 = Sender.SendResult.SUCCESS;
            } catch (Throwable var10) {
               this.handleFailureForSuspendedFiber(var3.getMsgInfo(), var10);
               var7 = Sender.SendResult.FAILURE;
            }
         } else if (var5.isOneWay()) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Sending OneWay msgId " + var1.getMessageId() + " msgNum " + var1.getSequenceNumber() + " conversation " + var1.getConversationName());
            }

            try {
               var6.invokeOneWay(var2);
               var8 = var2.createClientResponse((Message)null);
               this.handleResponseForSuspendedFiber(var3.getMsgInfo(), var8);
               var7 = Sender.SendResult.SUCCESS;
            } catch (Throwable var9) {
               this.handleFailureForSuspendedFiber(var3.getMsgInfo(), var9);
               var7 = Sender.SendResult.FAILURE;
            }
         } else {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Sending A-sync w/AsyncHandler(AsyncHandler) msgId " + var1.getMessageId() + " msgNum " + var1.getSequenceNumber() + " conversation " + var1.getConversationName());
            }

            var6.invokeAsync(var2, new AsyncHandler<Packet>() {
               public void handleResponse(Response<Packet> var1x) {
                  SourceSequenceSender.this._callback.sendSucceeded(var3.getSequenceNumber());

                  try {
                     Packet var2 = (Packet)var1x.get();
                     if (SourceSequenceSender.LOGGER.isLoggable(Level.FINE)) {
                        SourceSequenceSender.LOGGER.fine("Received AsyncHandler(AsyncHandler) response for request msgId " + var1.getMessageId() + " msgNum " + var1.getSequenceNumber() + " conversation " + var1.getConversationName());
                     }

                     SourceSequenceSender.this.handleResponseForSuspendedFiber(var3.getMsgInfo(), var2);
                  } catch (Throwable var3x) {
                     SourceSequenceSender.this.handleFailureForSuspendedFiber(var3.getMsgInfo(), var3x);
                  }

               }
            });
            var7 = Sender.SendResult.IN_PROCESS;
         }

         return var7;
      }
   }

   private Sender.SendResult dispatchServiceSideResponseAndCaptureErrors(Dispatch<Packet> var1, final SourceMessageInfo var2, Packet var3) {
      boolean var4 = !((SourceOfferSequence)this._seq).isMainSequenceNonBuffered() && ((SourceOfferSequence)this._seq).getMainSequenceDeliveryAssurance().isInOrder();
      this.copyPacketPropsToDispatch(var1, var3);
      if (var4) {
         try {
            Response var5 = var1.invokeAsync(var3);
            Packet var6 = (Packet)var5.get();
            this.captureServiceSideResponseErrors(var2.getMessageNum(), var6, (Throwable)null);
            return Sender.SendResult.SUCCESS;
         } catch (Throwable var7) {
            this.captureServiceSideResponseErrors(var2.getMessageNum(), (Packet)null, var7);
            return Sender.SendResult.FAILURE;
         }
      } else {
         var1.invokeAsync(var3, new AsyncHandler<Packet>() {
            public void handleResponse(Response<Packet> var1) {
               SourceSequenceSender.this._callback.sendSucceeded(var2.getMessageNum());

               try {
                  Packet var2x = (Packet)var1.get();
                  SourceSequenceSender.this.captureServiceSideResponseErrors(var2.getMessageNum(), var2x, (Throwable)null);
               } catch (Throwable var3) {
                  SourceSequenceSender.this.captureServiceSideResponseErrors(var2.getMessageNum(), (Packet)null, var3);
               }

            }
         });
         return Sender.SendResult.IN_PROCESS;
      }
   }

   private void captureServiceSideResponseErrors(long var1, Packet var3, Throwable var4) {
      if (var3 != null && var3.getMessage() != null && var3.getMessage().isFault()) {
         try {
            SOAPMessage var5 = var3.getMessage().readAsSOAPMessage();
            SOAPBody var6 = var5.getSOAPBody();
            SOAPFault var7 = var6.getFault();
            var4 = new SOAPFaultException(var7);
         } catch (Exception var8) {
            WseeRmLogger.logUnexpectedException(var8.toString(), var8);
         }
      }

      if (var4 != null) {
         Throwable var9 = checkForPermanentSendFailure((Throwable)var4);
         this._callback.sendFailed(var1, var9);
      }

   }

   public static Throwable checkForPermanentSendFailure(Throwable var0) {
      if (var0 instanceof PermanentSendException) {
         return (Throwable)var0;
      } else {
         if (WsrmExceptionUtil.isPermanentSendFailure((Throwable)var0)) {
            var0 = new PermanentSendException(((Throwable)var0).toString(), (Throwable)var0);
         }

         return (Throwable)var0;
      }
   }

   private void copyPacketPropsToDispatch(Dispatch<Packet> var1, Packet var2) {
      var1.getRequestContext().putAll(var2.invocationProperties);
      var1.getRequestContext().put("weblogic.wsee.jaxws.async.PersistentContext", var2.persistentContext);
   }

   private void handleResponseForSuspendedFiber(SourceMessageInfo var1, Packet var2) {
      WsrmPropertyBag var3 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var2);
      var3.setResponseToSuspendedFiber(true);
      SourceMessageInfo.ClientInvokeInfo var4 = var1.getClientInvokeInfo();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Handling response for suspended fiber on msgId " + var1.getMessageId() + " ClientInvokeInfo: " + var4);
      }

      Fiber var5 = var1.getClientInvokeInfo().getAndClearSuspendedRequestFiber();
      if (var5 != null) {
         var5.resumeAndReturn(var2, this._seq.getDeliveryAssurance().isInOrder());
      } else if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("IGNORING duplicate response for suspended fiber on msgId " + var1.getMessageId() + " ClientInvokeInfo: " + var4);
      }

   }

   private void handleFailureForSuspendedFiber(SourceMessageInfo var1, Throwable var2) {
      var2 = checkForPermanentSendFailure(var2);
      if (this._callback.sendFailed(var1.getMessageNum(), var2)) {
         SourceMessageInfo.ClientInvokeInfo var3 = var1.getClientInvokeInfo();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Handling permanent failure for suspended fiber on msgId " + var1.getMessageId() + " ClientInvokeInfo: " + var3);
         }

         Fiber var4 = var1.getClientInvokeInfo().getAndClearSuspendedRequestFiber();
         if (var4 != null) {
            var4.resumeAndThrow(var2, this._seq.getDeliveryAssurance().isInOrder());
         } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("IGNORING duplicate permanent failure for suspended fiber on msgId " + var1.getMessageId() + " ClientInvokeInfo: " + var3);
         }
      }

   }

   private void checkSequenceCreated(DispatchFactory var1) throws SOAPException, WsrmException, SendException {
      if (this._seq.getState() != SequenceState.NEW && this._seq.getState() != SequenceState.CREATING) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SourceSequenceSender clearing 'skip count' for CreateSequence on sequence " + this._seq.getId() + " in state " + this._seq.getState());
         }

         this._skipCreateSequenceSendCount = 0;
      } else if (this._skipCreateSequenceSendCount > 0) {
         --this._skipCreateSequenceSendCount;
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SourceSequenceSender skipping one send after having sent CreateSequence on sequence " + this._seq.getId() + " in state " + this._seq.getState());
         }

         throw new SenderNotReadyException("SourceSequence " + this._seq + " has not completed the handshake process (CreateSequenceResponse) yet");
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SourceSequenceSender forcing CreateSequence ahead of send on sequence " + this._seq.getId() + " in state " + this._seq.getState());
         }

         WsrmTubeUtils.sendCreateSequenceMsg(this._seq, var1, new weblogic.wsee.reliability2.tube.Sender.SendFailureCallback() {
            public void sendFailed(Throwable var1) {
               var1 = SourceSequenceSender.checkForPermanentSendFailure(var1);
               SourceSequenceSender.this._callback.conversationFailedToStart(var1);
            }
         });
         this._skipCreateSequenceSendCount = 2;
         throw new SenderNotReadyException("SourceSequence " + this._seq + " has not completed the handshake process (CreateSequenceResponse) yet");
      }
   }

   private SenderDispatchFactory.Key getSenderDispatchKey() throws PermanentSendException {
      if (this._seq.getSenderDispatchKey() == null) {
         throw new PermanentSendException("No WS-RM Sender Dispatch Factory key found in SourceSequence: " + this._seq);
      } else {
         return this._seq.getSenderDispatchKey();
      }
   }

   public synchronized void close() throws SendException {
      if (this._seq != null) {
         this._callback = null;
         this._factory.senderClosed(this._seq.getId());
         this._seq = null;
      }
   }

   private void updatePacket(SourceSequence var1, WsrmPropertyBag var2, Packet var3) throws WsrmException {
      if (var2.getOutboundMsgNeedsDestSeqId()) {
         SequenceHeader var4 = (SequenceHeader)WsrmHeaderFactory.getInstance().getHeaderFromPacket(SequenceHeader.class, var3);
         var4.setSequenceId(var1.getDestinationId());
         var2.setOutboundMsgNeedsDestSeqId(false);
         var3.getMessage().getHeaders().addOrReplace(var4);
      }

   }

   private class StatusCallback implements ConversationStatusCallback {
      private StatusCallback() {
      }

      public void deliveryFailure(SendRequest var1, List<Throwable> var2) throws SendingServiceException {
         try {
            ReliabilityErrorListener var3 = SourceSequenceSender.this.getSenderDispatchKey().findErrorListener();
            SourceSequenceSendRequest var4 = (SourceSequenceSendRequest)var1;
            if (var3 == null) {
               SourceMessageInfo var12 = var4.getMsgInfo();
               if (var12 != null) {
                  SourceMessageInfo.ClientInvokeInfo var13 = var12.getClientInvokeInfo();
                  if (var13 != null && var13.impliesSuspendedRequestFiber()) {
                     Fiber var15 = var13.getAndClearSuspendedRequestFiber();
                     if (var15 != null) {
                        var15.resumeAndThrow((Throwable)var2.get(0), true);
                        return;
                     }
                  }
               }

               String var14 = SourceSequenceSender.this._seq.getId() + " - " + var1.getMessageId();
               var14 = this.getErrorMessage(var14, var2);
               WseeRmLogger.logNoErrorListenerProvided(SourceSequenceSender.this.getSenderDispatchKey().toString(), var14);
            } else {
               Packet var5 = new Packet();
               PersistentMessageFactory.getInstance().setMessageIntoPacket((PersistentMessage)var4.getPayload(), var5);
               WsrmPropertyBag var6 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var5);
               String var7 = var6.getOutboundWsdlOperationName();
               ReliabilityErrorContext var8 = this.createReliabilityErrorContextImpl(true, var7, var5.getMessage(), var5.persistentContext, var2);
               var3.onReliabilityError(var8);
            }
         } catch (PermanentSendException var9) {
            throw new SendingServiceException(var9.toString(), var9);
         } catch (IllegalStateException var10) {
            throw new SendingServiceException(var10.toString(), var10);
         } catch (DispatchFactoryNotReadyException var11) {
            throw new ConversationStatusCallbackNotReadyException(var11.toString(), var11);
         }
      }

      private String getErrorMessage(String var1, List<Throwable> var2) {
         StringBuffer var3 = new StringBuffer(var1);
         var3.append("\n");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Throwable var5 = (Throwable)var4.next();
            var5 = this.unwrapError(var5);
            var3.append("\n").append(var5.toString());
         }

         return var3.toString();
      }

      private Throwable unwrapError(Throwable var1) {
         Throwable var2 = var1;

         for(Throwable var3 = var1; var3 != null; var3 = var3.getCause()) {
            if (var3 instanceof PermanentSendException || var3 instanceof AccessException) {
               var2 = var3;
               break;
            }
         }

         return var2;
      }

      private ReliabilityErrorContext createReliabilityErrorContextImpl(boolean var1, String var2, Message var3, Map<String, Serializable> var4, List<Throwable> var5) {
         String var6 = this.getErrorMessage("", var5);
         return new ReliabilityErrorContextImpl(var1, var2, var3, var5, var6, var4);
      }

      public void conversationClosed(String var1, List<Throwable> var2) throws SendingServiceException {
         try {
            ReliabilityErrorListener var3 = SourceSequenceSender.this.getSenderDispatchKey().findErrorListener();
            if (var3 == null) {
               String var9 = SourceSequenceSender.this._seq.getId();
               var9 = this.getErrorMessage(var9, var2);
               WseeRmLogger.logNoErrorListenerProvided(SourceSequenceSender.this.getSenderDispatchKey().toString(), var9);
            } else {
               Map var4 = (Map)SourceSequenceManager.getInstance().getSenderInvokeProperties((SourceSequence)SourceSequenceSender.this._seq, (Packet)null).get("weblogic.wsee.jaxws.async.PersistentContext");
               ReliabilityErrorContext var5 = this.createReliabilityErrorContextImpl(false, (String)null, (Message)null, var4, var2);
               var3.onReliabilityError(var5);
            }
         } catch (PermanentSendException var6) {
            throw new SendingServiceException(var6.toString(), var6);
         } catch (IllegalStateException var7) {
            throw new SendingServiceException(var7.toString(), var7);
         } catch (DispatchFactoryNotReadyException var8) {
            throw new ConversationStatusCallbackNotReadyException(var8.toString(), var8);
         }
      }

      public void conversationNotStarted(String var1, List<Throwable> var2) throws SendingServiceException {
         this.conversationClosed(var1, var2);
      }

      // $FF: synthetic method
      StatusCallback(Object var2) {
         this();
      }
   }
}
