package weblogic.servlet.internal;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.HttpJspPage;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ServletRuntimeMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.jsp.AddToMapException;
import weblogic.servlet.jsp.CompilationException;
import weblogic.servlet.jsp.JspFileNotFoundException;
import weblogic.servlet.jsp.JspStub;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.ChangeAwareClassLoader;
import weblogic.utils.enumerations.EmptyEnumerator;
import weblogic.utils.enumerations.IteratorEnumerator;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class ServletStubImpl implements ServletConfig {
   static final String DISPATCH_POLICY = "wl-dispatch-policy";
   private static final String CLASSPATH_SERVLET_NAME = "weblogic.servlet.ClasspathServlet";
   private static final String FILE_SERVLET_NAME = "weblogic.servlet.FileServlet";
   private static final String HTTP_CLUSTER_SERVLET_NAME = "weblogic.servlet.proxy.HttpClusterServlet";
   private static final String HTTP_PROXY_SERVLET_NAME = "weblogic.servlet.proxy.HttpProxyServlet";
   private static final String PUBSUB_CONTROLLER_SERVLET_NAME = "com.bea.httppubsub.servlet.ControllerServlet";
   private final String name;
   protected final String className;
   private final WebAppServletContext context;
   private final Map initParams;
   private final StubSecurityHelper securityHelper;
   private WorkManager workManager;
   private StubLifecycleHelper lifecycleHelper;
   private WorkManager sessionFetchingWorkManager;
   private boolean filtersInvoked = false;
   private boolean isInternalServlet = false;
   private final boolean isClasspathServlet;
   private final boolean isFileServlet;
   private final boolean isProxyServlet;
   private final boolean isPubSubControllerServlet;
   private boolean dynamicallyGenerated = false;
   private ServletRuntimeMBeanImpl runtime;
   static final long serialVersionUID = -7625015173865500484L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.servlet.internal.ServletStubImpl");
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Execute_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public ServletStubImpl(String var1, String var2, WebAppServletContext var3, Map var4) {
      this.name = var1;
      this.className = var2;
      this.isClasspathServlet = "weblogic.servlet.ClasspathServlet".equals(var2);
      this.isFileServlet = "weblogic.servlet.FileServlet".equals(var2);
      this.isProxyServlet = "weblogic.servlet.proxy.HttpClusterServlet".equals(var2) || "weblogic.servlet.proxy.HttpProxyServlet".equals(var2);
      this.isPubSubControllerServlet = "com.bea.httppubsub.servlet.ControllerServlet".equals(var2);
      this.context = var3;
      this.initParams = var4;
      this.setDispatchPolicy(this.getInitParameter("wl-dispatch-policy"));
      String var5 = var3.getServer().getMBean().getWorkManagerForRemoteSessionFetching();
      this.setWorkManagerForSessionFetching(var5);
      this.securityHelper = new StubSecurityHelper(this);
   }

   public final void initRuntime() throws ManagementException {
      Debug.assertion(this.context.getRuntimeMBean() != null);
      if (this.runtime == null) {
         this.runtime = ServletRuntimeMBeanImpl.newInstance(this);
      }
   }

   final void setDispatchPolicy(String var1) {
      if (var1 != null) {
         this.workManager = WorkManagerFactory.getInstance().find(var1, this.context.getApplicationId(), this.context.getId());
      }

   }

   private void setWorkManagerForSessionFetching(String var1) {
      if (var1 != null) {
         this.sessionFetchingWorkManager = WorkManagerFactory.getInstance().find(var1, this.context.getApplicationId(), this.context.getId());
      }

   }

   final WorkManager getWorkManagerForSessionFetching() {
      return this.sessionFetchingWorkManager;
   }

   final WorkManager getWorkManager() {
      return this.workManager != null ? this.workManager : this.context.getConfigManager().getWorkManager();
   }

   protected String getDefaultContentType() {
      return null;
   }

   final ServletRuntimeMBean getRuntimeMBean() {
      return this.runtime;
   }

   final boolean isSingleThreadModel() {
      return this.lifecycleHelper == null ? false : this.lifecycleHelper.isSingleThreadModel();
   }

   final boolean isFutureResponseServlet() {
      return this.lifecycleHelper == null ? false : this.lifecycleHelper.isFutureResponseServlet();
   }

   public final boolean isProxyServlet() {
      return this.isProxyServlet;
   }

   final boolean isInternalServlet() {
      return this.isInternalServlet;
   }

   final void setInternalServlet(boolean var1) {
      this.isInternalServlet = var1;
   }

   public final String getClassName() {
      return this.className;
   }

   public final boolean isClasspathServlet() {
      return this.isClasspathServlet;
   }

   public final boolean isFileServlet() {
      return this.isFileServlet;
   }

   public final boolean isPubSubControllerServlet() {
      return this.isPubSubControllerServlet;
   }

   public final void execute(ServletRequest var1, ServletResponse var2) throws ServletException, IOException {
      this.execute(var1, var2, (FilterChainImpl)null);
   }

   public void execute(ServletRequest var1, ServletResponse var2, FilterChainImpl var3) throws ServletException, IOException {
      boolean var20;
      boolean var10000 = var20 = _WLDF$INST_FLD_Servlet_Execute_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var21 = null;
      DiagnosticActionState[] var22 = null;
      Object var19 = null;
      if (var10000) {
         Object[] var15 = null;
         if (_WLDF$INST_FLD_Servlet_Execute_Around_Medium.isArgumentsCaptureNeeded()) {
            var15 = new Object[]{this, var1, var2, var3};
         }

         DynamicJoinPoint var40 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var15, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Servlet_Execute_Around_Medium;
         DiagnosticAction[] var10002 = var21 = var10001.getActions();
         InstrumentationSupport.preProcess(var40, var10001, var10002, var22 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         ServletRequestImpl var4;
         ServletResponseImpl var5;
         if (var3 == null) {
            var4 = ServletRequestImpl.getOriginalRequest(var1);
            var5 = var4.getResponse();
         } else {
            if (!this.filtersInvoked) {
               this.filtersInvoked = true;
            }

            var4 = var3.getOrigRequest();
            var5 = var3.getOrigResponse();
         }

         Object var6;
         if (var1 instanceof HttpServletRequest) {
            var6 = (HttpServletRequest)var1;
         } else {
            var6 = var4;
         }

         Object var7;
         if (var2 instanceof HttpServletResponse) {
            var7 = (HttpServletResponse)var2;
         } else {
            var7 = var5;
         }

         if (this.getDefaultContentType() != null) {
            var2.setContentType(this.getDefaultContentType());
         }

         Servlet var8;
         try {
            RequestCallbackImpl var9 = new RequestCallbackImpl((HttpServletRequest)var6, (HttpServletResponse)var7, var5);
            if (this.lifecycleHelper != null) {
               this.checkForReload(var9);
            } else {
               synchronized(this) {
                  if (this.lifecycleHelper == null) {
                     this.prepareServlet(var9);
                  }
               }
            }

            var8 = this.lifecycleHelper.getServlet();
         } catch (JspFileNotFoundException var34) {
            this.context.removeServletStub(this, false);
            throw var34;
         } catch (CompilationException var35) {
            ((HttpServletRequest)var6).setAttribute("javax.servlet.error.exception", var35);
            this.context.getErrorManager().setErrorAttributes((HttpServletRequest)var6, var35.getJavaFileName(), var35);
            this.context.removeServletStub(this, false);
            throw new ServletException(var35);
         }

         if (var8 instanceof HttpJspPage) {
            String var38 = var4.getRequestParameters().peekParameter("jsp_precompile");
            if (var38 != null) {
               if ("".equals(var38) || "true".equals(var38)) {
                  return;
               }

               if (!"false".equals(var38)) {
                  var5.sendError(500);
                  return;
               }
            }
         }

         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("Start to execute servlet: " + this.toString());
         }

         long var39 = System.currentTimeMillis();

         try {
            Throwable var11 = this.securityHelper.invokeServlet(var1, (HttpServletRequest)var6, var4, var2, (HttpServletResponse)var7, var8);
            if (var11 != null) {
               if (!(var11 instanceof AddToMapException)) {
                  if (var11 instanceof UnavailableException) {
                     UnavailableException var12 = (UnavailableException)var11;
                     if (var12.isPermanent()) {
                        this.context.removeServletStub(this, true);
                     } else {
                        this.lifecycleHelper.makeUnavailable(var12);
                     }

                     throw var12;
                  }

                  if (var11 instanceof ServletException) {
                     throw (ServletException)var11;
                  }

                  if (var11 instanceof IOException) {
                     throw (IOException)var11;
                  }

                  if (var11 instanceof RuntimeException) {
                     throw (RuntimeException)var11;
                  }

                  throw new ServletException(var11);
               }

               this.onAddToMapException(var11, var4, var1, var2, var3);
            }
         } finally {
            this.recordInvoke(var39);
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug("Servlet execution completed: " + this.toString());
            }

            if (this.lifecycleHelper != null) {
               this.lifecycleHelper.returnServlet(var8);
            }

         }
      } finally {
         if (var20) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_Execute_Around_Medium, var21, var22);
         }

      }

   }

   private void onAddToMapException(Throwable var1, ServletRequestImpl var2, ServletRequest var3, ServletResponse var4, FilterChainImpl var5) throws ServletException, IOException {
      AddToMapException var6 = (AddToMapException)var1;
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.context.getLogContext() + ": registering servlet stub dynamically for the JSP : " + var2.getRequestURI());
      }

      ServletStubImpl var7;
      synchronized(this.context) {
         var7 = this.context.getServletStub(var6.pattern);
         if (var7 != null) {
            if (var7.getClassName().equals(this.getClassName())) {
               var7 = null;
            } else if (var7 != var6.sstub) {
               if (var7 instanceof JspStub && var6.sstub instanceof JspStub) {
                  var6.sstub.destroy();
               } else {
                  var7 = null;
               }
            }
         }

         if (var7 == null) {
            String var9 = var6.pattern;
            if (!this.context.getJSPManager().isJspExactMapping()) {
               var9 = var9 + "/*";
            }

            this.context.registerServletStub(var9, var6.sstub);
            this.context.registerServletMap(var9, var9, var6.sstub);
            var7 = var6.sstub;

            try {
               var7.initRuntime();
            } catch (ManagementException var12) {
               throw new ServletException(var12);
            }
         }
      }

      if (var2.getServletStub() == this) {
         var2.setServletStub(var7);
      }

      var7.dynamicallyGenerated = true;
      var7.execute(var3, var4, var5);
   }

   private void recordInvoke(long var1) {
      if (this.runtime != null) {
         this.runtime.incInvocationCount();
         long var3 = System.currentTimeMillis() - var1;
         if (var3 >= 0L) {
            this.runtime.setExecutionTimeHighLow(var3);
            this.runtime.addExecutionTimeTotal(var3);
         }

      }
   }

   protected final synchronized void destroy() {
      if (this.lifecycleHelper != null) {
         this.lifecycleHelper.destroy();
         this.lifecycleHelper = null;
      }

      if (this.runtime != null) {
         this.runtime.destroy();
         this.runtime = null;
      }

   }

   protected void checkForReload(RequestCallback var1) throws ServletException, UnavailableException, IOException {
      long var2 = (long)this.context.getConfigManager().getServletReloadCheckSecs();
      if (var2 >= 0L) {
         if (this.lifecycleHelper.getContextLoader() != this.context.getServletClassLoader()) {
            synchronized(this) {
               if (this.lifecycleHelper.getContextLoader() != this.context.getServletClassLoader()) {
                  this.lifecycleHelper.destroy();
                  this.context.removeTransientAttributes(this.lifecycleHelper.getContextLoader());
                  this.prepareServlet(var1);
               }
            }
         } else if (!this.filtersInvoked && this.checkReloadTimeout(var2)) {
            synchronized(this) {
               if (this.lifecycleHelper.getContextLoader() != this.context.getServletClassLoader()) {
                  this.lifecycleHelper.destroy();
                  this.context.removeTransientAttributes(this.lifecycleHelper.getContextLoader());
                  this.prepareServlet(var1);
               } else if (this.checkReloadTimeout(var2) && this.needToReload()) {
                  this.lifecycleHelper.destroy();
                  this.context.reloadServletClassLoader();
                  this.prepareServlet(var1);
               }
            }
         }

      }
   }

   private boolean checkReloadTimeout(long var1) {
      if (this.lifecycleHelper == null) {
         return true;
      } else {
         ClassLoader var3 = this.lifecycleHelper.getContextLoader();
         if (!(var3 instanceof ChangeAwareClassLoader)) {
            return false;
         } else {
            ChangeAwareClassLoader var4 = (ChangeAwareClassLoader)var3;
            long var5 = System.currentTimeMillis();
            return var5 - var1 * 1000L > var4.getLastChecked();
         }
      }
   }

   private boolean needToReload() {
      if (this.lifecycleHelper == null) {
         return false;
      } else {
         ClassLoader var1 = this.lifecycleHelper.getContextLoader();
         if (!(var1 instanceof ChangeAwareClassLoader)) {
            return false;
         } else {
            ChangeAwareClassLoader var2 = (ChangeAwareClassLoader)var1;
            return !var2.upToDate();
         }
      }
   }

   protected void prepareServlet(RequestCallback var1) throws ServletException, UnavailableException, IOException {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.context.getLogContext() + ": Preparing servlet: " + this.name);
      }

      String var3;
      String var4;
      Loggable var5;
      Loggable var13;
      try {
         ClassLoader var2 = this.getClassLoader();
         Class var14 = var2.loadClass(this.className);
         if (this.runtime != null) {
            this.runtime.incReloadCount();
         }

         this.lifecycleHelper = new StubLifecycleHelper(this, var14, this.context.getServletClassLoader());
      } catch (ClassNotFoundException var7) {
         var3 = this.context.getClasspath();
         var4 = this.context.getLogContext();
         Object var15 = var7.getException();
         if (var15 == null) {
            var15 = var7;
         }

         Loggable var6 = HTTPLogger.logServletClassNotFoundLoggable(var4, this.name, this.className, var3, (Throwable)var15);
         var6.log();
         throw new ServletException(var6.getMessage().replace(var3, ""));
      } catch (NoClassDefFoundError var8) {
         var3 = this.context.getClasspath();
         var4 = this.context.getLogContext();
         var5 = HTTPLogger.logServletClassDefNotFoundLoggable(var4, this.name, this.className, var3, var8);
         var5.log();
         throw new ServletException(var5.getMessage().replace(var3, ""));
      } catch (UnsatisfiedLinkError var9) {
         var3 = this.context.getClasspath();
         var4 = this.context.getLogContext();
         var5 = HTTPLogger.logServletUnsatisfiedLinkLoggable(var4, this.name, this.className, var3, var9);
         var5.log();
         throw new ServletException(var5.getMessage().replace(var3, ""));
      } catch (VerifyError var10) {
         var3 = this.context.getLogContext();
         var13 = HTTPLogger.logServletVerifyErrorLoggable(var3, this.name, this.className, var10);
         var13.log();
         throw new ServletException(var13.getMessage());
      } catch (ClassFormatError var11) {
         var3 = this.context.getLogContext();
         var13 = HTTPLogger.logServletClassFormatErrorLoggable(var3, this.name, this.className, var11);
         var13.log();
         throw new ServletException(var13.getMessage());
      } catch (LinkageError var12) {
         var3 = this.context.getClasspath();
         var4 = this.context.getLogContext();
         var5 = HTTPLogger.logServletLinkageErrorLoggable(var4, this.name, this.className, var3, var12);
         var5.log();
         throw new ServletException(var5.getMessage().replace(var3, ""));
      }
   }

   protected ClassLoader getClassLoader() {
      return this.context.getServletClassLoader();
   }

   public final ServletContext getServletContext() {
      return this.context;
   }

   public final String getServletName() {
      return this.name;
   }

   Map getInitParametersMap() {
      return this.initParams;
   }

   public final Enumeration getInitParameterNames() {
      return (Enumeration)(this.initParams == null ? new EmptyEnumerator() : new IteratorEnumerator(this.initParams.keySet().iterator()));
   }

   public final String getInitParameter(String var1) {
      return this.initParams == null ? null : (String)this.initParams.get(var1);
   }

   public final String toString() {
      return super.toString() + " - " + this.getServletName() + " class: '" + this.className + "'";
   }

   public final boolean isDynamicallyGenerated() {
      return this.dynamicallyGenerated;
   }

   public final StubSecurityHelper getSecurityHelper() {
      return this.securityHelper;
   }

   public StubLifecycleHelper getLifecycleHelper() {
      return this.lifecycleHelper;
   }

   public WebAppServletContext getContext() {
      return this.context;
   }

   static final ServletStubImpl getUnavailableStub(ServletStubImpl var0) {
      return new ServletStubImpl(var0.getServletName(), var0.getClassName(), (WebAppServletContext)var0.getServletContext(), (Map)null) {
         public void execute(ServletRequest var1, ServletResponse var2, FilterChainImpl var3) throws ServletException, IOException {
            throw new UnavailableException("Stub had been removed earlier due to UnavailableException with no timeout.");
         }
      };
   }

   static {
      _WLDF$INST_FLD_Servlet_Execute_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Execute_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ServletStubImpl.java", "weblogic.servlet.internal.ServletStubImpl", "execute", "(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;Lweblogic/servlet/internal/FilterChainImpl;)V", 207, InstrumentationSupport.makeMap(new String[]{"Servlet_Execute_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("this", "weblogic.diagnostics.instrumentation.gathering.ServletStubImplRenderer", false, true), (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("req", "weblogic.diagnostics.instrumentation.gathering.ServletRequestRenderer", false, true), null, null})}), (boolean)0);
   }
}
