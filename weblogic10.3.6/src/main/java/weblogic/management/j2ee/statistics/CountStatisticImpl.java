package weblogic.management.j2ee.statistics;

import javax.management.j2ee.statistics.CountStatistic;

public class CountStatisticImpl extends StatisticImpl implements CountStatistic {
   private long count = 0L;

   public CountStatisticImpl(String var1, String var2, String var3, String var4) throws StatException {
      super(var1, var2, var3, var4);
   }

   public CountStatisticImpl(String var1, String var2, String var3) throws StatException {
      super(var1, var2, var3);
   }

   public long getCount() {
      return this.count;
   }

   public void setCount(long var1) throws StatException {
      this.count = var1;
   }
}
