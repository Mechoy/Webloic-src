package weblogic.management.remote.iiop;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerProvider;
import javax.management.remote.JMXServiceURL;

public class ServerProvider implements JMXConnectorServerProvider {
   public JMXConnectorServer newJMXConnectorServer(JMXServiceURL var1, Map var2, MBeanServer var3) throws IOException {
      if (!var1.getProtocol().equals("iiop")) {
         throw new MalformedURLException("Protocol not iiop: " + var1.getProtocol());
      } else {
         return new IIOPConnectorServer(var1, var2, var3);
      }
   }
}
