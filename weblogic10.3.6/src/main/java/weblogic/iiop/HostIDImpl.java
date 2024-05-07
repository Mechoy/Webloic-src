package weblogic.iiop;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import weblogic.rmi.spi.Channel;
import weblogic.rmi.spi.HostID;

public final class HostIDImpl implements HostID, Channel {
   private final InetSocketAddress address;
   private final String host;
   private final int port;

   public HostIDImpl(String var1, int var2) {
      this.host = var1;
      this.port = var2;
      this.address = new InetSocketAddress(var1, var2);
   }

   public int hashCode() {
      return this.getInetAddress().hashCode() ^ this.port;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof HostIDImpl) {
         HostIDImpl var2 = (HostIDImpl)var1;
         return var2.getInetAddress().equals(this.getInetAddress()) & var2.port == this.port;
      } else {
         return false;
      }
   }

   public int compareTo(Object var1) {
      try {
         HostIDImpl var2 = (HostIDImpl)var1;
         int var3 = this.getInetAddress().hashCode();
         int var4 = var2.getInetAddress().hashCode();
         if (var3 == var4) {
            if (var3 == var4) {
               return 0;
            } else {
               return this.port < var2.port ? -1 : 1;
            }
         } else {
            return var3 < var4 ? -1 : 1;
         }
      } catch (ClassCastException var5) {
         throw new AssertionError(var1 + " is not an instanceof HostIDImpl");
      }
   }

   public String getPublicAddress() {
      return this.host;
   }

   public InetSocketAddress getPublicInetAddress() {
      return this.address;
   }

   public int getPublicPort() {
      return this.port;
   }

   public String toString() {
      return this.host + "/" + this.port;
   }

   public boolean isLocal() {
      return false;
   }

   public String objectToString() {
      return this.toString();
   }

   public InetAddress getInetAddress() {
      if (this.address.isUnresolved()) {
         throw new AssertionError("Invalid address: " + this.address);
      } else {
         return this.address.getAddress();
      }
   }

   public String getProtocolPrefix() {
      return ProtocolHandlerIIOP.PROTOCOL_IIOP.getAsURLPrefix();
   }

   public boolean supportsTLS() {
      return false;
   }

   public ConnectionKey getConnectionKey() {
      return new ConnectionKey(this.host, this.port);
   }
}
