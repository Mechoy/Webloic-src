package weblogic.net.http;

import java.net.Proxy;
import java.net.URL;

class KeepAliveKey {
   private String protocol;
   private String host;
   private int port;
   private Object clientInfo;
   private Proxy proxy;

   public KeepAliveKey(URL var1, Object var2) {
      this(var1, var2, (Proxy)null);
   }

   public KeepAliveKey(URL var1, Object var2, Proxy var3) {
      this.protocol = null;
      this.host = null;
      this.port = 0;
      this.clientInfo = null;
      this.proxy = null;
      this.protocol = var1.getProtocol();
      this.host = var1.getHost();
      this.port = var1.getPort();
      this.clientInfo = var2;
      this.proxy = var3 == null ? Proxy.NO_PROXY : var3;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof KeepAliveKey)) {
         return false;
      } else {
         KeepAliveKey var2 = (KeepAliveKey)var1;
         return this.host.equals(var2.host) && this.port == var2.port && this.protocol.equals(var2.protocol) && (this.clientInfo == var2.clientInfo || this.clientInfo != null && this.clientInfo.equals(var2.clientInfo)) && this.proxy.equals(var2.proxy);
      }
   }

   public int hashCode() {
      int var1 = (this.protocol.hashCode() * 31 + this.host.hashCode()) * 31 + this.port;
      var1 *= 31;
      if (this.clientInfo != null) {
         var1 += this.clientInfo.hashCode();
         var1 *= 31;
      }

      var1 += this.proxy.hashCode();
      return var1;
   }

   public String toString() {
      return "protocol: " + this.protocol + ", host: " + this.host + ", port: " + this.port + ", clientInfo: " + this.clientInfo + ", proxy: " + this.proxy;
   }
}
