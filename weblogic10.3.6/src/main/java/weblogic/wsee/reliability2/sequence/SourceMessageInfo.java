package weblogic.wsee.reliability2.sequence;

import com.sun.xml.ws.api.pipe.Fiber;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SourceMessageInfo extends MessageInfo implements Serializable {
   private static final Logger LOGGER = Logger.getLogger(SourceMessageInfo.class.getName());
   private static final long serialVersionUID = 1L;
   long _responseMsgNum;
   private ClientInvokeInfo _clientInvokeInfo;

   public SourceMessageInfo(String var1, String var2, long var3, String var5) {
      super(var1, var2, var3, var5);
   }

   public SourceMessageInfo(SourceMessageInfo var1) {
      super(var1);
      this._responseMsgNum = var1._responseMsgNum;
      this._clientInvokeInfo = var1._clientInvokeInfo;
   }

   public long getResponseMessageNum() {
      long var1;
      try {
         this.getLock().readLock().lock();
         var1 = this._responseMsgNum;
      } finally {
         this.getLock().readLock().unlock();
      }

      return var1;
   }

   public void setResponseMessageNum(long var1) {
      PropertyChangeEvent var3 = null;

      try {
         this.getLock().writeLock().lock();
         if (this._responseMsgNum != var1) {
            var3 = new PropertyChangeEvent(this, "responseMessageNum", this._responseMsgNum, var1);
            this._responseMsgNum = var1;
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Set source message " + this.getMessageId() + " seq: " + this.getSequenceId() + " msgNum: " + this.getMessageNum() + " with response message num " + var1);
            }
         }
      } finally {
         this.getLock().writeLock().unlock();
      }

      if (var3 != null) {
         this.fireEvent(var3);
      }

   }

   public ClientInvokeInfo getClientInvokeInfo() {
      return this._clientInvokeInfo;
   }

   public void setClientInvokeInfo(ClientInvokeInfo var1) {
      this._clientInvokeInfo = var1;
   }

   public static class ClientInvokeInfo implements Serializable {
      private static final long serialVersionUID = 1L;
      private transient Fiber _suspendedRequestFiber;
      private boolean _syncMEP;
      private boolean _oneWay;
      private boolean _usingAsyncClientHandlerFeature;

      public ClientInvokeInfo(Fiber var1, boolean var2, boolean var3, boolean var4) {
         this._suspendedRequestFiber = var1;
         this._syncMEP = var2;
         this._oneWay = var3;
         this._usingAsyncClientHandlerFeature = var4;
      }

      public boolean impliesSuspendedRequestFiber() {
         return this._syncMEP || !this._usingAsyncClientHandlerFeature;
      }

      public Fiber getSuspendedRequestFiber() {
         return this._suspendedRequestFiber;
      }

      public Fiber getAndClearSuspendedRequestFiber() {
         Fiber var1 = this.getSuspendedRequestFiber();
         this._suspendedRequestFiber = null;
         return var1;
      }

      public boolean isSyncMEP() {
         return this._syncMEP;
      }

      public boolean isOneWay() {
         return this._oneWay;
      }

      public boolean isUsingAsyncClientHandlerFeature() {
         return this._usingAsyncClientHandlerFeature;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString());
         var1.append(" Fiber=").append(this.getSuspendedRequestFiber());
         var1.append(" SyncMEP=").append(this._syncMEP);
         var1.append(" OneWay=").append(this._oneWay);
         var1.append(" AsyncClientHandler=").append(this._usingAsyncClientHandlerFeature);
         return var1.toString();
      }
   }
}
