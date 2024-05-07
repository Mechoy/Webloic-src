package weblogic.ejb.container.timer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.common.CompletionRequest;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.ejb.WLTimerInfo;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.TimerHelper;
import weblogic.ejb.container.interfaces.TimerManager;
import weblogic.ejb.container.internal.MethodDescriptor;
import weblogic.ejb.container.monitoring.EJBTimerRuntimeMBeanImpl;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.management.runtime.EJBTimerRuntimeMBean;
import weblogic.store.PersistentHandle;
import weblogic.store.PersistentStoreConnection;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreManager;
import weblogic.store.PersistentStoreRecord;
import weblogic.store.PersistentStoreTransaction;
import weblogic.store.gxa.GXAException;
import weblogic.store.gxa.GXAOperation;
import weblogic.store.gxa.GXAOperationWrapper;
import weblogic.store.gxa.GXAResource;
import weblogic.store.gxa.GXATraceLogger;
import weblogic.store.gxa.GXATransaction;
import weblogic.store.gxa.GXid;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.timers.Timer;
import weblogic.timers.TimerManagerFactory;

public final class EJBTimerManager implements TimerManager {
   private static final DebugLogger debugLogger;
   private weblogic.timers.TimerManager timerManager;
   private BeanManager beanManager;
   private EJBTimerRuntimeMBeanImpl timerRtMBean;
   private ClassLoader moduleClassLoader;
   private MethodDescriptor md;
   private Map pk2EJBTimerMap = new HashMap();
   private Map id2EJBTimerMap = new HashMap();
   private Map disabledTimers = new HashMap();
   private PersistentStoreXA persistentStore;
   private PersistentStoreConnection persistentConnection;
   private GXAResource gxa;
   private boolean isTransactional;
   private boolean isInitialized = false;
   long timerIDCounter = 0L;
   Object IDLock = new Object();
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = 6799463590828290438L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.timer.EJBTimerManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;
   public static final JoinPoint _WLDF$INST_JPFLD_3;
   public static final JoinPoint _WLDF$INST_JPFLD_4;

   public EJBTimerManager(BeanManager var1) {
      this.beanManager = var1;
   }

   public void setup(EJBTimerRuntimeMBean var1) throws WLDeploymentException {
      this.timerRtMBean = (EJBTimerRuntimeMBeanImpl)var1;

      try {
         TimerManagerFactory var2 = TimerManagerFactory.getTimerManagerFactory();
         BeanInfo var3 = this.beanManager.getBeanInfo();
         this.timerManager = var2.getTimerManager(var3.getIsIdenticalKey(), var3.getDispatchPolicy());
         this.moduleClassLoader = var3.getModuleClassLoader();
         this.initializePersistentStore(var3);
         this.initializeMethodDescriptor(var3);
         this.initializeTimerHelper(var3);
         this.initializePersistedTimers();
      } catch (WLDeploymentException var4) {
         this.undeploy();
         throw var4;
      }
   }

   public EJBTimerRuntimeMBeanImpl getTimerRuntimeMBean() {
      return this.timerRtMBean;
   }

   public void disableTimer(TimerImpl var1) {
      Timer var2 = var1.getTimer();
      if (var2 != null) {
         var2.cancel();
      }

      synchronized(this.disabledTimers) {
         this.disabledTimers.put(var1.getID(), var1);
         this.timerRtMBean.incrementDisabledTimerCount();
      }
   }

   public void enableDisabledTimers() {
      if (this.isInitialized) {
         synchronized(this.disabledTimers) {
            Iterator var2 = this.disabledTimers.values().iterator();

            while(var2.hasNext()) {
               TimerImpl var3 = (TimerImpl)var2.next();
               if (!var3.isCancelled()) {
                  this.scheduleTimer(var3);
               }
            }

            this.disabledTimers.clear();
            if (this.timerRtMBean != null) {
               this.timerRtMBean.resetDisabledTimerCount();
            }

         }
      }
   }

   public void scheduleTimer(TimerImpl var1) {
      boolean var10;
      boolean var10000 = var10 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var11 = null;
      DiagnosticActionState[] var12 = null;
      Object var9 = null;
      if (var10000) {
         JoinPoint var17 = _WLDF$INST_JPFLD_0;
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High;
         DiagnosticAction[] var10002 = var11 = var10001.getActions();
         InstrumentationSupport.preProcess(var17, var10001, var10002, var12 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         if (!this.isInitialized) {
            if (debugLogger.isDebugEnabled()) {
               debug("TimerManager not initialized, adding timer to disabledTimers");
            }

            synchronized(this.disabledTimers) {
               if (!this.isInitialized) {
                  this.disabledTimers.put(var1.getID(), var1);
                  return;
               }
            }
         }

         Timer var2;
         if (!var1.isIntervalTimer()) {
            if (!$assertionsDisabled && var1.getTimer() != null && var1.getTimer().cancel()) {
               throw new AssertionError("cancel succeeded unexpectedly");
            }

            if (debugLogger.isDebugEnabled()) {
               debug("Scheduling single expiration Timer: " + var1);
            }

            var2 = this.timerManager.schedule(var1, var1.getNextExpiration());
            var1.setTimer(var2);
         } else {
            if (debugLogger.isDebugEnabled()) {
               debug("Scheduling interval Timer: " + var1);
            }

            var2 = var1.getTimer();
            if (var2 != null) {
               var2.cancel();
            }

            var1.accountForSkippedIntervals();
            var2 = this.timerManager.scheduleAtFixedRate(var1, var1.getNextExpiration(), var1.getTimerData().getIntervalDuration());
            var1.setTimer(var2);
         }

      } finally {
         if (var10) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var11, var12);
         }

      }
   }

   public javax.ejb.Timer createTimer(Object var1, Date var2, long var3, Serializable var5, WLTimerInfo var6) {
      boolean var15;
      boolean var10000 = var15 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var16 = null;
      DiagnosticActionState[] var17 = null;
      Object var14 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         JoinPoint var21 = _WLDF$INST_JPFLD_1;
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High;
         DiagnosticAction[] var10002 = var16 = var10001.getActions();
         InstrumentationSupport.preProcess(var21, var10001, var10002, var17 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var19 = false;

      TimerImpl var22;
      try {
         var19 = true;
         if (debugLogger.isDebugEnabled()) {
            debug("Creating timer with pk: " + var1 + " expiration: " + var2 + " duration: " + var3 + " info: " + var5);
         }

         Long var7 = new Long(this.getNextTimerID());
         TimerImpl var8 = new TimerImpl(this, this.beanManager, var1, var5, this.isTransactional, var2, var3, var7, var6);
         this.addNewTimer(var8);
         var22 = var8;
         var19 = false;
      } finally {
         if (var19) {
            var10001 = null;
            if (var15) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var16, var17);
            }

         }
      }

      if (var15) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var16, var17);
      }

      return var22;
   }

   public javax.ejb.Timer createTimer(Object var1, Date var2, Serializable var3, WLTimerInfo var4) {
      boolean var13;
      boolean var10000 = var13 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var14 = null;
      DiagnosticActionState[] var15 = null;
      Object var12 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         JoinPoint var19 = _WLDF$INST_JPFLD_2;
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High;
         DiagnosticAction[] var10002 = var14 = var10001.getActions();
         InstrumentationSupport.preProcess(var19, var10001, var10002, var15 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var17 = false;

      TimerImpl var20;
      try {
         var17 = true;
         if (debugLogger.isDebugEnabled()) {
            debug("Creating timer with pk: " + var1 + " expiration: " + var2 + " info: " + var3);
         }

         Long var5 = new Long(this.getNextTimerID());
         TimerImpl var6 = new TimerImpl(this, this.beanManager, var1, var3, this.isTransactional, var2, -1L, var5, var4);
         this.addNewTimer(var6);
         var20 = var6;
         var17 = false;
      } finally {
         if (var17) {
            var10001 = null;
            if (var13) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var14, var15);
            }

         }
      }

      if (var13) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var14, var15);
      }

      return var20;
   }

   public javax.ejb.Timer createTimer(Object var1, long var2, long var4, Serializable var6, WLTimerInfo var7) {
      boolean var15;
      boolean var10000 = var15 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var16 = null;
      DiagnosticActionState[] var17 = null;
      Object var14 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         JoinPoint var21 = _WLDF$INST_JPFLD_3;
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High;
         DiagnosticAction[] var10002 = var16 = var10001.getActions();
         InstrumentationSupport.preProcess(var21, var10001, var10002, var17 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var19 = false;

      javax.ejb.Timer var22;
      try {
         var19 = true;
         Date var8 = new Date(System.currentTimeMillis() + var2);
         var22 = this.createTimer(var1, var8, var4, var6, var7);
         var19 = false;
      } finally {
         if (var19) {
            var10001 = null;
            if (var15) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var16, var17);
            }

         }
      }

      if (var15) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var16, var17);
      }

      return var22;
   }

   public javax.ejb.Timer createTimer(Object var1, long var2, Serializable var4, WLTimerInfo var5) {
      boolean var13;
      boolean var10000 = var13 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var14 = null;
      DiagnosticActionState[] var15 = null;
      Object var12 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         JoinPoint var19 = _WLDF$INST_JPFLD_4;
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High;
         DiagnosticAction[] var10002 = var14 = var10001.getActions();
         InstrumentationSupport.preProcess(var19, var10001, var10002, var15 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var17 = false;

      javax.ejb.Timer var20;
      try {
         var17 = true;
         Date var6 = new Date(System.currentTimeMillis() + var2);
         var20 = this.createTimer(var1, var6, var4, var5);
         var17 = false;
      } finally {
         if (var17) {
            var10001 = null;
            if (var13) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_4, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var14, var15);
            }

         }
      }

      if (var13) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_4, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var14, var15);
      }

      return var20;
   }

   public Collection getTimers(Object var1) {
      HashSet var2 = null;
      synchronized(this.pk2EJBTimerMap) {
         var2 = (HashSet)this.pk2EJBTimerMap.get(var1);
      }

      if (var2 == null) {
         var2 = new HashSet();
      } else {
         var2 = (HashSet)var2.clone();
      }

      return var2;
   }

   public void removeTimersForPK(Object var1) {
      Collection var2 = this.getTimers(var1);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         TimerImpl var4 = (TimerImpl)var3.next();
         var4.remove();
      }

   }

   public static void removeAllTimers(BeanInfo var0) {
      if (debugLogger.isDebugEnabled()) {
         debug("Removing all EJB Timers from store for EJB: " + var0.getDisplayName());
      }

      try {
         PersistentStoreManager var1 = PersistentStoreManager.getManager();
         String var2 = var0.getTimerStoreName();
         PersistentStoreXA var3 = null;
         if (var2 == null) {
            var3 = (PersistentStoreXA)var1.getDefaultStore();
         } else {
            if (!var1.storeExistsByLogicalName(var2)) {
               return;
            }

            var3 = (PersistentStoreXA)var1.getStoreByLogicalName(var2);
         }

         String var4 = getStoreConnectionKey(var0);
         PersistentStoreConnection var5 = var3.createConnection(var4);
         var5.delete();
      } catch (Exception var6) {
         if (debugLogger.isDebugEnabled()) {
            debug("Error removing EJB Timer store objects", var6);
         }

         EJBLogger.logErrorRemovingEJBTimersFromStore(var0.getDisplayName(), var6);
      }

   }

   public void undeploy() {
      if (debugLogger.isDebugEnabled()) {
         debug("shutting down EJB timer service");
      }

      this.isInitialized = false;

      try {
         this.timerManager.waitForStop(0L);
      } catch (InterruptedException var2) {
      }

      if (this.persistentConnection != null) {
         this.persistentConnection.close();
      }

      this.uninitializeTimerHelper();
   }

   private void addNewTimer(TimerImpl var1) {
      Long var2 = var1.getID();

      try {
         GXATransaction var3 = this.gxa.enlist();
         if (var3 != null) {
            TimerCreationOperation var6 = new TimerCreationOperation(var1, this, this.persistentConnection);
            this.gxa.addNewOperation(var3, var6);
            var1.setState(4);
         } else {
            PersistentStoreTransaction var7 = this.persistentStore.begin();
            var1.setPersistentHandle(this.persistentConnection.create(var7, var1.getTimerData(), 0));
            var7.commit();
            var1.finalizeCreate();
         }
      } catch (Exception var5) {
         EJBException var4 = new EJBException("Error creating Timer.", var5);
         var4.initCause(var5);
         throw var4;
      }

      this.addTimerToMaps(var1);
   }

   public javax.ejb.Timer getTimer(Long var1) {
      synchronized(this.id2EJBTimerMap) {
         return (javax.ejb.Timer)this.id2EJBTimerMap.get(var1);
      }
   }

   public MethodDescriptor getMethodDescriptor() {
      return this.md;
   }

   public void removePersistentStoreEntry(TimerImpl var1) throws PersistentStoreException {
      PersistentStoreTransaction var2 = this.persistentStore.begin();
      this.persistentConnection.delete(var2, var1.getPersistentHandle(), 0);
      var2.commit();
   }

   public void updatePersistentStoreEntry(TimerImpl var1) throws PersistentStoreException {
      PersistentStoreTransaction var2 = this.persistentStore.begin();
      this.persistentConnection.update(var2, var1.getPersistentHandle(), var1.getTimerData(), 0);
      var2.commit();
   }

   public boolean registerTimerExpirationOperation(TimerImpl var1) throws GXAException {
      GXATransaction var2 = this.gxa.enlist();
      if (var2 != null) {
         TimerExpirationOperation var3 = new TimerExpirationOperation(var1, this, this.persistentConnection, this.moduleClassLoader);
         this.gxa.addNewOperation(var2, var3);
         if (debugLogger.isDebugEnabled()) {
            debug("Created TimerExpirationOperation: " + var3);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean registerTimerCancellationOperation(TimerImpl var1) throws GXAException {
      GXATransaction var2 = this.gxa.enlist();
      if (var2 != null) {
         TimerCancellationOperation var3 = new TimerCancellationOperation(var1, this, this.persistentConnection);
         this.gxa.addNewOperation(var2, var3);
         return true;
      } else {
         return false;
      }
   }

   private void initializePersistedTimers() throws WLDeploymentException {
      try {
         long var1 = 0L;
         HashSet var3 = null;
         PersistentStoreConnection.Cursor var4 = this.persistentConnection.createCursor(0);
         boolean var6 = false;

         PersistentStoreRecord var5;
         while((var5 = var4.next()) != null) {
            Object var7 = null;

            try {
               var7 = var5.getData();
            } catch (PersistentStoreException var12) {
               if (var6) {
                  continue;
               }

               BeanInfo var9 = this.beanManager.getBeanInfo();
               Object var10 = var12.getCause();
               if (var10 == null) {
                  var10 = var12;
               }

               EJBLogger.logEJBTimerSerializationError(var9.getDisplayName(), ((Throwable)var10).toString());
               var6 = true;
               continue;
            }

            if (var7 instanceof TimerData) {
               TimerData var8 = (TimerData)var7;
               TimerImpl var15 = new TimerImpl(this, this.beanManager, this.isTransactional, var8);
               var15.setPersistentHandle(var5.getHandle());
               var15.setState(1);
               this.addTimerToMaps(var15);
               Long var16 = var15.getID();
               if (this.disabledTimers.containsKey(var16)) {
                  ArrayList var11 = new ArrayList();
                  var11.add(this.disabledTimers.get(var16));
                  var11.add(var15);
                  this.disabledTimers.put(var16, var11);
               } else {
                  this.disabledTimers.put(var16, var15);
               }

               if (var15.getID() > var1) {
                  var1 = var15.getID();
               }
            } else {
               if (!(var7 instanceof PrepareRecord)) {
                  throw new AssertionError("Unexpected object in EJB timer store!");
               }

               PrepareRecord var14 = (PrepareRecord)var7;
               var14.handle = var5.getHandle();
               if (var3 == null) {
                  var3 = new HashSet();
               }

               var3.add(var14);
            }
         }

         this.timerIDCounter = ++var1;
         if (var3 != null) {
            this.handleRecoveredOperations(var3);
         }

      } catch (Exception var13) {
         if (debugLogger.isDebugEnabled()) {
            debug("Error initializing TimerService", var13);
         }

         throw new WLDeploymentException("Error starting Timer service", var13);
      }
   }

   private void handleRecoveredOperations(Set var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         PrepareRecord var3 = (PrepareRecord)var2.next();
         Long var4 = var3.timerID;
         if (!$assertionsDisabled && !this.disabledTimers.containsKey(var4)) {
            throw new AssertionError();
         }

         switch (var3.operation) {
            case 1:
               TimerImpl var7 = (TimerImpl)this.disabledTimers.remove(var4);
               if (var7.getState() != 3) {
                  var7.setState(4);
               }

               TimerCreationOperation var8 = new TimerCreationOperation(var7, this, this.persistentConnection);
               var8.recover(var3.xid, var3.handle);
               this.gxa.addRecoveredOperation(var8);
               break;
            case 2:
               TimerImpl var5 = (TimerImpl)this.disabledTimers.remove(var4);
               var5.setState(3);
               TimerCancellationOperation var6 = new TimerCancellationOperation(var5, this, this.persistentConnection);
               var6.recover(var3.xid, var3.handle);
               this.gxa.addRecoveredOperation(var6);
               break;
            case 3:
            case 4:
               TimerExpirationOperation var9 = null;
               Object var10 = this.disabledTimers.remove(var4);
               if (var10 instanceof List) {
                  List var11 = (List)var10;
                  TimerImpl var12 = (TimerImpl)var11.get(0);
                  TimerImpl var13 = (TimerImpl)var11.get(1);
                  if (!$assertionsDisabled && !var12.isIntervalTimer()) {
                     throw new AssertionError();
                  }

                  if (var12.getNextExpiration().getTime() > var13.getNextExpiration().getTime()) {
                     this.removeTimerFromMaps(var13);
                     var12.setState(5);
                     var9 = new TimerExpirationOperation(var12, this, this.persistentConnection, this.moduleClassLoader);
                     var9.recover(var3.xid, var3.handle, var13.getPersistentHandle(), var12.getPersistentHandle());
                  } else {
                     this.removeTimerFromMaps(var12);
                     var13.setState(5);
                     var9 = new TimerExpirationOperation(var13, this, this.persistentConnection, this.moduleClassLoader);
                     var9.recover(var3.xid, var3.handle, var12.getPersistentHandle(), var13.getPersistentHandle());
                  }
               } else {
                  TimerImpl var14 = (TimerImpl)var10;
                  if (!$assertionsDisabled && var14.isIntervalTimer()) {
                     throw new AssertionError();
                  }

                  if (var3.operation == 3) {
                     var14.setState(5);
                  } else {
                     var14.setState(6);
                  }

                  var9 = new TimerExpirationOperation(var14, this, this.persistentConnection, this.moduleClassLoader);
                  var9.recover(var3.xid, var3.handle, var14.getPersistentHandle(), (PersistentHandle)null);
               }

               this.gxa.addRecoveredOperation(var9);
               break;
            default:
               throw new AssertionError("Unknown type: " + var3.operation);
         }
      }

   }

   public void perhapsStart() {
      if (debugLogger.isDebugEnabled()) {
         debug("invoking perhapsStart");
      }

      EJBTimerStarter.addTimerManagerStarter(this);
   }

   public void start() {
      if (debugLogger.isDebugEnabled()) {
         debug("invoking start");
      }

      this.isInitialized = true;
      this.enableDisabledTimers();
   }

   private void initializePersistentStore(BeanInfo var1) throws WLDeploymentException {
      try {
         PersistentStoreManager var2 = PersistentStoreManager.getManager();
         String var3 = var1.getTimerStoreName();
         if (var3 == null) {
            this.persistentStore = (PersistentStoreXA)var2.getDefaultStore();
         } else {
            this.persistentStore = (PersistentStoreXA)var2.getStoreByLogicalName(var3);
         }

         String var4 = getStoreConnectionKey(var1);
         this.persistentConnection = this.persistentStore.createConnection(var4);
         this.gxa = this.persistentStore.getGXAResource();
      } catch (Exception var5) {
         if (debugLogger.isDebugEnabled()) {
            debug("Error initializing Timer store", var5);
         }

         throw new WLDeploymentException("Error initializing Timer store", var5);
      }
   }

   private static String getStoreConnectionKey(BeanInfo var0) {
      DeploymentInfo var1 = var0.getDeploymentInfo();
      return "weblogic.ejb.timer." + var1.getApplicationName() + "." + var1.getModuleURI() + "." + var0.getEJBName();
   }

   private void initializeMethodDescriptor(BeanInfo var1) throws WLDeploymentException {
      this.md = var1.getEjbTimeoutMethodDescriptor();
      int var2 = this.md.getTXAttribute();
      this.isTransactional = var2 == 3 || var2 == 1;
   }

   private void initializeTimerHelper(BeanInfo var1) throws WLDeploymentException {
      DeploymentInfo var2 = var1.getDeploymentInfo();
      String var3 = var2.getModuleURI();
      String var4 = var1.getEJBName();
      Context var5 = this.beanManager.getEnvironmentContext();
      TimerHelperImpl var6 = new TimerHelperImpl(this);

      try {
         var5.bind("app/ejb/" + var3 + "#" + var4 + "/timerHelper", var6);
      } catch (NamingException var8) {
         throw new WLDeploymentException("Error initializing TimerHelper:", var8);
      }
   }

   private void uninitializeTimerHelper() {
      BeanInfo var1 = this.beanManager.getBeanInfo();
      DeploymentInfo var2 = var1.getDeploymentInfo();
      String var3 = var2.getModuleURI();
      String var4 = var1.getEJBName();
      Context var5 = this.beanManager.getEnvironmentContext();

      try {
         var5.unbind("app/ejb/" + var3 + "#" + var4 + "/timerHelper");
      } catch (NamingException var7) {
         if (debugLogger.isDebugEnabled()) {
            debug("Error unbinding TimerHelper", var7);
         }
      }

   }

   public void cancelTimer(TimerImpl var1) {
      Timer var2 = var1.getTimer();
      if (var2 != null) {
         var2.cancel();
      }

   }

   public void addTimerToMaps(TimerImpl var1) {
      Object var2 = var1.getPK();
      Long var3 = var1.getID();
      synchronized(this.id2EJBTimerMap) {
         this.id2EJBTimerMap.put(var3, var1);
      }

      synchronized(this.pk2EJBTimerMap) {
         Object var5 = (Set)this.pk2EJBTimerMap.get(var2);
         if (var5 == null) {
            var5 = new HashSet();
            this.pk2EJBTimerMap.put(var2, var5);
         }

         ((Set)var5).add(var1);
      }

      this.timerRtMBean.incrementActiveTimerCount();
   }

   public void removeTimerFromMaps(TimerImpl var1) {
      Object var2 = var1.getPK();
      Long var3 = var1.getID();
      synchronized(this.id2EJBTimerMap) {
         this.id2EJBTimerMap.remove(var3);
      }

      synchronized(this.pk2EJBTimerMap) {
         Set var5 = (Set)this.pk2EJBTimerMap.get(var2);
         if (var5 == null) {
            return;
         }

         var5.remove(var1);
         if (var5.isEmpty()) {
            this.pk2EJBTimerMap.remove(var2);
         }
      }

      this.timerRtMBean.decrementActiveTimerCount();
   }

   private long getNextTimerID() {
      synchronized(this.IDLock) {
         return ++this.timerIDCounter;
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[EJBTimerManager] " + var0);
   }

   private static void debug(String var0, Throwable var1) {
      debugLogger.debug("[EJBTimerManager] " + var0, var1);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Timer_Manager_Around_High");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EJBTimerManager.java", "weblogic.ejb.container.timer.EJBTimerManager", "scheduleTimer", "(Lweblogic/ejb/container/timer/TimerImpl;)V", 175, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EJBTimerManager.java", "weblogic.ejb.container.timer.EJBTimerManager", "createTimer", "(Ljava/lang/Object;Ljava/util/Date;JLjava/io/Serializable;Lweblogic/ejb/WLTimerInfo;)Ljavax/ejb/Timer;", 228, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EJBTimerManager.java", "weblogic.ejb.container.timer.EJBTimerManager", "createTimer", "(Ljava/lang/Object;Ljava/util/Date;Ljava/io/Serializable;Lweblogic/ejb/WLTimerInfo;)Ljavax/ejb/Timer;", 247, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_3 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EJBTimerManager.java", "weblogic.ejb.container.timer.EJBTimerManager", "createTimer", "(Ljava/lang/Object;JJLjava/io/Serializable;Lweblogic/ejb/WLTimerInfo;)Ljavax/ejb/Timer;", 263, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_4 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EJBTimerManager.java", "weblogic.ejb.container.timer.EJBTimerManager", "createTimer", "(Ljava/lang/Object;JLjava/io/Serializable;Lweblogic/ejb/WLTimerInfo;)Ljavax/ejb/Timer;", 272, (Map)null, (boolean)0);
      $assertionsDisabled = !EJBTimerManager.class.desiredAssertionStatus();
      debugLogger = EJBDebugService.timerLogger;
   }

   public static class TimerExpirationOperation implements GXAOperation {
      private TimerImpl timer;
      private EJBTimerManager timerManager;
      private PersistentStoreConnection pConn;
      private GXATransaction gxaTransaction;
      private GXid xid;
      private PersistentHandle prepareHandle;
      private PersistentHandle newTimerHandle;
      private PersistentHandle oldTimerHandle;
      private TimerData oldTimerState;
      private ClassLoader moduleClassLoader;

      public TimerExpirationOperation(TimerImpl var1, EJBTimerManager var2, PersistentStoreConnection var3, ClassLoader var4) {
         this.timer = var1;
         this.timerManager = var2;
         this.pConn = var3;
         this.oldTimerHandle = var1.getPersistentHandle();
         this.moduleClassLoader = var4;
      }

      public void recover(GXid var1, PersistentHandle var2, PersistentHandle var3, PersistentHandle var4) {
         this.xid = var1;
         this.prepareHandle = var2;
         this.oldTimerHandle = var3;
         this.newTimerHandle = var4;
      }

      public void onInitialize(GXATraceLogger var1, GXATransaction var2, GXAOperationWrapper var3) {
         this.gxaTransaction = var2;
         this.xid = var2.getGXid();
         this.timer.setXid(this.xid.getXAXid());
      }

      public boolean onPrepare(int var1, boolean var2) {
         PersistentStoreTransaction var3;
         if (this.timer.isIntervalTimer() && !this.timer.isCancelled()) {
            if (var1 == 1) {
               var3 = this.gxaTransaction.getStoreTransaction();
               this.newTimerHandle = this.pConn.create(var3, this.timer.getTimerData(), 0);
            }

            if (var1 == 3) {
               this.timer.setPersistentHandle(this.newTimerHandle);
            }
         }

         if (var1 == 2 && !var2) {
            var3 = null;
            PrepareRecord var5;
            if (this.timer.isCancelled() && this.timer.isIntervalTimer()) {
               var5 = new PrepareRecord(this.timer.getID(), 4, this.xid);
            } else {
               var5 = new PrepareRecord(this.timer.getID(), 3, this.xid);
            }

            PersistentStoreTransaction var4 = this.gxaTransaction.getStoreTransaction();
            this.prepareHandle = this.pConn.create(var4, var5, 0);
         }

         return true;
      }

      public void onCommit(int var1) {
         PersistentStoreTransaction var2;
         if (var1 == 1) {
            var2 = this.gxaTransaction.getStoreTransaction();
            this.pConn.delete(var2, this.oldTimerHandle, 0);
         } else if (var1 == 2 && this.prepareHandle != null) {
            var2 = this.gxaTransaction.getStoreTransaction();
            this.pConn.delete(var2, this.prepareHandle, 0);
         } else if (var1 == 3) {
            this.timerManager.getTimerRuntimeMBean().incrementTimeoutCount();
            if (this.timer.isIntervalTimer() && !this.timer.isCancelled()) {
               this.timer.handleTimeoutSuccess();
            } else {
               if (this.timer.isCancelled()) {
                  this.timerManager.getTimerRuntimeMBean().incrementCancelledTimerCount();
               }

               this.timer.setState(7);
               this.timerManager.removeTimerFromMaps(this.timer);
            }
         }

      }

      public void onRollback(int var1) {
         PersistentStoreTransaction var2;
         if (var1 == 1) {
            var2 = this.gxaTransaction.getStoreTransaction();
            if (this.newTimerHandle != null) {
               this.pConn.delete(var2, this.newTimerHandle, 0);
            }

            if (this.timer.exists()) {
               CompletionRequest var3 = new CompletionRequest();
               this.pConn.read(var2, this.oldTimerHandle, var3);
               Thread var4 = Thread.currentThread();
               ClassLoader var5 = var4.getContextClassLoader();
               var4.setContextClassLoader(this.moduleClassLoader);

               try {
                  PersistentStoreRecord var6 = (PersistentStoreRecord)var3.getResult();
                  this.oldTimerState = (TimerData)var6.getData();
               } catch (Throwable var11) {
                  throw new AssertionError("Error reading from persistent store: " + var11);
               } finally {
                  if (var5 != null) {
                     var4.setContextClassLoader(var5);
                  }

               }
            } else {
               this.pConn.delete(var2, this.oldTimerHandle, 0);
            }
         } else if (var1 == 2 && this.prepareHandle != null) {
            var2 = this.gxaTransaction.getStoreTransaction();
            this.pConn.delete(var2, this.prepareHandle, 0);
         } else if (var1 == 3) {
            this.timer.setPersistentHandle(this.oldTimerHandle);
            this.timer.handleTimeoutFailure(this.oldTimerState);
         }

      }

      public GXid getGXid() {
         return this.xid;
      }

      public String getDebugPrefix() {
         return "TimerExpiration";
      }
   }

   public static class TimerCancellationOperation implements GXAOperation {
      private TimerImpl timer;
      private EJBTimerManager timerManager;
      private PersistentStoreConnection pConn;
      private GXATransaction gxaTransaction;
      private GXid xid;
      private PersistentHandle prepareHandle;

      public TimerCancellationOperation(TimerImpl var1, EJBTimerManager var2, PersistentStoreConnection var3) {
         this.timer = var1;
         this.timerManager = var2;
         this.pConn = var3;
      }

      public void recover(GXid var1, PersistentHandle var2) {
         this.xid = var1;
         this.prepareHandle = var2;
      }

      public void onInitialize(GXATraceLogger var1, GXATransaction var2, GXAOperationWrapper var3) {
         this.gxaTransaction = var2;
         this.xid = var2.getGXid();
         this.timer.setXid(this.xid.getXAXid());
      }

      public boolean onPrepare(int var1, boolean var2) {
         if (var1 == 2 && !var2) {
            PrepareRecord var3 = new PrepareRecord(this.timer.getID(), 2, this.xid);
            PersistentStoreTransaction var4 = this.gxaTransaction.getStoreTransaction();
            this.prepareHandle = this.pConn.create(var4, var3, 0);
         }

         return true;
      }

      public void onCommit(int var1) {
         PersistentStoreTransaction var2;
         if (var1 == 1) {
            var2 = this.gxaTransaction.getStoreTransaction();
            this.pConn.delete(var2, this.timer.getPersistentHandle(), 0);
         } else if (var1 == 2 && this.prepareHandle != null) {
            var2 = this.gxaTransaction.getStoreTransaction();
            this.pConn.delete(var2, this.prepareHandle, 0);
         } else if (var1 == 3) {
            this.timerManager.getTimerRuntimeMBean().incrementCancelledTimerCount();
            this.timer.finalizeCancel();
         }

      }

      public void onRollback(int var1) {
         if (var1 == 2 && this.prepareHandle != null) {
            PersistentStoreTransaction var2 = this.gxaTransaction.getStoreTransaction();
            this.pConn.delete(var2, this.prepareHandle, 0);
         } else if (var1 == 3) {
            this.timer.undoCancel();
         }

      }

      public GXid getGXid() {
         return this.xid;
      }

      public String getDebugPrefix() {
         return "TimerCancellation";
      }
   }

   public static class TimerCreationOperation implements GXAOperation {
      private TimerImpl timer;
      private EJBTimerManager timerManager;
      private PersistentStoreConnection pConn;
      private GXATransaction gxaTransaction;
      private GXid xid;
      private PersistentHandle prepareHandle;

      public TimerCreationOperation(TimerImpl var1, EJBTimerManager var2, PersistentStoreConnection var3) {
         this.timer = var1;
         this.timerManager = var2;
         this.pConn = var3;
      }

      public void recover(GXid var1, PersistentHandle var2) {
         this.xid = var1;
         this.prepareHandle = var2;
      }

      public void onInitialize(GXATraceLogger var1, GXATransaction var2, GXAOperationWrapper var3) {
         this.gxaTransaction = var2;
         this.xid = var2.getGXid();
         this.timer.setXid(this.xid.getXAXid());
      }

      public boolean onPrepare(int var1, boolean var2) {
         if (var1 == 1) {
            PersistentStoreTransaction var3 = this.gxaTransaction.getStoreTransaction();
            PersistentHandle var4 = this.pConn.create(var3, this.timer.getTimerData(), 0);
            this.timer.setPersistentHandle(var4);
         } else if (var1 == 2 && !var2) {
            PrepareRecord var5 = new PrepareRecord(this.timer.getID(), 1, this.xid);
            PersistentStoreTransaction var6 = this.gxaTransaction.getStoreTransaction();
            this.prepareHandle = this.pConn.create(var6, var5, 0);
         }

         return true;
      }

      public void onCommit(int var1) {
         if (var1 == 2 && this.prepareHandle != null) {
            PersistentStoreTransaction var2 = this.gxaTransaction.getStoreTransaction();
            this.pConn.delete(var2, this.prepareHandle, 0);
         } else if (var1 == 3) {
            this.timer.finalizeCreate();
         }

      }

      public void onRollback(int var1) {
         PersistentStoreTransaction var2;
         if (var1 == 1 && this.timer.getPersistentHandle() != null) {
            var2 = this.gxaTransaction.getStoreTransaction();
            this.pConn.delete(var2, this.timer.getPersistentHandle(), 0);
         } else if (var1 == 2 && this.prepareHandle != null) {
            var2 = this.gxaTransaction.getStoreTransaction();
            this.pConn.delete(var2, this.prepareHandle, 0);
         } else if (var1 == 3) {
            this.timer.undoCreate();
         }

      }

      public GXid getGXid() {
         return this.xid;
      }

      public String getDebugPrefix() {
         return "TimerCreation";
      }
   }

   public static class TimerHelperImpl implements TimerHelper {
      private EJBTimerManager timerManager;

      public TimerHelperImpl(EJBTimerManager var1) {
         this.timerManager = var1;
      }

      public javax.ejb.Timer getTimer(Long var1) {
         return this.timerManager.getTimer(var1);
      }
   }
}
