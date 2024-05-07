package weblogic.servlet.logging;

final class HeaderLogField implements LogField {
   private int prefix;
   private String header;

   HeaderLogField(String var1, String var2) {
      this.header = var2;
      if (var1.startsWith("c")) {
         this.prefix = 3;
      } else if (var1.startsWith("s")) {
         this.prefix = 4;
      } else {
         this.prefix = 0;
      }

   }

   public void logField(HttpAccountingInfo var1, FormatStringBuffer var2) {
      String var3 = null;
      if (this.header != null) {
         switch (this.prefix) {
            case 0:
            case 1:
            case 2:
            default:
               break;
            case 3:
               var3 = var1.getHeader(this.header);
               break;
            case 4:
               var3 = var1.getResponseHeader(this.header);
         }
      }

      var2.appendQuotedValueOrDash(var3);
   }
}
