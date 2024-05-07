package weblogic.wsee.security.wst.faults;

public class FailedAuthenticationException extends WSTFaultException {
   public FailedAuthenticationException(String var1) {
      super(var1);
      this.faultString = "Authentication failed";
      this.faultCode = "FailedAuthentication";
   }
}
