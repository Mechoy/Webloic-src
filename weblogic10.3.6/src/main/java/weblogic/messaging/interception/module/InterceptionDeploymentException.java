package weblogic.messaging.interception.module;

import java.io.PrintWriter;
import weblogic.management.DeploymentException;
import weblogic.utils.AssertionError;
import weblogic.utils.PlatformConstants;
import weblogic.utils.StackTraceUtils;

public final class InterceptionDeploymentException extends DeploymentException {
   private String interceptionName;
   private String interceptionFileName;
   private WLDeploymentException[] deploymentExceptions;
   private Throwable unexpectedError;

   public InterceptionDeploymentException(String var1, String var2, WLDeploymentException var3) {
      super(var1);
      this.interceptionName = var1;
      this.interceptionFileName = var2;
      this.deploymentExceptions = new WLDeploymentException[1];
      this.deploymentExceptions[0] = var3;
   }

   public InterceptionDeploymentException(String var1, String var2, WLDeploymentException[] var3) {
      super(var1);
      this.interceptionName = var1;
      this.interceptionFileName = var2;
      this.deploymentExceptions = var3;
   }

   public InterceptionDeploymentException(String var1, String var2, Throwable var3) {
      super(var1);
      this.interceptionName = var1;
      this.interceptionFileName = var2;
      this.unexpectedError = var3;
   }

   public String getMessage() {
      StringBuffer var1 = new StringBuffer(200);
      if (this.deploymentExceptions != null) {
         var1.append(PlatformConstants.EOL);
         var1.append("Reason: ");

         for(int var2 = 0; var2 < this.deploymentExceptions.length; ++var2) {
            var1.append(this.deploymentExceptions[var2].getErrorMessage());
            var1.append(PlatformConstants.EOL);
         }
      } else {
         if (this.unexpectedError == null) {
            throw new AssertionError("Expected either deploymentExceptions or unexpectedError to be non-null.");
         }

         var1.append(StackTraceUtils.throwable2StackTrace(this.unexpectedError));
         var1.append(PlatformConstants.EOL);
      }

      var1.append(PlatformConstants.EOL);
      return var1.toString();
   }

   public String toString() {
      return this.getMessage();
   }

   public void printStackTrace(PrintWriter var1) {
      super.printStackTrace(var1);
      var1.println(this.getMessage());
   }
}
