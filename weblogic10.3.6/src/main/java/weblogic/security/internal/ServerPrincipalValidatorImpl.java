package weblogic.security.internal;

import java.security.AccessController;
import java.security.Principal;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.HMAC;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.principal.WLSServerIdentity;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.spi.PrincipalValidator;

public class ServerPrincipalValidatorImpl implements PrincipalValidator {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private byte[] secret;

   public boolean validate(Principal var1) throws SecurityException {
      if (!(var1 instanceof WLSServerIdentity)) {
         return false;
      } else {
         WLSServerIdentity var2 = (WLSServerIdentity)var1;
         byte[] var3 = var2.getSignature();
         if (var3 == null) {
            return false;
         } else {
            byte[] var4 = var2.getSignedData();
            byte[] var5 = var2.getSalt();
            return HMAC.verify(var3, var4, this.getSecret(), var5);
         }
      }
   }

   public boolean sign(Principal var1) {
      if (!(var1 instanceof WLSServerIdentity)) {
         return false;
      } else {
         WLSServerIdentity var2 = (WLSServerIdentity)var1;
         SecurityServiceManager.checkKernelPermission();
         byte[] var3 = var2.getSignedData();
         byte[] var4 = var2.getSalt();
         var2.setSignature(HMAC.digest(var3, this.getSecret(), var4));
         return true;
      }
   }

   public Class getPrincipalBaseClass() {
      return WLSServerIdentity.class;
   }

   private byte[] getSecret() {
      if (this.secret == null) {
         RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
         this.secret = var1.getDomain().getSecurityConfiguration().getCredential().getBytes();
      }

      return this.secret;
   }
}
