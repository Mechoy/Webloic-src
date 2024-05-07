package weblogic.auddi.uddi;

public class AccountLimitExceededException extends UDDIException {
   public AccountLimitExceededException() {
      this((String)null);
   }

   public AccountLimitExceededException(String var1) {
      super(10160, var1 == null ? "" : var1);
   }
}
