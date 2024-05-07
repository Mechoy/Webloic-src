package weblogic.wsee.security.wst.faults;

public class RenewNeededException extends WSTFaultException {
   public RenewNeededException(String var1) {
      super(var1);
      this.faultString = "A renewable security token has expired";
      this.faultCode = "RenewNeeded";
   }
}
