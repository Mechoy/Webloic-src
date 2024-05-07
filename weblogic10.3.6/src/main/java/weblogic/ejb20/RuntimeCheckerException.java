package weblogic.ejb20;

import java.rmi.RemoteException;

public final class RuntimeCheckerException extends RemoteException {
   private static final long serialVersionUID = 2689241955047378499L;

   public RuntimeCheckerException() {
   }

   public RuntimeCheckerException(String var1) {
      super(var1);
   }

   public RuntimeCheckerException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
