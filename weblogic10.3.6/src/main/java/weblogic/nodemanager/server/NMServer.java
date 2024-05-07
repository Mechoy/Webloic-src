package weblogic.nodemanager.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.nodemanager.NMException;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.common.ConfigException;
import weblogic.nodemanager.plugin.NMServerI;
import weblogic.nodemanager.system.NodeManagerSystem;
import weblogic.nodemanager.util.Platform;

public class NMServer implements NMServerI {
   private NMServerConfig config;
   private SSLConfig sslConfig;
   private Encryptor encryptor;
   private Map<String, DomainManager> domains = new HashMap();
   private boolean verbose;
   private boolean debug;
   private static Channel inheritedChannel;
   public static final String VERSION = "10.3";
   public static final String FULL_VERSION = "Node manager v10.3";
   public static final String CONFIG_FILE_NAME = "nodemanager.properties";
   public static final Logger nmLog;
   private static final NodeManagerTextTextFormatter nmText;
   private static final String NM_PROP = "weblogic.nodemanager.";
   private static final String[] usageMsg;

   public boolean isDebugEnabled() {
      return this.debug;
   }

   public static void redirectStandardStreams(String var0, String var1, String var2) {
      PrintStream var3 = null;
      PrintStream var4 = null;
      FileInputStream var5 = null;

      try {
         var3 = new PrintStream(new FileOutputStream(var1, true));
         if (Platform.isWindows() && var1.equalsIgnoreCase(var2)) {
            var4 = var3;
         } else if (Platform.isUnix() && var1.equals(var2)) {
            var4 = var3;
         } else {
            var4 = new PrintStream(new FileOutputStream(var2, true));
         }

         System.setOut(var3);
         System.setErr(var4);
      } catch (Exception var8) {
         nmLog.warning(nmText.getStdOutErrStreams(var1.toString(), var2.toString()));
      }

      try {
         var5 = new FileInputStream(var0);
         System.setIn(var5);
      } catch (Exception var7) {
         nmLog.warning(nmText.getInputStream(var0.toString()));
      }

   }

   public NMServer(String[] var1) throws IOException, ConfigException {
      NMProperties var2 = new NMProperties(System.getProperties());
      if (inheritedChannel != null) {
         redirectStandardStreams("/dev/null", "nodemanager.out", "nodemanager.out");
      }

      this.parseArguments(var1, var2);
      Properties var3 = System.getProperties();
      Iterator var4 = var3.keySet().iterator();

      String var6;
      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var6 = var3.getProperty(var5);
         if (var5.startsWith("weblogic.nodemanager.")) {
            var5 = var5.substring("weblogic.nodemanager.".length());
            if (!var2.containsKey(var5)) {
               var2.setProperty(var5, var6);
            }
         } else if (var5.length() > 0 && Character.isUpperCase(var5.charAt(0)) && !var2.containsKey(var5)) {
            var2.setProperty(var5, var6);
         }
      }

      File var11 = new File(var2.getProperty("NodeManagerHome", System.getProperty("user.dir")));
      var6 = var2.getProperty("PropertiesFile");
      File var12;
      if (var6 != null) {
         var12 = new File(var6);
      } else {
         var12 = new File(var11, "nodemanager.properties");
      }

      NMProperties var7 = new NMProperties();
      if (var12.exists()) {
         try {
            var7.load(var12);
         } catch (IllegalArgumentException var9) {
            throw (IOException)(new IOException(nmText.getInvalidNMPropFile(var12.toString()))).initCause(var9);
         } catch (IOException var10) {
            throw (IOException)(new IOException(nmText.getErrorReadingNMPropFile(var12.toString()))).initCause(var10);
         }
      }

      var7.putAll(var2);
      if (this.verbose && var7.getProperty("LogToStderr") == null) {
         var7.setProperty("LogToStderr", "true");
      }

      this.init(var7);
      if (var12.exists()) {
         nmLog.info(nmText.getLoadedNMProps(var12.toString()));
      } else {
         nmLog.warning(nmText.getNMPropsNotFound(var12.toString()));
         nmLog.info(nmText.getSavingNMProps(var12.toString()));
         NMProperties var8 = this.config.getConfigProperties();
         var8.putAll(var7);
         var8.save(var12);
      }

      Upgrader.upgrade(this.config);
      if (this.verbose) {
         String var13 = var7.getProperty("LogToStderr");
         if (var13 == null || "true".equals(var13)) {
            System.err.println("Node manager v10.3");
            System.err.println();
            this.config.print(System.err);
         }
      }

   }

   public NMServer(Properties var1) throws IOException, ConfigException {
      NMProperties var2 = new NMProperties();
      var2.putAll(var1);
      this.init(var2);
   }

   private void init(NMProperties var1) throws IOException, ConfigException {
      this.config = new NMServerConfig(var1);
      if (this.config.isSecureListener()) {
         this.encryptor = new Encryptor(this.config);
         this.sslConfig = new SSLConfig(var1, this.encryptor);
      }

   }

   public void start(Channel var1) throws ConfigException, IOException {
      this.initDomains();
      Object var2;
      if (this.config.isSecureListener()) {
         var2 = new SSLListener(this, var1);
      } else {
         var2 = new Listener(this, var1);
      }

      ((Listener)var2).init();
      ((Listener)var2).run();
   }

   private void initDomains() throws ConfigException, IOException {
      Map var1 = this.config.getDomainsMap();
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         String var4 = (String)var3.getKey();
         DomainDir var5 = new DomainDir((String)var3.getValue());
         if (var5.isValid()) {
            String var6 = var5.getPath();

            try {
               var6 = var5.getCanonicalPath();
            } catch (IOException var10) {
               throw new ConfigException(nmText.getConfigError(var6) + " " + var10);
            }

            DomainManager var7;
            try {
               var7 = new DomainManager(this, var4, var6);
            } catch (IOException var9) {
               nmLog.log(Level.WARNING, nmText.getConfigError(var6), var9);
               return;
            }

            this.domains.put(var6, var7);
         }
      }

   }

   public DomainManager getDomainManager(String var1, String var2) throws ConfigException, IOException {
      Map var3 = this.config.getDomainsMap();
      String var4 = (String)var3.get(var1);
      if (var4 == null) {
         Iterator var5 = this.domains.values().iterator();

         while(var5.hasNext()) {
            DomainManager var6 = (DomainManager)var5.next();
            if (var6.getDomainName().equals(var1)) {
               var6.checkFileStamps();
               return var6;
            }
         }

         if (!this.config.isDomainRegistrationEnabled()) {
            throw new ConfigException(nmText.getDynamicDomainRegistrationNotAllowed(var1, var2));
         }

         if (var2 == null) {
            nmLog.warning(nmText.getUnregisteredDomainName(var1));
            var2 = this.config.getWeblogicHome();
         }
      } else if (var2 == null) {
         var2 = var4;
      }

      return this.findOrCreateDomainManager(var1, var2);
   }

   private DomainManager findOrCreateDomainManager(String var1, String var2) throws ConfigException, IOException {
      var2 = (new File(var2)).getCanonicalPath();
      synchronized(this.domains) {
         DomainManager var4 = (DomainManager)this.domains.get(var2);
         if (var4 == null) {
            try {
               var4 = new DomainManager(this, var1, var2);
            } catch (ConfigException var7) {
               nmLog.warning(nmText.getDomainInitError(var1, var2));
               throw var7;
            } catch (IOException var8) {
               nmLog.warning(nmText.getDomainInitError(var1, var2));
               throw var8;
            }

            this.domains.put(var2, var4);
         } else {
            var4.checkFileStamps();
         }

         return var4;
      }
   }

   public void reportDomainError(String var1, String var2) {
      nmLog.warning(nmText.getDomainInitError(var1, var2));
   }

   public Thread initializeAndStartServerMonitor(WLSProcess var1) throws NMException {
      if (var1 == null) {
         throw new NMException("Given process is null");
      } else {
         NMException var3;
         try {
            ServerManager var2 = this.findOrCreateServerManager(var1.getServerName(), var1.getDomainName(), var1.getDomainDirectory());
            return var2.createAndStartMonitor(var1);
         } catch (IOException var4) {
            if (var4 instanceof NMException) {
               throw (NMException)var4;
            } else {
               var3 = new NMException(nmText.getStartMonitorIOError(var1.getServerName()));
               var3.initCause(var4);
               throw var3;
            }
         } catch (ConfigException var5) {
            var3 = new NMException(nmText.getStartMonitorConfigError(var1.getServerName()));
            var3.initCause(var5);
            throw var3;
         }
      }
   }

   public NMServerConfig getConfig() {
      return this.config;
   }

   public SSLConfig getSSLConfig() {
      return this.sslConfig;
   }

   public Encryptor getEncryptor() {
      return this.encryptor;
   }

   public static void main(String[] var0) {
      assert inheritedChannel == null || inheritedChannel instanceof ServerSocketChannel : "Unexpected inherited channel" + inheritedChannel;

      for(int var1 = 0; var1 < var0.length; ++var1) {
         if (var0[var1].equals("-?") || var0[var1].equals("-h") || var0[var1].equals("-help")) {
            printUsage();
            System.exit(0);
         }
      }

      try {
         NodeManagerSystem var4 = NodeManagerSystem.getInstance();
         NMServer var2 = new NMServer(var0);
         var4.initialize(var2);
         var2.start(inheritedChannel);
      } catch (Throwable var3) {
         nmLog.log(Level.SEVERE, nmText.getFatalError(), var3);
      }

   }

   private void parseArguments(String[] var1, NMProperties var2) {
      try {
         int var3 = 0;

         while(var3 < var1.length) {
            String var4 = var1[var3++];
            if (var4.equals("-f")) {
               var2.setProperty("PropertiesFile", var1[var3++]);
            } else if (var4.equals("-n")) {
               var2.setProperty("NodeManagerHome", var1[var3++]);
            } else if (var4.equals("-d")) {
               var2.setProperty("LogLevel", "ALL");
               this.debug = true;
            } else if (var4.equals("-v")) {
               this.verbose = true;
            } else {
               if (!var4.startsWith("-%")) {
                  throw new IllegalArgumentException(nmText.getUnrecognizedOption(var4));
               }

               String var5 = var1[var3++];
               if (var5.contains("Q")) {
                  var2.setProperty("QuitEnabled", String.valueOf(true));
               }
            }
         }

      } catch (IndexOutOfBoundsException var6) {
         throw new IllegalArgumentException(nmText.getInvalidArgument());
      }
   }

   private static void printUsage() {
      for(int var0 = 0; var0 < usageMsg.length; ++var0) {
         System.err.println(usageMsg[var0]);
      }

   }

   private ServerManager findOrCreateServerManager(String var1, String var2, String var3) throws IOException, ConfigException {
      if (var1 != null && var1.length() != 0) {
         if (var2 != null && var2.length() != 0) {
            DomainManager var4 = this.getDomainManager(var2, var3);
            if (var4 == null) {
               throw new NMException(nmText.getBadDomain(var2));
            } else {
               return var4.getServerManager(var1);
            }
         } else {
            throw new NMException(nmText.getDomainNameNull());
         }
      } else {
         throw new NMException(nmText.getServerNameNull());
      }
   }

   static {
      try {
         inheritedChannel = System.inheritedChannel();
      } catch (Exception var1) {
         inheritedChannel = null;
      }

      nmLog = Logger.getLogger("weblogic.nodemanager");
      nmText = NodeManagerTextTextFormatter.getInstance();
      usageMsg = new String[]{"Usage: java weblogic.nodemanager.server.NMServer [OPTIONS]", "", "Where options include:", "  -n <home>  Specify node manager home directory (default is PWD)", "  -f <file>  Specify node manager properties file", "             (default is NM_HOME/nodemanager.properties)", "  -v         Run in verbose mode", "  -d         Enable debug output to log file", "  -?, -h     Print this usage message"};
   }
}
