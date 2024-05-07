package weblogic.wsee.security.wst.faults;

public class InvalidScopeException extends WSTFaultException {
   public InvalidScopeException(String var1) {
      super(var1);
      this.faultString = "The requst scope is invalid or unsupported";
      this.faultCode = "InvalidScope";
   }
}
