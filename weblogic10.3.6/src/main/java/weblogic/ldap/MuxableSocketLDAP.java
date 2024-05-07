package weblogic.ldap;

import com.octetstring.vde.ConnectionHandler;
import com.octetstring.vde.LDAPServer;
import java.io.IOException;
import java.net.Socket;
import weblogic.protocol.ServerChannel;
import weblogic.socket.AbstractMuxableSocket;
import weblogic.socket.SocketMuxer;
import weblogic.socket.WeblogicSocket;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedInputStream;

class MuxableSocketLDAP extends AbstractMuxableSocket {
   private static LDAPServer ldapServer;
   private final ConnectionHandler connectionHandler;

   MuxableSocketLDAP(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      super(var1, var2, var3);
      LDAPSocket var4 = new LDAPSocket(this.getSocket(), this);
      this.connectionHandler = ldapServer.createConnectionHandler(var4);
      this.connectionHandler.setExternalExecutor(EmbeddedLDAP.getEmbeddedLDAP());
   }

   static void initialize(LDAPServer var0) {
      ProtocolHandlerLDAP.getProtocolHandler();
      ldapServer = var0;
   }

   protected int getHeaderLength() {
      return 2;
   }

   protected int getMessageLength() {
      byte var1 = this.getHeaderByte(1);
      if ((var1 & 128) == 0) {
         return 2 + (var1 & 127);
      } else {
         int var2 = 2 + (var1 & 127);
         if (this.availBytes < var2) {
            return -1;
         } else {
            int var3 = 0;

            for(int var4 = 2; var4 < var2; ++var4) {
               var3 <<= 8;
               var3 += this.getHeaderByte(var4) & 255;
            }

            return var3 + var2;
         }
      }
   }

   public void dispatch(Chunk var1) {
      try {
         ChunkedInputStream var2 = new ChunkedInputStream(var1, 0);
         if (!this.connectionHandler.dispatch(var2)) {
            this.close();
            return;
         }
      } catch (Throwable var3) {
         SocketMuxer.getMuxer().deliverHasException(this.getSocketFilter(), var3);
         this.close();
      }

   }

   public int getIdleTimeoutMillis() {
      return 0;
   }

   public class LDAPSocket extends WeblogicSocket {
      private MuxableSocketLDAP muxableSocketLDAP;

      LDAPSocket(Socket var2, MuxableSocketLDAP var3) {
         super(var2);
         this.muxableSocketLDAP = var3;
      }

      public final void close() throws IOException {
         if (this.muxableSocketLDAP.getSocketInfo() == null && this.muxableSocketLDAP.getSocketFilter().getSocketInfo() == null) {
            super.close();
            this.muxableSocketLDAP.endOfStream();
         } else {
            SocketMuxer.getMuxer().deliverEndOfStream(this.muxableSocketLDAP);
         }

      }
   }
}
