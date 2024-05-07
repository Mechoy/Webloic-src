package weblogic.deploy.service;

public class FailureDescription {
   private final String server;
   private final Exception reason;
   private final String operation;

   public FailureDescription(String var1, Exception var2, String var3) {
      this.server = var1;
      this.reason = var2;
      this.operation = var3;
   }

   public String getServer() {
      return this.server;
   }

   public Exception getReason() {
      return this.reason;
   }

   public String getAttemptedOperation() {
      return this.operation;
   }

   public String toString() {
      return "'" + this.operation + "' failure on '" + this.server + "' due to '" + this.reason.toString() + "'";
   }
}
