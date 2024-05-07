package weblogic.ejb20.locks;

import java.rmi.RemoteException;

public class LockTimedOutException extends RemoteException {
   public LockTimedOutException() {
   }

   public LockTimedOutException(String var1) {
      super(var1);
   }

   public LockTimedOutException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
