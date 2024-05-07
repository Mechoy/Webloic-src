package weblogic.management.scripting;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import org.python.core.ArgParser;
import org.python.core.PyDictionary;
import org.python.core.PyObject;
import org.python.core.PyString;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.scripting.utils.ErrorInformation;
import weblogic.management.scripting.utils.WLSTMsgTextFormatter;
import weblogic.nodemanager.NMException;
import weblogic.nodemanager.client.NMClient;
import weblogic.nodemanager.common.ServerType;
import weblogic.nodemanager.server.NMEncryptionHelper;
import weblogic.security.UserConfigFileManager;
import weblogic.security.UsernameAndPassword;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.utils.FileUtils;

public class NodeManagerService implements NMConstants {
   WLScriptContext ctx = null;
   private static WLSTMsgTextFormatter txtFmt;
   private NMClient nmc = null;
   private String domainName = "mydomain";
   private boolean connectedToNM = false;
   private String nmVersion = "";
   private String domainDir = ".";
   private final String DUMMY_DOMAIN_NAME = "<NO_DOMAIN_NAME>";
   private String nmType = null;
   private String nmHost = null;
   private int nmPort = -1;
   private UsernameAndPassword nmCredential = new UsernameAndPassword();
   private String cmdPath = null;
   private String verbose = "false";
   private static final String SALT_FILE = "security/SerializedSystemIni.dat";
   private static final String SERVICEMIGRATION = "bin/service_migration";
   private static final String SERVERMIGRATION = "bin/server_migration";
   private static final String NM_USER_FILE_NAME = "nm_password.properties";
   private static final String HTTP_STRING = "http";
   private static final String HTTPS_STRING = "https";
   private static final String T3S_STRING = "t3s";
   private static final String IIOPS_STRING = "iiops";

   public NodeManagerService(WLScriptContext ctx) {
      this.ctx = ctx;
      txtFmt = ctx.getWLSTMsgFormatter();
   }

   private void initSystemProperties(String nmType) {
      if (nmType.equalsIgnoreCase("ssl") || nmType.toLowerCase().startsWith("vmms-".toLowerCase())) {
         String keyStore = System.getProperty("weblogic.security.TrustKeyStore");
         if (keyStore == null) {
            System.setProperty("weblogic.security.TrustKeyStore", "DemoTrust");
         }

         String igSSL = System.getProperty("weblogic.security.SSL.ignoreHostnameVerification");
         if (igSSL == null) {
            System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
         }
      }

   }

   public void nmConnect(PyObject[] args, String[] kw) throws ScriptException {
      this.ctx.commandType = "connect";
      ArgParser ap = new ArgParser("nmConnect", args, kw, "username", "password");
      if (ap.getString(0) != null) {
         this.nmCredential.setUsername(ap.getString(0));
      }

      if (ap.getString(1) != null) {
         this.nmCredential.setPassword(ap.getString(1).toCharArray());
      }

      this.nmHost = ap.getString(2, "localhost");
      this.nmPort = ap.getInt(3);
      this.domainName = ap.getString(4);
      this.domainDir = ap.getString(5);
      this.nmType = ap.getString(6, "ssl");
      this.verbose = ap.getString(7, "false");
      this.initSystemProperties(this.nmType);
      boolean usingBootProps = false;
      if (!this.nmCredential.isUsernameSet() || !this.nmCredential.isPasswordSet()) {
         usingBootProps = true;
         this.ctx.printDebug("Will check if userConfig and userKeyFile should be used to connect to the server");
      }

      if (this.nmPort == -1) {
         this.nmPort = this.determinePort(this.nmType);
      }

      PyDictionary objs = (PyDictionary)ap.getPyObject(8);
      String userConfig = null;
      String userKey = null;
      this.ctx.commandType = "nmConnect";
      if (usingBootProps) {
         WLScriptContext var10002 = this.ctx;
         PyString pyuserConfigFile = new PyString("userConfigFile");
         var10002 = this.ctx;
         PyString pyuserKeyFile = new PyString("userKeyFile");
         if (objs.has_key(pyuserConfigFile)) {
            userConfig = objs.get(pyuserConfigFile).toString();
            this.ctx.printDebug("The userConfig file location is " + userConfig);
         }

         if (objs.has_key(pyuserKeyFile)) {
            userKey = objs.get(pyuserKeyFile).toString();
            this.ctx.printDebug("The user key loaction is " + userKey);
         }

         UsernameAndPassword UAndP = UserConfigFileManager.getUsernameAndPassword(userConfig, userKey, "weblogic.management");
         if (UAndP != null && UAndP.isUsernameSet() && UAndP.isPasswordSet()) {
            this.nmCredential = UAndP;
            this.ctx.printDebug("The username is " + this.nmCredential.getUsername());
            this.ctx.printDebug("The password is ******");
         }
      }

      if (!this.nmCredential.isUsernameSet() || !this.nmCredential.isPasswordSet()) {
         this.ctx.println(txtFmt.getDefaultingNMUsername("weblogic"));
         this.ctx.println(txtFmt.getDefaultingNMPassword("welcome1"));
         this.nmCredential.setUsername("weblogic");
         this.nmCredential.setPassword("welcome1".toCharArray());
      }

      this.ctx.println(txtFmt.getConnectingToNodeManager());
      if (this.domainName == null) {
         this.domainName = "mydomain";
      }

      this.nmc = NMClient.getInstance(this.nmType);
      this.nmc.setHost(this.nmHost);
      this.nmc.setPort(this.nmPort);
      this.nmc.setDomainName(this.domainName);
      if (this.domainDir != null) {
         this.nmc.setDomainDir(this.domainDir);
      }

      if (this.ctx.getBoolean(this.verbose)) {
         this.nmc.setVerbose(true);
      }

      this.nmc.setNMUser(this.nmCredential.getUsername());
      this.nmc.setNMPass(new String(this.nmCredential.getPassword()));
      this.verifyConnection();
      this.connectedToNM = true;
   }

   private void verifyConnection() throws ScriptException {
      try {
         this.ctx.printDebug("checking the username & password for NM");
         this.nmVersion = this.nmc.getVersion();
         this.ctx.printDebug("got connected to NM and got the version " + this.nmVersion);
         this.ctx.println(txtFmt.getConnectedToNodeManager());
      } catch (IOException var2) {
         this.ctx.throwWLSTException(txtFmt.getCouldNotConnectToNodeManager(), var2);
      }

   }

   private int determinePort(String nmtype) {
      if (!nmtype.equalsIgnoreCase("plain") && !nmtype.equalsIgnoreCase("ssl")) {
         if (nmtype.equalsIgnoreCase("ssh")) {
            return 22;
         } else if (nmtype.equalsIgnoreCase("rsh")) {
            return 514;
         } else if (nmtype.startsWith("vmms-")) {
            return 4443;
         } else {
            return nmtype.startsWith("vmm-") ? 8888 : 5556;
         }
      } else {
         return 5556;
      }
   }

   NMClient getNMClient() {
      return this.nmc;
   }

   public String getDomainName() {
      return this.domainName;
   }

   public boolean isConnectedToNM() {
      return this.connectedToNM;
   }

   public void nmDisconnect() throws ScriptException {
      this.ctx.commandType = "nmDisconnect";

      try {
         if (this.nmc != null) {
            this.nmc.done();
         }
      } catch (IOException var2) {
         if (this.ctx.debug) {
            var2.printStackTrace();
         }
      }

      this.nmc = null;
      this.connectedToNM = false;
      this.nmCredential.setUsername((String)null);
      this.nmCredential.setPassword((char[])null);
      this.ctx.println(txtFmt.getDisconnectedFromNodeManager());
   }

   public boolean nm() throws ScriptException {
      this.ctx.commandType = "nm";
      if (this.connectedToNM) {
         this.ctx.println(txtFmt.getCurrentlyConnectedNM(this.domainName));
      } else {
         this.ctx.println(txtFmt.getNotConnectedNM());
      }

      return this.connectedToNM;
   }

   public boolean nmKill(String serverName) throws ScriptException {
      return this.nmKill(serverName, ServerType.WebLogic.name());
   }

   public boolean nmKill(String serverName, String serverType) throws ScriptException {
      this.ctx.commandType = "nmKill";
      boolean result = false;
      if (!this.connectedToNM) {
         this.ctx.println(txtFmt.getNotConnectedNM());
         return result;
      } else {
         this.ctx.println(txtFmt.getKillingServer(serverName));
         this.nmc = this.getNewNMClient(serverName, this.getServerType(serverType), this.domainDir, this.domainName);

         try {
            this.nmc.kill();
         } catch (NMException var5) {
            this.ctx.throwWLSTException(txtFmt.getErrorKillingServer(serverName), var5);
         } catch (IOException var6) {
            this.ctx.throwWLSTException(txtFmt.getErrorKillingServer(serverName), var6);
         }

         this.ctx.println(txtFmt.getKilledServer(serverName));
         return true;
      }
   }

   String decrypt(Object obj, String domainDir) throws ScriptException {
      try {
         EncryptionService es = null;
         ClearOrEncryptedService ces = null;
         if (domainDir != null) {
            File f = new File(domainDir);
            this.ctx.printDebug("Setting the root directory to " + f.getAbsolutePath());
            es = SerializedSystemIni.getEncryptionService(f.getAbsolutePath());
         } else {
            es = SerializedSystemIni.getExistingEncryptionService();
         }

         if (es == null) {
            this.ctx.errorMsg = txtFmt.getErrorInitializingEncryptionService();
            this.ctx.errorInfo = new ErrorInformation(this.ctx.errorMsg);
            this.ctx.exceptionHandler.handleException(this.ctx.errorInfo);
         }

         ces = new ClearOrEncryptedService(es);
         return obj instanceof String ? ces.decrypt((String)obj) : new String(ces.decryptBytes((byte[])((byte[])obj)));
      } catch (Throwable var6) {
         this.ctx.throwWLSTException(txtFmt.getErrorEncryptingValue(), var6);
         return null;
      }
   }

   public void nmEnrollMachine(String domainDir, String nmHome) throws ScriptException {
      try {
         this.ctx.commandType = "nmEnroll";
         if (domainDir == null) {
            domainDir = ".";
         }

         File domainDirFile = new File(domainDir);
         this.ctx.println(txtFmt.getEnrollingMachineInDomain(domainDir));
         this.downloadRequiredFiles(domainDirFile.getAbsolutePath());
         String username = this.ctx.runtimeDomainMBean.getSecurityConfiguration().getNodeManagerUsername();
         byte[] encryptedPassword = this.ctx.runtimeDomainMBean.getSecurityConfiguration().getNodeManagerPasswordEncrypted();
         String password = this.decrypt(encryptedPassword, domainDir);
         if (password == null) {
            password = "";
         }

         this.ctx.printDebug("The username and pwd are " + username + "   ****");
         Properties props = new Properties();
         String hash = NMEncryptionHelper.getNMSecretHash(domainDir, username, password);
         if (hash == null) {
            hash = "";
         }

         props.setProperty("hashed", hash);
         File propsFile = new File(domainDirFile.getAbsolutePath() + "/config/nodemanager/" + "nm_password.properties");
         propsFile.getParentFile().mkdirs();
         if (propsFile.exists()) {
            this.ctx.printDebug("Found an existing properties file, will delete it");
            FileUtils.remove(propsFile);
         }

         FileOutputStream os = new FileOutputStream(propsFile);
         props.store(os, "");
         os.close();
         File home = null;
         if (nmHome == null) {
            home = new File(BootStrap.getWebLogicHome());
            nmHome = home.getParentFile().getAbsolutePath() + "/common/nodemanager";
         }

         this.ctx.printDebug("NMHome is " + nmHome);
         File nmDomains = new File(nmHome + "/nodemanager.domains");
         Properties nmDomainProps = new Properties();
         String domainName = this.ctx.runtimeDomainMBean.getName();
         if (nmDomains.exists()) {
            this.ctx.printDebug("nodemanager.domains file exists, the new domain will be added");
            FileInputStream fis = new FileInputStream(nmDomains);
            nmDomainProps.load(fis);
            nmDomainProps.put(domainName, domainDirFile.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(nmDomains);
            nmDomainProps.store(fos, "");
            fos.close();
         } else {
            this.ctx.printDebug("creating a new nodemanager.domains, the new domain will be added");
            nmDomainProps.put(domainName, domainDirFile.getAbsolutePath());
            nmDomains.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(nmDomains);
            this.ctx.printDebug("The file will be written to " + nmDomains.getAbsolutePath());
            nmDomainProps.store(fos, "");
            fos.close();
         }

         this.ctx.println(txtFmt.getEnrolledMachineInDomain(domainDirFile.getAbsolutePath()));
      } catch (IOException var17) {
         this.ctx.throwWLSTException(txtFmt.getFailedToEnrolMachineInDomain(), var17);
      }

   }

   private void downloadRequiredFiles(String domainDir) throws ScriptException {
      this.downloadFile(domainDir, "security/SerializedSystemIni.dat");
      this.downloadFile(domainDir, "bin/service_migration");
      this.downloadFile(domainDir, "bin/server_migration");
   }

   public boolean nmStart(String serverName, String domainDir, Properties props, Writer outWriter) throws ScriptException, IOException {
      return this.nmStart(serverName, domainDir, props, outWriter, ServerType.WebLogic.name());
   }

   public boolean nmStart(String serverName, String domainDir, Properties props, Writer outWriter, String serverType) throws ScriptException, IOException {
      this.ctx.commandType = "nmStart";
      boolean result = false;
      if (!this.connectedToNM) {
         this.ctx.println(txtFmt.getNotConnectedNM());
         return result;
      } else {
         this.ctx.println(txtFmt.getStartingServer(serverName));
         if (domainDir == null) {
            domainDir = this.domainDir;
         }

         if (this.domainName.equals("<NO_DOMAIN_NAME>")) {
            this.nmc = this.getNewNMClient(serverName, this.getServerType(serverType), domainDir, "mydomain");
            this.domainName = "mydomain";
            this.domainDir = domainDir;
         } else {
            this.nmc = this.getNewNMClient(serverName, this.getServerType(serverType), domainDir, this.domainName);
         }

         try {
            if (props == null) {
               this.nmc.start();
            } else {
               this.nmc.start(props);
            }
         } catch (NMException var8) {
            this.ctx.println(txtFmt.getErrorStartingServer(serverName, var8.toString()));
            return false;
         }

         this.ctx.println(txtFmt.getStartedServer(serverName));
         return true;
      }
   }

   public String nmVersion() throws ScriptException {
      this.ctx.commandType = "nmVersion";
      if (!this.connectedToNM) {
         this.ctx.println(txtFmt.getNotConnectedNM());
         return "";
      } else {
         this.ctx.println(txtFmt.getNMVersion(this.nmVersion));
         return this.nmVersion;
      }
   }

   public String nmServerStatus(String serverName) throws ScriptException, IOException {
      return this.nmServerStatus(serverName, ServerType.WebLogic.name());
   }

   public String nmServerStatus(String serverName, String serverType) throws ScriptException, IOException {
      this.ctx.commandType = "nmServerStatus";
      String result = "";
      if (!this.connectedToNM) {
         this.ctx.println(txtFmt.getNotConnectedNM());
         return result;
      } else {
         this.nmc = this.getNewNMClient(serverName, this.getServerType(serverType), this.domainDir, this.domainName);
         result = this.nmc.getState(0);
         this.ctx.println("\n" + result + "\n");
         return result;
      }
   }

   public void nmServerLog(String serverName, Writer outWriter) throws ScriptException, IOException {
      this.nmServerLog(serverName, outWriter, ServerType.WebLogic.name());
   }

   public void nmServerLog(String serverName, Writer outWriter, String serverType) throws ScriptException, IOException {
      this.ctx.commandType = "nmServerLog";
      if (!this.connectedToNM) {
         this.ctx.println(txtFmt.getNotConnectedNM());
      } else {
         this.nmc = this.getNewNMClient(serverName, this.getServerType(serverType), this.domainDir, this.domainName);
         if (outWriter == null) {
            outWriter = this.getWriter();
         }

         this.nmc.getLog(outWriter);
         outWriter.flush();
      }
   }

   public void nmQuit() throws ScriptException, IOException {
      this.ctx.commandType = "startNodeManager";
      if (!this.connectedToNM) {
         this.ctx.println("\nCannot stop the Node Manager unless you are connected to it.\n");
      } else {
         if (this.nmc != null) {
            try {
               this.nmc.quit();
               this.ctx.println("Stopped NodeManager successfully");
               this.nmc = null;
               this.connectedToNM = false;
            } catch (NMException var2) {
               this.ctx.throwWLSTException("Problem stopping the Node Manager.", var2);
            } catch (IOException var3) {
               this.ctx.throwWLSTException("Problem stopping the Node Manager.", var3);
            }
         }

      }
   }

   private void initFDSConnection(String user, String password, String filePath, HttpURLConnection connection) {
      connection.setDoOutput(true);
      connection.setDoInput(true);
      connection.setAllowUserInteraction(true);
      connection.setRequestProperty("wl_request_type", "wl_managed_server_independence_request");
      connection.setRequestProperty("username", user);
      connection.setRequestProperty("password", password);
      if (filePath != null) {
         connection.setRequestProperty("wl_managed_server_independence_request_filename", filePath);
      }

   }

   private String getFDSUrl(String adminUrlString) throws URISyntaxException {
      if (!adminUrlString.startsWith("http")) {
         URI url = new URI(adminUrlString);
         URI httpUrl = new URI(this.getHTTPProtocol(url.getScheme()), (String)null, url.getHost(), url.getPort(), (String)null, (String)null, (String)null);
         adminUrlString = httpUrl.toString();
      }

      if (!adminUrlString.endsWith("/")) {
         adminUrlString = adminUrlString + "/";
      }

      return adminUrlString + "bea_wls_management_internal2/wl_management";
   }

   private String getHTTPProtocol(String scheme) {
      return !scheme.equals("https") && !scheme.equals("t3s") && !scheme.equals("iiops") ? "http" : "https";
   }

   private HttpURLConnection getFDSConnection(String fdsUrlString) throws IOException {
      URL fdsUrl = new URL(fdsUrlString);
      return (HttpURLConnection)fdsUrl.openConnection();
   }

   private void downloadFile(String domainDir, String fileToDownload) throws ScriptException {
      String fdsUrlString = null;

      try {
         fdsUrlString = this.getFDSUrl(this.ctx.url);
         HttpURLConnection connection = this.getFDSConnection(fdsUrlString);
         this.initFDSConnection(new String(this.ctx.username_bytes), new String(this.ctx.password_bytes), fileToDownload, connection);
         connection.connect();
         this.ctx.printDebug("Downloading the file " + fileToDownload);
         InputStream fileObject = null;

         try {
            fileObject = connection.getInputStream();
         } catch (FileNotFoundException var12) {
            this.ctx.printDebug("Error downloading the file " + fileToDownload);
            return;
         }

         File file = new File(domainDir + "/" + fileToDownload);
         if (!"bin/service_migration".equalsIgnoreCase(fileToDownload) && !"bin/server_migration".equalsIgnoreCase(fileToDownload)) {
            if (!file.exists()) {
               FileUtils.writeToFile(fileObject, file);
               this.ctx.printDebug("downloaded the file " + fileToDownload + " successfully");
            } else if (file.isFile()) {
               File tmpFile = File.createTempFile("nmservice", "tmp");
               tmpFile.deleteOnExit();
               FileUtils.writeToFile(fileObject, tmpFile);
               long oldFileCRC = FileUtils.computeCRC(file);
               long newFileCRC = FileUtils.computeCRC(tmpFile);
               if (oldFileCRC != newFileCRC) {
                  FileUtils.copy(tmpFile, file);
                  this.ctx.printDebug("downloaded the file " + fileToDownload + " successfully");
               }
            }
         } else {
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(fileObject));
            Object retObject = in.readObject();
            if (retObject instanceof File[]) {
               file.mkdir();
               File[] fis = (File[])((File[])retObject);

               for(int i = 0; i < fis.length; ++i) {
                  this.downloadFile(domainDir, fileToDownload + "/" + fis[i].getName());
               }
            }
         }

         return;
      } catch (StreamCorruptedException var13) {
      } catch (Throwable var14) {
         this.ctx.throwWLSTException("Problem enrolling the machine.", var14);
      }

   }

   public void nmLog(Writer outWriter) throws ScriptException, IOException {
      this.ctx.commandType = "nmLog";
      if (!this.connectedToNM) {
         this.ctx.println(txtFmt.getNotConnectedNM());
      } else {
         if (outWriter == null) {
            outWriter = this.getWriter();
         }

         this.nmc = this.getNewNMClient((String)null, ServerType.WebLogic, this.domainDir, this.domainName);
         this.nmc.getNMLog(outWriter);
         outWriter.flush();
      }
   }

   public void nmGenBootStartupProps(String serverName) throws ScriptException {
      this.ctx.commandType = "nmGenBootStartupProps";
      if (serverName == null || serverName.equals("")) {
         this.ctx.throwWLSTException(txtFmt.getNullOrEmptyServerName());
      }

      ServerMBean smb = this.ctx.runtimeServiceMBean.getDomainConfiguration().lookupServer(serverName);
      if (smb == null) {
         this.ctx.throwWLSTException(txtFmt.getNoServerMBean(serverName));
      }

      String rootDir = smb.getServerStart().getRootDirectory();
      File serversDir = new File(rootDir, "servers");
      File dataDir = new File(serversDir, serverName + "/" + "data");
      File nmDir = new File(dataDir, "nodemanager");
      nmDir.mkdirs();
      Properties props = smb.getServerStart().getBootProperties();

      File f;
      FileOutputStream fos;
      try {
         f = new File(nmDir, "boot.properties");
         fos = new FileOutputStream(f);
         props.save(fos, (String)null);
         fos.flush();
         fos.close();
         this.ctx.println(txtFmt.getGeneratedBootProperties(f.getAbsolutePath()));
      } catch (IOException var11) {
         this.ctx.throwWLSTException(txtFmt.getErrorSavingBootProperties(), var11);
      }

      props = smb.getServerStart().getStartupProperties();

      try {
         f = new File(nmDir, "startup.properties");
         fos = new FileOutputStream(f);
         props.save(fos, (String)null);
         fos.flush();
         fos.close();
         this.ctx.println(txtFmt.getGeneratedStartupProperties(f.getAbsolutePath()));
      } catch (IOException var10) {
         this.ctx.throwWLSTException(txtFmt.getErrorSavingStartupProperties(), var10);
      }

   }

   private Writer getWriter() {
      Writer outWriter = null;
      Object stdOutputMedium = this.ctx.getStandardOutputMedium();
      if (stdOutputMedium == null) {
         outWriter = new PrintWriter(System.out);
      } else if (stdOutputMedium instanceof OutputStream) {
         outWriter = new PrintWriter((OutputStream)stdOutputMedium);
      } else if (stdOutputMedium instanceof Writer) {
         outWriter = (Writer)stdOutputMedium;
      }

      return (Writer)outWriter;
   }

   private ServerType getServerType(String serverType) throws ScriptException {
      if (serverType == null) {
         return ServerType.WebLogic;
      } else {
         try {
            return ServerType.valueOf(serverType);
         } catch (IllegalArgumentException var3) {
            this.ctx.throwWLSTException(txtFmt.getUnsupportedServerType(serverType));
            return ServerType.WebLogic;
         }
      }
   }

   private NMClient getNewNMClient(String serverName, ServerType serverType, String domainDir, String domain_name) throws ScriptException {
      if (this.nmc != null && !this.nmType.equalsIgnoreCase("ssh")) {
         if (serverName != null) {
            this.nmc.setServerName(serverName);
         }

         if (serverType != null) {
            this.nmc.setServerType(serverType);
         }

         return this.nmc;
      } else {
         try {
            this.nmc = NMClient.getInstance(this.nmType);
            this.nmc.setHost(this.nmHost);
            this.nmc.setPort(this.nmPort);
            this.nmc.setDomainName(domain_name);
            this.nmc.setNMUser(this.nmCredential.getUsername());
            this.nmc.setNMPass(new String(this.nmCredential.getPassword()));
            if (domainDir != null) {
               this.nmc.setDomainDir(domainDir);
            }

            if (serverName != null) {
               this.nmc.setServerName(serverName);
            }

            if (serverType != null) {
               this.nmc.setServerType(serverType);
            }

            if (this.ctx.getBoolean(this.verbose)) {
               this.nmc.setVerbose(true);
            }
         } catch (Exception var6) {
            this.ctx.throwWLSTException(txtFmt.getErrorGettingNMClient(), var6);
         }

         return this.nmc;
      }
   }

   byte[] getNMUser() {
      return this.nmCredential.isUsernameSet() ? this.nmCredential.getUsername().getBytes() : null;
   }

   byte[] getNMPwd() {
      return this.nmCredential.isPasswordSet() ? (new String(this.nmCredential.getPassword())).getBytes() : null;
   }
}
