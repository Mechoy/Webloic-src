package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.transport.http.servlet.ServletAdapter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PrivilegedExceptionAction;
import javax.naming.Context;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.MTOMFeature;
import weblogic.jndi.factories.java.javaURLContextFactory;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.servlet.http.AbstractAsyncServlet;
import weblogic.servlet.http.RequestResponseKey;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.work.ExecuteThread;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.server.servlet.SecurityHelper;
import weblogic.wsee.util.ServerSecurityHelper;
import weblogic.wsee.util.ServletDebugUtil;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;

class HttpServletAdapter implements HTTPProcessor {
   private final WebAppServletContext servletContext;
   private final ServletAdapter servletAdapter;
   private final WLSServletAdapterList owner;
   private final WLSContainer container;
   private final SOAPVersion soapVersion;
   private WSEndpoint<?> endpoint;
   private final WsdlPort port;
   private final PolicyServer ps;
   private final String realmName;
   private NormalizedExpression ep;
   private boolean isSSLRequired;
   private boolean isClientCertRequired;
   private URL httpsServerURL;
   private boolean forceAuth;
   private final boolean isScheduleRequest;

   HttpServletAdapter(WSEndpoint<?> var1, WLSContainer var2, WebAppServletContext var3, ServletAdapter var4) {
      this(var1, var2, var3, var4, false);
   }

   HttpServletAdapter(WSEndpoint<?> var1, WLSContainer var2, WebAppServletContext var3, ServletAdapter var4, boolean var5) {
      this.ep = null;
      this.isSSLRequired = false;
      this.endpoint = var1;
      this.container = var2;
      this.servletContext = var3;
      this.servletAdapter = var4;
      this.isScheduleRequest = var5;
      this.owner = var4.owner instanceof WLSServletAdapterList ? (WLSServletAdapterList)var4.owner : null;
      this.realmName = var3.getSecurityRealmName();
      EnvironmentFactory var6 = JAXRPCEnvironmentFeature.getFactory(var1);
      WsdlDefinitions var7 = var1.getPort() != null ? var6.getWsdlDef() : null;
      this.port = var7 != null ? (WsdlPort)((WsdlService)var7.getServices().get(var1.getServiceName())).getPorts().get(var1.getPortName()) : null;
      EnvironmentFactory.SingletonService var8 = this.port != null ? var6.getService() : null;
      this.ps = var8 != null ? var8.getPolicyServer() : null;
      this.checkEndpointPolicy();
      this.checkTwoWaySSLEnabled();
      this.soapVersion = var1.getBinding().getSOAPVersion();
   }

   private void checkTwoWaySSLEnabled() {
      if (this.isClientCertRequired && !SecurityHelper.isTwoWaySSLEnabled()) {
         throw new WebServiceException("The WLS server is disabling the TwoWaySSL while Client Certificate is required!");
      }
   }

   private void checkEndpointPolicy() {
      if (this.port != null && this.ps != null) {
         try {
            NormalizedExpression var1 = this.ps.getEndpointPolicy(this.port);
            if (this.ep == null || !this.ep.equals(var1)) {
               this.isSSLRequired = SecurityHelper.isHttpsRequiredByWssp(var1);
               if (this.isSSLRequired) {
                  this.httpsServerURL = new URL(ServerUtil.getHTTPServerURL(true));
                  this.isClientCertRequired = SecurityHelper.isClientCertRequiredByWssp(var1);
               }

               this.forceAuth = SecurityHelper.isBasicAuthReqByWssp(var1);
               if (EndpointPolicyUtility.checkMTOMPolicy(var1) && this.endpoint.getBinding().getFeature(MTOMFeature.class) == null) {
                  ((WebServiceFeatureList)this.endpoint.getBinding().getFeatures()).add(new MTOMFeature());
               }

               WebServiceFeature var2 = EndpointPolicyUtility.checkUsingAddressingPolicy(var1);
               if (var2 != null && this.endpoint.getBinding().getFeature(var2.getClass()) == null) {
                  ((WebServiceFeatureList)this.endpoint.getBinding().getFeatures()).add(var2);
               }

               this.ep = var1;
            }
         } catch (PolicyException var3) {
            throw new WebServiceException(var3);
         } catch (MalformedURLException var4) {
            throw new WebServiceException(var4);
         }
      }

   }

   public boolean get(RequestResponseKey var1, boolean var2) throws ServletException {
      Object var3;
      Object var4;
      if (var2) {
         var3 = new VerboseHttpProcessor.VerboseHttpServletRequest(var1.getRequest());
         var4 = new VerboseHttpProcessor.VerboseHttpServletResponse(var1.getResponse());
         ServletDebugUtil.printRequest((HttpServletRequest)var3);
      } else {
         var3 = var1.getRequest();
         var4 = var1.getResponse();
      }

      boolean var7;
      try {
         this.checkEndpointPolicy();
         Object var5 = var3;
         if (this.isSSLRequired) {
            HttpServletRequestWrapper var6 = new HttpServletRequestWrapper((HttpServletRequest)var3) {
               public String getScheme() {
                  return "https";
               }

               public int getServerPort() {
                  return HttpServletAdapter.this.httpsServerURL.getPort();
               }
            };
            if (this.owner != null) {
               this.owner.registerPortAddressResolver(getBaseAddress((HttpServletRequest)var3), getBaseAddress(var6));
            } else {
               var5 = var6;
            }
         }

         RequestResponseWrapper var13 = new RequestResponseWrapper(var1, (HttpServletRequest)var5, (HttpServletResponse)var4, false);
         this.servletAdapter.handle(this.servletContext, var13.getRequest(), var13.getResponse());
         if (var2) {
            ServletDebugUtil.printResponse((HttpServletResponse)var4);
         }

         if (var2 && !var13.isNotified) {
            ServletDebugUtil.printResponse((HttpServletResponse)var4);
         }

         var7 = var13.isNotified;
      } catch (IOException var11) {
         throw new ServletException(var11);
      } finally {
         if (this.owner != null) {
            this.owner.clearPortAddressResolver();
         }

      }

      return var7;
   }

   private static String getBaseAddress(HttpServletRequest var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(var0.getScheme());
      var1.append("://");
      var1.append(var0.getServerName());
      var1.append(':');
      var1.append(var0.getServerPort());
      var1.append(var0.getContextPath());
      return var1.toString();
   }

   public boolean post(final RequestResponseKey var1, final boolean var2) throws ServletException {
      Object var3;
      final Object var4;
      if (var2) {
         var3 = new VerboseHttpProcessor.VerboseHttpServletRequest(var1.getRequest());
         var4 = new VerboseHttpProcessor.VerboseHttpServletResponse(var1.getResponse());
         ServletDebugUtil.printRequest((HttpServletRequest)var3);
      } else {
         var3 = var1.getRequest();
         var4 = var1.getResponse();
      }

      try {
         try {
            this.checkEndpointPolicy();
            this.checkTwoWaySSLEnabled();
         } catch (WebServiceException var11) {
            this.sendAccessError((HttpServletRequest)var3, (HttpServletResponse)var4, var11.getMessage());
            if (var2) {
               ServletDebugUtil.printResponse((HttpServletResponse)var4);
            }

            return false;
         }

         if (((HttpServletRequest)var3).isSecure()) {
            if (this.isClientCertRequired && !SecurityHelper.isClientCertPresent((HttpServletRequest)var3)) {
               this.sendAccessError((HttpServletRequest)var3, (HttpServletResponse)var4, "Client Certificate Required!");
               if (var2) {
                  ServletDebugUtil.printResponse((HttpServletResponse)var4);
               }

               return false;
            }
         } else if (this.isSSLRequired) {
            this.sendAccessError((HttpServletRequest)var3, (HttpServletResponse)var4, "SSL Required!");
            if (var2) {
               ServletDebugUtil.printResponse((HttpServletResponse)var4);
            }

            return false;
         }

         final RequestResponseWrapper var14 = new RequestResponseWrapper(var1, (HttpServletRequest)var3, (HttpServletResponse)var4, true);
         final AuthorizedInvoke var6 = new AuthorizedInvoke(var14);
         final AuthenticatedSubject var8 = ServerSecurityHelper.getCurrentSubject();
         Runnable var7;
         if (this.forceAuth && SecurityHelper.isAnonymous(var8)) {
            final AuthenticatedSubject var9 = SecurityHelper.getRequestSubject((HttpServletRequest)var3, this.realmName);
            if (var8 == null) {
               this.sendAuthError((HttpServletRequest)var3, (HttpServletResponse)var4, "Authentication Required!");
               if (var2) {
                  ServletDebugUtil.printResponse((HttpServletResponse)var4);
               }

               return false;
            }

            var7 = new Runnable() {
               public void run() {
                  Throwable var1x = null;

                  try {
                     ServerSecurityHelper.authenticatedInvoke(var9, var6);
                  } catch (Throwable var4x) {
                     var1x = var4x;
                  }

                  try {
                     if (HttpServletAdapter.this.isScheduleRequest && !var14.isNotified) {
                        if (var2) {
                           ServletDebugUtil.printResponse((HttpServletResponse)var4);
                        }

                        AbstractAsyncServlet.notify(var1, var1x);
                     }
                  } catch (IOException var3) {
                  }

               }
            };
         } else {
            var7 = new Runnable() {
               public void run() {
                  Throwable var1x = null;

                  try {
                     ServerSecurityHelper.authenticatedInvoke(var8, var6);
                  } catch (Throwable var4x) {
                     var1x = var4x;
                  }

                  try {
                     if (HttpServletAdapter.this.isScheduleRequest && !var14.isNotified) {
                        if (var2) {
                           ServletDebugUtil.printResponse((HttpServletResponse)var4);
                        }

                        AbstractAsyncServlet.notify(var1, var1x);
                     }
                  } catch (IOException var3) {
                  }

               }
            };
         }

         if (this.isScheduleRequest) {
            this.getWorkManager().schedule(var7);
            return true;
         } else {
            var7.run();
            if (var2 && !var14.isNotified) {
               ServletDebugUtil.printResponse((HttpServletResponse)var4);
            }

            return var14.isNotified;
         }
      } catch (LoginException var12) {
         LoginException var5 = var12;

         try {
            this.sendAuthError((HttpServletRequest)var3, (HttpServletResponse)var4, var5.getMessage());
            return false;
         } catch (IOException var10) {
            throw new ServletException(var10);
         }
      } catch (Throwable var13) {
         throw new ServletException(var13);
      }
   }

   private WorkManager getWorkManager() {
      Thread var1 = Thread.currentThread();
      Object var2 = null;
      if (var1 instanceof ExecuteThread) {
         var2 = ((ExecuteThread)var1).getWorkManager();
      }

      if (var2 == null) {
         var2 = WorkManagerFactory.getInstance().getDefault();
      }

      return (WorkManager)var2;
   }

   protected void sendAccessError(HttpServletRequest var1, HttpServletResponse var2, String var3) throws IOException, ServletException {
      try {
         var2.setStatus(403);
         SOAPMessage var4 = this.getFaultMessage(new QName(this.soapVersion.nsUri, "Client.Access"), var3);
         this.setResponseContentType(var1, var2);
         var4.writeTo(var2.getOutputStream());
         var2.getOutputStream().close();
      } catch (SOAPException var5) {
         throw new ServletException("unable to send error:", var5);
      }
   }

   protected void sendAuthError(HttpServletRequest var1, HttpServletResponse var2, String var3) throws IOException, ServletException {
      try {
         var2.setHeader("WWW-Authenticate", "Basic realm=\"" + this.realmName + "\"");
         var2.setStatus(401);
         SOAPMessage var4 = this.getFaultMessage(new QName(this.soapVersion.nsUri, "Client.Authentication"), var3);
         this.setResponseContentType(var1, var2);
         var4.writeTo(var2.getOutputStream());
         var2.getOutputStream().close();
      } catch (SOAPException var5) {
         throw new ServletException("unable to send error:", var5);
      }
   }

   private SOAPMessage getFaultMessage(QName var1, String var2) throws SOAPException {
      SOAPMessage var3 = this.soapVersion.getMessageFactory().createMessage();
      SOAPFault var4 = var3.getSOAPPart().getEnvelope().getBody().addFault();
      var4.setFaultCode(var1);
      var4.setFaultString(var2);
      return var3;
   }

   private void setResponseContentType(HttpServletRequest var1, HttpServletResponse var2) {
      String var3 = var1.getContentType();
      if (var3 == null) {
         var2.setContentType("text/xml");
      } else {
         var2.setContentType(var3);
      }

   }

   private class AuthorizedInvoke implements PrivilegedExceptionAction<Object> {
      private final RequestResponseWrapper wrap;
      private final ClassLoader cl;
      private final Context context;

      public AuthorizedInvoke(RequestResponseWrapper var2) {
         this.wrap = var2;
         this.cl = Thread.currentThread().getContextClassLoader();
         this.context = HttpServletAdapter.this.servletContext.getEnvironmentContext();
      }

      public Object run() throws Exception {
         if (HttpServletAdapter.this.isScheduleRequest) {
            HttpServletAdapter.this.container.setCurrent();

            try {
               Thread var1 = Thread.currentThread();
               ClassLoader var2 = var1.getContextClassLoader();
               var1.setContextClassLoader(this.cl);
               javaURLContextFactory.pushContext(this.context);

               try {
                  HttpServletAdapter.this.servletAdapter.handle(HttpServletAdapter.this.servletContext, this.wrap.getRequest(), this.wrap.getResponse());
               } finally {
                  javaURLContextFactory.popContext();
                  var1.setContextClassLoader(var2);
               }
            } finally {
               HttpServletAdapter.this.container.resetCurrent();
            }
         } else {
            HttpServletAdapter.this.servletAdapter.handle(HttpServletAdapter.this.servletContext, this.wrap.getRequest(), this.wrap.getResponse());
         }

         return null;
      }
   }

   private class RequestResponseWrapper {
      private RequestWrapper request;
      private ResponseWrapper response;
      private RequestResponseKey rrk;
      private boolean isEndpointProcessingComplete;
      private transient boolean isNotified;
      private transient boolean isNotifyPending;
      private transient boolean isRequestClosed;

      public RequestResponseWrapper(RequestResponseKey var2) {
         this(var2, var2.getRequest(), var2.getResponse(), true);
      }

      public RequestResponseWrapper(RequestResponseKey var2, HttpServletRequest var3, HttpServletResponse var4, boolean var5) {
         this.isNotified = false;
         this.isNotifyPending = false;
         this.isRequestClosed = false;
         this.request = new RequestWrapper(var3);
         this.response = new ResponseWrapper(var4);
         this.rrk = var2;
         this.isEndpointProcessingComplete = !var5;
      }

      public RequestWrapper getRequest() {
         return this.request;
      }

      public ResponseWrapper getResponse() {
         return this.response;
      }

      private class InputStreamWrapper extends ServletInputStream {
         private ServletInputStream delegate;

         public InputStreamWrapper(ServletInputStream var2) {
            this.delegate = var2;
         }

         public int readLine(byte[] var1, int var2, int var3) throws IOException {
            return this.delegate.readLine(var1, var2, var3);
         }

         public int read() throws IOException {
            return this.delegate.read();
         }

         public int read(byte[] var1) throws IOException {
            return this.delegate.read(var1);
         }

         public int read(byte[] var1, int var2, int var3) throws IOException {
            return this.delegate.read(var1, var2, var3);
         }

         public long skip(long var1) throws IOException {
            return this.delegate.skip(var1);
         }

         public int available() throws IOException {
            return this.delegate.available();
         }

         public void close() throws IOException {
            RequestResponseWrapper.this.isRequestClosed = true;
            if (RequestResponseWrapper.this.isEndpointProcessingComplete && !HttpServletAdapter.this.isScheduleRequest) {
               this.delegate.close();
            } else if (!RequestResponseWrapper.this.isNotified) {
               this.delegate.close();
               if (RequestResponseWrapper.this.isNotifyPending) {
                  try {
                     RequestResponseWrapper.this.isNotified = true;
                     AbstractAsyncServlet.notify(RequestResponseWrapper.this.rrk, (Object)null);
                  } catch (IOException var2) {
                  }
               }
            }

         }

         public void mark(int var1) {
            this.delegate.mark(var1);
         }

         public void reset() throws IOException {
            this.delegate.reset();
         }

         public boolean markSupported() {
            return this.delegate.markSupported();
         }
      }

      private class RequestWrapper extends HttpServletRequestWrapper {
         private InputStreamWrapper is = null;

         public RequestWrapper(HttpServletRequest var2) {
            super(var2);
         }

         public ServletInputStream getInputStream() throws IOException {
            if (this.is == null) {
               this.is = RequestResponseWrapper.this.new InputStreamWrapper(super.getInputStream());
            }

            return this.is;
         }
      }

      private class OutputStreamWrapper extends ServletOutputStream implements EndpointProcessingComplete {
         private ServletOutputStream delegate;

         public OutputStreamWrapper(ServletOutputStream var2) {
            this.delegate = var2;
         }

         public void onEndpointProcessingComplete() {
            RequestResponseWrapper.this.isEndpointProcessingComplete = true;
         }

         public void print(String var1) throws IOException {
            this.delegate.print(var1);
         }

         public void print(boolean var1) throws IOException {
            this.delegate.print(var1);
         }

         public void print(char var1) throws IOException {
            this.delegate.print(var1);
         }

         public void print(int var1) throws IOException {
            this.delegate.print(var1);
         }

         public void print(long var1) throws IOException {
            this.delegate.print(var1);
         }

         public void print(float var1) throws IOException {
            this.delegate.print(var1);
         }

         public void print(double var1) throws IOException {
            this.delegate.print(var1);
         }

         public void println() throws IOException {
            this.delegate.println();
         }

         public void println(String var1) throws IOException {
            this.delegate.println(var1);
         }

         public void println(boolean var1) throws IOException {
            this.delegate.println(var1);
         }

         public void println(char var1) throws IOException {
            this.delegate.println(var1);
         }

         public void println(int var1) throws IOException {
            this.delegate.println(var1);
         }

         public void println(long var1) throws IOException {
            this.delegate.println(var1);
         }

         public void println(float var1) throws IOException {
            this.delegate.println(var1);
         }

         public void println(double var1) throws IOException {
            this.delegate.println(var1);
         }

         public void write(int var1) throws IOException {
            this.delegate.write(var1);
         }

         public void write(byte[] var1) throws IOException {
            this.delegate.write(var1);
         }

         public void write(byte[] var1, int var2, int var3) throws IOException {
            this.delegate.write(var1, var2, var3);
         }

         public void flush() throws IOException {
            this.delegate.flush();
         }

         public void close() throws IOException {
            if (RequestResponseWrapper.this.isEndpointProcessingComplete && !HttpServletAdapter.this.isScheduleRequest) {
               this.delegate.close();
            } else if (!RequestResponseWrapper.this.isNotified) {
               this.delegate.close();
               if (RequestResponseWrapper.this.isRequestClosed) {
                  try {
                     RequestResponseWrapper.this.isNotified = true;
                     AbstractAsyncServlet.notify(RequestResponseWrapper.this.rrk, (Object)null);
                  } catch (IOException var2) {
                  }
               } else {
                  RequestResponseWrapper.this.isNotifyPending = true;
               }
            }

         }
      }

      private class ResponseWrapper extends HttpServletResponseWrapper {
         private OutputStreamWrapper os = null;

         public ResponseWrapper(HttpServletResponse var2) {
            super(var2);
         }

         public ServletOutputStream getOutputStream() throws IOException {
            if (this.os == null) {
               this.os = RequestResponseWrapper.this.new OutputStreamWrapper(super.getOutputStream());
            }

            return this.os;
         }
      }
   }

   public interface EndpointProcessingComplete {
      void onEndpointProcessingComplete();
   }
}
