package weblogic.wsee.jws.container;

import java.util.Date;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManagerFactory;
import weblogic.wsee.conversation.LockManager;
import weblogic.wsee.jws.conversation.ConversationTimeout;
import weblogic.wsee.jws.conversation.Store;
import weblogic.wsee.jws.conversation.StoreManager;
import weblogic.wsee.util.PathServiceUtil;
import weblogic.wsee.util.Verbose;

class ConversationTimeoutListener implements TimerListener {
   private static final boolean verbose = Verbose.isVerbose(ConversationTimeoutListener.class);
   private static final String TIMER_MANAGER_NAME = "weblogic.wsee.ConversationTimeoutManager";
   private ConversationTimeout to;
   private String id;
   private boolean cancelled;
   private Timer timer;

   ConversationTimeoutListener(String var1, ConversationTimeout var2) {
      this.id = var1;
      this.to = var2;
      if (verbose) {
         Verbose.say("Created ConversationTimeoutListener " + var1 + " of type " + var2.getEventType() + " for conversation: " + var2.getConversationID());
      }

   }

   synchronized void schedule() {
      TimerManager var1 = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.wsee.ConversationTimeoutManager", WorkManagerFactory.getInstance().getSystem());
      this.timer = var1.schedule(this, new Date(this.to.getTime()));
      if (verbose) {
         Verbose.say("Scheduled timer for ConversationTimeoutListener " + this.id);
      }

   }

   synchronized void cancel() {
      this.cancelled = true;
      this.to = null;
      if (this.timer != null) {
         this.timer.cancel();
         this.timer = null;
      }

      if (verbose) {
         Verbose.say("Cancelled ConversationTimeoutListener " + this.id);
      }

   }

   public String getId() {
      return this.id;
   }

   public void timerExpired(Timer var1) {
      synchronized(this) {
         if (this.cancelled) {
            return;
         }
      }

      if (verbose) {
         Verbose.log((Object)("JWS timeout for ConversationTimeoutListener: " + this.id));
      }

      ConversationalContainer.removeSerializedTimeoutListener(this.id);
      LockManager.Lock var2 = null;

      try {
         var2 = LockManager.getInstance().lock(this.to.getConversationID());
         Store var3 = StoreManager.getStore(this.to.getStoreConfig());
         ConversationalContainer var4 = (ConversationalContainer)var3.readForUpdate(this.to.getConversationID());
         if (var4 != null) {
            this.notify(var4);
         }
      } catch (Exception var10) {
         throw new RuntimeException("Exception in timeout", var10);
      } finally {
         if (var2 != null) {
            var2.release();
         }

      }

   }

   private void notify(ConversationalContainer var1) throws Exception {
      if (verbose) {
         Verbose.log((Object)("timeout notified - " + this.to.getConversationID()));
         Verbose.log((Object)this.to);
         Verbose.log((Object)("_maxAgeTime     = " + var1.getMaxAgeTime()));
         Verbose.log((Object)("_maxIdleSeconds = " + var1.getMaxIdleSeconds()));
         Verbose.log((Object)("_lastReqTime    = " + var1.getLastReqTime()));
      }

      switch (this.to.getEventType()) {
         case EVENT_AGE_TIMEOUT:
            this.handleAgeTimeout(var1);
            break;
         case EVENT_IDLE_TIMEOUT:
            this.handleIdleTimeout(var1);
            break;
         default:
            System.out.println("Unhandled event: " + this.to);
      }

   }

   private void handleIdleTimeout(ConversationalContainer var1) throws Exception {
      if (var1.getMaxIdleSeconds() > 0L && this.to.getTime() - var1.getLastReqTime() >= var1.getMaxIdleSeconds() * 1000L) {
         var1.setState(1);
         var1.getListeners().onIdleTimeout(this.to.getTime());
         var1.finish();
         this.updateStore(var1);
      }

   }

   private void handleAgeTimeout(ConversationalContainer var1) throws Exception {
      if (var1.getMaxAgeTime() > 0L && this.to.getTime() >= var1.getMaxAgeTime()) {
         var1.setState(2);
         var1.getListeners().onAgeTimeout(this.to.getTime() - var1.getStartTime());
         var1.finish();
         this.updateStore(var1);
      }

   }

   private void updateStore(ConversationalContainer var1) throws Exception {
      Store var2 = StoreManager.getStore(this.to.getStoreConfig());
      var2.delete(var1.getId());

      try {
         PathServiceUtil.removeConversationIdMapping(var1.getId());
      } catch (Throwable var4) {
      }

   }
}
