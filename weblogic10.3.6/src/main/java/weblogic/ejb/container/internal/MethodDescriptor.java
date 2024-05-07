package weblogic.ejb.container.internal;

import java.lang.reflect.Method;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRequiredException;
import javax.ejb.TransactionRequiredLocalException;
import javax.security.jacc.EJBMethodPermission;
import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionRequiredException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.utils.ToStringUtils;
import weblogic.logging.Loggable;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.EJBResource;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.utils.AssertionError;

public final class MethodDescriptor implements Cloneable {
   private static final DebugLogger debugLogger;
   private int txAttribute;
   private String applicationName;
   private String ejbComponentName;
   private String ejbName;
   private Method method;
   private String methodName;
   private int txTimeoutMS;
   private Integer isolationLevel;
   private int selectForUpdate = 0;
   private final MethodInfo methodInfo;
   private boolean entityAlwaysUsesTransaction;
   private boolean isLocal;
   private String methodId;
   private EJBResource ejbResource;
   private EJBMethodPermission ejbMethodPermission;
   private boolean usingAlternateRunAsSubject = false;
   private AuthenticatedSubject runAsSubject;
   private SecurityHelper securityHelper;
   private boolean isEntityBean = false;
   private String txName;

   public MethodDescriptor(BeanInfo var1, String var2, String var3, String var4, Method var5, MethodInfo var6, int var7, int var8, boolean var9, boolean var10) {
      this.applicationName = var2;
      this.ejbComponentName = var3;
      this.ejbName = var4;
      this.txTimeoutMS = var8;
      this.txAttribute = var7;
      this.method = var5;
      this.methodName = var5.getName();
      this.isLocal = var10;
      this.entityAlwaysUsesTransaction = var9;
      this.methodInfo = var6;

      assert var6 != null : "Could not find MethodInfo for method:" + var5;

      this.setupIsolationLevel(var6.getTxIsolationLevel());
      this.txName = "[EJB " + var1.getBeanClassName() + "." + var6.getSignature() + "]";
      if (var1 instanceof EntityBeanInfo) {
         this.isEntityBean = true;
      }

      this.selectForUpdate = var6.getSelectForUpdate();
   }

   public void setupIsolationLevel(int var1) {
      switch (var1) {
         case -1:
            this.isolationLevel = null;
            return;
         case 0:
         case 1:
         case 2:
         case 4:
         case 8:
            this.isolationLevel = new Integer(var1);
            return;
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            throw new AssertionError("Bad isolation level setting" + var1);
      }
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public String getEjbComponentName() {
      return this.ejbComponentName;
   }

   public String getEjbName() {
      return this.ejbName;
   }

   public void setTXAttribute(int var1) {
      this.txAttribute = var1;
   }

   public int getTXAttribute() {
      return this.txAttribute;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.methodName + "(");
      String[] var2 = this.methodInfo.getMethodParams();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.append(var2[var3]);
            if (var3 < var2.length - 1) {
               var1.append(", ");
            }
         }
      }

      var1.append(")");
      return "MethodDescriptor: Method: " + var1.toString() + " Transaction attribute: " + ToStringUtils.txAttributeToString(this.txAttribute) + " Isolation Level: " + ToStringUtils.isoToString(this.isolationLevel) + " Tx Timeout: " + this.txTimeoutMS;
   }

   public boolean isLocal() {
      return this.isLocal;
   }

   Transaction getCallerTx() {
      return TxHelper.getTransaction();
   }

   public Method getMethod() {
      return this.method;
   }

   public void setMethod(Method var1) {
      this.method = var1;
      this.methodName = var1.getName();
   }

   public String getMethodName() {
      return this.methodName;
   }

   public MethodInfo getMethodInfo() {
      return this.methodInfo;
   }

   public int getSelectForUpdate() {
      return this.selectForUpdate;
   }

   private void setSelectForUpdateInTxMaybe(Transaction var1) {
      Object var2 = null;
      switch (this.selectForUpdate) {
         case 0:
            return;
         case 1:
         case 2:
            ((TransactionImpl)var1).setProperty("SELECT_FOR_UPDATE", new Integer(this.selectForUpdate));
            return;
         default:
            throw new AssertionError("Unknown selectForUpdate type: '" + this.selectForUpdate + "'");
      }
   }

   public void setRunAsSubject(AuthenticatedSubject var1) {
      this.runAsSubject = var1;
   }

   public void setEJBResource(EJBResource var1) {
      this.ejbResource = var1;
   }

   public void setEJBMethodPermission(EJBMethodPermission var1) {
      this.ejbMethodPermission = var1;
   }

   public int getTxTimeoutMS() {
      return this.txTimeoutMS;
   }

   public void updateTxTimeoutMS(int var1) {
      this.txTimeoutMS = var1;
   }

   public void setRetryOnRollbackCount(int var1) {
      this.methodInfo.setRetryOnRollbackCount(var1);
   }

   public int getRetryOnRollbackCount() {
      return this.methodInfo.getRetryOnRollbackCount();
   }

   private Transaction startLocalTransaction() throws InvalidTransactionException {
      Transaction var1 = this.startTransaction();
      ((TransactionImpl)var1).setProperty("LOCAL_ENTITY_TX", "true");
      return var1;
   }

   private Transaction startTransaction() throws InvalidTransactionException {
      try {
         TransactionManager var1 = TxHelper.getTransactionManager();
         var1.setTransactionTimeout(this.txTimeoutMS / 1000);
         var1.begin();
         Transaction var2 = var1.getTransaction();
         ((weblogic.transaction.Transaction)var2).setName(this.txName);
         if (this.isolationLevel != null) {
            ((TransactionImpl)var2).setProperty("ISOLATION LEVEL", this.isolationLevel);
         }

         this.setSelectForUpdateInTxMaybe(var2);
         return var2;
      } catch (Exception var3) {
         throw new InvalidTransactionException("Transaction could not begin:\n" + var3.getMessage());
      }
   }

   Object getInvokeTx() throws InternalException {
      Transaction var1 = this.getCallerTx();
      return this.getInvokeTx(var1);
   }

   Object getInvokeTx(Transaction var1) throws InternalException {
      try {
         if (this.entityAlwaysUsesTransaction && this.isEntityBean && var1 != null) {
            TransactionImpl var2 = (TransactionImpl)var1;
            if (var2.getProperty("LOCAL_ENTITY_TX") != null) {
               try {
                  TxHelper.getTransactionManager().suspend();
               } catch (SystemException var5) {
                  EJBRuntimeUtils.throwInternalException("Error suspending tx:", var5);
               }

               var1 = null;
            }
         }

         switch (this.txAttribute) {
            case 0:
               try {
                  TxHelper.getTransactionManager().suspend();
                  if (this.isEntityBean) {
                     if (this.entityAlwaysUsesTransaction) {
                        return this.startLocalTransaction();
                     }

                     return Thread.currentThread();
                  }

                  return null;
               } catch (SystemException var6) {
                  EJBRuntimeUtils.throwInternalException("Error suspending tx:", var6);
               }
            case 2:
               if (this.isEntityBean) {
                  if (var1 == null) {
                     if (this.entityAlwaysUsesTransaction) {
                        return this.startLocalTransaction();
                     }

                     return Thread.currentThread();
                  }

                  this.setSelectForUpdateInTxMaybe(var1);
                  return var1;
               }

               return var1;
            case 1:
               if (var1 == null) {
                  return this.startTransaction();
               }

               this.setSelectForUpdateInTxMaybe(var1);
               return var1;
            case 3:
               try {
                  TxHelper.getTransactionManager().suspend();
               } catch (SystemException var4) {
                  EJBRuntimeUtils.throwInternalException("Error suspending tx:", var4);
               }

               return this.startTransaction();
            case 4:
               if (var1 == null) {
                  throw new TxRequiredException("Method " + this.methodName + " is deployed as TX_MANDATORY, but it was called without " + "a transaction.");
               }

               this.setSelectForUpdateInTxMaybe(var1);
               return var1;
            case 5:
               if (var1 != null) {
                  Loggable var8 = EJBLogger.logTxNerverMethodCalledWithnInTxLoggable(this.methodName);
                  String var3 = var8.getMessage();
                  if (this.isLocal) {
                     throw new EJBException(var3);
                  }

                  throw new RemoteException(var3);
               } else {
                  if (this.isEntityBean) {
                     if (this.entityAlwaysUsesTransaction) {
                        return this.startLocalTransaction();
                     }

                     return Thread.currentThread();
                  }

                  return null;
               }
            default:
               throw new AssertionError("Unexpected tx Attribute:" + this.txAttribute);
         }
      } catch (Exception var7) {
         if (var7 instanceof TxRequiredException) {
            throw (TxRequiredException)var7;
         } else {
            EJBRuntimeUtils.throwInternalException("EJBerror: ", var7);
            throw new AssertionError("Should not reach.");
         }
      }
   }

   Object getInvokeTxForCom(Transaction var1) throws InternalException {
      try {
         try {
            return this.getInvokeTx(var1);
         } catch (TxRequiredException var3) {
            if (this.isLocal) {
               throw new TransactionRequiredLocalException(var3.getMessage());
            } else {
               throw new TransactionRequiredException(var3.getMessage());
            }
         }
      } catch (Exception var4) {
         EJBRuntimeUtils.throwInternalException("EJBerror: ", var4);
         return var1;
      }
   }

   Object getInvokeTxForBus(Transaction var1) throws InternalException {
      try {
         try {
            return this.getInvokeTx(var1);
         } catch (TxRequiredException var3) {
            throw new EJBTransactionRequiredException(var3.getMessage());
         }
      } catch (Exception var4) {
         EJBRuntimeUtils.throwInternalException("EJBerror: ", var4);
         return var1;
      }
   }

   public void setSecurityHelper(SecurityHelper var1) {
      this.securityHelper = var1;
   }

   public void pushRunAsIdentity() {
      this.pushRunAsIdentity((AuthenticatedSubject)null);
   }

   public void pushRunAsIdentity(AuthenticatedSubject var1) {
      if (var1 == null) {
         if (this.runAsSubject != null) {
            if (debugLogger.isDebugEnabled()) {
               debug(" method: '" + this.methodName + "' push RunAs for Subject: '" + this.runAsSubject.toString() + "'");
            }

            SecurityHelper.pushRunAsSubject(this.runAsSubject);
         }
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug(" method: '" + this.methodName + "' push alternateRunAs for Subject: '" + var1.toString() + "'");
         }

         SecurityHelper.pushRunAsSubject(var1);
         this.usingAlternateRunAsSubject = true;
      }

   }

   public void popRunAsIdentity() {
      if (this.usingAlternateRunAsSubject) {
         if (debugLogger.isDebugEnabled()) {
            debug(" method: '" + this.methodName + "' pop RunAs for Alternate Subject");
         }

         SecurityHelper.popRunAsSubject();
         this.usingAlternateRunAsSubject = false;
      } else if (this.runAsSubject != null) {
         if (debugLogger.isDebugEnabled()) {
            debug(" method: '" + this.methodName + "' pop RunAs for Subject: '" + this.runAsSubject.toString() + "'");
         }

         SecurityHelper.popRunAsSubject();
      }

   }

   public boolean checkMethodPermissionsRemote(ContextHandler var1) throws AccessException {
      if (!this.checkMethodPermissions(var1)) {
         if (this.checkExcluded()) {
            Loggable var2 = EJBLogger.logaccessExceptionLoggable(this.methodName);
            throw new AccessException(var2.getMessage());
         } else {
            throw new AccessException(this.getAccessDeniedMessage());
         }
      } else {
         return true;
      }
   }

   public boolean checkMethodPermissionsLocal(ContextHandler var1) throws AccessLocalException {
      if (!this.checkMethodPermissions(var1)) {
         if (this.checkExcluded()) {
            Loggable var2 = EJBLogger.logaccessExceptionLoggable(this.methodName);
            throw new AccessLocalException(var2.getMessage());
         } else {
            throw new AccessLocalException(this.getAccessDeniedMessage());
         }
      } else {
         return true;
      }
   }

   public boolean checkMethodPermissionsBusiness(ContextHandler var1) throws EJBAccessException {
      if (!this.checkMethodPermissions(var1)) {
         if (this.checkExcluded()) {
            Loggable var2 = EJBLogger.logaccessExceptionLoggable(this.methodName);
            throw new EJBAccessException(var2.getMessage());
         } else {
            throw new EJBAccessException(this.getAccessDeniedMessage());
         }
      } else {
         return true;
      }
   }

   public boolean checkMethodPermissions(ContextHandler var1) {
      if (!this.securityHelper.fullyDelegateSecurityCheck()) {
         if (this.checkExcluded()) {
            return false;
         }

         if (this.isUnchecked()) {
            return true;
         }
      }

      return this.checkAccess(var1);
   }

   private boolean checkAccess(ContextHandler var1) {
      if (debugLogger.isDebugEnabled()) {
         debug(this.toString());
         debug("needsSecurityCheck: " + this.methodInfo.needsSecurityCheck());
         debug("subject: " + SecurityHelper.getCurrentSubject());
      }

      if (this.methodInfo.needsSecurityCheck()) {
         this.securityHelper.setContext(var1);
         boolean var2 = this.securityHelper.isAccessAllowed(this.ejbResource, this.ejbMethodPermission, var1);
         if (debugLogger.isDebugEnabled()) {
            debug(var2 ? "  Access Allowed. \n" : "  Access Denied ! \n");
         }

         this.securityHelper.resetContext();
         return var2;
      } else {
         return true;
      }
   }

   public boolean checkExcluded() {
      return this.methodInfo.getIsExcluded();
   }

   public boolean isUnchecked() {
      return this.methodInfo.getUnchecked();
   }

   private String getAccessDeniedMessage() {
      SecurityHelper var10000 = this.securityHelper;
      String var1 = SecurityHelper.getCurrentPrincipal().getName();
      if (var1 == null) {
         var1 = "UNKNOWN";
      }

      String var2 = null;
      if (this.ejbResource != null) {
         var2 = this.ejbResource.toString();
      } else if (this.ejbMethodPermission != null) {
         var2 = this.ejbMethodPermission.toString();
      }

      if (var2 == null) {
         var2 = this.methodName;
      }

      Loggable var3 = EJBLogger.logaccessDeniedOnEJBResourceLoggable(var1, var2);
      return var3.getMessage();
   }

   public void setMethodId(String var1) {
      this.methodId = var1;
   }

   public String getMethodId() {
      return this.methodId;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError("Unable to clone MethodDescriptor", var2);
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[MethodDescriptor] " + var0);
   }

   static {
      debugLogger = EJBDebugService.deploymentLogger;
   }

   public final class TxRequiredException extends RuntimeException {
      private static final long serialVersionUID = 1239800172031557020L;

      public TxRequiredException(String var2) {
         super(var2);
      }
   }
}
