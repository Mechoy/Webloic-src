package weblogic.messaging.common;

public final class IDFactory {
   private static int seed = MessagingUtilities.getSeed();
   private static long timestamp = System.currentTimeMillis();
   private static int counter = 2147483646;
   private static Object lock = new Object();

   final void initId(IDImpl var1) {
      int var2;
      int var3;
      long var4;
      synchronized(lock) {
         var2 = ++counter;
         if (var2 == Integer.MAX_VALUE) {
            timestamp = System.currentTimeMillis();
            counter = 1;
            var2 = 1;
         }

         var3 = seed;
         var4 = timestamp;
      }

      var1.init(var4, var3, var2);
   }
}
