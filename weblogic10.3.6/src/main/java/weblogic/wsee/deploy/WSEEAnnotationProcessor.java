package weblogic.wsee.deploy;

import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.ws.WebServiceException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.j2ee.descriptor.ServiceImplBeanBean;
import weblogic.j2ee.descriptor.WebserviceDescriptionBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.WSATConfigBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.jws.Policies;
import weblogic.jws.Policy;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.jws.WebServiceRuntimeDecl;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.util.ClassLoaderUtil;
import weblogic.wsee.util.ClassUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wstx.wsat.config.DDHelper;

public class WSEEAnnotationProcessor {
   private static boolean verbose = Verbose.isVerbose(WSEEAnnotationProcessor.class);
   private Map<String, PortComponentBean> portMap = new HashMap();
   private Map<String, weblogic.j2ee.descriptor.wl.PortComponentBean> wlJwsPortMapForJAXWS = new HashMap();
   private Map<String, WebserviceDescriptionBean> serviceMapForJAXWS = new HashMap();
   private Map<String, String> serviceLinkMap = new HashMap();
   private Map<String, weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean> wlServiceMapForJAXWS = new HashMap();
   private Set<String> jaxRpcPorts = new HashSet();

   public WebservicesBean process(WebservicesBean var1, WeblogicWebservicesBean var2, WSEEBaseModule var3) throws WsException {
      return this.process(var1, var2, var3, (ClassLoader)null);
   }

   public WebservicesBean process(WebservicesBean var1, WeblogicWebservicesBean var2, WSEEBaseModule var3, ClassLoader var4) throws WsException {
      WebservicesBean var5 = var1;
      this.loadWLServiceAndPorts(var2);
      this.load(var1, var3);
      ClassLoader var6 = Thread.currentThread().getContextClassLoader();
      Map var7 = var3.getLinkMap();
      Iterator var8 = var7.keySet().iterator();

      while(true) {
         String var9;
         Class var10;
         do {
            if (!var8.hasNext()) {
               if (var5 != null) {
                  this.checkWsdlPortNames(var5);
               }

               return var5;
            }

            var9 = (String)var8.next();
            var10 = (Class)var7.get(var9);
         } while(!ClassUtil.isWebService(var10));

         var5 = this.getWebServicesBean(var5);
         var2 = getWeblogicWebServicesBean(var2);

         try {
            if (var4 != null) {
               ClassLoaderUtil.DelegatingLoader var11 = new ClassLoaderUtil.DelegatingLoader(var10.getClassLoader(), var4);
               if (var6 != null) {
                  var11 = new ClassLoaderUtil.DelegatingLoader(var11, var6);
               }

               Thread.currentThread().setContextClassLoader(var11);
            }

            WebServiceRuntimeDecl var24 = new WebServiceRuntimeDecl(WebServiceType.JAXWS, var10);
            PortComponentBean var12 = (PortComponentBean)this.portMap.get(var9);
            if (var12 == null) {
               WebserviceDescriptionBean var13 = this.createWebserviceDescriptionBeanForJAXWS(var5, var24);
               var12 = var13.createPortComponent();
               this.portMap.put(var9, var12);
               this.serviceLinkMap.put(var9, var13.getWebserviceDescriptionName());
               if (!StringUtil.isEmpty(var13.getWsdlFile()) && this.isWsdlHasPolicy(var3, var13.getWsdlFile())) {
                  JClass var14 = var24.getJClass();
                  if (var14 != null && (this.isAnnotationPresent(var14, Policy.class) || this.isAnnotationPresent(var14, Policies.class))) {
                     throw new WsException("The Policy and Policies annotations are not allowed on JWS '" + var14.getQualifiedName() + "' when a wsdl which contains WS-Policy is specified by webservices.xml or WebService annotation. Please remove the annotations and use the policy reference descriptor file to attach policy to endpoint");
                  }

                  var14 = var24.getEIClass();
                  if (var14 != null && (this.isAnnotationPresent(var14, Policy.class) || this.isAnnotationPresent(var14, Policies.class))) {
                     throw new WsException("The Policy and Policies annotations are not allowed on endpoint interface '" + var14.getQualifiedName() + "' when a wsdl which contains WS-Policy is specified by webservices.xml or WebService annotation. Please remove the annotations and use the policy reference descriptor file to attach policy to endpoint");
                  }
               }
            }

            String var25 = (String)this.serviceLinkMap.get(var9);
            if (!this.jaxRpcPorts.contains(var12.getPortComponentName())) {
               this.merge(var3, var12, var24, var9);
               weblogic.j2ee.descriptor.wl.PortComponentBean var26 = this.createWLComponentBeanForJAXWS(var24, var2, var12, var25);
               WSATConfigBean var15 = var26.getWSATConfig();
               String var16 = var24.getEndPointInterface();
               Class var17 = StringUtil.isEmpty(var16) ? null : ClassUtil.loadClass(var16);
               boolean var18 = var3 instanceof WSEEModule;
               DDHelper.populateServiceDDFromJWS(var17, var10, var26, var18);
            }
         } catch (ClassNotFoundException var22) {
            throw new WebServiceException(var22);
         } finally {
            if (var4 != null) {
               Thread.currentThread().setContextClassLoader(var6);
            }

         }
      }
   }

   private void checkWsdlPortNames(WebservicesBean var1) throws WsException {
      WebserviceDescriptionBean[] var2 = var1.getWebserviceDescriptions();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WebserviceDescriptionBean var5 = var2[var4];
         HashSet var6 = new HashSet(2);
         PortComponentBean[] var7 = var5.getPortComponents();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            PortComponentBean var10 = var7[var9];
            if (!var6.add(var10.getWsdlPort())) {
               throw new WsException("Two port in the " + var5.getWebserviceDescriptionName() + "(WebserviceDescriptionBean) has the same name " + var10.getWsdlPort().toString() + ". ");
            }
         }
      }

   }

   private weblogic.j2ee.descriptor.wl.PortComponentBean createWLComponentBeanForJAXWS(WebServiceRuntimeDecl var1, WeblogicWebservicesBean var2, PortComponentBean var3, String var4) {
      weblogic.j2ee.descriptor.wl.PortComponentBean var5 = (weblogic.j2ee.descriptor.wl.PortComponentBean)this.wlJwsPortMapForJAXWS.get(var3.getPortComponentName());
      if (var5 == null) {
         weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean var6 = this.createWebserviceDescriptionBeanForJAXWS(var2, var4);
         var5 = var6.createPortComponent();
         var5.setPortComponentName(var3.getPortComponentName());
         this.wlJwsPortMapForJAXWS.put(var5.getPortComponentName(), var5);
      }

      return var5;
   }

   WebserviceDescriptionBean createWebserviceDescriptionBeanForJAXWS(WebservicesBean var1, WebServiceRuntimeDecl var2) {
      WebserviceDescriptionBean var3 = (WebserviceDescriptionBean)this.serviceMapForJAXWS.get(var2.getServiceName());
      if (var3 == null) {
         var3 = var1.createWebserviceDescription();
         if (!this.isSet("WebserviceDescriptionName", var3)) {
            var3.setWebserviceDescriptionName(var2.getServiceName());
         }

         if (!this.isSet("WsdlFile", var3) && !StringUtil.isEmpty(var2.getWsdlLocation())) {
            var3.setWsdlFile(var2.getWsdlLocation());
         }

         this.serviceMapForJAXWS.put(var2.getServiceName(), var3);
      }

      return var3;
   }

   weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean createWebserviceDescriptionBeanForJAXWS(WeblogicWebservicesBean var1, String var2) {
      weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean var3 = (weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean)this.wlServiceMapForJAXWS.get(var2);
      if (var3 == null) {
         var3 = var1.createWebserviceDescription();
         if (!this.isSet("WebserviceDescriptionName", var3)) {
            var3.setWebserviceDescriptionName(var2);
         }

         var3.setWebserviceType("JAXWS");
         this.wlServiceMapForJAXWS.put(var2, var3);
      }

      return var3;
   }

   private void loadWLServiceAndPorts(WeblogicWebservicesBean var1) {
      if (var1 != null) {
         weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean[] var2 = var1.getWebserviceDescriptions();
         if (var2 != null) {
            weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean var6 = var3[var5];
               String var7 = var6.getWebserviceType();
               weblogic.j2ee.descriptor.wl.PortComponentBean[] var8;
               weblogic.j2ee.descriptor.wl.PortComponentBean[] var9;
               int var10;
               int var11;
               weblogic.j2ee.descriptor.wl.PortComponentBean var12;
               if (!StringUtil.isEmpty(var7) && WebServiceType.valueOf(var7) != WebServiceType.JAXRPC) {
                  this.wlServiceMapForJAXWS.put(var6.getWebserviceDescriptionName(), var6);
                  var8 = var6.getPortComponents();
                  if (var8 != null) {
                     var9 = var8;
                     var10 = var8.length;

                     for(var11 = 0; var11 < var10; ++var11) {
                        var12 = var9[var11];
                        this.wlJwsPortMapForJAXWS.put(var12.getPortComponentName(), var12);
                     }
                  }
               } else {
                  var8 = var6.getPortComponents();
                  if (var8 != null) {
                     var9 = var8;
                     var10 = var8.length;

                     for(var11 = 0; var11 < var10; ++var11) {
                        var12 = var9[var11];
                        this.jaxRpcPorts.add(var12.getPortComponentName());
                     }
                  }
               }
            }
         }
      }

   }

   private void merge(WSEEBaseModule var1, PortComponentBean var2, WebServiceRuntimeDecl var3, String var4) {
      if (!this.isSet("PortComponentName", var2)) {
         var2.setPortComponentName(var3.getPortComponentName());
      }

      if (!this.isSet("WsdlService", var2)) {
         var2.setWsdlService(var3.getServiceQName());
      }

      if (!this.isSet("WsdlPort", var2)) {
         var2.setWsdlPort(var3.getPortQName());
      }

      if (!this.isSet("ProtocolBinding", var2)) {
         var2.setProtocolBinding(var3.getProtocolBinding());
      }

      if (!this.isSet("EnableMtom", var2)) {
         var2.setEnableMtom(var3.isMtomEnabled());
      }

      ServiceImplBeanBean var5 = var2.getServiceImplBean();
      if (var5 == null) {
         var5 = var2.createServiceImplBean();
      }

      var1.setLinkName(var5, var4);
      var3.getHandlerChainDecl().populatePort(var2, var1.getEnvEntries(var5));
   }

   protected boolean isSet(String var1, Object var2) {
      return ((DescriptorBean)var2).isSet(var1);
   }

   private WebservicesBean getWebServicesBean(WebservicesBean var1) {
      WebservicesBean var2 = var1;
      if (var1 == null) {
         if (verbose) {
            Verbose.log((Object)"Creating web services bean.");
         }

         EditableDescriptorManager var3 = new EditableDescriptorManager();
         var2 = (WebservicesBean)var3.createDescriptorRoot(WebservicesBean.class).getRootBean();
         var2.setVersion("1.2");
      }

      return var2;
   }

   private static WeblogicWebservicesBean getWeblogicWebServicesBean(WeblogicWebservicesBean var0) {
      if (var0 == null) {
         if (verbose) {
            Verbose.log((Object)"Creating weblogic web services bean.");
         }

         EditableDescriptorManager var1 = new EditableDescriptorManager();
         var0 = (WeblogicWebservicesBean)var1.createDescriptorRoot(WeblogicWebservicesBean.class).getRootBean();
         var0.setVersion("1.2");
      }

      return var0;
   }

   private void load(WebservicesBean var1, WSEEBaseModule var2) {
      if (var1 != null) {
         WebserviceDescriptionBean[] var3 = var1.getWebserviceDescriptions();
         if (var3 != null) {
            WebserviceDescriptionBean[] var4 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               WebserviceDescriptionBean var7 = var4[var6];
               String var8 = var7.getWebserviceDescriptionName();
               if (!StringUtil.isEmpty(var8) && this.wlServiceMapForJAXWS.get(var8) != null) {
                  this.serviceMapForJAXWS.put(var7.getWebserviceDescriptionName(), var7);
               }

               this.loadPorts(var7, var2);
            }
         }
      }

   }

   private void loadPorts(WebserviceDescriptionBean var1, WSEEBaseModule var2) {
      PortComponentBean[] var3 = var1.getPortComponents();
      if (var3 != null) {
         Map var4 = null;
         PortComponentBean[] var5 = var3;
         int var6 = var3.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            PortComponentBean var8 = var5[var7];
            ServiceImplBeanBean var9 = var8.getServiceImplBean();
            if (var9 != null) {
               String var10 = var2.getLinkName(var9);
               if (var4 == null) {
                  var4 = var2.getLinkMap();
               }

               Class var11 = (Class)var4.get(var10);
               if (var11 == null) {
                  throw new IllegalArgumentException("Class not found for link: " + var10 + ":\t avaialbe:" + var4);
               }

               this.portMap.put(var10, var8);
               this.serviceLinkMap.put(var10, var1.getWebserviceDescriptionName());
            }
         }
      }

   }

   private boolean isAnnotationPresent(JClass var1, Class var2) {
      if (var1.getAnnotation(var2) != null) {
         return true;
      } else {
         JMethod[] var3 = var1.getMethods();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].getAnnotation(var2) != null) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean isWsdlHasPolicy(WSEEBaseModule var1, String var2) throws WsException {
      boolean var3 = false;
      if (var1 instanceof WSEEModule) {
         WSEEModule var4 = (WSEEModule)var1;
         WsdlDefinitions var5 = var4.loadWsdl(var2);
         if (var5 != null) {
            var3 = (new WsdlPolicySubject(var5)).getPolicies().size() != 0;
         }
      }

      return var3;
   }
}
