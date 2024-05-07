package weblogic.jms.backend;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.naming.NamingException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.application.ModuleException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.DescriptorUpdateFailedException;
import weblogic.descriptor.DescriptorUpdateRejectedException;
import weblogic.health.HealthMonitorService;
import weblogic.health.HealthState;
import weblogic.health.LowMemoryNotificationService;
import weblogic.health.MemoryEvent;
import weblogic.health.MemoryListener;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.NamedEntityBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.client.JMSConnectionFactory;
import weblogic.jms.common.DurableSubscription;
import weblogic.jms.common.EntityName;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDestinationCreateResponse;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageOpenDataConverter;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.JMSServerSessionPoolCreateResponse;
import weblogic.jms.common.LeaderManager;
import weblogic.jms.common.MessageStatisticsLogger;
import weblogic.jms.common.ModuleName;
import weblogic.jms.common.ObjectMessageImpl;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.jms.extensions.JMSModuleHelper;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.jms.store.JMSObjectHandler;
import weblogic.logging.jms.JMSMessageLogger;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSInteropModuleMBean;
import weblogic.management.configuration.JMSSessionPoolMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.JMSDestinationRuntimeMBean;
import weblogic.management.runtime.JMSServerRuntimeMBean;
import weblogic.management.runtime.JMSSessionPoolRuntimeMBean;
import weblogic.management.utils.BeanListenerCustomizer;
import weblogic.messaging.ID;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.common.ThresholdHandler;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.kernel.Cursor;
import weblogic.messaging.kernel.Destination;
import weblogic.messaging.kernel.Kernel;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.Quota;
import weblogic.messaging.kernel.QuotaPolicy;
import weblogic.messaging.kernel.Topic;
import weblogic.messaging.kernel.internal.KernelImpl;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.ObjectHandler;
import weblogic.store.PersistentStoreException;
import weblogic.store.TestStoreException;
import weblogic.store.gxa.internal.GXAResourceImpl;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.transaction.internal.XidImpl;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class BackEnd extends JMSMessageCursorRuntimeImpl implements JMSServerRuntimeMBean, MessageStatisticsLogger, Invocable, MemoryListener, BeanListenerCustomizer {
   static final long serialVersionUID = -3550452657980202118L;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String NORMAL_WM_NAME = ".System";
   private static final String LIMITED_WM_NAME = ".Limited";
   private static final String ASYNC_WM_NAME = ".AsyncPush";
   private Kernel kernel;
   private Quota backEndQuota;
   private HashMap BEQuotas = new HashMap();
   private TemporaryModule temporaryModule;
   private int temporaryCounter;
   private boolean isHostingTemporaryDestinations = false;
   private ModuleName temporaryTemplateResource;
   private String temporaryTemplateName;
   private final String name;
   private final String configType;
   private final JMSServerId backEndId;
   private HashMap durableSubscribers = new HashMap();
   private final HashMap serverSessionPoolsByName = new HashMap();
   private final HashMap serverSessionPoolsById = new HashMap();
   private long serverSessionPoolsCurrentCount;
   private long serverSessionPoolsHighCount;
   private long serverSessionPoolsTotalCount;
   private long destinationsHighCount;
   private long destinationsTotalCount;
   private boolean bound;
   private final HashMap name2Destination = new HashMap();
   private final HashMap createId2Destination = new HashMap();
   private TimerManager timerManager;
   private static final String BE_NAME_PREFIX = "weblogic.jms.";
   private static final String HEALTH_NAME_PREFIX = "JMSServer.";
   private PersistentStoreXA persistentStore;
   private boolean downgradeable;
   private String pagingDirectory;
   private final HashMap pagingConfig = new HashMap();
   private BEDurableSubscriptionStore durableSubscriptionStore;
   private BEMultiSender multiSender;
   private ThresholdHandler thresholdHandler;
   private static final int LIMITED_WM_NUM_THREADS = 8;
   private WorkManager workManager;
   private WorkManager limitedWorkManager;
   private WorkManager asyncPushWorkManager;
   private volatile boolean isMemoryLow;
   private BackEndTempDestinationFactory tempDestinationFactory;
   private BEServerSessionPoolFactory serverSessionPoolFactory;
   private JMSSessionPoolMBean[] sessionPoolMBeans;
   private int state = 0;
   private int pausedState;
   private final Object shutdownLock;
   private final Object destinationDeletionLock;
   private static final String DEFAULT_SESSION_POOL_FACTORY_JNDI = "weblogic.jms.ServerSessionPoolFactory:";
   private static final double JMS_ABOVE_QUOTA_RATE = 0.9;
   private static final long JMS_ABOVE_QUOTA_TIME = 3600000L;
   private static final HealthState OK_HEALTH_STATE = new HealthState(0);
   private Exception backEndHealthException;
   private long startupTime;
   private final InvocableMonitor invocableMonitor;
   private QuotaPolicy blockingSendPolicy;
   private JMSMessageLogger jmsMessageLogger;
   private static final String PRODUCTION = "Production";
   private static final String INSERTION = "Insertion";
   private static final String CONSUMPTION = "Consumption";
   private String productionPausedAtStartup;
   private String insertionPausedAtStartup;
   private String consumptionPausedAtStartup;

   public BackEnd(String var1, String var2) throws ManagementException {
      super(var1, false);
      this.pausedState = this.state;
      this.shutdownLock = new Object();
      this.destinationDeletionLock = new Object();
      this.backEndHealthException = null;
      this.jmsMessageLogger = null;
      this.productionPausedAtStartup = "default";
      this.insertionPausedAtStartup = "default";
      this.consumptionPausedAtStartup = "default";
      this.name = var1;
      this.configType = var2;
      JMSService var3 = JMSService.getJMSService();
      if (var3 == null) {
         throw new ManagementException("JMSService is not active");
      } else {
         this.backEndId = var3.getNextServerId();
         this.initializeWorkManagers();

         try {
            HashMap var4 = new HashMap(2);
            var4.put("WorkManager", this.workManager);
            var4.put("LimitedWorkManager", this.limitedWorkManager);
            this.kernel = new KernelImpl(this.name, var4);
            this.thresholdHandler = new BEThresholdHandler(this.kernel, this.name);
            this.multiSender = new BEMultiSender();
            this.backEndQuota = createQuota(this.kernel, this.name + ".Quota." + System.currentTimeMillis(), Long.MAX_VALUE, Integer.MAX_VALUE, QuotaPolicy.FIFO);
         } catch (KernelException var5) {
            throw new DeploymentException(var5);
         }

         LeaderManager.getLeaderManager(this.backEndId.getId());
         this.invocableMonitor = new InvocableMonitor(JMSService.getJMSService().getInvocableMonitor());
         this.serverSessionPoolFactory = new BEServerSessionPoolFactory(this.backEndId);
         this.startupTime = System.currentTimeMillis();
         LowMemoryNotificationService.addMemoryListener(this);
         HealthMonitorService.register("JMSServer." + var1, this, false);
         this.state = 1;
      }
   }

   private static Quota createQuota(Kernel var0, String var1, long var2, int var4, QuotaPolicy var5) throws KernelException {
      Quota var6 = var0.createQuota(var1);
      var6.setPolicy(var5);
      var6.setBytesMaximum(var2);
      var6.setMessagesMaximum(var4);
      return var6;
   }

   BEQuota findBEQuota(String var1) {
      synchronized(this.BEQuotas) {
         return (BEQuota)this.BEQuotas.get(var1);
      }
   }

   BEQuota createBEQuota(String var1, QuotaBean var2) throws BeanUpdateFailedException {
      int var4 = var2.getMessagesMaximum() > 2147483647L ? Integer.MAX_VALUE : (int)var2.getMessagesMaximum();

      Quota var3;
      try {
         var3 = createQuota(this.kernel, var1, var2.getBytesMaximum(), var4, QuotaPolicy.get(var2.getPolicy()));
      } catch (KernelException var9) {
         throw new BeanUpdateFailedException(var9.getMessage(), var9);
      }

      BEQuota var5 = new BEQuota(var1, var3);
      synchronized(this.BEQuotas) {
         this.BEQuotas.put(var1, var5);
         return var5;
      }
   }

   void removeBEQuota(String var1) {
      BEQuota var2 = this.findBEQuota(var1);
      if (var2 != null) {
         this.kernel.deleteQuota(var1);
      }

      synchronized(this.BEQuotas) {
         this.BEQuotas.remove(var1);
      }
   }

   WorkManager getWorkManager() {
      return this.workManager;
   }

   WorkManager getThreadLimitedWorkManager() {
      return this.limitedWorkManager;
   }

   public WorkManager getAsyncPushWorkManager() {
      return this.asyncPushWorkManager;
   }

   private void initializeWorkManagers() {
      ServerMBean var1 = ManagementService.getRuntimeAccess(KERNEL_ID).getServer();
      if (var1.getUse81StyleExecuteQueues()) {
         this.workManager = WorkManagerFactory.getInstance().getDefault();
         this.limitedWorkManager = WorkManagerFactory.getInstance().getDefault();
         this.asyncPushWorkManager = WorkManagerFactory.getInstance().getDefault();
      } else {
         this.workManager = WorkManagerFactory.getInstance().findOrCreate("weblogic.jms." + this.name + ".System", 100, 1, -1);
         this.limitedWorkManager = WorkManagerFactory.getInstance().findOrCreate("weblogic.jms." + this.name + ".Limited", 1, 8);
         this.asyncPushWorkManager = WorkManagerFactory.getInstance().findOrCreate("weblogic.jms." + this.name + ".AsyncPush", 100, 1, -1);
      }

      this.timerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.jms." + this.name, this.workManager);
   }

   public JMSMessageLogger getJMSMessageLogger() {
      return this.jmsMessageLogger;
   }

   public void setPagingDirectory(String var1) {
      this.pagingDirectory = var1;
   }

   public void setPagingFileLockingEnabled(boolean var1) {
      this.pagingConfig.put("FileLockingEnabled", var1);
   }

   public void setPagingMinWindowBufferSize(int var1) {
      this.pagingConfig.put("MinWindowBufferSize", var1);
   }

   public void setPagingMaxWindowBufferSize(int var1) {
      this.pagingConfig.put("MaxWindowBufferSize", var1);
   }

   public void setPagingIoBufferSize(int var1) {
      this.pagingConfig.put("IoBufferSize", var1);
   }

   public void setPagingBlockSize(int var1) {
      this.pagingConfig.put("BlockSize", var1);
   }

   public void setPagingMaxFileSize(long var1) {
      this.pagingConfig.put("MaxFileSize", var1);
   }

   public void setPersistentStore(PersistentStoreXA var1) {
      this.persistentStore = var1;
   }

   public PersistentStoreXA getPersistentStore() {
      return this.persistentStore;
   }

   boolean isStoreEnabled() {
      return this.persistentStore != null;
   }

   boolean isAllowsPersistentDowngrade() {
      return this.downgradeable;
   }

   public void setAllowsPersistentDowngrade(boolean var1) {
      this.downgradeable = var1;
   }

   public void setJMSMessageLogger(JMSMessageLogger var1) {
      this.jmsMessageLogger = var1;
   }

   BEDurableSubscriptionStore getDurableSubscriptionStore() {
      return this.durableSubscriptionStore;
   }

   BEMultiSender getMultiSender() {
      return this.multiSender;
   }

   public JMSServerId getJMSServerId() {
      return this.backEndId;
   }

   public Kernel getKernel() {
      return this.kernel;
   }

   public void setBlockingSendPolicy(String var1) {
      if (var1 == null) {
         JMSLogger.logIllegalThresholdValue(this.name, "BlockingSendPolicy");
         throw new IllegalArgumentException("Illegal BlockingSendPolicy value of null");
      } else {
         QuotaPolicy var2 = QuotaPolicy.get(var1);
         if (var2 != this.blockingSendPolicy) {
            this.blockingSendPolicy = var2;
            this.backEndQuota.setPolicy(this.blockingSendPolicy);
         }

      }
   }

   public HealthState getHealthState() {
      if (this.backEndHealthException != null) {
         return new HealthState(3, this.backEndHealthException.toString());
      } else if (this.thresholdHandler.isArmed()) {
         byte var1 = 0;
         ArrayList var2 = new ArrayList(6);
         long var3 = System.currentTimeMillis();
         double var5 = (double)(var3 - this.startupTime);
         long var7 = var3 - this.thresholdHandler.getMessagesThresholdTime();
         if (var7 > 3600000L) {
            var2.add(JMSExceptionLogger.logMessagesThresholdTimeExceededLoggable(this.name).getMessage());
            var1 = 1;
         } else if ((double)var7 / var5 > 0.9) {
            var2.add(JMSExceptionLogger.logMessagesThresholdRunningTimeExceededLoggable(this.name).getMessage());
            var1 = 1;
         }

         var7 = var3 - this.thresholdHandler.getBytesThresholdTime();
         if (var7 > 3600000L) {
            var2.add(JMSExceptionLogger.logBytesThresholdTimeExceededLoggable(this.name).getMessage());
            var1 = 1;
         } else if ((double)var7 / var5 > 0.9) {
            var2.add(JMSExceptionLogger.logBytesThresholdRunningTimeExceededLoggable(this.name).getMessage());
            var1 = 1;
         }

         if (var2.isEmpty()) {
            return new HealthState(var1);
         } else {
            String[] var9 = new String[var2.size()];
            var2.toArray(var9);
            return new HealthState(var1, var9);
         }
      } else {
         return OK_HEALTH_STATE;
      }
   }

   public void destroy() {
      if (this.temporaryModule != null) {
         synchronized(this.temporaryModule) {
            this.temporaryModule.close();
         }

         this.temporaryModule = null;
      }

      LowMemoryNotificationService.removeMemoryListener(this);
      HealthMonitorService.unregister("JMSServer." + this.name);
   }

   TimerManager getTimerManager() {
      return this.timerManager;
   }

   private final void createServerSessionPools() throws JMSException {
      if (this.sessionPoolMBeans != null) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("About to process " + this.sessionPoolMBeans.length + " sessionpools");
         }

         BEServerSessionPool var2;
         for(int var1 = 0; var1 < this.sessionPoolMBeans.length; ++var1) {
            JMSID var3 = JMSService.getJMSService().getNextId();

            try {
               var2 = new BEServerSessionPool(this.sessionPoolMBeans[var1].getName(), var3, this, this.sessionPoolMBeans[var1]);
               this.serverSessionPoolAdd(var2);
            } catch (Exception var7) {
               JMSLogger.logErrorCreateSSP(this.name, this.sessionPoolMBeans[var1].getName(), var7);
               throw new weblogic.jms.common.JMSException("Error creating ServerSessionPool " + this.sessionPoolMBeans[var1], var7);
            }
         }

         if (this.sessionPoolMBeans.length > 0) {
            JMSLogger.logServerSessionPoolsDeprecated();
         }

         JMSLogger.logCntPools(this.name, this.sessionPoolMBeans.length);
         Iterator var8;
         synchronized(this.shutdownLock) {
            var8 = ((HashMap)this.serverSessionPoolsById.clone()).values().iterator();
         }

         while(var8.hasNext()) {
            var2 = (BEServerSessionPool)var8.next();
            var2.start();
         }

         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug(this.sessionPoolMBeans.length + " sessionpools are created");
         }

      }
   }

   private final void destroyServerSessionPools() {
      Iterator var1;
      synchronized(this.shutdownLock) {
         var1 = ((HashMap)this.serverSessionPoolsByName.clone()).values().iterator();
      }

      while(var1.hasNext()) {
         this.serverSessionPoolRemove((BEServerSessionPool)var1.next());
      }

      synchronized(this.shutdownLock) {
         this.serverSessionPoolsByName.clear();
         this.serverSessionPoolsById.clear();
      }
   }

   private void unAdvertise() {
      if (this.bound) {
         this.bound = false;
         if (this.tempDestinationFactory != null) {
            try {
               JMSService.getJMSService().getBEDeployer().removeTempDestinationFactory(this.tempDestinationFactory);
               this.tempDestinationFactory = null;
            } catch (NamingException var4) {
            }
         }

         this.bound = false;

         try {
            PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(), "weblogic.jms.backend." + this.name, KERNEL_ID);
         } catch (NamingException var3) {
         }

         if (this.serverSessionPoolFactory != null) {
            try {
               PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(), "weblogic.jms.ServerSessionPoolFactory:" + this.getName(), KERNEL_ID);
            } catch (NamingException var2) {
            }
         }

      }
   }

   private void advertise() throws JMSException {
      try {
         this.activateFinished();
      } catch (BeanUpdateFailedException var45) {
         throw new weblogic.jms.common.JMSException(var45);
      }

      boolean var1 = false;

      try {
         try {
            PrivilegedActionUtilities.bindAsSU(JMSService.getContext(), "weblogic.jms.backend." + this.name, this.backEndId, KERNEL_ID);
         } catch (NamingException var47) {
            try {
               PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(), "weblogic.jms.backend." + this.name, this.backEndId, KERNEL_ID);
            } catch (NamingException var44) {
            }

            try {
               PrivilegedActionUtilities.bindAsSU(JMSService.getContext(), "weblogic.jms.backend." + this.name, this.backEndId, KERNEL_ID);
            } catch (NamingException var46) {
               if (JMSDebug.JMSConfig.isDebugEnabled()) {
                  JMSDebug.JMSConfig.debug("Failed to bind backend to jndiname");
               }

               JMSLogger.logBackEndBindingFailed(this.name, "weblogic.jms.backend." + this.name);
               throw new weblogic.jms.common.JMSException("Error binding JMSServer into JNDI", var47);
            }
         }

         var1 = true;
      } finally {
         if (!var1 && this.tempDestinationFactory != null) {
            try {
               JMSService.getJMSService().getBEDeployer().removeTempDestinationFactory(this.tempDestinationFactory);
               this.tempDestinationFactory = null;
            } catch (NamingException var38) {
            }
         }

      }

      var1 = false;

      try {
         if (this.serverSessionPoolFactory != null) {
            try {
               PrivilegedActionUtilities.bindAsSU(JMSService.getContext(), "weblogic.jms.ServerSessionPoolFactory:" + this.getName(), this.serverSessionPoolFactory, KERNEL_ID);
            } catch (NamingException var43) {
               try {
                  PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(), "weblogic.jms.ServerSessionPoolFactory:" + this.getName(), this.serverSessionPoolFactory, KERNEL_ID);
               } catch (NamingException var42) {
               }

               try {
                  PrivilegedActionUtilities.bindAsSU(JMSService.getContext(), "weblogic.jms.ServerSessionPoolFactory:" + this.getName(), this.serverSessionPoolFactory, KERNEL_ID);
               } catch (NamingException var41) {
                  throw new weblogic.jms.common.JMSException("Error binding weblogic.jms.ServerSessionPoolFactory:" + this.getName() + var43);
               }
            }
         }

         var1 = true;
      } finally {
         if (!var1) {
            try {
               PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(), "weblogic.jms.backend." + this.name, KERNEL_ID);
            } catch (NamingException var40) {
            }

            if (this.tempDestinationFactory != null) {
               try {
                  JMSService.getJMSService().getBEDeployer().removeTempDestinationFactory(this.tempDestinationFactory);
                  this.tempDestinationFactory = null;
               } catch (NamingException var39) {
               }
            }
         }

      }

      this.bound = true;
   }

   public void close() {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Suspending backend " + this.name);
      }

      JMSLogger.logJMSServerSuspending(this.name);
      boolean var2 = false;
      synchronized(this.shutdownLock) {
         if ((this.state & 8) == 0) {
            this.markShuttingDown();
            var2 = true;
         }
      }

      Iterator var1;
      synchronized(this.shutdownLock) {
         synchronized(this.durableSubscribers) {
            var1 = ((HashMap)this.durableSubscribers.clone()).values().iterator();
         }
      }

      while(var1.hasNext()) {
         try {
            DurableSubscription var3 = (DurableSubscription)var1.next();
            var3.close();
         } catch (JMSException var19) {
            JMSLogger.logJMSServerShutdownError(this.name, var19.toString(), var19);
         }
      }

      synchronized(this.shutdownLock) {
         synchronized(this.durableSubscribers) {
            this.durableSubscribers.clear();
         }
      }

      if (var2) {
         this.invocableMonitor.waitForInvocablesCompletion();
      }

      synchronized(this.shutdownLock) {
         var1 = ((HashMap)this.name2Destination.clone()).values().iterator();
      }

      while(var1.hasNext()) {
         this.removeDestination((BEDestinationImpl)var1.next());
      }

      synchronized(this.shutdownLock) {
         this.name2Destination.clear();
         this.createId2Destination.clear();
      }

      this.timerManager.suspend();

      try {
         PrivilegedActionUtilities.unregister(this, KERNEL_ID);
      } catch (Exception var14) {
         JMSLogger.logErrorUnregisterJMSServer(this.name, var14);
      }

      try {
         if (this.durableSubscriptionStore != null) {
            this.durableSubscriptionStore.close();
         }

         if (this.kernel != null) {
            this.kernel.close();
         }
      } catch (KernelException var13) {
         JMSLogger.logJMSServerShutdownError(this.name, var13.toString(), var13);
      }

      synchronized(this.shutdownLock) {
         this.state = 16;
      }

      JMSLogger.logJMSServerSuspended(this.name);
   }

   public void open() throws JMSException {
      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("About to start backend " + this.name + " and state =" + this.state);
      }

      JMSLogger.logJMSServerResuming(this.name);

      try {
         synchronized(this.shutdownLock) {
            this.checkShutdown();
            if ((this.state & 4) != 0) {
               return;
            }

            if ((this.state & 1) == 0) {
               throw new weblogic.jms.common.JMSException("Failed to initialize JMSServer " + this.name + ": wrong state");
            }

            this.state |= 128;
         }

         try {
            this.kernel.setProperty("Store", this.persistentStore);
            this.kernel.setProperty("ObjectHandler", this.getJMSObjectHandler());
            this.kernel.setProperty("PagingDirectory", this.pagingDirectory);
            this.kernel.setProperty("PagingParams", this.pagingConfig);
            this.kernel.open();
         } catch (KernelException var23) {
            throw new weblogic.jms.common.JMSException(var23);
         }

         if (this.isStoreEnabled()) {
            this.durableSubscriptionStore = new BEDurableSubscriptionStore(this.name, this.persistentStore);
            this.durableSubscriptionStore.recover();
         }

         this.timerManager.resume();

         try {
            PrivilegedActionUtilities.register(this, KERNEL_ID);
         } catch (ManagementException var22) {
            try {
               PrivilegedActionUtilities.unregister(this, KERNEL_ID);
            } catch (ManagementException var21) {
            }

            try {
               PrivilegedActionUtilities.register(this, KERNEL_ID);
            } catch (ManagementException var20) {
               throw new weblogic.jms.common.JMSException("Error in resuming JMS server " + this.name, var22);
            }
         }

         this.advertise();
         synchronized(this.shutdownLock) {
            this.checkShutdown();
            this.state = 4;
         }
      } finally {
         synchronized(this.shutdownLock) {
            this.state &= -129;
         }
      }
   }

   private ObjectHandler getJMSObjectHandler() {
      JMSObjectHandler var1 = new JMSObjectHandler();
      return (ObjectHandler)(ObjectMessageImpl.isTestStoreExceptionEnabled() ? new ObjectHandlerTestStoreException(var1) : var1);
   }

   public void markShuttingDown() {
      boolean var1 = false;
      synchronized(this.shutdownLock) {
         if ((this.state & 16) != 0) {
            return;
         }

         var1 = (this.state & 3) == 0;
         this.state = 8;
         Iterator var3 = this.name2Destination.values().iterator();

         while(var3.hasNext()) {
            ((BEDestinationImpl)var3.next()).markShuttingDown();
         }

         var3 = this.serverSessionPoolsByName.values().iterator();

         while(true) {
            if (!var3.hasNext()) {
               break;
            }

            ((BEServerSessionPool)var3.next()).markShuttingDown();
         }
      }

      if (var1) {
         this.preSuspendOrShutdown();
      }

   }

   private boolean isShutdown() {
      return (this.state & 24) != 0;
   }

   private boolean isShutdownOrSuspended() {
      return this.isShutdown() || (this.state & 3) != 0;
   }

   private void checkShutdown() throws JMSException {
      if (this.isShutdown()) {
         throw new weblogic.jms.common.JMSException("JMSServer is shutdown");
      }
   }

   private void checkShutdown(String var1) throws JMSException {
      if (this.isShutdown()) {
         throw new weblogic.jms.common.JMSException("Failed to " + var1 + " because JMSServer is shutdown");
      }
   }

   public void checkShutdownOrSuspended(String var1) throws JMSException {
      if (this.isShutdownOrSuspended()) {
         throw new weblogic.jms.common.JMSException("Failed to " + var1 + " because JMSServer is shutdown or " + "suspended");
      }
   }

   public void checkShutdownNeedLock(String var1) throws JMSException {
      synchronized(this.shutdownLock) {
         if (this.isShutdown()) {
            throw new weblogic.jms.common.JMSException("Failed to " + var1 + " because JMSServer is shutdown");
         }
      }
   }

   public void checkShutdownOrSuspendedNeedLock(String var1) throws JMSException {
      synchronized(this.shutdownLock) {
         if (this.isShutdown() || (this.state & 3) != 0) {
            throw new weblogic.jms.common.JMSException("Failed to " + var1 + " because JMSServer " + this.name + " is shutdown or suspended " + this.state);
         }
      }
   }

   private void preSuspendOrShutdown() {
      this.unAdvertise();
      this.destroyServerSessionPools();
   }

   public void setSessionPoolMBeans(JMSSessionPoolMBean[] var1) {
      this.sessionPoolMBeans = var1;
   }

   private void serverSessionPoolAdd(BEServerSessionPool var1) throws JMSException {
      synchronized(this.shutdownLock) {
         this.checkShutdown("create server session pool");
         if (this.serverSessionPoolsById.put(var1.getId(), var1) == null && this.serverSessionPoolsByName.put(var1.getName(), var1) == null) {
            if (++this.serverSessionPoolsCurrentCount > this.serverSessionPoolsHighCount) {
               this.serverSessionPoolsHighCount = this.serverSessionPoolsCurrentCount;
            }

            ++this.serverSessionPoolsTotalCount;
         }

      }
   }

   private int serverSessionGet(Request var1) throws JMSException {
      this.checkShutdownOrSuspendedNeedLock("get server session");
      BEServerSessionGetRequest var2 = (BEServerSessionGetRequest)var1;
      BEServerSessionPool var3 = this.serverSessionPoolFind(var2.getServerSessionPoolId());
      if (var3 != null) {
         var2.setResult(new BEServerSessionGetResponse((BEServerSession)var3.getServerSession(var3.getBackEndId().getDispatcherId())));
         var2.setState(Integer.MAX_VALUE);
         return var2.getState();
      } else {
         throw new JMSException("Server session pool not found");
      }
   }

   private int serverSessionPoolCreate(Request var1) throws JMSException {
      this.checkShutdownOrSuspendedNeedLock("create server session pool");
      BEServerSessionPoolCreateRequest var2 = (BEServerSessionPoolCreateRequest)var1;
      final JMSID var3 = JMSService.getJMSService().getNextId();
      final BackEnd var4 = this;
      final JMSConnectionFactory var5 = var2.getConnectionFactory();
      final int var6 = var2.getSessionsMaximum();
      final int var7 = var2.getAcknowledgeMode();
      final boolean var8 = var2.isTransacted();
      final String var9 = var2.getMessageListenerClass();
      final Serializable var10 = var2.getClientData();
      final String var11 = "ServerSessionPool" + var3.getCounter();

      BEServerSessionPool var12;
      try {
         try {
            var12 = (BEServerSessionPool)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
               public Object run() throws ManagementException, JMSException {
                  return new BEServerSessionPool(var11, var3, var4, var5, var6, var7, var8, var9, var10);
               }
            });
         } catch (PrivilegedActionException var14) {
            throw var14.getException();
         }

         this.serverSessionPoolAdd(var12);
      } catch (Exception var15) {
         throw new weblogic.jms.common.JMSException("Error create server session pool", var15);
      }

      var2.setResult(new JMSServerSessionPoolCreateResponse((ServerSessionPool)var12.getRemoteWrapper()));
      var2.setState(Integer.MAX_VALUE);
      return var2.getState();
   }

   private int serverSessionPoolRemove(Request var1) throws JMSException {
      this.checkShutdownOrSuspendedNeedLock("remove server session pool");
      BEServerSessionPoolCloseRequest var2 = (BEServerSessionPoolCloseRequest)var1;
      BEServerSessionPool var3 = this.serverSessionPoolFind(var2.getServerSessionPoolId());
      if (var3 != null) {
         this.serverSessionPoolRemove(var3);
         var3.close();
         var2.setResult(new VoidResponse());
         var2.setState(Integer.MAX_VALUE);
         return var2.getState();
      } else {
         throw new JMSException("Error removing server session pool: instance not found");
      }
   }

   public void serverSessionPoolRemove(BEServerSessionPool var1) {
      synchronized(this.shutdownLock) {
         if (this.serverSessionPoolsByName.remove(var1.getName()) != null || this.serverSessionPoolsById.remove(var1.getId()) != null) {
            --this.serverSessionPoolsCurrentCount;
         }
      }

      var1.shutdown();
   }

   public BEServerSessionPool serverSessionPoolFind(JMSID var1) {
      synchronized(this.shutdownLock) {
         return (BEServerSessionPool)this.serverSessionPoolsById.get(var1);
      }
   }

   synchronized Queue findKernelQueue(String var1) {
      return this.kernel.findQueue(var1);
   }

   Queue createKernelQueue(String var1, Map var2) throws JMSException {
      try {
         return this.kernel.createQueue(var1, var2);
      } catch (KernelException var4) {
         throw new weblogic.jms.common.JMSException("Can't create kernel queue", var4);
      }
   }

   synchronized Topic findKernelTopic(String var1) {
      return this.kernel.findTopic(var1);
   }

   Topic createKernelTopic(String var1, Map var2) throws JMSException {
      try {
         return this.kernel.createTopic(var1, var2);
      } catch (KernelException var4) {
         throw new weblogic.jms.common.JMSException("Can't create kernel topic", var4);
      }
   }

   public BEDestinationImpl createTemporaryDestination(DispatcherId var1, String var2, JMSID var3, boolean var4, long var5, String var7) throws JMSException {
      BEDestinationImpl var9 = null;
      if (this.temporaryModule == null) {
         throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logTemporaryTemplateNotConfiguredLoggable(this.name));
      } else {
         synchronized(this.temporaryModule) {
            EntityName var8 = this.temporaryModule.prepareCreateTemporaryDestination(var2.equalsIgnoreCase("Queue"), this.temporaryCounter++);
            boolean var11 = false;

            try {
               var9 = this.findDestination(var8.toString());
               if (var9 == null) {
                  throw new weblogic.jms.common.JMSException("ERROR: Creation of temporary destination failed to find the newly added destination");
               }

               var9.setConnectionId(var3);
               this.checkShutdownOrSuspendedNeedLock("create temporary destination");
               synchronized(this.shutdownLock) {
                  this.checkShutdownOrSuspended("create temporary destination");
                  BEConnection var13 = null;
                  JMSDispatcher var14 = null;

                  try {
                     var14 = JMSDispatcherManager.dispatcherFindOrCreate(var1);
                  } catch (DispatcherException var28) {
                     throw new weblogic.jms.common.JMSException("Error finding FE dispatcher: " + var1, var28);
                  }

                  var13 = BEManager.connectionFindOrCreate(var3, var14, var4, var5, var7);
                  var13.tempDestinationAdd(var9);
               }

               var11 = true;
            } finally {
               if (!var11) {
                  try {
                     this.temporaryModule.rollbackCreateTemporaryDestination();
                  } catch (weblogic.jms.common.JMSException var27) {
                  }
               }

            }

            this.temporaryModule.activateCreateTemporaryDestination();
            return var9;
         }
      }
   }

   public void addDestination(BEDestinationImpl var1) throws JMSException {
      this.checkShutdown();
      synchronized(this.shutdownLock) {
         String var3 = var1.getName();
         String var4 = var1.getJMSCreateDestinationIdentifier();
         if (var4 == null) {
            var4 = var3;
         }

         if (this.createId2Destination.containsKey(var4)) {
            BEDestinationImpl var5 = (BEDestinationImpl)this.createId2Destination.get(var4);
            String var6 = var5.getName();
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCreateDestinationIdentifierNameConflictLoggable(this.name, var4, var6, var3));
         } else {
            this.createId2Destination.put(var4, var1);
            if (this.name2Destination.containsKey(var3)) {
               this.createId2Destination.remove(var4);
               throw new JMSException(JMSExceptionLogger.logNameConflictLoggable(this.name, var3).getMessage());
            } else {
               this.name2Destination.put(var1.getName(), var1);

               try {
                  InvocableManagerDelegate.delegate.invocableAdd(20, var1);
                  if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
                     JMSDebug.JMSDistTopic.debug("Added destination " + var1.getName() + var1.getJMSID());
                  }
               } catch (JMSException var8) {
                  this.name2Destination.remove(var1.getName());
                  this.createId2Destination.remove(var4);
                  throw var8;
               }

               ++this.destinationsTotalCount;
               this.destinationsHighCount = Math.max(this.destinationsHighCount, (long)this.name2Destination.size());
               if (JMSDebug.JMSBoot.isDebugEnabled()) {
                  JMSDebug.JMSBoot.debug("Configured destination " + var1);
               }

            }
         }
      }
   }

   public void removeDestination(BEDestinationImpl var1) {
      String var2 = var1.getJMSCreateDestinationIdentifier();
      if (var2 == null) {
         var2 = var1.getName();
      }

      var1.markShuttingDown();
      var1.shutdown();
      synchronized(this.shutdownLock) {
         this.createId2Destination.remove(var2);
         this.name2Destination.remove(var1.getName());
         InvocableManagerDelegate.delegate.invocableRemove(20, var1.getJMSID());
      }
   }

   private int findDestination(Request var1) throws JMSException {
      this.checkShutdownOrSuspendedNeedLock("find destination");
      BEDestinationCreateRequest var2 = (BEDestinationCreateRequest)var1;
      BEDestinationImpl var3;
      if (!var2.isForCreateDestination()) {
         var3 = this.findDestination(var2.getDestinationName());
      } else {
         var3 = this.findDestinationByCreateName(var2.getDestinationName());
      }

      if (var3 == null) {
         throw new weblogic.jms.common.JMSException("Destination " + var2.getDestinationName() + " not found");
      } else if (var2.getDestType() == 1 && var3.getDestinationImpl().isTopic()) {
         throw new weblogic.jms.common.JMSException("No destination " + var2.getDestinationName() + " of type queue");
      } else if (var2.getDestType() == 2 && var3.getDestinationImpl().isQueue()) {
         throw new weblogic.jms.common.JMSException("No destination " + var2.getDestinationName() + " of type topic");
      } else if (var3.isStarted()) {
         var2.setResult(new JMSDestinationCreateResponse(var3.getDestinationImpl()));
         var2.setState(Integer.MAX_VALUE);
         return var2.getState();
      } else {
         throw new weblogic.jms.common.JMSException("Destination " + var2.getDestinationName() + " not found");
      }
   }

   public BEDestinationImpl findDestination(String var1) {
      synchronized(this.shutdownLock) {
         return (BEDestinationImpl)this.name2Destination.get(var1);
      }
   }

   public BEDestinationImpl findDestinationByCreateName(String var1) {
      synchronized(this.shutdownLock) {
         return (BEDestinationImpl)this.createId2Destination.get(var1);
      }
   }

   public final long getBytesMaximum() {
      long var1 = this.backEndQuota.getBytesMaximum();
      return var1 == Long.MAX_VALUE ? -1L : var1;
   }

   public void setBytesMaximum(long var1) {
      if (var1 < 0L) {
         var1 = Long.MAX_VALUE;
      }

      this.backEndQuota.setBytesMaximum(var1);
   }

   public long getBytesThresholdHigh() {
      return this.thresholdHandler.getBytesThresholdHigh();
   }

   public void setBytesThresholdHigh(long var1) {
      this.thresholdHandler.setBytesThresholdHigh(var1);
   }

   public long getBytesThresholdLow() {
      return this.thresholdHandler.getBytesThresholdLow();
   }

   public void setBytesThresholdLow(long var1) {
      this.thresholdHandler.setBytesThresholdLow(var1);
   }

   public final long getMessagesMaximum() {
      int var1 = this.backEndQuota.getMessagesMaximum();
      return var1 == Integer.MAX_VALUE ? -1L : (long)var1;
   }

   public void setMessagesMaximum(long var1) {
      if (var1 < 0L || var1 > 2147483647L) {
         var1 = 2147483647L;
      }

      this.backEndQuota.setMessagesMaximum((int)var1);
   }

   public long getMessagesThresholdHigh() {
      return this.thresholdHandler.getMessagesThresholdHigh();
   }

   public void setMessagesThresholdHigh(long var1) {
      this.thresholdHandler.setMessagesThresholdHigh(var1);
   }

   public long getMessagesThresholdLow() {
      return this.thresholdHandler.getMessagesThresholdLow();
   }

   public void setMessagesThresholdLow(long var1) {
      this.thresholdHandler.setMessagesThresholdLow(var1);
   }

   public void setMaximumMessageSize(int var1) {
      try {
         this.kernel.setProperty("MaximumMessageSize", new Integer(var1));
      } catch (KernelException var3) {
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public void setMessageBufferSize(long var1) {
      try {
         this.kernel.setProperty("MessageBufferSize", new Long(var1));
      } catch (KernelException var4) {
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public void setExpirationScanInterval(int var1) {
   }

   public void setProductionPausedAtStartup(String var1) {
      this.productionPausedAtStartup = var1;
   }

   public String getProductionPausedAtStartup() {
      return this.productionPausedAtStartup;
   }

   public void setInsertionPausedAtStartup(String var1) {
      this.insertionPausedAtStartup = var1;
   }

   public String getInsertionPausedAtStartup() {
      return this.insertionPausedAtStartup;
   }

   public void setConsumptionPausedAtStartup(String var1) {
      this.consumptionPausedAtStartup = var1;
   }

   public String getConsumptionPausedAtStartup() {
      return this.consumptionPausedAtStartup;
   }

   public void startAddJMSSessionPools(JMSSessionPoolMBean var1) throws BeanUpdateRejectedException {
      String var2 = var1.getName();
      JMSID var3 = JMSService.getJMSService().getNextId();

      BEServerSessionPool var4;
      try {
         var4 = new BEServerSessionPool(var2, var3, this, var1);
      } catch (ManagementException var8) {
         throw new BeanUpdateRejectedException("Could not create a session pool", var8);
      } catch (JMSException var9) {
         throw new BeanUpdateRejectedException("Could not create a session pool", var9);
      }

      synchronized(this.shutdownLock) {
         if (this.serverSessionPoolsByName.get(var2) != null) {
            var4.cleanup();
            if (JMSDebug.JMSConfig.isDebugEnabled()) {
               JMSDebug.JMSConfig.debug("Error adding server session pool: instance already exists");
            }

            throw new BeanUpdateRejectedException("Error adding server session pool: instance already exists");
         } else {
            try {
               this.serverSessionPoolAdd(var4);
               if ((this.state & 4) != 0) {
                  var4.start();
               }
            } catch (JMSException var10) {
               throw new BeanUpdateRejectedException("Could not add or start a session pool", var10);
            }

         }
      }
   }

   public void finishAddJMSSessionPools(JMSSessionPoolMBean var1, boolean var2) {
      if (!var2) {
         synchronized(this.shutdownLock) {
            BEServerSessionPool var3 = (BEServerSessionPool)this.serverSessionPoolsByName.get(var1.getName());
            if (var3 != null) {
               this.serverSessionPoolRemove(var3);
            }
         }
      }
   }

   public void startRemoveJMSSessionPools(JMSSessionPoolMBean var1) throws BeanUpdateRejectedException {
      BEServerSessionPool var2 = null;
      synchronized(this.shutdownLock) {
         var2 = (BEServerSessionPool)this.serverSessionPoolsByName.get(var1.getName());
         if (var2 == null) {
            if (JMSDebug.JMSConfig.isDebugEnabled()) {
               JMSDebug.JMSConfig.debug("Error removing SessionPool: instance doesn't exist");
            }

            throw new BeanUpdateRejectedException("Error removing SessionPool: instance doesn't exist");
         }
      }
   }

   public void finishRemoveJMSSessionPools(JMSSessionPoolMBean var1, boolean var2) {
      if (var2) {
         BEServerSessionPool var3 = null;
         synchronized(this.shutdownLock) {
            var3 = (BEServerSessionPool)this.serverSessionPoolsByName.get(var1.getName());
            if (var3 != null) {
               this.serverSessionPoolRemove(var3);
            }
         }
      }
   }

   public JMSSessionPoolRuntimeMBean[] getSessionPoolRuntimes() {
      synchronized(this.shutdownLock) {
         JMSSessionPoolRuntimeMBean[] var2 = new JMSSessionPoolRuntimeMBean[this.serverSessionPoolsByName.size()];
         Iterator var3 = this.serverSessionPoolsByName.values().iterator();

         for(int var4 = 0; var3.hasNext(); ++var4) {
            var2[var4] = (JMSSessionPoolRuntimeMBean)var3.next();
         }

         return var2;
      }
   }

   public BEServerSessionPool[] getSessionPools() {
      synchronized(this.shutdownLock) {
         BEServerSessionPool[] var2 = new BEServerSessionPool[this.serverSessionPoolsByName.size()];
         Iterator var3 = this.serverSessionPoolsByName.values().iterator();

         for(int var4 = 0; var3.hasNext(); ++var4) {
            var2[var4] = (BEServerSessionPool)var3.next();
         }

         return var2;
      }
   }

   public long getSessionPoolsCurrentCount() {
      synchronized(this.shutdownLock) {
         return this.serverSessionPoolsCurrentCount;
      }
   }

   public synchronized long getSessionPoolsHighCount() {
      synchronized(this.shutdownLock) {
         return this.serverSessionPoolsHighCount;
      }
   }

   public synchronized long getSessionPoolsTotalCount() {
      synchronized(this.shutdownLock) {
         return this.serverSessionPoolsTotalCount;
      }
   }

   public JMSDestinationRuntimeMBean[] getDestinations() {
      synchronized(this.shutdownLock) {
         JMSDestinationRuntimeMBean[] var2 = new JMSDestinationRuntimeMBean[this.name2Destination.size()];
         Iterator var3 = this.name2Destination.values().iterator();

         BEDestinationImpl var5;
         for(int var4 = 0; var3.hasNext(); var2[var4++] = var5.getRuntimeMBean()) {
            var5 = (BEDestinationImpl)var3.next();
         }

         return var2;
      }
   }

   public BEDestinationImpl[] getBEDestinations() {
      synchronized(this.shutdownLock) {
         BEDestinationImpl[] var2 = new BEDestinationImpl[this.name2Destination.size()];
         Iterator var3 = this.name2Destination.values().iterator();

         for(int var4 = 0; var3.hasNext(); var2[var4++] = (BEDestinationImpl)var3.next()) {
         }

         return var2;
      }
   }

   public long getDestinationsCurrentCount() {
      synchronized(this.shutdownLock) {
         return (long)this.name2Destination.size();
      }
   }

   public long getDestinationsHighCount() {
      synchronized(this.shutdownLock) {
         return this.destinationsHighCount;
      }
   }

   public long getDestinationsTotalCount() {
      synchronized(this.shutdownLock) {
         return this.destinationsTotalCount;
      }
   }

   private int removeTempDestination(Request var1) throws JMSException {
      BETemporaryDestinationDestroyRequest var2 = (BETemporaryDestinationDestroyRequest)var1;

      try {
         this.checkShutdownOrSuspendedNeedLock("remove temporary destination");
      } catch (JMSException var8) {
         var2.setResult(new VoidResponse());
         var2.setState(Integer.MAX_VALUE);
         return var2.getState();
      }

      BEDestinationImpl var3 = (BEDestinationImpl)InvocableManagerDelegate.delegate.invocableFind(20, var2.getDestinationId());
      var3.deleteTempDestination();
      BEConnection var4 = (BEConnection)InvocableManagerDelegate.delegate.invocableFind(15, var3.getConnectionId());
      var4.tempDestinationRemove(var3.getJMSID());
      synchronized(this.temporaryModule) {
         this.temporaryModule.removeTemporaryDestination(var3.getName());
      }

      var2.setResult(new VoidResponse());
      var2.setState(Integer.MAX_VALUE);
      return var2.getState();
   }

   void addDurableSubscription(String var1, DurableSubscription var2) {
      synchronized(this.durableSubscribers) {
         this.durableSubscribers.put(var1, var2);
      }
   }

   void removeDurableSubscription(String var1) {
      synchronized(this.durableSubscribers) {
         this.durableSubscribers.remove(var1);
      }
   }

   Map getDurableSubscriptionsMap() {
      return this.durableSubscribers;
   }

   public DurableSubscription getDurableSubscription(String var1) {
      synchronized(this.durableSubscribers) {
         return (DurableSubscription)this.durableSubscribers.get(var1);
      }
   }

   public void logMessagesThresholdHigh() {
      JMSLogger.logMessagesThresholdHighServer(this.name);
   }

   public void logMessagesThresholdLow() {
      JMSLogger.logMessagesThresholdLowServer(this.name);
   }

   public void logBytesThresholdHigh() {
      JMSLogger.logBytesThresholdHighServer(this.name);
   }

   public void logBytesThresholdLow() {
      JMSLogger.logBytesThresholdLowServer(this.name);
   }

   public long getMessagesCurrentCount() {
      return (long)(this.kernel.getStatistics().getMessagesCurrent() - this.kernel.getStatistics().getMessagesPending());
   }

   public long getMessagesHighCount() {
      return (long)this.kernel.getStatistics().getMessagesHigh();
   }

   public long getMessagesPendingCount() {
      return (long)this.kernel.getStatistics().getMessagesPending();
   }

   public long getMessagesReceivedCount() {
      return (long)this.kernel.getStatistics().getMessagesReceived();
   }

   public long getBytesCurrentCount() {
      return this.kernel.getStatistics().getBytesCurrent() - this.kernel.getStatistics().getBytesPending();
   }

   public long getBytesHighCount() {
      return this.kernel.getStatistics().getBytesHigh();
   }

   public long getBytesPendingCount() {
      return this.kernel.getStatistics().getBytesPending();
   }

   public long getBytesReceivedCount() {
      return this.kernel.getStatistics().getBytesReceived();
   }

   public long getMessagesThresholdTime() {
      return this.thresholdHandler.getMessagesThresholdTime();
   }

   public long getBytesThresholdTime() {
      return this.thresholdHandler.getBytesThresholdTime();
   }

   public int getMessagesPageableCurrentCount() {
      return this.kernel.getStatistics().getUnpagedMessages();
   }

   public long getBytesPageableCurrentCount() {
      return this.kernel.getStatistics().getUnpagedBytes();
   }

   public int getMessagesPagedOutTotalCount() {
      return this.kernel.getStatistics().getMessagesPagedOut();
   }

   public int getMessagesPagedInTotalCount() {
      return this.kernel.getStatistics().getMessagesPagedIn();
   }

   public long getBytesPagedOutTotalCount() {
      return this.kernel.getStatistics().getBytesPagedOut();
   }

   public long getBytesPagedInTotalCount() {
      return this.kernel.getStatistics().getBytesPagedIn();
   }

   public long getPagingAllocatedWindowBufferBytes() {
      return this.kernel.getStatistics().getPagingAllocatedWindowBufferBytes();
   }

   public long getPagingAllocatedIoBufferBytes() {
      return this.kernel.getStatistics().getPagingAllocatedIoBufferBytes();
   }

   public long getPagingPhysicalWriteCount() {
      return this.kernel.getStatistics().getPagingPhysicalWriteCount();
   }

   public String getName() {
      return this.name;
   }

   public String getConfigType() {
      return this.configType;
   }

   public String toString() {
      return "(" + System.identityHashCode(this) + ", name=" + this.name + ")";
   }

   public JMSID getJMSID() {
      return this.backEndId.getId();
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 11534:
            return this.findDestination(var1);
         case 12302:
            return this.serverSessionGet(var1);
         case 12558:
            return this.serverSessionPoolRemove(var1);
         case 12814:
            return this.serverSessionPoolCreate(var1);
         case 15118:
            return this.removeTempDestination(var1);
         default:
            throw new weblogic.jms.common.JMSException("No such method " + var1.getMethodId());
      }
   }

   protected void finalize() {
      try {
         super.finalize();
      } catch (Throwable var3) {
      }

      if (this.bound) {
         try {
            PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(), "weblogic.jms.backend." + this.name, KERNEL_ID);
            this.bound = false;
         } catch (Throwable var2) {
         }
      }

   }

   public Object getDestinationDeletionLock() {
      return this.destinationDeletionLock;
   }

   boolean needsFlowControl() {
      return this.isMemoryLow || this.thresholdHandler.isArmed();
   }

   public void memoryChanged(MemoryEvent var1) {
      if (var1.getEventType() == 1) {
         JMSLogger.logFlowControlEnabledDueToLowMemory(this.name);
         this.isMemoryLow = true;
         this.suspendMessageLogging();
      } else if (var1.getEventType() == 0) {
         this.isMemoryLow = false;
         this.resumeMessageLogging();
      }

   }

   public void pauseProduction() throws JMSException {
      this.pauseProduction(true);
   }

   public void pauseProduction(boolean var1) throws JMSException {
      synchronized(this.shutdownLock) {
         if (!this.isProductionPaused()) {
            this.pausedState |= 256;
            this.pauseDestinations("Production", var1);
            this.pausedState &= -257;
            this.pausedState |= 512;
         }
      }

      if (var1) {
         JMSLogger.logProductionPauseOfJMSServer(this.name);
      }

   }

   public void resumeProduction() throws JMSException {
      this.resumeProduction(true);
   }

   public void resumeProduction(boolean var1) throws JMSException {
      synchronized(this.shutdownLock) {
         this.pausedState |= 1024;
         this.resumeDestinations("Production", var1);
         this.pausedState &= -1025;
         this.pausedState &= -513;
      }

      if (var1) {
         JMSLogger.logProductionResumeOfJMSServer(this.name);
      }

   }

   public boolean isProductionPaused() {
      return (this.pausedState & 768) != 0;
   }

   public String getProductionPausedState() {
      if ((this.pausedState & 256) != 0) {
         return "Production-Pausing";
      } else {
         return (this.pausedState & 512) != 0 ? "Production-Paused" : "Production-Enabled";
      }
   }

   public void pauseInsertion() throws JMSException {
      synchronized(this.shutdownLock) {
         this.pausedState |= 2048;
         this.pauseDestinations("Insertion", true);
         this.pausedState &= -2049;
         this.pausedState |= 4096;
      }

      JMSLogger.logInsertionPauseOfJMSServer(this.name);
   }

   public void resumeInsertion() throws JMSException {
      synchronized(this.shutdownLock) {
         this.pausedState |= 8192;
         this.resumeDestinations("Insertion", true);
         this.pausedState &= -8193;
         this.pausedState &= -4097;
      }

      JMSLogger.logInsertionResumeOfJMSServer(this.name);
   }

   public boolean isInsertionPaused() {
      return (this.pausedState & 6144) != 0;
   }

   public String getInsertionPausedState() {
      if ((this.pausedState & 2048) != 0) {
         return "Insertion-Pausing";
      } else {
         return (this.pausedState & 4096) != 0 ? "Insertion-Paused" : "Insertion-Enabled";
      }
   }

   public void pauseConsumption() throws JMSException {
      this.pauseConsumption(true);
   }

   public void pauseConsumption(boolean var1) throws JMSException {
      synchronized(this.shutdownLock) {
         this.pausedState |= 16384;
         this.pauseDestinations("Consumption", var1);
         this.pausedState &= -16385;
         this.pausedState |= 32768;
      }

      if (var1) {
         JMSLogger.logConsumptionPauseOfJMSServer(this.name);
      }

   }

   public void resumeConsumption() throws JMSException {
      this.resumeConsumption(true);
   }

   public void resumeConsumption(boolean var1) throws JMSException {
      synchronized(this.shutdownLock) {
         this.pausedState |= 65536;
         this.resumeDestinations("Consumption", var1);
         this.pausedState &= -65537;
         this.pausedState &= -32769;
      }

      if (var1) {
         JMSLogger.logConsumptionResumeOfJMSServer(this.name);
      }

   }

   public boolean isConsumptionPaused() {
      return (this.pausedState & '쀀') != 0;
   }

   public String getConsumptionPausedState() {
      if ((this.pausedState & 16384) != 0) {
         return "Consumption-Pausing";
      } else {
         return (this.pausedState & '耀') != 0 ? "Consumption-Paused" : "Consumption-Enabled";
      }
   }

   private void pauseDestinations(String var1, boolean var2) throws JMSException {
      this.checkShutdownOrSuspendedNeedLock("pausing " + var1 + " on all the destinations hosted by this JMSServer");
      BEDestinationImpl[] var3 = this.getBEDestinations();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         BEDestinationImpl var5 = var3[var4];
         if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
            JMSDebug.JMSPauseResume.debug("Performing " + var1 + " pause operation on " + var5.getName());
         }

         if (var1.equals("Production")) {
            var5.pauseProduction(var2);
         } else if (var1.equals("Insertion")) {
            var5.pauseInsertion();
         } else if (var1.equals("Consumption")) {
            var5.pauseConsumption(var2);
         }
      }

   }

   private void resumeDestinations(String var1, boolean var2) throws JMSException {
      this.checkShutdownOrSuspendedNeedLock("resuming " + var1 + " on all the destinations hosted by this JMSServer");
      BEDestinationImpl[] var3 = this.getBEDestinations();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         BEDestinationImpl var5 = var3[var4];
         if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
            JMSDebug.JMSPauseResume.debug("Performing " + var1 + " resume operation on " + var5.getName());
         }

         if (var1.equals("Production")) {
            var5.resumeProduction(var2);
         } else if (var1.equals("Insertion")) {
            var5.resumeInsertion();
         } else if (var1.equals("Consumption")) {
            var5.resumeConsumption(var2);
         }
      }

   }

   public Quota getQuota() {
      return this.backEndQuota;
   }

   public void postDeploymentsStart() {
      if ((this.state & 4) != 0) {
         try {
            this.createServerSessionPools();
         } catch (JMSException var7) {
            System.out.println("ERROR: Could not create server session pools: " + var7);
         }

         try {
            if (this.durableSubscriptionStore != null) {
               this.durableSubscriptionStore.deleteOrphanedSubscriptions();
            }
         } catch (JMSException var6) {
            JMSDebug.JMSBoot.debug("Error deleting orphaned topic subscriptions", var6);
         }

         Collection var1 = this.kernel.getDestinations();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Destination var3 = (Destination)var2.next();
            if (var3.isSuspended(16384) && !var3.isCreated()) {
               if (JMSDebug.JMSBoot.isDebugEnabled()) {
                  JMSDebug.JMSBoot.debug("Deleting orphaned kernel destination " + var3.getName());
               }

               try {
                  KernelRequest var4 = new KernelRequest();
                  var3.delete(var4);
               } catch (KernelException var5) {
                  JMSDebug.JMSBoot.debug("Error deleting kernel destination", var5);
               }
            }
         }

         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug(this.name + "is started");
         }

         JMSLogger.logJMSServerDeployed(this.name);
      }
   }

   public void postDeploymentsStop() {
      if ((this.state & 16) != 0) {
         this.destroyServerSessionPools();
      }
   }

   private GXAResourceImpl getGXAResource() {
      PersistentStoreXA var1 = (PersistentStoreXA)this.kernel.getProperty("Store");
      if (var1 == null) {
         return null;
      } else {
         GXAResourceImpl var2 = null;

         try {
            var2 = (GXAResourceImpl)var1.getGXAResource();
            return var2;
         } catch (PersistentStoreException var4) {
            return null;
         }
      }
   }

   private String[] convertXidsToStrings(Xid[] var1) {
      if (var1 == null) {
         return null;
      } else {
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = var1[var3].toString();
         }

         return var2;
      }
   }

   public String[] getTransactions() {
      GXAResourceImpl var1 = this.getGXAResource();
      return var1 == null ? null : this.convertXidsToStrings(var1.getXIDs(-1));
   }

   public String[] getPendingTransactions() {
      GXAResourceImpl var1 = this.getGXAResource();
      return var1 == null ? null : this.convertXidsToStrings(var1.getXIDs(3));
   }

   public Integer getTransactionStatus(String var1) {
      GXAResourceImpl var2 = this.getGXAResource();
      if (var2 == null) {
         return new Integer(6);
      } else {
         XidImpl var3 = XidImpl.create(var1);
         int var4 = var2.getStatus(var3);
         switch (var4) {
            case 0:
            case 1:
               return new Integer(0);
            case 2:
               return new Integer(7);
            case 3:
               return new Integer(2);
            case 4:
               return new Integer(4);
            case 5:
               return new Integer(1);
            case 6:
               return new Integer(3);
            default:
               return new Integer(5);
         }
      }
   }

   public String getMessages(String var1, Integer var2) throws ManagementException {
      XidImpl var3 = XidImpl.create(var1);
      Cursor var4 = null;

      try {
         var4 = this.kernel.createCursor(var3);
      } catch (KernelException var6) {
         throw new ManagementException("Error creating message cursor for Xid " + var1 + " on server " + this.getName(), var6);
      }

      JMSMessageCursorDelegate var5 = new JMSMessageCursorDelegate(this, new JMSMessageOpenDataConverter(false), var4, new JMSMessageOpenDataConverter(true), var2);
      this.addCursorDelegate(var5);
      return var5.getHandle();
   }

   public Void forceCommit(String var1) throws ManagementException {
      GXAResourceImpl var2 = this.getGXAResource();
      if (var2 == null) {
         throw new ManagementException("Resource not available for performing forceCommit operation.");
      } else {
         XidImpl var3 = XidImpl.create(var1);

         try {
            if (var2.getStatus(var3) == 3) {
               var2.commit(var3, false);
            } else {
               var2.commit(var3, true);
            }

            JMSLogger.logAdminForceCommit(this.name, var1);
            return null;
         } catch (XAException var5) {
            JMSLogger.logAdminForceCommitError(this.name, var1, var5);
            throw new ManagementException("Error on forceCommit of JMS transaction branch " + var1 + ". ", var5);
         }
      }
   }

   public Void forceRollback(String var1) throws ManagementException {
      GXAResourceImpl var2 = this.getGXAResource();
      if (var2 == null) {
         throw new ManagementException("Resource not available for performing forceRollback operation.");
      } else {
         XidImpl var3 = XidImpl.create(var1);

         try {
            var2.rollback(var3);
            JMSLogger.logAdminForceRollback(this.name, var1);
            return null;
         } catch (XAException var5) {
            JMSLogger.logAdminForceRollbackError(this.name, var1, var5);
            throw new ManagementException("Error on forceRollback of JMS transaction branch " + var1 + ". ", var5);
         }
      }
   }

   private final void suspendMessageLogging() {
      BEDestinationImpl[] var1 = this.getBEDestinations();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         BEDestinationImpl var3 = var1[var2];
         if (var3.isMessageLoggingEnabled()) {
            try {
               var3.suspendMessageLogging();
            } catch (JMSException var5) {
            }
         }
      }

   }

   private final void resumeMessageLogging() {
      BEDestinationImpl[] var1 = this.getBEDestinations();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         BEDestinationImpl var3 = var1[var2];
         if (var3.isMessageLoggingEnabled()) {
            try {
               var3.resumeMessageLogging();
            } catch (JMSException var5) {
            }
         }
      }

   }

   public final boolean isMemoryLow() {
      return this.isMemoryLow;
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("JMSServer");
      var2.writeAttribute("name", this.name != null ? this.name : "");
      var2.writeAttribute("id", this.backEndId != null ? this.backEndId.toString() : "");
      String var3;
      if (this.backEndQuota == null || (var3 = this.backEndQuota.getName()) == null) {
         var3 = "";
      }

      var2.writeAttribute("quotaName", var3);
      var2.writeAttribute("destinationsHighCount", String.valueOf(this.destinationsHighCount));
      var2.writeAttribute("destinationsTotalCount", String.valueOf(this.destinationsTotalCount));
      var2.writeAttribute("serverSessionPoolsCurrentCount", String.valueOf(this.serverSessionPoolsCurrentCount));
      var2.writeAttribute("serverSessionPoolsHighCount", String.valueOf(this.serverSessionPoolsHighCount));
      var2.writeAttribute("serverSessionPoolsTotalCount", String.valueOf(this.serverSessionPoolsTotalCount));
      var2.writeStartElement("Health");
      HealthState var4 = this.getHealthState();
      JMSDiagnosticImageSource.dumpHealthStateElement(var2, var4);
      var2.writeEndElement();
      var2.writeStartElement("Destinations");
      HashMap var5 = (HashMap)this.name2Destination.clone();
      var2.writeAttribute("currentCount", String.valueOf(var5.size()));
      Iterator var6 = var5.values().iterator();

      while(var6.hasNext()) {
         BEDestinationImpl var7 = (BEDestinationImpl)var6.next();
         var7.dump(var1, var2);
      }

      var2.writeEndElement();
      var2.writeStartElement("DurableSubscribers");
      HashMap var10 = (HashMap)this.durableSubscribers.clone();
      var2.writeAttribute("currentCount", String.valueOf(var10.size()));
      var6 = var10.values().iterator();

      while(var6.hasNext()) {
         DurableSubscription var8 = (DurableSubscription)var6.next();
         if (var8.getConsumer() != null) {
            var8.getConsumer().dumpRef(var1, var2);
         }
      }

      var2.writeEndElement();
      var2.writeStartElement("ServerSessionPools");
      BEServerSessionPool[] var11 = this.getSessionPools();
      if (var11 != null && var11.length != 0) {
         var2.writeAttribute("currentCount", String.valueOf(var11.length));

         for(int var9 = 0; var9 < var11.length; ++var9) {
            var11[var9].dump(var1, var2);
         }
      } else {
         var2.writeAttribute("currentCount", "0");
      }

      var2.writeEndElement();
      ((KernelImpl)this.kernel).dump(var1, var2);
      var2.writeEndElement();
   }

   public void setHostingTemporaryDestinations(boolean var1) {
      this.isHostingTemporaryDestinations = var1;
   }

   public void setTemporaryTemplateResource(String var1) {
      if (var1 == null) {
         this.temporaryTemplateResource = null;
      } else {
         this.temporaryTemplateResource = new ModuleName(var1, (String)null);
      }

      if (this.temporaryModule != null) {
         this.temporaryModule.setAuxiliaryModuleName(this.temporaryTemplateResource);
      }

   }

   public void setTemporaryTemplateName(String var1) {
      this.temporaryTemplateName = var1;
   }

   public void activateFinished() throws BeanUpdateFailedException {
      if (this.isHostingTemporaryDestinations) {
         if (this.temporaryTemplateResource != null) {
            DomainMBean var1 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain();
            Object var2 = null;
            if (this.temporaryTemplateResource.toString().equals("interop-jms")) {
               JMSInteropModuleMBean[] var3 = var1.getJMSInteropModules();
               if (var3.length > 0) {
                  var2 = var3[0];
               }
            }

            if (var2 == null) {
               var2 = var1.lookupJMSSystemResource(this.temporaryTemplateResource.toString());
            }

            JMSBean var7 = ((JMSSystemResourceMBean)var2).getJMSResource();
            TemplateBean var4 = var7.lookupTemplate(this.temporaryTemplateName);
            if (this.temporaryModule != null) {
               this.temporaryModule.setTemplate(var4);
            } else {
               this.temporaryModule = new TemporaryModule(this.name, var4, this.temporaryTemplateResource);
            }
         } else if (this.temporaryModule != null) {
            this.temporaryModule.setTemplate((TemplateBean)null);
         } else {
            this.temporaryModule = new TemporaryModule(this.name);
         }

         if ((this.state & 16) == 0 && this.tempDestinationFactory == null) {
            try {
               this.tempDestinationFactory = new BackEndTempDestinationFactory(this);
               JMSService.getJMSService().getBEDeployer().addTempDestinationFactory(this.tempDestinationFactory);
            } catch (NamingException var6) {
               throw new BeanUpdateFailedException(var6.toString());
            }
         }
      } else {
         if (this.temporaryModule != null) {
            this.temporaryModule.setTemplate((TemplateBean)null);
         }

         if (this.tempDestinationFactory != null) {
            try {
               JMSService.getJMSService().getBEDeployer().removeTempDestinationFactory(this.tempDestinationFactory);
               this.tempDestinationFactory = null;
            } catch (NamingException var5) {
               throw new BeanUpdateFailedException(var5.toString());
            }
         }
      }

   }

   public String getFullSAFDestinationName(String var1) {
      return var1 + "@" + this.name;
   }

   public void setThresholdHandler(ThresholdHandler var1) {
      this.thresholdHandler = var1;
   }

   public void setHealthFailed(Exception var1) {
      this.backEndHealthException = var1;
      HealthMonitorService.subsystemFailed("JMSServer." + this.getName(), var1.toString());
   }

   private static class TemplateListener implements BeanUpdateListener {
      private String backEndName;
      private String tempDestinationName;
      private JMSBean temporaryModule;
      private TemplateBean listenToMe;
      private boolean isQueue;
      private boolean updateInProgress;

      private TemplateListener(String var1, String var2, JMSBean var3, TemplateBean var4, boolean var5) {
         this.updateInProgress = false;
         this.backEndName = var1;
         this.tempDestinationName = var2;
         this.temporaryModule = var3;
         this.listenToMe = var4;
         this.isQueue = var5;
         DescriptorBean var6 = (DescriptorBean)this.listenToMe;
         var6.addBeanUpdateListener(this);
         var6 = (DescriptorBean)this.listenToMe.getThresholds();
         var6.addBeanUpdateListener(this);
         var6 = (DescriptorBean)this.listenToMe.getDeliveryParamsOverrides();
         var6.addBeanUpdateListener(this);
         var6 = (DescriptorBean)this.listenToMe.getDeliveryFailureParams();
         var6.addBeanUpdateListener(this);
         var6 = (DescriptorBean)this.listenToMe.getMulticast();
         var6.addBeanUpdateListener(this);
         var6 = (DescriptorBean)this.listenToMe.getMessageLoggingParams();
         var6.addBeanUpdateListener(this);
      }

      private JMSBean getWholeModuleClone(JMSBean var1) {
         DescriptorBean var2 = (DescriptorBean)var1;
         return (JMSBean)((Descriptor)var2.getDescriptor().clone()).getRootBean();
      }

      public synchronized void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
         if (!this.updateInProgress) {
            this.updateInProgress = true;
            synchronized(this.temporaryModule) {
               DescriptorBean var3 = var1.getProposedBean();
               if (var3 == null) {
                  throw new BeanUpdateRejectedException("ERROR: proposed bean was null in update of temporary template for " + this.backEndName);
               } else {
                  if (!(var3 instanceof TemplateBean)) {
                     var3 = var3.getParentBean();
                  }

                  if (var3 != null && var3 instanceof TemplateBean) {
                     TemplateBean var4 = (TemplateBean)var3;
                     DescriptorBean var5 = (DescriptorBean)this.temporaryModule;
                     JMSBean var6 = this.getWholeModuleClone(this.temporaryModule);
                     TemplateBean var7 = var6.lookupTemplate(this.listenToMe.getName());
                     var6.destroyTemplate(var7);
                     var7 = var6.createTemplate(this.listenToMe.getName());
                     Object var8 = this.isQueue ? var6.lookupQueue(this.tempDestinationName) : var6.lookupTopic(this.tempDestinationName);
                     ((DestinationBean)var8).setTemplate(var7);
                     QuotaBean var9;
                     String var10;
                     String var12;
                     if (var4.isSet("Quota")) {
                        var9 = this.listenToMe.getQuota();
                        var10 = var9 == null ? "" : var9.getName();
                        QuotaBean var11 = var4.getQuota();
                        var12 = var11 == null ? "" : var11.getName();
                        if (!var10.equals(var12)) {
                           QuotaBean var13 = var6.lookupQuota(var10);
                           if (var13 != null) {
                              var6.destroyQuota(var13);
                           }

                           if (var11 != null) {
                              var6.createQuota(var12);
                           }
                        }
                     } else {
                        var9 = this.listenToMe.getQuota();
                        if (var9 != null) {
                           QuotaBean var19 = var6.lookupQuota(var9.getName());
                           if (var19 != null) {
                              var6.destroyQuota(var19);
                           }
                        }
                     }

                     DestinationBean var18;
                     if (var4.getDeliveryFailureParams().isSet("ErrorDestination")) {
                        var18 = this.listenToMe.getDeliveryFailureParams().getErrorDestination();
                        var10 = var18 == null ? "" : var18.getName();
                        DestinationBean var22 = var4.getDeliveryFailureParams().getErrorDestination();
                        var12 = var22 == null ? "" : var22.getName();
                        if (!var10.equals(var12)) {
                           if (var18 != null) {
                              DestinationBean var24 = JMSModuleHelper.findDestinationBean(var10, var6);
                              if (var24 != null) {
                                 JMSBeanHelper.destroyDestination(var6, var24);
                              }
                           }

                           if (var22 != null) {
                              if (var22 instanceof QueueBean) {
                                 var6.createQueue(var12);
                              } else {
                                 var6.createTopic(var12);
                              }
                           }
                        }
                     } else {
                        var18 = this.listenToMe.getDeliveryFailureParams().getErrorDestination();
                        if (var18 != null) {
                           DestinationBean var21 = JMSModuleHelper.findDestinationBean(var18.getName(), var6);
                           if (var21 != null) {
                              JMSBeanHelper.destroyDestination(var6, var21);
                           }
                        }
                     }

                     try {
                        JMSBeanHelper.copyTemplateBean(var7, var6, var4);
                     } catch (ManagementException var16) {
                        throw new BeanUpdateRejectedException("ERROR: Could not copy the proposed template bean in update of temporary template for " + this.backEndName, var16);
                     }

                     Descriptor var20 = var5.getDescriptor();
                     Descriptor var23 = ((DescriptorBean)var6).getDescriptor();

                     try {
                        var20.prepareUpdate(var23);
                     } catch (DescriptorUpdateRejectedException var15) {
                        throw new BeanUpdateRejectedException("ERROR: Could not prepare the temporary destination module of " + this.backEndName, var15);
                     }

                  } else {
                     throw new BeanUpdateRejectedException("ERROR: normalized proposed bean was null in update of temporary template for " + this.backEndName);
                  }
               }
            }
         }
      }

      public synchronized void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
         if (this.updateInProgress) {
            this.updateInProgress = false;
            synchronized(this.temporaryModule) {
               Descriptor var3 = ((DescriptorBean)this.temporaryModule).getDescriptor();

               try {
                  var3.activateUpdate();
               } catch (DescriptorUpdateFailedException var6) {
                  throw new BeanUpdateFailedException(var6.getMessage(), var6);
               }

            }
         }
      }

      public synchronized void rollbackUpdate(BeanUpdateEvent var1) {
         if (this.updateInProgress) {
            this.updateInProgress = false;
            synchronized(this.temporaryModule) {
               Descriptor var3 = ((DescriptorBean)this.temporaryModule).getDescriptor();
               var3.rollbackUpdate();
            }
         }
      }

      private void close() {
         DescriptorBean var1 = (DescriptorBean)this.listenToMe;
         var1.removeBeanUpdateListener(this);
         var1 = (DescriptorBean)this.listenToMe.getThresholds();
         var1.removeBeanUpdateListener(this);
         var1 = (DescriptorBean)this.listenToMe.getDeliveryParamsOverrides();
         var1.removeBeanUpdateListener(this);
         var1 = (DescriptorBean)this.listenToMe.getDeliveryFailureParams();
         var1.removeBeanUpdateListener(this);
         var1 = (DescriptorBean)this.listenToMe.getMulticast();
         var1.removeBeanUpdateListener(this);
         var1 = (DescriptorBean)this.listenToMe.getMessageLoggingParams();
         var1.removeBeanUpdateListener(this);
      }

      // $FF: synthetic method
      TemplateListener(String var1, String var2, JMSBean var3, TemplateBean var4, boolean var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }
   }

   private static class BeansAndSauce {
      private JMSBean bean;
      private JMSModuleManagedEntity sauce;
      private TemplateListener listener;
      private boolean isQueue;

      private BeansAndSauce(JMSBean var1, JMSModuleManagedEntity var2, boolean var3) {
         this.bean = var1;
         this.sauce = var2;
         this.isQueue = var3;
      }

      private JMSBean getBean() {
         return this.bean;
      }

      private JMSModuleManagedEntity getSauce() {
         return this.sauce;
      }

      private void setListener(TemplateListener var1) {
         this.listener = var1;
      }

      private TemplateListener getListener() {
         return this.listener;
      }

      private boolean isQueue() {
         return this.isQueue;
      }

      // $FF: synthetic method
      BeansAndSauce(JMSBean var1, JMSModuleManagedEntity var2, boolean var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static class TemporaryModule {
      private static final DestinationEntityProvider DESTINATION_PROVIDER = new DestinationEntityProvider();
      private String backEndName;
      private TemplateBean template;
      private ModuleName auxiliaryModuleName;
      private HashMap entityMap;
      private String proposedTemporaryDestinationName;

      private TemporaryModule(String var1, TemplateBean var2, ModuleName var3) {
         this.entityMap = new HashMap();
         this.backEndName = var1;
         this.template = var2;
         this.auxiliaryModuleName = var3;
      }

      private TemporaryModule(String var1) {
         this(var1, (TemplateBean)null, new ModuleName(var1, (String)null));
      }

      private void setTemplate(TemplateBean var1) {
         this.template = var1;
      }

      private void setAuxiliaryModuleName(ModuleName var1) {
         this.auxiliaryModuleName = var1;
      }

      private void close() {
         Iterator var1 = this.entityMap.keySet().iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            BeansAndSauce var3 = (BeansAndSauce)this.entityMap.get(var2);
            TemplateListener var4 = var3.getListener();
            if (var4 != null) {
               var4.close();
            }
         }

         this.entityMap.clear();
      }

      private EntityName prepareCreateTemporaryDestination(boolean var1, int var2) throws weblogic.jms.common.JMSException {
         JMSBean var3 = (JMSBean)(new DescriptorManager()).createDescriptorRoot(JMSBean.class).getRootBean();
         TemplateBean var4 = null;
         if (this.template != null) {
            var4 = var3.createTemplate(this.template.getName());
            QuotaBean var5 = this.template.getQuota();
            if (var5 != null) {
               var3.createQuota(var5.getName());
            }

            DestinationBean var6 = this.template.getDeliveryFailureParams().getErrorDestination();
            if (var6 != null) {
               if (var6 instanceof QueueBean) {
                  var3.createQueue(var6.getName());
               } else {
                  var3.createTopic(var6.getName());
               }
            }

            String[] var7 = this.template.getDestinationKeys();
            if (var7.length > 0) {
               DomainMBean var8 = ManagementService.getRuntimeAccess(BackEnd.KERNEL_ID).getDomain();
               Object var9 = null;
               if (this.auxiliaryModuleName.toString().equals("interop-jms")) {
                  JMSInteropModuleMBean[] var10 = var8.getJMSInteropModules();
                  if (var10.length > 0) {
                     var9 = var10[0];
                  }
               }

               if (var9 == null) {
                  var9 = var8.lookupJMSSystemResource(this.auxiliaryModuleName.toString());
               }

               JMSBean var20 = ((JMSSystemResourceMBean)var9).getJMSResource();

               for(int var11 = 0; var11 < var7.length; ++var11) {
                  try {
                     JMSBeanHelper.copyDestinationKeyBean(var3, var20, var7[var11]);
                  } catch (ManagementException var15) {
                     throw new weblogic.jms.common.JMSException(var15);
                  }
               }
            }

            try {
               JMSBeanHelper.copyTemplateBean(var4, var3, this.template);
            } catch (ManagementException var14) {
               throw new weblogic.jms.common.JMSException(var14);
            }
         }

         Object var16;
         if (var1) {
            this.proposedTemporaryDestinationName = this.backEndName + ".TemporaryQueue" + var2;
            QueueBean var17 = var3.createQueue(this.proposedTemporaryDestinationName);
            var16 = var17;
         } else {
            this.proposedTemporaryDestinationName = this.backEndName + ".TemporaryTopic" + var2;
            TopicBean var18 = var3.createTopic(this.proposedTemporaryDestinationName);
            var16 = var18;
         }

         if (var4 != null) {
            ((DestinationBean)var16).setTemplate(var4);
         }

         ((DestinationBean)var16).getDeliveryParamsOverrides().setDeliveryMode("Non-Persistent");

         try {
            JMSModuleManagedEntity var19 = DESTINATION_PROVIDER.createTemporaryEntity(var3, (NamedEntityBean)var16, this.backEndName, this.auxiliaryModuleName);
            var19.prepare();
            this.entityMap.put(JMSBeanHelper.getDecoratedName(this.backEndName, this.proposedTemporaryDestinationName), new BeansAndSauce(var3, var19, var1));
         } catch (ModuleException var13) {
            throw new weblogic.jms.common.JMSException(var13);
         }

         return new EntityName(this.backEndName, (String)null, this.proposedTemporaryDestinationName);
      }

      private void rollbackCreateTemporaryDestination() throws weblogic.jms.common.JMSException {
         weblogic.jms.common.JMSException var1 = null;
         String var2 = JMSBeanHelper.getDecoratedName(this.backEndName, this.proposedTemporaryDestinationName);
         this.proposedTemporaryDestinationName = null;
         BeansAndSauce var3 = (BeansAndSauce)this.entityMap.remove(var2);
         JMSModuleManagedEntity var4 = var3.getSauce();

         try {
            var4.unprepare();
            var4.destroy();
            var4.remove();
         } catch (ModuleException var6) {
            var1 = new weblogic.jms.common.JMSException(var6);
         }

         if (var1 != null) {
            throw var1;
         }
      }

      private void activateCreateTemporaryDestination() throws weblogic.jms.common.JMSException {
         String var1 = JMSBeanHelper.getDecoratedName(this.backEndName, this.proposedTemporaryDestinationName);
         String var2 = this.proposedTemporaryDestinationName;
         this.proposedTemporaryDestinationName = null;
         BeansAndSauce var3 = (BeansAndSauce)this.entityMap.get(var1);
         JMSModuleManagedEntity var4 = var3.getSauce();
         JMSBean var5 = var3.getBean();

         try {
            var4.activate(var5);
         } catch (ModuleException var7) {
            throw new weblogic.jms.common.JMSException(var7);
         }

         if (this.template != null) {
            var3.setListener(new TemplateListener(this.backEndName, var2, var5, this.template, var3.isQueue()));
         }

      }

      private void removeTemporaryDestination(String var1) throws weblogic.jms.common.JMSException {
         weblogic.jms.common.JMSException var2 = null;
         BeansAndSauce var3 = (BeansAndSauce)this.entityMap.remove(var1);
         if (var3 != null) {
            TemplateListener var4 = var3.getListener();
            if (var4 != null) {
               var4.close();
            }

            JMSModuleManagedEntity var5 = var3.getSauce();

            try {
               var5.deactivate();
            } catch (ModuleException var7) {
               var2 = new weblogic.jms.common.JMSException(var7);
            }

            try {
               var5.unprepare();
            } catch (ModuleException var10) {
               if (var2 != null) {
                  var2.printStackTrace();
               }

               var2 = new weblogic.jms.common.JMSException(var10);
            }

            try {
               var5.destroy();
            } catch (ModuleException var9) {
               if (var2 != null) {
                  var2.printStackTrace();
               }

               var2 = new weblogic.jms.common.JMSException(var9);
            }

            try {
               var5.remove();
            } catch (ModuleException var8) {
               if (var2 != null) {
                  var2.printStackTrace();
               }

               var2 = new weblogic.jms.common.JMSException(var8);
            }

            if (var2 != null) {
               throw var2;
            }
         }
      }

      // $FF: synthetic method
      TemporaryModule(String var1, TemplateBean var2, ModuleName var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      TemporaryModule(String var1, Object var2) {
         this(var1);
      }
   }

   private class ObjectHandlerTestStoreException implements ObjectHandler, TestStoreException {
      ObjectHandler delegate;

      public ObjectHandlerTestStoreException(ObjectHandler var2) {
         assert ObjectMessageImpl.isTestStoreExceptionEnabled() : "system property for store debug required";

         this.delegate = var2;
      }

      public Object readObject(ObjectInput var1) throws ClassNotFoundException, IOException {
         return this.delegate.readObject(var1);
      }

      public void writeObject(ObjectOutput var1, Object var2) throws IOException {
         this.delegate.writeObject(var1, var2);
      }

      public PersistentStoreException getTestException() {
         return null;
      }
   }
}
