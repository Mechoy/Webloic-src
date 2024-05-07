package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.DocumentAddressResolver;
import com.sun.xml.ws.api.server.PortAddressResolver;
import com.sun.xml.ws.api.server.SDDocument;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.transport.http.HttpMetadataPublisher;
import com.sun.xml.ws.transport.http.WSHTTPConnection;
import com.sun.xml.ws.transport.http.servlet.ServletAdapter;
import com.sun.xml.ws.transport.http.servlet.ServletAdapterList;
import com.sun.xml.ws.transport.http.servlet.ServletConnectionImpl;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.Principal;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceException;
import weblogic.application.ApplicationContextInternal;
import weblogic.j2ee.ComponentRuntimeMBeanImpl;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.management.ManagementException;
import weblogic.management.runtime.WseeBaseRuntimeMBean;
import weblogic.management.runtime.WseePortRuntimeMBean;
import weblogic.management.runtime.WseeRuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.security.SubjectUtils;
import weblogic.security.WLSPrincipals;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.RoleManager;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.WebServiceResource;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.deploy.DeployInfoUtil;
import weblogic.wsee.jaxws.framework.policy.advertisementimpl.AdvertisementHelper;
import weblogic.wsee.jaxws.framework.policy.advertisementimpl.AdvertisementHelperFactory;
import weblogic.wsee.jaxws.tubeline.standard.ClientContainerUtil;
import weblogic.wsee.monitoring.WseeRuntimeMBeanManager;

public class WLSServletAdapter extends ServletAdapter {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private WseeBaseRuntimeMBean runtimeMBean;
   private WseePortRuntimeMBean portRuntimeMBean;
   private MonitoringStatMap statMap;

   public WLSServletAdapter(String var1, String var2, WSEndpoint<?> var3, ServletAdapterList var4) {
      super(var1, var2, var3, var4);
      DeployInfo var5 = (DeployInfo)var3.getContainer().getSPI(DeployInfo.class);
      WebAppServletContext var6 = (WebAppServletContext)var3.getContainer().getSPI(ServletContext.class);

      try {
         ApplicationContextInternal var7 = var6.getApplicationContext();
         J2EEApplicationRuntimeMBeanImpl var8 = var7.getRuntime();
         String var9 = var5 == null ? null : ((DeployInfo)var3.getContainer().getSPI(DeployInfo.class)).getModuleName();
         ComponentRuntimeMBeanImpl var10 = ClientContainerUtil.getContainingComponentRuntimeByModuleName(var9);
         if (var8 != null) {
            this.runtimeMBean = WseeRuntimeMBeanManager.createJaxWsMBean(var3, var8, var10, var5 != null ? var5.getApplication() : var7.getApplicationId(), var5 != null ? var5.getWebserviceDescriptionName() : var3.getServiceName().getLocalPart(), var6.getContextPath(), var2, var5 != null ? var5.getWssPolicyContext() : null);
            if (var3 instanceof WLSContainer.WLSEndpointFactory.WLSEndpointImpl) {
               ((WLSContainer.WLSEndpointFactory.WLSEndpointImpl)var3).setWseeRuntimeMBean(this.runtimeMBean);
            }

            WseePortRuntimeMBean[] var11 = this.runtimeMBean.getPorts();
            int var12 = var11.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               WseePortRuntimeMBean var14 = var11[var13];
               if (var14.getPortName().equals(var3.getPortName().getLocalPart())) {
                  this.portRuntimeMBean = var14;
               }
            }

            this.statMap = new MonitoringStatMap(this.runtimeMBean, var3);
         }
      } catch (ManagementException var15) {
         throw new WebServiceException(var15);
      }

      AdvertisementHelper var16 = AdvertisementHelperFactory.getAdvertisementHelper();
      if (var16 != null && this.endpoint.getServiceDefinition() != null && var16.hasPolicyAdvertisementFilter()) {
         this.wsdls.put("orawsdl", this.wsdls.get("wsdl"));
         this.wsdls.put("ORAWSDL", this.wsdls.get("WSDL"));
      }

   }

   public DocumentAddressResolver getDocumentAddressResolver(PortAddressResolver var1) {
      if (var1 instanceof WLSServletAdapterList.WLSPortAddressResolver) {
         var1 = ((WLSServletAdapterList.WLSPortAddressResolver)var1).getDocumentPortAddressResolver();
      }

      return super.getDocumentAddressResolver(var1);
   }

   protected WSHTTPConnection createConnection(ServletContext var1, HttpServletRequest var2, HttpServletResponse var3) {
      return new WLSServletConnection(this, var1, var2, var3);
   }

   public String getServiceUri() {
      return this.urlPattern;
   }

   public <T> T getSPI(Class<T> var1) {
      if (var1 == MonitoringStatMap.class) {
         return var1.cast(this.statMap);
      } else if (var1 == WseePortRuntimeMBean.class) {
         return var1.cast(this.portRuntimeMBean);
      } else if (var1 == WseeRuntimeMBean.class) {
         return var1.cast(this.runtimeMBean);
      } else {
         return var1 == WseeV2RuntimeMBean.class ? var1.cast(this.runtimeMBean) : null;
      }
   }

   public void handle(ServletContext var1, HttpServletRequest var2, HttpServletResponse var3) throws IOException {
      if (var2.getMethod().equals("GET") || var2.getMethod().equals("HEAD")) {
         HttpMetadataPublisher var4 = (HttpMetadataPublisher)this.endpoint.getSPI(HttpMetadataPublisher.class);
         if (var4 != null && var4.handleMetadataRequest(this, this.createConnection(var1, var2, var3))) {
            return;
         }

         if (this.isOraWsdlMetadataQuery(var2.getQueryString())) {
            this.publishWSDL(this.createConnection(var1, var2, var3));
            return;
         }
      }

      super.handle(var1, var2, var3);
   }

   public void publishWSDL(WSHTTPConnection var1) throws IOException {
      DeployInfo var2 = (DeployInfo)this.endpoint.getContainer().getSPI(DeployInfo.class);
      if ((var2 == null || DeployInfoUtil.exposeWsdl(var2)) && this.getEndpoint().getServiceDefinition() != null) {
         AdvertisementHelper var3 = AdvertisementHelperFactory.getAdvertisementHelper();
         if (var3 != null) {
            SDDocument var4 = (SDDocument)this.wsdls.get(getRequestCmd(var1.getQueryString()));
            if (var4 == this.getEndpoint().getServiceDefinition().getPrimary()) {
               PortAddressResolver var5 = this.getPortAddressResolver(var1.getBaseAddress());
               DocumentAddressResolver var6 = this.getDocumentAddressResolver(var5);
               boolean var7 = var3.handleAdvertisementRequest(var4, this.endpoint, var1, var5, var6);
               if (var7) {
                  return;
               }
            }
         }

         super.publishWSDL(var1);
      } else {
         var1.setStatus(404);
         var1.getOutput();
      }
   }

   public static String getRequestCmd(String var0) {
      String[] var1 = var0.split("&");

      for(int var2 = 0; var2 < var1.length; ++var2) {
         String var3 = var1[var2];
         int var4 = var3.indexOf(61);
         if (var4 > -1) {
            var3 = var3.substring(0, var4);
         }

         if ("WSDL".equalsIgnoreCase(var3) || "ORAWSDL".equalsIgnoreCase(var3)) {
            return var1[var2];
         }
      }

      return null;
   }

   private boolean isOraWsdlMetadataQuery(String var1) {
      if (var1 != null) {
         String[] var2 = var1.split("&");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3];
            int var5 = var4.indexOf(61);
            if (var5 > -1) {
               var4 = var4.substring(0, var5);
            }

            if ("WSDL".equalsIgnoreCase(var4) || "ORAWSDL".equalsIgnoreCase(var4)) {
               return true;
            }
         }
      }

      return false;
   }

   public class WLSServletConnection extends ServletConnectionImpl {
      public WLSServletConnection(WLSServletAdapter var2, ServletContext var3, HttpServletRequest var4, HttpServletResponse var5) {
         super(var2, var3, var4, var5);
      }

      public boolean isUserInRole(Packet var1, String var2) {
         if (super.isUserInRole(var1, var2)) {
            return true;
         } else {
            RoleManager var3 = SecurityServiceManager.getRoleManager(WLSServletAdapter.KERNEL_ID, SecurityServiceManager.getDefaultRealmName());
            WebServiceResource var4 = (WebServiceResource)var1.invocationProperties.get("weblogic.wsee.jaxws.security.resource");
            if (var4 != null) {
               Map var5 = var3.getRoles((AuthenticatedSubject)var1.invocationProperties.get("weblogic.wsee.jaxws.security.subject"), var4, (ContextHandler)var1.invocationProperties.get("weblogic.wsee.jaxws.security.contexthandler"));
               return var5.containsKey(var2);
            } else {
               return false;
            }
         }
      }

      public Principal getUserPrincipal(Packet var1) {
         Principal var2 = null;
         AuthenticatedSubject var3 = SecurityServiceManager.getCurrentSubject(WLSServletAdapter.KERNEL_ID);
         if (var3 != null) {
            var2 = SubjectUtils.getUserPrincipal(var3);
         }

         return var2 == null ? WLSPrincipals.getAnonymousUserPrincipal() : var2;
      }

      public OutputStream getOutput() throws IOException {
         OutputStream var1 = super.getOutput();
         if (this.isEndpointProcessingComplete()) {
            ((HttpServletAdapter.EndpointProcessingComplete)var1).onEndpointProcessingComplete();
         }

         return var1;
      }
   }
}
