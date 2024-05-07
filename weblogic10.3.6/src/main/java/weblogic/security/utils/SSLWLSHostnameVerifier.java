package weblogic.security.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import weblogic.kernel.Kernel;
import weblogic.logging.Loggable;
import weblogic.management.configuration.KernelMBean;
import weblogic.management.configuration.SSLMBean;
import weblogic.security.SecurityLogger;
import weblogic.security.SSL.HostnameVerifier;
import weblogic.security.SSL.HostnameVerifierJSSE;
import weblogic.utils.NestedRuntimeException;

public class SSLWLSHostnameVerifier implements SSLHostnameVerifier {
   private static final String IGNORE_VERIFICATION_PROP = "weblogic.security.SSL.ignoreHostnameVerification";
   private static final String IGNORE_VERIFICATION2_PROP = "weblogic.security.SSL.ignoreHostnameVerify";
   private static final String VERIFIER_CLASS_PROP = "weblogic.security.SSL.hostnameVerifier";
   private static final String REVERSE_DNS_ALLOWED_PROP = "weblogic.ReverseDNSAllowed";
   private String urlHostName = null;
   private String proxyHostName = null;
   private static HostnameVerifier defaultVerifier = null;
   private HostnameVerifier verifier = null;
   private String expectedName = null;

   public SSLWLSHostnameVerifier() {
      this.verifier = getDefaultVerifer();
   }

   public boolean hostnameValidationCallback(String var1, SSLSocket var2) {
      SSLSession var3 = var2.getSession();
      boolean var4 = this.isProxying(var1, var2);
      if (var4) {
         var1 = this.urlHostName;
      }

      try {
         boolean var5 = SSLSetup.isDebugEnabled(3);
         if (var5) {
            SSLSetup.info("Performing hostname validation checks: " + var1);
            if (var4) {
               SSLSetup.info("Proxying through " + this.proxyHostName);
            }
         }

         boolean var12 = this.verifier.verify(var1, var3);
         if (!var12) {
            if (SSLSetup.logSSLRejections()) {
               String var14 = this.getPeerName(var4, var2);
               Loggable var15 = SecurityLogger.logHostnameVerificationErrorLoggable(var14, SSLCertUtility.getCommonName(var3), var1);
               var15.log();
               SSLSetup.setFailureDetails(var3, var15.getMessage());
            }

            if (var5) {
               SSLSetup.info("Hostname Verification failed for certificate with CommonName '" + SSLCertUtility.getCommonName(var3) + "' against hostname: " + var1);
            }

            return false;
         } else {
            if (this.expectedName != null) {
               X509Certificate var13 = SSLCertUtility.getPeerLeafCert(var2);
               String var8;
               if (var13 == null) {
                  if (SSLSetup.logSSLRejections()) {
                     var8 = this.getPeerName(var4, var2);
                     Loggable var16 = SecurityLogger.logHostnameVerificationNoCertificateErrorLoggable(var8);
                     var16.log();
                     SSLSetup.setFailureDetails(var3, var16.getMessage());
                  }

                  if (var5) {
                     SSLSetup.info("No identity certificate, cannot verify expected name: " + this.expectedName);
                  }

                  return false;
               }

               var8 = SSLCertUtility.getCommonName(var3);
               if (!this.expectedName.equals(var8)) {
                  if (SSLSetup.logSSLRejections()) {
                     String var9 = this.getPeerName(var4, var2);
                     Loggable var10 = SecurityLogger.logHostnameVerificationErrorLoggable(var9, var8, this.expectedName);
                     var10.log();
                     SSLSetup.setFailureDetails(var3, var10.getMessage());
                  }

                  if (var5) {
                     SSLSetup.info("Hostname Verification failed since certificate CommonName '" + var8 + "' does not match expected name: " + this.expectedName);
                  }

                  return false;
               }
            }

            return true;
         }
      } catch (Exception var11) {
         if (SSLSetup.logSSLRejections()) {
            String var6 = this.getPeerName(var4, var2);
            Loggable var7 = SecurityLogger.logHostnameVerificationExceptionErrorLoggable(var6);
            var7.log();
            SSLSetup.setFailureDetails(var3, var7.getMessage());
         }

         SSLSetup.debug(1, var11, "Hostname Verification error");
         return false;
      }
   }

   private boolean isProxying(String var1, SSLSocket var2) {
      if (this.proxyHostName != null && this.urlHostName != null) {
         if (this.proxyHostName.equals(var1)) {
            return true;
         }

         InetAddress var3 = var2.getInetAddress();
         if (var3 != null && (this.proxyHostName.equals(var3.getHostAddress()) || this.proxyHostName.equals(var3.getHostName()))) {
            return true;
         }
      }

      return false;
   }

   private String getPeerName(boolean var1, SSLSocket var2) {
      String var3 = SSLSetup.getPeerName(var2);
      if (var1) {
         var3 = var3 + " --> " + this.urlHostName;
      }

      return var3;
   }

   /** @deprecated */
   public void setExpectedName(String var1) {
      this.expectedName = var1;
   }

   public void setHostnameVerifier(HostnameVerifier var1) {
      this.verifier = var1 != null ? var1 : defaultVerifier;
   }

   private static synchronized HostnameVerifier getDefaultVerifer() {
      if (defaultVerifier == null) {
         Object var0 = null;
         KernelMBean var1 = Kernel.getConfig();
         SSLMBean var2 = null;
         if (var1 != null) {
            var2 = var1.getSSL();
         }

         if (isHostnameVerificationIgnored(var2)) {
            var0 = new NullHostnameVerifier();
         } else {
            String var3 = getHostnameVerifierClassName(var2);
            Loggable var4;
            if (var3 == null) {
               if (SSLSetup.isDebugEnabled(3)) {
                  SSLSetup.info("HostnameVerifier: using default hostnameverifier");
               }

               var0 = new DefaultHostnameVerifier();
               var4 = SecurityLogger.logUsingDefaultHVLoggable();
               var4.log();
            } else {
               var4 = null;

               Loggable var6;
               Object var8;
               try {
                  var8 = Class.forName(var3).newInstance();
               } catch (Exception var7) {
                  var6 = SecurityLogger.logHostnameVerifierInitErrorLoggable(var3);
                  var6.log();
                  throw new NestedRuntimeException(var6.getMessage(), var7);
               }

               Loggable var5;
               if (var8 instanceof HostnameVerifier) {
                  var0 = (HostnameVerifier)var8;
                  if (SSLSetup.isDebugEnabled(3)) {
                     SSLSetup.info("HostnameVerifier: using configured hostnameverifier: " + var0.getClass().getName());
                  }

                  var5 = SecurityLogger.logUsingConfiguredHVLoggable(var0.getClass().getName());
                  var5.log();
               } else {
                  if (!(var8 instanceof HostnameVerifierJSSE)) {
                     var5 = SecurityLogger.logHostnameVerifierInvalidErrorLoggable(var3);
                     var5.log();
                     throw new NestedRuntimeException(var5.getMessage());
                  }

                  final HostnameVerifierJSSE var9 = (HostnameVerifierJSSE)var8;
                  var0 = new HostnameVerifier() {
                     public boolean verify(String var1, SSLSession var2) {
                        return var9.verify(var1, SSLCertUtility.getCommonName(var2));
                     }
                  };
                  var6 = SecurityLogger.logUsingConfiguredHVLoggable(var0.getClass().getName());
                  var6.log();
                  if (SSLSetup.isDebugEnabled(3)) {
                     SSLSetup.info("HostnameVerifier: using configured hostnameverifier: " + var0.getClass().getName());
                  }
               }
            }
         }

         if (var2 == null && Kernel.isServer()) {
            return (HostnameVerifier)var0;
         }

         defaultVerifier = (HostnameVerifier)var0;
      }

      return defaultVerifier;
   }

   private static boolean isHostnameVerificationIgnored(SSLMBean var0) {
      boolean var1 = false;

      try {
         var1 = var0 != null && var0.isHostnameVerificationIgnored() || Boolean.getBoolean("weblogic.security.SSL.ignoreHostnameVerification") || Boolean.getBoolean("weblogic.security.SSL.ignoreHostnameVerify");
      } catch (SecurityException var3) {
      }

      return var1;
   }

   public void setProxyMapping(String var1, String var2) {
      this.urlHostName = var2;
      this.proxyHostName = var1;
   }

   private static String getHostnameVerifierClassName(SSLMBean var0) {
      String var1 = null;

      try {
         var1 = System.getProperty("weblogic.security.SSL.hostnameVerifier");
      } catch (SecurityException var3) {
      }

      if (var1 == null && var0 != null) {
         var1 = var0.getHostnameVerifier();
      }

      return var1;
   }

   public static class NullHostnameVerifier implements HostnameVerifier {
      public boolean verify(String var1, SSLSession var2) {
         return true;
      }
   }

   public static class DefaultHostnameVerifier implements HostnameVerifier {
      private static final String LOCALHOST_HOSTNAME = "localhost";
      private static final String LOCALHOST_IPADDRESS = "127.0.0.1";
      private boolean allowReverseDNS = false;

      public DefaultHostnameVerifier() {
         if (!Kernel.isApplet() && System.getProperty("weblogic.ReverseDNSAllowed") != null) {
            this.allowReverseDNS = Boolean.getBoolean("weblogic.ReverseDNSAllowed");
         } else if (Kernel.getConfig() != null) {
            this.allowReverseDNS = Kernel.getConfig().isReverseDNSAllowed();
         }

         if (SSLSetup.isDebugEnabled(3)) {
            SSLSetup.info("HostnameVerifier: allowReverseDNS=" + this.allowReverseDNS);
         }

      }

      public boolean verify(String var1, SSLSession var2) {
         if (var1 != null && var1.length() != 0) {
            String var3 = SSLCertUtility.getCommonName(var2);
            if (this.doVerify(var1, var2, var3)) {
               return true;
            } else {
               Collection var4 = SSLCertUtility.getDNSSubjAltNames(var2);
               return this.doDNSSubjAltNamesVerify(var1, var4);
            }
         } else {
            return false;
         }
      }

      private boolean doVerify(String var1, SSLSession var2, String var3) {
         if (var3 != null && var3.length() != 0) {
            if (var1.equalsIgnoreCase(var3)) {
               return true;
            } else {
               X509Certificate var4 = SSLCertUtility.getPeerLeafCert(var2);
               if (isDemoCert(var4) && var1.toLowerCase(Locale.ENGLISH).startsWith(var3.toLowerCase(Locale.ENGLISH) + ".")) {
                  return true;
               } else {
                  try {
                     InetAddress var8 = InetAddress.getLocalHost();
                     String var5 = var8.getHostName();
                     if (var5.equalsIgnoreCase(var3)) {
                        if (var8.getHostAddress().equalsIgnoreCase(var1)) {
                           return true;
                        }

                        if (this.allowReverseDNS) {
                           InetAddress var6 = InetAddress.getByName(var1);
                           if (var6.isLoopbackAddress()) {
                              return true;
                           }
                        } else if ("localhost".equalsIgnoreCase(var1) || "127.0.0.1".equalsIgnoreCase(var1)) {
                           return true;
                        }
                     }
                  } catch (UnknownHostException var7) {
                     SSLSetup.info("HostnameVerifier: unknown host");
                  }

                  return false;
               }
            }
         } else {
            return false;
         }
      }

      private boolean doDNSSubjAltNamesVerify(String var1, Collection var2) {
         if (var2 != null && !var2.isEmpty()) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               if (var4.equalsIgnoreCase(var1)) {
                  return true;
               }
            }
         }

         return false;
      }

      private static boolean isDemoCert(X509Certificate var0) {
         String var3 = var0.getIssuerDN().getName();
         String var4 = var0.getSubjectDN().getName();
         return "C=US, ST=MyState, L=MyTown, O=MyOrganization, OU=FOR TESTING ONLY, CN=CertGenCAB".equalsIgnoreCase(var3) && var4.startsWith("C=US, ST=MyState, L=MyTown, O=MyOrganization, OU=FOR TESTING ONLY");
      }
   }
}
