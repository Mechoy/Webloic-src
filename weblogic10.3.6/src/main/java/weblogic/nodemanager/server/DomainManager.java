package weblogic.nodemanager.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.common.ConfigException;
import weblogic.nodemanager.common.ServerType;
import weblogic.nodemanager.system.NodeManagerSystem;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;

public class DomainManager {
   private NMServer nmServer;
   private String domainName;
   private File scriptDir;
   private DomainDir domainDir;
   private UserInfo userInfo;
   private final Map<String, ServerManager> serverMgrs;
   private final Map<String, CoherenceServerManager> coherenceServerMgrs;
   private ClearOrEncryptedService encryptor;
   private Logger nmLog = Logger.getLogger("weblogic.nodemanager");
   private long saltFileTimeStamp;
   private long secretFileTimeStamp;
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   public DomainManager(NMServer var1, String var2, String var3) throws ConfigException, IOException {
      this.nmServer = var1;
      this.domainName = var2;
      this.domainDir = new DomainDir(var3);
      this.serverMgrs = new ConcurrentHashMap();
      this.coherenceServerMgrs = new ConcurrentHashMap();
      this.scriptDir = this.domainDir.getMigrationScriptDir();
      this.initialize();
   }

   void resetCredentials(String var1, String var2) throws IOException {
      File var3 = this.domainDir.getSecretFile();
      if (!var3.exists()) {
         throw new FileNotFoundException(nmText.getPropertiesFileNotFound(var3.toString()));
      } else if (!var3.canWrite()) {
         throw new FileNotFoundException(nmText.getPropertiesFileNotWritable(var3.toString()));
      } else {
         this.userInfo.set(var1, var2);
         this.userInfo.save(var3);
      }
   }

   private void initialize() throws ConfigException, IOException {
      if (!this.domainDir.isDirectory()) {
         throw new FileNotFoundException(nmText.getDomainDirNotFound(this.domainDir.toString()));
      } else if (!this.domainDir.getSaltFile().exists() && !this.domainDir.getOldSaltFile().exists()) {
         throw new FileNotFoundException(nmText.getInvalidDomainSalt(this.domainDir.toString()));
      } else {
         this.loadSaltFile();
         if (this.nmServer.getConfig().isAuthenticationEnabled()) {
            this.loadUserInfo();
         }

         ServerDir[] var1 = this.domainDir.getServerDirs();
         if (var1 != null) {
            ServerDir[] var2 = var1;
            int var3 = var1.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               ServerDir var5 = var2[var4];
               this.serverMgrs.put(var5.getName(), new ServerManager(this, var5.getName()));
            }

            Iterator var6 = this.serverMgrs.values().iterator();

            while(var6.hasNext()) {
               ServerManager var7 = (ServerManager)var6.next();
               var7.recoverServer();
            }
         }

         this.recoverCoherenceServers();
      }
   }

   private void recoverCoherenceServers() throws ConfigException, IOException {
      ServerDir[] var1 = this.domainDir.getServerDirs(ServerType.Coherence);
      if (var1 != null) {
         ServerDir[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ServerDir var5 = var2[var4];
            this.coherenceServerMgrs.put(var5.getName(), new CoherenceServerManager(this, var5.getName()));
         }

         Iterator var6 = this.coherenceServerMgrs.values().iterator();

         while(var6.hasNext()) {
            CoherenceServerManager var7 = (CoherenceServerManager)var6.next();
            var7.recoverServer();
         }
      }

   }

   private void loadUserInfo() throws IOException {
      if (this.encryptor == null) {
         throw new FileNotFoundException(nmText.getSaltFileNotFound());
      } else {
         File var1 = this.domainDir.getSecretFile();
         if (!var1.exists()) {
            throw new FileNotFoundException(nmText.getPropertiesFileNotFound(var1.toString()));
         } else {
            this.userInfo = new UserInfo(this.domainDir);
            this.userInfo.load(var1);
            if (this.userInfo.saveNeeded() && var1.canWrite()) {
               this.userInfo.save(var1);
            }

            this.secretFileTimeStamp = var1.lastModified();
         }
      }
   }

   public Map<String, String> getAllStates() throws IOException {
      Map var1 = this.getAllStates(this.serverMgrs);
      if (this.nmLog.isLoggable(Level.FINE)) {
         this.log(Level.FINE, "States = " + var1);
         this.log(Level.FINE, "Coherence States = " + (this.coherenceServerMgrs != null ? this.getAllStates(this.coherenceServerMgrs) : Collections.EMPTY_MAP));
      }

      return var1;
   }

   private Map<String, String> getAllStates(Map<String, ? extends AbstractServerManager> var1) throws IOException {
      HashMap var2 = new HashMap();
      if (var1 != null) {
         Set var3 = var1.keySet();
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            AbstractServerManager var6 = (AbstractServerManager)var1.get(var5);
            String var7 = var6.getState();
            var2.put(var5, var7 == null ? "UNKNOWN" : var7);
         }
      }

      return var2;
   }

   private synchronized void loadSaltFile() throws IOException {
      EncryptionService var1;
      try {
         var1 = SerializedSystemIni.getEncryptionService(this.domainDir.getPath());
         this.saltFileTimeStamp = this.getSaltFileTimeStamp();
      } catch (RuntimeException var3) {
         throw (IOException)(new IOException(nmText.getErrorLoadingSalt())).initCause(var3);
      }

      this.encryptor = new ClearOrEncryptedService(var1);
   }

   public synchronized ClearOrEncryptedService getEncryptor() {
      return this.encryptor;
   }

   public ServerManager getServerManager(String var1) throws ConfigException, IOException {
      synchronized(this.serverMgrs) {
         ServerManager var3 = (ServerManager)this.serverMgrs.get(var1);
         if (var3 == null) {
            var3 = new ServerManager(this, var1);
            this.serverMgrs.put(var1, var3);
         }

         return var3;
      }
   }

   public CoherenceServerManager getCoherenceServerManager(String var1) throws ConfigException, IOException {
      synchronized(this.coherenceServerMgrs) {
         CoherenceServerManager var3 = (CoherenceServerManager)this.coherenceServerMgrs.get(var1);
         if (var3 == null) {
            var3 = new CoherenceServerManager(this, var1);
            this.coherenceServerMgrs.put(var1, var3);
         }

         return var3;
      }
   }

   public NMServer getNMServer() {
      return this.nmServer;
   }

   public String getDomainName() {
      return this.domainName;
   }

   public DomainDir getDomainDir() {
      return this.domainDir;
   }

   public boolean isAuthorized(String var1, String var2) {
      return this.userInfo != null ? this.userInfo.verify(var1, var2) : true;
   }

   public void checkFileStamps() throws IOException {
      Map var1 = this.getAllStates();
      Iterator var2 = var1.values().iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            long var7 = this.getSaltFileTimeStamp();
            if (var7 > this.saltFileTimeStamp) {
               this.loadSaltFile();
            }

            if (this.nmServer.getConfig().isAuthenticationEnabled() && this.domainDir.getSecretFile().exists()) {
               long var5 = this.domainDir.getSecretFile().lastModified();
               if (var5 > this.secretFileTimeStamp) {
                  this.loadUserInfo();
               }
            }

            return;
         }

         var3 = (String)var2.next();
      } while(var3.equals("SHUTDOWN") || var3.equals("UNKNOWN"));

   }

   private long getSaltFileTimeStamp() {
      long var1 = -1L;
      if (this.domainDir.getSaltFile().exists()) {
         var1 = this.domainDir.getSaltFile().lastModified();
      } else if (this.domainDir.getOldSaltFile().exists()) {
         var1 = this.domainDir.getOldSaltFile().lastModified();
      }

      return var1;
   }

   public void log(Level var1, String var2, Throwable var3) {
      LogRecord var4 = new LogRecord(var1, var2);
      var4.setParameters(new String[]{this.domainName});
      if (var3 != null) {
         var4.setThrown(var3);
      }

      this.nmLog.log(var4);
   }

   public void log(Level var1, String var2) {
      this.log(var1, var2, (Throwable)null);
   }

   public int execScript(String var1, long var2) throws IOException {
      File var4 = new File(this.scriptDir, var1);
      if (!var4.exists()) {
         throw new FileNotFoundException(nmText.scriptNotFound(this.scriptDir.getPath(), var1));
      } else {
         return NodeManagerSystem.getInstance().executeScript(new String[]{var4.getPath()}, (Properties)null, this.scriptDir, var2);
      }
   }
}
