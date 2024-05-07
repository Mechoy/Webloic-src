package weblogic.deploy.service.internal.transport;

import java.io.ObjectStreamException;
import java.rmi.RemoteException;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;

public final class UnreachableHostException extends RemoteException {
   private final String unreachableMessage;
   private final Exception internalCause;

   public UnreachableHostException(String var1) {
      this(var1, (Exception)null);
   }

   public UnreachableHostException(String var1, Exception var2) {
      this.unreachableMessage = DeployerRuntimeLogger.serverUnreachable(var1);
      this.internalCause = var2;
   }

   public String getMessage() {
      return this.internalCause == null ? this.unreachableMessage : this.unreachableMessage + "; nested exception is: \n\t" + this.internalCause.toString();
   }

   public String getLocalizedMessage() {
      return this.getMessage();
   }

   private Object writeReplace() throws ObjectStreamException {
      return new RemoteException(this.unreachableMessage);
   }
}
