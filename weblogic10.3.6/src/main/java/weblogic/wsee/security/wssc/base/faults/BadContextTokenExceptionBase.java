package weblogic.wsee.security.wssc.base.faults;

public abstract class BadContextTokenExceptionBase extends WSCFaultException {
   protected BadContextTokenExceptionBase(String var1, String var2, String var3) {
      super(var1, var2, var3);
      this.faultCode = "BadContextToken";
      this.faultString = "The requested context elements are insufficient or unsupported";
   }
}
