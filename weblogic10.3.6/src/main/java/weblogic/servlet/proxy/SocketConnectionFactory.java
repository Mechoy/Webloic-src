package weblogic.servlet.proxy;

import weblogic.common.ResourceException;
import weblogic.common.resourcepool.PooledResource;
import weblogic.common.resourcepool.PooledResourceFactory;
import weblogic.common.resourcepool.PooledResourceInfo;

public class SocketConnectionFactory implements PooledResourceFactory {
   private final String host;
   private final int port;

   public SocketConnectionFactory(String var1, int var2) {
      this.host = var1;
      this.port = var2;
   }

   public PooledResource createResource(PooledResourceInfo var1) throws ResourceException {
      return new SocketConnResource(this.host, this.port);
   }

   public void refreshResource(PooledResource var1) throws ResourceException {
      SocketConnResource var2 = (SocketConnResource)var1;
      var2.destroy();
      var2.initialize();
   }
}
