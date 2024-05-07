package weblogic.wsee.jaxws.framework.jaxrpc.server;

import com.sun.xml.ws.api.server.BoundEndpoint;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.api.server.Module;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.jws.security.WssConfiguration;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.jaxws.WLSContainer;
import weblogic.wsee.jaxws.WLSServletAdapter;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.policy.PolicyMap;
import weblogic.wsee.jws.VisitableJWS;
import weblogic.wsee.jws.VisitableJWSBuilder;
import weblogic.wsee.monitoring.WsspStats;
import weblogic.wsee.policy.deployment.PolicyBeanVisitor;
import weblogic.wsee.policy.deployment.PolicyDeployUtils;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.policy.runtime.PolicyFinder;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.ws.WsEndpoint;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;

public class ServerEnvironmentFactory extends EnvironmentFactory {
   private WSEndpoint<?> endpoint;
   private WsdlDefinitions wsdlDef;

   public ServerEnvironmentFactory(WSEndpoint<?> var1) {
      super(var1.getBinding(), var1.getPort(), var1.getContainer());
      this.endpoint = var1;
   }

   protected void initPolicyContext(EnvironmentFactory.SingletonService var1) {
      Container var2 = this.getContainer();
      DeployInfo var3 = var2 != null ? (DeployInfo)var2.getSPI(DeployInfo.class) : null;
      WssPolicyContext var4 = null;
      WsdlDefinitions var5 = null;
      if (var3 != null) {
         var4 = var3.getWssPolicyContext();
         if (var3.getWsdlDef() == null) {
            var5 = this.getWsdlDef();
         } else {
            this.attachAnnotationPolicyForStartFromWSDLEndpoint();
         }

         this.attachPolicyForProviderBasedEndpoint(var3);
      } else {
         var4 = this.buildWssPolicyContextForProviderBasedEndpoint(var2);
         var5 = this.getWsdlDef();
         this.attachAnnotationPolicyForStartFromWSDLEndpoint();
      }

      if (var5 != null) {
         var4.getPolicyServer().addPolicies((new WsdlPolicySubject(var5)).getPolicies());
      }

      var1.setWssPolicyContext(var4);
   }

   private WssPolicyContext buildWssPolicyContextForProviderBasedEndpoint(Container var1) {
      String var2 = "default_wss";
      WssConfiguration var3 = (WssConfiguration)this.endpoint.getImplementationClass().getAnnotation(WssConfiguration.class);
      if (var3 != null) {
         var2 = var3.value();
      }

      WssPolicyContext var4 = new WssPolicyContext(var2);
      PolicyServer var5 = var4.getPolicyServer();

      try {
         PolicyFinder.loadPolicies(Thread.currentThread().getContextClassLoader().getResource("policies"), var5);
         ServletContext var6 = (ServletContext)var1.getSPI(ServletContext.class);
         if (var6 != null) {
            URL var7 = var6.getResource("/WEB-INF/policies");
            PolicyFinder.loadPolicies(var7, var5);
         }

         return var4;
      } catch (Exception var8) {
         throw new WebServiceException(var8);
      }
   }

   private void attachAnnotationPolicyForStartFromWSDLEndpoint() {
      PolicyMap var1 = new PolicyMap(this.endpoint.getContainer(), this.endpoint.getSEIModel(), this.endpoint.getBinding(), this.endpoint.getPortName(), this.endpoint.getImplementationClass());
      var1.updateWSDLModel(this.endpoint.getPort());
   }

   public WSEndpoint<?> getEndpoint() {
      return this.endpoint;
   }

   public String[] getServiceUris() {
      String[] var1 = super.getServiceUris();
      if (var1 != null) {
         return var1;
      } else {
         Container var2 = this.getContainer();
         return var2 instanceof WLSContainer ? new String[]{((WLSServletAdapter)((WLSContainer)var2).getBoundEndpoint(this.endpoint)).getServiceUri()} : null;
      }
   }

   public String getContextPath() {
      String var1 = super.getContextPath();
      if (var1 != null) {
         return var1;
      } else {
         ServletContext var2 = (ServletContext)this.getContainer().getSPI(ServletContext.class);
         return var2 != null ? var2.getContextPath() : null;
      }
   }

   public String getServiceName() {
      String var1 = super.getServiceName();
      return var1 != null ? var1 : this.endpoint.getServiceName().getLocalPart();
   }

   private void attachPolicyForProviderBasedEndpoint(DeployInfo var1) {
      if (var1 == null && this.wsdlDef != null) {
         PolicyBeanVisitor var2 = new PolicyBeanVisitor();
         VisitableJWS var3 = VisitableJWSBuilder.jaxws().impl(this.endpoint.getImplementationClass()).portName(this.endpoint.getPortName()).build();
         var3.accept(var2);
         WebservicePolicyRefBean var4 = var2.getPolicyRefBean();
         WsdlPort var5 = (WsdlPort)this.wsdlDef.getPorts().get(this.endpoint.getPortName());
         if (var5 == null) {
            throw new WebServiceException("Error encountered while deploying WebService  '" + this.endpoint.getImplementationClass().getName() + "'. " + "'  - wsdl port:" + this.endpoint.getPortName() + " is not found in wsdl.");
         }

         if (var4 != null) {
            PolicyDeployUtils.attachPolicy(var4, this.wsdlDef, var5, (String)null);
         }
      }

   }

   protected String getCurrentVersionId() {
      return ApplicationVersionUtils.getCurrentVersionId();
   }

   protected EnvironmentFactory.SimulatedWsMethod createSimulatedWsMethod(WsdlOperation var1) {
      return new ServerSimulatedWsMethod(var1);
   }

   protected EnvironmentFactory.SimulatedWsEndpoint createSimulatedWsEndpoint(Map<String, WsMethod> var1, QName var2) {
      return new ServerSimulatedWsEndpoint(var1, var2);
   }

   protected EnvironmentFactory.SimulatedWsPort createSimulatedWsPort(WsEndpoint var1, QName var2) {
      return new ServerSimulatedWsPort(var1, var2);
   }

   protected class ServerSimulatedWsPort extends EnvironmentFactory.SimulatedWsPort {
      private WsspStats stats;

      protected ServerSimulatedWsPort(WsEndpoint var2, QName var3) {
         super(var2, var3);
      }

      public RuntimeMBean getRuntimeMBean() {
         Container var1 = ServerEnvironmentFactory.this.getContainer();
         if (var1 != null) {
            Module var2 = (Module)var1.getSPI(Module.class);
            if (var2 != null) {
               List var3 = var2.getBoundEndpoints();
               Iterator var4 = var3.iterator();

               while(var4.hasNext()) {
                  BoundEndpoint var5 = (BoundEndpoint)var4.next();
                  if (ServerEnvironmentFactory.this.endpoint == var5.getEndpoint()) {
                     return (RuntimeMBean)var5.getSPI(WseeV2RuntimeMBean.class);
                  }
               }
            }
         }

         return null;
      }

      public WsspStats getWsspStats() {
         return this.stats;
      }

      public void setWsspStats(WsspStats var1) {
         this.stats = var1;
      }
   }

   protected class ServerSimulatedWsEndpoint extends EnvironmentFactory.SimulatedWsEndpoint {
      protected ServerSimulatedWsEndpoint(Map<String, WsMethod> var2, QName var3) {
         super(var2, var3);
      }

      public Class getJwsClass() {
         return ServerEnvironmentFactory.this.endpoint.getImplementationClass();
      }
   }

   protected class ServerSimulatedWsMethod extends EnvironmentFactory.SimulatedWsMethod {
      public ServerSimulatedWsMethod(WsdlOperation var2) {
         super(var2);
      }

      public String getMethodName() {
         return ServerEnvironmentFactory.this.endpoint.getSEIModel().getJavaMethod(this.getOperationName()).getMethod().getName();
      }
   }
}
