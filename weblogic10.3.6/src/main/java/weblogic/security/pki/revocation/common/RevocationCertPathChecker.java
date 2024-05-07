package weblogic.security.pki.revocation.common;

import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import javax.security.auth.x500.X500Principal;

public final class RevocationCertPathChecker extends PKIXCertPathChecker {
   private static final String CLASSNAME = RevocationCertPathChecker.class.getName();
   private final AbstractCertRevocContext context;
   private final OcspChecker ocspChecker;
   private final CrlChecker crlChecker;
   private X509Certificate issuerX509Cert;

   public static RevocationCertPathChecker getInstance(AbstractCertRevocContext var0) {
      return new RevocationCertPathChecker(var0);
   }

   private RevocationCertPathChecker(AbstractCertRevocContext var1) {
      Util.checkNotNull("AbstractCertRevocContext", var1);
      this.context = var1;
      this.ocspChecker = OcspChecker.getInstance(var1);
      this.crlChecker = CrlChecker.getInstance(var1);
   }

   public void init(boolean var1) throws CertPathValidatorException {
      if (this.context.isLoggable(Level.FINEST)) {
         this.context.log(Level.FINEST, "{0}.init called (forward not supported), forward={1}.", CLASSNAME, var1);
      }

      this.issuerX509Cert = null;
      if (var1) {
         throw new CertPathValidatorException("Forward checking is not supported.");
      }
   }

   public boolean isForwardCheckingSupported() {
      return false;
   }

   public Set<String> getSupportedExtensions() {
      Set var1 = Collections.emptySet();
      if (this.context.isLoggable(Level.FINEST)) {
         this.context.log(Level.FINEST, "{0}.getSupportedExtensions called.", CLASSNAME);
      }

      return var1;
   }

   public void check(Certificate var1, Collection<String> var2) throws CertPathValidatorException {
      if (!this.isCertToCheckNull(var1) && this.isCertToCheckX509(var1)) {
         X509Certificate var3 = (X509Certificate)var1;

         try {
            if (!this.isEnabled()) {
               CrlCacheUpdater.cancelAllMaintenanceTasks(this.context.getLogListener());
               return;
            }

            if (!CrlCacheUpdater.isAllMaintenanceTasksActive()) {
               this.startAllMaintenanceTasks();
            }

            X500Principal var4 = var3.getSubjectX500Principal();
            X500Principal var5 = var3.getIssuerX500Principal();
            if (this.context.isLoggable(Level.FINE)) {
               this.context.log(Level.FINE, "Revocation status checking X509 certificate with subject \"{0}\" and issuer \"{1}\".", var4, var5);
            }

            if (this.isIssuerDnMissing(var5)) {
               return;
            }

            if (this.isCheckingDisabled(var5)) {
               return;
            }

            boolean var6 = this.isFailOnUnknownRevocStatus(var5);

            try {
               if (!this.ensureIssuerCert(var5, var6)) {
                  return;
               }

               if (this.isSubjectDnMissing(var6, var4)) {
                  return;
               }

               if (this.isExpectedIssuer(var5, var6)) {
                  CertRevocStatus var7 = null;
                  CertRevocCheckMethodList var8 = this.context.getMethodOrder(var5);
                  Iterator var9 = var8.iterator();

                  while(null == var7 && var9.hasNext()) {
                     CertRevocCheckMethodList.SelectableMethod var10 = (CertRevocCheckMethodList.SelectableMethod)var9.next();
                     if (this.context.isLoggable(Level.FINEST)) {
                        this.context.log(Level.FINEST, "Trying revocation check using method {0}.", var10);
                     }

                     if (null == var10) {
                        if (this.context.isLoggable(Level.FINER)) {
                           this.context.log(Level.FINER, "Skipping null revocation check method.");
                        }
                     } else {
                        switch (var10) {
                           case OCSP:
                              var7 = this.ocspChecker.getCertRevocStatus(this.issuerX509Cert, var3);
                              break;
                           case CRL:
                              var7 = this.crlChecker.getCertRevocStatus(this.issuerX509Cert, var3);
                              break;
                           default:
                              if (this.context.isLoggable(Level.FINE)) {
                                 this.context.log(Level.FINE, "Skipping unknown SelectableMethod: {0}", var10);
                              }
                        }
                     }
                  }

                  if (this.context.isLoggable(Level.FINEST)) {
                     this.context.log(Level.FINEST, "The revocation status of certificate {0} is:\n{1}.", var4, var7 == null ? "Unknown" : var7);
                  }

                  if (null == var7) {
                     if (var6) {
                        throw new CertPathValidatorException("Unknown revocation status for certificate \"" + var4 + "\".");
                     }
                  } else if (var7.isRevoked()) {
                     throw new CertPathValidatorException("Certificate revoked: \"" + var4 + "\".");
                  }

                  return;
               }
            } catch (Exception var14) {
               if (this.context.isLoggable(Level.FINE)) {
                  this.context.log(Level.FINE, var14, "An exception occurred while checking revocation of certificate={0},\nexception={1}", var4, var14.getMessage());
               }

               if (var6) {
                  if (var14 instanceof CertPathValidatorException) {
                     throw (CertPathValidatorException)var14;
                  }

                  throw new CertPathValidatorException("Unknown revocation status for certificate \"" + var4 + "\".", var14);
               }

               return;
            }
         } finally {
            this.issuerX509Cert = var3;
         }

      } else {
         this.issuerX509Cert = null;
      }
   }

   private void startAllMaintenanceTasks() {
      CrlCacheAccessor var1 = this.crlChecker.getCrlCacheAccessor();
      if (null != var1) {
         CrlCacheUpdater.startAllMaintenanceTasks(var1, this.context);
      }
   }

   private boolean isCheckingDisabled(X500Principal var1) {
      if (this.context.isCheckingDisabled(var1)) {
         if (this.context.isLoggable(Level.FINE)) {
            this.context.log(Level.FINE, "Revocation status checking is disabled for issuer \"{0}\".", var1);
         }

         return true;
      } else {
         if (this.context.isLoggable(Level.FINE)) {
            this.context.log(Level.FINE, "Revocation status checking is enabled for issuer \"{0}\".", var1);
         }

         return false;
      }
   }

   private boolean isSubjectDnMissing(boolean var1, X500Principal var2) throws CertPathValidatorException {
      if (null != var2 && null != var2.getName() && var2.getName().length() != 0) {
         return false;
      } else if (var1) {
         throw new CertPathValidatorException("Unknown Revocation Status: Certificate to check has no subject.");
      } else {
         if (this.context.isLoggable(Level.FINE)) {
            this.context.log(Level.FINE, "Skipping revocation status checking since certificate to check has no subject.");
         }

         return true;
      }
   }

   private boolean isIssuerDnMissing(X500Principal var1) {
      if (null != var1 && null != var1.getName() && var1.getName().length() != 0) {
         return false;
      } else {
         if (this.context.isLoggable(Level.FINE)) {
            this.context.log(Level.FINE, "Unable to check revocation status, missing issuer DN.");
         }

         return true;
      }
   }

   private boolean isCertToCheckX509(Certificate var1) {
      if (var1 instanceof X509Certificate) {
         return true;
      } else {
         if (this.context.isLoggable(Level.FINE)) {
            this.context.log(Level.FINE, "Unable to check revocation of certificate of type {0}.", null == var1 ? null : var1.getClass().getName());
         }

         return false;
      }
   }

   private boolean isCertToCheckNull(Certificate var1) {
      if (null == var1) {
         if (this.context.isLoggable(Level.FINE)) {
            this.context.log(Level.FINE, "Given null certificate, no revocation checking is needed.");
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean isEnabled() {
      boolean var1 = this.context.isCheckingEnabled();
      if (this.context.isLoggable(Level.FINE)) {
         this.context.log(Level.FINE, "Certificate revocation checking is {0}.", var1 ? "enabled" : "disabled");
      }

      return var1;
   }

   private boolean isFailOnUnknownRevocStatus(X500Principal var1) {
      boolean var2 = this.context.isFailOnUnknownRevocStatus(var1);
      if (this.context.isLoggable(Level.FINE)) {
         this.context.log(Level.FINE, "Certificate validation will {0} if revocation status is indeterminable.", var2 ? "FAIL" : "not be affected");
      }

      return var2;
   }

   private boolean ensureIssuerCert(X500Principal var1, boolean var2) throws CertPathValidatorException {
      if (null == this.issuerX509Cert) {
         this.issuerX509Cert = this.context.getValidTrustedCert(var1);
         if (null == this.issuerX509Cert) {
            if (var2) {
               throw new CertPathValidatorException("Unknown Revocation Status: Could not find trusted issuer certificate with subject \"" + var1 + "\".");
            }

            if (this.context.isLoggable(Level.FINE)) {
               this.context.log(Level.FINE, "Skipping revocation status checking since cannot find trusted issuer certificate with subject \"{0}\".", var1);
            }

            return false;
         }
      }

      return true;
   }

   private boolean isExpectedIssuer(X500Principal var1, boolean var2) throws CertPathValidatorException {
      X500Principal var3 = this.issuerX509Cert.getSubjectX500Principal();
      if (!var1.equals(var3)) {
         if (var2) {
            throw new CertPathValidatorException("Unexpected issuer for certificate to check, expected issuer=\"" + var3 + "\".");
         } else {
            if (this.context.isLoggable(Level.FINE)) {
               this.context.log(Level.FINE, "Unexpected issuer for certificate to check, expected issuer=\n{0}.", var3);
            }

            return false;
         }
      } else {
         return true;
      }
   }
}
