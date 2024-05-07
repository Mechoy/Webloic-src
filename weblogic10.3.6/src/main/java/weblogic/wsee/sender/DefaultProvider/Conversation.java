package weblogic.wsee.sender.DefaultProvider;

import com.sun.istack.Nullable;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.wsee.WseePersistLogger;
import weblogic.wsee.WseeSenderLogger;
import weblogic.wsee.jaxws.framework.RetryDelayCalculator;
import weblogic.wsee.jaxws.persistence.PersistentMessage;
import weblogic.wsee.jaxws.persistence.PersistentMessageFactory;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.sender.api.ConversationStatusCallback;
import weblogic.wsee.sender.api.PermanentSendException;
import weblogic.wsee.sender.api.Resources;
import weblogic.wsee.sender.api.SendException;
import weblogic.wsee.sender.api.SendRequest;
import weblogic.wsee.sender.api.Sender;
import weblogic.wsee.sender.api.SenderNotReadyException;
import weblogic.wsee.sender.api.SendingServiceException;

public class Conversation {
   private static final Logger LOGGER = Logger.getLogger(Conversation.class.getName());
   private String _name;
   private Resources _resources;
   private State _state;
   private RetryDelayCalculator _sendDelayCalculator;
   private transient ReentrantReadWriteLock _stateLock = new ReentrantReadWriteLock(false);
   private transient RequestStore _store;
   private transient List<Long> _pendingRequestSeqNums;
   private transient Map<Long, RequestInfo> _pendingRequestInfos;
   private transient long _maxSeqNum;
   private final transient ReentrantReadWriteLock _pendingRequestsLock = new ReentrantReadWriteLock(false);
   private transient Sender _sender;
   private transient ConversationStatusCallback _callback;
   private transient Throwable _lastSendError;
   private transient ReentrantReadWriteLock _lastSendErrorLock = new ReentrantReadWriteLock(false);
   private transient boolean _sendingFlag;
   private final transient Object _sendingFlagMonitor = "_sendingFlagMonitor";
   private transient boolean _stopFlag;
   private final transient Object _stopFlagMonitor = "_stopFlagMonitor";
   private transient TimerManager _timerMgr;
   private transient Timer _sendTimer;
   private final transient Object _sendTimerMonitor = "_sendTimerMonitor";
   private int _improperTimerEventCount = 0;

   public Conversation() {
   }

   public Conversation(String var1, RequestStore var2, Resources var3) throws SendingServiceException {
      this._name = var1;
      this._resources = new Resources(var3);
      this.setState(Conversation.State.NEW);
      this.start(var2);
   }

   private void setState(State var1) {
      try {
         this._stateLock.writeLock().lock();
         this._state = var1;
      } finally {
         this._stateLock.writeLock().unlock();
      }

   }

   public State getState() {
      State var1;
      try {
         this._stateLock.readLock().lock();
         var1 = this._state;
      } finally {
         this._stateLock.readLock().unlock();
      }

      return var1;
   }

   void start(RequestStore var1) throws SendingServiceException {
      this._store = var1;

      long var5;
      try {
         this._pendingRequestsLock.writeLock().lock();
         List var2 = this._store.getPendingRequests(this._name);
         this._pendingRequestSeqNums = new ArrayList();
         this._pendingRequestInfos = new LinkedHashMap();
         this._maxSeqNum = 0L;
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            SendRequest var4 = (SendRequest)var3.next();
            var5 = var4.getSequenceNumber();
            this._pendingRequestSeqNums.add(var5);
            this._pendingRequestInfos.put(var5, new RequestInfo());
            if (var5 > this._maxSeqNum) {
               this._maxSeqNum = var5;
            }
         }
      } finally {
         this._pendingRequestsLock.writeLock().unlock();
      }

      this._sender = this._resources.getSenderFactory().createSender(this._name);
      this._callback = this._resources.getSenderFactory().getStatusCallback(this._name);
      this._timerMgr = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager();
      this.cancelSendTimer();
      Duration var14 = this._resources.getOptions().getBaseRetransmissionInterval();
      if (var14 == null) {
         try {
            var14 = DatatypeFactory.newInstance().newDuration("P0DT3S");
         } catch (Exception var12) {
            WseePersistLogger.logUnexpectedException(var12.toString(), var12);
            throw new RuntimeException(var12.toString(), var12);
         }
      }

      long var15 = var14.getTimeInMillis(new Date());
      var5 = var15 << 10;
      long var7 = this._resources.getOptions().isExponentialBackoffEnabled() ? 2L : 1L;
      this._sendDelayCalculator = new RetryDelayCalculator(var15, var5, (double)var7);
      Sender.ConversationCallback var9 = new Sender.ConversationCallback() {
         public void conversationReady() {
            try {
               Conversation.this._stateLock.readLock().lock();
               Conversation.this.clearLastSendError();
               if (Conversation.this._state == Conversation.State.NEW) {
                  Conversation.this._state = Conversation.State.READY;
                  Conversation.this.scheduleImmediateSend();
               }
            } finally {
               Conversation.this._stateLock.readLock().unlock();
            }

         }

         public void sendSucceeded(long var1) {
         }

         public boolean conversationFailedToStart(Throwable var1) {
            if (var1 instanceof PermanentSendException) {
               Conversation.this.closeConversationOnError(var1);
               return true;
            } else {
               Conversation.this.setLastSendError(var1);
               return false;
            }
         }

         public boolean sendFailed(long var1, Throwable var3) {
            if (var3 instanceof PermanentSendException) {
               Conversation.this.invalidateRequestOnError(var1, var3);
               return true;
            } else {
               Conversation.this.setLastSendError(var3);
               return false;
            }
         }
      };
      this._sender.setConversationCallback(var9);
   }

   public void addRequest(SendRequest var1) throws SendingServiceException {
      try {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Adding request " + var1 + " to pending requests for conversation " + this._name);
         }

         this._pendingRequestsLock.writeLock().lock();
         var1.setConversationName(this._name);
         if (var1.getSequenceNumber() < 1L) {
            var1.setSequenceNumber(++this._maxSeqNum);
         } else if (var1.getSequenceNumber() > this._maxSeqNum) {
            this._maxSeqNum = var1.getSequenceNumber();
         }

         this._pendingRequestSeqNums.add(var1.getSequenceNumber());
         this._pendingRequestInfos.put(var1.getSequenceNumber(), new RequestInfo());
         this._store.put(var1.getMessageId(), var1);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Added new request to conversation " + this._name + ". Current count = " + this._pendingRequestSeqNums.size());
         }

         this.scheduleImmediateSend();
      } catch (Exception var7) {
         this._pendingRequestSeqNums.remove(var1.getSequenceNumber());
         throw new SendingServiceException(var7.toString(), var7);
      } finally {
         this._pendingRequestsLock.writeLock().unlock();
      }

   }

   private void setLastSendError(Throwable var1) {
      try {
         this._lastSendErrorLock.writeLock().lock();
         this._lastSendError = var1;
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Set last send error on conversation " + this._name + ": " + var1);
         }
      } finally {
         this._lastSendErrorLock.writeLock().unlock();
      }

   }

   private void clearLastSendError() {
      this.setLastSendError((Throwable)null);
   }

   public Throwable getLastSendError() {
      Throwable var1;
      try {
         this._lastSendErrorLock.readLock().lock();
         var1 = this._lastSendError;
      } finally {
         this._lastSendErrorLock.readLock().unlock();
      }

      return var1;
   }

   public boolean hasSendError() {
      boolean var1;
      try {
         this._lastSendErrorLock.readLock().lock();
         var1 = this._lastSendError != null;
      } finally {
         this._lastSendErrorLock.readLock().unlock();
      }

      return var1;
   }

   public List<Long> getPendingRequestSeqNums() throws SendingServiceException {
      ArrayList var1;
      try {
         this._pendingRequestsLock.readLock().lock();
         var1 = new ArrayList(this._pendingRequestSeqNums);
      } catch (Exception var6) {
         throw new SendingServiceException(var6.toString(), var6);
      } finally {
         this._pendingRequestsLock.readLock().unlock();
      }

      return var1;
   }

   public SendRequest getRequestBySequenceNumber(long var1) throws SendingServiceException {
      SendRequest var3 = this._store.get(this._name, var1);
      if (var3 == null) {
         throw new SendingServiceException(WseeSenderLogger.logSendRequestWithSeqNumNotFoundLoggable(this._name, var1).getMessage());
      } else {
         return var3;
      }
   }

   public void acknowledgeRequests(long var1, long var3) throws SendingServiceException {
      try {
         this._pendingRequestsLock.writeLock().lock();
         ArrayList var5 = new ArrayList(this._pendingRequestSeqNums);
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            long var7 = (Long)var6.next();
            if (var7 >= var1 && var7 <= var3) {
               SendRequest var9 = this._store.get(this._name, var7);
               if (var9 == null) {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Request " + var7 + " has already been removed from pending requests for conversation " + this._name);
                  }
               } else {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Ack'ing/removing request " + var9 + " from pending requests for conversation " + this._name);
                  }

                  this._store.remove(var9.getObjectId());
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Removed ack'd request " + var9 + " from conversation " + this._name + ". Current count = " + this._pendingRequestSeqNums.size());
                  }
               }

               this._pendingRequestSeqNums.remove(var7);
               this._pendingRequestInfos.remove(var7);
            }
         }
      } catch (Exception var14) {
         throw new SendingServiceException(var14.toString(), var14);
      } finally {
         this._pendingRequestsLock.writeLock().unlock();
      }

   }

   public void closeConversation() throws SendException, StoreException {
      this.stop();
      this.setState(Conversation.State.CLOSED);
      this._store.removeAllPendingRequests(this._name);
   }

   public void cancelConversation() throws SendException, StoreException {
      ConversationCancelledExceptionImpl var1 = new ConversationCancelledExceptionImpl(this._name);
      var1.fillInStackTrace();
      this.closeConversationOnError(var1);
   }

   public void stop() throws SendException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("!! Stopping conversation: " + this._name + " and closing sender for it");
      }

      this.setState(Conversation.State.STOPPED);
      synchronized(this._stopFlagMonitor) {
         this._stopFlag = true;
      }

      this.cancelSendTimer();
      this._sender.close();
      this._sender = null;
   }

   void startSending() {
      this.scheduleImmediateSend();
   }

   private void scheduleFutureSend() {
      this.scheduleSendTimerDontUseDirectly(this._sendDelayCalculator.getNextRetryDelayMillis());
   }

   private void scheduleImmediateSend() {
      if (this.isSending()) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Bypassing immediate send due to already sending. Conversation name: " + this._name);
         }

      } else if (this.hasSendError()) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Bypassing immediate send due to previous send failure. Waiting for next scheduled send. Conversation name: " + this._name);
         }

         this.scheduleFutureSend();
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Scheduling immediate send. Conversation name: " + this._name);
         }

         this.scheduleSendTimerDontUseDirectly(0L);
      }
   }

   private void scheduleSendTimerDontUseDirectly(long var1) {
      synchronized(this._sendTimerMonitor) {
         if (this._sendTimer == null || !this.hasSendError() && var1 == 0L) {
            this.cancelSendTimer();
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Setting send timer into future by " + var1 + " ms");
            }

            this._sendTimer = this._timerMgr.schedule(new TimerListener() {
               public void timerExpired(Timer var1) {
                  synchronized(Conversation.this._sendTimerMonitor) {
                     Conversation.this._sendTimer = null;
                  }

                  if (Conversation.this._sender == null) {
                     Conversation.this._improperTimerEventCount++;
                     String var2 = "Timer still active in Conversation that has been stopped!. This has happened " + Conversation.this._improperTimerEventCount + " times in conversation: " + Conversation.this._name;
                     if (Conversation.LOGGER.isLoggable(Level.FINE)) {
                        Conversation.LOGGER.fine(var2);
                     }

                  } else {
                     Conversation.this.sendPendingRequests();
                  }
               }
            }, var1);
         }

      }
   }

   private boolean isSending() {
      synchronized(this._sendingFlagMonitor) {
         return this._sendingFlag;
      }
   }

   private void sendPendingRequests() {
      synchronized(this._sendingFlagMonitor) {
         if (this._sendingFlag) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Aborting sendPendingRequests because we are already in sendPendingRequests on another thread");
            }

            return;
         }

         this._sendingFlag = true;
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("In sendPendingRequests for conversation " + this._name);
         }
      }

      try {
         this.cancelSendTimer();
         boolean var1;
         synchronized(this._stopFlagMonitor) {
            var1 = this._stopFlag;
         }

         long var2 = -1L;

         while(!var1) {
            Long[] var4 = this.getNextRequestInfoBatch(var2);
            if (var4.length < 1) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("No more pending requests found for this pass, sender going back to sleep");
               }
               break;
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine(var4.length + " pending requests found for this pass");
            }

            Long[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Long var8 = var5[var7];
               var2 = var8;

               RequestInfo var9;
               try {
                  this._pendingRequestsLock.readLock().lock();
                  var9 = (RequestInfo)this._pendingRequestInfos.get(var8);
                  if (var9 == null) {
                     if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Ignoring send of request with seqNum " + var8 + " as it is no longer managed by this Conversation (probably has been ack'd/removed)");
                     }
                     continue;
                  }

                  var9.request = this._store.get(this._name, var8);
               } finally {
                  this._pendingRequestsLock.readLock().unlock();
               }

               if (var9.request == null) {
                  var1 = true;
                  break;
               }

               Sender.SendResult var10 = this.sendOneRequest(var9);
               if (var10 == Sender.SendResult.FAILURE) {
                  if (this.getLastSendError() instanceof PermanentSendException || this.getLastSendError() instanceof SenderNotReadyException) {
                     var1 = true;
                     break;
                  }

                  if (this._resources.getOptions().isInOrder()) {
                     var1 = true;
                     break;
                  }
               } else {
                  this.markRequestSent(var9.seqNum, var9, var10);
               }

               synchronized(this._stopFlagMonitor) {
                  var1 = this._stopFlag;
               }
            }
         }
      } finally {
         synchronized(this._sendingFlagMonitor) {
            this._sendingFlag = false;
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Finished with sendPendingRequests for conversation: " + this._name);
            }

            this.scheduleFutureSend();
         }
      }

   }

   private void markRequestSent(long var1, @Nullable RequestInfo var3, Sender.SendResult var4) {
      if (this.getState() != Conversation.State.READY) {
         this.setState(Conversation.State.READY);
      }

      this.clearLastSendError();
      if (var3 != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Successful send for request " + var1 + " on conversation " + this._name + ". Pending request count: " + this._pendingRequestSeqNums.size());
         }

         var3.lastSendTime = System.currentTimeMillis();
         var3.lastSendDelay = this._sendDelayCalculator.getCurrentRetryDelayMillis();
      }

   }

   private void cancelSendTimer() {
      synchronized(this._sendTimerMonitor) {
         if (this._sendTimer != null) {
            this._sendTimer.cancel();
            this._sendTimer = null;
            this._improperTimerEventCount = 0;
         }

      }
   }

   private Sender.SendResult sendOneRequest(RequestInfo var1) {
      final SendRequest var2 = var1.request;
      synchronized(var2) {
         Sender.SendResult var3;
         try {
            AuthenticatedSubject var5 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
            PrivilegedExceptionAction var14 = new PrivilegedExceptionAction<Sender.SendResult>() {
               public Sender.SendResult run() throws Exception {
                  return Conversation.this._sender.send(var2);
               }
            };
            PersistentMessage var7 = (PersistentMessage)var2.getPayload();
            if (var7 != null && var7.getContext() != null) {
               var3 = (Sender.SendResult)PersistentMessageFactory.getInstance().runActionInContext(var7.getContext(), var5, var14);
            } else {
               var3 = this._sender.send(var2);
            }

            this.setLastSendError((Throwable)null);
            this._sendDelayCalculator.reset();
         } catch (PermanentSendException var11) {
            this.setLastSendError(var11);
            boolean var6;
            synchronized(this._stopFlagMonitor) {
               var6 = this._stopFlag;
            }

            if (!var6) {
               this.invalidateRequestOnError(var2.getSequenceNumber(), var11);
            } else if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Got send error on request " + var2 + " for stopped conversation " + this._name + ": " + var11.toString());
            }

            var3 = Sender.SendResult.FAILURE;
         } catch (Throwable var12) {
            this.setLastSendError(var12);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Got send error on request " + var2 + " for conversation " + this._name + ": " + var12.toString());
            }

            var3 = Sender.SendResult.FAILURE;
         }

         return var3;
      }
   }

   private Long[] getNextRequestInfoBatch(long var1) {
      Long[] var3;
      try {
         this._pendingRequestsLock.readLock().lock();
         if (!this._pendingRequestSeqNums.isEmpty()) {
            LinkedList var14 = new LinkedList();
            Iterator var4 = this._pendingRequestSeqNums.iterator();

            while(var4.hasNext()) {
               long var5 = (Long)var4.next();
               if (var5 > var1) {
                  RequestInfo var7 = (RequestInfo)this._pendingRequestInfos.get(var5);
                  var7.request = this._store.get(this._name, var5);
                  if (var7.lastSendTime > 0L && this.getState() == Conversation.State.READY) {
                     long var8 = System.currentTimeMillis() - var7.lastSendTime;
                     if (var8 < var7.lastSendDelay) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                           LOGGER.fine("Bypassing send of request " + var7.request + " on conversation " + this._name + " as it was sent before too recently: (" + var8 + " ms ago)");
                        }
                        continue;
                     }
                  } else if (this.getState() != Conversation.State.READY && LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Ignoring 'last send' times on all requests in this conversation because of it's state: " + this.getState() + ". Will honor 'last send' times when the conversation is READY");
                  }

                  var14.add(var5);
               }
            }

            Long[] var15 = (Long[])var14.toArray(new Long[var14.size()]);
            return var15;
         }

         var3 = new Long[0];
      } finally {
         this._pendingRequestsLock.readLock().unlock();
      }

      return var3;
   }

   private void closeConversationOnError(Throwable var1) {
      if (this.getState() != Conversation.State.CLOSED && this.getState() != Conversation.State.STOPPED) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Closing conversation " + this._name + " on permanent/fatal error: " + var1.toString());
         }

         try {
            try {
               this._pendingRequestsLock.readLock().lock();
               if (this._pendingRequestSeqNums.size() > 0) {
                  Iterator var2 = this._pendingRequestSeqNums.iterator();

                  while(var2.hasNext()) {
                     long var3 = (Long)var2.next();
                     SendRequest var5 = this._store.get(this._name, var3);

                     try {
                        this._callback.deliveryFailure(var5, Arrays.asList(var1));
                     } catch (Exception var22) {
                        WseeSenderLogger.logUnexpectedException(var22.toString(), var22);
                     }
                  }
               }
            } finally {
               this._pendingRequestsLock.readLock().unlock();
            }
         } finally {
            try {
               this.closeConversation();
            } catch (Exception var21) {
               WseeSenderLogger.logUnexpectedException(var21.toString(), var21);
            }

         }

      }
   }

   private void invalidateRequestOnError(long var1, Throwable var3) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Invalidating msg num " + var1 + " on conversation " + this._name + " due to permanent/fatal error: " + var3.toString());
      }

      SendRequest var4 = this._store.get(this._name, var1);
      if (var4 != null) {
         try {
            this._callback.deliveryFailure(var4, Arrays.asList(var3));
         } catch (Exception var7) {
            WseeSenderLogger.logUnexpectedException(var7.toString(), var7);
         }

         try {
            this.acknowledgeRequests(var1, var1);
         } catch (Exception var6) {
            WseeSenderLogger.logUnexpectedException(var6.toString(), var6);
         }
      } else if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.info("Didn't find msg num " + var1 + " while trying to invalidate it on conversation " + this._name + " due to permanent/fatal error: " + var3.toString());
      }

   }

   private class RequestInfo {
      int pos;
      long seqNum;
      long lastSendTime;
      long lastSendDelay;
      SendRequest request;

      private RequestInfo() {
      }

      // $FF: synthetic method
      RequestInfo(Object var2) {
         this();
      }
   }

   public static enum State {
      NEW,
      READY,
      CLOSED,
      STOPPED;
   }
}
