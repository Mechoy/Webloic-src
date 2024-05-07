package weblogic.security.pk;

import java.security.cert.CertPathParameters;
import java.security.cert.X509Certificate;
import weblogic.security.SecurityLogger;
import weblogic.security.service.ContextHandler;

public final class CertPathValidatorParameters implements CertPathParameters {
   private String realmName;
   private X509Certificate[] trustedCAs;
   private ContextHandler context;

   public CertPathValidatorParameters(String var1, X509Certificate[] var2, ContextHandler var3) {
      if (var1 != null && var1.length() >= 1) {
         this.realmName = var1;
         this.trustedCAs = var2;
         this.context = var3;
      } else {
         throw new IllegalArgumentException(SecurityLogger.getCertPathValidatorParametersIllegalRealm());
      }
   }

   public String getRealmName() {
      return this.realmName;
   }

   public X509Certificate[] getTrustedCAs() {
      return this.trustedCAs;
   }

   public ContextHandler getContext() {
      return this.context;
   }

   public Object clone() {
      throw new UnsupportedOperationException();
   }
}
