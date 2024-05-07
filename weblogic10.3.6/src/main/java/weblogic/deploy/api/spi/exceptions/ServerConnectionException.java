package weblogic.deploy.api.spi.exceptions;

import weblogic.management.ManagementException;

public class ServerConnectionException extends IllegalStateException {
   public ServerConnectionException(String var1) {
      super(var1);
   }

   public ServerConnectionException(String var1, Throwable var2) {
      super(var1);
      this.initCause(var2);
   }

   public Throwable getRootCause() {
      return ManagementException.unWrapExceptions(this);
   }
}
