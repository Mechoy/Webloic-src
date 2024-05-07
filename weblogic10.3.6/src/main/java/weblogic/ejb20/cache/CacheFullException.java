package weblogic.ejb20.cache;

import java.rmi.RemoteException;

public final class CacheFullException extends RemoteException {
   private static final long serialVersionUID = 4801611082075828086L;

   public CacheFullException() {
   }

   public CacheFullException(String var1) {
      super(var1);
   }

   public CacheFullException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
