package weblogic.cluster.messaging.internal;

import java.io.IOException;
import java.net.Socket;

public abstract class AbstractConnectionManager implements ConnectionManager {
   private static final boolean DEBUG;

   private static void debug(String var0) {
      Environment.getLogService().debug("[AbstractConnectionManager] " + var0);
   }

   public Connection createConnection(Socket var1) throws IOException {
      return new ConnectionImpl(var1);
   }

   public abstract Connection createConnection(ServerConfigurationInformation var1) throws IOException;

   static {
      DEBUG = Environment.DEBUG;
   }
}
