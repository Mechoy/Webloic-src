package weblogic.socket;

import java.io.IOException;
import java.net.Socket;
import javax.net.SocketFactory;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.MessageReceiverStatistics;
import weblogic.protocol.MessageSenderStatistics;
import weblogic.protocol.ServerChannel;
import weblogic.server.channels.ServerChannelImpl;
import weblogic.server.channels.ServerConnectionRuntimeImpl;
import weblogic.utils.Debug;
import weblogic.utils.io.Chunk;

public abstract class AbstractMuxableSocket extends BaseAbstractMuxableSocket implements MessageReceiverStatistics {
   private static final long serialVersionUID = 7960171920400419300L;
   private static final boolean DEBUG = false;
   private final ServerConnectionRuntimeImpl runtime;

   protected AbstractMuxableSocket(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      this(var1, var3);
      this.connect(var2);
      this.socket.setTcpNoDelay(true);
      if (KernelStatus.isServer() && var3 instanceof ServerChannelImpl) {
         ServerChannelImpl var4 = (ServerChannelImpl)var3;
         Debug.assertion(var4.getRuntime() != null);
         if (var4.getRuntime().getConnectionsCount() > (long)var3.getMaxConnectedClients()) {
            throw new MaxConnectionsExceededException(var3.getMaxConnectedClients(), var3.getChannelName());
         }
      }

      for(Chunk var5 = this.head; var5 != null; var5 = var5.next) {
         this.availBytes += var5.end;
         this.tail = var5;
      }

      if (this.availBytes > this.maxMessageSize) {
         throw new MaxMessageSizeExceededException(this.availBytes, this.maxMessageSize, var3.getConfiguredProtocol());
      }
   }

   public void prepareForReuse() {
      this.resetData();
   }

   protected void registerForRuntimeMonitoring(ServerChannel var1, ServerConnectionRuntimeImpl var2) {
      if (var1 instanceof ServerChannelImpl && ((ServerChannelImpl)var1).getRuntime() != null) {
         ((ServerChannelImpl)var1).getRuntime().addServerConnectionRuntime(var2);
      }

   }

   protected AbstractMuxableSocket(Chunk var1, ServerChannel var2) {
      super(var1, var2);
      this.socketFactory = (SocketFactory)(var2.supportsTLS() ? new ChannelSSLSocketFactory(this.channel) : new ChannelSocketFactory(this.channel));
      this.runtime = new ServerConnectionRuntimeImpl((MessageSenderStatistics)null, this, this);
      this.registerForRuntimeMonitoring(this.channel, this.runtime);
   }

   protected AbstractMuxableSocket(ServerChannel var1) {
      this(Chunk.getChunk(), var1);
   }

   protected static void p(String var0) {
      System.out.println("<AbstractMuxableSocket>: " + var0);
   }

   public final void addSenderStatistics(MessageSenderStatistics var1) {
      this.runtime.addSender(var1);
   }

   protected void cleanup() {
      super.cleanup();
      if (KernelStatus.isServer() && this.channel instanceof ServerChannelImpl) {
         ServerChannelImpl var1 = (ServerChannelImpl)this.channel;
         if (var1.getRuntime() != null) {
            var1.getRuntime().removeServerConnectionRuntime(this);
         }
      }

   }
}
