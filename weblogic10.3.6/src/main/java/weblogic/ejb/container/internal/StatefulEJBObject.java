package weblogic.ejb.container.internal;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import javax.ejb.ConcurrentAccessException;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.NoSuchEJBException;
import javax.ejb.RemoveException;
import javax.transaction.Transaction;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.StatefulEJBObjectIntf;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.ejb20.internal.HandleImpl;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;

public abstract class StatefulEJBObject extends StatefulRemoteObject implements StatefulEJBObjectIntf {
   protected Handle getHandleObject() throws RemoteException {
      if (debugLogger.isDebugEnabled()) {
         debug("Getting handle in eo:" + this);
      }

      return new HandleImpl(this, this.primaryKey);
   }

   protected final Object getPrimaryKeyObject() throws RemoteException {
      Loggable var1 = EJBLogger.logsessionBeanCannotCallGetPrimaryKeyLoggable();
      throw new RemoteException(var1.getMessage());
   }

   public boolean isIdentical(MethodDescriptor var1, EJBObject var2) throws RemoteException {
      if (super.isIdentical(var1, var2)) {
         StatefulEJBObjectIntf var3 = (StatefulEJBObjectIntf)var2;
         return this.primaryKey.equals(var3.getPK());
      } else {
         return false;
      }
   }

   protected void remove(MethodDescriptor var1) throws RemoteException, RemoveException {
      Transaction var2 = null;
      InvocationWrapper var3 = null;
      BaseEJBHome var4 = (BaseEJBHome)this.getEJBHome();

      try {
         var4.pushEnvironment();
         this.checkMethodPermissions(var1, EJBContextHandler.EMPTY);

         try {
            MethodInvocationHelper.pushMethodObject(var4.getBeanInfo());
            SecurityHelper.pushCallerPrincipal();
            var1.pushRunAsIdentity();
            if (!this.isImplementsRemote()) {
               var3 = EJBRuntimeUtils.createWrapWithTxsForBus(var1, this.primaryKey);
            } else {
               var3 = EJBRuntimeUtils.createWrapWithTxs(var1, this.primaryKey);
            }

            var2 = var3.getCallerTx();

            try {
               var4.getBeanManager().remove(var3);
            } catch (InternalException var31) {
               if (this.isImplementsRemote()) {
                  if (var31.detail instanceof NoSuchEJBException) {
                     NoSuchObjectException var6 = new NoSuchObjectException(var31.getMessage());
                     EJBRuntimeUtils.throwInternalException(var31.getMessage(), var6);
                  }

                  if (var31.detail instanceof ConcurrentAccessException) {
                     Throwable var35 = var31.detail.getCause();
                     EJBRuntimeUtils.throwInternalException(var31.getMessage(), var35);
                  }
               }

               throw var31;
            }
         } catch (InternalException var32) {
            if (var32.detail instanceof RemoveException) {
               throw (RemoveException)var32.detail;
            }

            if (var32.detail instanceof RemoteException) {
               throw (RemoteException)var32.detail;
            }

            var4.handleSystemException(var3, var32);
            throw new AssertionError("should not reach");
         } finally {
            StatefulEJBHome var9 = (StatefulEJBHome)var4;
            if (var9.getIsNoObjectActivation() || var9.getIsInMemoryReplication()) {
               var9.releaseEO(this.primaryKey);
               var4.unexportEO(this, false);
            }

            var1.popRunAsIdentity();

            try {
               SecurityHelper.popCallerPrincipal();
            } catch (PrincipalNotFoundException var30) {
               EJBLogger.logErrorPoppingCallerPrincipal(var30);
            }

            MethodInvocationHelper.popMethodObject(var4.getBeanInfo());
         }
      } finally {
         var4.popEnvironment();

         try {
            EJBRuntimeUtils.resumeCallersTransaction(var2, (Transaction)null);
         } catch (InternalException var29) {
            EJBRuntimeUtils.throwRemoteException(var29);
         }

      }

   }

   public void remove() throws RemoveException, RemoteException {
      throw new Error("NYI");
   }

   private static void debug(String var0) {
      debugLogger.debug("[StatefulEJBObject] " + var0);
   }
}
