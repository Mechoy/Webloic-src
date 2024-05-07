package weblogic.xml.crypto.dsig.api;

import java.io.PrintStream;
import java.io.PrintWriter;

public class XMLSignatureException extends Exception {
   private String message;
   private Throwable cause;

   public XMLSignatureException() {
   }

   public XMLSignatureException(String var1) {
      this.message = var1;
   }

   public XMLSignatureException(String var1, Throwable var2) {
      this.message = var1;
      this.cause = var2;
   }

   public XMLSignatureException(Throwable var1) {
      this.cause = var1;
   }

   public Throwable getCause() {
      return this.cause;
   }

   public void printStackTrace() {
      super.printStackTrace();
      if (this.cause != null) {
         this.cause.printStackTrace();
      }

   }

   public void printStackTrace(PrintStream var1) {
      super.printStackTrace(var1);
      if (this.cause != null) {
         this.cause.printStackTrace(var1);
      }

   }

   public void printStackTrace(PrintWriter var1) {
      super.printStackTrace(var1);
      if (this.cause != null) {
         this.cause.printStackTrace(var1);
      }

   }
}
