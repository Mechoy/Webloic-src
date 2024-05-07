package weblogic.xml.crypto.utils;

import java.security.cert.X509Certificate;
import javax.xml.rpc.handler.MessageContext;
import weblogic.security.SSL.TrustManager;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public final class ClientBSTUtils {
   private static final String SERVER_VERIFY_CERT = "weblogic.wsee.security.bst.serverVerifyCert";
   private static final String SERVER_ENCRYPT_CERT = "weblogic.wsee.security.bst.serverEncryptCert";
   private static final boolean VALIDATION_OFF = Boolean.getBoolean("weblogic.xml.crypto.wss.X509ValidationOff");

   public static boolean isTrusted(SecurityToken var0, MessageContext var1, WSSecurityContext var2, ContextHandler var3) throws WSSecurityException {
      if (VALIDATION_OFF) {
         return true;
      } else {
         X509Certificate var4 = ((BinarySecurityToken)var0).getCertificate();
         TrustManager var5 = (TrustManager)var2.getProperty("weblogic.wsee.security.wss.TrustManager");
         if (var5 != null) {
            X509Certificate[] var11 = new X509Certificate[]{var4};
            return var5.certificateCallback(var11, 16);
         } else {
            X509Certificate var6 = (X509Certificate)var1.getProperty("weblogic.wsee.security.bst.serverEncryptCert");
            if (var6 == null) {
               throw new WSSecurityException("Could not validate certificate: no TrustManager set and no server cert set.");
            } else if (var6.equals(var4)) {
               return true;
            } else {
               X509Certificate var7 = (X509Certificate)var1.getProperty("weblogic.wsee.security.bst.serverVerifyCert");
               if (var7 != null && var7.equals(var4)) {
                  return true;
               } else {
                  Purpose var8 = (Purpose)var1.getProperty("weblogic.xml.crypto.wss.provider.Purpose");
                  if (var8 == null) {
                     return false;
                  } else {
                     CredentialProvider var9 = var2.getCredentialProvider("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
                     Object var10 = var9.getCredential("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3", (String)null, var3, var8);
                     if (var10 != null) {
                        return true;
                     } else {
                        var9 = var2.getCredentialProvider("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1");
                        var10 = var9.getCredential("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1", (String)null, var3, var8);
                        return var10 != null;
                     }
                  }
               }
            }
         }
      }
   }
}
