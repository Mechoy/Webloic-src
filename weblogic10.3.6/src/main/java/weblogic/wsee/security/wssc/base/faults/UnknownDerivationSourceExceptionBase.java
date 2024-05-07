package weblogic.wsee.security.wssc.base.faults;

public abstract class UnknownDerivationSourceExceptionBase extends WSCFaultException {
   protected UnknownDerivationSourceExceptionBase(String var1, String var2, String var3) {
      super(var1, var2, var3);
      this.faultCode = "UnknownDerivationSource";
      this.faultString = "The specified source for the derivation is unknown.";
   }
}
