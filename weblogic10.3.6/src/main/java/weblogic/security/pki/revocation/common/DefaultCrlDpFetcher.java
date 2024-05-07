package weblogic.security.pki.revocation.common;

import com.rsa.certj.cert.NameException;
import com.rsa.certj.cert.X500Name;
import com.rsa.certj.cert.extensions.CRLDistributionPoints;
import com.rsa.certj.cert.extensions.GeneralName;
import com.rsa.certj.cert.extensions.GeneralNames;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import javax.security.auth.x500.X500Principal;

class DefaultCrlDpFetcher extends CrlDpFetcher {
   boolean updateCrls(X509Certificate var1, CrlCacheAccessor var2, URI var3, AbstractCertRevocContext.AttributeUsage var4, long var5, long var7, LogListener var9) throws Exception {
      Util.checkNotNull("X509Certificate with DPs", var1);
      Util.checkNotNull("CrlCacheAccessor", var2);
      X500Principal var10 = this.getIssuerX500Principal(var1, var9);
      if (null == var10) {
         return false;
      } else {
         com.rsa.certj.cert.X509Certificate var11 = this.toRsaCert(var1, var9);
         if (null == var11) {
            return false;
         } else {
            X500Name var12 = this.getRsaIssuerX500Name(var1, var9, var11);
            if (null == var12) {
               return false;
            } else {
               URI var13 = this.getUri(var3, var4, var9, var10, var11, var12);
               if (null != var13) {
                  if (null != var9 && var9.isLoggable(Level.FINEST)) {
                     var9.log(Level.FINEST, "Attempting to download CRL from URI \"{0}\".", var13);
                  }

                  int var14 = var7 > 2147483647L ? Integer.MAX_VALUE : (int)var7;
                  int var15 = var5 > 2147483647L ? Integer.MAX_VALUE : (int)var5;
                  return this.loadCrl(var2, var9, var10, var14, var15, var13);
               } else {
                  if (null != var9 && var9.isLoggable(Level.FINER)) {
                     var9.log(Level.FINER, "Unable to determine CRL DP URI for certificate with subject \"{0}\".", var1.getSubjectX500Principal());
                  }

                  return false;
               }
            }
         }
      }
   }

   private URI getUri(URI var1, AbstractCertRevocContext.AttributeUsage var2, LogListener var3, X500Principal var4, com.rsa.certj.cert.X509Certificate var5, X500Name var6) throws NameException {
      URI var7 = null;
      if (AbstractCertRevocContext.AttributeUsage.OVERRIDE == var2) {
         if (!isAlternateUriNull(var1, var3, var4)) {
            var7 = var1;
         }
      } else {
         CRLDistributionPoints var8 = this.getDistributionPoints(var3, var5);
         if (null != var8) {
            var7 = this.findUriInDp(var3, var4, var6, var8);
            if (null == var7) {
               var7 = this.findFailoverUri(var1, var2, var3, var4);
            }
         }
      }

      return var7;
   }

   private CRLDistributionPoints getDistributionPoints(LogListener var1, com.rsa.certj.cert.X509Certificate var2) {
      CRLDistributionPoints var3 = (CRLDistributionPoints)RsaUtil.getExtension((com.rsa.certj.cert.X509Certificate)var2, 31);
      if (null == var3 && null != var1 && var1.isLoggable(Level.FINER)) {
         var1.log(Level.FINER, "No Distribution points found in cert with subject \"{0}\".", var2.getSubjectName());
      }

      return var3;
   }

   private URI findFailoverUri(URI var1, AbstractCertRevocContext.AttributeUsage var2, LogListener var3, X500Principal var4) {
      URI var5 = null;
      if (null != var3 && var3.isLoggable(Level.FINER)) {
         var3.log(Level.FINER, "Unable to find any usable CRL DP URI, checking FAILOVER CRL DP URI.");
      }

      if (AbstractCertRevocContext.AttributeUsage.FAILOVER == var2 && null != var1) {
         if (null != var3 && var3.isLoggable(Level.FINER)) {
            var3.log(Level.FINER, "Trying FAILOVER CRL DP URI \"{0}\".", var1);
         }

         var5 = var1;
      } else if (null != var3 && var3.isLoggable(Level.FINER)) {
         var3.log(Level.FINER, "NO FAILOVER CRL DP URI for issuer \"{0}\".", var4);
      }

      return var5;
   }

   private URI findUriInDp(LogListener var1, X500Principal var2, X500Name var3, CRLDistributionPoints var4) throws NameException {
      URI var5 = null;
      int var6 = var4.getDistributionPointCount();

      for(int var7 = 0; var7 < var6; ++var7) {
         int var8 = var4.getReasonFlags(var7);
         if (-1 == var8 && this.isDpCrlIssuerEqual(var3, var4, var7)) {
            Object var9 = var4.getDistributionPointName(var7);
            if (var9 instanceof GeneralNames) {
               GeneralNames var10 = (GeneralNames)var9;
               var5 = this.getUri(var1, var2, var10);
               if (null != var5) {
                  break;
               }
            }
         }
      }

      return var5;
   }

   private static boolean isAlternateUriNull(URI var0, LogListener var1, X500Principal var2) {
      if (null == var0) {
         if (null != var1 && var1.isLoggable(Level.FINE)) {
            var1.log(Level.FINE, "Unable to fetch CRL from DP. CRL DP override URI set to null for cert issuer \"{0}\".", var2);
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean loadCrl(CrlCacheAccessor var1, LogListener var2, X500Principal var3, int var4, int var5, URI var6) {
      boolean var7 = false;
      InputStream var8 = null;

      try {
         var8 = this.getInputStream(var4, var5, var6);
         var7 = var1.loadCrl(var8);
      } catch (Exception var18) {
         if (null != var2 && var2.isLoggable(Level.FINE)) {
            var2.log(Level.FINE, var18, "Exception fetching CRL from DP URI \"{0}\" for cert issuer \"{1}\".", var6, var3);
         }
      } finally {
         if (null != var8) {
            try {
               var8.close();
            } catch (IOException var17) {
            }
         }

      }

      return var7;
   }

   private InputStream getInputStream(int var1, int var2, URI var3) throws IOException {
      URL var4 = var3.toURL();
      URLConnection var5 = var4.openConnection();
      var5.setConnectTimeout(var1);
      var5.setReadTimeout(var2);
      InputStream var6 = var5.getInputStream();
      return var6;
   }

   private URI getUri(LogListener var1, X500Principal var2, GeneralNames var3) throws NameException {
      URI var4 = null;
      int var5 = var3.getNameCount();

      for(int var6 = 0; var6 < var5; ++var6) {
         GeneralName var7 = var3.getGeneralName(var6);
         if (var7.getGeneralNameType() == 7) {
            String var8 = (String)var7.getGeneralName();

            try {
               var4 = new URI(var8);
               String var9 = var4.getScheme().toLowerCase();
               if (var9.equals("http") || var9.equals("ftp")) {
                  break;
               }
            } catch (URISyntaxException var10) {
               if (null != var1 && var1.isLoggable(Level.FINE)) {
                  var1.log(Level.FINE, "Unable to parse DP URI \"{0}\" for cert issuer \"{1}\".", var8, var2);
               }
            }
         }
      }

      return var4;
   }

   private boolean isDpCrlIssuerEqual(X500Name var1, CRLDistributionPoints var2, int var3) throws NameException {
      GeneralNames var4 = var2.getCRLIssuer(var3);
      if (null == var4) {
         return true;
      } else {
         if (var4 != null) {
            int var5 = var4.getNameCount();

            for(int var6 = 0; var6 < var5; ++var6) {
               GeneralName var7 = var4.getGeneralName(var6);
               if (var1.equals(var7)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private X500Name getRsaIssuerX500Name(X509Certificate var1, LogListener var2, com.rsa.certj.cert.X509Certificate var3) {
      X500Name var4 = var3.getIssuerName();
      if (null == var4 && null != var2 && var2.isLoggable(Level.FINE)) {
         var2.log(Level.FINE, "Unable to update CRLs, missing internal issuer, certificate=\"{0}\".", var1.getSubjectDN());
      }

      return var4;
   }

   private com.rsa.certj.cert.X509Certificate toRsaCert(X509Certificate var1, LogListener var2) {
      com.rsa.certj.cert.X509Certificate var3 = RsaUtil.toRsaCert(var1, var2);
      if (null == var3 && null != var2 && var2.isLoggable(Level.FINE)) {
         var2.log(Level.FINE, "Unable to update CRLs, certificate not convertible, certificate=\"{0}\".", var1.getSubjectDN());
      }

      return var3;
   }

   private X500Principal getIssuerX500Principal(X509Certificate var1, LogListener var2) {
      X500Principal var3 = var1.getIssuerX500Principal();
      if (null == var3 && null != var2 && var2.isLoggable(Level.FINE)) {
         var2.log(Level.FINE, "Unable to update CRLs, missing issuer, certificate=\"{0}\".", var1.getSubjectDN());
      }

      return var3;
   }
}
