package weblogic.ejb.container.deployer;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.SessionSynchronization;
import javax.jws.WebService;
import javax.xml.ws.WebServiceProvider;
import kodo.jdbc.conf.descriptor.PersistenceUnitConfigurationBean;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.dd.DDDefaults;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.interfaces.PoolIntf;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.manager.ReplicatedStatefulSessionManager;
import weblogic.ejb.container.manager.StatefulSessionManager;
import weblogic.ejb.container.manager.StatelessManager;
import weblogic.ejb.spi.EJBCache;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb.spi.WSObjectFactory;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.PoolBean;
import weblogic.j2ee.descriptor.wl.StatefulSessionCacheBean;
import weblogic.j2ee.descriptor.wl.StatefulSessionClusteringBean;
import weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.TransactionDescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.GenericClassLoader;

class SessionBeanInfoImpl extends ClientDrivenBeanInfoImpl implements SessionBeanInfo {
   private final boolean usesBeanManagedTx;
   private final boolean isStateful;
   private final boolean isEndpointView;
   private final String generatedBeanClassName;
   private final String generatedBeanInterfaceName;
   private Class generatedBeanClass;
   private Class generatedBeanInterface;
   private int replicationType = 1;
   private boolean implementsSessionSynchronization;
   private String swapDirectoryName;
   private long idleTimeoutMS;
   private long sessionTimeoutMS;
   private boolean statefulSessionSerializesConcurrentCalls = false;
   private boolean allowRemoveDuringTx = false;
   private boolean passivateDuringReplication = true;
   private boolean calculateDeltaUsingReflection = false;

   public SessionBeanInfoImpl(DeploymentInfo var1, CompositeMBeanDescriptor var2, GenericClassLoader var3) throws ClassNotFoundException, WLDeploymentException {
      super(var1, var2, var3);
      SessionBeanBean var4 = (SessionBeanBean)var2.getBean();

      assert "bean".equalsIgnoreCase(var4.getTransactionType()) || "container".equalsIgnoreCase(var4.getTransactionType());

      this.usesBeanManagedTx = "bean".equalsIgnoreCase(var4.getTransactionType());
      this.implementsSessionSynchronization = SessionSynchronization.class.isAssignableFrom(this.getBeanClass());
      this.idleTimeoutMS = (long)var2.getIdleTimeoutSecondsCache() * 1000L;
      this.sessionTimeoutMS = (long)var2.getSessionTimeoutSeconds() * 1000L;
      Debug.assertion("stateful".equalsIgnoreCase(var4.getSessionType()) || "stateless".equalsIgnoreCase(var4.getSessionType()));
      this.isStateful = "stateful".equalsIgnoreCase(var4.getSessionType());
      if (!"stateless".equalsIgnoreCase(var4.getSessionType()) || !this.beanClass.isAnnotationPresent(WebService.class) && !this.beanClass.isAnnotationPresent(WebServiceProvider.class)) {
         this.isEndpointView = false;
      } else {
         this.isEndpointView = true;
      }

      if (this.isStateful) {
         WeblogicEnterpriseBeanBean var5 = var2.getWl60Bean();
         StatefulSessionDescriptorBean var6 = var5.getStatefulSessionDescriptor();
         if (null != var6) {
            this.swapDirectoryName = var6.getPersistentStoreDir();
            StatefulSessionClusteringBean var7 = var6.getStatefulSessionClustering();
            if (var7 != null) {
               this.passivateDuringReplication = var7.isPassivateDuringReplication();
               this.calculateDeltaUsingReflection = var7.isCalculateDeltaUsingReflection();
            }
         }

         this.statefulSessionSerializesConcurrentCalls = var6.isAllowConcurrentCalls();
         this.allowRemoveDuringTx = var6.isAllowRemoveDuringTransaction();
         if (this.needSetReplicationType(var2)) {
            String var10 = var6.getStatefulSessionClustering().getReplicationType();
            if ("InMemory".equals(var10)) {
               this.replicationType = 2;
               if (isServer()) {
                  AuthenticatedSubject var8 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
                  if (ManagementService.getRuntimeAccess(var8).getServer().getCluster() == null) {
                     EJBLogger.logWarningOnSFSBInMemoryReplicationFeature(this.getEJBName());
                  }
               }
            }
         }
      }

      if (this.swapDirectoryName == null) {
         this.swapDirectoryName = "pstore";
      }

      NamingConvention var9 = new NamingConvention(var4.getEjbClass(), var4.getEjbName());
      this.generatedBeanClassName = var9.getGeneratedBeanClassName();
      if (this.getDeploymentInfo().isEnableBeanClassRedeploy()) {
         var3.excludeClass(this.generatedBeanClassName);
      }

      this.generatedBeanInterfaceName = var9.getGeneratedBeanInterfaceName();
   }

   protected boolean needSetReplicationType(CompositeMBeanDescriptor var1) {
      return this.getJNDIName() != null;
   }

   public String getGeneratedBeanClassName() {
      return this.generatedBeanClassName;
   }

   public String getGeneratedBeanInterfaceName() {
      return this.generatedBeanInterfaceName;
   }

   public Class getGeneratedBeanClass() {
      try {
         if (this.generatedBeanClass == null) {
            this.generatedBeanClass = this.loadClass(this.generatedBeanClassName);
         }

         return this.generatedBeanClass;
      } catch (ClassNotFoundException var2) {
         throw new AssertionError("Unable to load class " + this.generatedBeanClassName, var2);
      }
   }

   public Class getGeneratedBeanInterface() {
      try {
         if (this.generatedBeanInterface == null) {
            this.generatedBeanInterface = this.loadClass(this.generatedBeanInterfaceName);
         }

         return this.generatedBeanInterface;
      } catch (ClassNotFoundException var2) {
         throw new AssertionError("Unable to load class " + this.generatedBeanInterfaceName, var2);
      }
   }

   public boolean statefulSessionSerializesConcurrentCalls() {
      return this.statefulSessionSerializesConcurrentCalls;
   }

   public int getReplicationType() {
      return this.replicationType;
   }

   public boolean isStateful() {
      return this.isStateful;
   }

   public boolean usesBeanManagedTx() {
      return this.usesBeanManagedTx;
   }

   public boolean implementsSessionSynchronization() {
      return this.implementsSessionSynchronization;
   }

   public String getSwapDirectoryName() {
      return this.swapDirectoryName;
   }

   public long getIdleTimeoutMS() {
      return this.idleTimeoutMS;
   }

   public long getSessionTimeoutMS() {
      return this.sessionTimeoutMS;
   }

   public boolean isTimerDriven() {
      return this.isStateful() ? false : super.isTimerDriven();
   }

   public boolean getCalculateDeltaUsingReflection() {
      return this.calculateDeltaUsingReflection;
   }

   public boolean getPassivateDuringReplication() {
      return this.passivateDuringReplication;
   }

   public BeanManager getBeanManagerInstance(EJBComponentRuntimeMBeanImpl var1) {
      if (this.isStateful()) {
         return (BeanManager)(this.getReplicationType() == 2 ? new ReplicatedStatefulSessionManager(var1) : new StatefulSessionManager(var1));
      } else {
         return new StatelessManager(var1);
      }
   }

   protected EJBCache getCache(Map var1) {
      return null;
   }

   protected short getTxAttribute(MethodInfo var1, Class var2) {
      if (this.usesBeanManagedTx) {
         return 0;
      } else {
         String var3;
         if (!EJBHome.class.isAssignableFrom(var2) && !EJBLocalHome.class.isAssignableFrom(var2)) {
            if (EJBObject.class.isAssignableFrom(var2) || EJBLocalObject.class.isAssignableFrom(var2)) {
               var3 = var1.getMethodName();
               String[] var4 = var1.getMethodParams();
               if (var3.equals("remove") && (var4 == null || var4.length == 0)) {
                  if (this.allowRemoveDuringTx) {
                     return 2;
                  }

                  return 0;
               }
            }
         } else {
            var3 = var1.getMethodName();
            if (var3.equals("remove") || var3.startsWith("create")) {
               return 0;
            }
         }

         return var1.getTransactionAttribute();
      }
   }

   public void assignDefaultTXAttributesIfNecessary() {
      if (this.usesBeanManagedTx) {
         Iterator var1 = this.getAllMethodInfosIterator();

         while(var1.hasNext()) {
            ((MethodInfo)var1.next()).setTransactionAttribute((short)0);
         }
      } else {
         ArrayList var4 = new ArrayList();
         if (this.hasDeclaredRemoteHome()) {
            var4.addAll(this.getAllHomeMethodInfos());
            var4.add(this.getRemoteMethodInfo("remove()"));
            var4.add(this.getRemoteMethodInfo("getEJBHome()"));
            var4.add(this.getRemoteMethodInfo("getHandle()"));
            var4.add(this.getRemoteMethodInfo("getPrimaryKey()"));
            var4.add(this.getRemoteMethodInfo("isIdentical(javax.ejb.EJBObject)"));
         }

         if (this.hasDeclaredLocalHome()) {
            var4.addAll(this.getAllLocalHomeMethodInfos());
            var4.add(this.getLocalMethodInfo("remove()"));
            var4.add(this.getLocalMethodInfo("getEJBLocalHome()"));
            var4.add(this.getLocalMethodInfo("getPrimaryKey()"));
            var4.add(this.getLocalMethodInfo("isIdentical(javax.ejb.EJBLocalObject)"));
            var4.add(this.getLocalMethodInfo("getLocalHandle()"));
         }

         Iterator var2 = var4.iterator();

         while(var2.hasNext()) {
            MethodInfo var3 = (MethodInfo)var2.next();
            if (var3 != null && var3.getTransactionAttribute() == -1) {
               var3.setTransactionAttribute(DDDefaults.getTransactionAttribute(this));
            }
         }

         super.assignDefaultTXAttributesIfNecessary();
      }

   }

   public void updateImplClassLoader() throws WLDeploymentException {
      super.updateImplClassLoader();

      try {
         this.generatedBeanClass = this.loadClass(this.generatedBeanClassName);
      } catch (ClassNotFoundException var4) {
         throw new WLDeploymentException("Couldn't load updated impl class: " + var4);
      }

      if (this.implementsSessionSynchronization != SessionSynchronization.class.isAssignableFrom(this.getBeanClass())) {
         throw new WLDeploymentException("Unable to perform a partial redeploy due to a SessionSynchronization change in the bean class.");
      } else {
         BeanManager var1 = this.getBeanManager();

         try {
            var1.beanImplClassChangeNotification();
         } catch (UnsupportedOperationException var3) {
            throw new WLDeploymentException("Bean Manager does not support partial updates");
         }
      }
   }

   public void updatePoolIdleTimeoutSeconds(int var1) {
      if (!this.isStateful) {
         BeanManager var2 = this.getBeanManager();
         if (var2 instanceof StatelessManager) {
            PoolIntf var3 = ((StatelessManager)var2).getPool();
            var3.updateIdleTimeoutSeconds(var1);
         }
      }

      if (debugLogger.isDebugEnabled()) {
         debug("updated Pool IdleTimeoutSeconds to " + var1 + " for EJB " + this.getDisplayName());
      }

   }

   public void updateMaxBeansInFreePool(int var1) {
      if (!this.isStateful) {
         BeanManager var2 = this.getBeanManager();
         if (var2 instanceof StatelessManager) {
            PoolIntf var3 = ((StatelessManager)var2).getPool();
            var3.updateMaxBeansInFreePool(var1);
         }
      }

      if (debugLogger.isDebugEnabled()) {
         debug("updated MaxBeansInFreePool to " + var1 + " for EJB " + this.getDisplayName());
      }

   }

   public WSObjectFactory getWSObjectFactory() {
      return this.webserviceObjectFactory;
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      if (debugLogger.isDebugEnabled()) {
         debug("prepareUpdate: " + var1);
      }

      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      DescriptorBean var3 = var1.getProposedBean();
      int var4 = 0;

      while(var4 < var2.length) {
         BeanUpdateEvent.PropertyUpdate var5 = var2[var4];
         switch (var5.getUpdateType()) {
            case 1:
               String var6 = var5.getPropertyName();
               if (debugLogger.isDebugEnabled()) {
                  debug("Preparing property of type: " + var6);
               }

               PersistenceUnitConfigurationBean var7;
               int var8;
               if (var6.equals("LockTimeout")) {
                  if (debugLogger.isDebugEnabled()) {
                     debug("Preparing persistence property: " + var6);
                  }

                  var7 = (PersistenceUnitConfigurationBean)var3;
                  var8 = var7.getLockTimeout();
                  this.prepareLockTimeout(var8);
               } else if (var6.equals("DataCacheTimeout")) {
                  if (debugLogger.isDebugEnabled()) {
                     debug("Preparing persistence property: " + var6);
                  }

                  var7 = (PersistenceUnitConfigurationBean)var3;
                  var8 = var7.getDataCacheTimeout();
                  this.prepareDataCacheTimeout(var8);
               } else if (var6.equals("FetchBatchSize")) {
                  if (debugLogger.isDebugEnabled()) {
                     debug("Preparing persistence property: " + var6);
                  }

                  var7 = (PersistenceUnitConfigurationBean)var3;
                  var8 = var7.getFetchBatchSize();
                  this.prepareFetchBatchSize(var8);
               }
            default:
               ++var4;
               break;
            case 2:
            case 3:
               throw new AssertionError("Unexpected BeanUpdateEvent: " + var1);
         }
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("rollbackUpdate: " + var1);
      }

   }

   public void activateUpdate(BeanUpdateEvent var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("activateUpdate: " + var1);
      }

      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      DescriptorBean var3 = var1.getProposedBean();
      int var4 = 0;

      while(var4 < var2.length) {
         BeanUpdateEvent.PropertyUpdate var5 = var2[var4];
         switch (var5.getUpdateType()) {
            case 1:
               String var6 = var5.getPropertyName();
               if (debugLogger.isDebugEnabled()) {
                  debug("Changing property of type: " + var6);
               }

               PoolBean var7;
               if (var6.equals("MaxBeansInFreePool")) {
                  assert !this.isStateful();

                  var7 = (PoolBean)var3;
                  this.updateMaxBeansInFreePool(var7.getMaxBeansInFreePool());
               } else {
                  StatefulSessionCacheBean var10;
                  if (var6.equals("MaxBeansInCache")) {
                     assert this.isStateful();

                     var10 = (StatefulSessionCacheBean)var3;
                     this.updateMaxBeansInCache(var10.getMaxBeansInCache());
                  } else if (var6.equals("TransTimeoutSeconds")) {
                     TransactionDescriptorBean var11 = (TransactionDescriptorBean)var3;
                     this.updateTransactionTimeoutSeconds(var11.getTransTimeoutSeconds());
                  } else if (var6.equals("IdleTimeoutSeconds")) {
                     if (var3 instanceof StatefulSessionCacheBean) {
                        assert this.isStateful();

                        var10 = (StatefulSessionCacheBean)var3;
                        this.updateCacheIdleTimeoutSeconds(var10.getIdleTimeoutSeconds());
                     } else {
                        var7 = (PoolBean)var3;
                        this.updatePoolIdleTimeoutSeconds(var7.getIdleTimeoutSeconds());
                     }
                  } else {
                     String var8;
                     int var9;
                     PersistenceUnitConfigurationBean var12;
                     if (var6.equals("LockTimeout")) {
                        if (debugLogger.isDebugEnabled()) {
                           debug("Changing persistence property: " + var6);
                        }

                        var12 = (PersistenceUnitConfigurationBean)var3;
                        var8 = var12.getName();
                        var9 = var12.getLockTimeout();
                        this.updateLockTimeout(var9, var8);
                     } else if (var6.equals("DataCacheTimeout")) {
                        if (debugLogger.isDebugEnabled()) {
                           debug("Changing persistence property: " + var6);
                        }

                        var12 = (PersistenceUnitConfigurationBean)var3;
                        var8 = var12.getName();
                        var9 = var12.getDataCacheTimeout();
                        this.updateDataCacheTimeout(var9, var8);
                     } else {
                        if (!var6.equals("FetchBatchSize")) {
                           throw new AssertionError("Unexpected propertyName: " + var6);
                        }

                        if (debugLogger.isDebugEnabled()) {
                           debug("Changing persistence property: " + var6);
                        }

                        var12 = (PersistenceUnitConfigurationBean)var3;
                        var8 = var12.getName();
                        var9 = var12.getFetchBatchSize();
                        this.updateFetchBatchSize(var9, var8);
                     }
                  }
               }
            default:
               ++var4;
               break;
            case 2:
            case 3:
               throw new AssertionError("Unexpected BeanUpdateEvent: " + var1);
         }
      }

   }

   private static void debug(String var0) {
      debugLogger.debug("[SessionBeanInfoImpl] " + var0);
   }

   public boolean isSessionBean() {
      return true;
   }

   public boolean isAllowRemoveDuringTx() {
      return this.allowRemoveDuringTx;
   }

   public boolean isEndpointView() {
      return this.isEndpointView;
   }
}
