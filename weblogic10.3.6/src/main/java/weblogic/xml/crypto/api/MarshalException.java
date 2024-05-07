package weblogic.xml.crypto.api;

import java.io.PrintStream;
import java.io.PrintWriter;

public class MarshalException extends Exception {
   private String message;
   private Throwable cause;

   public MarshalException(String var1) {
      super(var1);
   }

   public MarshalException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public MarshalException(Throwable var1) {
      super(var1);
   }

   public Throwable getCause() {
      return super.getCause();
   }

   public void printStackTrace() {
      super.printStackTrace();
   }

   public void printStackTrace(PrintStream var1) {
      super.printStackTrace(var1);
   }

   public void printStackTrace(PrintWriter var1) {
      super.printStackTrace(var1);
   }
}
