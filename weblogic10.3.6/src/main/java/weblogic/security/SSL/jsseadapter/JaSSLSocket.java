package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.net.InetAddress;

final class JaSSLSocket extends JaAbstractSSLSocket {
   JaSSLSocket(JaSSLContext var1) throws IOException {
      super(var1);
   }

   JaSSLSocket(JaSSLContext var1, JaSSLParameters var2, String var3, int var4) throws IOException {
      super(var1, var3, var4);
      this.init(var2);
   }

   JaSSLSocket(JaSSLContext var1, JaSSLParameters var2, InetAddress var3, int var4) throws IOException {
      super(var1, var3, var4);
      this.init(var2);
   }

   JaSSLSocket(JaSSLContext var1, JaSSLParameters var2, String var3, int var4, InetAddress var5, int var6) throws IOException {
      super(var1, var3, var4, var5, var6);
      this.init(var2);
   }

   JaSSLSocket(JaSSLContext var1, JaSSLParameters var2, InetAddress var3, int var4, InetAddress var5, int var6) throws IOException {
      super(var1, var3, var4, var5, var6);
      this.init(var2);
   }
}
