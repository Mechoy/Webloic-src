package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.nio.channels.SocketChannel;

final class JaNioSSLSocket extends JaAbstractLayeredSSLSocket {
   private final SocketChannel socketChannel;

   public SocketChannel getChannel() {
      return this.socketChannel;
   }

   JaNioSSLSocket(SocketChannel var1, JaSSLContext var2, JaSSLParameters var3, boolean var4) throws IOException {
      super(null == var1 ? null : var1.socket(), var2, var4);
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null SocketChannel.");
      } else {
         this.socketChannel = var1;
         this.init(var1, var3);
      }
   }
}
