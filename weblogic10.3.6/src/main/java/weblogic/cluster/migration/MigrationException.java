package weblogic.cluster.migration;

public class MigrationException extends RuntimeException {
   static final long serialVersionUID = -5904150673307531553L;
   private boolean fatal = false;

   public MigrationException() {
   }

   public MigrationException(String var1) {
      super(var1);
   }

   public MigrationException(String var1, boolean var2) {
      super(var1);
      this.fatal = var2;
   }

   public MigrationException(Exception var1) {
      super(var1);
   }

   public MigrationException(Exception var1, boolean var2) {
      super(var1);
      this.fatal = var2;
   }

   public MigrationException(String var1, Exception var2) {
      super(var1, var2);
   }

   public MigrationException(String var1, Exception var2, boolean var3) {
      super(var1, var2);
      this.fatal = var3;
   }

   public boolean isFatal() {
      return this.fatal;
   }
}
