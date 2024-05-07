package weblogic.wsee.reliability2.sequence;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.framework.RetryDelayCalculator;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.jaxws.persistence.PersistentRequestContext;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentity;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmSecurityContext;
import weblogic.wsee.reliability.headers.AckRequestedHeader;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.exception.WsrmExceptionUtil;
import weblogic.wsee.reliability2.store.SenderDispatchFactory;
import weblogic.wsee.reliability2.tube.DispatchFactory;
import weblogic.wsee.reliability2.tube.DispatchFactoryNotReadyException;
import weblogic.wsee.reliability2.tube.DispatchFactoryResolver;
import weblogic.wsee.reliability2.tube.Sender;

public class SourceSequence extends Sequence<SourceMessageInfo> implements Serializable, DispatchFactoryResolver.LifecycleListener {
   private static final long serialVersionUID = 1L;
   private static final Logger LOGGER = Logger.getLogger(SourceSequence.class.getName());
   private String _destinationId;
   private transient WSEndpointReference _endpointEpr;
   private transient WSEndpointReference _acksToEpr;
   private String _offerSequenceId;
   private DestinationOfferSequence _offerSequence;
   private long _lastAllocatedMessageNum;
   private boolean _complete;
   private SenderDispatchFactory.Key _senderDispatchKey;
   private ClientInstanceIdentity _creatingClientInstanceId;
   private Duration _baseRetransmissionInterval;
   private boolean _exponentialBackoffEnabled;
   private PersistentRequestContext _firstRequestContext;
   private transient TimerManager _timerMgr;
   private transient Timer _ackTimer = null;
   private transient RetryDelayCalculator _ackRequestDelayCalculator;

   private void initTransients() {
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject("10.3.6");
      WsUtil.serializeWSEndpointReference(this._endpointEpr, var1);
      WsUtil.serializeWSEndpointReference(this._acksToEpr, var1);
      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.initTransients();
      var1.readObject();
      this._endpointEpr = WsUtil.deserializeWSEndpointReference(var1, this.getAddressingVersion());
      this._acksToEpr = WsUtil.deserializeWSEndpointReference(var1, this.getAddressingVersion());
      var1.defaultReadObject();
      this.initAckRequestDelayCalculator();
   }

   public SourceSequence(String var1, String var2, WsrmConstants.RMVersion var3, AddressingVersion var4, SOAPVersion var5, WsrmSecurityContext var6, boolean var7, PersistentRequestContext var8) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.initTransients();
      this._firstRequestContext = var8;

      try {
         Duration var9 = DatatypeFactory.newInstance().newDuration("PT3S");
         this.setBaseRetransmissionInterval(var9);
      } catch (Exception var10) {
         WseeRmLogger.logUnexpectedException(var10.toString(), var10);
      }

      this._destinationId = null;
      this._endpointEpr = null;
      this._acksToEpr = null;
      this._offerSequenceId = null;
      this._complete = false;
   }

   public SourceMessageInfo copyMessageInfo(SourceMessageInfo var1) {
      return new SourceMessageInfo(var1);
   }

   public boolean markMessageAckd(SourceMessageInfo var1, boolean var2) {
      boolean var3 = false;
      if (var1.isAck() != var2) {
         var1.setAck(var2);
         this.markChanged();
         var3 = true;
      }

      this._ackRequestDelayCalculator.reset();
      return var3;
   }

   public String getSourceId() {
      return this.getId();
   }

   public String getDestinationId() {
      return this._destinationId;
   }

   public WSEndpointReference getEndpointEpr() {
      return this._endpointEpr;
   }

   public WSEndpointReference getAcksToEpr() {
      return this._acksToEpr;
   }

   public WSEndpointReference getPiggybackEpr() {
      return this.getEndpointEpr();
   }

   public DestinationOfferSequence getOffer() {
      if (this._offerSequence != null && this._offerSequenceId == null) {
         return this._offerSequence;
      } else {
         this._offerSequence = null;
         if (this._offerSequenceId == null) {
            return null;
         } else {
            try {
               return (DestinationOfferSequence)DestinationSequenceManager.getInstance().getSequence(this.getRmVersion(), this._offerSequenceId, true);
            } catch (Exception var2) {
               throw new RuntimeException(var2.toString(), var2);
            }
         }
      }
   }

   public long getNextMessageNum(SequenceHeader var1) {
      boolean var2 = false;

      long var3;
      try {
         this.getMessageLock().writeLock().lock();
         ++this._lastAllocatedMessageNum;
         var2 = true;
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("*** Last allocated message num for sequence " + this.getId() + " increased to " + this._lastAllocatedMessageNum);
         }

         var1.setMessageNumber(this._lastAllocatedMessageNum);
         var3 = this._lastAllocatedMessageNum;
      } finally {
         this.getMessageLock().writeLock().unlock();
         if (var2) {
            this.markChanged();
         }

      }

      return var3;
   }

   public boolean isComplete() {
      return this._complete;
   }

   public void setDestinationId(String var1) {
      this._destinationId = var1;
      this.markChanged();
   }

   public void setEndpointEpr(WSEndpointReference var1) {
      this._endpointEpr = var1;
      this.setUsingSsl(HeaderList.isUsingSsl(var1));
      this.markChanged();
   }

   public void setAcksToEpr(WSEndpointReference var1) {
      this._acksToEpr = var1;
      this.markChanged();
   }

   public void setOffer(DestinationOfferSequence var1) {
      this._offerSequenceId = var1 != null ? var1.getId() : null;
      this.markChanged();
   }

   void setComplete(boolean var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Marking sequence COMPLETE: " + this);
      }

      this._complete = var1;
      this.markChanged();
   }

   public PersistentRequestContext getFirstRequestContext() {
      return this._firstRequestContext;
   }

   public SenderDispatchFactory.Key getSenderDispatchKey() {
      return this._senderDispatchKey;
   }

   public void setSenderDispatchKey(SenderDispatchFactory.Key var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Setting sender dispatch key to '" + var1 + "' for seq: " + this);
      }

      this._senderDispatchKey = var1;
      this.markChanged();
   }

   public ClientInstanceIdentity getCreatingClientInstanceId() {
      return this._creatingClientInstanceId;
   }

   public void setCreatingClientInstanceId(ClientInstanceIdentity var1) {
      this._creatingClientInstanceId = var1;
   }

   public Duration getBaseRetransmissionInterval() {
      return this._baseRetransmissionInterval;
   }

   public void setBaseRetransmissionInterval(Duration var1) {
      this._baseRetransmissionInterval = var1;
      this.initAckRequestDelayCalculator();
      this.markChanged();
   }

   private void initAckRequestDelayCalculator() {
      long var1 = this._baseRetransmissionInterval.getTimeInMillis(new Date());
      var1 *= 3L;
      long var3 = var1 << 10;
      long var5 = this._exponentialBackoffEnabled ? 2L : 1L;
      this._ackRequestDelayCalculator = new RetryDelayCalculator(var1, var3, (double)var5);
   }

   public boolean isExponentialBackoffEnabled() {
      return this._exponentialBackoffEnabled;
   }

   public void setExponentialBackoffEnabled(boolean var1) {
      this._exponentialBackoffEnabled = var1;
      this.initAckRequestDelayCalculator();
      this.markChanged();
   }

   protected void handleMasterInstanceChange(boolean var1, boolean var2) {
      if (var1 && !var2) {
         this.stopListening();
         this.cancelAckTimer();
      } else if (var2 && !var1) {
         this.startListening();
         this.resetAckTimer();
      }

   }

   private void cancelAckTimer() {
      try {
         this.getMessageLock().writeLock().lock();
         if (this._ackTimer != null) {
            this._ackTimer.cancel();
            this._ackTimer = null;
         }
      } finally {
         this.getMessageLock().writeLock().unlock();
      }

   }

   private void startListening() {
      this.startListeningOnDispatchKey();
   }

   private void stopListening() {
      this.stopListeningOnDispatchKey();
   }

   private void startListeningOnDispatchKey() {
      if (this._senderDispatchKey != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SourceSequence " + this + " starting listen on dispatch key: " + this._senderDispatchKey);
         }

         this._senderDispatchKey.addLifecycleListener(this);
      }

   }

   private void stopListeningOnDispatchKey() {
      if (this._senderDispatchKey != null) {
         this._senderDispatchKey.removeLifecycleListener(this);
      }

   }

   public void onDispatchFactoryDispose(DispatchFactory var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(this.getClass().getName() + " " + this.getId() + " detected its DispatchFactory key is being disposed. Making any necessary ack requests before the key becomes invalid");
      }

      if (this.getUnackdCount() > 0L) {
         try {
            this.deliverAckRequest();
         } catch (Exception var3) {
            WseeRmLogger.logUnexpectedException(var3.toString(), var3);
         }
      }

      this._senderDispatchKey.removeLifecycleListener(this);
      this.setSenderDispatchKey((SenderDispatchFactory.Key)null);
   }

   protected void setUnackedCount(long var1) {
      super.setUnackedCount(var1);
      this.resetAckTimer();
   }

   public void startup() {
      SequenceState var1 = this.getState();

      try {
         this.getMessageLock().writeLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            long var2 = this.getUnackdCount();
            LOGGER.fine("Starting up " + this + " in state " + var1 + (var2 > 0L ? " WITH " + var2 + " UNACK'D REQUESTS" : ""));
         }

         super.startup();
         this.resetAckTimer();
      } finally {
         this.getMessageLock().writeLock().unlock();
      }

   }

   public void shutdown() {
      SequenceState var1 = this.getState();

      try {
         this.getMessageLock().writeLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            long var2 = this.getUnackdCount();
            LOGGER.fine("Shutting down " + this + " in state " + var1 + (var2 > 0L ? " WITH " + var2 + " UNACK'D REQUESTS" : ""));
         }

         this.cancelAckTimer();
         super.shutdown();
      } finally {
         this.getMessageLock().writeLock().unlock();
      }

   }

   public void resetAckTimer() {
      try {
         SequenceState var1 = this.getState();
         this.getMessageLock().writeLock().lock();
         if (var1 == SequenceState.CREATING || var1 == SequenceState.CREATED) {
            if (this._timerMgr == null) {
               TimerManagerFactory var2 = TimerManagerFactory.getTimerManagerFactory();
               this._timerMgr = var2.getDefaultTimerManager();
            }

            long var8 = this.getUnackdCount();
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Found unack'd request count for source sequence " + this + " as: " + var8);
            }

            boolean var4 = var8 > 0L;
            this.cancelAckTimer();
            if (var4) {
               if (this._endpointEpr != null && !this._endpointEpr.isAnonymous()) {
                  this.scheduleAckTimer();
               } else if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Cancelling ack request timer (anonymous endpoint EPR)");
                  return;
               }

               return;
            } else {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Cancelled ack request timer (no unack'd requests) for sequence: " + this.getId());
                  return;
               }

               return;
            }
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Aborting request to reset ack request timer for sequence " + this + " to endpoint " + this._endpointEpr + " using dispatch key: " + this._senderDispatchKey + " because the sequence is not ready (state=" + var1 + ")");
         }
      } finally {
         this.getMessageLock().writeLock().unlock();
      }

   }

   private void scheduleAckTimer() {
      long var1 = this._ackRequestDelayCalculator.getNextRetryDelayMillis();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Scheduling ack request timer (" + var1 + " ms timer) for sequence: " + this.getId());
      }

      this._ackTimer = this._timerMgr.schedule(new TimerListener() {
         public void timerExpired(Timer var1) {
            try {
               final WorkManager var2 = WorkManagerFactory.getInstance().getSystem();
               var2.schedule(new Runnable() {
                  public void run() {
                     try {
                        SourceSequence.this.deliverAckRequest();
                     } catch (Exception var2x) {
                        if (SourceSequence.LOGGER.isLoggable(Level.FINE)) {
                           SourceSequence.LOGGER.fine("Failed to deliver ack request message: " + var2x.toString());
                           SourceSequence.LOGGER.log(Level.FINE, var2x.toString(), var2x);
                        }

                        if (WsrmExceptionUtil.isPermanentSendFailure(var2x)) {
                           WseeRmLogger.logUnexpectedException(var2x.toString(), var2x);
                        }
                     }

                     var2.schedule(new Runnable() {
                        public void run() {
                           SourceSequence.this.resetAckTimer();
                        }
                     });
                  }
               });
            } catch (Exception var3) {
               if (SourceSequence.LOGGER.isLoggable(Level.FINE)) {
                  SourceSequence.LOGGER.fine("Failed to deliver ack request message: " + var3.toString());
                  SourceSequence.LOGGER.log(Level.FINE, var3.toString(), var3);
               }

               WseeRmLogger.logUnexpectedException(var3.toString(), var3);
            }

         }
      }, var1);
   }

   private void deliverAckRequest() throws Exception {
      if ((this._endpointEpr == null || this._endpointEpr.isAnonymous()) && LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Aborting request to send ack request message for sequence " + this + " because the target endpoint is anonymous");
      }

      SequenceState var1 = this.getState();
      if ((var1 == SequenceState.CREATED || var1 == SequenceState.CLOSING || var1 == SequenceState.CLOSED) && this.getDestinationId() != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Sending ack request message for sequence " + this + " dest ID: " + this.getDestinationId() + " to endpoint " + this._endpointEpr + " using dispatch key: " + this._senderDispatchKey);
         }

         if (this._senderDispatchKey != null) {
            DispatchFactory var2;
            try {
               var2 = this._senderDispatchKey.resolve();
            } catch (DispatchFactoryNotReadyException var8) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine(var8.toString());
               }

               return;
            }

            String var3 = WsrmConstants.Action.ACK_REQUESTED.getActionURI(this.getRmVersion());
            Message var4 = WsUtil.createEmptyMessage(var3, this._endpointEpr.getVersion(), this.getSoapVersion());
            AckRequestedHeader var5 = new AckRequestedHeader(this.getRmVersion());
            var5.setSequenceId(this.getDestinationId());
            var4.getHeaders().addOrReplace(var5);
            Sender var6 = new Sender(var2);
            Map var7 = SourceSequenceManager.getInstance().getSenderInvokeProperties((SourceSequence)this, (Packet)null);
            var6.send(var4, var3, this._endpointEpr, this._acksToEpr, var7);
         }

      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Aborting request to send ack request message for sequence " + this + " to endpoint " + this._endpointEpr + " using dispatch key: " + this._senderDispatchKey + " because the sequence is not ready (state=" + var1 + ")");
         }

      }
   }
}
