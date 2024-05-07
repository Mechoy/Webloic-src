package weblogic.socket;

import java.io.IOException;
import java.net.Socket;
import weblogic.platform.VM;

public class SocketUtils {
   public static int getFileDescriptor(Socket var0) throws IOException {
      if (var0 instanceof WeblogicSocket) {
         var0 = ((WeblogicSocket)var0).getSocket();
      }

      return VM.getVM().getFD(var0);
   }
}
