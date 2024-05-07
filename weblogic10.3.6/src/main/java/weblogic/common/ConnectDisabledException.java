package weblogic.common;

public final class ConnectDisabledException extends ResourceException {
   private static final long serialVersionUID = 1073506607380441357L;

   public ConnectDisabledException(String var1) {
      super(var1);
   }

   public ConnectDisabledException() {
      this((String)null);
   }

   public ConnectDisabledException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
