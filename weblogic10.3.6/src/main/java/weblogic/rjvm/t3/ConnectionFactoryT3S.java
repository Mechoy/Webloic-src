package weblogic.rjvm.t3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import weblogic.protocol.ServerChannel;
import weblogic.rjvm.JVMID;
import weblogic.rjvm.MsgAbbrevJVMConnection;
import weblogic.rjvm.RJVMConnectionFactory;
import weblogic.security.utils.SSLIOContext;
import weblogic.security.utils.SSLIOContextTable;
import weblogic.socket.JSSESocket;
import weblogic.socket.SSLFilter;
import weblogic.socket.SocketMuxer;
import weblogic.socket.utils.JSSEUtils;

public class ConnectionFactoryT3S implements RJVMConnectionFactory {
   public MsgAbbrevJVMConnection createConnection(InetAddress var1, int var2, ServerChannel var3, JVMID var4, int var5) throws IOException {
      MuxableSocketT3S var6 = new MuxableSocketT3S(var3);
      MsgAbbrevJVMConnection var7 = var6.getConnection();
      var6.initializeClientCertPlugin(var4, var3);
      int var9 = var5 > 0 ? var5 : var3.getConnectTimeout() * 1000;

      try {
         var6.connect(var1, var2, var9);
      } catch (SSLException var15) {
         Socket var11 = var6.getSocket();
         if (var11 != null && !var11.isClosed()) {
            try {
               var11.close();
            } catch (IOException var14) {
            }
         }

         throw var15;
      }

      SSLSocket var10 = (SSLSocket)var6.getSocket();
      JSSESocket var16 = JSSEUtils.getJSSESocket(var10);
      if (var16 != null) {
         JSSEUtils.registerJSSEFilter(var16, var6);
         if (var6.isMessageComplete()) {
            var6.dispatch();
         } else {
            SocketMuxer.getMuxer().read(var16.getFilter());
         }
      } else {
         SSLIOContext var12 = SSLIOContextTable.findContext(var10);
         SSLFilter var13 = (SSLFilter)var12.getFilter();
         var6.setSocketFilter(var13);
         var13.setDelegate(var6);
         var13.activate();
      }

      return var7;
   }
}
