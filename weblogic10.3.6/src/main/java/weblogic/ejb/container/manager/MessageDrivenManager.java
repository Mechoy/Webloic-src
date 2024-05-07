package weblogic.ejb.container.manager;

import com.oracle.pitchfork.interfaces.inject.LifecycleEvent;
import com.oracle.pitchfork.interfaces.intercept.__ProxyControl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import javax.ejb.EJBContext;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.naming.Context;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;
import weblogic.application.ApplicationAccess;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.cluster.migration.Migratable;
import weblogic.cluster.migration.MigratableMDB;
import weblogic.cluster.migration.MigrationException;
import weblogic.cluster.migration.MigrationManager;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.dd.DDConstants;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenManagerIntf;
import weblogic.ejb.container.interfaces.PoolIntf;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.container.internal.JCABindingManager;
import weblogic.ejb.container.internal.JMSConnectionPoller;
import weblogic.ejb.container.internal.MDConnectionManager;
import weblogic.ejb.container.internal.MessageDrivenEJBContextImpl;
import weblogic.ejb.container.internal.MessageDrivenLocalObject;
import weblogic.ejb.container.monitoring.MessageDrivenEJBRuntimeMBeanImpl;
import weblogic.ejb.container.pool.MessageDrivenPool;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.jms.extensions.DestinationDetail;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.EJBRuntimeMBean;
import weblogic.management.runtime.EJBTimerRuntimeMBean;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class MessageDrivenManager extends BaseEJBManager implements BeanManager, MessageDrivenManagerIntf, Migratable, MigratableMDB {
   private Method createMethod;
   private PoolIntf pool = null;
   private MessageDrivenBeanInfo info = null;
   private MessageDrivenEJBRuntimeMBeanImpl runtimeMBean;
   private Class mdoClass;
   private DestinationDetail destinationInfo;
   private MessageDrivenContext messageDrivenContext;
   private static final DebugCategory DEBUG_APP_VERSION;
   private boolean started = false;
   private String destinationName = null;
   private String jmsClientID = "";
   private String uniqueGlobalID = "";
   private String messageSelector;
   private boolean isDurableSubscriptionDeletion;
   private int topicMessagesDistributionMode;
   private String domainName;
   private String currServerName;
   private String jmsClientIDBase;
   private String connectionFacortyJNDIName;
   private String providerURL;
   private String resourceAdapterJndiName;
   private TargetMBean targetMBean = null;
   private MDConnectionManager mdConnManager = null;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = -1423676407120807163L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.manager.MessageDrivenManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   public MessageDrivenManager(EJBComponentRuntimeMBeanImpl var1) {
      super(var1);
   }

   public String toString() {
      return "[MessageDrivenManager] home: " + this.ejbHome + "pool: " + this.pool + " debug: " + debugLogger.isDebugEnabled();
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4) throws WLDeploymentException {
      this.info = (MessageDrivenBeanInfo)var3;
      super.setup(var1, var2, var3, var4);
      this.resourceAdapterJndiName = this.info.getResourceAdapterJndiName();
      if (var3.isEJB30() && !this.info.getMessagingTypeInterfaceClass().isAssignableFrom(var3.getBeanClass())) {
         this.beanClass = ((MessageDrivenBeanInfo)var3).getGeneratedBeanClass();
      } else {
         this.beanClass = var3.getBeanClass();
      }

      this.mdoClass = this.info.getMessageDrivenLocalObjectClass();
      this.setCreateMethod();
      this.suspendActiveVersion();
      this.initialize();
      this.messageDrivenContext = new MessageDrivenEJBContextImpl(this);
      if (debugLogger.isDebugEnabled()) {
         debug("In setup for :" + this);
      }

   }

   protected void createEJBTimerManager() {
      if (this.info.getTimerManagerFactory() != null) {
         this.timerManager = this.info.getTimerManagerFactory().createEJBTimerManager(this);
      }

   }

   public void setup(MessageDrivenBeanInfo var1, Context var2, String var3, String var4, String var5, String var6, TargetMBean var7, DestinationDetail var8) throws WLDeploymentException {
      this.destinationName = var3;
      this.targetMBean = var7;
      this.domainName = var4;
      this.currServerName = var5;
      this.jmsClientIDBase = var6;
      this.destinationInfo = var8;
      this.info = var1;
      this.setup((BaseEJBRemoteHomeIntf)null, (BaseEJBLocalHomeIntf)null, var1, var2);
   }

   public MessageDrivenContext getMessageDrivenContext() {
      return this.messageDrivenContext;
   }

   public PoolIntf getPool() {
      return this.pool;
   }

   public Method getCreateMethod() {
      return this.createMethod;
   }

   public EnterpriseBean preInvoke(InvocationWrapper var1) throws InternalException {
      boolean var11 = false;

      EnterpriseBean var10000;
      try {
         var11 = true;
         super.preInvoke();
         int var2 = var1.getMethodDescriptor().getTxTimeoutMS();
         if (debugLogger.isDebugEnabled()) {
            debug("In preInvoke with timeout:" + var2 + " on manager: " + this);
         }

         EnterpriseBean var3 = this.pool.getBean((long)var2);
         var10000 = var3;
         var11 = false;
      } finally {
         if (var11) {
            Object var10001 = null;
            if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium.isEnabledAndNotDyeFiltered()) {
               DelegatingMonitor var10003 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium;
               InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10003, var10003.getActions());
            }

         }
      }

      if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium.isEnabledAndNotDyeFiltered()) {
         DelegatingMonitor var10002 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium;
         InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10002, var10002.getActions());
      }

      return var10000;
   }

   public void postInvoke(InvocationWrapper var1) throws InternalException {
      if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium.isEnabledAndNotDyeFiltered()) {
         Object[] var3 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium.isArgumentsCaptureNeeded()) {
            var3 = new Object[]{this, var1};
         }

         DynamicJoinPoint var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var3, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      if (debugLogger.isDebugEnabled()) {
         debug("In postInvoke on " + this);
      }

      if (var1.isLocal()) {
         this.pool.releaseBean(var1.getBean());
      }

   }

   public EnterpriseBean preHomeInvoke(InvocationWrapper var1) throws InternalException {
      throw new AssertionError("message driven beans cannot have home methods");
   }

   public void postHomeInvoke(InvocationWrapper var1) throws InternalException {
      throw new AssertionError("message driven beans cannot have home methods");
   }

   public void destroyPooledInstance(InvocationWrapper var1, Throwable var2) throws InternalException {
      throw new AssertionError("message driven beans cannot have home methods");
   }

   public void destroyInstance(InvocationWrapper var1, Throwable var2) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("In destroyInstance for manager: " + this);
      }

      this.pool.destroyBean((EnterpriseBean)null);
   }

   public void beforeCompletion(InvocationWrapper var1) throws InternalException {
   }

   public void beforeCompletion(Object var1) throws InternalException {
   }

   public void beforeCompletion(Collection var1, Transaction var2) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("In beforeCompletion for manager: " + this);
      }

   }

   public void afterCompletion(InvocationWrapper var1) {
   }

   public void afterCompletion(Object var1) {
   }

   public void afterCompletion(Collection var1, Transaction var2, int var3, Object var4) {
      if (debugLogger.isDebugEnabled()) {
         debug("In afterCompletion for manager: " + this);
      }

   }

   public EJBContext allocateContext(EnterpriseBean var1, Object var2) {
      throw new AssertionError("message driven beans cannot have a key");
   }

   public EJBContext allocateContext(EnterpriseBean var1, EJBObject var2, EJBLocalObject var3) {
      throw new AssertionError("message driven beans cannot have a eo");
   }

   public MessageDrivenBean createBean() throws InternalException {
      MessageDrivenBean var1 = null;
      if (null == this.createMethod) {
         this.setCreateMethod();
      }

      try {
         var1 = (MessageDrivenBean)this.allocateBean();
         var1.setMessageDrivenContext(this.messageDrivenContext);
         ClassLoader var2 = this.beanInfo.getModuleClassLoader();
         Thread var3 = Thread.currentThread();
         ClassLoader var4 = var3.getContextClassLoader();
         var3.setContextClassLoader(var2);

         try {
            if (this.beanInfo.isEJB30() && var1 instanceof __ProxyControl) {
               ((__ProxyControl)var1).invokeLifecycleMethod(LifecycleEvent.POST_CONSTRUCT);
            }

            if (null != this.createMethod) {
               this.createMethod.invoke(var1, (Object[])null);
            }
         } finally {
            if (var4 != null) {
               var3.setContextClassLoader(var4);
            }

         }
      } catch (IllegalAccessException var11) {
         EJBLogger.logStackTrace(var11);
         EJBRuntimeUtils.throwInternalException("Error in the bean's constructor : ", var11);
      } catch (InvocationTargetException var12) {
         EJBLogger.logStackTrace(var12);
         EJBRuntimeUtils.throwInternalException("Error in the bean's constructor : ", var12);
      }

      return var1;
   }

   public MessageDrivenLocalObject allocateMDO(XAResource var1) {
      try {
         MessageDrivenLocalObject var2 = (MessageDrivenLocalObject)this.mdoClass.newInstance();
         var2.setBeanManager(this);
         var2.setBeanInfo(this.beanInfo);
         if (var1 != null) {
            var2.setXAResource(var1);
         }

         return var2;
      } catch (Exception var4) {
         throw new AssertionError(var4);
      }
   }

   public EJBObject remoteCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      throw new AssertionError("message driven beans cannot allocateEO");
   }

   public EJBLocalObject localCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      throw new AssertionError("message driven beans cannot allocateEO");
   }

   public void remove(InvocationWrapper var1) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("In remove for manager: " + this);
      }

   }

   public EJBObject remoteFindByPrimaryKey(InvocationWrapper var1, Object var2) throws InternalException {
      throw new AssertionError("Finds do not apply to message driven beans");
   }

   public Object localFindByPrimaryKey(InvocationWrapper var1, Object var2) throws InternalException {
      throw new AssertionError("Finds do not apply to message driven beans");
   }

   public EJBObject remoteScalarFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      throw new AssertionError("Finds do not apply to message driven beans");
   }

   public EJBLocalObject localScalarFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      throw new AssertionError("Finds do not apply to message driven beans");
   }

   public Enumeration enumFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      throw new AssertionError("Finds do not apply to message driven beans");
   }

   public Collection collectionFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      throw new AssertionError("Finds do not apply to message driven beans");
   }

   public void beanImplClassChangeNotification() {
      if (this.info.isEJB30() && !this.info.getMessagingTypeInterfaceClass().isAssignableFrom(this.info.getBeanClass())) {
         this.beanClass = this.info.getGeneratedBeanClass();
      } else {
         this.beanClass = this.info.getBeanClass();
      }

      this.pool.reset();
   }

   public void releaseBean(InvocationWrapper var1) {
      EnterpriseBean var2 = var1.getBean();
      this.pool.releaseBean(var2);
   }

   private void setCreateMethod() {
      if (!this.info.isEJB30()) {
         try {
            this.createMethod = this.beanClass.getMethod("ejbCreate", (Class[])null);
         } catch (Exception var2) {
            if (!this.info.isEJB30()) {
               throw new AssertionError("Could not find ejbCreate()", var2);
            }
         }

      }
   }

   public synchronized void updateImplCL() {
      if (this.info.isEJB30() && !this.info.getMessagingTypeInterfaceClass().isAssignableFrom(this.info.getBeanClass())) {
         this.beanClass = this.info.getGeneratedBeanClass();
      } else {
         this.beanClass = this.info.getBeanClass();
      }

      this.setCreateMethod();
   }

   public void reInitializeCacheAndPool() {
      this.reInitializePool();
   }

   public void reInitializePool() {
      this.pool.reInitializePool();
   }

   public String getDestinationName() {
      return this.destinationName;
   }

   public String getDDMemberName() {
      return this.destinationInfo == null ? this.destinationName : this.destinationInfo.getMemberConfigName();
   }

   public String getJMSClientID() {
      return this.jmsClientID;
   }

   public String getUniqueGlobalID() {
      return this.uniqueGlobalID;
   }

   public String getMessageSelector() {
      return this.messageSelector;
   }

   public boolean isDurableSubscriptionDeletion() {
      return this.isDurableSubscriptionDeletion;
   }

   public int getTopicMessagesDistributionMode() {
      return this.topicMessagesDistributionMode;
   }

   public TargetMBean getTargetMBean() {
      return this.targetMBean;
   }

   public String getName() {
      return this.info.getEJBName();
   }

   private String getRuntimeMBeanName() {
      if (this.info.getIsWeblogicJMS()) {
         return this.isNoneDDMD() ? this.info.getEJBName() + "_" + this.destinationName : this.info.getEJBName() + "_" + this.getDDMemberName();
      } else {
         return this.info.getEJBName();
      }
   }

   private void initialize() throws WLDeploymentException {
      this.resetConnValues();
      if (this.runtimeMBean == null) {
         String var1 = this.getRuntimeMBeanName();

         try {
            this.runtimeMBean = new MessageDrivenEJBRuntimeMBeanImpl(var1, this.info.getEJBName(), this.getEJBComponentRuntime(), this.destinationName, this.getTimerManager());
            this.setEJBRuntimeMBean(this.runtimeMBean);
            this.addEJBRuntimeMBean(this.runtimeMBean);
         } catch (ManagementException var4) {
            Loggable var3 = EJBLogger.logFailedToCreateRuntimeMBeanLoggable(var4);
            throw new WLDeploymentException(var3.getMessage(), var4);
         }
      }

      this.perhapsSetupTimerManager(this.runtimeMBean.getTimerRuntime());
      this.pool = new MessageDrivenPool(this, this.info, this.runtimeMBean.getPoolRuntime());
      if (this.info.getIsWeblogicJMS()) {
         this.mdConnManager = new JMSConnectionPoller(this.info, this.environmentContext, this.runtimeMBean);
      } else {
         this.mdConnManager = new JCABindingManager(this.info, this.environmentContext, this.runtimeMBean);
      }

   }

   private void unInitialize() {
      try {
         if (this.runtimeMBean != null) {
            this.runtimeMBean.unregister();
            this.removeEJBRuntimeMBean(this.runtimeMBean);
         }
      } catch (ManagementException var2) {
         if (debugLogger.isDebugEnabled()) {
            debug("Couldn't unregister MBean: ", var2);
         }
      }

      this.runtimeMBean = null;
      this.pool = null;
   }

   private boolean runtimeValidation() {
      if (this.runtimeMBean.getMDBStatus() != null && this.runtimeMBean.getMDBStatus().equals(DDConstants.MDBStatus[0])) {
         this.runtimeMBean.setLastException(new WLDeploymentException("The ejb " + this.info.getDisplayName() + " could not be activated because the destionation " + this.destinationName + " is not available now!"));
         return false;
      } else if (this.destinationInfo == null) {
         this.runtimeMBean.setMDBStatus(DDConstants.MDBStatus[4]);
         this.runtimeMBean.setLastException(new AssertionError("Illegal status for MDB " + this.info.getDisplayName()));
         return false;
      } else {
         if (this.destinationInfo.getType() == 6) {
            if (this.topicMessagesDistributionMode == 0) {
               this.runtimeMBean.setMDBStatus(DDConstants.MDBStatus[4]);
               String var1 = EJBLogger.logIllegalPermutationOnPDTAndComp(this.info.getDisplayName());
               this.runtimeMBean.setLastException(new WLDeploymentException(var1));
               return false;
            }
         } else if ((this.destinationInfo.getType() == 4 || this.destinationInfo.getType() == 2 || this.destinationInfo.getType() == 0) && this.topicMessagesDistributionMode > 0) {
            this.runtimeMBean.setMDBStatus(DDConstants.MDBStatus[4] + ":" + EJBLogger.logInvalidConfigurationForTopicMessagesDistributionModeLoggable(this.info.getDisplayName()).getMessage());
            return false;
         }

         return true;
      }
   }

   public synchronized void start() throws WLDeploymentException {
      if (!this.info.getIsWeblogicJMS() && this.info.getIsInactive()) {
         this.setMDBStatus(DDConstants.MDBStatus[5]);
      } else if (this.info.getIsWeblogicJMS() && !this.runtimeValidation()) {
         this.runtimeMBean.setJMSConnectionAlive(false);
      } else {
         if (!this.started) {
            this.pool.createInitialBeans();
            if (this.mdConnManager != null) {
               this.mdConnManager.startConnectionPolling();
               this.started = true;
            }
         }

      }
   }

   public void stop() {
      if (this.runtimeMBean != null && this.mdConnManager != null && this.pool != null) {
         this.mdConnManager.cancelConnectionPolling();
         this.pool.cleanup();
      }

   }

   public void undeploy() {
      super.undeploy();
      if (this.isNoneDDMD() && this.targetMBean instanceof MigratableTargetMBean) {
         try {
            MigrationManager.singleton().unregister(this, (MigratableTargetMBean)this.targetMBean);
         } catch (MigrationException var2) {
            EJBLogger.logErrorUndeploying(this.getDestinationName(), var2);
         }
      }

      this.info.getMDManagerList().remove(this);
      if (this.mdConnManager != null) {
         this.mdConnManager.cancelConnectionPolling();
      }

      if (this.pool != null) {
         this.pool.cleanup();
      }

      this.unInitialize();
   }

   protected void undeployTimerManager() {
      if (this.info.getTimerManagerFactory() != null) {
         this.info.getTimerManagerFactory().undeploy(this);
      }

   }

   public void remove() {
      if (this.mdConnManager != null) {
         this.mdConnManager.deleteDurableSubscriber();
      }

      this.mdConnManager = null;
   }

   public void onRAUndeploy() {
      if (this.mdConnManager != null) {
         ((JCABindingManager)this.mdConnManager).onRAUndeploy();
      }

   }

   public void updateJMSPollingIntervalSeconds(int var1) {
      this.mdConnManager.updateJMSPollingIntervalSeconds(var1);
   }

   public void resetMessageConsumer(boolean var1) {
      if (this.info.isDurableSubscriber() && var1 && var1) {
         this.mdConnManager.deleteDurableSubscriber();
      }

      this.resetConnValues();

      try {
         this.mdConnManager.startConnectionPolling();
      } catch (WLDeploymentException var3) {
         debug(": resetMessageConsumer failed to start connection polling", var3);
      }

   }

   public void migratableInitialize() throws MigrationException {
   }

   public void migratableActivate() throws MigrationException {
      try {
         this.start();
      } catch (WLDeploymentException var2) {
         throw new MigrationException(var2.getErrorMessage());
      }
   }

   public void migratableDeactivate() throws MigrationException {
      this.stop();
   }

   private static void debug(String var0) {
      debugLogger.debug("[MessageDrivenManager] " + var0);
   }

   private static void debug(String var0, Throwable var1) {
      debugLogger.debug("[MessageDrivenManager] " + var0, var1);
   }

   private void suspendActiveVersion() {
      debugLogger.debug("[MessageDrivenManager] suspendActiveVersion");
      String var1 = ApplicationAccess.getApplicationAccess().getCurrentApplicationName();
      String var2 = ApplicationVersionUtils.getVersionId(var1);
      if (var2 != null) {
         String var3 = ApplicationVersionUtils.getApplicationName(var1);
         ApplicationRuntimeMBean var4 = ApplicationVersionUtils.getActiveApplicationRuntime(var3);
         if (var4 != null) {
            String var5 = var4.getApplicationVersion();
            if (DEBUG_APP_VERSION.isEnabled()) {
               Debug.say("*** Active version of app: " + var4.getName() + var4.getApplicationVersion());
            }

            if (!this.isAppScopedJMSDestination()) {
               ComponentRuntimeMBean[] var6 = var4.getComponentRuntimes();
               if (var6 != null) {
                  for(int var7 = 0; var7 < var6.length; ++var7) {
                     if (this.isSameEJBComponent(var6[var7], var5, var2)) {
                        EJBRuntimeMBean[] var8 = ((EJBComponentRuntimeMBean)var6[var7]).getEJBRuntimes();
                        if (var8 == null) {
                           return;
                        }

                        for(int var9 = 0; var9 < var8.length; ++var9) {
                           if (this.isSameMDB(var8[var9], var5, var2)) {
                              MessageDrivenEJBRuntimeMBean var10 = (MessageDrivenEJBRuntimeMBean)var8[var9];
                              if (!this.info.isDestinationQueue() && !this.info.isDurableSubscriber()) {
                                 EJBLogger.logSuspendNonDurableSubscriber(this.info.getEJBName(), ApplicationVersionUtils.getDisplayName(var3, var5), this.info.getDestinationName(), var2);
                              } else {
                                 EJBLogger.logSuspendMDB(this.info.getEJBName(), ApplicationVersionUtils.getDisplayName(var3, var5), this.info.getDestinationName(), var2);
                              }

                              try {
                                 var10.suspend();
                              } catch (Exception var12) {
                                 var12.printStackTrace();
                              }
                           }
                        }
                     }
                  }

               }
            }
         }
      }
   }

   private boolean isAppScopedJMSDestination() {
      String var1 = this.info.getDestinationResourceLink();
      return var1 != null && var1.length() > 0;
   }

   private boolean isSameEJBComponent(ComponentRuntimeMBean var1, String var2, String var3) {
      if (!(var1 instanceof EJBComponentRuntimeMBean)) {
         return false;
      } else {
         EJBComponentRuntimeMBeanImpl var4 = this.getEJBComponentRuntime();
         if (DEBUG_APP_VERSION.isEnabled()) {
            Debug.say("*** isSameEJBComponent: " + var1.getName() + " " + var4.getName() + " " + ApplicationVersionUtils.isSameComponent(var1.getName(), var2, var4.getName(), var3));
         }

         return var4 == null ? false : ApplicationVersionUtils.isSameComponent(var1.getName(), var2, var4.getName(), var3);
      }
   }

   private boolean isSameMDB(EJBRuntimeMBean var1, String var2, String var3) {
      if (!(var1 instanceof MessageDrivenEJBRuntimeMBean)) {
         return false;
      } else {
         if (DEBUG_APP_VERSION.isEnabled()) {
            Debug.say("*** isSameMDB: " + var1.getName() + " " + this.getRuntimeMBeanName() + " " + ApplicationVersionUtils.isSameComponent(var1.getName(), var2, this.getRuntimeMBeanName(), var3));
         }

         return ApplicationVersionUtils.isSameComponent(var1.getName(), var2, this.getRuntimeMBeanName(), var3);
      }
   }

   public int getOrder() {
      return Integer.MAX_VALUE;
   }

   public boolean isNoneDDMD() {
      if (this.info.getIsWeblogicJMS() && this.destinationInfo != null) {
         return this.destinationInfo.getType() != 6 && this.destinationInfo.getType() != 5 && this.destinationInfo.getType() != 4;
      } else {
         return false;
      }
   }

   public boolean subscriptionDeletionRequired() {
      return this.isDurableSubscriptionDeletion && this.info.isDestinationTopic() && this.info.isDurableSubscriber();
   }

   private void removeEJBRuntimeMBean(EJBRuntimeMBean var1) {
      EJBComponentRuntimeMBeanImpl var2 = this.getEJBComponentRuntime();
      var2.removeEJBRuntimeMBean(var1);
   }

   public boolean isAdvancedTopicSupported() {
      return this.destinationInfo.isAdvancedTopicSupported();
   }

   public boolean supportMultipleConncitons() {
      if (this.destinationInfo == null) {
         return false;
      } else {
         int var1 = this.destinationInfo.getType();
         if (var1 != 6 && var1 != 1 && var1 != 5) {
            return false;
         } else {
            return this.info.getTopicMessagesDistributionMode() == 2 || this.info.getTopicMessagesDistributionMode() == 1;
         }
      }
   }

   public int getDestinationType() {
      return this.destinationInfo.getType();
   }

   public String getCreateDestinationArgument() {
      return this.destinationInfo.getCreateDestinationArgument();
   }

   public boolean needsSetForwardFilter() {
      return this.info.getDistributedDestinationConnection() == 1 && this.getDestinationType() == 5 || this.info.getTopicMessagesDistributionMode() == 2 && this.getDestinationType() == 5;
   }

   public boolean isStarted() {
      return this.started;
   }

   public String getDDJNDIName() {
      return this.destinationInfo == null ? this.destinationName : this.destinationInfo.getJNDIName();
   }

   private void resetConnValues() {
      this.messageSelector = this.info.getMessageSelector();
      this.isDurableSubscriptionDeletion = this.info.isDurableSubscriptionDeletion();
      this.topicMessagesDistributionMode = this.info.getTopicMessagesDistributionMode();
      this.connectionFacortyJNDIName = this.info.getConnectionFactoryJNDIName();
      this.providerURL = this.info.getProviderURL();
      switch (this.topicMessagesDistributionMode) {
         case 0:
            if (this.info.isGenerateUniqueJmsClientId()) {
               String var1 = this.domainName + "_" + (this.targetMBean == null ? this.currServerName : this.targetMBean.getName()) + "_" + this.info.getIsIdenticalKey();
               if (this.info.getDistributedDestinationConnection() == 1) {
                  var1 = var1 + "_" + this.getDDMemberName();
               }

               this.jmsClientID = this.jmsClientIDBase + "_" + var1;
            } else {
               this.jmsClientID = this.info.getJMSClientID();
            }
            break;
         case 1:
            this.jmsClientID = this.jmsClientIDBase + "_" + this.domainName + "_" + this.currServerName;
            break;
         case 2:
            this.jmsClientID = this.jmsClientIDBase;
            break;
         default:
            throw new AssertionError();
      }

      if (debugLogger.isDebugEnabled()) {
         debug("jmsClientID: " + this.jmsClientID);
      }

   }

   public String getConnectionFactoryJNDIName() {
      return this.connectionFacortyJNDIName;
   }

   public String getProviderURL() {
      return this.providerURL;
   }

   public DestinationDetail getDestination() {
      return this.destinationInfo;
   }

   public void enableDestination(DestinationDetail var1) throws WLDeploymentException {
      this.destinationInfo = var1;
      this.targetMBean = this.info.getMtMBean(var1.getMigratableTargetName());
      if (this.targetMBean != null) {
         this.unInitialize();
         this.initialize();
      }

   }

   public void setMDBStatus(String var1) {
      this.runtimeMBean.setMDBStatus(var1);
   }

   public String getResourceAdapterJndiName() {
      return this.resourceAdapterJndiName;
   }

   protected void perhapsSetupTimerManager(EJBTimerRuntimeMBean var1) throws WLDeploymentException {
      if (!$assertionsDisabled && this.runtimeMBean == null) {
         throw new java.lang.AssertionError();
      } else {
         if (this.info != null && this.info.getTimerManagerFactory() != null) {
            this.info.getTimerManagerFactory().setup(this, var1);
         }

      }
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "MessageDrivenManager.java", "weblogic.ejb.container.manager.MessageDrivenManager", "preInvoke", "(Lweblogic/ejb/container/internal/InvocationWrapper;)Ljavax/ejb/EnterpriseBean;", 186, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "MessageDrivenManager.java", "weblogic.ejb.container.manager.MessageDrivenManager", "postInvoke", "(Lweblogic/ejb/container/internal/InvocationWrapper;)V", 210, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true)})}), (boolean)0);
      $assertionsDisabled = !MessageDrivenManager.class.desiredAssertionStatus();
      DEBUG_APP_VERSION = Debug.getCategory("weblogic.AppVersion");
   }
}
