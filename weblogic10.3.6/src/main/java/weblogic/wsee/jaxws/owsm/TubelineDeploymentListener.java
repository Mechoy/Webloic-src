package weblogic.wsee.jaxws.owsm;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.server.ContainerResolver;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.util.ServiceFinder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.ws.WebServiceException;
import weblogic.application.ApplicationContextInternal;
import weblogic.j2ee.ComponentRuntimeMBeanImpl;
import weblogic.kernel.KernelStatus;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.deploy.EJBDeployInfo;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.jaxws.framework.jaxrpc.ListenerUsage;
import weblogic.wsee.jaxws.framework.policy.EnvironmentMetadataFactory;
import weblogic.wsee.jaxws.framework.policy.PolicySubjectMetadata;
import weblogic.wsee.jaxws.framework.policy.PolicySubjectMetadataImpl;
import weblogic.wsee.jaxws.tubeline.standard.ClientContainerUtil;
import weblogic.wsee.jws.jaxws.owsm.PolicySubjectBindingFeature;
import weblogic.wsee.jws.jaxws.owsm.SecurityPoliciesFeature;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicyFeature;
import weblogic.wsee.policy.deployment.WseePolicyReferenceInfo;
import weblogic.wsee.runtime.owsm.PolicySubjectUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public abstract class TubelineDeploymentListener extends weblogic.wsee.jaxws.framework.jaxrpc.TubelineDeploymentListener implements WsDeploymentListener {
   protected static String HANDLER_NAME = "OWSM_SECURITY_POLICY_HANDLER";
   protected Map config = new HashMap();

   public TubelineDeploymentListener(Class var1, ListenerUsage var2) {
      super(var1, var2);
   }

   protected HandlerInfo getHandlerInfo(Class var1) {
      return new HandlerInfo(var1, this.config, (QName[])null);
   }

   protected List<String> getSecurityPolicyFeatureURIs(EnvironmentFactory var1) {
      WSBinding var2 = var1.getBinding();
      ArrayList var3 = new ArrayList();
      SecurityPoliciesFeature var4 = (SecurityPoliciesFeature)var2.getFeature(SecurityPoliciesFeature.class);
      if (var4 != null && var4.isEnabled()) {
         Iterator var5 = var4.getPolicies().iterator();

         while(var5.hasNext()) {
            SecurityPolicyFeature var6 = (SecurityPolicyFeature)var5.next();
            var3.add(var6.getUri());
         }
      }

      SecurityPolicyFeature var7 = (SecurityPolicyFeature)var2.getFeature(SecurityPolicyFeature.class);
      if (var7 != null && var7.isEnabled()) {
         var3.add(var7.getUri());
      }

      return var3;
   }

   abstract Class getHandlerClass();

   abstract List getPrecedingHandlers();

   abstract List getFollowingHandlers();

   abstract HandlerInfo getXopHandlerInfo();

   abstract List getPrecedingXopHandlers();

   abstract List getFollowingXopHandlers();

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      WsService var2 = var1.getWsService();
      Iterator var3 = var2.getPorts();

      while(var3.hasNext()) {
         WsPort var4 = (WsPort)var3.next();
         HandlerList var5 = var4.getInternalHandlerList();
         if (var5.contains(HANDLER_NAME)) {
            return;
         }

         try {
            assert var1 instanceof DeploymentContext;

            DeploymentContext var6 = (DeploymentContext)var1;
            EnvironmentFactory var7 = var6.getEnvFactory();
            HandlerInfo var8 = this.getHandlerInfo(this.getHandlerClass());
            this.setProperties(var7, var8.getHandlerConfig());
            this.removeHandlers(var5);
            var5.lenientInsert(HANDLER_NAME, var8, this.getFollowingHandlers(), this.getPrecedingHandlers());
            var5.insert("NORMAL_XOP_HANDLER", this.getXopHandlerInfo(), this.getFollowingXopHandlers(), this.getPrecedingXopHandlers());
         } catch (HandlerException var9) {
            throw new WsDeploymentException(var9);
         }
      }

   }

   private void removeHandlers(HandlerList var1) {
      var1.remove("WS_SECURITY_1.1");
      var1.remove("PRE_WS_SECURITY_POLICY_1.2");
      var1.remove("POST_WS_SECURITY_POLICY_1.2");
      var1.remove("NORMAL_XOP_HANDLER");
   }

   protected SecurityPolicyHandler loadHandler(ClassLoader var1) {
      Iterator var2 = ServiceFinder.find(SecurityPolicyHandler.class, var1).iterator();
      if (!var2.hasNext()) {
         throw new WebServiceException("No Handler for OWSM Security Policy found.");
      } else {
         SecurityPolicyHandler var3 = (SecurityPolicyHandler)var2.next();
         if (var2.hasNext()) {
            throw new WebServiceException("More than one Handler for OWSM Security Policy found.");
         } else {
            return var3;
         }
      }
   }

   protected abstract void setProperties(EnvironmentFactory var1, Map var2);

   protected EnvironmentFactory getEnvironmentFactory(ClientTubeAssemblerContext var1) {
      ClientEnvironmentFactory var2 = new ClientEnvironmentFactory(var1);
      WSBinding var3 = var1.getBinding();
      JAXRPCEnvironmentFeature var4 = (JAXRPCEnvironmentFeature)var3.getFeature(JAXRPCEnvironmentFeature.class);
      var4.setFactory(var2);
      return var2;
   }

   protected EnvironmentFactory getEnvironmentFactory(ServerTubeAssemblerContext var1) {
      ServerEnvironmentFactory var2 = new ServerEnvironmentFactory(var1.getEndpoint());
      return var2;
   }

   protected void setAdditionalOwsmProperties(EnvironmentFactory var1, Map var2, List<String> var3, boolean var4) {
      PolicySubjectMetadata var5 = this.initializePolicySubjectMetadata(var1, var4);
      PolicySubjectBindingFeature var6 = PolicySubjectBindingFeature.create(var3, var5);
      WSBinding var7 = var1.getBinding();
      ((WebServiceFeatureList)var7.getFeatures()).add(var6);
      var2.put("weblogic.wsee.jaxws.framework.policy.PolicySubjectBinding", var6.getPolicySubjectBinding());
      if (!var4) {
         var2.put("weblogic.wsee.jaxws.framework.policy.EnvironmentMetadata", EnvironmentMetadataFactory.getEnvironmentMetadata());
      }

   }

   protected void setAdditionalOwsmPropertiesWithOverrides(EnvironmentFactory var1, Map var2, List<WseePolicyReferenceInfo> var3, boolean var4) {
      PolicySubjectMetadata var5 = this.initializePolicySubjectMetadata(var1, var4);
      PolicySubjectBindingFeature var6 = PolicySubjectBindingFeature.createFeature(var3, var5);
      WSBinding var7 = var1.getBinding();
      ((WebServiceFeatureList)var7.getFeatures()).add(var6);
      var2.put("weblogic.wsee.jaxws.framework.policy.PolicySubjectBinding", var6.getPolicySubjectBinding());
      if (!var4) {
         var2.put("weblogic.wsee.jaxws.framework.policy.EnvironmentMetadata", EnvironmentMetadataFactory.getEnvironmentMetadata());
      }

   }

   protected PolicySubjectMetadata initializePolicySubjectMetadata(EnvironmentFactory var1, boolean var2) {
      ComponentRuntimeMBeanImpl var3 = ClientContainerUtil.getContainingComponentRuntimeByModuleName(ContainerResolver.getInstance().getContainer().getSPI(DeployInfo.class) == null ? null : ((DeployInfo)ContainerResolver.getInstance().getContainer().getSPI(DeployInfo.class)).getModuleName());
      DeployInfo var4 = null;
      ServletContext var5 = null;
      if (var1.getContainer() != null) {
         var4 = (DeployInfo)var1.getContainer().getSPI(DeployInfo.class);
         var5 = (ServletContext)var1.getContainer().getSPI(ServletContext.class);
      }

      String var6 = this.getApplicationName(var4, var5);
      String var7 = this.getModuleName(var4, var5, var3);
      PolicySubjectMetadata.ModuleType var8 = this.getModuleType(var4, var3);
      QName var9 = null;
      if (var1.getPort().getOwner() != null) {
         var9 = var1.getPort().getOwner().getName();
      }

      if (var9 == null && var1.getService().getWsdlService() != null) {
         var9 = var1.getService().getWsdlService().getName();
      }

      QName var10 = var1.getPort().getName();
      String var11 = null;
      if (var2) {
         var11 = PolicySubjectUtil.formatReferencedPortResourcePattern(var10.getLocalPart(), var9.getLocalPart());
      } else {
         var11 = PolicySubjectUtil.formatEndpointPortResourcePattern(var10.getLocalPart(), var9.getLocalPart(), var7);
      }

      return new PolicySubjectMetadataImpl(var6, var7, var8, var10, var9.getLocalPart(), var2 ? PolicySubjectMetadata.Type.REFERENCE : PolicySubjectMetadata.Type.SERVICE, var11);
   }

   private String getApplicationName(DeployInfo var1, ServletContext var2) {
      String var3 = null;
      if (var1 != null) {
         var3 = var1.getApplication();
      } else if (var2 instanceof WebAppServletContext) {
         try {
            ApplicationContextInternal var4 = ((WebAppServletContext)var2).getApplicationContext();
            var3 = var4.getApplicationId();
         } catch (Exception var5) {
         }
      }

      if (StringUtil.isEmpty(var3) && ClientContainerUtil.getContainingApplicationRuntime() != null) {
         var3 = ClientContainerUtil.getContainingApplicationRuntime().getApplicationName();
      }

      return var3;
   }

   private String getModuleName(DeployInfo var1, ServletContext var2, ComponentRuntimeMBean var3) {
      String var4 = null;
      if (var1 != null) {
         var4 = var1.getModuleName();
      } else if (var2 instanceof WebAppServletContext) {
         var4 = ((WebAppServletContext)var2).getName();
      }

      if (StringUtil.isEmpty(var4)) {
         var4 = var3 instanceof WebAppComponentRuntimeMBean ? ((WebAppComponentRuntimeMBean)var3).getModuleURI() : ClientContainerUtil.getContainingModuleName();
      }

      if (!StringUtil.isEmpty(var4) && var4.startsWith("/")) {
         var4 = var4.substring(1);
      }

      return var4;
   }

   private PolicySubjectMetadata.ModuleType getModuleType(DeployInfo var1, ComponentRuntimeMBean var2) {
      PolicySubjectMetadata.ModuleType var3 = KernelStatus.isServer() ? PolicySubjectMetadata.ModuleType.WEB : PolicySubjectMetadata.ModuleType.JSE;
      if (var1 instanceof EJBDeployInfo) {
         var3 = PolicySubjectMetadata.ModuleType.EJB;
      }

      if (var2 instanceof WebAppComponentRuntimeMBean) {
         var3 = PolicySubjectMetadata.ModuleType.WEB;
      } else if (var2 instanceof EJBComponentRuntimeMBean) {
         var3 = PolicySubjectMetadata.ModuleType.EJB;
      }

      return var3;
   }
}
