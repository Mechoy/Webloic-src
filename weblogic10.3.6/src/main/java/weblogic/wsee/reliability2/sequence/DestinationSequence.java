package weblogic.wsee.reliability2.sequence;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.message.RelatesToHeader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import weblogic.jws.jaxws.client.async.FiberBox;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.client.async.AsyncTransportProvider;
import weblogic.wsee.jaxws.client.async.AsyncTransportProviderPropertyBag;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmSecurityContext;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.WsrmHeader;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEvent;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEventListenerRegistry;
import weblogic.wsee.reliability2.exception.WsrmExceptionUtil;
import weblogic.wsee.reliability2.property.WsrmPropertyBag;
import weblogic.wsee.reliability2.tube.DispatchFactory;
import weblogic.wsee.reliability2.tube.DispatchFactoryNotReadyException;
import weblogic.wsee.reliability2.tube.DispatchFactoryResolver;
import weblogic.wsee.reliability2.tube.Sender;

public class DestinationSequence extends Sequence<DestinationMessageInfo> implements DispatchFactoryResolver.LifecycleListener, Serializable {
   private static final long serialVersionUID = 1L;
   private static final Logger LOGGER = Logger.getLogger(DestinationSequence.class.getName());
   private transient WSEndpointReference _acksToEpr;
   private transient WSEndpointReference _hostEpr;
   private String _offerSequenceId;
   private transient SourceOfferSequence _offerSequence;
   private DispatchFactoryResolver.Key _dispatchKey;
   private Duration _ackInterval;
   private transient TimerManager _timerMgr;
   private transient Timer _ackTimer = null;
   private transient LinkedBlockingQueue<DispatchItem> _dispatchQueue;
   private transient DispatchThread _dispatchThread;

   private void initTransients() {
      if (this.isUsingDispatchQueue()) {
         this._dispatchQueue = new LinkedBlockingQueue();
         this._dispatchThread = new DispatchThread();
      }

   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject("10.3.6");
      WsUtil.serializeWSEndpointReference(this._acksToEpr, var1);
      WsUtil.serializeWSEndpointReference(this._hostEpr, var1);
      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.initTransients();
      var1.readObject();
      this._acksToEpr = WsUtil.deserializeWSEndpointReference(var1, this.getAddressingVersion());
      this._hostEpr = WsUtil.deserializeWSEndpointReference(var1, this.getAddressingVersion());
      var1.defaultReadObject();
   }

   public DestinationSequence(String var1, String var2, WsrmConstants.RMVersion var3, AddressingVersion var4, SOAPVersion var5, WsrmSecurityContext var6, boolean var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.initTransients();

      try {
         this._ackInterval = DatatypeFactory.newInstance().newDuration("P0DT0.2S");
      } catch (Exception var9) {
         throw new RuntimeException(var9.toString(), var9);
      }
   }

   public void startup() {
      super.startup();
      if (this.isNonBuffered() && this.isUsingDispatchQueue()) {
         this._dispatchThread.start();
      }

   }

   public void shutdown() {
      SequenceState var1 = this.getState();

      try {
         this.getPiggybackLock().writeLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Shutting down: " + this + " in state " + var1);
         }

         if (this.isNonBuffered()) {
            if (this._ackTimer != null) {
               this._ackTimer.cancel();
               this._ackTimer = null;
            }

            if (this.isUsingDispatchQueue()) {
               this._dispatchThread.quit();
               this._dispatchThread = null;
               this._dispatchQueue = null;
            }
         }

         super.shutdown();
      } finally {
         this.getPiggybackLock().writeLock().unlock();
      }

   }

   public DestinationMessageInfo copyMessageInfo(DestinationMessageInfo var1) {
      return new DestinationMessageInfo(var1);
   }

   public boolean isUsingDispatchQueue() {
      return this.isNonBuffered() && this.getDeliveryAssurance().isInOrder();
   }

   public WSEndpointReference getAcksToEpr() {
      return this._acksToEpr;
   }

   public WSEndpointReference getHostEpr() {
      return this._hostEpr;
   }

   public WSEndpointReference getPiggybackEpr() {
      return this.getAcksToEpr();
   }

   public SourceOfferSequence getOffer() {
      if (this._offerSequence != null && this._offerSequenceId == null) {
         return this._offerSequence;
      } else {
         this._offerSequence = null;
         if (this._offerSequenceId == null) {
            return null;
         } else {
            try {
               return (SourceOfferSequence)SourceSequenceManager.getInstance().getSequence(this.getRmVersion(), this._offerSequenceId, true);
            } catch (Exception var2) {
               throw new RuntimeException(var2.toString(), var2);
            }
         }
      }
   }

   public DispatchFactoryResolver.Key getDispatchKey() {
      return this._dispatchKey;
   }

   public void setDispatchKey(DispatchFactoryResolver.Key var1) {
      this.stopListeningOnDispatchKey();
      this._dispatchKey = var1;
      this.markChanged();
      if (this.isMasterInstance()) {
         this.startListeningOnDispatchKey();
      }

   }

   private void startListeningOnDispatchKey() {
      if (this._dispatchKey != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("DestinationSequence " + this + " starting listen on dispatch key: " + this._dispatchKey);
         }

         this._dispatchKey.addLifecycleListener(this);
      }

   }

   private void stopListeningOnDispatchKey() {
      if (this._dispatchKey != null) {
         this._dispatchKey.removeLifecycleListener(this);
      }

   }

   public void onDispatchFactoryDispose(DispatchFactory var1) {
      if (this._dispatchKey != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(this.getClass().getName() + " " + this.getId() + " detected its DispatchFactory key is being disposed. Delivering any pending acks before the key becomes invalid");
         }

         this.deliverPendingAcks(var1);
         this._dispatchKey.removeLifecycleListener(this);
         this._dispatchKey = null;
      }
   }

   public boolean setState(SequenceState var1) {
      boolean var2 = super.setState(var1);
      if (var1 == SequenceState.TERMINATED) {
         this.dispose();
      }

      return var2;
   }

   void dispose() {
      if (this._dispatchKey != null) {
         this._dispatchKey.removeLifecycleListener(this);
         this._dispatchKey = null;
      }

   }

   public void setAcksToEpr(WSEndpointReference var1) {
      this._acksToEpr = var1;
      this.markChanged();
   }

   public void setHostEpr(WSEndpointReference var1) {
      this._hostEpr = var1;
      this.setUsingSsl(HeaderList.isUsingSsl(var1));
      this.markChanged();
   }

   public void setOffer(SourceOfferSequence var1) {
      this._offerSequenceId = var1 != null ? var1.getId() : null;
      this.markChanged();
   }

   public Duration getAckInterval() {
      return this._ackInterval;
   }

   public void setAckInterval(Duration var1) {
      this._ackInterval = var1;
      this.markChanged();
   }

   public long calculateFinalResponseMessageNum() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("*** Calculating final response message number for sequence " + this);
      }

      if (this.hasFinalMessageNum()) {
         long var1 = -1L;
         StringBuffer var3 = new StringBuffer();

         try {
            this.getMessageLock().readLock().lock();
            Iterator var4 = this.getRequests().values().iterator();

            while(var4.hasNext()) {
               DestinationMessageInfo var5 = (DestinationMessageInfo)var4.next();
               long var6 = var5.getResponseMsgNum();
               if (LOGGER.isLoggable(Level.FINE)) {
                  var3.append("    message id ");
                  var3.append(var5.getMessageId());
                  var3.append(" msgNum ");
                  var3.append(var5.getMessageNum());
                  var3.append(" responseMsgNum ");
                  var3.append(var6);
                  var3.append("\n");
               }

               if (var6 < 0L) {
                  var1 = -1L;
                  break;
               }

               if (var6 > var1) {
                  var1 = var6;
               }
            }
         } finally {
            this.getMessageLock().readLock().unlock();
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Dump of message info for sequence: " + this + "\n" + var3.toString());
         }

         if (var1 >= 0L) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("*** Final response message number for sequence " + this + " is: " + var1);
            }

            return var1;
         } else {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("*** Final response message number could not be determined for sequence " + this);
            }

            return -1L;
         }
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("*** Final request message hasn't been indicated for sequence " + this + " so final response message number could not be determined");
         }

         return -1L;
      }
   }

   public Sequence.AddResult addRequest(DestinationMessageInfo var1) {
      FiberBox var3;
      if (!this.isNonBuffered()) {
         WsrmPropertyBag var14 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var1.getRequestPacket());
         var3 = null;
         if (var14.getResponseToSuspendedFiber()) {
            var3 = new FiberBox(Fiber.current());
            var1.setSuspendedFiber(var3);
         }

         DestinationSequenceAddResult var15 = new DestinationSequenceAddResult(super.addRequest(var1));
         if (var14.getResponseToSuspendedFiber()) {
            var15.needSuspendOnCurrentFiber = true;
            var15.currentFiberSuspendingCallback = this.createCurrentFiberSuspendedingCallbackImpl(var3);
         }

         return var15;
      } else {
         DestinationSequenceAddResult var2 = new DestinationSequenceAddResult();
         var2.added = true;
         var2.notAddedReason = null;

         try {
            this.getMessageLock().writeLock().lock();
            if ((this.getDeliveryAssurance().getQos() == WsrmConstants.DeliveryQOS.AtMostOnce || this.getDeliveryAssurance().getQos() == WsrmConstants.DeliveryQOS.ExactlyOnce) && this.getRequests().containsKey(var1.getMessageNum())) {
               var2.added = false;
               var2.notAddedReason = Sequence.NotAddedReason.DUPLICATE;
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("*** Destination message number " + var1.getMessageNum() + " not added to sequence " + this.getId() + ". Reason: " + var2.notAddedReason);
               }
            }

            if (var2.added && this.getDeliveryAssurance().isInOrder() && var1.getMessageNum() != this.getMaxMessageNum() + 1L) {
               var2.added = false;
               var2.notAddedReason = Sequence.NotAddedReason.OUT_OF_ORDER;
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("*** Request message msgId " + var1.getMessageId() + " and msgNum " + var1.getMessageNum() + " not added to sequence " + this.getId() + ". Reason: " + var2.notAddedReason);
               }
            }

            if (var2.added) {
               super.addRequestInternal(var1);
               if (this.isUsingDispatchQueue()) {
                  try {
                     var3 = new FiberBox(Fiber.current());
                     if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(this + " adding request msgId " + var1.getMessageId() + " msgNum " + var1.getMessageNum() + " to DispatchQueue by storing FiberBox: " + var3);
                     }

                     DispatchItem var4 = new DispatchItem(var3, var1);
                     this._dispatchQueue.put(var4);
                     var2.needSuspendOnCurrentFiber = true;
                     var2.currentFiberSuspendingCallback = this.createCurrentFiberSuspendedingCallbackImpl(var3);
                  } catch (Exception var11) {
                     throw new RuntimeException(var11.toString(), var11);
                  }
               }
            }
         } finally {
            this.getMessageLock().writeLock().unlock();
            if (var2.added) {
               this.markChanged();
            } else {
               try {
                  preventRemovalOfAsyncRequestContext(this, var1, var1.getRequestPacket());
               } catch (Exception var12) {
                  if (LOGGER.isLoggable(Level.INFO)) {
                     LOGGER.log(Level.INFO, "Couldn't prevent the removal of async request context for msgId: " + var1.getMessageId() + ": " + var12.toString(), var12);
                  }
               }
            }

         }

         return var2;
      }
   }

   private Fiber.Listener createCurrentFiberSuspendedingCallbackImpl(final FiberBox var1) {
      return new Fiber.Listener() {
         public void fiberSuspended(Fiber var1x) {
            if (DestinationSequence.LOGGER.isLoggable(Level.FINE)) {
               DestinationSequence.LOGGER.fine(this + " detected suspension of fiber in FiberBox: " + var1);
            }

            var1.open();
            var1x.removeListener(this);
         }

         public void fiberResumed(Fiber var1x) {
            var1x.removeListener(this);
         }
      };
   }

   public boolean isAnonymous() {
      return this._acksToEpr == null || this._acksToEpr.isAnonymous();
   }

   public void addPiggybackAckHeader() {
      this.flagPiggybackAck();
      this.resetAckTimer();
   }

   @NotNull
   public void putBackUnusedPiggybackHeaders(List<WsrmHeader> var1) {
      if (super.putBackUnusedPiggybackHeadersInternal(var1)) {
         this.resetAckTimer();
         this.markChanged();
      }

   }

   protected boolean flagPiggybackAck() {
      boolean var1 = false;

      try {
         this.getPiggybackLock().writeLock().lock();
         if (super.flagPiggybackAckInternal()) {
            this.resetAckTimer();
            var1 = true;
         } else {
            var1 = false;
         }
      } finally {
         this.getPiggybackLock().writeLock().unlock();
         if (var1) {
            this.markChanged();
         }

      }

      return var1;
   }

   public void addPiggybackHeader(WsrmHeader var1) {
      boolean var2 = false;

      try {
         this.getPiggybackLock().writeLock().lock();
         var2 = super.addPiggybackHeaderInternal(var1);
         this.resetAckTimer();
      } finally {
         this.getPiggybackLock().writeLock().unlock();
         if (var2) {
            this.markChanged();
         }

      }

   }

   public void addPiggybackHeaders(List<WsrmHeader> var1) {
      try {
         this.getPiggybackLock().writeLock().lock();
         super.addPiggybackHeaders(var1);
         this.resetAckTimer();
      } finally {
         this.getPiggybackLock().writeLock().unlock();
      }

   }

   public List<WsrmHeader> getAndClearPiggybackHeaders() {
      List var1 = null;
      SequenceState var2 = this.getState();

      List var3;
      try {
         this.getPiggybackLock().writeLock().lock();
         var1 = this.getAndClearPiggybackHeadersLocal(var2 == SequenceState.CLOSED);
         if (var2 == SequenceState.CLOSED) {
            this.addPiggybackAckHeader();
         } else {
            this.resetAckTimer();
         }

         if (var1.size() > 0) {
            if (this instanceof DestinationOfferSequence) {
               WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.CLIENT_IN_MSG_AFTER_ACK);
            } else {
               WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.SERV_IN_MSG_AFTER_ACK);
            }
         }

         var3 = var1;
      } finally {
         this.getPiggybackLock().writeLock().unlock();
         if (var1 != null && var1.size() > 0) {
            this.markChanged();
         }

      }

      return var3;
   }

   public void forceDeliverPendingAcks() {
      this.deliverPendingAcks();
   }

   public void resetAckTimer() {
      try {
         this.getPiggybackLock().writeLock().lock();
         if (this._timerMgr == null) {
            TimerManagerFactory var1 = TimerManagerFactory.getTimerManagerFactory();
            this._timerMgr = var1.getDefaultTimerManager();
         }

         boolean var7 = !this.isAnonymous() && this.hasPiggybackHeaders();
         if (this._ackTimer != null && !var7) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Cancelling ack timer (no acks) for sequence: " + this.getId());
            }

            this._ackTimer.cancel();
            this._ackTimer = null;
         }

         if (var7 && this._ackTimer == null) {
            long var2 = this._ackInterval.getTimeInMillis(new Date());
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Scheduling ack timer (" + var2 + " ms timer) for sequence: " + this.getId());
            }

            this._ackTimer = this._timerMgr.schedule(new TimerListener() {
               public void timerExpired(Timer var1) {
                  DestinationSequence.this.deliverPendingAcks();
               }
            }, var2);
         }
      } finally {
         this.getPiggybackLock().writeLock().unlock();
      }

   }

   private void deliverPendingAcks() {
      try {
         if (this.getDispatchKey() == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("We were asked to deliver pending acks on seq " + this + " but have no dispatch key to do so. Aborting.");
            }

            return;
         }

         DispatchFactory var1 = this.getDispatchKey().resolve();
         this.deliverPendingAcks(var1);
      } catch (DispatchFactoryNotReadyException var2) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(var2.toString());
         }
      } catch (Exception var3) {
         WseeRmLogger.logUnexpectedException(var3.toString(), var3);
      }

   }

   @NotNull
   private List<WsrmHeader> getAndClearPiggybackHeadersLocal(boolean var1) {
      List var2 = this.getAndClearPiggybackHeadersInternal();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         WsrmHeader var4 = (WsrmHeader)var3.next();
         if (var4 instanceof AcknowledgementHeader) {
            ((AcknowledgementHeader)var4).setFinal(var1);
         }
      }

      return var2;
   }

   private void deliverPendingAcks(DispatchFactory var1) {
      List var2 = null;
      SequenceState var3 = this.getState();
      if (var3 != SequenceState.CLOSING && var3 != SequenceState.CLOSED && var3 != SequenceState.TERMINATING && var3 != SequenceState.TERMINATED) {
         try {
            this.getPiggybackLock().writeLock().lock();
            var2 = this.getAndClearPiggybackHeadersLocal(var3 == SequenceState.CLOSED);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Explicitly delivering pending ack (" + var2.size() + " total headers) for sequence: " + this.getId());
            }

            this.resetAckTimer();
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Done getting piggyback headers, and resetting ack timer, proceeding to send explicit ack");
            }
         } finally {
            this.getPiggybackLock().writeLock().unlock();
            if (var2 != null && var2.size() > 0) {
               this.markChanged();
            }

         }

         try {
            this.internalDeliverAcks(var1, var2, (String)null);
         } catch (Exception var7) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Failed to deliver pending acks for " + this + ": " + var7.toString());
            }

            if (WsrmExceptionUtil.isPermanentSendFailure(var7)) {
               WseeRmLogger.logUnexpectedException(var7.toString(), var7);
            }
         }

      }
   }

   public void deliverAckForAckRequest(WsrmHeader var1, String var2) throws Exception {
      if (this._acksToEpr != null && !this._acksToEpr.isAnonymous()) {
         DispatchFactoryResolver.Key var3 = this.getDispatchKey();
         if (var3 == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Failed to deliver ack for " + this + ": dispatchKey has been disposed");
            }

            return;
         }

         DispatchFactory var4 = var3.resolve();
         ArrayList var5 = new ArrayList();
         var5.add(var1);
         this.internalDeliverAcks(var4, var5, var2);
      } else {
         this.flagPiggybackAck();
      }

   }

   private void internalDeliverAcks(DispatchFactory var1, List<WsrmHeader> var2, String var3) throws Exception {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Internally sending explicit ack with " + var2.size() + " headers");
      }

      if (!var2.isEmpty()) {
         if (this._acksToEpr != null && !this._acksToEpr.isAnonymous()) {
            Sender var4 = new Sender(var1);
            String var5 = WsrmConstants.Action.ACK.getActionURI(this.getRmVersion());
            Message var6 = WsUtil.createEmptyMessage(var5, this._acksToEpr.getVersion(), this.getSoapVersion());
            this.addHeadersForAckMsg(var2, var3, var6);
            Map var7 = DestinationSequenceManager.getInstance().getSenderInvokeProperties((DestinationSequence)this, (Packet)null);
            var4.send(var6, var5, this._acksToEpr, this.getHostEpr(), var7, false, true);
         } else {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Aborting request to send explicit/standalone ack for sequence " + this + " because the target endpoint is anonymous");
            }

         }
      }
   }

   private void addHeadersForAckMsg(List<WsrmHeader> var1, String var2, Message var3) {
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         WsrmHeader var5 = (WsrmHeader)var4.next();
         var3.getHeaders().add(var5);
      }

      if (var2 != null && this.getAddressingVersion() != null) {
         RelatesToHeader var6 = new RelatesToHeader(this.getAddressingVersion().relatesToTag, var2);
         var3.getHeaders().addOrReplace(var6);
      }

   }

   protected void handleMasterInstanceChange(boolean var1, boolean var2) {
      if (var1 && !var2) {
         this.stopListening();
      } else if (var2 && !var1) {
         this.startListening();
      }

   }

   private void startListening() {
      this.startListeningOnDispatchKey();
   }

   private void stopListening() {
      this.stopListeningOnDispatchKey();
   }

   public static void preventRemovalOfAsyncRequestContext(DestinationSequence var0, DestinationMessageInfo var1, Packet var2) {
      if (var2.getSatellite(AsyncTransportProviderPropertyBag.class) != null) {
         AsyncTransportProviderPropertyBag var3 = (AsyncTransportProviderPropertyBag)AsyncTransportProviderPropertyBag.propertySetRetriever.getFromPacket(var2);
         AsyncTransportProvider.RequestContextRemovalCallback var4 = var3.getRequestContextRemovalCallback();
         if (var4 != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine(var0 + " is preventing the removal of async request context for request msgId " + var1.getRelatesTo() + " by removing the removal callback from AsyncTransportProviderPropertyBag");
            }

            var4.stopUsingAsyncRequestContext();
            var3.setRequestContextRemovalCallback((AsyncTransportProvider.RequestContextRemovalCallback)null);
         }
      }

   }

   private class DispatchThread extends Thread {
      private boolean _quit;
      private Object _monitor;
      private boolean _fiberDone;

      private DispatchThread() {
         this._monitor = "DispatchThreadMonitor" + this.getName();
         this.setDaemon(true);
         this._quit = false;
      }

      public void quit() {
         synchronized(this) {
            this._quit = true;
         }

         this.interrupt();
      }

      public void run() {
         if (DestinationSequence.LOGGER.isLoggable(Level.FINE)) {
            DestinationSequence.LOGGER.fine("DispatchThread starting up: " + this);
         }

         boolean var1;
         synchronized(this) {
            var1 = this._quit;
         }

         while(!var1) {
            final Fiber var2 = null;
            String var3 = null;

            while(var2 == null && !var1) {
               try {
                  if (DestinationSequence.LOGGER.isLoggable(Level.FINE)) {
                     DestinationSequence.LOGGER.fine(this + " taking next FiberBox from dispatch queue (this call may block)");
                  }

                  DispatchItem var5 = (DispatchItem)DestinationSequence.this._dispatchQueue.take();
                  FiberBox var6 = var5.fiberBox;
                  DestinationMessageInfo var7 = var5.msgInfo;
                  String var4 = var7.getMessageId();
                  var3 = "msgId=" + var4 + " msgNum=" + var7.getMessageNum() + " fiber=(" + var6 + ")";
                  if (DestinationSequence.LOGGER.isLoggable(Level.FINE)) {
                     DestinationSequence.LOGGER.fine(this + " got next item from dispatch queue " + var3 + ". Getting fiber from it (this call may block)");
                  }

                  var2 = var6.get();
                  if (DestinationSequence.LOGGER.isLoggable(Level.FINE)) {
                     DestinationSequence.LOGGER.fine(this + " got " + var3 + " from dispatch queue.");
                  }
               } catch (InterruptedException var13) {
               }

               synchronized(this) {
                  var1 = this._quit;
               }
            }

            if (!var1) {
               final Fiber.CompletionCallback var18 = var2.getCompletionCallback();
               Fiber.CompletionCallback var19 = new Fiber.CompletionCallback() {
                  public void onCompletion(@NotNull Packet var1) {
                     if (var18 != null) {
                        var18.onCompletion(var1);
                     }

                     if (!var2.isStartedSync()) {
                        this.signalDone();
                     }

                  }

                  public void onCompletion(@NotNull Throwable var1) {
                     if (var18 != null) {
                        var18.onCompletion(var1);
                     }

                     if (!var2.isStartedSync()) {
                        this.signalDone();
                     }

                  }

                  private void signalDone() {
                     synchronized(DispatchThread.this._monitor) {
                        DispatchThread.this._fiberDone = true;
                        DispatchThread.this._monitor.notify();
                     }
                  }
               };
               if (DestinationSequence.LOGGER.isLoggable(Level.FINE)) {
                  DestinationSequence.LOGGER.fine(this + " resuming " + var3 + " from dispatch queue");
               }

               synchronized(this._monitor) {
                  this._fiberDone = false;
                  var2.resume(var2.getPacket(), false, var19);
                  if (var2.isStartedSync()) {
                     if (DestinationSequence.LOGGER.isLoggable(Level.FINE)) {
                        DestinationSequence.LOGGER.fine(this + " completed execution of " + var3 + " from dispatch queue. Getting the next one...");
                     }
                  } else {
                     if (DestinationSequence.LOGGER.isLoggable(Level.FINE)) {
                        DestinationSequence.LOGGER.fine(this + " going inactive to allow completion callback to call us when it is ready");
                     }

                     while(!this._fiberDone) {
                        try {
                           this._monitor.wait();
                        } catch (Exception var15) {
                        }
                     }

                     if (DestinationSequence.LOGGER.isLoggable(Level.FINE)) {
                        DestinationSequence.LOGGER.fine(this + " detected the completion of " + var3 + " from dispatch queue. Getting the next one...");
                     }
                  }
               }
            }

            synchronized(this) {
               var1 = this._quit;
            }
         }

         if (DestinationSequence.LOGGER.isLoggable(Level.FINE)) {
            DestinationSequence.LOGGER.fine(this + " quiting: " + DestinationSequence.this);
         }

      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString());
         var1.append(" - seqId=").append(DestinationSequence.this.getId());
         return var1.toString();
      }

      // $FF: synthetic method
      DispatchThread(Object var2) {
         this();
      }
   }

   public interface CurrentFiberSuspendingCallback {
      void currentFiberSuspending();
   }

   public static class DestinationSequenceAddResult extends Sequence.AddResult {
      public boolean needSuspendOnCurrentFiber;
      public Fiber.Listener currentFiberSuspendingCallback;

      DestinationSequenceAddResult() {
      }

      DestinationSequenceAddResult(Sequence.AddResult var1) {
         super(var1);
      }
   }

   private class DispatchItem {
      FiberBox fiberBox;
      DestinationMessageInfo msgInfo;

      private DispatchItem(FiberBox var2, DestinationMessageInfo var3) {
         this.fiberBox = var2;
         this.msgInfo = var3;
      }

      // $FF: synthetic method
      DispatchItem(FiberBox var2, DestinationMessageInfo var3, Object var4) {
         this(var2, var3);
      }
   }
}
