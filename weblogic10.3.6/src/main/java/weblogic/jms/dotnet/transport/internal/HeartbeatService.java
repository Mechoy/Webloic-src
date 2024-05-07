package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.ReceivedOneWay;
import weblogic.jms.dotnet.transport.SendHandlerOneWay;
import weblogic.jms.dotnet.transport.ServiceOneWay;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;

class HeartbeatService implements ServiceOneWay, TimerListener {
   static final boolean debug = false;
   private long heartbeatInterval;
   private int allowedMissedBeats;
   private int sentHeartbeatNumber = 0;
   private int recvHeartbeatNumber = -1;
   private int recvHeartbeatNumberOld = -1;
   private int missedHeartbeatCount = 0;
   private Transport transport;
   private Timer heartbeatTimer;
   private RunningState runningState;
   private HeartbeatNumberLock heartbeatNumberLock;
   private MissedHeartbeatLock missedHeartbeatLock;
   private HeartbeatStateLock stateLock;
   private TimerManager timerManager;

   HeartbeatService(int var1, int var2, Transport var3) {
      this.runningState = HeartbeatService.RunningState.INIT;
      this.heartbeatNumberLock = new HeartbeatNumberLock();
      this.missedHeartbeatLock = new MissedHeartbeatLock();
      this.stateLock = new HeartbeatStateLock();
      this.heartbeatInterval = (long)var1;
      if (this.heartbeatInterval < 1000L) {
         this.heartbeatInterval = 1000L;
      }

      this.allowedMissedBeats = var2;
      if (this.allowedMissedBeats < 1) {
         this.allowedMissedBeats = 1;
      }

      this.transport = var3;
      this.timerManager = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager();
   }

   void startHeartbeat() {
      synchronized(this.stateLock) {
         if (this.runningState == HeartbeatService.RunningState.INIT) {
            try {
               this.heartbeatTimer = this.timerManager.scheduleAtFixedRate(this, 0L, this.heartbeatInterval);
            } catch (IllegalStateException var4) {
               this.runningState = HeartbeatService.RunningState.SHUTDOWN;
               return;
            }

            this.runningState = HeartbeatService.RunningState.RUNNING;
         }
      }
   }

   void stopHeartbeat() {
      synchronized(this.stateLock) {
         if (this.runningState == HeartbeatService.RunningState.RUNNING) {
            this.heartbeatTimer.cancel();
         }

         this.runningState = HeartbeatService.RunningState.SHUTDOWN;
      }
   }

   void resetMissCounter() {
      synchronized(this.missedHeartbeatLock) {
         this.missedHeartbeatCount = 0;
      }
   }

   public void timerExpired(Timer var1) {
      synchronized(this.heartbeatNumberLock) {
         this.sendHeartbeatMessage();
         this.checkReceivedRemoteHeartbeat();
      }
   }

   private void sendHeartbeatMessage() {
      SendHandlerOneWay var1 = this.transport.createOneWay(10001L);
      HeartbeatRequest var2 = new HeartbeatRequest(this.sentHeartbeatNumber);
      var1.send(var2);
      ++this.sentHeartbeatNumber;
   }

   private void checkReceivedRemoteHeartbeat() {
      if (this.recvHeartbeatNumber != this.recvHeartbeatNumberOld) {
         this.resetMissCounter();
         this.recvHeartbeatNumberOld = this.recvHeartbeatNumber;
      } else {
         synchronized(this.missedHeartbeatLock) {
            ++this.missedHeartbeatCount;
            if (this.missedHeartbeatCount > this.allowedMissedBeats) {
               TransportError var2 = new TransportError(new Exception("Closing stale connection:  Missed " + this.missedHeartbeatCount + " heartbeat messages " + " with a heartbeat interval of " + this.heartbeatInterval + "ms."));
               ((TransportImpl)this.transport).shutdown(var2);
            }
         }
      }
   }

   public void invoke(ReceivedOneWay var1) {
      MarshalReadable var2 = var1.getRequest();
      HeartbeatRequest var3 = (HeartbeatRequest)var2;
      synchronized(this.heartbeatNumberLock) {
         this.recvHeartbeatNumber = var3.getHeartbeatNumber();
      }
   }

   public void onPeerGone(TransportError var1) {
      this.stopHeartbeat();
   }

   public void onShutdown() {
      this.stopHeartbeat();
   }

   public void onUnregister() {
      this.stopHeartbeat();
   }

   private void debug(String var1) {
   }

   private static enum RunningState {
      INIT,
      RUNNING,
      SHUTDOWN;
   }
}
