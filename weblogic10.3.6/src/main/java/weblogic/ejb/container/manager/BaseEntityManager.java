package weblogic.ejb.container.manager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBContext;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import weblogic.dbeans.ConversationImpl;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.ejb.EJBObjectEnum;
import weblogic.ejb.OptimisticConcurrencyException;
import weblogic.ejb.PreparedQuery;
import weblogic.ejb.WLQueryProperties;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSM2NSet;
import weblogic.ejb.container.cmp.rdbms.RDBMSPersistenceManager;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.InvalidationBeanManager;
import weblogic.ejb.container.interfaces.PoolIntf;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.EntityEJBContextImpl;
import weblogic.ejb.container.internal.EntityEJBHome;
import weblogic.ejb.container.internal.EntityEJBLocalHome;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.container.internal.RuntimeHelper;
import weblogic.ejb.container.internal.TxManager;
import weblogic.ejb.container.monitoring.EJBCacheRuntimeMBeanImpl;
import weblogic.ejb.container.monitoring.EntityEJBRuntimeMBeanImpl;
import weblogic.ejb.container.persistence.spi.CMPBean;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.CMPBeanManager;
import weblogic.ejb.container.persistence.spi.EloWrapper;
import weblogic.ejb.container.persistence.spi.EoWrapper;
import weblogic.ejb.container.persistence.spi.PersistenceManager;
import weblogic.ejb.container.persistence.spi.RSInfo;
import weblogic.ejb.container.pool.EntityPool;
import weblogic.ejb.container.utils.PartialOrderSet;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.utils.OrderedSet;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EntityEJBRuntimeMBean;
import weblogic.transaction.Transaction;
import weblogic.transaction.TxHelper;

public abstract class BaseEntityManager extends BaseEJBManager implements BeanManager {
   private PoolIntf entityPool;
   protected EntityEJBHome remoteHome = null;
   protected EntityEJBLocalHome localHome = null;
   protected boolean findersLoadBean = false;
   protected boolean isBeanManagedPersistence;
   protected boolean uses20CMP;
   protected PersistenceManager persistence;
   protected RDBMSPersistenceManager rdbmsPersistence;
   private boolean isReentrant;
   private boolean isCascadeDelete;
   protected final boolean isBeanClassAbstract = true;
   protected boolean enableBatchOperations = false;
   protected boolean orderDatabaseOperations = false;
   protected boolean isOptimistic = false;
   private Method findByPrimaryKeyMethod;
   private Method isModifiedMethod;
   protected volatile EntityBean reflectionTargetLocal;
   protected volatile EntityBean reflectionTargetRemote;
   private Map bmMap;
   protected Set parentBeanManagerSet = new HashSet();
   protected Set childBeanManagerSet = new HashSet();
   protected Set many2ManyCmrFieldInsertSet = new HashSet();
   protected Set notNullableParentBeanManagerSet = new HashSet();
   protected Set notNullableChildBeanManagerSet = new HashSet();
   private boolean cycleExists = false;
   protected EJBCacheRuntimeMBeanImpl cacheRTMBean;
   protected Class[] extraPostCreateExceptions = new Class[]{CreateException.class};
   private boolean isReadOnly = false;
   private boolean initialized = false;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = -7880010158127038176L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.manager.BaseEntityManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;

   public BaseEntityManager(EJBComponentRuntimeMBeanImpl var1) {
      super(var1);
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4) throws WLDeploymentException {
      super.setup(var1, var2, var3, var4);
      EntityBeanInfo var5 = (EntityBeanInfo)var3;
      if (5 == var5.getConcurrencyStrategy()) {
         this.isReadOnly = true;
      }

      this.isBeanManagedPersistence = var5.getIsBeanManagedPersistence();
      this.beanClass = var5.getGeneratedBeanClass();
      Class var6 = var5.getPrimaryKeyClass();
      CMPBeanDescriptor var7;
      if (var5.isUnknownPrimaryKey() && !var5.getIsBeanManagedPersistence()) {
         var7 = var5.getCMPInfo().getCMPBeanDescriptor(var5.getEJBName());
         var6 = var7.getPrimaryKeyClass();
      }

      try {
         this.findByPrimaryKeyMethod = var5.getGeneratedBeanInterface().getMethod("ejbFindByPrimaryKey", var6);
      } catch (NoSuchMethodException var11) {
         throw new AssertionError(var11);
      }

      var7 = null;

      EntityEJBRuntimeMBeanImpl var13;
      try {
         var13 = new EntityEJBRuntimeMBeanImpl(var3.getEJBName(), var3.getEJBName(), this.getEJBComponentRuntime(), this instanceof ExclusiveEntityManager, this.getTimerManager());
         this.setEJBRuntimeMBean(var13);
         this.addEJBRuntimeMBean(var13);
         this.cacheRTMBean = (EJBCacheRuntimeMBeanImpl)((EntityEJBRuntimeMBean)this.getEJBRuntimeMBean()).getCacheRuntime();
      } catch (ManagementException var10) {
         Loggable var9 = EJBLogger.logFailedToCreateRuntimeMBeanLoggable(var10);
         throw new WLDeploymentException(var9.getMessage(), var10);
      }

      this.perhapsSetupTimerManager(var13.getTimerRuntime());
      this.txManager = new TxManager(this);
      if (var1 != null) {
         this.remoteHome = (EntityEJBHome)var1;
      }

      if (var2 != null) {
         this.localHome = (EntityEJBLocalHome)var2;
      }

      this.isReentrant = var5.isReentrant();
      if (!this.isBeanManagedPersistence) {
         this.findersLoadBean = var5.getCMPInfo().findersLoadBean();
      }

      if (!this.isBeanManagedPersistence) {
         this.uses20CMP = var5.getCMPInfo().uses20CMP();
      } else {
         this.uses20CMP = false;
      }

      String var8 = var5.getIsModifiedMethodName();
      if (var8 == null) {
         this.isModifiedMethod = null;
      } else {
         try {
            Class var14 = var5.getGeneratedBeanInterface();
            this.isModifiedMethod = var14.getMethod(var8, (Class[])null);
         } catch (NoSuchMethodException var12) {
            if (!this.uses20CMP) {
               throw new AssertionError(var12);
            }
         }
      }

      this.persistence = this.setupPM(var5);
      if (!this.initialized && this.uses20CMP && this.persistence instanceof RDBMSPersistenceManager) {
         RDBMSBean var15 = ((RDBMSPersistenceManager)this.persistence).getRDBMSBean();
         this.isCascadeDelete = var15.isCascadeDelete();
      }

      if (this.uses20CMP) {
         this.isOptimistic = var5.isOptimistic();
         if (this.persistence instanceof RDBMSPersistenceManager) {
            this.rdbmsPersistence = (RDBMSPersistenceManager)this.persistence;
            this.enableBatchOperations = this.rdbmsPersistence.getEnableBatchOperations();
            this.orderDatabaseOperations = this.rdbmsPersistence.getOrderDatabaseOperations();
         }
      }

      this.entityPool = new EntityPool((EntityEJBHome)var1, (EntityEJBLocalHome)var2, this, var3, ((EntityEJBRuntimeMBean)this.getEJBRuntimeMBean()).getPoolRuntime());
      this.initialized = true;
   }

   private PersistenceManager setupPM(EntityBeanInfo var1) throws WLDeploymentException {
      Transaction var2 = null;
      PersistenceManager var3 = null;

      PersistenceManager var4;
      try {
         if (TxHelper.getTransaction() == null) {
            TxHelper.getTransactionManager().begin();
            var2 = TxHelper.getTransaction();
         }

         var3 = var1.getPersistenceManager();
         var3.setup(this);
         if (var2 != null) {
            TxHelper.getTransactionManager().commit();
            var2 = null;
         }

         var4 = var3;
      } catch (WLDeploymentException var15) {
         throw var15;
      } catch (Exception var16) {
         Loggable var5 = EJBLogger.logPersistenceManagerSetupErrorLoggable(var16);
         throw new WLDeploymentException(var5.getMessage(), var16);
      } finally {
         if (var2 != null) {
            try {
               TxHelper.getTransactionManager().rollback();
            } catch (Exception var14) {
            }
         }

      }

      return var4;
   }

   public void setBMMap(Map var1) throws WLDeploymentException {
      this.bmMap = var1;
   }

   public EJBContext allocateContext(EnterpriseBean var1, EJBObject var2, EJBLocalObject var3) {
      return new EntityEJBContextImpl(var1, this, this.remoteHome, this.localHome, var2, var3);
   }

   public EJBContext allocateContext(EnterpriseBean var1, Object var2) {
      EJBObject var3 = null;
      BaseEJBLocalObjectIntf var4 = null;
      if (this.remoteHome != null) {
         var3 = this.remoteHome.allocateEO(var2);
      }

      if (this.localHome != null) {
         var4 = this.localHome.allocateELO(var2);
      }

      return this.allocateContext(var1, var3, var4);
   }

   protected EnterpriseBean createNewBeanInstance() throws IllegalAccessException, InstantiationException {
      return (EnterpriseBean)this.beanClass.newInstance();
   }

   public EnterpriseBean createBean(EJBObject var1, EJBLocalObject var2, EnterpriseBean var3) throws InternalException {
      var3 = super.createBean(var1, var2, var3);
      if (!this.isBeanManagedPersistence) {
         if (this.uses20CMP) {
            if (!$assertionsDisabled && this.bmMap == null) {
               throw new AssertionError();
            }

            if (!$assertionsDisabled && this.persistence == null) {
               throw new AssertionError();
            }

            try {
               ((CMPBean)var3).__WL_setup(this.bmMap, this.persistence);
            } catch (Throwable var5) {
               EJBRuntimeUtils.throwInternalException("Error initializing CMP", var5);
            }
         } else {
            ((CMPBean)var3).__WL_setup((Map)null, this.persistence);
         }

         ((CMPBean)var3).__WL_setConnected(true);
      }

      return var3;
   }

   protected EJBCacheRuntimeMBeanImpl getEJBCacheRuntimeMBeanImpl() {
      return this.cacheRTMBean;
   }

   public boolean isReadOnly() {
      return this.isReadOnly;
   }

   public Class getBeanClass() {
      return this.beanClass;
   }

   public Map getBeanManagerMap() {
      return this.bmMap;
   }

   public Method getFindByPrimaryKeyMethod() {
      return this.findByPrimaryKeyMethod;
   }

   public void addParentBeanManager(BeanManager var1) {
      this.parentBeanManagerSet.add(var1);
   }

   public void addChildBeanManager(BeanManager var1) {
      this.childBeanManagerSet.add(var1);
   }

   public void addNotNullableParentBeanManager(BeanManager var1) {
      this.notNullableParentBeanManagerSet.add(var1);
   }

   public void addNotNullableChildBeanManager(BeanManager var1) {
      this.notNullableChildBeanManagerSet.add(var1);
   }

   public boolean setCycleExists(Set var1) {
      var1.add(this);
      if (this.parentBeanManagerSet != null && !this.cycleExists) {
         Iterator var2 = this.parentBeanManagerSet.iterator();

         while(var2.hasNext()) {
            BaseEntityManager var3 = (BaseEntityManager)var2.next();
            if (!var1.contains(var3)) {
               boolean var4 = var3.setCycleExists(var1);
               if (var4) {
                  this.cycleExists = true;
               }
            } else {
               if (debugLogger.isDebugEnabled()) {
                  debug("cycle exists: from " + this.beanInfo.getEJBName() + " to " + var3.getBeanInfo().getEJBName());
               }

               this.cycleExists = true;
            }
         }
      }

      var1.remove(this);
      return this.cycleExists;
   }

   public boolean getOrderDatabaseOperations() {
      return this.orderDatabaseOperations;
   }

   public void addM2NInsertSet(String var1) {
      if (debugLogger.isDebugEnabled()) {
         debug(this.beanInfo.getEJBName() + "  adding cmrfield '" + var1 + "' to M2NInsertSet");
      }

      this.many2ManyCmrFieldInsertSet.add(var1);
   }

   public boolean isM2NInsertSet(String var1) {
      return this.many2ManyCmrFieldInsertSet.contains(var1);
   }

   protected abstract List pkListToBeanList(Collection var1, javax.transaction.Transaction var2, boolean var3);

   protected abstract void prepareVerificationForBatch(Collection var1, javax.transaction.Transaction var2) throws InternalException;

   protected abstract Map pkListToPkBeanMap(Collection var1, javax.transaction.Transaction var2, boolean var3);

   public void registerInsertBeanAndTxUser(Object var1, javax.transaction.Transaction var2, WLEnterpriseBean var3) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("Setting up tx listener for batch insert for tx: " + var2);
      }

      if (var2 != null) {
         this.getTxManager().registerInsertBean(var1, var2);
      }

      var3.__WL_setLoadUser(RuntimeHelper.getCurrentNew());
   }

   public boolean isBeanManagedPersistence() {
      return this.isBeanManagedPersistence;
   }

   public boolean registerDeleteBean(Object var1, javax.transaction.Transaction var2) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("Setting up tx listener for batch delete for tx: " + var2);
      }

      return var2 != null ? this.getTxManager().registerDeleteBean(var1, var2) : false;
   }

   public void registerInsertDeletedBeanAndTxUser(Object var1, javax.transaction.Transaction var2, WLEnterpriseBean var3) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("Setting up tx listener for batch insert removed for tx: " + var2);
      }

      if (var2 != null) {
         this.getTxManager().registerInsertDeletedBean(var1, var2);
      }

      var3.__WL_setLoadUser(RuntimeHelper.getCurrentNew());
   }

   public void registerM2NJoinTableInsert(String var1, Object var2, javax.transaction.Transaction var3) throws InternalException {
      if (this.many2ManyCmrFieldInsertSet.contains(var1)) {
         if (debugLogger.isDebugEnabled()) {
            debug("Register M2N Join Table insert for cmrField: " + var1 + ", pk: " + var2 + ", tx: " + var3);
         }

         if (var3 != null) {
            this.getTxManager().registerM2NJoinTableInsert(var2, var1, var3);
         }

      }
   }

   public boolean needsToBeInserted(Object var1) throws SystemException, RollbackException {
      return this.getTxManager().needsToBeInserted((javax.transaction.Transaction)EJBRuntimeUtils.getInvokeTxOrThread(), var1);
   }

   public void addBeanToInsertStmt(PreparedStatement[] var1, List var2, CMPBean var3, boolean var4, boolean var5) throws Exception {
      if (!var2.contains(var3.__WL_getPrimaryKey())) {
         try {
            var2.add(var3.__WL_getPrimaryKey());
            if (var5 && ((RDBMSPersistenceManager)this.persistence).isSelfRelationship()) {
               if (debugLogger.isDebugEnabled()) {
                  debug(this.beanInfo.getEJBName() + ": calling __WL_addSelfRelatedBeansToInsertStmt");
               }

               var3.__WL_addSelfRelatedBeansToInsertStmt(var1, var2, var4);
            }

            if (debugLogger.isDebugEnabled()) {
               debug(this.beanInfo.getEJBName() + ": adding bean to insert stmt where pk=" + var3.__WL_getPrimaryKey());
            }

            if (((RDBMSPersistenceManager)this.persistence).isFkColsNullable() && ((RDBMSPersistenceManager)this.persistence).isSelfRelationship()) {
               var3.__WL_setBeanParamsForStmtArray(var1, (boolean[])null, 0, true);
               this.registerModifiedBean(var3.__WL_getPrimaryKey(), (javax.transaction.Transaction)EJBRuntimeUtils.getInvokeTxOrThread());
            } else {
               var3.__WL_setBeanParamsForStmtArray(var1, (boolean[])null, 0, false);
            }

            if (var5) {
               for(int var6 = 0; var6 < var1.length; ++var6) {
                  if (var1[var6] != null) {
                     var1[var6].addBatch();
                  }
               }
            }
         } catch (SQLException var7) {
            EJBRuntimeUtils.throwInternalException("Error during addBatch():", var7);
         }

      }
   }

   public void executeInsertStmt(List var1, javax.transaction.Transaction var2, Set var3, boolean var4, boolean var5, Collection var6) throws InternalException {
      boolean var28;
      boolean var10000 = var28 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var29 = null;
      DiagnosticActionState[] var30 = null;
      Object var27 = null;
      if (var10000) {
         Object[] var23 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isArgumentsCaptureNeeded()) {
            var23 = InstrumentationSupport.toSensitive(7);
         }

         DynamicJoinPoint var62 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var23, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
         DiagnosticAction[] var10002 = var29 = var10001.getActions();
         InstrumentationSupport.preProcess(var62, var10001, var10002, var30 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         var3.add(this);
         if (this.parentBeanManagerSet != null) {
            Iterator var7 = this.parentBeanManagerSet.iterator();

            while(var7.hasNext()) {
               BaseEntityManager var8 = (BaseEntityManager)var7.next();
               if (!var3.contains(var8) && !this.notNullableChildBeanManagerSet.contains(var8)) {
                  var8.getTxManager().executeInsertOperations((javax.transaction.Transaction)EJBRuntimeUtils.getInvokeTxOrThread(), var3, var4, var5);
               }
            }
         }

         if (var1.isEmpty()) {
            return;
         }

         boolean var55 = false;
         Connection var57 = null;
         PreparedStatement[] var9 = null;
         ArrayList var10 = new ArrayList();
         List var11 = null;
         CMPBean var12 = null;
         var11 = this.pkListToBeanList(var1, var2, false);
         if (var11.size() == 0) {
            return;
         }

         boolean var13 = false;

         try {
            var12 = (CMPBean)var11.get(0);
            var57 = ((RDBMSPersistenceManager)this.persistence).getConnection();
            if (((RDBMSPersistenceManager)this.persistence).isFkColsNullable() && ((RDBMSPersistenceManager)this.persistence).isSelfRelationship()) {
               var9 = var12.__WL_getStmtArray(var57, (boolean[])null, 0, true);
            } else {
               var9 = var12.__WL_getStmtArray(var57, (boolean[])null, 0, false);
            }

            Iterator var58;
            int var61;
            if (this.enableBatchOperations && var1.size() > 1) {
               var58 = var11.iterator();

               while(var58.hasNext()) {
                  var12 = (CMPBean)var58.next();
                  this.addBeanToInsertStmt(var9, var10, var12, var5, true);
               }

               if (debugLogger.isDebugEnabled()) {
                  debug(this.beanInfo.getEJBName() + ": about to execute batch create.");
               }

               int[] var59 = new int[var9.length];
               var61 = 0;

               while(true) {
                  if (var61 >= var9.length) {
                     var13 = true;

                     Iterator var63;
                     for(var63 = var11.iterator(); var63.hasNext(); var12.__WL_perhapsReloadOptimisticColumn()) {
                        var12 = (CMPBean)var63.next();
                        if (((RDBMSPersistenceManager)this.persistence).isFkColsNullable()) {
                           var12.__WL_resetIsModifiedVars(0, var57, true);
                        } else {
                           var12.__WL_resetIsModifiedVars(0, var57, false);
                        }
                     }

                     if (var5) {
                        var63 = var1.iterator();

                        while(var63.hasNext()) {
                           var6.add(var63.next());
                        }
                     }

                     var1.clear();
                     break;
                  }

                  if (var9[var61] != null) {
                     var59 = var9[var61].executeBatch();

                     for(int var56 = 0; var56 < var59.length; ++var56) {
                        if (var59[var56] == 0 || var59[var56] == -3) {
                           throw new Exception("Failed to CREATE Bean.   Primary Key Value: '" + var1.get(var56) + "'");
                        }
                     }
                  }

                  ++var61;
               }
            } else {
               var58 = var11.iterator();

               while(var58.hasNext()) {
                  var12 = (CMPBean)var58.next();
                  this.addBeanToInsertStmt(var9, var10, var12, var5, false);
                  if (debugLogger.isDebugEnabled()) {
                     debug(this.beanInfo.getEJBName() + ": about to execute multi create.");
                  }

                  boolean var60 = false;

                  for(int var64 = 0; var64 < var9.length; ++var64) {
                     if (var9[var64] != null) {
                        var61 = var9[var64].executeUpdate();
                        if (var61 == 0) {
                           throw new Exception("Failed to CREATE Bean.   Primary Key Value: '" + var12.__WL_getPrimaryKey() + "'");
                        }
                     }
                  }

                  var13 = true;
                  if (((RDBMSPersistenceManager)this.persistence).isFkColsNullable()) {
                     var12.__WL_resetIsModifiedVars(0, var57, true);
                  } else {
                     var12.__WL_resetIsModifiedVars(0, var57, false);
                  }

                  var12.__WL_perhapsReloadOptimisticColumn();
                  Object var66 = var12.__WL_getPrimaryKey();
                  var1.remove(var66);
                  if (var5) {
                     var6.add(var66);
                  }
               }
            }

            var58 = this.notNullableChildBeanManagerSet.iterator();

            while(var58.hasNext()) {
               BaseEntityManager var65 = (BaseEntityManager)var58.next();
               var65.getTxManager().executeInsertOperations((javax.transaction.Transaction)EJBRuntimeUtils.getInvokeTxOrThread(), var3, var4, var5);
            }
         } catch (SQLException var51) {
            SQLException var14 = var51;
            if ((!this.enableBatchOperations || var1.size() <= 1) && !var13) {
               Object var15 = var12.__WL_getPrimaryKey();
               if (var15 != null) {
                  boolean var16 = false;

                  try {
                     var16 = var12.__WL_exists(var15);
                  } catch (Exception var49) {
                     EJBRuntimeUtils.throwInternalException("EJB Exception:", var14);
                  } finally {
                     if (!var5) {
                        this.cacheRemoveCMPBeansOnError(var2, var11);
                     }

                  }

                  if (var16) {
                     EJBRuntimeUtils.throwInternalException("EJB Exception:", new DuplicateKeyException("Bean with primary key: '" + var15 + "' already exists."));
                  }
               }

               EJBRuntimeUtils.throwInternalException("EJB Exception:", var51);
            } else {
               EJBRuntimeUtils.throwInternalException("EJB Exception:", var51);
            }
         } catch (Throwable var52) {
            EJBLogger.logExcepInMethod1("executeInsertStmt", var52);
            if (!var5) {
               this.cacheRemoveCMPBeansOnError(var2, var11);
            }

            EJBRuntimeUtils.throwInternalException("EJB Exception:", var52);
         } finally {
            ((RDBMSPersistenceManager)this.persistence).releaseArrayResources(var57, var9, (ResultSet[])null);
         }
      } finally {
         if (var28) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var29, var30);
         }

      }

   }

   public void addBeanToUpdateStmt(PreparedStatement[] var1, boolean[] var2, CMPBean var3, boolean var4) throws Exception {
      try {
         if (debugLogger.isDebugEnabled()) {
            debug(this.beanInfo.getEJBName() + ": adding bean to update stmt where pk=" + var3.__WL_getPrimaryKey());
         }

         var3.__WL_setBeanParamsForStmtArray(var1, var2, 1, false);
         if (var4) {
            for(int var5 = 0; var5 < var1.length; ++var5) {
               if (var1[var5] != null) {
                  var1[var5].addBatch();
               }
            }
         }
      } catch (SQLException var6) {
         EJBRuntimeUtils.throwInternalException("Error during addBatch():", var6);
      }

   }

   public void executeUpdateStmt(List var1, javax.transaction.Transaction var2, Set var3, boolean var4, boolean var5, Collection var6) throws InternalException {
      boolean var33;
      boolean var10000 = var33 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var34 = null;
      DiagnosticActionState[] var35 = null;
      Object var32 = null;
      if (var10000) {
         Object[] var28 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isArgumentsCaptureNeeded()) {
            var28 = InstrumentationSupport.toSensitive(7);
         }

         DynamicJoinPoint var70 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var28, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
         DiagnosticAction[] var10002 = var34 = var10001.getActions();
         InstrumentationSupport.preProcess(var70, var10001, var10002, var35 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         var3.add(this);
         if (this.childBeanManagerSet != null) {
            Iterator var7 = this.childBeanManagerSet.iterator();

            while(var7.hasNext()) {
               BaseEntityManager var8 = (BaseEntityManager)var7.next();
               if (!var3.contains(var8)) {
                  var8.getTxManager().executeUpdateOperations((javax.transaction.Transaction)EJBRuntimeUtils.getInvokeTxOrThread(), var3, var4, var5);
               }
            }
         }

         if (!var1.isEmpty()) {
            boolean var63 = false;
            Connection var65 = null;
            PreparedStatement[] var9 = null;
            Map var10 = null;
            CMPBean var11 = null;
            boolean var12 = false;
            boolean var13 = false;
            if (var1.size() > 1) {
               var13 = this.rdbmsPersistence.perhapsUseSendBatchForOracle();
               if (!var13) {
                  this.prepareVerificationForBatch(var1, var2);
               }
            }

            var10 = this.pkListToPkBeanMap(var1, var2, ((RDBMSPersistenceManager)this.persistence).isSelfRelationship());
            if (var10.size() == 0) {
               return;
            }

            try {
               var11 = (CMPBean)var10.values().iterator().next();
               var65 = ((RDBMSPersistenceManager)this.persistence).getConnection();
               if (this.enableBatchOperations && this.isOptimistic) {
                  var12 = this.verifyQueriesForBatching(var10.values());
               }

               if (var12) {
                  EJBLogger var14 = new EJBLogger();
                  EJBLogger.logBatchingTurnedOff(this.beanInfo.getEJBName());
               }

               int var20;
               Object var21;
               Loggable var22;
               boolean var76;
               int var77;
               if (this.enableBatchOperations && !var12 && var1.size() > 1) {
                  boolean[] var67 = null;

                  for(Iterator var68 = var10.values().iterator(); var68.hasNext(); var67 = var11.__WL_getIsModifiedUnion(var67)) {
                     var11 = (CMPBean)var68.next();
                  }

                  boolean var69 = false;
                  if (var67 != null) {
                     for(int var72 = 0; var72 < var67.length; ++var72) {
                        var69 |= var67[var72];
                     }

                     if (!var69) {
                        if (debugLogger.isDebugEnabled()) {
                           debug("baseEntityManager.executeUpdateStmt(): nothing changed, avoided stores for update");
                        }

                        return;
                     }
                  }

                  var9 = var11.__WL_getStmtArray(var65, var67, 1, false);
                  int[] var73 = new int[var9.length];
                  int[] var74 = new int[var9.length];
                  var76 = false;
                  if (var13) {
                     for(var77 = 0; var77 < var9.length; ++var77) {
                        if (var9[var77] != null) {
                           this.invokeOracleSetExecuteBatch(var9[var77], 20);
                        }
                     }
                  }

                  Iterator var78 = var10.values().iterator();

                  while(true) {
                     while(var78.hasNext()) {
                        var11 = (CMPBean)var78.next();
                        if (var13) {
                           var11.__WL_setBeanParamsForStmtArray(var9, var67, 1, false);

                           for(var20 = 0; var20 < var9.length; ++var20) {
                              if (var9[var20] != null) {
                                 int var81 = var9[var20].executeUpdate();
                                 var74[var20] += var81;
                                 int var75 = var73[var20]++;
                              }
                           }
                        } else {
                           this.addBeanToUpdateStmt(var9, var67, var11, true);
                        }
                     }

                     if (debugLogger.isDebugEnabled()) {
                        debug(this.beanInfo.getEJBName() + ": about to execute batch update.");
                     }

                     if (var13) {
                        for(var77 = 0; var77 < var9.length; ++var77) {
                           if (var9[var77] != null) {
                              var74[var77] += this.invokeOracleSendBatch(var9[var77]);
                              var76 = true;
                              if (var74[var77] != var73[var77]) {
                                 if (this.isOptimistic) {
                                    throw new OptimisticConcurrencyException("Instance/s of bean '" + this.beanInfo.getEJBName() + "' in the batch was " + "changed by another transaction. The primary key is " + "unknown due to Oracle driver limitation.");
                                 }

                                 throw new NoSuchEntityException("Instance/s of bean '" + this.beanInfo.getEJBName() + "' in the batch does not exist. " + "The primary key is unknown due to Oracle driver " + "limitation.");
                              }
                           }
                        }
                     } else {
                        int[] var80 = new int[var9.length];

                        for(var20 = 0; var20 < var9.length; ++var20) {
                           if (var9[var20] != null) {
                              var80 = var9[var20].executeBatch();
                              var76 = true;

                              for(int var64 = 0; var64 < var80.length; ++var64) {
                                 if (var80[var64] == 0 || var80[var64] == -3) {
                                    var21 = var1.get(var64);
                                    if (this.isOptimistic) {
                                       var22 = EJBLogger.logoptimisticUpdateFailedLoggable(this.beanInfo.getEJBName(), var21.toString());
                                       throw new OptimisticConcurrencyException(var22.getMessage());
                                    }

                                    var22 = EJBLogger.logbeanDoesNotExistLoggable(this.beanInfo.getEJBName(), var21.toString());
                                    throw new NoSuchEntityException(var22.getMessage());
                                 }
                              }
                           }
                        }
                     }

                     for(var78 = var10.values().iterator(); var78.hasNext(); var11.__WL_perhapsReloadOptimisticColumn()) {
                        var11 = (CMPBean)var78.next();
                        var11.__WL_resetIsModifiedVars(1, var65, false);
                        if (var76) {
                           var11.__WL_perhapsTakeSnapshot();
                        }
                     }

                     if (var5) {
                        var78 = var1.iterator();

                        while(var78.hasNext()) {
                           var6.add(var78.next());
                        }
                     }

                     var1.clear();
                     return;
                  }
               }

               Iterator var66 = var10.entrySet().iterator();

               while(true) {
                  while(var66.hasNext()) {
                     Map.Entry var15 = (Map.Entry)var66.next();
                     var11 = (CMPBean)var15.getValue();
                     Object var16 = null;
                     boolean[] var71 = var11.__WL_getIsModifiedUnion((boolean[])var16);
                     boolean var17 = false;
                     if (var71 != null) {
                        for(int var18 = 0; var18 < var71.length; ++var18) {
                           var17 |= var71[var18];
                        }

                        if (!var17) {
                           if (debugLogger.isDebugEnabled()) {
                              debug("baseEntityManager.executeUpdateStmt(): nothing changed, avoided stores for update");
                           }
                           continue;
                        }
                     }

                     var9 = var11.__WL_getStmtArray(var65, var71, 1, false);
                     var76 = false;
                     this.addBeanToUpdateStmt(var9, var71, var11, false);
                     if (debugLogger.isDebugEnabled()) {
                        debug(this.beanInfo.getEJBName() + ": about to execute multi update.");
                     }

                     boolean var19 = false;

                     for(var20 = 0; var20 < var9.length; ++var20) {
                        if (var9[var20] != null) {
                           var77 = var9[var20].executeUpdate();
                           var76 = true;
                           if (var77 == 0) {
                              var21 = var15.getKey();
                              if (this.isOptimistic) {
                                 var22 = EJBLogger.logoptimisticUpdateFailedLoggable(this.beanInfo.getEJBName(), var21.toString());
                                 throw new OptimisticConcurrencyException(var22.getMessage());
                              }

                              var22 = EJBLogger.logbeanDoesNotExistLoggable(this.beanInfo.getEJBName(), var21.toString());
                              throw new NoSuchEntityException(var22.getMessage());
                           }
                        }
                     }

                     var11.__WL_resetIsModifiedVars(1, var65, false);
                     if (var76) {
                        var11.__WL_perhapsTakeSnapshot();
                     }

                     var11.__WL_perhapsReloadOptimisticColumn();
                     Object var79 = var15.getKey();
                     var1.remove(var79);
                     if (var5) {
                        var6.add(var79);
                     }
                  }

                  return;
               }
            } catch (Throwable var60) {
               if (!(var60 instanceof OptimisticConcurrencyException)) {
                  EJBLogger.logExcepFromStore(var60);
               }

               if (!var5) {
                  this.cacheRemoveCMPBeansOnError(var2, var10.values());
               }

               EJBRuntimeUtils.throwInternalException("Exception from ejbStore:", var60);
               return;
            } finally {
               if (var13 && var9 != null) {
                  for(int var25 = 0; var25 < var9.length; ++var25) {
                     if (var9[var25] != null) {
                        try {
                           this.invokeOracleSetExecuteBatch(var9[var25], 1);
                        } catch (SQLException var58) {
                        } catch (Throwable var59) {
                        }
                     }
                  }
               }

               ((RDBMSPersistenceManager)this.persistence).releaseArrayResources(var65, var9, (ResultSet[])null);
            }
         }
      } finally {
         if (var33) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var34, var35);
         }

      }

   }

   public void addBeanToDeleteStmt(PreparedStatement[] var1, List var2, boolean[] var3, CMPBean var4, boolean var5, boolean var6) throws Exception {
      EntityEJBContextImpl var7 = (EntityEJBContextImpl)var4.__WL_getEntityContext();
      if (!var2.contains(var7.__WL_getPrimaryKey())) {
         try {
            var2.add(var7.__WL_getPrimaryKey());
            if (var6 && ((RDBMSPersistenceManager)this.persistence).isSelfRelationship()) {
               if (debugLogger.isDebugEnabled()) {
                  debug(this.beanInfo.getEJBName() + ": calling __WL_addSelfRelatedBeansToDeleteStmt");
               }

               var4.__WL_addSelfRelatedBeansToDeleteStmt(var1, var2, var3, var5);
            }

            if (debugLogger.isDebugEnabled()) {
               debug(this.beanInfo.getEJBName() + ": adding bean to delete stmt where pk=" + var4.__WL_getPrimaryKey());
            }

            var4.__WL_setBeanParamsForStmtArray(var1, var3, 2, false);
            if (var6) {
               for(int var8 = 0; var8 < var1.length; ++var8) {
                  if (var1[var8] != null) {
                     var1[var8].addBatch();
                  }
               }
            }
         } catch (SQLException var9) {
            EJBRuntimeUtils.throwInternalException("Error during addBatch():", var9);
         }

      }
   }

   public void executeDeleteStmt(List var1, javax.transaction.Transaction var2, Set var3, boolean var4, boolean var5, Collection var6) throws InternalException {
      boolean var31;
      boolean var10000 = var31 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var32 = null;
      DiagnosticActionState[] var33 = null;
      Object var30 = null;
      if (var10000) {
         Object[] var26 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isArgumentsCaptureNeeded()) {
            var26 = InstrumentationSupport.toSensitive(7);
         }

         DynamicJoinPoint var63 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var26, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
         DiagnosticAction[] var10002 = var32 = var10001.getActions();
         InstrumentationSupport.preProcess(var63, var10001, var10002, var33 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         var3.add(this);
         if (this.childBeanManagerSet != null) {
            Iterator var7 = this.childBeanManagerSet.iterator();

            while(var7.hasNext()) {
               BaseEntityManager var8 = (BaseEntityManager)var7.next();
               if (!var3.contains(var8) && !this.notNullableParentBeanManagerSet.contains(var8)) {
                  var8.getTxManager().executeDeleteOperations((javax.transaction.Transaction)EJBRuntimeUtils.getInvokeTxOrThread(), var3, var4, var5);
               }
            }
         }

         if (var1.isEmpty()) {
            return;
         }

         boolean var57 = false;
         Connection var59 = null;
         PreparedStatement[] var9 = null;
         ArrayList var10 = new ArrayList();
         List var11 = null;
         CMPBean var12 = null;
         boolean var13 = false;
         if (var1.size() > 1) {
            var13 = this.rdbmsPersistence.perhapsUseSendBatchForOracle();
            if (!var13) {
               this.prepareVerificationForBatch(var1, var2);
            }
         }

         var11 = this.pkListToBeanList(var1, var2, true);
         if (var11.size() == 0) {
            return;
         }

         try {
            List var15;
            try {
               var12 = (CMPBean)var11.get(0);
               var59 = ((RDBMSPersistenceManager)this.persistence).getConnection();
               int var17;
               Object var19;
               Loggable var20;
               if (this.enableBatchOperations && var1.size() > 1) {
                  boolean[] var60 = null;

                  for(Iterator var62 = var11.iterator(); var62.hasNext(); var60 = var12.__WL_getIsModifiedUnion(var60)) {
                     var12 = (CMPBean)var62.next();
                  }

                  var9 = var12.__WL_getStmtArray(var59, var60, 2, false);
                  int[] var64 = new int[var9.length];
                  int[] var66 = new int[var9.length];
                  if (var13) {
                     for(var17 = 0; var17 < var9.length; ++var17) {
                        if (var9[var17] != null) {
                           this.invokeOracleSetExecuteBatch(var9[var17], 20);
                        }
                     }
                  }

                  Iterator var70 = var11.iterator();

                  while(true) {
                     int var68;
                     while(var70.hasNext()) {
                        var12 = (CMPBean)var70.next();
                        if (var13) {
                           var12.__WL_setBeanParamsForStmtArray(var9, var60, 2, false);

                           for(var68 = 0; var68 < var9.length; ++var68) {
                              if (var9[var68] != null) {
                                 int var72 = var9[var68].executeUpdate();
                                 var66[var68] += var72;
                                 int var69 = var64[var68]++;
                              }
                           }
                        } else {
                           this.addBeanToDeleteStmt(var9, var10, var60, var12, var5, true);
                        }
                     }

                     if (debugLogger.isDebugEnabled()) {
                        debug(this.beanInfo.getEJBName() + ": about to execute batch delete.");
                     }

                     if (var13) {
                        for(var17 = 0; var17 < var9.length; ++var17) {
                           if (var9[var17] != null) {
                              var66[var17] += this.invokeOracleSendBatch(var9[var17]);
                              if (var66[var17] != var64[var17]) {
                                 if (this.isOptimistic) {
                                    throw new OptimisticConcurrencyException("Instance/s of bean '" + this.beanInfo.getEJBName() + "' in the batch was " + "changed by another transaction. The primary key is " + "unknown due to Oracle driver limitation.");
                                 }

                                 throw new NoSuchEntityException("Instance/s of bean '" + this.beanInfo.getEJBName() + "' in the batch does not exist. " + "The primary key is unknown due to Oracle driver " + "limitation.");
                              }
                           }
                        }
                     } else {
                        int[] var71 = new int[var9.length];

                        for(var68 = 0; var68 < var9.length; ++var68) {
                           if (var9[var68] != null) {
                              var71 = var9[var68].executeBatch();

                              for(int var58 = 0; var58 < var71.length; ++var58) {
                                 if (var71[var58] == 0 || var71[var58] == -3) {
                                    var19 = var1.get(var58);
                                    if (this.isOptimistic) {
                                       var20 = EJBLogger.logoptimisticUpdateFailedLoggable(this.beanInfo.getEJBName(), var19.toString());
                                       throw new OptimisticConcurrencyException(var20.getMessage());
                                    }

                                    var20 = EJBLogger.logbeanDoesNotExistLoggable(this.beanInfo.getEJBName(), var19.toString());
                                    throw new NoSuchEntityException(var20.getMessage());
                                 }
                              }
                           }
                        }
                     }

                     if (var5) {
                        var70 = var1.iterator();

                        while(var70.hasNext()) {
                           var6.add(var70.next());
                        }
                     }

                     var1.clear();
                     this.cacheRemoveCMPBeans(var2, var11);
                     break;
                  }
               } else {
                  for(Iterator var14 = var11.iterator(); var14.hasNext(); this.cacheRemoveCMPBean(var2, var12)) {
                     var12 = (CMPBean)var14.next();
                     var15 = null;
                     boolean[] var61 = var12.__WL_getIsModifiedUnion((boolean[])var15);
                     var9 = var12.__WL_getStmtArray(var59, var61, 2, false);
                     this.addBeanToDeleteStmt(var9, var10, var61, var12, var5, false);
                     if (debugLogger.isDebugEnabled()) {
                        debug(this.beanInfo.getEJBName() + ": about to execute multi delete.");
                     }

                     boolean var16 = false;

                     for(var17 = 0; var17 < var9.length; ++var17) {
                        if (var9[var17] != null) {
                           int var65 = var9[var17].executeUpdate();
                           if (var65 == 0) {
                              EntityEJBContextImpl var18 = (EntityEJBContextImpl)var12.__WL_getEntityContext();
                              var19 = var18.__WL_getPrimaryKey();
                              if (this.isOptimistic) {
                                 var20 = EJBLogger.logoptimisticUpdateFailedLoggable(this.beanInfo.getEJBName(), var19.toString());
                                 throw new OptimisticConcurrencyException(var20.getMessage());
                              }

                              var20 = EJBLogger.logbeanDoesNotExistLoggable(this.beanInfo.getEJBName(), var19.toString());
                              throw new NoSuchEntityException(var20.getMessage());
                           }
                        }
                     }

                     Object var67 = ((EntityEJBContextImpl)var12.__WL_getEntityContext()).__WL_getPrimaryKey();
                     var1.remove(var67);
                     if (var5) {
                        var6.add(var67);
                     }
                  }
               }
            } catch (Throwable var54) {
               EJBLogger.logExcepInMethod1("executeDeleteStmt", var54);
               if (!var5) {
                  this.cacheRemoveCMPBeansOnError(var2, var11);
               } else if (var6.size() > 0) {
                  var15 = this.pkListToBeanList(var6, var2, true);
                  this.cacheRemoveCMPBeansOnError(var2, var15);
               }

               EJBRuntimeUtils.throwInternalException("EJB Exception:", var54);
            }
         } finally {
            if (var13 && var9 != null) {
               for(int var23 = 0; var23 < var9.length; ++var23) {
                  if (var9[var23] != null) {
                     try {
                        this.invokeOracleSetExecuteBatch(var9[var23], 1);
                     } catch (SQLException var52) {
                     } catch (Throwable var53) {
                     }
                  }
               }
            }

            ((RDBMSPersistenceManager)this.persistence).releaseArrayResources(var59, var9, (ResultSet[])null);
         }
      } finally {
         if (var31) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var32, var33);
         }

      }

   }

   private void invokeOracleSetExecuteBatch(PreparedStatement var1, int var2) throws Throwable {
      if ("oracle.jdbc.OraclePreparedStatement".equals(var1.getClass().getName())) {
         try {
            Method var3 = var1.getClass().getMethod("setExecuteBatch", Integer.TYPE);
            if (var3 != null) {
               var3.invoke(var1, var2);
            }
         } catch (InvocationTargetException var5) {
            Throwable var4 = var5.getTargetException();
            if (var4 instanceof SQLException) {
               throw (SQLException)var4;
            }

            throw var4;
         }
      }

   }

   private int invokeOracleSendBatch(PreparedStatement var1) throws Throwable {
      if ("oracle.jdbc.OraclePreparedStatement".equals(var1.getClass().getName())) {
         try {
            Method var2 = var1.getClass().getMethod("sendBatch");
            if (var2 != null) {
               Object var5 = var2.invoke(var1);
               return (Integer)var5;
            }
         } catch (InvocationTargetException var4) {
            Throwable var3 = var4.getTargetException();
            if (var3 instanceof SQLException) {
               throw (SQLException)var3;
            }

            throw var3;
         }
      }

      return 0;
   }

   public void executeM2NJoinTableInserts(Map var1, javax.transaction.Transaction var2, boolean var3) throws InternalException {
      if (var1 == null) {
         if (debugLogger.isDebugEnabled()) {
            debug(this.beanInfo.getEJBName() + " no deferred M2N INSERTs.");
         }

      } else {
         Iterator var4 = var1.keySet().iterator();

         while(true) {
            String var5;
            List var6;
            do {
               do {
                  if (!var4.hasNext()) {
                     return;
                  }

                  var5 = (String)var4.next();
               } while(!this.many2ManyCmrFieldInsertSet.contains(var5));

               var6 = (List)var1.get(var5);
               if (debugLogger.isDebugEnabled()) {
                  debug(this.beanInfo.getEJBName() + " " + var5 + " process deferred M2N INSERTs for " + var6.size() + " source Beans.");
               }
            } while(var6.size() <= 0);

            Connection var7 = null;
            PreparedStatement var8 = null;
            List var9 = this.pkListToBeanList(var6, var2, false);
            if (var9.size() == 0) {
               return;
            }

            try {
               CMPBean var10 = (CMPBean)var9.get(0);
               var7 = ((RDBMSPersistenceManager)this.persistence).getConnection();
               String var11 = var10.__WL_getM2NSQL(var5, 0);
               var8 = var7.prepareStatement(var11);
               if (this.enableBatchOperations && var6.size() > 1) {
                  Iterator var29 = var9.iterator();

                  Collection var30;
                  Set var31;
                  while(var29.hasNext()) {
                     var10 = (CMPBean)var29.next();
                     if (debugLogger.isDebugEnabled()) {
                        debug("set M2N Join Table INSERT for " + this.beanInfo.getEJBName() + ",\n pk '" + var10.__WL_getPrimaryKey() + "'");
                     }

                     var30 = var10.__WL_getCmrBeansForCmrField(var5);
                     var31 = ((RDBMSM2NSet)var30).getAddSet();
                     if (var31.size() > 0) {
                        Object var32 = ((RDBMSM2NSet)var30).getCreatorPk();
                        Iterator var33 = var31.iterator();

                        while(var33.hasNext()) {
                           Object var34 = var33.next();
                           if (debugLogger.isDebugEnabled()) {
                              debug("setting Join Table INSERT params for thisPk: '" + var32 + "', otherBeanPK: '" + var34 + "'");
                           }

                           ((RDBMSM2NSet)var30).setAddJoinTableSQLParams(var8, var32, var34);
                           var8.addBatch();
                        }
                     }
                  }

                  if (debugLogger.isDebugEnabled()) {
                     debug(this.beanInfo.getEJBName() + ": about to execute batch M2NJoinTableInserts.");
                  }

                  var8.executeBatch();
                  var29 = var9.iterator();

                  while(var29.hasNext()) {
                     var10 = (CMPBean)var29.next();
                     var30 = var10.__WL_getCmrBeansForCmrField(var5);
                     var31 = ((RDBMSM2NSet)var30).getAddSet();
                     var31.clear();
                  }
               } else {
                  ArrayList var12 = new ArrayList();

                  Set var15;
                  for(Iterator var13 = var9.iterator(); var13.hasNext(); var15.clear()) {
                     var10 = (CMPBean)var13.next();
                     if (debugLogger.isDebugEnabled()) {
                        debug("set M2N Join Table INSERT for " + this.beanInfo.getEJBName() + ", pk '" + var10.__WL_getPrimaryKey() + "'");
                     }

                     Collection var14 = var10.__WL_getCmrBeansForCmrField(var5);
                     var15 = ((RDBMSM2NSet)var14).getAddSet();
                     if (var15.size() > 0) {
                        var12.clear();
                        Object var16 = ((RDBMSM2NSet)var14).getCreatorPk();

                        Object var18;
                        for(Iterator var17 = var15.iterator(); var17.hasNext(); var12.add(var18)) {
                           var18 = var17.next();
                           if (debugLogger.isDebugEnabled()) {
                              debug("setting Join Table INSERT params for thisPk: '" + var16 + "', otherBeanPK: '" + var18 + "'");
                           }

                           ((RDBMSM2NSet)var14).setAddJoinTableSQLParams(var8, var16, var18);

                           try {
                              if (debugLogger.isDebugEnabled()) {
                                 debug(this.beanInfo.getEJBName() + ": about to execute single M2NJoinTableInsert.");
                              }

                              var8.executeUpdate();
                           } catch (SQLException var26) {
                              Iterator var20 = var12.iterator();

                              while(var20.hasNext()) {
                                 var18 = var20.next();
                                 var15.remove(var18);
                              }

                              throw var26;
                           }
                        }
                     }
                  }
               }
            } catch (SQLException var27) {
               EJBLogger.logExcepInMethod1("executeM2NJoinTableInserts", var27);
               if (!var3) {
                  this.cacheRemoveCMPBeansOnError(var2, var9);
               }

               EJBRuntimeUtils.throwInternalException("EJB Exception:", var27);
            } finally {
               ((RDBMSPersistenceManager)this.persistence).releaseResources(var7, var8, (ResultSet)null);
            }
         }
      }
   }

   public void beforeCompletion(InvocationWrapper var1) throws InternalException {
      if (var1.getPrimaryKey() != null) {
         try {
            ArrayList var2 = new ArrayList();
            var2.add(var1.getPrimaryKey());
            this.beforeCompletion(var2, Thread.currentThread());
         } catch (InternalException var3) {
            throw (RuntimeException)var3.detail;
         }
      }

   }

   public void beforeCompletion(Object var1) throws InternalException {
      Collection var2 = this.finderRetToPks(var1);

      try {
         this.beforeCompletion(var2, Thread.currentThread());
      } catch (InternalException var4) {
         throw (RuntimeException)var4.detail;
      }
   }

   abstract void beforeCompletion(Collection var1, Object var2) throws InternalException;

   public void afterCompletion(InvocationWrapper var1) {
      if (var1.getPrimaryKey() != null) {
         ArrayList var2 = new ArrayList();
         var2.add(var1.getPrimaryKey());
         this.afterCompletion(var2, Thread.currentThread(), 6, (Object)null);
      }

   }

   public void afterCompletion(Object var1) {
      Collection var2 = this.finderRetToPks(var1);
      this.afterCompletion(var2, Thread.currentThread(), 6, (Object)null);
   }

   private boolean verifyQueriesForBatching(Collection var1) {
      boolean var2 = false;
      Iterator var3 = null;
      CMPBean var4 = null;
      int var5 = 0;
      if (debugLogger.isDebugEnabled()) {
         debug("perform a check to verify if the queries are fit for batching");
      }

      HashSet[] var6 = new HashSet[var1.size()];
      var3 = var1.iterator();

      while(var3.hasNext()) {
         var4 = (CMPBean)var3.next();
         if (!this.isBeanManagedPersistence && this.uses20CMP) {
            var6[var5++] = var4.__WL_getNullSnapshotVariables();
         }
      }

      int var7;
      HashSet var8;
      for(var7 = 0; var7 < var6.length; ++var7) {
         var8 = var6[var7];
         int var9 = var8.size();

         for(int var10 = var7 + 1; var10 < var6.length; ++var10) {
            HashSet var11 = var6[var10];
            if (var9 != var11.size()) {
               if (debugLogger.isDebugEnabled()) {
                  debug("the collection of snapshot variables that are null are of different sizes for bean " + this.beanInfo.getEJBName() + ", hence the queries are not suitable for batching");
               }

               var2 = true;
               break;
            }
         }
      }

      if (!var2) {
         label62:
         for(var7 = 0; var7 < var6.length; ++var7) {
            var8 = var6[var7];
            Iterator var13 = var8.iterator();

            while(true) {
               while(true) {
                  if (!var13.hasNext()) {
                     continue label62;
                  }

                  String var14 = (String)var13.next();

                  for(int var15 = var7 + 1; var15 < var6.length; ++var15) {
                     HashSet var12 = var6[var15];
                     if (!var12.contains(var14)) {
                        if (debugLogger.isDebugEnabled()) {
                           debug("the snapshot variables that are null for this batch of queries are different for bean " + this.beanInfo.getEJBName() + ", " + "hence the queries are not suitable for batching");
                        }

                        var2 = true;
                        break;
                     }
                  }
               }
            }
         }
      }

      return var2;
   }

   abstract void afterCompletion(Collection var1, Object var2, int var3, Object var4);

   private Collection finderRetToPks(Object var1) {
      ArrayList var2 = new ArrayList();

      try {
         if (var1 instanceof EJBObject) {
            var2.add(((EJBObject)var1).getPrimaryKey());
         } else if (var1 instanceof EJBLocalObject) {
            var2.add(((EJBLocalObject)var1).getPrimaryKey());
         } else if (var1 instanceof CMPBean) {
            var2.add(((CMPBean)var1).__WL_getPrimaryKey());
         } else {
            Object var4;
            if (var1 instanceof Enumeration) {
               Enumeration var3 = (Enumeration)var1;

               while(var3.hasMoreElements()) {
                  var4 = var3.nextElement();
                  if (var4 instanceof EJBObject) {
                     var2.add(((EJBObject)var4).getPrimaryKey());
                  } else if (var4 instanceof EJBLocalObject) {
                     var2.add(((EJBLocalObject)var4).getPrimaryKey());
                  }
               }
            } else if (var1 instanceof Collection) {
               Iterator var6 = ((Collection)var1).iterator();

               while(var6.hasNext()) {
                  var4 = var6.next();
                  if (var4 instanceof EJBObject) {
                     var2.add(((EJBObject)var4).getPrimaryKey());
                  } else if (var4 instanceof EJBLocalObject) {
                     var2.add(((EJBLocalObject)var4).getPrimaryKey());
                  }
               }
            }
         }

         return var2;
      } catch (Exception var5) {
         throw (RuntimeException)var5;
      }
   }

   public abstract void flushModified(Collection var1, javax.transaction.Transaction var2, boolean var3, Collection var4) throws InternalException;

   public PoolIntf getPool() {
      return this.entityPool;
   }

   public EntityBean getBeanFromRS(Object var1, RSInfo var2) throws InternalException {
      Object var3 = EJBRuntimeUtils.getInvokeTxOrThread();
      return this.getBeanFromRS(var3, var1, var2);
   }

   public abstract EntityBean getBeanFromRS(Object var1, Object var2, RSInfo var3) throws InternalException;

   public abstract void postFinderCleanup(Object var1, Collection var2, boolean var3, boolean var4);

   public EntityBean getBeanFromPool() throws InternalException {
      return (EntityBean)this.entityPool.getBean();
   }

   public EntityBean getReflectionTarget(boolean var1) throws InternalException {
      if (this.isBeanManagedPersistence) {
         return this.getBeanFromPool();
      } else {
         EntityBean var3;
         if (var1) {
            if (this.reflectionTargetLocal == null) {
               synchronized(this) {
                  if (this.reflectionTargetLocal == null) {
                     var3 = (EntityBean)((EntityPool)this.entityPool).createBean();
                     ((WLEnterpriseBean)var3).__WL_setIsLocal(true);
                     this.reflectionTargetLocal = var3;
                  }
               }
            }

            return this.reflectionTargetLocal;
         } else {
            if (this.reflectionTargetRemote == null) {
               synchronized(this) {
                  if (this.reflectionTargetRemote == null) {
                     var3 = (EntityBean)((EntityPool)this.entityPool).createBean();
                     ((WLEnterpriseBean)var3).__WL_setIsLocal(false);
                     this.reflectionTargetRemote = var3;
                  }
               }
            }

            return this.reflectionTargetRemote;
         }
      }
   }

   public void destroyReflectionTarget(EntityBean var1) {
      if (this.isBeanManagedPersistence) {
         this.destroyPooledBean(var1);
      }

   }

   public void releaseReflectionTarget(EntityBean var1) {
      if (this.isBeanManagedPersistence) {
         this.releaseBeanToPool(var1);
      }

   }

   protected void destroyPooledBean(EntityBean var1) {
      this.entityPool.destroyBean(var1);
   }

   public void destroyPooledInstance(InvocationWrapper var1, Throwable var2) {
      EntityBean var3 = (EntityBean)var1.getBean();
      this.destroyPooledBean(var3);
   }

   public void releaseBeanToPool(EntityBean var1) {
      EntityEJBContextImpl var2 = (EntityEJBContextImpl)((WLEnterpriseBean)var1).__WL_getEJBContext();
      var2.__WL_setPrimaryKey((Object)null);
      this.entityPool.releaseBean(var1);
   }

   protected abstract void cacheRemoveBean(javax.transaction.Transaction var1, Object var2);

   private void cacheRemoveCMPBeans(javax.transaction.Transaction var1, List var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         CMPBean var4 = (CMPBean)var3.next();
         this.cacheRemoveCMPBean(var1, var4);
      }

   }

   private void cacheRemoveCMPBean(javax.transaction.Transaction var1, CMPBean var2) {
      EntityEJBContextImpl var3 = (EntityEJBContextImpl)var2.__WL_getEntityContext();
      Object var4 = var3.__WL_getPrimaryKey();
      if (var2.__WL_getIsRemoved()) {
         var2.__WL_initialize();
         var2.__WL_setIsRemoved(false);
      }

      this.cacheRemoveBean(var1, var4);
   }

   protected abstract void cacheRemoveBeanOnError(javax.transaction.Transaction var1, Object var2);

   private void cacheRemoveCMPBeansOnError(javax.transaction.Transaction var1, Collection var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         CMPBean var4 = (CMPBean)var3.next();
         EntityEJBContextImpl var5 = (EntityEJBContextImpl)var4.__WL_getEntityContext();
         Object var6 = var5.__WL_getPrimaryKey();
         this.cacheRemoveBeanOnError(var1, var6);
      }

   }

   protected abstract EntityBean alreadyCached(Object var1, Object var2) throws InternalException;

   protected abstract boolean finderCacheInsert(Object var1, Object var2, EJBObject var3, EJBLocalObject var4, EntityBean var5) throws InternalException;

   protected boolean finderCacheInsert(EntityBean var1) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("called BaseEntityManager.finderCacheInsert...");
      }

      Object var2 = ((CMPBean)var1).__WL_getPrimaryKey();
      EntityEJBContextImpl var3 = (EntityEJBContextImpl)((WLEnterpriseBean)var1).__WL_getEJBContext();
      var3.__WL_setPrimaryKey(var2);
      if (!$assertionsDisabled && var2 == null) {
         throw new AssertionError();
      } else {
         EJBObject var4 = null;
         BaseEJBLocalObjectIntf var5 = null;
         if (this.remoteHome != null) {
            var4 = this.remoteHome.allocateEO(var2);
         }

         if (this.localHome != null) {
            var5 = this.localHome.allocateELO(var2);
         }

         Object var6 = EJBRuntimeUtils.getInvokeTxOrThread();
         return this.finderCacheInsert(var6, var2, var4, var5, var1);
      }
   }

   public Object finderCacheInsert(EntityBean var1, boolean var2) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("called BaseEntityManager.finderCacheInsert...");
      }

      Object var3 = ((CMPBean)var1).__WL_getPrimaryKey();
      if (!$assertionsDisabled && var3 == null) {
         throw new AssertionError();
      } else {
         EntityEJBContextImpl var4 = (EntityEJBContextImpl)((WLEnterpriseBean)var1).__WL_getEJBContext();
         var4.__WL_setPrimaryKey(var3);
         EJBObject var5 = null;
         BaseEJBLocalObjectIntf var6 = null;
         if (this.remoteHome != null) {
            var5 = this.remoteHome.allocateEO(var3);
         }

         if (this.localHome != null) {
            var6 = this.localHome.allocateELO(var3);
         }

         Object var7 = EJBRuntimeUtils.getInvokeTxOrThread();
         this.finderCacheInsert(var7, var3, var5, var6, var1);
         return var2 ? var6 : var5;
      }
   }

   public Object finderGetEoFromBeanOrPk(EntityBean var1, Object var2, boolean var3) {
      if (var1 != null) {
         return var3 ? ((EntityEJBContextImpl)((CMPBean)var1).__WL_getEntityContext()).__WL_getEJBLocalObject() : ((EntityEJBContextImpl)((CMPBean)var1).__WL_getEntityContext()).__WL_getEJBObject();
      } else {
         EJBObject var4 = null;
         BaseEJBLocalObjectIntf var5 = null;
         if (this.remoteHome != null) {
            var4 = this.remoteHome.allocateEO(var2);
         }

         if (this.localHome != null) {
            var5 = this.localHome.allocateELO(var2);
         }

         return var3 ? var5 : var4;
      }
   }

   protected void checkForReentrant(EntityBean var1, Object var2) throws InternalException {
      WLEnterpriseBean var3 = (WLEnterpriseBean)var1;
      Loggable var4;
      if (this.uses20CMP && ((CMPBean)var3).__WL_getIsRemoved()) {
         var4 = EJBLogger.lognoSuchEntityExceptionLoggable(var2.toString());
         throw new NoSuchEntityException(var4.getMessage());
      } else if (!this.isReentrant && var3.__WL_isBusy()) {
         var4 = EJBLogger.logillegalReentrantCallLoggable(this.ejbHome.getDisplayName(), var2.toString());
         throw new InternalException(var4.getMessage());
      }
   }

   public EJBObject remoteFindByPrimaryKey(Method var1, Object var2) throws InternalException {
      return (EJBObject)this.findByPrimaryKey(EJBRuntimeUtils.getInvokeTxOrThread(), var1, var2, false);
   }

   public EJBLocalObject localFindByPrimaryKey(Method var1, Object var2) throws InternalException {
      return (EJBLocalObject)this.findByPrimaryKey(EJBRuntimeUtils.getInvokeTxOrThread(), var1, var2, true);
   }

   public EJBObject remoteFindByPrimaryKey(InvocationWrapper var1, Object var2) throws InternalException {
      if (!$assertionsDisabled && var1 == null) {
         throw new AssertionError();
      } else {
         Object var3 = var1.getInvokeTxOrThread();
         Method var4 = this.getFindByPrimaryKeyMethod();
         return (EJBObject)this.findByPrimaryKey(var3, var4, var2, false);
      }
   }

   public Object localFindByPrimaryKey(InvocationWrapper var1, Object var2) throws InternalException {
      if (!$assertionsDisabled && var1 == null) {
         throw new AssertionError();
      } else {
         Object var3 = var1.getInvokeTxOrThread();
         Method var4 = this.getFindByPrimaryKeyMethod();
         return this.findByPrimaryKey(var3, var4, var2, true);
      }
   }

   private Object findByPrimaryKey(Object var1, Method var2, Object var3, boolean var4) throws InternalException {
      if (var3 == null) {
         throw new InternalException("Null primary key during findByPrimaryKey", new ObjectNotFoundException("Primary key was null!"));
      } else {
         EJBObject var5 = null;
         BaseEJBLocalObjectIntf var6 = null;
         if (this.remoteHome != null) {
            var5 = this.remoteHome.allocateEO(var3);
         }

         if (this.localHome != null) {
            var6 = this.localHome.allocateELO(var3);
         }

         this.cacheRTMBean.incrementCacheAccessCount();
         EntityBean var7 = this.alreadyCached(var1, var3);
         if (var7 != null) {
            this.cacheRTMBean.incrementCacheHitCount();
            return var4 ? var6 : var5;
         } else {
            Object var8 = null;
            EntityBean var9 = this.getReflectionTarget(var4);
            if (!$assertionsDisabled && var9 == null) {
               throw new AssertionError();
            } else {
               try {
                  if (this.findersLoadBean) {
                     Object var12 = this.persistence.findByPrimaryKey(var9, var2, var3);
                     this.postFinderCleanup(var3, (Collection)null, true, var4);
                     return var12;
                  }

                  var8 = this.persistence.findByPrimaryKey(var9, var2, var3);
                  if (var8 == null) {
                     Loggable var10 = EJBLogger.logfindByPkReturnedNullLoggable(this.ejbHome.getDisplayName(), (String)null);
                     throw new RemoteException(var10.getMessage());
                  }
               } catch (Throwable var11) {
                  this.destroyReflectionTarget(var9);
                  this.handleMethodException(var2, (Class[])null, var11);
               }

               if (!$assertionsDisabled && !var3.equals(var8)) {
                  throw new AssertionError("ejbFindByPrimaryKey returned" + var8 + " but we passed in :" + var3);
               } else {
                  this.releaseReflectionTarget(var9);
                  return var4 ? var6 : var5;
               }
            }
         }
      }
   }

   public EJBObject remoteScalarFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      Object var4 = var1.getInvokeTxOrThread();
      return (EJBObject)this.scalarFinder(var4, var2, var3, false);
   }

   public EJBLocalObject localScalarFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      Object var4 = var1.getInvokeTxOrThread();
      return (EJBLocalObject)this.scalarFinder(var4, var2, var3, true);
   }

   public EJBObject remoteScalarFinder(Method var1, Object[] var2) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("called ExclusiveEntityManager.scalarFinder...");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            debug("\tparam: " + var3 + ", " + var2[var3].getClass().getName() + ", " + var2[var3]);
         }
      }

      return (EJBObject)this.scalarFinder(EJBRuntimeUtils.getInvokeTxOrThread(), var1, var2, false);
   }

   public EJBLocalObject localScalarFinder(Method var1, Object[] var2) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("called ExclusiveEntityManager.scalarFinder...");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            debug("\tparam: " + var3 + ", " + var2[var3].getClass().getName() + ", " + var2[var3]);
         }
      }

      return (EJBLocalObject)this.scalarFinder(EJBRuntimeUtils.getInvokeTxOrThread(), var1, var2, true);
   }

   public Object scalarFinder(Object var1, Method var2, Object[] var3, boolean var4) throws InternalException {
      EntityBean var5 = this.getReflectionTarget(var4);
      Object var6 = null;

      try {
         if (this.findersLoadBean) {
            Object var10 = this.persistence.scalarFinder(var5, var2, var3);
            this.postFinderCleanup(var10, (Collection)null, false, var4);
            return var10;
         }

         var6 = this.persistence.scalarFinder(var5, var2, var3);
      } catch (Throwable var9) {
         this.destroyReflectionTarget(var5);
         this.handleMethodException(var2, (Class[])null, var9);
      }

      this.releaseReflectionTarget(var5);
      EJBObject var7 = null;
      BaseEJBLocalObjectIntf var8 = null;
      if (var6 == null) {
         return null;
      } else {
         if (this.remoteHome != null) {
            var7 = this.remoteHome.allocateEO(var6);
         }

         if (this.localHome != null) {
            var8 = this.localHome.allocateELO(var6);
         }

         return var4 ? var8 : var7;
      }
   }

   public Enumeration enumFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      EntityBean var4 = this.getReflectionTarget(var1.isLocal());
      Collection var5 = null;
      Enumeration var6 = null;
      Collection var7 = null;

      Loggable var8;
      try {
         if (this.findersLoadBean) {
            var7 = this.persistence.collectionFinder(var4, var2, var3);
         } else if (this.isBeanManagedPersistence) {
            var6 = this.persistence.enumFinder(var4, var2, var3);
            if (var6 == null) {
               var8 = EJBLogger.logfinderReturnedNullLoggable(var2.getName(), this.ejbHome.getDisplayName());
               throw new RemoteException(var8.getMessage());
            }
         } else {
            var5 = this.persistence.collectionFinder(var4, var2, var3);
         }
      } catch (Throwable var9) {
         this.destroyReflectionTarget(var4);
         this.handleMethodException(var2, (Class[])null, var9);
      }

      this.releaseReflectionTarget(var4);
      var8 = null;
      Object var10;
      if (this.findersLoadBean) {
         var10 = new EJBObjectEnum(var7);
         this.postFinderCleanup((Object)null, var7, false, var1.isLocal());
      } else if (this.isBeanManagedPersistence) {
         var10 = this.pkEnumToEnum(var6, var1.isLocal());
      } else {
         var10 = this.pkCollToEnum(var5, var1.isLocal());
      }

      return (Enumeration)var10;
   }

   public Collection collectionFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      return this.collectionFinder(var2, var3, false, var1.isLocal());
   }

   public Collection remoteCollectionFinder(Method var1, Object[] var2) throws InternalException {
      return this.collectionFinder(var1, var2, false, false);
   }

   public Collection localCollectionFinder(Method var1, Object[] var2) throws InternalException {
      return this.collectionFinder(var1, var2, false, true);
   }

   private Collection collectionFinder(Method var1, Object[] var2, boolean var3, boolean var4) throws InternalException {
      EntityBean var5 = this.getReflectionTarget(var4);
      Collection var6 = null;

      try {
         var6 = this.persistence.collectionFinder(var5, var1, var2);
         if (var6 == null) {
            Loggable var9 = EJBLogger.logfinderReturnedNullLoggable(var1.getName(), this.ejbHome.getDisplayName());
            throw new RemoteException(var9.getMessage());
         }
      } catch (Throwable var8) {
         this.destroyReflectionTarget(var5);
         this.handleMethodException(var1, (Class[])null, var8);
      }

      this.releaseReflectionTarget(var5);
      Object var7 = null;
      if (this.findersLoadBean) {
         if (var3) {
            var7 = new OrderedSet(var6);
         } else {
            var7 = new ArrayList(var6);
         }

         this.postFinderCleanup((Object)null, (Collection)var7, false, var4);
      } else if (var3) {
         var7 = this.pkCollToSet(var6, var4);
      } else {
         var7 = this.pkCollToColl(var6, var4);
      }

      return (Collection)var7;
   }

   public Object executePreparedQuery(String var1, InvocationWrapper var2, Method var3, boolean var4, Map var5, Map var6, PreparedQuery var7) throws InternalException {
      boolean var8 = var2.isLocal();
      Object var9 = null;

      try {
         var9 = this.rdbmsPersistence.executePreparedQuery(var1, var8, var4, var5, var6, var7);
      } catch (Throwable var11) {
         this.handleMethodException(var3, (Class[])null, var11);
      }

      return var9;
   }

   public Object dynamicSqlQuery(String var1, Object[] var2, WLQueryProperties var3, Method var4, boolean var5, Class var6, ConversationImpl var7) throws InternalException {
      Object var8 = null;

      try {
         var8 = this.rdbmsPersistence.dynamicSqlQuery(var1, var2, var3, var5, var6, var7);
      } catch (Throwable var10) {
         this.handleMethodException(var4, (Class[])null, var10);
      }

      return var8;
   }

   public Object dynamicQuery(String var1, Object[] var2, WLQueryProperties var3, Method var4, boolean var5, boolean var6) throws InternalException {
      Object var7 = null;

      try {
         var7 = this.rdbmsPersistence.dynamicQuery(var1, var3, var5, var6);
      } catch (Throwable var9) {
         this.handleMethodException(var4, (Class[])null, var9);
      }

      return var7;
   }

   public String nativeQuery(String var1) throws FinderException {
      return this.rdbmsPersistence.nativeQuery(var1);
   }

   public String getDatabaseProductName() throws FinderException {
      return this.rdbmsPersistence.getDatabaseProductName();
   }

   public String getDatabaseProductVersion() throws FinderException {
      return this.rdbmsPersistence.getDatabaseProductVersion();
   }

   public Set remoteWrapperSetFinder(Method var1, Object[] var2) throws InternalException {
      return this.wrapperSetFinder(var1, var2, false, false);
   }

   public Set remoteWrapperSetFinder(Method var1, Object[] var2, boolean var3) throws InternalException {
      return this.wrapperSetFinder(var1, var2, false, var3);
   }

   public Set localWrapperSetFinder(Method var1, Object[] var2) throws InternalException {
      return this.wrapperSetFinder(var1, var2, true, false);
   }

   public Set localWrapperSetFinder(Method var1, Object[] var2, boolean var3) throws InternalException {
      return this.wrapperSetFinder(var1, var2, true, var3);
   }

   public Set wrapperSetFinder(Method var1, Object[] var2, boolean var3, boolean var4) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("called wrapperSetFinder...");
      }

      EntityBean var5 = this.getReflectionTarget(var3);
      Collection var6 = null;
      Collection var7 = null;

      try {
         if (this.findersLoadBean) {
            var7 = this.persistence.collectionFinder(var5, var1, var2);
         } else {
            var6 = this.persistence.collectionFinder(var5, var1, var2);
         }
      } catch (Throwable var9) {
         if (debugLogger.isDebugEnabled()) {
            debug("\texception thrown in setFinder: ");
            var9.printStackTrace();
         }

         this.destroyReflectionTarget(var5);
         this.handleMethodException(var1, (Class[])null, var9);
      }

      this.releaseReflectionTarget(var5);
      Set var8 = null;
      if (this.findersLoadBean) {
         var8 = this.beanCollToObjectWrapperSet(var7, var3, var4);
      } else {
         var8 = this.pkCollToObjectWrapperSet(var6, var3, var4);
      }

      if (debugLogger.isDebugEnabled()) {
         debug("returning " + var8.size() + "objects from setFinder.");
      }

      return var8;
   }

   public Set remoteSetFinder(Method var1, Object[] var2) throws InternalException {
      return (Set)this.collectionFinder(var1, var2, true, false);
   }

   public Set localSetFinder(Method var1, Object[] var2) throws InternalException {
      return (Set)this.collectionFinder(var1, var2, true, true);
   }

   public void ensureDBExistence(Object var1) throws Throwable {
      EntityBean var2 = this.getBeanFromPool();
      ((WLEnterpriseBean)var2).__WL_setIsLocal(true);

      try {
         this.disableTransactionStatusCheck();
         this.persistence.findByPrimaryKey(var2, this.getFindByPrimaryKeyMethod(), var1);
         this.releaseBeanToPool(var2);
      } catch (Throwable var9) {
         this.destroyPooledBean(var2);
         if (var9 instanceof InternalException) {
            Throwable var4 = ((InternalException)var9).detail;
            if (var4 != null) {
               throw var4;
            }
         }

         throw var9;
      } finally {
         this.enableTransactionStatusCheck();
      }

   }

   public void disableTransactionStatusCheck() throws SystemException {
      javax.transaction.Transaction var1 = TxHelper.getTransactionManager().getTransaction();
      if (var1 != null) {
         Transaction var2 = (Transaction)var1;
         var2.setProperty("DISABLE_TX_STATUS_CHECK", "true");
      }
   }

   public void enableTransactionStatusCheck() throws SystemException {
      javax.transaction.Transaction var1 = TxHelper.getTransactionManager().getTransaction();
      if (var1 != null) {
         Transaction var2 = (Transaction)var1;
         var2.setProperty("DISABLE_TX_STATUS_CHECK", (Serializable)null);
      }
   }

   protected Iterator cascadeDeleteRemove(InvocationWrapper var1, EntityBean var2) throws InternalException {
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      HashMap var5 = new HashMap();
      EntityBean var6 = null;
      Iterator var7 = var3.iterator();
      if (!this.isCascadeDelete) {
         return var7;
      } else if (var2 instanceof CMPBean && this.uses20CMP) {
         try {
            ((CMPBean)var2).__WL_makeCascadeDelList(var5, var3, var4, false);
         } catch (Exception var12) {
            EJBRuntimeUtils.throwInternalException("Error creating cascade delet list.", var12);
            throw new AssertionError("cannot reach");
         }

         if (debugLogger.isDebugEnabled()) {
            debug("---------------------mapCascadeDelBeans, ejbName- " + this.beanInfo.getEJBName());
            Iterator var8 = var5.keySet().iterator();

            while(var8.hasNext()) {
               debug(var8.next().toString());
            }

            debug("---------------------listCascadeDelBeans, ejbName- " + this.beanInfo.getEJBName());
            var8 = var3.iterator();

            CMPBean var9;
            while(var8.hasNext()) {
               var9 = (CMPBean)var8.next();
               var9.__WL_setMethodState(16);
               debug(var9.__WL_getEntityContext().getPrimaryKey().toString());
            }

            debug("---------------------listCascadeDelBeansWithoutDBUpdate, ejbName- " + this.beanInfo.getEJBName());
            var8 = var4.iterator();

            while(var8.hasNext()) {
               var9 = (CMPBean)var8.next();
               var9.__WL_setMethodState(16);
               debug(var9.__WL_getEntityContext().getPrimaryKey().toString());
            }
         }

         if (!this.orderDatabaseOperations) {
            PersistenceManager var13 = ((CMPBean)var2).__WL_getPersistenceManager();
            if (var13 instanceof RDBMSPersistenceManager) {
               ((RDBMSPersistenceManager)var13).flushModifiedBeans();
            }
         }

         var7 = var4.iterator();

         while(var7.hasNext()) {
            try {
               var6 = (EntityBean)var7.next();
               this.getCMPBMFromBeanObj(var6).remove(var1, var6, true);
            } catch (Exception var11) {
               EJBRuntimeUtils.throwInternalException("Error creating cascade step 2.", var11);
            }
         }

         var7 = var3.iterator();

         while(var7.hasNext()) {
            try {
               var6 = (EntityBean)var7.next();
               if (var6 == var2) {
                  return var7;
               }

               this.getCMPBMFromBeanObj(var6).remove(var1, var6, false);
            } catch (Exception var10) {
               EJBRuntimeUtils.throwInternalException("Error during cascade delete.", var10);
            }
         }

         return var7;
      } else {
         return var7;
      }
   }

   protected void cascadeDeleteRemove(InvocationWrapper var1, EntityBean var2, Iterator var3) throws InternalException {
      if (this.uses20CMP) {
         EntityBean var4 = null;

         while(var3.hasNext()) {
            try {
               var4 = (EntityBean)var3.next();
               this.getCMPBMFromBeanObj(var4).remove(var1, var4, false);
            } catch (Exception var6) {
               EJBRuntimeUtils.throwInternalException("Error during cascade delete", var6);
            }
         }

      }
   }

   private CMPBeanManager getCMPBMFromBeanObj(EntityBean var1) {
      RDBMSPersistenceManager var2 = (RDBMSPersistenceManager)((CMPBean)var1).__WL_getPersistenceManager();
      CMPBeanManager var3 = (CMPBeanManager)var2.getBeanManager();
      return var3;
   }

   public abstract void unpin(Object var1, Object var2);

   private Set beanCollToObjectWrapperSet(Collection var1, boolean var2) {
      return this.beanCollToObjectWrapperSet(var1, var2, false);
   }

   private Set beanCollToObjectWrapperSet(Collection var1, boolean var2, boolean var3) {
      if (!$assertionsDisabled && var1 == null) {
         throw new AssertionError();
      } else {
         HashSet var4 = new HashSet();
         Iterator var5 = var1.iterator();

         while(var5.hasNext()) {
            Object var6 = var5.next();
            if (var6 == null) {
               if (!var3) {
                  var4.add((Object)null);
               }
            } else if (var2) {
               var4.add(new EloWrapper((EJBLocalObject)var6));
            } else {
               var4.add(new EoWrapper((EJBObject)var6));
            }
         }

         return var4;
      }
   }

   public Collection pkCollToColl(Collection var1, boolean var2) {
      if (!$assertionsDisabled && var1 == null) {
         throw new AssertionError();
      } else {
         ArrayList var3 = new ArrayList();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            if (var5 == null) {
               var3.add((Object)null);
            } else if (var2) {
               BaseEJBLocalObjectIntf var6 = this.localHome.allocateELO(var5);
               var3.add(var6);
            } else {
               EJBObject var7 = this.remoteHome.allocateEO(var5);
               var3.add(var7);
            }
         }

         return var3;
      }
   }

   private Enumeration pkEnumToEnum(Enumeration var1, boolean var2) {
      if (!$assertionsDisabled && var1 == null) {
         throw new AssertionError();
      } else {
         EJBObjectEnum var3 = new EJBObjectEnum();

         while(var1.hasMoreElements()) {
            Object var4 = var1.nextElement();
            if (var4 == null) {
               var3.addElement((Object)null);
            } else if (var2) {
               BaseEJBLocalObjectIntf var5 = this.localHome.allocateELO(var4);
               var3.addElement(var5);
            } else {
               EJBObject var6 = this.remoteHome.allocateEO(var4);
               var3.addElement(var6);
            }
         }

         return var3;
      }
   }

   private Enumeration pkCollToEnum(Collection var1, boolean var2) {
      if (!$assertionsDisabled && var1 == null) {
         throw new AssertionError();
      } else {
         EJBObjectEnum var3 = new EJBObjectEnum();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            if (var5 == null) {
               var3.addElement((Object)null);
            } else if (var2) {
               BaseEJBLocalObjectIntf var6 = this.localHome.allocateELO(var5);
               var3.addElement(var6);
            } else {
               EJBObject var7 = this.remoteHome.allocateEO(var5);
               var3.addElement(var7);
            }
         }

         return var3;
      }
   }

   private Set pkCollToSet(Collection var1, boolean var2) {
      if (!$assertionsDisabled && var1 == null) {
         throw new AssertionError();
      } else {
         OrderedSet var3 = new OrderedSet();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            if (var5 == null) {
               var3.add((Object)null);
            } else if (var2) {
               BaseEJBLocalObjectIntf var6 = this.localHome.allocateELO(var5);
               var3.add(var6);
            } else {
               EJBObject var7 = this.remoteHome.allocateEO(var5);
               var3.add(var7);
            }
         }

         return var3;
      }
   }

   public Set pkCollToObjectWrapperSet(Collection var1, boolean var2) {
      return this.pkCollToObjectWrapperSet(var1, var2, false);
   }

   public Set pkCollToObjectWrapperSet(Collection var1, boolean var2, boolean var3) {
      if (!$assertionsDisabled && var1 == null) {
         throw new AssertionError();
      } else {
         HashSet var4 = new HashSet();
         Iterator var5 = var1.iterator();

         while(var5.hasNext()) {
            Object var6 = var5.next();
            if (var6 == null) {
               if (!var3) {
                  var4.add((Object)null);
               }
            } else if (var2) {
               BaseEJBLocalObjectIntf var7 = this.localHome.allocateELO(var6);
               var4.add(new EloWrapper(var7));
            } else {
               EJBObject var8 = this.remoteHome.allocateEO(var6);
               var4.add(new EoWrapper(var8));
            }
         }

         return var4;
      }
   }

   public boolean hasBeansEnrolledInTx(javax.transaction.Transaction var1) {
      return this.getTxManager().hasListener(var1);
   }

   public void registerModifiedBean(Object var1, javax.transaction.Transaction var2) throws InternalException {
      if (var2 != null) {
         this.getTxManager().registerModifiedBean(var1, var2);
      }

   }

   public void registerInvalidatedBean(Object var1, Object var2) throws InternalException {
      if (var2 instanceof javax.transaction.Transaction) {
         this.getTxManager().registerInvalidatedBean(var1, (javax.transaction.Transaction)var2);
      } else {
         ((InvalidationBeanManager)this).invalidate(var2, var1);
      }

   }

   public void unregisterModifiedBean(Object var1, javax.transaction.Transaction var2) throws InternalException {
      if (var2 != null) {
         this.getTxManager().unregisterModifiedBean(var1, var2);
      }

   }

   public void flushModifiedBeans(javax.transaction.Transaction var1) throws InternalException {
      if (var1 != null) {
         this.getTxManager().flushModifiedBeans(var1);
      }
   }

   public void initializePool() throws WLDeploymentException {
      this.entityPool.createInitialBeans();
   }

   public void setupTxListenerAndTxUser(Object var1, Object var2, WLEnterpriseBean var3) throws InternalException {
      this.setupTxListener(var1, var2);
      var3.__WL_setLoadUser(RuntimeHelper.getCurrentNew());
   }

   public boolean needsRemoval(EnterpriseBean var1) {
      return var1 == null || this.beanClass != var1.getClass();
   }

   public void beanImplClassChangeNotification() {
      this.beanClass = ((EntityBeanInfo)this.beanInfo).getGeneratedBeanClass();
      this.entityPool.reset();
      this.persistence.updateClassLoader(this.beanInfo.getClassLoader());
   }

   public void updateKeyCacheSize(int var1) {
      if (this.uses20CMP && this.persistence instanceof RDBMSPersistenceManager) {
         ((RDBMSPersistenceManager)this.persistence).updateKeyCacheSize(var1);
      }

   }

   protected boolean shouldStore(EntityBean var1) throws Throwable {
      if (this.isModifiedMethod != null) {
         Boolean var2 = (Boolean)this.isModifiedMethod.invoke(var1, (Object[])null);
         return var2;
      } else {
         return true;
      }
   }

   public PartialOrderSet getEnrolledInTxKeys(javax.transaction.Transaction var1) {
      return this.getTxManager().getEnrolledKeys(var1);
   }

   public boolean isFlushPending(javax.transaction.Transaction var1, Object var2) {
      return this.getTxManager().isFlushPending(var1, var2);
   }

   public abstract boolean beanIsOpsComplete(javax.transaction.Transaction var1, Object var2);

   public int passivateUnModifiedBean(javax.transaction.Transaction var1, Object var2) {
      return !this.passivateBasicCheck(var1, var2) ? 0 : this.cachePassivateUnModifiedBean(var1, var2);
   }

   public boolean passivateLockedUnModifiedBean(javax.transaction.Transaction var1, Object var2, EntityBean var3) {
      if (this.getTxManager().isFlushPending(var1, var2)) {
         if (debugLogger.isDebugEnabled()) {
            debug(" bean is queued for a flush, cannot passivate as an unmodified bean. pk=" + var2);
         }

         return false;
      } else if (this.nonFKHolderRelationChange(var3)) {
         if (debugLogger.isDebugEnabled()) {
            debug("this bean is the non foreign key holder of a relationship that has been modified. This bean cannot be passivated.  pk=" + var2);
         }

         return false;
      } else if (this.m2NInsert(var3)) {
         if (debugLogger.isDebugEnabled()) {
            debug("this bean has had a deferred INSERT made to one of its many to many relationship member sets. This bean cannot be passivated.  pk=" + var2);
         }

         return false;
      } else {
         return true;
      }
   }

   public int passivateModifiedBean(javax.transaction.Transaction var1, Object var2, boolean var3) {
      if (!this.passivateBasicCheck(var1, var2)) {
         return 0;
      } else {
         return !var3 && !this.uses20CMP ? 0 : this.cachePassivateModifiedBean(var1, var2, var3);
      }
   }

   public boolean passivateLockedModifiedBean(javax.transaction.Transaction var1, Object var2, boolean var3, EntityBean var4) {
      if (!var3) {
         if (this.nonFKHolderRelationChange(var4)) {
            if (debugLogger.isDebugEnabled()) {
               debug(" flush did not succeed, and this bean is the non foreign key holder of a relationship that has been modified. This bean cannot be passivated.  pk=" + var2);
            }

            return false;
         }

         if (this.m2NInsert(var4)) {
            if (debugLogger.isDebugEnabled()) {
               debug("this bean has had a deferred INSERT made to one of its many to many relationship member sets. This bean cannot be passivated.  pk=" + var2);
            }

            return false;
         }
      }

      return true;
   }

   private boolean passivateBasicCheck(javax.transaction.Transaction var1, Object var2) {
      if (this.getVerifyReads()) {
         if (debugLogger.isDebugEnabled()) {
            debug(" bean is an optimistic concurrency bean with verify reads enabled. cannot passivate this bean, it must remain in cache. pk=" + var2);
         }

         return false;
      } else {
         return true;
      }
   }

   public boolean nonFKHolderRelationChange(EntityBean var1) {
      if (!this.uses20CMP) {
         return false;
      } else {
         return !(var1 instanceof CMPBean) ? false : ((CMPBean)var1).__WL_getNonFKHolderRelationChange();
      }
   }

   public boolean m2NInsert(EntityBean var1) {
      if (!this.uses20CMP) {
         return false;
      } else {
         return !(var1 instanceof CMPBean) ? false : ((CMPBean)var1).__WL_getM2NInsert();
      }
   }

   abstract boolean getVerifyReads();

   abstract int cachePassivateModifiedBean(javax.transaction.Transaction var1, Object var2, boolean var3);

   abstract int cachePassivateUnModifiedBean(javax.transaction.Transaction var1, Object var2);

   public void undeploy() {
      super.undeploy();
      if (this.reflectionTargetLocal != null) {
         this.releaseBeanToPool(this.reflectionTargetLocal);
      }

      if (this.reflectionTargetRemote != null) {
         this.releaseBeanToPool(this.reflectionTargetRemote);
      }

      if (this.entityPool != null) {
         this.entityPool.cleanup();
      }

   }

   public void reInitializePool() {
      this.entityPool.reInitializePool();
   }

   public boolean isBeanClassAbstract() {
      return true;
   }

   private static void debug(String var0) {
      debugLogger.debug("[BaseEntityManager] " + var0);
   }

   private static void debug(String var0, Throwable var1) {
      debugLogger.debug("[BaseEntityManager] " + var0, var1);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Database_Access_Around_High");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "BaseEntityManager.java", "weblogic.ejb.container.manager.BaseEntityManager", "executeInsertStmt", "(Ljava/util/List;Ljavax/transaction/Transaction;Ljava/util/Set;ZZLjava/util/Collection;)V", 642, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "BaseEntityManager.java", "weblogic.ejb.container.manager.BaseEntityManager", "executeUpdateStmt", "(Ljava/util/List;Ljavax/transaction/Transaction;Ljava/util/Set;ZZLjava/util/Collection;)V", 915, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "BaseEntityManager.java", "weblogic.ejb.container.manager.BaseEntityManager", "executeDeleteStmt", "(Ljava/util/List;Ljavax/transaction/Transaction;Ljava/util/Set;ZZLjava/util/Collection;)V", 1319, (Map)null, (boolean)0);
      $assertionsDisabled = !BaseEntityManager.class.desiredAssertionStatus();
   }
}
