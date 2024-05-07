package weblogic.iiop;

import java.net.InetSocketAddress;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.utils.net.InetAddressHelper;

public final class ConnectionKey {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   public static final int PORT_DISABLED = 0;
   private String address;
   private int port;
   private int connectedPort;
   private boolean isUsingBidir;
   private InetSocketAddress inAddr;
   public static final ConnectionKey NULL_KEY = new ConnectionKey((String)null, -1);

   public ConnectionKey(String var1, int var2) {
      if (InetAddressHelper.isIPV6Address(var1) && var1.indexOf("[") == -1) {
         var1 = "[" + var1 + "]";
      }

      this.address = var1;
      this.connectedPort = this.port = var2;
      if (var2 > 0 && var1 != null) {
         this.inAddr = new InetSocketAddress(var1, var2);
      }

   }

   public ConnectionKey(String var1, int var2, int var3) {
      if (InetAddressHelper.isIPV6Address(var1) && var1.indexOf("[") == -1) {
         var1 = "[" + var1 + "]";
      }

      this.address = var1;
      this.connectedPort = var3;
      this.port = var2;
      if (var2 > 0 && var1 != null) {
         this.inAddr = new InetSocketAddress(var1, var2);
      }

   }

   public ConnectionKey(IIOPInputStream var1) {
      this.read(var1);
   }

   public boolean isBidirSet() {
      return this.isUsingBidir;
   }

   public void setBidirSet() {
      this.isUsingBidir = true;
   }

   public int getPort() {
      return this.port;
   }

   public int getConnectedPort() {
      return this.connectedPort;
   }

   public String getAddress() {
      return this.address;
   }

   public int hashCode() {
      return this.inAddr != null ? this.inAddr.hashCode() : this.address.hashCode() ^ this.port;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ConnectionKey)) {
         return false;
      } else {
         ConnectionKey var2 = (ConnectionKey)var1;
         return this.inAddr != null && this.inAddr.equals(var2.inAddr) || var2.port == this.port & var2.address.equals(this.address);
      }
   }

   private void read(IIOPInputStream var1) {
      this.address = var1.read_string();
      this.port = var1.read_unsigned_short();
      if (this.port > 0 && this.address != null) {
         this.inAddr = new InetSocketAddress(this.address, this.port);
      }

   }

   public void write(IIOPOutputStream var1) {
      var1.write_string(this.address);
      var1.write_unsigned_short(this.port < 0 ? 0 : this.port);
   }

   public ConnectionKey writeReplace(IIOPOutputStream var1, ServerIdentity var2) {
      EndPoint var3 = var1.getEndPoint();
      ServerChannel var4 = var1.getServerChannel();
      ConnectionKey var5 = this;
      if (this.port == -1 && var3 != null && !KernelStatus.isServer()) {
         var5 = new ConnectionKey(this.address, var3.getConnection().getConnectionKey().getConnectedPort());
      } else if (KernelStatus.isServer() && var3 != null && var3.getConnection() != null && var2.isLocal()) {
         if (var4 != null) {
            var5 = new ConnectionKey(var4.getPublicAddress(), this.port <= 0 && this.port != -1 ? 0 : var4.getPublicPort());
         }
      } else {
         ServerChannel var6;
         if (KernelStatus.isServer() && var2 != null && var4 != null && !var2.isLocal()) {
            var6 = ServerChannelManager.findServerChannel(var2, var4.getProtocol(), var4.getChannelName());
            if (var6 != null) {
               var5 = new ConnectionKey(var6.getPublicAddress(), this.port <= 0 && this.port != -1 ? 0 : var6.getPublicPort());
            }
         } else if (KernelStatus.isServer() && this.address == null && var2 != null && var2.isLocal()) {
            var6 = ServerChannelManager.findLocalServerChannel(ProtocolHandlerIIOP.PROTOCOL_IIOP);
            if (var6 != null) {
               var5 = new ConnectionKey(var6.getPublicAddress(), this.port <= 0 && this.port != -1 ? 0 : var6.getPublicPort());
            } else {
               var5 = new ConnectionKey("", 0);
            }
         }
      }

      if (KernelStatus.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("mapped " + this + " => " + var5 + " for " + var2);
      }

      return var5;
   }

   public void writeForChannel(IIOPOutputStream var1, ServerIdentity var2) {
      this.writeReplace(var1, var2).write(var1);
   }

   public void writeForChannel(IIOPOutputStream var1) {
      EndPoint var2 = var1.getEndPoint();
      ConnectionKey var3 = this;
      if (this.port == -1 && !KernelStatus.isServer() && var2 != null) {
         var3 = new ConnectionKey(this.address, var2.getConnection().getConnectionKey().getConnectedPort());
      }

      var3.write(var1);
   }

   public ConnectionKey readResolve(IIOPInputStream var1) {
      EndPoint var2 = var1.getEndPoint();
      return var2 != null && !KernelStatus.isServer() && this.port == var2.getConnection().getConnectionKey().getConnectedPort() ? new ConnectionKey(this.address, -1) : this;
   }

   public String toString() {
      return this.address + ":" + this.port + "(" + this.connectedPort + ")";
   }

   private static void p(String var0) {
      System.err.println("<ConnectionKey>: " + var0);
   }
}
