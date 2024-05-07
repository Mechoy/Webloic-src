package weblogic.logging;

import java.util.logging.ErrorManager;
import java.util.logging.Handler;

public class WLErrorManager extends ErrorManager {
   private final Handler handler;
   private static final int EXCEPTION_TOLERANCE_LIMIT = 3;
   private int exceptionsEncountered = 0;
   private boolean exitImmediately = false;

   public WLErrorManager(Handler var1) {
      this.handler = var1;
   }

   public synchronized void error(String var1, Exception var2, int var3) {
      if (!this.exitImmediately) {
         ++this.exceptionsEncountered;
         String var4 = null;
         if (this.exceptionsEncountered <= 3 && var3 != 4) {
            var4 = "Handler: '" + this.handler.toString() + "' raised exception" + codeToDesc(var3) + ".";
         } else {
            this.exitImmediately = true;
            this.handler.close();
            var4 = "Handler: '" + this.handler.toString() + "' reported critical error(s). Shutting it down.";
         }

         System.err.println(var4);
      }
   }

   private static String codeToDesc(int var0) {
      switch (var0) {
         case 1:
            return " when writing";
         case 2:
            return " when flushing";
         case 3:
            return " when closing";
         case 4:
            return " when opening";
         case 5:
            return " when formatting";
         default:
            return "";
      }
   }
}
