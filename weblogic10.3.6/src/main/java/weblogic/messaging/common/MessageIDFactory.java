package weblogic.messaging.common;

public final class MessageIDFactory {
   private int seed = MessagingUtilities.getSeed();
   private long timestamp = System.currentTimeMillis();
   private int counter;

   final void initMessageId(MessageIDImpl var1) {
      long var2;
      int var4;
      synchronized(this) {
         var2 = System.currentTimeMillis();
         if (var2 == this.timestamp) {
            var4 = ++this.counter;
         } else {
            this.timestamp = var2;
            var4 = this.counter = 0;
         }
      }

      var1.init(this.seed, var2, var4);
   }
}
