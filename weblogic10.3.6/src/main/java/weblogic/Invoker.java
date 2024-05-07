package weblogic;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.LibDirClassFinder;
import weblogic.utils.classloaders.MultiClassFinder;

public final class Invoker {
   public static final String WEBLOGIC_INSTRUMENTATION_PROPERTY = "weblogic.diagnostics.instrumentation";
   public static final String DIAGNOSTIC_PRE_PROCESSOR_CLASS = "weblogic.diagnostics.instrumentation.DiagnosticClassPreProcessor";
   public static final String WEBLOGIC_INSTRUMENTATION_SERVER_SCOPE = "_WL_INTERNAL_SERVER_SCOPE";
   public static final String ASPECT_PRE_PROCESSOR_CLASS = "weblogic.aspects.AspectClassPreProcessor";
   public static final String CLASSLOADER_PREPROCESSOR = "weblogic.classloader.preprocessor";
   private static final boolean SUPPORTS_DOMAIN_LIB;
   private static final ClassLoader INVOKER_CLASSLOADER;
   private static final List helpArgs;
   private static final Map tools;

   public static final ClassLoader getInvokerClassLoader() {
      return INVOKER_CLASSLOADER;
   }

   public static void main(String[] var0) throws Throwable {
      if (var0.length != 0 && !helpArgs.contains(var0[0])) {
         String var1 = var0[0];
         String var2 = (String)tools.get(var1);
         if (var2 == null) {
            var2 = var1;
         }

         String[] var3 = new String[var0.length - 1];
         System.arraycopy(var0, 1, var3, 0, var3.length);
         ClassLoader var4 = INVOKER_CLASSLOADER;
         Class var5 = var4.loadClass(var2);
         Method var6 = var5.getMethod("main", String[].class);

         try {
            var6.invoke(var5, var3);
         } catch (InvocationTargetException var8) {
            throw var8.getTargetException();
         }
      } else {
         printUsage();
      }
   }

   private static ClassLoader intializeBootstrapClassloader() {
      String var0 = System.getProperty("weblogic.aspects");
      String var1 = System.getProperty("weblogic.diagnostics.instrumentation");
      String var2;
      if ("all".equals(var0) || "apps".equals(var0)) {
         var2 = System.getProperty("weblogic.classloader.preprocessor");
         if (var2 != null) {
            var2 = var2 + ",weblogic.aspects.AspectClassPreProcessor";
         } else {
            var2 = "weblogic.aspects.AspectClassPreProcessor";
         }

         System.setProperty("weblogic.classloader.preprocessor", var2);
      }

      if ("all".equals(var1) || "apps".equals(var1)) {
         var2 = System.getProperty("weblogic.classloader.preprocessor");
         if (var2 != null) {
            var2 = var2 + ",weblogic.diagnostics.instrumentation.DiagnosticClassPreProcessor";
         } else {
            var2 = "weblogic.diagnostics.instrumentation.DiagnosticClassPreProcessor";
         }

         System.setProperty("weblogic.classloader.preprocessor", var2);
      }

      Object var4 = Invoker.class.getClassLoader();
      if ("all".equals(var0) || "all".equals(var1) || SUPPORTS_DOMAIN_LIB) {
         GenericClassLoader var3 = GenericClassLoader.getRootClassLoader(getClassFinder());
         if ("all".equals(var1)) {
            var3.setAnnotation(new Annotation("_WL_INTERNAL_SERVER_SCOPE"));
         }

         var4 = var3;
      }

      Thread.currentThread().setContextClassLoader((ClassLoader)var4);
      return (ClassLoader)var4;
   }

   private static ClassFinder getClassFinder() {
      Object var0 = null;
      ClasspathClassFinder2 var1 = new ClasspathClassFinder2(System.getProperty("java.class.path"));
      if (SUPPORTS_DOMAIN_LIB) {
         MultiClassFinder var2 = new MultiClassFinder();
         var2.addFinder(var1);
         File var3 = new File(getRootDirectory());
         LibDirClassFinder var4 = new LibDirClassFinder(var3, "classes", "lib");
         var2.addFinder(var4);
         var0 = var2;
      } else {
         var0 = var1;
      }

      return (ClassFinder)var0;
   }

   private static void printUsage() {
      System.out.println("Usage java [options] weblogic.Invoker <cmd> [args...]");
      System.out.println("Where <cmd> is one of: ");
      Iterator var0 = (new TreeSet(helpArgs)).iterator();

      String var1;
      while(var0.hasNext()) {
         var1 = (String)var0.next();
         System.out.println("  " + var1 + ": shows this help message");
      }

      System.out.println();
      var0 = (new TreeSet(tools.keySet())).iterator();

      while(var0.hasNext()) {
         var1 = (String)var0.next();
         System.out.println("  " + var1 + ": calls: " + tools.get(var1));
      }

      System.out.println();
      System.out.println("  $classname: calls: $classname");
      System.out.println("Examples:");
      System.out.println("  java weblogic.Invoker -jspc d:/myjspfiles");
      System.out.println("  java weblogic.Invoker weblogic.jspc d:/myjspfiles");
   }

   private static String getRootDirectory() {
      String var0 = System.getProperty("weblogic.RootDirectory");
      if (var0 == null || var0.trim().length() == 0) {
         var0 = ".";
      }

      return var0;
   }

   public String toString() {
      return "WebLogic Invoker";
   }

   static {
      String[] var0 = new String[]{"-help", "-usage", "-?"};
      helpArgs = Arrays.asList(var0);
      tools = new HashMap();
      tools.put("-appc", "weblogic.appc");
      tools.put("-jspc", "weblogic.jspc");
      tools.put("-wlst", "weblogic.WLST");
      tools.put("-deployer", "weblogic.Deployer");
      tools.put("-admin", "weblogic.Admin");
      tools.put("-buildxmlgen", "weblogic.BuildXMLGen");
      tools.put("-cdeployer", "weblogic.ClientDeployer");
      tools.put("-ejbcc", "weblogic.EJBComplianceChecker");
      tools.put("-getmsg", "weblogic.GetMessage");
      tools.put("-upgrade", "weblogic.Upgrade");
      tools.put("-dtdc", "weblogic.dtdc");
      tools.put("-ejbc", "weblogic.ejbc");
      tools.put("-i18ngen", "weblogic.i18ngen");
      tools.put("-j2idl", "weblogic.j2idl");
      File var3 = new File(getRootDirectory());
      File var1 = new File(var3, "lib");
      File var2 = new File(var3, "classes");
      SUPPORTS_DOMAIN_LIB = var1.exists() && var1.isDirectory() || var2.exists() && var2.isDirectory();
      INVOKER_CLASSLOADER = intializeBootstrapClassloader();
   }
}
