package weblogic.jdbc.common.internal;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.application.ApplicationFactoryManager;
import weblogic.common.ResourceException;
import weblogic.deploy.event.DeploymentEventManager;
import weblogic.descriptor.DescriptorBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.image.ImageManager;
import weblogic.health.HealthMonitorService;
import weblogic.health.HealthState;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.module.JDBCApplicationModuleFactory;
import weblogic.jdbc.module.JDBCDataSourceFactory;
import weblogic.jdbc.module.JDBCDeploymentFactory;
import weblogic.jdbc.module.JDBCDeploymentListener;
import weblogic.jdbc.module.JDBCEditCompatibilityObserver;
import weblogic.jdbc.module.JDBCModuleFactory;
import weblogic.logging.LoggingOutputStream;
import weblogic.logging.LoggingPrintStream;
import weblogic.logging.WLLevel;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.JDBCDataSourceFactoryMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.JDBCDataSourceRuntimeMBean;
import weblogic.management.runtime.JDBCDriverRuntimeMBean;
import weblogic.management.runtime.JDBCMultiDataSourceRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class JDBCService extends AbstractServerService {
   private boolean isResumed = false;
   private ServerMBean serverMBean;
   private static ConnectionPoolManager cpMgr;
   private static DataSourceManager dsMgr;
   private ServiceRuntimeMBeanImpl serviceRTMBean;
   private static HashMap dsRTMBeans;
   private static HashMap cpRTMBeans;
   private static HashMap mdsRTMBeans;
   private static HashMap driverRTMBeans;
   private JDBCDeploymentListener deploymentListener;
   private static final AuthenticatedSubject KERNELID = getKernelID();

   private static AuthenticatedSubject getKernelID() {
      return (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private void initialize() throws ServiceFailureException {
      JDBCLogger.logInit();

      try {
         this.serverMBean = ManagementService.getRuntimeAccess(KERNELID).getServer();
         dsRTMBeans = new HashMap();
         cpRTMBeans = new HashMap();
         mdsRTMBeans = new HashMap();
         driverRTMBeans = new HashMap();
         JDBCHelper.setHelper(new JDBCServerHelperImpl());
         DataSourceManager.setDataSourceService(new DataSourceServiceFullImpl());
         HAUtil.setInstance(new HAUtilImpl());
         this.createRuntimeMBean();
         int var1 = this.serverMBean.getJDBCLoginTimeoutSeconds();
         if (var1 > 0) {
            DriverManager.setLoginTimeout(var1);
            JDBCLogger.logSetLoginTimeout(var1);
         }

         ImageManager.getInstance().registerImageSource("JDBC", new JDBCImageSource());
         this.initLog();
         cpMgr = new ConnectionPoolManager();
         dsMgr = DataSourceManager.getInstance();
         this.initFactories();
         this.setupConfigurationHandlers();
         HealthMonitorService.register("JDBC", this.serviceRTMBean, true);
      } catch (Exception var3) {
         String var2 = JDBCLogger.logInitFailed2();
         JDBCLogger.logStackTraceId(var2, var3);
         throw new ServiceFailureException(var2, var3);
      }

      JDBCLogger.logInitDone();
   }

   public String getVersion() {
      return "JSR-221, JDBC 4.0";
   }

   public void start() throws ServiceFailureException {
      this.initialize();
      JDBCLogger.logResume();
      if (this.isResumed) {
         JDBCLogger.logResumeOpInvalid();
      } else {
         this.isResumed = true;

         try {
            cpMgr.resume();
            Iterator var1 = ConnectionPoolManager.getConnectionPools();

            while(true) {
               if (!var1.hasNext()) {
                  dsMgr.resume();
                  break;
               }

               ConnectionPool var4 = (ConnectionPool)var1.next();
               createConnectionPoolRuntimeMBean(var4, var4.getAppName(), var4.getModuleName());
               createDataSourceRuntimeMBean(var4, var4.getAppName(), var4.getModuleName(), var4.getJDBCDataSource());
            }
         } catch (Exception var3) {
            String var2 = JDBCLogger.logResumeFailed();
            JDBCLogger.logStackTraceId(var2, var3);
            throw new ServiceFailureException(var3);
         }

         JDBCLogger.logResumeDone();
      }
   }

   public void stop() throws ServiceFailureException {
      JDBCLogger.logSuspend();

      try {
         if (dsMgr != null) {
            dsMgr.suspend(true);
         }

         if (cpMgr != null) {
            cpMgr.suspend(true);
         }

         this.isResumed = false;
         this.destroyRuntimeMBeans();
      } catch (Exception var3) {
         String var2 = JDBCLogger.logSuspendFailed();
         JDBCLogger.logStackTraceId(var2, var3);
         throw new ServiceFailureException(var2, var3);
      }

      JDBCLogger.logSuspendDone();
      this.shutdown();
   }

   public void halt() throws ServiceFailureException {
      JDBCLogger.logFSuspend();

      try {
         if (dsMgr != null) {
            dsMgr.forceSuspend(true);
         }

         if (cpMgr != null) {
            cpMgr.forceSuspend(true);
         }

         this.isResumed = false;
      } catch (Exception var3) {
         String var2 = JDBCLogger.logFSuspendFailed();
         JDBCLogger.logStackTraceId(var2, var3);
      }

      JDBCLogger.logFSuspendDone();
      this.shutdown();
   }

   public void shutdown() throws ServiceFailureException {
      JDBCLogger.logShutdown2();

      try {
         HealthMonitorService.unregister("JDBC");
         this.removeConfigurationHandlers();
         if (dsMgr != null) {
            dsMgr.shutdown();
         }

         if (cpMgr != null) {
            cpMgr.shutdown();
         }

         this.destroyRuntimeMBeans();
      } catch (Exception var3) {
         String var2 = JDBCLogger.logShutdownFailed();
         JDBCLogger.logStackTraceId(var2, var3);
         throw new ServiceFailureException(var3);
      }

      JDBCLogger.logShutdownDone();
   }

   public static ConnectionPoolManager getConnectionPoolManager() {
      return cpMgr;
   }

   public static DataSourceManager getDataSourceManager() {
      return dsMgr;
   }

   public static synchronized JDBCDataSourceRuntimeMBean[] getJDBCDataSourceRuntimeMBeans() {
      Object[] var0 = dsRTMBeans.values().toArray();
      JDBCDataSourceRuntimeMBean[] var1 = new JDBCDataSourceRuntimeMBean[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = (JDBCDataSourceRuntimeMBean)var0[var2];
      }

      return var1;
   }

   public static synchronized JDBCMultiDataSourceRuntimeMBean[] getJDBCMultiDataSourceRuntimeMBeans() {
      Object[] var0 = mdsRTMBeans.values().toArray();
      JDBCMultiDataSourceRuntimeMBean[] var1 = new JDBCMultiDataSourceRuntimeMBean[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = (JDBCMultiDataSourceRuntimeMBean)var0[var2];
      }

      return var1;
   }

   public static synchronized void addJDBCDriverRuntimeMBean(JDBCDriverRuntimeMBean var0, String var1) {
      driverRTMBeans.put(var1, var0);
   }

   public static synchronized void removeJDBCDriverRuntimeMBean(String var0) {
      driverRTMBeans.remove(var0);
   }

   public static synchronized JDBCDriverRuntimeMBean getJDBCDriverRuntimeMBean(String var0) {
      return (JDBCDriverRuntimeMBean)((JDBCDriverRuntimeMBean)driverRTMBeans.get(var0));
   }

   public static synchronized JDBCDriverRuntimeMBean[] getJDBCDriverRuntimeMBeans() {
      Object[] var0 = driverRTMBeans.values().toArray();
      JDBCDriverRuntimeMBean[] var1 = new JDBCDriverRuntimeMBean[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = (JDBCDriverRuntimeMBean)var0[var2];
      }

      return var1;
   }

   public HealthState getHealthState() {
      byte var1 = 0;
      float var2 = 0.0F;
      float var3 = 0.0F;
      float var4 = 0.0F;
      StringBuffer var5 = new StringBuffer();
      Iterator var6 = ConnectionPoolManager.getConnectionPools();

      while(var6.hasNext()) {
         ++var2;
         ConnectionPool var7 = (ConnectionPool)var6.next();
         String var8 = var7.getDerivedState();
         if (var8.equals("Unhealthy")) {
            ++var3;
            var5.append("Connection Pool Name = " + this.getPresentationName(var7) + ", State = " + "Unhealthy" + "\n");
         } else if (var8.equals("Overloaded")) {
            ++var4;
            var5.append("Connection Pool Name = " + this.getPresentationName(var7) + ", State = " + "Overloaded" + "\n");
         }
      }

      if (var3 > 0.0F) {
         if (var3 == var2) {
            var1 = 3;
         } else if (var3 >= var2 / 2.0F) {
            var1 = 2;
         } else {
            var1 = 1;
         }
      } else if (var4 > 0.0F) {
         var1 = 4;
      }

      String[] var9 = new String[]{var5.toString()};
      HealthState var10 = new HealthState(var1, var9);
      return var10;
   }

   private void createRuntimeMBean() throws Exception {
      this.serviceRTMBean = new ServiceRuntimeMBeanImpl(this);
      ManagementService.getRuntimeAccess(KERNELID).getServerRuntime().setJDBCServiceRuntime(this.serviceRTMBean);
   }

   private void destroyRuntimeMBeans() throws ManagementException {
      unregister(dsRTMBeans.values());
      unregister(cpRTMBeans.values());
      unregister(mdsRTMBeans.values());
      unregister((RuntimeMBeanDelegate)this.serviceRTMBean);
   }

   private static void unregister(Collection var0) throws ManagementException {
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         unregister((RuntimeMBeanDelegate)var1.next());
      }

      var0.clear();
   }

   private void initFactories() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(KERNELID);
      JDBCDataSourceFactoryMBean[] var2 = var1.getDomain().getJDBCDataSourceFactories();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         JDBCDataSourceFactory.addDataSourceFactory(var2[var3]);
      }

      ApplicationFactoryManager var4 = ApplicationFactoryManager.getApplicationFactoryManager();
      var4.addWLAppModuleFactory(new JDBCApplicationModuleFactory());
      var4.addWblogicModuleFactory(new JDBCModuleFactory());
      var4.addDeploymentFactory(new JDBCDeploymentFactory());
   }

   private void initLog() {
      DebugLogger var1 = DebugLogger.getDebugLogger("DebugJDBCDriverLogging");
      if (var1.isDebugEnabled() || this.serverMBean.isJDBCLoggingEnabled()) {
         try {
            DriverManager.setLogStream(new LoggingPrintStream(new LoggingOutputStream("JDBCDriverLogging", WLLevel.DEBUG, true)));
            DriverManager.println("JDBC log stream started at " + (new Date()).toString());
         } catch (Exception var3) {
            JDBCLogger.logErrorLogInit(var3.toString());
         }
      }

      var1 = null;
   }

   private void setupConfigurationHandlers() throws DeploymentException {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(KERNELID);
      var1.addAccessCallbackClass(JDBCEditCompatibilityObserver.class.getName());
      this.deploymentListener = new JDBCDeploymentListener();
      DeploymentEventManager.addVetoableDeploymentListener(this.deploymentListener);
      DeploymentEventManager.addVetoableSystemResourceDeploymentListener(this.deploymentListener);
   }

   private void removeConfigurationHandlers() {
      if (this.deploymentListener != null) {
         DeploymentEventManager.removeVetoableDeploymentListener(this.deploymentListener);
         DeploymentEventManager.removeVetoableSystemResourceDeploymentListener(this.deploymentListener);
      }

   }

   private static void registerMBeansWithService(ConnectionPool var0, JDBCDataSourceRuntimeMBean var1, String var2) throws ManagementException {
      if (getJDBCDriverRuntimeMBean(var2) == null) {
         DriverRuntimeMBeanImpl var3 = new DriverRuntimeMBeanImpl(var2);
         addJDBCDriverRuntimeMBean(var3, var2);
         ((DataSourceRuntimeMBeanImpl)var1).setJDBCDriverRuntime(var3);
      } else {
         ((DataSourceRuntimeMBeanImpl)var1).setJDBCDriverRuntime(getJDBCDriverRuntimeMBean(var2));
      }

   }

   public static synchronized ComponentRuntimeMBean createJDBCMultiDataSourceRuntimeMBean(MultiPool var0, String var1, String var2) throws ManagementException {
      RuntimeMBean var3 = getParent(var1);
      String var4 = qualifiedName(var1, var2, var0.getName());
      MultiDataSourceRuntimeMBeanImpl var5 = new MultiDataSourceRuntimeMBeanImpl(var0, var4, var3, (DescriptorBean)var0.getDataSourceBean());
      mdsRTMBeans.put(var4, var5);
      return var5;
   }

   public static synchronized void destroyMultiDataSourceRuntimeMBean(String var0, String var1, String var2) throws ManagementException {
      String var3 = qualifiedName(var0, var1, var2);
      MultiDataSourceRuntimeMBeanImpl var4 = (MultiDataSourceRuntimeMBeanImpl)mdsRTMBeans.remove(var3);
      unregister((RuntimeMBeanDelegate)var4);
   }

   public static ComponentRuntimeMBean createConnectionPoolRuntimeMBean(ConnectionPool var0, String var1, String var2) throws ManagementException {
      RuntimeMBean var3 = getParent(var1);
      String var4 = qualifiedName(var1, var2, var0.getName());
      ConnectionPoolRuntimeMBeanImpl var5 = new ConnectionPoolRuntimeMBeanImpl(var0, var4, var3);
      cpRTMBeans.put(var4, var5);
      return var5;
   }

   public static void destroyConnectionPoolRuntimeMBean(String var0, String var1, String var2) throws ManagementException {
      String var3 = qualifiedName(var0, var1, var2);
      ConnectionPoolRuntimeMBeanImpl var4 = (ConnectionPoolRuntimeMBeanImpl)cpRTMBeans.remove(var3);
      unregister((RuntimeMBeanDelegate)var4);
   }

   public static OracleDataSourceRuntimeImpl createHADataSourceRuntimeMBean(HAConnectionPool var0, String var1, String var2, JDBCDataSourceBean var3) throws ManagementException {
      RuntimeMBean var4 = getParent(var1);
      String var5 = qualifiedName(var1, var2, var0.getName());
      OracleDataSourceRuntimeImpl var6 = new OracleDataSourceRuntimeImpl(var0, var5, var4, (DescriptorBean)var3);
      dsRTMBeans.put(var5, var6);
      return var6;
   }

   public static DataSourceRuntimeMBeanImpl createDataSourceRuntimeMBean(ConnectionPool var0, String var1, String var2, JDBCDataSourceBean var3) throws ManagementException {
      RuntimeMBean var4 = getParent(var1);
      String var5 = qualifiedName(var1, var2, var0.getName());
      DataSourceRuntimeMBeanImpl var6 = new DataSourceRuntimeMBeanImpl(var0, var5, var4, (DescriptorBean)var3);
      dsRTMBeans.put(var5, var6);
      RuntimeAccess var7 = ManagementService.getRuntimeAccess(KERNELID);
      String var8 = var7.getDomainName() + "_" + var7.getServerName() + "_" + var0.getDriverVersion();
      registerMBeansWithService(var0, var6, var8);
      return var6;
   }

   public static void destroyDataSourceRuntimeMBean(final String var0, String var1, String var2, String var3) throws ResourceException {
      var3 = qualifiedName(var1, var2, var3);
      final DataSourceRuntimeMBeanImpl var4 = (DataSourceRuntimeMBeanImpl)dsRTMBeans.remove(var3);

      try {
         SecurityServiceManager.runAs(KERNELID, KERNELID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               DriverRuntimeMBeanImpl var1 = (DriverRuntimeMBeanImpl)JDBCService.getJDBCDriverRuntimeMBean(var0);
               if (var1 != null) {
                  JDBCService.removeJDBCDriverRuntimeMBean(var1.getName());
                  var1.unregister();
               }

               if (var4 != null) {
                  var4.unregister();
               }

               return null;
            }
         });
      } catch (PrivilegedActionException var6) {
         throw new ResourceException(var6.toString());
      }
   }

   private static String qualifiedName(String var0, String var1, String var2) {
      if (var0 != null) {
         var2 = var0 + "@" + var1 + "@" + var2;
      }

      return var2;
   }

   private String getPresentationName(ConnectionPool var1) {
      return (var1.getAppName() != null ? var1.getAppName() + ":" : "") + (var1.getModuleName() != null ? var1.getModuleName() + ":" : "") + var1.getName();
   }

   private static RuntimeMBean getParent(String var0) {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(KERNELID);
      return var1.getServerRuntime().lookupApplicationRuntime(var0);
   }

   private static void unregister(final RuntimeMBeanDelegate var0) throws ManagementException {
      try {
         SecurityServiceManager.runAs(KERNELID, KERNELID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               if (var0 != null) {
                  var0.unregister();
               }

               return null;
            }
         });
      } catch (PrivilegedActionException var2) {
         throw new ManagementException(var2.toString());
      }
   }
}
