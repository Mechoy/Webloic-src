package weblogic.ejb.container.deployer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import javax.naming.Context;
import kodo.jdbc.conf.descriptor.PersistenceUnitConfigurationBean;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.ejb.QueryHome;
import weblogic.ejb.QueryLocalHome;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.dd.DDDefaults;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.InvalidationBeanManager;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.interfaces.PoolIntf;
import weblogic.ejb.container.interfaces.QueryCache;
import weblogic.ejb.container.interfaces.ReadOnlyManager;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.MethodDescriptor;
import weblogic.ejb.container.manager.BaseEntityManager;
import weblogic.ejb.container.manager.BeanManagedPersistenceManager;
import weblogic.ejb.container.manager.DBManager;
import weblogic.ejb.container.manager.ExclusiveEntityManager;
import weblogic.ejb.container.manager.ReadOnlyEntityManager;
import weblogic.ejb.container.manager.TTLManager;
import weblogic.ejb.container.persistence.spi.PersistenceManager;
import weblogic.ejb.spi.EJBCache;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.QueryBean;
import weblogic.j2ee.descriptor.wl.ApplicationEntityCacheBean;
import weblogic.j2ee.descriptor.wl.AutomaticKeyGenerationBean;
import weblogic.j2ee.descriptor.wl.EntityCacheBean;
import weblogic.j2ee.descriptor.wl.EntityCacheRefBean;
import weblogic.j2ee.descriptor.wl.PoolBean;
import weblogic.j2ee.descriptor.wl.TransactionDescriptorBean;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;
import weblogic.utils.classloaders.GenericClassLoader;

public final class EntityBeanInfoImpl extends ClientDrivenBeanInfoImpl implements EntityBeanInfo {
   protected static EJBComplianceTextFormatter fmt;
   private final int concurrencyStrategy;
   private final boolean isBeanManagedPersistence;
   private final String persistenceUseIdentifier;
   private final String persistenceUseVersion;
   private final String persistenceUseStorage;
   private boolean cacheBetweenTransactions;
   private boolean boxCarUpdates;
   private String isModifiedMethodName;
   private boolean unknownPK = false;
   private final String primaryKeyClassName;
   private final Collection allQueries;
   private final boolean isReentrant;
   private final CMPInfoImpl cmpInfo;
   private final String generatedBeanClassName;
   private final String generatedBeanInterfaceName;
   private Class generatedBeanClass;
   private Class generatedBeanInterface;
   private Class pkClass;
   private boolean disableReadyInstances;
   private final String invalidationTargetEJBName;
   private String jarFileName;
   private PersistenceManager persistenceManager = null;
   private BeanManager beanManager = null;
   private boolean singleInstanceReadOnly = false;
   private boolean enableDynamicQueries = false;
   private String entityCacheName = null;
   private int estimatedBeanSize = -1;
   private String categoryCmpField = null;

   public EntityBeanInfoImpl(DeploymentInfo var1, CompositeMBeanDescriptor var2, GenericClassLoader var3) throws ClassNotFoundException, WLDeploymentException {
      super(var1, var2, var3);
      this.jarFileName = var2.getEJBDescriptor().getJarFileName();
      EntityBeanBean var4 = (EntityBeanBean)var2.getBean();
      fmt = new EJBComplianceTextFormatter();
      this.isBeanManagedPersistence = var2.isBeanManagedPersistence();
      this.isModifiedMethodName = var2.getIsModifiedMethodName();
      this.boxCarUpdates = var2.getDelayUpdatesUntilEndOfTx();
      this.primaryKeyClassName = var4.getPrimKeyClass();
      if (this.primaryKeyClassName.equals("java.lang.Object")) {
         this.unknownPK = true;
      }

      this.isReentrant = var4.isReentrant();
      NamingConvention var5 = new NamingConvention(var4.getEjbClass(), var2.getEJBName());

      try {
         this.pkClass = this.loadClass(this.primaryKeyClassName);
      } catch (ClassNotFoundException var10) {
         throw new ClassNotFoundException(fmt.ENTITYBEANINFOIMPL(this.primaryKeyClassName));
      }

      this.checkClassLoaders(var2, this.pkClass);
      if (!this.isBeanManagedPersistence) {
         this.cmpInfo = new CMPInfoImpl(this, var2);
         this.persistenceUseIdentifier = this.cmpInfo.getPersistenceUseIdentifier();
         this.persistenceUseVersion = this.cmpInfo.getPersistenceUseVersion();
         this.persistenceUseStorage = this.cmpInfo.getPersistenceUseStorage();
         if (this.persistenceUseIdentifier != null) {
            this.generatedBeanClassName = var5.getCmpBeanClassName(this.persistenceUseIdentifier);
            if (this.getDeploymentInfo().isEnableBeanClassRedeploy()) {
               var3.excludeClass(this.generatedBeanClassName);
            }

            this.cmpInfo.setGeneratedBeanClassName(this.generatedBeanClassName);
         } else {
            this.generatedBeanClassName = null;
         }

         this.allQueries = new ArrayList();
         Collection var6 = this.cmpInfo.getAllQueries();
         Enumeration var7 = Collections.enumeration(var6);

         while(var7.hasMoreElements()) {
            QueryBean var8 = (QueryBean)var7.nextElement();
            EntityBeanQueryImpl var9 = new EntityBeanQueryImpl(var8);
            this.allQueries.add(var9);
         }
      } else {
         this.persistenceUseIdentifier = "N/A: not a CMP Bean";
         this.persistenceUseVersion = "N/A: not a CMP Bean";
         this.persistenceUseStorage = "N/A: not a CMP Bean";
         this.generatedBeanClassName = var5.getGeneratedBeanClassName();
         var3.excludeClass(this.generatedBeanClassName);
         this.generatedBeanClass = null;
         this.allQueries = null;
         this.cmpInfo = null;
      }

      this.generatedBeanInterfaceName = var5.getGeneratedBeanInterfaceName();
      if (var2.hasEntityCacheReference()) {
         this.entityCacheName = var2.getEntityCacheName();
         this.estimatedBeanSize = var2.getEstimatedBeanSize();
      }

      String var11 = var2.getConcurrencyStrategy();
      this.concurrencyStrategy = DDUtils.concurrencyStringToInt(var11);
      this.cacheBetweenTransactions = var2.getCacheBetweenTransactions();
      this.invalidationTargetEJBName = var2.getInvalidationTargetEJBName();
      this.disableReadyInstances = var2.getDisableReadyIntances();
      this.initializeDynamicQueryMethodInfos(var2);
   }

   public void activate(Context var1, Map var2, Map var3, DeploymentInfo var4, Context var5) throws WLDeploymentException {
      super.activate(var1, var2, var3, var4, var5);
      if (!this.isBeanManagedPersistence) {
         Iterator var6 = this.getMethodDescriptors().iterator();

         while(var6.hasNext()) {
            MethodDescriptor var7 = (MethodDescriptor)var6.next();
            Method var8 = var7.getMethod();
            if (this.cmpInfo.isQueryCachingEnabled(var8)) {
               var7.setMethodId(this.cmpInfo.getQueryCachingEnabledFinderIndex(var8));
               if (this.cmpInfo.isEnableEagerRefresh(var8)) {
                  ((TTLManager)this.beanManager).addEagerRefreshMethod(var8, var7.getMethodId());
               }
            }
         }
      }

   }

   public int getInstanceLockOrder() {
      return !this.isBeanManagedPersistence ? this.cmpInfo.getInstanceLockOrder() : 100;
   }

   public boolean isReadOnlyWithSingleInstance() {
      return this.singleInstanceReadOnly;
   }

   public boolean isReadOnly() {
      return this.concurrencyStrategy == 5;
   }

   public boolean isOptimistic() {
      return this.concurrencyStrategy == 6;
   }

   public int getConcurrencyStrategy() {
      return this.concurrencyStrategy;
   }

   public boolean usesBeanManagedTx() {
      return false;
   }

   public Class getPrimaryKeyClass() {
      return this.pkClass;
   }

   public boolean isUnknownPrimaryKey() {
      return this.unknownPK;
   }

   public boolean getIsBeanManagedPersistence() {
      return this.isBeanManagedPersistence;
   }

   public String getPersistenceUseIdentifier() {
      return this.persistenceUseIdentifier;
   }

   public String getPersistenceUseVersion() {
      return this.persistenceUseVersion;
   }

   public String getPersistenceUseStorage() {
      return this.persistenceUseStorage;
   }

   public boolean getCacheBetweenTransactions() {
      return this.cacheBetweenTransactions;
   }

   public boolean getDisableReadyInstances() {
      return this.disableReadyInstances;
   }

   public boolean getBoxCarUpdates() {
      return this.boxCarUpdates;
   }

   public String getPrimaryKeyClassName() {
      return this.primaryKeyClassName;
   }

   public String getIsModifiedMethodName() {
      return this.isModifiedMethodName;
   }

   public Collection getAllQueries() {
      return this.allQueries;
   }

   public boolean isReentrant() {
      return this.isReentrant;
   }

   public CMPInfo getCMPInfo() {
      return this.cmpInfo;
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
         throw new AssertionError(var2);
      }
   }

   public Class getGeneratedBeanInterface() {
      try {
         if (this.generatedBeanInterface == null) {
            this.generatedBeanInterface = this.loadClass(this.generatedBeanInterfaceName);
         }

         return this.generatedBeanInterface;
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   public PersistenceManager getPersistenceManager() {
      if (this.persistenceManager == null) {
         if (this.getIsBeanManagedPersistence()) {
            this.persistenceManager = new BeanManagedPersistenceManager();
         } else {
            assert this.getCMPInfo() != null;

            assert this.getCMPInfo().getPersistenceType() != null;

            this.persistenceManager = this.getCMPInfo().getPersistenceType().getPersistenceManager();
         }
      }

      return this.persistenceManager;
   }

   public BeanManager getBeanManagerInstance(EJBComponentRuntimeMBeanImpl var1) {
      if (this.beanManager == null) {
         switch (this.getConcurrencyStrategy()) {
            case 1:
               if (!this.getIsBeanManagedPersistence() && this.getCMPInfo().getPersistenceType().hasExclusiveManager()) {
                  this.beanManager = this.getCMPInfo().getPersistenceType().getExclusiveManager();
               } else {
                  this.beanManager = new ExclusiveEntityManager(var1);
               }
               break;
            case 2:
               if (!this.getIsBeanManagedPersistence() && this.getCMPInfo().getPersistenceType().hasDatabaseManager()) {
                  this.beanManager = this.getCMPInfo().getPersistenceType().getDatabaseManager();
               } else {
                  this.beanManager = new DBManager(var1);
               }
               break;
            case 3:
            default:
               throw new AssertionError("Unexpected value: " + this.getConcurrencyStrategy());
            case 4:
               if (!this.getIsBeanManagedPersistence() && this.getCMPInfo().getPersistenceType().hasReadonlyManager()) {
                  this.beanManager = this.getCMPInfo().getPersistenceType().getReadonlyManager();
               } else {
                  this.beanManager = new ReadOnlyEntityManager(var1);
               }
               break;
            case 5:
               if (!this.getIsBeanManagedPersistence() && this.getCMPInfo().getPersistenceType().hasReadonlyManager()) {
                  this.beanManager = this.getCMPInfo().getPersistenceType().getReadonlyManager();
               } else {
                  this.beanManager = new TTLManager(var1);
               }
               break;
            case 6:
               if (!this.getIsBeanManagedPersistence() && this.getCMPInfo().getPersistenceType().hasDatabaseManager()) {
                  this.beanManager = this.getCMPInfo().getPersistenceType().getDatabaseManager();
               } else if (this.cacheBetweenTransactions) {
                  this.beanManager = new TTLManager(var1);
               } else {
                  this.beanManager = new DBManager(var1);
               }
         }
      }

      return this.beanManager;
   }

   private String beanNameToHomeName(String var1) {
      StringBuffer var2 = new StringBuffer(var1);
      var2.delete(0, 4);
      if (var1.startsWith("ejbF")) {
         return "f" + var2;
      } else if (var1.startsWith("ejbC")) {
         return "c" + var2;
      } else {
         throw new AssertionError("Unexpected method: " + var1);
      }
   }

   protected EJBCache getCache(Map var1) throws WLDeploymentException {
      EJBCache var2 = null;
      if (this.entityCacheName != null) {
         Loggable var3;
         if (var1 == null) {
            var3 = EJBLogger.logmustUseTwoPhaseDeploymentLoggable(this.getEJBName(), this.entityCacheName);
            throw new WLDeploymentException(var3.getMessage());
         }

         var2 = (EJBCache)var1.get(this.entityCacheName);
         if (var2 == null) {
            var3 = EJBLogger.logmissingCacheDefinitionLoggable(this.getEJBName(), this.entityCacheName);
            throw new WLDeploymentException(var3.getMessage());
         }
      }

      return var2;
   }

   protected QueryCache getQueryCache(Map var1) throws WLDeploymentException {
      QueryCache var2 = null;
      if (this.entityCacheName != null) {
         if (var1 == null) {
            Loggable var3 = EJBLogger.logmustUseTwoPhaseDeploymentLoggable(this.getEJBName(), this.entityCacheName);
            throw new WLDeploymentException(var3.getMessage());
         }

         var2 = (QueryCache)var1.get(this.entityCacheName);
      }

      return var2;
   }

   public int getEstimatedBeanSize() {
      return this.estimatedBeanSize;
   }

   public String getCacheName() {
      return this.entityCacheName;
   }

   protected short getTxAttribute(MethodInfo var1, Class var2) {
      return this.getConcurrencyStrategy() == 4 ? 0 : var1.getTransactionAttribute();
   }

   public void assignDefaultTXAttributesIfNecessary() {
      ArrayList var1 = new ArrayList();
      if (this.hasRemoteClientView()) {
         var1.add(this.getRemoteMethodInfo("getEJBHome()"));
         var1.add(this.getRemoteMethodInfo("getHandle()"));
         var1.add(this.getRemoteMethodInfo("getPrimaryKey()"));
         var1.add(this.getRemoteMethodInfo("isIdentical(javax.ejb.EJBObject)"));
         var1.add(this.getHomeMethodInfo("getEJBMetaData()"));
         var1.add(this.getHomeMethodInfo("getHomeHandle()"));
      }

      if (this.hasLocalClientView()) {
         var1.add(this.getLocalMethodInfo("getEJBLocalHome()"));
         var1.add(this.getLocalMethodInfo("getPrimaryKey()"));
         var1.add(this.getLocalMethodInfo("isIdentical(javax.ejb.EJBLocalObject)"));
         var1.add(this.getLocalMethodInfo("getLocalHandle()"));
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         MethodInfo var3 = (MethodInfo)var2.next();
         if (var3 != null && var3.getTransactionAttribute() == -1) {
            var3.setTransactionAttribute(DDDefaults.getTransactionAttribute(this));
         }
      }

      super.assignDefaultTXAttributesIfNecessary();
   }

   private boolean hasAMethodDescriptor(Method var1, Class var2) {
      String var3 = var1.getName();
      if (!var3.startsWith("ejbCreate") && !var3.startsWith("ejbFind")) {
         return false;
      } else {
         try {
            var2.getMethod(this.beanNameToHomeName(var3), var1.getParameterTypes());
            return true;
         } catch (NoSuchMethodException var5) {
            return false;
         }
      }
   }

   public String getInvalidationTargetEJBName() {
      return this.invalidationTargetEJBName;
   }

   public InvalidationBeanManager getInvalidationTargetBeanManager() {
      if (this.invalidationTargetEJBName == null) {
         return null;
      } else {
         DeploymentInfo var1 = this.getDeploymentInfo();
         EntityBeanInfo var2 = (EntityBeanInfo)var1.getBeanInfo(this.invalidationTargetEJBName);
         if (var2 == null) {
            throw new AssertionError("Unable to find entity bean in deployment with name: " + this.invalidationTargetEJBName);
         } else {
            BeanManager var3 = var2.getBeanManager();
            if (var3 == null) {
               throw new AssertionError("Unable to find entity bean in deployment with name: " + this.invalidationTargetEJBName);
            } else {
               assert var3 instanceof InvalidationBeanManager;

               return (InvalidationBeanManager)var3;
            }
         }
      }
   }

   public String getJarFileName() {
      return this.jarFileName;
   }

   public boolean isDynamicQueriesEnabled() {
      return this.enableDynamicQueries;
   }

   private void initializeDynamicQueryMethodInfos(CompositeMBeanDescriptor var1) {
      if (!this.isBeanManagedPersistence && this.getCMPInfo().uses20CMP()) {
         if (var1.isDynamicQueriesEnabled()) {
            this.enableDynamicQueries = true;
         }

         ArrayList var2;
         if (this.hasRemoteClientView()) {
            var2 = new ArrayList();
            var2.add(getRemoteCreateQueryMethod());
            this.createMethodInfoImpls(var2, "Home", this.homeMethods);
         }

         if (this.hasLocalClientView()) {
            var2 = new ArrayList();
            var2.add(getLocalCreateQueryMethod());
            this.createMethodInfoImpls(var2, "LocalHome", this.localHomeMethods);
         }
      }

   }

   public static Method getRemoteCreateQueryMethod() {
      return getCreateQueryMethod(QueryHome.class);
   }

   public static Method getLocalCreateQueryMethod() {
      return getCreateQueryMethod(QueryLocalHome.class);
   }

   public static String getCreateQuerySignature() {
      return DDUtils.getMethodSignature(getRemoteCreateQueryMethod());
   }

   public static Method getCreateQueryMethod(Class var0) {
      try {
         return var0.getMethod("createQuery");
      } catch (NoSuchMethodException var2) {
         throw new AssertionError("Couldn't find createQuery method");
      }
   }

   public void updateImplClassLoader() throws WLDeploymentException {
      super.updateImplClassLoader();

      try {
         this.generatedBeanClass = this.loadClass(this.generatedBeanClassName);
      } catch (ClassNotFoundException var4) {
         throw new WLDeploymentException("Couldn't load updated impl class: " + var4);
      }

      if (!this.isBeanManagedPersistence) {
         this.cmpInfo.beanImplClassChangeNotification();
      }

      BeanManager var1 = this.getBeanManager();

      try {
         var1.beanImplClassChangeNotification();
      } catch (UnsupportedOperationException var3) {
         throw new WLDeploymentException("Bean Manager does not support partial updates");
      }
   }

   public void updateReadTimeoutSeconds(int var1) {
      this.getCachingDescriptor().setReadTimeoutSeconds(var1);
      BeanManager var2 = this.getBeanManager();
      if (var2 instanceof ReadOnlyManager) {
         ((ReadOnlyManager)var2).updateReadTimeoutSeconds(var1);
      }

      if (debugLogger.isDebugEnabled()) {
         debug("updated ReadTimeoutSeconds to " + var1 + " for EJB " + this.getDisplayName());
      }

   }

   public void updateKeyCacheSize(int var1) {
      BeanManager var2 = this.getBeanManager();
      if (var2 instanceof BaseEntityManager) {
         ((BaseEntityManager)var2).updateKeyCacheSize(var1);
      }

      if (debugLogger.isDebugEnabled()) {
         debug("updated KeyCacheSize to " + var1 + " for EJB " + this.getDisplayName());
      }

   }

   public void updateMaxBeansInFreePool(int var1) {
      BeanManager var2 = this.getBeanManager();
      if (var2 instanceof BaseEntityManager) {
         PoolIntf var3 = ((BaseEntityManager)var2).getPool();
         var3.updateMaxBeansInFreePool(var1);
      }

      if (debugLogger.isDebugEnabled()) {
         debug("updated MaxBeansInFreePool to " + var1 + " for EJB " + this.getDisplayName());
      }

   }

   public void updatePoolIdleTimeoutSeconds(int var1) {
      BeanManager var2 = this.getBeanManager();
      if (var2 instanceof BaseEntityManager) {
         PoolIntf var3 = ((BaseEntityManager)var2).getPool();
         var3.updateIdleTimeoutSeconds(var1);
      }

      if (debugLogger.isDebugEnabled()) {
         debug("updated Pool IdleTimeoutSeconds to " + var1 + " for EJB " + this.getDisplayName());
      }

   }

   public int getMaxQueriesInCache() {
      return this.isBeanManagedPersistence ? 0 : this.getCMPInfo().getMaxQueriesInCache();
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
               } else {
                  if (!var6.equals("FetchBatchSize")) {
                     throw new AssertionError("Unexpected propertyName: " + var6);
                  }

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
                  var7 = (PoolBean)var3;
                  this.updateMaxBeansInFreePool(var7.getMaxBeansInFreePool());
               } else {
                  EntityCacheBean var10;
                  if (var6.equals("MaxBeansInCache")) {
                     if (var3 instanceof EntityCacheBean) {
                        var10 = (EntityCacheBean)var3;
                        this.updateMaxBeansInCache(var10.getMaxBeansInCache());
                     } else {
                        ApplicationEntityCacheBean var11 = (ApplicationEntityCacheBean)var3;
                        this.updateMaxBeansInCache(var11.getMaxBeansInCache());
                     }
                  } else if (var6.equals("TransTimeoutSeconds")) {
                     TransactionDescriptorBean var12 = (TransactionDescriptorBean)var3;
                     this.updateTransactionTimeoutSeconds(var12.getTransTimeoutSeconds());
                  } else {
                     EntityCacheRefBean var13;
                     if (var6.equals("IdleTimeoutSeconds")) {
                        if (var3 instanceof EntityCacheBean) {
                           var10 = (EntityCacheBean)var3;
                           this.updateCacheIdleTimeoutSeconds(var10.getIdleTimeoutSeconds());
                        } else if (var3 instanceof EntityCacheRefBean) {
                           var13 = (EntityCacheRefBean)var3;
                           this.updateCacheIdleTimeoutSeconds(var13.getIdleTimeoutSeconds());
                        } else {
                           var7 = (PoolBean)var3;
                           this.updatePoolIdleTimeoutSeconds(var7.getIdleTimeoutSeconds());
                        }
                     } else if (var6.equals("ReadTimeoutSeconds")) {
                        if (var3 instanceof EntityCacheBean) {
                           var10 = (EntityCacheBean)var3;
                           this.updateReadTimeoutSeconds(var10.getReadTimeoutSeconds());
                        } else {
                           var13 = (EntityCacheRefBean)var3;
                           this.updateReadTimeoutSeconds(var13.getReadTimeoutSeconds());
                        }
                     } else if (var6.equals("KeyCacheSize")) {
                        AutomaticKeyGenerationBean var14 = (AutomaticKeyGenerationBean)var3;
                        this.updateKeyCacheSize(var14.getKeyCacheSize());
                     } else {
                        String var8;
                        int var9;
                        PersistenceUnitConfigurationBean var15;
                        if (var6.equals("LockTimeout")) {
                           if (debugLogger.isDebugEnabled()) {
                              debug("Changing persistence property: " + var6);
                           }

                           var15 = (PersistenceUnitConfigurationBean)var3;
                           var8 = var15.getName();
                           var9 = var15.getLockTimeout();
                           this.updateLockTimeout(var9, var8);
                        } else if (var6.equals("DataCacheTimeout")) {
                           if (debugLogger.isDebugEnabled()) {
                              debug("Changing persistence property: " + var6);
                           }

                           var15 = (PersistenceUnitConfigurationBean)var3;
                           var8 = var15.getName();
                           var9 = var15.getDataCacheTimeout();
                           this.updateDataCacheTimeout(var9, var8);
                        } else {
                           if (!var6.equals("FetchBatchSize")) {
                              throw new AssertionError("Unexpected propertyName: " + var6);
                           }

                           if (debugLogger.isDebugEnabled()) {
                              debug("Changing persistence property: " + var6);
                           }

                           var15 = (PersistenceUnitConfigurationBean)var3;
                           var8 = var15.getName();
                           var9 = var15.getFetchBatchSize();
                           this.updateFetchBatchSize(var9, var8);
                        }
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

   public void updateCacheIdleTimeoutSeconds(int var1) {
      this.getCachingDescriptor().setIdleTimeoutSecondsCache(var1);
      super.updateCacheIdleTimeoutSeconds(var1);
   }

   private static void debug(String var0) {
      debugLogger.debug("[EntityBeanInfoImpl] " + var0);
   }

   public boolean isEntityBean() {
      return true;
   }

   public String getCategoryCmpField() {
      return this.categoryCmpField;
   }

   public void setCategoryCmpField(String var1) {
      this.categoryCmpField = var1;
   }
}
