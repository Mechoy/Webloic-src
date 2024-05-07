package weblogic.ejb.spi;

import java.io.PrintWriter;
import weblogic.ejb.container.EJBTextTextFormatter;
import weblogic.management.DeploymentException;
import weblogic.utils.AssertionError;
import weblogic.utils.PlatformConstants;

public final class EJBDeploymentException extends DeploymentException {
   private static final long serialVersionUID = 5093229614062171037L;
   private String ejbName;
   private String ejbFileName;
   private WLDeploymentException[] deploymentExceptions;
   private Throwable unexpectedError;

   public EJBDeploymentException(String var1, String var2, WLDeploymentException var3) {
      super(var1, var3);
      this.ejbName = var1;
      this.ejbFileName = var2;
      this.deploymentExceptions = new WLDeploymentException[1];
      this.deploymentExceptions[0] = var3;
   }

   public EJBDeploymentException(String var1, String var2, Throwable var3) {
      super(var1, var3);
      this.ejbName = var1;
      this.ejbFileName = var2;
      this.unexpectedError = var3;
   }

   public String getMessage() {
      StringBuffer var1 = new StringBuffer(200);
      EJBTextTextFormatter var2 = new EJBTextTextFormatter();
      var1.append(var2.ejbDeploymentError(this.ejbName, this.ejbFileName));
      if (this.deploymentExceptions != null) {
         for(int var3 = 0; var3 < this.deploymentExceptions.length; ++var3) {
            var1.append(this.deploymentExceptions[var3].getErrorMessage());
            var1.append(PlatformConstants.EOL);
         }
      } else {
         if (this.unexpectedError == null) {
            throw new AssertionError("Expected either deploymentExceptions or unexpectedError to be non-null.");
         }

         var1.append(this.unexpectedError.getMessage());
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
