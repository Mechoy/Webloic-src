package weblogic.wsee.security.wst.faults;

public class InvalidSecurityTokenException extends WSTFaultException {
   public InvalidSecurityTokenException(String var1) {
      super(var1);
      this.faultString = "Security token has been revoked";
      this.faultCode = "InvalidSecurityToken";
   }
}
