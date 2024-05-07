package weblogic.wtc.wls;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.intf.TCAuthenticatedUser;
import java.io.Serializable;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;

public final class WlsAuthenticatedUser implements TCAuthenticatedUser, Serializable {
   static final long serialVersionUID = 3257277652989361369L;
   private AuthenticatedSubject _subject = null;
   private AuthenticatedSubject _kid = null;
   private boolean traceEnabled = false;

   public WlsAuthenticatedUser() {
      this.traceEnabled = ntrace.isTraceEnabled(4);
      if (this.traceEnabled) {
         ntrace.doTrace("[ WlsAuthenticatedUser()");
         this._kid = WlsSecurityService.getKernelUser();
         this._subject = SecurityServiceManager.getCurrentSubject(this._kid);
         ntrace.doTrace("] WlsAuthenticatedUser/10");
      } else {
         this._kid = WlsSecurityService.getKernelUser();
         this._subject = SecurityServiceManager.getCurrentSubject(this._kid);
      }

   }

   WlsAuthenticatedUser(AuthenticatedSubject var1) {
      this.traceEnabled = ntrace.isTraceEnabled(4);
      if (this.traceEnabled) {
         ntrace.doTrace("[ WlsAuthenticatedUser(" + var1 + ")");
         this._kid = WlsSecurityService.getKernelUser();
         this._subject = var1;
         ntrace.doTrace("] WlsAuthenticatedUser/10");
      } else {
         this._kid = WlsSecurityService.getKernelUser();
         this._subject = var1;
      }

   }

   public void setAsCurrentUser() {
      if (this.traceEnabled) {
         ntrace.doTrace("[/WlsAuthenticatedUser/setAsCurrentUser()");
         SecurityServiceManager.pushSubject(this._kid, this._subject);
         ntrace.doTrace("]/WlsAuthenticatedUser/setAsCurrentUser/10");
      } else {
         SecurityServiceManager.pushSubject(this._kid, this._subject);
      }

   }

   public Object[] getPrincipals() {
      Object[] var1;
      if (this.traceEnabled) {
         ntrace.doTrace("[/WlsAuthenticatedUser/getPrincipals()");
         var1 = this._subject.getPrincipals().toArray();
         ntrace.doTrace("]/WlsAuthenticatedUser/getPrincipals/10/return length = " + var1.length);
      } else {
         var1 = this._subject.getPrincipals().toArray();
      }

      return var1;
   }

   public AuthenticatedSubject getWlsSubject() {
      return this._subject;
   }
}
