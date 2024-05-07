package weblogic.wsee.reliability2.sequence;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.Fiber;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.jws.jaxws.client.async.FiberBox;
import weblogic.wsee.jaxws.framework.WsUtil;

public class DestinationMessageInfo extends MessageInfo implements Serializable {
   private static final Logger LOGGER = Logger.getLogger(DestinationMessageInfo.class.getName());
   private static final long serialVersionUID = 1L;
   private static final long NEW_MSG_NUM = Long.MIN_VALUE;
   private static final long RECEIVED_MSG_NUM = -2L;
   private static final long IN_PROCESS_MSG_NUM = -1L;
   private static final long NO_RESPONSE_MSG_NUM = 0L;
   private long _responseMsgNum;
   private String _relatesTo;
   private transient FiberBox _suspendedFiber;
   private String _suspendedFiberId;
   private boolean _suspendedFiberAddedToMap = false;
   private static ConcurrentHashMap<String, FiberBox> _suspendedFiberMap = new ConcurrentHashMap(60000);

   private void initTransients() {
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      try {
         this.getLock().readLock().lock();
         var1.defaultWriteObject();
         if (this._suspendedFiber != null && !this._suspendedFiberAddedToMap) {
            Fiber var2 = this._suspendedFiber.peek();
            final Fiber.CompletionCallback var3 = var2.getCompletionCallback();
            var2.setCompletionCallback(new Fiber.CompletionCallback() {
               public void onCompletion(@NotNull Packet var1) {
                  this.finish();
                  if (var3 != null) {
                     var3.onCompletion(var1);
                  }

               }

               public void onCompletion(@NotNull Throwable var1) {
                  this.finish();
                  if (var3 != null) {
                     var3.onCompletion(var1);
                  }

               }

               private void finish() {
                  if (DestinationMessageInfo.LOGGER.isLoggable(Level.FINE)) {
                     DestinationMessageInfo.LOGGER.fine("Removing fiber " + DestinationMessageInfo.this._suspendedFiber.peek().toString() + " from suspendedFiberMap. Remaining count: " + DestinationMessageInfo._suspendedFiberMap.size());
                  }

                  DestinationMessageInfo._suspendedFiberMap.remove(DestinationMessageInfo.this._suspendedFiberId);
               }
            });
            _suspendedFiberMap.put(this._suspendedFiberId, this._suspendedFiber);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Added fiber " + this._suspendedFiber.peek().toString() + " to suspendedFiberMap. Current count: " + _suspendedFiberMap.size());
            }

            this._suspendedFiberAddedToMap = true;
         }
      } finally {
         this.getLock().readLock().unlock();
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.initTransients();

      try {
         this.getLock().writeLock().lock();
         var1.defaultReadObject();
         if (this._suspendedFiberId != null) {
            this._suspendedFiber = (FiberBox)_suspendedFiberMap.get(this._suspendedFiberId);
            if (this._suspendedFiber == null) {
               if (LOGGER.isLoggable(Level.INFO)) {
                  LOGGER.info("Unable to find suspended fiber for serialized (and likely buffered) DestinationMessageInfo for msgId: " + this.getMessageId() + ". This message cannot be processed further.");
               }

               this._suspendedFiberId = null;
            } else if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Got fiber " + this._suspendedFiber.peek().toString() + " from suspendedFiberMap. Current count: " + _suspendedFiberMap.size());
            }
         }
      } finally {
         this.getLock().writeLock().unlock();
      }

   }

   public DestinationMessageInfo(String var1, String var2, long var3, String var5) {
      super(var1, var2, var3, var5);
      this._responseMsgNum = Long.MIN_VALUE;
      this._suspendedFiber = null;
      this._suspendedFiberId = null;
      this.initTransients();
   }

   public DestinationMessageInfo(DestinationMessageInfo var1) {
      super(var1);
      this._responseMsgNum = var1._responseMsgNum;
      this._suspendedFiber = var1._suspendedFiber;
      this._suspendedFiberId = var1._suspendedFiberId;
      this.initTransients();
   }

   public long getResponseMsgNum() {
      long var1;
      try {
         this.getLock().readLock().lock();
         var1 = this._responseMsgNum;
      } finally {
         this.getLock().readLock().unlock();
      }

      return var1;
   }

   public void setResponseMsgNum(long var1) {
      if (this.internalSetResponseMsgNum(var1) && LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Set destination message " + this.getMessageId() + " seq: " + this.getSequenceId() + " msgNum: " + this.getMessageNum() + " with response message num " + var1);
      }

   }

   public String getRelatesTo() {
      String var1;
      try {
         this.getLock().readLock().lock();
         var1 = this._relatesTo;
      } finally {
         this.getLock().readLock().unlock();
      }

      return var1;
   }

   public void setRelatesTo(String var1) {
      try {
         this.getLock().writeLock().lock();
         this._relatesTo = var1;
      } finally {
         this.getLock().writeLock().unlock();
      }

   }

   public boolean isNew() {
      boolean var1;
      try {
         this.getLock().readLock().lock();
         var1 = this._responseMsgNum == Long.MIN_VALUE;
      } finally {
         this.getLock().readLock().unlock();
      }

      return var1;
   }

   public void setReceived() {
      if (this.internalSetResponseMsgNum(-2L) && LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Set destination message " + this.getMessageId() + " seq: " + this.getSequenceId() + " msgNum: " + this.getMessageNum() + " RECEIVED");
      }

   }

   public boolean isReceived() {
      boolean var1;
      try {
         this.getLock().readLock().lock();
         var1 = this._responseMsgNum == -2L;
      } finally {
         this.getLock().readLock().unlock();
      }

      return var1;
   }

   public void setInProcess() {
      if (this.internalSetResponseMsgNum(-1L) && LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Set destination message " + this.getMessageId() + " seq: " + this.getSequenceId() + " msgNum: " + this.getMessageNum() + " IN_PROCESS");
      }

   }

   private boolean internalSetResponseMsgNum(long var1) {
      PropertyChangeEvent var3 = null;

      try {
         this.getLock().writeLock().lock();
         if (var1 > this._responseMsgNum) {
            var3 = new PropertyChangeEvent(this, "responseMessageNum", this._responseMsgNum, var1);
            this._responseMsgNum = var1;
         } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Bypassing set of responseMsgNum on seq " + this.getSequenceId() + " msg num " + this.getMessageNum() + " msg id " + this.getMessageId() + " to " + var1 + " because this is less than the current value of " + this._responseMsgNum);
         }
      } finally {
         this.getLock().writeLock().unlock();
      }

      if (var3 != null) {
         this.fireEvent(var3);
         return true;
      } else {
         return false;
      }
   }

   public boolean isInProcess() {
      boolean var1;
      try {
         this.getLock().readLock().lock();
         var1 = this._responseMsgNum == -1L;
      } finally {
         this.getLock().readLock().unlock();
      }

      return var1;
   }

   public void setNoResponse() {
      if (this.internalSetResponseMsgNum(0L) && LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Set destination message " + this.getMessageId() + " seq: " + this.getSequenceId() + " msgNum: " + this.getMessageNum() + " NO_RESPONSE");
      }

   }

   public boolean isNoResponse() {
      boolean var1;
      try {
         this.getLock().readLock().lock();
         var1 = this._responseMsgNum == 0L;
      } finally {
         this.getLock().readLock().unlock();
      }

      return var1;
   }

   public FiberBox getSuspendedFiber() {
      FiberBox var1;
      try {
         this.getLock().readLock().lock();
         var1 = this._suspendedFiber;
      } finally {
         this.getLock().readLock().unlock();
      }

      return var1;
   }

   public void setSuspendedFiber(FiberBox var1) {
      try {
         this.getLock().writeLock().lock();
         this._suspendedFiber = var1;
         this._suspendedFiberAddedToMap = false;
         this._suspendedFiberId = WsUtil.generateUUID();
      } finally {
         this.getLock().writeLock().unlock();
      }

   }
}
