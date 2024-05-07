package weblogic.common;

public final class ConnectDeadException extends ResourceException {
   private static final long serialVersionUID = -7172663358986720879L;

   public ConnectDeadException(String var1) {
      super(var1);
   }

   public ConnectDeadException() {
      this((String)null);
   }

   public ConnectDeadException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
