package weblogic.rjvm.t3;

import java.io.IOException;
import java.net.Socket;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolHandler;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.server.channels.ServerChannelImpl;
import weblogic.socket.MuxableSocket;
import weblogic.utils.io.Chunk;

public class ProtocolHandlerT3S extends ProtocolHandlerT3 {
   private static final String PROTOCOL_NAME = "t3s";
   private static final ProtocolHandler theOne = new ProtocolHandlerT3S();
   public static final Protocol PROTOCOL_T3S = ProtocolManager.createProtocol((byte)2, "t3s", "t3s", true, getProtocolHandler());

   public static ProtocolHandler getProtocolHandler() {
      return theOne;
   }

   private ProtocolHandlerT3S() {
   }

   public final ServerChannel getDefaultServerChannel() {
      return ProtocolHandlerT3S.ChannelInitializer.CHANNEL;
   }

   public Protocol getProtocol() {
      return PROTOCOL_T3S;
   }

   public boolean claimSocket(Chunk var1) {
      return this.claimSocket(var1, "t3s");
   }

   public MuxableSocket createSocket(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      return new MuxableSocketT3S(var1, var2, var3);
   }

   private static final class ChannelInitializer {
      private static final ServerChannel CHANNEL;

      static {
         CHANNEL = ServerChannelImpl.createDefaultServerChannel(ProtocolHandlerT3S.PROTOCOL_T3S);
      }
   }
}
