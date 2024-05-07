package weblogic.wsee.security.wst.faults;

public class BadRequestException extends WSTFaultException {
   public BadRequestException(String var1) {
      super(var1);
      this.faultString = "The specified RequestSecurityToken is not understood";
      this.faultCode = "BadRequest";
   }
}
