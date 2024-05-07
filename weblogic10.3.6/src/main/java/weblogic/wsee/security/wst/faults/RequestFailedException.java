package weblogic.wsee.security.wst.faults;

public class RequestFailedException extends WSTFaultException {
   public RequestFailedException(String var1) {
      super(var1);
      this.faultString = "The specified request failed";
      this.faultCode = "RequestFailed";
   }
}
