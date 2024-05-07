package weblogic.wsee.security.wst.faults;

public class InvalidTimeRangeException extends WSTFaultException {
   public InvalidTimeRangeException(String var1) {
      super(var1);
      this.faultString = "The requested time range is invalid or unsupported";
      this.faultCode = "wst:InvalidTimeRange";
   }
}
