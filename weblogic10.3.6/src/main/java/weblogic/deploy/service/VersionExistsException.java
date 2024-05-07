package weblogic.deploy.service;

public class VersionExistsException extends Exception {
   private final String msg;

   public VersionExistsException(String var1) {
      super(var1);
      this.msg = var1;
   }

   public String toString() {
      return this.msg != null ? "VersionExistsException : " + this.msg : "VersionExistsException";
   }
}
