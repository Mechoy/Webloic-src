package weblogic.t3.srvr;

import weblogic.server.ServerLogger;
import weblogic.utils.StackTraceUtils;

class GracefulShutdownRequest implements Runnable {
   private Exception exception;
   private boolean isCompleted = false;
   private Object syncObj = new Object();
   private boolean ignoreSessions = false;
   private int destinationState;

   GracefulShutdownRequest(boolean var1) {
      this.ignoreSessions = var1;
      this.destinationState = 0;
   }

   GracefulShutdownRequest(boolean var1, int var2) {
      this.ignoreSessions = var1;
      this.destinationState = var2;
   }

   public Exception getException() {
      return this.exception;
   }

   public boolean isCompleted() {
      return this.isCompleted;
   }

   public void run() {
      try {
         if (this.destinationState == 17) {
            T3Srvr.getT3Srvr().gracefulSuspend(this.ignoreSessions);
         } else {
            T3Srvr.getT3Srvr().gracefulShutdown(this.ignoreSessions);
         }
      } catch (RuntimeException var4) {
         ServerLogger.logServerRuntimeError(var4.toString());
         ServerLogger.logServerRuntimeError(StackTraceUtils.throwable2StackTrace(var4));
         this.exception = var4;
      } catch (Exception var5) {
         this.exception = var5;
      }

      this.isCompleted = true;
      synchronized(this.syncObj) {
         this.syncObj.notify();
      }
   }

   public void waitForCompletion(int var1) {
      if (!this.isCompleted) {
         synchronized(this.syncObj) {
            if (!this.isCompleted) {
               try {
                  this.syncObj.wait((long)var1);
               } catch (InterruptedException var5) {
               }
            }
         }
      }

   }
}
