package weblogic.messaging.saf.internal;

import java.util.Date;
import weblogic.messaging.saf.OperationState;

public final class RemoteEndpointRuntimeCommonAddition {
   private long downtimeHigh;
   private long downtimeTotal;
   private long uptimeHigh;
   private long uptimeTotal;
   private long lastTimeDisconnected;
   private long lastTimeConnected;
   private boolean connected;
   private Exception lastException;
   private OperationState expireAllState;

   public RemoteEndpointRuntimeCommonAddition() {
      long var1 = System.currentTimeMillis();
      this.lastTimeConnected = var1;
      this.lastTimeDisconnected = var1 - 1L;
      this.connected = true;
      this.expireAllState = OperationState.COMPLETED;
   }

   public boolean isConnected() {
      return this.connected;
   }

   public synchronized void updateLastTimeConnected(long var1) {
      if (this.lastTimeConnected == 0L) {
         this.lastTimeConnected = var1;
      } else {
         if (this.lastTimeConnected <= this.lastTimeDisconnected) {
            this.lastTimeConnected = var1;
         }

      }
   }

   public synchronized void updateLastTimeDisconnected(long var1, Exception var3) {
      this.lastException = var3;
      if (this.lastTimeDisconnected == 0L) {
         this.lastTimeDisconnected = var1;
      } else {
         if (this.lastTimeDisconnected <= this.lastTimeConnected) {
            this.lastTimeDisconnected = var1;
         }

      }
   }

   public void connected() {
      long var1 = System.currentTimeMillis();
      synchronized(this) {
         if (!this.connected) {
            this.lastException = null;
            this.connected = true;
            this.updateLastTimeConnected(var1);
            if (this.downtimeHigh < this.lastTimeConnected - this.lastTimeDisconnected) {
               this.downtimeHigh = this.lastTimeConnected - this.lastTimeDisconnected;
            }

            this.downtimeTotal += this.lastTimeConnected - this.lastTimeDisconnected;
         }
      }
   }

   public void disconnected(Exception var1) {
      long var2 = System.currentTimeMillis();
      synchronized(this) {
         if (this.connected) {
            this.connected = false;
            this.updateLastTimeDisconnected(var2, var1);
            if (this.uptimeHigh < this.lastTimeDisconnected - this.lastTimeConnected) {
               this.uptimeHigh = this.lastTimeDisconnected - this.lastTimeConnected;
            }

            this.uptimeTotal += this.lastTimeDisconnected - this.lastTimeConnected;
         }
      }
   }

   public long getDowntimeHigh() {
      long var1 = System.currentTimeMillis();
      synchronized(this) {
         return !this.connected && this.downtimeHigh < var1 - this.lastTimeDisconnected ? (var1 - this.lastTimeDisconnected) / 1000L : this.downtimeHigh / 1000L;
      }
   }

   public long getDowntimeTotal() {
      long var1 = System.currentTimeMillis();
      synchronized(this) {
         return !this.connected ? (this.downtimeTotal + var1 - this.lastTimeDisconnected) / 1000L : this.downtimeTotal / 1000L;
      }
   }

   public long getUptimeHigh() {
      long var1 = System.currentTimeMillis();
      synchronized(this) {
         return this.connected && this.uptimeHigh < var1 - this.lastTimeConnected ? (var1 - this.lastTimeConnected) / 1000L : this.uptimeHigh / 1000L;
      }
   }

   public long getUptimeTotal() {
      long var1 = System.currentTimeMillis();
      synchronized(this) {
         return this.connected ? (this.uptimeTotal + var1 - this.lastTimeConnected) / 1000L : this.uptimeTotal / 1000L;
      }
   }

   public synchronized Date getLastTimeConnected() {
      return new Date(this.lastTimeConnected);
   }

   public synchronized Date getLastTimeFailedToConnect() {
      return new Date(this.lastTimeDisconnected);
   }

   public synchronized Exception getLastException() {
      return this.lastException;
   }

   public OperationState getOperationState() {
      return this.expireAllState;
   }

   public void setOperationState(OperationState var1) {
      this.expireAllState = var1;
   }
}
