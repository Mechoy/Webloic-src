package weblogic.jms.bridge.internal;

import java.io.PrintStream;
import java.io.PrintWriter;
import weblogic.jms.JMSLogger;

public class MessagingBridgeException extends Exception {
   static final long serialVersionUID = 896851532291231868L;
   private static String reason;
   private Throwable throwable;

   public MessagingBridgeException(String var1) {
      reason = var1;
      this.throwable = null;
   }

   public MessagingBridgeException(String var1, Throwable var2) {
      reason = var1;
      this.throwable = var2;
   }

   public synchronized void setLinkedException(Exception var1) {
      this.throwable = var1;
   }

   public synchronized void setLinkedThrowable(Throwable var1) {
      this.throwable = var1;
   }

   public Exception getLinkedException() {
      if (this.throwable != null && this.throwable instanceof Exception) {
         return (Exception)this.throwable;
      } else {
         Exception var1 = new Exception("Linked Exception");
         return var1;
      }
   }

   public void printStackTrace() {
      printWLJMSStackTrace(this.throwable);
   }

   public void printStackTrace(PrintStream var1) {
      printWLJMSStackTrace(this.throwable, var1);
   }

   public void printStackTrace(PrintWriter var1) {
      printWLJMSStackTrace(this.throwable, var1);
   }

   static void printWLJMSStackTrace(Throwable var0) {
      System.out.println("MessagingBridgeException: " + reason);
      if (var0 != null) {
         JMSLogger.logStackTraceLinked(var0);
      }

   }

   static void printWLJMSStackTrace(Throwable var0, PrintStream var1) {
      var1.println("MessagingBridgeException: " + reason);
      if (var0 != null) {
         JMSLogger.logStackTraceLinked(var0);
      }

   }

   static void printWLJMSStackTrace(Throwable var0, PrintWriter var1) {
      var1.println("MessagingBridgeException: " + reason);
      if (var0 != null) {
         JMSLogger.logStackTraceLinked(var0);
      }

   }
}
