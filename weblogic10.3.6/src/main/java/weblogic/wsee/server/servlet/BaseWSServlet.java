package weblogic.wsee.server.servlet;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.jws.Policies;
import weblogic.jws.Policy;
import weblogic.jws.security.RolesAllowed;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.handler.HandlerListImpl;
import weblogic.wsee.jaxws.WLSContainer;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.policy.assertions.SecurityPolicyAssertionFactory;
import weblogic.wsee.util.ServerSecurityHelper;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsSkel;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlUtils;

public abstract class BaseWSServlet extends HttpServlet {
   private static final boolean verbose = Verbose.isVerbose(BaseWSServlet.class);
   private boolean forceNoFlush = Boolean.getBoolean("weblogic.wsee.NoFlush");
   protected WsPort wsPort = null;
   private SecurityHelper securityHelper = null;
   private List<Processor> processerList = null;
   private boolean forceAuth = false;
   private boolean forceHttps = false;
   private boolean forceClientCert = false;
   protected DeployInfo info = null;
   private int responseBufferSize = -1;
   private WLSContainer container;

   BaseWSServlet() {
   }

   public void init() throws ServletException {
      WebAppServletContext var1 = (WebAppServletContext)this.getServletContext();
      this.securityHelper = new SecurityHelper(var1);

      try {
         this.info = this.loadDeployInfo();

         assert this.info != null;

         this.info.setVersion(var1.getVersionId());
         this.info.setServletContext(var1);
         this.wsPort = this.info.createWsPort();
         this.processerList = this.getProcessorList();
         this.forceAuth = checkIfAuthRequired(this.info);
         this.forceHttps = SecurityHelper.isHttpsRequiredByWssp(this.info.getEndpointPolicy());
         this.forceClientCert = SecurityHelper.isClientCertRequiredByWssp(this.info.getEndpointPolicy());
         if (System.getProperty("weblogic.wsee.http.response.BufferSize") != null) {
            this.responseBufferSize = Integer.parseInt(System.getProperty("weblogic.wsee.http.response.BufferSize"));
         }

         PortComponentBean var2 = this.info.getWlPortComp();
         if (var2 != null) {
            int var3 = var2.getHttpResponseBuffersize();
            if (var3 != 0) {
               this.responseBufferSize = var3;
            }

            boolean var4 = !var2.isHttpFlushResponse();
            this.forceNoFlush = var4 || this.forceNoFlush;
         }

         this.container = new WLSContainer(var1, this.info);
         if (verbose) {
            Verbose.banner("Webservice successfully deployed");
         }

      } catch (Throwable var5) {
         if (verbose) {
            Verbose.logException(var5);
         }

         if (this.info != null) {
            this.info.clean();
         }

         throw new ServletException("Web Service init() failed: " + var5, var5);
      }
   }

   protected List<Processor> getProcessorList() {
      return ProcessorFactory.instance().getProcessorList(this.info);
   }

   abstract DeployInfo loadDeployInfo() throws ServletException;

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      if (verbose) {
         Verbose.banner(var1.getRequestURI());
      }

      this.container.setCurrent();

      try {
         label149: {
            if (var1.isSecure()) {
               if (this.forceClientCert && !SecurityHelper.isClientCertPresent(var1)) {
                  this.sendAccessError(var1, var2, "Client Certificate Required!");
                  return;
               }
            } else if (this.forceHttps) {
               this.sendAccessError(var1, var2, "SSL Required!");
               return;
            }

            if (this.responseBufferSize != -1) {
               var2.setBufferSize(this.responseBufferSize);
            }

            if (this.forceNoFlush) {
               var2 = this.createWrappedNoFlushResponse(var2);
            }

            AuthorizedInvoke var3 = new AuthorizedInvoke(var1, var2, this);
            AuthenticatedSubject var4 = ServerSecurityHelper.getCurrentSubject();
            if (this.forceAuth) {
               SecurityHelper var10000 = this.securityHelper;
               if (SecurityHelper.isAnonymous(var4)) {
                  var4 = this.securityHelper.getRequestSubject(var1);
                  if (var4 == null) {
                     this.sendAuthError(var1, var2, "Authentication Required!");
                  } else {
                     ServerSecurityHelper.authenticatedInvoke(var4, var3);
                  }
                  break label149;
               }
            }

            var3.run();
         }
      } catch (IOException var10) {
         throw var10;
      } catch (LoginException var11) {
         this.sendAuthError(var1, var2, var11.getMessage());
      } catch (Exception var12) {
         var12.printStackTrace();
         throw new ServletException(var12.getMessage(), var12);
      } finally {
         this.container.resetCurrent();
      }

      if (verbose) {
         Verbose.log((Object)"Web Service invocation completed");
      }

   }

   public void destroy() {
      ((WsSkel)this.wsPort.getEndpoint()).getComponent().destroy();
      HandlerList var1 = this.wsPort.getInternalHandlerList();
      if (null != var1 && var1 instanceof HandlerListImpl) {
         ((HandlerListImpl)var1).destroy();
      }

   }

   protected void sendAccessError(HttpServletRequest var1, HttpServletResponse var2, String var3) throws IOException, ServletException {
      try {
         var2.setStatus(403);
         SOAPMessage var4 = this.getFaultMessage("env:Client.Access", var3);
         var2.setContentType("text/xml");
         var4.writeTo(var2.getOutputStream());
      } catch (SOAPException var5) {
         throw new ServletException("unable to send error:", var5);
      }
   }

   protected void sendAuthError(HttpServletRequest var1, HttpServletResponse var2, String var3) throws IOException, ServletException {
      try {
         var2.setHeader("WWW-Authenticate", "Basic realm=\"" + this.securityHelper.getSecurityRealm() + "\"");
         var2.setStatus(401);
         SOAPMessage var4 = this.getFaultMessage("env:Client.Authentication", var3);
         if (WsdlUtils.isSoap12(this.wsPort)) {
            var2.setContentType("application/soap+xml");
         } else {
            var2.setContentType("text/xml");
         }

         var4.writeTo(var2.getOutputStream());
      } catch (SOAPException var5) {
         throw new ServletException("unable to send error:", var5);
      }
   }

   private SOAPMessage getFaultMessage(String var1, String var2) throws SOAPException {
      SOAPMessage var3 = WLMessageFactory.getInstance().getMessageFactory(WsdlUtils.isSoap12(this.wsPort)).createMessage();
      SOAPFault var4 = var3.getSOAPPart().getEnvelope().getBody().addFault();
      var4.setFaultCode(var1);
      var4.setFaultString(var2);
      return var3;
   }

   WsPort getPort() {
      return this.wsPort;
   }

   DeployInfo getDeployInfo() {
      return this.info;
   }

   private static boolean checkIfAuthRequired(DeployInfo var0) throws ServletException {
      try {
         if (SecurityHelper.isBasicAuthReqByWssp(var0.getEndpointPolicy())) {
            return true;
         }
      } catch (PolicyException var2) {
      }

      Class var1 = var0.getJwsClass();
      if (var1.isAnnotationPresent(RolesAllowed.class)) {
         if (!var1.isAnnotationPresent(Policy.class) && !var1.isAnnotationPresent(Policies.class)) {
            return true;
         } else {
            return !hasSecurityPolicy(var0);
         }
      } else {
         return false;
      }
   }

   /** @deprecated */
   private static boolean hasSecurityPolicy(DeployInfo var0) throws ServletException {
      QName var1 = new QName(var0.getWsdlDef().getTargetNamespace(), var0.getServiceName());
      WsdlPort var2 = (WsdlPort)var0.getWsdlDef().getPorts().get(var1);
      WsdlBinding var3 = var2.getBinding();
      Iterator var4 = var3.getOperations().values().iterator();
      Iterator var5 = var2.getPortType().getOperations().values().iterator();
      Map var6 = var0.gePolicySubject().getPolicies();
      PolicyServer var7 = var0.getWssPolicyContext().getPolicyServer();

      try {
         NormalizedExpression var8 = null;

         while(var4.hasNext()) {
            WsdlBindingOperation var9 = (WsdlBindingOperation)var4.next();

            while(var5.hasNext()) {
               WsdlOperation var10 = (WsdlOperation)var5.next();
               var8 = PolicyContext.getRequestEffectivePolicy(var2, var10, var9, var7, var6);
               if (var8 != null && (SecurityPolicyAssertionFactory.hasSecurityPolicy(var8) || SecurityHelper.hasWsspMessageSecurityPolicy(var8) && !SecurityHelper.hasWsTrustPolicy(var8))) {
                  return true;
               }
            }
         }

         return false;
      } catch (PolicyException var11) {
         throw new ServletException(var11);
      }
   }

   private HttpServletResponse createWrappedNoFlushResponse(HttpServletResponse var1) {
      return new HttpServletResponseWrapper(var1) {
         private ServletOutputStream wrappedStream = null;

         public ServletOutputStream getOutputStream() throws IOException {
            if (this.wrappedStream == null) {
               this.wrappedStream = new NoFlushServletOutputStream(super.getOutputStream());
            }

            return this.wrappedStream;
         }
      };
   }

   private static class AuthorizedInvoke implements PrivilegedExceptionAction {
      HttpServletRequest request;
      HttpServletResponse response;
      BaseWSServlet servlet;

      AuthorizedInvoke(HttpServletRequest var1, HttpServletResponse var2, BaseWSServlet var3) {
         this.request = var1;
         this.response = var2;
         this.servlet = var3;
      }

      public Object run() throws Exception {
         Iterator var1 = this.servlet.processerList.iterator();

         while(var1.hasNext()) {
            Processor var2 = (Processor)var1.next();
            boolean var3 = var2.process(this.request, this.response, this.servlet);
            if (var3) {
               break;
            }
         }

         return null;
      }
   }
}
