package weblogic.time.t3client;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.T3Client;
import weblogic.common.T3Exception;
import weblogic.time.common.ScheduledTriggerDef;
import weblogic.time.common.Scheduler;
import weblogic.time.common.TimeTriggerException;
import weblogic.time.common.Trigger;
import weblogic.time.t3client.internal.TimeMsg;
import weblogic.work.WorkManager;

public final class ScheduledTrigger implements ScheduledTriggerDef, Externalizable {
   static final long serialVersionUID = 265454191653137961L;
   private transient T3Client t3;
   private Scheduler scheduler;
   private Trigger trigger;
   private transient int regID = -1;
   private transient boolean isDaemon = false;

   public ScheduledTrigger(Scheduler var1, Trigger var2, T3Client var3) {
      this.scheduler = var1;
      this.trigger = var2;
      this.t3 = var3;
   }

   public ScheduledTrigger() {
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.scheduler = (Scheduler)var1.readObject();
      this.trigger = (Trigger)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.scheduler);
      var1.writeObject(this.trigger);
   }

   public void initialize() {
      this.scheduler = null;
      this.trigger = null;
      this.t3 = null;
   }

   public void destroy() {
      this.scheduler = null;
      this.trigger = null;
   }

   public Trigger getTrigger() {
      return this.trigger;
   }

   public Scheduler getScheduler() {
      return this.scheduler;
   }

   public int schedule() throws TimeTriggerException {
      String var1 = "(unknown)";

      try {
         this.regID = (Integer)this.t3.sendRecv("weblogic.time.t3client.internal.TimeProxy", (new TimeMsg()).doSchedule(this, this.isDaemon));
         if (this.isDaemon) {
            this.setDaemon(this.isDaemon);
         }
      } catch (T3Exception var3) {
         if (var3.getNestedException() instanceof TimeTriggerException) {
            throw (TimeTriggerException)var3.getNestedException();
         }

         throw new TimeTriggerException("" + var3.getNestedException());
      }

      return this.regID;
   }

   public static boolean cancel(T3Client var0, int var1) throws TimeTriggerException {
      try {
         Boolean var2 = (Boolean)var0.sendRecv("weblogic.time.t3client.internal.TimeProxy", (new TimeMsg()).doCancel(var1));
         return var2;
      } catch (T3Exception var3) {
         if (var3.getNestedException() instanceof TimeTriggerException) {
            throw (TimeTriggerException)var3.getNestedException();
         } else {
            throw new TimeTriggerException("" + var3.getNestedException());
         }
      }
   }

   public void setDaemon(boolean var1) throws TimeTriggerException {
      if (this.regID == -1) {
         this.isDaemon = var1;
      } else {
         try {
            this.t3.sendRecv("weblogic.time.t3client.internal.TimeProxy", (new TimeMsg()).doSetDaemon(var1, this.regID));
            this.isDaemon = var1;
         } catch (T3Exception var3) {
            if (var3.getNestedException() instanceof TimeTriggerException) {
               throw (TimeTriggerException)var3.getNestedException();
            }

            throw new TimeTriggerException("" + var3.getNestedException());
         }
      }

   }

   public boolean isDaemon() {
      return this.isDaemon;
   }

   public void setWorkManager(WorkManager var1) {
   }

   public boolean cancel() throws TimeTriggerException {
      return cancel(this.t3, this.regID);
   }
}
