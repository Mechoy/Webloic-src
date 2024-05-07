package weblogic.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.application.AppClassLoaderManager;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.VersionInfo;
import weblogic.corba.utils.RemoteInfo;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.iiop.Utils;
import weblogic.j2ee.ApplicationManager;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.kernel.AuditableThread;
import weblogic.rmi.extensions.server.DescriptorHelper;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.rmi.internal.StubGenerator;
import weblogic.servlet.internal.ServletOutputStreamImpl;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.application.WarDetector;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.AugmentableClassLoaderManager;
import weblogic.utils.classloaders.ByteArraySource;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;
import weblogic.utils.http.HttpParsing;
import weblogic.utils.io.FilenameEncoder;

public final class ClasspathServlet extends HttpServlet {
   private static final String SERVE_MANIFEST = "weblogic.classpathservlet.servemanifest";
   public static final String URI = "/classes";
   private static final DebugCategory debugging;
   private static final GenericClassLoader AUG_GCL;
   private static final String WLS_STUB_VERSION;
   private static final AppClassLoaderManager appClassLoaderManager;
   private static final String SYSTEM_INFO_RESOURCE_NAME = "/weblogic/SystemInfo";
   private static final Source systemInfoSource;
   private static final boolean serveManifest;
   private final ClassFinder finder = new ClasspathClassFinder2();
   private ServletContext context;
   private String defaultFilename;
   private boolean inited = false;
   private final ArrayList restrictedFiles = new ArrayList();
   private boolean strictCheck;
   private String restrictedDirectory;
   static final long serialVersionUID = -5240636019486779403L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.servlet.ClasspathServlet");
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   private static final boolean initServeManifest() {
      return Boolean.getBoolean("weblogic.classpathservlet.servemanifest");
   }

   public synchronized void init(ServletConfig var1) throws ServletException {
      if (!this.inited) {
         super.init(var1);
         this.context = var1.getServletContext();
         this.defaultFilename = var1.getInitParameter("defaultFilename");
         this.fillRestrictedFileList();
         this.inited = true;
      }
   }

   private void fillRestrictedFileList() {
      this.restrictedFiles.add(".bat");
      this.restrictedFiles.add(".cmd");
      this.restrictedFiles.add(".crt");
      this.restrictedFiles.add(".der");
      this.restrictedFiles.add(".ini");
      this.restrictedFiles.add(".jks");
      this.restrictedFiles.add(".log");
      this.restrictedFiles.add(".p12");
      this.restrictedFiles.add(".pem");
      this.restrictedFiles.add(".sh");
      this.restrictedFiles.add("boot.properties");
      this.restrictedFiles.add("cacerts");
      this.restrictedFiles.add("config.xml");
      this.restrictedFiles.add("filerealm.properties");
      this.restrictedFiles.add("license.bea");
      this.restrictedFiles.add("SerializedSystemIni.dat");
      this.restrictedFiles.add("weblogiclicense");
      this.restrictedFiles.add("weblogic.properties");

      try {
         if (Boolean.getBoolean("weblogic.servlet.ClasspathServlet.disableStrictCheck")) {
            this.strictCheck = false;
         } else {
            this.strictCheck = true;
         }
      } catch (SecurityException var2) {
         this.strictCheck = true;
      }

      this.restrictedDirectory = "config/mydomain";
   }

   private static String parseResourceString(String var0) {
      String var1 = FilenameEncoder.resolveRelativeURIPath(HttpParsing.unescape(var0).toLowerCase(Locale.ENGLISH).trim());
      if (var1.endsWith("/")) {
         var1 = var1.substring(0, var1.length() - 1);
      }

      return var1;
   }

   private boolean isRestrictedResource(String var1) {
      String var2 = parseResourceString(var1);
      if (var2.length() == 0) {
         return false;
      } else if (!var2.endsWith(".class") && !WarDetector.instance.suffixed(var2) && !var2.endsWith(".jar") && !var2.endsWith(".ear") && !var2.endsWith(".rar") && !var2.endsWith(".dtd") && !var2.endsWith("rtd.xml") && !var2.endsWith(".mf")) {
         if (this.strictCheck) {
            return true;
         } else {
            Iterator var3 = this.restrictedFiles.iterator();

            String var4;
            do {
               if (!var3.hasNext()) {
                  if (var2.indexOf(this.restrictedDirectory) != -1) {
                     if (debugging.isEnabled()) {
                        logDebug(this.restrictedDirectory + " is restricted. It cannot be served by ClasspathServlet.");
                     }

                     return true;
                  }

                  return false;
               }

               var4 = (String)var3.next();
            } while(!var2.endsWith(var4.toLowerCase(Locale.ENGLISH)));

            if (debugging.isEnabled()) {
               logDebug(var4 + " is restricted. It cannot be served by ClasspathServlet.");
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws IOException, ServletException {
      this.doGet(var1, var2);
   }

   public void doGet(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      boolean var12;
      boolean var10000 = var12 = _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var13 = null;
      DiagnosticActionState[] var14 = null;
      Object var11 = null;
      if (var10000) {
         Object[] var7 = null;
         if (_WLDF$INST_FLD_Servlet_Request_Action_Around_Medium.isArgumentsCaptureNeeded()) {
            var7 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var17 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var7, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium;
         DiagnosticAction[] var10002 = var13 = var10001.getActions();
         InstrumentationSupport.preProcess(var17, var10001, var10002, var14 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         if (debugging.isEnabled()) {
            logDebug("incoming request: " + var1);
            logDebug("path info:        " + var1.getPathInfo());
            logDebug("path trans:       " + var1.getPathTranslated());
            logDebug("parsed request:   " + parseResourceString(var1.getPathInfo()));
            logDebug("ctx path :        " + var1.getContextPath());
            logDebug("req uri :         " + var1.getRequestURI());
            logDebug("servlet path :    " + var1.getServletPath());
         }

         Source var3 = this.findResource(var1);
         if (var3 == null) {
            if (debugging.isEnabled()) {
               logDebug("Couldn't find resource for URI: " + var1.getRequestURI() + " sending 404");
            }

            var2.sendError(404);
            return;
         }

         long var4 = var3.length();
         if (var4 == 0L) {
            var2.setContentLength(0);
            return;
         }

         if (!isModified(var1, var2, var3)) {
            var2.setStatus(304);
            return;
         }

         var2.setContentType(this.context.getMimeType(var3.getURL().toString()));
         var2.setContentLength((int)var4);
         sendResource(var3, var2);
      } finally {
         if (var12) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium, var13, var14);
         }

      }

   }

   private static boolean isModified(HttpServletRequest var0, HttpServletResponse var1, Source var2) throws IOException {
      if (var0.getAttribute("javax.servlet.include.request_uri") != null) {
         return true;
      } else {
         long var3 = var2.lastModified();
         var3 -= var3 % 1000L;
         if (var3 == 0L) {
            if (debugging.isEnabled()) {
               logDebug("Couldn't find resource for URI: " + var0.getRequestURI() + " - sending 404");
            }

            var1.sendError(404);
            return false;
         } else {
            long var5 = -1L;

            try {
               var5 = var0.getDateHeader("If-Modified-Since");
            } catch (IllegalArgumentException var8) {
            }

            if (var5 >= var3) {
               if (debugging.isEnabled()) {
                  logDebug("Resource: " + var2.getURL() + " last modified: " + var3 + " cache last modified: " + var5 + " sending 302");
               }

               var1.setStatus(304);
               return false;
            } else {
               var1.setDateHeader("Last-Modified", var3);
               return true;
            }
         }
      }
   }

   private Source findResource(HttpServletRequest var1) throws IOException {
      String var2 = var1.getPathInfo();
      if (var2 == null) {
         return null;
      } else if (var2.toLowerCase(Locale.ENGLISH).indexOf("weblogiclicense") != -1) {
         return null;
      } else if ("/weblogic/SystemInfo".equals(var2)) {
         return systemInfoSource;
      } else if (this.isRestrictedResource(var2)) {
         return null;
      } else if (var2.endsWith("MANIFEST.MF") && serveManifest) {
         ByteArraySource var9 = new ByteArraySource(VersionInfo.theOne().getVersionInfoForNetworkClassLoader(), new URL(var1.getRequestURL().toString()));
         return var9;
      } else {
         byte var3 = 0;
         if (var2.charAt(0) == '/') {
            var3 = 1;
         }

         int var4 = var2.indexOf("/", var3);
         int var5 = var2.indexOf(64);
         String var6;
         if (var5 != -1 && var5 < var4) {
            var6 = var2.substring(var3, var5);
            String var10 = var2.substring(var5 + 1, var4);
            String var11 = var2.substring(var4 + 1);
            if (debugging.isEnabled()) {
               logDebug("appName: " + var6 + ", componentName: " + var10);
            }

            return this.findSourceFromApplicationContainer(var6, var10, var11);
         } else {
            var6 = var2.substring(var3);
            Source var7 = AUG_GCL.getClassFinder().getSource(var6);
            if (var7 != null) {
               return var7;
            } else {
               if (isGeneratableClass(var6)) {
                  Source var8;
                  if (JDBCWrapperFactory.isJDBCRMIWrapperClass(var6)) {
                     var8 = getJDBCRMIWrapperClassSource(var6);
                     if (var8 != null) {
                        return var8;
                     }
                  } else {
                     var8 = this.getGeneratedClassSource(var6, (String)null);
                     if (var8 != null) {
                        return var8;
                     }
                  }
               }

               return this.sourceOrDefault(this.finder.getSource(var6));
            }
         }
      }
   }

   private static boolean isGeneratableClass(String var0) {
      return var0.endsWith("_WLStub.class") || var0.endsWith("_WLSkel.class") || Utils.isStubName(var0) || isWrapperInterface(var0);
   }

   private static boolean isWrapperInterface(String var0) {
      return var0.endsWith("_RemoteInterface.class");
   }

   private Source findSourceFromApplicationContainer(String var1, String var2, String var3) throws IOException {
      GenericClassLoader var4 = appClassLoaderManager.findLoader(new Annotation(var1));
      Source var5;
      if (var4 != null) {
         var5 = var4.getClassFinder().getSource(var3);
         if (var5 != null) {
            return var5;
         }
      }

      var4 = appClassLoaderManager.findLoader(new Annotation(var1, var2));
      if (var4 != null) {
         var5 = var4.getClassFinder().getSource(var3);
         if (var5 != null) {
            return var5;
         }
      }

      var5 = this.sourceOrDefault(this.finder.getSource(var3));
      if (var5 != null) {
         return var5;
      } else if (isGeneratableClass(var3)) {
         if (debugging.isEnabled()) {
            logDebug("\n \n This is a Generatable class. name :" + var3 + "\n \n");
         }

         if (isWrapperInterface(var3)) {
            return AUG_GCL.getClassFinder().getSource(var3);
         } else {
            return JDBCWrapperFactory.isJDBCRMIWrapperClass(var3) ? getJDBCRMIWrapperClassSource(var3) : this.getGeneratedClassSource(var3, var1);
         }
      } else {
         return null;
      }
   }

   private Source getGeneratedClassSource(String var1, String var2) throws IOException {
      Source var3 = null;
      String var4 = var1.replace('/', '.');
      int var5 = var4.indexOf("_WLSkel.class");
      if (var5 != -1) {
         var4 = var4.substring(0, var5);
         Class var15 = loadClass(var4, var2);
         if (var15 != null && !var15.isInterface()) {
            var3 = getSkelClassSource(var15, var1);
            return var3;
         } else {
            return null;
         }
      } else {
         int var6 = var4.indexOf(WLS_STUB_VERSION);
         if (var6 != -1) {
            var4 = var4.substring(0, var6);
            Class var16 = loadClass(var4, var2);
            if (var16 != null && !var16.isInterface()) {
               var3 = getStubClassSource(var16, var1);
               return var3;
            } else {
               return null;
            }
         } else if (Utils.isStubName(var1)) {
            ClassLoader var7 = Thread.currentThread().getContextClassLoader();

            try {
               Thread.currentThread().setContextClassLoader(this.getDefaultContextClassLoader());
               Class var8 = ApplicationManager.loadClass(Utils.getClassNameFromStubName(var4), var2);
               var3 = getIIOPStubClassSource(var8, var1);
            } catch (ClassNotFoundException var13) {
               if (debugging.isEnabled()) {
                  logDebug("Unexpected error in ClasspathServlet: " + var13);
               }
            } finally {
               Thread.currentThread().setContextClassLoader(var7);
            }

            return var3;
         } else {
            return var3;
         }
      }
   }

   private ClassLoader getDefaultContextClassLoader() {
      return Thread.currentThread() instanceof AuditableThread ? ((AuditableThread)Thread.currentThread()).getDefaultContextClassLoader() : null;
   }

   private static Source getSkelClassSource(Class var0, String var1) throws IOException {
      ServerHelper.getRuntimeDescriptor(var0);
      return AUG_GCL.getClassFinder().getSource(var1);
   }

   private static Source getJDBCRMIWrapperClassSource(String var0) {
      if (debugging.isEnabled()) {
         logDebug("wrapperName: " + var0);
      }

      JDBCWrapperFactory.generateWrapperClass(var0, false);
      Source var1 = AUG_GCL.getClassFinder().getSource(var0);
      if (debugging.isEnabled()) {
         logDebug("src: " + var1);
      }

      return var1;
   }

   private static Source getStubClassSource(Class var0, String var1) throws IOException {
      RuntimeDescriptor var2 = DescriptorHelper.getDescriptor(var0);
      if (debugging.isEnabled()) {
         logDebug("cls: " + var0);
         logDebug("stubName: " + var1);
         logDebug("rtd: " + var2);
      }

      (new StubGenerator(var2, (String)null)).generateClass(AUG_GCL);
      Source var3 = AUG_GCL.getClassFinder().getSource(var1);
      if (debugging.isEnabled()) {
         logDebug("src: " + var3);
      }

      return var3;
   }

   private static Source getIIOPStubClassSource(Class var0, String var1) {
      if (debugging.isEnabled()) {
         logDebug("cls :" + var0);
         logDebug("stubName :" + var1);
      }

      Object var2 = var0.getClassLoader();
      if (!(var2 instanceof GenericClassLoader)) {
         var2 = AUG_GCL;
      }

      RemoteInfo.createStubs(var0, (ClassLoader)var2);
      Source var3 = ((GenericClassLoader)var2).getClassFinder().getSource(var1);
      if (debugging.isEnabled()) {
         logDebug("src: " + var3);
      }

      return var3;
   }

   private Source sourceOrDefault(Source var1) {
      return var1 == null && this.defaultFilename != null ? this.finder.getSource(this.defaultFilename) : var1;
   }

   private static void sendResource(Source var0, HttpServletResponse var1) throws IOException {
      ServletOutputStream var2 = var1.getOutputStream();
      InputStream var3 = var0.getInputStream();

      try {
         ((ServletOutputStreamImpl)var2).writeStream(var3);
      } finally {
         if (var3 != null) {
            var3.close();
         }

      }

   }

   private static Source createSystemInfoSource() {
      String var0 = "ssl_strength=domestic\n";

      try {
         URL var1 = new URL("file", "", ".txt");
         return new ByteArraySource(var0.getBytes("ASCII"), var1);
      } catch (MalformedURLException var2) {
         throw new AssertionError(var2);
      } catch (UnsupportedEncodingException var3) {
         throw new AssertionError(var3);
      }
   }

   private static void logDebug(String var0) {
      Debug.say(var0);
   }

   private static Class loadClass(String var0, String var1) {
      try {
         return ApplicationManager.loadClass(var0, var1);
      } catch (ClassNotFoundException var4) {
         try {
            return AUG_GCL.loadClass(var0);
         } catch (ClassNotFoundException var3) {
            if (debugging.isEnabled()) {
               logDebug("Unexpected error in ClasspathServlet: " + var3);
            }

            return null;
         }
      }
   }

   static {
      _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Request_Action_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ClasspathServlet.java", "weblogic.servlet.ClasspathServlet", "doGet", "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V", 216, InstrumentationSupport.makeMap(new String[]{"Servlet_Request_Action_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("req", "weblogic.diagnostics.instrumentation.gathering.ServletRequestRenderer", false, true), null})}), (boolean)0);
      debugging = Debug.getCategory("weblogic.ClasspathServlet");
      AUG_GCL = AugmentableClassLoaderManager.getAugmentableSystemClassLoader();
      WLS_STUB_VERSION = "_" + PeerInfo.getPeerInfo().getMajor() + PeerInfo.getPeerInfo().getMinor() + PeerInfo.getPeerInfo().getServicePack() + "_WLStub";
      appClassLoaderManager = AppClassLoaderManager.getAppClassLoaderManager();
      systemInfoSource = createSystemInfoSource();
      serveManifest = initServeManifest();
   }
}
