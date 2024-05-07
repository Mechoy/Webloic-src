package weblogic.socket;

import java.net.Socket;
import weblogic.server.channels.ServerThrottle;
import weblogic.socket.internal.SocketEnvironment;

public class WLSSocketEnvironmentImpl extends SocketEnvironment {
   public boolean serverThrottleEnabled() {
      return ServerThrottle.getServerThrottle().isEnabled();
   }

   public Socket getWeblogicSocket(Socket var1) {
      return new WeblogicSocketImpl(var1);
   }

   public boolean isJSSE() {
      return false;
   }
}
