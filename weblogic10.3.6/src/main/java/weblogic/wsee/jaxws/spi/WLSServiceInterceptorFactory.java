package weblogic.wsee.jaxws.spi;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSFeatureList;
import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.api.client.ServiceCreationInterceptor;
import com.sun.xml.ws.api.client.ServiceInterceptor;
import com.sun.xml.ws.api.client.ServiceInterceptorFactory;
import com.sun.xml.ws.api.client.WSPortInfo;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.client.Stub;
import com.sun.xml.ws.developer.MemberSubmissionAddressingFeature;
import com.sun.xml.ws.developer.WSBindingProvider;
import java.lang.ref.WeakReference;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.soap.MTOMFeature;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.wsee.jaxws.EndpointPolicyUtility;
import weblogic.wsee.jaxws.ServiceCreationInterceptorFeature;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;

public class WLSServiceInterceptorFactory extends ServiceInterceptorFactory {
   public ServiceInterceptor create(WSService var1) {
      return new ServiceInterceptor() {
         public void postCreateDispatch(WSBindingProvider var1) {
            WSBinding var2 = (WSBinding)var1.getBinding();
            this.checkEndpointPolicy(var1);
            ServiceCreationInterceptorFeature var3 = (ServiceCreationInterceptorFeature)var2.getFeature(ServiceCreationInterceptorFeature.class);
            Iterator var5;
            if (var3 != null) {
               ArrayList var4 = new ArrayList();
               var5 = var3.getInterceptors().iterator();

               WeakReference var6;
               while(var5.hasNext()) {
                  var6 = (WeakReference)var5.next();
                  ServiceCreationInterceptor var7 = (ServiceCreationInterceptor)var6.get();
                  if (var7 != null) {
                     var7.postCreateDispatch(var1);
                  } else {
                     var4.add(var6);
                  }
               }

               var5 = var4.iterator();

               while(var5.hasNext()) {
                  var6 = (WeakReference)var5.next();
                  var3.getInterceptors().remove(var6);
               }
            }

            WSFeatureList var8 = var2.getFeatures();
            var5 = var8.iterator();

            while(var5.hasNext()) {
               WebServiceFeature var9 = (WebServiceFeature)var5.next();
               if (var9 instanceof ServiceCreationInterceptor) {
                  ((ServiceCreationInterceptor)var9).postCreateDispatch(var1);
               }
            }

         }

         public void postCreateProxy(WSBindingProvider var1, Class<?> var2) {
            WSBinding var3 = (WSBinding)var1.getBinding();
            this.checkEndpointPolicy(var1);
            ServiceCreationInterceptorFeature var4 = (ServiceCreationInterceptorFeature)var3.getFeature(ServiceCreationInterceptorFeature.class);
            ArrayList var5 = new ArrayList();
            if (var4 != null) {
               Iterator var6 = var4.getInterceptors().iterator();

               WeakReference var7;
               while(var6.hasNext()) {
                  var7 = (WeakReference)var6.next();
                  ServiceCreationInterceptor var8 = (ServiceCreationInterceptor)var7.get();
                  if (var8 != null) {
                     var8.postCreateProxy(var1, var2);
                  } else {
                     var5.add(var7);
                  }
               }

               var6 = var5.iterator();

               while(var6.hasNext()) {
                  var7 = (WeakReference)var6.next();
                  var4.getInterceptors().remove(var7);
               }
            }

            WSFeatureList var9 = var3.getFeatures();
            Iterator var10 = var9.iterator();

            while(var10.hasNext()) {
               WebServiceFeature var11 = (WebServiceFeature)var10.next();
               if (var11 instanceof ServiceCreationInterceptor) {
                  ((ServiceCreationInterceptor)var11).postCreateProxy(var1, var2);
               }
            }

         }

         public List<WebServiceFeature> preCreateBinding(@NotNull WSPortInfo var1, @Nullable Class<?> var2, @NotNull WSFeatureList var3) {
            ArrayList var4 = new ArrayList();
            Iterator var5 = var3.iterator();

            while(var5.hasNext()) {
               WebServiceFeature var6 = (WebServiceFeature)var5.next();
               var4.add(var6);
            }

            if (var3.isEnabled(AsyncClientTransportFeature.class) && !var3.isEnabled(AddressingFeature.class) && !var3.isEnabled(MemberSubmissionAddressingFeature.class)) {
               var4.add(new AddressingFeature());
            }

            return var4;
         }

         private void checkEndpointPolicy(WSBindingProvider var1) {
            Stub var2 = Proxy.isProxyClass(var1.getClass()) ? (Stub)Proxy.getInvocationHandler(var1) : (Stub)var1;
            WSDLPort var3 = var2.getWSDLPort();
            if (var3 != null) {
               EnvironmentFactory var4 = JAXRPCEnvironmentFeature.getFactory(var2.getBinding(), var2.getService(), var3);
               WsdlDefinitions var5 = var4.getWsdlDef();
               WsdlPort var6 = var5 != null ? (WsdlPort)((WsdlService)var5.getServices().get(var3.getOwner().getName())).getPorts().get(var3.getName()) : null;
               EnvironmentFactory.SingletonService var7 = var6 != null ? var4.getService() : null;
               PolicyServer var8 = var7 != null ? var7.getPolicyServer() : null;

               try {
                  NormalizedExpression var9 = var8.getEndpointPolicy(var6);
                  if (var9 != null) {
                     if (EndpointPolicyUtility.checkMTOMPolicy(var9) && var2.getBinding().getFeature(MTOMFeature.class) == null) {
                        ((WebServiceFeatureList)var2.getBinding().getFeatures()).add(new MTOMFeature());
                     }

                     WebServiceFeature var10 = EndpointPolicyUtility.checkUsingAddressingPolicy(var9);
                     if (var10 != null && var2.getBinding().getFeature(var10.getClass()) == null) {
                        ((WebServiceFeatureList)var2.getBinding().getFeatures()).add(var10);
                        var2.resetAddressingVersion();
                     }
                  }
               } catch (PolicyException var11) {
                  throw new WebServiceException(var11);
               }
            }

         }
      };
   }
}
