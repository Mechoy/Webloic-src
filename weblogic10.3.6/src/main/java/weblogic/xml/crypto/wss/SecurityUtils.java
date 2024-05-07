package weblogic.xml.crypto.wss;

import java.security.AccessController;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.xml.crypto.wss.api.UsernameToken;

public class SecurityUtils {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final Map AUTHENTICATORS = new HashMap();

   public static AuthenticatedSubject assertIdentity(X509Certificate[] var0, String var1) throws LoginException {
      try {
         return getPrincipalAuthenticator(var1).assertIdentity("X.509", var0);
      } catch (SecurityException var3) {
         return null;
      } catch (ClassCastException var4) {
         return null;
      }
   }

   public static Subject assertIdentity(UsernameToken var0, String var1) throws WSSecurityException {
      Subject var2 = null;
      AuthenticatedSubject var3 = null;
      UsernameToken var4 = var0;
      String var5 = var0.getPasswordType();

      try {
         if (var5.equals("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText")) {
            var3 = assertId(var4.getUsername(), var4.getPassword(), var1);
         } else {
            var3 = assertId(var4, var1);
         }
      } catch (LoginException var7) {
         throw new WSSecurityException("Failed to assert identity with UsernameToken.", WSSConstants.FAILURE_AUTH);
      }

      if (var3 != null) {
         var2 = var3.getSubject();
      }

      if (var2 != null) {
         return var2;
      } else {
         throw new WSSecurityException("Failed to get subject from UsernameToken.", WSSConstants.FAILURE_AUTH);
      }
   }

   private static AuthenticatedSubject assertId(String var0, byte[] var1, String var2) throws LoginException {
      SimpleCallbackHandler var3 = new SimpleCallbackHandler(var0, var1);
      return getPrincipalAuthenticator(var2).authenticate(var3);
   }

   private static AuthenticatedSubject assertId(UsernameToken var0, String var1) throws LoginException {
      return getPrincipalAuthenticator(var1).assertIdentity("wsse:PasswordDigest", var0);
   }

   private static PrincipalAuthenticator getPrincipalAuthenticator(String var0) {
      if (var0 == null) {
         var0 = "weblogicDEFAULT";
      }

      PrincipalAuthenticator var1 = (PrincipalAuthenticator)AUTHENTICATORS.get(var0);
      if (var1 == null) {
         var1 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(getKernelID(), var0, ServiceType.AUTHENTICATION);
         AUTHENTICATORS.put(var0, var1);
      }

      return var1;
   }

   private static AuthenticatedSubject getKernelID() {
      return KERNEL_ID;
   }

   public static boolean isTokenTypeSupported(String var0) {
      return getPrincipalAuthenticator((String)null).isTokenTypeSupported(var0);
   }

   public static boolean isPasswordDigestSupported() {
      return isTokenTypeSupported("wsse:PasswordDigest");
   }

   public static boolean isX509Supported() {
      return isTokenTypeSupported("X.509");
   }

   public static boolean isSAMLTokenSupported() {
      return isTokenTypeSupported("SAML.Assertion.DOM");
   }
}
