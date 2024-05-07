package weblogic.deploy.service;

public class RegistrationException extends Exception {
   private final String msg;

   public RegistrationException(String var1) {
      super(var1);
      this.msg = var1;
   }

   public String toString() {
      return this.msg != null ? "RegistrationException : " + this.msg : "RegistrationException";
   }
}
