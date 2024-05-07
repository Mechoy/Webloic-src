package weblogic.cluster.migration;

import java.rmi.RemoteException;

public class MigratableActivatingException extends RemoteException {
   public MigratableActivatingException() {
   }

   public MigratableActivatingException(String var1) {
      super(var1);
   }

   public MigratableActivatingException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
