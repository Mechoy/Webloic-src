package weblogic.socket;

import javax.net.SocketFactory;
import weblogic.protocol.ServerChannel;
import weblogic.utils.io.Chunk;

public abstract class SSLAbstractMuxableSocket extends BaseAbstractMuxableSocket {
   protected SSLAbstractMuxableSocket(Chunk var1, ServerChannel var2) {
      super(var1, var2);
      this.socketFactory = (SocketFactory)(var2.supportsTLS() ? new ChannelSSLSocketFactory(this.channel) : new ChannelSocketFactory(this.channel));
   }

   protected static void p(String var0) {
      System.out.println("<SSLAbstractMuxableSocket>: " + var0);
   }
}
