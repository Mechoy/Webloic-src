package weblogic.rjvm.t3.client;

import java.io.IOException;
import java.net.InetAddress;
import weblogic.protocol.ServerChannel;
import weblogic.rjvm.JVMID;
import weblogic.rjvm.MsgAbbrevJVMConnection;
import weblogic.rjvm.RJVMConnectionFactory;
import weblogic.socket.SocketMuxer;

public class ConnectionFactoryT3 implements RJVMConnectionFactory {
   public MsgAbbrevJVMConnection createConnection(InetAddress var1, int var2, ServerChannel var3, JVMID var4, int var5) throws IOException {
      MuxableSocketT3 var6 = new MuxableSocketT3(var3);
      int var8 = var5 > 0 ? var5 : var3.getConnectTimeout() * 1000;
      var6.connect(var1, var2, var8);
      SocketMuxer.getMuxer().register(var6);
      SocketMuxer.getMuxer().read(var6);
      return var6.getConnection();
   }
}
