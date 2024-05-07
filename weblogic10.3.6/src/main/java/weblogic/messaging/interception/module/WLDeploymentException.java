package weblogic.messaging.interception.module;

import java.io.PrintWriter;

public final class WLDeploymentException extends Exception {
   private String msg;
   private Throwable th;

   public WLDeploymentException(String var1) {
      super(var1);
      this.msg = var1;
   }

   public WLDeploymentException(String var1, Throwable var2) {
      super(var1);
      this.msg = var1;
      this.th = var2;
   }

   public Throwable getEmbeddedThrowable() {
      return this.th;
   }

   public String getErrorMessage() {
      return this.msg;
   }

   public void printStackTrace(PrintWriter var1) {
      super.printStackTrace(var1);
      if (this.th != null) {
         this.th.printStackTrace(var1);
      }

   }
}
