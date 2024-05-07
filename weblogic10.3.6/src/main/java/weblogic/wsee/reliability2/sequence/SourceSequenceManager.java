package weblogic.wsee.reliability2.sequence;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.Fiber;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import weblogic.jws.jaxws.client.async.AsyncClientHandlerFeature;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.client.async.AsyncClientHandlerMarkerFeature;
import weblogic.wsee.reliability.MessageRange;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmPermanentTransportException;
import weblogic.wsee.reliability.faults.SequenceTerminatedFaultMsg;
import weblogic.wsee.reliability.faults.UnknownSequenceFaultMsg;
import weblogic.wsee.reliability.faults.WsrmFaultMsg;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEvent;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEventListenerRegistry;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.property.WsrmInvocationPropertyBag;
import weblogic.wsee.reliability2.property.WsrmPropertyBag;
import weblogic.wsee.reliability2.store.SourceSequenceMap;
import weblogic.wsee.reliability2.store.TimedSequenceMap;
import weblogic.wsee.reliability2.tube.DispatchFactory;
import weblogic.wsee.reliability2.tube.DispatchFactoryNotReadyException;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;

public class SourceSequenceManager extends SequenceManager<SourceSequence> implements TimedSequenceMap.SequenceTimerListener {
   private static final Logger LOGGER = Logger.getLogger(SourceSequenceManager.class.getName());
   private static SourceSequenceManager _instance;
   private Map<String, SourceSequence> _destSeqIdToSeqMap = new HashMap();
   private ReentrantReadWriteLock _mapLock = new ReentrantReadWriteLock(false);

   public static SourceSequenceManager getInstance() {
      return _instance;
   }

   private SourceSequenceManager() throws PersistentStoreException, NamingException, WsrmException {
      super(new SourceSequenceMap());
      this.recoverMap();
   }

   protected void recoverMap() throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("SourceSequenceManager recovering source sequences now");
      }

      ((TimedSequenceMap)this._seqIdToSeqMap).setSequenceTimerListener(this);
      super.recoverMap();
   }

   protected void handleSequenceActiveChange(SourceSequence var1, boolean var2, boolean var3) {
      if (var2 && !var3) {
         if (var1.getDestinationId() != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Seq Deactivation. Removing mapping of dest seq ID " + var1.getDestinationId() + " to seq " + var1.getId());
            }

            try {
               this._mapLock.writeLock().lock();
               this._destSeqIdToSeqMap.remove(var1.getDestinationId());
            } finally {
               this._mapLock.writeLock().unlock();
            }
         }
      } else if (!var2 && var3 && var1.getDestinationId() != null && !var1.getDestinationId().equals(var1.getId())) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Seq Activation. Adding mapping for dest seq ID " + var1.getDestinationId() + " to seq " + var1.getId());
         }

         try {
            this._mapLock.writeLock().lock();
            this._destSeqIdToSeqMap.put(var1.getDestinationId(), var1);
         } finally {
            this._mapLock.writeLock().unlock();
         }
      }

      super.handleSequenceActiveChange(var1, var2, var3);
   }

   public void sequenceExpiration(String var1) {
      SourceSequence var2 = (SourceSequence)this.getSequence(WsrmConstants.RMVersion.latest(), var1);
      if (var2 != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** SourceSequence reached its maximum lifetime. Seq id: " + var1);
         }

         this.terminateSequence(var1);
      }
   }

   public void idleTimeout(String var1) {
      SourceSequence var2 = (SourceSequence)this.getSequence(WsrmConstants.RMVersion.latest(), var1);
      if (var2 != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** SourceSequence timed out on idle timeout. Seq id: " + var1);
         }

         this.terminateSequence(var1);
      }
   }

   public void terminateSequence(String var1) {
      try {
         SourceSequence var2 = this.getSequence(WsrmConstants.RMVersion.latest(), var1, false);
         if (var2.getSenderDispatchKey() == null) {
            throw new WsrmPermanentTransportException("No WS-RM SAF Dispatch Factory key found in SourceSequence");
         }

         DispatchFactory var3 = var2.getSenderDispatchKey().resolve();
         WsrmTubeUtils.sendTerminateSequence(var2, var3);
      } catch (DispatchFactoryNotReadyException var4) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(var4.toString());
         }
      } catch (Exception var5) {
         WseeRmLogger.logUnexpectedException(var5.toString(), var5);
      }

   }

   public void handleRmFault(WsrmFaultMsg var1) {
      try {
         String var2 = null;
         boolean var3 = false;
         if (var1 instanceof SequenceTerminatedFaultMsg) {
            var3 = true;
            var2 = ((SequenceTerminatedFaultMsg)var1).getSequenceId();
         }

         if (var1 instanceof UnknownSequenceFaultMsg) {
            var3 = true;
            var2 = ((UnknownSequenceFaultMsg)var1).getSequenceId();
         }

         if (var3) {
            SourceSequence var4 = this.getSequence(var1.getRmVersion(), var2, true);
            if (var4 != null && var4.getState() != SequenceState.TERMINATING && var4.getState() != SequenceState.TERMINATED) {
               this.terminateSequence(var2);
            }
         }
      } catch (Exception var5) {
         WseeRmLogger.logUnexpectedException(var5.toString(), var5);
      }

   }

   public void handleAck(AcknowledgementHeader var1, SourceSequence var2) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Handling ack header on sequence " + var2.getId() + ". Ack header toString() is: " + var1);
      }

      if (var2 instanceof SourceOfferSequence) {
         WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.SERV_OUT_FOUND_ACK);
      } else {
         WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.CLIENT_OUT_FOUND_ACK);
      }

      synchronized(var2) {
         boolean var4 = false;
         if (var1.getNack() > 0L) {
            SourceMessageInfo var5 = (SourceMessageInfo)var2.getRequest(var1.getNack());
            if (var5 != null) {
               var2.markMessageAckd(var5, false);
            }

            var4 = this.syncUpWithAcks(var2);
         } else if (!var1.getNone()) {
            SortedSet var8 = var1.getAcknowledgementRanges();
            var4 = this.syncUpWithAcks(var2, var8);
         }

         if (var4) {
            this.updateSequence(var2);
         }

      }
   }

   public boolean syncUpWithAcks(SourceSequence var1) {
      return this.syncUpWithAcks(var1, var1.getAckRanges());
   }

   public boolean syncUpWithAcks(SourceSequence var1, SortedSet<MessageRange> var2) {
      boolean var3 = false;
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Sync'ing up with acks on seq: " + var1);
      }

      ArrayList var4 = new ArrayList();
      MessageRange var5 = null;

      MessageRange var7;
      for(Iterator var6 = var2.iterator(); var6.hasNext(); var5 = var7) {
         var7 = (MessageRange)var6.next();

         for(long var8 = var7.lowerBounds; var8 <= var7.upperBounds; ++var8) {
            SourceMessageInfo var10 = (SourceMessageInfo)var1.getRequest(var8);
            if (var10 != null) {
               var3 |= var1.markMessageAckd(var10, true);
            }
         }

         if (var5 != null && var7.lowerBounds > var5.upperBounds) {
            Gap var11 = new Gap();
            var11.lower = var5.upperBounds + 1L;
            var11.upper = var7.lowerBounds - 1L;
            var4.add(var11);
         }
      }

      if (var1.hasFinalMessageNum() && var5 != null && var5.upperBounds == var1.getFinalMessageNum() && var4.isEmpty() && !var1.isComplete()) {
         var1.setComplete(true);
         var3 = true;
      }

      return var3;
   }

   public SourceMessageInfo processSequenceMessage(SequenceHeader var1, Packet var2, AddressingVersion var3, SOAPVersion var4) throws WsrmException {
      if (var2 != null && var2.getMessage() != null) {
         long var5 = var1.getMessageNumber();
         SourceSequence var7 = this.getSequence(var1.getRmVersion(), var1.getSequenceId(), false);
         WsrmInvocationPropertyBag var8 = (WsrmInvocationPropertyBag)var2.invocationProperties.get(WsrmInvocationPropertyBag.key);
         if (var8 == null) {
            var8 = new WsrmInvocationPropertyBag(var2);
            var2.invocationProperties.put(WsrmInvocationPropertyBag.key, var8);
         }

         WsrmPropertyBag var9 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var2);
         if (var8.getFinalMsgFlag()) {
            if (var7.getRmVersion() == WsrmConstants.RMVersion.RM_10) {
               var1.setLastMessage(true);
               var7.setFinalMessageNum(var5);
            } else {
               var7.setFinalMessageNum(var5);
            }
         }

         Message var10 = var2.getMessage();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SourceSequenceManager processing outbound sequence message with msg id " + var10.getHeaders().getMessageID(var3, var4) + ", seq " + var1.getSequenceId() + " and msg num " + var1.getMessageNumber());
         }

         String var11 = var10.getHeaders().getMessageID(var3, var4);
         String var12 = var10.getHeaders().getAction(var3, var4);
         SourceMessageInfo var13 = new SourceMessageInfo(var7.getId(), var11, var5, var12);
         Packet var14 = var2.copy(false);
         var14.setMessage(var2.getMessage());
         var14.soapAction = var2.soapAction;
         var13.setRequestPacket(var14);
         if (!var7.isNonBuffered()) {
            boolean var15 = var2.getBinding().getFeature(AsyncClientHandlerFeature.class) != null || var2.getBinding().getFeature(AsyncClientHandlerMarkerFeature.class) != null;
            SourceMessageInfo.ClientInvokeInfo var16 = new SourceMessageInfo.ClientInvokeInfo(Fiber.current(), Boolean.TRUE.equals(var2.isSynchronousMEP), Boolean.FALSE.equals(var2.expectReply), var15);
            var13.setClientInvokeInfo(var16);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Stored ClientInvokeInfo for source msgId " + var13.getMessageId() + " ClientInvokeInfo: " + var13.getClientInvokeInfo());
            }
         }

         var7.addRequest(var13);
         var13.setRequestPacket((Packet)null);
         if (var7 instanceof SourceOfferSequence) {
            String var19 = var9.getInboundMessageId();
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("SourceSequenceManager processing response msg " + var11 + " as a response to reliable service-side request msg " + var19 + ". Attempting to correlate them in sequence state");
            }

            SourceOfferSequence var20 = (SourceOfferSequence)var7;
            DestinationSequence var17 = var20.getMainSequence();
            DestinationMessageInfo var18;
            if (var17 != null) {
               var18 = (DestinationMessageInfo)var17.getRequestByMessageId(var19);
               var18.setResponseMsgNum(var5);
               DestinationSequenceManager.getInstance().updateSequence(var17);
            }

            var18 = var9.getDestMessageInfoFromRequest();
            if (var7.getRmVersion() == WsrmConstants.RMVersion.RM_10 && var18.isLastMessage()) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("** SourceSequenceManager found it is sending a response on sequence " + var7.getId() + " to the 'last' message on the request sequence " + var20.getMainSequenceId() + ". Marking this response 'last' too");
               }

               var1.setLastMessage(true);
               var7.setFinalMessageNum(var5);
            }
         }

         this.updateSequence(var7);
         return var13;
      } else {
         throw new IllegalArgumentException("Attempt to send a sequence message with a null/empty body");
      }
   }

   public void updateSequence(SourceSequence var1) throws UnknownSourceSequenceException {
      try {
         super.updateSequence(var1);
      } catch (UnknownSequenceException var7) {
         throw new UnknownSourceSequenceException(var7);
      }

      if (var1.getDestinationId() != null && !var1.getDestinationId().equals(var1.getId())) {
         try {
            this._mapLock.writeLock().lock();
            this._destSeqIdToSeqMap.put(var1.getDestinationId(), var1);
         } finally {
            this._mapLock.writeLock().unlock();
         }
      }

   }

   public SourceSequence getSequence(WsrmConstants.RMVersion var1, String var2, boolean var3) throws UnknownSourceSequenceException {
      SourceSequence var4 = null;

      try {
         var4 = (SourceSequence)super.getSequence(var1, var2, true);
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      if (var4 == null) {
         try {
            this._mapLock.readLock().lock();
            var4 = (SourceSequence)this._destSeqIdToSeqMap.get(var2);
         } finally {
            this._mapLock.readLock().unlock();
         }
      }

      if (var4 == null && !var3) {
         throw new UnknownSourceSequenceException(var1, var2);
      } else {
         return var4;
      }
   }

   public void removeSequence(SourceSequence var1) {
      if (var1.getDestinationId() != null) {
         try {
            this._mapLock.writeLock().lock();
            this._destSeqIdToSeqMap.remove(var1.getDestinationId());
         } finally {
            this._mapLock.writeLock().unlock();
         }
      }

      super.removeSequence(var1);
   }

   public Map<String, Object> getSenderInvokeProperties(SourceSequence var1, Packet var2) {
      HashMap var3 = new HashMap();
      var1.getFirstRequestContext().loadRequestContext(var3);
      Map var4 = super.getSenderInvokeProperties(var1, var2);
      HashMap var5 = new HashMap();
      Iterator var6 = var3.keySet().iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         if (!var4.containsKey(var7)) {
            var5.put(var7, var3.get(var7));
         }
      }

      var4.putAll(var5);
      return var4;
   }

   static {
      try {
         _instance = new SourceSequenceManager();
      } catch (Exception var1) {
         throw new RuntimeException(var1.toString(), var1);
      }
   }

   private static class Gap {
      long lower;
      long upper;

      private Gap() {
      }

      // $FF: synthetic method
      Gap(Object var1) {
         this();
      }
   }
}
