package weblogic.rjvm.t3.client;

import java.io.IOException;
import java.net.Socket;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolHandler;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.server.channels.BasicServerChannelImpl;
import weblogic.socket.MuxableSocket;
import weblogic.utils.io.Chunk;

public class ProtocolHandlerT3 implements ProtocolHandler {
   private static final String PROTOCOL_NAME = "t3";
   private static final ProtocolHandler theOne = new ProtocolHandlerT3();
   public static final Protocol PROTOCOL_T3 = ProtocolManager.createProtocol((byte)0, "t3", "t3", false, getProtocolHandler());

   public static ProtocolHandler getProtocolHandler() {
      return theOne;
   }

   public ServerChannel getDefaultServerChannel() {
      return ProtocolHandlerT3.ChannelInitializer.CHANNEL;
   }

   protected ProtocolHandlerT3() {
   }

   public int getHeaderLength() {
      return "t3".length() + 1;
   }

   public int getPriority() {
      return 0;
   }

   public Protocol getProtocol() {
      return PROTOCOL_T3;
   }

   public boolean claimSocket(Chunk var1) {
      return this.claimSocket(var1, "t3");
   }

   boolean claimSocket(Chunk var1, String var2) {
      int var3 = var2.length();
      if (var1.end < var3 + 1) {
         return false;
      } else {
         byte[] var4 = var1.buf;

         for(int var5 = 0; var5 < var3; ++var5) {
            if (var4[var5] != var2.charAt(var5)) {
               return false;
            }
         }

         if (var4[var3] != 32) {
            return false;
         } else {
            return true;
         }
      }
   }

   public MuxableSocket createSocket(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      throw new UnsupportedOperationException("This method is not supported on the weblogic client side");
   }

   private static final class ChannelInitializer {
      private static final ServerChannel CHANNEL;

      static {
         CHANNEL = BasicServerChannelImpl.createDefaultServerChannel(ProtocolHandlerT3.PROTOCOL_T3);
      }
   }
}
