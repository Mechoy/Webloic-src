package weblogic.ejb.container.manager;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import javax.ejb.EJBContext;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.PoolIntf;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.internal.AllowedMethodsHelper;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.EJBContextManager;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.container.internal.SessionEJBContextImpl;
import weblogic.ejb.container.internal.StatelessEJBHome;
import weblogic.ejb.container.internal.StatelessEJBLocalHome;
import weblogic.ejb.container.internal.TxManager;
import weblogic.ejb.container.monitoring.StatelessEJBRuntimeMBeanImpl;
import weblogic.ejb.container.pool.StatelessSessionPool;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.transaction.TxHelper;

public final class StatelessManager extends BaseEJBManager implements BeanManager {
   private StatelessEJBHome remoteHome = null;
   private StatelessEJBLocalHome localHome = null;
   private Class beanIntf = null;
   private Method createMethod;
   private PoolIntf pool = null;
   static final long serialVersionUID = 4921096245799829863L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.manager.StatelessManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;

   public StatelessManager(EJBComponentRuntimeMBeanImpl var1) {
      super(var1);
   }

   public String toString() {
      return "[StatelessManager] home/class: " + (this.ejbHome != null ? this.ejbHome.toString() : this.beanIntf.getName()) + "pool: " + this.pool + " debug: " + debugLogger.isDebugEnabled();
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4) throws WLDeploymentException {
      super.setup(var1, var2, var3, var4);
      SessionBeanInfo var5 = (SessionBeanInfo)var3;
      this.beanClass = var5.getGeneratedBeanClass();
      this.beanIntf = var5.getGeneratedBeanInterface();

      try {
         if (!var3.isEJB30()) {
            this.createMethod = this.beanIntf.getMethod("ejbCreate", (Class[])null);
         }
      } catch (Exception var9) {
         if (!var3.isEJB30()) {
            AssertionError var7 = new AssertionError("Could not find ejbCreate");
            var7.initCause(var9);
            throw var7;
         }
      }

      this.remoteHome = (StatelessEJBHome)var1;
      this.localHome = (StatelessEJBLocalHome)var2;

      try {
         StatelessEJBRuntimeMBeanImpl var6 = new StatelessEJBRuntimeMBeanImpl(var3.getEJBName(), var3.getEJBName(), this.getEJBComponentRuntime(), this.getTimerManager());
         this.setEJBRuntimeMBean(var6);
         this.addEJBRuntimeMBean(var6);
         this.perhapsSetupTimerManager(var6.getTimerRuntime());
         this.pool = new StatelessSessionPool(this.remoteHome, this.localHome, this, var3, var6.getPoolRuntime());
      } catch (ManagementException var8) {
         Loggable var10 = EJBLogger.logFailedToCreateRuntimeMBeanLoggable(var8);
         throw new WLDeploymentException(var10.getMessage(), var8);
      }

      this.txManager = new TxManager(this);
      if (debugLogger.isDebugEnabled()) {
         debug("In setup for :" + this);
      }

   }

   public PoolIntf getPool() {
      return this.pool;
   }

   public Method getCreateMethod() {
      return this.createMethod;
   }

   public EnterpriseBean preInvoke(InvocationWrapper var1) throws InternalException {
      boolean var12 = false;

      EnterpriseBean var10000;
      try {
         var12 = true;
         super.preInvoke();
         int var2 = var1.getMethodDescriptor().getTxTimeoutMS();
         if (debugLogger.isDebugEnabled()) {
            debug("In preInvoke with timeout:" + var2 + " on manager: " + this);
         }

         Transaction var3 = var1.getInvokeTx();
         if (var3 != null) {
            this.setupTxListener(var1);
         }

         EnterpriseBean var4 = this.pool.getBean((long)var2);
         var10000 = var4;
         var12 = false;
      } finally {
         if (var12) {
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
         Object[] var6 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium.isArgumentsCaptureNeeded()) {
            var6 = new Object[]{this, var1};
         }

         DynamicJoinPoint var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var6, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      if (debugLogger.isDebugEnabled()) {
         debug("In postInvoke on " + this);
      }

      if (this.usesBeanManagedTx()) {
         try {
            weblogic.transaction.Transaction var2 = TxHelper.getTransaction();
            if (var2 != null && var2.getStatus() == 0) {
               EJBLogger.logMustCommit();
               if (var2 instanceof weblogic.transaction.Transaction) {
                  ((weblogic.transaction.Transaction)var2).setRollbackOnly("Bean-managed stateless session beans cannot have a transaction that spans multiple methods", (Throwable)null);
               } else {
                  var2.setRollbackOnly();
               }

               this.pool.destroyBean((EnterpriseBean)null);
               Loggable var3 = EJBLogger.logSLSBMethodDidNotCompleteTXLoggable(var1.getMethodDescriptor().getMethodInfo().getSignature(), this.beanInfo.getEJBName());
               Exception var4 = new Exception(var3.getMessage());
               EJBRuntimeUtils.throwInternalException("Error during postInvoke.", var4);
            }
         } catch (SystemException var7) {
            this.pool.destroyBean((EnterpriseBean)null);
            EJBRuntimeUtils.throwInternalException("Error getting current tx: ", var7);
         }
      }

      if (var1.isLocal()) {
         this.pool.releaseBean(var1.getBean());
      }

   }

   public EnterpriseBean preHomeInvoke(InvocationWrapper var1) throws InternalException {
      throw new AssertionError("Stateless session beans cannot have home methods");
   }

   public void postHomeInvoke(InvocationWrapper var1) throws InternalException {
      throw new AssertionError("Stateless session beans cannot have home methods");
   }

   public void destroyPooledInstance(InvocationWrapper var1, Throwable var2) throws InternalException {
      throw new AssertionError("Stateless session beans cannot have home methods");
   }

   public void destroyInstance(InvocationWrapper var1, Throwable var2) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("In destroyInstance for manager: " + this);
      }

      if (var1 == null || var1.shouldLogException()) {
         EJBLogger.logExcepDuringInvocFromHomeOrBusiness(this.ejbHome != null ? this.ejbHome.toString() : this.beanIntf.getName(), var2.toString());
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

   public EJBContext allocateContext(EnterpriseBean var1, EJBObject var2, EJBLocalObject var3) {
      return new SessionEJBContextImpl(var1, this, this.remoteHome, this.localHome, var2, var3);
   }

   public EJBContext allocateContext(EnterpriseBean var1, Object var2) {
      EJBObject var3 = null;
      BaseEJBLocalObjectIntf var4 = null;
      if (this.remoteHome != null) {
         var3 = this.remoteHome.allocateEO();
      }

      if (this.localHome != null) {
         var4 = this.localHome.allocateELO();
      }

      return this.allocateContext(var1, var3, var4);
   }

   public EnterpriseBean createBean(EJBObject var1, EJBLocalObject var2) throws InternalException {
      boolean var15;
      boolean var10000 = var15 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var16 = null;
      DiagnosticActionState[] var17 = null;
      Object var14 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         Object[] var10 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium.isArgumentsCaptureNeeded()) {
            var10 = InstrumentationSupport.toSensitive(3);
         }

         DynamicJoinPoint var35 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var10, (Object)null);
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium;
         DiagnosticAction[] var10002 = var16 = var10001.getActions();
         InstrumentationSupport.preProcess(var35, var10001, var10002, var17 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var22 = false;

      SessionBean var36;
      try {
         var22 = true;
         SessionContext var3 = (SessionContext)this.allocateContext((EnterpriseBean)null, var1, var2);

         SessionBean var4;
         try {
            EJBContextManager.pushEjbContext(var3);
            AllowedMethodsHelper.pushMethodInvocationState(new Integer(1));
            var4 = (SessionBean)this.allocateBean();
         } finally {
            EJBContextManager.popEjbContext();
            AllowedMethodsHelper.popMethodInvocationState();
         }

         ((SessionEJBContextImpl)var3).setBean(var4);

         try {
            AllowedMethodsHelper.pushMethodInvocationState(new Integer(1));
            var4.setSessionContext(var3);
         } catch (Throwable var31) {
            EJBRuntimeUtils.throwInternalException("Error during setSessionContext", var31);
         } finally {
            AllowedMethodsHelper.popMethodInvocationState();
         }

         ((WLEnterpriseBean)var4).__WL_setEJBContext(var3);
         var36 = var4;
         var22 = false;
      } finally {
         if (var22) {
            var10001 = null;
            if (var15) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium, var16, var17);
            }

         }
      }

      if (var15) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium, var16, var17);
      }

      return var36;
   }

   public EJBObject remoteCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("In create for manager: " + this);
      }

      return this.remoteHome.allocateEO();
   }

   public EJBLocalObject localCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("In create for manager: " + this);
      }

      return this.localHome.allocateELO();
   }

   public void remove(InvocationWrapper var1) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("In remove for manager: " + this);
      }

   }

   public EJBObject remoteFindByPrimaryKey(InvocationWrapper var1, Object var2) throws InternalException {
      throw new AssertionError("Finds do not apply to stateless beans");
   }

   public Object localFindByPrimaryKey(InvocationWrapper var1, Object var2) throws InternalException {
      throw new AssertionError("Finds do not apply to stateless beans");
   }

   public EJBObject remoteScalarFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      throw new AssertionError("Finds do not apply to stateless beans");
   }

   public EJBLocalObject localScalarFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      throw new AssertionError("Finds do not apply to stateless beans");
   }

   public Enumeration enumFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      throw new AssertionError("Finds do not apply to stateless beans");
   }

   public Collection collectionFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      throw new AssertionError("Finds do not apply to stateless beans");
   }

   public void handleUncommittedLocalTransaction(InvocationWrapper var1) throws InternalException {
      this.handleUncommittedLocalTransaction(var1.getMethodDescriptor().getMethodInfo().getSignature(), var1.getBean());
   }

   public void handleUncommittedLocalTransaction(String var1, EnterpriseBean var2) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("There is local transaction in progess.");
      }

      EJBLogger.logMustCommit();
      this.pool.destroyBean(var2);
      Loggable var3 = EJBLogger.logSLSBMethodDidNotCompleteTXLoggable(var1, this.beanInfo.getEJBName());
      Exception var4 = new Exception(var3.getMessage());
      EJBRuntimeUtils.throwInternalException("Error during postInvoke.", var4);
   }

   public void undeploy() {
      super.undeploy();
      if (this.pool != null) {
         this.pool.cleanup();
      }

   }

   public void initializePool() throws WLDeploymentException {
      this.pool.createInitialBeans();
   }

   public void beanImplClassChangeNotification() {
      this.beanClass = ((SessionBeanInfo)this.beanInfo).getGeneratedBeanClass();
      this.pool.reset();
   }

   public void releaseBean(InvocationWrapper var1) {
      EnterpriseBean var2 = var1.getBean();
      this.pool.releaseBean(var2);
   }

   public void reInitializeCacheAndPool() {
      this.reInitializePool();
   }

   public void reInitializePool() {
      this.pool.reInitializePool();
   }

   private static void debug(String var0) {
      debugLogger.debug("[StatelessManager] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Create_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatelessManager.java", "weblogic.ejb.container.manager.StatelessManager", "preInvoke", "(Lweblogic/ejb/container/internal/InvocationWrapper;)Ljavax/ejb/EnterpriseBean;", 132, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatelessManager.java", "weblogic.ejb.container.manager.StatelessManager", "postInvoke", "(Lweblogic/ejb/container/internal/InvocationWrapper;)V", 156, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatelessManager.java", "weblogic.ejb.container.manager.StatelessManager", "createBean", "(Ljavax/ejb/EJBObject;Ljavax/ejb/EJBLocalObject;)Ljavax/ejb/EnterpriseBean;", 297, (Map)null, (boolean)0);
   }
}
