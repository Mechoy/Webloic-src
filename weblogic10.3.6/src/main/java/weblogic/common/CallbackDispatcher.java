package weblogic.common;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import weblogic.rjvm.RemoteInvokable;
import weblogic.rjvm.RemoteRequest;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public final class CallbackDispatcher implements RemoteInvokable {
   private final ClientCallback callback;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public CallbackDispatcher(ClientCallback var1) {
      this.callback = var1;
   }

   public void invoke(final RemoteRequest var1) throws RemoteException {
      WorkManagerFactory.getInstance().getSystem().schedule(new WorkAdapter() {
         public void run() {
            try {
               CallbackDispatcher.this.dispatch(var1);
            } catch (Exception var2) {
               CommonLogger.logCallbackFailed("error during dispatch", var2);
            }

         }
      });
   }

   private void dispatch(final RemoteRequest var1) throws Exception {
      SecurityServiceManager.runAs(kernelId, (AuthenticatedSubject)var1.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws IOException, ClassNotFoundException {
            CallbackDispatcher.this.callback.dispatch((Throwable)null, var1.readObjectWL());
            return null;
         }
      });
   }
}
