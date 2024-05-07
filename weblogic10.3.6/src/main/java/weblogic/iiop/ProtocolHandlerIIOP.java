package weblogic.iiop;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.Socket;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolHandler;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.server.channels.ServerChannelImpl;
import weblogic.socket.MuxableSocket;
import weblogic.socket.SocketLogger;
import weblogic.utils.io.Chunk;

public class ProtocolHandlerIIOP implements ProtocolHandler {
   protected static final String PROTOCOL_NAME = "GIOP";
   private static final ProtocolHandler theOne = new ProtocolHandlerIIOP();
   public static final Protocol PROTOCOL_IIOP = ProtocolManager.createProtocol((byte)4, "iiop", "iiop", false, getProtocolHandler());

   public static ProtocolHandler getProtocolHandler() {
      return theOne;
   }

   public ServerChannel getDefaultServerChannel() {
      return ProtocolHandlerIIOP.ChannelInitializer.CHANNEL;
   }

   protected ProtocolHandlerIIOP() {
   }

   public int getHeaderLength() {
      return "GIOP".length();
   }

   public int getPriority() {
      return 0;
   }

   public Protocol getProtocol() {
      return PROTOCOL_IIOP;
   }

   public MuxableSocket createSocket(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      if (!MuxableSocketIIOP.isEnabled()) {
         SocketLogger.logConnectionRejectedProtocol(var3.getChannelName(), var3.getConfiguredProtocol());
         throw new ProtocolException("IIOP is disabled");
      } else {
         MuxableSocketIIOP var4 = new MuxableSocketIIOP(var1, var2, var3);
         return var4;
      }
   }

   public boolean claimSocket(Chunk var1) {
      return this.claimSocket(var1, "GIOP");
   }

   protected boolean claimSocket(Chunk var1, String var2) {
      int var3 = var2.length();
      if (var1.end < var3) {
         return false;
      } else {
         byte[] var4 = var1.buf;

         for(int var5 = 0; var5 < var3; ++var5) {
            if (var4[var5] != var2.charAt(var5)) {
               return false;
            }
         }

         return true;
      }
   }

   private static final class ChannelInitializer {
      private static final ServerChannel CHANNEL;

      static {
         CHANNEL = ServerChannelImpl.createDefaultServerChannel(ProtocolHandlerIIOP.PROTOCOL_IIOP);
      }
   }
}
