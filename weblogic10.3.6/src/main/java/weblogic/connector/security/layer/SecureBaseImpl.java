package weblogic.connector.security.layer;

import java.security.AccessController;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.kernel.Kernel;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

class SecureBaseImpl {
   protected Object myObj;
   protected RAInstanceManager raIM;

   SecureBaseImpl(Object var1, RAInstanceManager var2, String var3, AuthenticatedSubject var4) {
      if (!SecurityServiceManager.isKernelIdentity(var4)) {
         throw new SecurityException("KernelId is required to instantiate SecureBaseImpl class, Subject '" + (var4 == null ? "<null>" : var4.toString()) + "' is not the kernel identity");
      } else {
         if (var1 == null) {
            Debug.throwAssertionError(var3 + " == null");
         }

         this.myObj = var1;
         this.raIM = var2;
      }
   }

   public boolean equals(Object var1) {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.raIM.getAdapterLayer().pushSubject(var2);

      boolean var3;
      try {
         var3 = this.myObj.equals(var1);
      } finally {
         this.raIM.getAdapterLayer().popSubject(var2);
      }

      return var3;
   }

   public int hashCode() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.raIM.getAdapterLayer().pushSubject(var1);

      int var2;
      try {
         var2 = this.myObj.hashCode();
      } finally {
         this.raIM.getAdapterLayer().popSubject(var1);
      }

      return var2;
   }

   public String toString() {
      if (Kernel.isServer()) {
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         this.raIM.getAdapterLayer().pushSubject(var1);

         String var2;
         try {
            var2 = this.myObj.toString();
         } finally {
            this.raIM.getAdapterLayer().popSubject(var1);
         }

         return var2;
      } else {
         return this.myObj.toString();
      }
   }

   public Object getSourceObj() {
      return this.myObj;
   }
}
