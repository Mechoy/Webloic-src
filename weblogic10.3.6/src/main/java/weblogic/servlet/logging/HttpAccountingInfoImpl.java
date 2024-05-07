package weblogic.servlet.logging;

import java.nio.ByteBuffer;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import javax.servlet.http.Cookie;
import weblogic.protocol.ServerChannel;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.servlet.internal.ServletRequestImpl;

public final class HttpAccountingInfoImpl implements HttpAccountingInfo {
   private final ServletRequestImpl request;
   private String remoteUser = null;
   private String requestedSessionId = null;
   private Principal userPrincipal = null;
   private boolean isRequestedSessionIdValid;
   private long invocationTime;

   public HttpAccountingInfoImpl(ServletRequestImpl var1) {
      this.request = var1;
   }

   public void init(ServletRequestImpl var1, AuthenticatedSubject var2) {
      if (var2 != null) {
         this.userPrincipal = SubjectUtils.getUserPrincipal(var2);
      }

      this.requestedSessionId = var1.getRequestedSessionId();
      this.isRequestedSessionIdValid = var1.isRequestedSessionIdValid();
   }

   public void clear() {
      this.remoteUser = null;
      this.userPrincipal = null;
      this.requestedSessionId = null;
      this.isRequestedSessionIdValid = false;
      this.invocationTime = -1L;
   }

   public Object getAttribute(String var1) {
      return this.request.getAttribute(var1);
   }

   public Enumeration getAttributeNames() {
      return this.request.getAttributeNames();
   }

   public String getCharacterEncoding() {
      return this.request.getCharacterEncoding();
   }

   public int getResponseContentLength() {
      return this.request.getResponse().getContentLength();
   }

   public String getContentType() {
      return this.request.getContentType();
   }

   public Locale getLocale() {
      return this.request.getLocale();
   }

   public Enumeration getLocales() {
      return this.request.getLocales();
   }

   public String getParameter(String var1) {
      return this.request.getParameter(var1);
   }

   public Enumeration getParameterNames() {
      return this.request.getParameterNames();
   }

   public String[] getParameterValues(String var1) {
      return this.request.getParameterValues(var1);
   }

   public String getProtocol() {
      return this.request.getProtocol();
   }

   public String getRemoteAddr() {
      return this.request.getRemoteAddr();
   }

   public String getRemoteHost() {
      return this.request.getRemoteHost();
   }

   public String getScheme() {
      return this.request.getScheme();
   }

   public String getServerName() {
      return this.request.getServerName();
   }

   public int getServerPort() {
      return this.request.getServerPort();
   }

   public boolean isSecure() {
      return this.request.isSecure();
   }

   public String getAuthType() {
      return this.request.getAuthType();
   }

   public String getContextPath() {
      return this.request.getContextPath();
   }

   public Cookie[] getCookies() {
      return this.request.getCookies();
   }

   public long getDateHeader(String var1) {
      return this.request.getDateHeader(var1);
   }

   public String getHeader(String var1) {
      return this.request.getHeader(var1);
   }

   public Enumeration getHeaderNames() {
      return this.request.getHeaderNames();
   }

   public Enumeration getHeaders(String var1) {
      return this.request.getHeaders(var1);
   }

   public int getIntHeader(String var1) {
      return this.request.getIntHeader(var1);
   }

   public String getMethod() {
      return this.request.getMethod();
   }

   public String getPathInfo() {
      return this.request.getPathInfo();
   }

   public String getPathTranslated() {
      return this.request.getPathTranslated();
   }

   public String getQueryString() {
      return this.request.getQueryString();
   }

   public void setRemoteUser(String var1) {
      this.remoteUser = var1;
   }

   public String getRemoteUser() {
      return this.remoteUser;
   }

   public String getRequestURI() {
      return this.request.getRequestURI();
   }

   public String getRequestedSessionId() {
      return this.requestedSessionId;
   }

   public String getServletPath() {
      return this.request.getServletPath();
   }

   public Principal getUserPrincipal() {
      return this.userPrincipal;
   }

   public boolean isRequestedSessionIdFromCookie() {
      return this.request.isRequestedSessionIdFromCookie();
   }

   public boolean isRequestedSessionIdFromURL() {
      return this.request.isRequestedSessionIdFromURL();
   }

   public boolean isRequestedSessionIdFromUrl() {
      return this.request.isRequestedSessionIdFromUrl();
   }

   public boolean isRequestedSessionIdValid() {
      return this.isRequestedSessionIdValid;
   }

   public ByteBuffer getURIAsBytes() {
      return this.request.getInputHelper().getRequestParser().getOriginalRequestUriBytes();
   }

   public long getInvokeTime() {
      return this.invocationTime;
   }

   public void setInvokeTime(long var1) {
      this.invocationTime = var1;
   }

   public int getResponseStatusCode() {
      return this.request.getResponse().getStatus();
   }

   public String getResponseHeader(String var1) {
      return this.request.getResponse().getHeader(var1);
   }

   public ServerChannel getServerChannel() {
      return this.request.getServerChannel();
   }
}
