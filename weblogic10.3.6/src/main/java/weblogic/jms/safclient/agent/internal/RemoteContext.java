package weblogic.jms.safclient.agent.internal;

import weblogic.jms.forwarder.Forwarder;
import weblogic.jms.forwarder.RuntimeHandler;
import weblogic.messaging.kernel.Queue;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.work.WorkManager;

public final class RemoteContext {
   private String name;
   private Forwarder forwarder;

   public RemoteContext(String var1) {
      this.name = var1;
      this.forwarder = new Forwarder(false, new ReplyHandlerImpl(), new ClientEnvironmentFactoryImpl());
   }

   public String getName() {
      return this.name;
   }

   public void setCompressionThreshold(int var1) {
      this.forwarder.setCompressionThreshold(var1);
   }

   public void setLoginURL(String var1) {
      this.forwarder.setLoginURL(var1);
   }

   public void setUsername(String var1) {
      this.forwarder.setUsername(var1);
   }

   public void setPassword(String var1) {
      this.forwarder.setPassword(var1);
   }

   public void setRetryDelayBase(long var1) {
      this.forwarder.setRetryDelayBase(var1);
   }

   public void setRetryDelayMaximum(long var1) {
      this.forwarder.setRetryDelayMaximum(var1);
   }

   public void setRetryDelayMultiplier(double var1) {
      this.forwarder.setRetryDelayMultiplier(var1);
   }

   public void setWindowSize(int var1) {
      this.forwarder.setWindowSize(var1);
   }

   public void setWindowInterval(int var1) {
      this.forwarder.setWindowInterval((long)var1);
   }

   public void addForwarder(PersistentStoreXA var1, WorkManager var2, RuntimeHandler var3, Queue var4, String var5, int var6) {
      this.forwarder.addSubforwarder(var1, var2, var3, var4, var5, var6);
   }

   public void shutdown() {
      this.forwarder.stop();
      this.forwarder = null;
   }

   public String toString() {
      return "RemoteContext(" + this.name + ")";
   }
}
