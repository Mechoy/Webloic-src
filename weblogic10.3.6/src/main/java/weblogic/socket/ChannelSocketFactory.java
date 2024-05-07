package weblogic.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.ServerChannel;

public final class ChannelSocketFactory extends WeblogicSocketFactory {
   private ServerChannel channel;

   public ChannelSocketFactory(ServerChannel var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Channel must not be null");
      } else {
         this.channel = var1;
      }
   }

   public Socket createSocket(String var1, int var2) throws IOException, UnknownHostException {
      return this.createSocket(InetAddress.getByName(var1), var2);
   }

   public Socket createSocket(String var1, int var2, InetAddress var3, int var4) {
      throw new UnsupportedOperationException("Binding characteristics are determined by the channel");
   }

   public Socket createSocket(InetAddress var1, int var2) throws IOException {
      return this.createSocket(var1, var2, 0);
   }

   public Socket createSocket(InetAddress var1, int var2, int var3) throws IOException {
      int var4 = var3 > 0 ? var3 : this.channel.getConnectTimeout() * 1000;
      if (KernelStatus.isServer() && this.channel.isOutboundEnabled()) {
         if (this.channel.getProxyAddress() != null) {
            return SocketMuxer.getMuxer().newProxySocket(var1, var2, this.getInetAddressFor(this.channel.getAddress()), 0, InetAddress.getByName(this.channel.getProxyAddress()), this.channel.getProxyPort(), var4);
         } else {
            return this.channel.isSDPEnabled() ? SocketMuxer.getMuxer().newSDPSocket(var1, var2, this.getInetAddressFor(this.channel.getAddress()), 0, var4) : SocketMuxer.getMuxer().newSocket(var1, var2, this.getInetAddressFor(this.channel.getAddress()), 0, var4);
         }
      } else {
         return SocketMuxer.getMuxer().newClientSocket(var1, var2, var4);
      }
   }

   private InetAddress getInetAddressFor(String var1) throws UnknownHostException {
      return var1 != null ? InetAddress.getByName(var1) : (InetAddress)null;
   }

   public Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) {
      throw new UnsupportedOperationException("Binding characteristics are determined by the channel");
   }
}
