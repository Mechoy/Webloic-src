package weblogic.servlet.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.security.Principal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.spi.BeanInfo;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.logging.Loggable;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelStream;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.session.RSID;
import weblogic.servlet.internal.session.ReplicatedSessionContext;
import weblogic.servlet.internal.session.SessionContext;
import weblogic.servlet.internal.session.SessionData;
import weblogic.servlet.internal.session.SessionInternal;
import weblogic.servlet.internal.session.SharedSessionData;
import weblogic.servlet.logging.HttpAccountingInfoImpl;
import weblogic.servlet.security.internal.SecurityModule;
import weblogic.servlet.security.internal.WebAppContextHandler;
import weblogic.utils.Debug;
import weblogic.utils.NestedRuntimeException;
import weblogic.utils.StringUtils;
import weblogic.utils.collections.ArrayMap;
import weblogic.utils.collections.Iterators;
import weblogic.utils.enumerations.EmptyEnumerator;
import weblogic.utils.enumerations.IteratorEnumerator;
import weblogic.utils.http.BytesToString;
import weblogic.utils.http.HttpConstants;
import weblogic.utils.http.HttpParsing;
import weblogic.utils.http.HttpRequestParser;
import weblogic.utils.http.QueryParams;
import weblogic.utils.io.FilenameEncoder;
import weblogic.utils.string.CachingDateFormat;
import weblogic.utils.string.ThreadLocalDateFormat;
import weblogic.work.ServerWorkAdapter;
import weblogic.work.WorkManager;

public final class ServletRequestImpl extends ServerWorkAdapter implements HttpServletRequest, ServerChannelStream {
   protected static final String WL_DEBUG_SESSION = "wl_debug_session";
   private boolean futureResponse;
   private static final boolean isWLProxyHeadersAccessible;
   private static final boolean isRemoteUserHeaderAccessible;
   private WebAppServletContext context;
   private ContextVersionManager contextManager;
   private ServletResponseImpl response;
   private ServletInputStream inputStream;
   private WebAppContextHandler securityContextHandler;
   private final VirtualConnection connection;
   private final HttpAccountingInfoImpl accountInfo = new HttpAccountingInfoImpl(this);
   private final SessionHelper sessionHelper;
   private final RequestInputHelper inputHelper = new RequestInputHelper();
   private final RequestHeaders headers = new RequestHeaders();
   private final RequestParameters parameters = new RequestParameters(this);
   private final AttributesMap attributes = new AttributesMap("request");
   private ServletStubImpl sstub;
   private boolean useInputStream;
   private boolean useReader;
   private BufferedReader bufferedReader;
   private String relativeURI;
   private String servletPath;
   private String pathInfo;
   private boolean sendRedirect;
   private String redirectLocation;
   private boolean checkIndexFile;
   private String inputEncoding;
   private boolean inputEncodingInitialized;
   private String queryParamsEncoding;
   private String serverName;
   private int serverPort;
   private Locale[] locales;
   private Cookie[] cookies;
   private boolean cookiesParsed;
   private boolean underExecution;
   private boolean performOverloadAction;
   private String overloadRejectionMessage;
   private boolean preventRedispatch;
   static final long serialVersionUID = 1200336484278099024L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.servlet.internal.ServletRequestImpl");
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Request_Run_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   ServletRequestImpl(MuxableSocketHTTP var1) {
      this.connection = new VirtualConnection(this, var1);
      this.sessionHelper = new SessionHelper(this, (SessionHelper)null);
   }

   private ServletRequestImpl(MuxableSocketHTTP var1, SessionHelper var2) {
      this.connection = new VirtualConnection(this, var1);
      this.sessionHelper = new SessionHelper(this, var2);
   }

   void reset() {
      this.skipUnreadBody();
      this.useInputStream = false;
      this.useReader = false;
      this.relativeURI = null;
      this.sendRedirect = false;
      this.redirectLocation = null;
      this.locales = null;
      this.sstub = null;
      this.inputStream = null;
      this.bufferedReader = null;
      this.servletPath = null;
      this.pathInfo = null;
      this.attributes.clear();
      this.inputEncoding = null;
      this.inputEncodingInitialized = false;
      this.serverName = null;
      this.checkIndexFile = false;
      this.cookiesParsed = false;
      this.cookies = null;
      this.connection.reset();
      this.accountInfo.clear();
      this.sessionHelper.reset();
      this.inputHelper.reset();
      this.headers.reset();
      this.parameters.reset();
      this.context = null;
      this.preventRedispatch = false;
   }

   void skipUnreadBody() {
      if (!this.inputHelper.getRequestParser().isMethodSafe()) {
         boolean var1 = "Chunked".equalsIgnoreCase(this.headers.getTransferEncoding());
         ServletInputStreamImpl var2 = this.getServletInputStreamImpl();
         if (var2 != null && !var2.isClosed()) {
            try {
               if (var1) {
                  var2.ensureChunkedConsumed();
               } else {
                  int var3 = this.getContentLength();
                  int var4 = var2.getBytesRead();
                  if (var4 < var3) {
                     var2.skip((long)(var3 - var4));
                  }
               }
            } catch (IOException var5) {
            }
         }
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(512);
      var1.append(super.toString()).append("[\n").append(this.getMethod()).append(' ').append(this.getRequestURI());
      String var2 = this.getQueryString();
      if (var2 != null) {
         var1.append('?').append(var2);
      }

      var1.append(' ').append(this.getProtocol()).append('\n');
      ArrayList var3 = this.headers.getHeaderNamesAsArrayList();
      ArrayList var4 = this.headers.getHeaderValuesAsArrayList();
      if (var3 != null) {
         for(int var5 = 0; var5 < var3.size(); ++var5) {
            String var6 = (String)var3.get(var5);
            if (var6 != null) {
               byte[] var7 = (byte[])((byte[])var4.get(var5));
               if (var7 != null && !"Host".equalsIgnoreCase(var6)) {
                  String var8;
                  try {
                     var8 = new String(var7, this.getInputEncoding());
                  } catch (UnsupportedEncodingException var10) {
                     if (HTTPDebugLogger.isEnabled()) {
                        this.trace(var10, var10.getMessage());
                     }

                     var8 = new String(var7);
                  }

                  var1.append(var6).append(": ").append(var8).append('\n');
               }
            }
         }
      }

      var1.append("\n]");
      return var1.toString();
   }

   public String toStringSimple() {
      return "HttpRequest@" + super.hashCode() + " - " + this.getRequestURI();
   }

   public RequestInputHelper getInputHelper() {
      return this.inputHelper;
   }

   public RequestHeaders getRequestHeaders() {
      return this.headers;
   }

   public RequestParameters getRequestParameters() {
      return this.parameters;
   }

   void initFromRequestParser(HttpRequestParser var1) {
      this.inputHelper.initFromRequestParser(var1, this.parameters);
      this.headers.setHeaders(var1.getHeaderNames(), var1.getHeaderValues());
      this.getConnection().init();
      this.initProxyHeaders();
      this.relativeURI = this.computeRelativeUri(this.inputHelper.getNormalizedURI());
   }

   void initFromRequestParser() {
      this.inputHelper.restore();
      this.relativeURI = this.computeRelativeUri(this.inputHelper.getNormalizedURI());
   }

   void initFromRequestURI(String var1) {
      this.inputHelper.initFromRequestURI(var1);
      this.relativeURI = this.computeRelativeUri(this.inputHelper.getNormalizedURI());
      if (HTTPDebugLogger.isEnabled()) {
         this.trace("URI: " + this.inputHelper.getNormalizedURI());
      }

   }

   void addForwardParameter(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.parameters.addForwardQueryString(var1);
      }
   }

   void addIncludeParameter(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.parameters.addIncludeQueryString(var1);
      }
   }

   void removeRequestDispatcherQueryString() {
      this.parameters.removeRequestDispatcherQueryString();
   }

   private void initProxyHeaders() {
      if (this.headers.isWlProxyFound()) {
         ArrayList var1 = this.headers.getHeaderNamesAsArrayList();
         ArrayList var2 = this.headers.getHeaderValuesAsArrayList();

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            String var4 = (String)var1.get(var3);
            byte[] var5 = (byte[])((byte[])var2.get(var3));
            if (StringUtils.indexOfIgnoreCase(var4, "WL-Proxy-") >= 0) {
               this.getConnection().processProxyHeader(var4, var5);
               if (!isWLProxyHeadersAccessible) {
                  var1.remove(var3);
                  var2.remove(var3);
                  --var3;
               }
            }
         }

      }
   }

   void initContext(WebAppServletContext var1) {
      WebAppServletContext var2 = this.context;
      this.context = var1;
      if (var2 == null) {
         this.relativeURI = this.computeRelativeUri(this.inputHelper.getNormalizedURI());
      }

   }

   void initContextManager(ContextVersionManager var1) {
      this.contextManager = var1;
      this.context = null;
      if (var1 != null) {
         this.sessionHelper.initSessionInfo();
      }

   }

   private void initServerNameAndPort() {
      String var1 = this.headers.getHost();
      int var2 = this.headers.getPort();
      HttpServer var3 = this.context.getServer();
      String var4 = var3.getFrontendHost();
      if (var4 != null) {
         this.setServerName(var4);
      } else if (var1 != null) {
         this.setServerName(var1);
      } else {
         this.setServerName(var3.getListenAddress());
      }

      int var5;
      short var6;
      if (this.connection.isSecure()) {
         var5 = var3.getFrontendHTTPSPort();
         var6 = 443;
      } else {
         var5 = var3.getFrontendHTTPPort();
         var6 = 80;
      }

      if (var5 != 0) {
         this.setServerPort(var5);
      } else if (var2 != -1) {
         this.setServerPort(var2);
      } else {
         if (var1 == null) {
            this.setServerPort(this.connection.getLocalPort());
         } else {
            this.setServerPort(var6);
         }

      }
   }

   public WebAppServletContext getContext() {
      return this.context;
   }

   public HttpAccountingInfoImpl getHttpAccountingInfo() {
      return this.accountInfo;
   }

   public String getInputEncoding() {
      if (this.inputEncoding != null) {
         return this.inputEncoding;
      } else {
         return this.context != null ? this.context.getConfigManager().getDefaultEncoding() : "ISO-8859-1";
      }
   }

   private String getQueryParamsEncoding() {
      return this.queryParamsEncoding != null ? this.queryParamsEncoding : this.getInputEncoding();
   }

   public void setCharacterEncoding(String var1) throws UnsupportedEncodingException {
      if (!this.useReader) {
         this.setCharacterEncodingInternal(var1);
      }
   }

   private void setCharacterEncodingInternal(String var1) throws UnsupportedEncodingException {
      boolean var2;
      try {
         var2 = Charset.isSupported(var1);
      } catch (IllegalCharsetNameException var4) {
         throw new UnsupportedEncodingException("Unsupported Encoding " + var1);
      }

      if (!var2) {
         throw new UnsupportedEncodingException("Unsupported Encoding" + var1);
      } else {
         String var3 = this.getQueryParamsEncoding();
         this.inputEncoding = this.context.getConfigManager().getCharsetMap().getJavaCharset(var1);
         if (!this.inputEncoding.equals(var3)) {
            this.parameters.resetQueryParams();
         }

      }
   }

   public String getCharacterEncoding() {
      try {
         this.initRequestEncoding();
      } catch (UnsupportedEncodingException var2) {
         return null;
      }

      return this.inputEncoding;
   }

   public void setQueryCharacterEncoding(String var1) throws UnsupportedEncodingException {
      if (!Charset.isSupported(var1)) {
         throw new UnsupportedEncodingException("Unsupported Encoding " + var1);
      } else {
         this.queryParamsEncoding = this.context.getConfigManager().getCharsetMap().getJavaCharset(var1);
         this.parameters.resetQueryParams();
      }
   }

   public ServletResponseImpl getResponse() {
      return this.response;
   }

   void setResponse(ServletResponseImpl var1) {
      this.response = var1;
   }

   public ServletStubImpl getServletStub() {
      return this.sstub;
   }

   void setServletStub(ServletStubImpl var1) {
      this.sstub = var1;
   }

   void enableFutureResponse() {
      this.futureResponse = true;
      ((MuxableSocketHTTP)this.connection.getSocketRuntime()).disableRequestResponseReuse();
   }

   boolean isFutureResponseEnabled() {
      return this.futureResponse;
   }

   void disableFutureResponse() {
      this.futureResponse = false;
   }

   boolean getSendRedirect() {
      return this.sendRedirect;
   }

   void setRedirectURI(String var1) {
      this.sendRedirect = true;
      this.redirectLocation = var1;
   }

   String getRedirectURI() {
      return this.redirectLocation;
   }

   public String getMethod() {
      return this.inputHelper.getRequestParser().getMethod();
   }

   public void setMethod(String var1) {
      this.inputHelper.getRequestParser().setMethod(var1);
      this.response.getServletOutputStream().setWriteEnabled(!this.inputHelper.getRequestParser().isMethodHead());
   }

   public int getContentLength() {
      return this.headers.getContentLength();
   }

   public String getContentType() {
      return this.headers.getContentType();
   }

   public StringBuffer getRequestURL() {
      StringBuffer var1 = new StringBuffer(this.getScheme());
      var1.append("://");
      var1.append(this.serverName);
      var1.append(":");
      var1.append(this.serverPort);
      var1.append(this.inputHelper.getOriginalURI());
      return var1;
   }

   public String getRequestURI() {
      if (this.context != null) {
         String var1 = this.context.getSessionContext().getConfigMgr().getCookieName();
         return this.inputHelper.getRequestURI(var1);
      } else {
         return this.inputHelper.getRequestURI((String)null);
      }
   }

   public static String getResolvedURI(HttpServletRequest var0) {
      if (var0 instanceof ServletRequestImpl) {
         return ((ServletRequestImpl)var0).inputHelper.getNormalizedURI();
      } else {
         String var1 = HttpParsing.unescape(var0.getRequestURI(), HttpRequestParser.getURIDecodeEncoding()).trim();
         return FilenameEncoder.resolveRelativeURIPath(var1);
      }
   }

   public static String getResolvedContextPath(HttpServletRequest var0) {
      if (var0 instanceof ServletRequestImpl) {
         return ((ServletRequestImpl)var0).getContext().getContextPath();
      } else {
         String var1 = HttpParsing.unescape(var0.getContextPath(), HttpRequestParser.getURIDecodeEncoding()).trim();
         return FilenameEncoder.resolveRelativeURIPath(var1);
      }
   }

   public String getRealPath(String var1) {
      return this.context.getRealPath(var1);
   }

   public String getContextPath() {
      String var1 = null;
      String var2 = (String)this.getAttribute("weblogic.servlet.internal.crosscontext.type");
      if ("include" == var2) {
         var1 = (String)this.getAttribute("weblogic.servlet.internal.crosscontext.path");
      }

      if (var1 == null) {
         var1 = this.context.getContextPath();
      }

      if (var1.length() < 2) {
         return "";
      } else if ("forward" == var2) {
         return var1;
      } else {
         int var3 = var1.length();
         String var4 = this.inputHelper.getOriginalURI();
         int var5 = var4.length();
         String var6 = this.inputHelper.getNormalizedURI();
         if (var4.equals(var6)) {
            return var1;
         } else {
            String var7 = null;
            String var8 = null;
            int var9 = 0;
            int var10 = var5 - var3;
            int var11 = (var10 - var9) / 2;

            while(var11 != var9 || var11 != var10) {
               var7 = var4.substring(0, var11 + var3);
               var8 = HttpParsing.unescape(var7, HttpRequestParser.getURIDecodeEncoding());
               var8 = FilenameEncoder.resolveRelativeURIPath(var8, true);
               if (var8 == null) {
                  break;
               }

               boolean var12 = var8.startsWith(var1);
               boolean var13 = var8.endsWith(var1);
               if (var12) {
                  if (var13 && var8.length() == var1.length()) {
                     break;
                  }

                  var10 = var11;
                  var11 = var9 + (var11 - var9) / 2;
               } else if (var7.endsWith("/..")) {
                  var11 -= "/..".length();
               } else {
                  var9 = var11;
                  var11 = var10 - (var10 - var11) / 2;
               }
            }

            Debug.assertion(var7 != null, "Cannot determine encoded context path");
            return var7;
         }
      }
   }

   public String getServletPath() {
      return this.servletPath;
   }

   public String getPathInfo() {
      return this.pathInfo;
   }

   void setServletPathAndPathInfo(String var1, String var2) {
      this.servletPath = var2;
      this.pathInfo = computePathInfo(var1, this.servletPath);
   }

   static String computePathInfo(String var0, String var1) {
      int var2 = var1.length();
      return var2 >= var0.length() ? null : var0.substring(var2);
   }

   public String getPathTranslated() {
      return this.pathInfo != null ? this.getRealPath(this.pathInfo) : null;
   }

   public Object getAttribute(String var1) {
      return this.attributes.get(var1, this.context);
   }

   public void setAttribute(String var1, Object var2) {
      if (var2 == null) {
         this.removeAttribute(var1);
      } else {
         this.attributes.put(var1, var2, this.context);
      }
   }

   public void removeAttribute(String var1) {
      this.attributes.remove(var1);
   }

   public Enumeration getAttributeNames() {
      return (Enumeration)(this.attributes.isEmpty() ? new EmptyEnumerator() : new IteratorEnumerator(this.attributes.keys()));
   }

   public RequestDispatcher getRequestDispatcher(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = null;
         if (var1.length() > 0) {
            if (var1.charAt(0) != '/') {
               String var3 = this.getRelativeUri();
               int var4 = var3.lastIndexOf(47);
               if (var4 < 1) {
                  var1 = "/" + var1;
               } else {
                  var1 = var3.substring(0, var4 + 1) + var1;
               }
            }

            int var5 = var1.indexOf(63);
            if (var5 > 0 && var5 < var1.length()) {
               var2 = var1.substring(var5 + 1);
               if (var2.equals("")) {
                  var2 = null;
               }

               var1 = var1.substring(0, var5);
            }
         }

         var1 = FilenameEncoder.resolveRelativeURIPath(var1, true);
         return var1 == null ? null : new RequestDispatcherImpl(var1, var2, this.context, -1);
      }
   }

   public String getQueryString() {
      return this.parameters.getQueryString(this.getQueryParamsEncoding());
   }

   String getOriginalQueryString() {
      return this.parameters.getOriginalQueryString(this.getQueryParamsEncoding());
   }

   public String getParameter(String var1) {
      return this.parameters.getParameter(var1);
   }

   public Enumeration getParameterNames() {
      return this.parameters.getParameterNames();
   }

   public String[] getParameterValues(String var1) {
      if (HTTPDebugLogger.isEnabled()) {
         this.trace("Querying multiple: " + var1);
      }

      return this.parameters.getParameterValues(var1);
   }

   public String getProtocol() {
      return this.inputHelper.getRequestParser().getProtocol();
   }

   public String getServerName() {
      return this.serverName;
   }

   void setServerName(String var1) {
      this.serverName = var1;
      if (HTTPDebugLogger.isEnabled()) {
         this.trace("Servername: " + var1);
      }

   }

   public int getServerPort() {
      return this.serverPort;
   }

   void setServerPort(int var1) {
      this.serverPort = var1;
      if (HTTPDebugLogger.isEnabled()) {
         this.trace("Serverport: " + var1);
      }

   }

   public String getRemoteUser() {
      if (isRemoteUserHeaderAccessible) {
         String var1 = this.headers.getRemoteUser();
         if (var1 != null) {
            return var1;
         }
      }

      AuthenticatedSubject var2 = SecurityModule.getCurrentUser(this.context.getServer(), this);
      return var2 == null ? null : SubjectUtils.getUsername(var2);
   }

   public Map getParameterMap() {
      ArrayMap var1 = new ArrayMap();
      Enumeration var2 = this.getParameterNames();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         String[] var4 = this.getParameterValues(var3);
         var1.put(var3, var4);
      }

      return Collections.unmodifiableMap(var1);
   }

   public String getRemoteAddr() {
      if (this.context != null) {
         String var1 = this.context.getServer().getClientIpHeader();
         if (var1 != null) {
            String var2 = this.getHeader(var1);
            if (var2 != null) {
               int var3 = var2.indexOf(44);
               if (var3 >= 0) {
                  var2 = var2.substring(0, var3);
               }

               return var2;
            }
         }
      }

      return this.connection.getRemoteAddr();
   }

   public String getRemoteHost() {
      return this.connection.getRemoteHost();
   }

   public int getRemotePort() {
      return this.connection.getRemotePort();
   }

   public String getLocalAddr() {
      return this.connection.getLocalAddr();
   }

   public String getLocalName() {
      return this.connection.getLocalName();
   }

   public int getLocalPort() {
      return this.connection.getLocalPort();
   }

   public String getAuthType() {
      AuthenticatedSubject var1 = SecurityModule.getCurrentUser(this.context.getServer(), this);
      if (var1 == null) {
         return null;
      } else {
         String var2 = this.headers.getProxyAuthType();
         return var2 != null ? var2 : this.context.getSecurityManager().getWebAppSecurity().getAuthMethod();
      }
   }

   public boolean isSecure() {
      return this.connection.isSecure();
   }

   public boolean isUserInRole(String var1) {
      if (var1 == null) {
         return false;
      } else {
         AuthenticatedSubject var2 = SecurityModule.getCurrentUser(this.context.getServer(), this);
         return this.context.getSecurityManager().getWebAppSecurity().isSubjectInRole(var2, var1, this.getSecurityContextHandler(), this.sstub);
      }
   }

   public WebAppContextHandler getSecurityContextHandler() {
      if (this.securityContextHandler == null) {
         this.securityContextHandler = new WebAppContextHandler(this, this.response);
      }

      return this.securityContextHandler;
   }

   public Principal getUserPrincipal() {
      AuthenticatedSubject var1 = SecurityModule.getCurrentUser(this.context.getServer(), this);
      return var1 == null ? null : SubjectUtils.getUserPrincipal(var1);
   }

   public String getHeader(String var1) {
      return this.headers.getHeader(var1, this.getInputEncoding());
   }

   public int getIntHeader(String var1) {
      String var2 = this.getHeader(var1);
      return var2 == null ? -1 : Integer.parseInt(var2);
   }

   public long getDateHeader(String var1) {
      String var2 = this.getHeader(var1);
      if (var2 != null) {
         DateFormat[] var3 = ThreadLocalDateFormat.getInstance().getDateFormats();
         long var4 = CachingDateFormat.parseDate(StringUtils.upto(var2, ';'), var3);
         if (var4 != -1L) {
            return var4;
         } else {
            throw new IllegalArgumentException("Bad date header: '" + var2 + "'");
         }
      } else {
         return -1L;
      }
   }

   public Enumeration getHeaderNames() {
      return this.headers.getHeaderNames();
   }

   public Enumeration getHeaders(String var1) {
      return this.headers.getHeaders(var1, this.getInputEncoding());
   }

   public Locale getLocale() {
      if (this.locales == null) {
         this.initLocales();
      }

      return this.locales[0];
   }

   public Enumeration getLocales() {
      if (this.locales == null) {
         this.initLocales();
      }

      return new IteratorEnumerator(Arrays.asList((Object[])this.locales).iterator());
   }

   private void initLocales() {
      String var1 = this.headers.getAcceptLanguages();
      if (var1 != null && var1.trim().length() != 0) {
         String[] var2 = StringUtils.splitCompletely(var1, ",");
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            String[] var5 = StringUtils.split(var2[var4], ';');
            String var6 = null;
            String var7 = "";
            String var8 = "";
            int var9 = var5[0].indexOf(45);
            if (var9 < 0) {
               var6 = var5[0].trim();
            } else {
               var6 = var5[0].substring(0, var9).trim();
               var7 = var5[0].substring(var9 + 1).trim();
               int var10 = var7.indexOf(45);
               if (var10 > 0) {
                  var8 = var7.substring(var10 + 1).trim();
                  var7 = var7.substring(0, var10).trim();
               }
            }

            if (isValid(var6) && isValid(var7) && isValid(var8)) {
               Locale var11 = new Locale(var6, var7, var8);
               var3.add(var11);
            }
         }

         if (var3.isEmpty()) {
            this.locales = new Locale[1];
            this.locales[0] = Locale.getDefault();
         } else {
            this.locales = (Locale[])var3.toArray(new Locale[var3.size()]);
         }

      } else {
         this.locales = new Locale[1];
         this.locales[0] = Locale.getDefault();
      }
   }

   private static boolean isValid(String var0) {
      if (var0 != null && var0.length() != 0) {
         for(int var1 = 0; var1 < var0.length(); ++var1) {
            char var2 = var0.charAt(var1);
            if ((var2 < 'a' || var2 > 'z') && (var2 < 'A' || var2 > 'Z')) {
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public void setInputStream(ServletInputStream var1) {
      this.inputStream = var1;
   }

   private ServletInputStreamImpl getServletInputStreamImpl() {
      return (ServletInputStreamImpl)this.inputStream;
   }

   public ServletInputStream getInputStream() {
      if (this.useReader) {
         throw new IllegalStateException("getInputStream() called after getReader() called");
      } else {
         this.useInputStream = true;
         return this.inputStream;
      }
   }

   public BufferedReader getReader() throws UnsupportedEncodingException {
      if (this.useInputStream) {
         throw new IllegalStateException("getReader() called after getInputStream() called");
      } else {
         this.useReader = true;
         this.initReader();
         return this.bufferedReader;
      }
   }

   private void initReader() throws UnsupportedEncodingException {
      if (this.bufferedReader == null) {
         this.initRequestEncoding();
         this.bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream, this.getInputEncoding()));
      }
   }

   private void initRequestEncoding() throws UnsupportedEncodingException {
      if (!this.inputEncodingInitialized) {
         String var1 = this.findCharSetFromContentType();
         if (var1 != null && var1.length() != 0) {
            try {
               this.setCharacterEncodingInternal(var1);
            } catch (UnsupportedEncodingException var4) {
               String var3 = this.context == null ? "" : this.context.getLogContext();
               HTTPLogger.logUnsupportedEncoding(var3, var1, var4);
               throw var4;
            }
         }

         this.inputEncodingInitialized = true;
      }
   }

   private String findCharSetFromContentType() {
      String var1 = this.getContentType();
      if (var1 != null && var1.length() != 0) {
         int var2 = var1.length() - 1;
         int var3 = 0;

         while(var3 <= var2 && (var3 = var1.indexOf(59, var3)) != -1) {
            ++var3;

            while(var3 <= var2 && HttpParsing.isWS(var1.charAt(var3))) {
               ++var3;
            }

            if (var1.startsWith("charset", var3) || var1.startsWith("CHARSET", var3)) {
               int var4 = var1.indexOf(61, var3 + "charset".length());
               if (var4 != -1 && var4 < var2) {
                  int var6 = var1.indexOf(59, var4);
                  String var5;
                  if (var6 == -1) {
                     var5 = var1.substring(var4 + 1);
                  } else {
                     var5 = var1.substring(var4 + 1, var6);
                  }

                  return HttpParsing.StripHTTPFieldValue(var5.trim());
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   void initInputEncoding() {
      if (this.context != null) {
         HashMap var1 = this.context.getConfigManager().getInputEncodings();
         if (!var1.isEmpty()) {
            String var2 = this.getRelativeUri();

            String var3;
            while(true) {
               var3 = (String)var1.get(var2);
               if (var3 != null) {
                  break;
               }

               int var4 = var2.lastIndexOf(47);
               if (var4 <= 0) {
                  break;
               }

               var2 = var2.substring(0, var4);
            }

            if (var3 == null) {
               var3 = (String)var1.get("");
            }

            if (var3 != null) {
               if (!this.getInputEncoding().equals(this.context.getConfigManager().getCharsetMap().getJavaCharset(var3))) {
                  try {
                     this.setCharacterEncoding(var3);
                  } catch (UnsupportedEncodingException var6) {
                  }

               }
            }
         }
      }
   }

   public String getRelativeUri() {
      return this.relativeURI;
   }

   private String computeRelativeUri(String var1) {
      if (var1 != null && this.context != null && !this.context.isDefaultContext()) {
         int var2 = this.context.getContextPath().length();
         return var1.length() <= var2 ? "" : var1.substring(var2);
      } else {
         return var1;
      }
   }

   void setInputStream(InputStream var1) {
      this.inputStream = new ServletInputStreamImpl(var1);
   }

   static boolean eq(String var0, String var1, int var2) {
      return var0 == var1 || var1.regionMatches(true, 0, var0, 0, var2);
   }

   public String getScheme() {
      return this.connection.isSecure() ? "https" : "http";
   }

   public Cookie[] getCookies() {
      if (this.cookies == null && !this.cookiesParsed) {
         this.parseCookies();
      }

      return this.cookies;
   }

   private void parseCookies() {
      if (HTTPDebugLogger.isEnabled()) {
         this.trace("Parsing cookies");
      }

      this.cookiesParsed = true;
      ArrayList var1 = new ArrayList();
      List var2 = this.headers.getCookieHeaders();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         byte[] var4 = (byte[])((byte[])var3.next());
         String var5 = BytesToString.newString(var4, this.getInputEncoding());

         try {
            Iterators.addAll(var1, CookieParser.parseCookies(var5));
         } catch (MalformedCookieHeaderException var7) {
            HTTPLogger.logBadCookieHeader(this.getContext().getLogContext(), var7.getHeader(), this.headers.getUserAgent(), var7);
         }
      }

      if (var1.size() != 0) {
         this.cookies = (Cookie[])((Cookie[])var1.toArray(new Cookie[var1.size()]));
      }
   }

   public SessionHelper getSessionHelper() {
      return this.sessionHelper;
   }

   public HttpSession getSession() {
      return this.getSession(true);
   }

   public HttpSession getSession(boolean var1) {
      HttpSession var2 = this.sessionHelper.getSession(var1);
      this.checkAndSetDebugSessionFlag(var2);
      return var2;
   }

   public String getRequestedSessionId() {
      return this.sessionHelper.getRequestedSessionId();
   }

   public boolean isRequestedSessionIdFromCookie() {
      return this.sessionHelper.isRequestedSessionIdFromCookie();
   }

   public boolean isRequestedSessionIdFromURL() {
      return this.sessionHelper.isRequestedSessionIdFromURL();
   }

   public boolean isRequestedSessionIdFromUrl() {
      return this.sessionHelper.isRequestedSessionIdFromUrl();
   }

   public boolean isRequestedSessionIdValid() {
      return this.sessionHelper.isRequestedSessionIdValid();
   }

   private void trace(String var1) {
      HTTPDebugLogger.debug(this.toStringSimple() + ": " + var1);
   }

   private void trace(Exception var1, String var2) {
      HTTPDebugLogger.debug(this.toStringSimple() + ": " + var2, var1);
   }

   public ServerChannel getServerChannel() {
      return this.connection.getChannel();
   }

   public boolean isAdminChannelRequest() {
      return this.getConnection().isInternalDispatch() ? ApplicationVersionUtils.getCurrentAdminMode() : ChannelHelper.isAdminChannel(this.getServerChannel());
   }

   private final void checkAndSetDebugSessionFlag(HttpSession var1) {
      if (!HttpServer.isProductionModeEnabled() && var1 != null && var1 instanceof SessionData && !((SessionData)var1).isDebuggingSession() && this.parameters.peekQueryParameter("wl_debug_session") != null) {
         ((SessionData)var1).setDebugFlag(true);
      }

   }

   boolean validate(HttpServer var1) throws IOException {
      if (this.inputHelper.getRequestParser().isProtocolVersion_1_1() && this.headers.getHost() == null) {
         ((MuxableSocketHTTP)this.connection.getSocketRuntime()).sendError(this.response, 400);
         if (HTTPDebugLogger.isEnabled()) {
            this.trace("HOST header was missing from HTTP1.1 request");
         }

         return false;
      } else if (this.inputHelper.getNormalizedURI() == null) {
         ((MuxableSocketHTTP)this.connection.getSocketRuntime()).sendError(this.response, 404);
         return false;
      } else if (this.response.getContext() == null) {
         if (HTTPDebugLogger.isEnabled()) {
            Loggable var4 = HTTPLogger.logNoContextLoggable(var1.toString(), this.inputHelper.getNormalizedURI());
            HTTPDebugLogger.debug(var4.getMessage());
         }

         ((MuxableSocketHTTP)this.connection.getSocketRuntime()).sendError(this.response, 404);
         return false;
      } else if (!this.response.getContext().isStarted()) {
         ((MuxableSocketHTTP)this.connection.getSocketRuntime()).sendError(this.response, 503);
         return false;
      } else {
         int var2 = this.getContentLength();
         if (var2 > 0) {
            int var3 = var1.getMaxPostSize();
            if (var3 > 0 && var2 > var3) {
               HTTPLogger.logPOSTSizeExceeded(var3);
               ((MuxableSocketHTTP)this.connection.getSocketRuntime()).sendError(this.response, 413);
               return false;
            }
         }

         return true;
      }
   }

   void setCheckIndexFile(boolean var1) {
      this.checkIndexFile = var1;
   }

   public static ServletRequestImpl getOriginalRequest(ServletRequest var0) {
      if (var0 == null) {
         return null;
      } else {
         while(var0 instanceof ServletRequestWrapper) {
            var0 = ((ServletRequestWrapper)var0).getRequest();
         }

         if (var0 == null) {
            throw new AssertionError("Original request not available");
         } else {
            return (ServletRequestImpl)var0;
         }
      }
   }

   public VirtualConnection getConnection() {
      return this.connection;
   }

   public void run() {
      boolean var11;
      boolean var10000 = var11 = _WLDF$INST_FLD_Servlet_Request_Run_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var12 = null;
      DiagnosticActionState[] var13 = null;
      Object var10 = null;
      if (var10000) {
         Object[] var6 = null;
         if (_WLDF$INST_FLD_Servlet_Request_Run_Around_Medium.isArgumentsCaptureNeeded()) {
            var6 = new Object[]{this};
         }

         DynamicJoinPoint var34 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var6, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Servlet_Request_Run_Around_Medium;
         DiagnosticAction[] var10002 = var12 = var10001.getActions();
         InstrumentationSupport.preProcess(var34, var10001, var10002, var13 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         WebAppServletContext var1 = this.context;
         if (this.dispatchDueToRemoteSessionFetching()) {
            return;
         }

         try {
            this.underExecution = true;
            if (ServerHelper.useExtendedSessionFormat()) {
               weblogic.rmi.extensions.server.ServerHelper.setServerChannel(this.connection.getChannel());
            }

            if (this.context.getVersionId() != null && this.dispatchVersion()) {
               return;
            }

            MethodInvocationHelper.pushMethodObject((BeanInfo)null);
            this.context.getSecurityManager().getWebAppSecurity().initContextHandler(this);

            try {
               if (!this.context.getServer().getMBean().isKeepAliveEnabled()) {
                  this.response.disableKeepAlive();
               }

               this.initServerNameAndPort();
               this.connection.initCerts();
               if (this.checkIndexFile) {
                  ServletStubImpl var2 = this.context.getIndexServletStub(this.getRelativeUri(), this, this);
                  if (var2 != null) {
                     this.setServletStub(var2);
                  }

                  this.checkIndexFile = false;
               }

               if (this.performOverloadAction) {
                  this.sendOverLoadResponse(var1);
               } else {
                  this.context.execute(this, this.response);
               }
            } finally {
               MethodInvocationHelper.popMethodObject((BeanInfo)null);
               var1.getSecurityManager().getWebAppSecurity().resetContextHandler();
               if (!this.isFutureResponseEnabled() && !this.performOverloadAction) {
                  this.response.send();
               }

               this.performOverloadAction = false;
            }
         } catch (SocketException var30) {
            if (HTTPDebugLogger.isEnabled()) {
               HTTPLogger.logServletFailed(var1.getLogContext(), var30);
            }
         } catch (IOException var31) {
            HTTPLogger.logServletFailed(var1.getLogContext(), var31);
         } finally {
            if (ServerHelper.useExtendedSessionFormat()) {
               weblogic.rmi.extensions.server.ServerHelper.setServerChannel((ServerChannel)null);
            }

            this.underExecution = false;
         }
      } finally {
         if (var11) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_Request_Run_Around_Medium, var12, var13);
         }

      }

   }

   private void sendOverLoadResponse(WebAppServletContext var1) throws IOException {
      this.setAttribute("javax.servlet.error.message", this.overloadRejectionMessage);
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();

      try {
         Thread.currentThread().setContextClassLoader(var1.getServletClassLoader());
         this.response.sendError(this.context.getServer().getMBean().getOverloadResponseCode());
      } finally {
         this.context.getServer().getLogManager().log(this, this.response);
         Thread.currentThread().setContextClassLoader(var2);
      }

   }

   public Runnable overloadAction(String var1) {
      if (!this.context.isInternalApp() && !this.isAdminChannelRequest()) {
         this.performOverloadAction = true;
         this.overloadRejectionMessage = var1;
         return this;
      } else {
         return null;
      }
   }

   public Runnable cancel(String var1) {
      return this.underExecution ? null : this.overloadAction(var1);
   }

   protected AuthenticatedSubject getAuthenticatedSubject() {
      SessionContext var1 = this.context.getSessionContext();
      SessionInternal var2 = this.sessionHelper.getSessionOnThisServer(var1);
      if (var2 == null) {
         String var3 = this.sessionHelper.getRequestedSessionId(false);
         return var3 == null ? null : this.context.getServer().getSessionLogin().getUser(RSID.getID(var3));
      } else {
         return SecurityModule.getCurrentUser(this.context.getServer(), this, var2);
      }
   }

   public void send100ContinueResponse() throws IOException {
      OutputStream var1 = this.getConnection().getSocket().getOutputStream();
      var1.write(HttpConstants.SC_CONTINUE_RESPONSE, 0, HttpConstants.SC_CONTINUE_RESPONSE.length);
   }

   private void reInitContextIfNeeded(SessionInternal var1) {
      if (this.context == null || this.context.getVersionId() != null && !this.context.getVersionId().equals(var1.getVersionId())) {
         WebAppServletContext var2 = (WebAppServletContext)((HttpSession)var1).getServletContext();
         if (HTTPDebugLogger.isEnabled()) {
            this.trace("reInitContext from session for session=" + this.sessionHelper.getSessionID() + ", old version=" + this.context.getVersionId() + ", new ctx=" + var2);
         }

         this.initContext(var2);
         this.response.initContext(var2);
      }

   }

   private boolean dispatchDueToRemoteSessionFetching() {
      if (this.preventRedispatch) {
         return false;
      } else {
         WorkManager var1 = this.getServletStub().getWorkManagerForSessionFetching();
         if (HTTPDebugLogger.isEnabled()) {
            this.trace("[RemoteSessionFetching] obtained workManager: " + var1);
         }

         if (var1 == null) {
            return false;
         } else {
            SessionContext var2 = this.context.getSessionContext();
            if (HTTPDebugLogger.isEnabled()) {
               this.trace("[RemoteSessionFetching] obtained session context: " + var2);
            }

            if (!(var2 instanceof ReplicatedSessionContext)) {
               return false;
            } else {
               String var3 = this.sessionHelper.getRequestedSessionId(false);
               if (HTTPDebugLogger.isEnabled()) {
                  this.trace("[RemoteSessionFetching] obtained sessionId: " + var3);
               }

               if (((ReplicatedSessionContext)var2).isPrimaryOrSecondary(var3)) {
                  if (HTTPDebugLogger.isEnabled()) {
                     this.trace("[RemoteSessionFetching] session is primary or secondary !");
                  }

                  return false;
               } else {
                  if (HTTPDebugLogger.isEnabled()) {
                     this.trace("[RemoteSessionFetching] obtained sessionFetchingWorkManager and dispatching to " + var1);
                  }

                  this.preventRedispatch = true;
                  var1.schedule(this);
                  return true;
               }
            }
         }
      }
   }

   private boolean dispatchVersion() throws IOException {
      WebAppServletContext var1 = this.getContext();
      WebAppServletContext var2 = this.findVersionedContext();
      if (var2 != null && var2 != var1) {
         if (HTTPDebugLogger.isEnabled()) {
            this.trace("re-dispatch request to " + var2);
         }

         this.initContext(var2);
         this.response.initContext(var2);
         MuxableSocketHTTP var3 = (MuxableSocketHTTP)this.connection.getSocketRuntime();
         if (var3 != null && var3.initAndValidateRequest(var2)) {
            WorkManager var4 = this.getServletStub().getWorkManager();
            if (var4 == null) {
               throw new AssertionError("Could not determine WorkManager for : " + this);
            }

            var4.schedule(this);
         }

         return true;
      } else {
         if (HTTPDebugLogger.isEnabled()) {
            this.trace("dispatch request to " + var1);
         }

         return false;
      }
   }

   private WebAppServletContext findVersionedContext() {
      WebAppServletContext var1 = this.getContext();
      if (this.contextManager == null) {
         return var1;
      } else {
         String var2 = this.sessionHelper.getRequestedSessionId(false);
         if (var2 == null) {
            return var1;
         } else {
            WebAppServletContext var3 = this.contextManager.getContextForSession(var2);
            if (var3 == null) {
               if (HTTPDebugLogger.isEnabled()) {
                  this.trace("Finding Versioned ServletContext with Application Version Info for SessionID : " + var2);
               }

               String var4 = null;

               try {
                  var4 = var1.getSessionContext().lookupAppVersionIdForSession(var2, this, this.response);
               } catch (Throwable var10) {
                  HTTPLogger.logErrorWithThrowable(var1.getLogContext(), "Error Fetching Version Infomation for Application Version ", var10);
               }

               if (var4 == null) {
                  boolean var5 = this.isAdminChannelRequest();
                  Iterator var6 = this.contextManager.getServletContexts(var5);

                  while(var4 == null && var6.hasNext()) {
                     WebAppServletContext var7 = (WebAppServletContext)var6.next();
                     if (var7 != null && var7 != var1) {
                        try {
                           var4 = var7.getSessionContext().lookupAppVersionIdForSession(var2, this, this.getResponse());
                        } catch (Throwable var9) {
                           HTTPLogger.logErrorWithThrowable(var7.getLogContext(), "Error Fetching Version Infomation for Application Version ", var9);
                        }
                     }
                  }
               }

               if (var4 != null) {
                  var3 = this.contextManager.getContext(var4);
               }
            }

            return var3 == null ? var1 : var3;
         }
      }
   }

   HttpServletRequest copy() {
      if (this.sessionHelper.isClone) {
         throw new IllegalStateException("Cannot clone a request from a cloned request.");
      } else {
         ServletRequestImpl var1 = new ServletRequestImpl((MuxableSocketHTTP)this.connection.getSocketRuntime(), this.sessionHelper);
         var1.initFromRequestParser(this.getInputHelper().getRequestParser());
         if (this.getAttribute("javax.servlet.forward.request_uri") != null) {
            String var2 = this.getRequestURI();
            var1.initFromRequestURI(var2);
         }

         var1.setResponse(this.getResponse());
         if (this.contextManager != null && this.contextManager.isVersioned()) {
            var1.initContextManager(this.contextManager);
         } else {
            var1.initContext(this.context);
         }

         if (this.inputEncoding != null) {
            try {
               var1.setCharacterEncoding(this.inputEncoding);
            } catch (UnsupportedEncodingException var4) {
            }
         }

         var1.initLocales();
         var1.setInputStream(this.inputStream);
         var1.setServletStub(this.getServletStub());
         var1.useInputStream = this.useInputStream;
         var1.useReader = this.useReader;
         var1.setServerName(this.serverName);
         var1.setServerPort(this.serverPort);
         var1.servletPath = this.servletPath;
         var1.pathInfo = this.pathInfo;
         var1.setCheckIndexFile(this.checkIndexFile);
         if (this.getSendRedirect()) {
            var1.setRedirectURI(this.redirectLocation);
         }

         this.parameters.parseQueryParams(true);
         var1.parameters.setQueryParams(this.parameters.getQueryParams());
         var1.parameters.setExtraQueryParams(this.parameters.getExtraQueryParams());
         Iterator var5 = this.attributes.keys();

         while(var5.hasNext()) {
            String var3 = (String)var5.next();
            var1.attributes.put(var3, this.attributes.get(var3, this.context));
         }

         return var1;
      }
   }

   static {
      _WLDF$INST_FLD_Servlet_Request_Run_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Request_Run_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ServletRequestImpl.java", "weblogic.servlet.internal.ServletRequestImpl", "run", "()V", 1457, InstrumentationSupport.makeMap(new String[]{"Servlet_Request_Run_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("req", "weblogic.diagnostics.instrumentation.gathering.ServletRequestRenderer", false, true), (ValueHandlingInfo)null, (ValueHandlingInfo[])null)}), (boolean)0);
      isWLProxyHeadersAccessible = Boolean.getBoolean("weblogic.http.isWLProxyHeadersAccessible");
      isRemoteUserHeaderAccessible = Boolean.getBoolean("weblogic.http.enableRemoteUserHeader");
   }

   public static final class SessionHelper {
      private final ServletRequestImpl request;
      private HttpSession session;
      private final Map allSessions;
      private boolean sessionInitialized;
      private boolean fromCookie;
      private boolean fromURL;
      private boolean sessionExistanceChecked;
      private String sessionID;
      private String requestedSessionID;
      private String encodedSessionID;
      private final HashMap sessionIds;
      private final boolean isClone;

      private SessionHelper(ServletRequestImpl var1, SessionHelper var2) {
         this.request = var1;
         if (var2 != null) {
            this.allSessions = var2.allSessions;
            this.sessionIds = var2.sessionIds;
            this.isClone = true;
         } else {
            this.allSessions = new HashMap();
            this.sessionIds = new HashMap();
            this.isClone = false;
         }

      }

      private void reset() {
         this.sessionInitialized = false;
         this.sessionExistanceChecked = false;
         this.fromCookie = false;
         this.fromURL = false;
         this.sessionIds.clear();
         this.session = null;
         this.allSessions.clear();
         this.encodedSessionID = null;
         this.sessionID = null;
         this.requestedSessionID = null;
      }

      private String getSessionIDFromMap(String var1, String var2) {
         return (String)this.sessionIds.get(new CookieKey(var1, var2));
      }

      void rememberSessionID(String var1, String var2, String var3) {
         this.sessionIds.put(new CookieKey(var1, var2), var3);
      }

      void setSession(HttpSession var1) {
         this.session = var1;
         this.sessionInitialized = true;
      }

      public String getEncodedSessionID() {
         return this.encodedSessionID;
      }

      boolean getSessionExistanceChecked() {
         return this.sessionExistanceChecked;
      }

      void setSessionExistanceChecked(boolean var1) {
         this.sessionExistanceChecked = var1;
      }

      public HttpSession getSession() {
         return this.getSession(true);
      }

      public HttpSession getSession(boolean var1) {
         if (!this.isClone) {
            return this.getSessionInternal(var1);
         } else {
            synchronized(this.allSessions) {
               return this.getSessionInternal(var1);
            }
         }
      }

      private HttpSession getSessionInternal(boolean var1) {
         if (!this.sessionInitialized) {
            this.initSessionInfo();
         }

         if (this.session != null) {
            if (((SessionInternal)this.session).isValid()) {
               return this.session;
            }

            this.session = null;
         }

         this.session = (HttpSession)this.allSessions.get(this.request.getContext().getContextPath());
         if (this.session != null) {
            if (((SessionInternal)this.session).isValid()) {
               return this.session;
            }

            this.allSessions.remove(this.request.getContext().getContextPath());
            this.session = null;
         }

         if (!var1 && this.sessionExistanceChecked) {
            return null;
         } else {
            this.sessionExistanceChecked = true;
            String var2 = this.getSessionIDFromMap(this.request.getContext().getSessionContext().getConfigMgr().getCookieName(), this.request.getContext().getSessionContext().getConfigMgr().getCookiePath());
            if (var2 == null) {
               var2 = this.requestedSessionID;
            }

            if (var2 == null) {
               if (!var1) {
                  return null;
               }

               this.getNewSession((String)null);
            } else {
               this.session = this.getValidSession(var2);
               if (this.session == null) {
                  if (!var1) {
                     return null;
                  }

                  this.getNewSession(var2);
                  if (this.session == null) {
                     return null;
                  }
               }

               if (this.sessionID.length() != this.request.getContext().getSessionContext().getConfigMgr().getIDLength()) {
                  this.updateSessionId();
               }
            }

            this.storeSessionInAllSessions(this.request.getContext().getContextPath(), this.session);
            return this.session;
         }
      }

      public void killOldSession() {
         Iterator var1 = this.allSessions.values().iterator();

         while(var1.hasNext()) {
            HttpSession var2 = (HttpSession)var1.next();
            if (((SessionInternal)var2).isValid()) {
               var2.invalidate();
            }
         }

         String var4 = this.request.getContext().getSessionContext().getConfigMgr().getCookieName();
         String var3 = this.request.getContext().getSessionContext().getConfigMgr().getCookiePath();
         this.invalidateCookie(var4, var3);
         if (this.request.getContext().getServer().isAuthCookieEnabled()) {
            this.invalidateCookie(SecurityModule.getWLSAuthCookieName((SessionInternal)this.session), var3);
         }

         this.allSessions.clear();
         this.session = null;
         this.sessionID = null;
         this.requestedSessionID = null;
      }

      void invalidateCookie(String var1, String var2) {
         Cookie var3 = this.request.getResponse().getCookie(var1);
         if (var3 != null) {
            var3.setValue("");
            var3.setMaxAge(0);
         } else {
            var3 = new Cookie(var1, "");
            var3.setValue("");
            var3.setMaxAge(0);
            String var4 = this.request.getContext().getSessionContext().getConfigMgr().getCookieDomain();
            if (var4 != null) {
               var3.setDomain(var4);
            }

            var3.setPath(var2);
            if (this.request.getContext().getSessionContext().getConfigMgr().isCookieSecure()) {
               var3.setSecure(true);
            }

            this.request.getResponse().addCookie(var3);
         }

      }

      String getIncomingSessionCookieValue() {
         return this.requestedSessionID;
      }

      public String getRequestedSessionId() {
         return this.getRequestedSessionId(true);
      }

      private String getRequestedSessionId(boolean var1) {
         if (!this.sessionInitialized) {
            this.initSessionInfo();
         }

         if (this.requestedSessionID != null && var1) {
            HttpSession var2 = this.getSession(false);

            try {
               if (var2 != null && !var2.isNew()) {
                  return var2.getId();
               }
            } catch (IllegalStateException var5) {
               SessionInternal var4 = (SessionInternal)var2;
               if (var4.isValid()) {
                  throw var5;
               }
            }
         }

         return this.requestedSessionID;
      }

      String getSessionID() {
         return this.sessionID;
      }

      public boolean isRequestedSessionIdFromCookie() {
         if (!this.sessionInitialized) {
            this.initSessionInfo();
         }

         return this.fromCookie;
      }

      public boolean isRequestedSessionIdFromURL() {
         if (!this.sessionInitialized) {
            this.initSessionInfo();
         }

         return this.fromURL;
      }

      public boolean isRequestedSessionIdFromUrl() {
         return this.isRequestedSessionIdFromURL();
      }

      public boolean isRequestedSessionIdValid() {
         if (this.requestedSessionID == null) {
            return false;
         } else {
            if (!this.sessionInitialized) {
               this.initSessionInfo();
            }

            SessionInternal var1 = (SessionInternal)this.getSession(false);
            if (var1 == null) {
               return false;
            } else {
               String var2 = RSID.getID(this.requestedSessionID);
               String var3 = var1.getInternalId();
               return var2.equals(var3);
            }
         }
      }

      private boolean getSessionIdFromCookieHeaders(String var1) {
         List var2 = this.request.getRequestHeaders().getCookieHeaders();
         if (var2.size() == 0) {
            return false;
         } else {
            int var3 = var1.length();
            byte var4 = (byte)var1.charAt(0);

            for(int var5 = var2.size() - 1; var5 > -1; --var5) {
               byte[] var6 = (byte[])((byte[])var2.get(var5));
               int var7 = var6.length;
               int var8 = var7 - var3;

               for(int var9 = 0; var9 < var8; ++var9) {
                  if (var6[var9] != var4) {
                     if (var6[var9] != 32) {
                        while(var9 < var8 && var6[var9] != 59 && var6[var9] != 44) {
                           ++var9;
                        }
                     }
                  } else {
                     int var10;
                     for(var10 = 1; var10 < var3 && var6[var9 + var10] == (byte)var1.charAt(var10); ++var10) {
                     }

                     if (var10 >= var3 && var6[var9 + var10] == 61) {
                        var9 += var10;

                        boolean var11;
                        for(var11 = false; var9 < var7 && var6[var9] == 32; ++var9) {
                        }

                        while(var9 < var7 && var6[var9] == 61) {
                           ++var9;
                        }

                        while(var9 < var7 && var6[var9] == 32) {
                           ++var9;
                        }

                        while(var9 < var7 && var6[var9] == 34) {
                           ++var9;
                        }

                        for(var10 = 0; var9 + var10 < var7; ++var10) {
                           byte var12 = var6[var9 + var10];
                           if (var12 == 59 || var12 == 44 || var12 == 34) {
                              var11 = true;
                              break;
                           }
                        }

                        if (var11) {
                           this.requestedSessionID = StringUtils.getString(var6, var9, var10);
                        } else {
                           this.requestedSessionID = StringUtils.getString(var6, var9, var7 - var9);
                        }

                        return true;
                     }
                  }
               }
            }

            return false;
         }
      }

      private void initSessionInfo() {
         if (!this.sessionInitialized) {
            WebAppServletContext var1 = this.request.getContext();
            if (var1 != null) {
               this.initSessionInfoWithContext(var1);
            } else {
               boolean var2 = this.request.isAdminChannelRequest();
               WebAppServletContext var3 = this.request.contextManager.getActiveContext(var2);
               if (var3 != null && var3.getVersionId() == null) {
                  this.initSessionInfoWithContext(var3);
               } else {
                  WebAppServletContext var4 = null;
                  if (var3 == null || !this.initSessionInfoWithContext(var3)) {
                     Iterator var5 = this.request.contextManager.getServletContexts(var2);

                     while(var5.hasNext()) {
                        WebAppServletContext var6 = (WebAppServletContext)var5.next();
                        if (var6 != var3 && this.initSessionInfoWithContext(var6)) {
                           var4 = var6;
                           break;
                        }
                     }
                  }

                  if (this.sessionID != null) {
                     WebAppServletContext var7 = this.request.contextManager.getContextForSession(this.sessionID);
                     this.request.initContext(var7);
                     if (HTTPDebugLogger.isEnabled() && var7 != null) {
                        this.request.trace("initContext from ctxManager lookup for sessionID=" + this.requestedSessionID + ", context=" + var7);
                     }
                  }

                  if (this.request.context == null) {
                     if (var3 != null) {
                        this.request.initContext(var3);
                        if (HTTPDebugLogger.isEnabled()) {
                           this.request.trace("initContext with active context=" + var3);
                        }
                     } else {
                        this.request.initContext(var4);
                        if (HTTPDebugLogger.isEnabled()) {
                           this.request.trace("initContext with sessionId context=" + var4);
                        }
                     }
                  }
               }
            }

            this.sessionInitialized = true;
         }
      }

      private boolean initSessionInfoWithContext(WebAppServletContext var1) {
         if (!var1.getSessionContext().getConfigMgr().isSessionTrackingEnabled()) {
            return false;
         } else {
            String var2 = var1.getSessionContext().getConfigMgr().getCookieName();
            if (var2 == null) {
               if (HTTPDebugLogger.isEnabled()) {
                  this.request.trace("session-cookie-name is set to null");
               }

               return false;
            } else {
               if (var1.getSessionContext().getConfigMgr().isSessionCookiesEnabled()) {
                  if (this.request.cookiesParsed) {
                     this.getSessionIdFromParsedCookies(var2);
                  } else if (this.getSessionIdFromCookieHeaders(var2)) {
                     if (HTTPDebugLogger.isEnabled()) {
                        this.request.trace("SessionID: " + this.requestedSessionID + " found in cookie header");
                     }

                     this.fromCookie = true;
                  }
               }

               if (this.requestedSessionID == null && this.isUrlRewritingEnabled(var1)) {
                  this.getSessionIdFromEncodedURL(var2);
                  if (this.requestedSessionID == null) {
                     this.getSessionIdFromQueryParams(var2);
                  }

                  if (this.requestedSessionID == null) {
                     this.getSessionIdFromPostData(var2);
                  }
               }

               if (this.requestedSessionID != null) {
                  this.sessionID = RSID.getID(this.requestedSessionID);
                  if (HTTPDebugLogger.isEnabled()) {
                     this.request.trace("SessionID= " + this.sessionID + " found for WASC=" + var1);
                  }
               } else if (HTTPDebugLogger.isEnabled()) {
                  this.request.trace("SessionID not found for WASC=" + var1);
               }

               return this.requestedSessionID != null;
            }
         }
      }

      private boolean isUrlRewritingEnabled(WebAppServletContext var1) {
         return var1.getSessionContext().getConfigMgr().isUrlRewritingEnabled();
      }

      private void getSessionIdFromQueryParams(String var1) {
         this.requestedSessionID = this.request.getRequestParameters().peekQueryParameter(var1);
         if (this.requestedSessionID != null) {
            if (HTTPDebugLogger.isEnabled()) {
               this.request.trace("SessionID: " + this.requestedSessionID + " found in query params or URL");
            }

            this.fromURL = true;
         }

      }

      private void getSessionIdFromEncodedURL(String var1) {
         String var2 = this.request.getInputHelper().getPathParameters();
         if (var2 != null) {
            int var3 = var2.toLowerCase().indexOf(";" + var1.toLowerCase() + "=");
            if (var3 >= 0) {
               this.encodedSessionID = var2.substring(var3 + var1.length() + 2);
               var3 = this.encodedSessionID.indexOf(59);
               if (var3 != -1) {
                  this.encodedSessionID = this.encodedSessionID.substring(0, var3);
               }

               this.requestedSessionID = this.encodedSessionID;
               if (HTTPDebugLogger.isEnabled()) {
                  this.request.trace("SessionID: " + this.requestedSessionID + " found encoded with the URL");
               }

               this.fromURL = true;
            }
         }
      }

      private void getSessionIdFromParsedCookies(String var1) {
         Cookie[] var2 = this.request.getCookies();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               if (var1.equalsIgnoreCase(var2[var3].getName())) {
                  this.requestedSessionID = var2[var3].getValue();
                  if (HTTPDebugLogger.isEnabled()) {
                     this.request.trace("SessionID: " + this.requestedSessionID + " found in cookie");
                  }

                  this.fromCookie = true;
                  break;
               }
            }

         }
      }

      private void getSessionIdFromPostData(String var1) {
         this.requestedSessionID = this.request.getRequestParameters().peekPostParameter(var1);
         if (this.requestedSessionID != null) {
            if (HTTPDebugLogger.isEnabled()) {
               this.request.trace("SessionID: " + this.requestedSessionID + " found in post data");
            }

            this.fromURL = true;
         }

      }

      void syncSession() {
         if (this.request.getContext() != null) {
            this.request.getContext().getServer().getWorkContextManager().copyThreadContexts(this.request.getContext(), this.request);
         }

         Iterator var1 = this.allSessions.values().iterator();

         while(var1.hasNext()) {
            HttpSession var2 = (HttpSession)var1.next();
            this.syncSession(var2, this.request.getHttpAccountingInfo().getInvokeTime());
         }

         this.session = null;
         this.allSessions.clear();
         this.sessionInitialized = false;
      }

      private void syncSession(HttpSession var1, long var2) {
         if (var1 instanceof SharedSessionData) {
            var1 = ((SharedSessionData)var1).getSession();
         }

         SessionInternal var4 = (SessionInternal)var1;
         if (var4.isValid()) {
            SessionContext var5 = var4.getContext();
            if (!var5.getConfigMgr().isSessionTrackingEnabled()) {
               var1.invalidate();
            } else {
               try {
                  if (var1.isNew() || var4.getLAT() < var2) {
                     var4.setLastAccessedTime(var2);
                  }

                  var4.setNew(false);
                  var5.sync(var1);
               } catch (IllegalStateException var7) {
                  if (var4.isValid()) {
                     throw var7;
                  }
               }

            }
         }
      }

      private void getNewSession(String var1) {
         if (HTTPDebugLogger.isEnabled()) {
            this.request.trace("Creating new session");
         }

         this.session = this.request.getContext().getSessionContext().getNewSession(var1, this.request, this.request.getResponse());
         if (this.session == null) {
            this.sessionID = null;
            HTTPLogger.logSessionCreateError(this.request.getContext().getLogContext());
         } else {
            this.sessionID = ((SessionInternal)this.session).getInternalId();
            if (HTTPDebugLogger.isEnabled()) {
               this.request.trace("New Session: " + this.sessionID);
            }

            this.request.getResponse().setSessionCookie(this.session);
            this.request.getContext().getServer().getSessionLogin().register(((SessionInternal)this.session).getInternalId(), this.request.getContext().getContextPath());
         }

      }

      private HttpSession getValidSession(String var1) {
         if (var1 != null && var1.length() >= 1) {
            if (HTTPDebugLogger.isEnabled()) {
               this.request.trace("Trying to find session: " + var1);
            }

            Object var2;
            try {
               var2 = this.request.getContext().getSessionContext().getSessionInternal(var1, this.request, this.request.getResponse());
            } catch (IllegalStateException var4) {
               if (HTTPDebugLogger.isEnabled()) {
                  this.request.trace(var4, "Exception finding session for id: " + var1);
               }

               var2 = null;
            }

            if (var2 == null) {
               if (HTTPDebugLogger.isEnabled()) {
                  this.request.trace("Trying other contexts to find valid session for id: " + var1);
               }

               var2 = this.request.getContext().getSessionContext().getSessionFromOtherContexts(var1, this.request, this.request.getResponse());
            }

            if (var2 == null) {
               var2 = this.request.getContext().getSessionContext().getSharedSession(var1, this.request, this.request.getResponse());
            }

            if (var2 == null) {
               if (HTTPDebugLogger.isEnabled()) {
                  this.request.trace("Couldn't find valid session for id: " + var1);
               }

               return null;
            } else {
               this.request.reInitContextIfNeeded((SessionInternal)var2);
               this.request.getResponse().setSessionCookie((HttpSession)var2);
               return (HttpSession)var2;
            }
         } else {
            return null;
         }
      }

      void resetSession(boolean var1) {
         this.session = null;
         if (var1) {
            this.sessionInitialized = false;
         }

         this.sessionExistanceChecked = false;
      }

      public void updateSessionId() {
         Object var1 = null;
         HttpSession var2 = this.session;
         String var3 = this.sessionID;
         this.getNewSession((String)null);
         WebAppServletContext[] var4 = this.request.getContext().getServer().getServletContextManager().getAllContexts();
         boolean var7 = false;
         SessionContext var8 = ((SessionInternal)var2).getContext();
         if (var8 != null) {
            var7 = var8.getConfigMgr().isSessionSharingEnabled();
         }

         for(int var9 = 0; var9 < var4.length; ++var9) {
            SessionContext var5;
            if ((var5 = var4[var9].getSessionContext()) != null && (this.request.context == var4[var9] || !var7 || this.request.context.getApplicationContext() != var4[var9].getApplicationContext()) && (this.request.context == var4[var9] || (var1 = var5.getSessionInternal(var3, (ServletRequestImpl)null, (ServletResponseImpl)null)) != null)) {
               HttpSession var6;
               if (this.request.context == var4[var9]) {
                  var6 = this.session;
                  var1 = var2;
               } else {
                  var6 = var5.getNewSession(this.sessionID, this.request, this.request.getResponse());
                  var4[var9].getServer().getSessionLogin().register(((SessionInternal)var6).getInternalId(), var4[var9].getContextPath());
               }

               var6.setMaxInactiveInterval(((HttpSession)var1).getMaxInactiveInterval());
               Enumeration var10 = ((HttpSession)var1).getAttributeNames();

               while(var10.hasMoreElements()) {
                  String var11 = (String)var10.nextElement();
                  ((SessionInternal)var6).setAttribute(var11, ((HttpSession)var1).getAttribute(var11), false);
               }

               Enumeration var13 = ((SessionInternal)var1).getInternalAttributeNames();

               while(var13.hasMoreElements()) {
                  String var12 = (String)var13.nextElement();
                  ((SessionInternal)var6).setInternalAttribute(var12, ((SessionInternal)var1).getInternalAttribute(var12));
               }

               this.storeSessionInAllSessions(var4[var9].getContextPath(), var6);
               ((SessionInternal)var1).invalidate(false);
            }
         }

      }

      SessionInternal getSessionOnThisServer(SessionContext var1) {
         String var2 = this.getSessionIDFromMap(var1.getConfigMgr().getCookieName(), var1.getConfigMgr().getCookiePath());
         if (var2 == null) {
            var2 = this.requestedSessionID;
         }

         return var2 == null ? null : var1.getSessionInternal(var2, (ServletRequestImpl)null, (ServletResponseImpl)null);
      }

      private void storeSessionInAllSessions(String var1, HttpSession var2) {
         if (var2 == null) {
            this.allSessions.remove(var1);
         } else {
            this.allSessions.put(var1, var2);
         }

      }

      // $FF: synthetic method
      SessionHelper(ServletRequestImpl var1, SessionHelper var2, Object var3) {
         this(var1, var2);
      }
   }

   public static final class RequestInputHelper {
      private HttpRequestParser parser = new HttpRequestParser();
      private String originalURI;
      private String normalizedURI;
      private String pathParameters;
      private boolean pathParamsParsed;

      private void reset() {
         this.normalizedURI = null;
         this.pathParamsParsed = false;
         this.pathParameters = null;
         this.originalURI = null;
         this.parser.reset();
      }

      void restore() {
         this.originalURI = this.parser.getRequestURI();
         this.pathParameters = this.parser.getPathParameters();
         this.pathParamsParsed = false;
         this.normalizedURI = this.parser.getNormalizedURI();
      }

      void initFromRequestParser(HttpRequestParser var1, RequestParameters var2) {
         this.parser = var1;
         this.originalURI = this.parser.getRequestURI();
         this.pathParameters = this.parser.getPathParameters();
         this.pathParamsParsed = false;
         this.normalizedURI = this.parser.getNormalizedURI();
         var2.queryStringBuffer = this.parser.getHttpRequestBuffer();
         var2.queryStringStart = this.parser.getQeuryStringStart();
         var2.queryStringLength = this.parser.getQueryStringLength();
      }

      void initFromRequestURI(String var1) {
         assert var1.indexOf("://") == -1 : "passed in uri should not have scheme";

         assert var1.indexOf(63) == -1 : "passed in uri should not have request parameter";

         if (var1.length() == 0) {
            var1 = "/";
         }

         int var2 = var1.indexOf(59);
         if (var2 > 0) {
            this.originalURI = var1.substring(0, var2);
            this.pathParameters = var1.substring(var2);
            this.pathParamsParsed = false;
         } else {
            this.originalURI = var1;
         }

         this.normalizedURI = HttpParsing.unescape(this.originalURI, HttpRequestParser.getURIDecodeEncoding());
         this.normalizedURI = FilenameEncoder.resolveRelativeURIPath(this.normalizedURI, true);
         if (this.normalizedURI == null) {
            throw new IllegalArgumentException("Unsafe path for the incoming request");
         }
      }

      public HttpRequestParser getRequestParser() {
         return this.parser;
      }

      public String getRequestURI(String var1) {
         if (this.pathParameters != null && !this.pathParamsParsed) {
            if (var1 != null) {
               int var2 = this.pathParameters.toLowerCase().indexOf(";" + var1.toLowerCase() + "=");
               if (var2 != -1) {
                  this.originalURI = this.originalURI + this.pathParameters.substring(0, var2);
                  String var3 = this.pathParameters.substring(var2 + 1);
                  var2 = var3.indexOf(59);
                  if (var2 != -1) {
                     this.originalURI = this.originalURI + var3.substring(var2);
                  }
               } else {
                  this.originalURI = this.originalURI + this.pathParameters;
               }
            } else {
               this.originalURI = this.originalURI + this.pathParameters;
            }

            this.pathParamsParsed = true;
         }

         return this.originalURI;
      }

      public String getNormalizedURI() {
         return this.normalizedURI;
      }

      private String getOriginalURI() {
         return this.originalURI;
      }

      public String getPathParameters() {
         return this.pathParameters;
      }
   }

   public static final class RequestParameters {
      private final ServletRequestImpl request;
      private byte[] postData;
      private boolean postDataRead;
      private boolean postParamsParsed;
      private boolean queryParamsParsed;
      private final QueryParams queryParameters = new QueryParams();
      private List extraParameters = new ArrayList();
      private byte[] queryStringBuffer = null;
      private int queryStringStart = -1;
      private int queryStringLength = -1;

      public RequestParameters(ServletRequestImpl var1) {
         this.request = var1;
      }

      public void reset() {
         this.postParamsParsed = false;
         this.queryParamsParsed = false;
         this.postDataRead = false;
         this.postData = null;
         this.queryStringBuffer = null;
         this.queryStringStart = -1;
         this.queryStringLength = -1;
         this.queryParameters.clear();
         this.extraParameters.clear();
      }

      public String getQueryString(String var1) {
         StringBuilder var2 = null;
         int var3 = this.extraParameters.size();
         if (var3 > 0) {
            var2 = new StringBuilder();

            for(int var4 = 0; var4 < var3; ++var4) {
               String var5 = this.extraParameters.get(var4).toString();
               if (var5 != null && this.preferFowardQueryString() && !(this.extraParameters.get(var4) instanceof ExtraIncludeParams)) {
                  return var5;
               }

               if (var5.length() != 0) {
                  var2.append(var5);
                  if (var4 < var3 - 1 && this.extraParameters.get(var4 + 1).toString().length() > 0) {
                     var2.append('&');
                  }
               }
            }
         }

         if (this.queryStringBuffer != null && this.queryStringStart != -1 && this.queryStringLength != -1) {
            String var6 = BytesToString.newString(this.queryStringBuffer, this.queryStringStart, this.queryStringLength, var1);
            return var2 != null && var2.length() != 0 ? var2.append('&').append(var6).toString() : var6;
         } else {
            return var2 != null && var2.length() != 0 ? var2.toString() : null;
         }
      }

      private boolean preferFowardQueryString() {
         return this.request.getContext() != null && this.request.getContext().getConfigManager().isPreferForwardQueryString();
      }

      String getOriginalQueryString(String var1) {
         return this.queryStringStart != -1 && this.queryStringLength != -1 ? BytesToString.newString(this.queryStringBuffer, this.queryStringStart, this.queryStringLength, var1) : null;
      }

      private void addForwardQueryString(String var1) {
         this.extraParameters.add(0, new ExtraParams(var1));
      }

      private void addIncludeQueryString(String var1) {
         this.extraParameters.add(0, new ExtraIncludeParams(var1));
      }

      private void removeRequestDispatcherQueryString() {
         this.extraParameters.remove(0);
      }

      public QueryParams getQueryParams() {
         this.parseQueryParams(false);
         return this.queryParameters;
      }

      public List getExtraQueryParams() {
         this.parseExtraQueryParams();
         return this.extraParameters;
      }

      public void setQueryParams(QueryParams var1) {
         this.queryParameters.clear();
         if (var1 != null && !var1.isEmpty()) {
            Iterator var2 = var1.keySet().iterator();

            while(true) {
               String var3;
               String[] var4;
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  var3 = (String)var2.next();
                  var4 = var1.getValues(var3);
               } while(var4 == null);

               for(int var5 = 0; var5 < var4.length; ++var5) {
                  this.queryParameters.put(var3, var4[var5]);
               }
            }
         }
      }

      public void setExtraQueryParams(List var1) {
         this.extraParameters.clear();
         if (var1 != null && !var1.isEmpty()) {
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               ExtraParams var3 = (ExtraParams)var2.next();
               this.extraParameters.add(0, var3.clone());
            }

         }
      }

      private Enumeration getParameterNames() {
         List var1 = this.getExtraQueryParams();
         if (var1.size() == 0) {
            return new IteratorEnumerator(this.getQueryParams().keySet().iterator());
         } else {
            HashSet var2 = new HashSet();
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               ExtraParams var4 = (ExtraParams)var3.next();
               var2.addAll(var4.params.keySet());
            }

            var2.addAll(this.getQueryParams().keySet());
            return new IteratorEnumerator(var2.iterator());
         }
      }

      private String[] getParameterValues(String var1) {
         List var2 = this.getExtraQueryParams();
         if (var2.size() == 0) {
            return this.getQueryParams().getValues(var1);
         } else {
            ArrayList var3 = new ArrayList();
            Iterator var4 = var2.iterator();

            while(var4.hasNext()) {
               ExtraParams var5 = (ExtraParams)var4.next();
               QueryParams var6 = var5.params;
               String[] var7 = var6.getValues(var1);
               if (var7 != null) {
                  var3.addAll(Arrays.asList(var7));
               }
            }

            String[] var8 = this.getQueryParams().getValues(var1);
            if (var8 != null) {
               var3.addAll(Arrays.asList(var8));
            }

            return var3.size() != 0 ? (String[])((String[])var3.toArray(new String[var3.size()])) : null;
         }
      }

      private String getParameter(String var1) {
         String var2 = this.getExtraParameter(var1);
         return var2 != null ? var2 : this.getQueryParams().getValue(var1);
      }

      private String getExtraParameter(String var1) {
         List var2 = this.getExtraQueryParams();
         Iterator var3 = var2.iterator();

         String var6;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            ExtraParams var4 = (ExtraParams)var3.next();
            QueryParams var5 = var4.params;
            var6 = var5.getValue(var1);
         } while(var6 == null);

         return var6;
      }

      void resetQueryParams() {
         this.queryParamsParsed = false;
         this.postParamsParsed = false;
         this.queryParameters.clear();
         Iterator var1 = this.extraParameters.iterator();

         while(var1.hasNext()) {
            ExtraParams var2 = (ExtraParams)var1.next();
            var2.clear();
         }

      }

      private void parseQueryParams(boolean var1) {
         if (!this.queryParamsParsed) {
            this.mergeQueryParams();
         }

         if (!this.postParamsParsed) {
            this.mergePostParams(var1);
         }

      }

      private void parseExtraQueryParams() {
         Iterator var1 = this.extraParameters.iterator();

         while(var1.hasNext()) {
            ExtraParams var2 = (ExtraParams)var1.next();
            var2.parseQueryParams(this.request.getQueryParamsEncoding());
         }

      }

      private void mergeQueryParams() {
         if (!this.queryParamsParsed) {
            this.queryParamsParsed = true;
            if (this.queryStringBuffer != null && this.queryStringStart != -1 && this.queryStringLength != -1) {
               HttpParsing.parseQueryString(this.queryStringBuffer, this.queryStringStart, this.queryStringLength, this.queryParameters, this.request.getQueryParamsEncoding());
            }

         }
      }

      private void mergePostParams(boolean var1) {
         if (!this.postParamsParsed) {
            this.postParamsParsed = true;
            String var2 = this.request.getContentType();
            if (this.request.inputHelper.getRequestParser().isMethodPost() && var2 != null && var2.startsWith("application/x-www-form-urlencoded")) {
               if (this.postDataRead && this.postData != null) {
                  HttpParsing.parseQueryString(this.postData, 0, this.postData.length, this.queryParameters, this.request.getInputEncoding());
               } else if (this.request.inputStream != null) {
                  if (((ServletInputStreamImpl)this.request.inputStream).getBytesRead() > 0) {
                     if (!var1) {
                        HTTPLogger.logInvalidGetParameterInvocation(this.request.getContext().getLogContext());
                     }
                  } else {
                     int var3 = this.request.getContentLength();
                     boolean var4 = this.request.getRequestHeaders().isChunked();
                     if (var4) {
                        var3 = 8192;
                     }

                     if (var3 >= 1) {
                        this.postData = new byte[var3];

                        try {
                           if (var1) {
                              this.request.inputStream.mark(var3);
                           }

                           this.request.initRequestEncoding();

                           int var5;
                           int var6;
                           for(var5 = 0; var5 < var3; var5 += var6) {
                              var6 = this.request.inputStream.read(this.postData, var5, var3 - var5);
                              if (var4 && var6 == -1) {
                                 break;
                              }

                              Debug.assertion(var6 > -1);
                           }

                           if (var4 && var5 < this.postData.length) {
                              byte[] var8 = new byte[var5];
                              System.arraycopy(this.postData, 0, var8, 0, var5);
                              this.postData = var8;
                           }

                           if (var1) {
                              this.request.inputStream.reset();
                           }

                           this.mergeQueryParams();
                           this.postDataRead = true;
                           HttpParsing.parseQueryString(this.postData, 0, this.postData.length, this.queryParameters, this.request.getInputEncoding());
                           this.postParamsParsed = true;
                        } catch (IOException var7) {
                           throw new NestedRuntimeException("Cannot parse POST parameters of request: '" + this.request.getRequestURI() + "'", var7);
                        }
                     }
                  }
               }
            }
         }
      }

      private String peekQueryParameter(String var1) {
         boolean var2 = this.postParamsParsed;
         this.postParamsParsed = true;

         String var3;
         try {
            var3 = this.peekParameter(var1);
         } finally {
            this.postParamsParsed = var2;
         }

         return var3;
      }

      private String peekPostParameter(String var1) {
         boolean var2 = this.queryParamsParsed;
         this.queryParamsParsed = true;

         String var3;
         try {
            var3 = this.peekParameter(var1);
         } finally {
            this.queryParamsParsed = var2;
         }

         return var3;
      }

      String peekParameter(String var1) {
         String var2 = this.getExtraParameter(var1);
         if (var2 != null) {
            return var2;
         } else {
            this.parseQueryParams(true);
            return this.queryParameters.getValue(var1);
         }
      }
   }

   private static class ExtraIncludeParams extends ExtraParams {
      private ExtraIncludeParams() {
         super((<undefinedtype>)null);
      }

      public ExtraIncludeParams(String var1) {
         super(var1);
      }

      public String toString() {
         return "";
      }

      protected Object clone() {
         ExtraIncludeParams var1 = new ExtraIncludeParams();
         var1.parsed = this.parsed;
         var1.str = this.str;
         var1.params = (QueryParams)this.params.clone();
         return var1;
      }
   }

   private static class ExtraParams {
      protected String str;
      protected boolean parsed;
      protected QueryParams params;

      private ExtraParams() {
      }

      public ExtraParams(String var1) {
         this.str = var1;
         this.parsed = false;
         this.params = new QueryParams();
      }

      public void parseQueryParams(String var1) {
         if (!this.parsed) {
            HttpParsing.parseQueryString(this.str, this.params, var1);
            this.parsed = true;
         }

      }

      public void clear() {
         this.parsed = false;
         this.params.clear();
      }

      public String toString() {
         return this.str;
      }

      protected Object clone() {
         ExtraParams var1 = new ExtraParams();
         var1.parsed = this.parsed;
         var1.str = this.str;
         var1.params = (QueryParams)this.params.clone();
         return var1;
      }

      // $FF: synthetic method
      ExtraParams(Object var1) {
         this();
      }
   }

   private static final class CookieKey {
      private final String cookieName;
      private final String cookiePath;

      CookieKey(String var1, String var2) {
         this.cookieName = var1;
         this.cookiePath = var2;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof CookieKey)) {
            return false;
         } else {
            CookieKey var2 = (CookieKey)var1;
            if (!this.cookieName.equals(var2.cookieName)) {
               return false;
            } else {
               return this.cookiePath.equals(var2.cookiePath);
            }
         }
      }

      public int hashCode() {
         int var1 = this.cookieName.hashCode();
         var1 = 29 * var1 + this.cookiePath.hashCode();
         return var1;
      }
   }
}
