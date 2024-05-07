package weblogic.connector.security.layer;

import java.security.AccessController;
import javax.transaction.xa.Xid;
import weblogic.connector.common.RAInstanceManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class XidImpl extends SecureBaseImpl implements Xid {
   public XidImpl(Xid var1, RAInstanceManager var2, AuthenticatedSubject var3) {
      super(var1, var2, "Xid", var3);
   }

   public int getFormatId() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.raIM.getAdapterLayer().pushSubject(var1);

      int var2;
      try {
         var2 = ((Xid)this.myObj).getFormatId();
      } finally {
         this.raIM.getAdapterLayer().popSubject(var1);
      }

      return var2;
   }

   public byte[] getGlobalTransactionId() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.raIM.getAdapterLayer().pushSubject(var1);

      byte[] var2;
      try {
         var2 = ((Xid)this.myObj).getGlobalTransactionId();
      } finally {
         this.raIM.getAdapterLayer().popSubject(var1);
      }

      return var2;
   }

   public byte[] getBranchQualifier() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.raIM.getAdapterLayer().pushSubject(var1);

      byte[] var2;
      try {
         var2 = ((Xid)this.myObj).getBranchQualifier();
      } finally {
         this.raIM.getAdapterLayer().popSubject(var1);
      }

      return var2;
   }
}
