package weblogic.cluster.messaging.internal;

import java.io.IOException;
import java.net.Socket;

public interface ConnectionManager {
   Connection createConnection(Socket var1) throws IOException;

   Connection createConnection(ServerConfigurationInformation var1) throws IOException;
}
