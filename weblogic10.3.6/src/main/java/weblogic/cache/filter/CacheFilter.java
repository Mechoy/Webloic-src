package weblogic.cache.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import weblogic.cache.CacheException;
import weblogic.cache.CacheValue;
import weblogic.cache.KeyEnumerator;
import weblogic.cache.webapp.CacheMonitor;
import weblogic.cache.webapp.CacheSystem;
import weblogic.cache.webapp.KeySet;
import weblogic.cache.webapp.ServletCacheUtils;
import weblogic.cache.webapp.WebAppCacheSystem;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.ServletResponseImpl;

public class CacheFilter implements Filter {
   public static final long MAX_CACHED_SIZE = 64000L;
   private static boolean verbose = false;
   private int timeout = -1;
   private String timeoutString;
   private String scope;
   private int size;
   private String key;
   private String vars;
   private boolean configured;
   private long maxSize = 64000L;
   private CacheMonitor cm;
   private FilterConfig config;

   protected KeySet getKeySet(CacheSystem var1) throws CacheException {
      if (this.key == null) {
         return null;
      } else {
         KeySet var2 = new KeySet(var1);
         KeyEnumerator var3 = new KeyEnumerator(this.key);

         while(var3.hasMoreKeys()) {
            String var4 = var3.getNextKey();
            String var5 = var3.getKeyScope();
            var2.addKey(var5, var4);
         }

         return var2;
      }
   }

   public void init(FilterConfig var1) throws ServletException {
      this.config = var1;
      this.timeoutString = var1.getInitParameter("timeout");
      this.scope = var1.getInitParameter("scope");
      if (this.scope == null) {
         this.scope = "application";
      }

      String var2 = var1.getInitParameter("max-cache-size");
      if (var2 == null) {
         this.maxSize = 64000L;
      } else {
         try {
            this.maxSize = Long.parseLong(var2);
         } catch (NumberFormatException var6) {
            this.maxSize = 64000L;
         }
      }

      String var3 = var1.getInitParameter("size");
      if (var3 == null) {
         this.size = -1;
      } else {
         try {
            this.size = Integer.parseInt(var3);
         } catch (NumberFormatException var5) {
            this.size = -1;
         }
      }

      this.key = var1.getInitParameter("key");
      this.vars = var1.getInitParameter("vars");
      String var4 = var1.getInitParameter("verbose");
      if (var4 != null && var4.equals("true")) {
         verbose = true;
      }

   }

   public void destroy() {
   }

   public void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws ServletException, IOException {
      if (verbose) {
         System.out.println("Filter start");
      }

      WebAppCacheSystem var4 = new WebAppCacheSystem();

      try {
         if (var1.getAttribute("weblogic.cache.tag.CacheTag.caching") != null) {
            throw new ServletException("Cannot cache inside another cache");
         }

         var1.setAttribute("weblogic.cache.tag.CacheTag.caching", "true");
         boolean var5 = var1.getAttribute("javax.servlet.include.request_uri") != null;
         HttpServletRequest var6 = (HttpServletRequest)var1;
         HttpServletResponse var7 = (HttpServletResponse)var2;
         ServletResponseImpl var8 = null;
         String var9 = this.config.getInitParameter("name");
         if (var9 == null) {
            var9 = var6.getRequestURI();
         } else {
            var6.setAttribute("uri", var6.getRequestURI());
         }

         try {
            if (this.timeout == -1 && this.timeoutString != null) {
               this.timeout = ServletCacheUtils.getTimeout(this.timeoutString);
            }

            var4.setRequest(var6);
            var4.setResponse(var7);
            var4.setContext(this.config.getServletContext());
            KeySet var14 = this.getKeySet(var4);
            CacheValue var12;
            if (this.key == null) {
               var12 = var4.getCache(this.scope, var9);
            } else {
               var12 = var4.getCache(this.scope, var9, this.size, var14);
            }

            int var15 = -1;
            byte[] var13;
            if (var12 == null) {
               if (verbose) {
                  System.out.println("No cache found for " + var9);
               }

               long var10 = System.currentTimeMillis();
               CacheServletResponse var40 = new CacheServletResponse(var7, this.maxSize);
               CacheServletRequest var41 = new CacheServletRequest(var6);
               var3.doFilter(var41, var40);
               var8 = this.getOriginalResponse(var7);
               var15 = var8.getStatus();
               if (verbose) {
                  System.out.println("status = " + var15);
               }

               long var18;
               int var20;
               if (var40.largeCL() || !var40.isCaching()) {
                  if (verbose) {
                     System.out.println("Not caching the response since it exceeded the maxSize: " + this.maxSize);
                  }

                  var18 = System.currentTimeMillis();
                  var20 = (int)(var18 - var10);
                  if (this.key == null) {
                     var4.setCache(this.scope, var9, (CacheValue)null, var20);
                  } else {
                     var4.setCache(this.scope, var9, this.size, var14, (CacheValue)null, var20);
                  }

                  return;
               }

               var13 = var40.getContent();
               if (var15 != 200) {
                  var18 = System.currentTimeMillis();
                  var20 = (int)(var18 - var10);
                  if (this.key == null) {
                     var4.setCache(this.scope, var9, (CacheValue)null, var20);
                  } else {
                     var4.setCache(this.scope, var9, this.size, var14, (CacheValue)null, var20);
                  }

                  if (var13 != null && var13.length > 0) {
                     var2.setContentLength(var13.length);

                     try {
                        var2.getOutputStream().write(var13, 0, var13.length);
                     } catch (IllegalStateException var36) {
                        var2.getWriter().write(new String(var13, var2.getCharacterEncoding()));
                     }

                     return;
                  }

                  return;
               }

               var18 = System.currentTimeMillis();
               var20 = (int)(var18 - var10);
               var12 = new CacheValue();
               var12.setContent(var13);
               var12.setTimeout(this.timeout);
               if (!var5) {
                  String var21 = var8.getHeader("Content-Type");
                  if (var21 != null) {
                     var12.setAttribute("Content-Type", var21);
                  }

                  String var22 = var8.getHeader("Last-Modified");
                  if (var22 == null) {
                     var7.setDateHeader("Last-Modified", var18);
                     var22 = var8.getHeader("Last-Modified");
                  }

                  if (var22 != null) {
                     var12.setAttribute("Last-Modified", var22);
                  }
               }

               ServletCacheUtils.saveVars(var4, var12, this.vars, "request");
               if (this.key == null) {
                  var4.setCache(this.scope, var9, var12, var20);
               } else {
                  var4.setCache(this.scope, var9, this.size, var14, var12, var20);
               }
            } else {
               if (verbose) {
                  System.out.println("Cache exists for " + var9);
               }

               String var16;
               if (!var5) {
                  var16 = (String)var12.getAttribute("Content-Type");
                  if (var16 != null) {
                     var7.setHeader("Content-Type", var16);
                  }

                  String var17 = (String)var12.getAttribute("Last-Modified");
                  if (var17 != null) {
                     var7.setHeader("Last-Modified", var17);
                  }
               }

               ServletCacheUtils.restoreVars(var4, var12, this.vars, "request");
               if (!var5) {
                  var16 = var6.getHeader("If-Modified-Since");
                  if (var16 != null && var16.equals(var12.getAttribute("Last-Modified"))) {
                     var7.setStatus(304);
                     return;
                  }
               }
            }

            if (!var5 && var6.getMethod().equals("HEAD")) {
               return;
            }

            if (var5 || var15 != 304) {
               var13 = (byte[])((byte[])var12.getContent());
               if (var13 != null && var13.length > 0) {
                  var2.setContentLength(var13.length);

                  try {
                     var2.getOutputStream().write(var13, 0, var13.length);
                  } catch (IllegalStateException var37) {
                     var2.getWriter().write(new String(var13, var2.getCharacterEncoding()));
                  }
               }
            }
         } catch (CacheException var38) {
            HTTPLogger.logNotCachingTheResponse(var6.getRequestURI(), var38.getMessage());
            var3.doFilter(var1, var2);
         }

         if (verbose) {
            System.out.println("Filter end");
         }
      } finally {
         var1.removeAttribute("weblogic.cache.tag.CacheTag.caching");

         try {
            var4.releaseAllLocks();
         } catch (CacheException var35) {
            throw new ServletException("Cache may be corrupt", var35);
         }
      }

   }

   private ServletResponseImpl getOriginalResponse(HttpServletResponse var1) {
      while(var1 instanceof ServletResponseWrapper) {
         ServletResponseWrapper var2 = (ServletResponseWrapper)var1;
         var1 = (HttpServletResponse)var2.getResponse();
      }

      if (var1 instanceof ServletResponseImpl) {
         return (ServletResponseImpl)var1;
      } else {
         return null;
      }
   }

   private static class CacheOutputStream extends ServletOutputStream {
      private ByteArrayOutputStream baos = new ByteArrayOutputStream();
      private ServletResponse res = null;
      private boolean isCaching = true;
      private long maxSize;
      private boolean useWriter = false;
      private CacheServletResponse csr;

      public CacheOutputStream(ServletResponse var1, long var2, CacheServletResponse var4) {
         this.res = var1;
         this.isCaching = true;
         this.maxSize = var2;
         this.csr = var4;
      }

      public byte[] getContent() {
         return this.baos.toByteArray();
      }

      public boolean isCaching() {
         return this.isCaching;
      }

      private void writeDirectly(int var1) throws IOException {
         if (this.useWriter) {
            this.res.getWriter().write((char)var1);
         } else {
            try {
               this.res.getOutputStream().write(var1);
            } catch (IllegalStateException var3) {
               this.res.getWriter().write((char)var1);
               this.useWriter = true;
            }
         }

      }

      private void writeDirectly(byte[] var1, int var2, int var3) throws IOException {
         if (this.useWriter) {
            this.res.getWriter().write(new String(var1, var2, var3, this.res.getCharacterEncoding()));
         } else {
            try {
               this.res.getOutputStream().write(var1, var2, var3);
            } catch (IllegalStateException var5) {
               this.res.getWriter().write(new String(var1, var2, var3, this.res.getCharacterEncoding()));
               this.useWriter = true;
            }
         }

      }

      public void write(int var1) throws IOException {
         if (!this.csr.largeCL() && this.isCaching) {
            if ((long)this.baos.size() >= this.maxSize) {
               this.isCaching = false;
               byte[] var2 = this.baos.toByteArray();
               if (var2 != null && var2.length > 0) {
                  this.writeDirectly(var2, 0, var2.length);
               }

               this.writeDirectly(var1);
            } else {
               this.baos.write(var1);
            }
         } else {
            this.writeDirectly(var1);
         }
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         if (!this.csr.largeCL() && this.isCaching) {
            if ((long)(this.baos.size() + var3) > this.maxSize) {
               this.isCaching = false;
               byte[] var4 = this.baos.toByteArray();
               if (var4 != null && var4.length > 0) {
                  this.writeDirectly(var4, 0, var4.length);
               }

               this.writeDirectly(var1, var2, var3);
            } else {
               this.baos.write(var1, var2, var3);
            }
         } else {
            this.writeDirectly(var1, var2, var3);
         }
      }
   }

   private static class CacheServletRequest extends HttpServletRequestWrapper {
      public CacheServletRequest(HttpServletRequest var1) {
         super(var1);
      }

      public String getHeader(String var1) {
         return var1.equals("If-Modified-Since") ? null : super.getHeader(var1);
      }

      public long getDateHeader(String var1) {
         return var1.equals("If-Modified-Since") ? 0L : super.getDateHeader(var1);
      }
   }

   private static class CacheServletResponse extends HttpServletResponseWrapper {
      private PrintWriter writer;
      private ServletOutputStream sos;
      private CacheOutputStream cos;
      private long maxSize;
      boolean largeCL = false;

      public CacheServletResponse(HttpServletResponse var1, long var2) throws IOException {
         super(var1);
         this.maxSize = var2;
         this.cos = new CacheOutputStream(var1, var2, this);
      }

      public byte[] getContent() {
         return this.cos.getContent();
      }

      public boolean isCaching() {
         return this.cos.isCaching();
      }

      public boolean largeCL() {
         return this.largeCL;
      }

      public ServletOutputStream getOutputStream() throws IOException {
         if (this.writer != null) {
            throw new IllegalStateException("Cannot get Writer then OutputStream");
         } else {
            this.sos = this.cos;
            return this.sos;
         }
      }

      public PrintWriter getWriter() throws IOException {
         if (this.sos != null) {
            throw new IllegalStateException("Cannot get OutputStream then Writer");
         } else {
            if (this.writer == null) {
               this.writer = new PrintWriter(new OutputStreamWriter(this.cos, this.getCharacterEncoding()));
            }

            return this.writer;
         }
      }

      public void setHeader(String var1, String var2) {
         if (var1.equalsIgnoreCase("Content-Length")) {
            this.setContentLength(Integer.parseInt(var2));
         } else {
            super.setHeader(var1, var2);
         }
      }

      public void addHeader(String var1, String var2) {
         if (var1.equalsIgnoreCase("Content-Length")) {
            this.setContentLength(Integer.parseInt(var2));
         } else {
            super.addHeader(var1, var2);
         }
      }

      public void setContentLength(int var1) {
         if ((long)var1 > this.maxSize) {
            this.largeCL = true;
         }

         super.setContentLength(var1);
      }

      public void flushBuffer() {
         try {
            if (this.writer != null) {
               this.writer.flush();
            }

            if (this.sos != null) {
               this.sos.flush();
            }
         } catch (IOException var2) {
            if (CacheFilter.verbose) {
               var2.printStackTrace();
            }
         }

      }

      public void resetBuffer() {
         this.cos = new CacheOutputStream(this.getResponse(), this.maxSize, this);
         if (this.writer != null) {
            try {
               this.writer = new PrintWriter(new OutputStreamWriter(this.cos, this.getCharacterEncoding()));
            } catch (UnsupportedEncodingException var2) {
            }
         } else if (this.sos != null) {
            this.sos = this.cos;
         }

      }
   }
}
