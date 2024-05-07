package weblogic.servlet.internal;

import java.io.IOException;
import java.net.Socket;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolHandler;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.server.channels.ServerChannelImpl;
import weblogic.socket.MuxableSocket;
import weblogic.utils.io.Chunk;

public class ProtocolHandlerHTTP implements ProtocolHandler {
   private static final ProtocolHandler theOne = new ProtocolHandlerHTTP();
   public static final Protocol PROTOCOL_HTTP = ProtocolManager.createProtocol((byte)1, "http", "http", false, getProtocolHandler());

   public static ProtocolHandler getProtocolHandler() {
      return theOne;
   }

   public ServerChannel getDefaultServerChannel() {
      return ProtocolHandlerHTTP.ChannelInitializer.CHANNEL;
   }

   public int getHeaderLength() {
      return 0;
   }

   public int getPriority() {
      return Integer.MAX_VALUE;
   }

   public Protocol getProtocol() {
      return PROTOCOL_HTTP;
   }

   public boolean claimSocket(Chunk var1) {
      return true;
   }

   public MuxableSocket createSocket(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      return new MuxableSocketHTTP(var1, var2, false, var3);
   }

   private static final class ChannelInitializer {
      private static final ServerChannel CHANNEL;

      static {
         CHANNEL = ServerChannelImpl.createDefaultServerChannel(ProtocolHandlerHTTP.PROTOCOL_HTTP);
      }
   }
}
