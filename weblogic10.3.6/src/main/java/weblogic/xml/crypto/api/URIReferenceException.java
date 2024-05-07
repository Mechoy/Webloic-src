package weblogic.xml.crypto.api;

import java.io.PrintStream;
import java.io.PrintWriter;

public class URIReferenceException extends Exception {
   private String message;
   private Throwable cause;
   private URIReference uriReference;

   public URIReferenceException(String var1) {
      this.message = var1;
   }

   public URIReferenceException(String var1, Throwable var2) {
      this.message = var1;
      this.cause = var2;
   }

   public URIReferenceException(String var1, Throwable var2, URIReference var3) {
      this.message = var1;
      this.cause = var2;
      this.uriReference = var3;
   }

   public URIReferenceException(Throwable var1) {
      this.cause = var1;
   }

   public Throwable getCause() {
      return this.cause;
   }

   public URIReference getURIReference() {
      return this.uriReference;
   }

   public void printStackTrace() {
   }

   public void printStackTrace(PrintStream var1) {
   }

   public void printStackTrace(PrintWriter var1) {
   }
}
