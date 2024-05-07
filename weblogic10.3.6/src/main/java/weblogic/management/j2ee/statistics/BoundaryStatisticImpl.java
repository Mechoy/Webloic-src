package weblogic.management.j2ee.statistics;

import javax.management.j2ee.statistics.BoundaryStatistic;

public class BoundaryStatisticImpl extends StatisticImpl implements BoundaryStatistic {
   private long lowerBound = 0L;
   private long upperBound = 0L;

   public BoundaryStatisticImpl(String var1, String var2, String var3, String var4) throws StatException {
      super(var1, var2, var3, var4);
   }

   public BoundaryStatisticImpl(String var1, String var2, String var3) throws StatException {
      super(var1, var2, var3);
   }

   public long getLowerBound() {
      return this.lowerBound;
   }

   public long getUpperBound() {
      return this.upperBound;
   }

   public void setUpperBound(long var1) throws StatException {
      this.upperBound = var1;
   }

   public void setLowerBound(long var1) throws StatException {
      this.lowerBound = var1;
   }
}
