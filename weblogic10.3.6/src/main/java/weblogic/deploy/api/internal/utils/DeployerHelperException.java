package weblogic.deploy.api.internal.utils;

public class DeployerHelperException extends Exception {
   private Throwable original;

   DeployerHelperException(String var1) {
      super(var1);
   }

   DeployerHelperException(String var1, Throwable var2) {
      super(var1);
      this.original = var2;
   }

   public Throwable getOrignal() {
      return this.original;
   }

   public String getMessage() {
      String var1 = super.getMessage();
      if (this.original != null) {
         var1 = var1 + "\n" + this.original.getMessage();
      }

      return var1;
   }
}
