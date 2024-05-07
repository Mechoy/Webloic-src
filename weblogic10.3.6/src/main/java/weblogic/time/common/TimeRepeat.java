package weblogic.time.common;

import java.util.Date;
import weblogic.common.T3MiscLogger;

/** @deprecated */
public class TimeRepeat implements Schedulable {
   private long intervalMillis = 0L;
   private long lasttime = 0L;
   private long nexttime = 0L;
   private long firstTime = 0L;

   public TimeRepeat() {
   }

   /** @deprecated */
   public TimeRepeat(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Argument cannot be negative(or rolled over ) " + var1);
      } else {
         this.intervalMillis = (long)var1;
      }
   }

   /** @deprecated */
   public TimeRepeat(long var1) {
      this.intervalMillis = var1;
   }

   /** @deprecated */
   public long lastTime() {
      return this.lasttime;
   }

   /** @deprecated */
   public long schedule(long var1) {
      this.lasttime = var1;
      if (this.nexttime == 0L && this.firstTime != 0L) {
         if (this.firstTime < var1) {
            try {
               T3MiscLogger.logPastTime((new Date(this.firstTime)).toString());
            } catch (Exception var4) {
            }
         }

         this.nexttime = this.firstTime;
         return this.firstTime;
      } else {
         if (this.nexttime == 0L) {
            this.nexttime = var1;
         }

         do {
            this.nexttime += this.intervalMillis;
         } while(this.nexttime < var1);

         return this.nexttime;
      }
   }

   /** @deprecated */
   public void setFirstScheduleTime(long var1) {
      this.firstTime = var1;
   }

   /** @deprecated */
   public long getFirstScheduleTime() {
      return this.firstTime;
   }
}
