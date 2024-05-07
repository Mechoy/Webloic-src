package weblogic.wsee.tools.logging;

import java.io.PrintStream;

public class DefaultLogger implements Logger {
   public void log(EventLevel var1, LogEvent var2) {
      this.log(var1, var2.toString());
   }

   public void log(EventLevel var1, String var2) {
      getPrintStream(var1).print("[" + var1 + "] " + var2);
   }

   private static PrintStream getPrintStream(EventLevel var0) {
      if (var0 == EventLevel.ERROR) {
         return System.err;
      } else {
         return var0 == EventLevel.FATAL ? System.err : System.out;
      }
   }
}
