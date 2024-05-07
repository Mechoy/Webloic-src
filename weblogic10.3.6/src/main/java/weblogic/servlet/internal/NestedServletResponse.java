package weblogic.servlet.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import weblogic.servlet.HTTPLogger;

public class NestedServletResponse extends ServletResponseImpl implements HttpServletResponse {
   protected final HttpServletResponse delegate;
   protected final ServletResponse orig;

   protected NestedServletResponse(ServletResponse var1) {
      for(this.orig = (ServletResponse)var1; var1 instanceof NestedServletResponse; var1 = ((NestedServletResponse)var1).delegate) {
      }

      this.delegate = (HttpServletResponse)var1;
   }

   public final void flushBuffer() throws IOException {
      this.delegate.flushBuffer();
   }

   public final int getBufferSize() {
      return this.delegate.getBufferSize();
   }

   public final String getCharacterEncoding() {
      return this.delegate.getCharacterEncoding();
   }

   public final Locale getLocale() {
      return this.delegate.getLocale();
   }

   public CharsetMap getCharsetMap() {
      return ((ServletResponseImpl)this.delegate).getCharsetMap();
   }

   public ServletOutputStream getOutputStream() throws IOException {
      return this.delegate instanceof ServletResponseImpl ? ((ServletResponseImpl)this.delegate).getOutputStreamNoCheck() : this.delegate.getOutputStream();
   }

   public ServletOutputStream getOutputStreamNoCheck() {
      return ((ServletResponseImpl)this.delegate).getOutputStreamNoCheck();
   }

   public PrintWriter getWriter() throws IOException {
      return this.delegate instanceof ServletResponseImpl ? ((ServletResponseImpl)this.delegate).getWriterNoCheck() : this.delegate.getWriter();
   }

   public final boolean isCommitted() {
      return this.delegate.isCommitted();
   }

   public final void reset() {
   }

   public final void setBufferSize(int var1) {
   }

   public final void setBufferSizeNoWriteCheck(int var1) {
   }

   public final void setContentLength(int var1) {
   }

   public final void setContentType(String var1) {
   }

   public final void setCharacterEncoding(String var1) {
      this.delegate.setCharacterEncoding(var1);
   }

   public final void setLocale(Locale var1) {
      this.delegate.setLocale(var1);
   }

   public final void resetBuffer() {
   }

   public final void addCookie(Cookie var1) {
   }

   public final void addDateHeader(String var1, long var2) {
   }

   public final void addHeader(String var1, String var2) {
   }

   public final void addIntHeader(String var1, int var2) {
   }

   public final boolean containsHeader(String var1) {
      return this.delegate.containsHeader(var1);
   }

   public final String encodeRedirectURL(String var1) {
      return this.delegate.encodeRedirectUrl(var1);
   }

   public final String encodeRedirectUrl(String var1) {
      return this.delegate.encodeRedirectUrl(var1);
   }

   public final String encodeURL(String var1) {
      return this.delegate.encodeUrl(var1);
   }

   public final String encodeUrl(String var1) {
      return this.delegate.encodeUrl(var1);
   }

   public final void sendError(int var1) {
      if (var1 == 404) {
         ServletRequestImpl var2 = ((ServletResponseImpl)this.delegate).getRequest();
         HTTPLogger.logIncludedFileNotFound((String)var2.getAttribute("javax.servlet.include.request_uri"), var2.getRequestURI());
      }

   }

   public final void sendError(int var1, String var2) {
      if (var1 == 404) {
         ServletRequestImpl var3 = ((ServletResponseImpl)this.delegate).getRequest();
         HTTPLogger.logIncludedFileNotFound((String)var3.getAttribute("javax.servlet.include.request_uri"), var3.getRequestURI());
      }

   }

   public final void sendRedirect(String var1) {
   }

   public final void setDateHeader(String var1, long var2) {
   }

   public final void setHeader(String var1, String var2) {
   }

   public final void setIntHeader(String var1, int var2) {
   }

   public final void setStatus(int var1) {
   }

   public final void setStatus(int var1, String var2) {
   }

   public final HttpServletResponse getDelegate() {
      return this.delegate;
   }

   public final ServletResponse getOriginalResponse() {
      return this.orig;
   }
}
