package weblogic.deploy.service;

public class RegistrationExistsException extends RegistrationException {
   private final String msg;

   public RegistrationExistsException(String var1) {
      super(var1);
      this.msg = var1;
   }

   public String toString() {
      return this.msg != null ? "RegistrationExistsException : " + this.msg : "RegistrationExistsException";
   }
}
