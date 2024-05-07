package weblogic.servlet.logging;

public final class TimeLogField implements LogField {
   private int type;

   TimeLogField(String var1) {
      if ("time".equals(var1)) {
         this.type = 1;
      } else if ("time-taken".equals(var1)) {
         this.type = 2;
      } else if ("date".equals(var1)) {
         this.type = 3;
      } else if ("bytes".equals(var1)) {
         this.type = 4;
      } else {
         this.type = 0;
      }

   }

   public void logField(HttpAccountingInfo var1, FormatStringBuffer var2) {
      switch (this.type) {
         case 0:
            var2.appendValueOrDash((String)null);
            return;
         case 1:
            var2.appendTime();
            return;
         case 2:
            long var3 = System.currentTimeMillis() - var1.getInvokeTime();
            Float var5 = new Float((double)var3 / 1000.0);
            var2.append(var5.toString());
            return;
         case 3:
            var2.appendDate();
            return;
         case 4:
            var2.append(var1.getResponseContentLength());
            return;
         default:
      }
   }
}
