package weblogic.nodemanager.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.common.CoherenceStartupConfig;
import weblogic.nodemanager.common.StartupConfig;
import weblogic.nodemanager.util.Platform;

class CoherenceProcessBuilder extends WLSProcessBuilder {
   private static final String STARTUP_CLASS = "weblogic.nodemanager.server.provider.WeblogicCacheServer";
   private static final String COHERENCE_ROOT_DIR = "coherence_3.8";
   private static final String COHERENCE_JAR = "coherence.jar";
   private static final String COHERENCE_SERVER_JAR_VERSION = "10.3.6.0";
   private static final String COHERENCE_SERVER_JAR = "weblogic.server.modules.coherence.server_10.3.6.0.jar";
   private static final Logger nmLog = Logger.getLogger("weblogic.nodemanager");
   protected static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   CoherenceProcessBuilder(ServerManagerI var1, StartupConfig var2) {
      super(var1, var2);

      assert var2 instanceof CoherenceStartupConfig;

   }

   String[] getJavaCommandLine(ServerManagerI var1, StartupConfig var2) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         ArrayList var3 = new ArrayList();
         String var4;
         if ((var4 = var2.getJavaHome()) == null) {
            var4 = System.getProperty("java.home");
         }

         var3.add(var4 + File.separator + "bin" + File.separator + "java");
         var3.add("-Dtangosol.coherence.member=" + var1.getServerName());
         CoherenceStartupConfig var5 = (CoherenceStartupConfig)var2;
         String var6 = this.getOperationalConfigFile(var1, var5);
         if (var6 != null) {
            var3.add("-Dtangosol.coherence.override=" + var6);
         } else {
            CoherenceStartupConfig.WellKnownAddress[] var7 = var5.getWellKnownAddresses();
            int var8 = var7.length;

            int var9;
            for(var9 = 0; var9 < var8; ++var9) {
               CoherenceStartupConfig.WellKnownAddress var10 = var7[var9];
               var3.add("-Dtangosol.coherence.wka=" + var10.getListenAddress());
               var3.add("-Dtangosol.coherence.wka.port=" + var10.getListenPort());
            }

            int var13 = var5.getMulticastListenPort();
            if (var13 > 0) {
               var3.add("-Dtangosol.coherence.clusterport=" + var13);
            }

            String var15 = var5.getMulticastListenAddress();
            if (var15 != null) {
               var3.add("-Dtangosol.coherence.clusteraddress=" + var15);
            }

            var9 = var5.getTimeToLive();
            if (var9 != 4) {
               var3.add("-Dtangosol.coherence.ttl=" + var9);
            }

            int var17 = var5.getUnicastListenPort();
            if (var17 > 0) {
               var3.add("-Dtangosol.coherence.localport=" + var17);
            }

            String var11 = var5.getUnicastListenAddress();
            if (var11 != null) {
               var3.add("-Dtangosol.coherence.localhost=" + var11);
            }

            boolean var12 = var5.isUnicastPortAutoAdjust();
            if (!var12) {
               var3.add("-Dtangosol.coherence.localport.adjust=false");
            }
         }

         String var14 = System.getProperty("java.class.path");
         var4 = var2.getClassPath();
         if (var4 == null) {
            var4 = this.getCoherenceClassPath(var2);
         } else {
            var4 = Platform.parseClassPath(var4, var14);
            StringBuilder var16 = new StringBuilder(var4);
            var16.append(File.pathSeparatorChar).append(var4);
         }

         var3.add("-Djava.class.path=" + Platform.preparePathForCommand(var4));
         if ((var4 = var2.getArguments()) != null) {
            var3.addAll(this.toOptionsList(var4));
         }

         var3.add("-Dweblogic.RootDirectory=" + var1.getDomainManager().getDomainDir().getAbsolutePath());
         var3.add("weblogic.nodemanager.server.provider.WeblogicCacheServer");
         return (String[])var3.toArray(new String[var3.size()]);
      }
   }

   private String getOperationalConfigFile(ServerManagerI var1, CoherenceStartupConfig var2) {
      String var3 = var2.getCustomClusterConfigurationFileName();
      String var4 = var2.getClusterName();
      if (var3 != null && var4 != null) {
         StringBuilder var5 = new StringBuilder(var1.getDomainManager().getDomainDir().getAbsolutePath());
         var5.append(File.separator).append("config");
         var5.append(File.separator).append("coherence");
         var5.append(File.separator).append(var4);
         var5.append(File.separator).append(var3);
         String var6 = var5.toString();
         File var7 = new File(var6);
         if (var7.canRead()) {
            return var6;
         } else {
            nmLog.log(Level.INFO, nmText.msgInvalidCoherenceOperationalConfigFile(var6));
            return null;
         }
      } else {
         return null;
      }
   }

   private String getCoherenceClassPath(StartupConfig var1) {
      StringBuilder var2 = new StringBuilder();
      String var3 = var1.getBeaHome();
      if (var3 == null) {
         var3 = System.getProperty("bea.home");
      }

      if (var3 != null) {
         var2.append(var3);
         var2.append(File.separatorChar).append("modules");
         var2.append(File.separatorChar).append("features");
         var2.append(File.separatorChar);
      }

      var2.append("weblogic.server.modules.coherence.server_10.3.6.0.jar");
      var2.append(File.pathSeparator);
      String var4 = System.getProperty("coherence.home");
      if (var4 != null) {
         var2.append(var4);
         var2.append(File.separatorChar).append("lib");
         var2.append(File.separatorChar);
      } else if (var3 != null) {
         var2.append(var3);
         var2.append(File.separatorChar).append("coherence_3.8");
         var2.append(File.separatorChar).append("lib");
         var2.append(File.separatorChar);
      }

      var2.append("coherence.jar");
      return var2.toString();
   }

   String[] getScriptCommandLine() {
      return this.getJavaCommandLine();
   }

   public Map<String, String> getScriptEnvironment() {
      return this.getEnvironment();
   }
}
