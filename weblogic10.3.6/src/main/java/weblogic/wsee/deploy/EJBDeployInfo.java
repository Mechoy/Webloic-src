package weblogic.wsee.deploy;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.transport.http.WSHTTPConnection;
import com.sun.xml.ws.transport.http.servlet.ServletAdapter;
import com.sun.xml.ws.transport.http.servlet.ServletAdapterList;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.SessionContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceException;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.spi.SessionBeanInfo;
import weblogic.ejb.spi.WSObjectFactory;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.jaxws.JAXWSEjbServlet;
import weblogic.wsee.jaxws.WLSEjbInstanceResolver;
import weblogic.wsee.jaxws.WLSServletAdapter;
import weblogic.wsee.jaxws.WLSServletAdapterList;
import weblogic.wsee.jaxws.injection.WSEEComponentContributor;
import weblogic.wsee.jaxws.injection.WSEEServerComponentContributor;
import weblogic.wsee.jws.JWSVisitor;
import weblogic.wsee.jws.VisitableJWSBuilder;
import weblogic.wsee.server.servlet.EjbWSServlet;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.ws.WsFactory;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.soap11.SoapAddress;

public class EJBDeployInfo extends DeployInfo {
   static final String EJB_SERVICE_URL = "weblogic.wsee.EJBServiceURL";
   public static final String ALL_OPERTION = "--*";
   private static final String EJB_DEPLOY_INFO = "weblogic.wsee.EJBDeployInfo";
   private WSObjectFactory beanFactory;
   private WLSEjbInstanceResolver resolver;
   private SessionBeanInfo sbi;
   private Map<String, Short> transactionAttributes;

   EJBDeployInfo() {
   }

   public String getEjbName() {
      return this.linkName;
   }

   public void setServiceURI(String var1) {
      this.serviceURIs = new String[]{var1};
   }

   public void setServiceURIs(String[] var1) {
      this.serviceURIs = var1;
   }

   public WSObjectFactory getBeanFactory() {
      return this.beanFactory;
   }

   public void setBeanFactory(WSObjectFactory var1) {
      this.beanFactory = var1;
   }

   public WsService createWsService() throws WsException {
      return WsFactory.instance().createServerService(this);
   }

   String getContextPathFromWsdl() {
      if (this.getWsdlDef() == null) {
         return this.getJwsClass().getSimpleName();
      } else {
         WsdlPort var1 = (WsdlPort)this.getWsdlDef().getPorts().get(this.getPortComp().getWsdlPort());
         SoapAddress var2 = WsdlUtils.getSoapAddress(var1);
         if (var2 == null) {
            return null;
         } else {
            String var3 = var2.getLocation();
            if (var3 == null) {
               return null;
            } else if ("REPLACE_WITH_ACTUAL_URL".equals(var3)) {
               return this.getJwsClass().getSimpleName();
            } else {
               try {
                  URL var4 = new URL(var3);
                  return !"http".equalsIgnoreCase(var4.getProtocol()) && !"https".equalsIgnoreCase(var4.getProtocol()) ? null : var4.getPath();
               } catch (MalformedURLException var5) {
                  return null;
               }
            }
         }
      }
   }

   String getServlet() {
      return this.getWebServicesType() == WebServiceType.JAXRPC ? EjbWSServlet.class.getName() : JAXWSEjbServlet.class.getName();
   }

   void store(String var1, ServletContext var2) {
      var2.setAttribute("weblogic.wsee.EJBDeployInfo" + var1, this);
   }

   public static EJBDeployInfo load(HttpServlet var0) {
      String var1 = var0.getInitParameter("weblogic.wsee.EJBServiceURL");
      ServletContext var2 = var0.getServletContext();
      EJBDeployInfo var3 = (EJBDeployInfo)var2.getAttribute("weblogic.wsee.EJBDeployInfo" + var1);

      assert var3 != null;

      if (verbose) {
         Verbose.banner("Init virtual Web App for EJB: " + var3.getEjbName());
         Verbose.log((Object)("Service uri" + var1));
      }

      var2.removeAttribute("weblogic.wsee.EJBDeployInfo" + var1);
      return var3;
   }

   public WLSEjbInstanceResolver createInstanceResolver() {
      if (this.resolver == null) {
         this.resolver = new WLSEjbInstanceResolver(this.getBeanFactory(), this.getJwsClass());
      }

      return this.resolver;
   }

   ServletAdapterList createServletAdapterList() {
      return new WLSEjbServletAdapterList();
   }

   public void setEJBInfo(SessionBeanInfo var1) {
      this.sbi = var1;
   }

   public Map<String, Short> getTransactionAttributes() {
      ClientDrivenBeanInfo var1 = (ClientDrivenBeanInfo)this.sbi;
      if (var1.usesBeanManagedTx()) {
         return null;
      } else {
         if (this.transactionAttributes == null) {
            this.transactionAttributes = new HashMap();
            Map var2 = this.buildOperationMap(var1);
            Collection var3 = var1.getAllWebserviceMethodInfos();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               MethodInfo var5 = (MethodInfo)var4.next();
               short var6 = var5.getTransactionAttribute();
               String var7 = (String)var2.get(var5.getMethod());
               boolean var8 = var7 != null;
               if (var8) {
                  this.transactionAttributes.put(var7, var6);
               }
            }
         }

         return this.transactionAttributes;
      }
   }

   public WSEEComponentContributor loadComponentContributor() {
      List var1 = this.createServerHandlerChainsResolver().getMatchingHandlers();
      PitchforkContext var2 = new PitchforkContext((String)null);
      WSEEServerComponentContributor var3 = new WSEEServerComponentContributor(var1, var2);

      try {
         var3.init();
         return var3;
      } catch (Throwable var5) {
         throw new WebServiceException(var5);
      }
   }

   public URL getCatalog() throws MalformedURLException {
      return this.getResource("/META-INF" + File.separator + "jax-ws-catalog.xml");
   }

   private Map<Method, String> buildOperationMap(ClientDrivenBeanInfo var1) {
      final HashMap var2 = new HashMap();
      JWSVisitor var3 = new JWSVisitor() {
         boolean isProviderBasedWS = false;

         public void visitClass(JWSVisitor.JWSClass var1) {
            if (var1.isProviderBased()) {
               Method var2x = var1.getInvokeMethod();
               var2.put(var2x, "--*");
            }

         }

         public void visitMethod(JWSVisitor.WsMethod var1) {
            var2.put(var1.getSeiMethod(), var1.getOperationName());
         }

         public Map<Method, String> getOperationsMap() {
            return var2;
         }
      };
      Class var4 = var1.getBeanClass();
      VisitableJWSBuilder.jaxws().sei(var1.getServiceEndpointClass()).impl(var4).build().accept(var3);
      return var2;
   }

   private class WLSEjbServletAdapterList extends WLSServletAdapterList {
      private SessionContext context;

      private WLSEjbServletAdapterList() {
      }

      protected ServletAdapter createHttpAdapter(String var1, String var2, WSEndpoint<?> var3) {
         if (this.context == null) {
            this.context = EJBDeployInfo.this.createInstanceResolver().getContext();
         }

         return new WLSServletAdapter(var1, var2, var3, this) {
            protected WSHTTPConnection createConnection(ServletContext var1, HttpServletRequest var2, HttpServletResponse var3) {
               return new WLSServletAdapter.WLSServletConnection(this, var1, var2, var3) {
                  public Principal getUserPrincipal(Packet var1) {
                     return WLSEjbServletAdapterList.this.context.getCallerPrincipal();
                  }

                  public boolean isUserInRole(Packet var1, String var2) {
                     return WLSEjbServletAdapterList.this.context.isCallerInRole(var2);
                  }
               };
            }
         };
      }

      // $FF: synthetic method
      WLSEjbServletAdapterList(Object var2) {
         this();
      }
   }
}
