package weblogic.security.pk;

import java.security.cert.CertPathParameters;
import java.security.cert.X509Certificate;
import weblogic.security.SecurityLogger;
import weblogic.security.service.ContextHandler;

public final class CertPathBuilderParameters implements CertPathParameters {
   private String realmName;
   private CertPathSelector selector;
   private X509Certificate[] trustedCAs;
   private ContextHandler context;

   public CertPathBuilderParameters(String var1, CertPathSelector var2, X509Certificate[] var3, ContextHandler var4) {
      if (var1 != null && var1.length() >= 1) {
         if (var2 == null) {
            throw new IllegalArgumentException(SecurityLogger.getCertPathBuilderParametersIllegalCertPathSelector());
         } else {
            this.realmName = var1;
            this.selector = var2;
            this.trustedCAs = var3;
            this.context = var4;
         }
      } else {
         throw new IllegalArgumentException(SecurityLogger.getCertPathBuilderParametersIllegalRealm());
      }
   }

   public String getRealmName() {
      return this.realmName;
   }

   public CertPathSelector getSelector() {
      return this.selector;
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
