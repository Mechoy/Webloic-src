package weblogic.wsee.tools.logging;

import java.util.ArrayList;
import java.util.List;

public class CountLogger implements Logger {
   private int debugCount = 0;
   private int infoCount = 0;
   private int warningCount = 0;
   private int errorCount = 0;
   private int verboseCount = 0;
   private List<String> errorMsgs = new ArrayList();

   public void log(EventLevel var1, LogEvent var2) {
      assert var1 != null;

      assert var2 != null;

      String var3 = var2.toString();
      this.log(var1, var3);
   }

   public void log(EventLevel var1, String var2) {
      switch (var1) {
         case DEBUG:
            ++this.debugCount;
            break;
         case INFO:
            ++this.infoCount;
            break;
         case WARNING:
            ++this.warningCount;
            break;
         case ERROR:
            ++this.errorCount;
            this.errorMsgs.add(var2);
            break;
         case VERBOSE:
            ++this.verboseCount;
      }

   }

   public int getDebugCount() {
      return this.debugCount;
   }

   public int getInfoCount() {
      return this.infoCount;
   }

   public int getWarningCount() {
      return this.warningCount;
   }

   public int getErrorCount() {
      return this.errorCount;
   }

   public List<String> getErrorMsgs() {
      return this.errorMsgs;
   }

   public int getVerboseCount() {
      return this.verboseCount;
   }
}
