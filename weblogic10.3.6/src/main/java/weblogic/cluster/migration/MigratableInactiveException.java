package weblogic.cluster.migration;

import java.rmi.RemoteException;

public class MigratableInactiveException extends RemoteException {
   public MigratableInactiveException() {
   }

   public MigratableInactiveException(String var1) {
      super(var1);
   }

   public MigratableInactiveException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
