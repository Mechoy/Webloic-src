package weblogic.wsee.security.wst.faults;

public class AuthenticationBadElementsException extends WSTFaultException {
   public AuthenticationBadElementsException(String var1) {
      super(var1);
      this.faultString = "Insufficient Digest Elements";
      this.faultCode = "AuthenticationBadElements";
   }
}
