package weblogic.xml.crypto.utils;

import com.bea.security.utils.wss.WSSThumbprint;
import com.bea.security.utils.wss.WSSThumbprintException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import weblogic.security.pk.CertPathBuilderParameters;
import weblogic.security.pk.CertPathSelector;
import weblogic.security.pk.CertPathValidatorParameters;
import weblogic.security.pk.EndCertificateSelector;
import weblogic.security.pk.IssuerDNSerialNumberSelector;
import weblogic.security.pk.SubjectKeyIdentifierSelector;
import weblogic.security.pk.X509ThumbprintSelector;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.shared.LoggerWrapper;
import weblogic.security.utils.SSLContextManager;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.xml.crypto.wss.WSSecurityConfigurationException;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.security.utils.Utils;

public class CertUtils {
   private static final LoggerWrapper logger = LoggerWrapper.getInstance("weblogic.xml.crypto.Logger");

   public static X509Certificate getCertificate(byte[] var0) {
      X509Certificate var1 = null;
      UnsyncByteArrayInputStream var2 = new UnsyncByteArrayInputStream(var0);

      try {
         var1 = (X509Certificate)Utils.getCertFactory().generateCertificate(var2);
         return var1;
      } catch (CertificateException var4) {
         throw new AssertionError("Unable to decode certificate: " + var4);
      }
   }

   public static X509CRL getCRL(byte[] var0) {
      X509CRL var1 = null;
      UnsyncByteArrayInputStream var2 = new UnsyncByteArrayInputStream(var0);

      try {
         var1 = (X509CRL)Utils.getCertFactory().generateCRL(var2);
         return var1;
      } catch (CRLException var4) {
         throw new AssertionError("Unable to decode certificate revocation list: " + var4);
      }
   }

   public static boolean validateCertPath(CertPath var0) {
      String var1 = SecurityServiceManager.getDefaultRealmName();
      if (var1 == null) {
         logger.error("CertPathBuilder cannot be used off-platform");
         return false;
      } else {
         CertPathValidatorParameters var2 = new CertPathValidatorParameters(var1, (X509Certificate[])null, (ContextHandler)null);
         CertPathValidator var3 = null;

         try {
            var3 = CertPathValidator.getInstance("WLSCertPathValidator");
         } catch (NoSuchAlgorithmException var7) {
            throw new AssertionError(var7);
         }

         try {
            var3.validate(var0, var2);
            return true;
         } catch (CertPathValidatorException var5) {
            return false;
         } catch (InvalidAlgorithmParameterException var6) {
            return false;
         }
      }
   }

   public static boolean validateCertificate(X509Certificate var0) {
      CertPathBuilderResult var1 = buildCertPath(new EndCertificateSelector(var0));
      return var1 != null;
   }

   public static X509Certificate lookupCertificate(String var0) {
      X509ThumbprintSelector var1 = new X509ThumbprintSelector(var0);
      return lookupCertificate((CertPathSelector)var1);
   }

   public static X509Certificate lookupCertificate(String var0, BigInteger var1) {
      IssuerDNSerialNumberSelector var2 = new IssuerDNSerialNumberSelector(var0, var1);
      return lookupCertificate((CertPathSelector)var2);
   }

   public static X509Certificate lookupCertificate(byte[] var0) {
      SubjectKeyIdentifierSelector var1 = new SubjectKeyIdentifierSelector(var0);
      return lookupCertificate((CertPathSelector)var1);
   }

   public static X509Certificate lookupCertificate(CertPathSelector var0) {
      CertPathBuilderResult var1 = buildCertPath(var0);
      if (var1 != null) {
         Object var2 = var1.getCertPath().getCertificates().get(0);
         return var2 instanceof X509Certificate ? (X509Certificate)var2 : null;
      } else {
         return null;
      }
   }

   private static CertPathBuilderResult buildCertPath(CertPathSelector var0) {
      String var1 = SecurityServiceManager.getDefaultRealmName();
      if (var1 == null) {
         logger.error("CertPathBuilder cannot be used off-platform");
         return null;
      } else {
         CertPathBuilderParameters var2 = new CertPathBuilderParameters(var1, var0, (X509Certificate[])null, (ContextHandler)null);
         CertPathBuilder var3 = null;

         try {
            var3 = CertPathBuilder.getInstance("WLSCertPathBuilder");
         } catch (NoSuchAlgorithmException var7) {
            throw new AssertionError(var7);
         }

         try {
            return var3.build(var2);
         } catch (CertPathBuilderException var5) {
            logger.error("Failed to build CertPath", var5);
            return null;
         } catch (InvalidAlgorithmParameterException var6) {
            logger.error("CertPathBuilder does not support building cert path from " + var0.getClass(), var6);
            return null;
         }
      }
   }

   public static boolean supportsSign(X509Certificate var0) {
      boolean[] var1 = var0.getKeyUsage();
      return var1 == null ? true : var1[0];
   }

   public static boolean supportsKeyEncrypt(X509Certificate var0) {
      boolean[] var1 = var0.getKeyUsage();
      return var1 == null ? true : var1[2];
   }

   public static X509Certificate[] getTrustedCAs() throws WSSecurityConfigurationException {
      try {
         return SSLContextManager.getServerTrustedCAs();
      } catch (Exception var1) {
         throw new WSSecurityConfigurationException("Failed to get trusted CAs.");
      }
   }

   public static final byte[] getThumbprint(X509Certificate var0) throws WSSecurityException {
      try {
         return weblogic.xml.crypto.encrypt.Utils.base64(WSSThumbprint.generateThumbprint(var0));
      } catch (WSSThumbprintException var2) {
         throw new WSSecurityException(var2);
      }
   }
}
