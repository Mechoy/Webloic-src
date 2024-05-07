package weblogic.jms.bridge;

import java.io.PrintStream;
import java.io.PrintWriter;
import javax.resource.ResourceException;
import weblogic.jms.JMSLogger;

public final class ResourceTransactionRolledBackException extends ResourceException {
   static final long serialVersionUID = -4349407695017988608L;
   private Exception le;

   public ResourceTransactionRolledBackException(String var1) {
      super(var1);
   }

   public ResourceTransactionRolledBackException(String var1, String var2) {
      super(var1, var2);
   }

   public void setLinkedException(Exception var1) {
      this.le = var1;
   }

   public Exception getLinkedException() {
      return this.le == null ? super.getLinkedException() : this.le;
   }

   public void printStackTrace() {
      JMSLogger.logStackTrace(this);
      printWLJMSStackTrace(this.le);
   }

   public void printStackTrace(PrintStream var1) {
      JMSLogger.logStackTrace(this);
      printWLJMSStackTrace(this.le, var1);
   }

   public void printStackTrace(PrintWriter var1) {
      JMSLogger.logStackTrace(this);
      printWLJMSStackTrace(this.le, var1);
   }

   static void printWLJMSStackTrace(Exception var0) {
      if (var0 != null) {
         JMSLogger.logStackTraceLinked(var0);
      }

   }

   static void printWLJMSStackTrace(Exception var0, PrintStream var1) {
      if (var0 != null) {
         JMSLogger.logStackTraceLinked(var0);
      }

   }

   static void printWLJMSStackTrace(Exception var0, PrintWriter var1) {
      if (var0 != null) {
         JMSLogger.logStackTraceLinked(var0);
      }

   }
}
