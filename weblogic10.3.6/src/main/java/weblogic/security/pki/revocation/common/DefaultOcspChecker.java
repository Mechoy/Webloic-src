package weblogic.security.pki.revocation.common;

import com.rsa.certj.CertJ;
import com.rsa.certj.DatabaseService;
import com.rsa.certj.InvalidParameterException;
import com.rsa.certj.InvalidUseException;
import com.rsa.certj.NoServiceException;
import com.rsa.certj.Provider;
import com.rsa.certj.ProviderManagementException;
import com.rsa.certj.cert.CertificateException;
import com.rsa.certj.cert.NameException;
import com.rsa.certj.cert.X509V3Extensions;
import com.rsa.certj.provider.db.MemoryDB;
import com.rsa.certj.provider.path.X509V1CertPath;
import com.rsa.certj.provider.revocation.ocsp.OCSP;
import com.rsa.certj.provider.revocation.ocsp.OCSPEvidence;
import com.rsa.certj.provider.revocation.ocsp.OCSPRequestControl;
import com.rsa.certj.provider.revocation.ocsp.OCSPResponder;
import com.rsa.certj.provider.revocation.ocsp.OCSPRevocationInfo;
import com.rsa.certj.spi.db.DatabaseException;
import com.rsa.certj.spi.path.CertPathCtx;
import com.rsa.certj.spi.revocation.CertRevocationInfo;
import com.rsa.certj.spi.revocation.CertStatusException;
import com.rsa.jsafe.JSAFE_PrivateKey;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import javax.security.auth.x500.X500Principal;

class DefaultOcspChecker extends OcspChecker {
   private static final String DB_PROVIDER_NAME = "OCSP_DB_PROVIDER";
   private static final String CERT_PATH_PROVIDER_NAME = "OCSP_CERT_PATH_PROVIDER";
   private static final String OCSP_PROVIDER_NAME = "OCSP_PROVIDER";

   DefaultOcspChecker(AbstractCertRevocContext var1) {
      super(var1);
   }

   CertRevocStatus getRemoteStatus(X509Certificate var1, X509Certificate var2) {
      Util.checkNotNull("Issuer X509Certificate.", var1);
      Util.checkNotNull("X509Certificate to be checked.", var2);
      AbstractCertRevocContext var3 = this.getContext();
      LogListener var4 = var3.getLogListener();
      X500Principal var5 = var2.getIssuerX500Principal();
      com.rsa.certj.cert.X509Certificate var6 = RsaUtil.toRsaCert(var2, var4);
      com.rsa.certj.cert.X509Certificate var7 = RsaUtil.toRsaCert(var1, var4);
      if (null != var6 && null != var7) {
         com.rsa.certj.cert.X509Certificate var8 = this.getResponderTrustedCert(var5);
         JSAFE_PrivateKey var11 = this.getRequestSigningPrivateKey(var5);
         com.rsa.certj.cert.X509Certificate var12 = this.getRequestSigningPublicCert(var5);
         if (null != var11 && null != var12) {
            if (var3.isLoggable(Level.FINEST)) {
               var3.log(Level.FINEST, "OCSP request signing enabled, private key and public certificate configured.");
            }
         } else {
            if (var3.isLoggable(Level.FINEST)) {
               var3.log(Level.FINEST, "OCSP request signing disabled: Private key={0}, Public cert={1}.", null == var11 ? "missing" : "gotIt", null == var12 ? "missing" : "gotIt");
            }

            var11 = null;
            var12 = null;
         }

         JSAFE_PrivateKey var9 = var11;
         com.rsa.certj.cert.X509Certificate var10 = var12;
         if (!RsaUtil.isFIPS140UsageOk(var4)) {
            return null;
         } else {
            CertRevocationInfo var14;
            try {
               var14 = this.checkCertRevocation(var5, var6, var7, var8, var9, var10);
            } catch (Exception var13) {
               if (var3.isLoggable(Level.FINE)) {
                  var3.log(Level.FINE, var13, "Exception while checking revocation status using OCSP.");
               }

               return null;
            }

            return this.evalRevocationInfo(var2, var14);
         }
      } else {
         if (var3.isLoggable(Level.FINE)) {
            var3.log(Level.FINE, "Unable to check OCSP revocation status, unable to convert both subject and issuer certificates.");
         }

         return null;
      }
   }

   private JSAFE_PrivateKey getRequestSigningPrivateKey(X500Principal var1) {
      Util.checkNotNull("issuerDn", var1);
      AbstractCertRevocContext var2 = this.getContext();
      JSAFE_PrivateKey var3 = null;
      PrivateKey var4 = var2.getOcspRequestSigningPrivateKey(var1);
      if (null != var4) {
         var3 = RsaUtil.toRsaPrivateKey(var4, var2.getLogListener());
         if (null == var3 && var2.isLoggable(Level.FINE)) {
            var2.log(Level.FINE, "Unable to convert request signing private key.");
         }
      }

      return var3;
   }

   private com.rsa.certj.cert.X509Certificate getRequestSigningPublicCert(X500Principal var1) {
      Util.checkNotNull("issuerDn", var1);
      AbstractCertRevocContext var2 = this.getContext();
      com.rsa.certj.cert.X509Certificate var3 = null;
      X509Certificate var4 = var2.getOcspRequestSigningCert(var1);
      if (null != var4) {
         var3 = RsaUtil.toRsaCert(var4, var2.getLogListener());
         if (null == var3 && var2.isLoggable(Level.FINE)) {
            var2.log(Level.FINE, "Unable to convert request signing public certificate.");
         }
      }

      return var3;
   }

   private com.rsa.certj.cert.X509Certificate getResponderTrustedCert(X500Principal var1) {
      Util.checkNotNull("issuerDn", var1);
      AbstractCertRevocContext var2 = this.getContext();
      com.rsa.certj.cert.X509Certificate var3 = null;
      X509Certificate var4 = var2.getOcspResponderTrustedCert(var1);
      if (null == var4) {
         if (var2.isLoggable(Level.FINEST)) {
            var2.log(Level.FINEST, "No OCSP responder explicitly trusted certificate is available.");
         }
      } else {
         var3 = RsaUtil.toRsaCert(var4, var2.getLogListener());
         if (null == var3) {
            if (var2.isLoggable(Level.FINE)) {
               var2.log(Level.FINE, "Unable to convert OCSP responder explicitly trusted certificate.");
            }
         } else if (var2.isLoggable(Level.FINEST)) {
            var2.log(Level.FINEST, "OCSP using explicitly trust certificate \"{0}\".", var3.getSubjectName());
         }
      }

      return var3;
   }

   private CertJ initCertJ() throws InvalidParameterException, ProviderManagementException, InvalidUseException {
      MemoryDB var1 = new MemoryDB("OCSP_DB_PROVIDER");
      X509V1CertPath var2 = new X509V1CertPath("OCSP_CERT_PATH_PROVIDER");
      Provider[] var3 = new Provider[]{var1, var2};
      CertJ var4 = new CertJ(var3);
      var4.setDevice(RsaUtil.getCryptoJDeviceList());
      return var4;
   }

   private DatabaseService initDbService(com.rsa.certj.cert.X509Certificate var1, com.rsa.certj.cert.X509Certificate var2, com.rsa.certj.cert.X509Certificate var3, JSAFE_PrivateKey var4, CertJ var5) throws InvalidParameterException, ProviderManagementException, NoServiceException, DatabaseException {
      Util.checkNotNull("certJ", var5);
      DatabaseService var6 = (DatabaseService)var5.bindService(1, "OCSP_DB_PROVIDER");
      if (null != var1) {
         var6.insertCertificate(var1);
      }

      Util.checkNotNull("issuerCert", var2);
      var6.insertCertificate(var2);
      if (null != var3 && null != var4) {
         var6.insertCertificate(var3);
         var6.insertPrivateKeyByCertificate(var3, var4);
      }

      return var6;
   }

   private CertPathCtx initCertPathCtx(X500Principal var1, com.rsa.certj.cert.X509Certificate var2, com.rsa.certj.cert.X509Certificate var3, DatabaseService var4) {
      Util.checkNotNull("issuerDn", var1);
      Util.checkNotNull("issuerCert", var2);
      Util.checkNotNull("dbService", var4);
      com.rsa.certj.cert.X509Certificate[] var5;
      if (null != var3) {
         var5 = new com.rsa.certj.cert.X509Certificate[]{var2, var3};
      } else {
         var5 = new com.rsa.certj.cert.X509Certificate[]{var2};
      }

      AbstractCertRevocContext var6 = this.getContext();
      AbstractCertRevocContext.AttributeUsage var7 = var6.getOcspResponderUrlUsage(var1);
      if (var6.isLoggable(Level.FINEST)) {
         var6.log(Level.FINEST, "OcspResponderUrlUsage={0}", var7);
      }

      URI var8 = var6.getOcspResponderUrl(var1);
      if (var6.isLoggable(Level.FINEST)) {
         var6.log(Level.FINEST, "OcspResponderUrl={0}", var8);
      }

      int var9 = 4;
      if (AbstractCertRevocContext.AttributeUsage.OVERRIDE == var7) {
         if (null == var8) {
            throw new IllegalStateException("OCSP responder URI override is null, preventing OCSP checking for cert issuer \"" + var1 + "\"");
         }

         var9 |= 2048;
      }

      byte[][] var10 = (byte[][])null;
      Date var11 = new Date();
      if (var6.isLoggable(Level.FINEST)) {
         var6.log(Level.FINEST, "Validation time=\"{0}\"", var11);
      }

      CertPathCtx var12 = new CertPathCtx(var9, var5, var10, var11, var4);
      return var12;
   }

   private String[] initDestList(X500Principal var1) {
      Util.checkNotNull("issuerDn", var1);
      AbstractCertRevocContext var2 = this.getContext();
      URI var3 = var2.getOcspResponderUrl(var1);
      if (var2.isLoggable(Level.FINEST)) {
         var2.log(Level.FINEST, "OcspResponderUrl={0}", var3);
      }

      String[] var4 = null;
      if (null != var3) {
         var4 = new String[]{var3.toASCIIString()};
      }

      return var4;
   }

   private OCSPResponder initOcspResponder(com.rsa.certj.cert.X509Certificate var1, com.rsa.certj.cert.X509Certificate var2, X500Principal var3, com.rsa.certj.cert.X509Certificate var4, DatabaseService var5) throws InvalidParameterException {
      Util.checkNotNull("issuerDn", var3);
      Util.checkNotNull("issuerCert", var4);
      Util.checkNotNull("dbService", var5);
      AbstractCertRevocContext var6 = this.getContext();
      byte var7 = 0;
      if (var6.isLoggable(Level.FINEST)) {
         var6.log(Level.FINEST, "Using OCSP responder profile={0}", Integer.valueOf(var7));
      }

      int var8 = 8;
      boolean var9 = var6.isOcspNonceEnabled(var3);
      if (var6.isLoggable(Level.FINEST)) {
         var6.log(Level.FINEST, "OcspNonceEnabled={0}", var9);
      }

      if (!var9) {
         var8 |= 1;
      }

      if (var6.isLoggable(Level.FINEST)) {
         var6.log(Level.FINEST, "ocspResponderFlags={0}", var8);
      }

      Object var10 = null;
      if (var6.isLoggable(Level.FINEST)) {
         var6.log(Level.FINEST, "proxyList is empty, reverting to System Properties.");
      }

      OCSPRequestControl var11 = this.initOcspRequestControl(var1);
      com.rsa.certj.cert.X509Certificate[] var12 = new com.rsa.certj.cert.X509Certificate[]{var4};
      int var13 = var6.getOcspTimeTolerance(var3);
      if (var6.isLoggable(Level.FINEST)) {
         var6.log(Level.FINEST, "OcspTimeTolerance={0}", var13);
      }

      String[] var14 = this.initDestList(var3);
      OCSPResponder var15 = new OCSPResponder(var7, var8, var14, (String[])var10, var11, var2, var12, var5, var13);
      return var15;
   }

   private OCSPRequestControl initOcspRequestControl(com.rsa.certj.cert.X509Certificate var1) throws InvalidParameterException {
      AbstractCertRevocContext var2 = this.getContext();
      String var3 = "SHA1/RSA/PKCS1Block01Pad";
      String var4 = "SHA1";
      Object var5 = null;
      Object var6 = null;
      Object var7 = null;
      Object var8 = null;
      String var9 = null;
      if (var1 != null && var1.getSubjectName() != null) {
         var9 = var1.getSubjectName().toStringRFC2253();
      }

      if (var2.isLoggable(Level.FINEST)) {
         var2.log(Level.FINEST, "Request signing: signingCert={0}, digestAlg={1}, signatureAlg={2}, extraCerts={3}, requestExtensions={4}", var9, var4, var3, var6, var8);
      }

      OCSPRequestControl var10 = new OCSPRequestControl(var1, var4, var3, (com.rsa.certj.cert.X509Certificate[])var5, (X509V3Extensions)var7);
      return var10;
   }

   private OCSP initOcspProvider(X500Principal var1, OCSPResponder var2) throws UnsupportedEncodingException, InvalidParameterException, CertificateException, NameException {
      Util.checkNotNull("issuerDn", var1);
      Util.checkNotNull("ocspResponder", var2);
      AbstractCertRevocContext var3 = this.getContext();
      long var4 = var3.getOcspResponseTimeout(var1);
      if (var3.isLoggable(Level.FINEST)) {
         var3.log(Level.FINEST, "OcspResponseTimeout={0}", var4);
      }

      StringBuilder var6 = new StringBuilder();
      var6.append("timeoutSecs=");
      var6.append(var4);
      var6.append("\n");
      if (var3.isLoggable(Level.FINEST)) {
         var3.log(Level.FINEST, "OCSP configStream=\"{0}\"", var6.toString());
      }

      ByteArrayInputStream var7 = new ByteArrayInputStream(var6.toString().getBytes("US-ASCII"));
      OCSP var8 = new OCSP("OCSP_PROVIDER", var2, var7);
      return var8;
   }

   private CertRevocationInfo checkCertRevocation(X500Principal var1, com.rsa.certj.cert.X509Certificate var2, com.rsa.certj.cert.X509Certificate var3, com.rsa.certj.cert.X509Certificate var4, JSAFE_PrivateKey var5, com.rsa.certj.cert.X509Certificate var6) throws InvalidParameterException, ProviderManagementException, InvalidUseException, NoServiceException, DatabaseException, UnsupportedEncodingException, CertificateException, NameException, CertStatusException {
      Util.checkNotNull("issuerDn", var1);
      Util.checkNotNull("certToCheck", var2);
      Util.checkNotNull("issuerCert", var3);
      CertJ var7 = this.initCertJ();
      DatabaseService var8 = this.initDbService(var4, var3, var6, var5, var7);
      CertPathCtx var9 = this.initCertPathCtx(var1, var3, var4, var8);
      OCSPResponder var10 = this.initOcspResponder(var6, var4, var1, var3, var8);
      OCSP var11 = this.initOcspProvider(var1, var10);
      var7.registerService(var11);
      CertRevocationInfo var12 = var7.checkCertRevocation(var9, var2);
      return var12;
   }

   private CertRevocStatus evalRevocationInfo(X509Certificate var1, CertRevocationInfo var2) {
      Util.checkNotNull("certToCheck", var1);
      AbstractCertRevocContext var3 = this.getContext();
      if (null == var2) {
         if (var3.isLoggable(Level.FINER)) {
            var3.log(Level.FINER, "Revocation status unavailable from OCSP (CertRevocationInfo is null).");
         }

         return null;
      } else {
         Boolean var4 = RsaUtil.evalRevocStatusCode(CertRevocCheckMethodList.SelectableMethod.OCSP, var2.getStatus(), var3.getLogListener());
         if (null == var4) {
            return null;
         } else {
            int var5 = var2.getType();
            if (var5 != 2) {
               if (var3.isLoggable(Level.FINE)) {
                  var3.log(Level.FINE, "Revocation status unavailable from OCSP, unexpected evidence type {0}.", var5);
               }

               return null;
            } else {
               OCSPEvidence var6 = (OCSPEvidence)var2.getEvidence();
               if (null == var6) {
                  if (var3.isLoggable(Level.FINE)) {
                     var3.log(Level.FINE, "Revocation status unavailable from OCSP, no evidence available.");
                  }

                  return null;
               } else {
                  int var7 = var6.getFlags();
                  boolean var8 = isNonceIgnored(var7);
                  Date var9 = null;
                  Integer var10 = null;
                  OCSPRevocationInfo var11 = var6.getRevocationInfo();
                  if (null != var11) {
                     var9 = var11.getRevocationTime();
                     var10 = var11.getReasonCode();
                  }

                  HashMap var12 = new HashMap(10);
                  var12.put("Flags", Integer.toString(var7, 2));
                  var12.put("ProducedAt", CertRevocStatus.format(var6.getProducedAt()));
                  var12.put("RevocationTime", CertRevocStatus.format(var9));
                  var12.put("ReasonCode", null == var10 ? null : var10.toString());
                  return new CertRevocStatus(CertRevocCheckMethodList.SelectableMethod.OCSP, var1.getSubjectX500Principal(), var1.getIssuerX500Principal(), var1.getSerialNumber(), var6.getThisUpdate(), var6.getNextUpdate(), var4, var8, var12);
               }
            }
         }
      }
   }

   private static boolean isNonceIgnored(int var0) {
      return (var0 & 1) != 0;
   }
}
