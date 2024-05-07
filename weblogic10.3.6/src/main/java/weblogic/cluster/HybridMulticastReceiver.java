package weblogic.cluster;

import java.security.AccessController;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class HybridMulticastReceiver extends MulticastReceiver {
   private boolean httpReqDispatched;
   private int senderNum;
   private WorkManager workManager;
   private final HostID memberID;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   HybridMulticastReceiver(HostID var1, int var2) {
      this(var1, var2, WorkManagerFactory.getInstance().getSystem());
   }

   HybridMulticastReceiver(HostID var1, int var2, WorkManager var3) {
      super(var1, var2, var3);
      this.senderNum = var2;
      this.workManager = var3;
      this.memberID = var1;
   }

   void processLastSeqNum(long var1) {
      if (var1 >= this.currentSeqNum) {
         this.fetchStateDumpOverHttp(var1);
      }

   }

   void setInSync(int var1) {
      synchronized(this) {
         this.httpReqDispatched = false;
         super.setInSync((long)var1);
      }
   }

   protected void setOutOfSync() {
   }

   void setHttpRequestDispatched(boolean var1) {
      synchronized(this) {
         this.httpReqDispatched = false;
      }
   }

   private void fetchStateDumpOverHttp(long var1) {
      if (!this.httpReqDispatched) {
         synchronized(this) {
            this.httpReqDispatched = true;
         }

         HTTPExecuteRequest var3 = new HTTPExecuteRequest(var1, this.senderNum, this.memberID);
         this.workManager.schedule(var3);
      }
   }

   synchronized void dispatch(long var1, int var3, int var4, int var5, boolean var6, boolean var7, byte[] var8) {
      if (!this.httpReqDispatched) {
         super.dispatch(var1, var3, var4, var5, var6, var7, var8);
      }
   }
}
