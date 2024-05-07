package weblogic.security.SSL.jsseadapter;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import javax.net.ssl.CertPathTrustManagerParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import weblogic.kernel.Kernel;
import weblogic.security.pki.revocation.common.RevocationCertPathChecker;
import weblogic.security.pki.revocation.wls.WlsCertRevocContext;

class JaTrustManager implements X509TrustManager {
   private final X509Certificate[] trustedCAs;
   private final Set<TrustAnchor> trustAnchors;
   private X509TrustManager xTm;
   private static final String ID_CE_BASIC_CONSTRAINTS = "2.5.29.19";
   private static final int CERT_X509_V1 = 1;
   private static final int CERT_X509_V3 = 3;

   JaTrustManager(X509Certificate[] var1) {
      this.trustedCAs = this.copyCerts(var1);
      this.trustAnchors = Collections.unmodifiableSet(createTrustAnchors(var1));
      TrustManagerFactory var2 = null;

      try {
         var2 = TrustManagerFactory.getInstance("PKIX");
         if (Kernel.isServer()) {
            X509CertSelector var3 = new X509CertSelector();
            PKIXBuilderParameters var4 = new PKIXBuilderParameters(this.trustAnchors, var3);
            var4.setRevocationEnabled(false);
            Set var5 = this.toUnmodifiableSet(this.trustedCAs);
            WlsCertRevocContext var6 = new WlsCertRevocContext(var5);
            RevocationCertPathChecker var7 = RevocationCertPathChecker.getInstance(var6);
            var4.addCertPathChecker(var7);
            CertPathTrustManagerParameters var8 = new CertPathTrustManagerParameters(var4);
            var2.init(var8);
         } else {
            KeyStore var10 = KeyStore.getInstance(KeyStore.getDefaultType());
            var10.load((InputStream)null, (char[])null);
            loadCerts(var10, this.trustedCAs);
            var2.init(var10);
         }
      } catch (Exception var9) {
         if (JaLogger.isLoggable(Level.WARNING)) {
            JaLogger.log(Level.WARNING, JaLogger.Component.TRUSTSTORE_MANAGER, var9, "Error initializing trust manager factory: {0}.", var9.getMessage());
         }
      }

      if (var2 != null) {
         TrustManager[] var11 = var2.getTrustManagers();
         TrustManager[] var12 = var11;
         int var13 = var11.length;

         for(int var14 = 0; var14 < var13; ++var14) {
            TrustManager var15 = var12[var14];
            if (var15 instanceof X509TrustManager) {
               this.xTm = (X509TrustManager)var15;
               break;
            }
         }
      }

      if (null == this.xTm) {
         if (JaLogger.isLoggable(Level.WARNING)) {
            JaLogger.log(Level.WARNING, JaLogger.Component.TRUSTSTORE_MANAGER, "Unable to determine TrustManager.");
         }

         throw new IllegalStateException("Unable to determine TrustManager.");
      }
   }

   public void checkClientTrusted(X509Certificate[] var1, String var2) throws CertificateException {
      this.xTm.checkClientTrusted(var1, var2);
      this.checkCertPath(var1);
   }

   public void checkServerTrusted(X509Certificate[] var1, String var2) throws CertificateException {
      this.xTm.checkServerTrusted(var1, var2);
      this.checkCertPath(var1);
   }

   public X509Certificate[] getAcceptedIssuers() {
      X509Certificate[] var1;
      if (!Boolean.getBoolean("weblogic.security.SSL.sendEmptyCAList")) {
         var1 = this.copyCerts(this.trustedCAs);
      } else {
         var1 = new X509Certificate[0];
      }

      return var1;
   }

   void checkCertPath(X509Certificate[] var1) throws CertificateException {
      if ((null == var1 || var1.length <= 0) && JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.TRUSTSTORE_MANAGER, "Empty peer certificate chain.");
      }

      PKIXCertPathBuilderResult var2 = this.buildPKIXCertPath(var1);
      if (this.hasCertPath(var2)) {
         if (JaSSLSupport.isNoV1CAs() && this.hasV1CAs(var2)) {
            if (JaLogger.isLoggable(Level.WARNING)) {
               JaLogger.log(Level.WARNING, JaLogger.Component.TRUSTSTORE_MANAGER, "The certificate path has a version 1 CA certificate. Version 1 CA certificates are disallowed.");
            }

            throw new CertificateException("The certificate path has a version 1 CA certificate. Version 1 CA certificates are disallowed.");
         }

         if (JaSSLSupport.isX509BasicConstraintsStrict() && !this.isBasicConstraintsExtensionMarkedCritical(var2)) {
            if (JaLogger.isLoggable(Level.WARNING)) {
               JaLogger.log(Level.WARNING, JaLogger.Component.TRUSTSTORE_MANAGER, "The Basic Constraints extension of at least one of the version 3 CA certificates in the chain is not marked critical.  This is being rejected due to the strict enforcement of Basic Constraints.");
            }

            throw new CertificateException("The Basic Constraints extension of at least one of the version 3 CA certificates in the chain is not marked critical.  This is being rejected due to the strict enforcement of Basic Constraints.");
         }
      } else if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.TRUSTSTORE_MANAGER, "Additional cert path checks encountered empty cert path.");
      }

   }

   private static void loadCerts(KeyStore var0, Certificate[] var1) {
      Certificate[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Certificate var5 = var2[var4];

         try {
            if (null == var5) {
               if (JaLogger.isLoggable(Level.FINEST)) {
                  JaLogger.log(Level.FINEST, JaLogger.Component.TRUSTSTORE_MANAGER, "Null trusted certificate encountered.");
               }
            } else {
               var0.setCertificateEntry(var5.toString(), var5);
            }
         } catch (KeyStoreException var7) {
            if (JaLogger.isLoggable(Level.WARNING)) {
               JaLogger.log(Level.WARNING, JaLogger.Component.TRUSTSTORE_MANAGER, var7, "Unable to add certificate to keystore: cert={0}, message={1}.", var5.toString(), var7.getMessage());
            }
         }
      }

   }

   boolean isBasicConstraintsExtensionMarkedCritical(PKIXCertPathBuilderResult var1) {
      CertPath var2 = var1.getCertPath();
      boolean var3 = true;
      Iterator var4 = var2.getCertificates().iterator();

      while(var4.hasNext()) {
         Certificate var5 = (Certificate)var4.next();
         if (var3) {
            var3 = false;
         } else if (!(var5 instanceof X509Certificate)) {
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.TRUSTSTORE_MANAGER, "Critical Basic Constraint Extensions check skipping non-X509Certificate instance: {0}", var5);
            }
         } else {
            X509Certificate var6 = (X509Certificate)var5;
            if (var6.getVersion() != 3) {
               if (JaLogger.isLoggable(Level.FINER)) {
                  JaLogger.log(Level.FINER, JaLogger.Component.TRUSTSTORE_MANAGER, "Checking for Critical Basic Constraint Extensions, skipping non-v3 cert: Version={0}, SubjectDN={1}.", var6.getVersion(), var6.getSubjectDN().toString());
               }
            } else {
               Set var7 = var6.getCriticalExtensionOIDs();
               if (!isElementFound(var7, "2.5.29.19")) {
                  if (JaLogger.isLoggable(Level.FINE)) {
                     JaLogger.log(Level.FINE, JaLogger.Component.TRUSTSTORE_MANAGER, "Found v3 cert without critical BasicConstraints extension: {0}", var6.getSubjectDN().toString());
                  }

                  return false;
               }
            }
         }
      }

      TrustAnchor var8 = var1.getTrustAnchor();
      X509Certificate var9 = var8.getTrustedCert();
      if (var9.getVersion() != 3) {
         if (JaLogger.isLoggable(Level.FINER)) {
            JaLogger.log(Level.FINER, JaLogger.Component.TRUSTSTORE_MANAGER, "Checking for Critical Basic Constraint Extensions, skipping non-v3 anchor cert: Version={0}, SubjectDN={1}.", var9.getVersion(), var9.getSubjectDN().toString());
         }

         return true;
      } else {
         Set var10 = var9.getCriticalExtensionOIDs();
         if (!isElementFound(var10, "2.5.29.19")) {
            if (JaLogger.isLoggable(Level.FINE)) {
               JaLogger.log(Level.FINE, JaLogger.Component.TRUSTSTORE_MANAGER, "Found v3 anchor cert without critical BasicConstraints extension: {0}", var9.getSubjectDN().toString());
            }

            return false;
         } else {
            return true;
         }
      }
   }

   boolean hasV1CAs(PKIXCertPathBuilderResult var1) {
      CertPath var2 = var1.getCertPath();
      boolean var3 = true;
      Iterator var4 = var2.getCertificates().iterator();

      while(var4.hasNext()) {
         Certificate var5 = (Certificate)var4.next();
         if (var3) {
            var3 = false;
         } else if (!(var5 instanceof X509Certificate)) {
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.TRUSTSTORE_MANAGER, "V1 CA certificate check skipping non-X509Certificate instance: {0}", var5);
            }
         } else {
            X509Certificate var6 = (X509Certificate)var5;
            if (var6.getVersion() == 1) {
               if (JaLogger.isLoggable(Level.FINE)) {
                  JaLogger.log(Level.FINE, JaLogger.Component.TRUSTSTORE_MANAGER, "Found version 1 certificate: {0}", var6.getSubjectDN().toString());
               }

               return true;
            }
         }
      }

      TrustAnchor var7 = var1.getTrustAnchor();
      X509Certificate var8 = var7.getTrustedCert();
      if (var8.getVersion() == 1) {
         if (JaLogger.isLoggable(Level.FINE)) {
            JaLogger.log(Level.FINE, JaLogger.Component.TRUSTSTORE_MANAGER, "Found version 1 anchor certificate: {0}", var8.getSubjectDN().toString());
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean isElementFound(Set<String> var0, String var1) {
      if (var0 != null && var1 != null && var0.size() > 0) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (var1.equalsIgnoreCase(var3)) {
               return true;
            }
         }
      }

      return false;
   }

   static Set<TrustAnchor> createTrustAnchors(X509Certificate[] var0) {
      HashSet var1 = new HashSet();
      if (null != var0 && var0.length > 0) {
         X509Certificate[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            X509Certificate var5 = var2[var4];
            if (null == var5) {
               if (JaLogger.isLoggable(Level.FINEST)) {
                  JaLogger.log(Level.FINEST, JaLogger.Component.TRUSTSTORE_MANAGER, "Null certificate encountered while populating trust anchors.");
               }
            } else {
               var1.add(new TrustAnchor(var5, (byte[])null));
            }
         }

         return var1;
      } else {
         if (JaLogger.isLoggable(Level.WARNING)) {
            JaLogger.log(Level.WARNING, JaLogger.Component.TRUSTSTORE_MANAGER, "No trusted CAs available to populate trust anchors.");
         }

         return var1;
      }
   }

   PKIXCertPathBuilderResult buildPKIXCertPath(X509Certificate[] var1) throws CertificateException {
      try {
         X509CertSelector var3 = new X509CertSelector();
         var3.setSubject(var1[0].getSubjectX500Principal());
         PKIXBuilderParameters var4 = new PKIXBuilderParameters(this.trustAnchors, var3);
         var4.addCertStore(CertStore.getInstance("Collection", new CollectionCertStoreParameters(Arrays.asList(var1))));
         var4.setRevocationEnabled(false);
         CertPathBuilder var5 = CertPathBuilder.getInstance("PKIX");
         PKIXCertPathBuilderResult var2 = (PKIXCertPathBuilderResult)var5.build(var4);
         return var2;
      } catch (Exception var6) {
         if (JaLogger.isLoggable(Level.WARNING)) {
            JaLogger.log(Level.WARNING, JaLogger.Component.TRUSTSTORE_MANAGER, var6, "Error using PKIX CertPathBuilder.");
         }

         throw new IllegalStateException("Error using PKIX CertPathBuilder.", var6);
      }
   }

   boolean hasCertPath(PKIXCertPathBuilderResult var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null PKIXCertPathBuilderResult.");
      } else if (var1.getCertPath() != null && var1.getCertPath().getCertificates() != null && var1.getCertPath().getCertificates().size() > 0) {
         return true;
      } else {
         return var1.getTrustAnchor() != null && var1.getTrustAnchor().getTrustedCert() != null;
      }
   }

   X509Certificate[] copyCerts(X509Certificate[] var1) {
      if (null != var1 && var1.length > 0) {
         X509Certificate[] var2 = new X509Certificate[var1.length];
         System.arraycopy(var1, 0, var2, 0, var1.length);
         return var2;
      } else {
         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.TRUSTSTORE_MANAGER, "No certs to copy.");
         }

         return new X509Certificate[0];
      }
   }

   private Set<X509Certificate> toUnmodifiableSet(X509Certificate[] var1) {
      if (null == var1) {
         var1 = new X509Certificate[0];
      }

      return Collections.unmodifiableSet(new HashSet(Arrays.asList(var1)));
   }
}
