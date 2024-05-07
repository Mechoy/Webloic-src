package weblogic.wsee.security.wst.faults;

public class ExpiredDataException extends WSTFaultException {
   public ExpiredDataException(String var1) {
      super(var1);
      this.faultString = "The request data is out-of-date";
      this.faultCode = "ExpiredData";
   }
}
