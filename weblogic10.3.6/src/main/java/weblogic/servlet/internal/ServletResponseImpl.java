package weblogic.servlet.internal;

import com.bea.sslplus.SSLNioSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.Socket;
import java.nio.channels.WritableByteChannel;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.version;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.WebAppContainerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.MessageSenderStatistics;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.ServletResponseAttributeEvent;
import weblogic.servlet.ServletResponseAttributeListener;
import weblogic.servlet.http.WLHttpServletResponse;
import weblogic.servlet.internal.session.SessionConfigManager;
import weblogic.servlet.internal.session.SessionContext;
import weblogic.servlet.internal.session.SessionInternal;
import weblogic.servlet.security.Utils;
import weblogic.servlet.security.internal.SecurityModule;
import weblogic.servlet.utils.URLMapping;
import weblogic.transaction.TxHelper;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.StringUtils;
import weblogic.utils.http.BytesToString;
import weblogic.utils.http.HttpParsing;
import weblogic.utils.http.HttpReasonPhraseCoder;
import weblogic.utils.io.FilenameEncoder;

public class ServletResponseImpl implements HttpServletResponse, FutureServletResponse, WLHttpServletResponse, MessageSenderStatistics {
   public static final String X_WEBLOGIC_REQUEST_CLUSTERINFO = "X-WebLogic-Request-ClusterInfo";
   public static final String X_WEBLOGIC_CLUSTER_HASH = "X-WebLogic-Cluster-Hash";
   public static final String X_WEBLOGIC_CLUSTER_LIST = "X-WebLogic-Cluster-List";
   public static final String X_WEBLOGIC_LOAD = "X-WebLogic-Load";
   public static final String X_WEBLOGIC_FORCE_JVMID = "X-WebLogic-Force-JVMID";
   public static final String X_WEBLOGIC_JVMID = "X-WebLogic-JVMID";
   public static final String X_WEBLOGIC_KEEPALIVE_SECS = "X-WebLogic-KeepAliveSecs";
   private static final String HTTP_VERSION_1_0 = "HTTP/1.0";
   private static final String HTTP_VERSION_1_1 = "HTTP/1.1";
   private static final String STATUS_OK_HEADER_1_0;
   private static final String STATUS_OK_HEADER_1_1;
   private static final boolean X_POWERED_BY_HEADER_ENABLED;
   private static final String X_POWERED_BY_HEADER;
   private static final boolean P3P_ENABLED;
   private static final String P3P_HEADER_VALUE;
   private static ArrayList<String> noHttpOnlyInternalApps;
   private final ServletRequestImpl request;
   private final ServletOutputStreamImpl outputStream;
   private WebAppServletContext context;
   private int statusCode = 200;
   private String statusMessage = null;
   private int contentLength = -1;
   private String encoding;
   private boolean encodingSetExplicitly;
   private PrintWriter printWriter;
   private ChunkWriter outputStreamWriter;
   private final ArrayList cookies = new ArrayList();
   private ResponseHeaders headers = new ResponseHeaders(this);
   private Locale locale;
   private boolean gotOutputStream;
   private boolean useKeepAlive = true;
   private static final String[] CRLF;
   private FileSenderImpl fileSender = null;
   private long bytesSent = 0L;
   private long messageSent = 0L;
   private Set attributeListeners = new HashSet(4);
   static final long serialVersionUID = -8811939890096004942L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.servlet.internal.ServletResponseImpl");
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Response_Write_Headers_Around_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Response_Send_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   ServletResponseImpl() {
      this.request = null;
      this.outputStream = null;
   }

   ServletResponseImpl(ServletRequestImpl var1, OutputStream var2) {
      this.outputStream = new ServletOutputStreamImpl(var2, this);
      this.request = var1;
      this.request.setResponse(this);
   }

   final void init() {
      this.statusCode = 200;
      this.statusMessage = null;
      this.contentLength = -1;
      this.headers = new ResponseHeaders(this);
      this.useKeepAlive = true;
      this.context = null;
      this.encoding = null;
      this.encodingSetExplicitly = false;
      this.printWriter = null;
      this.outputStreamWriter = null;
      this.cookies.clear();
      this.locale = null;
      this.gotOutputStream = false;
      this.fileSender = null;
      this.attributeListeners.clear();
   }

   public void flushBuffer() throws IOException {
      this.outputStream.flush();
   }

   public int getBufferSize() {
      return this.outputStream.getBufferSize();
   }

   public String getCharacterEncoding() {
      if (this.outputStreamWriter != null) {
         this.encoding = this.outputStreamWriter.getEncoding();
      }

      if (this.encoding == null) {
         if (this.context != null) {
            this.encoding = this.context.getConfigManager().getDefaultEncoding();
         } else {
            this.encoding = "ISO-8859-1";
         }
      }

      return this.encoding;
   }

   public void setCharacterEncoding(String var1) {
      if (!this.isCommitted()) {
         String var2 = this.getCharacterEncoding();
         this.encodingSetExplicitly = true;
         this.setEncoding(var1);
         var1 = this.getCharacterEncoding();
         if (!var1.equalsIgnoreCase(var2)) {
            String var3 = this.getHeader("Content-Type");
            if (var3 == null) {
               return;
            }

            StringBuilder var4 = new StringBuilder();
            int var5 = var3.indexOf(59);
            if (var5 != -1) {
               var4.append(var3.substring(0, var5));
            } else {
               var4.append(var3);
            }

            var4.append("; charset=");
            var4.append(var1);
            this.headers.setHeader("Content-Type", var4.toString());
         }

      }
   }

   final boolean isOutputStreamInitialized() {
      return this.gotOutputStream || this.printWriter != null;
   }

   final void resetOutputState() {
      this.gotOutputStream = false;
      this.printWriter = null;
   }

   public ServletOutputStream getOutputStream() throws IOException {
      if (this.printWriter != null && this.request != null && this.request.getAttribute("javax.servlet.include.request_uri") == null) {
         throw new IllegalStateException("strict servlet API: cannot call getOutputStream() after getWriter()");
      } else {
         this.gotOutputStream = true;
         return this.getOutputStreamNoCheck();
      }
   }

   public ServletOutputStream getOutputStreamNoCheck() {
      return this.outputStream;
   }

   public final ServletOutputStreamImpl getServletOutputStream() {
      return this.outputStream;
   }

   public PrintWriter getWriter() throws IOException {
      if (this.gotOutputStream && this.request != null && this.request.getAttribute("javax.servlet.include.request_uri") == null) {
         throw new IllegalStateException("strict servlet API: cannot call getWriter() after getOutputStream()");
      } else {
         this.initWriter();
         return this.printWriter;
      }
   }

   protected final PrintWriter getWriterNoCheck() {
      this.initWriter();
      return this.printWriter;
   }

   public boolean isCommitted() {
      return this.outputStream.isCommitted();
   }

   public void reset() throws IllegalStateException {
      this.outputStream.reset();
      this.setStatus(200);
      this.headers = new ResponseHeaders(this);
      this.cookies.clear();
   }

   public void resetBuffer() {
      this.outputStream.clearBuffer();
   }

   public void setBufferSize(int var1) {
      if (this.outputStream.getTotal() > 0) {
         throw new IllegalStateException("Cannot resize buffer - " + this.outputStream.getTotal() + " bytes have already been written (Servlet " + "2.3, sec. 5.1)");
      } else {
         this.setBufferSizeNoWriteCheck(var1);
      }
   }

   public void setBufferSizeNoWriteCheck(int var1) {
      this.objectIfCommitted();
      this.outputStream.setBufferSize(var1);
   }

   public void setContentLength(int var1) {
      if (!this.isCommitted()) {
         if (var1 != -1) {
            this.contentLength = var1;
            this.headers.setContentLength(var1);

            try {
               this.outputStream.setContentLength(var1);
            } catch (ProtocolException var3) {
               throw new RuntimeException(var3.toString());
            }
         }
      }
   }

   public void setContentType(String var1) {
      if (!this.isCommitted()) {
         if (var1 == null) {
            this.headers.setContentType((String)null);
         } else {
            int var2 = StringUtils.indexOfIgnoreCase(var1, "charset");
            String var3;
            if (var2 != -1) {
               var3 = null;
               int var4 = var1.indexOf(59, var2);
               int var5 = var1.indexOf(61, var2);
               if (var4 == -1) {
                  if (var5 != -1 && var5 < var1.length()) {
                     var3 = var1.substring(var5 + 1).trim();
                  }
               } else if (var5 != -1 && var5 < var4) {
                  var3 = var1.substring(var5 + 1, var4).trim();
               }

               var3 = HttpParsing.StripHTTPFieldValue(var3);
               if (var3 != null && var3.length() != 0) {
                  this.encodingSetExplicitly = true;
                  if (!var3.equalsIgnoreCase(this.getCharacterEncoding())) {
                     this.setEncoding(var3);
                     if (!var3.equals(this.getCharacterEncoding())) {
                        int var6 = var1.indexOf(";");
                        if (var6 != -1) {
                           var1 = var1.substring(0, var6);
                        }

                        if (this.encoding != null && CharsetMap.isCharsetAllowedForType(var1)) {
                           var1 = var1 + "; charset=" + this.encoding;
                        }
                     }
                  }
               }
            } else if (var1.toLowerCase().trim().startsWith("text/")) {
               if (this.printWriter != null) {
                  var1 = var1 + "; charset=" + this.outputStreamWriter.getEncoding();
               } else {
                  var3 = this.outputStream.getOutput().getOutput().getEncoding();
                  if (var3 != null && CharsetMap.isCharsetAllowedForType(var1)) {
                     var1 = var1 + "; charset=" + var3;
                  }
               }
            }

            this.headers.setContentType(var1);
         }
      }
   }

   public final String getContentType() {
      return this.getHeader("Content-Type");
   }

   public Locale getLocale() {
      if (this.locale == null) {
         this.locale = Locale.getDefault();
      }

      return this.locale;
   }

   public void setLocale(Locale var1) {
      if (!this.isCommitted()) {
         if (var1 != null) {
            if (!this.encodingSetExplicitly) {
               String var2 = this.context.getConfigManager().getLocaleEncodingMap().getJavaCharset(var1);
               String var3 = this.getContentType();
               if (var3 == null) {
                  this.setEncoding(var2);
               } else {
                  int var4 = StringUtils.indexOfIgnoreCase(var3, "charset=");
                  var3 = var4 > -1 ? var3.substring(0, var4 + 8) : var3 + "; charset=";
                  this.setEncoding(var2);
                  this.headers.setHeader("Content-Type", var3 + var2);
               }
            }

            StringBuffer var5 = new StringBuffer(var1.getLanguage());
            if (!"".equals(var1.getCountry())) {
               var5.append('-').append(var1.getCountry());
            }

            this.setHeader("Content-Language", var5.toString());
            this.locale = var1;
         }
      }
   }

   public CharsetMap getCharsetMap() {
      return this.context.getConfigManager().getCharsetMap();
   }

   public void addCookie(Cookie var1) {
      if (var1 == null) {
         throw new NullPointerException("Invalid Cookie");
      } else {
         this.checkForCRLFChars(var1);
         this.addCookieInternal(var1);
      }
   }

   public void addCookieInternal(Cookie var1) {
      if (!this.isCommitted()) {
         this.cookies.add(var1);
      }
   }

   public final Cookie getCookie(String var1) {
      if (this.cookies.size() > 0) {
         int var2 = this.cookies.size();

         while(true) {
            --var2;
            if (var2 <= -1) {
               break;
            }

            Cookie var3 = (Cookie)this.cookies.get(var2);
            if (var3.getName().equals(var1)) {
               return var3;
            }
         }
      }

      return null;
   }

   public final void removeCookie(String var1, String var2) {
      if (this.cookies.size() >= 1) {
         int var3 = this.cookies.size();

         while(true) {
            --var3;
            if (var3 <= -1) {
               break;
            }

            Cookie var4 = (Cookie)this.cookies.get(var3);
            if (var4.getName() != null && var4.getName().equals(var1) && var4.getPath() != null && var4.getPath().equals(var2)) {
               this.cookies.remove(var4);
               break;
            }
         }

      }
   }

   public void addDateHeader(String var1, long var2) {
      if (!this.isCommitted()) {
         this.headers.addDateHeader(var1, var2);
      }
   }

   public void addHeader(String var1, String var2) {
      this.checkForCRLFChars(var1, var2);
      this.addHeaderInternal(var1, var2);
   }

   public void addHeaderInternal(String var1, String var2) {
      if (!this.isCommitted()) {
         if (var2 == null) {
            var2 = "";
         }

         this.headers.addHeader(var1, var2);
      }
   }

   public void addIntHeader(String var1, int var2) {
      if (!this.isCommitted()) {
         this.headers.addIntHeader(var1, var2);
      }
   }

   public boolean containsHeader(String var1) {
      return this.headers.containsHeader(var1);
   }

   public String encodeRedirectUrl(String var1) {
      return this.encodeRedirectURL(var1);
   }

   public String encodeRedirectURL(String var1) {
      if (var1.startsWith("/")) {
         var1 = this.processProxyPathHeaders(var1);
      }

      return this.encodeURL(var1);
   }

   public String encodeUrl(String var1) {
      return this.encodeURL(var1);
   }

   public String encodeURL(String var1) {
      return this.encodeURL(var1, this.request.getSession(false));
   }

   public void sendError(int var1) throws IOException {
      this.sendError(var1, ErrorMessages.getErrorPage(var1));
   }

   private void sendNoContentError() {
      this.disableKeepAlive();
      this.setContentLength(0);
      this.getWriterNoCheck().flush();
      this.outputStream.getOutput().setWriteEnabled(false);
      this.incrementBytesSentCount((long)this.outputStream.getTotal());
      this.incrementMessageSentCount();
   }

   private void sendContentError(int var1, String var2) {
      this.disableKeepAliveOnSendError(var1);
      this.setContentType("text/html");
      this.setContentLength(var2.length());
      this.setCharacterEncoding("UTF-8");
      this.getWriterNoCheck().print(var2);
      this.getWriterNoCheck().flush();
      this.incrementBytesSentCount((long)this.outputStream.getOutput().getTotal());
      this.incrementMessageSentCount();
   }

   public void sendError(int var1, String var2) throws IOException {
      this.objectIfCommitted();
      this.outputStream.clearCurrentBuffer();
      this.resetOutputState();
      this.setStatus(var1);
      this.setStatusMessage(var2);
      if (var1 != 100 && var1 != 101 && var1 != 204 && var1 != 304) {
         String var3 = this.request.getRequestURI();

         try {
            String var4 = null;
            if (this.context != null) {
               var4 = this.context.getErrorManager().getErrorLocation(String.valueOf(var1));
            }

            if (this.request.getAttribute("javax.servlet.error.status_code") != null) {
               Integer var12 = (Integer)this.request.getAttribute("javax.servlet.error.status_code");
               int var6 = var12;
               String var7 = this.context.getErrorManager().getErrorLocation(String.valueOf(var6));
               if (var6 > 0 && var7 != null && var1 != var6 && var3.endsWith(var7)) {
                  HTTPLogger.logNoLocation(this.context.getLogContext(), var7, var6);
               }
            }

            if (var4 == null) {
               this.sendContentError(var1, var2);
               return;
            }

            if (var3.endsWith(var4)) {
               HTTPLogger.logNoLocation(this.context.getLogContext(), var4, var1);
               this.sendContentError(var1, var2);
               return;
            }

            if (WebAppServletContext.isAbsoluteURL(var4)) {
               this.sendRedirect(var4);
               return;
            }

            try {
               if (var1 != 401) {
                  this.reset();
               }

               this.setStatus(var1);
            } catch (Exception var10) {
               HTTPLogger.logUnableToServeErrorPage(this.context.getLogContext(), var4, var1, var10);
               return;
            }

            URLMapping var14 = this.context.getServletMapping();
            URLMatchHelper var15 = (URLMatchHelper)var14.get(var4);
            String var8;
            RequestDispatcher var13;
            if (var15 != null && !var15.getPattern().startsWith("*.") && !var15.isFileOrJspServlet()) {
               var8 = (String)this.request.getAttribute("weblogic.servlet.errorPage");
               if (var8 != null && var8.equals(var4)) {
                  HTTPLogger.logBadErrorPage(this.context.getLogContext(), var4, var1);
                  this.sendContentError(var1, var2);
                  return;
               }

               this.request.setAttribute("weblogic.servlet.errorPage", var4);
               var13 = this.context.getNamedDispatcher(var15.getServletStub().getServletName(), 3);
            } else {
               var13 = this.context.getRequestDispatcher(var4, 3);
            }

            if (var13 == null) {
               this.sendContentError(var1, var2);
               return;
            }

            ServletStubImpl var9 = null;
            if (var3.startsWith(this.context.getContextPath())) {
               var8 = var3.substring(this.context.getContextPath().length());
            } else {
               var8 = var3;
            }

            if (var8 != null) {
               var9 = this.context.getServletStub(var8);
            }

            this.setErrorAttributes(var9, var1, var2);
            var13.forward(this.request, this);
         } catch (ServletException var11) {
            Throwable var5 = WebAppServletContext.getRootCause(var11);
            HTTPLogger.logSendError(this.context.getLogContext(), var5);
         }

      } else {
         this.sendNoContentError();
      }
   }

   private void disableKeepAliveOnSendError(int var1) {
      switch (var1) {
         case 402:
         case 406:
         case 407:
         case 408:
         case 409:
         case 410:
         case 411:
         default:
            this.disableKeepAlive();
         case 401:
         case 403:
         case 404:
         case 405:
         case 412:
      }
   }

   private void setStatusMessage(String var1) {
      if ((this.statusMessage == null || this.statusMessage.equals("Unknown")) && var1 != null) {
         if (var1.length() < 256) {
            this.statusMessage = Utils.encodeXSS(var1);
         } else {
            this.statusMessage = Utils.encodeXSS(var1.substring(0, 256));
         }
      }

   }

   public String getURLForRedirect(String var1) {
      var1 = this.processProxyPathHeaders(var1);
      return this.getAbsoluteURL(var1);
   }

   private String getAbsoluteURL(String var1) {
      if (this.request.getRequestHeaders().getHost() == null && this.context.getServer().getFrontendHost() == null) {
         return var1;
      } else {
         int var2 = this.request.getServerPort();
         String var3 = this.request.getScheme().toLowerCase();
         return (var2 != 80 || !var3.equals("http")) && (var2 != 443 || !var3.equals("https")) ? var3 + "://" + this.request.getServerName() + ':' + this.request.getServerPort() + var1 : var3 + "://" + this.request.getServerName() + var1;
      }
   }

   public final String processProxyPathHeaders(String var1) {
      String var2 = var1;
      String var3 = this.request.getHeader("WL-PATH-PREPEND");
      boolean var4 = true;
      if (var3 != null) {
         if (var3.endsWith("/")) {
            var3 = var3.substring(0, var3.length() - 1);
         }

         if (!var1.startsWith(var3 + '/') && !var1.equals(var3)) {
            var4 = false;
         } else {
            var2 = var1.substring(var3.length());
            if (var2.length() == 0) {
               var2 = "/";
            } else if (var2.charAt(0) != '/') {
               var2 = "/" + var2;
            }
         }
      }

      String var5 = this.request.getHeader("WL-PATH-TRIM");
      if (var4 && var5 != null) {
         if (var5.charAt(0) != '/') {
            var5 = "/" + var5;
         }

         if (!var2.startsWith(var5 + "/")) {
            var2 = var5 + var2;
         }
      }

      return var2;
   }

   public void sendRedirect(String var1) throws IOException {
      this.objectIfCommitted();
      String var2;
      if (WebAppServletContext.isAbsoluteURL(var1)) {
         if (this.request.getHeader("X-WebLogic-KeepAliveSecs") == null) {
            this.disableKeepAlive();
         }
      } else if (this.context.getConfigManager().isRedirectWithAbsoluteURLEnabled()) {
         if (var1.startsWith("/")) {
            var1 = this.processProxyPathHeaders(var1);
         } else {
            var2 = this.request.getRequestURI();
            String var3 = var2;
            if (!var2.endsWith("/")) {
               var3 = var2.substring(0, var2.lastIndexOf("/") + 1);
            }

            var3 = this.processProxyPathHeaders(var3);
            var1 = FilenameEncoder.resolveRelativeURIPath(var3 + var1);
         }

         var1 = this.getAbsoluteURL(var1);
      }

      this.setHeader("Location", var1);
      this.setStatus(302);
      var2 = this.headers.getHeader("Content-Type");
      if (var2 == null) {
         this.setContentType(this.context.getConfigManager().getDefaultMimeType());
      }

      this.outputStream.reset();
      this.outputStream.println("<html><head><title>302 Moved Temporarily</title></head>");
      this.outputStream.println("<body bgcolor=\"#FFFFFF\">");
      this.outputStream.println("<p>This document you requested has moved temporarily.</p>");
      var1 = Utils.encodeXSS(var1);
      this.outputStream.println("<p>It's now at <a href=\"" + var1 + "\">" + var1 + "</a>.</p>");
      this.outputStream.println("</body></html>");
      this.outputStream.flush();
      this.incrementBytesSentCount((long)this.outputStream.getTotal());
      this.incrementMessageSentCount();
      this.outputStream.commit();
      this.outputStream.setWriteEnabled(false);
   }

   public void setDateHeader(String var1, long var2) {
      if (!this.isCommitted()) {
         this.headers.setDateHeader(var1, var2);
      }
   }

   public void setHeader(String var1, String var2) {
      this.checkForCRLFChars(var1, var2);
      this.setHeaderInternal(var1, var2);
   }

   public void setHeaderInternal(String var1, String var2) {
      if (!this.isCommitted()) {
         if (var2 == null) {
            var2 = "";
         }

         if (var1.equalsIgnoreCase("Content-Type")) {
            this.setContentType(var2);
         } else {
            if (var1.equalsIgnoreCase("Content-Length")) {
               try {
                  this.setContentLength(Integer.parseInt(var2.trim()));
                  return;
               } catch (NumberFormatException var4) {
               }
            }

            this.headers.setHeader(var1, var2);
         }
      }
   }

   public void setIntHeader(String var1, int var2) {
      if (!this.isCommitted()) {
         this.headers.setIntHeader(var1, var2);
      }
   }

   public void setStatus(int var1) {
      if (!this.isCommitted()) {
         this.setStatus(var1, HttpReasonPhraseCoder.getReasonPhrase(var1));
      }
   }

   public void setStatus(int var1, String var2) {
      if (!this.isCommitted()) {
         this.statusCode = var1;
         this.statusMessage = var2;
      }
   }

   final void initHttpServer(HttpServer var1) {
      if (!this.isCommitted()) {
         if (var1.getMBean().isSendServerHeaderEnabled()) {
            this.headers.setHeader("Server", HttpServer.SERVER_INFO);
         }

         if (X_POWERED_BY_HEADER_ENABLED) {
            this.headers.setHeader("X-Powered-By", X_POWERED_BY_HEADER);
         }

         if (P3P_ENABLED) {
            this.headers.setHeader("P3P", P3P_HEADER_VALUE);
         }

      }
   }

   final void initContext(WebAppServletContext var1) {
      this.context = var1;
   }

   final ServletRequestImpl getRequest() {
      return this.request;
   }

   final boolean hasKeepAliveHeader() {
      return this.headers.getKeepAlive();
   }

   public final void disableKeepAlive() {
      this.useKeepAlive = false;
   }

   final boolean getUseKeepAlive() {
      return this.request.getInputHelper().getRequestParser().isKeepAlive() && this.useKeepAlive;
   }

   public final String getHeader(String var1) {
      String var2 = this.headers.getHeader(var1);
      return var2;
   }

   public Socket getSocket() throws IOException {
      if (this.request == null) {
         return null;
      } else {
         VirtualConnection var1 = this.request.getConnection();
         if (var1 == null) {
            return null;
         } else {
            this.outputStream.setNativeControlsPipe(true);
            return var1.getSocket();
         }
      }
   }

   public WritableByteChannel getWritableByteChannel() throws IOException {
      Socket var1 = this.getSocket();
      if (var1 == null) {
         return null;
      } else {
         return (WritableByteChannel)(var1 instanceof SSLNioSocket ? ((SSLNioSocket)var1).getWritableByteChannel() : var1.getChannel());
      }
   }

   final void setDefaultEncoding(String var1) {
      if (!BytesToString.is8BitUnicodeSubset(var1)) {
         if (this.outputStream != null && this.context != null) {
            ChunkOutputWrapper var2 = this.outputStream.getOutput();
            String var3 = var2.getEncoding();
            if (var1 != null && !var1.equalsIgnoreCase(var3)) {
               try {
                  var2.changeToCharset(var1, this.context.getConfigManager().getCharsetMap());
                  this.outputStream.setOutput(var2);
               } catch (UnsupportedEncodingException var5) {
               }
            }
         }

      }
   }

   private void setEncoding(String var1) {
      if (var1 != null) {
         if (this.printWriter != null) {
            String var2 = this.getCharacterEncoding();
            if (var2 != null && !var2.equalsIgnoreCase(var1)) {
               return;
            }
         }

         ChunkOutputWrapper var7 = this.outputStream.getOutput();

         try {
            CharsetMap var3;
            if (this.context != null) {
               var3 = this.context.getConfigManager().getCharsetMap();
            } else {
               var3 = new CharsetMap((Map)null);
            }

            var7.changeToCharset(var1, var3);
            Iterator var4 = this.attributeListeners.iterator();

            while(var4.hasNext()) {
               ServletResponseAttributeListener var5 = (ServletResponseAttributeListener)var4.next();
               var5.attributeChanged(new ServletResponseAttributeEvent(this, "ENCODING", this.encoding));
            }

            this.encoding = var1;
            this.headers.setEncoding(var3.getJavaCharset(var1));
         } catch (UnsupportedEncodingException var6) {
            throw new IllegalArgumentException("unsupported encoding: '" + var1 + "': " + var6);
         }
      }
   }

   private void initWriter() {
      if (this.printWriter == null) {
         ChunkOutputWrapper var1 = this.outputStream.getOutput();
         this.outputStreamWriter = new ChunkWriter(var1);
         this.printWriter = new UnsynchronizedPrintWriter(this.outputStreamWriter, false);
      }
   }

   final void writeHeaders() throws IOException {
      boolean var21;
      boolean var10000 = var21 = _WLDF$INST_FLD_Servlet_Response_Write_Headers_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var22 = null;
      DiagnosticActionState[] var23 = null;
      Object var20 = null;
      if (var10000) {
         Object[] var16 = null;
         if (_WLDF$INST_FLD_Servlet_Response_Write_Headers_Around_Medium.isArgumentsCaptureNeeded()) {
            var16 = new Object[]{this};
         }

         DynamicJoinPoint var36 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var16, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Servlet_Response_Write_Headers_Around_Medium;
         DiagnosticAction[] var10002 = var22 = var10001.getActions();
         InstrumentationSupport.preProcess(var36, var10001, var10002, var23 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         if (this.isCommitted()) {
            return;
         }

         if (HTTPDebugLogger.isEnabled()) {
            this.trace("Writing headers for " + this.request.toStringSimple());
         }

         ServletOutputStreamImpl var1 = this.outputStream;
         String var2 = this.buildFirstLine();
         this.headers.setDate(this.request.getHttpAccountingInfo().getInvokeTime());
         SessionConfigManager var3 = this.context == null ? null : this.context.getSessionContext().getConfigMgr();
         String var7;
         if (this.cookies.size() > 0) {
            int var4 = 0;

            while(true) {
               if (var4 >= this.cookies.size()) {
                  if (var3 != null && !var3.isCacheSessionCookieEnabled()) {
                     this.headers.disableCacheControlForCookie();
                  }
                  break;
               }

               Cookie var5 = (Cookie)this.cookies.get(var4);
               boolean var6 = false;
               if (var3 != null || !var5.getName().equals("JSESSIONID") && !var5.getName().equals("_WL_AUTHCOOKIE_JSESSIONID")) {
                  if (var3 != null && (var5.getName().equals(var3.getCookieName()) || var5.getName().equals(var3.getWLSAuthCookieName()))) {
                     var6 = var3.isCookieHttpOnly();
                  }
               } else {
                  var6 = true;
               }

               if (this.context.isInternalApp() && noHttpOnlyInternalApps.contains(this.context.getContextPath())) {
                  var6 = false;
               }

               var7 = CookieParser.formatCookie(var5, var6);
               if (HTTPDebugLogger.isEnabled()) {
                  this.trace("Wrote cookie: " + var7);
               }

               this.headers.addHeader("Set-Cookie", var7);
               ++var4;
            }
         }

         HttpServer var28 = this.getHttpServer();
         if (var28 != null) {
            boolean var29 = false;
            ServerMBean var31 = ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID).getServer();
            if (var31.getCluster() != null && this.request.getRequestHeaders().getXWeblogicRequestClusterInfo() != null) {
               var29 = true;
               this.headers.unsetHeader("X-WebLogic-Cluster-List");
               this.headers.unsetHeader("X-WebLogic-Cluster-Hash");
               if (HTTPDebugLogger.isEnabled()) {
                  this.trace("writing server list for " + this.request.toString());
               }

               var7 = this.request.getRequestHeaders().getXWeblogicClusterHash();
               String var8 = var7 == null ? "" : var7;
               String var9 = MembershipControllerImpl.getInstance().getHash();
               String var10 = this.headers.getHeader("X-WebLogic-Cluster-Hash");
               if (var9 != null && !var9.equals(var8)) {
                  String[] var11 = MembershipControllerImpl.getInstance().getClusterList(this.request.getConnection().getChannel());
                  if (var11 != null && var11.length > 0) {
                     StringBuffer var12 = new StringBuffer();

                     for(int var13 = 0; var13 < var11.length; ++var13) {
                        String var14 = var11[var13];
                        var12.append(var14);
                        if (var13 < var11.length - 1) {
                           var12.append('|');
                        }
                     }

                     this.headers.setHeader("X-WebLogic-Cluster-Hash", var9);
                     this.headers.setHeader("X-WebLogic-Cluster-List", var12.toString());
                  }
               }
            }

            if (this.request.getRequestHeaders().getXWeblogicForceJvmId() != null) {
               var29 = true;
               this.headers.unsetHeader("X-WebLogic-JVMID");
               this.headers.setHeader("X-WebLogic-JVMID", var28.getServerHash());
            }

            if (var29) {
               this.headers.unsetHeader("X-WebLogic-KeepAliveSecs");
               var7 = this.request.getRequestHeaders().getXWeblogicKeepaliveSecs();
               if (this.context != null && var7 != null) {
                  int var34 = this.context.getServer().getMBean().getKeepAliveSecs();

                  try {
                     int var35 = Integer.parseInt(var7);
                     if (var35 > var34) {
                        this.headers.setHeader("X-WebLogic-KeepAliveSecs", "" + var34);
                     }
                  } catch (NumberFormatException var26) {
                     this.headers.setHeader("X-WebLogic-KeepAliveSecs", "" + var34);
                  }
               }
            }
         }

         String var30 = this.getHeader("Content-Type");
         if (var30 != null && this.request.getAttribute("weblogic.servlet.jsp") != null && var30.toLowerCase().trim().startsWith("text/") && var30.indexOf(59) < 0) {
            StringBuilder var32 = new StringBuilder();
            var32.append(var30);
            var32.append("; charset=");
            var32.append(this.getCharacterEncoding());
            this.headers.setContentType(var32.toString());
         }

         if (!this.getUseKeepAlive()) {
            this.headers.setConnection("close");
         }

         int var33 = this.headers.writeHeaders(var1, var2);
         this.incrementBytesSentCount((long)var33);
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("Response committed. request: '" + this.request.toStringSimple() + "' response: " + this.toString());
         }
      } finally {
         if (var21) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_Response_Write_Headers_Around_Medium, var22, var23);
         }

      }

   }

   private final String buildFirstLine() {
      String var1;
      if (this.statusCode == 200 && (this.statusMessage == null || "OK".equals(this.statusMessage))) {
         if (this.request != null && !this.request.getInputHelper().getRequestParser().isProtocolVersion_1_1() && !this.useHighestCompatibleHttpVersion()) {
            var1 = STATUS_OK_HEADER_1_0;
         } else {
            var1 = STATUS_OK_HEADER_1_1;
         }
      } else {
         String var2;
         if (this.request != null && !this.request.getInputHelper().getRequestParser().isProtocolVersion_1_1() && !this.useHighestCompatibleHttpVersion()) {
            var2 = "HTTP/1.0";
         } else {
            var2 = "HTTP/1.1";
         }

         var1 = var2 + ' ' + this.statusCode + ' ' + this.statusMessage + "\r\n";
      }

      return var1;
   }

   private String encodeURL(String var1, HttpSession var2) {
      if (var2 != null && !this.request.isRequestedSessionIdFromCookie() && this.context.getSessionContext().getConfigMgr().isSessionTrackingEnabled() && this.context.getSessionContext().getConfigMgr().isUrlRewritingEnabled() && ((SessionInternal)var2).isValid()) {
         String var3 = null;
         String var4 = null;
         int var5 = var1.indexOf(63);
         if (var5 != -1) {
            var3 = var1.substring(var5 + 1);
            var1 = var1.substring(0, var5);
         } else {
            int var6 = var1.indexOf(35);
            if (var6 != -1) {
               var4 = var1.substring(var6 + 1);
               var1 = var1.substring(0, var6);
            }
         }

         if (!this.context.getSessionContext().getConfigMgr().isEncodeSessionIdInQueryParamsEnabled()) {
            String var9 = this.context.getSessionContext().getConfigMgr().getCookieName();
            if (var9.equals("JSESSIONID")) {
               var9 = "jsessionid";
            }

            int var7 = var1.indexOf(59);
            if (var7 == -1) {
               var1 = var1 + ';' + var9 + '=' + ((SessionInternal)var2).getIdWithServerInfo();
            } else {
               String var8 = var1.substring(0, var7);
               var1 = var8 + ';' + var9 + '=' + ((SessionInternal)var2).getIdWithServerInfo();
            }
         } else {
            StringBuffer var10 = (new StringBuffer(this.context.getSessionContext().getConfigMgr().getCookieName())).append('=').append(((SessionInternal)var2).getIdWithServerInfo());
            if (var3 != null) {
               var10.append('&').append(var3);
            }

            var3 = var10.toString();
         }

         if (var3 != null) {
            var1 = var1 + '?' + var3;
         } else if (var4 != null) {
            var1 = var1 + "#" + var4;
         }

         return var1;
      } else {
         return var1;
      }
   }

   private void setErrorAttributes(ServletStubImpl var1, int var2, String var3) {
      String var4 = this.request.getRequestURI();
      this.request.setAttribute("javax.servlet.error.status_code", new Integer(var2));
      if (this.request.getAttribute("javax.servlet.error.message") == null) {
         String var5 = var3 != null ? var3 : ErrorMessages.getSection(var2);
         this.request.setAttribute("javax.servlet.error.message", var5);
      }

      this.request.setAttribute("javax.servlet.error.request_uri", var4);
      if (var1 != null) {
         this.request.setAttribute("javax.servlet.error.servlet_name", var1.getServletName());
      }

   }

   final void setSessionCookie(HttpSession var1) {
      if (this.context.getSessionContext().getConfigMgr().isSessionCookiesEnabled() && this.context.getSessionContext().getConfigMgr().isSessionTrackingEnabled()) {
         SessionContext var2 = this.context.getSessionContext();
         this.removeCookie(var2.getConfigMgr().getCookieName(), var2.getConfigMgr().getCookiePath());
         Cookie var3 = new Cookie(var2.getConfigMgr().getCookieName(), ((SessionInternal)var1).getIdWithServerInfo());
         if (!this.requestHadCookie(var3)) {
            if (HTTPDebugLogger.isEnabled()) {
               this.trace("Setting cookie for " + this.request.toStringSimple());
            }

            var3.setComment(var2.getConfigMgr().getCookieComment());
            var3.setMaxAge(var2.getConfigMgr().getCookieMaxAgeSecs());
            String var4 = var2.getConfigMgr().getCookieDomain();
            if (var4 != null) {
               var3.setDomain(var4);
            }

            var3.setPath(var2.getConfigMgr().getCookiePath());
            if (var2.getConfigMgr().isCookieSecure()) {
               var3.setSecure(true);
            }

            this.addCookie(var3);
         }
      }
   }

   public final int getStatus() {
      return this.statusCode;
   }

   public final int getContentLength() {
      return this.contentLength > -1 ? this.contentLength : this.outputStream.getTotal();
   }

   private void ensureContentLength() throws IOException {
      if (this.fileSender != null && !this.fileSender.usesServletOutputStream()) {
         this.outputStream.ensureContentLength(this.fileSender.getBytesSent());
      } else {
         this.outputStream.ensureContentLength(0L);
      }

   }

   public void setResponseReady() {
      if (this.request != null) {
         this.request.disableFutureResponse();
      }

   }

   public final void send() throws IOException {
      boolean var16;
      boolean var10000 = var16 = _WLDF$INST_FLD_Servlet_Response_Send_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var17 = null;
      DiagnosticActionState[] var18 = null;
      Object var15 = null;
      if (var10000) {
         Object[] var11 = null;
         if (_WLDF$INST_FLD_Servlet_Response_Send_Around_Medium.isArgumentsCaptureNeeded()) {
            var11 = new Object[]{this};
         }

         DynamicJoinPoint var74 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var11, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Servlet_Response_Send_Around_Medium;
         DiagnosticAction[] var10002 = var17 = var10001.getActions();
         InstrumentationSupport.preProcess(var74, var10001, var10002, var18 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         HttpServer var1 = this.getHttpServer();

         try {
            if (this.context == null) {
               this.request.getConnection().close();
               throw new AssertionError("context not available");
            }

            if (var1.getLogManager() != null && var1.getLogManager().isExtendedFormat()) {
               try {
                  this.request.getHttpAccountingInfo().init(this.request, SecurityModule.getCurrentUser(var1, this.request));
               } catch (Error var68) {
                  this.request.getConnection().close();
                  throw var68;
               } catch (RuntimeException var69) {
                  this.request.getConnection().close();
                  throw var69;
               }
            }

            Thread var2 = Thread.currentThread();
            ClassLoader var3 = this.context.pushEnvironment(var2);
            AuthenticatedSubject var4 = null;

            try {
               var4 = SecurityModule.getCurrentUser(var1, this.request);
            } catch (Error var66) {
               this.request.getConnection().close();
               throw var66;
            } catch (RuntimeException var67) {
               this.request.getConnection().close();
               throw var67;
            }

            if (var4 == null) {
               var4 = SubjectUtils.getAnonymousSubject();
            }

            Throwable var5 = (Throwable)SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, var4, new PrivilegedAction() {
               public Object run() {
                  try {
                     ServletResponseImpl.this.request.getSessionHelper().syncSession();
                     return null;
                  } catch (Throwable var2) {
                     return var2;
                  }
               }
            });
            WebAppServletContext var75 = this.context;
            WebAppServletContext.popEnvironment(var2, var3);
            if (var5 != null) {
               if (var5 instanceof Error) {
                  this.request.getConnection().close();
                  throw (Error)var5;
               }

               if (var5 instanceof RuntimeException) {
                  this.request.getConnection().close();
                  throw (RuntimeException)var5;
               }

               IOException var6 = new IOException(StackTraceUtils.throwable2StackTrace(var5));
               this.request.getConnection().deliverHasException(var6);
               throw var6;
            }

            try {
               this.ensureContentLength();
               this.outputStream.commit();
               this.incrementBytesSentCount((long)this.outputStream.getTotal());
               this.incrementMessageSentCount();
            } catch (IOException var64) {
               this.request.getConnection().deliverHasException(var64);
               throw var64;
            } finally {
               this.abortActiveTx();
            }
         } finally {
            var1.getLogManager().log(this.request, this);
         }

         try {
            this.outputStream.finish();
         } catch (IOException var63) {
            this.request.getConnection().deliverHasException(var63);
            throw var63;
         }

         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("Response finished for request '" + this.request.toStringSimple() + "'");
         }

         if (this.getUseKeepAlive()) {
            if (HTTPDebugLogger.isEnabled()) {
               this.trace("Requeuing Keep-Alive connection");
            }

            this.request.getConnection().requeue();
         } else {
            if (HTTPDebugLogger.isEnabled()) {
               this.trace("Closing non Keep-Alive connection");
            }

            try {
               this.request.skipUnreadBody();
            } catch (Throwable var70) {
               if (HTTPDebugLogger.isEnabled()) {
                  this.trace(var70.toString());
               }
            } finally {
               this.request.getConnection().close();
            }
         }
      } finally {
         if (var16) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Servlet_Response_Send_Around_Medium, var17, var18);
         }

      }

   }

   private void abortActiveTx() {
      if (!this.request.isFutureResponseEnabled()) {
         try {
            Transaction var1 = TxHelper.getTransactionManager().getTransaction();
            if (var1 == null) {
               return;
            }

            if (var1.getStatus() == 1 || var1.getStatus() == 0) {
               if (HTTPDebugLogger.isEnabled()) {
                  this.trace("Aborting unhandled Tx - " + var1);
               }

               var1.rollback();
            }
         } catch (SystemException var2) {
            HTTPLogger.logFailedToLookupTransaction(this.context.getLogContext(), var2);
         }

      }
   }

   private void trace(String var1) {
      HTTPDebugLogger.debug(this.request.toStringSimple() + ": " + var1);
   }

   private boolean requestHadCookie(Cookie var1) {
      if (!this.request.isRequestedSessionIdFromCookie()) {
         return false;
      } else {
         String var2 = this.request.getSessionHelper().getIncomingSessionCookieValue();
         return var2 == null ? false : var2.startsWith(var1.getValue());
      }
   }

   public final WebAppServletContext getContext() {
      return this.context;
   }

   private boolean useHighestCompatibleHttpVersion() {
      return this.getHttpServer().getMBean().isUseHighestCompatibleHTTPVersion();
   }

   private HttpServer getHttpServer() {
      return this.context == null ? WebService.defaultHttpServer() : this.context.getServer();
   }

   private void objectIfCommitted() {
      if (this.isCommitted()) {
         throw new IllegalStateException("Response already committed");
      }
   }

   public static ServletResponseImpl getOriginalResponse(ServletResponse var0) {
      while(var0 instanceof ServletResponseWrapper) {
         var0 = ((ServletResponseWrapper)var0).getResponse();
      }

      if (var0 == null) {
         throw new AssertionError("Original response not available");
      } else {
         return (ServletResponseImpl)var0;
      }
   }

   public final boolean isInternalDispatch() {
      return this.request.getConnection().isInternalDispatch();
   }

   public long getBytesSentCount() {
      return this.bytesSent;
   }

   public long getMessagesSentCount() {
      return this.messageSent;
   }

   public void registerAttributeListener(ServletResponseAttributeListener var1) {
      if (var1 != null) {
         this.attributeListeners.add(var1);
      }
   }

   private static String initXPoweredByHeaderValue(String var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append("Servlet/2.5 JSP/2.1");
      if (var0.equals("MEDIUM")) {
         var1.append(" (").append("WebLogic/" + version.getPLInfo()[1]).append(")");
      } else if (var0.equals("FULL")) {
         var1.append(" (").append("WebLogic/" + version.getPLInfo()[1]).append(" JDK/");
         var1.append(System.getProperty("java.version")).append(")");
      }

      return var1.toString();
   }

   private void checkForCRLFChars(String var1, String var2) {
      if (containsCRLFChars(var2)) {
         throw new IllegalArgumentException("Header:" + var1 + " Cannot contain CRLF Charcters");
      }
   }

   private void checkForCRLFChars(Cookie var1) {
      if (var1 != null && containsCRLFChars(var1.getValue())) {
         throw new IllegalArgumentException("Cookie:" + var1.getName() + " Cannot contain CRLF Charcters");
      }
   }

   private static boolean containsCRLFChars(String var0) {
      if (var0 == null) {
         return false;
      } else {
         for(int var1 = 0; var1 < CRLF.length; ++var1) {
            if (containsPattern(var0, 0, CRLF[var1])) {
               return true;
            }
         }

         return false;
      }
   }

   private static boolean containsPattern(String var0, int var1, String var2) {
      boolean var3 = true;
      int var6;
      if ((var6 = var0.indexOf(var2, var1)) == -1) {
         return false;
      } else {
         int var4 = var2.length();
         if (var4 <= 2 && var6 + var4 != var0.length()) {
            char var5 = var0.charAt(var6 + var4);
            return var5 != ' ' && var5 != '\t' ? true : containsPattern(var0, var6 + var4, var2);
         } else {
            return true;
         }
      }
   }

   void incrementBytesSentCount(long var1) {
      this.bytesSent += var1;
   }

   void incrementMessageSentCount() {
      ++this.messageSent;
   }

   FileSenderImpl getFileSender() {
      return this.fileSender;
   }

   void setFileSender(FileSenderImpl var1) {
      if (this.fileSender != null) {
         throw new AssertionError("Attempt to replace FileSender");
      } else {
         this.fileSender = var1;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(128);
      var1.append(super.toString()).append("[\n").append(this.buildFirstLine()).append(this.headers.toString()).append(']');
      return var1.toString();
   }

   static {
      _WLDF$INST_FLD_Servlet_Response_Write_Headers_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Response_Write_Headers_Around_Medium");
      _WLDF$INST_FLD_Servlet_Response_Send_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Response_Send_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ServletResponseImpl.java", "weblogic.servlet.internal.ServletResponseImpl", "writeHeaders", "()V", 1148, InstrumentationSupport.makeMap(new String[]{"Servlet_Response_Write_Headers_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("req", "weblogic.diagnostics.instrumentation.gathering.ServletRequestRenderer", false, true), (ValueHandlingInfo)null, (ValueHandlingInfo[])null)}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ServletResponseImpl.java", "weblogic.servlet.internal.ServletResponseImpl", "send", "()V", 1458, InstrumentationSupport.makeMap(new String[]{"Servlet_Response_Send_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("req", "weblogic.diagnostics.instrumentation.gathering.ServletRequestRenderer", false, true), (ValueHandlingInfo)null, (ValueHandlingInfo[])null)}), (boolean)0);
      STATUS_OK_HEADER_1_0 = "HTTP/1.0 200 " + HttpReasonPhraseCoder.getReasonPhrase(200) + "\r\n";
      STATUS_OK_HEADER_1_1 = "HTTP/1.1 200 " + HttpReasonPhraseCoder.getReasonPhrase(200) + "\r\n";
      noHttpOnlyInternalApps = new ArrayList();
      WebAppContainerMBean var0 = ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID).getDomain().getWebAppContainer();
      String var1 = var0.getXPoweredByHeaderLevel();
      X_POWERED_BY_HEADER_ENABLED = !var1.equals("NONE");
      X_POWERED_BY_HEADER = initXPoweredByHeaderValue(var1);
      P3P_HEADER_VALUE = var0.getP3PHeaderValue();
      P3P_ENABLED = P3P_HEADER_VALUE != null;
      String var2 = System.getProperty("weblogic.cookies.HttpOnlyDisabledInternalApps");
      if (var2 != null) {
         StringTokenizer var3 = new StringTokenizer(var2, "|");

         while(var3.hasMoreTokens()) {
            noHttpOnlyInternalApps.add(var3.nextToken().trim());
         }
      }

      CRLF = new String[]{"\r\n", "\r%0a", "%0d\n", "%0d%0a", "\r%0A", "%0D\n", "%0D%0A", "%0d%0A", "%0D%0a"};
   }
}
