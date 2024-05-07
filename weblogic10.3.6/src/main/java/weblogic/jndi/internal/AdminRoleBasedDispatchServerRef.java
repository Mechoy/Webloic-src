package weblogic.jndi.internal;

import java.rmi.RemoteException;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.BasicServerRef;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class AdminRoleBasedDispatchServerRef extends BasicServerRef {
   public AdminRoleBasedDispatchServerRef(Object var1) throws RemoteException {
      super(var1);
   }

   public AdminRoleBasedDispatchServerRef(int var1, Object var2) throws RemoteException {
      super(var1, var2);
   }

   protected WorkManager getWorkManager(RuntimeMethodDescriptor var1, AuthenticatedSubject var2) {
      return this.subjectHasAdminQOS(var2) ? WorkManagerFactory.getInstance().getSystem() : WorkManagerFactory.getInstance().getDefault();
   }

   private boolean subjectHasAdminQOS(AuthenticatedSubject var1) {
      if (SecurityServiceManager.isKernelIdentity(var1)) {
         return true;
      } else {
         return var1 != null && var1.getQOS() == 103;
      }
   }
}
