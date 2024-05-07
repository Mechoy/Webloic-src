package weblogic.wsee.connection;

import java.io.IOException;

public class ConnectionException extends IOException {
   public ConnectionException(String var1) {
      super(var1);
   }

   public ConnectionException(String var1, Throwable var2) {
      super(var1);
      this.initCause(var2);
   }
}
