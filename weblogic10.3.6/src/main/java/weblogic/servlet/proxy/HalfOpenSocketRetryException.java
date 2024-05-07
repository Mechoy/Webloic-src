package weblogic.servlet.proxy;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class HalfOpenSocketRetryException extends IOException {
   private IOException nested;

   public HalfOpenSocketRetryException() {
   }

   public HalfOpenSocketRetryException(String var1) {
      super(var1);
   }

   public HalfOpenSocketRetryException(IOException var1) {
      this.nested = var1;
   }

   public String toString() {
      return this.nested != null ? this.nested.toString() : super.toString();
   }

   public void printStackTrace(PrintStream var1) {
      if (this.nested != null) {
         this.nested.printStackTrace(var1);
      } else {
         super.printStackTrace(var1);
      }

   }

   public void printStackTrace(PrintWriter var1) {
      if (this.nested != null) {
         this.nested.printStackTrace(var1);
      } else {
         super.printStackTrace(var1);
      }

   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }
}
