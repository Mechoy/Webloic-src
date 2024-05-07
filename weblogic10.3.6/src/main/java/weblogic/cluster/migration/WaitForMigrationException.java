package weblogic.cluster.migration;

import java.rmi.ConnectException;

public final class WaitForMigrationException extends ConnectException {
   private long suggestedWait;

   public WaitForMigrationException(String var1) {
      super(var1);
   }

   public WaitForMigrationException(String var1, Exception var2) {
      super(var1, var2);
   }

   WaitForMigrationException(String var1, Exception var2, long var3) {
      super(var1, var2);
      this.suggestedWait = var3;
   }

   public long getSuggestedWait() {
      return this.suggestedWait;
   }
}
