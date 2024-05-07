package weblogic.cluster.singleton;

public final class ServerMigrationException extends Exception {
   private String message;
   private int status;

   public ServerMigrationException(String var1, int var2, Throwable var3) {
      this(var1, var3);
      this.status = var2;
   }

   public ServerMigrationException(String var1, Throwable var2) {
      super(var1, var2);
      this.message = var1;
   }

   public int getStatus() {
      return this.status;
   }

   public String toString() {
      return this.message;
   }
}
