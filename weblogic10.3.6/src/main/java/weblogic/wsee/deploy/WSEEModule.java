package weblogic.wsee.deploy;

import com.sun.xml.ws.transport.http.servlet.ServletAdapterList;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationContext;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.j2ee.descriptor.ServiceImplBeanBean;
import weblogic.j2ee.descriptor.WebserviceDescriptionBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.OperationPolicyBean;
import weblogic.j2ee.descriptor.wl.OwsmSecurityPolicyBean;
import weblogic.j2ee.descriptor.wl.PortPolicyBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.j2ee.descriptor.wl.WebserviceSecurityBean;
import weblogic.j2ee.descriptor.wl.WsPolicyBean;
import weblogic.jws.security.WssConfiguration;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.jws.VisitableJWS;
import weblogic.wsee.jws.VisitableJWSBuilder;
import weblogic.wsee.policy.deployment.PolicyBeanVisitor;
import weblogic.wsee.policy.deployment.PolicyDeployUtils;
import weblogic.wsee.policy.deployment.WsPolicyDescriptor;
import weblogic.wsee.policy.runtime.PolicyFinder;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.util.ClassUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlPort;

abstract class WSEEModule extends WSEEBaseModule {
   static boolean verbose = Verbose.isVerbose(WSEEModule.class);
   Map<String, DeployInfo> deployInfoMap = new HashMap();
   Map serviceAndPorts = new HashMap();
   private Map loadedWsdl = new HashMap();
   ApplicationContext appCtx = null;
   private Map<String, Class> linkMap = new HashMap();
   private Map<String, String> endpointInterfaceMap = new HashMap();
   private WsdlFilePublishHelper filePublish;

   abstract DeployInfo createDeployInfo();

   abstract String getModuleName();

   abstract WSEEDescriptor loadDescriptor(File var1, DeploymentPlanBean var2) throws IOException, XMLStreamException;

   abstract WsPolicyDescriptor loadWsPolicyDescriptor(File var1, DeploymentPlanBean var2) throws IOException, XMLStreamException;

   abstract void registerEndpoint(WebservicesBean var1) throws Exception;

   void wrapContextListeners() {
   }

   protected WSEEModule(ApplicationContext var1) {
      this.appCtx = var1;
   }

   void prepare() throws DeploymentException {
      try {
         this.linkMap = this.getLinkMap();
         File var1 = null;
         DeploymentPlanBean var2 = null;
         if (this.appCtx != null) {
            AppDeploymentMBean var3 = this.appCtx.getAppDeploymentMBean();
            var2 = var3.getDeploymentPlanDescriptor();
            if (var3.getPlanDir() != null) {
               var1 = new File(var3.getLocalPlanDir());
            }
         }

         String var13 = this.getModuleName();
         WSEEDescriptor var4 = this.loadDescriptor(var1, var2);
         WebservicesBean var5 = var4.getWebservicesBean();
         WeblogicWebservicesBean var6 = var4.getWeblogicWebservicesBean();
         if (var6 == null) {
            EditableDescriptorManager var7 = new EditableDescriptorManager();
            var6 = (WeblogicWebservicesBean)var7.createDescriptorRoot(WeblogicWebservicesBean.class).getRootBean();
            var6.setVersion("1.2");
         }

         WSEEAnnotationProcessor var14 = new WSEEAnnotationProcessor();
         var5 = var14.process(var5, var6, this);
         if (!this.noWebService(var5)) {
            if (verbose) {
               Verbose.banner("Starting Deployment for " + this.getModuleName());
            }

            WsPolicyDescriptor var8 = this.loadWsPolicyDescriptor(var1, var2);
            WebservicePolicyRefBean var9 = var8.getWebservicesPolicyBean();
            this.verifyWsdd(var5, var9);
            if (verbose) {
               Verbose.log((Object)("Web Services Version: " + var5.getVersion()));
               Verbose.log((Object)(var13 + "#weblogic-webservices.xml bean is " + var6));
            }

            if (var6 != null) {
               this.verifyWlwsdd(var6);
            }

            this.verifyOWSMPolicyWithJAXRPC(var9, var5);
            this.wrapContextListeners();
            if (var5 != null) {
               this.registerEndpoint(var5);
            }

            if (var6 != null && var5 != null) {
               this.publishWsdlFile(var5, var6);
            }

            String var10 = this.updateWssConfigInfo(var6);
            this.deployRolesAndPolicy();
            this.loadPolicies(var10);
         }
      } catch (WsException var11) {
         throw new DeploymentException("Error encountered during prepare phase of deploying WebService module '" + this.getModuleName() + "'. " + var11.getMessage(), var11);
      } catch (Exception var12) {
         throw new DeploymentException("Error encountered during prepare phase of deploying WebService module '" + this.getModuleName() + "'. " + var12.getMessage(), var12);
      }
   }

   protected abstract ClassLoader getClassLoader();

   private boolean noWebService(WebservicesBean var1) {
      return var1 == null || var1.getWebserviceDescriptions() == null || var1.getWebserviceDescriptions().length == 0;
   }

   private void verifyWsdd(WebservicesBean var1, WebservicePolicyRefBean var2) throws Exception {
      ServletAdapterList var3 = null;
      WebserviceDescriptionBean[] var4 = var1.getWebserviceDescriptions();
      WebserviceDescriptionBean[] var5 = var4;
      int var6 = var4.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         WebserviceDescriptionBean var8 = var5[var7];
         WsdlDefinitions var9 = this.loadWsdl(var8.getWsdlFile());
         JavaWsdlMappingBean var10 = this.loadMappingFile(var8);
         String var11 = var8.getWebserviceDescriptionName();
         HashMap var12 = new HashMap();
         if (this.serviceAndPorts.containsKey(var11)) {
            throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'.  In webservices.xml, " + "webservice-description-name " + var11 + " is not unique within webservices.");
         }

         this.serviceAndPorts.put(var11, var12);
         PortComponentBean[] var13 = var8.getPortComponents();
         PortComponentBean[] var14 = var13;
         int var15 = var13.length;

         for(int var16 = 0; var16 < var15; ++var16) {
            PortComponentBean var17 = var14[var16];
            String var18 = var17.getPortComponentName();
            if (var12.containsKey(var18)) {
               throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'.  In webservices.xml, port-component-name " + var18 + "is not unique within webservice-description " + var11);
            }

            var12.put(var18, var17);
            String var19 = var17.getServiceEndpointInterface();
            ServiceImplBeanBean var20 = var17.getServiceImplBean();
            if (this.getLinkName(var20) == null) {
               throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'.  Link not found for port:" + var18 + " in webservices.xml.");
            }

            String var21 = this.getLinkName(var20);
            if (this.deployInfoMap.keySet().contains(var21)) {
               if (!var19.equals(this.endpointInterfaceMap.get(var21))) {
                  throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'.  Link: " + var21 + " is linked to multiple port-component.");
               }
            } else {
               QName var22 = var17.getWsdlPort();
               if (verbose) {
                  Verbose.log((Object)("Deploying to " + var22));
               }

               DeployInfo var23 = this.createDeployInfo();
               var23.setServiceName(var22.getLocalPart());
               var23.setWebServiceDescriptionName(var11);
               var23.setMappingdd(var10);
               var23.setPortComp(var17);
               var23.setPolicyRef(var2);
               var23.setWsdlDef(var9);
               var23.setApplicationContext(this.appCtx);
               var23.setJwsClass((Class)this.linkMap.get(var21));
               var23.setLinkName(var21);
               var23.setModuleName(this.getModuleName());
               if (var23.getWebServicesType() == WebServiceType.JAXWS) {
                  if (var3 == null) {
                     var3 = var23.createServletAdapterList();
                  }

                  var23.setAdpaterList(var3);
               }

               WebservicePolicyRefBean var24 = var2;
               if (var10 == null && var9 != null) {
                  boolean var25 = var2 == null || var2.getOperationPolicy().length == 0 && var2.getPortPolicy().length == 0;
                  if (var25) {
                     PolicyBeanVisitor var26 = new PolicyBeanVisitor();
                     Class var27 = var19 == null ? null : ClassUtil.loadClass(var19);
                     VisitableJWS var28 = VisitableJWSBuilder.jaxws().sei(var27).impl(var23.getJwsClass()).portName(var22).build();
                     var28.accept(var26);
                     var24 = var26.getPolicyRefBean();
                  }
               }

               this.verifyWsPolicyRefBean(var24, var23.getWebServicesType());
               if (var9 != null) {
                  WsdlPort var29 = (WsdlPort)var9.getPorts().get(var22);
                  if (var29 == null) {
                     throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'.  port component '" + var17.getPortComponentName() + "'  - wsdl port:" + var22 + " is not found in wsdl.");
                  }

                  if (var24 != null) {
                     PolicyDeployUtils.attachPolicy(var24, var9, var29, var21);
                  }
               }

               this.deployInfoMap.put(var21, var23);
               this.endpointInterfaceMap.put(var21, var19);
            }
         }
      }

   }

   private void verifyWlwsdd(WeblogicWebservicesBean var1) throws WsException {
      weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean[] var2 = var1.getWebserviceDescriptions();
      HashSet var3 = new HashSet();
      weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean var7 = var4[var6];
         weblogic.j2ee.descriptor.wl.PortComponentBean[] var8 = var7.getPortComponents();
         String var9 = var7.getWebserviceDescriptionName();
         if (!var3.add(var9)) {
            throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'.  In weblogic-webservices.xml, webservice-description-name " + var9 + " is not unique within weblogic-webservices");
         }

         Map var10 = (Map)this.serviceAndPorts.get(var9);
         if (var10 == null) {
            throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'.  webservice-description-name " + var9 + " referenced in weblogic-webservices.xml doesn't exist in webservices.xml.");
         }

         HashSet var11 = new HashSet();
         weblogic.j2ee.descriptor.wl.PortComponentBean[] var12 = var8;
         int var13 = var8.length;

         for(int var14 = 0; var14 < var13; ++var14) {
            weblogic.j2ee.descriptor.wl.PortComponentBean var15 = var12[var14];
            String var16 = var15.getPortComponentName();
            if (!var11.add(var16)) {
               throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'. In weblogic-webservices.xml, port-component-name " + var16 + " is not unique within webservice-description " + var9);
            }

            PortComponentBean var17 = (PortComponentBean)var10.get(var16);
            if (var17 == null) {
               throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'. port-component-name " + var16 + " referenced in weblogic-webservices.xml doesn't exist in webservices.xml.");
            }

            String var18 = this.getLinkName(var17.getServiceImplBean());
            DeployInfo var19 = (DeployInfo)this.deployInfoMap.get(var18);

            assert var19 != null;

            var19.setWebServicesType(this.getWebServiceType(var7));
            var19.setWlPortComp(var15);
         }
      }

   }

   private WebServiceType getWebServiceType(weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean var1) {
      String var2 = var1.getWebserviceType();
      return StringUtil.isEmpty(var2) ? null : WebServiceType.valueOf(var2);
   }

   private JavaWsdlMappingBean loadMappingFile(WebserviceDescriptionBean var1) throws WsException {
      String var2 = var1.getJaxrpcMappingFile();
      JavaWsdlMappingBean var3 = null;
      if (!StringUtil.isEmpty(var2)) {
         var3 = this.loadMappingFile(var2);
      }

      return var3;
   }

   WsdlDefinitions loadWsdl(String var1) throws WsException {
      if (var1 == null) {
         return null;
      } else {
         WsdlDefinitions var2 = (WsdlDefinitions)this.loadedWsdl.get(var1);
         if (var2 == null) {
            var2 = this.loadWsdlDefinitions(var1);
            if (var2 == null) {
               return null;
            }

            this.loadedWsdl.put(var1, var2);
         }

         return var2;
      }
   }

   abstract WsdlDefinitions loadWsdlDefinitions(String var1) throws WsException;

   abstract JavaWsdlMappingBean loadMappingFile(String var1) throws WsException;

   private void publishWsdlFile(WebservicesBean var1, WeblogicWebservicesBean var2) throws WsException {
      this.filePublish = new WsdlFilePublishHelper(var2);
      WebserviceDescriptionBean[] var3 = var1.getWebserviceDescriptions();
      WebserviceDescriptionBean[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         WebserviceDescriptionBean var7 = var4[var6];
         PortComponentBean[] var8 = var7.getPortComponents();
         String var9 = this.getLinkName(var8[0].getServiceImplBean());
         DeployInfo var10 = (DeployInfo)this.deployInfoMap.get(var9);
         WsdlDefinitions var11 = var10.getWsdlDef();
         if (var11 != null) {
            try {
               String var12 = var7.getWsdlFile();
               this.filePublish.publish(var7.getWebserviceDescriptionName(), var12, var11);
            } catch (IOException var13) {
               throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'.  Failed to publish wsdl " + var13, var13);
            }
         }
      }

   }

   private String updateWssConfigInfo(WeblogicWebservicesBean var1) {
      WebserviceSecurityBean var2 = var1 != null ? var1.getWebserviceSecurity() : null;
      String var3 = null;
      if (var2 != null) {
         var3 = var2.getMbeanName();
      }

      Iterator var4 = this.deployInfoMap.values().iterator();

      while(var4.hasNext()) {
         DeployInfo var5 = (DeployInfo)var4.next();
         if (var3 == null) {
            Class var6 = var5.getJwsClass();
            if (var6 != null) {
               WssConfiguration var7 = (WssConfiguration)var6.getAnnotation(WssConfiguration.class);
               if (var7 != null) {
                  var3 = var7.value();
               }
            }
         }

         if (var3 != null) {
            var5.setWssConfigMBeanName(var3);
         }
      }

      return var3;
   }

   private void deployRolesAndPolicy() {
      Iterator var1 = this.deployInfoMap.values().iterator();

      while(var1.hasNext()) {
         DeployInfo var2 = (DeployInfo)var1.next();
         SecurityRoleAndPolicyHelper var3 = new SecurityRoleAndPolicyHelper(var2);
         var3.deploySecurityRolesAndPolicies();
      }

   }

   abstract void loadModulePolicies(PolicyServer var1) throws Exception;

   private void loadPolicies(String var1) throws Exception {
      WssPolicyContext var2 = new WssPolicyContext(var1);
      PolicyServer var3 = var2.getPolicyServer();
      PolicyFinder.loadPolicies(Thread.currentThread().getContextClassLoader().getResource("policies"), var3);
      this.loadModulePolicies(var3);
      Iterator var4 = this.deployInfoMap.values().iterator();

      while(var4.hasNext()) {
         DeployInfo var5 = (DeployInfo)var4.next();
         var5.setWssPolicyContext(var2);
      }

   }

   void destroy() throws DeploymentException {
      if (this.filePublish != null) {
         this.filePublish.unpublishAll();
      }

      Iterator var1 = this.deployInfoMap.values().iterator();

      while(var1.hasNext()) {
         DeployInfo var2 = (DeployInfo)var1.next();
         var2.clean();
      }

   }

   void activate() throws DeploymentException {
   }

   void deactivate() throws DeploymentException {
   }

   private void verifyWsPolicyRefBean(WebservicePolicyRefBean var1, WebServiceType var2) throws Exception {
      if (var1 != null) {
         boolean var3 = false;
         boolean var4 = false;
         PortPolicyBean[] var5 = var1.getPortPolicy();
         int var6 = var5.length;

         int var7;
         for(var7 = 0; var7 < var6; ++var7) {
            PortPolicyBean var8 = var5[var7];
            int var10;
            int var11;
            if (var8.getOwsmSecurityPolicy() != null && var8.getOwsmSecurityPolicy().length > 0) {
               OwsmSecurityPolicyBean[] var9 = var8.getOwsmSecurityPolicy();
               var10 = var9.length;

               for(var11 = 0; var11 < var10; ++var11) {
                  OwsmSecurityPolicyBean var12 = var9[var11];
                  if (var12.getStatus().equals("enabled")) {
                     var4 = true;
                     break;
                  }
               }
            }

            if (var8.getWsPolicy() != null && var8.getWsPolicy().length > 0) {
               WsPolicyBean[] var15 = var8.getWsPolicy();
               var10 = var15.length;

               for(var11 = 0; var11 < var10; ++var11) {
                  WsPolicyBean var16 = var15[var11];
                  if (var16.getStatus().equals("enabled")) {
                     var3 = true;
                     break;
                  }
               }
            }
         }

         OperationPolicyBean[] var13 = var1.getOperationPolicy();
         var6 = var13.length;

         for(var7 = 0; var7 < var6; ++var7) {
            OperationPolicyBean var14 = var13[var7];
            if (var14.getWsPolicy() != null && var14.getWsPolicy().length > 0) {
               var3 = true;
            }
         }

         if (WebServiceType.JAXRPC == var2 & var4) {
            throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'.  OWSM Security Policy can not be used to a JAX-RPC WebService Endpoint.");
         } else if (var3 & var4) {
            throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'.  OWSM Security Policy and WebLogic WebServices Policy can not be attached to a WebService Endpoint at the same time.");
         }
      }
   }

   private void verifyOWSMPolicyWithJAXRPC(WebservicePolicyRefBean var1, WebservicesBean var2) throws Exception {
      WebserviceDescriptionBean[] var3 = var2.getWebserviceDescriptions();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         WebserviceDescriptionBean var6 = var3[var5];
         PortComponentBean[] var7 = var6.getPortComponents();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            PortComponentBean var10 = var7[var9];
            PortPolicyBean[] var11 = var1.getPortPolicy();
            int var12 = var11.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               PortPolicyBean var14 = var11[var13];
               if (var14.getPortName() != null && var14.getPortName().equals(var10.getPortComponentName()) && var14.getOwsmSecurityPolicy() != null && var14.getOwsmSecurityPolicy().length > 0 && WebServiceType.JAXRPC == ((DeployInfo)this.deployInfoMap.get(this.getLinkName(var10.getServiceImplBean()))).getWebServicesType()) {
                  throw new WsException("Error encountered while deploying WebService module '" + this.getModuleName() + "'.  " + "OWSM Security Policy can not be used with a JAX-RPC WebService Endpoint.");
               }
            }
         }
      }

   }
}
