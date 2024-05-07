package weblogic.management.j2ee.statistics;

import javax.management.j2ee.statistics.TimeStatistic;

public class TimeStatisticImpl extends StatisticImpl implements TimeStatistic {
   private long count = 0L;
   private long maxTime = 0L;
   private long minTime = 0L;
   private long totalTime = 0L;

   public TimeStatisticImpl(String var1, String var2, String var3, String var4) throws StatException {
      super(var1, var2, var3, var4);
   }

   public TimeStatisticImpl(String var1, String var2, String var3) throws StatException {
      super(var1, var2, var3);
   }

   public long getCount() {
      return this.count;
   }

   public long getMinTime() {
      return this.minTime;
   }

   public long getMaxTime() {
      return this.maxTime;
   }

   public long getTotalTime() {
      return this.totalTime;
   }

   public void setCount(long var1) throws StatException {
      this.count = var1;
   }

   public void setMinTime(long var1) throws StatException {
      this.minTime = var1;
   }

   public void setMaxTime(long var1) throws StatException {
      this.maxTime = var1;
   }

   public void setTotalTime(long var1) throws StatException {
      this.totalTime = var1;
   }
}
