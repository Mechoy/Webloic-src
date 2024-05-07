package weblogic.rjvm.t3.client;

import java.io.IOException;
import java.net.InetAddress;
import javax.net.ssl.SSLSocket;
import weblogic.protocol.ServerChannel;
import weblogic.rjvm.JVMID;
import weblogic.rjvm.MsgAbbrevJVMConnection;
import weblogic.rjvm.RJVMConnectionFactory;
import weblogic.security.utils.SSLIOContext;
import weblogic.security.utils.SSLIOContextTable;
import weblogic.socket.ClientSSLFilterImpl;

public class ConnectionFactoryT3S implements RJVMConnectionFactory {
   public MsgAbbrevJVMConnection createConnection(InetAddress var1, int var2, ServerChannel var3, JVMID var4, int var5) throws IOException {
      MuxableSocketT3S var6 = new MuxableSocketT3S(var3);
      int var8 = var5 > 0 ? var5 : var3.getConnectTimeout() * 1000;
      var6.connect(var1, var2, var8);
      SSLSocket var9 = (SSLSocket)var6.getSocket();
      ClientSSLFilterImpl var10 = new ClientSSLFilterImpl(var9.getInputStream(), var9);
      SSLIOContext var11 = new SSLIOContext(var9.getInputStream(), var9.getOutputStream(), var9, var10);
      SSLIOContextTable.addContext(var11);
      SSLIOContext var12 = SSLIOContextTable.findContext(var9);
      var6.setSocketFilter(var10);
      var10.setDelegate(var6);
      var10.activate();
      return var6.getConnection();
   }
}
