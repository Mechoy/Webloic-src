package weblogic.security.net;

import java.net.InetAddress;
import java.net.Socket;
import java.util.EventObject;

public final class ConnectionEvent extends EventObject {
   private static final long serialVersionUID = -8861858081041858122L;
   private InetAddress remAddr;
   private int remPort;
   private InetAddress localAddr;
   private int localPort;
   private String protocol;

   public ConnectionEvent(Socket var1, String var2) {
      this(var1.getInetAddress(), var1.getPort(), var1.getLocalAddress(), var1.getLocalPort(), var2);
   }

   public ConnectionEvent(InetAddress var1, int var2, InetAddress var3, int var4, String var5) {
      super(ConnectionFilterImpl.impl);
      this.remAddr = var1;
      this.remPort = var2;
      this.localAddr = var3;
      this.localPort = var4;
      this.protocol = var5;
   }

   public InetAddress getRemoteAddress() {
      return this.remAddr;
   }

   public int getRemotePort() {
      return this.remPort;
   }

   public InetAddress getLocalAddress() {
      return this.localAddr;
   }

   public int getLocalPort() {
      return this.localPort;
   }

   public String getProtocol() {
      return this.protocol;
   }

   public int hashCode() {
      return this.remPort ^ this.localPort ^ this.remAddr.hashCode() ^ this.localAddr.hashCode() ^ (this.protocol != null ? this.protocol.hashCode() : 0);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ConnectionEvent)) {
         return false;
      } else {
         boolean var10000;
         label37: {
            ConnectionEvent var2 = (ConnectionEvent)var1;
            if (this.remPort == var2.remPort && this.localPort == var2.localPort && this.remAddr.equals(var2.remAddr) && this.localAddr.equals(var2.localAddr)) {
               if (this.protocol != null) {
                  if (this.protocol.equals(var2.protocol)) {
                     break label37;
                  }
               } else if (var2.protocol == null) {
                  break label37;
               }
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      }
   }
}
