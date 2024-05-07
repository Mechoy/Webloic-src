package weblogic.auddi.util;

import java.io.PrintStream;

public class LoggerException extends RuntimeException {
   protected Throwable m_nested;

   public LoggerException(String var1) {
      this((Throwable)null, var1);
   }

   public LoggerException(Throwable var1, String var2) {
      super(var2);
      this.m_nested = null;
      this.m_nested = var1;
   }

   public LoggerException(Throwable var1) {
      this(var1, (String)null);
   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public void printStackTrace(PrintStream var1) {
      super.printStackTrace(var1);
      if (this.m_nested != null) {
         var1.println("----- Nested Exception is : -------------------------------------");
         this.m_nested.printStackTrace(var1);
      }

   }

   public Throwable getNestedException() {
      return this.m_nested;
   }
}
