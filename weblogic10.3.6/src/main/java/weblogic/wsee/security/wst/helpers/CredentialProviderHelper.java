package weblogic.wsee.security.wst.helpers;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import weblogic.wsee.security.bst.ClientBSTCredentialProvider;
import weblogic.wsee.security.bst.StubPropertyBSTCredProv;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.WrapperCredentialProvider;

public class CredentialProviderHelper {
   private static final boolean verbose = Verbose.isVerbose(CredentialProviderHelper.class);

   public static List replaceCredentialProviderWithNewCert(List var0, X509Certificate var1) {
      if (null != var1 && null != var0 && !var0.isEmpty()) {
         if (verbose) {
            Verbose.say("Replace source of  server cert is:" + var1.toString());
         }

         ArrayList var2 = new ArrayList();
         boolean var3 = false;

         for(int var4 = 0; var4 < var0.size(); ++var4) {
            Object var5 = null;
            Object var6 = var0.get(var4);
            if (var6 instanceof ClientBSTCredentialProvider) {
               if (verbose) {
                  Verbose.say("Replace ClientBSTCredentialProvider with this server cert ");
               }

               ClientBSTCredentialProvider var9 = (ClientBSTCredentialProvider)var6;
               var5 = var9.cloneAndReplaceServerCert(var1);
               if (verbose) {
                  Verbose.say("Replace ClientBSTCredentialProvider with this server cert ");
               }
            } else if (var6 instanceof WrapperCredentialProvider) {
               WrapperCredentialProvider var8 = (WrapperCredentialProvider)var6;
               if (var8.hasClientBSTCredentialProvider() || var8.hasStubPropertyBSTCredProv()) {
                  var5 = var8.replaceServerCertOnClientBSTCredentialProvider(var1);
                  if (verbose) {
                     Verbose.say("Replace ClientBSTCredentialProvider in WrapperCredentialProvider with this server cert ");
                  }
               }
            } else if (var6 instanceof StubPropertyBSTCredProv) {
               StubPropertyBSTCredProv var7 = (StubPropertyBSTCredProv)var6;
               var5 = var7.cloneAndReplaceServerCert(var1);
            }

            if (null != var5) {
               var2.add(var5);
               var3 = true;
            } else {
               var2.add(var6);
            }
         }

         if (!var3) {
            var2.add(new StubPropertyBSTCredProv(var1, (X509Certificate)null));
         }

         if (verbose) {
            Verbose.say("New Credential Provider List is returned witth size = " + var2.size() + " that replaced the old list size = " + var0.size());
         }

         return var2;
      } else {
         return var0;
      }
   }
}
