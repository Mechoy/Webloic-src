package weblogic.auddi;

import java.io.PrintStream;

public class NestedException extends Exception {
   protected Throwable m_nested;

   public NestedException(Throwable var1, String var2) {
      super(var2);
      this.m_nested = null;
      this.m_nested = var1;
   }

   public NestedException(Throwable var1) {
      this(var1, (String)null);
   }

   public NestedException(String var1) {
      this((Throwable)null, var1);
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
