package weblogic.ejb.container.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.Identity;
import java.security.Principal;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.TimerService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.UserTransaction;
import weblogic.ejb.WLTimerService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.WLEJBContext;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.internal.usertransactioncheck.BaseUserTransactionProxy;
import weblogic.ejb.container.internal.usertransactioncheck.MDBUserTransactionProxy;
import weblogic.ejb.container.internal.usertransactioncheck.SFSBUserTransactionProxy;
import weblogic.ejb.container.internal.usertransactioncheck.SLSBUserTransactionProxy;
import weblogic.ejb.container.manager.BaseEJBManager;
import weblogic.ejb.container.manager.BaseEntityManager;
import weblogic.ejb.container.manager.MessageDrivenManager;
import weblogic.ejb.container.manager.StatefulSessionManager;
import weblogic.ejb.container.manager.StatelessManager;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.logging.Loggable;
import weblogic.security.acl.User;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.utils.StackTraceUtils;

public abstract class BaseEJBContext implements WLEJBContext {
   protected final BaseEJBHomeIntf ejbHome;
   protected final EJBHome remoteHome;
   protected final BaseEJBLocalHome localHome;
   protected final BeanManager beanManager;
   protected EJBObject ejbObject;
   protected EJBLocalObject ejbLocalObject;
   private TimerService timerService;
   private EnterpriseBean bean;
   private Context compEnv;
   private boolean isSession = false;
   private boolean isStateless = false;
   private boolean isStateful = false;
   private boolean isEntity = false;
   private boolean isMessageDriven = false;

   BaseEJBContext(EnterpriseBean var1, BeanManager var2, BaseEJBHome var3, BaseEJBLocalHome var4, EJBObject var5, EJBLocalObject var6) {
      this.bean = var1;
      this.beanManager = var2;
      this.localHome = var4;
      this.ejbObject = var5;
      this.ejbLocalObject = var6;
      if (var3 != null) {
         this.ejbHome = var3;
      } else {
         this.ejbHome = this.localHome;
      }

      this.setBeanType();
      if (var3 != null && !var3.getBeanInfo().useCallByReference()) {
         this.remoteHome = var3.getCBVHomeStub();
      } else {
         this.remoteHome = var3;
      }

   }

   private void setBeanType() {
      if (this.beanManager instanceof StatelessManager) {
         this.isSession = true;
         this.isStateless = true;
      } else if (this.beanManager instanceof StatefulSessionManager) {
         this.isSession = true;
         this.isStateful = true;
      } else if (this.beanManager instanceof BaseEntityManager) {
         this.isEntity = true;
      } else {
         if (!(this.beanManager instanceof MessageDrivenManager)) {
            throw new AssertionError("Unexpected manager type: " + this.beanManager.getClass().getName());
         }

         this.isMessageDriven = true;
      }

   }

   protected void checkAllowedMethod(int var1) throws IllegalStateException {
      if (!this.isMessageDriven) {
         int var2 = RuntimeHelper.getCurrentState(this.bean);
         if ((var2 & var1) == 0) {
            Loggable var3 = EJBLogger.logillegalCallToEJBContextMethodLoggable(EJBRuntimeUtils.getEJBStateAsString(var2), EJBRuntimeUtils.ejbEJBOperationAsString(var1));
            throw new IllegalStateException(var3.getMessage());
         }
      }

   }

   public void setBean(EnterpriseBean var1) {
      this.bean = var1;
   }

   public UserTransaction getUserTransaction() throws IllegalStateException {
      UserTransaction var1 = null;
      if (!this.beanManager.usesBeanManagedTx()) {
         Loggable var2 = EJBLogger.logbmtCanUseUserTransactionLoggable();
         throw new IllegalStateException(var2.getMessage());
      } else {
         this.checkAllowedMethod(196852);
         var1 = TxHelper.getUserTransaction();
         if (this.isEntity) {
            return new BaseUserTransactionProxy(true, var1);
         } else if (this.isMessageDriven) {
            return new MDBUserTransactionProxy(var1);
         } else {
            return (UserTransaction)(this.isSession && this.isStateful ? new SFSBUserTransactionProxy(var1) : new SLSBUserTransactionProxy(var1));
         }
      }
   }

   public Identity getCallerIdentity() {
      if (this.isStateful) {
         this.checkAllowedMethod(2036);
      } else if (this.isStateless) {
         this.checkAllowedMethod(196736);
      } else if (this.isEntity) {
         this.checkAllowedMethod(125084);
      }

      Principal var1 = this.getCallerPrincipal();
      String var2 = var1.getName();
      return new User(var1.getName());
   }

   public Principal getCallerPrincipal() {
      if (this.isStateful) {
         this.checkAllowedMethod(2036);
      } else if (this.isStateless) {
         this.checkAllowedMethod(196736);
      } else if (this.isEntity) {
         this.checkAllowedMethod(125084);
      }

      try {
         Principal var1;
         if (this.bean instanceof WLEnterpriseBean && ((WLEnterpriseBean)this.bean).__WL_getMethodState() == 32768) {
            AuthenticatedSubject var2 = (AuthenticatedSubject)((AuthenticatedSubject)((WLEnterpriseBean)this.bean).__WL_getLoadUser());
            var1 = SecurityHelper.getPrincipalFromSubject(var2);
            if (var1 == null) {
               String var3 = "getCallerPrincipal";
               Loggable var4 = EJBLogger.logmissingCallerPrincipalLoggable(var3);
               throw new PrincipalNotFoundException(var4.getMessage());
            } else {
               return var1;
            }
         } else {
            var1 = SecurityHelper.getCallerPrincipal();
            return var1;
         }
      } catch (PrincipalNotFoundException var5) {
         throw new EJBException(var5);
      }
   }

   public EJBHome getEJBHome() {
      this.checkAllowedMethod(258047);
      return this.remoteHome;
   }

   public EJBLocalHome getEJBLocalHome() {
      this.checkAllowedMethod(258047);
      return this.localHome;
   }

   public EJBObject getEJBObject() {
      if (this.remoteHome == null) {
         Loggable var1 = EJBLogger.logonlyRemoteCanInvokeGetEJBObjectLoggable();
         throw new IllegalStateException(var1.getMessage());
      } else {
         if (this.isSession) {
            this.checkAllowedMethod(198644);
         } else if (this.isEntity) {
            this.checkAllowedMethod(114936);
         }

         return this.ejbObject;
      }
   }

   public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
      if (this.localHome == null) {
         Loggable var1 = EJBLogger.logonlyLocalCanInvokeGetEJBObjectLoggable();
         throw new IllegalStateException(var1.getMessage());
      } else {
         if (this.isSession) {
            this.checkAllowedMethod(198644);
         } else if (this.isEntity) {
            this.checkAllowedMethod(114936);
         }

         return this.ejbLocalObject;
      }
   }

   public void setEJBObject(EJBObject var1) {
      this.ejbObject = var1;
   }

   public void setEJBLocalObject(EJBLocalObject var1) {
      this.ejbLocalObject = var1;
   }

   public String getApplicationName() {
      return this.ejbHome.getDeploymentInfo().getApplicationName();
   }

   public String getComponentName() {
      return this.beanManager.getBeanInfo().getComponentName();
   }

   public String getComponentURI() {
      return this.beanManager.getBeanInfo().getComponentURI();
   }

   public String getEJBName() {
      return this.beanManager.getBeanInfo().getEJBName();
   }

   public Properties getEnvironment() {
      Loggable var1 = EJBLogger.loggetEnvironmentDeprecatedLoggable();
      throw new RuntimeException(var1.getMessage());
   }

   private boolean isLocalEntityTx(Transaction var1) {
      TransactionImpl var2 = (TransactionImpl)var1;
      return var2.getProperty("LOCAL_ENTITY_TX") != null;
   }

   public boolean getRollbackOnly() throws IllegalStateException {
      if (this.beanManager.usesBeanManagedTx()) {
         Loggable var5 = EJBLogger.logonlyCMTBeanCanInvokeGetRollbackOnlyLoggable();
         throw new IllegalStateException(var5.getMessage());
      } else {
         if (this.isSession) {
            this.checkAllowedMethod(197504);
         } else if (this.isEntity) {
            this.checkAllowedMethod(125084);
         }

         try {
            weblogic.transaction.Transaction var1 = TxHelper.getTransaction();
            if (var1 != null && (var1 == null || !this.isLocalEntityTx(var1))) {
               int var6 = var1.getStatus();
               boolean var3 = false;
               if (var6 == 1 || var6 == 9 || var6 == 4) {
                  var3 = true;
               }

               return var3;
            } else {
               Loggable var2 = EJBLogger.logillegalCallToGetRollbackOnlyLoggable();
               throw new IllegalStateException(var2.getMessage());
            }
         } catch (SystemException var4) {
            EJBLogger.logExcepLookingUpXn2(StackTraceUtils.throwable2StackTrace(var4));
            return false;
         }
      }
   }

   public void setRollbackOnly() throws IllegalStateException {
      if (this.beanManager.usesBeanManagedTx()) {
         Loggable var4 = EJBLogger.logonlyCMTBeanCanInvokeSetRollbackOnlyLoggable();
         throw new IllegalStateException(var4.getMessage());
      } else {
         if (this.isSession) {
            this.checkAllowedMethod(197504);
         } else if (this.isEntity) {
            this.checkAllowedMethod(125084);
         }

         try {
            weblogic.transaction.Transaction var1 = TxHelper.getTransaction();
            if (var1 == null || var1 != null && this.isLocalEntityTx(var1)) {
               Loggable var2 = EJBLogger.logillegalCallToSetRollbackOnlyLoggable();
               throw new IllegalStateException(var2.getMessage());
            }

            var1.setRollbackOnly();
         } catch (SystemException var3) {
            EJBLogger.logExcepLookingUpXn3(StackTraceUtils.throwable2StackTrace(var3));
         }

      }
   }

   public boolean isCallerInRole(String var1) {
      if (this.isStateful) {
         this.checkAllowedMethod(2036);
      } else if (this.isStateless) {
         this.checkAllowedMethod(196736);
      } else if (this.isEntity) {
         this.checkAllowedMethod(125084);
      }

      return ((BaseEJBManager)this.beanManager).checkWritable(var1);
   }

   public boolean isCallerInRole(Identity var1) {
      return this.isCallerInRole(var1.getName());
   }

   public TimerService getTimerService() {
      this.checkAllowedToGetTimerService();
      if (this.timerService == null) {
         if (this.beanManager.getBeanInfo().isTimerDriven()) {
            this.timerService = new TimerServiceImpl(this.beanManager.getTimerManager(), this, this.beanManager.getBeanInfo().isClusteredTimers());
         } else {
            this.timerService = (TimerService)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{TimerService.class, WLTimerService.class}, new InvocationHandler() {
               public Object invoke(Object var1, Method var2, Object[] var3) {
                  Loggable var4 = EJBLogger.logIllegalAttemptToAccessTimerServiceLoggable();
                  throw new IllegalStateException(var4.getMessage());
               }
            });
         }
      }

      return this.timerService;
   }

   public void checkAllowedToUseTimerService() {
      this.checkAllowedMethod(245912);
   }

   protected void checkAllowedToGetTimerService() {
      this.checkAllowedMethod(254204);
   }

   public void checkAllowedToGetMessageContext() {
      this.checkAllowedMethod(131072);
   }

   public Object lookup(String var1) throws IllegalArgumentException {
      if (var1 != null) {
         try {
            if (var1.startsWith("java:")) {
               return (new InitialContext()).lookup(var1);
            }

            return this.beanManager.getEnvironmentContext().lookup("comp/env/" + var1);
         } catch (NamingException var3) {
         }
      }

      throw new IllegalArgumentException("Argument " + var1 + " is not found in environment context.");
   }
}
