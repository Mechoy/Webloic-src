package weblogic.servlet.jsp;

import java.security.AccessController;
import java.util.Map;
import java.util.StringTokenizer;
import javax.el.ExpressionFactory;
import javax.el.VariableMapper;
import javax.servlet.ServletConfig;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import weblogic.kernel.Kernel;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.HTTPLogger;

public final class JspConfig {
   public static final ICommonUtils COMMON_UTILS = makeCommonUtils();
   public static final String DEFAULT_JSP_SERVLET = getDefaultJSPServlet();
   public static final String DEFAULT_COMPILER_CLASS = "com.sun.tools.javac.Main";
   public static final String DEFAULT_PACKAGE_PREFIX = "jsp_servlet";
   public static final String DEFAULT_SUPER_CLASS = "weblogic.servlet.jsp.JspBase";
   public static final String DEFAULT_DEFAULT_FILE_NAME = "index.jsp";
   public static final int DEFAULT_PAGE_CHECK_SECONDS = 1;
   public static final boolean DEFAULT_VERBOSE = true;
   public static final boolean DEFAULT_KEEP_GENERATED = false;
   public static final boolean DEFAULT_COMPILER_SUPPORTS_ENCODING = true;
   public static final boolean DEFAULT_NO_TRY_BLOCKS = false;
   public static final boolean DEFAULT_PRECOMPILE = false;
   public static final boolean DEFAULT_PRECOMPILE_CONTINUE = false;
   public static final boolean DEFAULT_EXACT_MAPPING = true;
   public static final boolean DEFAULT_DEBUG_ENABLED = false;
   public static final boolean DEFAULT_BACKWARD_COMPATIBLE = false;
   public static final boolean DEFAULT_PRINT_NULLS = true;
   public static final boolean DEFAULT_COMPRESS_HTML_TEMPLATE = false;
   public static final boolean DEFAULT_OPTIMIZE_JAVA_EXPRESSION = false;
   public static final boolean DEFAULT_STRICT_STALE_CHECK = true;
   public static final String BACKWARD_COMPATIBLE = "backwardCompatible";
   public static final String RTEXPRVALUE_JSP_PARAM_NAME = "rtexprvalueJspParamName";
   public static final String COMPILER = "compiler";
   public static final String COMPILE_COMMAND = "compileCommand";
   public static final String COMPILER_CLASS = "compilerClass";
   public static final String COMPILE_FLAGS = "compileFlags";
   public static final String COMPILER_SUPPORTS_ENCODING = "compilerSupportsEncoding";
   public static final String DEBUG = "debug";
   public static final String DEFAULT_FILE_NAME = "defaultFileName";
   public static final String ENCODING = "encoding";
   public static final String EXACT_MAPPING = "exactMapping";
   public static final String JSP_PRECOMPILER = "jspPrecompiler";
   public static final String JSP_SERVLET = "jspServlet";
   public static final String KEEP_GENERATED = "keepgenerated";
   public static final String PACKAGE_PREFIX = "packagePrefix";
   public static final String PAGE_CHECK_SECONDS = "pageCheckSeconds";
   public static final String PRECOMPILE = "precompile";
   public static final String PRECOMPILE_CONTINUE = "precompileContinue";
   public static final String PRINT_NULLS = "printNulls";
   public static final String NO_TRY_BLOCKS = "noTryBlocks";
   public static final String SUPER_CLASS = "superclass";
   public static final String VERBOSE = "verbose";
   public static final String WORKING_DIR = "workingDir";
   public static final String COMPRESS_HTML_TEMPLATE = "compressHtmlTemplate";
   public static final String OPTIMIZE_JAVA_EXPRESSION = "optimizeJavaExpression";
   public static final String RESOURCE_PROVIDER_CLASS = "resourceProviderClass";
   public static final String STRICT_STALE_CHECK = "strictStaleCheck";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean PRODUCTION_MODE;
   private static Boolean compilerClassExists;
   private boolean verbose;
   private boolean keepgenerated;
   private boolean compilerSupportsEncoding;
   private boolean noTryBlocks;
   private boolean precompileContinue;
   private boolean compressHtmlTemplate;
   private boolean optimizeJavaExpression;
   private long pageCheckSeconds;
   private String[] compileFlags;
   private String compileFlagsString;
   private boolean debugEnabled;
   private boolean backwardCompatible;
   private boolean isRtexprvalueJspParamName;
   private boolean printNulls;
   private boolean exactMapping;
   private boolean useByteBuffer;
   private boolean isStrictStaleCheck;
   public String packagePrefix;
   public String compileropt;
   public String compilerval;
   public String compilerClass;
   public String compiler;
   public String compileCommand;
   public String workingDir;
   public String superclassName;
   public String encoding;
   public String defaultFilename;
   public String jspServlet;

   public JspConfig(Map var1) {
      String var2 = null;
      var2 = this.get(var1, "verbose");
      if (var2 == null) {
         this.verbose = true;
      } else {
         this.verbose = "true".equalsIgnoreCase(var2);
      }

      this.compileFlagsString = this.get(var1, "compileFlags");
      this.compileFlags = parseFlags(this.compileFlagsString);
      var2 = this.get(var1, "keepgenerated");
      this.keepgenerated = "true".equalsIgnoreCase(var2);
      var2 = this.get(var1, "useByteBuffer");
      this.useByteBuffer = "true".equalsIgnoreCase(var2);
      var2 = this.get(var1, "strictStaleCheck");
      this.isStrictStaleCheck = "true".equalsIgnoreCase(var2);
      var2 = this.get(var1, "precompileContinue");
      this.precompileContinue = "true".equalsIgnoreCase(var2);
      var2 = this.get(var1, "compilerSupportsEncoding");
      if (var2 == null) {
         this.compilerSupportsEncoding = true;
      } else {
         this.compilerSupportsEncoding = "true".equalsIgnoreCase(var2);
      }

      var2 = this.get(var1, "noTryBlocks");
      this.noTryBlocks = "true".equalsIgnoreCase(var2);
      this.pageCheckSeconds = PRODUCTION_MODE ? -1L : 1L;
      var2 = this.get(var1, "pageCheckSeconds");
      if (var2 != null) {
         try {
            this.pageCheckSeconds = Long.parseLong(var2);
         } catch (NumberFormatException var4) {
         }
      }

      if ((this.workingDir = this.get(var1, "workingDir")) == null) {
         throw new IllegalArgumentException("JSP 'workingDir' must be specified");
      } else {
         var2 = this.get(var1, "debug");
         this.debugEnabled = "true".equalsIgnoreCase(var2);
         var2 = this.get(var1, "compressHtmlTemplate");
         this.compressHtmlTemplate = "true".equalsIgnoreCase(var2);
         var2 = this.get(var1, "optimizeJavaExpression");
         this.optimizeJavaExpression = "true".equalsIgnoreCase(var2);
         var2 = this.get(var1, "compiler");
         if (var2 != null) {
            this.compiler = var2;
         }

         String var3 = this.get(var1, "compilerclass");
         if (var3 == null) {
            this.compileropt = "-compiler";
            this.compilerval = this.compileCommand = this.get(var1, "compileCommand");
         } else {
            this.compileropt = "-compilerclass";
            this.compilerval = this.compilerClass = var3;
         }

         this.superclassName = this.get(var1, "superclass");
         this.encoding = this.get(var1, "encoding");
         if ((this.defaultFilename = this.get(var1, "defaultFilename")) == null) {
            this.defaultFilename = "index.jsp";
         }

         if ((this.packagePrefix = this.get(var1, "packagePrefix")) == null) {
            this.packagePrefix = "jsp_servlet";
         }

         var2 = this.get(var1, "backwardCompatible");
         this.backwardCompatible = "true".equalsIgnoreCase(var2);
         var2 = this.get(var1, "rtexprvalueJspParamName");
         this.isRtexprvalueJspParamName = "true".equalsIgnoreCase(var2);
         this.printNulls = !"false".equalsIgnoreCase(this.get(var1, "printNulls"));
         this.exactMapping = !"false".equalsIgnoreCase(this.get(var1, "exactMapping"));
         this.jspServlet = this.get(var1, "jspServlet");
      }
   }

   public static boolean checkCompilerClass() {
      if (compilerClassExists != null) {
         return compilerClassExists == Boolean.TRUE;
      } else {
         try {
            Class var0 = Class.forName("com.sun.tools.javac.Main");
            if (var0 != null) {
               compilerClassExists = Boolean.TRUE;
            } else {
               compilerClassExists = Boolean.FALSE;
            }
         } catch (Throwable var1) {
            HTTPLogger.logUnableToLoadDefaultCompilerClass("com.sun.tools.javac.Main", var1);
            compilerClassExists = Boolean.FALSE;
         }

         return compilerClassExists == Boolean.TRUE;
      }
   }

   private static String getDefaultJSPServlet() {
      return "weblogic.servlet.JSPServlet";
   }

   private String get(Map var1, String var2) {
      return this.trim((String)var1.get(var2));
   }

   private String get(ServletConfig var1, String var2) {
      return this.trim(var1.getInitParameter(var2));
   }

   private String trim(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() == 0 ? null : var1;
      }
   }

   public static String[] parseFlags(String var0) {
      if (var0 == null) {
         return new String[0];
      } else {
         StringTokenizer var1 = new StringTokenizer(var0, " \n\t\r", false);
         String[] var2 = new String[var1.countTokens()];

         for(int var3 = 0; var1.hasMoreTokens(); var2[var3++] = var1.nextToken()) {
         }

         return var2;
      }
   }

   public void setValuesFromInitArgs(ServletConfig var1) {
      String var2 = this.get(var1, "defaultFilename");
      if (var2 != null) {
         this.defaultFilename = var2;
      }

      var2 = this.get(var1, "verbose");
      if (var2 != null) {
         this.verbose = "true".equalsIgnoreCase(var2);
      }

      var2 = this.get(var1, "noTryBlocks");
      if (var2 != null) {
         this.noTryBlocks = "true".equalsIgnoreCase(var2);
      }

      var2 = this.get(var1, "debug");
      if (var2 != null) {
         this.debugEnabled = "true".equalsIgnoreCase(var2);
      }

      var2 = this.get(var1, "keepgenerated");
      if (var2 != null) {
         this.keepgenerated = "true".equalsIgnoreCase(var2);
      }

      var2 = this.get(var1, "backwardCompatible");
      if (var2 != null) {
         this.backwardCompatible = "true".equalsIgnoreCase(var2);
      }

      var2 = this.get(var1, "rtexprvalueJspParamName");
      if (var2 != null) {
         this.isRtexprvalueJspParamName = "true".equalsIgnoreCase(var2);
      }

      var2 = this.get(var1, "printNulls");
      if (var2 != null) {
         this.printNulls = "true".equalsIgnoreCase(var2);
      }

      var2 = this.get(var1, "exactMapping");
      if (var2 != null) {
         this.exactMapping = "true".equalsIgnoreCase(var2);
      }

      var2 = this.get(var1, "compilerSupportsEncoding");
      if (var2 != null) {
         this.compilerSupportsEncoding = "true".equalsIgnoreCase(var2);
      }

      var2 = this.get(var1, "precompileContinue");
      if (var2 != null) {
         this.precompileContinue = "true".equalsIgnoreCase(var2);
      }

      var2 = this.get(var1, "compileFlags");
      if (var2 != null) {
         this.compileFlagsString = var2;
         this.compileFlags = parseFlags(var2);
      }

      var2 = this.get(var1, "pageCheckSeconds");
      if (var2 != null) {
         try {
            this.pageCheckSeconds = Long.parseLong(var2);
         } catch (NumberFormatException var4) {
         }
      }

      var2 = this.get(var1, "packagePrefix");
      if (var2 != null) {
         this.packagePrefix = var2;
      }

      var2 = this.get(var1, "compilerclass");
      if (var2 != null) {
         this.compileropt = "-compilerclass";
         this.compilerClass = this.compilerval = var2;
      } else {
         var2 = this.get(var1, "compileCommand");
         if (var2 != null) {
            this.compileropt = "-compiler";
            this.compilerval = this.compileCommand = var2;
         }
      }

      var2 = this.get(var1, "workingDir");
      if (var2 != null) {
         this.workingDir = var2;
      }

      var2 = this.get(var1, "superclass");
      if (var2 != null) {
         this.superclassName = var2;
      }

      var2 = this.get(var1, "encoding");
      if (var2 != null) {
         this.encoding = var2;
      }

      var2 = this.get(var1, "jspServlet");
      if (var2 != null) {
         this.jspServlet = var2;
      }

   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public String getPackagePrefix() {
      return this.packagePrefix;
   }

   public String getCompiler() {
      return this.compiler;
   }

   public String getCompileropt() {
      return this.compileropt;
   }

   public String getCompilerClass() {
      return this.compilerClass;
   }

   public String getWorkingDir() {
      return this.workingDir;
   }

   public long getPageCheckSecs() {
      return this.pageCheckSeconds;
   }

   public String getSuperClassName() {
      return this.superclassName;
   }

   public boolean isKeepGenerated() {
      return this.keepgenerated;
   }

   public boolean useByteBuffer() {
      return this.useByteBuffer;
   }

   public boolean isPrecompileContinue() {
      return this.precompileContinue;
   }

   public boolean isCompilerSupportsEncoding() {
      return this.compilerSupportsEncoding;
   }

   public String getEncoding() {
      return this.encoding;
   }

   public String getDefaultFilename() {
      return this.defaultFilename;
   }

   public String[] getCompileFlags() {
      return this.compileFlags;
   }

   public String getCompileFlagsString() {
      return this.compileFlagsString;
   }

   public boolean isNoTryBlocks() {
      return this.noTryBlocks;
   }

   public boolean isDebugEnabled() {
      return this.debugEnabled;
   }

   public boolean isBackwardCompatible() {
      return this.backwardCompatible;
   }

   public boolean isRtexprvalueJspParamName() {
      return this.isRtexprvalueJspParamName;
   }

   public boolean isPrintNulls() {
      return this.printNulls;
   }

   public boolean isExactMapping() {
      return this.exactMapping;
   }

   public String getJspServlet() {
      return this.jspServlet;
   }

   public boolean isCompressHtmlTemplate() {
      return this.compressHtmlTemplate;
   }

   public boolean isOptimizeJavaExpression() {
      return this.optimizeJavaExpression;
   }

   public boolean isStrictStaleCheck() {
      return this.isStrictStaleCheck;
   }

   public String getCompilerval() {
      return this.compilerval == null ? this.getCompileCommand() : this.compilerval;
   }

   public String getCompileCommand() {
      if (this.compileCommand != null && this.compileCommand.length() > 0) {
         return this.compileCommand;
      } else {
         String var1 = null;
         if (Kernel.isServer()) {
            ServerMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServer();
            if (var2 != null) {
               var1 = var2.getJavaCompiler();
            }
         }

         if (var1 == null || var1.length() == 0) {
            var1 = "javac";
         }

         return var1;
      }
   }

   public void setCompileCommand(String var1) {
      this.compileropt = "-compiler";
      this.compilerval = this.compileCommand = var1;
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public void setKeepGenerated(boolean var1) {
      this.keepgenerated = var1;
   }

   public void setUseByteBuffer(boolean var1) {
      this.useByteBuffer = var1;
   }

   public void setDebugEnabled(boolean var1) {
      this.debugEnabled = var1;
   }

   public void setCompressHtmlTemplate(boolean var1) {
      this.compressHtmlTemplate = var1;
   }

   public void setOptimizeJavaExpression(boolean var1) {
      this.optimizeJavaExpression = var1;
   }

   public void setPageCheckSecs(long var1) {
      this.pageCheckSeconds = var1;
   }

   public void setWorkingDir(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("JSP 'workingDir' cannot be null");
      } else {
         this.workingDir = var1;
      }
   }

   public void setCompilerClass(String var1) {
      this.compileropt = "-compilerclass";
      this.compilerval = this.compilerClass = var1;
   }

   public void setPrecompileContinue(boolean var1) {
      this.precompileContinue = var1;
   }

   public void setNoTryBlocks(boolean var1) {
      this.noTryBlocks = var1;
   }

   public void setCompilerSupportsEncoding(boolean var1) {
      this.compilerSupportsEncoding = var1;
   }

   public void setEncoding(String var1) {
      this.encoding = var1;
   }

   public void setCompileFlagsString(String var1) {
      this.compileFlagsString = var1;
      this.compileFlags = parseFlags(var1);
   }

   public void setExactMapping(boolean var1) {
      this.exactMapping = var1;
   }

   public static boolean isJspServletValid(String var0, String var1, ClassLoader var2) throws DeploymentException {
      if (var1 == null) {
         return false;
      } else if (var1.equals(DEFAULT_JSP_SERVLET)) {
         return true;
      } else if (var2 == null) {
         return true;
      } else {
         try {
            Class var3 = var2.loadClass(var1);
            Class var8 = var2.loadClass("javax.servlet.http.HttpServlet");
            boolean var5 = var8.isAssignableFrom(var3);
            if (!var5) {
               Loggable var6 = HTTPLogger.logInvalidJspServletLoggable(var0, var1);
               throw new DeploymentException(var6.getMessage());
            } else {
               return var5;
            }
         } catch (Exception var7) {
            Loggable var4 = HTTPLogger.logUnableToLoadJspServletClassLoggable(var0, var1, var7);
            var4.log();
            throw new DeploymentException(var4.getMessage(), var7);
         }
      }
   }

   public String toString() {
      String var1 = this.getCompileFlagsString();
      if (var1 == null) {
         var1 = "";
      }

      return "[JspConfig: verbose=" + this.isVerbose() + ",packagePrefix=" + this.getPackagePrefix() + "," + this.compileropt + "=" + this.compilerval + ",compileFlags=" + var1 + ",workingDir=" + this.getWorkingDir() + ",pageCheckSeconds=" + this.getPageCheckSecs() + ",superclass=" + this.getSuperClassName() + ",keepgenerated=" + this.isKeepGenerated() + ",precompileContinue=" + this.isPrecompileContinue() + ",compilerSupportsEncoding=" + this.isCompilerSupportsEncoding() + ",encoding=" + this.getEncoding() + ",defaultfilename=" + this.getDefaultFilename() + ",compilerclass=" + this.getCompilerClass() + ",noTryBlocks=" + this.isNoTryBlocks() + ",debugEnabled=" + this.isDebugEnabled() + ",printNulls=" + this.isPrintNulls() + ",jspServlet=" + this.getJspServlet() + "]";
   }

   private static ICommonUtils makeCommonUtils() {
      try {
         Class var0 = Class.forName("weblogic.servlet.jsp.CommonUtils15");
         if (var0 != null) {
            return (ICommonUtils)var0.newInstance();
         }
      } catch (Throwable var2) {
      }

      try {
         return new CommonUtils14();
      } catch (Throwable var1) {
         return null;
      }
   }

   static {
      PRODUCTION_MODE = !Kernel.isServer() || ManagementService.getRuntimeAccess(kernelId).getDomain().isProductionModeEnabled();
   }

   public interface ICommonUtils {
      ExpressionEvaluator getExpressionEvaluator();

      VariableResolver getVariableResolver(PageContextImpl var1);

      String getDefaultJSPServlet();

      String getJspPrecompilerClass();

      ExpressionFactory getExpressionFactory();

      VariableMapper getVariableMapper();
   }
}
