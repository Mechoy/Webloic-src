package weblogic.socket.utils;

import java.io.IOException;
import javax.net.ssl.SSLSocket;
import weblogic.socket.JSSEFilterImpl;
import weblogic.socket.JSSESocket;
import weblogic.socket.MuxableSocket;
import weblogic.socket.SocketMuxer;

public final class JSSEUtils {
   public static JSSESocket getJSSESocket(SSLSocket var0) {
      return var0 instanceof JSSESocket ? (JSSESocket)var0 : null;
   }

   public static void registerJSSEFilter(JSSESocket var0, MuxableSocket var1) throws IOException {
      JSSEFilterImpl var2 = var0.getFilter();
      var1.setSocketFilter(var2);
      var2.setDelegate(var1);
      SocketMuxer.getMuxer().register(var2);
   }

   public static void activate(JSSESocket var0, MuxableSocket var1) throws IOException {
      try {
         var0.startHandshake();
      } catch (IOException var5) {
         if (!var0.isClosed()) {
            try {
               var0.close();
            } catch (IOException var4) {
            }
         }

         throw var5;
      }

      if (var1.isMessageComplete()) {
         var1.dispatch();
      } else {
         SocketMuxer.getMuxer().read(var0.getFilter());
      }

   }
}
