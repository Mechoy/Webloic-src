package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.net.Socket;

final class JaLayeredSSLSocket extends JaAbstractLayeredSSLSocket {
   JaLayeredSSLSocket(Socket var1, JaSSLContext var2, JaSSLParameters var3, boolean var4) throws IOException {
      super(var1, var2, var4);
      this.init(var3, var1.getInputStream(), var1.getOutputStream());
   }
}
