package weblogic.iiop.csi;

import weblogic.iiop.ServiceContext;
import weblogic.iiop.VendorInfoSecurity;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.service.SecurityServiceManager;

public final class ClientSecurityContext {
   private long clientContextId;
   private ServiceContext messageInContext;
   private boolean needCredentials = false;
   private boolean contextEstablished = false;

   public ClientSecurityContext(long var1, SASServiceContext var3) {
      this.clientContextId = var1;
      this.messageInContext = var3;
      this.needCredentials = true;
   }

   public ClientSecurityContext(AuthenticatedSubject var1) {
      this.clientContextId = -1L;
      this.needCredentials = false;
      if (!SubjectUtils.isUserAnonymous(var1) && !SecurityServiceManager.isKernelIdentity(var1)) {
         AuthenticatedUser var2 = SecurityServiceManager.convertToAuthenticatedUser(var1);
         this.messageInContext = new VendorInfoSecurity(var2);
      } else {
         this.messageInContext = VendorInfoSecurity.ANONYMOUS;
      }

   }

   public long getClientContextId() {
      return this.clientContextId;
   }

   public boolean needCredentials() {
      return this.needCredentials;
   }

   public void setNeedCredentials(boolean var1) {
      this.needCredentials = var1;
   }

   public void contextEstablished() {
      if (!this.contextEstablished) {
         this.messageInContext = new SASServiceContext(this.clientContextId);
         this.messageInContext.premarshal();
         this.contextEstablished = true;
      }

   }

   public ServiceContext getMessageInContext() {
      return this.messageInContext;
   }

   public String toString() {
      return this.clientContextId >= 0L ? this.messageInContext.toString() + " (context id==" + this.clientContextId + ")" : this.messageInContext.toString();
   }
}
