package weblogic.ldap;

import java.io.IOException;
import java.net.Socket;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolHandler;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.server.channels.ServerChannelImpl;
import weblogic.socket.MuxableSocket;
import weblogic.utils.io.Chunk;

class ProtocolHandlerLDAP implements ProtocolHandler {
   private static ProtocolHandler theOne = new ProtocolHandlerLDAP();
   private static final String PROTOCOL_NAME = "LDAP";
   public static final Protocol PROTOCOL_LDAP = ProtocolManager.createProtocol((byte)10, "ldap", "ldap", false, getProtocolHandler());
   private static final byte ASN1_SEQUENCE_DEFINITE_LENGTH = 48;
   private static final byte BER_ENCODED_INTEGER = 2;
   private static final byte BER_ENCODED_APPLICATION_0 = 96;
   private static final int DISCRIMINATION_LENGTH = 11;

   public static ProtocolHandler getProtocolHandler() {
      return theOne;
   }

   public ServerChannel getDefaultServerChannel() {
      return ProtocolHandlerLDAP.ChannelInitializer.CHANNEL;
   }

   public int getHeaderLength() {
      return 11;
   }

   public int getPriority() {
      return 2;
   }

   public Protocol getProtocol() {
      return PROTOCOL_LDAP;
   }

   public boolean claimSocket(Chunk var1) {
      if (var1.end < 11) {
         return false;
      } else {
         byte[] var2 = var1.buf;
         int var3 = 0;
         if (var2[var3++] != 48) {
            return false;
         } else {
            boolean var4 = false;
            if ((var2[var3] & 128) == 0) {
               int var6 = var2[var3++] & 127;
            } else {
               int var5 = var2[var3++] & 127;
               if (var5 > 2) {
                  return false;
               }

               byte var7 = var2[var3++];
               if (var5 == 2) {
                  int var10000 = (var7 << 8) + var2[var3++];
               }
            }

            if (var2[var3++] != 2) {
               return false;
            } else if ((var2[var3] & 128) == 128) {
               return false;
            } else {
               byte var8 = var2[var3++];
               if (var8 > 4) {
                  return false;
               } else {
                  var3 += var8;
                  return var2[var3] == 96;
               }
            }
         }
      }
   }

   public MuxableSocket createSocket(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      return new MuxableSocketLDAP(var1, var2, var3);
   }

   private static final class ChannelInitializer {
      private static final ServerChannel CHANNEL;

      static {
         CHANNEL = ServerChannelImpl.createDefaultServerChannel(ProtocolHandlerLDAP.PROTOCOL_LDAP);
      }
   }
}
