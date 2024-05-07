package weblogic.wsee.jaxws.tubeline.standard;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.util.pipe.StandaloneTubeAssembler;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import weblogic.jws.jaxws.client.async.AsyncClientHandlerFeature;
import weblogic.kernel.KernelStatus;
import weblogic.wsee.jaxws.ClientLateInitTube;
import weblogic.wsee.jaxws.MonitoringPipe;
import weblogic.wsee.jaxws.ServerLateInitTube;
import weblogic.wsee.jaxws.WLSContainer;
import weblogic.wsee.jaxws.client.async.HandlerInvokerTube;
import weblogic.wsee.jaxws.security.AuthorizationTube;
import weblogic.wsee.jaxws.tubeline.AbstractTubeFactory;
import weblogic.wsee.jaxws.tubeline.TubelineAssemblerItem;
import weblogic.wsee.jaxws.tubeline.TubelineDeploymentListener;
import weblogic.wsee.jaxws.workcontext.WorkContextClientTube;
import weblogic.wsee.jaxws.workcontext.WorkContextServerTube;

public class StandardTubelineDeploymentListener implements TubelineDeploymentListener {
   private static final Logger LOGGER = Logger.getLogger(StandardTubelineDeploymentListener.class.getName());
   private static final boolean dump;

   public void createClient(ClientTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      LOGGER.fine("Creating client tubeline items for " + var1.getService().getServiceName());
      var2.add(new TubelineAssemblerItem("client", new AbstractTubeFactory() {
         public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
            WseeClientTube var3 = new WseeClientTube(var2, var1);
            return var3;
         }
      }, (Set)null, Collections.singleton("monitoring"), (Set)null));
      var2.add(new TubelineAssemblerItem("async_handler", new AbstractTubeFactory() {
         public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
            WSBinding var3 = var2.getBinding();
            AsyncClientHandlerFeature var4 = (AsyncClientHandlerFeature)var3.getFeature(AsyncClientHandlerFeature.class);
            return (Tube)(var4 != null ? new HandlerInvokerTube(var1, var4, var3) : var1);
         }
      }));
      var2.add(new TubelineAssemblerItem("monitoring", new AbstractTubeFactory() {
         public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
            return (Tube)(KernelStatus.isServer() ? new MonitoringPipe(var2.getWsdlModel(), var1, var2.getBinding(), var2.getService(), true) : var1);
         }
      }));
      var2.add(new TubelineAssemblerItem("handler", new AbstractTubeFactory() {
         public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
            return var2.createHandlerTube(var1);
         }
      }, (Set)null, Collections.singleton("async_handler"), (Set)null));
      var2.add(new TubelineAssemblerItem("validation", new AbstractTubeFactory() {
         public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
            return var2.createValidationTube(var1);
         }
      }, (Set)null, Collections.singleton("handler"), (Set)null));
      var2.add(new TubelineAssemblerItem("workcontext", new AbstractTubeFactory() {
         public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
            return new WorkContextClientTube(var1);
         }
      }, (Set)null, Collections.singleton("validation"), (Set)null));
      var2.add(new TubelineAssemblerItem("mu", new AbstractTubeFactory() {
         public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
            return var2.createClientMUTube(var1);
         }
      }, (Set)null, Collections.singleton("workcontext"), (Set)null));
      var2.add(new TubelineAssemblerItem("ADDRESSING_HANDLER", new AbstractTubeFactory() {
         public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
            return new ClientLateInitTube(var1, var2, new AbstractTubeFactory() {
               public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
                  return var2.createWsaTube(var1);
               }
            });
         }
      }, Collections.singleton("PRE_WS_SECURITY_POLICY_1.2"), Collections.singleton("mu"), (Set)null));
      if (dump) {
         var2.add(new TubelineAssemblerItem("dump", new AbstractTubeFactory() {
            public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
               return var2.createDumpTube("client", System.out, var1);
            }
         }, (Set)null, new HashSet(Arrays.asList("ADDRESSING_HANDLER", "POST_WS_SECURITY_POLICY_1.2", "OWSM_SECURITY_POLICY_HANDLER")), (Set)null));
      }

   }

   public void createServer(ServerTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      LOGGER.fine("Creating server tubeline items for service " + var1.getEndpoint().getServiceName() + " with ID " + var1.getEndpoint().getEndpointId());
      var2.add(new TubelineAssemblerItem("WseeServer", new AbstractTubeFactory() {
         public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
            return new WseeServerTube(var2, var1);
         }
      }));
      if (var1.getEndpoint().getContainer() instanceof WLSContainer) {
         var2.add(new TubelineAssemblerItem("monitoring", new AbstractTubeFactory() {
            public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
               return new MonitoringPipe(var2.getEndpoint().getPort(), var1, var2.getEndpoint().getBinding(), var2.getEndpoint(), false);
            }
         }));
      }

      var2.add(new TubelineAssemblerItem("ADDRESSING_HANDLER", new AbstractTubeFactory() {
         public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
            return new ServerLateInitTube(var1, var2, new AbstractTubeFactory() {
               public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
                  return var2.createWsaTube(var1);
               }
            });
         }
      }, Collections.singleton("PRE_WS_SECURITY_POLICY_1.2"), Collections.singleton("monitoring"), (Set)null));
      var2.add(new TubelineAssemblerItem("mu", new AbstractTubeFactory() {
         public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
            return new ServerLateInitTube(var1, var2, new AbstractTubeFactory() {
               public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
                  return var2.createServerMUTube(var1);
               }
            });
         }
      }, new HashSet(Arrays.asList("WS_SECURITY_1.1", "POST_WS_SECURITY_POLICY_1.2", "OWSM_SECURITY_POLICY_HANDLER")), new HashSet(Arrays.asList("ADDRESSING_HANDLER", "PRE_WS_SECURITY_POLICY_1.2")), (Set)null));
      if (KernelStatus.isServer()) {
         var2.add(new TubelineAssemblerItem("AUTHORIZATION_HANDLER", new AbstractTubeFactory() {
            public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
               return new AuthorizationTube(var2, var1);
            }
         }, (Set)null, new HashSet(Arrays.asList("mu", "PRE_WS_SECURITY_POLICY_1.2", "WS_SECURITY_1.1", "POST_WS_SECURITY_POLICY_1.2", "OWSM_SECURITY_POLICY_HANDLER")), (Set)null));
      }

      var2.add(new TubelineAssemblerItem("workcontext", new AbstractTubeFactory() {
         public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
            return new WorkContextServerTube(var1);
         }
      }, (Set)null, new HashSet(Arrays.asList("mu", "AUTHORIZATION_HANDLER")), (Set)null));
      var2.add(new TubelineAssemblerItem("handler", new AbstractTubeFactory() {
         public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
            return var2.createHandlerTube(var1);
         }
      }, (Set)null, Collections.singleton("workcontext"), (Set)null));
      var2.add(new TubelineAssemblerItem("validation", new AbstractTubeFactory() {
         public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
            return var2.createValidationTube(var1);
         }
      }, (Set)null, Collections.singleton("handler"), (Set)null));
   }

   static {
      boolean var0 = false;

      try {
         var0 = Boolean.getBoolean(StandaloneTubeAssembler.class.getName() + ".dump") || Boolean.getBoolean(StandardTubelineDeploymentListener.class.getName() + ".dump");
      } catch (Throwable var2) {
      }

      dump = var0;
   }
}
