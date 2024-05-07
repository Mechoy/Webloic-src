package weblogic.messaging.dispatcher;

import weblogic.rmi.extensions.server.RemoteDomainSecurityHelper;
import weblogic.rmi.spi.EndPoint;

public final class ServerCrossDomainUtil extends CrossDomainUtilCommon implements CrossDomainUtil {
   public boolean isRemoteDomain(DispatcherWrapper var1) {
      DispatcherRemote var2 = var1.getRemoteDispatcher();
      return var2 instanceof DispatcherProxy && RemoteDomainSecurityHelper.isRemoteDomain((EndPoint)((DispatcherProxy)var2).getRJVM());
   }
}
