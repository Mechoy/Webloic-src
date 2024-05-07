package weblogic.deploy.api.spi.deploy.internal;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.enterprise.deploy.shared.ActionType;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.shared.StateType;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.OperationUnsupportedException;
import javax.enterprise.deploy.spi.exceptions.TargetException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.internal.utils.DeployerHelperException;
import weblogic.deploy.api.internal.utils.InstallDir;
import weblogic.deploy.api.internal.utils.JMXDeployerHelper;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.shared.WebLogicTargetType;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.deploy.ServerConnection;
import weblogic.deploy.api.spi.deploy.TargetImpl;
import weblogic.deploy.api.spi.deploy.TargetModuleIDImpl;
import weblogic.deploy.api.spi.deploy.mbeans.ModuleCache;
import weblogic.deploy.api.spi.deploy.mbeans.TargetCache;
import weblogic.deploy.api.spi.exceptions.ServerConnectionException;
import weblogic.deploy.api.spi.status.ProgressObjectImpl;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.jndi.Environment;
import weblogic.management.DeploymentNotification;
import weblogic.management.DomainDir;
import weblogic.management.RemoteNotificationListener;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.mbeanservers.edit.EditServiceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.remote.common.WLSJMXConnector;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.FileUtils;
import weblogic.utils.StackTraceUtils;

public class ServerConnectionImpl implements ServerConnection, Serializable {
   private static final long serialVersionUID = 1L;
   private static final boolean debug = Debug.isDebug("deploy");
   private static final boolean ddebug = Debug.isDebug("internal");
   private static final String IIOP = "iiop";
   private static final String POLLER_NAME = "J2EE-Deployment-task-poller";
   private static final int RUNTIME = 2;
   private static final int DOMAIN_RUNTIME = 3;
   private boolean isRemote = false;
   private transient String adminUrl;
   private transient URI adminURI = null;
   private transient String auth1 = null;
   private transient String auth2 = null;
   private transient MBeanServerConnection mbs = null;
   private transient MBeanServerConnection runtimeMBS = null;
   private transient JMXDeployerHelper helper = null;
   private transient Map listeners = Collections.synchronizedMap(new HashMap());
   private transient Thread poller = null;
   private boolean forceStop = false;
   private transient String domain = null;
   private transient WebLogicDeploymentManager dm;
   private transient DeployerRuntimeMBean deployer;
   private transient TargetCache targetCache;
   private transient ModuleCache moduleCache;
   private Context ctx;
   private transient JMXConnector jmx = null;
   private transient JMXConnector runtimeJmx = null;
   private transient JMXConnector editJmx = null;
   private File delApp = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private ConfigurationManagerMBean configMgr;
   private transient DomainManager domainManager;
   private boolean closed = false;

   protected void finalize() throws Throwable {
      if (!this.closed) {
         this.close(true);
      }

   }

   public void init(URI var1, String var2, String var3, WebLogicDeploymentManager var4) throws ServerConnectionException {
      this.dm = var4;
      this.adminURI = var1;
      this.auth1 = var2;
      this.auth2 = var3;

      try {
         this.ctx = this.getEnvironment(var1, var2, var3);
      } catch (NamingException var7) {
         throw new ServerConnectionException(var7.getMessage(), var7);
      } catch (URISyntaxException var8) {
         throw new ServerConnectionException(var8.getMessage(), var8);
      }

      this.mbs = this.getMBeanServerForType(3);
      this.runtimeMBS = this.getMBeanServerForType(2);

      try {
         this.helper = new JMXDeployerHelper(this.jmx);
         this.helper.setConfigMgr(this.configMgr);
      } catch (DeployerHelperException var6) {
         throw new ServerConnectionException(var6.getMessage(), var6);
      }

      this.initialize();
      if (!var4.isLocal()) {
         this.setRemote();
      }

      if (debug) {
         Debug.say("Initializing ServerConnection : " + this);
      }

   }

   private void initialize() throws ServerConnectionException {
      this.domainManager = new DomainManager(this);
      DomainMBean var1 = this.domainManager.getDomain();
      this.domain = var1.getName();
      this.dm.setDomain(this.domain);
      if (debug) {
         Debug.say("Connected to WLS domain: " + this.domain);
      }

      try {
         this.deployer = this.helper.getDeployer();
      } catch (Throwable var3) {
         throw new ServerConnectionException(SPIDeployerLogger.connectionError(), var3);
      }

      if (this.deployer == null) {
         throw new ServerConnectionException(SPIDeployerLogger.connectionError());
      }
   }

   private void initCaches(DomainMBean var1) {
      if (this.targetCache != null) {
         this.targetCache.reset();
      }

      this.targetCache = new TargetCache(var1, this.dm);
      if (this.moduleCache != null) {
         this.moduleCache.reset();
      }

      this.moduleCache = new ModuleCache(var1, this.dm);
   }

   private MBeanServerConnection getMBeanServerForType(int var1) throws ServerConnectionException {
      if (this.adminURI == null) {
         throw new ServerConnectionException("Admin URI cannot be null");
      } else {
         return this.getMBeanServer(this.adminURI, this.auth1, this.auth2, var1);
      }
   }

   private MBeanServerConnection getMBeanServer(URI var1, String var2, String var3, int var4) throws ServerConnectionException {
      String var5 = "";
      if (var4 == 3) {
         var5 = "weblogic.management.mbeanservers.domainruntime";
      } else {
         var5 = "weblogic.management.mbeanservers.runtime";
      }

      String var6 = "localhost";
      int var7 = 7001;
      String var8 = "t3";

      try {
         Hashtable var9 = new Hashtable();
         if (var2 != null) {
            var9.put("java.naming.security.principal", var2);
            var9.put("java.naming.security.credentials", var3);
         }

         if (var1 == null || var2 == null) {
            RuntimeAccess var10 = ManagementService.getRuntimeAccess(kernelId);
            if (var10 != null && var10.isAdminServer()) {
               return null;
            }

            String var11 = ManagementService.getPropertyService(kernelId).getAdminBinaryURL();
            this.adminUrl = var11;
            if (var11 != null) {
               var1 = new URI(var11);
            }
         }

         if (var1 != null) {
            this.adminUrl = var1.toString();
            var6 = var1.getHost();
            var7 = var1.getPort();
            var8 = this.extractProtocol(var1.getScheme());
         }

         var9.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
         JMXServiceURL var13 = new JMXServiceURL(var8, var6, var7, "/jndi/" + var5);
         if (debug) {
            Debug.say("Connecting to MBeanServer at " + var13.toString());
         }

         JMXConnector var14 = JMXConnectorFactory.connect(var13, var9);
         if (var4 == 3) {
            this.jmx = var14;
            this.getConfigurationManager(var8, var6, var7, var9);
         } else {
            this.runtimeJmx = var14;
         }

         return var14.getMBeanServerConnection();
      } catch (Exception var12) {
         if (debug) {
            var12.printStackTrace();
         }

         throw new ServerConnectionException(SPIDeployerLogger.failedMBeanConnection(var8 + "://" + var6 + ":" + var7, var2, var12.getMessage()), var12);
      }
   }

   private void getConfigurationManager(String var1, String var2, int var3, Hashtable var4) throws IOException, MalformedObjectNameException {
      if (this.configMgr == null) {
         if (this.editJmx == null) {
            JMXServiceURL var5 = new JMXServiceURL(var1, var2, var3, "/jndi/weblogic.management.mbeanservers.edit");
            this.editJmx = JMXConnectorFactory.connect(var5, var4);
         }

         EditServiceMBean var6 = (EditServiceMBean)MBeanServerInvocationHandler.newProxyInstance(this.editJmx, new ObjectName(EditServiceMBean.OBJECT_NAME));
         this.configMgr = var6.getConfigurationManager();
      }
   }

   private String extractProtocol(String var1) {
      return var1 == null ? "t3" : var1;
   }

   private Context getEnvironment(URI var1, String var2, String var3) throws NamingException, URISyntaxException {
      if (this.dm.isAuthenticated()) {
         return null;
      } else {
         if (debug) {
            Debug.say("setting environment");
         }

         if (var2 != null && var1 != null) {
            String var5 = this.getUriAsString(var1);
            if (debug) {
               Debug.say("getting context using " + var5);
            }

            Context var4;
            if (var5.startsWith("iiop")) {
               if (System.getProperty("weblogic.system.iiop.enableClient") == null) {
                  System.setProperty("weblogic.system.iiop.enableClient", "false");
               }

               var4 = this.getIIOPContext(var5, var2, var3);
            } else {
               var4 = this.getContext(var5, var2, var3);
            }

            return var4;
         } else {
            return null;
         }
      }
   }

   private String getUriAsString(URI var1) throws URISyntaxException {
      String var2 = this.extractProtocol(var1.getScheme());
      String var3 = var1.getHost();
      int var4 = var1.getPort();
      return (new URI(var2, (String)null, var3, var4, (String)null, (String)null, (String)null)).toString();
   }

   private Context getIIOPContext(String var1, String var2, String var3) throws NamingException {
      Hashtable var4 = new Hashtable();
      var4.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      var4.put("java.naming.provider.url", var1);
      var4.put("java.naming.security.principal", var2);
      var4.put("java.naming.security.credentials", var3);
      return new InitialContext(var4);
   }

   private Context getContext(String var1, String var2, String var3) throws NamingException {
      Environment var4 = new Environment();
      var4.setProviderUrl(var1);
      var4.setSecurityPrincipal(var2);
      var4.setSecurityCredentials(var3);
      return var4.getInitialContext();
   }

   public synchronized void close(boolean var1) {
      if (debug) {
         Debug.say("Closing DM connection");
      }

      synchronized(this.listeners) {
         Iterator var3 = this.listeners.keySet().iterator();

         while(true) {
            if (!var3.hasNext()) {
               this.listeners.clear();
               this.forceStop = true;
               if (this.poller != null) {
                  this.poller.interrupt();
               }
               break;
            }

            ProgressObjectImpl var4 = (ProgressObjectImpl)var3.next();
            if (var1) {
               try {
                  var4.cancel();
                  var4.setAction(ActionType.CANCEL);
               } catch (OperationUnsupportedException var8) {
               }
            }

            var4.setState(StateType.RELEASED);
            var4.reportEvent();
         }
      }

      if (debug) {
         Debug.say("Unregistered all listeners");
      }

      this.waitForPollerToStop();
      if (this.moduleCache != null) {
         this.moduleCache.close();
      }

      if (this.targetCache != null) {
         this.targetCache.close();
      }

      if (this.domainManager != null) {
         this.domainManager.close();
      }

      this.closeJMX();

      try {
         if (this.ctx != null) {
            this.ctx.close();
         }
      } catch (NamingException var7) {
      }

      this.mbs = null;
      this.closed = true;
   }

   private void closeJMX() {
      try {
         if (this.jmx != null) {
            this.jmx.close();
            if (debug) {
               Debug.say("Closed JMX connection");
            }
         }
      } catch (IOException var4) {
         if (debug) {
            Debug.say("Failed to close JMX connection");
            var4.printStackTrace();
         }
      }

      try {
         if (this.runtimeJmx != null) {
            this.runtimeJmx.close();
            if (debug) {
               Debug.say("Closed Runtime JMX connection");
            }
         }
      } catch (IOException var3) {
         if (debug) {
            Debug.say("Failed to close Runtime JMX connection");
            var3.printStackTrace();
         }
      }

      try {
         if (this.editJmx != null) {
            this.editJmx.close();
            if (debug) {
               Debug.say("Closed Edit JMX connection");
            }
         }
      } catch (IOException var2) {
         if (debug) {
            Debug.say("Failed to close Edit JMX connection");
            var2.printStackTrace();
         }
      }

      this.jmx = null;
      this.runtimeJmx = null;
      this.editJmx = null;
   }

   private void waitForPollerToStop() {
      while(this.poller != null) {
         try {
            Thread.sleep(10L);
         } catch (InterruptedException var2) {
         }
      }

   }

   public void registerListener(ProgressObjectImpl var1) throws ServerConnectionException {
      try {
         String var3 = var1.getTask();
         if (ddebug) {
            Debug.say("Register listener for task " + var3);
         }

         if (var3 == null) {
            var1.setMessage(SPIDeployerLogger.lostTask());
            var1.reportEvent();
         } else {
            DeploymentTaskRuntimeMBean var2 = this.helper.getTaskMBean(var3);
            if (var2 != null) {
               if (ddebug) {
                  Debug.say("Adding app listener for task " + var3);
               }

               AppListener var4 = new AppListener(var1, var2);
               synchronized(this.listeners) {
                  this.listeners.put(var1, var4);
               }
            }

            if (ddebug) {
               Debug.say("Starting poller as nec for  task " + var3);
            }

            synchronized(this.listeners) {
               if (this.poller == null) {
                  this.poller = new TaskPoller("J2EE-Deployment-task-poller");
                  this.poller.start();
               }

            }
         }
      } catch (ServerConnectionException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new ServerConnectionException(SPIDeployerLogger.noSuchApp(var1.getTask()), var11);
      }
   }

   public void deregisterListener(ProgressObjectImpl var1) {
      synchronized(this.listeners) {
         AppListener var3 = (AppListener)this.listeners.get(var1);
         if (var3 != null) {
            if (ddebug) {
               Debug.say("removing listener: " + var3);
            }

            this.listeners.remove(var1);
         }

      }
   }

   public JMXDeployerHelper getHelper() {
      return this.helper;
   }

   public MBeanServerConnection getMBeanServerConnection() {
      return this.mbs;
   }

   public MBeanServerConnection getRuntimeServerConnection() {
      return this.runtimeMBS;
   }

   public void setRemote() {
      if (debug) {
         Debug.say("Running in remote mode");
      }

      this.isRemote = true;
   }

   public boolean isUploadEnabled() {
      return this.isRemote;
   }

   public List getTargets() throws ServerConnectionException {
      List var1 = this.getServers();
      var1.addAll(this.getClusters());
      var1.addAll(this.getHosts());
      var1.addAll(this.getJmsServers());
      var1.addAll(this.getSafAgents());
      return var1;
   }

   public List getServers() throws ServerConnectionException {
      return this.targetCache.getTargets(WebLogicTargetType.SERVER);
   }

   public List getClusters() throws ServerConnectionException {
      return this.targetCache.getTargets(WebLogicTargetType.CLUSTER);
   }

   public List getHosts() throws ServerConnectionException {
      return this.targetCache.getTargets(WebLogicTargetType.VIRTUALHOST);
   }

   public List getJmsServers() throws ServerConnectionException {
      return this.targetCache.getTargets(WebLogicTargetType.JMSSERVER);
   }

   public List getSafAgents() throws ServerConnectionException {
      return this.targetCache.getTargets(WebLogicTargetType.SAFAGENT);
   }

   public boolean isRunning(TargetModuleID var1) throws ServerConnectionException {
      try {
         if (!this.isTargetAlive(var1.getTarget())) {
            return false;
         } else if (((TargetModuleIDImpl)var1).getValue() == WebLogicModuleType.SUBMODULE.getValue()) {
            return true;
         } else {
            AppRuntimeStateRuntimeMBean var2 = this.helper.getAppRuntimeStateMBean();
            if (var2 == null) {
               return false;
            } else {
               String var3;
               if (var1.getParentTargetModuleID() == null) {
                  var3 = var2.getCurrentState(var1.getModuleID(), var1.getTarget().getName());
               } else {
                  var3 = var2.getCurrentState(var1.getParentTargetModuleID().getModuleID(), var1.getModuleID(), var1.getTarget().getName());
               }

               return "STATE_ACTIVE".equals(var3);
            }
         }
      } catch (Exception var4) {
         throw new ServerConnectionException(var4.getMessage(), var4);
      }
   }

   private boolean isTargetAlive(Target var1) {
      List var2 = this.getServersForTarget((TargetImpl)var1);

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         Target var4 = (Target)var2.get(var3);
         if (this.helper.isServerAlive(var4.getName())) {
            return true;
         }
      }

      return false;
   }

   public void validateTargets(Target[] var1) throws TargetException, ServerConnectionException {
      if (debug) {
         Debug.say("Validating targets");
      }

      if (var1 != null && var1.length != 0) {
         List var2 = this.getTargets();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var1[var3] == null) {
               throw new TargetException(SPIDeployerLogger.nullTarget());
            }

            if (!var2.contains(var1[var3])) {
               throw new TargetException(SPIDeployerLogger.noSuchTarget(var1[var3].getName(), var1[var3].getDescription()));
            }
         }

      } else {
         throw new TargetException(SPIDeployerLogger.nullTargetArray());
      }
   }

   public List getModules(ConfigurationMBean var1) throws ServerConnectionException {
      boolean var2 = var1 instanceof AppDeploymentMBean;
      if (!(var1 instanceof BasicDeploymentMBean) && !(var1 instanceof SubDeploymentMBean)) {
         throw new IllegalArgumentException(SPIDeployerLogger.invalidMBean(var1.getObjectName().toString()));
      } else {
         List var3 = this.getModules();
         Iterator var4 = var3.iterator();

         while(true) {
            while(var4.hasNext()) {
               TargetModuleIDImpl var5 = (TargetModuleIDImpl)var4.next();
               if (!var5.getModuleID().equals(var1.getName())) {
                  var4.remove();
               } else if (var2 && var5.getParentTargetModuleID() != null) {
                  var4.remove();
               } else if (!var2 && !var1.getParent().getName().equals(var5.getParentTargetModuleID().getModuleID())) {
                  var4.remove();
               }
            }

            return var3;
         }
      }
   }

   public List getModulesForTarget(ModuleType var1, Target var2) throws TargetException, ServerConnectionException {
      Target[] var3 = new Target[]{var2};
      this.validateTargets(var3);
      List var4 = this.getModules(Arrays.asList(var3).iterator());
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         TargetModuleIDImpl var6 = (TargetModuleIDImpl)var5.next();
         if (debug) {
            Debug.say("checking tmid, " + var6.getModuleID() + ", " + Integer.toString(var1.getValue()));
         }

         if (var6.getValue() != var1.getValue()) {
            var5.remove();
         }

         if (!var6.getTarget().getName().equals(var2.getName())) {
            var5.remove();
         }
      }

      return var4;
   }

   public List getModulesForTargets(ModuleType var1, Target[] var2) throws TargetException, ServerConnectionException {
      this.validateTargets(var2);
      List var3 = this.getModules(Arrays.asList(var2).iterator());
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         TargetModuleIDImpl var5 = (TargetModuleIDImpl)var4.next();
         this.populateWarUrlIfNecessary(var5);
         if (debug) {
            Debug.say("checking tmid, " + var5.getModuleID() + ", " + Integer.toString(var1.getValue()));
         }

         if (var5.getValue() != var1.getValue()) {
            var4.remove();
         }

         boolean var6 = false;

         for(int var7 = 0; var7 < var2.length; ++var7) {
            if (var5.getTarget().getName().equals(var2[var7].getName())) {
               var6 = true;
            }
         }

         if (!var6) {
            var4.remove();
         }
      }

      return var3;
   }

   private void populateWarUrlIfNecessary(TargetModuleIDImpl var1) {
      if (var1 != null) {
         TargetModuleIDImpl var2 = var1;
         String var3 = null;
         if (var1.getValue() == ModuleType.WAR.getValue()) {
            try {
               String var4 = this.getServerName(var2);
               DomainRuntimeServiceMBean var5 = (DomainRuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(this.getMBeanServerConnection(), new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME));
               if (var5 != null) {
                  ServerRuntimeMBean var6 = var5.lookupServerRuntime(var4);
                  if (var6 != null) {
                     String var7 = var6.getURL("http");
                     ApplicationRuntimeMBean var8 = var6.lookupApplicationRuntime(var2.getApplicationName());
                     if (var8 != null) {
                        ComponentRuntimeMBean[] var9 = var8.getComponentRuntimes();
                        if (var9 != null) {
                           ComponentRuntimeMBean[] var10 = var9;
                           int var11 = var9.length;

                           for(int var12 = 0; var12 < var11; ++var12) {
                              ComponentRuntimeMBean var13 = var10[var12];
                              if (var13 instanceof WebAppComponentRuntimeMBean) {
                                 WebAppComponentRuntimeMBean var14 = (WebAppComponentRuntimeMBean)var13;
                                 if (this.isMatchingTmidAndComponentMBean(var2, var14)) {
                                    var3 = var14.getContextRoot();
                                    break;
                                 }
                              }
                           }
                        }
                     }

                     if (var7 != null && var3 != null) {
                        var2.setWebURL(var7 + var3);
                     }
                  }
               }
            } catch (Exception var15) {
               if (debug) {
                  Debug.say("Unable to lookup the WAR's URL: " + var15.getMessage());
               }
            }
         }

      }
   }

   private boolean isMatchingTmidAndComponentMBean(TargetModuleIDImpl var1, WebAppComponentRuntimeMBean var2) {
      String var3;
      String var4;
      if (var1.getParentTargetModuleID() != null) {
         var3 = var1.getModuleID();
         var4 = var2.getModuleId();
         if (var4.charAt(0) == '/') {
            var4 = var4.substring(1);
         }

         return var3.equals(var4);
      } else {
         var3 = var1.getApplicationName();
         var4 = var2.getApplicationIdentifier();
         int var5 = var4.indexOf(35);
         if (var5 != -1) {
            var4 = var4.substring(0, var5);
         }

         return var3.equals(var4);
      }
   }

   public void populateWarUrlInChildren(TargetModuleID var1) {
      if (var1 instanceof TargetModuleIDImpl) {
         TargetModuleIDImpl var2 = (TargetModuleIDImpl)var1;
         if (var2.getValue() == ModuleType.WAR.getValue()) {
            this.populateWarUrlIfNecessary(var2);
         } else {
            TargetModuleID[] var3 = var1.getChildTargetModuleID();
            if (var3 != null) {
               TargetModuleID[] var4 = var3;
               int var5 = var3.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  TargetModuleID var7 = var4[var6];
                  this.populateWarUrlInChildren(var7);
               }
            }
         }

      }
   }

   private String getServerName(TargetModuleIDImpl var1) {
      String var2 = null;
      TargetImpl var3 = (TargetImpl)var1.getTarget();
      if (var3.isCluster()) {
         DomainMBean var4 = this.domainManager.getDomain();
         ClusterMBean[] var5 = var4.getClusters();
         ClusterMBean[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            ClusterMBean var9 = var6[var8];
            if (var9.getName().equals(var3.getName())) {
               ServerMBean[] var10 = var9.getServers();
               if (var10 != null && var10.length > 0) {
                  var2 = var10[0].getName();
                  break;
               }
            }
         }
      } else {
         var2 = var3.getName();
      }

      return var2;
   }

   public List getModules(Iterator var1) throws ServerConnectionException {
      ArrayList var2 = new ArrayList();

      while(var1.hasNext()) {
         Target var3 = (Target)var1.next();
         var2.addAll(this.moduleCache.getModules(var3));
      }

      return var2;
   }

   public List getModules() throws ServerConnectionException {
      Iterator var1 = this.getTargets().iterator();
      return this.getModules(var1);
   }

   public InstallDir upload(InstallDir var1, String var2, String[] var3) throws ServerConnectionException, IOException {
      InstallDir var4 = var1;
      boolean var5 = false;

      try {
         if (this.isRemote) {
            if (!var1.isProper()) {
               var5 = true;
               File var6 = new File(DomainDir.getTempDirForServer(this.helper.getAdminServerName()), var1.getArchive().getName());
               if (!var6.exists()) {
                  String var7 = System.getProperty("java.io.tmpdir");
                  if (var7 == null) {
                     var7 = "/tmp";
                  }

                  var6 = new File(var7, var1.getArchive().getName());
               }

               var4 = new InstallDir(var1.getArchive().getName(), var6);
               var4.getAppDir().mkdir();
               FileUtils.copy(var1.getArchive().getCanonicalFile(), var4.getArchive());
               var4.getConfigDir().mkdir();
               if (var1.getConfigDir().exists()) {
                  FileUtils.copy(var1.getConfigDir(), var4.getConfigDir());
               }

               if (var1.getPlan() != null) {
                  var4.setPlan(new File(var4.getConfigDir(), var1.getPlan().getName()));
                  FileUtils.copy(var1.getPlan().getCanonicalFile(), var4.getPlan());
               }
            }

            String var13 = this.helper.uploadSource(this.adminUrl, this.auth1, this.auth2, var4.getInstallDir().getPath(), var3, var2);
            File var14 = new File(var13);
            var1.resetInstallDir(var14);
            var1.setArchive(new File(var1.getAppDir(), var4.getArchive().getName()));
            if (var4.getPlan() != null) {
               var1.setPlan(new File(var1.getConfigDir(), var4.getPlan().getName()));
            }

            if (debug) {
               Debug.say("Uploaded app to " + var13);
            }
         }
      } catch (DeployerHelperException var11) {
         throw new ServerConnectionException(SPIDeployerLogger.uploadFailure(this.adminUrl, var1.getArchive().getPath()), var11);
      } finally {
         if (var5) {
            FileUtils.remove(var4.getInstallDir());
         }

      }

      return var1;
   }

   public InstallDir upload2(InstallDir var1, String var2, String[] var3) throws ServerConnectionException, IOException {
      String var4 = var1.getArchive() == null ? null : var1.getArchive().getCanonicalPath();
      String var5 = null;
      if (var1.getPlan() != null) {
         var5 = var1.getPlan().getPath();
      }

      try {
         if (this.isRemote) {
            File var6 = null;
            File var7 = null;
            File var8;
            File var9;
            if (var4 != null) {
               var7 = new File(var4);
               if (var5 != null) {
                  var6 = new File(var5);
               }

               if (!var1.isInAppDir(var7)) {
                  var8 = var1.getAppDir();
                  if (var8.exists()) {
                     FileUtils.remove(var8);
                  }

                  var8.mkdirs();
                  var9 = new File(var8, var7.getName());
                  if (debug) {
                     Debug.say("Copying " + var7.getPath() + " to app area, " + var9.getPath());
                  }

                  HashSet var10 = new HashSet();
                  var10.add(var8);
                  FileUtils.copy(var7, var9, var10);
                  this.delApp = var9;
                  var1.setArchive(var9.getCanonicalFile());
               } else {
                  var1.setArchive(var7.getCanonicalFile());
               }
            }

            var8 = var1.getConfigDir().getCanonicalFile().getParentFile();
            if (!var1.getInstallDir().equals(var8)) {
               var8 = new File(var1.getInstallDir(), "plan");
               var8.mkdirs();
               if (debug) {
                  Debug.say("Copying plan dir at " + var1.getConfigDir() + " to " + var8);
               }

               FileUtils.copy(var1.getConfigDir(), var8);
               var1.setConfigDir(var8);
            }

            if (var6 != null) {
               if (!var1.isInConfigDir(var6)) {
                  var9 = new File(var1.getConfigDir(), var6.getName());
                  if (debug) {
                     Debug.say("Copying plan at " + var6.getPath() + " to config area, " + var9.getPath());
                  }

                  FileUtils.copy(var6, var9);
                  var1.setPlan(var9.getCanonicalFile());
               } else {
                  var1.setPlan(var6.getCanonicalFile());
               }
            }

            String var14 = this.helper.uploadSource(this.adminUrl, this.auth1, this.auth2, var1.getInstallDir().getPath(), var3, var2);
            File var15 = new File(var14);

            try {
               var15 = new File(new URI(var14));
            } catch (Exception var12) {
               Debug.say("Caught: " + var12);
            }

            var1.resetInstallDir(var15);
            if (var7 != null) {
               var1.setArchive(new File(var1.getAppDir(), var7.getName()));
            }

            if (var6 != null) {
               var1.setPlan(new File(var1.getConfigDir(), var6.getName()));
            }

            if (debug) {
               Debug.say("Uploaded app to " + var14);
            }

            if (this.delApp != null) {
               FileUtils.remove(this.delApp);
               this.delApp = null;
            }
         }

         return var1;
      } catch (DeployerHelperException var13) {
         throw new ServerConnectionException(SPIDeployerLogger.uploadFailure(this.adminUrl, var4), var13);
      }
   }

   public String uploadApp(String var1, String var2, String[] var3) throws ServerConnectionException {
      String var4 = var1;

      try {
         if (this.isRemote) {
            var4 = this.helper.uploadSource(this.adminUrl, this.auth1, this.auth2, var1, var3, var2);
            if (debug) {
               Debug.say("Uploaded file, " + var1 + " to " + this.adminUrl + ": " + var4);
            }
         }

         return var4;
      } catch (DeployerHelperException var6) {
         throw new ServerConnectionException(SPIDeployerLogger.uploadFailure(this.adminUrl, var1), var6);
      }
   }

   public String uploadConfig(String var1, DeploymentPlanBean var2, String var3) throws ServerConnectionException {
      String var5 = var2.getConfigRoot();
      String var6 = null;
      if (var5 != null) {
         var6 = (new File(var5)).getParent();
      }

      try {
         InstallDir var7 = new InstallDir(var3, var6);
         String var8 = (new File(var1)).getAbsoluteFile().getPath();
         String var4 = var8;
         if (this.isRemote) {
            File var9 = new File(var8);
            if (!var7.isInConfigDir(var9)) {
               File var10 = new File(var7.getConfigDir(), var9.getName());
               if (debug) {
                  Debug.say("Copying " + var9.getPath() + " to config area, " + var10.getPath());
               }

               FileUtils.copy(var9, var10);
               var7.setPlan(var10);
               var8 = var7.getConfigDir().getPath();
            }

            var4 = this.helper.uploadPlan(this.adminUrl, this.auth1, this.auth2, var8, var3);
            if (debug) {
               Debug.say("Uploaded file, " + var8 + " to " + this.adminUrl + ": " + var4);
            }
         }

         return var4;
      } catch (DeployerHelperException var11) {
         throw new ServerConnectionException(SPIDeployerLogger.uploadFailure(this.adminUrl, var5), var11);
      } catch (IOException var12) {
         throw new ServerConnectionException(SPIDeployerLogger.uploadFailure(this.adminUrl, var5), var12);
      }
   }

   public TargetModuleID[] getResultTmids(AppDeploymentMBean var1, Target[] var2) throws TargetException, ServerConnectionException {
      if (var1 == null) {
         return new TargetModuleID[0];
      } else {
         if (ddebug) {
            Debug.say("Getting tmids for app: " + var1.getName());
         }

         if (var1 == null) {
            return new TargetModuleID[0];
         } else {
            ArrayList var4 = new ArrayList();

            for(int var5 = 0; var5 < var2.length; ++var5) {
               TargetModuleID var3 = this.moduleCache.getTMID(var1, var2[var5]);
               if (var3 != null) {
                  var4.add(var3);
               }
            }

            return (TargetModuleID[])((TargetModuleID[])var4.toArray(new TargetModuleID[0]));
         }
      }
   }

   public ModuleCache getModuleCache() {
      return this.moduleCache;
   }

   public String uploadPlan(String var1, String var2) throws ServerConnectionException {
      try {
         String var3 = this.helper.uploadPlan(this.adminUrl, this.auth1, this.auth2, var1, var2);
         if (debug) {
            Debug.say("Uploaded file, " + var1 + " to " + this.adminUrl + ": " + var3);
         }

         return var3;
      } catch (DeployerHelperException var4) {
         throw new ServerConnectionException(SPIDeployerLogger.uploadFailure(this.adminUrl, var1), var4);
      }
   }

   public void test() throws Throwable {
      this.helper.getTaskByID("23");
   }

   public void resetDomain(DomainMBean var1) {
      this.initCaches(var1);
   }

   public List getServersForCluster(TargetImpl var1) throws ServerConnectionException {
      ArrayList var2 = new ArrayList();
      List var3 = this.targetCache.getMBeans();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         Object var5 = var3.get(var4);
         if (var5 instanceof ServerMBean) {
            ClusterMBean var6 = ((ServerMBean)var5).getCluster();
            if (var6 != null && var6.getName().equals(var1.getName())) {
               var2.add(this.targetCache.getTarget(((ServerMBean)var5).getName()));
            }
         }
      }

      return var2;
   }

   public List getServersForJmsServer(TargetImpl var1) throws ServerConnectionException {
      return this.getServersForDeployableTarget(var1);
   }

   public List getServersForSafAgent(TargetImpl var1) throws ServerConnectionException {
      return this.getServersForTarget(var1);
   }

   public List getServersForHost(TargetImpl var1) throws ServerConnectionException {
      return this.getServersForDeployableTarget(var1);
   }

   private List getServersForDeployableTarget(TargetImpl var1) throws ServerConnectionException {
      HashSet var2 = new HashSet();
      ConfigurationMBean var3 = this.targetCache.getMBean(var1.getName());
      if (var3 instanceof DeploymentMBean) {
         DeploymentMBean var4 = (DeploymentMBean)var3;
         TargetMBean[] var5 = var4.getTargets();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            TargetMBean var7 = var5[var6];
            if (var7 instanceof ServerMBean) {
               var2.add(this.targetCache.getTarget(var7.getName()));
            } else if (var7 instanceof ClusterMBean) {
               var2.addAll(this.getServersForCluster(this.targetCache.getTarget(var7.getName())));
            }
         }
      }

      return new ArrayList(var2);
   }

   private List getServersForTarget(TargetImpl var1) throws ServerConnectionException {
      ArrayList var2 = new ArrayList();
      if (var1.isServer()) {
         var2.add(var1);
      } else if (var1.isCluster()) {
         var2.addAll(this.getServersForCluster(var1));
      } else if (var1.isVirtualHost()) {
         var2.addAll(this.getServersForHost(var1));
      } else if (var1.isJMSServer()) {
         var2.addAll(this.getServersForJmsServer(var1));
      }

      return var2;
   }

   public TargetImpl getTarget(String var1) throws ServerConnectionException {
      return this.targetCache.getTarget(var1);
   }

   public void setLocale(Locale var1) throws IOException {
      this.setLocale(var1, this.jmx);
      this.setLocale(var1, this.runtimeJmx);
      this.setLocale(var1, this.editJmx);
   }

   private void setLocale(Locale var1, JMXConnector var2) throws IOException {
      if (var2 != null) {
         if (var2 instanceof WLSJMXConnector) {
            ((WLSJMXConnector)var2).getMBeanServerConnection(var1);
         }

      }
   }

   public AppRuntimeStateRuntimeMBean getAppRuntimeStateRuntimeMBean() {
      try {
         return this.helper.getAppRuntimeStateMBean();
      } catch (Exception var2) {
         return null;
      }
   }

   public DomainMBean getDomainMBean() {
      return this.domainManager.getDomain();
   }

   public class TaskPoller extends Thread {
      private Map msgMap = new HashMap();

      public TaskPoller(String var2) {
         super(var2);
      }

      public void run() {
         try {
            if (ServerConnectionImpl.ddebug) {
               Debug.say("Poller starting up");
            }

            boolean var1 = true;

            while(var1 && (!interrupted() || !ServerConnectionImpl.this.forceStop)) {
               var1 = this.poll();

               try {
                  if (var1) {
                     Thread.sleep(100L);
                  }
               } catch (InterruptedException var5) {
                  if (ServerConnectionImpl.this.forceStop) {
                     break;
                  }
               }
            }

            synchronized(ServerConnectionImpl.this.listeners) {
               ServerConnectionImpl.this.poller = null;
               if (ServerConnectionImpl.ddebug) {
                  Debug.say("Poller shut down");
               }
            }
         } catch (Throwable var6) {
            SPIDeployerLogger.logPollerError(var6);
            ServerConnectionImpl.this.dm.release();
         }

      }

      private boolean poll() {
         HashSet var1 = new HashSet();
         synchronized(ServerConnectionImpl.this.listeners) {
            Iterator var3 = ServerConnectionImpl.this.listeners.keySet().iterator();

            while(var3.hasNext()) {
               ProgressObjectImpl var5 = (ProgressObjectImpl)var3.next();
               int var7 = this.getMessageIndex(var5);

               try {
                  DeploymentTaskRuntimeMBean var4 = var5.getDtrm();
                  if (var4 != null) {
                     this.report(var4, var7, var5);
                     int var6 = var4.getState();
                     if (var6 != 1 && var6 != 0) {
                        this.completeTask(var6, var5, var4);
                        var5.reportEvent();
                        var1.add(var5);
                     }
                  } else {
                     if (var5.getDeploymentStatus().getState() == StateType.RUNNING) {
                        var5.setMessage(SPIDeployerLogger.lostTask());
                        var5.setState(StateType.RELEASED);
                        var5.reportEvent();
                     }

                     var1.add(var5);
                  }
               } catch (Throwable var10) {
                  SPIDeployerLogger.logConnectionError(var10.getMessage(), var10);
                  var5.setState(StateType.RELEASED);
                  var5.setError(var10);
                  var5.reportEvent();
                  var1.add(var5);
               }
            }

            var3 = var1.iterator();

            while(var3.hasNext()) {
               ServerConnectionImpl.this.deregisterListener((ProgressObjectImpl)var3.next());
            }

            return ServerConnectionImpl.this.listeners.size() > 0;
         }
      }

      private void completeTask(int var1, ProgressObjectImpl var2, DeploymentTaskRuntimeMBean var3) {
         switch (var1) {
            case 2:
               if (ServerConnectionImpl.ddebug) {
                  Debug.say("task state is complete");
               }

               var2.setState(StateType.COMPLETED);
               break;
            case 3:
               if (ServerConnectionImpl.ddebug) {
                  Debug.say("task state is failed");
               }

               Exception var4 = null;

               try {
                  var4 = var3.getError();
               } catch (Throwable var10) {
                  try {
                     List var6 = var3.getTaskMessages();
                     if (var6 != null) {
                        for(int var7 = 0; var7 < var6.size(); ++var7) {
                           String var8 = (String)var6.get(var7);
                           var2.setMessage(var8);
                        }
                     }
                  } catch (Throwable var9) {
                     var2.setMessage(StackTraceUtils.throwable2StackTrace(var9));
                  }
               }

               if (var4 != null) {
                  var2.setError(var4);
               } else if (var2.getDeploymentStatus().getMessage() == null) {
                  var2.setMessage(SPIDeployerLogger.unknownError(var3.getDescription()));
               }

               var2.setState(StateType.FAILED);
               break;
            case 4:
               if (ServerConnectionImpl.ddebug) {
                  Debug.say("task state is deferred");
               }

               var2.setState(StateType.COMPLETED);
         }

      }

      private int getMessageIndex(ProgressObjectImpl var1) {
         Integer var2 = (Integer)this.msgMap.get(var1);
         if (var2 == null) {
            var2 = new Integer(0);
            this.msgMap.put(var1, var2);
         }

         return var2;
      }

      private void updateMessageIndex(ProgressObjectImpl var1, int var2) {
         this.msgMap.put(var1, new Integer(var2));
      }

      private void report(DeploymentTaskRuntimeMBean var1, int var2, ProgressObjectImpl var3) {
         List var4 = var1.getTaskMessages();
         if (var4 != null && var4.size() > var2) {
            for(int var5 = var2; var5 < var4.size(); ++var5) {
               String var6 = (String)var4.get(var5);
               var3.reportEvent(var6);
            }

            this.updateMessageIndex(var3, var4.size());
         }

      }
   }

   public class AppFilter implements NotificationFilter, Serializable {
      private static final long serialVersionUID = 1L;

      public boolean isNotificationEnabled(Notification var1) {
         return var1 instanceof DeploymentNotification;
      }
   }

   public class AppListener implements RemoteNotificationListener, Serializable {
      transient String tid;
      transient DeploymentTaskRuntimeMBean task;
      transient ProgressObjectImpl po;
      private static final long serialVersionUID = 1L;

      AppListener(ProgressObjectImpl var2, DeploymentTaskRuntimeMBean var3) {
         this.task = var3;
         this.tid = var3.getId();
         this.po = var2;
      }

      public void handleNotification(Notification var1, Object var2) {
         if (this.tid.equals(var2)) {
            DeploymentNotification var3 = (DeploymentNotification)var1;
            if (ServerConnectionImpl.debug) {
               Debug.say("Received notification: " + var3.getMessage());
            }

            String var4 = var3.getAppName();
            String var5 = var3.getServerName();
            String var6 = null;
            String var7 = null;
            String var8;
            if (var3.isModuleNotification()) {
               var7 = var3.getModuleName();
               var8 = var3.getCurrentState();
               String var9 = var3.getTargetState();
               String var10 = var3.getTransition();
               if (var10.equals("end")) {
                  var6 = SPIDeployerLogger.successfulTransition(var4, var7, var8, var9, var5);
               } else if (var10.equals("failed")) {
                  var6 = SPIDeployerLogger.failedTransition(var4, var7, var8, var9, var5);
                  this.po.setError(this.task.getError());
               }

               if (var6 != null) {
                  this.po.setMessage(var6);
               }
            } else {
               var8 = var3.getPhase();
               var6 = SPIDeployerLogger.appNotification(var4, var5, var8);
               this.po.setMessage(var6);
            }

            if (var6 != null) {
               this.updateProgress();

               try {
                  this.po.reportEvent(var4, var7, var5, var6);
               } catch (ServerConnectionException var11) {
                  this.po.reportEvent(var4, var7, var5, var6, var11);
               }
            }
         }

      }

      private void updateProgress() {
         int var1 = this.task.getState();
         int var2 = this.task.getCancelState();
         switch (var1) {
            case 1:
               this.po.setState(StateType.RUNNING);
               if (var2 == 2) {
                  this.po.setAction(ActionType.CANCEL);
               } else {
                  this.po.setAction(ActionType.EXECUTE);
               }
               break;
            case 2:
            case 4:
               if (ServerConnectionImpl.ddebug) {
                  Debug.say("completing task " + this.tid);
               }

               this.po.setState(StateType.COMPLETED);
               this.po.setAction(ActionType.EXECUTE);
               ServerConnectionImpl.this.deregisterListener(this.po);
               break;
            case 3:
               if (ServerConnectionImpl.ddebug) {
                  Debug.say("failing task " + this.tid);
               }

               this.po.setState(StateType.FAILED);
               this.po.setAction(ActionType.EXECUTE);
               ServerConnectionImpl.this.deregisterListener(this.po);
         }

      }

      public String toString() {
         return "Listener on task " + this.tid;
      }
   }
}
