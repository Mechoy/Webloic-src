package weblogic.wsee.security.util;

import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.security.bst.ClientBSTCredentialProvider;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.X509Credential;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.security.utils.Utils;

public class BSTCredentialProviderUtil {
   private static final boolean verbose = Verbose.isVerbose(BSTCredentialProviderUtil.class);
   private static final String X509V3_VALUE_TYPE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3";

   public static void dumpBST(String var0, MessageContext var1) {
      try {
         Verbose.banner("BST --" + var0);
         Map var2 = WSSecurityContext.getCredentialProviders(var1);
         if (null == var2) {
            if (verbose) {
               Verbose.log((Object)"No crdential provider found");
            }

            return;
         }

         CredentialProvider var3 = (CredentialProvider)var2.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
         if (null == var3) {
            if (verbose) {
               Verbose.log((Object)"No X509V3 crdential provider found");
            }

            return;
         }

         if (var3 instanceof ClientBSTCredentialProvider) {
            Verbose.log((Object)("ClientBSTCredentialProvider CP =" + var3.toString() + "\n"));
         } else {
            Verbose.log((Object)("CredentialProvider CP =" + var3.toString() + "\n"));
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static X509Credential findX509Credential(MessageContext var0, X509Certificate var1) {
      if (verbose) {
         Verbose.log((Object)("Finding ClientBSTCredentialProvider that has X509 cert =" + dumpX509CertInfo(var1)));
      }

      WSSecurityContext var2 = WSSecurityContext.getSecurityContext(var0);
      if (null == var2) {
         if (verbose) {
            Verbose.log((Object)"No securityContext found");
         }

         return null;
      } else {
         try {
            CredentialProvider var3 = var2.getRequiredCredentialProvider("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
            Object var4 = var3.getCredential("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3", (String)null, new SecurityTokenContextHandler(var2), Purpose.SIGN);
            if (var4 != null && var4 instanceof X509Credential) {
               X509Credential var5 = (X509Credential)var4;
               if (var1.equals(var5.getCertificate())) {
                  if (verbose) {
                     Verbose.log((Object)"Found the credendtial for the given X509 cert");
                  }

                  return (X509Credential)var4;
               } else {
                  if (verbose) {
                     Verbose.log((Object)("Unable to find the matched X509 credentail for valueType = http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3 credential found was " + var5));
                  }

                  return null;
               }
            } else {
               Verbose.log((Object)("Unable to find the X509 credentail for valueType = http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3 credential is " + var4));
               return null;
            }
         } catch (WSSecurityException var6) {
            var6.printStackTrace();
            Verbose.logException(var6);
            return null;
         }
      }
   }

   public static String dumpX509CertInfo(X509Certificate var0) {
      if (null == var0) {
         return "Null";
      } else {
         StringBuffer var1 = new StringBuffer();

         try {
            Principal var2 = var0.getSubjectDN();
            var1.append(var0.getSubjectDN());
            var1.append(" SKI=");
            byte[] var3 = Utils.getSubjectKeyIdentifier(var0);
            if (null != var3 && var3.length != 0) {
               var1.append(Utils.toBase64(var3));
            } else {
               var1.append("Null");
            }

            var1.append(" TP=");
            byte[] var4 = weblogic.xml.crypto.utils.CertUtils.getThumbprint(var0);
            var1.append(Utils.toBase64(var4));
         } catch (Exception var5) {
            var1.append("? exception=" + var5.getMessage());
         }

         return var1.toString();
      }
   }
}
