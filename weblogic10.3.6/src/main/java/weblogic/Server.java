package weblogic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.DomainDir;
import weblogic.security.utils.SecurityUtils;
import weblogic.t3.srvr.ShutdownOnExitThread;
import weblogic.t3.srvr.T3Srvr;
import weblogic.utils.ArrayUtils;
import weblogic.utils.Classpath;
import weblogic.utils.FileUtils;
import weblogic.utils.StringUtils;
import weblogic.utils.io.ExtensionFilter;

public final class Server {
   private static final String WEBLOGIC_EXTENSION_DIRS = "weblogic.ext.dirs";
   private static final String PATH_SEPARATOR_STRING;
   public static final String WEBLOGIC_INSTRUMENTATION_PROPERTY = "weblogic.diagnostics.instrumentation";
   public static final String DIAGNOSTIC_PRE_PROCESSOR_CLASS = "weblogic.diagnostics.instrumentation.DiagnosticClassPreProcessor";
   public static final String WEBLOGIC_INSTRUMENTATION_SERVER_SCOPE = "_WL_INTERNAL_SERVER_SCOPE";
   public static final String CLASSLOADER_PREPROCESSOR = "weblogic.classloader.preprocessor";

   public static void main(String[] var0) {
      if (var0.length <= 0 || !var0[0].equals("-help") && !var0[0].equals("-?")) {
         int var1 = 0;

         try {
            SecurityUtils.turnOffCryptoJDefaultJCEVerification();
            SecurityUtils.changeCryptoJDefaultPRNG();
            intializeClassloader();
            var1 = T3Srvr.run(var0);
            ShutdownOnExitThread.exitViaServerLifeCycle = true;
         } catch (Exception var4) {
            if (var4 instanceof AccessControlException) {
               System.err.println("***************************************************************************");
               System.err.println("The WebLogic Server encountered a critical failure");
               System.err.println("Exception raised: '" + var4 + "'");
               System.err.println("Check you have both java.security.manager and java.security.policy defined and java.security.policy has the correct entries");
               System.err.println("***************************************************************************");
            }
         }

         try {
            System.exit(var1);
         } catch (Throwable var3) {
         }

      } else {
         System.out.println(getUsage());
      }
   }

   private static URL[] getURLs(File[] var0) {
      if (var0 != null && var0.length != 0) {
         URL[] var1 = new URL[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            try {
               var1[var2] = var0[var2].toURL();
            } catch (MalformedURLException var4) {
               throw new AssertionError(var4);
            }
         }

         return var1;
      } else {
         return null;
      }
   }

   private static File[] getJars(File var0) {
      if (var0.exists()) {
         if (var0.isFile()) {
            return new File[]{var0};
         }

         if (var0.isDirectory()) {
            return FileUtils.find(var0, new ExtensionFilter("jar"));
         }
      }

      return null;
   }

   private static void appendToClassPath(File[] var0) {
      if (var0 != null) {
         StringBuffer var1 = new StringBuffer();

         for(int var2 = 0; var2 < var0.length; ++var2) {
            Classpath.append(var0[var2]);

            try {
               if (var2 != 0) {
                  var1.append(PATH_SEPARATOR_STRING);
               }

               var1.append(var0[var2].getCanonicalPath());
            } catch (IOException var4) {
            }
         }

         T3SrvrLogger.logDomainLibPath(var1.toString());
      }
   }

   private static void intializeClassloader() {
      Object var0 = ClassLoader.getSystemClassLoader();
      File[] var1 = getExtensionJars();
      if (var1 != null && var1.length > 0) {
         var0 = new URLClassLoader(getURLs(var1), (ClassLoader)var0);
         appendToClassPath(var1);
      }

      Thread.currentThread().setContextClassLoader((ClassLoader)var0);
      if (!isRedefineClassesSupported()) {
         String var2 = System.getProperty("weblogic.classloader.preprocessor");
         if (var2 != null) {
            var2 = "weblogic.diagnostics.instrumentation.DiagnosticClassPreProcessor," + var2;
         } else {
            var2 = "weblogic.diagnostics.instrumentation.DiagnosticClassPreProcessor";
         }

         System.setProperty("weblogic.classloader.preprocessor", var2);
      }

   }

   public static boolean isRedefineClassesSupported() {
      boolean var0 = false;

      try {
         Class var1 = Class.forName("weblogic.diagnostics.instrumentation.agent.WLDFInstrumentationAgent");
         Class[] var2 = new Class[0];
         Method var3 = var1.getMethod("isRedefineClassesSupported", var2);
         Object[] var4 = new Object[0];
         Boolean var5 = (Boolean)var3.invoke((Object)null, var4);
         var0 = var5;
      } catch (Exception var6) {
      }

      return var0;
   }

   private static File[] getExtensionJars() {
      File[] var0 = getLibraryExtensions();
      if (var0 != null && var0.length != 0) {
         ArrayList var1 = new ArrayList();

         for(int var2 = 0; var2 < var0.length; ++var2) {
            File[] var3 = getJars(var0[var2]);
            if (var3 != null && var3.length != 0) {
               Arrays.sort(var3);
               ArrayUtils.addAll(var1, var3);
            }
         }

         File[] var4 = new File[var1.size()];
         var1.toArray(var4);
         return var4;
      } else {
         return null;
      }
   }

   private static final File[] getLibraryExtensions() {
      ArrayList var0 = new ArrayList();
      String var1 = System.getProperty("weblogic.ext.dirs");
      boolean var2 = false;
      if (var1 != null) {
         if (var1.startsWith("=")) {
            var2 = true;
            var1 = var1.substring(1);
         }

         String[] var3 = StringUtils.splitCompletely(var1, PATH_SEPARATOR_STRING);

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var0.add(new File(var3[var4]));
         }
      }

      if (!var2) {
         var0.add(new File(new File(DomainDir.getRootDir()), "lib"));
         File var5 = new File(Home.getPath());
         if (var5.getParentFile() != null) {
            var0.add(new File(var5.getParentFile(), "common" + File.separator + "lib" + File.separator + "ext"));
         }

         var0.add(new File(var5, "lib" + File.separator + "ext"));
      }

      return (File[])((File[])var0.toArray(new File[var0.size()]));
   }

   public String toString() {
      return "WebLogic Server";
   }

   public static String getUsage() {
      return "Usage: java [options] weblogic.Server [args...]\n\nWhere WebLogic options include:\n\t-Djava.security.policy=<value>\t the location of the security policy\n\t\t\t\t\t file\n\t-Dweblogic.Domain=<value>\t WebLogic domain name\n\t-Dweblogic.Name=<value>\t\t WebLogic server name\n\t-Dweblogic.ext.dirs=<value>\n\t\t\t\t\t '" + PATH_SEPARATOR_STRING + "' separated list of directories to pick up jars from " + "\n\t\t\t\t\t and add to the end of the server classpath." + "\n\t\t\t\t\t The list can also contain individual jars." + "\n\t-Dweblogic.management.server=<value>" + "\n\t\t\t\t\t WebLogic Admin Server URL for starting" + "\n\t\t\t\t\t a Managed Server, the value can be:" + "\n\t\t\t\t\t host:port or " + "\n\t\t\t\t\t http://host:port or" + "\n\t\t\t\t\t https://host:port" + "\n\t-Dweblogic.home=<value>" + "\n\t\t\t\t\t The location of the WebLogic Server" + "\n\t\t\t\t\t product install.  By default, this will" + "\n\t\t\t\t\t be derived from the classpath." + "\n\t-Dweblogic.RootDirectory=<value>" + "\n\t\t\t\t\t The root directory of your domain," + "\n\t\t\t\t\t where your configuration is housed." + "\n\t\t\t\t\t default is the current working" + "\n\t\t\t\t\t directory" + "\n\t-Dweblogic.management.username=<value>" + "\n\t\t\t\t\t user name" + "\n\t-Dweblogic.management.password=<value>" + "\n\t\t\t\t\t user password" + "\n\t-Dweblogic.management.pkpassword=<value>" + "\n\t\t\t\t\t private key password" + "\n\t-Dweblogic.security.unixrealm.authProgram=<value>" + "\n\t\t\t\t\t the name of the program used to" + "\n\t\t\t\t\t authenticate users in the unix" + "\n\t\t\t\t\t security realm" + "\n\t-Dweblogic.<ServerAttributeName>=<value>" + "\n\t\t\t\t\t specify a server attribute, it will" + "\n\t\t\t\t\t override the attribute value set in" + "\n\t\t\t\t\t config.xml for this server" + "\n\t-Dweblogic.admin.host=<value>" + "\t same as weblogic.management.server, an" + "\n\t\t\t\t\t old property " + "\n\t-javaagent:$WL_HOME/server/lib/diagnostics-agent.jar" + "\n\t\t\t\t\t enable diagnostics hot code-swap for application classes" + "\nAnd WebLogic args include:" + "\n\t-? -help" + "\t\t\t print this help message" + "\n";
   }

   static {
      PATH_SEPARATOR_STRING = File.pathSeparator;
   }
}
