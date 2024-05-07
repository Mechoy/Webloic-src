package weblogic.ldap;

import com.octetstring.vde.LDAPServer;
import java.io.IOException;
import java.net.Socket;
import weblogic.protocol.ServerChannel;
import weblogic.utils.io.Chunk;

final class MuxableSocketLDAPS extends MuxableSocketLDAP {
   MuxableSocketLDAPS(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      super(var1, var2, var3);
   }

   static void initialize(LDAPServer var0) {
      ProtocolHandlerLDAPS.getProtocolHandler();
   }
}
