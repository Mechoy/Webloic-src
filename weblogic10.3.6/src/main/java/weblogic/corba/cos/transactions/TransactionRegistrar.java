package weblogic.corba.cos.transactions;

import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.transaction.xa.XAException;
import org.omg.CosTransactions.Inactive;
import org.omg.CosTransactions.RecoveryCoordinator;
import weblogic.iiop.IIOPLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public final class TransactionRegistrar extends WorkAdapter {
   private static final boolean DEBUG = false;
   static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private PropagationContextImpl ctx;
   private ResourceImpl res;
   private AuthenticatedSubject subject;

   TransactionRegistrar(PropagationContextImpl var1, ResourceImpl var2) {
      this.ctx = var1;
      this.res = var2;
      this.ctx.requiresRegistration();
      this.subject = SecurityServiceManager.getCurrentSubject(kernelId);
      WorkManagerFactory.getInstance().getSystem().schedule(this);
   }

   public void run() {
      SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedAction() {
         public Object run() {
            try {
               if (OTSHelper.isDebugEnabled()) {
                  IIOPLogger.logDebugOTS("registering " + TransactionRegistrar.this.ctx);
               }

               RecoveryCoordinator var1 = TransactionRegistrar.this.ctx.getPropagationContext().current.coord.register_resource(TransactionRegistrar.this.res);
               TransactionRegistrar.this.res.registerForRecovery(var1);
               TransactionRegistrar.this.ctx.notifyRegistration();
            } catch (Inactive var2) {
               TransactionRegistrar.this.ctx.notifyRegistration(new XAException(-7));
            } catch (Throwable var3) {
               IIOPLogger.logOTSError("resource registration failed", var3);
               TransactionRegistrar.this.ctx.notifyRegistration(var3);
            }

            return null;
         }
      });
   }

   private static final void p(String var0) {
      System.err.println("<TransactionRegistrar> " + var0);
   }
}
