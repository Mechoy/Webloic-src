package weblogic.auddi.uddi;

public class UDDIException extends Exception {
   private int errno;

   public UDDIException(int var1) {
      this(var1, "UDDI error code: " + var1);
   }

   public UDDIException(int var1, String var2) {
      super(UDDIErrorCodes.getMessage(var1) + " '" + var2 + "'");
      this.errno = 0;
      this.errno = var1;
   }

   public int getErrno() {
      return this.errno;
   }
}
