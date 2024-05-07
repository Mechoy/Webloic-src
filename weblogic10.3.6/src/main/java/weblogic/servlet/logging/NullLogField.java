package weblogic.servlet.logging;

public final class NullLogField implements LogField {
   NullLogField() {
   }

   public void logField(HttpAccountingInfo var1, FormatStringBuffer var2) {
      var2.appendValueOrDash((String)null);
   }
}
