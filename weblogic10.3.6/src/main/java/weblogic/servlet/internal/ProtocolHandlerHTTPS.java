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

public class ProtocolHandlerHTTPS extends ProtocolHandlerHTTP {
   private static final ProtocolHandler theOne = new ProtocolHandlerHTTPS();
   private static final String PROTOCOL_NAME = "HTTPS";
   public static final Protocol PROTOCOL_HTTPS = ProtocolManager.createProtocol((byte)3, "https", "https", true, getProtocolHandler());

   public static ProtocolHandler getProtocolHandler() {
      return theOne;
   }

   public Protocol getProtocol() {
      return PROTOCOL_HTTPS;
   }

   public final ServerChannel getDefaultServerChannel() {
      return ProtocolHandlerHTTPS.ChannelInitializer.CHANNEL;
   }

   public MuxableSocket createSocket(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      return new MuxableSocketHTTP(var1, var2, true, var3);
   }

   private static final class ChannelInitializer {
      private static final ServerChannel CHANNEL;

      static {
         CHANNEL = ServerChannelImpl.createDefaultServerChannel(ProtocolHandlerHTTPS.PROTOCOL_HTTPS);
      }
   }
}
