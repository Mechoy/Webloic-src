package weblogic.iiop.csi;

import weblogic.security.acl.internal.AuthenticatedSubject;

public final class SecurityContext {
   private long clientContextId;
   private EstablishContext establishedContext;
   private AuthenticatedSubject subject;

   public SecurityContext() {
   }

   public SecurityContext(long var1, EstablishContext var3, AuthenticatedSubject var4) {
      this.clientContextId = var1;
      this.establishedContext = var3;
      this.subject = var4;
   }

   public long getClientContextId() {
      return this.clientContextId;
   }

   public AuthenticatedSubject getSubject() {
      return this.subject;
   }

   public EstablishContext getEstablishContext() {
      return this.establishedContext;
   }

   public String toString() {
      return "SecurityContext (clientContext = " + this.clientContextId + ", subject = " + this.subject + ", ctx = " + this.establishedContext + ")";
   }
}
