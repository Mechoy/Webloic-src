package weblogic.transaction.internal;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.transaction.xa.Xid;
import weblogic.health.HealthFeedback;
import weblogic.health.HealthMonitorService;
import weblogic.health.HealthState;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TransactionLogJDBCStoreMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.JTARecoveryRuntimeMBean;
import weblogic.management.runtime.JTARuntimeMBean;
import weblogic.management.runtime.JTATransaction;
import weblogic.management.runtime.NonXAResourceRuntimeMBean;
import weblogic.management.runtime.PersistentStoreRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.TransactionNameRuntimeMBean;
import weblogic.management.runtime.TransactionResourceRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.admin.PersistentStoreRuntimeMBeanImpl;
import weblogic.transaction.Transaction;

public final class JTARuntimeImpl extends JTATransactionStatisticsImpl implements Constants, JTARuntimeMBean, JTAHealthListener, JTARuntime {
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final long serialVersionUID = -3773752601197689481L;
   public static final String OVERFLOW_NAME = "weblogic.transaction.statistics.namedOverflow";
   private long namedInstanceCounter;
   private HashMap namedStats = new HashMap();
   private TransactionNameRuntimeImpl namedOverflow = null;
   private ServerTransactionManagerImpl tm;
   private ArrayList registeredResources = new ArrayList(5);
   private ArrayList registeredNonXAResources = new ArrayList(5);
   private Health health = new Health();
   private HealthFeedback jdbcStoreHealthFeedback;
   private Health jdbcStoreHealth = new Health();
   private boolean isJDBCStoreHealthRegistered;
   private boolean isAutomaticMigrationMode = true;
   private static final String HEALTH_JTA_SUBSYSTEM_NAME = "JTA";
   private static final String HEALTH_JTA_JDBCSTORE_SUBSYSTEM_NAME = "JTA_JDBCSTORE";
   private volatile Set txNameRuntimes = new HashSet();
   private Set txResourceRuntimes = new HashSet();
   private Set nonXAResourceRuntimes = new HashSet();
   private PersistentStoreRuntimeMBeanImpl persistentStoreRuntimeMBean;

   public JTARuntimeImpl(String var1, ServerTransactionManagerImpl var2) throws ManagementException {
      super(var1, ManagementService.getRuntimeAccess(kernelID).getServerRuntime());
      ManagementService.getRuntimeAccess(kernelID).getServerRuntime().setJTARuntime(this);
      this.tm = var2;
   }

   JTARuntimeImpl(String var1, ServerTransactionManagerImpl var2, RuntimeMBean var3) throws ManagementException {
      super(var1, var3);
      this.tm = var2;
   }

   public void tallyCompletion(Transaction var1) {
      super.tallyCompletion(var1);
      String var2 = var1.getName();
      TransactionNameRuntimeImpl var3 = null;
      if (var2 != null) {
         synchronized(this.namedStats) {
            var3 = (TransactionNameRuntimeImpl)this.namedStats.get(var2);
            if (var3 == null) {
               if (this.namedStats.size() >= this.tm.getMaxUniqueNameStatistics()) {
                  if (this.namedOverflow == null) {
                     try {
                        this.namedOverflow = (TransactionNameRuntimeImpl)SecurityServiceManager.runAs(kernelID, kernelID, new CreateNamedMBeanAction("weblogic.transaction.statistics.namedOverflow", (long)(this.namedInstanceCounter++), this));
                     } catch (Exception var8) {
                     }
                  }

                  var3 = this.namedOverflow;
               } else {
                  try {
                     var3 = (TransactionNameRuntimeImpl)SecurityServiceManager.runAs(kernelID, kernelID, new CreateNamedMBeanAction(var2, (long)(this.namedInstanceCounter++), this));
                     this.namedStats.put(var2, var3);
                  } catch (Exception var7) {
                  }
               }
            }
         }

         if (var3 != null) {
            var3.tallyCompletion(var1);
         }
      }

   }

   public TransactionNameRuntimeMBean[] getTransactionNameRuntimeMBeans() throws RemoteException {
      return (TransactionNameRuntimeMBean[])((TransactionNameRuntimeMBean[])this.txNameRuntimes.toArray(new TransactionNameRuntimeMBean[this.txNameRuntimes.size()]));
   }

   public boolean addTransactionNameRuntimeMBean(TransactionNameRuntimeMBean var1) {
      return this.txNameRuntimes.add(var1);
   }

   public boolean removeTransactionNameRuntimeMBean(TransactionNameRuntimeMBean var1) {
      return this.txNameRuntimes.remove(var1);
   }

   public TransactionResourceRuntimeMBean[] getTransactionResourceRuntimeMBeans() throws RemoteException {
      int var1 = this.txResourceRuntimes.size();
      return (TransactionResourceRuntimeMBean[])((TransactionResourceRuntimeMBean[])this.txResourceRuntimes.toArray(new TransactionResourceRuntimeMBean[var1]));
   }

   public boolean addTransactionResourceRuntimeMBean(TransactionResourceRuntimeMBean var1) {
      return this.txResourceRuntimes.add(var1);
   }

   public boolean removeTransactionResourceRuntimeMBean(TransactionResourceRuntimeMBean var1) {
      return this.txResourceRuntimes.remove(var1);
   }

   public NonXAResourceRuntimeMBean[] getNonXAResourceRuntimeMBeans() throws RemoteException {
      int var1 = this.nonXAResourceRuntimes.size();
      return (NonXAResourceRuntimeMBean[])((NonXAResourceRuntimeMBean[])this.nonXAResourceRuntimes.toArray(new NonXAResourceRuntimeMBean[var1]));
   }

   public boolean addNonXAResourceRuntimeMBean(NonXAResourceRuntimeMBean var1) {
      return this.nonXAResourceRuntimes.add(var1);
   }

   public boolean removeNonXAResourceRuntimeMBean(NonXAResourceRuntimeMBean var1) {
      return this.nonXAResourceRuntimes.remove(var1);
   }

   public JTATransaction[] getJTATransactions() {
      return this.getTransactionsOlderThan(new Integer(0));
   }

   public JTATransaction[] getTransactionsOlderThan(Integer var1) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = this.tm.getTransactions();
      if (var4 == null) {
         return new JTATransaction[0];
      } else {
         while(var4.hasNext()) {
            ServerTransactionImpl var5 = (ServerTransactionImpl)var4.next();
            if (var5.getMillisSinceBegin() >= (long)(var1 * 1000)) {
               var3.add(new JTATransactionImpl(var5));
            }
         }

         JTATransaction[] var2;
         if (var3.size() > 0) {
            var3.trimToSize();
            var2 = new JTATransaction[var3.size()];

            for(int var6 = 0; var6 < var3.size(); ++var6) {
               var2[var6] = (JTATransaction)var3.get(var6);
            }
         } else {
            var2 = new JTATransaction[0];
         }

         return var2;
      }
   }

   public String[] getRegisteredResourceNames() {
      return (String[])((String[])this.registeredResources.toArray(new String[this.registeredResources.size()]));
   }

   public String[] getRegisteredNonXAResourceNames() {
      return (String[])((String[])this.registeredNonXAResources.toArray(new String[this.registeredNonXAResources.size()]));
   }

   public int getActiveTransactionsTotalCount() {
      return this.tm == null ? 0 : this.tm.getNumTransactions();
   }

   public JTARecoveryRuntimeMBean[] getRecoveryRuntimeMBeans() {
      return TransactionRecoveryService.getAllRuntimeMBeans();
   }

   public JTARecoveryRuntimeMBean getRecoveryRuntimeMBean(String var1) {
      return (JTARecoveryRuntimeMBean)TransactionRecoveryService.getRuntimeMBean(var1);
   }

   public HealthState getHealthState() {
      return this.health.getState();
   }

   public TransactionResourceRuntime registerResource(String var1) throws Exception {
      return (TransactionResourceRuntime)SecurityServiceManager.runAs(kernelID, kernelID, new CreateResourceMBeanAction(this, var1, this.registeredResources));
   }

   public void unregisterResource(TransactionResourceRuntime var1) throws Exception {
      SecurityServiceManager.runAs(kernelID, kernelID, new UnregisterResourceMBeanAction(this, (TransactionResourceRuntimeMBean)var1, this.registeredResources));
      this.healthEvent(new HealthEvent(7, var1.getResourceName(), "Resource " + var1.getResourceName() + " has been unregistered"));
   }

   public NonXAResourceRuntime registerNonXAResource(String var1) throws Exception {
      return (NonXAResourceRuntime)SecurityServiceManager.runAs(kernelID, kernelID, new CreateNonXAResourceMBeanAction(this, var1, this.registeredNonXAResources));
   }

   public void unregisterNonXAResource(NonXAResourceRuntime var1) throws Exception {
      SecurityServiceManager.runAs(kernelID, kernelID, new UnregisterNonXAResourceMBeanAction(this, (NonXAResourceRuntimeMBean)var1, this.registeredNonXAResources));
   }

   public void healthEvent(HealthEvent var1) {
      this.health.healthEvent(var1);
   }

   public void forceLocalRollback(Xid var1) throws RemoteException {
      TXLogger.logForceLocalRollbackInvoked(var1.toString());
      ServerTransactionImpl var2 = (ServerTransactionImpl)this.tm.getTransaction(var1);
      if (var2 == null) {
         TXLogger.logForceLocalRollbackNoTx(var1.toString());
         throw new RemoteException("forceLocalRollback invoked on unknown transaction '" + var1 + "'");
      } else {
         try {
            var2.forceLocalRollback();
         } catch (Exception var4) {
            TXLogger.logForceLocalRollbackFailed(var1.toString(), var4);
            throw new RemoteException("Unable to perform local rollback", var4);
         }
      }
   }

   public void forceGlobalRollback(Xid var1) throws RemoteException {
      TXLogger.logForceGlobalRollbackInvoked(var1.toString());
      ServerTransactionImpl var2 = (ServerTransactionImpl)this.tm.getTransaction(var1);
      if (var2 == null) {
         TXLogger.logForceGlobalRollbackNoTx(var1.toString());
         throw new RemoteException("forceLocalRollback invoked on unknown transaction '" + var1 + "'");
      } else {
         try {
            var2.forceGlobalRollback();
         } catch (Exception var4) {
            TXLogger.logForceGlobalRollbackFailed(var1.toString(), var4);
            throw new RemoteException("Unable to perform global rollback", var4);
         }
      }
   }

   public void forceLocalCommit(Xid var1) throws RemoteException {
      TXLogger.logForceLocalCommitInvoked(var1.toString());
      ServerTransactionImpl var2 = (ServerTransactionImpl)this.tm.getTransaction(var1);
      if (var2 == null) {
         TXLogger.logForceLocalCommitNoTx(var1.toString());
         throw new RemoteException("forceLocalCommit invoked on unknown transaction '" + var1 + "'");
      } else {
         try {
            var2.forceLocalCommit();
         } catch (Exception var4) {
            TXLogger.logForceLocalCommitFailed(var1.toString(), var4);
            throw new RemoteException("Unable to perform local commit", var4);
         }
      }
   }

   public void forceGlobalCommit(Xid var1) throws RemoteException {
      TXLogger.logForceGlobalCommitInvoked(var1.toString());
      ServerTransactionImpl var2 = (ServerTransactionImpl)this.tm.getTransaction(var1);
      if (var2 == null) {
         TXLogger.logForceGlobalCommitNoTx(var1.toString());
         throw new RemoteException("forceLocalCommit invoked on unknown transaction '" + var1 + "'");
      } else {
         try {
            var2.forceGlobalCommit();
         } catch (Exception var4) {
            TXLogger.logForceGlobalCommitFailed(var1.toString(), var4);
            throw new RemoteException("Unable to perform global commit", var4);
         }
      }
   }

   public JTATransaction getJTATransaction(String var1) throws RemoteException {
      XidImpl var2 = XidImpl.create(var1);
      if (var2 == null) {
         return null;
      } else {
         ServerTransactionImpl var3 = (ServerTransactionImpl)this.tm.getTransaction(var2);
         return var3 == null ? null : new JTATransactionImpl(var3);
      }
   }

   void registerWithHealthService() {
      if (TxDebug.JTAHealth.isDebugEnabled()) {
         TxDebug.JTAHealth.debug("Registering JTARuntimeMBean with Health Monitoring Service");
      }

      HealthMonitorService.register("JTA", this, true);
      if (this.isJDBCTLogEnabled()) {
         TxDebug.JTAHealth.debug("Registering JTA JDBC Store with Health Monitoring Service");
         HealthMonitorService.register("JTA_JDBCSTORE", this.getOrCreateJDBCStoreHealthState(), false);
         this.health.setJDBCStoreHealth(this.jdbcStoreHealth);
         this.isJDBCStoreHealthRegistered = true;
      }

   }

   void unregisterFromHealthService() {
      if (TxDebug.JTAHealth.isDebugEnabled()) {
         TxDebug.JTAHealth.debug("Unregistering JTARuntimeMBean from Health Monitoring Service");
      }

      HealthMonitorService.unregister("JTA");
      if (this.isJDBCStoreHealthRegistered) {
         if (TxDebug.JTAHealth.isDebugEnabled()) {
            TxDebug.JTAHealth.debug("Unregistering JTA JDBC Store from Health Monitoring Service");
         }

         HealthMonitorService.unregister("JTA_JDBCSTORE");
      }

   }

   private HealthFeedback getOrCreateJDBCStoreHealthState() {
      if (this.jdbcStoreHealthFeedback == null) {
         this.jdbcStoreHealth = new Health();
         this.jdbcStoreHealthFeedback = new HealthFeedback() {
            public HealthState getHealthState() {
               return JTARuntimeImpl.this.jdbcStoreHealth.getState();
            }
         };
      }

      return this.jdbcStoreHealthFeedback;
   }

   public PersistentStoreRuntimeMBean getTransactionLogStoreRuntimeMBean() {
      ServerRuntimeMBean var1 = (ServerRuntimeMBean)this.parent;
      String var2 = var1.getName();
      return this.isJDBCTLogEnabled() ? this.getJDBCPersistentStoreRuntimeMBean() : var1.lookupPersistentStoreRuntime("_WLS_" + var2);
   }

   private PersistentStoreRuntimeMBean getJDBCPersistentStoreRuntimeMBean() {
      try {
         if (this.persistentStoreRuntimeMBean == null) {
            this.persistentStoreRuntimeMBean = new PersistentStoreRuntimeMBeanImpl(PlatformHelper.getPlatformHelper().getPrimaryStore());
         }

         return this.persistentStoreRuntimeMBean;
      } catch (ManagementException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   private boolean isJDBCTLogEnabled() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelID);
      if (var1 != null) {
         ServerMBean var2 = ManagementService.getRuntimeAccess(kernelID).getServer();
         if (var2 != null) {
            TransactionLogJDBCStoreMBean var3 = var2.getTransactionLogJDBCStore();
            if (var3 != null) {
               return var3.isEnabled();
            }
         }
      }

      return false;
   }

   private boolean checkAutomaticMigrationMode() {
      this.isAutomaticMigrationMode = ManagementService.getRuntimeAccess(kernelID) == null ? false : TransactionRecoveryService.isAutomaticMigrationMode();
      return this.isAutomaticMigrationMode;
   }

   public boolean isAutomaticMigrationMode() {
      return this.isAutomaticMigrationMode;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(512);
      var1.append("{");
      var1.append(super.toString());
      if (this.namedStats != null) {
         HashMap var2 = (HashMap)this.namedStats.clone();
         Iterator var3 = var2.values().iterator();

         while(var3.hasNext()) {
            TransactionNameRuntimeImpl var4 = (TransactionNameRuntimeImpl)var3.next();
            var1.append("\n");
            var1.append(var4.toString());
         }

         var1.append("\n");
         var1.append(this.namedOverflow);
      }

      var1.append("}");
      return var1.toString();
   }

   public String getHealthStateString() {
      return HealthState.mapToString(this.health.getState().getState());
   }

   public String[] getHealthStateReasonCodes() {
      return this.health.getState().getReasonCode();
   }

   private class UnregisterNonXAResourceMBeanAction implements PrivilegedExceptionAction {
      private JTARuntimeImpl jtaRuntime;
      private NonXAResourceRuntimeMBean mbean;
      private ArrayList registeredNonXAResources;

      UnregisterNonXAResourceMBeanAction(JTARuntimeImpl var2, NonXAResourceRuntimeMBean var3, ArrayList var4) {
         this.jtaRuntime = var2;
         this.mbean = var3;
         this.registeredNonXAResources = var4;
      }

      public Object run() throws Exception {
         String var1 = this.mbean.getNonXAResourceName();
         this.registeredNonXAResources.remove(var1);
         String[] var2 = (String[])((String[])this.registeredNonXAResources.toArray(new String[this.registeredNonXAResources.size()]));
         this.registeredNonXAResources.remove(var1);
         this.jtaRuntime.removeNonXAResourceRuntimeMBean(this.mbean);
         ((NonXAResourceRuntimeImpl)this.mbean).unregister();
         String[] var3 = (String[])((String[])this.registeredNonXAResources.toArray(new String[this.registeredNonXAResources.size()]));
         JTARuntimeImpl.this._postSet("RegisteredNonXAResourceNames", var2, var3);
         return null;
      }
   }

   private class CreateNonXAResourceMBeanAction implements PrivilegedExceptionAction {
      private JTARuntimeImpl jtaRuntime;
      private String name;
      private ArrayList registeredNonXAResources;

      CreateNonXAResourceMBeanAction(JTARuntimeImpl var2, String var3, ArrayList var4) {
         this.jtaRuntime = var2;
         this.name = var3;
         this.registeredNonXAResources = var4;
      }

      public Object run() throws Exception {
         NonXAResourceRuntimeImpl var1 = new NonXAResourceRuntimeImpl(this.name, this.jtaRuntime);
         String[] var2 = (String[])((String[])this.registeredNonXAResources.toArray(new String[this.registeredNonXAResources.size()]));
         this.registeredNonXAResources.add(this.name);
         String[] var3 = (String[])((String[])this.registeredNonXAResources.toArray(new String[this.registeredNonXAResources.size()]));
         JTARuntimeImpl.this._postSet("RegisteredNonXAResourceNames", var2, var3);
         return var1;
      }
   }

   private class UnregisterResourceMBeanAction implements PrivilegedExceptionAction {
      private JTARuntimeImpl jtaRuntime;
      private TransactionResourceRuntimeMBean mbean;
      private ArrayList registeredResources;

      UnregisterResourceMBeanAction(JTARuntimeImpl var2, TransactionResourceRuntimeMBean var3, ArrayList var4) {
         this.jtaRuntime = var2;
         this.mbean = var3;
         this.registeredResources = var4;
      }

      public Object run() throws Exception {
         this.jtaRuntime.removeTransactionResourceRuntimeMBean(this.mbean);
         ((TransactionResourceRuntimeImpl)this.mbean).unregister();
         String var1 = this.mbean.getResourceName();
         String[] var2 = (String[])((String[])this.registeredResources.toArray(new String[this.registeredResources.size()]));
         this.registeredResources.remove(var1);
         String[] var3 = (String[])((String[])this.registeredResources.toArray(new String[this.registeredResources.size()]));
         JTARuntimeImpl.this._postSet("RegisteredResourceNames", var2, var3);
         return null;
      }
   }

   private class CreateResourceMBeanAction implements PrivilegedExceptionAction {
      private JTARuntimeImpl jtaRuntime;
      private String name;
      private ArrayList registeredResources;

      CreateResourceMBeanAction(JTARuntimeImpl var2, String var3, ArrayList var4) {
         this.jtaRuntime = var2;
         this.name = var3;
         this.registeredResources = var4;
      }

      public Object run() throws Exception {
         TransactionResourceRuntimeImpl var1 = new TransactionResourceRuntimeImpl(this.name, this.jtaRuntime);
         String[] var2 = (String[])((String[])this.registeredResources.toArray(new String[this.registeredResources.size()]));
         this.registeredResources.add(this.name);
         String[] var3 = (String[])((String[])this.registeredResources.toArray(new String[this.registeredResources.size()]));
         JTARuntimeImpl.this._postSet("RegisteredResourceNames", var2, var3);
         return var1;
      }
   }

   private static class CreateNamedMBeanAction implements PrivilegedExceptionAction {
      private String name;
      private long instanceCounter;
      private JTARuntimeImpl jtaRuntime;

      CreateNamedMBeanAction(String var1, long var2, JTARuntimeImpl var4) {
         this.name = var1;
         this.instanceCounter = var2;
         this.jtaRuntime = var4;
      }

      public Object run() throws Exception {
         return new TransactionNameRuntimeImpl(this.name, this.instanceCounter, this.jtaRuntime);
      }
   }

   class Health {
      private HealthEvent tlogEvent;
      private HealthEvent txmapEvent;
      private HashMap resourceEvents = new HashMap();
      private HealthState state = new HealthState(0);
      private Health jdbcStoreHealth;

      void setJDBCStoreHealth(Health var1) {
         this.jdbcStoreHealth = var1;
      }

      void healthEvent(HealthEvent var1) {
         this.healthEvent(var1, false);
      }

      void healthEvent(HealthEvent var1, boolean var2) {
         if (var1 != null) {
            synchronized(this) {
               if (!var2 && ("JDBCSTORE_FAILURE".equals(var1.getName()) || "JDBCSTORE_RECOVERED".equals(var1.getName()))) {
                  if (this.jdbcStoreHealth != null) {
                     this.jdbcStoreHealth.healthEvent(var1, true);
                  }

               } else {
                  switch (var1.getType()) {
                     case 1:
                        if (TxDebug.JTAHealth.isDebugEnabled()) {
                           TxDebug.JTAHealth.debug("JTA Health: health event TLOG_FAILURE");
                        }

                        this.tlogEvent = var1;
                        this.updateState();
                        break;
                     case 2:
                        if (TxDebug.JTAHealth.isDebugEnabled()) {
                           TxDebug.JTAHealth.debug("JTA Health: health event TLOG_OK");
                        }

                        if (this.tlogEvent != null) {
                           this.tlogEvent = null;
                           this.updateState();
                        }
                        break;
                     case 3:
                        if (TxDebug.JTAHealth.isDebugEnabled()) {
                           TxDebug.JTAHealth.debug("JTA Health: health event TXMAP_FULL");
                        }

                        if (this.txmapEvent == null) {
                           this.txmapEvent = var1;
                        }

                        this.updateState();
                        break;
                     case 4:
                        if (TxDebug.JTAHealth.isDebugEnabled()) {
                           TxDebug.JTAHealth.debug("JTA Health: health event TXMAP_OK");
                        }

                        if (this.txmapEvent != null) {
                           this.txmapEvent = null;
                           this.updateState();
                        }
                        break;
                     case 5:
                        if (TxDebug.JTAHealth.isDebugEnabled()) {
                           TxDebug.JTAHealth.debug("JTA Health: health event RESOURCE_UNHEALTHY (" + var1.getName() + ")");
                        }

                        this.resourceEvents.put(var1.getName(), var1);
                        this.updateState();
                        break;
                     case 6:
                        if (TxDebug.JTAHealth.isDebugEnabled()) {
                           TxDebug.JTAHealth.debug("JTA Health: health event RESOURCE_HEALTHY (" + var1.getName() + ")");
                        }

                        if (this.resourceEvents.remove(var1.getName()) != null) {
                           this.updateState();
                        }
                        break;
                     case 7:
                        if (TxDebug.JTAHealth.isDebugEnabled()) {
                           TxDebug.JTAHealth.debug("JTA Health: health event RESOURCE_UNREGISTERED (" + var1.getName() + ")");
                        }

                        if (this.resourceEvents.remove(var1.getName()) != null) {
                           this.updateState();
                        }
                        break;
                     default:
                        if (TxDebug.JTAHealth.isDebugEnabled()) {
                           TxDebug.JTAHealth.debug("JTA Health: unknown event type: " + var1.getType());
                        }
                  }

                  if (!var2 && this.state.getState() == 3 && JTARuntimeImpl.this.checkAutomaticMigrationMode()) {
                     TXExceptionLogger.logJTAFailedAndForceShutdown();
                     HealthMonitorService.subsystemFailedForceShutdown("JTA", TXExceptionLogger.logJTAFailedAndForceShutdownLoggable().getMessage());
                  }

               }
            }
         }
      }

      void updateState() {
         byte var1 = 0;
         ArrayList var2 = new ArrayList();
         if (this.tlogEvent != null && this.tlogEvent.getType() == 1) {
            var1 = 3;
            var2.add(this.tlogEvent.getDescription());
         }

         if (this.txmapEvent != null && this.txmapEvent.getType() == 3) {
            long var3 = System.currentTimeMillis() - this.txmapEvent.getTimestamp();
            if (var3 > JTARuntimeImpl.this.tm.getMaxTransactionsHealthIntervalMillis()) {
               if (var1 != 3 && var1 != 4) {
                  var1 = 4;
               }

               var2.add(this.txmapEvent.getDescription() + " (" + "full for " + var3 + " ms which is longer than the " + "failure threshold of " + JTARuntimeImpl.this.tm.getMaxTransactionsHealthIntervalMillis() + " ms)");
            } else {
               if (var1 == 0) {
                  var1 = 1;
               }

               var2.add(this.txmapEvent.getDescription());
            }
         }

         if (this.resourceEvents.size() > 0) {
            if (var1 == 0) {
               var1 = 1;
            }

            Iterator var6 = this.resourceEvents.values().iterator();

            while(var6.hasNext()) {
               HealthEvent var4 = (HealthEvent)var6.next();
               var2.add(var4.getDescription());
            }
         }

         HealthState var7 = this.state;
         this.state = new HealthState(var1, (String[])((String[])var2.toArray(new String[var2.size()])));
         JTARuntimeImpl.this._postSet("HealthState", var7, this.state);
         if (TxDebug.JTAHealth.isDebugEnabled()) {
            TxDebug.JTAHealth.debug("JTA Health: new state = " + this.state);
         }

         if (this.state.getState() != var7.getState() || this.state.getReasonCode().length != var7.getReasonCode().length) {
            StringBuffer var8 = new StringBuffer();

            for(int var5 = 0; var5 < var2.size(); ++var5) {
               if (var5 > 0) {
                  var8.append("; ");
               }

               var8.append(var2.get(var5));
            }

            switch (this.state.getState()) {
               case 0:
                  TXLogger.logHealthOK(HealthState.mapToString(var7.getState()), HealthState.mapToString(this.state.getState()));
                  break;
               case 1:
               case 4:
                  TXLogger.logHealthWarning(HealthState.mapToString(var7.getState()), HealthState.mapToString(this.state.getState()), var8.toString());
               case 2:
               default:
                  break;
               case 3:
                  TXLogger.logHealthError(HealthState.mapToString(var7.getState()), HealthState.mapToString(this.state.getState()), var8.toString());
            }
         }

      }

      HealthState getState() {
         return this.state;
      }
   }
}
