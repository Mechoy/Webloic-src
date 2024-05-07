package weblogic.connector.security.layer;

import java.security.AccessController;
import javax.resource.NotSupportedException;
import javax.resource.spi.work.ExecutionContext;
import javax.transaction.xa.Xid;
import weblogic.connector.common.RAInstanceManager;
import weblogic.kernel.Kernel;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ExecutionContextImpl extends ExecutionContext {
   SecureBaseImpl secureBase;

   public ExecutionContextImpl(ExecutionContext var1, RAInstanceManager var2, AuthenticatedSubject var3) {
      this.secureBase = new SecureBaseImpl(var1, var2, "ExecutionContext", var3);
   }

   public long getTransactionTimeout() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.secureBase.raIM.getAdapterLayer().pushSubject(var1);

      long var2;
      try {
         var2 = ((ExecutionContext)this.secureBase.myObj).getTransactionTimeout();
      } finally {
         this.secureBase.raIM.getAdapterLayer().popSubject(var1);
      }

      return var2;
   }

   public Xid getXid() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.secureBase.raIM.getAdapterLayer().pushSubject(var1);

      XidImpl var3;
      try {
         Xid var2 = ((ExecutionContext)this.secureBase.myObj).getXid();
         if (var2 != null) {
            var3 = new XidImpl(var2, this.secureBase.raIM, var1);
            return var3;
         }

         var3 = null;
      } finally {
         this.secureBase.raIM.getAdapterLayer().popSubject(var1);
      }

      return var3;
   }

   public void setTransactionTimeout(long var1) throws NotSupportedException {
      AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.secureBase.raIM.getAdapterLayer().pushSubject(var3);

      try {
         ((ExecutionContext)this.secureBase.myObj).setTransactionTimeout(var1);
      } finally {
         this.secureBase.raIM.getAdapterLayer().popSubject(var3);
      }

   }

   public void setXid(Xid var1) {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.secureBase.raIM.getAdapterLayer().pushSubject(var2);

      try {
         ((ExecutionContext)this.secureBase.myObj).setXid(var1);
      } finally {
         this.secureBase.raIM.getAdapterLayer().popSubject(var2);
      }

   }

   public boolean equals(Object var1) {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.secureBase.raIM.getAdapterLayer().pushSubject(var2);

      boolean var3;
      try {
         var3 = this.secureBase.myObj.equals(var1);
      } finally {
         this.secureBase.raIM.getAdapterLayer().popSubject(var2);
      }

      return var3;
   }

   public int hashCode() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.secureBase.raIM.getAdapterLayer().pushSubject(var1);

      int var2;
      try {
         var2 = this.secureBase.myObj.hashCode();
      } finally {
         this.secureBase.raIM.getAdapterLayer().popSubject(var1);
      }

      return var2;
   }

   public String toString() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (Kernel.isServer()) {
         this.secureBase.raIM.getAdapterLayer().pushSubject(var1);

         String var2;
         try {
            var2 = this.secureBase.myObj.toString();
         } finally {
            this.secureBase.raIM.getAdapterLayer().popSubject(var1);
         }

         return var2;
      } else {
         return this.secureBase.myObj.toString();
      }
   }
}
