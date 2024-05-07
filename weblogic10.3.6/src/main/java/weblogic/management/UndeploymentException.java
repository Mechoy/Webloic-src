package weblogic.management;

public final class UndeploymentException extends ManagementException {
   private static final long serialVersionUID = 4423808334005698365L;

   public UndeploymentException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public UndeploymentException(Throwable var1) {
      this("", var1);
   }

   public UndeploymentException(String var1) {
      this(var1, (Throwable)null);
   }
}
