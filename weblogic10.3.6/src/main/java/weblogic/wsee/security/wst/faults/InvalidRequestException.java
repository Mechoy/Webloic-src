package weblogic.wsee.security.wst.faults;

public class InvalidRequestException extends WSTFaultException {
   public InvalidRequestException(String var1) {
      super(var1);
      this.faultString = "The request was invalid or malformed";
      this.faultCode = "InvalidRequest";
   }
}
