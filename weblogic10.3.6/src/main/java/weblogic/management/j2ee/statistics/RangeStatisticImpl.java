package weblogic.management.j2ee.statistics;

import javax.management.j2ee.statistics.RangeStatistic;

public class RangeStatisticImpl extends StatisticImpl implements RangeStatistic {
   private long current = 0L;
   private long highWaterMark = 0L;
   private long lowWaterMark = 0L;

   public RangeStatisticImpl(String var1, String var2, String var3, String var4) throws StatException {
      super(var1, var2, var3, var4);
   }

   public RangeStatisticImpl(String var1, String var2, String var3) throws StatException {
      super(var1, var2, var3);
   }

   public long getCurrent() {
      return this.current;
   }

   public long getLowWaterMark() {
      return this.lowWaterMark;
   }

   public long getHighWaterMark() {
      return this.highWaterMark;
   }

   public void setCurrent(long var1) throws StatException {
      this.current = var1;
   }

   public void setLowWaterMark(long var1) throws StatException {
      this.lowWaterMark = var1;
   }

   public void setHighWaterMark(long var1) throws StatException {
      this.highWaterMark = var1;
   }
}
