package weblogic.ejb.container.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.dbeans.ConversationImpl;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.cache.EntityCache;
import weblogic.ejb.container.cache.TxPk;
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.InvalidationBeanManager;
import weblogic.ejb.container.manager.BaseEJBManager;
import weblogic.ejb.container.manager.BaseEntityManager;
import weblogic.ejb.container.manager.StatelessManager;
import weblogic.ejb.container.monitoring.EJBTransactionRuntimeMBeanImpl;
import weblogic.ejb.container.utils.PartialOrderSet;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.collections.ArrayMap;

public final class TxManager {
   private final ConcurrentHashMap listeners = new ConcurrentHashMap();
   private final BaseEJBHomeIntf ejbHome;
   private final BaseEJBManager beanManager;
   private BeanInfo beanInfo;
   private boolean isBMP = false;
   private boolean isDead = false;
   private final EJBTransactionRuntimeMBeanImpl rtMBean;
   private InvalidationBeanManager invalidationTargetBeanManager = null;
   private boolean doOptimisticInvalidation = false;
   private int instanceLockOrder = 100;

   public TxManager(BeanManager var1) {
      this.beanManager = (BaseEJBManager)var1;
      this.ejbHome = this.beanManager.getEJBHome();
      this.beanInfo = this.beanManager.getBeanInfo();
      if (var1 instanceof BaseEntityManager) {
         BaseEntityManager var2 = (BaseEntityManager)var1;
         EntityBeanInfo var3 = (EntityBeanInfo)var2.getBeanInfo();
         if (var3.isOptimistic() && var3.getCacheBetweenTransactions()) {
            this.doOptimisticInvalidation = true;
         }

         this.invalidationTargetBeanManager = var3.getInvalidationTargetBeanManager();
         this.instanceLockOrder = var3.getInstanceLockOrder();
         this.isBMP = var2.isBeanManagedPersistence();
      }

      this.rtMBean = (EJBTransactionRuntimeMBeanImpl)var1.getEJBRuntimeMBean().getTransactionRuntime();
   }

   public void registerSynchronization(Object var1, Transaction var2) throws InternalException {
      try {
         if (this.handleRollback(var2)) {
            return;
         }

         TxListener var3 = this.getListener(var2);
         if (!(this.beanManager instanceof StatelessManager)) {
            var3.addPrimaryKey(var1);
         }

         if (this.isBMP) {
            var3.addModifiedKey(var1);
         }
      } catch (Exception var4) {
         EJBRuntimeUtils.throwInternalException("EJB Exception: ", var4);
      }

   }

   public boolean hasListener(Transaction var1) {
      return this.listeners.get(var1) != null;
   }

   private TxListener getListener(Transaction var1) throws RollbackException, SystemException {
      TxListener var2 = (TxListener)this.listeners.get(var1);
      if (var2 != null) {
         if (var2.beforeCompletionInvoked) {
            var1.registerSynchronization(var2);
            var2.beforeCompletionInvoked = false;
         }

         return var2;
      } else {
         if (this.isDead) {
            var1.rollback();
         }

         TxListener var3 = new TxListener(var1);
         var2 = (TxListener)this.listeners.putIfAbsent(var1, var3);
         if (var2 != null) {
            return var2;
         } else {
            var1.registerSynchronization(var3);
            return var3;
         }
      }
   }

   public EntityCache getCache(Transaction var1) {
      TxListener var2 = (TxListener)this.listeners.get(var1);
      if (var2 == null) {
         return null;
      } else {
         ConversationImpl var3 = var2.getConversation();
         if (var3 == null) {
            return null;
         } else {
            Object var4 = null;
            return (EntityCache)var4;
         }
      }
   }

   public void setConversation(Transaction var1, ConversationImpl var2) throws RollbackException, SystemException {
      TxListener var3 = this.getListener(var1);
      var3.setConversation(var2);
   }

   public PartialOrderSet getEnrolledKeys(Transaction var1) {
      if (!this.hasListener(var1)) {
         return null;
      } else {
         try {
            return this.getListener(var1).getPrimaryKeys();
         } catch (Exception var3) {
            return null;
         }
      }
   }

   public ArrayList getNotModifiedOtherTxKeys(Transaction var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.listeners.keySet().iterator();

      while(var3.hasNext()) {
         Transaction var4 = (Transaction)var3.next();
         if (var4 != null && var4 != var1) {
            TxListener var5 = (TxListener)this.listeners.get(var4);
            if (var5 != null) {
               var2.addAll(var5.getNotModifiedKeys());
            }
         }
      }

      return var2;
   }

   public boolean isFlushPending(Transaction var1, Object var2) {
      if (!this.hasListener(var1)) {
         return false;
      } else {
         try {
            return this.getListener(var1).isFlushPending(var2);
         } catch (Exception var4) {
            return false;
         }
      }
   }

   public boolean needsToBeInserted(Transaction var1, Object var2) throws SystemException, RollbackException {
      TxListener var3 = this.getListener(var1);
      return var3.needsToBeInserted(var2);
   }

   public ArrayList getFlushedKeys(Transaction var1) {
      if (!this.hasListener(var1)) {
         return null;
      } else {
         try {
            return this.getListener(var1).getFlushedKeys();
         } catch (Exception var3) {
            return null;
         }
      }
   }

   private boolean handleRollback(Transaction var1) throws SystemException {
      if (this.isDead) {
         var1.rollback();
      }

      return this.isDead;
   }

   private TxListener getExistingListener(Transaction var1) throws InternalException {
      try {
         return this.handleRollback(var1) ? null : (TxListener)this.listeners.get(var1);
      } catch (SystemException var3) {
         EJBRuntimeUtils.throwInternalException("EJB Exception: ", var3);
         throw new AssertionError();
      }
   }

   public void executeUpdateOperations(Transaction var1, Set var2, boolean var3, boolean var4) throws InternalException {
      TxListener var5 = this.getExistingListener(var1);
      if (var5 != null) {
         var5.executeUpdate(var2, var3, var4);
      }
   }

   public void executeDeleteOperations(Transaction var1, Set var2, boolean var3, boolean var4) throws InternalException {
      TxListener var5 = this.getExistingListener(var1);
      if (var5 != null) {
         var5.executeDelete(var2, var3, var4);
      }
   }

   public void executeInsertOperations(Transaction var1, Set var2, boolean var3, boolean var4) throws InternalException {
      TxListener var5 = this.getExistingListener(var1);
      if (var5 != null) {
         var5.executeInsert(var2, var3, var4);
      }
   }

   public void registerInsertBean(Object var1, Transaction var2) throws InternalException {
      try {
         if (this.handleRollback(var2)) {
            return;
         }

         TxListener var3 = this.getListener(var2);
         var3.addPrimaryKey(var1);
         var3.addInsertKey(var1);
      } catch (Exception var4) {
         EJBRuntimeUtils.throwInternalException("EJB Exception: ", var4);
      }

   }

   public boolean registerDeleteBean(Object var1, Transaction var2) throws InternalException {
      try {
         return this.handleRollback(var2) ? false : this.getListener(var2).addDeleteKey(var1);
      } catch (Exception var4) {
         EJBRuntimeUtils.throwInternalException("EJB Exception: ", var4);
         return false;
      }
   }

   public void registerInsertDeletedBean(Object var1, Transaction var2) throws InternalException {
      try {
         if (this.handleRollback(var2)) {
            return;
         }

         this.getListener(var2).addInsertDeletedKey(var1);
      } catch (Exception var4) {
         EJBRuntimeUtils.throwInternalException("EJB Exception: ", var4);
      }

   }

   public void registerM2NJoinTableInsert(Object var1, String var2, Transaction var3) throws InternalException {
      try {
         if (this.handleRollback(var3)) {
            return;
         }

         this.getListener(var3).addM2NJoinTableInsertMap(var1, var2);
      } catch (Exception var5) {
         EJBRuntimeUtils.throwInternalException("EJB Exception: ", var5);
      }

   }

   public void registerModifiedBean(Object var1, Transaction var2) throws InternalException {
      try {
         if (this.handleRollback(var2)) {
            return;
         }

         this.getListener(var2).addModifiedKey(var1);
      } catch (Exception var4) {
         EJBRuntimeUtils.throwInternalException("EJB Exception: ", var4);
      }

   }

   public void registerInvalidatedBean(Object var1, Transaction var2) throws InternalException {
      try {
         if (this.handleRollback(var2)) {
            return;
         }

         this.getListener(var2).addInvalidationKey(var1);
      } catch (Exception var4) {
         EJBRuntimeUtils.throwInternalException("EJB Exception: ", var4);
      }

   }

   public void unregisterModifiedBean(Object var1, Transaction var2) throws InternalException {
      try {
         if (this.handleRollback(var2)) {
            return;
         }
      } catch (Exception var7) {
         EJBRuntimeUtils.throwInternalException("EJB Exception: ", var7);
      }

      TxListener var3 = (TxListener)this.listeners.get(var2);
      if (var3 == null) {
         synchronized(this.listeners) {
            var3 = (TxListener)this.listeners.get(var2);
            if (var3 == null) {
               throw new AssertionError("Fatal error: attempted to unregister an EJB 2.0 CMP bean that was not registered with a transaction.");
            }
         }
      }

      var3.removeModifiedKey(var1);
   }

   public void flushModifiedBeans(Transaction var1) throws InternalException {
      this.flushModifiedBeans(var1, false);
   }

   public void flushModifiedBeans(Transaction var1, boolean var2) throws InternalException {
      weblogic.transaction.Transaction var3 = (weblogic.transaction.Transaction)var1;
      Object var4 = (Set)var3.getLocalProperty("modifiedListeners");
      Object var5 = (Set)var3.getLocalProperty("modifiedBMPListeners");
      Boolean var6 = (Boolean)var3.getLocalProperty("alreadyOwned");
      boolean var7 = false;
      if (var6 == null) {
         var3.setLocalProperty("alreadyOwned", new Boolean(true));
         var7 = true;
      } else {
         Set var8 = (Set)var3.getLocalProperty("currentIterationListeners");
         if (var4 != null) {
            HashSet var9 = new HashSet();
            var9.addAll(var8);
            var9.addAll((Collection)var4);
            var4 = var9;
         } else {
            var4 = var8;
         }
      }

      while(var4 != null) {
         var3.setLocalProperty("modifiedListeners", (Object)null);
         var3.setLocalProperty("currentIterationListeners", var4);
         if (var2) {
            this.initializeFlushHistory((Set)var4);
         }

         Iterator var10 = ((Set)var4).iterator();

         while(var10.hasNext()) {
            TxListener var11 = (TxListener)var10.next();
            var11.flushModifiedKeys(var2);
            if (var11.isBMPListener()) {
               if (var5 == null) {
                  var5 = new HashSet();
                  var3.setLocalProperty("modifiedBMPListeners", var5);
               }

               ((Set)var5).add(var11);
            }
         }

         var4 = (Set)var3.getLocalProperty("modifiedListeners");
      }

      if (var7) {
         var3.setLocalProperty("alreadyOwned", (Object)null);
         var3.setLocalProperty("modifiedListeners", var5);
         var3.setLocalProperty("modifiedBMPListeners", (Object)null);
         var3.setLocalProperty("currentIterationListeners", (Object)null);
         var7 = false;
      }

   }

   private void initializeFlushHistory(Set var1) {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            TxListener var3 = (TxListener)var2.next();
            var3.initializeFlushHistory();
         }
      }

   }

   public void undeploy() {
      this.isDead = true;
      Iterator var1 = this.listeners.values().iterator();

      while(var1.hasNext()) {
         TxListener var2 = (TxListener)var1.next();

         try {
            var2.tx.rollback();
         } catch (IllegalStateException var4) {
         } catch (SystemException var5) {
            EJBLogger.logIgnoreExcepOnRollback(this.beanInfo.getDisplayName(), var5);
         }
      }

   }

   private final class TxListener implements Synchronization {
      private static final boolean debug = false;
      private final Transaction tx;
      private final weblogic.transaction.Transaction wtx;
      private PartialOrderSet primaryKeys;
      private List modifiedKeys;
      private final List insertKeys;
      private final List deleteKeys;
      private final LinkedList invalidationKeys;
      private ArrayList flushedModifiedKeys = new ArrayList();
      private ArrayList flushedInsertKeys = null;
      private ArrayList flushedDeleteKeys = null;
      private ArrayList flushedKeys = null;
      private ArrayMap m2nJoinTableInsertMap;
      private ConversationImpl conversation = null;
      private EntityCache entityCache = null;
      private final ClassLoader cl;
      private boolean beforeCompletionInvoked;
      private boolean needsFlushModified;
      private boolean isJtaCallback;
      public static final String MODIFIED_LISTENERS = "modifiedListeners";
      public static final String MODIFIED_BMP_LISTENERS = "modifiedBMPListeners";
      public static final String CURRENT_ITERATION_LISTENERS = "currentIterationListeners";
      public static final String ALREADY_OWNED = "alreadyOwned";

      TxListener(Transaction var2) {
         this.tx = var2;
         this.wtx = (weblogic.transaction.Transaction)this.tx;
         this.primaryKeys = new PartialOrderSet(TxManager.this.instanceLockOrder);
         this.modifiedKeys = Collections.synchronizedList(new LinkedList());
         this.insertKeys = Collections.synchronizedList(new LinkedList());
         this.deleteKeys = Collections.synchronizedList(new LinkedList());
         this.m2nJoinTableInsertMap = null;
         this.beforeCompletionInvoked = false;
         this.needsFlushModified = false;
         this.isJtaCallback = true;
         if (TxManager.this.invalidationTargetBeanManager == null && !TxManager.this.doOptimisticInvalidation) {
            this.invalidationKeys = null;
         } else {
            this.invalidationKeys = new LinkedList();
         }

         this.cl = TxManager.this.beanInfo.getModuleClassLoader();
      }

      public void setConversation(ConversationImpl var1) {
         this.conversation = var1;
      }

      public ConversationImpl getConversation() {
         return this.conversation;
      }

      public boolean isBMPListener() {
         return TxManager.this.isBMP;
      }

      public void addPrimaryKey(Object var1) {
         this.primaryKeys.add(var1);
      }

      PartialOrderSet getPrimaryKeys() {
         return this.primaryKeys;
      }

      public ArrayList getNotModifiedKeys() {
         ArrayList var1 = new ArrayList();
         Iterator var2 = this.primaryKeys.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (!this.isFlushPending(var3)) {
               var1.add(new TxPk(this.tx, var3));
            }
         }

         return var1;
      }

      private void enrollAsModifiedListener() {
         this.needsFlushModified = false;
         Object var1 = (Set)this.wtx.getLocalProperty("modifiedListeners");
         if (var1 == null) {
            var1 = new HashSet();
            this.wtx.setLocalProperty("modifiedListeners", var1);
         }

         ((Set)var1).add(this);
      }

      public boolean isFlushPending(Object var1) {
         if (this.modifiedKeys.contains(var1)) {
            return true;
         } else if (this.insertKeys.contains(var1)) {
            return true;
         } else {
            return this.deleteKeys.contains(var1);
         }
      }

      public void addModifiedKey(Object var1) {
         if (this.modifiedKeys.isEmpty()) {
            this.enrollAsModifiedListener();
         }

         this.modifiedKeys.add(var1);
         if (this.invalidationKeys != null) {
            this.invalidationKeys.add(var1);
         }

      }

      public void addInvalidationKey(Object var1) {
         if (this.invalidationKeys != null) {
            this.invalidationKeys.add(var1);
         }

      }

      public void removeModifiedKey(Object var1) {
         this.modifiedKeys.remove(var1);
         if (this.modifiedKeys.isEmpty() && this.insertKeys.isEmpty() && this.deleteKeys.isEmpty()) {
            this.needsFlushModified = true;
            Set var2 = (Set)this.wtx.getLocalProperty("modifiedListeners");
            if (var2 != null) {
               var2.remove(this);
            }
         }

      }

      public void addInsertKey(Object var1) {
         if (this.insertKeys.isEmpty()) {
            this.enrollAsModifiedListener();
         }

         this.insertKeys.add(var1);
      }

      public boolean needsToBeInserted(Object var1) {
         return this.insertKeys.contains(var1);
      }

      public boolean addDeleteKey(Object var1) {
         if (this.insertKeys.remove(var1)) {
            this.removeModifiedKey(var1);
            return false;
         } else {
            if (this.deleteKeys.isEmpty()) {
               this.enrollAsModifiedListener();
            }

            this.deleteKeys.add(var1);
            if (this.invalidationKeys != null) {
               this.invalidationKeys.add(var1);
            }

            return true;
         }
      }

      public void addInsertDeletedKey(Object var1) {
         if (this.deleteKeys.remove(var1) && !this.modifiedKeys.contains(var1)) {
            this.addModifiedKey(var1);
         }

      }

      public void addM2NJoinTableInsertMap(Object var1, String var2) {
         if (this.m2nJoinTableInsertMap == null) {
            this.m2nJoinTableInsertMap = new ArrayMap();
         }

         ArrayList var3 = (ArrayList)this.m2nJoinTableInsertMap.get(var2);
         if (var3 == null) {
            var3 = new ArrayList();
            this.m2nJoinTableInsertMap.put(var2, var3);
         }

         if (!var3.contains(var1)) {
            var3.add(var1);
         }

      }

      private ArrayList getFlushedKeys() {
         if (this.flushedModifiedKeys == null && this.flushedInsertKeys == null && this.flushedDeleteKeys == null) {
            return null;
         } else {
            if (this.flushedKeys == null) {
               this.flushedKeys = new ArrayList();
            } else {
               this.flushedKeys.clear();
            }

            if (this.flushedModifiedKeys != null) {
               this.flushedKeys.addAll(this.flushedModifiedKeys);
            }

            if (this.flushedInsertKeys != null) {
               this.flushedKeys.addAll(this.flushedInsertKeys);
            }

            if (this.flushedDeleteKeys != null) {
               this.flushedKeys.addAll(this.flushedDeleteKeys);
            }

            return this.flushedKeys;
         }
      }

      public void executeDBOperations(boolean var1, boolean var2) throws InternalException {
         HashSet var3 = new HashSet();
         this.executeInsert(var3, var1, var2);
         var3.clear();
         this.executeUpdate(var3, var1, var2);
         var3.clear();
         this.executeDelete(var3, var1, var2);
         this.executeM2NJoinTableInserts(var2);
      }

      private void executeDelete(Set var1, boolean var2, boolean var3) throws InternalException {
         ((BaseEntityManager)TxManager.this.beanManager).executeDeleteStmt(this.deleteKeys, this.tx, var1, var2, var3, this.flushedDeleteKeys);
         this.deleteKeys.clear();
         if (!var2) {
            Set var4 = (Set)this.wtx.getLocalProperty("modifiedListeners");
            if (var4 != null) {
               var4.remove(this);
            }
         }

      }

      private void executeUpdate(Set var1, boolean var2, boolean var3) throws InternalException {
         ((BaseEntityManager)TxManager.this.beanManager).executeUpdateStmt(this.modifiedKeys, this.tx, var1, var2, var3, this.flushedModifiedKeys);
         this.modifiedKeys.clear();
      }

      private void executeInsert(Set var1, boolean var2, boolean var3) throws InternalException {
         if (var2) {
            this.flushModifiedKeys(var3);
         } else {
            this.isJtaCallback = false;
            this.beforeCompletion();
         }

         this.isJtaCallback = true;
         ((BaseEntityManager)TxManager.this.beanManager).executeInsertStmt(this.insertKeys, this.tx, var1, var2, var3, this.flushedInsertKeys);
         this.insertKeys.clear();
      }

      public void executeM2NJoinTableInserts(boolean var1) throws InternalException {
         if (this.m2nJoinTableInsertMap != null) {
            ((BaseEntityManager)TxManager.this.beanManager).executeM2NJoinTableInserts(this.m2nJoinTableInsertMap, this.tx, var1);
            this.m2nJoinTableInsertMap.clear();
         }
      }

      public void afterCompletion(int var1) {
         if (var1 == 3) {
            TxManager.this.rtMBean.incrementTransactionsCommitted();
         } else {
            TxManager.this.rtMBean.incrementTransactionsRolledBack();
            weblogic.transaction.Transaction var2 = (weblogic.transaction.Transaction)this.tx;
            if (var2.isTimedOut()) {
               TxManager.this.rtMBean.incrementTransactionsTimedOut();
            }
         }

         if (TxManager.this.beanManager instanceof StatelessManager) {
            TxManager.this.listeners.remove(this.tx);
         } else {
            Thread var21 = Thread.currentThread();
            ClassLoader var3 = var21.getContextClassLoader();

            try {
               var21.setContextClassLoader(this.cl);
               TxManager.this.ejbHome.pushEnvironment();
               if (this.invalidationKeys != null && !this.invalidationKeys.isEmpty() && var1 == 3) {
                  if (TxManager.this.doOptimisticInvalidation) {
                     try {
                        ((InvalidationBeanManager)TxManager.this.beanManager).invalidate(this.tx, (Collection)this.invalidationKeys);
                     } catch (InternalException var18) {
                        EJBLogger.logExceptionDuringROInvalidation(TxManager.this.beanInfo.getDisplayName(), StackTraceUtils.throwable2StackTrace(var18));
                     } finally {
                        TxManager.this.beanManager.afterCompletion(this.primaryKeys, this.tx, var1, this.entityCache);
                     }
                  } else {
                     TxManager.this.beanManager.afterCompletion(this.primaryKeys, this.tx, var1, this.entityCache);

                     try {
                        TxManager.this.invalidationTargetBeanManager.invalidate((Object)null, (Collection)this.invalidationKeys);
                     } catch (InternalException var17) {
                        EJBLogger.logExceptionDuringROInvalidation(TxManager.this.beanInfo.getDisplayName(), StackTraceUtils.throwable2StackTrace(var17));
                     }
                  }
               } else {
                  TxManager.this.beanManager.afterCompletion(this.primaryKeys, this.tx, var1, this.entityCache);
               }
            } finally {
               var21.setContextClassLoader(var3);
               TxManager.this.listeners.remove(this.tx);
               TxManager.this.ejbHome.popEnvironment();
            }

         }
      }

      public void beforeCompletion() {
         if (!(TxManager.this.beanManager instanceof StatelessManager)) {
            Thread var1 = Thread.currentThread();
            ClassLoader var2 = var1.getContextClassLoader();

            try {
               var1.setContextClassLoader(this.cl);
               TxManager.this.ejbHome.pushEnvironment();
               if (!this.beforeCompletionInvoked) {
                  boolean var3 = false;
                  PartialOrderSet var4 = this.primaryKeys;

                  try {
                     while(!this.primaryKeys.isEmpty()) {
                        if (var3) {
                           this.addToPartialOrderSet(var4, this.primaryKeys);
                        }

                        PartialOrderSet var5 = this.primaryKeys;
                        this.primaryKeys = new PartialOrderSet(TxManager.this.instanceLockOrder);
                        TxManager.this.beanManager.beforeCompletion(var5, this.tx);
                        var3 = true;
                     }
                  } finally {
                     this.primaryKeys = var4;
                     this.beforeCompletionInvoked = true;
                  }
               }

               if (TxManager.this.beanManager instanceof BaseEntityManager && ((BaseEntityManager)TxManager.this.beanManager).getOrderDatabaseOperations() && this.isJtaCallback) {
                  this.executeDBOperations(false, false);
               }
            } catch (InternalException var17) {
               EJBRuntimeUtils.throwRuntimeException("Error in beforeCompletion", var17);
            } finally {
               var1.setContextClassLoader(var2);
               TxManager.this.ejbHome.popEnvironment();
            }

         }
      }

      public void flushModifiedKeys(boolean var1) throws InternalException {
         if (!this.needsFlushModified) {
            this.needsFlushModified = true;

            try {
               TxManager.this.ejbHome.pushEnvironment();
               List var2 = this.modifiedKeys;
               boolean var3 = false;

               while(true) {
                  if (this.modifiedKeys.isEmpty()) {
                     this.modifiedKeys = var2;
                     this.needsFlushModified = true;
                     if (((BaseEntityManager)TxManager.this.beanManager).getOrderDatabaseOperations()) {
                        if (this.isJtaCallback) {
                           this.executeDBOperations(true, var1);
                        }
                     } else if (TxManager.this.isBMP) {
                        this.needsFlushModified = false;
                     } else {
                        this.modifiedKeys.clear();
                     }
                     break;
                  }

                  if (var3) {
                     var2.addAll(this.modifiedKeys);
                  }

                  ArrayList var4 = new ArrayList();
                  synchronized(this.modifiedKeys) {
                     Iterator var6 = this.modifiedKeys.iterator();

                     while(true) {
                        if (!var6.hasNext()) {
                           break;
                        }

                        Object var7 = var6.next();
                        if (var7 != null) {
                           var4.add(var7);
                        }
                     }
                  }

                  this.modifiedKeys = Collections.synchronizedList(new LinkedList());
                  ArrayList var5 = new ArrayList();
                  synchronized(this.flushedModifiedKeys) {
                     Iterator var18 = this.flushedModifiedKeys.iterator();

                     while(var18.hasNext()) {
                        Object var8 = var18.next();
                        if (var8 != null) {
                           var5.add(var8);
                        }
                     }
                  }

                  ((BaseEntityManager)TxManager.this.beanManager).flushModified(var4, this.tx, var1, var5);
                  var3 = true;
               }
            } finally {
               TxManager.this.ejbHome.popEnvironment();
            }

         }
      }

      private void addToPartialOrderSet(Collection var1, Collection var2) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (var4 != null) {
               var1.add(var4);
            }
         }

      }

      protected void initializeFlushHistory() {
         if (this.flushedModifiedKeys != null) {
            this.flushedModifiedKeys.clear();
         } else {
            this.flushedModifiedKeys = new ArrayList();
         }

         if (this.flushedInsertKeys != null) {
            this.flushedInsertKeys.clear();
         } else {
            this.flushedInsertKeys = new ArrayList();
         }

         if (this.flushedDeleteKeys != null) {
            this.flushedDeleteKeys.clear();
         } else {
            this.flushedDeleteKeys = new ArrayList();
         }

         if (this.flushedKeys != null) {
            this.flushedKeys.clear();
         } else {
            this.flushedKeys = new ArrayList();
         }

      }
   }
}
