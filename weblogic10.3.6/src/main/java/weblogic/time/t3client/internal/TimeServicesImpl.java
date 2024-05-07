package weblogic.time.t3client.internal;

import weblogic.common.DisconnectEvent;
import weblogic.common.DisconnectListener;
import weblogic.common.T3Client;
import weblogic.common.T3Exception;
import weblogic.common.T3ServicesDef;
import weblogic.time.common.Schedulable;
import weblogic.time.common.ScheduledTriggerDef;
import weblogic.time.common.Scheduler;
import weblogic.time.common.TimeServicesDef;
import weblogic.time.common.TimeTriggerException;
import weblogic.time.common.Trigger;
import weblogic.time.common.Triggerable;
import weblogic.time.common.internal.ScheduledTrigger;
import weblogic.time.common.internal.TimeEventGenerator;

public final class TimeServicesImpl implements TimeServicesDef, DisconnectListener {
   T3Client t3;
   T3ServicesDef svc;
   private static final boolean verbose = false;
   boolean needsInitSync = true;
   int roundTripDelayMillis = 0;
   int localClockOffsetMillis = 0;
   long serverTimeDiff = 0L;
   Object initLock = new Object();
   private TimeEventGenerator teg = null;

   public TimeServicesImpl(T3ServicesDef var1, T3Client var2) {
      this.t3 = var2;
      this.svc = var1;
      var2.addDisconnectListener(this);
   }

   public ScheduledTriggerDef getScheduledTrigger(Schedulable var1, Triggerable var2) {
      if (this.teg == null) {
         this.teg = TimeEventGenerator.getOne();
      }

      return new ScheduledTrigger(var1, var2, this.teg);
   }

   public ScheduledTriggerDef getScheduledTrigger(Scheduler var1, Trigger var2) throws TimeTriggerException {
      if (var1.theObject() != null && var2.theObject() != null) {
         var1.private_initialize(this.svc);
         var2.private_initialize(this.svc);
         return this.getScheduledTrigger((Schedulable)var1.theObject(), (Triggerable)var2.theObject());
      } else {
         return new weblogic.time.t3client.ScheduledTrigger(var1, var2, this.t3);
      }
   }

   public void disconnectOccurred(DisconnectEvent var1) {
      if (this.teg != null) {
         this.teg.stop();
      }

   }

   private void initTimeSync() throws T3Exception {
      if (this.needsInitSync) {
         synchronized(this.initLock) {
            if (this.needsInitSync) {
               this.needsInitSync = false;

               try {
                  TimeMsg var2 = (TimeMsg)this.t3.sendRecv("weblogic.time.t3client.internal.TimeProxy", (new TimeMsg()).doPing());
                  long var3 = (var2.t2 - var2.t1 + (var2.t3 - var2.t4)) / 2L;
                  long var5 = var2.t4 - var2.t1 - (var2.t2 - var2.t3);
                  this.localClockOffsetMillis = (int)var3;
                  this.roundTripDelayMillis = (int)var5;
                  this.serverTimeDiff = var2.t2 - var2.t1 - var5 / 2L;
               } catch (T3Exception var8) {
                  if (var8.getNestedException() instanceof T3Exception) {
                     throw (T3Exception)var8.getNestedException();
                  }

                  throw new T3Exception("" + var8.getNestedException());
               }
            }
         }
      }

   }

   public long currentTimeMillis() throws T3Exception {
      this.initTimeSync();
      return System.currentTimeMillis() + this.serverTimeDiff;
   }

   public int getRoundTripDelayMillis() throws T3Exception {
      this.initTimeSync();
      return this.roundTripDelayMillis;
   }

   public int getLocalClockOffsetMillis() throws T3Exception {
      this.initTimeSync();
      return this.localClockOffsetMillis;
   }
}
