package weblogic.jms.utils.tracing;

import java.util.concurrent.atomic.AtomicInteger;

public class AggregationCounter {
   SubBuffer dataArea;
   private AtomicInteger[] values;

   public AggregationCounter(String var1, int var2) {
      this.dataArea = DataLog.newDataArea("AGGREGATION-" + var1, var2 * 4);
      this.values = new AtomicInteger[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.values[var3] = new AtomicInteger();
      }

   }

   public void increment(int var1) {
      if (var1 > this.values.length) {
         throw new AssertionError("I'm FUCKED");
      } else {
         int var2 = this.values[var1].addAndGet(1);
         this.dataArea.putInt(var1 * 4, var2);
      }
   }
}
