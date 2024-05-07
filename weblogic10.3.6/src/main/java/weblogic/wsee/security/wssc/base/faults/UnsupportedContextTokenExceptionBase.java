package weblogic.wsee.security.wssc.base.faults;

public abstract class UnsupportedContextTokenExceptionBase extends WSCFaultException {
   protected UnsupportedContextTokenExceptionBase(String var1, String var2, String var3) {
      super(var1, var2, var3);
      this.faultCode = "UnsupportedContextToken";
      this.faultString = "Not all of the values associated witht the SCT are supported";
   }
}
