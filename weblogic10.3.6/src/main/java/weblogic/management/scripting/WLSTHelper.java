package weblogic.management.scripting;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.AttributeChangeNotificationFilter;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.NotificationFilter;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.ConfigurationException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.python.core.ArgParser;
import org.python.core.PyDictionary;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyString;
import weblogic.management.MBeanHome;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.management.mbeanservers.MBeanTypeService;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.edit.EditServiceMBean;
import weblogic.management.mbeanservers.runtime.RuntimeServiceMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.scripting.utils.WLSTMsgTextFormatter;
import weblogic.management.scripting.utils.WLSTUtil;
import weblogic.rmi.extensions.RemoteHelper;
import weblogic.security.UserConfigFileManager;
import weblogic.security.UsernameAndPassword;
import weblogic.security.internal.BootProperties;

class WLSTHelper {
   WLScriptContext wlCtx = null;
   String connectionResult = "";
   ServerRuntimeMBean serverRuntime = null;
   boolean isIIOP = false;
   boolean usingInSecureProtocol = true;
   private final String SERVER_NAME = "adminServerName";
   private static String NONE = "None";
   private WLSTMsgTextFormatter txtFmt;
   private boolean addedCompatChangeListener = false;
   private boolean addedEditChangeListener = false;

   public WLSTHelper(WLScriptContext ctx) {
      this.wlCtx = ctx;
      this.txtFmt = ctx.getWLSTMsgFormatter();
   }

   public synchronized void connect(PyObject[] args, String[] kw) throws ScriptException {
      this.wlCtx.commandType = "connect";

      try {
         ArgParser ap = new ArgParser("connect", args, kw, "username", "password", "url");
         String username = null;
         if (ap.getString(0) != null) {
            username = ap.getString(0);
         }

         char[] pwd = null;
         if (ap.getString(1) != null) {
            pwd = ap.getString(1).toCharArray();
         }

         boolean usingBootProps = false;
         if (username.length() == 0 && pwd.length == 0) {
            usingBootProps = true;
            this.wlCtx.printDebug("Will check if userConfig and userKeyFile should be used to connect to the server");
         }

         String url = ap.getString(2);
         PyDictionary objs = null;

         try {
            objs = (PyDictionary)ap.getPyObject(4);
         } catch (PyException var17) {
            objs = (PyDictionary)ap.getPyObject(3);
         }

         String userConfig = null;
         String userKey = null;
         String adminServerName = null;
         this.wlCtx.commandType = "connect";
         this.wlCtx.connectTimeout = 0;
         WLScriptContext var10002 = this.wlCtx;
         PyString pyConnectTimeout = new PyString("timeout");
         String username2;
         if (objs.has_key(pyConnectTimeout)) {
            username2 = objs.get(pyConnectTimeout).toString();
            this.wlCtx.printDebug("The connect timeout is " + username2);
            if (username2 != null && !username2.equals(NONE)) {
               this.wlCtx.connectTimeout = Integer.parseInt(username2);
            }
         }

         PyString pwd2;
         if (usingBootProps) {
            var10002 = this.wlCtx;
            PyString pyuserConfigFile = new PyString("userConfigFile");
            var10002 = this.wlCtx;
            pwd2 = new PyString("userKeyFile");
            PyString pyserverName = new PyString("adminServerName");
            if (objs.has_key(pyuserConfigFile)) {
               userConfig = objs.get(pyuserConfigFile).toString();
               this.wlCtx.printDebug("The userConfig file location is " + userConfig);
            }

            if (objs.has_key(pwd2)) {
               userKey = objs.get(pwd2).toString();
               this.wlCtx.printDebug("The user key location is " + userKey);
            }

            UsernameAndPassword UAndP = UserConfigFileManager.getUsernameAndPassword(userConfig, userKey, "weblogic.management");
            if (UAndP != null && UAndP.isUsernameSet() && UAndP.isPasswordSet()) {
               username = UAndP.getUsername();
               this.wlCtx.printDebug("The username is " + new String(username));
               pwd = UAndP.getPassword();
               this.wlCtx.printDebug("The password is ******");
            }

            if (username.length() == 0 && pwd.length == 0) {
               adminServerName = objs.get(pyserverName).toString();
               if (adminServerName.equals(NONE)) {
                  adminServerName = null;
               }
            }
         }

         if (username.length() == 0 && pwd.length == 0 && this.wlCtx.isExecutingFromDomainDir()) {
            this.wlCtx.printDebug("wlst is invoked from a domain directory, hence we will try to load username and password from boot.properties file");
            HashMap uap = this.getUsernameAndPassword(adminServerName);
            if (uap != null) {
               if (uap.get("username") != null) {
                  username = (String)uap.get("username");
               }

               if (uap.get("password") != null) {
                  pwd = ((String)uap.get("password")).toCharArray();
               }

               this.wlCtx.printDebug("loaded username and pwd from the boot.properties file");
            }
         }

         if (username.length() == 0 && pwd.length == 0) {
            username2 = this.wlCtx.promptValue(this.txtFmt.getEnterUsername(), true);
            pwd2 = null;
            String pwd2 = this.wlCtx.promptValue(this.txtFmt.getEnterPassword(), false);
            if (username2.length() == 0) {
               this.wlCtx.throwWLSTException(this.txtFmt.getEmptyUsername());
            } else {
               username = username2;
            }

            if (pwd2.trim().length() != 0 && !this.hasUnicodeCharacters(pwd2)) {
               pwd = pwd2.toCharArray();
            } else {
               this.wlCtx.throwWLSTException(this.txtFmt.getEmptyPassword());
            }

            if (url.length() == 0) {
               var10002 = this.wlCtx;
               url = this.wlCtx.promptValue(this.txtFmt.getEnterURL("t3://localhost:7001"), true);
            }
         }

         this.wlCtx.username_bytes = username.getBytes();
         this.wlCtx.password_bytes = (new String(pwd)).getBytes();
         if (url.length() == 0) {
            WLScriptContext var10000 = this.wlCtx;
            url = "t3://localhost:7001";
            this.wlCtx.printDebug(this.txtFmt.getUseDefaultURL(url));
         }

         url = this.checkUrlSanity(url);
         if (this.isIIOP) {
         }

         this.wlCtx.url = url;
         this.initConnections();
         this.wlCtx.newBrowseHandler.configRuntimeNavigatedBefore = true;
         this.wlCtx.newBrowseHandler.configRuntime();
      } catch (Throwable var18) {
         if (var18 instanceof ScriptException) {
            throw (ScriptException)var18;
         }

         if (var18 instanceof IllegalArgumentException) {
            this.wlCtx.throwWLSTException("Authentication Failed.", var18);
         } else {
            this.wlCtx.throwWLSTException("Error connecting to the server", var18);
         }
      }

   }

   public synchronized void disconnect() {
      this.wlCtx.printDebug("OnDisconnect event occurred");
   }

   private boolean hasUnicodeCharacters(String s) {
      char[] cs = s.toCharArray();

      for(int i = 0; i < cs.length; ++i) {
         if (Character.isUnicodeIdentifierPart(cs[i]) && cs.length == 4) {
            this.wlCtx.printDebug("Found a Unicode character in the string specified, hence this string will be considered empty");
            return true;
         }
      }

      return false;
   }

   private HashMap getUsernameAndPassword(String adminServerName) {
      HashMap map = null;
      File bootProps;
      if (adminServerName == null) {
         bootProps = new File("./boot.properties");
         if (bootProps.exists()) {
            map = this.loadUsernameAndPasswordFromBootProperties(bootProps);
            return map;
         } else {
            File serverDir = new File("./servers/myserver/security/boot.properties");
            if (serverDir.exists()) {
               map = this.loadUsernameAndPasswordFromBootProperties(serverDir);
               return map;
            } else {
               return null;
            }
         }
      } else {
         bootProps = new File("./servers/" + adminServerName + "/security/boot.properties");
         if (bootProps.exists()) {
            map = this.loadUsernameAndPasswordFromBootProperties(bootProps);
            return map;
         } else {
            return null;
         }
      }
   }

   private HashMap loadUsernameAndPasswordFromBootProperties(File bootProps) {
      return loadUsernameAndPasswordFromBootProperties(bootProps, ".");
   }

   public static HashMap loadUsernameAndPasswordFromBootProperties(File bootProps, String domainDir) {
      File saltFile = new File(domainDir + File.separator + "security" + File.separator + "SerializedSystemIni.dat");
      if (saltFile.exists()) {
         BootProperties.load(bootProps.getAbsolutePath(), false);
         BootProperties props = BootProperties.getBootProperties();
         if (props.getOneClient().length() == 0) {
            return null;
         } else {
            HashMap map = new HashMap();
            map.put("username", props.getOneClient());
            map.put("password", props.getTwoClient());
            BootProperties.unload(false);
            return map;
         }
      } else {
         return null;
      }
   }

   private void initConnections() throws Throwable {
      String msg = this.txtFmt.getConnectingToURL(this.wlCtx.url, new String(this.wlCtx.username_bytes));
      this.wlCtx.println(msg);
      this.initDeprecatedConnection(new String(this.wlCtx.username_bytes), new String(this.wlCtx.password_bytes), this.wlCtx.url);
      this.initRuntimeServerConnection();
      if (this.wlCtx.isAdminServer) {
         this.initDomainRuntimeServerConnection();
         this.initEditServerConnection();
      }

      this.initJsr77ServerConnection();
      this.determineServerInfo(this.serverRuntime);
      this.verifyServerConections();
      this.setLoggingLevel();
   }

   public void addEditChangeListener() throws Throwable {
      if (this.wlCtx.isAdminServer && !this.isIIOP && !this.addedEditChangeListener) {
         this.addedEditChangeListener = true;
         this.wlCtx.printDebug("Adding the edit change listener ...");
         ChangeListener clForEditTree = new ChangeListener();
         ObjectName on = new ObjectName("JMImplementation:type=MBeanServerDelegate");
         if (this.wlCtx.getMBSConnection("ConfigEdit") != null) {
            this.wlCtx.getMBSConnection("ConfigEdit").addNotificationListener(on, clForEditTree, (NotificationFilter)null, (Object)null);
         }

         this.wlCtx.printDebug("Done adding the edit change listener ...");
      }
   }

   public void addCompatChangeListener() throws Throwable {
      if (this.wlCtx.isAdminServer && !this.isIIOP && !this.addedCompatChangeListener) {
         this.addedCompatChangeListener = true;
         this.wlCtx.printDebug("Adding the compatibility change listener ...");
         ChangeListener clForDepTree = new ChangeListener();
         ObjectName on = new ObjectName("JMImplementation:type=MBeanServerDelegate");
         if (this.wlCtx.getMBSConnection("Domain") != null) {
            this.wlCtx.getMBSConnection("Domain").addNotificationListener(on, clForDepTree, (NotificationFilter)null, (Object)null);
         }

         this.wlCtx.printDebug("Done adding the compatibility change listener ...");
      }
   }

   private void verifyServerConections() throws Throwable {
      this.wlCtx.println(this.txtFmt.getConnected(this.connectionResult));
      if (this.usingInSecureProtocol) {
         this.wlCtx.println(this.txtFmt.getInsecureProtocol());
      }

   }

   private String checkUrlSanity(String url) {
      if (!url.startsWith("t3s") && !url.startsWith("https")) {
         if (url.startsWith("iiops")) {
            this.usingInSecureProtocol = false;
         }
      } else {
         this.usingInSecureProtocol = false;
      }

      if (!url.startsWith("t3") && !url.startsWith("http")) {
         if (url.startsWith("iiop")) {
            this.isIIOP = true;
            return url;
         } else {
            url = "t3://" + url;
            return url;
         }
      } else {
         return url;
      }
   }

   void dumpAllMBeans(MBeanServerConnection connection, String server) throws Throwable {
      System.out.println("\n\n############ DUMPING ALL MBEANS FOR " + server + " ############\n\n");
      ObjectName pattern = new ObjectName("*:*");
      Set set = connection.queryNames(pattern, (QueryExp)null);
      System.out.println("There are " + set.size() + " MBeans in this MBeanServer");
      Iterator i = set.iterator();

      while(i.hasNext()) {
         System.out.println(i.next());
      }

      System.out.println("\n\n############ DONE DUMPING ALL MBEANS FOR " + server + " ############\n\n");
   }

   void initRuntimeServerConnection() throws Throwable {
      this.wlCtx.printDebug("Initing the RuntimeServer Connection");

      try {
         WLScriptContext var10002 = this.wlCtx;
         this.wlCtx.runtimeMSC = this.lookupMBeanServerConnection("weblogic.management.mbeanservers.runtime");
         ObjectName rserviceON = new ObjectName(RuntimeServiceMBean.OBJECT_NAME);
         this.wlCtx.runtimeServiceMBean = (RuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(this.wlCtx.runtimeMSC, rserviceON);
         this.wlCtx.runtimeDomainMBean = this.wlCtx.runtimeServiceMBean.getDomainConfiguration();
         this.wlCtx.runtimeServerRuntimeMBean = this.wlCtx.runtimeServiceMBean.getServerRuntime();
         this.serverRuntime = this.wlCtx.runtimeServerRuntimeMBean;
         ObjectName typeON = new ObjectName(MBeanTypeService.OBJECT_NAME);
         this.wlCtx.mbeanTypeService = (MBeanTypeService)MBeanServerInvocationHandler.newProxyInstance(this.wlCtx.runtimeMSC, typeON);
         this.wlCtx.printDebug("Got the RuntimeServiceMBean, the Domain Configuration and the ServerRuntime MBean");
         this.wlCtx.printDebug("Initialized the Runtime Server information");
         this.wlCtx.isRuntimeServerEnabled = true;
         this.wlCtx.connected = "true";
         this.wlCtx.atDomainLevel = true;
         this.wlCtx.serverName = this.serverRuntime.getName();
         this.wlCtx.domainName = this.wlCtx.runtimeDomainMBean.getName();
         WLSTUtil.disconnected = false;
      } catch (Exception var3) {
         this.wlCtx.println(this.txtFmt.getRuntimeMBSNotEnabled());
         this.wlCtx.stackTrace = var3;
         this.wlCtx.throwWLSTException(this.txtFmt.getFailedToConnect(), var3);
      }

   }

   void initDomainRuntimeServerConnection() throws Throwable {
      try {
         WLScriptContext var10002 = this.wlCtx;
         this.wlCtx.domainRTMSC = this.lookupMBeanServerConnection("weblogic.management.mbeanservers.domainruntime");
         ObjectName drsOn = new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME);
         this.wlCtx.domainRuntimeServiceMBean = (DomainRuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(this.wlCtx.domainRTMSC, drsOn);
         this.wlCtx.configDomainRuntimeDRMBean = this.wlCtx.domainRuntimeServiceMBean.getDomainConfiguration();
         this.wlCtx.runtimeDomainRuntimeDRMBean = this.wlCtx.domainRuntimeServiceMBean.getDomainRuntime();
         this.wlCtx.isDomainRuntimeServerEnabled = true;
         if (this.serverRuntime == null) {
            this.wlCtx.printDebug("Only the DomainRuntimeServer is enabled.");
         }

         this.wlCtx.printDebug("Initialized the Domain Runtime connection");
      } catch (Exception var2) {
         var2.printStackTrace();
         this.wlCtx.println(this.txtFmt.getDomainRuntimeMBSNotEnabled());
         this.wlCtx.stackTrace = var2;
         this.wlCtx.throwWLSTException(this.txtFmt.getFailedToConnect(), var2);
      }

   }

   void initEditServerConnection() throws Throwable {
      this.wlCtx.printDebug("Initing the EditServer Connection");

      try {
         WLScriptContext var10002 = this.wlCtx;
         this.wlCtx.editMSC = this.lookupMBeanServerConnection("weblogic.management.mbeanservers.edit");
         ObjectName editServiceON = new ObjectName(EditServiceMBean.OBJECT_NAME);
         this.wlCtx.editServiceMBean = (EditServiceMBean)MBeanServerInvocationHandler.newProxyInstance(this.wlCtx.editMSC, editServiceON);
         this.wlCtx.editDomainMBean = this.wlCtx.editServiceMBean.getDomainConfiguration();
         this.wlCtx.configurationManager = this.wlCtx.editServiceMBean.getConfigurationManager();
         this.wlCtx.printDebug("Got the EditServiceMBean, the Domain Configuration and the Configuration manager MBean");
         this.wlCtx.printDebug("Initialized the Edit Server information");
         this.wlCtx.isEditServerEnabled = true;
      } catch (Exception var2) {
         this.wlCtx.print(this.txtFmt.getEditMBSNotEnabled());
         this.wlCtx.stackTrace = var2;
      }
   }

   private void addEditListener() {
      try {
         ObjectName con = this.wlCtx.getObjectName(this.wlCtx.configurationManager);
         EditListener el = new EditListener();
         AttributeChangeNotificationFilter acf = new AttributeChangeNotificationFilter();
         acf.enableAttribute("CurrentEditor");
         this.wlCtx.editMSC.addNotificationListener(con, el, (NotificationFilter)null, (Object)null);
         EditListener _el = new EditListener();
         this.wlCtx.runtimeMSC.addNotificationListener(con, _el, (NotificationFilter)null, (Object)null);
      } catch (InstanceNotFoundException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
      }

   }

   void initJsr77ServerConnection() throws Throwable {
   }

   private void populateInitialContext() throws ScriptException {
      Hashtable h = new Hashtable();
      h.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      h.put("java.naming.provider.url", this.wlCtx.url);
      h.put("java.naming.security.principal", new String(this.wlCtx.username_bytes));
      h.put("java.naming.security.credentials", new String(this.wlCtx.password_bytes));

      try {
         this.wlCtx.iContext = new InitialContext(h);
      } catch (CommunicationException var5) {
         this.wlCtx.throwWLSTException(this.getRightErrorMessage(var5), var5);
      } catch (NamingException var6) {
         if (var6 instanceof CommunicationException) {
            Throwable th = var6.getRootCause();
            this.wlCtx.errorMsg = this.getRightErrorMessage(th);
         } else if (var6 instanceof AuthenticationException) {
            AuthenticationException ae = (AuthenticationException)var6;
            SecurityException se = (SecurityException)ae.getRootCause();
            if (se.getMessage() != null) {
               this.wlCtx.errorMsg = se.getMessage();
            } else {
               this.wlCtx.errorMsg = "The username or password supplied are incorrect. Please try again";
            }
         } else if (var6 instanceof ConfigurationException) {
            this.wlCtx.errorMsg = "The url specified is malformed. Please correct it.";
         }

         this.wlCtx.throwWLSTException(this.wlCtx.errorMsg, var6);
      }

   }

   private String getRightErrorMessage(Throwable th) {
      String result = "";
      if (th.getMessage() != null && th.getMessage().indexOf("Tunneling result unspecified - is the HTTP server at host") != -1 && this.wlCtx.url.startsWith("http")) {
         result = "Cannot connect via http. Connecting through 'http' will require you to enable 'TunnellingEnabled' attribute on the ServerMBean to 'true'";
      } else if (th.getMessage() != null && th.getMessage().indexOf("javax.net.ssl.SSLKeyException") != -1) {
         result = "Cannot connect via SSL.To connect via SSL, use the following two System properties as shown, java -Dweblogic.security.SSL.ignoreHostnameVerification=true -Dweblogic.security.TrustKeyStore=DemoTrust weblogic.WLST";
      } else {
         result = "Error getting the initial context. There is no server running at " + this.wlCtx.url;
      }

      return result;
   }

   private void initDeprecatedConnection(String username, String pwd, String url) throws Throwable {
      try {
         this.populateInitialContext();
         this.wlCtx.home = (MBeanHome)this.wlCtx.iContext.lookup("weblogic.management.home.localhome");
         this.wlCtx.mbs = this.wlCtx.home.getMBeanServer();
         this.wlCtx.compatMBS = (MBeanServer)this.wlCtx.mbs;
         Set set = this.wlCtx.home.getMBeansByType("ServerRuntime");
         this.serverRuntime = (ServerRuntimeMBean)set.iterator().next();
         this.wlCtx.serverName = this.serverRuntime.getName();
         this.wlCtx.username_bytes = username.getBytes();
         this.wlCtx.password_bytes = pwd.getBytes();
         this.wlCtx.setHome(this.wlCtx.home);
         if (this.serverRuntime.getObjectName() == null) {
            this.wlCtx.domainName = this.wlCtx.home.getActiveDomain().getName();
         } else {
            this.wlCtx.domainName = this.serverRuntime.getObjectName().getDomain();
         }

         this.wlCtx.version = this.serverRuntime.getWeblogicVersion();
         if (!this.isIIOP) {
            this.addPeerMonitor(this.wlCtx.serverName, this.wlCtx.home, 9);
         }

         ObjectName don = new ObjectName(this.wlCtx.domainName + ":Name=" + this.wlCtx.domainName + ",Type=DomainRuntime,Location=" + this.wlCtx.serverName);

         try {
            this.wlCtx.home.getProxy(don);
         } catch (Exception var7) {
            this.wlCtx.printDebug("Connecting to a server that is running in MSI mode");
            this.wlCtx.isAdminServer = false;
         }

         WLScriptContext var10001;
         if (this.wlCtx.isAdminServer) {
            this.wlCtx.wlcmo = this.wlCtx.home.getAdminMBean(this.wlCtx.domainName, "Domain");
            this.wlCtx.compatDomainMBean = (DomainMBean)this.wlCtx.wlcmo;
            this.wlCtx.compatDomainRuntimeMBean = null;
            this.wlCtx.adminHome = (MBeanHome)this.wlCtx.iContext.lookup("weblogic.management.adminhome");
            this.wlCtx.printDebug("The domain type is going to be " + this.wlCtx.domainType);
            var10001 = this.wlCtx;
            this.wlCtx.domainType = "Domain";
         } else {
            this.wlCtx.wlcmo = this.wlCtx.home.getConfigurationMBean(this.wlCtx.domainName, "DomainConfig");
            this.wlCtx.compatDomainMBean = (DomainMBean)this.wlCtx.wlcmo;
            this.wlCtx.compatServerRuntimeMBean = (ServerRuntimeMBean)this.wlCtx.home.getMBeansByType("ServerRuntime").iterator().next();
            this.wlCtx.printDebug("The domain type is going to be " + this.wlCtx.domainType);
            var10001 = this.wlCtx;
            this.wlCtx.domainType = "DomainConfig";
         }

         this.wlCtx.mbs = this.wlCtx.home.getMBeanServer();
         this.wlCtx.connected = "true";
         this.wlCtx.atDomainLevel = true;
         this.wlCtx.beans.add(this.wlCtx.wlcmo);
         WLSTUtil.disconnected = false;
         this.wlCtx.isCompatabilityServerEnabled = true;
      } catch (Exception var8) {
         if (var8 instanceof ScriptException) {
            throw var8;
         } else {
            if (this.wlCtx.debug) {
               var8.printStackTrace();
            }

            this.wlCtx.print(this.txtFmt.getCompatibilityMBSNotEnabled());
            this.wlCtx.isCompatabilityServerEnabled = false;
            this.wlCtx.stackTrace = var8;
         }
      }
   }

   private void determineServerInfo(ServerRuntimeMBean srbean) {
      if (this.wlCtx.isAdminServer) {
         if (this.wlCtx.domainName == null) {
            this.wlCtx.domainName = this.serverRuntime.getObjectName().getDomain();
         }

         this.connectionResult = "to Admin Server '" + this.wlCtx.serverName + "' that belongs to domain '" + this.wlCtx.domainName + "'.";
      } else {
         if (this.wlCtx.domainName == null) {
            this.wlCtx.domainName = this.serverRuntime.getObjectName().getDomain();
         }

         this.connectionResult = "to managed Server '" + this.wlCtx.serverName + "' that belongs to domain '" + this.wlCtx.domainName + "'.";
         this.wlCtx.printDebug("Checking if this server belongs to any cluster");
      }

   }

   private MBeanServerConnection lookupMBeanServerConnection(String jndiName) throws Exception {
      String host = this.wlCtx.getListenAddress(this.wlCtx.url);
      int port = Integer.parseInt(this.wlCtx.getListenPort(this.wlCtx.url));
      String protocol = this.wlCtx.getProtocol(this.wlCtx.url);
      StringBuilder var10005 = new StringBuilder();
      WLScriptContext var10006 = this.wlCtx;
      JMXServiceURL serviceURL = new JMXServiceURL(protocol, host, port, var10005.append("/jndi/").append(jndiName).toString());
      Hashtable h = new Hashtable();
      h.put("java.naming.security.principal", new String(this.wlCtx.username_bytes));
      h.put("java.naming.security.credentials", new String(this.wlCtx.password_bytes));
      h.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
      if (this.wlCtx.connectTimeout > 0) {
         h.put("jmx.remote.x.request.waiting.timeout", new Long((long)this.wlCtx.connectTimeout));
      } else {
         h.put("jmx.remote.x.request.waiting.timeout", new Long(0L));
      }

      JMXConnector connector = JMXConnectorFactory.connect(serviceURL, h);
      this.wlCtx.jmxConnectors.add(connector);
      return connector.getMBeanServerConnection();
   }

   private void addPeerMonitor(String serverName, Object remoteRef, int ver) {
      try {
         this.wlCtx.ep = RemoteHelper.getEndPoint(remoteRef);
         this.wlCtx.msMonitor = new ManagedServerMonitor(serverName, this.wlCtx.ep, this.wlCtx);
         this.wlCtx.msMonitor.initialize(serverName, this.wlCtx.ep, this.wlCtx);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void setLoggingLevel() {
      WLScriptContext var10000 = this.wlCtx;
      Iterator logList = WLScriptContext.getLoggersList().iterator();

      while(logList.hasNext()) {
         String log = (String)logList.next();
         Logger.getLogger(log).setLevel(Level.OFF);
      }

   }
}
