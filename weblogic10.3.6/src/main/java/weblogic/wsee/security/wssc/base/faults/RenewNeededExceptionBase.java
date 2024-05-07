package weblogic.wsee.security.wssc.base.faults;

public abstract class RenewNeededExceptionBase extends WSCFaultException {
   protected RenewNeededExceptionBase(String var1, String var2, String var3) {
      super(var1, var2, var3);
      this.faultCode = "RenewNeeded";
      this.faultString = "The provided context token has expired.";
   }
}
