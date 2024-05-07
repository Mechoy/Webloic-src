package weblogic.ejb.container.internal;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.ejb.EJBHome;
import javax.ejb.EJBMetaData;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.Name;
import javax.transaction.SystemException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.SecurityRoleReference;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.ejb20.interfaces.RemoteHome;
import weblogic.ejb20.internal.HomeHandleImpl;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.jndi.OpaqueReference;
import weblogic.logging.Loggable;
import weblogic.rmi.SupportsInterfaceBasedCallByReference;
import weblogic.rmi.extensions.activation.Activator;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.EJBResource;
import weblogic.transaction.RollbackException;
import weblogic.transaction.Transaction;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.AppSetRollbackOnlyException;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public abstract class BaseEJBHome implements BaseEJBRemoteHomeIntf, RemoteHome, SupportsInterfaceBasedCallByReference {
   protected static final DebugLogger debugLogger;
   protected Class eoClass;
   protected BeanManager beanManager;
   protected DeploymentInfo deploymentInfo;
   protected ClientDrivenBeanInfo beanInfo;
   private CBVHomeRef cbvHomeRef;
   private boolean securityInitialized = false;
   private SecurityHelper helper = null;
   private EJBResource ejbResource = null;
   private EJBMetaData ejbMetaData;
   private boolean isDeployed;

   BaseEJBHome(Class var1) {
      this.eoClass = var1;
   }

   public void setup(BeanInfo var1, BaseEJBHomeIntf var2, BeanManager var3) throws WLDeploymentException {
      this.beanInfo = (ClientDrivenBeanInfo)var1;
      this.beanManager = var3;
      if (!var1.useCallByReference()) {
         this.cbvHomeRef = new CBVHomeRef(this);
      }

   }

   public void unprepare() {
      if (!this.beanInfo.useCallByReference()) {
         this.cbvHomeRef.removeResolvedRef();
      }

   }

   public BeanInfo getBeanInfo() {
      return this.beanInfo;
   }

   public void setBeanInfo(BeanInfo var1) {
      this.beanInfo = (ClientDrivenBeanInfo)var1;
   }

   public BeanManager getBeanManager() {
      assert this.beanManager != null;

      return this.beanManager;
   }

   public void setDeploymentInfo(DeploymentInfo var1) {
      this.deploymentInfo = var1;
   }

   public DeploymentInfo getDeploymentInfo() {
      return this.deploymentInfo;
   }

   public Name getJNDIName() {
      return this.beanInfo.getJNDIName();
   }

   public String getJNDINameAsString() {
      Name var1 = this.getJNDIName();
      return var1 == null ? null : var1.toString();
   }

   public abstract EJBMetaData getEJBMetaData() throws RemoteException;

   protected abstract EJBMetaData getEJBMetaDataInstance();

   protected EJBMetaData getEJBMetaData(MethodDescriptor var1) throws RemoteException {
      var1.checkMethodPermissionsRemote(new EJBContextHandler(var1, new Object[0]));
      if (this.ejbMetaData == null) {
         this.ejbMetaData = this.getEJBMetaDataInstance();
      }

      return this.ejbMetaData;
   }

   public HomeHandle getHomeHandle(MethodDescriptor var1) throws RemoteException {
      var1.checkMethodPermissionsRemote(new EJBContextHandler(var1, new Object[0]));
      if (this.getJNDIName() == null) {
         Loggable var2 = EJBLogger.logneedJNDINameForHomeHandlesLoggable(this.getDisplayName());
         throw new RemoteException(var2.getMessage());
      } else {
         return this.getHomeHandleObject();
      }
   }

   private HomeHandle getHomeHandleObject() {
      return new HomeHandleImpl(this, this.getJNDIName(), URLDelegateProvider.getURLDelegate(this.isHomeClusterable()));
   }

   public abstract void remove(MethodDescriptor var1, Object var2) throws RemoteException, RemoveException;

   public abstract void remove(MethodDescriptor var1, Handle var2) throws RemoteException, RemoveException;

   public abstract EJBObject allocateEO(Object var1);

   public abstract EJBObject allocateEO();

   public abstract void cleanup();

   public EJBHome getCBVHomeStub() {
      return this.cbvHomeRef.getStub();
   }

   public Object getReferenceToBind() {
      return !this.beanInfo.useCallByReference() ? this.cbvHomeRef : this;
   }

   public EJBObject allocateEJBObject() throws RemoteException {
      return this.allocateEO();
   }

   public EJBObject allocateEJBObject(Object var1) throws RemoteException {
      return this.allocateEO(var1);
   }

   public String getIsIdenticalKey() {
      return this.beanInfo.getIsIdenticalKey();
   }

   public abstract boolean usesBeanManagedTx();

   public boolean isDeployed() {
      return this.isDeployed;
   }

   public void setIsDeployed(boolean var1) {
      this.isDeployed = var1;
   }

   public void activate() throws WLDeploymentException {
   }

   public void undeploy() {
      this.cleanup();
      if (this.beanInfo.useCallByReference()) {
         try {
            ServerHelper.unexportObject(this, true, true);
         } catch (NoSuchObjectException var2) {
         }
      } else {
         this.cbvHomeRef.removeResolvedRef();
      }

   }

   public void unexportEO(EJBObject var1) {
      this.unexportEO(var1, true);
   }

   public void unexportEO(Remote var1) {
      try {
         ServerHelper.unexportObject(var1, true, true);
      } catch (NoSuchObjectException var3) {
      }

   }

   public void unexportEO(EJBObject var1, boolean var2) {
      try {
         ServerHelper.unexportObject(var1, true, var2);
      } catch (NoSuchObjectException var4) {
      }

   }

   public void unexportEJBActivator(Activator var1, Class var2) {
      try {
         ServerHelper.removeRuntimeDescriptor(var2);
         ServerHelper.unexportObject(var1);
      } catch (NoSuchObjectException var4) {
      }

   }

   void handleSystemException(InvocationWrapper var1, Throwable var2) throws RemoteException {
      BaseRemoteObject.handleSystemException(var1, this.usesBeanManagedTx(), var2);
   }

   public void pushEnvironment() {
      EJBRuntimeUtils.pushEnvironment(this.beanManager.getEnvironmentContext());
   }

   public void popEnvironment() {
      EJBRuntimeUtils.popEnvironment();
   }

   protected InvocationWrapper preHomeInvoke(MethodDescriptor var1, ContextHandler var2) throws RemoteException {
      InvocationWrapper var3 = null;
      var1.checkMethodPermissionsRemote(var2);

      try {
         var3 = EJBRuntimeUtils.createWrapWithTxs(var1);
      } catch (InternalException var7) {
         InternalException var4 = var7;
         if (debugLogger.isDebugEnabled()) {
            debug("Failed to create a wrapper", var7);
         }

         try {
            Transaction var5 = TxHelper.getTransaction();
            if (var5 != null) {
               if (var5 instanceof Transaction) {
                  ((Transaction)var5).setRollbackOnly(var4);
               } else {
                  var5.setRollbackOnly();
               }
            }
         } catch (Exception var6) {
            EJBLogger.logErrorMarkingForRollback(var6);
         }

         EJBRuntimeUtils.throwRemoteException(var7);
      }

      MethodInvocationHelper.pushMethodObject(this.getBeanInfo());
      SecurityHelper.pushCallerPrincipal();
      var1.pushRunAsIdentity();
      return var3;
   }

   private void postHomeInvokeNoInvokeTx(MethodDescriptor var1, InvocationWrapper var2) throws RemoteException {
      if (var2.getInvokeTx() == null) {
         try {
            this.getBeanManager().beforeCompletion(var2);
            this.getBeanManager().afterCompletion(var2);
         } catch (InternalException var4) {
            if (!EJBRuntimeUtils.isAppException(var1.getMethod(), var4)) {
               this.handleSystemException(var2, var4);
               throw new AssertionError("Should never have reached here");
            }

            EJBRuntimeUtils.throwRemoteException(var4);
         }
      }

   }

   protected void postHomeInvoke(InvocationWrapper var1, Throwable var2) throws RemoteException {
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

                     EJBRuntimeUtils.throwRemoteException("Error committing transaction:", var23);
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

   protected void postHomeInvokeCleanup(InvocationWrapper var1) throws RemoteException {
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
         EJBRuntimeUtils.throwRemoteException(var7);
      }

      try {
         EJBRuntimeUtils.resumeCallersTransaction(var3, var2);
      } catch (InternalException var6) {
         EJBRuntimeUtils.throwRemoteException(var6);
      }

   }

   protected void validateHandleFromHome(Handle var1) throws RemoteException {
      if (var1 == null) {
         Loggable var8 = EJBLogger.loghandleNullLoggable();
         throw new RemoteException(var8.getMessage());
      } else {
         EJBObject var2 = var1.getEJBObject();
         if (var2 == null) {
            Loggable var9 = EJBLogger.logejbObjectNullLoggable();
            throw new RemoteException(var9.getMessage());
         } else {
            EJBHome var3 = var2.getEJBHome();
            Loggable var10;
            if (var3 == null) {
               var10 = EJBLogger.loghomeWasNullLoggable();
               throw new RemoteException(var10.getMessage());
            } else if (!(var3 instanceof RemoteHome)) {
               var10 = EJBLogger.logejbObjectNotFromThisHomeLoggable();
               throw new RemoteException(var10.getMessage());
            } else {
               RemoteHome var4 = (RemoteHome)var3;
               String var5 = this.getIsIdenticalKey();
               String var6 = var4.getIsIdenticalKey();
               if (!var5.equals(var6)) {
                  Loggable var7 = EJBLogger.logejbObjectNotFromThisHomeLoggable();
                  throw new RemoteException(var7.getMessage());
               }
            }
         }
      }
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

   public boolean isHomeClusterable() {
      return ServerHelper.isClusterable(this);
   }

   private static void debug(String var0) {
      debugLogger.debug("[BaseEJBHome] " + var0);
   }

   private static void debug(String var0, Throwable var1) {
      debugLogger.debug("[BaseEJBHome] " + var0, var1);
   }

   public Object getInstance() {
      return this;
   }

   static {
      debugLogger = EJBDebugService.invokeLogger;
   }

   private static class CBVHomeRef implements OpaqueReference {
      private final BaseEJBHome home;
      private EJBHome cbvStub;
      private Object referent;

      CBVHomeRef(BaseEJBHome var1) {
         this.home = var1;
      }

      public synchronized Object getReferent(Name var1, Context var2) {
         this.ensureResolved();
         return this.referent;
      }

      synchronized EJBHome getStub() {
         this.ensureResolved();
         return this.cbvStub;
      }

      private void ensureResolved() {
         if (this.cbvStub == null) {
            try {
               if (this.home.isHomeClusterable()) {
                  this.cbvStub = (EJBHome)ServerHelper.exportObject(this.home, this.home.getJNDINameAsString());
               } else {
                  this.cbvStub = (EJBHome)ServerHelper.exportObject(this.home);
               }

               this.referent = ServerHelper.getCBVWrapperObject(this.home);
            } catch (RemoteException var2) {
               throw new AssertionError(var2);
            }
         }
      }

      synchronized void removeResolvedRef() {
         this.cbvStub = null;
         this.referent = null;

         try {
            ServerHelper.unexportObject(this.home, true, true);
         } catch (NoSuchObjectException var2) {
         }

      }
   }
}
