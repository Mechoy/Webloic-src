package weblogic.application;

import java.io.PrintStream;
import java.io.PrintWriter;

public class WrappedDeploymentException extends ModuleException {
   public WrappedDeploymentException() {
   }

   public WrappedDeploymentException(String var1) {
      super(var1);
   }

   public WrappedDeploymentException(Throwable var1) {
      super(var1);
   }

   public WrappedDeploymentException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public String getMessage() {
      Throwable var1 = this.getCause();
      return var1 != null ? var1.getMessage() : super.getMessage();
   }

   public StackTraceElement[] getStackTrace() {
      Throwable var1 = this.getCause();
      return var1 != null ? var1.getStackTrace() : super.getStackTrace();
   }

   public String toString() {
      Throwable var1 = this.getCause();
      return var1 != null ? var1.toString() : super.toString();
   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public void printStackTrace(PrintStream var1) {
      Throwable var2 = this.getCause();
      if (var2 == null) {
         super.printStackTrace(var1);
      } else {
         var2.printStackTrace(var1);
      }

   }

   public void printStackTrace(PrintWriter var1) {
      Throwable var2 = this.getCause();
      if (var2 == null) {
         super.printStackTrace(var1);
      } else {
         var2.printStackTrace(var1);
      }

   }
}
