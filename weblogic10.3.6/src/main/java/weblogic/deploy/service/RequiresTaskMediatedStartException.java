package weblogic.deploy.service;

public class RequiresTaskMediatedStartException extends Exception {
   private final String msg;

   public RequiresTaskMediatedStartException(String var1) {
      super(var1);
      this.msg = var1;
   }

   public String toString() {
      return this.msg != null ? "RequiresTaskMediatedStartException : " + this.msg : "RequiresTaskMediatedStartException";
   }
}
