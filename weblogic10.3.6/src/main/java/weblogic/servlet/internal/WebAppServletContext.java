package weblogic.servlet.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import javax.naming.Context;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspApplicationContext;
import oracle.jsp.provider.JspResourceProvider;
import weblogic.version;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationFileManager;
import weblogic.application.ModuleException;
import weblogic.application.Type;
import weblogic.application.library.IllegalSpecVersionTypeException;
import weblogic.application.library.J2EELibraryReference;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryConstants;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryReferenceFactory;
import weblogic.application.library.LibraryReferencer;
import weblogic.application.utils.AppFileOverrideUtils;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.application.utils.PathUtils;
import weblogic.deployment.EnvironmentBuilder;
import weblogic.deployment.EnvironmentException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorDiff;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.health.HealthMonitorService;
import weblogic.j2ee.descriptor.JspConfigBean;
import weblogic.j2ee.descriptor.LoginConfigBean;
import weblogic.j2ee.descriptor.ParamValueBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.ServletMappingBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.WelcomeFileListBean;
import weblogic.j2ee.descriptor.wl.ContainerDescriptorBean;
import weblogic.j2ee.descriptor.wl.LibraryRefBean;
import weblogic.j2ee.descriptor.wl.LoggingBean;
import weblogic.j2ee.descriptor.wl.ServletDescriptorBean;
import weblogic.j2ee.descriptor.wl.SessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.VirtualDirectoryMappingBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.jndi.factories.java.javaURLContextFactory;
import weblogic.logging.Loggable;
import weblogic.logging.j2ee.ServletContextLogger;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.WebAppComponentMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.ServletRuntimeMBean;
import weblogic.protocol.ProtocolHandlerAdmin;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.AsyncInitServlet;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.JSPServlet;
import weblogic.servlet.WebLogicServletContext;
import weblogic.servlet.internal.session.SessionContext;
import weblogic.servlet.internal.session.SessionInternal;
import weblogic.servlet.internal.session.SharedSessionData;
import weblogic.servlet.jsp.JspApplicationContextImpl;
import weblogic.servlet.jsp.JspFactoryImpl;
import weblogic.servlet.jsp.JspFileNotFoundException;
import weblogic.servlet.jsp.JspStub;
import weblogic.servlet.jsp.StaleChecker;
import weblogic.servlet.jsp.TagFileClassLoader;
import weblogic.servlet.jsp.TagFileHelper;
import weblogic.servlet.security.internal.SecurityModule;
import weblogic.servlet.security.internal.ServletSecurityManager;
import weblogic.servlet.security.internal.WebAppSecurity;
import weblogic.servlet.utils.BeanELResolverCachePurger;
import weblogic.servlet.utils.FastSwapFilter;
import weblogic.servlet.utils.ServletMapping;
import weblogic.servlet.utils.URLMapping;
import weblogic.servlet.utils.URLMappingFactory;
import weblogic.servlet.utils.WarUtils;
import weblogic.spring.monitoring.instrumentation.SpringInstrumentationUtils;
import weblogic.utils.Debug;
import weblogic.utils.NestedRuntimeException;
import weblogic.utils.StringUtils;
import weblogic.utils.application.WarDetector;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.ChangeAwareClassLoader;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.enumerations.EmptyEnumerator;
import weblogic.utils.enumerations.IteratorEnumerator;
import weblogic.utils.http.HttpParsing;
import weblogic.utils.io.FilenameEncoder;
import weblogic.utils.jars.JarFileUtils;
import weblogic.work.WorkManagerFactory;

public final class WebAppServletContext implements ServletContext, StaleChecker, WebLogicServletContext {
   private static final boolean WIN_32;
   private static final boolean NO_VERSION_CHECK;
   private static final char FSC;
   private static final Map NON_BLOCKING_DISPATCH;
   private static final Map DIRECT_DISPATCH;
   private static final String TEMPDIR_ATTRIBUTE = "javax.servlet.context.tempdir";
   private static final String WL_HTTPD = "weblogic.httpd.";
   private static final String WL_RELOADCHECKSECS = "servlet.reloadCheckSecs";
   private static final String WL_CLASSPATH = "servlet.classpath";
   private static final String WL_PROXYCLIENTCERT = "clientCertProxy";
   private static final String WL_DEFAULTSERVLET = "defaultServlet";
   private static final String WL_INPUTCHARSET = "inputCharset";
   private static final String STANDARD_DD = "WEB-INF/web.xml";
   private static final String WEBLOGIC_DD = "WEB-INF/weblogic.xml";
   private static final String JSF_RI_DI_SPI = "com.sun.faces.injectionProvider";
   private static final String JSF_RI_SUNJSFJS = "com.sun.faces.sunJsfJs";
   private static final String WL_JSF_RI_DI_IMPL = "com.bea.faces.WeblogicInjectionProvider";
   private static final String WL_RP_COMPAT_SWITCH = "webapp.getrealpath.accept_context_path";
   public static final String WEBFLOW_RESOURCE = "webflow_resource";
   public static final DebugLogger DEBUG_URL_RES;
   private static Method wldfDyeInjectionMethod;
   private static int maxConcurrentRequestsAllowed;
   private static boolean mergeDescriptors;
   private static boolean doNotSendContinueHeader;
   private final WebAppModule module;
   private final HttpServer httpServer;
   private ContextVersionManager contextManager;
   private ClassLoader classLoader;
   private final TagFileHelper tagFileHelper;
   private final WebAppComponentMBean compMBean;
   private final ApplicationContextInternal appCtx;
   private WebAppRuntimeMBeanImpl runtime;
   private final CompEnv compEnv;
   private final String contextName;
   private String contextPath;
   private final String versionId;
   private final String fullCtxName;
   private boolean adminMode;
   private final String logContext;
   private String displayName;
   private String docroot;
   private final AttributesMap attributes;
   private final Map initParams;
   private final EventsManager eventsManager;
   private final SessionContext sessionContext;
   private final ServletSecurityManager securityManager;
   private final WebAppConfigManager configManager;
   private final WebAppHelper helper;
   private final JSPManager jspManager;
   private final ErrorManager errorManager;
   private final FilterManager filterManager;
   private boolean defaultContext;
   private final boolean programmaticallyDeployed;
   private final boolean internalUtilitiesWebApp;
   private final boolean internalUtilitiesWebSvcs;
   private final boolean internalApp;
   private final boolean internalSAMLApp;
   private final boolean onDemandDisplayRefresh;
   private boolean started;
   private boolean isArchived;
   private boolean acceptContextPathInGetRealPath;
   private final ConcurrentHashMap servletStubs;
   private URLMapping servletMapping;
   private String[] indexFiles;
   private final TreeMap servletLoadSequences;
   private boolean startedServletLoadSequences;
   private URLMatchHelper defaultURLMatchHelper;
   private String defaultServletName;
   private Object webservicesDD;
   private final ServletContextLogger servletContextLogger;
   private final boolean loggingEnabled;
   private War war;
   private File rootTempDir;
   private String tempPath;
   private HashMap jarFiles;
   private LibraryManager libraryManager;
   private boolean asyncInitsStillRunning;
   private final List asyncInitServlets;
   private WebComponentCreator componentCreator;
   private JspResourceProvider jspResourceProvider;
   private MDSClassFinder mdsFinder;
   private JspApplicationContextImpl jacImpl;
   private Object reloadServletClassLoaderLock;
   private boolean isJsfApplication;
   static final long serialVersionUID = -7328668877117740921L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.servlet.internal.WebAppServletContext");
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Context_Handle_Throwable_Around_High;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   private WebAppServletContext(HttpServer var1, ApplicationContextInternal var2, WebAppComponentMBean var3, WebAppModule var4, String var5, String var6, String var7, ClassLoader var8, boolean var9) throws DeploymentException {
      this.tagFileHelper = new TagFileHelper(this);
      this.adminMode = false;
      this.docroot = null;
      this.attributes = new AttributesMap("servlet-context");
      this.initParams = new HashMap();
      this.eventsManager = new EventsManager(this);
      this.configManager = new WebAppConfigManager(this);
      this.jspManager = new JSPManager(this);
      this.errorManager = new ErrorManager(this);
      this.filterManager = new FilterManager(this);
      this.started = false;
      this.isArchived = false;
      this.acceptContextPathInGetRealPath = false;
      this.servletStubs = new ConcurrentHashMap();
      this.servletMapping = new ServletMapping(WebAppConfigManager.isCaseInsensitive(), WebAppSecurity.getEnforceStrictURLPattern());
      this.servletLoadSequences = new TreeMap();
      this.startedServletLoadSequences = false;
      this.defaultURLMatchHelper = null;
      this.defaultServletName = null;
      this.libraryManager = null;
      this.asyncInitsStillRunning = false;
      this.asyncInitServlets = new ArrayList();
      this.jacImpl = null;
      this.reloadServletClassLoaderLock = new Object();
      this.isJsfApplication = false;
      this.httpServer = var1;
      this.appCtx = var2;
      if (var2 != null && var2.getAppDeploymentMBean() != null) {
         this.versionId = var2.getAppDeploymentMBean().getVersionIdentifier();
         this.onDemandDisplayRefresh = var2.getAppDeploymentMBean().isOnDemandDisplayRefresh();
      } else {
         this.versionId = null;
         this.onDemandDisplayRefresh = false;
      }

      this.contextName = var6;
      this.fullCtxName = this.versionId == null ? var6 : var6 + "#" + this.versionId;
      this.programmaticallyDeployed = var9;
      this.setContextPath(var7);
      this.compMBean = var3;
      this.module = var4;
      JspFactoryImpl.init();
      this.jacImpl = new JspApplicationContextImpl(this);
      this.internalUtilitiesWebApp = this.contextPath.equals(WebService.getInternalWebAppContextPath());
      this.internalUtilitiesWebSvcs = this.contextPath.equals("/_async");
      if (var3 != null) {
         this.attributes.put("weblogic.servlet.WebAppComponentMBean", var3);
         this.internalApp = this.internalUtilitiesWebApp || var3.getApplication().isInternalApp();
      } else {
         this.internalApp = this.internalUtilitiesWebApp;
      }

      this.internalSAMLApp = this.contextPath.equals("/samlits_ba") || this.contextPath.equals("/samlits_cc") || this.contextPath.equals("/samlacs") || this.contextPath.equals("/samlars") || this.contextPath.equals("/saml2");
      this.initSwitches();
      this.setDocroot(this.getRoot(var5));
      this.processWebAppLibraries(this.getRootTempDir());
      if (mergeDescriptors) {
         this.mergeLibraryDescriptors();
      }

      if (var4 != null) {
         try {
            if (WarUtils.configureFCL(var4.getWlWebAppBean(), var2.getAppClassLoader(), !var2.isEar())) {
               HTTPLogger.logFilteringConfigurationIgnored(var2.getApplicationId(), var4.getModuleURI());
            }
         } catch (Exception var11) {
            throw new DeploymentException(var11.getMessage());
         }

         this.isJsfApplication = WarUtils.isJsfApplication(var4.getWebAppBean(), var4.getWlWebAppBean());
      }

      if (this.isJsfApplication) {
         this.initParams.put("com.sun.faces.injectionProvider", "com.bea.faces.WeblogicInjectionProvider");
      }

      this.configManager.init();
      this.loadInitParams();
      this.helper = new ServletContextWebAppHelper();
      this.securityManager = new ServletSecurityManager(this);
      if (var8 != null) {
         this.classLoader = var8;
      } else {
         this.initClassLoader(false);
      }

      this.processAnnotations(false);
      this.registerInternalServlets();
      this.sessionContext = this.initSessionContext();
      this.registerDefaultServlet();
      this.initFromMBean();
      this.servletContextLogger = this.initLogger();
      this.loggingEnabled = this.servletContextLogger != null;
      this.logContext = this.toString();
      this.compEnv = new CompEnv(this);
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug("Initialized servlet context: " + this.getLogContext());
      }

   }

   WebAppServletContext(HttpServer var1, ApplicationContextInternal var2, String var3) throws DeploymentException {
      this(var1, var2, (WebAppComponentMBean)null, (WebAppModule)null, (String)null, var3, var3, (ClassLoader)null, true);
      this.activate();
      this.start();
   }

   WebAppServletContext(HttpServer var1, ApplicationContextInternal var2, String var3, ClassLoader var4) throws DeploymentException {
      this(var1, var2, (WebAppComponentMBean)null, (WebAppModule)null, (String)null, var3, var3, var4, true);
      this.activate();
      this.start();
   }

   WebAppServletContext(HttpServer var1, ApplicationContextInternal var2, WebAppComponentMBean var3, WebAppModule var4, String var5) throws DeploymentException, ModuleException {
      this(var1, var2, var3, var4, var4.getWarPath(), ApplicationVersionUtils.getNonVersionedName(var4.getName()), var5, (ClassLoader)null, false);
   }

   public Object getAttribute(String var1) {
      return this.attributes.get(var1, this);
   }

   public Enumeration getAttributeNames() {
      return (Enumeration)(this.attributes.isEmpty() ? new EmptyEnumerator() : new IteratorEnumerator(this.attributes.keys()));
   }

   public void setAttribute(String var1, Object var2) {
      if (var2 == null) {
         this.removeAttribute(var1);
      } else {
         Object var3 = this.attributes.put(var1, var2, this);
         this.eventsManager.notifyContextAttributeChange(var1, var2, var3);
      }
   }

   public void removeAttribute(String var1) {
      Object var2 = this.attributes.remove(var1);
      this.eventsManager.notifyContextAttributeChange(var1, (Object)null, var2);
   }

   public ServletContext getContext(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = HttpParsing.ensureStartingSlash(var1);
         ContextVersionManager var2 = this.httpServer.getServletContextManager().lookupVersionManager(var1);
         return var2 == null ? null : var2.getCurrentOrActiveContext(this.isAdminMode());
      }
   }

   public String getInitParameter(String var1) {
      return (String)this.initParams.get(var1);
   }

   public void setInitParameter(String var1, String var2) {
      this.initParams.put(var1, var2);
   }

   public Enumeration getInitParameterNames() {
      return (Enumeration)(this.initParams.isEmpty() ? new EmptyEnumerator() : new IteratorEnumerator(this.initParams.keySet().iterator()));
   }

   public int getMajorVersion() {
      return 2;
   }

   public int getMinorVersion() {
      return 5;
   }

   public String getMimeType(String var1) {
      return this.configManager.getMimeType(var1);
   }

   public RequestDispatcher getRequestDispatcher(String var1) {
      return this.getRequestDispatcher(var1, -1);
   }

   RequestDispatcher getRequestDispatcher(String var1, int var2) {
      if (var1 != null && var1.length() != 0 && var1.charAt(0) == '/') {
         String var3 = null;
         int var4 = var1.indexOf(63);
         if (var4 > 0 && var4 <= var1.length() - 1) {
            var3 = var1.substring(var4 + 1);
            if ("".equals(var3)) {
               var3 = null;
            }

            var1 = var1.substring(0, var4);
         }

         var1 = FilenameEncoder.resolveRelativeURIPath(var1, true);
         return var1 == null ? null : new RequestDispatcherImpl(var1, var3, this, var2);
      } else {
         return null;
      }
   }

   public RequestDispatcher getNamedDispatcher(String var1) {
      return this.getNamedDispatcher(var1, -1);
   }

   public RequestDispatcher getNamedDispatcher(String var1, int var2) {
      if (var1 == null) {
         return null;
      } else {
         ServletStubImpl var3 = (ServletStubImpl)this.servletStubs.get(var1);
         return var3 == null ? null : new RequestDispatcherImpl(var3, this, var2);
      }
   }

   public String getRealPath(String var1) {
      if (this.docroot == null) {
         HTTPLogger.logNullDocRoot(this.getLogContext(), "getRealPath()");
         return null;
      } else if (var1 == null) {
         return null;
      } else {
         String var2 = var1.replace('/', FSC);
         if (this.isArchived) {
            if (this.configManager.isShowArchivedRealPathEnabled()) {
               try {
                  File var12 = new File(this.getRootTempDir(), "war");
                  var12 = FilenameEncoder.getSafeFile(var12.getPath(), var2);
                  return var12.getCanonicalPath();
               } catch (FilenameEncoder.UnsafeFilenameException var7) {
                  HTTPLogger.logUnsafePath(this.getLogContext(), "getRealPath()", var1, var7);
                  return null;
               } catch (IOException var8) {
                  HTTPLogger.logUnsafePath(this.getLogContext(), "getRealPath()", var1, var8);
                  return null;
               }
            } else {
               return null;
            }
         } else {
            if (this.acceptContextPathInGetRealPath && !this.isDefaultContext()) {
               String var3 = FSC + this.contextName;
               if (var2.startsWith(var3)) {
                  var2 = var2.substring(var3.length());
               }
            }

            try {
               ApplicationFileManager var11 = this.appCtx.getApplicationFileManager();
               File[] var4 = var11.getVirtualJarFile(this.getURI()).getRootFiles();
               if (var4.length == 0) {
                  throw new AssertionError("Could not determine the docroot in getRealPath");
               } else {
                  File var5 = null;

                  for(int var6 = 0; var6 < var4.length; ++var6) {
                     var5 = FilenameEncoder.getSafeFile(var4[var6].getPath(), var2);
                     if (var5.exists()) {
                        break;
                     }
                  }

                  return var5.getCanonicalPath();
               }
            } catch (FilenameEncoder.UnsafeFilenameException var9) {
               HTTPLogger.logUnsafePath(this.getLogContext(), "getRealPath()", var1, var9);
               return null;
            } catch (IOException var10) {
               HTTPLogger.logUnsafePath(this.getLogContext(), "getRealPath()", var1, var10);
               return null;
            }
         }
      }
   }

   public URL getResource(String var1) throws MalformedURLException {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.getLogContext() + " getResource() invoked for : " + var1);
      }

      if (var1 != null && var1.length() >= 1 && var1.charAt(0) == '/') {
         WarSource var2 = this.getResourceAsSource(var1);
         return var2 == null ? null : var2.getURL();
      } else {
         throw new MalformedURLException("The path for getResource() must begin with a '/'");
      }
   }

   public URL[] getResources(String var1) throws MalformedURLException {
      if (var1 != null && var1.length() >= 1 && var1.charAt(0) == '/') {
         ClassFinder var2 = this.war.getResourceFinder(var1);
         ArrayList var3 = new ArrayList();
         Enumeration var4 = var2.getSources(var1);

         while(var4.hasMoreElements()) {
            var3.add(((Source)var4.nextElement()).getURL());
         }

         return (URL[])((URL[])var3.toArray(new URL[var3.size()]));
      } else {
         throw new MalformedURLException("The path for getResources() must begin with a '/'");
      }
   }

   public InputStream getResourceAsStream(String var1) {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.getLogContext() + " getResourceAsStream() invoked for : " + var1);
      }

      WarSource var2 = this.getResourceAsSource(var1);

      try {
         return var2 == null ? null : var2.getInputStream();
      } catch (IOException var4) {
         HTTPLogger.logUnableToGetStream(this.getLogContext(), var1, var4);
         return null;
      }
   }

   public String getServerInfo() {
      return HttpServer.SERVER_INFO;
   }

   public HttpServer getServer() {
      return this.httpServer;
   }

   public void log(String var1, Throwable var2) {
      if (this.loggingEnabled) {
         this.servletContextLogger.log(var1, var2);
      }
   }

   public void logError(String var1) {
      if (this.loggingEnabled) {
         this.servletContextLogger.logError(var1);
      }
   }

   public void log(String var1) {
      if (this.loggingEnabled) {
         this.servletContextLogger.log(var1);
      }
   }

   public Set getResourcePaths(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = HttpParsing.ensureEndingSlash(var1);
         HashSet var2 = new HashSet();
         this.war.getResourcePaths(var1, var2);
         return var2.isEmpty() ? null : var2;
      }
   }

   public String getServletContextName() {
      return this.displayName;
   }

   public Enumeration getServletNames() {
      return new IteratorEnumerator(this.servletStubs.keySet().iterator());
   }

   /** @deprecated */
   public Servlet getServlet(String var1) {
      return null;
   }

   /** @deprecated */
   public Enumeration getServlets() {
      return new EmptyEnumerator();
   }

   /** @deprecated */
   public void log(Exception var1, String var2) {
      this.log((String)var2, (Throwable)var1);
   }

   public ContextVersionManager getContextManager() {
      if (this.contextManager == null || this.contextManager.isOld()) {
         this.contextManager = this.httpServer.getServletContextManager().lookupVersionManagerForContextPath(this.getContextPath());
      }

      return this.contextManager;
   }

   public void addSession(String var1) {
      ContextVersionManager var2 = this.getContextManager();
      if (var2 != null) {
         var2.putContextForSession(var1, this);
      }

   }

   public void removeSession(String var1) {
      ContextVersionManager var2 = this.getContextManager();
      if (var2 != null) {
         var2.removeContextForSession(var1);
      }

   }

   public void enteringContext(ServletRequestImpl var1, ServletResponseImpl var2, HttpSession var3) {
      if (!(var3 instanceof SharedSessionData)) {
         this.sessionContext.enter(var1, var2, var3);
      }

   }

   public void exitingContext(ServletRequestImpl var1, ServletResponseImpl var2, HttpSession var3) {
      if (!(var3 instanceof SharedSessionData)) {
         this.sessionContext.exit(var1, var2, var3);
      }

   }

   ServletContextLogger getServletContextLogger() {
      return this.servletContextLogger;
   }

   private ServletContextLogger initLogger() {
      if (!this.programmaticallyDeployed && this.module != null && this.module.getWlWebAppBean() != null && this.module.getWlWebAppBean().getLoggings().length != 0) {
         LoggingBean var1 = this.module.getWlWebAppBean().getLoggings()[0];
         return var1.isLoggingEnabled() ? new ServletContextLogger("ServletContext-" + this.getContextPath(), var1) : null;
      } else {
         return new ServletContextLogger("ServletContext-" + this.getContextPath(), (LoggingBean)null);
      }
   }

   private void loadInitParams() {
      if (this.module != null && this.module.getWebAppBean() != null) {
         ParamValueBean[] var1 = this.module.getWebAppBean().getContextParams();
         if (var1 != null) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               this.addInitParameter(var1[var2].getParamName(), var1[var2].getParamValue());
            }
         }
      }

   }

   private void initFromMBean() {
      this.acceptContextPathInGetRealPath = this.httpServer.getMBean().isAcceptContextPathInGetRealPath();
      if (this.compMBean != null) {
         this.configManager.registerMBean(this.compMBean);
         this.setDefaultServlet(this.compMBean.getDefaultServlet());
      }

   }

   private void setDefaultServlet(String var1) {
      if (var1 != null) {
         if (this.servletStubs != null) {
            ServletStubImpl var2 = (ServletStubImpl)this.servletStubs.get(var1);
            if (var2 != null) {
               this.defaultURLMatchHelper = new URLMatchHelper("/", var2);
            }
         }

         this.defaultServletName = var1;
      }
   }

   private String getRoot(String var1) throws DeploymentException {
      Debug.assertion(var1 != null);
      File var2 = new File(var1);
      if (!var2.isDirectory() && WarDetector.instance.suffixed(var1)) {
         this.isArchived = true;
         return var2.getPath();
      } else {
         return var1.replace('/', FSC);
      }
   }

   private void registerDefaultServlet() {
      this.registerServlet("FileServlet", "/", "weblogic.servlet.FileServlet", new HashMap());
   }

   private void registerInternalServlets() throws DeploymentException {
      if (!this.programmaticallyDeployed) {
         this.registerWebServicesServlet();
      }

   }

   private void registerWebServicesServlet() throws DeploymentException {
      if (!this.configManager.isImplicitServletMappingDisabled()) {
         String var1;
         try {
            this.getServletClassLoader().loadClass("weblogic.webservice.server.servlet.WebServiceServlet");
            var1 = "weblogic.webservice.server.servlet.WebServiceServlet";
         } catch (ClassNotFoundException var3) {
            var1 = "weblogic.webservice.server.servlet.DummyServlet";
         }

         this.registerServletDefinition("WebServiceServlet", var1, (Map)null, false);
         this.registerServletLoadSequence("WebServiceServlet", 0);
      }
   }

   void prepare(RuntimeMBean var1) throws DeploymentException {
      AppDeploymentMBean var2 = this.appCtx.getAppDeploymentMBean();
      ServerMBean var3 = ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID).getServer();
      StringBuffer var4 = new StringBuffer();
      var4.append(this.httpServer.getName());
      var4.append("_").append(this.getContextPath());
      var4.append(this.getVersionId() == null ? "" : "_" + this.getVersionId());

      try {
         this.runtime = new WebAppRuntimeMBeanImpl(var4.toString(), this.contextPath, this, var1, var2.getApplicationIdentifier());
      } catch (ManagementException var9) {
         throw new DeploymentException(var9);
      }

      if (this.libraryManager != null && !this.libraryManager.hasUnresolvedReferences()) {
         this.runtime.setLibraryRuntimes(this.libraryManager.getReferencedLibraryRuntimes());
         this.libraryManager.getReferencer().setReferencerRuntime(this.runtime);
      }

      if (this.module != null && this.module.getRegisterFastSwapFilter()) {
         FastSwapFilter.registerFastSwapFilter(this);
      }

      this.prepareFromDescriptors();
      this.initResourceProvider();
      this.attributes.put("weblogic.servlet.WebAppComponentRuntimeMBean", this.runtime);
      this.initContextListeners();
      Iterator var5 = this.servletStubs.values().iterator();

      while(var5.hasNext()) {
         ServletStubImpl var6 = (ServletStubImpl)var5.next();

         try {
            var6.initRuntime();
         } catch (ManagementException var8) {
            throw new DeploymentException(var8);
         }
      }

      this.getSecurityManager().getWebAppSecurity().start();
   }

   private SessionContext initSessionContext() throws DeploymentException {
      SessionDescriptorBean var1 = null;
      WeblogicApplicationBean var2 = this.appCtx.getWLApplicationDD();
      if (var2 != null && var2.isSet("SessionDescriptor")) {
         var1 = var2.getSessionDescriptor();
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug(this.getLogContext() + ": shared session context enabled");
         }
      }

      if (var1 == null && this.module != null) {
         WeblogicWebAppBean var3 = this.module.getWlWebAppBean();
         if (var3 != null) {
            var1 = (SessionDescriptorBean)DescriptorUtils.getFirstChildOrDefaultBean(var3, var3.getSessionDescriptors(), "SessionDescriptor");
         }
      }

      return SessionContext.getInstance(this, var1);
   }

   private void initSwitches() {
      String var1 = this.appCtx.getApplicationParameter("webapp.getrealpath.accept_context_path");
      if (var1 != null && var1.equalsIgnoreCase("true")) {
         this.acceptContextPathInGetRealPath = true;
      }

   }

   private void prepareFromDescriptors() throws DeploymentException {
      if (this.module != null && this.module.getWebAppBean() != null) {
         String[] var1 = this.module.getWebAppBean().getDisplayNames();
         if (var1 != null && var1.length > 0) {
            this.displayName = var1[0];
         }

         this.configManager.registerContainerDescriptors(this.module.getWlWebAppBean());
         this.getSecurityManager().getWebAppSecurity().registerSecurityRoles(this.module);
         this.jspManager.registerJspDescriptor(this.module.getWebAppBean(), this.module.getWlWebAppBean());
         this.filterManager.registerServletFilters(this.module.getWebAppBean());
         this.registerURLMatchMapper();
         this.registerServlets();
         this.registerWelcomeFiles();
         this.getSecurityManager().getWebAppSecurity().registerSecurityConstraints(this.module.getWebAppBean().getSecurityConstraints());
         this.registerLoginConfig();
         this.jspManager.registerTagLibs(this.module.getWebAppBean().getJspConfigs());
         this.configManager.getLocaleEncodingMap().registerLocaleEncodingMap(this.module.getWebAppBean().getLocaleEncodingMappingLists());
         if (System.getSecurityManager() != null) {
            this.jspManager.registerSecurityPermissionSpec(this.module.getWlWebAppBean());
         }

         if (this.module.getWlWebAppBean() != null) {
            this.registerVirtualDirectoryMappings();
            this.registerAuthFilter();
            this.registerContainerDescriptorsWithSessionContext();
            this.configManager.registerCharsetParams(this.module.getWlWebAppBean());
            if (this.jspManager.isJspPrecompileEnabled()) {
               this.jspManager.precompileJSPs();
            }
         }

      }
   }

   private void activateFromDescriptors() throws DeploymentException {
      if (this.module != null && this.module.getWebAppBean() != null) {
         this.errorManager.registerErrorPages(this.module.getWebAppBean());
         WeblogicWebAppBean var1 = this.module.getWlWebAppBean();
         if (var1 != null) {
            this.configManager.setDispatchPolicy(var1.getWlDispatchPolicies().length > 0 ? var1.getWlDispatchPolicies()[0] : null);
         }

      }
   }

   private void registerLoginConfig() throws DeploymentException {
      LoginConfigBean[] var1 = this.module.getWebAppBean().getLoginConfigs();
      if (var1 != null && var1.length >= 1) {
         if (var1.length > 1) {
            Loggable var5 = HTTPLogger.logMultipleOccurrencesNotAllowedLoggable("<login-config>", "web.xml");
            var5.log();
            throw new DeploymentException(var5.getMessage());
         } else {
            Loggable var3;
            try {
               this.getSecurityManager().setLoginConfig(var1[0]);
            } catch (IllegalArgumentException var4) {
               var3 = HTTPLogger.logInvalidAuthMethodLoggable(this.getLogContext(), var4.getMessage());
               var3.log();
               throw new DeploymentException(var3.getMessage());
            }

            WebAppSecurity var2 = this.getSecurityManager().getWebAppSecurity();
            if (var2 != null && "FORM".equals(var2.getAuthMethod())) {
               if (var2.getLoginPage() == null || var2.getLoginPage().length() < 1) {
                  var3 = HTTPLogger.logLoginOrErrorPageMissingLoggable(this.getLogContext(), "form-login-page");
                  var3.log();
                  throw new DeploymentException(var3.getMessage());
               }

               if (var2.getErrorPage() == null || var2.getErrorPage().length() < 1) {
                  var3 = HTTPLogger.logLoginOrErrorPageMissingLoggable(this.getLogContext(), "form-error-page");
                  var3.log();
                  throw new DeploymentException(var3.getMessage());
               }
            }

            String var6 = var1[0].getRealmName();
            if (var6 != null) {
               this.configManager.setAuthRealmName(var6);
               this.securityManager.setAuthRealmName(var6);
            }

         }
      }
   }

   private void processAnnotations(boolean var1) throws DeploymentException {
      WebAnnotationProcessor var2 = null;
      ClassLoader var3 = Thread.currentThread().getContextClassLoader();
      Thread.currentThread().setContextClassLoader(this.getServletClassLoader());

      try {
         String var4 = "weblogic.servlet.internal.WebAnnotationProcessorImpl";
         var2 = (WebAnnotationProcessor)Class.forName(var4).newInstance();
         if (var1) {
            WebAppBean var12 = var2.processAnnotationsOnClone((GenericClassLoader)this.getServletClassLoader(), this.getWebAppModule().getWebAppBean(), this.helper);
            if (HTTPDebugLogger.isEnabled()) {
               System.out.println("------------------------------------------");
               DescriptorUtils.writeAsXML((DescriptorBean)this.getWebAppModule().getWebAppBean());
               System.out.println("------------------------------------------");
               DescriptorUtils.writeAsXML((DescriptorBean)var12);
               System.out.println("------------------------------------------");
            }

            DescriptorDiff var6 = ((DescriptorBean)this.getWebAppModule().getWebAppBean()).getDescriptor().computeDiff(((DescriptorBean)var12).getDescriptor());
            if (var6.size() > 0) {
               HTTPLogger.logAnnotationsChanged(this.getDocroot());
            }
         } else {
            var2.processAnnotations((GenericClassLoader)this.getServletClassLoader(), this.getWebAppModule().getWebAppBean(), this.helper);
         }

         String var13 = null;
         if (this.getWebAppModule().getWlWebAppBean() != null && this.getWebAppModule().getWlWebAppBean().getComponentFactoryClassName().length > 0) {
            var13 = this.getWebAppModule().getWlWebAppBean().getComponentFactoryClassName()[0];
         }

         if (var13 == null && this.appCtx != null && this.appCtx.getWLApplicationDD() != null) {
            var13 = this.appCtx.getWLApplicationDD().getComponentFactoryClassName();
         }

         PitchforkContext var14 = new PitchforkContext(var13);
         this.componentCreator = new WebComponentContributor(var14);
         this.componentCreator.initialize(this);
      } catch (Exception var10) {
         Loggable var5 = HTTPLogger.logAnnotationProcessingFailedLoggable(this.getDocroot(), var10.getMessage(), var10);
         var5.log();
         throw new DeploymentException(var5.getMessage(), var10);
      } finally {
         Thread.currentThread().setContextClassLoader(var3);
      }

   }

   public WebComponentCreator getComponentCreator() {
      return this.componentCreator;
   }

   private void registerAuthFilter() {
      WeblogicWebAppBean var1 = this.module.getWlWebAppBean();
      String var2 = var1.getAuthFilters().length > 0 ? var1.getAuthFilters()[0] : null;
      if (var2 != null) {
         this.getSecurityManager().getWebAppSecurity().setAuthFilter(var2);
      }

   }

   private void registerVirtualDirectoryMappings() {
      VirtualDirectoryMappingBean[] var1 = this.module.getWlWebAppBean().getVirtualDirectoryMappings();
      if (var1 != null && var1.length >= 1) {
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = var1[var3].getLocalPath();
            if (var4 != null && var4.length() >= 1) {
               if (!isAbsoluteFilePath(var4)) {
                  File var5 = new File(this.getDocroot() + FSC + var4);
                  if (HTTPDebugLogger.isEnabled()) {
                     HTTPDebugLogger.debug(this.getLogContext() + ":The localPath is :" + var4);
                     HTTPDebugLogger.debug(this.getLogContext() + ":docRoot is :" + this.getDocroot());
                  }

                  if (var5.exists()) {
                     try {
                        var4 = var5.getCanonicalPath();
                        var2.add(var4);
                     } catch (IOException var8) {
                        HTTPLogger.logInvalidVirtualDirectoryPath(this.getLogContext(), var4, this.getDocroot(), var8);
                        continue;
                     }
                  } else if (HTTPDebugLogger.isEnabled()) {
                     HTTPDebugLogger.debug(this.getLogContext() + ": local path is relative to the rootDir." + " It is :" + new File(var4));
                  }
               }

               String[] var9 = var1[var3].getUrlPatterns();
               if (var9 != null && var9.length >= 1) {
                  for(int var6 = 0; var6 < var9.length; ++var6) {
                     if (var9[var6] != null) {
                        String var7 = WebAppSecurity.fixupURLPattern(var9[var6]);
                        this.war.addVirtualDirectory(var4, var7);
                     }
                  }
               }
            }
         }

         this.war.setVirtualMappingPaths(var2);
      }
   }

   private void registerWelcomeFiles() {
      WelcomeFileListBean[] var1 = this.module.getWebAppBean().getWelcomeFileLists();
      ArrayList var2 = new ArrayList();
      int var6;
      if (var1 != null && var1.length > 0) {
         for(var6 = 0; var6 < var1.length; ++var6) {
            String[] var7 = var1[var6].getWelcomeFiles();
            if (var7 != null) {
               for(int var5 = 0; var5 < var7.length; ++var5) {
                  var2.add(var7[var5]);
               }
            }
         }
      } else if (this.compMBean != null) {
         String[] var3 = this.compMBean.getIndexFiles();
         if (var3 != null) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               var2.add(var3[var4]);
            }
         }
      }

      this.indexFiles = (String[])((String[])var2.toArray(new String[var2.size()]));
      if (this.indexFiles != null) {
         for(var6 = 0; var6 < this.indexFiles.length; ++var6) {
            if (this.indexFiles[var6].length() > 0 && this.indexFiles[var6].charAt(0) == '/') {
               this.indexFiles[var6] = this.indexFiles[var6].substring(1);
            }
         }
      }

   }

   private void registerServlets() throws DeploymentException {
      ServletBean[] var1 = this.module.getWebAppBean().getServlets();
      if (var1 != null) {
         this.registerServlets(var1);
      }

      ServletMappingBean[] var2 = this.module.getWebAppBean().getServletMappings();
      if (var2 != null) {
         this.registerServletMapping(var2);
      }

   }

   private void registerServlets(ServletBean[] var1) throws DeploymentException {
      HashMap var2 = new HashMap();
      if (this.module.getWlWebAppBean() != null) {
         ServletDescriptorBean[] var3 = this.module.getWlWebAppBean().getServletDescriptors();
         if (var3 != null && var3.length > 0) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               var2.put(var3[var4].getServletName(), var3[var4]);
            }
         }
      }

      for(int var14 = 0; var14 < var1.length; ++var14) {
         ServletBean var15 = var1[var14];
         ParamValueBean[] var5 = var15.getInitParams();
         HashMap var6 = new HashMap();
         if (var5 != null) {
            for(int var7 = 0; var7 < var5.length; ++var7) {
               ParamValueBean var8 = var5[var7];
               var6.put(var8.getParamName(), var8.getParamValue());
            }
         }

         String var16 = var15.getServletClass();
         ServletStubImpl var17;
         if (var16 != null) {
            var17 = this.registerServletDefinition(var15.getServletName(), var16, var6, false);
         } else {
            var17 = this.jspManager.registerJSPServletDefinition(var15, var6);
            this.getSecurityManager().getWebAppSecurity().registerRoleRefs(var17);
         }

         this.getSecurityManager().getWebAppSecurity().registerSecurityRoleRef(var17, var15.getSecurityRoleRefs());
         ServletDescriptorBean var9 = (ServletDescriptorBean)var2.get(var15.getServletName());
         if (var9 != null) {
            if (var9.getDispatchPolicy() != null) {
               var17.setDispatchPolicy(var9.getDispatchPolicy());
            }

            var17.getSecurityHelper().setInitAsIdentity(var9.getInitAsPrincipalName());
            var17.getSecurityHelper().setDestroyAsIdentity(var9.getDestroyAsPrincipalName());
         }

         String var10;
         if (var15.getRunAs() != null && (var10 = var15.getRunAs().getRoleName()) != null) {
            var17.getSecurityHelper().setRunAsIdentity(this.getSecurityManager().getWebAppSecurity().getRunAsPrincipalName(var9, var10));
         }

         int var11 = -1;

         try {
            var11 = Integer.parseInt(var15.getLoadOnStartup());
         } catch (NumberFormatException var13) {
         }

         this.registerServletLoadSequence(var15.getServletName(), var11);
      }

   }

   private void registerServletMapping(ServletMappingBean[] var1) throws DeploymentException {
      JspConfigBean[] var2 = this.module.getWebAppBean().getJspConfigs();
      Set var3 = JSPManager.getJspConfigPatterns(var2);

      for(int var4 = 0; var4 < var1.length; ++var4) {
         ServletMappingBean var5 = var1[var4];
         if (var5 != null) {
            String var6 = var5.getServletName();
            ServletStubImpl var7 = (ServletStubImpl)this.servletStubs.get(var6);
            String[] var8 = var5.getUrlPatterns();
            Debug.assertion(var8 != null && var8.length > 0, "web-app schema requires atleast one url-pattern");
            if (var7 == null) {
               Loggable var14 = HTTPLogger.logServletNotFoundForPatternLoggable(var6, StringUtils.join(var8, ", "));
               var14.log();
               if (!this.configManager.isOldDescriptor()) {
                  throw new DeploymentException(var14.getMessage());
               }

               return;
            }

            String var9 = null;
            if (var7 != null) {
               var9 = var7.getClassName();
            }

            boolean var10 = "weblogic.servlet.proxy.HttpClusterServlet".equals(var9) || "weblogic.servlet.proxy.HttpProxyServlet".equals(var9);
            boolean var11 = "weblogic.servlet.JSPServlet".equals(var9) || "weblogic.servlet.JSPClassServlet".equals(var9);
            boolean var12 = "oracle.jsp.runtimev2.JspServlet".equals(var9);

            for(int var13 = 0; var13 < var8.length; ++var13) {
               if (var8[var13] != null && var8[var13].equals("*.jsp") && !var10 && !var12) {
                  HTTPLogger.logFoundStarJspUrlPattern(var6);
               }

               if (var3 == null || !var3.contains(var8[var13]) || var11 || var10 || var12) {
                  this.registerServletMap(var6, var8[var13], var7);
               }
            }
         }
      }

   }

   public String getUrlMatchMap() {
      WeblogicWebAppBean var1 = this.module.getWlWebAppBean();
      if (var1 == null) {
         return null;
      } else {
         return var1.getUrlMatchMaps().length > 0 ? var1.getUrlMatchMaps()[0] : null;
      }
   }

   private void registerURLMatchMapper() {
      String var1 = this.getUrlMatchMap();
      if (var1 != null) {
         URLMapping var2 = URLMappingFactory.createCustomURLMapping(var1, this.getServletClassLoader(), WebAppConfigManager.isCaseInsensitive());
         if (var2 != null) {
            Object[] var3 = this.servletMapping.values();
            if (var3 != null) {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  URLMatchHelper var5 = (URLMatchHelper)var3[var4];
                  if (var5 != null) {
                     var2.put(var5.getPattern(), var5);
                  }
               }
            }

            this.servletMapping = var2;
         }
      }
   }

   private void registerContainerDescriptorsWithSessionContext() {
      WeblogicWebAppBean var1 = this.module.getWlWebAppBean();
      ContainerDescriptorBean var2 = (ContainerDescriptorBean)DescriptorUtils.getFirstChildOrDefaultBean(var1, var1.getContainerDescriptors(), "ContainerDescriptor");
      if (var2.isSaveSessionsEnabled()) {
         this.sessionContext.getConfigMgr().setSaveSessionsOnRedeployEnabled(true);
      }

   }

   public void registerFilter(String var1, String var2, String[] var3, String[] var4, Map var5) throws DeploymentException {
      this.filterManager.registerFilter(var1, var2, var3, var4, var5);
   }

   public void registerFilter(String var1, String var2, String[] var3, String[] var4, Map var5, String[] var6) throws DeploymentException {
      this.filterManager.registerFilter(var1, var2, var3, var4, var5, var6);
   }

   public boolean isFilterRegistered(String var1) {
      return this.filterManager.isFilterRegistered(var1);
   }

   public void registerListener(String var1) throws DeploymentException {
      this.eventsManager.registerEventListener(var1);
   }

   public boolean isListenerRegistered(String var1) {
      return this.eventsManager.isListenerRegistered(var1);
   }

   public void registerServlet(String var1, String var2, String[] var3, Map var4, int var5) throws DeploymentException {
      ServletStubImpl var6 = this.registerServletDefinition(var1, var2, var4, false);
      if (var3 != null) {
         for(int var7 = 0; var7 < var3.length; ++var7) {
            this.registerServletMap(var1, var3[var7], var6);
         }
      }

      this.registerServletLoadSequence(var1, var5);
   }

   public boolean isServletRegistered(String var1) {
      Object[] var2 = this.servletMapping.values();
      if (var2 != null && var2.length >= 1) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            URLMatchHelper var4 = (URLMatchHelper)var2[var3];
            if (var4.getPattern().equals(var1)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void bindResourceRef(String var1, String var2, String var3, String var4, boolean var5, String var6) throws DeploymentException {
      if (!this.isStarted()) {
         throw new DeploymentException("ResourceRef can be NOT bound when the application is NOT started :'" + var1 + "'");
      } else {
         this.compEnv.bindResourceRef(var1, var2, var3, var4, var5, var6);
      }
   }

   public void bindEjbRef(String var1, String var2, String var3, String var4, String var5, String var6, String var7) throws DeploymentException {
      if (!this.isStarted()) {
         throw new DeploymentException("EjbRef can be NOT bound when the application is NOT started :'" + var1 + "'");
      } else {
         this.compEnv.bindEjbRef(var1, var2, var3, var4, var5, var6, var7, false);
      }
   }

   public void bindEjbLocalRef(String var1, String var2, String var3, String var4, String var5, String var6, String var7) throws DeploymentException {
      if (!this.isStarted()) {
         throw new DeploymentException("EjbLocalRef can NOT be bound when the application is NOT started :'" + var1 + "'");
      } else {
         this.compEnv.bindEjbRef(var1, var2, var3, var4, var5, var6, var7, true);
      }
   }

   public boolean isResourceBound(String var1) {
      return this.compEnv.isResourceBound(var1);
   }

   public void setJspParam(String var1, String var2) throws DeploymentException {
      this.jspManager.setJspParam(var1, var2);
   }

   void registerServlet(String var1, String var2, String var3, Map var4) {
      this.registerServlet(var1, var2, var3, var4, false);
   }

   private void registerServlet(String var1, String var2, String var3, Map var4, boolean var5) {
      ServletStubImpl var6 = (ServletStubImpl)this.servletStubs.get(var1);
      if (var6 == null) {
         var6 = this.registerServletDefinition(var1, var3, var4, var5);
      }

      this.registerServletMap(var1, var2, var6);
   }

   private ServletStubImpl registerServletDefinition(String var1, String var2, Map var3, boolean var4) {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.getLogContext() + ": registering servlet : " + var2 + var1 + " initArgs: " + (var3 != null ? var3.toString() : ""));
      }

      try {
         ServletStubImpl var5 = new ServletStubImpl(var1, var2, this, var3);
         if (this.runtime != null) {
            var5.initRuntime();
         }

         var5.setInternalServlet(var4);
         this.registerServletStub(var1, var5);
         this.getSecurityManager().getWebAppSecurity().registerRoleRefs(var5);
         return var5;
      } catch (ManagementException var6) {
         HTTPLogger.logErrorCreatingServletStub(this.getLogContext(), var1, var2, var3, var6);
         return null;
      }
   }

   public synchronized void registerServletMap(String var1, String var2) {
      ServletStubImpl var3 = (ServletStubImpl)this.servletStubs.get(var1);
      this.registerServletMap(var1, var2, var3);
   }

   synchronized void registerServletMap(String var1, String var2, ServletStubImpl var3) {
      if (var2 != null && var2.length() != 0) {
         boolean var7 = var2.equals("/");
         var2 = WebAppSecurity.fixupURLPattern(var2);
         if (var2.equals("/")) {
            this.defaultURLMatchHelper = new URLMatchHelper(var7 ? "/" : "/*", var3);
         } else {
            if (this.defaultServletName != null && var1.equals(this.defaultServletName)) {
               this.defaultURLMatchHelper = new URLMatchHelper("/", var3);
            }

            URLMapping var5 = (URLMapping)((URLMapping)this.servletMapping.clone());
            URLMatchHelper var6 = new URLMatchHelper(var2, var3);
            var5.put(var2, var6);
            this.servletMapping = var5;
         }
      } else {
         Loggable var4 = HTTPLogger.logServletNameIsNullLoggable(this.toString(), var2);
         throw new IllegalArgumentException(var4.getMessage());
      }
   }

   private synchronized void registerServletLoadSequence(String var1, int var2) throws DeploymentException {
      if (var2 >= 0) {
         if (!this.started && !this.startedServletLoadSequences) {
            Integer var8 = new Integer(var2);
            Object var9 = this.servletLoadSequences.get(var8);
            ArrayList var5;
            if (var9 == null) {
               var5 = new ArrayList();
               var5.add(var1);
               this.servletLoadSequences.put(var8, var5);
            } else {
               var5 = (ArrayList)var9;
               var5.add(var1);
            }

         } else {
            Thread var3 = Thread.currentThread();
            ClassLoader var4 = this.pushEnvironment(var3);

            try {
               this.preloadServlet(var1);
            } finally {
               popEnvironment(var3, var4);
            }

         }
      }
   }

   private void initContextListeners() throws DeploymentException {
      Thread var1 = Thread.currentThread();
      ClassLoader var2 = this.pushEnvironment(var1);

      try {
         this.eventsManager.registerPreparePhaseListeners();
         this.eventsManager.notifyContextPreparedEvent();
      } finally {
         popEnvironment(var1, var2);
      }

   }

   public synchronized void preloadResources() throws DeploymentException {
      Thread var1 = Thread.currentThread();
      ClassLoader var2 = this.pushEnvironment(var1);

      try {
         this.eventsManager.registerEventListeners();
         this.eventsManager.notifyContextCreatedEvent();
         this.jacImpl.setContextStarted(true);
         this.filterManager.preloadFilters();
         this.loadServletsOnStartup();
      } finally {
         popEnvironment(var1, var2);
      }

      if (this.asyncInitServlets.isEmpty()) {
         this.asyncInitsStillRunning = false;
      } else {
         this.asyncInitsStillRunning = true;
         WorkManagerFactory.getInstance().getDefault().schedule(new AsyncInitRequest(this.asyncInitServlets));
      }

   }

   public static final Throwable getRootCause(ServletException var0) {
      for(int var1 = 0; var0.getRootCause() instanceof ServletException && var1 < 10; ++var1) {
         Throwable var2 = var0.getRootCause();
         if (var0 == var2) {
            break;
         }

         var0 = (ServletException)var2;
      }

      return (Throwable)(var0.getRootCause() == null ? var0 : var0.getRootCause());
   }

   private void loadServletsOnStartup() throws DeploymentException {
      this.startedServletLoadSequences = true;

      try {
         Collection var1 = this.servletLoadSequences.tailMap(new Integer(0)).values();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ArrayList var3 = (ArrayList)var2.next();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               this.preloadServlet(var5);
            }
         }
      } finally {
         this.startedServletLoadSequences = false;
      }

   }

   private void preloadServlet(String var1) throws DeploymentException {
      ServletStubImpl var2 = (ServletStubImpl)this.servletStubs.get(var1);
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.getLogContext() + ": loading servlet on startup: " + var1);
      }

      try {
         String var3 = null;
         if (var2 instanceof JspStub) {
            var3 = ((JspStub)var2).getFilePath();
         }

         ContextRequestCallback var9 = new ContextRequestCallback(this, var3);
         var2.prepareServlet(var9);
      } catch (ServletException var6) {
         Throwable var8 = getRootCause(var6);
         Loggable var5 = HTTPLogger.logServletFailedToPreloadOnStartupLoggable(var1, this.contextName, var8);
         var5.log();
         if (!(var8 instanceof UnavailableException)) {
            throw new DeploymentException(var5.getMessage(), var8);
         }
      } catch (Exception var7) {
         Loggable var4 = HTTPLogger.logServletFailedToPreloadOnStartupLoggable(var1, this.contextName, var7);
         var4.log();
         throw new DeploymentException(var4.getMessage(), var7);
      }

   }

   public void addAsyncInitServlet(AsyncInitServlet var1) {
      this.asyncInitServlets.add(var1);
   }

   public void addMimeMapping(String var1, String var2) {
      this.configManager.addMimeMapping(var1, var2);
   }

   public boolean isSSLRequired(String var1, String var2) {
      var1 = HttpParsing.ensureStartingSlash(var1);
      return this.getSecurityManager().getWebAppSecurity().isSSLRequired(var1, var2);
   }

   public ServletStubImpl getServletStub(String var1) {
      ServletStubImpl var2 = null;
      URLMatchHelper var3 = (URLMatchHelper)this.servletMapping.get(var1);
      if (var3 != null) {
         var2 = var3.getServletStub();
      }

      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.getLogContext() + ": servlet " + (var2 == null ? "not found" : "found") + " for the url-pattern: " + var1);
      }

      return var2;
   }

   synchronized void removeServletStub(ServletStubImpl var1, boolean var2) {
      String var3 = var1.getServletName();
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.getLogContext() + ": removing servlet stub with name: " + var3);
      }

      ServletStubImpl var4 = (ServletStubImpl)this.servletStubs.get(var3);
      if (var4 != var1) {
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug(this.getLogContext() + ": the stub is different, not destroying it");
         }

      } else {
         this.servletStubs.remove(var3);
         var1.destroy();
         URLMapping var5 = (URLMapping)((URLMapping)this.servletMapping.clone());
         Object[] var6 = var5.values();

         for(int var7 = 0; var6 != null && var7 < var6.length; ++var7) {
            URLMatchHelper var8 = (URLMatchHelper)var6[var7];
            if (var3.equals(var8.getServletStub().getServletName())) {
               if (var2) {
                  var5.put(var8.getPattern(), new URLMatchHelper(var8.getPattern(), ServletStubImpl.getUnavailableStub(var1)));
               } else {
                  var5.remove(var8.getPattern());
               }
            }
         }

         this.servletMapping = var5;
      }
   }

   void registerServletStub(String var1, ServletStubImpl var2) {
      ServletStubImpl var3 = (ServletStubImpl)this.servletStubs.put(var1, var2);
      if (var3 != null) {
         var3.destroy();
      }

   }

   public boolean webflowCheckAccess(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      var1 = HttpParsing.ensureStartingSlash(var1);
      var2.setAttribute("webflow_resource", var1);

      boolean var4;
      try {
         var4 = this.securityManager.checkAccess(var2, var3, false);
      } catch (ServletException var6) {
         return false;
      } catch (SocketException var7) {
         if (HTTPDebugLogger.isEnabled()) {
            HTTPLogger.logException(this.getLogContext(), var7);
         }

         var4 = false;
      } catch (IOException var8) {
         HTTPLogger.logIOException(this.getLogContext(), var8);
         var4 = false;
      }

      var2.removeAttribute("webflow_resource");
      return var4;
   }

   private String prependContextPath(String var1) {
      var1 = HttpParsing.ensureStartingSlash(var1);
      if (!this.isDefaultContext()) {
         var1 = this.getContextPath() + var1;
      }

      return var1;
   }

   void execute(ServletRequestImpl var1, ServletResponseImpl var2) throws IOException {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.getLogContext() + ": invoking servlet for : " + var1);
      }

      Thread var3 = Thread.currentThread();
      ClassLoader var4 = this.pushEnvironment(var3);

      try {
         if (StringUtils.indexOfIgnoreCase(var1.getRelativeUri(), "/WEB-INF") == 0 || StringUtils.indexOfIgnoreCase(var1.getRelativeUri(), "/META-INF") == 0) {
            var2.sendError(404);
            return;
         }

         if (!var1.getSendRedirect()) {
            if (!this.isStarted()) {
               var2.sendError(503);
               return;
            }

            if (var2.getStatus() != 200) {
               var2.sendError(var2.getStatus());
               return;
            }

            ServletStubImpl var5 = var1.getServletStub();
            if (var5 == null) {
               var2.sendError(404);
               return;
            }

            if (this.isSuspended()) {
               Loggable var6 = HTTPLogger.logServerSuspendedLoggable(this.toString(), ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID).getServer().getName());
               throw new UnavailableException(var6.getMessage());
            }

            if (!this.checkPermissionOnPort(var1, var2)) {
               return;
            }

            if (this.asyncInitsStillRunning) {
               var2.sendError(503);
               return;
            }

            this.securedExecute(var1, var2, true);
            return;
         }

         var2.sendRedirect(var2.encodeRedirectURL(var1.getRedirectURI()));
      } catch (JspFileNotFoundException var11) {
         var2.sendError(404);
         return;
      } catch (Throwable var12) {
         this.handleThrowableFromInvocation(var12, var1, var2);
         return;
      } finally {
         popEnvironment(var3, var4);
      }

   }

   private void handleOOME(Throwable var1) {
      if (var1 instanceof OutOfMemoryError || var1.getCause() instanceof OutOfMemoryError) {
         HealthMonitorService.panic(var1);
      }

   }

   public void securedExecute(HttpServletRequest var1, HttpServletResponse var2, boolean var3) throws Throwable {
      if (this.securityManager.checkAccess(var1, var2, var3)) {
         HttpSession var4 = var1.getSession(false);
         if (this.isSuspending() && var4 == null) {
            Loggable var10 = HTTPLogger.logServerSuspendedLoggable(this.toString(), ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID).getServer().getName());
            throw new UnavailableException(var10.getMessage());
         } else {
            if (var4 != null) {
               int var5 = ((SessionInternal)var4).getConcurrentRequestCount();
               if (maxConcurrentRequestsAllowed != -1 && var5 > maxConcurrentRequestsAllowed) {
                  this.logError("Rejecting request since concurrent requests allowable limit exceeded :" + maxConcurrentRequestsAllowed);
                  var2.sendError(500);
                  return;
               }
            }

            ServletRequestImpl var9 = ServletRequestImpl.getOriginalRequest(var1);
            if (!doNotSendContinueHeader && var9.getInputHelper().getRequestParser().isProtocolVersion_1_1() && this.getSecurityManager().getWebAppSecurity().getAuthMethod() != null && !this.getSecurityManager().getWebAppSecurity().isFormAuth() && !var9.getConnection().isInternalDispatch()) {
               String var6 = var9.getRequestHeaders().getExpect();
               if ("100-continue".equalsIgnoreCase(var6)) {
                  var9.send100ContinueResponse();
               }
            }

            AuthenticatedSubject var11 = SecurityModule.getCurrentUser(this.getServer(), var1);
            if (var11 == null) {
               var11 = SubjectUtils.getAnonymousSubject();
            } else {
               var9.getHttpAccountingInfo().setRemoteUser(SubjectUtils.getUsername(var11));
            }

            ServletInvocationAction var7 = new ServletInvocationAction(var1, var2, var9.getServletStub());
            Throwable var8 = (Throwable)SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, var11, var7);
            if (var8 != null) {
               throw var8;
            }
         }
      }
   }

   public void handleThrowableFromInvocation(Throwable var1, ServletRequestImpl var2, ServletResponseImpl var3) throws IOException {
      boolean var12;
      boolean var10000 = var12 = _WLDF$INST_FLD_Servlet_Context_Handle_Throwable_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var13 = null;
      DiagnosticActionState[] var14 = null;
      Object var11 = null;
      if (var10000) {
         Object[] var7 = null;
         if (_WLDF$INST_FLD_Servlet_Context_Handle_Throwable_Around_High.isArgumentsCaptureNeeded()) {
            var7 = new Object[]{this, var1, var2, var3};
         }

         DynamicJoinPoint var21 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var7, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Servlet_Context_Handle_Throwable_Around_High;
         DiagnosticAction[] var10002 = var13 = var10001.getActions();
         InstrumentationSupport.preProcess(var21, var10001, var10002, var14 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         if (!this.isSuspended()) {
            Throwable var4 = var1;
            Throwable var5;
            if (var1 instanceof ServletException) {
               var1 = getRootCause((ServletException)var1);
               this.handleOOME(var1);
               if (this.isSuspending() && var1 instanceof UnavailableException) {
                  if (HTTPDebugLogger.isEnabled()) {
                     HTTPLogger.logRootCause(addUpRequestInfo(this.httpServer, var2, this.getLogContext()), var1);
                  }
               } else if (var1 instanceof SocketException) {
                  if (HTTPDebugLogger.isEnabled()) {
                     HTTPLogger.logRootCause(this.getLogContext(), var1);
                  }
               } else if (var1 instanceof IOException) {
                  HTTPLogger.logRootCause(addUpRequestInfo(this.httpServer, var2, this.getLogContext()), var1);
               } else {
                  HTTPLogger.logRootCause(addUpRequestInfo(this.httpServer, var2, this.getLogContext()), var1);
               }

               var5 = ((ServletException)var4).getRootCause();
               var2.setAttribute("javax.servlet.error.exception", var5 == null ? var4 : var5);
            } else if (var1 instanceof SocketException) {
               if (HTTPDebugLogger.isEnabled()) {
                  HTTPLogger.logException(this.getLogContext(), var1);
               }
            } else if (var1 instanceof IOException) {
               HTTPLogger.logIOException(this.getLogContext(), var1);
            } else if (var1 instanceof NestedRuntimeException) {
               var5 = ((NestedRuntimeException)var1).getNestedException();
               if (var5 instanceof SocketException) {
                  if (HTTPDebugLogger.isEnabled()) {
                     HTTPLogger.logException(this.getLogContext(), var1);
                  }
               } else if (var5 instanceof IOException) {
                  HTTPLogger.logIOException(this.getLogContext(), var1);
               }
            } else if (!"Internal Servlet Session Process Error Found!".equals(var1.getMessage())) {
               HTTPLogger.logException(this.getLogContext(), var1);
            }

            if (var3.isCommitted()) {
               return;
            }

            try {
               this.errorManager.handleException(var2, var3, var4);
               return;
            } catch (SocketException var18) {
               if (!HTTPDebugLogger.isEnabled()) {
                  return;
               }

               throw var18;
            } catch (IOException var19) {
               throw var19;
            }
         }

         var3.sendError(503);
      } finally {
         if (var12) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_Context_Handle_Throwable_Around_High, var13, var14);
         }

      }

   }

   private boolean checkPermissionOnPort(ServletRequestImpl var1, ServletResponseImpl var2) throws IOException {
      if (!var1.getConnection().isInternalDispatch()) {
         ServerChannel var3 = var1.getServerChannel();
         ServerChannel var4 = ServerChannelManager.findLocalServerChannel(ProtocolHandlerAdmin.PROTOCOL_ADMIN);
         if (var4 != null) {
            if (ChannelHelper.isAdminChannel(var3)) {
               if (!this.isInternalApp() && !this.isAdminMode() && !this.configManager.isRequireAdminTraffic()) {
                  var2.sendError(403, "Operation is not allowed on an administration channel");
                  return false;
               }
            } else if (!this.internalUtilitiesWebApp && (this.isInternalApp() || this.configManager.isRequireAdminTraffic()) && !this.internalUtilitiesWebSvcs && !this.isInternalSAMLApp() && !var1.getServletStub().isClasspathServlet()) {
               var2.sendError(403, "Console/Management requests or requests with &lt;require-admin-traffic&gt; specified to 'true' can only be made through an administration channel");
               HTTPLogger.logInternalAppWrongPort(var3.getPublicAddress(), var3.getPublicPort(), var3.getChannelName(), var4.getPublicAddress(), var4.getPublicPort(), var4.getChannelName(), this.getContextPath());
               return false;
            }
         }
      }

      return true;
   }

   static boolean isAbsoluteURL(String var0) {
      String[] var1 = StringUtils.split(var0, ':');
      if (var1[0] != null && var1[1] != null) {
         return var1[0].equals("mailto") || var1[1].startsWith("//");
      } else {
         return false;
      }
   }

   static boolean isAbsoluteFilePath(String var0) {
      if (WIN_32 && var0.length() > 2 && Character.isLetter(var0.charAt(0)) && var0.charAt(1) == ':') {
         return true;
      } else {
         return var0.length() > 0 && var0.charAt(0) == FSC;
      }
   }

   ServletStubImpl resolveDirectRequest(ServletRequestImpl var1) {
      String var2 = var1.getRelativeUri();
      URLMatchHelper var3 = this.resolveRequest(var2);
      if (var3.isDefaultServlet()) {
         var1.setCheckIndexFile(true);
      }

      var1.setServletPathAndPathInfo(var2, var3.getServletPath(var2));
      ServletStubImpl var4 = var3.getServletStub();
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug("Servlet resource: " + var4 + " is mapped to request: " + var1.toStringSimple());
      }

      return var4;
   }

   ServletStubImpl resolveForwardedRequest(ServletRequestImpl var1, ServletRequest var2) {
      String var3 = var1.getRelativeUri();
      URLMatchHelper var4 = this.resolveRequest(var3);
      if (var4.isDefaultServlet()) {
         ServletStubImpl var5 = this.getIndexServletStub(var3, var1, var2);
         if (var5 != null) {
            return var5;
         }
      }

      var1.setServletPathAndPathInfo(var3, var4.getServletPath(var3));
      return var4.getServletStub();
   }

   ServletStubImpl resolveIncludedRequest(ServletRequestImpl var1, ServletRequest var2) {
      String var3 = (String)var2.getAttribute("javax.servlet.include.request_uri");
      if (var3 != null && !this.isDefaultContext() && var3.startsWith(var1.getContext().getContextPath())) {
         var3 = var3.substring(var1.getContext().getContextPath().length());
      }

      URLMatchHelper var4 = this.resolveRequest(var3);
      this.setIncludeServletPath(var3, var2, var4.getServletPath(var3));
      return var4.getServletStub();
   }

   private void setIncludeServletPath(String var1, ServletRequest var2, String var3) {
      String var4 = ServletRequestImpl.computePathInfo(var1, var3);
      var2.setAttribute("javax.servlet.include.servlet_path", var3);
      var2.setAttribute("javax.servlet.include.path_info", var4);
   }

   ServletStubImpl getIndexServletStub(String var1, ServletRequestImpl var2, ServletRequest var3) {
      String var4 = this.findIndexFile(var1);
      if (var4 == null) {
         return null;
      } else if ((!this.isDefaultContext() || var1.length() != 0) && !StringUtils.endsWith(var1, '/')) {
         String var9 = var2.getRequestURI();
         int var6 = var9.indexOf(59);
         StringBuffer var7 = new StringBuffer();
         if (var6 != -1) {
            var7.append(HttpParsing.ensureEndingSlash(var9.substring(0, var6)));
            var7.append(var9.substring(var6));
         } else {
            var7.append(HttpParsing.ensureEndingSlash(var9));
         }

         String var8 = var2.getQueryString();
         if (var8 != null) {
            var7.append('?').append(var8);
         }

         if (DEBUG_URL_RES.isDebugEnabled()) {
            DEBUG_URL_RES.debug(this.getLogContext() + ": redirecting " + var2 + " to :" + var7.toString());
         }

         var2.setRedirectURI(var7.toString());
         return null;
      } else {
         var2.initFromRequestURI(this.prependContextPath(var4));
         ServletStubImpl var5 = this.resolveDirectRequest(var2);
         if (var5.getClassName().equals("weblogic.servlet.proxy.HttpProxyServlet") || var5.getClassName().equals("weblogic.servlet.proxy.HttpClusterServlet")) {
            var2.initFromRequestURI(this.prependContextPath(var1));
            var5 = this.resolveDirectRequest(var2);
         }

         return var5;
      }
   }

   WebAppHelper getHelper() {
      return this.helper;
   }

   private URLMatchHelper resolveRequest(String var1) {
      if (DEBUG_URL_RES.isDebugEnabled()) {
         DEBUG_URL_RES.debug(this.getLogContext() + ": resolving request with relUri: " + var1);
      }

      URLMatchHelper var2 = (URLMatchHelper)this.servletMapping.get(var1);
      if (var2 == null) {
         int var3;
         if (!WebAppConfigManager.isCaseInsensitive()) {
            var3 = var1.indexOf(".jws/");
         } else {
            var3 = StringUtils.indexOfIgnoreCase(var1, ".jws/");
         }

         if (var3 != -1) {
            String var4 = var1.substring(0, var3 + 4);
            var2 = (URLMatchHelper)this.servletMapping.get(var4);
         }
      }

      if (var2 == null) {
         var2 = this.defaultURLMatchHelper;
      }

      return var2;
   }

   private String findIndexFile(String var1) {
      String[] var2 = this.indexFiles;
      if (var2 == null) {
         return null;
      } else {
         var1 = HttpParsing.ensureEndingSlash(var1);
         WarSource var3 = this.getResourceAsSource(var1);
         if (var3 == null) {
            return null;
         } else {
            if (var3.isDirectory()) {
               for(int var4 = 0; var4 < var2.length; ++var4) {
                  String var5 = var1 + var2[var4];
                  WarSource var6 = this.getResourceAsSource(var5);
                  if (var6 != null) {
                     return var5;
                  }

                  URLMatchHelper var7 = (URLMatchHelper)this.servletMapping.get(var5);
                  if (var7 != null && var7.isIndexServlet()) {
                     return var5;
                  }
               }
            }

            return null;
         }
      }
   }

   public String getClasspath() {
      return this.war.getClassFinder().getClassPath();
   }

   public String getFullClasspath() {
      TagFileClassLoader var1 = this.getTagFileHelper().getTagFileClassLoader();
      String var2 = var1.getClassPath();
      return FilenameEncoder.cleanClasspath(var2);
   }

   public String getLogContext() {
      return this.logContext == null ? this.toString() : this.logContext;
   }

   public WebAppModule getWebAppModule() {
      return this.module;
   }

   String getModuleName() {
      return this.compMBean == null ? "webapp" : this.compMBean.getName();
   }

   public String getAppName() {
      if (this.appCtx == null) {
         return "";
      } else {
         AppDeploymentMBean var1 = this.appCtx.getAppDeploymentMBean();
         return var1 == null ? "" : var1.getApplicationName();
      }
   }

   public String getAppDisplayName() {
      if (this.appCtx == null) {
         return "";
      } else {
         AppDeploymentMBean var1 = this.appCtx.getAppDeploymentMBean();
         return var1 == null ? null : ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var1);
      }
   }

   public SessionContext getSessionContext() {
      return this.sessionContext;
   }

   public String getURI() {
      return this.module == null ? null : this.module.getModuleURI();
   }

   public String getId() {
      return this.module == null ? this.contextName : this.module.getId();
   }

   public JspResourceProvider getJspResourceProvider() {
      return this.jspResourceProvider;
   }

   private void setDocroot(String var1) throws DeploymentException {
      this.initializeTempDir();
      File var2 = this.getRootTempDir();
      this.processDocroot(var1, var2);
   }

   private void mergeLibraryDescriptors() throws DeploymentException {
      if (this.module != null) {
         if (this.module.getWlWebAppBean() != null || this.libraryManager.getAutoReferencedLibraries().length != 0) {
            try {
               this.mergeLibraryDescriptors("WEB-INF/web.xml");
               this.mergeLibraryDescriptors("WEB-INF/weblogic.xml");
            } catch (Exception var3) {
               Loggable var2 = HTTPLogger.logLibraryDescriptorMergeFailedLoggable(this.getDocroot(), var3.getMessage(), var3);
               var2.log();
               throw new DeploymentException(var2.getMessage(), var3);
            }
         }
      }
   }

   private void mergeLibraryDescriptors(String var1) throws IOException, Exception {
      boolean var2 = "WEB-INF/web.xml".equals(var1);
      Enumeration var3 = this.getResourceFinder("/").getSources("/" + var1);
      ArrayList var4 = Collections.list(var3);
      if (var4.size() != 0) {
         if (var4.size() != 1 || !var2 || !this.module.hasWebDescriptorFile()) {
            if (!this.war.hasExtensions() && (var2 && this.module.hasWebDescriptorFile() || !var2)) {
               var4.remove(0);
            }

            Source[] var5 = (Source[])((Source[])var4.toArray(new Source[0]));
            if (var5.length >= 1) {
               this.module.mergeLibraryDescriptors(var5, var1);
            }

         }
      }
   }

   private void processWebAppLibraries(File var1) throws DeploymentException {
      if (this.module != null) {
         LibraryRefBean[] var2;
         if (this.module.getWlWebAppBean() == null) {
            var2 = new LibraryRefBean[0];
         } else {
            var2 = this.module.getWlWebAppBean().getLibraryRefs();
         }

         J2EELibraryReference[] var3 = null;

         try {
            var3 = LibraryReferenceFactory.getWebLibReference(var2);
         } catch (IllegalSpecVersionTypeException var7) {
            throw new DeploymentException(HTTPLogger.logIllegalWebLibSpecVersionRefLoggable(this.getLogContext(), var7.getSpecVersion()).getMessage());
         }

         this.libraryManager = new LibraryManager(new LibraryReferencer(this.getURI(), this.runtime, "Unresolved Webapp Library references for \"" + this.getLogContext() + "\", " + "defined in weblogic.xml"), var3);
         this.appCtx.addLibraryManager(this.getId(), this.libraryManager);
         if (this.libraryManager.hasUnresolvedReferences()) {
            throw new DeploymentException("Error: " + this.libraryManager.getUnresolvedReferencesError());
         } else {
            Library[] var4 = this.libraryManager.getReferencedLibraries();

            try {
               for(int var5 = 0; var5 < var4.length; ++var5) {
                  this.war.addLibrary(var4[var5], var1);
               }

               if (!this.isInternalApp() && !this.isInternalSAMLApp()) {
                  this.libraryManager.lookupAndAddAutoReferences(Type.WAR, LibraryConstants.AutoReferrer.WebApp);
                  Library[] var9 = this.libraryManager.getAutoReferencedLibraries();

                  for(int var10 = 0; var10 < var9.length; ++var10) {
                     this.war.addLibrary(var9[var10], var1);
                  }
               }
            } catch (IOException var8) {
               Loggable var6 = HTTPLogger.logErrorSettingDocumentRootLoggable(this.toString(), this.getDocroot(), var8);
               var6.log();
               throw new DeploymentException(var6.getMessage(), var8);
            }

            this.libraryManager.addReferences();
         }
      }
   }

   private void processDocroot(String var1, File var2) throws DeploymentException {
      try {
         File var3 = WebAppConfigManager.getDocrootFile(var1, this);
         this.docroot = var3.getCanonicalPath();
         if (DEBUG_URL_RES.isDebugEnabled()) {
            DEBUG_URL_RES.debug(this.getLogContext() + ": Creating a War() object for '" + this.getURI() + "' ");
         }

         try {
            this.war = new War(this.getURI(), var2, this);
         } catch (JarFileUtils.PathLengthException var6) {
            Loggable var5 = HTTPLogger.logExtractionPathTooLongLoggable(this.toString(), var1, var6);
            var5.log();
            throw new DeploymentException(var5.getMessage(), var6);
         }

         this.jarFiles = new HashMap();
         this.war.populateJarMap(this.jarFiles);
      } catch (IOException var7) {
         Loggable var4 = HTTPLogger.logErrorSettingDocumentRootLoggable(this.toString(), var1, var7);
         var4.log();
         throw new DeploymentException(var4.getMessage(), var7);
      }
   }

   public File getRootTempDir() {
      return this.rootTempDir;
   }

   public String getTempPath() {
      if (this.tempPath == null) {
         String var1 = ApplicationVersionUtils.replaceDelimiter(this.getApplicationId(), '_');
         String var2 = ApplicationVersionUtils.replaceDelimiter(this.getName(), '_');
         this.tempPath = PathUtils.generateTempPath(this.getServer().getName(), var1, var2);
      }

      return this.tempPath;
   }

   private void initializeTempDir() {
      this.rootTempDir = PathUtils.getAppTempDir(this.getTempPath());
      if (!this.rootTempDir.exists() && !this.rootTempDir.mkdirs()) {
         HTTPLogger.logUnableToMakeDirectory(this.getLogContext(), this.rootTempDir.getAbsolutePath());
      }

      File var1 = null;
      String var2 = this.configManager.getTempDir();
      if (var2 == null) {
         var1 = new File(this.rootTempDir, "public");
      } else if (isAbsoluteFilePath(var2)) {
         var1 = new File(var2);
      } else {
         var1 = new File(this.rootTempDir, var2);
      }

      if (!var1.exists() && !var1.mkdirs()) {
         HTTPLogger.logUnableToMakeDirectory(this.getLogContext(), var1.getAbsolutePath());
      }

      this.setAttribute("javax.servlet.context.tempdir", var1);
   }

   public String getDocroot() {
      return this.docroot;
   }

   private void setContextPath(String var1) {
      this.defaultContext = var1.equals("/") || var1.equals("");
      if (this.defaultContext) {
         this.contextPath = "";
      } else {
         if (!var1.startsWith("/")) {
            var1 = "/" + var1;
         }

         if (var1.length() > 2 && var1.endsWith("/")) {
            var1 = var1.substring(0, var1.length() - 1);
         }

         this.contextPath = var1;
      }

   }

   void precompileJspsOnUpdate(String var1) {
      ((War.ResourceFinder)this.getResourceFinder(this.docroot)).clearCache(var1);
      if (this.getJSPManager().createJspConfig().getPageCheckSecs() == -1L) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = this.pushEnvironment(var2);

         try {
            if (!var1.startsWith("/")) {
               var1 = "/" + var1;
            }

            String var4 = JSPServlet.uri2classname(this.getJSPManager().getJspcPkgPrefix(), var1);
            Iterator var5 = this.servletStubs.values().iterator();

            while(true) {
               JspStub var7;
               do {
                  ServletStubImpl var6;
                  do {
                     if (!var5.hasNext()) {
                        return;
                     }

                     var6 = (ServletStubImpl)var5.next();
                  } while(!(var6 instanceof JspStub));

                  var7 = (JspStub)var6;
               } while(!var7.getClassName().equals(var4));

               try {
                  ContextRequestCallback var8 = new ContextRequestCallback(this, var1);
                  var7.reloadJSPOnUpdate(var8);
                  return;
               } catch (Exception var13) {
                  Loggable var9 = HTTPLogger.logFailureInCompilingJSPLoggable(this.getAppName(), this.getModuleName(), var1, var13);
                  var9.log();
               }
            }
         } finally {
            popEnvironment(var2, var3);
         }
      }
   }

   public String getContextPath() {
      return this.contextPath;
   }

   public String getName() {
      return this.contextName;
   }

   public String getFullCtxName() {
      return this.fullCtxName;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public boolean isAdminMode() {
      return this.adminMode;
   }

   public void setAdminMode(boolean var1) {
      this.adminMode = var1;
   }

   public ApplicationContextInternal getApplicationContext() {
      return this.appCtx;
   }

   public String getApplicationName() {
      String var1 = this.appCtx.getApplicationId();
      return var1 != null ? ApplicationVersionUtils.getApplicationName(var1) : this.getName();
   }

   public String getApplicationId() {
      String var1 = this.appCtx.getApplicationId();
      return var1 != null ? var1 : this.getName();
   }

   public String getSecurityRealmName() {
      String var1 = null;
      if (this.appCtx != null) {
         var1 = this.appCtx.getApplicationSecurityRealmName();
      }

      return var1 == null ? "weblogicDEFAULT" : var1;
   }

   public Map getJarFiles() {
      return this.jarFiles;
   }

   public War getWarInstance() {
      return this.war;
   }

   public WebAppComponentMBean getMBean() {
      return this.compMBean;
   }

   public WebAppRuntimeMBeanImpl getRuntimeMBean() {
      return this.runtime;
   }

   public synchronized ServletRuntimeMBean[] getServletRuntimeMBeans() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.servletStubs.values().iterator();

      while(var2.hasNext()) {
         ServletStubImpl var3 = (ServletStubImpl)var2.next();
         if (var3.getRuntimeMBean() != null) {
            var1.add(var3.getRuntimeMBean());
         }
      }

      return (ServletRuntimeMBean[])((ServletRuntimeMBean[])var1.toArray(new ServletRuntimeMBean[var1.size()]));
   }

   private void initClassLoader(boolean var1) throws DeploymentException {
      if (var1 && this.classLoader != null) {
         BeanELResolverCachePurger.purgeCache(this.classLoader);
      }

      if (this.module != null) {
         this.module.setClassFinder(this.war.getClassFinder());
         this.classLoader = this.module.getWebClassLoader(var1);
      } else {
         this.classLoader = new GenericClassLoader(this.war.getClassFinder());
         if (this.appCtx != null && this.appCtx.getAppDeploymentMBean() != null) {
            AppDeploymentMBean var2 = this.appCtx.getAppDeploymentMBean();
            Annotation var3 = new Annotation(var2.getApplicationIdentifier(), this.getName());
            ((GenericClassLoader)this.classLoader).setAnnotation(var3);
         }
      }

      if (this.classLoader instanceof ChangeAwareClassLoader) {
         ChangeAwareClassLoader var9 = (ChangeAwareClassLoader)this.classLoader;
         var9.setChildFirst(this.configManager.getPreferWebInfClasses());
         if (this.module != null) {
            WeblogicWebAppBean var10 = this.module.getWlWebAppBean();
            if (var10 != null) {
               ContainerDescriptorBean var4 = (ContainerDescriptorBean)DescriptorUtils.getFirstChildOrDefaultBean(var10, var10.getContainerDescriptors(), "ContainerDescriptor");
               if (var4 != null && var4.isPreferWebInfClasses()) {
                  this.configManager.setPreferWebInfClasses(true);
                  var9.setChildFirst(this.configManager.getPreferWebInfClasses());
                  if (this.appCtx != null && this.appCtx.getAppDeploymentMBean() != null) {
                     try {
                        ClassFinder var5 = AppFileOverrideUtils.getFinderIfRequired(this.appCtx.getAppDeploymentMBean(), this.appCtx.isEar() ? this.getURI() : null);
                        if (var5 != null) {
                           var9.addClassFinderFirst(var5);
                        }
                     } catch (ModuleException var6) {
                     }
                  }
               }
            }
         }
      }

      if (this.module != null) {
         try {
            if (var1 && this.classLoader != null) {
               this.module.reloadPersistenceUnitRegistry();
            } else {
               this.module.setupPersistenceUnitRegistry();
            }
         } catch (EnvironmentException var7) {
            throw new DeploymentException(var7);
         } catch (MalformedURLException var8) {
            throw new DeploymentException(var8);
         }
      }

      this.tagFileHelper.initClassLoader(this.war.getClassFinder(), this.classLoader);
      if (this.classLoader instanceof GenericClassLoader) {
         SpringInstrumentationUtils.addSpringInstrumentor((GenericClassLoader)this.classLoader);
      }

   }

   public final ClassLoader getServletClassLoader() {
      return this.classLoader;
   }

   ClassLoader reloadServletClassLoader() {
      synchronized(this.reloadServletClassLoaderLock) {
         ClassLoader var2 = Thread.currentThread().getContextClassLoader();
         if (var2 != this.getServletClassLoader()) {
            return this.classLoader;
         } else {
            this.eventsManager.notifyContextDestroyedEvent();
            this.attributes.remove("com.sun.faces.sunJsfJs");
            this.jacImpl = new JspApplicationContextImpl(this);

            try {
               this.initClassLoader(true);
            } catch (DeploymentException var14) {
               HTTPLogger.logFailedToBounceClassLoader(this.getAppDisplayName(), this.getId(), var14);
            }

            try {
               Thread.currentThread().setContextClassLoader(var2);
               this.removeTransientAttributes(var2);
            } finally {
               Thread.currentThread().setContextClassLoader(this.getServletClassLoader());
            }

            try {
               this.processAnnotations(true);
            } catch (DeploymentException var12) {
               HTTPLogger.logAnnotationProcessingFailed(this.getDocroot(), var12.getMessage(), var12);
            }

            try {
               this.eventsManager.registerEventListeners();
               this.eventsManager.notifyContextCreatedEvent();
            } catch (DeploymentException var11) {
            }

            this.jacImpl.setContextStarted(true);
            return this.classLoader;
         }
      }
   }

   public final TagFileHelper getTagFileHelper() {
      return this.tagFileHelper;
   }

   public synchronized void addClassPath(String var1) {
      this.war.addClassPath(var1);
   }

   public Context getEnvironmentContext() {
      return this.compEnv.getEnvironmentContext();
   }

   public ServletSecurityManager getSecurityManager() {
      return this.securityManager;
   }

   void activate() throws DeploymentException {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.getLogContext() + ": activating ...");
      }

      this.activateFromDescriptors();
      this.compEnv.prepare();
      this.compEnv.activate();
      this.sessionContext.initialize(this);
   }

   void start() throws DeploymentException {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.getLogContext() + ": starting ...");
      }

      this.preloadResources();
      this.sessionContext.startTimers();
      this.started = true;
   }

   void stop() {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.getLogContext() + ": stopping ...");
      }

      this.started = false;
      this.compEnv.cleanup();
   }

   public boolean isStarted() {
      return this.started;
   }

   private boolean isSuspending() {
      return WebAppShutdownService.isSuspending() && !this.isInternalApp();
   }

   private boolean isSuspended() {
      return WebAppShutdownService.isSuspended() && !this.isInternalApp();
   }

   private boolean isServerShutDown() {
      return WebAppShutdownService.isSuspending() || WebAppShutdownService.isSuspended();
   }

   public boolean isInternalApp() {
      return this.internalApp;
   }

   public boolean isInternalSAMLApp() {
      return this.internalSAMLApp;
   }

   public boolean isOnDemandDisplayRefresh() {
      return this.onDemandDisplayRefresh;
   }

   public synchronized void destroy() {
      if (!this.isInternalApp() || this.isServerShutDown()) {
         this.started = false;
         this.asyncInitsStillRunning = false;
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = this.pushEnvironment(var1);

         try {
            this.getSessionContext().destroy(this.isServerShutDown());
            this.destroyServlets();
            this.filterManager.destroyFilters();
            this.eventsManager.notifyContextDestroyedEvent();
            BeanELResolverCachePurger.purgeCache(this.classLoader);
         } finally {
            popEnvironment(var1, var2);
         }

         try {
            if (this.runtime != null) {
               if (this.libraryManager != null) {
                  this.libraryManager.removeReferences();
                  this.appCtx.removeLibraryManager(this.getId(), this.libraryManager);
                  this.libraryManager = null;
               }

               this.removeAttribute("weblogic.servlet.WebAppComponentRuntimeMBean");
               this.runtime.unregister();
               this.runtime = null;
            }
         } catch (ManagementException var6) {
            HTTPLogger.logErrorUnregisteringWebAppRuntime(this.runtime.getObjectName(), var6);
         }

         this.war.closeAllFinders();
         this.getSecurityManager().getWebAppSecurity().unregister();
         this.compEnv.destroy();
         this.jspManager.destroy();
         this.tagFileHelper.close();
         this.classLoader = null;
         this.contextManager = null;
      }
   }

   private void destroyServlets() {
      Collection var1 = this.servletLoadSequences.tailMap(new Integer(0)).values();
      Object[] var2 = var1.toArray();

      for(int var3 = var2.length - 1; var3 >= 0; --var3) {
         ArrayList var4 = (ArrayList)var2[var3];
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            ServletStubImpl var7 = (ServletStubImpl)this.servletStubs.remove(var6);
            if (var7 != null) {
               var7.destroy();
            }
         }
      }

      if (!this.servletStubs.isEmpty()) {
         Iterator var8 = this.servletStubs.values().iterator();

         while(var8.hasNext()) {
            ServletStubImpl var9 = (ServletStubImpl)var8.next();
            var8.remove();
            var9.destroy();
         }
      }

      this.servletStubs.clear();
   }

   private void addInitParameter(String var1, String var2) {
      this.initParams.put(var1, var2);
      if (var1.startsWith("weblogic.httpd.")) {
         String var3 = var1.substring("weblogic.httpd.".length());
         if ("defaultServlet".equalsIgnoreCase(var3)) {
            HTTPLogger.logDeprecatedContextParamDefaultServlet();
            this.setDefaultServlet(var2);
         } else if ("clientCertProxy".equalsIgnoreCase(var3)) {
            HTTPLogger.logDeprecatedContextParam(var1, "client-cert-proxy-enabled");
            if (var2.equalsIgnoreCase("true")) {
               this.configManager.setClientCertProxyEnabled(true);
            }
         } else if ("servlet.classpath".equalsIgnoreCase(var3)) {
            HTTPLogger.logDeprecatedContextParamClasspath();
            this.addClassPath(var2);
         } else if ("servlet.reloadCheckSecs".equalsIgnoreCase(var3)) {
            HTTPLogger.logDeprecatedContextParam(var1, "servlet-reload-check-secs");

            try {
               this.configManager.setServletReloadCheckSecs(Integer.parseInt(var2));
            } catch (NumberFormatException var5) {
               HTTPLogger.logReloadCheckSecondsError(this.getLogContext(), "weblogic.httpd.", "servlet.reloadCheckSecs", var5);
            }
         } else if ("inputCharset".regionMatches(true, 0, var3, 0, 12)) {
            HTTPLogger.logDeprecatedContextParam(var1, "charset-params");
            this.configManager.addInputEncoding(var3.substring(13), var2);
         }

      }
   }

   public boolean isResourceStale(String var1, long var2, String var4, String var5) {
      if (HTTPDebugLogger.isEnabled()) {
         Loggable var6 = HTTPLogger.logCompareVersionLoggable(this.getLogContext(), var4, version.getReleaseBuildVersion());
         HTTPDebugLogger.debug(var6.getMessage());
      }

      if (!NO_VERSION_CHECK && !version.getReleaseBuildVersion().equals(var4)) {
         HTTPLogger.logServerVersionMismatchForJSPisStale(this.getLogContext(), var1, var4, version.getReleaseBuildVersion());
         return true;
      } else if (this.jspManager.getPageCheckSeconds() < 0) {
         return false;
      } else if (this.jspManager.getResourceProviderClass() != null) {
         if (this.jspResourceProvider == null) {
            return true;
         } else {
            try {
               return var2 < this.jspResourceProvider.getLastModified(var1);
            } catch (Exception var7) {
               return true;
            }
         }
      } else {
         WarSource var8 = this.getResourceAsSource(var1, true);
         if (var8 == null) {
            return true;
         } else if (var8.isFromArchive() || !var8.isFromLibrary() && this.isArchived) {
            if (isResourceTimeStale(var8, var2, var5)) {
               HTTPLogger.logJSPisStale(this.getLogContext(), var1);
               return true;
            } else {
               return false;
            }
         } else if (!this.jspManager.isStrictStaleCheck()) {
            return var2 != var8.lastModified() + 2000L;
         } else {
            return var2 < var8.lastModified();
         }
      }
   }

   public static boolean isResourceTimeStale(Source var0, long var1, String var3) {
      if (var0 == null) {
         return true;
      } else {
         long var4 = var0.lastModified();
         if (var3 != null) {
            String var6 = TimeZone.getDefault().getID();
            if (!var3.equals(var6)) {
               GregorianCalendar var7 = new GregorianCalendar(TimeZone.getTimeZone(var3));
               GregorianCalendar var8 = new GregorianCalendar(TimeZone.getTimeZone(var6));
               var7.setTime(new Date(var4));
               var8.setTime(new Date(var4));
               long var9 = (long)(var7.get(15) + var7.get(16));
               long var11 = (long)(var8.get(15) + var8.get(16));
               var4 -= var9 - var11;
            }
         }

         return var1 < var4;
      }
   }

   public ClassFinder getResourceFinder(String var1) {
      return this.war.getResourceFinder(var1);
   }

   public WarSource getResourceAsSource(String var1) {
      return this.getResourceAsSource(var1, false);
   }

   private WarSource getResourceAsSource(String var1, boolean var2) {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(this.getLogContext() + " getResourceAsSource() invoked for : " + var1);
      }

      WarSource var3 = this.war.getResourceAsSource(var1, var2);
      if ((HTTPDebugLogger.isEnabled() || DEBUG_URL_RES.isDebugEnabled()) && var3 == null) {
         DEBUG_URL_RES.debug(this.getLogContext() + ": getResourceAsSource() couldn't find source for : " + var1);
      }

      return var3;
   }

   public WarSource getResourceAsSourceWithMDS(String var1) {
      if (this.mdsFinder != null) {
         Source var2 = this.mdsFinder.getSource(var1);
         if (var2 != null) {
            return new WarSource(var2);
         } else {
            if (HTTPDebugLogger.isEnabled() || DEBUG_URL_RES.isDebugEnabled()) {
               DEBUG_URL_RES.debug(this.getLogContext() + ": getResourceAsSourceWithMDS() couldn't find source for : " + var1);
            }

            return null;
         }
      } else {
         return this.getResourceAsSource(var1);
      }
   }

   void setDefaultContext() {
      ContextVersionManager var1 = this.getContextManager();
      if (var1 != null) {
         var1.setDefaultContext();
      }

      this.setContextPath("");
   }

   boolean isDefaultContext() {
      return this.defaultContext;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("ServletContext@");
      var1.append(super.hashCode()).append("[").append("app:").append(this.getAppName()).append(" module:").append(this.getName()).append(" path:").append(this.getContextPath());
      if (this.module != null && this.module.getWebAppBean() != null) {
         var1.append(" spec-version:").append(this.module.getWebAppBean().getVersion());
      }

      if (this.getVersionId() != null) {
         var1.append(" version:").append(this.getVersionId());
      }

      return var1.append("]").toString();
   }

   public final ClassLoader pushEnvironment(Thread var1) {
      Context var2 = this.getEnvironmentContext();
      javaURLContextFactory.pushContext(var2);
      ClassLoader var3 = var1.getContextClassLoader();
      var1.setContextClassLoader(this.getServletClassLoader());
      return var3;
   }

   public static final void popEnvironment(Thread var0, ClassLoader var1) {
      javaURLContextFactory.popContext();
      var0.setContextClassLoader(var1);
   }

   public Object getWebservicesDD() {
      return this.webservicesDD;
   }

   public void setWebservicesDD(Object var1) {
      this.webservicesDD = var1;
   }

   public void swapServlet(String var1, String var2, Map var3) throws ServletException {
      ServletStubImpl var4 = (ServletStubImpl)this.servletStubs.get(var1);
      if (var4 != null) {
         this.servletStubs.remove(var1);
         var4.destroy();
         if (var3 == null) {
            var3 = new HashMap();
         }

         Map var5 = var4.getInitParametersMap();
         if (var5 != null) {
            ((Map)var3).putAll(var5);
         }

         ServletStubImpl var6 = this.registerServletDefinition(var1, var2, (Map)var3, false);
         this.swapServletStubs(var4, var6);
      } else {
         throw new ServletException("There is no sevlet \"" + var1 + "\"" + " defined for web service.");
      }
   }

   private void swapServletStubs(ServletStubImpl var1, ServletStubImpl var2) throws ServletException {
      if (this.defaultURLMatchHelper != null && this.defaultURLMatchHelper.getServletStub() == var1) {
         this.defaultURLMatchHelper = new URLMatchHelper(this.defaultURLMatchHelper.getPattern(), var2);
      }

      Object[] var3 = this.servletMapping.values();
      if (var3 != null && var3.length >= 1) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            URLMatchHelper var5 = (URLMatchHelper)var3[var4];
            if (var5.getServletStub() == var1) {
               this.servletMapping.put(var5.getPattern(), new URLMatchHelper(var5.getPattern(), var2));
            }
         }

         StubSecurityHelper var9 = var1.getSecurityHelper();
         StubSecurityHelper var10 = var2.getSecurityHelper();

         try {
            var10.setRunAsIdentity(var9.getRunAsIdentity());
         } catch (DeploymentException var8) {
            throw new ServletException(var8);
         }

         Iterator var6 = var9.getRoleNames();
         if (var6 != null) {
            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               var10.addRoleLink(var7, var9.getRoleLink(var7));
            }
         }

      }
   }

   private void initResourceProvider() throws DeploymentException {
      String var1 = this.jspManager.getResourceProviderClass();
      if (var1 != null) {
         try {
            Class var2 = this.classLoader.loadClass(var1);
            this.jspResourceProvider = (JspResourceProvider)var2.newInstance();
            this.jspResourceProvider.init((String)null, this.getContextPath(), "/", this, (HttpServletRequest)null, (Hashtable)null);
            this.mdsFinder = new MDSClassFinder(this.jspResourceProvider);
         } catch (ClassNotFoundException var3) {
            throw new DeploymentException("Couldn't load jsp resource provider class: " + var1);
         } catch (InstantiationException var4) {
            throw new DeploymentException("Couldn't instantiate jsp resource provider class: " + var1);
         } catch (IllegalAccessException var5) {
            throw new DeploymentException("Couldn't instantiate jsp resource provider class: " + var1);
         }
      }

   }

   URLMapping getServletMapping() {
      return this.servletMapping;
   }

   public EventsManager getEventsManager() {
      return this.eventsManager;
   }

   public WebAppConfigManager getConfigManager() {
      return this.configManager;
   }

   public JSPManager getJSPManager() {
      return this.jspManager;
   }

   public ErrorManager getErrorManager() {
      return this.errorManager;
   }

   public FilterManager getFilterManager() {
      return this.filterManager;
   }

   public void dump(PrintStream var1) {
      println(var1, "==================== Internal Context Information ==================");
      println(var1, "contextName: " + this.getName());
      println(var1, "contextPath: " + this.getContextPath());
      println(var1, "classpath: " + this.getClasspath());
      println(var1, "defaultServletName: " + this.defaultServletName);
      println(var1, "indexFiles: " + this.indexFiles);
      println(var1, "docroot: " + this.docroot);
      println(var1, "isArchived: " + this.isArchived);
      println(var1, "reloadCheckSeconds: " + this.configManager.getServletReloadCheckSecs());
      println(var1, "classLoader: " + this.getServletClassLoader());
      println(var1, "environmentCtx: " + this.getEnvironmentContext());
      println(var1, "statusErrors: " + this.errorManager.getStatusErrors());
      println(var1, "attributes: " + this.attributes);
      println(var1, "taglibs: " + this.jspManager.getTagLibs());
      println(var1, "defaultMimeType: " + this.configManager.getDefaultMimeType());
      println(var1, "initParams: " + this.initParams);
      println(var1, "classFinder: " + this.war.getClassFinder());
      println(var1, "httpServer.isDebugHttp: " + HTTPDebugLogger.isEnabled());
      println(var1, "caseSensitive: " + !WebAppConfigManager.isCaseInsensitive());
      println(var1, "servletStubs: " + this.servletStubs);
      println(var1, "servletMapping: " + this.servletMapping);
      println(var1, "authRealmName: " + this.configManager.getAuthRealmName());
      println(var1, "securityManager: " + this.getSecurityManager());
      println(var1, "webAppSecurity: " + this.getSecurityManager().getWebAppSecurity());
      println(var1, "exceptionMap: " + this.errorManager.getStatusErrors());
   }

   private static void println(PrintStream var0, String var1) {
      var0.println(var1 + "<br>");
   }

   private static final String addUpRequestInfo(HttpServer var0, ServletRequestImpl var1, String var2) {
      return HttpServer.isProductionModeEnabled() ? var2 : var2 + ", request: " + var1;
   }

   public static void enableWLDFDyeInjection(Boolean var0) throws Exception {
      if (var0) {
         Class var1 = Class.forName("weblogic.diagnostics.instrumentation.support.DyeInjectionMonitorSupport");
         Class[] var2 = new Class[]{Object.class};
         wldfDyeInjectionMethod = var1.getMethod("dyeWebAppRequest", var2);
      } else {
         wldfDyeInjectionMethod = null;
      }

   }

   public void invalidateSession(HttpSession var1) {
      Thread var2 = Thread.currentThread();
      ClassLoader var3 = this.pushEnvironment(var2);

      try {
         var1.invalidate();
      } finally {
         popEnvironment(var2, var3);
      }

   }

   void removeTransientAttributes(ClassLoader var1) {
      this.attributes.removeTransientAttributes(var1, this);
   }

   public HttpServletRequest cloneRequest(HttpServletRequest var1) {
      ServletRequestImpl var2 = ServletRequestImpl.getOriginalRequest(var1);
      return var2 == null ? null : var2.copy();
   }

   public JspApplicationContext getJspApplicationContext() {
      return this.jacImpl;
   }

   boolean isJsfApplication() {
      return this.isJsfApplication;
   }

   public EnvironmentBuilder getEnvironmentBuilder() {
      return this.compEnv.getEnvironmentBuilder();
   }

   static {
      _WLDF$INST_FLD_Servlet_Context_Handle_Throwable_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Context_Handle_Throwable_Around_High");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WebAppServletContext.java", "weblogic.servlet.internal.WebAppServletContext", "handleThrowableFromInvocation", "(Ljava/lang/Throwable;Lweblogic/servlet/internal/ServletRequestImpl;Lweblogic/servlet/internal/ServletResponseImpl;)V", 2286, InstrumentationSupport.makeMap(new String[]{"Servlet_Context_Handle_Throwable_Around_High"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{null, InstrumentationSupport.createValueHandlingInfo("req", "weblogic.diagnostics.instrumentation.gathering.ServletRequestRenderer", false, true), null})}), (boolean)0);
      WIN_32 = System.getProperty("os.name", "unknown").toLowerCase(Locale.ENGLISH).indexOf("windows") >= 0;
      NO_VERSION_CHECK = Boolean.getBoolean("weblogic.jspc.skipVersionCheck");
      FSC = File.separatorChar;
      NON_BLOCKING_DISPATCH = new HashMap();
      DIRECT_DISPATCH = new HashMap();
      NON_BLOCKING_DISPATCH.put("wl-dispatch-policy", "weblogic.kernel.Non-Blocking");
      DIRECT_DISPATCH.put("wl-dispatch-policy", "direct");
      DEBUG_URL_RES = DebugLogger.getDebugLogger("DebugURLResolution");
      wldfDyeInjectionMethod = null;
      maxConcurrentRequestsAllowed = -1;
      mergeDescriptors = true;
      doNotSendContinueHeader = false;
      doNotSendContinueHeader = Boolean.getBoolean("doNotSendContinueHeader");
      String var0 = System.getProperty("weblogic.http.session.maxConcurrentRequest");
      String var1 = System.getProperty("weblogic.http.descriptor.merge");

      try {
         if (var0 != null) {
            maxConcurrentRequestsAllowed = Integer.parseInt(var0);
         }

         if (maxConcurrentRequestsAllowed < 1) {
            maxConcurrentRequestsAllowed = -1;
         }

         if (var1 != null && "false".equalsIgnoreCase(var1)) {
            mergeDescriptors = false;
         }
      } catch (NumberFormatException var3) {
      }

   }

   private class ServletContextWebAppHelper implements WebAppHelper {
      private static final String JSF_CONFIG_FILES = "javax.faces.CONFIG_FILES";
      private List annotatedClasses = null;
      private Set managedBeans;

      public ServletContextWebAppHelper() {
         this.managedBeans = Collections.EMPTY_SET;
      }

      public Set getTagListeners(boolean var1) {
         return WebAppServletContext.this.war.getTagClasses(var1, "listener-class");
      }

      public Set getTagHandlers(boolean var1) {
         return WebAppServletContext.this.war.getTagClasses(var1, "tag-class");
      }

      public Set getManagedBeanClasses() {
         if (this.managedBeans == Collections.EMPTY_SET && WebAppServletContext.this.isJsfApplication()) {
            String var1 = WebAppServletContext.this.getInitParameter("javax.faces.CONFIG_FILES");
            this.managedBeans = WebAppServletContext.this.war.getFacesManagedBeans(var1, WebAppServletContext.this.rootTempDir);
         }

         return this.managedBeans;
      }

      public Set getManagedBeanClasses(Set<String> var1) {
         this.managedBeans = this.getManagedBeanClasses();
         if (this.managedBeans == Collections.EMPTY_SET) {
            this.managedBeans = new HashSet();
         }

         this.managedBeans.addAll(var1);
         return this.managedBeans;
      }

      public List getAnnotatedClasses(WebAnnotationProcessor var1) {
         if (this.annotatedClasses == null) {
            this.annotatedClasses = WebAppServletContext.this.war.getAnnotatedClasses(var1);
         }

         return this.annotatedClasses;
      }
   }

   private final class ServletInvocationAction implements PrivilegedAction {
      private final HttpServletRequest req;
      private final HttpServletResponse rsp;
      private final ServletStubImpl stub;
      static final long serialVersionUID = 6236264500641467779L;
      public static final String _WLDF$INST_VERSION = "9.0.0";
      // $FF: synthetic field
      static Class _WLDF$INST_FLD_class = Class.forName("weblogic.servlet.internal.WebAppServletContext$ServletInvocationAction");
      public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Invocation_Around_Low;
      public static final JoinPoint _WLDF$INST_JPFLD_0;

      ServletInvocationAction(HttpServletRequest var2, HttpServletResponse var3, ServletStubImpl var4) {
         this.req = var2;
         this.rsp = var3;
         this.stub = var4;
      }

      public Object run() {
         return this.wrapRun(this.stub, this.req, this.rsp);
      }

      private Object wrapRun(ServletStubImpl var1, HttpServletRequest var2, HttpServletResponse var3) {
         boolean var13;
         boolean var10000 = var13 = _WLDF$INST_FLD_Servlet_Invocation_Around_Low.isEnabledAndNotDyeFiltered();
         DiagnosticAction[] var14 = null;
         DiagnosticActionState[] var15 = null;
         Object var12 = null;
         DelegatingMonitor var10001;
         DynamicJoinPoint var29;
         if (var10000) {
            Object[] var8 = null;
            if (_WLDF$INST_FLD_Servlet_Invocation_Around_Low.isArgumentsCaptureNeeded()) {
               var8 = new Object[]{this, var1, var2, var3};
            }

            var29 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var8, (Object)null);
            var10001 = _WLDF$INST_FLD_Servlet_Invocation_Around_Low;
            DiagnosticAction[] var10002 = var14 = var10001.getActions();
            InstrumentationSupport.preProcess(var29, var10001, var10002, var15 = InstrumentationSupport.getActionStates(var10002));
         }

         boolean var21 = false;

         label162: {
            IOException var31;
            label163: {
               Throwable var30;
               try {
                  label153: {
                     try {
                        var21 = true;
                        WebAppServletContext.this.httpServer.getWorkContextManager().initOrRestoreThreadContexts(WebAppServletContext.this, ServletRequestImpl.getOriginalRequest(this.req));
                        if (WebAppServletContext.wldfDyeInjectionMethod != null) {
                           try {
                              Object[] var27 = new Object[]{this.req};
                              WebAppServletContext.wldfDyeInjectionMethod.invoke((Object)null, var27);
                           } catch (Throwable var22) {
                           }
                        }

                        if (!WebAppServletContext.this.getFilterManager().hasFilters() && !WebAppServletContext.this.eventsManager.hasRequestListeners()) {
                           this.stub.execute(this.req, this.rsp);
                        } else {
                           FilterChainImpl var28 = WebAppServletContext.this.getFilterManager().getFilterChain(this.stub, this.req, this.rsp, WebAppServletContext.this.eventsManager.hasRequestListeners(), 0);
                           if (var28 == null) {
                              this.stub.execute(this.req, this.rsp);
                           } else {
                              var28.doFilter(this.req, this.rsp);
                           }
                        }
                     } catch (UnavailableException var24) {
                        UnavailableException var4 = var24;

                        try {
                           if (var4.isPermanent()) {
                              this.rsp.sendError(404);
                           } else {
                              long var5 = System.currentTimeMillis() + (long)(var4.getUnavailableSeconds() * 1000);
                              this.rsp.addHeader("Retry-After", (new Date(var5)).toString());
                              this.rsp.sendError(503);
                           }
                        } catch (IOException var23) {
                           var31 = var23;
                           var21 = false;
                           break label163;
                        }
                     } catch (Throwable var25) {
                        var30 = var25;
                        var21 = false;
                        break label153;
                     }

                     var29 = null;
                     var21 = false;
                     break label162;
                  }
               } finally {
                  if (var21) {
                     var10001 = null;
                     if (var13) {
                        InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_Invocation_Around_Low, var14, var15);
                     }

                  }
               }

               if (var13) {
                  InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_Invocation_Around_Low, var14, var15);
               }

               return var30;
            }

            if (var13) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_Invocation_Around_Low, var14, var15);
            }

            return var31;
         }

         if (var13) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_Invocation_Around_Low, var14, var15);
         }

         return var29;
      }

      static {
         _WLDF$INST_FLD_Servlet_Invocation_Around_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Invocation_Around_Low");
         _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WebAppServletContext.java", "weblogic.servlet.internal.WebAppServletContext$ServletInvocationAction", "wrapRun", "(Lweblogic/servlet/internal/ServletStubImpl;Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)Ljava/lang/Object;", 3702, InstrumentationSupport.makeMap(new String[]{"Servlet_Invocation_Around_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("stub", "weblogic.diagnostics.instrumentation.gathering.ServletStubImplRenderer", false, true), InstrumentationSupport.createValueHandlingInfo("req", "weblogic.diagnostics.instrumentation.gathering.ServletRequestRenderer", false, true), null})}), (boolean)0);
      }
   }

   private static final class ContextRequestCallback implements RequestCallback {
      private final String uri;
      private final WebAppServletContext ctx;

      ContextRequestCallback(WebAppServletContext var1, String var2) {
         this.ctx = var1;
         this.uri = var2;
      }

      public final String getIncludeURI() {
         return this.uri;
      }

      public final void reportJSPTranslationFailure(String var1, String var2) {
         this.ctx.logError(var1);
      }

      public final void reportJSPCompilationFailure(String var1, String var2) {
         this.ctx.logError(var1);
      }
   }

   private class AsyncInitRequest implements Runnable {
      private final List asyncInitServlets;

      AsyncInitRequest(List var2) {
         this.asyncInitServlets = var2;
      }

      public void run() {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = WebAppServletContext.this.pushEnvironment(var1);

         try {
            Iterator var3 = this.asyncInitServlets.iterator();

            while(var3.hasNext()) {
               AsyncInitServlet var4 = (AsyncInitServlet)var3.next();

               try {
                  var4.initDelegate();
               } catch (ServletException var9) {
                  if (!WebAppServletContext.this.asyncInitsStillRunning) {
                     return;
                  }

                  HTTPLogger.logAsyncInitFailed(var4.getClass().getName(), (Throwable)(var9.getRootCause() != null ? var9.getRootCause() : var9));
               }
            }

         } finally {
            WebAppServletContext.popEnvironment(var1, var2);
            WebAppServletContext.this.asyncInitsStillRunning = false;
            this.asyncInitServlets.clear();
         }
      }
   }
}
