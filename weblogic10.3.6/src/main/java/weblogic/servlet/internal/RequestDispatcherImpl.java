package weblogic.servlet.internal;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpSession;
import weblogic.servlet.internal.session.SessionInternal;
import weblogic.utils.http.HttpParsing;

public final class RequestDispatcherImpl implements RequestDispatcher {
   public static final String REQUEST_URI_INCLUDE = "javax.servlet.include.request_uri";
   public static final String CONTEXT_PATH_INCLUDE = "javax.servlet.include.context_path";
   public static final String SERVLET_PATH_INCLUDE = "javax.servlet.include.servlet_path";
   public static final String PATH_INFO_INCLUDE = "javax.servlet.include.path_info";
   public static final String QUERY_STRING_INCLUDE = "javax.servlet.include.query_string";
   public static final String REQUEST_URI_FORWARD = "javax.servlet.forward.request_uri";
   private static final String CONTEXT_PATH_FORWARD = "javax.servlet.forward.context_path";
   private static final String SERVLET_PATH_FORWARD = "javax.servlet.forward.servlet_path";
   private static final String PATH_INFO_FORWARD = "javax.servlet.forward.path_info";
   private static final String QUERY_STRING_FORWARD = "javax.servlet.forward.query_string";
   private static final String SERVLET_PATH_FORWARD_TARGET = "weblogic.servlet.forward.target_servlet_path";
   static final String CROSS_CONTEXT_PATH = "weblogic.servlet.internal.crosscontext.path";
   static final String CROSS_CONTEXT_TYPE = "weblogic.servlet.internal.crosscontext.type";
   static final String INCLUDE = "include";
   static final String FORWARD = "forward";
   private final WebAppServletContext context;
   private final String requestPath;
   private final String param;
   private final int mode;
   private ServletStubImpl sstub;
   private boolean filtersDiabled;
   private boolean namedDispatcher;

   public RequestDispatcherImpl(String var1, WebAppServletContext var2, int var3) {
      this(var1, (String)null, var2, var3);
   }

   public RequestDispatcherImpl(String var1, String var2, WebAppServletContext var3, int var4) {
      this.filtersDiabled = false;
      this.namedDispatcher = false;
      this.context = var3;
      String var5 = var3.getContextPath();
      if (var5 != null && var5.length() > 1) {
         this.requestPath = var5 + var1;
      } else {
         this.requestPath = var1;
      }

      this.param = var2;
      this.sstub = null;
      this.mode = var4;
   }

   public RequestDispatcherImpl(ServletStubImpl var1, WebAppServletContext var2, int var3) {
      this.filtersDiabled = false;
      this.namedDispatcher = false;
      this.requestPath = null;
      this.param = null;
      this.context = var2;
      this.sstub = var1;
      this.mode = var3;
      this.namedDispatcher = true;
   }

   public void forward(ServletRequest var1, ServletResponse var2) throws IOException, ServletException {
      ServletRequestImpl var3 = ServletRequestImpl.getOriginalRequest(var1);
      ServletResponseImpl var4 = null;

      try {
         var4 = ServletResponseImpl.getOriginalResponse(var2);
      } catch (ClassCastException var65) {
         var4 = var3.getResponse();
      }

      if (var2.isCommitted()) {
         throw new IllegalStateException("Cannot forward a response that is already committed");
      } else {
         boolean var5 = var2 instanceof ServletResponseWrapper;
         if (var5) {
            var2.resetBuffer();
         } else {
            var4.resetBuffer();
         }

         var4.resetOutputState();
         this.clearIncludeAttributes(var1);
         Object var6 = var1.getAttribute("javax.servlet.forward.request_uri");
         Object var7 = var1.getAttribute("javax.servlet.forward.context_path");
         Object var8 = var1.getAttribute("javax.servlet.forward.servlet_path");
         Object var9 = var1.getAttribute("javax.servlet.forward.path_info");
         Object var10 = var1.getAttribute("javax.servlet.forward.query_string");
         Object var11 = var1.getAttribute("weblogic.servlet.forward.target_servlet_path");
         if (this.sstub == null && var1.getAttribute("javax.servlet.forward.request_uri") == null) {
            this.setAttributesForForward(var1, var3);
         }

         boolean var12 = false;
         HttpSession var13 = null;
         boolean var14 = false;
         WebAppServletContext var15 = var3.getContext();
         boolean var16 = var15 != this.context;
         ClassLoader var17 = null;
         Thread var18 = null;
         Object var19 = null;

         try {
            if (var16) {
               var18 = Thread.currentThread();
               var17 = this.context.pushEnvironment(var18);
               var19 = var3.getAttribute("weblogic.servlet.internal.crosscontext.type");
               var3.initContext(this.context);
               var4.initContext(this.context);
               var3.setAttribute("weblogic.servlet.internal.crosscontext.type", "forward");
               var13 = var3.getSession(false);
               var14 = var3.getSessionHelper().getSessionExistanceChecked();
               if (var13 != null) {
                  if (var13.isNew()) {
                     var3.getSessionHelper().rememberSessionID(var15.getSessionContext().getConfigMgr().getCookieName(), var15.getSessionContext().getConfigMgr().getCookiePath(), ((SessionInternal)var13).getIdWithServerInfo());
                  }

                  var15.exitingContext(var3, var4, var13);
               }

               boolean var20 = !this.isSessionCookieShared(var15);
               var3.getSessionHelper().resetSession(var20);
            }

            if (!this.namedDispatcher) {
               if (this.requestPath == null) {
                  throw new ServletException("Cannot resolve request - requestPath was null");
               }

               var3.initFromRequestURI(this.requestPath);
               var3.addForwardParameter(this.param);
               var3.initInputEncoding();
               this.sstub = this.context.resolveForwardedRequest(var3, var1);
               if (var3.getSendRedirect()) {
                  var4.sendRedirect(var3.getRedirectURI());
                  return;
               }

               if (this.sstub == null) {
                  throw new ServletException("Cannot forward request - servlet for path: '" + this.requestPath + "' not found.");
               }
            }

            if (!this.context.getConfigManager().isCheckAuthOnForwardEnabled() || this.namedDispatcher || this.context.getSecurityManager().checkAccess(var3, var4, false)) {
               ServletResponse var69 = var2;
               ServletResponseWrapper var21 = null;
               boolean var22 = false;
               if (var2 instanceof ServletResponseWrapper && !(var2 instanceof RemoveWrapperOnForward)) {
                  var22 = true;
                  var21 = (ServletResponseWrapper)var2;
               }

               while(var69 instanceof ServletResponseWrapper) {
                  var69 = ((ServletResponseWrapper)var69).getResponse();
                  if (!(var69 instanceof RemoveWrapperOnForward)) {
                     if (!var22) {
                        var2 = var69;
                        var22 = true;
                     }

                     if (var21 != null) {
                        var21.setResponse(var69);
                     }

                     if (var69 instanceof ServletResponseWrapper) {
                        var21 = (ServletResponseWrapper)var69;
                     }
                  }
               }

               var1.setAttribute("weblogic.servlet.forward.target_servlet_path", this.namedDispatcher ? null : var3.getServletPath());
               if (var2 instanceof ServletResponseImpl) {
                  var5 = false;
               }

               int var23 = this.mode;
               if (var23 == -1) {
                  var23 = 1;
               }

               this.invokeServlet(var16, var1, var2, var4, var23);
               return;
            }
         } catch (Throwable var67) {
            if (!var4.isCommitted()) {
               var12 = true;
            }

            if (var67 instanceof ServletException) {
               throw (ServletException)var67;
            }

            if (var67 instanceof IOException) {
               throw (IOException)var67;
            }

            if (var67 instanceof RuntimeException) {
               throw (RuntimeException)var67;
            }

            var12 = false;
            throw new ServletException(var67);
         } finally {
            try {
               if (!var12) {
                  ServletOutputStreamImpl var26 = (ServletOutputStreamImpl)var4.getOutputStreamNoCheck();
                  if (var5) {
                     var2.flushBuffer();
                  } else {
                     var26.getOutput().setWriteEnabled(false);
                     var26.commit();
                  }
               }
            } finally {
               if (var16) {
                  HttpSession var29 = var3.getSession(false);
                  if (var29 != null) {
                     this.context.exitingContext(var3, var4, var29);
                  }

                  WebAppServletContext.popEnvironment(var18, var17);
                  var3.setAttribute("weblogic.servlet.internal.crosscontext.type", var19);
                  var3.initContext(var15);
                  var4.initContext(var15);
                  var3.getSessionHelper().setSession(var13);
                  var3.getSessionHelper().setSessionExistanceChecked(var14);
                  if (var13 != null) {
                     var15.enteringContext(var3, var4, var13);
                  }
               }

               if (this.param != null && !this.namedDispatcher) {
                  var3.removeRequestDispatcherQueryString();
               }

               var3.initFromRequestParser();
               var1.setAttribute("javax.servlet.forward.request_uri", var6);
               var1.setAttribute("javax.servlet.forward.context_path", var7);
               var1.setAttribute("javax.servlet.forward.servlet_path", var8);
               var1.setAttribute("javax.servlet.forward.path_info", var9);
               var1.setAttribute("javax.servlet.forward.query_string", var10);
               var1.setAttribute("weblogic.servlet.forward.target_servlet_path", var11);
            }

         }

      }
   }

   public void include(ServletRequest var1, ServletResponse var2) throws IOException, ServletException {
      ServletRequestImpl var3 = ServletRequestImpl.getOriginalRequest(var1);
      ServletResponseImpl var4 = var3.getResponse();
      ServletOutputStreamImpl var5 = (ServletOutputStreamImpl)var4.getOutputStreamNoCheck();
      boolean var6 = var5.getDoFinish();
      Object var7 = var1.getAttribute("javax.servlet.include.request_uri");
      Object var8 = var1.getAttribute("javax.servlet.include.context_path");
      Object var9 = var1.getAttribute("javax.servlet.include.servlet_path");
      Object var10 = var1.getAttribute("javax.servlet.include.path_info");
      Object var11 = var1.getAttribute("javax.servlet.include.query_string");
      HttpSession var12 = null;
      boolean var13 = false;
      WebAppServletContext var14 = var3.getContext();
      boolean var15 = var14 != this.context;
      boolean var16 = var2 instanceof ServletResponseWrapper;
      Thread var17 = null;
      ClassLoader var18 = null;
      Object var19 = null;
      Object var20 = null;

      try {
         if (var15) {
            var17 = Thread.currentThread();
            var18 = this.context.pushEnvironment(var17);
            var12 = var3.getSession(false);
            var13 = var3.getSessionHelper().getSessionExistanceChecked();
            var19 = var3.getAttribute("weblogic.servlet.internal.crosscontext.path");
            var20 = var3.getAttribute("weblogic.servlet.internal.crosscontext.type");
            var3.initContext(this.context);
            var4.initContext(this.context);
            var3.setAttribute("weblogic.servlet.internal.crosscontext.path", var14.getContextPath());
            var3.setAttribute("weblogic.servlet.internal.crosscontext.type", "include");
            if (var12 != null) {
               if (var12.isNew()) {
                  var3.getSessionHelper().rememberSessionID(var14.getSessionContext().getConfigMgr().getCookieName(), var14.getSessionContext().getConfigMgr().getCookiePath(), ((SessionInternal)var12).getIdWithServerInfo());
               }

               var14.exitingContext(var3, var4, var12);
            }

            boolean var21 = !this.isSessionCookieShared(var14);
            var3.getSessionHelper().resetSession(var21);
         }

         if (!this.namedDispatcher) {
            if (this.requestPath == null) {
               throw new ServletException("requestPath was null");
            }

            String var28 = this.requestPath;
            var1.setAttribute("javax.servlet.include.context_path", this.context.getContextPath());
            if (this.param != null) {
               var1.setAttribute("javax.servlet.include.query_string", this.param);
               var3.addIncludeParameter(this.param);
            }

            var28 = HttpParsing.unescape(var28).trim();
            var1.setAttribute("javax.servlet.include.request_uri", var28);
            this.sstub = this.context.resolveIncludedRequest(var3, var1);
            if (this.sstub == null) {
               throw new ServletException("Failed to resolve path: " + var28);
            }
         }

         Object var29;
         if (!var16) {
            var29 = new NestedServletResponse(var4);
         } else {
            var29 = nestWrapperResponse(var2);
         }

         var5.setDoFinish(false);
         int var22 = this.mode;
         if (var22 == -1) {
            var22 = 2;
         }

         this.invokeServlet(var15, var1, (ServletResponse)var29, var4, var22);
      } finally {
         if (var15) {
            HttpSession var25 = var3.getSession(false);
            if (var25 != null) {
               this.context.exitingContext(var3, var4, var25);
            }

            WebAppServletContext.popEnvironment(var17, var18);
            var3.initContext(var14);
            var4.initContext(var14);
            if (var12 != null) {
               var14.enteringContext(var3, var4, var12);
            }

            var3.getSessionHelper().setSession(var12);
            var3.getSessionHelper().setSessionExistanceChecked(var13);
            var3.setAttribute("weblogic.servlet.internal.crosscontext.path", var19);
            var3.setAttribute("weblogic.servlet.internal.crosscontext.type", var20);
         }

         var5.setDoFinish(var6);
         if (this.param != null && !this.namedDispatcher) {
            var3.removeRequestDispatcherQueryString();
         }

         var1.setAttribute("javax.servlet.include.request_uri", var7);
         var1.setAttribute("javax.servlet.include.context_path", var8);
         var1.setAttribute("javax.servlet.include.servlet_path", var9);
         var1.setAttribute("javax.servlet.include.path_info", var10);
         var1.setAttribute("javax.servlet.include.query_string", var11);
         if (var16) {
            var2 = unsetNestedWrapper(var2);
         }

      }

   }

   private boolean isSessionCookieShared(WebAppServletContext var1) {
      return var1.getSessionContext().getConfigMgr().getCookieName().equals(this.context.getSessionContext().getConfigMgr().getCookieName()) && var1.getSessionContext().getConfigMgr().getCookiePath().equals(this.context.getSessionContext().getConfigMgr().getCookiePath());
   }

   private void clearIncludeAttributes(ServletRequest var1) {
      var1.setAttribute("javax.servlet.include.request_uri", (Object)null);
      var1.setAttribute("javax.servlet.include.context_path", (Object)null);
      var1.setAttribute("javax.servlet.include.servlet_path", (Object)null);
      var1.setAttribute("javax.servlet.include.path_info", (Object)null);
      var1.setAttribute("javax.servlet.include.query_string", (Object)null);
      var1.setAttribute("weblogic.servlet.BodyTagOutput", (Object)null);
      var1.setAttribute("javax.servlet.jsp.PageContext.out", (Object)null);
   }

   private void setAttributesForForward(ServletRequest var1, ServletRequestImpl var2) {
      var1.setAttribute("javax.servlet.forward.request_uri", var2.getRequestURI());
      var1.setAttribute("javax.servlet.forward.context_path", var2.getContextPath());
      var1.setAttribute("javax.servlet.forward.servlet_path", var2.getServletPath());
      var1.setAttribute("javax.servlet.forward.path_info", var2.getPathInfo());
      var1.setAttribute("javax.servlet.forward.query_string", var2.getOriginalQueryString());
   }

   public void disableFilters() {
      this.filtersDiabled = true;
   }

   private void invokeServlet(boolean var1, ServletRequest var2, ServletResponse var3, ServletResponseImpl var4, int var5) throws IOException, ServletException {
      boolean var6 = var1 && this.context.getEventsManager().hasRequestListeners();
      if (this.filtersDiabled || !this.context.getFilterManager().hasFilters() && !var6) {
         this.sstub.execute(var2, var3);
      } else {
         FilterChainImpl var7 = this.context.getFilterManager().getFilterChain(this.sstub, var2, var4, var6, var5);
         if (var7 == null) {
            this.sstub.execute(var2, var3);
         } else {
            var7.doFilter(var2, var3);
         }
      }

   }

   private static ServletResponse nestWrapperResponse(ServletResponse var0) {
      ServletResponse var1 = var0;

      ServletResponseWrapper var2;
      for(var2 = null; var0 instanceof ServletResponseWrapper; var0 = ((ServletResponseWrapper)var0).getResponse()) {
         var2 = (ServletResponseWrapper)var0;
      }

      if (var2 != null) {
         var2.setResponse(new NestedServletResponse(var0));
      }

      return var1;
   }

   private static ServletResponse unsetNestedWrapper(ServletResponse var0) {
      ServletResponse var1 = var0;

      ServletResponseWrapper var2;
      for(var2 = null; var0 instanceof ServletResponseWrapper; var0 = ((ServletResponseWrapper)var0).getResponse()) {
         var2 = (ServletResponseWrapper)var0;
      }

      if (var0 instanceof NestedServletResponse) {
         ServletResponse var3 = ((NestedServletResponse)var0).getOriginalResponse();
         if (var3 instanceof ServletResponseImpl && var2 != null) {
            var2.setResponse(var3);
         }
      }

      return var1;
   }
}
