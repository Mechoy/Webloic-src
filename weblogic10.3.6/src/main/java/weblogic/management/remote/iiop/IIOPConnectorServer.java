package weblogic.management.remote.iiop;

import java.io.IOException;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;

public class IIOPConnectorServer extends RMIConnectorServer {
   public IIOPConnectorServer(JMXServiceURL var1, Map var2, MBeanServer var3) throws IOException {
      super(var1, var2, new IIOPServerImpl(var2), var3);
   }
}
