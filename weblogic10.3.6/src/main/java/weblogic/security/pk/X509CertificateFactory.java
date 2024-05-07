package weblogic.security.pk;

import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateFactorySpi;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import weblogic.logging.Loggable;
import weblogic.security.SecurityInitializationException;
import weblogic.security.SecurityLogger;
import weblogic.security.utils.X509Utils;

public class X509CertificateFactory extends CertificateFactorySpi {
   private static final String MY_JDK_SECURITY_PROVIDER_NAME = "WLSX509CertificateFactoryProvider";
   private static final String FACTORY_ALGORITHM = "X.509";
   private static CertificateFactory standardFactory;

   public static void register() {
      if (Security.getProvider("WLSX509CertificateFactoryProvider") == null) {
         if (Boolean.parseBoolean(System.getProperty("weblogic.security.RegisterX509CertificateFactory", "true"))) {
            AccessController.doPrivileged(new PrivilegedAction() {
               public Object run() {
                  int var1 = Security.insertProviderAt(new MyJDKSecurityProvider(), 1);
                  if (var1 != 1) {
                     Loggable var2 = SecurityLogger.logCouldNotRegisterWLSX509CertificateFactoryAsDefaultFactoryLoggable();
                     var2.log();
                     throw new SecurityInitializationException(var2.getMessageText());
                  } else {
                     return null;
                  }
               }
            });
         }
      }
   }

   private synchronized CertificateFactory getStandardFactory() {
      if (standardFactory == null) {
         Provider[] var1 = Security.getProviders();

         for(int var2 = 0; standardFactory == null && var1 != null && var2 < var1.length; ++var2) {
            Provider var3 = var1[var2];
            if (!"WLSX509CertificateFactoryProvider".equals(var3.getName())) {
               try {
                  standardFactory = CertificateFactory.getInstance("X.509", var3);
               } catch (CertificateException var5) {
               }
            }
         }

         if (standardFactory == null) {
            throw new AssertionError("The WLS X.509 CertificateFactory could not find another X.509 CertificateFactory to delegate to");
         }
      }

      return standardFactory;
   }

   public CertPath engineGenerateCertPath(InputStream var1) throws CertificateException {
      String var2 = (String)((String)this.engineGetCertPathEncodings().next());
      return this.engineGenerateCertPath(var1, var2);
   }

   public CertPath engineGenerateCertPath(InputStream var1, String var2) throws CertificateException {
      CertPath var3 = this.getStandardFactory().generateCertPath(var1, var2);
      return "PKCS7".equals(var2) ? orderCertPath(var3) : var3;
   }

   public CertPath engineGenerateCertPath(List var1) throws CertificateException {
      return this.getStandardFactory().generateCertPath(var1);
   }

   public Certificate engineGenerateCertificate(InputStream var1) throws CertificateException {
      return this.getStandardFactory().generateCertificate(var1);
   }

   public Iterator engineGetCertPathEncodings() {
      return this.getStandardFactory().getCertPathEncodings();
   }

   public Collection engineGenerateCertificates(InputStream var1) throws CertificateException {
      return this.getStandardFactory().generateCertificates(var1);
   }

   public CRL engineGenerateCRL(InputStream var1) throws CRLException {
      return this.getStandardFactory().generateCRL(var1);
   }

   public Collection engineGenerateCRLs(InputStream var1) throws CRLException {
      return this.getStandardFactory().generateCRLs(var1);
   }

   private static X509Certificate findIssuer(X509Certificate[] var0, X509Certificate var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         X509Certificate var3 = var0[var2];
         if (var3 != null && X509Utils.isIssuedBy(var1, var3)) {
            var0[var2] = null;
            return var3;
         }
      }

      return null;
   }

   private static X509Certificate findIssued(X509Certificate[] var0, X509Certificate var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         X509Certificate var3 = var0[var2];
         if (var3 != null && X509Utils.isIssuedBy(var3, var1)) {
            var0[var2] = null;
            return var3;
         }
      }

      return null;
   }

   private static CertPath orderCertPath(CertPath var0) throws CertificateException {
      if (X509Utils.isOrdered(var0)) {
         return var0;
      } else {
         X509Certificate[] var1 = X509Utils.getCertificates(var0);
         Vector var2 = new Vector(var1.length);
         var2.add(var1[0]);
         var1[0] = null;
         X509Certificate var3 = (X509Certificate)var2.lastElement();

         while(var3 != null && !X509Utils.isSelfSigned(var3)) {
            var3 = findIssuer(var1, var3);
            if (var3 != null) {
               var2.add(var3);
            }
         }

         var3 = (X509Certificate)var2.firstElement();

         while(var3 != null) {
            var3 = findIssued(var1, var3);
            if (var3 != null) {
               var2.add(0, var3);
            }
         }

         if (var2.size() < var1.length) {
            return var0;
         } else {
            return CertificateFactory.getInstance("X.509").generateCertPath(new ArrayList(var2));
         }
      }
   }

   private static class MyJDKSecurityProvider extends Provider {
      private MyJDKSecurityProvider() {
         super("WLSX509CertificateFactoryProvider", 1.0, "WebLogic JDK CertPath provider");
         this.put("CertificateFactory.X.509", "weblogic.security.pk.X509CertificateFactory");
      }

      // $FF: synthetic method
      MyJDKSecurityProvider(Object var1) {
         this();
      }
   }
}
