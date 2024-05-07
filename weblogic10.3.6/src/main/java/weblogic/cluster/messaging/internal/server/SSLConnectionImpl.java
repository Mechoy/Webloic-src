package weblogic.cluster.messaging.internal.server;

import java.io.IOException;
import java.net.Socket;
import weblogic.cluster.messaging.internal.ConnectionImpl;
import weblogic.cluster.messaging.internal.Message;
import weblogic.cluster.messaging.internal.ServerConfigurationInformation;
import weblogic.utils.io.ChunkedDataOutputStream;

public class SSLConnectionImpl extends ConnectionImpl {
   public SSLConnectionImpl(ServerConfigurationInformation var1) {
      super(var1);
   }

   public SSLConnectionImpl(Socket var1) throws IOException {
      super(var1);
   }

   protected void skipHeader(ChunkedDataOutputStream var1) {
      var1.skip(Message.SSL_HEADER_LENGTH);
   }

   protected void writeHeader(ChunkedDataOutputStream var1) {
      var1.writeBytes("CLUSTER-BROADCAST-SECURE");
   }
}
