package weblogic.ejb.container.internal;

import com.oracle.pitchfork.interfaces.intercept.__ProxyControl;
import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EJBException;
import javax.ejb.EnterpriseBean;
import javax.ejb.NoSuchEJBException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.TransactionRolledbackLocalException;
import javax.naming.Context;
import javax.persistence.OptimisticLockException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionRolledbackException;
import weblogic.ejb.OptimisticConcurrencyException;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.deployer.MBeanDeploymentInfoImpl;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb20.persistence.spi.PersistenceRuntimeException;
import weblogic.health.HealthMonitorService;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.jndi.factories.java.javaURLContextFactory;
import weblogic.transaction.TimedOutException;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TransactionSystemException;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.utils.NestedRuntimeException;
import weblogic.utils.StackTraceUtils;

public final class EJBRuntimeUtils {
   private static final boolean debug = false;
   private static final boolean ignoreStackTraceInMsg = Boolean.getBoolean("weblogic.ejb.ignoreStackTraceInExceptionMessage");
   private static final HashMap ejbStateMap = new HashMap();
   private static final HashMap ejbAllowedActionsMap;

   public static void throwException(Throwable var0) throws Exception {
      if (var0 instanceof Exception) {
         throw (Exception)var0;
      } else {
         throw (Error)var0;
      }
   }

   public static void throwRemoteException(InternalException var0) throws RemoteException {
      throwRemoteException("EJB Exception: ", var0);
   }

   public static void throwRemoteException(String var0, Throwable var1) throws RemoteException {
      if (var1 instanceof OutOfMemoryError) {
         HealthMonitorService.panic(var1);
      }

      Object var2 = null;
      if (var1 instanceof InternalException) {
         InternalException var3 = (InternalException)var1;
         if (var3.detail == null) {
            throw new RemoteException(var3.getMessage() + ": " + StackTraceUtils.throwable2StackTrace(var3));
         }

         var1 = var3.detail;
         var0 = var3.getMessage();
      }

      if (var1 instanceof RollbackException) {
         throwTransactionRolledback(var0, var1);
      } else if (var1 instanceof NoSuchEntityException) {
         var2 = new NoSuchObjectException(var0 + ": " + StackTraceUtils.throwable2StackTrace(var1));
      } else if (var1 instanceof RemoteException) {
         var2 = (RemoteException)var1;
      } else {
         var2 = new RemoteException(var0, var1);
      }

      throw var2;
   }

   public static void throwEJBException(InternalException var0) throws EJBException {
      throwEJBException("EJB Exception: ", var0);
   }

   public static void throwEJBException(String var0, Throwable var1) throws EJBException {
      if (var1 instanceof InternalException) {
         InternalException var2 = (InternalException)var1;
         if (var2.detail == null) {
            throw new EJBException(var2.getMessage() + ": " + StackTraceUtils.throwable2StackTrace(var2));
         }

         var1 = var2.detail;
         var0 = var2.getMessage();
      }

      if (var1 instanceof RollbackException) {
         throwTransactionRolledbackLocal(var0, var1);
      } else if (var1 instanceof NoSuchEntityException) {
         NoSuchObjectLocalException var4 = new NoSuchObjectLocalException(var1.getMessage() + ": " + StackTraceUtils.throwable2StackTrace(var1), (Exception)var1);
         var4.initCause(var1);
         throw var4;
      } else if (var1 instanceof EJBException) {
         throw (EJBException)var1;
      } else if (var1 instanceof NoSuchObjectException) {
         throw new NoSuchObjectLocalException(var1.getMessage() + ": " + StackTraceUtils.throwable2StackTrace(var1));
      } else if (var1 instanceof Exception) {
         EJBException var3 = new EJBException(var0 + ": " + StackTraceUtils.throwable2StackTrace(var1), (Exception)var1);
         var3.initCause(var1);
         throw var3;
      } else {
         throw new EJBException(var0 + ": " + StackTraceUtils.throwable2StackTrace(var1));
      }
   }

   public static void throwInternalException(String var0, Throwable var1) throws InternalException {
      if (var1 instanceof InternalException) {
         throw (InternalException)var1;
      } else if (var1 instanceof PersistenceRuntimeException) {
         PersistenceRuntimeException var2 = (PersistenceRuntimeException)var1;
         if (var2.getCausedByException() instanceof InternalException) {
            throw (InternalException)var2.getCausedByException();
         } else {
            throw new InternalException(var0, var2.getCausedByException());
         }
      } else {
         throw new InternalException(var0, var1);
      }
   }

   public static void throwTransactionRolledback(String var0, Throwable var1) throws RemoteException {
      if (var1 instanceof InternalException) {
         InternalException var2 = (InternalException)var1;
         if (var2.detail == null) {
            throw new TransactionRolledbackException(var2.getMessage() + ": " + StackTraceUtils.throwable2StackTrace(var2));
         }

         var1 = var2.detail;
         var0 = var2.getMessage();
      }

      var1 = unwrapRollbackException(var1);
      String var4 = ignoreStackTraceInMsg ? var0 : var0 + ": " + StackTraceUtils.throwable2StackTrace(var1);
      TransactionRolledbackException var3 = new TransactionRolledbackException(var4);
      var3.detail = var1;
      throw var3;
   }

   public static void throwWrappedTransactionRolledback(String var0, Throwable var1) throws InternalException {
      try {
         throwTransactionRolledback(var0, var1);
      } catch (RemoteException var3) {
         throw new InternalException("EJB Exception: ", var3);
      }
   }

   public static void throwTransactionRolledbackLocal(String var0, Throwable var1) throws TransactionRolledbackLocalException {
      if (var1 instanceof InternalException) {
         InternalException var2 = (InternalException)var1;
         if (var2.detail == null) {
            throw new TransactionRolledbackLocalException(var2.getMessage() + ": " + StackTraceUtils.throwable2StackTrace(var1));
         }

         var1 = var2.detail;
         var0 = var2.getMessage();
      }

      var1 = unwrapRollbackException(var1);
      if (var1 instanceof Exception) {
         TransactionRolledbackLocalException var5 = new TransactionRolledbackLocalException(var0, (Exception)var1);
         var5.initCause(var1);
         throw var5;
      } else {
         String var4 = ignoreStackTraceInMsg ? var0 : var0 + ": " + StackTraceUtils.throwable2StackTrace(var1);
         TransactionRolledbackLocalException var3 = new TransactionRolledbackLocalException(var4);
         var3.initCause(var1);
         throw var3;
      }
   }

   public static void throwWrappedTransactionRolledbackLocal(String var0, Throwable var1) throws InternalException {
      try {
         throwTransactionRolledbackLocal(var0, var1);
      } catch (TransactionRolledbackLocalException var3) {
         throw new InternalException("EJB Exception: ", var3);
      }
   }

   public static void throwRuntimeException(String var0, InternalException var1) {
      Object var2 = null;
      if (var1.detail == null) {
         var2 = new NestedRuntimeException(var0, var1);
      } else if (var1.detail instanceof RuntimeException) {
         var2 = (RuntimeException)var1.detail;
      } else {
         var2 = new NestedRuntimeException(var0, var1.detail);
      }

      throw var2;
   }

   private static Throwable unwrapRollbackException(Throwable var0) {
      if (var0 instanceof weblogic.transaction.RollbackException) {
         weblogic.transaction.RollbackException var1 = (weblogic.transaction.RollbackException)var0;
         if (var1.getNested() != null) {
            for(var0 = var1.getNested(); var0 instanceof NestedRuntimeException && var0.getCause() != null; var0 = ((NestedRuntimeException)var0).getNestedException()) {
            }
         }
      }

      return var0;
   }

   public static boolean isSpecialSystemException(Throwable var0) {
      if (var0 instanceof InternalException) {
         InternalException var1 = (InternalException)var0;
         if (var1.detail != null) {
            var0 = var1.detail;
         }
      }

      return var0 instanceof NoSuchObjectException || var0 instanceof NoSuchEntityException || var0 instanceof NoSuchObjectLocalException || var0 instanceof NoSuchEJBException;
   }

   static InvocationWrapper createWrapWithTxs(MethodDescriptor var0) throws InternalException {
      Transaction var1 = var0.getCallerTx();
      Transaction var2 = null;
      Thread var3 = null;
      Object var4 = var0.getInvokeTxForCom(var1);
      if (var4 instanceof Transaction) {
         var2 = (Transaction)var4;
      } else {
         var3 = (Thread)var4;
      }

      InvocationWrapper var5 = new InvocationWrapper(var1, var2, var3, var0);
      return var5;
   }

   static InvocationWrapper createWrapWithTxsForBus(MethodDescriptor var0) throws InternalException {
      Transaction var1 = var0.getCallerTx();
      Transaction var2 = null;
      Thread var3 = null;
      Object var4 = var0.getInvokeTxForBus(var1);
      if (var4 instanceof Transaction) {
         var2 = (Transaction)var4;
      } else {
         var3 = (Thread)var4;
      }

      InvocationWrapper var5 = new InvocationWrapper(var1, var2, var3, var0);
      return var5;
   }

   public static InvocationWrapper createWrap() {
      Object var0 = null;
      Object var1 = null;
      Thread var2 = Thread.currentThread();
      InvocationWrapper var3 = new InvocationWrapper((Transaction)var0, (Transaction)var1, var2);
      return var3;
   }

   public static Object getInvokeTxOrThread() {
      weblogic.transaction.Transaction var0 = TxHelper.getTransaction();
      return var0 == null ? Thread.currentThread() : var0;
   }

   static InvocationWrapper createWrapWithTxs(MethodDescriptor var0, Object var1) throws InternalException {
      InvocationWrapper var2 = createWrapWithTxs(var0);
      var2.setPrimaryKey(var1);
      return var2;
   }

   static InvocationWrapper createWrapWithTxsForBus(MethodDescriptor var0, Object var1) throws InternalException {
      InvocationWrapper var2 = createWrapWithTxsForBus(var0);
      var2.setPrimaryKey(var1);
      return var2;
   }

   static void setWrapWithTxs(InvocationWrapper var0) throws InternalException {
      MethodDescriptor var1 = var0.getMethodDescriptor();
      Transaction var2 = var1.getCallerTx();
      Transaction var3 = null;
      Thread var4 = null;
      Object var5 = var1.getInvokeTxForCom(var2);
      if (var5 instanceof Transaction) {
         var3 = (Transaction)var5;
      } else {
         var4 = (Thread)var5;
      }

      var0.setInvokeTx(var3);
      var0.setThread(var4);
   }

   static void setWrapWithTxsForBus(InvocationWrapper var0) throws InternalException {
      MethodDescriptor var1 = var0.getMethodDescriptor();
      Transaction var2 = var1.getCallerTx();
      Transaction var3 = null;
      Thread var4 = null;
      Object var5 = var1.getInvokeTxForBus(var2);
      if (var5 instanceof Transaction) {
         var3 = (Transaction)var5;
      } else {
         var4 = (Thread)var5;
      }

      var0.setInvokeTx(var3);
      var0.setThread(var4);
   }

   public static boolean isAppExceptionNeedtoRollback(DeploymentInfo var0, Throwable var1) {
      Map var2 = var0.getApplicationExceptions();
      ConcurrentHashMap var3 = ((MBeanDeploymentInfoImpl)var0).getRollBackExeptionsCache();
      Class var4 = var1.getClass();
      Boolean var5 = (Boolean)var3.get(var4);
      if (var5 == null) {
         String var6 = var4.getName();
         if (var2.containsKey(var6)) {
            var5 = (Boolean)var2.get(var6);
            var3.put(var4, var5);
            return var5;
         } else if (((MBeanDeploymentInfoImpl)var0).addApplicationException(var4)) {
            return isAppExceptionNeedtoRollback(var0, var1);
         } else {
            var3.put(var4, false);
            return false;
         }
      } else {
         return var5;
      }
   }

   public static boolean isAppException(BeanInfo var0, Method var1, Throwable var2) {
      if (!var0.isEJB30()) {
         return isAppException(var1, var2);
      } else if (Error.class.isAssignableFrom(var2.getClass())) {
         return false;
      } else if (RemoteException.class.isAssignableFrom(var2.getClass())) {
         return false;
      } else if (!RuntimeException.class.isAssignableFrom(var2.getClass())) {
         return true;
      } else {
         Map var3 = var0.getDeploymentInfo().getApplicationExceptions();
         Class var4 = var2.getClass();
         MBeanDeploymentInfoImpl var5 = (MBeanDeploymentInfoImpl)var0.getDeploymentInfo();
         ConcurrentHashMap var6 = var5.getApplicationExeptionsCache();
         Boolean var7 = (Boolean)var6.get(var4);
         if (var7 == null) {
            String var8 = var4.getName();
            if (var3.containsKey(var8)) {
               var6.put(var4, true);
               return true;
            } else if (var5.addApplicationException(var4)) {
               return isAppException(var0, var1, var2);
            } else {
               var6.put(var4, false);
               return false;
            }
         } else {
            return var7;
         }
      }
   }

   public static boolean isAppException(Method var0, Throwable var1) {
      return isAppException((Method)var0, (Class[])null, var1);
   }

   public static boolean isAppException(Method var0, Class[] var1, Throwable var2) {
      if (var2 instanceof InternalException) {
         InternalException var3 = (InternalException)var2;
         if (var3.detail == null) {
            return false;
         }

         var2 = var3.detail;
      } else if (var2 instanceof PersistenceRuntimeException) {
         var2 = ((PersistenceRuntimeException)var2).getCausedByException();
      }

      if (var2 instanceof RemoteException) {
         return false;
      } else if (var2 instanceof RuntimeException) {
         return false;
      } else {
         Class var6 = var2.getClass();
         Class[] var4 = var0.getExceptionTypes();

         int var5;
         for(var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].isAssignableFrom(var6)) {
               return true;
            }
         }

         if (var1 != null) {
            for(var5 = 0; var5 < var1.length; ++var5) {
               if (var1[var5].isAssignableFrom(var6)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static boolean runningInOurTx(InvocationWrapper var0) {
      Transaction var1 = var0.getInvokeTx();
      Transaction var2 = var0.getCallerTx();
      if (var1 == null) {
         return false;
      } else {
         return !var1.equals(var2);
      }
   }

   public static boolean runningInCallerTx(InvocationWrapper var0) {
      Transaction var1 = var0.getInvokeTx();
      Transaction var2 = var0.getCallerTx();
      return var1 == null ? false : var1.equals(var2);
   }

   public static void pushEnvironment(Context var0) {
      javaURLContextFactory.pushContext(var0);
   }

   public static void popEnvironment() {
      javaURLContextFactory.popContext();
   }

   public static void resumeCallersTransaction(Transaction var0, Transaction var1) throws InternalException {
      try {
         if (var0 != null && !var0.equals(var1)) {
            TransactionManager var2 = TxHelper.getTransactionManager();

            try {
               var2.resume(var0);
            } catch (InvalidTransactionException var4) {
               var2.forceResume(var0);
            }
         }

      } catch (SystemException var5) {
         EJBLogger.logErrorResumingTx(var5);
         throw new InternalException("Error resuming caller's transaction", var5);
      }
   }

   public static boolean isRollback(InvocationWrapper var0) {
      boolean var1 = false;
      Transaction var2 = var0.getInvokeTx();

      try {
         if (var0.getBean() != null && var2 != null) {
            int var3 = var2.getStatus();
            if (var3 == 1 || var3 == 9 || var3 == 4) {
               var1 = true;
            }
         }
      } catch (SystemException var4) {
      }

      return var1;
   }

   public static boolean isSystemRollback(InvocationWrapper var0) {
      boolean var1 = false;
      Transaction var2 = var0.getInvokeTx();
      Throwable var3 = ((TransactionImpl)var2).getRollbackReason();
      if (var3 instanceof TimedOutException || var3 instanceof TransactionSystemException) {
         var1 = true;
      }

      return var1;
   }

   static String getEJBStateAsString(int var0) {
      return (String)ejbStateMap.get(new Integer(var0));
   }

   static void addOperationToejbAllowedActionsMap(Object var0, String var1) {
      String var2 = (String)ejbAllowedActionsMap.get(var0);
      if (var2 != null) {
         var1 = var2 + " OR '" + var1 + "'";
      } else {
         var1 = "'" + var1 + "'";
      }

      ejbAllowedActionsMap.put(var0, var1);
   }

   static String ejbEJBOperationAsString(int var0) {
      return (String)ejbAllowedActionsMap.get(new Integer(var0));
   }

   public static boolean beanEq(Class var0, EnterpriseBean var1, PitchforkContext var2) {
      if (var1 == null) {
         return false;
      } else if (var1 instanceof __ProxyControl) {
         Class var3 = var2.getPitchforkUtils().getTargetClass(var1);
         return var3 == var0;
      } else {
         return var0 == var1.getClass();
      }
   }

   public static boolean isOptimisticLockException(Throwable var0) {
      return var0 instanceof OptimisticLockException || var0 instanceof OptimisticConcurrencyException;
   }

   static {
      ejbStateMap.put(new Integer(1), "context setting");
      ejbStateMap.put(new Integer(2), "context unsetting");
      ejbStateMap.put(new Integer(4), "ejbCreate");
      ejbStateMap.put(new Integer(8), "ejbPostCreate");
      ejbStateMap.put(new Integer(16), "ejbRemove");
      ejbStateMap.put(new Integer(32), "ejbActivate");
      ejbStateMap.put(new Integer(64), "ejbPassivate");
      ejbStateMap.put(new Integer(128), "running business method");
      ejbStateMap.put(new Integer(256), "after begining business method");
      ejbStateMap.put(new Integer(512), "before completing business method");
      ejbStateMap.put(new Integer(1024), "after completing business method");
      ejbStateMap.put(new Integer(2048), "finder method");
      ejbStateMap.put(new Integer(4096), "select method");
      ejbStateMap.put(new Integer(8192), "home method");
      ejbStateMap.put(new Integer(16384), "ejbLoad");
      ejbStateMap.put(new Integer(32768), "ejbStore");
      ejbStateMap.put(new Integer(65536), "ejbTimeout");
      ejbStateMap.put(new Integer(131072), "webservice business method");
      ejbAllowedActionsMap = new HashMap();
      addOperationToejbAllowedActionsMap(new Integer(258047), "getting ejb home");
      addOperationToejbAllowedActionsMap(new Integer(2036), "getting caller principal for the Stateful EJB");
      addOperationToejbAllowedActionsMap(new Integer(196736), "getting caller principal for the Stateless EJB");
      addOperationToejbAllowedActionsMap(new Integer(125084), "getting caller principal for Entity EJB");
      addOperationToejbAllowedActionsMap(new Integer(2036), "is caller in role for Stateful EJB");
      addOperationToejbAllowedActionsMap(new Integer(196736), "is caller in role for Stateless EJB");
      addOperationToejbAllowedActionsMap(new Integer(125084), "is caller in role for Entity EJB");
      addOperationToejbAllowedActionsMap(new Integer(198644), "getting EJB object for Session bean");
      addOperationToejbAllowedActionsMap(new Integer(114936), "getting EJB object for Entity bean");
      addOperationToejbAllowedActionsMap(new Integer(197504), "getting rollback only for session bean");
      addOperationToejbAllowedActionsMap(new Integer(125084), "getting rollback only for entity bean");
      addOperationToejbAllowedActionsMap(new Integer(197504), "setting rollback only for session bean");
      addOperationToejbAllowedActionsMap(new Integer(125084), "setting rollback only for entity bean");
      addOperationToejbAllowedActionsMap(new Integer(196852), "getting the user transaction");
      addOperationToejbAllowedActionsMap(new Integer(114936), "getting the primary key");
      addOperationToejbAllowedActionsMap(new Integer(254204), "getting the Timer Service");
   }
}
