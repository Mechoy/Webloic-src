package weblogic.work;

import weblogic.security.acl.internal.AuthenticatedSubject;

public abstract class ServerWorkAdapter extends WorkAdapter {
   private AuthenticatedSubject subject;

   public ServerWorkAdapter() {
   }

   public ServerWorkAdapter(AuthenticatedSubject var1) {
      this.subject = var1;
   }

   protected AuthenticatedSubject getAuthenticatedSubject() {
      return this.subject;
   }

   final void setWorkManager(SelfTuningWorkManagerImpl var1) {
      super.setWorkManager(var1);
      if (var1 != null) {
         if (var1.getRequestClass() instanceof ContextRequestClass) {
            this.requestClass = ((ContextRequestClass)var1.getRequestClass()).getEffective(this.getAuthenticatedSubject());
         }

         this.subject = null;
      }
   }
}
