package weblogic.deploy.service;

public class InvalidCreateChangeDescriptorException extends Exception {
   private final String msg;

   public InvalidCreateChangeDescriptorException(String var1) {
      super(var1);
      this.msg = var1;
   }

   public String toString() {
      return this.msg != null ? "InvalidCreateChangeDescriptorException : " + this.msg : "InvalidCreateChangeDescriptorException";
   }
}
