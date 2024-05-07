package weblogic.application;

import weblogic.utils.Debug;
import weblogic.work.ShutdownCallback;

public final class AdminModeCompletionBarrier {
   private static final boolean debug = Debug.getCategory("weblogic.application.AdminModeBarrier").isEnabled();
   private static final int STATE_REGISTERING = 1;
   private static final int STATE_REGISTRATION_COMPLETE = 2;
   private final Deployment.AdminModeCallback callback;
   private int state = 1;
   private int pendingCallbacks = 0;

   public AdminModeCompletionBarrier(Deployment.AdminModeCallback var1) {
      this.callback = var1;
   }

   private String state2String(int var1) {
      switch (var1) {
         case 1:
            return "STATE_REGISTERING";
         case 2:
            return "STATE_REGISTRATION_COMPLETE";
         default:
            throw new AssertionError("unexpected state " + var1);
      }
   }

   private void callbackCompleted() {
      boolean var1;
      synchronized(this) {
         --this.pendingCallbacks;
         if (debug) {
            Debug.say("callbackCompleted " + this + ", pendingCallbacks=" + this.pendingCallbacks);
            Debug.stackdump();
         }

         var1 = this.pendingCallbacks == 0 && this.state == 2;
      }

      if (var1) {
         this.callback.completed();
      }

   }

   private synchronized void registerCallback() {
      if (this.state != 1) {
         throw new AssertionError("Unexpected state in registerCallback " + this.state2String(this.state));
      } else {
         ++this.pendingCallbacks;
         if (debug) {
            Debug.say("registerCallback " + this + ", pendingCallbacks=" + this.pendingCallbacks);
            Debug.stackdump();
         }

      }
   }

   public ShutdownCallback registerWMShutdown() {
      this.registerCallback();
      return new ShutdownCallback() {
         public void completed() {
            AdminModeCompletionBarrier.this.callbackCompleted();
         }
      };
   }

   public void registrationComplete() {
      boolean var1;
      synchronized(this) {
         if (this.state != 1) {
            throw new AssertionError("Unexpected state in registrationComplete " + this.state2String(this.state));
         }

         this.state = 2;
         var1 = this.pendingCallbacks == 0;
         if (debug) {
            Debug.say("registrationComplete " + this + ", pendingCallbacks=" + this.pendingCallbacks);
         }
      }

      if (var1) {
         this.callback.completed();
      }

   }
}
