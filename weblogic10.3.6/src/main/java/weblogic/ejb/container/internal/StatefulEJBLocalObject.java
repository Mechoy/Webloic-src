package weblogic.ejb.container.internal;

import javax.ejb.ConcurrentAccessException;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;
import javax.ejb.NoSuchEJBException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.RemoveException;
import javax.transaction.Transaction;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb20.interfaces.LocalHandle;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.ejb20.internal.LocalHandleImpl;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.utils.AssertionError;

public abstract class StatefulEJBLocalObject extends StatefulLocalObject implements BaseEJBLocalObjectIntf {
   protected boolean isIdentical(MethodDescriptor var1, EJBLocalObject var2) throws EJBException {
      if (super.isIdentical(var1, var2)) {
         if (!(var2 instanceof StatefulEJBLocalObject)) {
            return false;
         } else {
            StatefulEJBLocalObject var3 = (StatefulEJBLocalObject)var2;
            return this.primaryKey.equals(var3.getPK());
         }
      } else {
         return false;
      }
   }

   protected void remove(MethodDescriptor var1) throws RemoveException, EJBException {
      BaseEJBLocalHome var2 = (BaseEJBLocalHome)this.getEJBLocalHome();
      Transaction var3 = null;
      InvocationWrapper var4 = null;

      try {
         var2.pushEnvironment();
         this.checkMethodPermissions(var1, EJBContextHandler.EMPTY);

         try {
            MethodInvocationHelper.pushMethodObject(var2.getBeanInfo());
            SecurityHelper.pushCallerPrincipal();
            var1.pushRunAsIdentity();
            if (this.isEJB30ClientView()) {
               var4 = EJBRuntimeUtils.createWrapWithTxsForBus(var1, this.primaryKey);
            } else {
               var4 = EJBRuntimeUtils.createWrapWithTxs(var1, this.primaryKey);
            }

            var3 = var4.getCallerTx();

            try {
               var2.getBeanManager().remove(var4);
            } catch (InternalException var30) {
               if (!this.isEJB30ClientView()) {
                  if (var30.detail instanceof NoSuchEJBException) {
                     NoSuchObjectLocalException var6 = new NoSuchObjectLocalException(var30.getMessage());
                     EJBRuntimeUtils.throwInternalException(var30.getMessage(), var6);
                  }

                  if (var30.detail instanceof ConcurrentAccessException) {
                     throw new InternalException(var30.getMessage());
                  }
               }

               throw var30;
            }
         } catch (InternalException var31) {
            if (var31.detail instanceof RemoveException) {
               throw (RemoveException)var31.detail;
            }

            var2.handleSystemException(var4, var31);
            throw new AssertionError("should not reach");
         } finally {
            var1.popRunAsIdentity();

            try {
               SecurityHelper.popCallerPrincipal();
            } catch (PrincipalNotFoundException var29) {
               EJBLogger.logErrorPoppingCallerPrincipal(var29);
            }

            MethodInvocationHelper.popMethodObject(var2.getBeanInfo());
         }
      } finally {
         var2.popEnvironment();

         try {
            EJBRuntimeUtils.resumeCallersTransaction(var3, (Transaction)null);
         } catch (InternalException var28) {
            EJBRuntimeUtils.throwEJBException(var28);
         }

      }

   }

   public void remove() throws RemoveException {
      throw new Error("NYI");
   }

   public LocalHandle getLocalHandleObject() {
      if (debugLogger.isDebugEnabled()) {
         debug("Getting handle in eo:" + this);
      }

      return new LocalHandleImpl(this, this.primaryKey);
   }

   private static void debug(String var0) {
      debugLogger.debug("[StatefulEJBLocalObject] " + var0);
   }
}
