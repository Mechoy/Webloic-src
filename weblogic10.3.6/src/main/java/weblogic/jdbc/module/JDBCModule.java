package weblogic.jdbc.module;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.ModuleLocationInfo;
import weblogic.application.NonDynamicPropertyUpdateException;
import weblogic.application.UpdateListener;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.common.ResourceException;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.event.DeploymentVetoException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.ConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.ConnectionPropertiesBean;
import weblogic.j2ee.descriptor.wl.JDBCConnectionPoolBean;
import weblogic.j2ee.descriptor.wl.JDBCConnectionPoolParamsBean;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBean;
import weblogic.j2ee.descriptor.wl.JDBCDriverParamsBean;
import weblogic.j2ee.descriptor.wl.JDBCOracleParamsBean;
import weblogic.j2ee.descriptor.wl.JDBCPropertiesBean;
import weblogic.j2ee.descriptor.wl.JDBCPropertyBean;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.ConnectionPool;
import weblogic.jdbc.common.internal.ConnectionPoolManager;
import weblogic.jdbc.common.internal.DataSourceManager;
import weblogic.jdbc.common.internal.HAConnectionPool;
import weblogic.jdbc.common.internal.JDBCHelper;
import weblogic.jdbc.common.internal.JDBCMBeanConverter;
import weblogic.jdbc.common.internal.JDBCService;
import weblogic.jdbc.common.internal.MultiPool;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.t3.srvr.T3Srvr;
import weblogic.utils.FileUtils;
import weblogic.utils.classloaders.GenericClassLoader;

public class JDBCModule implements Module, UpdateListener, ModuleLocationInfo {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private ApplicationContextInternal appCtx;
   private Context envCtx;
   private String appName;
   private String moduleName;
   private DataSource dataSource;
   private boolean bound;
   private final String uri;
   private ConnectionPoolManager cpMgr;
   private DataSourceManager dsMgr;
   private JDBCDeploymentHelper deploymentHelper;
   private ConnectionPool pool;
   private MultiPool multipool;
   private ComponentRuntimeMBean compRTMB;
   private int legacyType;
   private JDBCConnectionPoolBean descriptor;
   private boolean oldAppScopedPool;
   private JDBCDataSourceBean dsBean;
   private final int BEAN_TYPE_PROPERTIES;
   private final int BEAN_TYPE_PROPERTY;
   private final int BEAN_TYPE_ORACLE;
   private int COMMIT_CHANGE;
   private int ROLLBACK_CHANGE;
   private String altDD;
   private static final String DEFAULT_APPENDIX = "-jdbc.xml";

   public JDBCModule(String var1) {
      this.bound = false;
      this.pool = null;
      this.multipool = null;
      this.BEAN_TYPE_PROPERTIES = 100;
      this.BEAN_TYPE_PROPERTY = 101;
      this.BEAN_TYPE_ORACLE = 102;
      this.COMMIT_CHANGE = 0;
      this.ROLLBACK_CHANGE = 1;
      this.altDD = null;
      this.uri = var1;
      this.deploymentHelper = new JDBCDeploymentHelper();
   }

   public JDBCModule(WeblogicModuleBean var1) {
      this(var1.getPath());
      this.moduleName = var1.getName();
   }

   public JDBCModule(JDBCConnectionPoolBean var1, String var2) {
      this(var2);
      this.descriptor = var1;
      this.oldAppScopedPool = true;
   }

   public String getId() {
      return this.moduleName != null ? this.moduleName : this.uri;
   }

   public String getModuleURI() {
      return this.uri;
   }

   public String getType() {
      return WebLogicModuleType.MODULETYPE_JDBC;
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return new ComponentRuntimeMBean[]{this.compRTMB};
   }

   public DescriptorBean[] getDescriptors() {
      if (this.descriptor != null) {
         return new DescriptorBean[]{(DescriptorBean)this.descriptor};
      } else {
         return this.dsBean != null ? new DescriptorBean[]{(DescriptorBean)this.dsBean} : new DescriptorBean[0];
      }
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.init(var1, var2, var3);
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.appCtx = (ApplicationContextInternal)var1;
      String var4 = var1.getApplicationId();
      this.envCtx = var1.getEnvContext();

      try {
         this.envCtx.lookup("/jdbc");
      } catch (NameNotFoundException var8) {
         try {
            this.envCtx.createSubcontext("jdbc");
         } catch (NamingException var7) {
            throw new AssertionError(var7);
         }
      } catch (NamingException var9) {
         throw new AssertionError(var9);
      }

      if (!this.oldAppScopedPool) {
         if (!this.uri.endsWith("-jdbc.xml")) {
            throw new ModuleException("Data source descriptor filename " + this.uri + " does not have the required suffix \"-jdbc.xml\"");
         }

         this.dsBean = this.deploymentHelper.createJDBCDataSourceDescriptor(this.appCtx, this.uri);
         if (this.dsBean == null) {
            throw new ModuleException("Descriptor " + this.uri + " not found.");
         }

         this.legacyType = JDBCMBeanConverter.getLegacyType(this.dsBean);
         if (this.legacyType == 0 && JDBCMBeanConverter.getInternalProperty(this.dsBean, "LegacyPoolName") != null) {
            throw new ModuleException("Cannot specify pool name for data source " + this.dsBean.getName() + ", URI = " + this.uri);
         }

         if (this.moduleName == null && !this.dsBean.getJDBCDataSourceParams().getScope().equals("Global")) {
            JDBCLogger.logInvalidApplicationScope(this.dsBean.getName(), this.uri);
            this.dsBean.getJDBCDataSourceParams().setScope("Global");
         }

         String var5 = ApplicationVersionUtils.getVersionId(var4);
         if ("Global".equals(this.dsBean.getJDBCDataSourceParams().getScope())) {
            if (var5 != null) {
               throw new ModuleException("An application-scoped data source (" + this.dsBean.getName() + ") cannot specify a scope of Global when application is versioned (" + var4 + ")");
            }
         } else {
            this.appName = var4;
         }

         var3.addUpdateListener(this);
         this.cpMgr = JDBCService.getConnectionPoolManager();
         this.dsMgr = JDBCService.getDataSourceManager();
      } else {
         this.appName = var4;
      }

      return var2;
   }

   public void start() {
   }

   public void prepare() throws ModuleException {
      if (this.oldAppScopedPool) {
         ConnectionFactoryBean var1 = this.descriptor.getConnectionFactory();

         try {
            if (this.dataSource == null) {
               if (var1 == null) {
                  JDBCDataSourceFactory var2 = JDBCDataSourceFactory.getDataSourceFactory();
                  this.dataSource = var2.createDataSource(this.appName, this.moduleName, this.descriptor);
               } else {
                  String var11 = var1.getFactoryName();
                  ConnectionPropertiesBean var3 = var1.getConnectionProperties();
                  if (var11 == null && var3 == null) {
                     throw new ModuleException("Connection factory must have factory name or connection properties defined");
                  }

                  JDBCDataSourceFactory var4 = JDBCDataSourceFactory.getDataSourceFactory(var11);
                  if (var4 == null) {
                     throw new ModuleException("Could not find DataSourceFactory named " + var11);
                  }

                  this.dataSource = var4.createDataSource(this.appName, this.moduleName, this.descriptor);
               }
            }
         } catch (ResourceException var7) {
            throw new ModuleException(var7.toString(), var7);
         } catch (ModuleException var8) {
            throw var8;
         } catch (Throwable var9) {
            JDBCLogger.logStackTrace(var9);
            throw new ModuleException(var9.toString());
         }

         this.bindDataSource();
      } else {
         boolean var10 = isMemberDSOfMultiDataSource(this.dsBean.getName(), this.appCtx);
         boolean var12 = isMemberOfMultiDataSourceLLR(this.dsBean.getName(), this.appCtx);

         try {
            if (this.legacyType == 0 || this.legacyType == 1 || this.legacyType == 2) {
               Object var13 = this.cpMgr.createAndStartPool(this.dsBean, this.appName, this.moduleName, var10, var12);
               if (var13 instanceof HAConnectionPool) {
                  this.pool = (HAConnectionPool)var13;
                  this.compRTMB = JDBCService.createHADataSourceRuntimeMBean((HAConnectionPool)this.pool, this.pool.getAppName(), this.pool.getModuleName(), this.pool.getJDBCDataSource());
               } else if (var13 instanceof ConnectionPool) {
                  this.pool = (ConnectionPool)var13;
                  this.pool.suspend(false);
                  JDBCService.createConnectionPoolRuntimeMBean(this.pool, this.pool.getAppName(), this.pool.getModuleName());
                  this.compRTMB = JDBCService.createDataSourceRuntimeMBean(this.pool, this.pool.getAppName(), this.pool.getModuleName(), this.pool.getJDBCDataSource());
               } else {
                  this.multipool = (MultiPool)var13;
                  this.compRTMB = JDBCService.createJDBCMultiDataSourceRuntimeMBean(this.multipool, this.appName, this.moduleName);
                  if (!"Global".equals(this.dsBean.getJDBCDataSourceParams().getScope())) {
                     this.multipool.setModuleNames(getCPModuleNames(this.dsBean));
                  }

                  this.multipool.suspend(false);
               }
            }

            if (this.legacyType == 0 || this.legacyType == 3 || this.legacyType == 4) {
               JDBCDataSourceBean[] var14 = this.getPoolBeans(this.dsBean, this.appCtx);
               this.dsMgr.checkDataSource(this.dsBean, this.appName, this.moduleName, var14);
            }
         } catch (Exception var6) {
            try {
               if (this.dsBean != null) {
                  this.cpMgr.shutdownAndDestroyPool(this.dsBean, this.appName, this.moduleName);
                  this.unregisterMBeans(this.dsBean);
               }
            } catch (Exception var5) {
               JDBCLogger.logStackTrace(var5);
            }

            throw new ModuleException(var6);
         }
      }

   }

   private void unregisterMBeans(JDBCDataSourceBean var1) throws ManagementException, ResourceException {
      if (var1.getJDBCDataSourceParams().getDataSourceList() != null) {
         JDBCService.destroyMultiDataSourceRuntimeMBean(this.appName, this.moduleName, var1.getName());
      } else {
         JDBCService.destroyConnectionPoolRuntimeMBean(this.appName, this.moduleName, var1.getName());
         JDBCService.destroyDataSourceRuntimeMBean(var1.getJDBCDriverParams().getDriverName(), this.appName, this.moduleName, var1.getName());
      }

   }

   public void activate() throws IllegalStateException, ModuleException {
      if (this.oldAppScopedPool) {
         if (this.dataSource instanceof LocalDataSource) {
            try {
               ConnectionPool var1 = ((LocalDataSource)this.dataSource).getPoolRef();
               if (var1 != null) {
                  var1.activate();
                  JDBCService.createConnectionPoolRuntimeMBean(var1, var1.getAppName(), var1.getModuleName());
                  this.compRTMB = JDBCService.createDataSourceRuntimeMBean(var1, var1.getAppName(), var1.getModuleName(), var1.getJDBCDataSource());
               }
            } catch (ResourceException var3) {
               throw new ModuleException(var3);
            } catch (ManagementException var4) {
               throw new ModuleException(var4);
            }
         }
      } else {
         boolean var6 = isMemberDSOfMultiDataSource(this.dsBean.getName(), this.appCtx);

         try {
            if (this.pool != null) {
               this.pool.resume();
            } else if (this.multipool != null) {
               this.multipool.setupConnPoolRefs();
               this.multipool.resume();
            }

            if (this.legacyType == 0 || this.legacyType == 3 || this.legacyType == 4) {
               JDBCDataSourceBean[] var2 = this.getPoolBeans(this.dsBean, this.appCtx);
               this.dsMgr.createAndStartDataSource(this.dsBean, this.appName, this.moduleName, this.appCtx.getEnvContext(), var2, var6);
            }
         } catch (Exception var5) {
            throw new ModuleException(var5);
         }
      }

   }

   public void deactivate() throws IllegalStateException, ModuleException {
      if (this.oldAppScopedPool) {
         if (this.dataSource instanceof LocalDataSource) {
            try {
               ConnectionPool var1 = ((LocalDataSource)this.dataSource).getPoolRef();
               if (var1 != null) {
                  var1.deactivate();
                  JDBCService.destroyConnectionPoolRuntimeMBean(this.appName, this.moduleName, var1.getName());
                  JDBCService.destroyDataSourceRuntimeMBean(var1.getDriverVersion(), this.appName, this.moduleName, var1.getName());
               }
            } catch (ResourceException var2) {
               throw new ModuleException(var2);
            } catch (ManagementException var3) {
               throw new ModuleException(var3);
            }
         }
      } else {
         try {
            if (this.legacyType == 0 || this.legacyType == 3 || this.legacyType == 4) {
               this.dsMgr.shutdownAndDestroyDataSource(this.dsBean, this.appName, this.moduleName);
            }

            if (this.pool != null) {
               JDBCService.destroyDataSourceRuntimeMBean(this.pool.getDriverVersion(), this.pool.getAppName(), this.pool.getModuleName(), this.pool.getName());
               if (T3Srvr.getT3Srvr().getState().equals("FORCE_SHUTTING_DOWN")) {
                  this.pool.forceSuspend(true);
               } else {
                  this.pool.suspend(false);
               }
            } else if (this.multipool != null) {
               this.multipool.suspend(false);
            }
         } catch (Exception var4) {
            throw new ModuleException(var4);
         }
      }

   }

   public void unprepare() throws IllegalStateException, ModuleException {
      if (this.oldAppScopedPool) {
         if (this.dataSource != null) {
            ((LocalDataSource)this.dataSource).unregister();
         }

         this.unbindDataSource();
         this.dataSource = null;
      } else {
         try {
            if (this.legacyType == 0 || this.legacyType == 1 || this.legacyType == 2) {
               this.cpMgr.shutdownAndDestroyPool(this.dsBean, this.appName, this.moduleName);
            }

            if (this.pool != null) {
               JDBCService.destroyConnectionPoolRuntimeMBean(this.pool.getAppName(), this.pool.getModuleName(), this.pool.getName());
               JDBCService.destroyDataSourceRuntimeMBean(this.pool.getDriverVersion(), this.pool.getAppName(), this.pool.getModuleName(), this.pool.getName());
            }
         } catch (Exception var2) {
            throw new ModuleException(var2);
         }
      }

   }

   public void remove() throws IllegalStateException, ModuleException {
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      var1.removeUpdateListener(this);
   }

   public void adminToProduction() {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) {
   }

   public void forceProductionToAdmin() {
   }

   private static HashMap getCPModuleNames(JDBCDataSourceBean var0) throws ResourceException {
      HashMap var1 = new HashMap();
      if (ApplicationAccess.getApplicationAccess().getCurrentApplicationContext() == null) {
         return null;
      } else {
         Module[] var2 = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext().getApplicationModules();
         String var3 = var0.getJDBCDataSourceParams().getDataSourceList();
         StringTokenizer var4 = new StringTokenizer(var3, ",");

         while(var4.hasMoreTokens()) {
            String var5 = var4.nextToken();
            Module var6 = getModule(var2, var5);
            if (var6 == null) {
               String var7 = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext().getApplicationId();
               throw new ResourceException("Unable to find module in application '" + var7 + "' for connection pool '" + var5 + "' being used by multi pool '" + var0.getName() + "'");
            }

            var1.put(var5, var6.getId());
         }

         return var1;
      }
   }

   private static Module getModule(Module[] var0, String var1) {
      int var2 = var1.indexOf("@");
      if (var2 != -1) {
         String var6 = var1.substring(var2 + 1);
         var2 = var6.indexOf("@");
         if (var2 == -1) {
            var2 = var1.indexOf("@");
            var6 = var1.substring(0, var2);
         } else {
            var6 = var6.substring(0, var2);
         }

         for(int var7 = 0; var7 < var0.length; ++var7) {
            if ("jdbc".equals(var0[var7].getType()) && var6.equals(var0[var7].getId())) {
               return var0[var7];
            }
         }

         return null;
      } else {
         for(int var3 = 0; var3 < var0.length; ++var3) {
            if ("jdbc".equals(var0[var3].getType())) {
               DescriptorBean[] var4 = var0[var3].getDescriptors();
               if (var4 != null && var4.length == 1 && var4[0] instanceof JDBCDataSourceBean) {
                  JDBCDataSourceBean var5 = (JDBCDataSourceBean)var4[0];
                  if (var1.equals(var5.getName())) {
                     return var0[var3];
                  }
               }
            }
         }

         return null;
      }
   }

   public boolean acceptURI(String var1) {
      return ".".equals(var1) ? true : this.uri.equals(var1);
   }

   public void prepareUpdate(String var1) throws ModuleException {
      this.processUpdate(var1, this.COMMIT_CHANGE);
   }

   public void rollbackUpdate(String var1) {
      try {
         this.processUpdate(var1, this.ROLLBACK_CHANGE);
      } catch (Exception var3) {
      }

   }

   public void activateUpdate(String var1) throws ModuleException {
      this.dsBean = this.deploymentHelper.createJDBCDataSourceDescriptor(this.appCtx, var1);
      if (this.pool != null) {
         this.pool.setJDBCDataSource(this.dsBean);
      }

   }

   public String toString() {
      return this.oldAppScopedPool ? "JDBCModule(" + this.descriptor.getDataSourceJNDIName() + ")" : "JDBCModule(" + this.dsBean.getName() + ")";
   }

   public void checkDependencies() throws DeploymentVetoException {
      if (!this.oldAppScopedPool) {
         if (this.pool != null) {
            String var1 = this.pool.getName();
            Iterator var2 = ConnectionPoolManager.getMultiPools();

            while(var2.hasNext()) {
               MultiPool var3 = (MultiPool)var2.next();
               if (this.isGloballyScoped() && var3.isGloballyScoped() && var3.hasMember(var1)) {
                  throw new DeploymentVetoException("Cannot undeploy JDBC Data Source " + this.pool.getName() + ", it is currently being used by the JDBC Multi Data Source " + var3.getName());
               }
            }
         }

      }
   }

   public void setAltDD(String var1) {
      this.altDD = var1;
   }

   public String getAltDD() {
      return this.altDD;
   }

   private void bindDataSource() throws ModuleException {
      if (!this.bound) {
         try {
            Context var1 = (Context)this.envCtx.lookup("jdbc");
            String var2 = this.descriptor.getDataSourceJNDIName();
            if (var2 == null) {
               throw new ModuleException("data-source-name not defined in jdbc-connection-pool");
            }

            var1.bind(var2, this.dataSource);
         } catch (NamingException var3) {
            throw new ModuleException(var3.toString(), var3);
         }

         this.bound = true;
      }
   }

   private void unbindDataSource() throws ModuleException {
      if (this.bound) {
         try {
            Context var1 = (Context)this.envCtx.lookup("jdbc");
            var1.unbind(this.descriptor.getDataSourceJNDIName());
         } catch (NamingException var2) {
            throw new ModuleException(var2.toString(), var2);
         }

         this.bound = false;
      }
   }

   private void processUpdate(String var1, int var2) throws ModuleException {
      try {
         JDBCDataSourceBean var3 = this.deploymentHelper.createJDBCDataSourceDescriptor(this.appCtx, var1);
         Iterator var4 = ((DescriptorBean)this.dsBean).getDescriptor().computeDiff(((DescriptorBean)var3).getDescriptor()).iterator();

         while(var4.hasNext()) {
            this.processBeanUpdateEvent((BeanUpdateEvent)var4.next(), var2);
         }

      } catch (Exception var5) {
         if (var2 == this.COMMIT_CHANGE) {
            throw new ModuleException("prepareUpdate failed for JDBC Module " + this.dsBean.getName() + ": " + var5.getMessage(), var5);
         } else {
            throw new ModuleException("rollbackUpdate failed for JDBC Module " + this.dsBean.getName() + ": " + var5.getMessage(), var5);
         }
      }
   }

   private void processBeanUpdateEvent(BeanUpdateEvent var1, int var2) throws Exception {
      JDBCConnectionPoolParamsBean var3 = null;
      JDBCDataSourceParamsBean var4 = null;
      JDBCPropertiesBean var5 = null;
      JDBCPropertyBean var6 = null;
      JDBCOracleParamsBean var7 = null;
      BeanUpdateEvent.PropertyUpdate[] var8 = var1.getUpdateList();
      if (var8 != null) {
         DescriptorBean var9 = var1.getProposedBean();
         byte var10;
         if (var9 instanceof JDBCConnectionPoolParamsBean) {
            var10 = 1;
            if (var2 == this.COMMIT_CHANGE) {
               var3 = (JDBCConnectionPoolParamsBean)var9;
            } else {
               var3 = this.dsBean.getJDBCConnectionPoolParams();
            }
         } else if (var9 instanceof JDBCDataSourceParamsBean) {
            var10 = 3;
            if (var2 == this.COMMIT_CHANGE) {
               var4 = (JDBCDataSourceParamsBean)var9;
            } else {
               var4 = this.dsBean.getJDBCDataSourceParams();
            }
         } else if (var9 instanceof JDBCPropertiesBean) {
            var10 = 100;
            if (var2 == this.COMMIT_CHANGE) {
               var5 = (JDBCPropertiesBean)var9;
            } else {
               var5 = this.dsBean.getInternalProperties();
            }
         } else if (var9 instanceof JDBCPropertyBean) {
            var10 = 101;
            if (var2 == this.COMMIT_CHANGE) {
               var6 = (JDBCPropertyBean)var9;
            } else {
               var6 = this.dsBean.getInternalProperties().lookupProperty(((JDBCPropertyBean)var9).getName());
            }
         } else {
            if (!(var9 instanceof JDBCOracleParamsBean)) {
               if (var9 instanceof JDBCDriverParamsBean) {
                  return;
               }

               JDBCLogger.logUnexpectedUpdateBeanType(var9.toString());
               return;
            }

            var10 = 102;
            if (var2 == this.COMMIT_CHANGE) {
               var7 = (JDBCOracleParamsBean)var9;
            } else {
               var7 = this.dsBean.getJDBCOracleParams();
            }
         }

         int var11 = -1;

         for(int var12 = 0; var12 < var8.length; ++var12) {
            switch (var8[var12].getUpdateType()) {
               case 1:
                  if (var10 == 1) {
                     try {
                        this.processCPUpdate(var8[var12].getPropertyName(), var3);
                     } catch (Exception var18) {
                        if (!var8[var12].getPropertyName().equals("InitialCapacity")) {
                           throw var18;
                        }

                        var11 = var12;
                     }
                  } else if (var10 == 3) {
                     this.processDSUpdate(var8[var12].getPropertyName(), var4);
                  } else if (var10 == 102) {
                     this.processOracleUpdate(var8[var12].getPropertyName(), var7);
                  } else if (var10 == 101) {
                     String var19 = var6.getSysPropValue();
                     if (var19 == null) {
                        var19 = var6.getValue();
                     } else {
                        try {
                           var19 = System.getProperty(var19);
                        } catch (Exception var17) {
                           var19 = null;
                        }
                     }

                     this.processInternalPropertyUpdate(var6.getName(), var19);
                  } else {
                     JDBCLogger.logUnexpectedBeanChangeType(var9.toString(), var8[var12].toString());
                  }
                  break;
               case 2:
                  if (var10 == 100) {
                     JDBCPropertyBean var13 = (JDBCPropertyBean)((JDBCPropertyBean)var8[var12].getAddedObject());
                     String var14 = var13.getSysPropValue();
                     if (var14 == null) {
                        var14 = var13.getValue();
                     } else {
                        try {
                           var14 = System.getProperty(var14);
                        } catch (Exception var16) {
                           var14 = null;
                        }
                     }

                     this.processInternalPropertyUpdate(var13.getName(), var14);
                  } else {
                     JDBCLogger.logUnexpectedBeanAddType(var9.toString(), var8[var12].toString());
                  }
                  break;
               default:
                  JDBCLogger.logUnexpectedUpdateType(var9.toString(), var8[var12].toString());
            }
         }

         if (var11 >= 0) {
            this.processCPUpdate(var8[var11].getPropertyName(), var3);
         }

      }
   }

   private void processCPUpdate(String var1, JDBCConnectionPoolParamsBean var2) throws Exception {
      if (var1.equals("MaxCapacity")) {
         this.pool.setMaximumCapacity(var2.getMaxCapacity());
      } else if (var1.equals("MinCapacity")) {
         this.pool.setMinimumCapacity(var2.getMinCapacity());
      } else if (var1.equals("InitialCapacity")) {
         this.pool.setInitialCapacity(var2.getInitialCapacity());
      } else if (var1.equals("CapacityIncrement")) {
         this.pool.setCapacityIncrement(var2.getCapacityIncrement());
      } else if (var1.equals("HighestNumWaiters")) {
         this.pool.setHighestNumWaiters(var2.getHighestNumWaiters());
      } else if (var1.equals("InactiveConnectionTimeoutSeconds")) {
         this.pool.setInactiveResourceTimeoutSeconds(var2.getInactiveConnectionTimeoutSeconds());
      } else if (var1.equals("ConnectionReserveTimeoutSeconds")) {
         this.pool.setResourceReserveTimeoutSeconds(var2.getConnectionReserveTimeoutSeconds());
      } else if (var1.equals("ConnectionCreationRetryFrequencySeconds")) {
         this.pool.setResourceCreationRetrySeconds(var2.getConnectionCreationRetryFrequencySeconds());
      } else if (var1.equals("ShrinkFrequencySeconds")) {
         this.pool.setShrinkFrequencySeconds(var2.getShrinkFrequencySeconds());
      } else if (var1.equals("TestFrequencySeconds")) {
         if (this.pool != null) {
            this.pool.setTestFrequencySeconds(var2.getTestFrequencySeconds());
         } else if (this.multipool != null) {
            this.multipool.setHealthCheckFrequencySeconds(var2.getTestFrequencySeconds());
         }
      } else if (var1.equals("TestConnectionsOnReserve")) {
         this.pool.setTestOnReserve(var2.isTestConnectionsOnReserve());
      } else if (var1.equals("StatementCacheSize")) {
         this.pool.setStatementCacheSize(var2.getStatementCacheSize());
      } else if (var1.equals("TestTableName")) {
         this.pool.setTestTableName(var2.getTestTableName());
      } else if (var1.equals("ProfileType")) {
         this.pool.setProfileType(var2.getProfileType());
      } else if (var1.equals("ProfileHarvestFrequencySeconds")) {
         this.pool.setProfileHarvestFrequencySeconds(var2.getProfileHarvestFrequencySeconds());
      } else if (var1.equals("IgnoreInUseConnsEnabled")) {
         this.pool.setIgnoreInUseResources(var2.isIgnoreInUseConnectionsEnabled());
      } else if (var1.equals("SecondsToTrustAnIdlePoolConnection")) {
         this.pool.setSecondsToTrustAnIdlePoolConnection(var2.getSecondsToTrustAnIdlePoolConnection());
      } else if (var1.equals("ConnectionHarvestMaxCount")) {
         this.pool.setConnectionHarvestMaxCount(var2.getConnectionHarvestMaxCount());
      } else {
         if (!var1.equals("ConnectionHarvestTriggerCount")) {
            throw new NonDynamicPropertyUpdateException(var1 + " is not dynamically updatable. Please redeploy the updated JDBC DataSource for the changes to take effect.");
         }

         this.pool.setConnectionHarvestTriggerCount(var2.getConnectionHarvestTriggerCount());
      }

   }

   private void processDSUpdate(String var1, JDBCDataSourceParamsBean var2) throws Exception {
      if (var1.equals("FailoverIfBusy")) {
         this.multipool.setFailoverRequestIfBusy(var2.isFailoverRequestIfBusy());
      } else if (var1.equals("DataSourceList")) {
         this.multipool.setDataSourceList(var2.getDataSourceList());
      }

   }

   private void processOracleUpdate(String var1, JDBCOracleParamsBean var2) throws Exception {
      if (var1.equals("OracleEnableJavaNetFastPath")) {
         this.pool.setOracleEnableJavaNetFastPath(var2.isOracleEnableJavaNetFastPath());
      } else if (var1.equals("OracleOptimizeUtf8Conversion")) {
         this.pool.setOracleOptimizeUtf8Conversion(var2.isOracleOptimizeUtf8Conversion());
      } else if (var1.equals("FanEnabled")) {
         if (this.pool instanceof HAConnectionPool) {
            HAConnectionPool var3 = (HAConnectionPool)this.pool;
            var3.setFanEnabled(var2.isFanEnabled());
         } else {
            JDBCLogger.logFANEnabledNotAllowed(this.pool.getName());
         }
      }

   }

   private void processInternalPropertyUpdate(String var1, String var2) throws Exception {
      if (var1.equals("TestConnectionsOnRelease")) {
         this.pool.setTestOnRelease(Boolean.valueOf(var2));
      } else if (var1.equals("TestConnectionsOnCreate")) {
         this.pool.setTestOnCreate(Boolean.valueOf(var2));
      } else if (var1.equals("HighestNumUnavailable")) {
         this.pool.setHighestNumUnavailable(Integer.parseInt(var2));
      } else if (var1.equals("CountOfTestFailuresTillFlush")) {
         this.pool.setCountOfTestFailuresTillFlush(Integer.parseInt(var2));
      } else {
         if (!var1.equals("CountOfRefreshFailuresTillDisable")) {
            throw new NonDynamicPropertyUpdateException(var1 + " is not dynamically updatable. Please redeploy the updated JDBC DataSource for the changes to take effect.");
         }

         this.pool.setCountOfRefreshFailuresTillDisable(Integer.parseInt(var2));
      }

   }

   public static String constructDefaultJDBCSystemFilename(String var0) {
      var0 = var0.trim();
      if (var0.endsWith("-jdbc")) {
         var0 = var0.substring(0, var0.length() - 5);
      }

      return "jdbc/" + FileUtils.mapNameToFileName(var0) + "-jdbc.xml";
   }

   private boolean isGloballyScoped() {
      return this.appCtx.getSystemResourceMBean() != null;
   }

   private JDBCDataSourceBean[] getPoolBeans(JDBCDataSourceBean var1, ApplicationContext var2) throws ResourceException {
      String var3 = var1.getName();
      String var4 = DataSourceManager.getInternalProperty(var1, "LegacyPoolName");
      String var5 = var4 != null ? var4 : var3;
      JDBCDataSourceBean[] var6 = new JDBCDataSourceBean[1];
      if (getLegacyType(var1) == 0) {
         if (var1.getJDBCDataSourceParams().getDataSourceList() != null) {
            var6 = getSystemResourceBeans(var1.getJDBCDataSourceParams().getDataSourceList(), 0, var2);
            if (var6 == null) {
               var6 = getAppDeploymentBeans(var1.getJDBCDataSourceParams().getDataSourceList(), var2);
            }

            if (var6 == null) {
               var6 = getApplicationBeans(var1.getJDBCDataSourceParams().getDataSourceList());
            }

            this.checkMDSConfig(var1, var6);
         } else {
            var6[0] = var1;
         }
      } else {
         JDBCDataSourceBean var7 = null;
         JDBCSystemResourceMBean[] var8 = getDomainMBean(var2).getJDBCSystemResources();

         for(int var9 = 0; var9 < var8.length; ++var9) {
            JDBCDataSourceBean var10 = var8[var9].getJDBCResource();
            if (var10 != null) {
               int var11 = getLegacyType(var10);
               if ((var11 == 1 || var11 == 2) && var5.equals(var10.getName())) {
                  var7 = var10;
                  break;
               }
            }
         }

         if (var7 != null) {
            if (var7.getJDBCDataSourceParams().getDataSourceList() != null) {
               var6 = getSystemResourceBeans(var7.getJDBCDataSourceParams().getDataSourceList(), 1, var2);
            } else {
               var6[0] = var7;
            }
         } else {
            var6 = null;
         }
      }

      return var6;
   }

   private static boolean isMemberDSOfMultiDataSource(String var0, ApplicationContext var1) {
      JDBCSystemResourceMBean[] var2 = getDomainMBean(var1).getJDBCSystemResources();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         JDBCDataSourceBean var4 = var2[var3].getJDBCResource();
         if (var4 != null) {
            String var5 = var4.getJDBCDataSourceParams().getDataSourceList();
            if (var5 != null) {
               List var6 = JDBCHelper.getHelper().dsToList(var5);
               Iterator var7 = var6.iterator();

               while(var6.size() > 0 && var7.hasNext()) {
                  String var8 = (String)var7.next();
                  if (var8.equals(var0)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   private static boolean isMemberOfMultiDataSourceLLR(String var0, ApplicationContext var1) {
      JDBCSystemResourceMBean[] var2 = getDomainMBean(var1).getJDBCSystemResources();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         JDBCDataSourceBean var5 = var2[var4].getJDBCResource();
         if (var5 != null && var5.getName().equals(var0)) {
            String var3 = var5.getJDBCDataSourceParams().getDataSourceList();
            if (var3 != null) {
               List var6 = JDBCHelper.getHelper().dsToList(var3);
               Iterator var7 = var6.iterator();
               if (!var7.hasNext()) {
                  return false;
               }

               String var8 = (String)var7.next();

               for(int var9 = 0; var9 < var2.length; ++var9) {
                  JDBCDataSourceBean var10 = var2[var9].getJDBCResource();
                  if (var10.getName().equals(var8)) {
                     return var10.getJDBCDataSourceParams().getGlobalTransactionsProtocol().equals("LoggingLastResource");
                  }
               }
            }
         }
      }

      return false;
   }

   private void checkMDSConfig(JDBCDataSourceBean var1, JDBCDataSourceBean[] var2) throws ResourceException {
      if (var2 == null) {
         throw new ResourceException("Unable to locate configuration of data sources (" + var1.getJDBCDataSourceParams().getDataSourceList() + ") being used by multi data source " + var1.getName());
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] == null) {
               throw new ResourceException("Unable to locate complete configuration of data sources (" + var1.getJDBCDataSourceParams().getDataSourceList() + ") being used by multi data source " + var1.getName());
            }
         }

      }
   }

   public static JDBCDataSourceBean[] getJDBCDataSourceBean(String var0) {
      JDBCDataSourceBean[] var1 = getApplicationBeans(var0);
      if (var1 == null) {
         var1 = getSystemResourceBeans(var0, 0, (ApplicationContext)null);
      }

      if (var1 == null) {
         var1 = getAppDeploymentBeans(var0, (ApplicationContext)null);
      }

      if (var1 == null) {
         var1 = getSystemResourceBeans(var0, 1, (ApplicationContext)null);
      }

      return var1;
   }

   private static JDBCDataSourceBean[] getSystemResourceBeans(String var0, int var1, ApplicationContext var2) {
      StringTokenizer var3 = new StringTokenizer(var0, ",");
      JDBCDataSourceBean[] var4 = new JDBCDataSourceBean[var3.countTokens()];
      int var5 = 0;
      JDBCSystemResourceMBean[] var6 = getDomainMBean(var2).getJDBCSystemResources();

      while(true) {
         while(var3.hasMoreTokens()) {
            String var7 = var3.nextToken();

            for(int var8 = 0; var8 < var6.length; ++var8) {
               JDBCDataSourceBean var9 = var6[var8].getJDBCResource();
               if (var9 != null && getLegacyType(var9) == var1 && var7.equals(var9.getName())) {
                  var4[var5++] = var9;
                  break;
               }
            }
         }

         if (var5 == 0) {
            return null;
         }

         return var4;
      }
   }

   private static JDBCDataSourceBean[] getAppDeploymentBeans(String var0, ApplicationContext var1) {
      StringTokenizer var2 = new StringTokenizer(var0, ",");
      JDBCDataSourceBean[] var3 = new JDBCDataSourceBean[var2.countTokens()];
      int var4 = 0;
      AppDeploymentMBean[] var5 = getDomainMBean(var1).getAppDeployments();

      while(true) {
         while(var2.hasMoreTokens()) {
            String var6 = var2.nextToken();

            for(int var7 = 0; var7 < var5.length; ++var7) {
               String var8 = var5[var7].getSourcePath();
               if (var8 != null && var8.endsWith("-jdbc.xml")) {
                  String var9 = var5[var7].getApplicationName();
                  ApplicationContextInternal var10 = ApplicationAccess.getApplicationAccess().getApplicationContext(var9);
                  if (var10 == null) {
                     JDBCLogger.logStandaloneMultiDataSourceMemberNotFound(var1.getApplicationId(), var9);
                     return null;
                  }

                  JDBCDataSourceBean var11 = getJDBCModuleFromAppModules(var10.getApplicationModules(), var6);
                  if (var11 != null) {
                     var3[var4++] = var11;
                     break;
                  }
               }
            }
         }

         if (var4 == 0) {
            return null;
         }

         return var3;
      }
   }

   private static JDBCDataSourceBean[] getApplicationBeans(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0, ",");
      JDBCDataSourceBean[] var2 = new JDBCDataSourceBean[var1.countTokens()];
      int var3 = 0;
      if (ApplicationAccess.getApplicationAccess().getCurrentApplicationContext() == null) {
         return null;
      } else {
         Module[] var4 = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext().getApplicationModules();

         while(var1.hasMoreTokens()) {
            String var5 = var1.nextToken();
            JDBCDataSourceBean var6 = getJDBCModuleFromAppModules(var4, var5);
            if (var6 != null) {
               var2[var3++] = var6;
            }
         }

         if (var3 == 0) {
            return null;
         } else {
            return var2;
         }
      }
   }

   private static JDBCDataSourceBean getJDBCModuleFromAppModules(Module[] var0, String var1) {
      int var2 = var1.indexOf("@");
      if (var2 != -1) {
         String var7 = var1.substring(var2 + 1);
         var2 = var7.indexOf("@");
         if (var2 == -1) {
            var2 = var1.indexOf("@");
            var7 = var1.substring(0, var2);
         } else {
            var7 = var7.substring(0, var2);
         }

         for(int var8 = 0; var8 < var0.length; ++var8) {
            if ("jdbc".equals(var0[var8].getType()) && var7.equals(var0[var8].getId())) {
               DescriptorBean[] var9 = var0[var8].getDescriptors();
               JDBCDataSourceBean var6 = (JDBCDataSourceBean)var9[0];
               return var6;
            }
         }

         return null;
      } else {
         for(int var3 = 0; var3 < var0.length; ++var3) {
            if ("jdbc".equals(var0[var3].getType())) {
               DescriptorBean[] var4 = var0[var3].getDescriptors();
               if (var4 != null && var4.length == 1 && var4[0] instanceof JDBCDataSourceBean) {
                  JDBCDataSourceBean var5 = (JDBCDataSourceBean)var4[0];
                  if (var1.equals(var5.getName())) {
                     return var5;
                  }
               }
            }
         }

         return null;
      }
   }

   private static DomainMBean getDomainMBean(ApplicationContext var0) {
      DomainMBean var1 = null;
      if (var0 != null) {
         try {
            var1 = ((ApplicationContextInternal)var0).getProposedDomain();
         } catch (Throwable var3) {
         }
      }

      if (var1 == null) {
         var1 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain();
      }

      return var1;
   }

   private static int getLegacyType(JDBCDataSourceBean var0) {
      int var1 = 0;
      String var2 = DataSourceManager.getInternalProperty(var0, "LegacyType");
      if (var2 != null) {
         var1 = Integer.parseInt(var2);
      }

      return var1;
   }
}
