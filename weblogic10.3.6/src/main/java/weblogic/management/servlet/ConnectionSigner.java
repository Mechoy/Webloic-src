package weblogic.management.servlet;

import com.bea.security.utils.random.SecureRandomData;
import java.io.IOException;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.servlet.http.HttpServletRequest;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.HMAC;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.encoders.BASE64Decoder;
import weblogic.utils.encoders.BASE64Encoder;

public class ConnectionSigner {
   public static final String REQUEST_SALT = "wls_salt";
   public static final String REQUEST_SIGNATURE = "wls_signature";
   private static byte[] domainWideSecret = null;
   private static boolean gotSecret = false;
   private static byte[] credentialBytes = new byte[]{78, 19, 9, 37, 37, 7, 81, 99, 37, 36, 7, 91, 5};
   private static String signatureString = null;
   private static String saltString = null;
   private static boolean isSaltInitialized = false;
   private static AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private static byte[] getSecret() {
      if (!gotSecret) {
         Class var0 = ConnectionSigner.class;
         synchronized(ConnectionSigner.class) {
            if (!gotSecret) {
               PrivilegedAction var1 = new PrivilegedAction() {
                  public Object run() {
                     SecurityConfigurationMBean var1 = ManagementService.getRuntimeAccess(ConnectionSigner.KERNEL_ID).getDomain().getSecurityConfiguration();
                     return var1.getCredential();
                  }
               };
               String var2 = (String)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, var1);
               domainWideSecret = var2.getBytes();
               gotSecret = true;
            }
         }
      }

      return domainWideSecret;
   }

   public static void signConnection(URLConnection var0, AuthenticatedSubject var1) {
      SecurityServiceManager.checkKernelIdentity(var1);
      if (!isSaltInitialized) {
         Class var2 = ConnectionSigner.class;
         synchronized(ConnectionSigner.class) {
            if (!isSaltInitialized) {
               byte[] var3 = SecureRandomData.getInstance().getRandomBytes(16);
               BASE64Encoder var4 = new BASE64Encoder();
               saltString = var4.encodeBuffer(var3);
               signatureString = var4.encodeBuffer(HMAC.digest(credentialBytes, getSecret(), var3));
               isSaltInitialized = true;
            }
         }
      }

      var0.setRequestProperty("wls_salt", saltString);
      var0.setRequestProperty("wls_signature", signatureString);
   }

   public static boolean authenticate(String var0, String var1) {
      byte[] var2;
      byte[] var3;
      try {
         BASE64Decoder var4 = new BASE64Decoder();
         var2 = var4.decodeBuffer(var0);
         var3 = var4.decodeBuffer(var1);
      } catch (IOException var5) {
         return false;
      }

      return HMAC.verify(var3, credentialBytes, getSecret(), var2);
   }

   public static boolean isConnectionSigned(HttpServletRequest var0) {
      try {
         String var1 = var0.getHeader("wls_salt");
         if (var1 != null) {
            String var2 = var0.getHeader("wls_signature");
            if (var2 == null) {
               return false;
            }

            return authenticate(var1, var2);
         }
      } catch (Exception var3) {
      }

      return false;
   }
}
