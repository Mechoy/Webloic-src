package weblogic.ejb.container.manager;

import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.util.Collection;
import java.util.Enumeration;
import javax.ejb.EJBContext;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.ejb.NoSuchEJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.EjbComponentCreator;
import weblogic.ejb.container.interfaces.SecurityRoleReference;
import weblogic.ejb.container.interfaces.TimerManager;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.internal.AllowedMethodsHelper;
import weblogic.ejb.container.internal.BaseEJBContext;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.EJBContextManager;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.container.internal.ManagerHelper;
import weblogic.ejb.container.internal.TxManager;
import weblogic.ejb.container.timer.EJBTimerManagerFactory;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.management.runtime.EJBRuntimeMBean;
import weblogic.management.runtime.EJBTimerRuntimeMBean;
import weblogic.security.service.EJBResource;
import weblogic.utils.Debug;

public abstract class BaseEJBManager implements BeanManager {
   protected static final DebugLogger debugLogger;
   private EJBRuntimeMBean runtimeMBean;
   private EJBComponentRuntimeMBeanImpl componentRuntime;
   private ManagerHelper helper;
   private EJBResource ejbResource;
   private boolean writableInitialized = false;
   protected BaseEJBHomeIntf ejbHome;
   protected BaseEJBRemoteHomeIntf remoteHome;
   protected BaseEJBLocalHomeIntf localHome;
   protected Class beanClass;
   protected Context environmentContext;
   protected boolean usesBeanManagedTx;
   protected BeanInfo beanInfo;
   protected EjbComponentCreator ejbComponentCreator;
   protected TimerManager timerManager;
   protected TxManager txManager;
   boolean isDeployed = false;

   public BaseEJBManager(EJBComponentRuntimeMBeanImpl var1) {
      this.componentRuntime = var1;
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4) throws WLDeploymentException {
      this.beanInfo = var3;
      this.remoteHome = var1;
      this.localHome = var2;
      if (var1 != null) {
         this.ejbHome = var1;
      } else if (var2 != null) {
         this.ejbHome = var2;
      }

      assert var4 != null;

      this.environmentContext = var4;
      this.createEJBTimerManager();
      this.usesBeanManagedTx = var3.usesBeanManagedTx();
      this.ejbComponentCreator = var3.getEjbComponentCreator();
   }

   public void setIsDeployed(boolean var1) {
      this.isDeployed = var1;
   }

   protected void createEJBTimerManager() {
      if (this.beanInfo.isTimerDriven()) {
         this.timerManager = EJBTimerManagerFactory.createEJBTimerManager(this);
      }

   }

   public boolean getIsDeployed() {
      return this.isDeployed;
   }

   public BaseEJBHomeIntf getEJBHome() {
      return this.ejbHome;
   }

   public TxManager getTxManager() {
      return this.txManager;
   }

   public void preInvoke() throws InternalException {
      if (!this.isDeployed) {
         EJBRuntimeUtils.throwInternalException("Exception during invoke.", new NoSuchEJBException("Bean is already undeployed."));
      }

   }

   public void businessReady(InvocationWrapper var1) {
   }

   public void setEJBRuntimeMBean(EJBRuntimeMBean var1) {
      this.runtimeMBean = var1;
   }

   public EJBRuntimeMBean getEJBRuntimeMBean() {
      return this.runtimeMBean;
   }

   public Context getEnvironmentContext() {
      return this.environmentContext;
   }

   public BeanInfo getBeanInfo() {
      return this.beanInfo;
   }

   protected void perhapsSetupTimerManager(EJBTimerRuntimeMBean var1) throws WLDeploymentException {
      assert this.runtimeMBean != null;

      if (this.timerManager != null) {
         this.timerManager.setup(var1);
      }

   }

   public TimerManager getTimerManager() {
      return this.timerManager;
   }

   public boolean usesBeanManagedTx() {
      return this.usesBeanManagedTx;
   }

   protected EnterpriseBean createNewBeanInstance() throws IllegalAccessException, InstantiationException {
      Object var1;
      if (this.beanInfo.isEJB30()) {
         ClassLoader var2 = this.beanInfo.getClassLoader();
         Thread var3 = Thread.currentThread();
         ClassLoader var4 = var3.getContextClassLoader();

         try {
            var3.setContextClassLoader(var2);
            var1 = this.ejbComponentCreator.getBean(this.beanInfo.getEJBName(), this.beanClass, true);
         } finally {
            var3.setContextClassLoader(var4);
         }
      } else {
         var1 = this.beanClass.newInstance();
      }

      return (EnterpriseBean)var1;
   }

   public final EnterpriseBean allocateBean() throws InternalException {
      try {
         return this.createNewBeanInstance();
      } catch (IllegalAccessException var2) {
         throw new AssertionError(var2);
      } catch (InstantiationException var3) {
         throw new InternalException("Error calling bean's constructor: ", var3);
      }
   }

   public abstract EJBContext allocateContext(EnterpriseBean var1, EJBObject var2, EJBLocalObject var3);

   public abstract EJBContext allocateContext(EnterpriseBean var1, Object var2);

   public EnterpriseBean createBean(EJBObject var1, EJBLocalObject var2) throws InternalException {
      return this.createBean(var1, var2, (EnterpriseBean)null);
   }

   public EnterpriseBean createBean(EJBObject var1, EJBLocalObject var2, EnterpriseBean var3) throws InternalException {
      EnterpriseBean var5;
      try {
         this.ejbHome.pushEnvironment();
         EJBContext var4 = this.allocateContext(var3, var1, var2);

         try {
            EJBContextManager.pushEjbContext(var4);
            AllowedMethodsHelper.pushMethodInvocationState(new Integer(1));
            if (var3 == null) {
               var3 = this.allocateBean();
            }
         } finally {
            EJBContextManager.popEjbContext();
            AllowedMethodsHelper.popMethodInvocationState();
         }

         ((BaseEJBContext)var4).setBean(var3);

         try {
            if (var3 instanceof EntityBean) {
               ((EntityBean)var3).setEntityContext((EntityContext)var4);
            } else if (var3 instanceof SessionBean) {
               ((SessionBean)var3).setSessionContext((SessionContext)var4);
            } else {
               if (!(var3 instanceof MessageDrivenBean)) {
                  throw new AssertionError("Unknown bean type:" + var3.getClass());
               }

               ((MessageDrivenBean)var3).setMessageDrivenContext((MessageDrivenContext)var4);
            }
         } catch (Exception var15) {
            throw new InternalException("Error during setXXXContext: ", var15);
         }

         ((WLEnterpriseBean)var3).__WL_setEJBContext(var4);
         var5 = var3;
      } finally {
         this.ejbHome.popEnvironment();
      }

      return var5;
   }

   public void setupTxListener(InvocationWrapper var1) throws InternalException {
      Transaction var2 = var1.getInvokeTx();

      assert var2 != null;

      try {
         if (debugLogger.isDebugEnabled()) {
            debug("Setting up tx listener for tx: " + var2);
         }

         if (var2 == var1.getCallerTx() || var2.getStatus() != 1) {
            this.getTxManager().registerSynchronization(var1.getPrimaryKey(), var2);
         }
      } catch (Exception var4) {
         this.handleSystemException(var1, var4);
         throw new AssertionError("Should never reach here");
      }
   }

   public void setupTxListener(Object var1, Object var2) throws InternalException {
      Transaction var3 = null;
      if (var2 instanceof Transaction) {
         var3 = (Transaction)var2;
         if (debugLogger.isDebugEnabled()) {
            debug("Setting up tx listener for tx: " + var3);
         }

         this.getTxManager().registerSynchronization(var1, var3);
      }
   }

   void handleSystemException(InvocationWrapper var1, Throwable var2) throws InternalException {
      Transaction var3 = var1.getInvokeTx();
      if (var3 == null) {
         EJBRuntimeUtils.throwInternalException("EJB Exception: ", var2);
      } else if (EJBRuntimeUtils.runningInOurTx(var1)) {
         try {
            var3.rollback();
         } catch (Exception var8) {
            EJBLogger.logStackTraceAndMessage(var8.getMessage(), var8);
            EJBLogger.logErrorOnRollback(var8);
         }

         EJBRuntimeUtils.throwInternalException("EJB Exception: ", var2);
      } else {
         int var4 = 0;

         try {
            var4 = var3.getStatus();
         } catch (SystemException var7) {
         }

         if (var4 == 0 || var4 == 5) {
            try {
               var3.setRollbackOnly();
            } catch (Exception var6) {
               EJBLogger.logErrorMarkingRollback(var6);
            }
         }

         if (!(var2 instanceof NoSuchObjectException) && !(var2 instanceof NoSuchEJBException)) {
            if (var1.isLocal()) {
               EJBRuntimeUtils.throwWrappedTransactionRolledbackLocal("EJB Exception: ", var2);
            } else {
               EJBRuntimeUtils.throwWrappedTransactionRolledback("EJB Exception: ", var2);
            }
         } else {
            EJBRuntimeUtils.throwInternalException("EJB Exception: ", var2);
         }
      }

   }

   protected void handleMethodException(Method var1, Class[] var2, Throwable var3) throws InternalException {
      boolean var4;
      if (this.beanInfo.isEJB30()) {
         var4 = EJBRuntimeUtils.isAppException(this.beanInfo, var1, var3);
      } else {
         var4 = EJBRuntimeUtils.isAppException(var1, var2, var3);
      }

      if (debugLogger.isDebugEnabled()) {
         debug("Got an exception: " + var3);
      }

      if (var4) {
         if (debugLogger.isDebugEnabled()) {
            debug("Throwing app exception");
         }

         EJBRuntimeUtils.throwInternalException("EJB Exception:", var3);
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("We think it's a system exception");
         }

         EJBLogger.logExcepInMethod1(var1.getName(), var3);
         EJBRuntimeUtils.throwInternalException("EJB Exception:", var3);
      }

   }

   protected EJBComponentRuntimeMBeanImpl getEJBComponentRuntime() {
      return this.componentRuntime;
   }

   protected void addEJBRuntimeMBean(EJBRuntimeMBean var1) {
      EJBComponentRuntimeMBeanImpl var2 = this.getEJBComponentRuntime();
      var2.addEJBRuntimeMBean(var1);
   }

   protected String getEJBRuntimeMBeanName() {
      return this.beanInfo.getEJBName();
   }

   public abstract EJBObject remoteFindByPrimaryKey(InvocationWrapper var1, Object var2) throws InternalException;

   public abstract Object localFindByPrimaryKey(InvocationWrapper var1, Object var2) throws InternalException;

   public abstract EJBObject remoteScalarFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException;

   public abstract EJBLocalObject localScalarFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException;

   public abstract Enumeration enumFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException;

   public abstract Collection collectionFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException;

   public void undeploy() {
      if (this.localHome != null) {
         this.localHome.undeploy();
      }

      if (this.remoteHome != null) {
         this.remoteHome.undeploy();
      }

      this.undeployTimerManager();
      if (this.beanInfo != null) {
         this.beanInfo.onUndeploy();
      }

      TxManager var1 = this.getTxManager();
      if (var1 != null) {
         var1.undeploy();
      }

   }

   protected void undeployTimerManager() {
      if (this.timerManager != null) {
         this.timerManager.undeploy();
      }

   }

   public void remove() {
   }

   public void handleUncommittedLocalTransaction(InvocationWrapper var1) throws InternalException {
      Debug.assertion(false, "handleUncommitedLocalTransaction N/A on this bean");
   }

   public boolean checkWritable(String var1) {
      this.initWritable();
      String var3 = var1;
      SecurityRoleReference var4 = this.beanInfo.getSecurityRoleReference(var1);
      if (var4 != null) {
         String var5 = var4.getReferencedRole();
         if (debugLogger.isDebugEnabled()) {
            debug(" referenced role for roleName: '" + var1 + "', is '" + var5 + "'");
         }

         var3 = var5;
      }

      return this.helper.isResWritable(this.ejbResource, var1, var3);
   }

   private void initWritable() {
      if (!this.writableInitialized) {
         this.helper = new ManagerHelper(this.beanInfo.getDeploymentInfo().getSecurityRealmName(), this.beanInfo.getEJBName(), this.beanInfo.getJACCPolicyContextId(), this.beanInfo.getJACCCodeSource(), this.beanInfo.getJACCPolicyConfig(), this.beanInfo.getJACCRoleMapper());
         this.ejbResource = this.helper.createEJBResource(this.beanInfo.getDeploymentInfo());
         this.writableInitialized = true;
      }

   }

   private static void debug(String var0) {
      debugLogger.debug("[BaseEJBManager] " + var0);
   }

   static {
      debugLogger = EJBDebugService.invokeLogger;
   }
}
