package weblogic.ejb.container.internal;

import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.SecurityRoleReference;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.interfaces.LocalHomeHandle;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.ejb20.internal.LocalHomeHandleImpl;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.EJBResource;
import weblogic.transaction.RollbackException;
import weblogic.transaction.Transaction;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.AppSetRollbackOnlyException;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public abstract class BaseEJBLocalHome implements BaseEJBLocalHomeIntf {
   protected static final DebugLogger debugLogger;
   protected Class eloClass;
   protected DeploymentInfo deploymentInfo;
   protected ClientDrivenBeanInfo beanInfo;
   private BaseEJBHomeIntf otherHome;
   protected BeanManager beanManager;
   private boolean securityInitialized = false;
   private SecurityHelper helper = null;
   private EJBResource ejbResource = null;
   private boolean isDeployed;

   BaseEJBLocalHome(Class var1) {
      this.eloClass = var1;
   }

   public void setup(BeanInfo var1, BaseEJBHomeIntf var2, BeanManager var3) throws WLDeploymentException {
      this.beanInfo = (ClientDrivenBeanInfo)var1;
      this.otherHome = var2;
      this.beanManager = var3;
   }

   public BeanInfo getBeanInfo() {
      return this.beanInfo;
   }

   public void setBeanInfo(BeanInfo var1) {
      this.beanInfo = (ClientDrivenBeanInfo)var1;
   }

   public void setDeploymentInfo(DeploymentInfo var1) {
      this.deploymentInfo = var1;
   }

   public DeploymentInfo getDeploymentInfo() {
      return this.deploymentInfo;
   }

   public Name getJNDIName() {
      return this.beanInfo.getLocalJNDIName();
   }

   public String getIsIdenticalKey() {
      return this.beanInfo.getIsIdenticalKey();
   }

   public String getJNDINameAsString() {
      Name var1 = this.getJNDIName();
      return var1 == null ? null : var1.toString();
   }

   public abstract void remove(MethodDescriptor var1, Object var2) throws RemoveException;

   public BeanManager getBeanManager() {
      return this.beanManager;
   }

   public boolean isDeployed() {
      return this.isDeployed;
   }

   public void setIsDeployed(boolean var1) {
      this.isDeployed = var1;
   }

   public void undeploy() {
      this.unbindInJNDI();
      this.cleanup();
   }

   protected void unbindInJNDI() {
      String var1 = this.getJNDINameAsString();
      if (var1 != null) {
         try {
            (new InitialContext()).unbind(var1);
         } catch (NamingException var3) {
            throw new AssertionError("Unbind of " + var1 + " failed", var3);
         }
      }
   }

   public abstract BaseEJBLocalObjectIntf allocateELO(Object var1);

   public abstract BaseEJBLocalObjectIntf allocateELO();

   public abstract void cleanup();

   void handleSystemException(InvocationWrapper var1, Throwable var2) throws EJBException {
      BaseLocalObject.handleSystemException(var1, this.usesBeanManagedTx(), var2);
   }

   public void pushEnvironment() {
      EJBRuntimeUtils.pushEnvironment(this.beanManager.getEnvironmentContext());
   }

   public void popEnvironment() {
      EJBRuntimeUtils.popEnvironment();
   }

   protected InvocationWrapper preHomeInvoke(MethodDescriptor var1, ContextHandler var2) throws EJBException {
      var1.checkMethodPermissionsLocal(var2);
      InvocationWrapper var3 = null;

      try {
         var3 = EJBRuntimeUtils.createWrapWithTxs(var1);
      } catch (InternalException var7) {
         if (debugLogger.isDebugEnabled()) {
            debug("Failed to create a wrapper: " + var7);
         }

         try {
            Transaction var5 = TxHelper.getTransaction();
            if (var5 != null) {
               var5.setRollbackOnly();
            }
         } catch (Exception var6) {
            EJBLogger.logErrorMarkingForRollback(var6);
         }

         EJBRuntimeUtils.throwEJBException(var7);
      }

      MethodInvocationHelper.pushMethodObject(this.getBeanInfo());
      SecurityHelper.pushCallerPrincipal();
      var1.pushRunAsIdentity();
      return var3;
   }

   private void postHomeInvokeNoInvokeTx(MethodDescriptor var1, InvocationWrapper var2) throws EJBException {
      if (var2.getInvokeTx() == null) {
         try {
            this.getBeanManager().beforeCompletion(var2);
            this.getBeanManager().afterCompletion(var2);
         } catch (InternalException var4) {
            if (!EJBRuntimeUtils.isAppException(var1.getMethod(), var4)) {
               this.handleSystemException(var2, var4);
               throw new AssertionError("Should never have reached here");
            }

            EJBRuntimeUtils.throwEJBException(var4);
         }
      }

   }

   protected void postHomeInvoke(InvocationWrapper var1, Throwable var2) throws EJBException {
      javax.transaction.Transaction var3 = var1.getInvokeTx();
      javax.transaction.Transaction var4 = var1.getCallerTx();
      MethodDescriptor var5 = var1.getMethodDescriptor();

      try {
         this.postHomeInvokeNoInvokeTx(var5, var1);
         if (var3 != null && !var3.equals(var4)) {
            int var6 = -1;

            try {
               var6 = var3.getStatus();
            } catch (SystemException var22) {
            }

            switch (var6) {
               case 0:
                  try {
                     if (var2 != null && this.beanInfo.isEJB30() && EJBRuntimeUtils.isAppExceptionNeedtoRollback(this.beanInfo.getDeploymentInfo(), var2)) {
                        try {
                           var3.rollback();
                        } catch (Exception var21) {
                           EJBLogger.logErrorDuringRollback1(var3.toString(), StackTraceUtils.throwable2StackTrace(var21));
                        }
                        break;
                     }

                     var3.commit();
                     break;
                  } catch (Exception var23) {
                     Exception var7 = var23;
                     if (var23 instanceof RollbackException && ((RollbackException)var23).getNested() instanceof AppSetRollbackOnlyException) {
                        break;
                     }

                     try {
                        this.getBeanManager().destroyInstance(var1, var7);
                     } catch (InternalException var18) {
                        EJBLogger.logErrorDuringCommit(var3.toString(), StackTraceUtils.throwable2StackTrace(var18));
                     }

                     EJBRuntimeUtils.throwEJBException("Error committing transaction:", var23);
                     throw new AssertionError("Should never reach here");
                  }
               case 1:
                  try {
                     var3.rollback();
                  } catch (Exception var20) {
                     EJBLogger.logErrorDuringRollback(var3.toString(), StackTraceUtils.throwable2StackTrace(var20));
                  }
            }
         } else if (var3 != null && var3.equals(var4) && var2 != null && this.beanInfo.isEJB30() && EJBRuntimeUtils.isAppExceptionNeedtoRollback(this.beanInfo.getDeploymentInfo(), var2)) {
            try {
               var4.setRollbackOnly();
            } catch (Exception var19) {
               EJBLogger.logExcepDuringSetRollbackOnly(var19);
            }
         }
      } finally {
         this.postHomeInvokeCleanup(var1);
      }

   }

   protected void postHomeInvokeCleanup(InvocationWrapper var1) throws EJBException {
      javax.transaction.Transaction var2 = var1.getInvokeTx();
      javax.transaction.Transaction var3 = var1.getCallerTx();
      MethodDescriptor var4 = var1.getMethodDescriptor();
      var4.popRunAsIdentity();

      try {
         SecurityHelper.popCallerPrincipal();
      } catch (PrincipalNotFoundException var8) {
         EJBLogger.logErrorPoppingCallerPrincipal(var8);
      }

      try {
         if (MethodInvocationHelper.popMethodObject(this.getBeanInfo())) {
            this.getBeanManager().handleUncommittedLocalTransaction(var1);
         }
      } catch (InternalException var7) {
         EJBRuntimeUtils.throwEJBException(var7);
      }

      try {
         EJBRuntimeUtils.resumeCallersTransaction(var3, var2);
      } catch (InternalException var6) {
         EJBRuntimeUtils.throwEJBException(var6);
      }

   }

   public LocalHomeHandle getLocalHomeHandle(MethodDescriptor var1) throws EJBException {
      var1.checkMethodPermissionsLocal(new EJBContextHandler(var1, new Object[0]));
      return this.getLocalHomeHandleObject();
   }

   public LocalHomeHandle getLocalHomeHandleObject() {
      return new LocalHomeHandleImpl(this, this.getJNDIName());
   }

   public boolean isCallerInRole(String var1) {
      this.initSecurity();
      String var3 = var1;
      SecurityRoleReference var4 = this.beanInfo.getSecurityRoleReference(var1);
      if (var4 != null) {
         String var5 = var4.getReferencedRole();
         if (debugLogger.isDebugEnabled()) {
            debug(" referenced role for roleName: '" + var1 + "', is '" + var5 + "'");
         }

         var3 = var5;
      }

      return this.helper.isCallerInRole(this.beanInfo.getEJBName(), this.ejbResource, var1, var3);
   }

   public String getDisplayName() {
      return this.beanInfo.getDisplayName();
   }

   private void initSecurity() {
      if (!this.securityInitialized) {
         try {
            this.helper = new SecurityHelper(this.deploymentInfo.getSecurityRealmName(), this.deploymentInfo.getJACCPolicyConfig(), this.deploymentInfo.getJACCPolicyContextId(), this.deploymentInfo.getJACCCodeSource(), this.deploymentInfo.getJACCRoleMapper());
         } catch (Throwable var2) {
            Debug.assertion(false, "could not create SecurityHelper: " + var2.getMessage());
         }

         this.ejbResource = SecurityHelper.createEJBResource(this.deploymentInfo);
         this.securityInitialized = true;
      }

   }

   private static void debug(String var0) {
      debugLogger.debug("[BaseEJBLocalHome] " + var0);
   }

   static {
      debugLogger = EJBDebugService.invokeLogger;
   }
}
