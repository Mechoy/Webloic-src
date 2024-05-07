package weblogic.jms.dd;

import weblogic.jms.common.SerialScheduler;

public class DDScheduler {
   private static SerialScheduler scheduler = new SerialScheduler();

   public static void schedule(Runnable var0) {
      scheduler.schedule(var0);
   }

   public static void drain() {
      scheduler.drain();
   }

   public static Throwable waitForComplete() {
      return scheduler.waitForComplete();
   }
}
