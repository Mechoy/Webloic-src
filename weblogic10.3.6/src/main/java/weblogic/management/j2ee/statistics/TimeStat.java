package weblogic.management.j2ee.statistics;

import java.io.Serializable;

public class TimeStat implements Serializable {
   private long startTime;
   private long lastSampleTime;

   public TimeStat(long var1, long var3) throws StatException {
      String var5 = "";
      if (var1 <= 0L) {
         var5 = var5 + "Start time is less than 0. Start time = " + var1;
      }

      if (var3 <= 0L) {
         var5 = var5 + "\n Last sample time is less than 0. Last sample time = " + var3;
      }

      if (var5.length() > 0) {
         throw new StatException(var5);
      } else {
         this.startTime = var1;
         this.lastSampleTime = var3;
      }
   }

   public synchronized void setLastSampleTime(long var1) throws StatException {
      if (var1 <= 0L) {
         throw new StatException("Last sample time is less than 0. Last sample time = " + var1);
      } else {
         this.lastSampleTime = var1;
      }
   }

   public long getStartTime() {
      return this.startTime;
   }

   public synchronized long getLastSampleTime() {
      return this.lastSampleTime;
   }
}
