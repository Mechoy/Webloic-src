package weblogic.time.server;

import weblogic.rjvm.PeerGoneEvent;
import weblogic.rjvm.PeerGoneListener;
import weblogic.rjvm.RJVM;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.EndPoint;
import weblogic.time.common.Schedulable;
import weblogic.time.common.TimeTriggerException;
import weblogic.time.common.Triggerable;
import weblogic.time.common.internal.TimeEventGenerator;
import weblogic.work.WorkManager;

public final class ScheduledTrigger extends weblogic.time.common.internal.ScheduledTrigger implements PeerGoneListener {
   private ClassLoader classLoader;
   private EndPoint clientEndPoint;

   public ScheduledTrigger(Schedulable var1, Triggerable var2) {
      super(var1, var2);
   }

   public ScheduledTrigger(Schedulable var1, Triggerable var2, WorkManager var3) {
      super(var1, var2, var3);
   }

   public ScheduledTrigger(Schedulable var1, Triggerable var2, TimeEventGenerator var3) {
      super(var1, var2, var3);
   }

   protected void execute() {
      if (this.classLoader != null) {
         Thread.currentThread().setContextClassLoader(this.classLoader);
      }

      try {
         super.execute();
      } finally {
         Thread.currentThread().setContextClassLoader((ClassLoader)null);
      }

   }

   public boolean cancel() throws TimeTriggerException {
      this.destroy();
      return super.cancel();
   }

   protected void destroy() {
      if (this.clientEndPoint != null && this.clientEndPoint instanceof RJVM) {
         ((RJVM)this.clientEndPoint).removePeerGoneListener(this);
      }

      this.clientEndPoint = null;
      this.classLoader = null;
   }

   public int schedule() throws TimeTriggerException {
      if (!this.isDaemon()) {
         this.clientEndPoint = ServerHelper.getClientEndPointInternal();
         if (this.clientEndPoint != null && this.clientEndPoint instanceof RJVM) {
            ((RJVM)this.clientEndPoint).addPeerGoneListener(this);
         }
      }

      this.classLoader = Thread.currentThread().getContextClassLoader();
      return super.schedule();
   }

   public void peerGone(PeerGoneEvent var1) {
      try {
         this.cancel();
      } catch (TimeTriggerException var3) {
      }

   }

   public String toString() {
      return "Scheduled Trigger";
   }
}
