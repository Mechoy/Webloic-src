package weblogic.deploy.service;

public class DataTransferHandlerExistsException extends Exception {
   private final String msg;

   public DataTransferHandlerExistsException(String var1) {
      super(var1);
      this.msg = var1;
   }

   public String toString() {
      return this.msg != null ? "DataTransferHandlerExistsException : " + this.msg : "DataTransferHandlerExistsException";
   }
}
