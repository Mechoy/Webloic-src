package weblogic.rjvm.http;

import java.io.IOException;
import java.net.InetAddress;
import weblogic.protocol.ServerChannel;
import weblogic.rjvm.JVMID;
import weblogic.rjvm.MsgAbbrevJVMConnection;
import weblogic.rjvm.RJVMConnectionFactory;
import weblogic.work.WorkManagerFactory;

public class HTTPSClientConnectionFactory implements RJVMConnectionFactory {
   public MsgAbbrevJVMConnection createConnection(InetAddress var1, int var2, ServerChannel var3, JVMID var4, int var5) throws IOException {
      HTTPSClientJVMConnection var6 = new HTTPSClientJVMConnection(var3);
      var6.connect(var1, var2);
      WorkManagerFactory.getInstance().getSystem().schedule(var6);
      return var6;
   }
}
