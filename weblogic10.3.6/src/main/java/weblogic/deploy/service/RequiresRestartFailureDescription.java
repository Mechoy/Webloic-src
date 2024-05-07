package weblogic.deploy.service;

public final class RequiresRestartFailureDescription extends FailureDescription {
   public RequiresRestartFailureDescription(String var1) {
      super(var1, (Exception)null, (String)null);
   }

   public Exception getReason() {
      return null;
   }

   public String getAttemptedOperation() {
      return null;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("RequiresRestart failure description on server '");
      var1.append(this.getServer());
      var1.append("' due to non-dynamic changes");
      return var1.toString();
   }
}
