package weblogic.rjvm.http;

import java.security.cert.X509Certificate;
import javax.servlet.http.HttpServletRequest;
import weblogic.management.runtime.SocketRuntime;
import weblogic.servlet.internal.VirtualConnection;

class HTTPSServerJVMConnection extends HTTPServerJVMConnection {
   private final X509Certificate[] javaChain;

   public HTTPSServerJVMConnection(HttpServletRequest var1, int var2, int var3, SocketRuntime var4, VirtualConnection var5) {
      super(var2, var3, var4, var5);
      this.setLocalPort(var1.getServerPort());
      this.javaChain = (X509Certificate[])((X509Certificate[])var1.getAttribute("javax.servlet.request.X509Certificate"));
   }

   public X509Certificate[] getJavaCertChain() {
      return this.javaChain;
   }

   public X509Certificate getClientJavaCert() {
      return this.javaChain != null && this.javaChain.length > 0 ? this.javaChain[0] : null;
   }
}
