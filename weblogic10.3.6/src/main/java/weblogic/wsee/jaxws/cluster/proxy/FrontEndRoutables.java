package weblogic.wsee.jaxws.cluster.proxy;

import com.sun.xml.ws.api.message.Packet;
import java.util.Map;
import weblogic.servlet.proxy.HttpClusterServlet;
import weblogic.wsee.jaxws.cluster.BaseSOAPRouter;
import weblogic.wsee.jaxws.cluster.spi.AffinityBasedRoutingInfoFinder;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfo;

public class FrontEndRoutables extends BaseSOAPRouter.BaseRoutables {
   public HttpClusterServlet.RequestInfo ri;
   public Map<AffinityBasedRoutingInfoFinder, RoutingInfo> abstainers = null;

   public FrontEndRoutables(HttpClusterServlet.RequestInfo var1, Packet var2) {
      super(var2);
      this.ri = var1;
   }
}
