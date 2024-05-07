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
import weblogic.utils.io.Chunk;

public class ProtocolHandlerIIOPS extends ProtocolHandlerIIOP {
   private static final ProtocolHandler theOne = new ProtocolHandlerIIOPS();
   public static final Protocol PROTOCOL_IIOPS = ProtocolManager.createProtocol((byte)5, "iiops", "iiops", true, getProtocolHandler());

   public static ProtocolHandler getProtocolHandler() {
      return theOne;
   }

   private ProtocolHandlerIIOPS() {
   }

   public final ServerChannel getDefaultServerChannel() {
      return ProtocolHandlerIIOPS.ChannelInitializer.CHANNEL;
   }

   public MuxableSocket createSocket(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      if (!MuxableSocketIIOP.isEnabled()) {
         IIOPLogger.logConnectionRejected();
         throw new ProtocolException("IIOPS is disabled");
      } else {
         MuxableSocketIIOPS var4 = new MuxableSocketIIOPS(var1, var2, var3);
         return var4.getSocketFilter();
      }
   }

   public Protocol getProtocol() {
      return PROTOCOL_IIOPS;
   }

   public boolean claimSocket(Chunk var1) {
      return this.claimSocket(var1, "GIOP");
   }

   private static final class ChannelInitializer {
      private static final ServerChannel CHANNEL;

      static {
         CHANNEL = ServerChannelImpl.createDefaultServerChannel(ProtocolHandlerIIOPS.PROTOCOL_IIOPS);
      }
   }
}
