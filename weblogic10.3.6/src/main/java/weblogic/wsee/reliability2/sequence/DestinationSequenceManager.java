package weblogic.wsee.reliability2.sequence;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.Fiber;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.persistence.PersistentContext;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.faults.IllegalRMVersionFaultException;
import weblogic.wsee.reliability.faults.SequenceClosedFaultException;
import weblogic.wsee.reliability.faults.SequenceClosedFaultMsg;
import weblogic.wsee.reliability.faults.WsrmFaultMsg;
import weblogic.wsee.reliability.headers.AckRequestedHeader;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEvent;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEventListenerRegistry;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.headers.WsrmHeaderFactory;
import weblogic.wsee.reliability2.property.WsrmPropertyBag;
import weblogic.wsee.reliability2.store.DestinationSequenceMap;
import weblogic.wsee.reliability2.store.TimedSequenceMap;
import weblogic.wsee.reliability2.tube.processors.TerminateSequenceProcessor;

public class DestinationSequenceManager extends SequenceManager<DestinationSequence> implements TimedSequenceMap.SequenceTimerListener {
   private static final Logger LOGGER = Logger.getLogger(DestinationSequenceManager.class.getName());
   private static DestinationSequenceManager _instance;

   public static DestinationSequenceManager getInstance() {
      return _instance;
   }

   private DestinationSequenceManager() throws PersistentStoreException, NamingException, WsrmException {
      super(new DestinationSequenceMap());
      this.recoverMap();
   }

   protected void recoverMap() throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("DestinationSequenceManager recovering destination sequences now");
      }

      ((TimedSequenceMap)this._seqIdToSeqMap).setSequenceTimerListener(this);
      super.recoverMap();
   }

   public void sequenceExpiration(String var1) {
      DestinationSequence var2 = (DestinationSequence)this.getSequence(WsrmConstants.RMVersion.latest(), var1);
      if (var2 != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** DestinationSequence reached its maximum lifetime. Seq id: " + var1);
         }

         this.terminateSequence(var1);
      }
   }

   public void idleTimeout(String var1) {
      DestinationSequence var2 = (DestinationSequence)this.getSequence(WsrmConstants.RMVersion.latest(), var1);
      if (var2 != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** DestinationSequence timed out on idle timeout. Seq id: " + var1);
         }

         this.terminateSequence(var1);
      }
   }

   public void terminateSequence(String var1) {
      try {
         DestinationSequence var2 = this.getSequence(WsrmConstants.RMVersion.latest(), var1, true);
         if (var2 == null) {
            return;
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** DestinationSequence being forcibly terminated. Seq id: " + var1);
         }

         TerminateSequenceProcessor.forceTerminateSequence(var2, var2.getRmVersion(), var2.getMaxMessageNum());
      } catch (Exception var3) {
         WseeRmLogger.logUnexpectedException(var3.toString(), var3);
      }

   }

   public void handleRmFault(WsrmFaultMsg var1) {
      try {
         var1.hashCode();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void handleAckRequest(AckRequestedHeader var1, String var2) throws WsrmException {
      DestinationSequence var3 = this.getSequence(var1.getRmVersion(), var1.getSequenceId(), true);
      if (var3 == null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** IGNORING ackRequested header for unknown destination sequence " + var1.getSequenceId());
         }

      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** Handling Ack Request on destination sequence " + var3.getId());
         }

         AcknowledgementHeader var4 = (AcknowledgementHeader)WsrmHeaderFactory.getInstance().createEmptyWsrmHeader(AcknowledgementHeader.class, var3.getRmVersion());
         var4.setSequenceId(var3.getId());
         var4.setFinal(var3.getState() == SequenceState.CLOSED);
         long var5 = -1L;
         Range var7 = null;
         Iterator var8 = var3.getRequests().keySet().iterator();

         while(var8.hasNext()) {
            Long var9 = (Long)var8.next();
            DestinationMessageInfo var10 = (DestinationMessageInfo)var3.getRequest(var9);
            if (var10.isAck()) {
               var7 = this.checkForNewRange(var9, var5, var7, var4);
               var5 = var9;
            }
         }

         this.checkForNewRange(Long.MAX_VALUE, var5, var7, var4);

         try {
            var3.deliverAckForAckRequest(var4, var2);
         } catch (WsrmException var11) {
            throw var11;
         } catch (Exception var12) {
            throw new WsrmException(var12.toString(), var12);
         }
      }
   }

   private Range checkForNewRange(Long var1, long var2, Range var4, AcknowledgementHeader var5) {
      if (var1 > var2 + 1L) {
         if (var4 != null) {
            var4.upper = var2;
            var5.acknowledgeMessages(var4.lower, var4.upper);
         }

         var4 = new Range();
         var4.lower = var1;
      }

      return var4;
   }

   public HandleSequenceMessageResult handleSequenceMessage(SequenceHeader var1, Packet var2, AddressingVersion var3, SOAPVersion var4, boolean var5) throws WsrmException {
      Message var6 = var2.getMessage();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("DestinationSequenceManager processing inbound sequence message with msg id " + var6.getHeaders().getMessageID(var3, var4) + ", seq " + var1.getSequenceId() + " and msg num " + var1.getMessageNumber());
      }

      String var7 = var1.getSequenceId();
      DestinationSequence var8 = getInstance().getSequence(var1.getRmVersion(), var7, false);
      if (var8 instanceof DestinationOfferSequence) {
         WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.CLIENT_IN_MSG_BEFORE_ACCEPT);
      } else {
         WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.SERV_IN_MSG_BEFORE_ACCEPT);
      }

      if (var1.getRmVersion() != var8.getRmVersion()) {
         throw new IllegalRMVersionFaultException(var7, var8.getRmVersion(), Arrays.asList(var1.getRmVersion()));
      } else {
         SequenceState var9 = var8.getState();
         if (var9 != SequenceState.CLOSING && var9 != SequenceState.CLOSED) {
            return this.internalHandleSequenceMessage(var8, var1, var2, var3, var4, var5);
         } else {
            SequenceClosedFaultMsg var10 = new SequenceClosedFaultMsg(var8.getRmVersion());
            var10.setSequenceId(var8.getId());
            throw new SequenceClosedFaultException(var10);
         }
      }
   }

   private HandleSequenceMessageResult internalHandleSequenceMessage(DestinationSequence var1, SequenceHeader var2, Packet var3, AddressingVersion var4, SOAPVersion var5, boolean var6) throws WsrmException {
      HandleSequenceMessageResult var7 = new HandleSequenceMessageResult();
      Message var8 = var3.getMessage();
      String var9 = var8.getID(var4, var5);
      String var10 = var8.getHeaders().getAction(var4, var5);
      String var11 = var8.getHeaders().getRelatesTo(var1.getAddressingVersion(), var1.getSoapVersion());
      if (!var1.isNonBuffered() && var11 == null) {
         Header var12 = var8.getHeaders().get(var1.getAddressingVersion().replyToTag, false);
         boolean var13 = var12 == null ? var6 : !String.valueOf(var12.getStringContent()).startsWith(var1.getAddressingVersion().anonymousUri);
         if (!var13) {
            throw new WsrmException("Request message " + var9 + " cannot have anonymous ReplyTo for buffered sequence " + var1.getId());
         }
      }

      long var17 = var2.getMessageNumber();
      DestinationMessageInfo var15 = (DestinationMessageInfo)var1.getRequest(var17);
      DestinationSequence.DestinationSequenceAddResult var14;
      if (var15 != null && !var15.isNew()) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Directly rejecting duplicate request msgId " + var9 + " msgNum " + var17 + " because we've already accepted this message (either via SAF or directly if non-buffered). This check was put in to avoid an infrequent issue with SAF where SAF gives us a successful 'ack' before it really knows if the message is duplicate or out-of-order.");
         }

         var14 = new DestinationSequence.DestinationSequenceAddResult();
         var14.added = false;
         var14.notAddedReason = Sequence.NotAddedReason.DUPLICATE;
      } else {
         var15 = new DestinationMessageInfo(var1.getId(), var9, var17, var10);
         var15.setRelatesTo(var11);
         if (var1.getRmVersion() == WsrmConstants.RMVersion.RM_10) {
            var15.setLastMessage(var2.isLastMessage());
            if (var15.isLastMessage() && (var3.getMessage() == null || !var3.getMessage().hasPayload())) {
               var15.setEmptyLastMessage(true);
            }
         }

         var15.setRequestPacket(var3);
         WsrmPropertyBag var16 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var3);
         var16.setDestMessageInfoFromRequest(var15);
         var14 = (DestinationSequence.DestinationSequenceAddResult)var1.addRequest(var15);
         var7.needSuspendOnCurrentFiber = var14.needSuspendOnCurrentFiber;
         var7.currentFiberSuspendingCallback = var14.currentFiberSuspendingCallback;
      }

      var15.setRequestPacket((Packet)null);
      if (var14.added) {
         if (var1.isNonBuffered()) {
            var15.setInProcess();
            this.markRequestAccepted(var1, var15);
         }
      } else {
         if (var14.notAddedReason == Sequence.NotAddedReason.DUPLICATE) {
            var1.addPiggybackAckHeader();
            if (!var6) {
               WSEndpointReference var18 = var8.getHeaders().getReplyTo(var4, var5);
               if (var18.isAnonymous()) {
                  if (LOGGER.isLoggable(Level.INFO)) {
                     LOGGER.info("Cannot accept message with msgId '" + var9 + "' and action '" + var10 + "' due to (" + var14.notAddedReason + ")");
                  }

                  throw new WsrmException(WseeRmLogger.logCannotAcceptDestinationSideRequestLoggable().getMessage());
               }
            }
         } else if (var14.notAddedReason == Sequence.NotAddedReason.OUT_OF_ORDER) {
         }

         var15 = null;
      }

      this.updateSequence(var1);
      var7.msgInfo = var15;
      return var7;
   }

   private void updateLinkedSourceSequenceIfNeeded(DestinationMessageInfo var1, DestinationSequence var2) throws UnknownSourceSequenceException {
      if (var1.getRelatesTo() != null && var2 instanceof DestinationOfferSequence) {
         DestinationOfferSequence var3 = (DestinationOfferSequence)var2;
         SourceSequence var4 = var3.getMainSequence();
         if (var4 != null) {
            SourceMessageInfo var5 = (SourceMessageInfo)var4.getRequestByMessageId(var1.getRelatesTo());
            if (var5 != null) {
               var5.setResponseMessageNum(var1.getMessageNum());
               SourceSequenceManager.getInstance().updateSequence(var4);
            }
         }
      }

   }

   public void markRequestAccepted(DestinationSequence var1, DestinationMessageInfo var2) {
      if (!var2.isAck()) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Marking request msgId=" + var2.getMessageId() + " msgNum=" + var2.getMessageNum() + " 'accepted' on sequence: " + var1.getId());
         }

         var2.setReceived();
         var2.setAck(true);
         if (var1.getRmVersion() == WsrmConstants.RMVersion.RM_10 && var2.isLastMessage()) {
            if (var2.isEmptyLastMessage()) {
               var2.setNoResponse();
            }

            var1.setFinalMessageNum(var2.getMessageNum());
         }

         var1.addPiggybackAckHeader();

         String var4;
         try {
            this.updateLinkedSourceSequenceIfNeeded(var2, var1);
         } catch (UnknownSourceSequenceException var6) {
            var4 = "Unknown";
            if (var1 instanceof DestinationOfferSequence) {
               DestinationOfferSequence var5 = (DestinationOfferSequence)var1;
               var4 = var5.getMainSequenceId();
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Failed to update source sequence " + var4 + " to reflect the response to msgId: " + var2.getRelatesTo() + " with response msgId " + var2.getMessageId() + ": " + var6.toString());
            }
         }

         if (var1 instanceof DestinationOfferSequence) {
            DestinationOfferSequence var3 = (DestinationOfferSequence)var1;
            var4 = var2.getRelatesTo();
            if (var4 != null) {
               String var7 = var1.getLogicalStoreName();
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine(this + " removing async request context for msgId " + var4 + " related to response msgId " + var2.getMessageId() + " and seq: " + var3.getMainSequenceId());
               }

               PersistentContext.getStoreMap(var7).remove(var4);
            }
         }

         if (var1 instanceof DestinationOfferSequence) {
            WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.CLIENT_IN_MSG_AFTER_ACCEPT);
         } else {
            WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.SERV_IN_MSG_AFTER_ACCEPT);
         }

      }
   }

   public void handleNoResponseSequenceMessage(DestinationMessageInfo var1) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Found NO RESPONSE destination message info for message number " + var1.getMessageNum() + " message id " + var1.getMessageId() + " and seq " + var1.getSequenceId());
      }

      DestinationSequence var2 = getInstance().getSequence(WsrmConstants.RMVersion.latest(), var1.getSequenceId(), true);
      boolean var3 = false;
      if (var2 != null) {
         try {
            var1 = (DestinationMessageInfo)var2.getRequestByMessageId(var1.getMessageId());
            if (var1 != null) {
               var1.setNoResponse();
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("** Writing NO RESPONSE destination message info for seq " + var1.getSequenceId() + " and message number " + var1.getMessageNum());
               }

               var3 = true;
            }
         } finally {
            if (var3) {
               try {
                  this.updateSequence(var2);
               } catch (UnknownDestinationSequenceException var12) {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Destination sequence " + var1.getSequenceId() + " was terminated before we could update NoResponse into msg info for msg: " + var1.getMessageId() + " msg num " + var1.getMessageNum());
                  }
               } catch (Exception var13) {
                  WseeRmLogger.logUnexpectedException(var13.toString(), var13);
               }
            }

         }
      }

   }

   public void addSequence(DestinationSequence var1) throws WsrmException {
      super.addSequence(var1);
      var1.resetAckTimer();
   }

   public void updateSequence(DestinationSequence var1) throws UnknownDestinationSequenceException {
      try {
         super.updateSequence(var1);
      } catch (UnknownSequenceException var3) {
         throw new UnknownDestinationSequenceException(var3);
      }
   }

   public DestinationSequence getSequence(WsrmConstants.RMVersion var1, String var2, boolean var3) throws UnknownDestinationSequenceException {
      try {
         DestinationSequence var4 = (DestinationSequence)super.getSequence(var1, var2, var3);
         if (var4 instanceof DestinationOfferSequence) {
            ((DestinationOfferSequence)var4).takeSecurityContextFromMainSequence();
         }

         return var4;
      } catch (UnknownSequenceException var5) {
         throw new UnknownDestinationSequenceException(var1, var2);
      }
   }

   public Map<String, Object> getSenderInvokeProperties(DestinationSequence var1, Packet var2) {
      HashMap var3 = new HashMap();
      if (var1 instanceof DestinationOfferSequence) {
         ((DestinationOfferSequence)var1).getMainSequenceFirstRequestContext().loadRequestContext(var3);
      }

      var3.putAll(super.getSenderInvokeProperties(var1, var2));
      return var3;
   }

   static {
      try {
         _instance = new DestinationSequenceManager();
      } catch (Exception var1) {
         throw new RuntimeException(var1.toString(), var1);
      }
   }

   public class HandleSequenceMessageResult {
      public DestinationMessageInfo msgInfo;
      public boolean needSuspendOnCurrentFiber;
      public Fiber.Listener currentFiberSuspendingCallback;
   }

   private class Range {
      long lower;
      long upper;

      private Range() {
      }

      // $FF: synthetic method
      Range(Object var2) {
         this();
      }
   }
}
