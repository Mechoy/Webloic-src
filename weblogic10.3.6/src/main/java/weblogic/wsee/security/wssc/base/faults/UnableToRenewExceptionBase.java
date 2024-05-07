package weblogic.wsee.security.wssc.base.faults;

public abstract class UnableToRenewExceptionBase extends WSCFaultException {
   protected UnableToRenewExceptionBase(String var1, String var2, String var3) {
      super(var1, var2, var3);
      this.faultCode = "UnableToRenew";
      this.faultString = "the specified context token could not be renewed.";
   }
}
