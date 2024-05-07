package weblogic.wsee.jaxws.cluster.proxy;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.servlet.proxy.HttpClusterServlet;
import weblogic.wsee.jaxws.cluster.BaseSOAPRouter;
import weblogic.wsee.jaxws.cluster.spi.AffinityBasedRoutingInfoFinder;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfo;

public class FrontEndSOAPRouter extends BaseSOAPRouter<HttpClusterServlet.Server, FrontEndRoutables> {
   private static final Logger LOGGER = Logger.getLogger(FrontEndSOAPRouter.class.getName());
   private SOAPRoutingHandler _handler;

   public FrontEndSOAPRouter(SOAPRoutingHandler var1) {
      this._handler = var1;
   }

   protected HttpClusterServlet.Server getTargetServerForRouting(RoutingInfo var1) {
      return this._handler.getTargetServerForRouting(var1);
   }

   protected RoutingInfo getRoutingForTargetServer(HttpClusterServlet.Server var1) {
      return this._handler.getRoutingForTargetServer(var1);
   }

   protected FrontEndRoutables deliverMessageToTargetServer(FrontEndRoutables var1, HttpClusterServlet.Server var2) throws Exception {
      var1.ri.setPrimaryServer(var2);
      if (var1.ri.getPrimaryServer() == null) {
         if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.warning("WARNING: Failed to set target server (" + var2 + ") on HttpClusterServlet.RequestInfo. Request will be round-robin'd into the cluster instead of being sent explicitly to intended target.");
         }

         this._handler.requestUpdatedStoreToServerMap();
      }

      return var1;
   }

   protected FrontEndRoutables setAbstainedFinders(FrontEndRoutables var1, Map<AffinityBasedRoutingInfoFinder, RoutingInfo> var2) {
      var1.abstainers = var2;
      return var1;
   }
}
