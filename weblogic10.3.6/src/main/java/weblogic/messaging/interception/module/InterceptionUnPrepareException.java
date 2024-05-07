package weblogic.messaging.interception.module;

import java.io.PrintWriter;
import java.rmi.RemoteException;

public final class InterceptionUnPrepareException extends RemoteException {
   public InterceptionUnPrepareException(String var1) {
      super(var1);
   }

   public InterceptionUnPrepareException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public void printStackTrace(PrintWriter var1) {
      super.printStackTrace(var1);
      if (this.detail != null) {
         this.detail.printStackTrace(var1);
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("weblogic.messaging.interception.module.InterceptionUnPrepareException: ");
      var1.append(this.getMessage());
      return var1.toString();
   }
}
