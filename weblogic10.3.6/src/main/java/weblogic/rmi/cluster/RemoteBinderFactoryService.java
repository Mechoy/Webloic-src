package weblogic.rmi.cluster;

import weblogic.server.AbstractServerService;

public final class RemoteBinderFactoryService extends AbstractServerService {
   public void start() {
      ClusterableRemoteBinderFactory.initialize();
      MigratableRemoteBinderFactory.initialize();
   }
}
