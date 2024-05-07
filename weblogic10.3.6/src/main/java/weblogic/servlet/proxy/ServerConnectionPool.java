package weblogic.servlet.proxy;

import java.util.Properties;
import weblogic.common.ResourceException;
import weblogic.common.resourcepool.PooledResourceFactory;
import weblogic.common.resourcepool.ResourcePoolImpl;

public class ServerConnectionPool extends ResourcePoolImpl {
   private final SocketConnectionFactory factory;

   ServerConnectionPool(String var1, int var2) {
      this.factory = new SocketConnectionFactory(var1, var2);
   }

   public PooledResourceFactory initPooledResourceFactory(Properties var1) throws ResourceException {
      return this.factory;
   }
}
