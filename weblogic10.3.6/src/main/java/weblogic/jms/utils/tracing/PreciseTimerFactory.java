package weblogic.jms.utils.tracing;

import weblogic.utils.time.Timer;

public class PreciseTimerFactory {
   public static Timer newTimer() {
      Class var0 = null;

      try {
         var0 = Class.forName("weblogic.utils.time.JDK15NanoTimer");
      } catch (ClassNotFoundException var3) {
         throw new AssertionError("Precise time is only supported on JDK 1.5 - where the use of System.nanoTime() is available");
      }

      try {
         return (Timer)var0.newInstance();
      } catch (Exception var2) {
         throw new AssertionError("Precise time support is not available");
      }
   }
}
