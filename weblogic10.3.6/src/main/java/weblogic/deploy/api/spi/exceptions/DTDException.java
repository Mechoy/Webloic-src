package weblogic.deploy.api.spi.exceptions;

public class DTDException extends Exception {
   public DTDException() {
   }

   public DTDException(String var1) {
      super(var1);
   }

   public String getDD() {
      return this.getMessage();
   }
}
