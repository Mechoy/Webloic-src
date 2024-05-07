package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.BindingID;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.client.ServiceCreationInterceptor;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.PartialForwardTube;
import com.sun.xml.ws.api.pipe.ResponseOnlyTube;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Stubs;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.TubelineAssembler;
import com.sun.xml.ws.api.pipe.TubelineAssemblerFactory;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.client.Stub;
import com.sun.xml.ws.server.EndpointFactory;
import com.sun.xml.ws.util.ServiceFinder;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Holder;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.wsee.jaxws.client.async.AsyncClientTransportReconnectTube;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.jaxws.tubeline.FlowControlAware;
import weblogic.wsee.jaxws.tubeline.FlowControlTube;
import weblogic.wsee.jaxws.tubeline.Ordering;
import weblogic.wsee.jaxws.tubeline.TubeFactory;
import weblogic.wsee.jaxws.tubeline.TubelineAssemblerItem;
import weblogic.wsee.jaxws.tubeline.TubelineDeploymentListener;
import weblogic.wsee.jaxws.tubeline.TubelineDeploymentListenerRepository;
import weblogic.wsee.jaxws.tubeline.TubelineSpliceFactory;
import weblogic.wsee.jaxws.tubeline.TubelineSpliceMarkerTube;
import weblogic.wsee.policy.checker.PolicyLevelChecker;
import weblogic.wsee.util.Pair;

public class WLSTubelineAssemblerFactory extends TubelineAssemblerFactory {
   private static final Logger LOGGER = Logger.getLogger(WLSTubelineAssemblerFactory.class.getName());

   public TubelineAssembler doCreate(BindingID var1) {
      return new TubelineAssemblerImpl();
   }

   private class TubelineAssemblerImpl implements TubelineAssembler {
      private TubelineAssemblerImpl() {
      }

      public Tube createClient(ClientTubeAssemblerContext var1) {
         WLSTubelineAssemblerFactory.LOGGER.fine("Creating client tubeline for " + var1.getService().getServiceName());
         Object var2 = var1.createTransportTube();
         if (var2 instanceof ServiceCreationInterceptor) {
            ServiceCreationInterceptorFeature var3 = (ServiceCreationInterceptorFeature)var1.getBinding().getFeature(ServiceCreationInterceptorFeature.class);
            if (var3 == null) {
               var3 = new ServiceCreationInterceptorFeature();
               ((WebServiceFeatureList)var1.getBinding().getFeatures()).add(var3);
            }

            var3.getInterceptors().add(new WeakReference((ServiceCreationInterceptor)var2));
         }

         boolean var15 = false;
         if (var2 instanceof FlowControlAware) {
            var15 = ((FlowControlAware)var2).isFlowControlRequired();
         }

         if (var1.getBinding().getFeature(TransportOnlyFeature.class) != null) {
            return (Tube)(var15 ? new FlowControlTube((Tube)var2) : var2);
         } else {
            LinkedHashSet var4 = new LinkedHashSet();
            Object var5 = null;
            TubelineDeploymentListenerRepository var6 = (TubelineDeploymentListenerRepository)var1.getService().getSPI(TubelineDeploymentListenerRepository.class);
            if (var6 != null) {
               var5 = var6.getListeners();
            }

            if (var5 == null) {
               var5 = ServiceFinder.find(TubelineDeploymentListener.class, var1.getService().getClass().getClassLoader());
            }

            Iterator var7 = ((Iterable)var5).iterator();

            while(var7.hasNext()) {
               TubelineDeploymentListener var8 = (TubelineDeploymentListener)var7.next();
               var8.createClient(var1, var4);
            }

            AsyncClientTransportFeature var16 = (AsyncClientTransportFeature)var1.getBinding().getFeature(AsyncClientTransportFeature.class);
            if (var16 != null) {
               var2 = new AsyncClientTransportReconnectTube(var16, var1, (Tube)var2);
            }

            Ordering var17 = this.prepareTubelineAssemblerItems(var4);
            Holder var9 = null;
            Iterator var10 = var17.iterator();

            while(var10.hasNext()) {
               TubelineAssemblerItem var11 = (TubelineAssemblerItem)var10.next();
               WLSTubelineAssemblerFactory.LOGGER.finer("Calling tubeline assembler item: " + var11.getName());
               TubeFactory var12 = var11.getFactory();
               if (var12 instanceof TubelineSpliceFactory) {
                  if (var9 != null) {
                     var9.value = TubeCloner.clone((Tube)var9.value);
                  }

                  var9 = new Holder(var2);
                  ((TubelineSpliceFactory)var12).createSplice((TubelineSpliceFactory.ClientDispatchFactory)(new MyClientDispatchFactory(var1, var9, TubeCloner.clone((Tube)var2))), (ClientTubeAssemblerContext)var1);
                  var2 = new TubelineSpliceMarkerTube((Tube)var2);
               }

               Tube var13 = var12.createClient((Tube)var2, var1);
               if (var13 != null) {
                  var2 = var13;
                  if (!var15 && var13 instanceof FlowControlAware) {
                     var15 = ((FlowControlAware)var13).isFlowControlRequired();
                  }

                  if (var13 instanceof ServiceCreationInterceptor) {
                     ServiceCreationInterceptorFeature var14 = (ServiceCreationInterceptorFeature)var1.getBinding().getFeature(ServiceCreationInterceptorFeature.class);
                     if (var14 == null) {
                        var14 = new ServiceCreationInterceptorFeature();
                        ((WebServiceFeatureList)var1.getBinding().getFeatures()).add(var14);
                     }

                     var14.getInterceptors().add(new WeakReference((ServiceCreationInterceptor)var13));
                  }

                  if (var9 != null) {
                     var9.value = var15 ? new FlowControlTube(var13) : var13;
                  }
               }
            }

            return (Tube)(var15 ? new FlowControlTube((Tube)var2) : var2);
         }
      }

      public Tube createServer(ServerTubeAssemblerContext var1) {
         WLSTubelineAssemblerFactory.LOGGER.fine("Creating server tubeline for " + var1.getEndpoint().getServiceName());
         this.doPolicyCheck(var1);
         Object var2 = var1.getTerminalTube();
         if (var2 instanceof EndpointCreationInterceptor) {
            EndpointCreationInterceptorFeature var3 = (EndpointCreationInterceptorFeature)var1.getEndpoint().getBinding().getFeature(EndpointCreationInterceptorFeature.class);
            if (var3 == null) {
               var3 = new EndpointCreationInterceptorFeature();
               ((WebServiceFeatureList)var1.getEndpoint().getBinding().getFeatures()).add(var3);
            }

            var3.getInterceptors().add(new WeakReference((EndpointCreationInterceptor)var2));
         }

         boolean var16 = false;
         if (var2 instanceof FlowControlAware) {
            var16 = ((FlowControlAware)var2).isFlowControlRequired();
         }

         if (var1.getEndpoint().getBinding().getFeature(InvokerOnlyFeature.class) != null) {
            return (Tube)(var16 ? new FlowControlTube((Tube)var2) : var2);
         } else {
            LinkedHashSet var4 = new LinkedHashSet();
            Object var5 = null;
            TubelineDeploymentListenerRepository var6 = (TubelineDeploymentListenerRepository)var1.getEndpoint().getSPI(TubelineDeploymentListenerRepository.class);
            if (var6 != null) {
               var5 = var6.getListeners();
            }

            if (var5 == null) {
               var5 = ServiceFinder.find(TubelineDeploymentListener.class, var1.getEndpoint().getImplementationClass().getClassLoader());
            }

            Iterator var7 = ((Iterable)var5).iterator();

            while(var7.hasNext()) {
               TubelineDeploymentListener var8 = (TubelineDeploymentListener)var7.next();
               var8.createServer(var1, var4);
            }

            Ordering var17 = this.prepareTubelineAssemblerItems(var4);
            Pair var18 = null;
            Container var9 = var1.getEndpoint().getContainer();
            EndpointFactory var10 = var9 != null ? (EndpointFactory)var9.getSPI(EndpointFactory.class) : null;
            if (var10 == null) {
               var10 = EndpointFactory.getInstance();
            }

            Iterator var11 = var17.iterator();

            while(var11.hasNext()) {
               TubelineAssemblerItem var12 = (TubelineAssemblerItem)var11.next();
               WLSTubelineAssemblerFactory.LOGGER.finer("Calling tubeline assembler item: " + var12.getName());
               TubeFactory var13 = var12.getFactory();
               if (var13 instanceof TubelineSpliceFactory) {
                  if (var18 != null) {
                     ((Holder)var18.getRight()).value = var10.createSpliceEndpoint(var1.getEndpoint(), var1.getSEIModel(), TubeCloner.clone((Tube)(var16 ? new FlowControlTube((Tube)var2) : var2)), TubeCloner.clone((Tube)var18.getLeft()));
                  }

                  var18 = new Pair(var2, new Holder());
                  final Holder var14 = (Holder)var18.getRight();
                  ((TubelineSpliceFactory)var13).createSplice(new TubelineSpliceFactory.DispatchFactory() {
                     public <T> Dispatch<T> createDispatch(Class<T> var1, Service.Mode var2) {
                        return ((WSEndpoint)var14.value).createDispatch(var1, var2);
                     }

                     public <T> Dispatch<T> createResponseDispatch(WSEndpointReference var1, Class<T> var2, Service.Mode var3) {
                        return ((WSEndpoint)var14.value).createResponseDispatch(var2, var3, var1);
                     }
                  }, var1);
                  var2 = new TubelineSpliceMarkerTube((Tube)var2);
               }

               Tube var19 = var13.createServer((Tube)var2, var1);
               if (var19 != null) {
                  var2 = var19;
                  if (!var16 && var19 instanceof FlowControlAware) {
                     var16 = ((FlowControlAware)var19).isFlowControlRequired();
                  }

                  if (var19 instanceof EndpointCreationInterceptor) {
                     EndpointCreationInterceptorFeature var15 = (EndpointCreationInterceptorFeature)var1.getEndpoint().getBinding().getFeature(EndpointCreationInterceptorFeature.class);
                     if (var15 == null) {
                        var15 = new EndpointCreationInterceptorFeature();
                        ((WebServiceFeatureList)var1.getEndpoint().getBinding().getFeatures()).add(var15);
                     }

                     var15.getInterceptors().add(new WeakReference((EndpointCreationInterceptor)var19));
                  }
               }
            }

            if (var18 != null) {
               ((Holder)var18.getRight()).value = var10.createSpliceEndpoint(var1.getEndpoint(), var1.getSEIModel(), TubeCloner.clone((Tube)(var16 ? new FlowControlTube((Tube)var2) : var2)), TubeCloner.clone((Tube)var18.getLeft()));
            }

            return (Tube)(var16 ? new FlowControlTube((Tube)var2) : var2);
         }
      }

      private Ordering prepareTubelineAssemblerItems(Set<TubelineAssemblerItem> var1) {
         Iterator var2 = var1.iterator();

         while(true) {
            TubelineAssemblerItem var3;
            do {
               if (!var2.hasNext()) {
                  return new Ordering(var1);
               }

               var3 = (TubelineAssemblerItem)var2.next();
            } while(var3.getRequired() == null);

            Iterator var4 = var3.getRequired().iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               boolean var6 = false;
               Iterator var7 = var1.iterator();

               while(var7.hasNext()) {
                  TubelineAssemblerItem var8 = (TubelineAssemblerItem)var7.next();
                  if (var5.equals(var8.getName())) {
                     var6 = true;
                     break;
                  }
               }

               if (!var6) {
                  throw new WebServiceException("TubelineAssemberItem " + var3.getName() + " requires support for " + var5 + ", which is not configured");
               }
            }
         }
      }

      public void doPolicyCheck(ServerTubeAssemblerContext var1) throws WebServiceException {
         EnvironmentFactory var2 = JAXRPCEnvironmentFeature.getFactory(var1.getEndpoint());
         if (var2 != null) {
            WSDLPort var3 = var2.getPort();
            if (var3 != null) {
               EnvironmentFactory.SingletonService var4 = var2.getService();
               if (var4 != null) {
                  QName var5 = var3.getName();
                  PolicyLevelChecker var6 = new PolicyLevelChecker(var4, var5);
                  var6.doJAXWSDefaultCheck();
               }
            }
         }
      }

      // $FF: synthetic method
      TubelineAssemblerImpl(Object var2) {
         this();
      }

      private class MyClientDispatchFactory implements TubelineSpliceFactory.ClientDispatchFactory {
         private final ClientTubeAssemblerContext context;
         private final Holder<Tube> preSpliceHeadHolder;
         private final Tube tube;

         public MyClientDispatchFactory(ClientTubeAssemblerContext var2, Holder<Tube> var3, Tube var4) {
            this.context = var2;
            this.preSpliceHeadHolder = var3;
            this.tube = var4;
         }

         public <T> Dispatch<T> createDispatch(Class<T> var1, Service.Mode var2) {
            return this.createDispatch((WSEndpointReference)null, var1, var2);
         }

         public <T> Dispatch<T> createPostSpliceDispatch(Class<T> var1, Service.Mode var2) {
            return this.createPostSpliceDispatch((WSEndpointReference)null, var1, var2);
         }

         public <T> Dispatch<T> createResponseDispatch(WSEndpointReference var1, Class<T> var2, Service.Mode var3) {
            WSDLPort var4 = this.context.getWsdlModel();
            ResponseOnlyTube var5 = new ResponseOnlyTube((Tube)this.preSpliceHeadHolder.value, this.tube, false, true);
            Dispatch var6 = Stubs.createDispatch(var4 != null ? var4.getName() : null, this.context.getService(), this.context.getBinding(), var2, var3, var5, var1);
            ((Stub)var6).setServerResponse(true);
            return var6;
         }

         public <T> Dispatch<T> createDispatch(WSEndpointReference var1, Class<T> var2, Service.Mode var3) {
            WSDLPort var4 = this.context.getWsdlModel();
            return Stubs.createDispatch(var4 != null ? var4.getName() : null, this.context.getService(), this.context.getBinding(), var2, var3, new PartialForwardTube((Tube)this.preSpliceHeadHolder.value, this.tube), var1);
         }

         public <T> Dispatch<T> createPostSpliceDispatch(WSEndpointReference var1, Class<T> var2, Service.Mode var3) {
            WSDLPort var4 = this.context.getWsdlModel();
            Dispatch var5 = Stubs.createDispatch(var4 != null ? var4.getName() : null, this.context.getService(), this.context.getBinding(), var2, var3, TubeCloner.clone(this.tube), var1);
            ((Stub)var5).setServerResponse(true);
            return var5;
         }
      }
   }
}
