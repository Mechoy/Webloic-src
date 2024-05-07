package weblogic.wsee.security.wst.faults;

public class UnableToRenewException extends WSTFaultException {
   public UnableToRenewException(String var1) {
      super(var1);
      this.faultString = "The requested renewal failed";
      this.faultCode = "UnableToRenew";
   }
}
