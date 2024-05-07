package weblogic.wsee.jaxws.cluster;

import com.sun.xml.ws.api.addressing.OneWayFeature;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.server.WSEndpointImpl;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3c.dom.Element;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.kernel.KernelStatus;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.jaxws.spi.WLSEndpoint;
import weblogic.wsee.jaxws.tubeline.AbstractTubeFactory;
import weblogic.wsee.jaxws.tubeline.TubelineAssemblerItem;
import weblogic.wsee.jaxws.tubeline.TubelineDeploymentListener;

public class ClusterTubelineDeploymentListener implements TubelineDeploymentListener {
   private static final Logger LOGGER = Logger.getLogger(ClusterTubelineDeploymentListener.class.getName());
   private static final boolean _isClusterServer;

   public static boolean isClusterServer() {
      return _isClusterServer;
   }

   public void createClient(ClientTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      if (_isClusterServer && var1.getBinding().getAddressingVersion() != null) {
         MyInjectionTubeFactory var3 = new MyInjectionTubeFactory();
         TubelineAssemblerItem var4 = new TubelineAssemblerItem("ClusterInjectionClient", var3);
         HashSet var5 = new HashSet();
         var5.add("WS_SECURITY_1.1");
         var4.setGoAfter(var5);
         var2.add(var4);
         MyRoutingTubeFactory var6 = new MyRoutingTubeFactory();
         TubelineAssemblerItem var7 = new TubelineAssemblerItem("ClusterRoutingClient", var6);
         HashSet var8 = new HashSet();
         var8.add("WS_SECURITY_1.1");
         var7.setGoBefore(var8);
         var2.add(var7);
      }
   }

   public void createServer(ServerTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      if (_isClusterServer) {
         TubelineAssemblerItem var4;
         HashSet var5;
         if (var1.getEndpoint().getBinding().getAddressingVersion() != null) {
            MyInjectionTubeFactory var3 = new MyInjectionTubeFactory();
            var4 = new TubelineAssemblerItem("ClusterInjectionServer", var3);
            var5 = new HashSet();
            var5.add("WS_SECURITY_1.1");
            var4.setGoBefore(var5);
            var2.add(var4);
         }

         MyRoutingTubeFactory var6 = new MyRoutingTubeFactory();
         var4 = new TubelineAssemblerItem("ClusterRoutingServer", var6);
         var5 = new HashSet();
         var5.add("WS_SECURITY_1.1");
         var4.setGoAfter(var5);
         var2.add(var4);
      }
   }

   static {
      if (KernelStatus.isServer()) {
         AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         RuntimeAccess var1 = ManagementService.getRuntimeAccess(var0);
         _isClusterServer = var1.getServer().getCluster() != null;
      } else {
         _isClusterServer = false;
      }

   }

   private static class MyAsyncEndpointListener implements AsyncClientTransportFeature.AsyncEndpointListener {
      MyRoutingTubeFactory _listener;
      ClientTubeAssemblerContext _context;
      AsyncClientTransportFeature _feature;
      ClusterRoutingClientTube _routingTube;

      public MyAsyncEndpointListener(MyRoutingTubeFactory var1, ClientTubeAssemblerContext var2, AsyncClientTransportFeature var3, ClusterRoutingClientTube var4) {
         this._listener = var1;
         this._context = var2;
         this._feature = var3;
         this._routingTube = var4;
      }

      public void endpointSet(AsyncClientTransportFeature var1) {
         Endpoint var2 = var1.getEndpoint();
         if (var2 instanceof WLSEndpoint) {
            WLSEndpoint var3 = (WLSEndpoint)var2;
            this._routingTube.setEndpoint(var3.getWSEndpoint());
         }

      }

      public void endpointPublished(AsyncClientTransportFeature var1) {
         if (ClusterTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE) && var1.getEndpoint() != null) {
            Endpoint var2 = var1.getEndpoint();
            W3CEndpointReference var3 = (W3CEndpointReference)var2.getEndpointReference(W3CEndpointReference.class, new Element[0]);
            ClusterTubelineDeploymentListener.LOGGER.fine("Cluster routing client tube deployment listener " + this._listener + " detected that feature " + var1 + " with endpoint " + var3 + " is now published");
         }

         var1.removeAsyncEndpointListener(this);
         this.endpointSet(var1);
      }

      public void endpointDisposed(AsyncClientTransportFeature var1) {
         var1.removeAsyncEndpointListener(this);
      }
   }

   private static class MyRoutingTubeFactory extends AbstractTubeFactory {
      public MyRoutingTubeFactory() {
      }

      public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
         if (ClusterTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
            ClusterTubelineDeploymentListener.LOGGER.fine("Cluster RoutingTubeFactory.createClient(" + var2.getAddress() + ") with isClusterServer=" + ClusterTubelineDeploymentListener._isClusterServer);
         }

         ClusterRoutingClientTube var3 = null;
         if (ClusterTubelineDeploymentListener._isClusterServer) {
            AsyncClientTransportFeature var4 = (AsyncClientTransportFeature)var2.getBinding().getFeature(AsyncClientTransportFeature.class);
            if (var4 != null) {
               if (ClusterTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
                  ClusterTubelineDeploymentListener.LOGGER.fine("Cluster deployment listener " + this + " setting up client tubes for client with AsyncClientTransportFeature " + var4);
               }

               ClusterRoutingClientTube var5 = new ClusterRoutingClientTube(var1, var2, (WSEndpoint)null);
               var3 = var5;
               MyAsyncEndpointListener var6 = new MyAsyncEndpointListener(this, var2, var4, var5);
               if (var4.isEndpointPublished()) {
                  var6.endpointPublished(var4);
               } else {
                  if (ClusterTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
                     ClusterTubelineDeploymentListener.LOGGER.fine("Cluster deployment listener " + this + " listening for future endpoint publish on  AsyncClientTransportFeature " + var4);
                  }

                  var4.addAsyncEndpointListener(var6);
               }
            }
         }

         return var3;
      }

      public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
         if (ClusterTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
            ClusterTubelineDeploymentListener.LOGGER.fine("Cluster RoutingTubeFactory.createServer(" + var2.getEndpoint() + ") with isClusterServer=" + ClusterTubelineDeploymentListener._isClusterServer);
         }

         ClusterRoutingServerTube var3 = null;
         if (ClusterTubelineDeploymentListener._isClusterServer) {
            WSEndpoint var4 = var2.getEndpoint();
            var3 = new ClusterRoutingServerTube(var1, var2, var4);
         }

         return var3;
      }
   }

   private static class MyInjectionTubeFactory extends AbstractTubeFactory {
      public MyInjectionTubeFactory() {
      }

      public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
         if (ClusterTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
            ClusterTubelineDeploymentListener.LOGGER.fine("Cluster InjectionTubeFactory.createClient(" + var2.getAddress() + ") with isClusterServer=" + ClusterTubelineDeploymentListener._isClusterServer);
         }

         ClusterInjectionClientTube var3 = null;
         if (ClusterTubelineDeploymentListener._isClusterServer) {
            WebServiceFeature var4 = var2.getBinding().getFeature(AsyncClientTransportFeature.class);
            if (var4 == null) {
               var4 = var2.getBinding().getFeature(OneWayFeature.class);
            }

            if (var4 != null) {
               var3 = new ClusterInjectionClientTube(var1, var2);
            }
         }

         return var3;
      }

      public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
         if (ClusterTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
            ClusterTubelineDeploymentListener.LOGGER.fine("Cluster InjectionTubeFactory.createServer(" + var2.getEndpoint() + ") with isClusterServer=" + ClusterTubelineDeploymentListener._isClusterServer);
         }

         ClusterInjectionServerTube var3 = null;
         if (ClusterTubelineDeploymentListener._isClusterServer) {
            WSEndpoint var4 = var2.getEndpoint();
            if (var4 instanceof WSEndpointImpl) {
               var3 = new ClusterInjectionServerTube(var1, var2);
            }
         }

         return var3;
      }
   }
}
