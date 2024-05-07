package weblogic.wsee.reliability;

public final class MessageRange implements Comparable {
   public final long lowerBounds;
   public final long upperBounds;

   public MessageRange(long var1, long var3) {
      this.lowerBounds = var1;
      this.upperBounds = var3;
   }

   public int compareTo(Object var1) {
      MessageRange var2 = (MessageRange)var1;
      if (this.lowerBounds > var2.lowerBounds) {
         return 1;
      } else if (this.lowerBounds < var2.lowerBounds) {
         return -1;
      } else if (this.upperBounds > var2.upperBounds) {
         return 1;
      } else {
         return this.upperBounds < var2.upperBounds ? -1 : 0;
      }
   }
}
