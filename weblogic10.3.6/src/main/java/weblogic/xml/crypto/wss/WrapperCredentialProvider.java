package weblogic.xml.crypto.wss;

import java.security.cert.X509Certificate;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.bst.ClientBSTCredentialProvider;
import weblogic.wsee.security.bst.StubPropertyBSTCredProv;
import weblogic.wsee.security.serviceref.ServiceRefBSTCredProv;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;

public class WrapperCredentialProvider implements CredentialProvider {
   private CredentialProvider[] delegates = new CredentialProvider[2];
   private String type = null;
   private String[] types = new String[1];

   public WrapperCredentialProvider(CredentialProvider var1, CredentialProvider var2, String var3) throws IllegalArgumentException {
      if (var1 != null && var2 != null) {
         if (supports(var1, var3) && supports(var2, var3)) {
            this.delegates[0] = var1;
            this.delegates[1] = var2;
            this.type = var3;
            this.types[0] = var3;
         } else {
            throw new IllegalArgumentException("Credenetial providers must support type.");
         }
      } else {
         throw new IllegalArgumentException("Credenetial providers must not be null.");
      }
   }

   public String[] getValueTypes() {
      return this.types;
   }

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      if (!this.type.equals(var1)) {
         LogUtils.logWss("Token type " + var1 + " not supported.");
         return null;
      } else {
         for(int var5 = 0; var5 < this.delegates.length; ++var5) {
            CredentialProvider var6 = this.delegates[var5];
            LogUtils.logWss("Trying to get credential for " + var1 + " and purpose " + var4 + " from delegate " + var6);
            Object var7 = var6.getCredential(var1, var2, var3, var4);
            if (var7 != null) {
               LogUtils.logWss("Got credential for " + var1 + " and purpose " + var4 + " from delegate " + var6);
               return var7;
            }
         }

         LogUtils.logWss("No credential found for " + var1 + " and purpose " + var4);
         return null;
      }
   }

   public Object getCredentialByKeyIdentifier(String var1, String var2, ContextHandler var3, Purpose var4) {
      if (!this.type.equals(var1)) {
         LogUtils.logWss("Token type " + var1 + " not supported for KeyIdentifier.");
         return null;
      } else {
         for(int var5 = 0; var5 < this.delegates.length; ++var5) {
            CredentialProvider var6 = this.delegates[var5];
            Object var7 = null;
            if (var6 instanceof WrapperCredentialProvider) {
               LogUtils.logWss("Call getCredentialByKeyIdentifier() to get thumbprint/KeyIdentifier credential for " + var1 + " and purpose " + var4 + " from delegate " + var6);
               var7 = ((WrapperCredentialProvider)var6).getCredentialByKeyIdentifier(var1, var2, var3, var4);
               if (null != var7) {
                  LogUtils.logWss("Thumbs up!");
               }
            } else if (var6 instanceof ServiceRefBSTCredProv) {
               LogUtils.logWss("Thumbs down!");
            } else {
               LogUtils.logWss("Trying to get thumbprint/KeyIdentifier credential for " + var1 + " and purpose " + var4 + " from delegate " + var6);
               var7 = var6.getCredential(var1, var2, var3, var4);
            }

            if (var7 != null) {
               LogUtils.logWss("Got thumbprint/KeyIdentifier credential for " + var1 + " and purpose " + var4 + " from delegate " + var6);
               return var7;
            }
         }

         LogUtils.logWss("No thumbprint/KeyIdentifier credential found for " + var1 + " and purpose " + var4);
         return null;
      }
   }

   public boolean hasClientBSTCredentialProvider() {
      for(int var1 = 0; var1 < this.delegates.length; ++var1) {
         if (this.delegates[var1] instanceof ClientBSTCredentialProvider) {
            return true;
         }

         if (this.delegates[var1] instanceof WrapperCredentialProvider) {
            WrapperCredentialProvider var2 = (WrapperCredentialProvider)this.delegates[var1];
            if (var2.hasClientBSTCredentialProvider()) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean hasStubPropertyBSTCredProv() {
      for(int var1 = 0; var1 < this.delegates.length; ++var1) {
         if (this.delegates[var1] instanceof StubPropertyBSTCredProv) {
            return true;
         }

         if (this.delegates[var1] instanceof WrapperCredentialProvider) {
            WrapperCredentialProvider var2 = (WrapperCredentialProvider)this.delegates[var1];
            if (var2.hasStubPropertyBSTCredProv()) {
               return true;
            }
         }
      }

      return false;
   }

   public WrapperCredentialProvider replaceServerCertOnClientBSTCredentialProvider(X509Certificate var1) {
      CredentialProvider[] var2 = new CredentialProvider[2];

      for(int var3 = 0; var3 < this.delegates.length; ++var3) {
         if (this.delegates[var3] instanceof ClientBSTCredentialProvider) {
            ClientBSTCredentialProvider var6 = (ClientBSTCredentialProvider)this.delegates[var3];
            var2[var3] = var6.cloneAndReplaceServerCert(var1);
         } else if (this.delegates[var3] instanceof StubPropertyBSTCredProv) {
            StubPropertyBSTCredProv var5 = (StubPropertyBSTCredProv)this.delegates[var3];
            var2[var3] = var5.cloneAndReplaceServerCert(var1);
         } else if (this.delegates[var3] instanceof WrapperCredentialProvider) {
            WrapperCredentialProvider var4 = (WrapperCredentialProvider)this.delegates[var3];
            if (!var4.hasClientBSTCredentialProvider() && !var4.hasStubPropertyBSTCredProv()) {
               var2[var3] = this.delegates[var3];
            } else {
               var2[var3] = var4.replaceServerCertOnClientBSTCredentialProvider(var1);
            }
         } else {
            var2[var3] = this.delegates[var3];
         }
      }

      return new WrapperCredentialProvider(var2[0], var2[1], this.type);
   }

   public static boolean supports(CredentialProvider var0, String var1) {
      String[] var2 = var0.getValueTypes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].equals(var1)) {
            return true;
         }
      }

      return false;
   }
}
