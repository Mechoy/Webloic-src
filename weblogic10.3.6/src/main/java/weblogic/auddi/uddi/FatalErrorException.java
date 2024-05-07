package weblogic.auddi.uddi;

import java.io.PrintStream;

public class FatalErrorException extends UDDIException {
   protected Throwable m_nested;

   public FatalErrorException() {
      this((String)null, (Throwable)null);
   }

   public FatalErrorException(String var1) {
      this(var1, (Throwable)null);
   }

   public FatalErrorException(Throwable var1) {
      this((String)null, var1);
   }

   public FatalErrorException(String var1, Throwable var2) {
      super(10500, (var1 == null ? "" : var1) + (var2 == null ? "" : (var1 != null && var2.getMessage() != null ? ", " : "") + (var2.getMessage() == null ? "" : var2.getMessage())));
      this.m_nested = null;
      this.m_nested = var2;
   }

   public Throwable getNestedException() {
      return this.m_nested;
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
}
