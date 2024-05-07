package weblogic.security.utils;

import java.security.AccessController;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import javax.net.ssl.SSLSocket;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.pk.CertPathValidatorParameters;
import weblogic.security.service.ContextElement;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.shared.LoggerWrapper;

public final class CertPathTrustManagerUtils {
   public static final int CERT_PATH_VAL_IF_CONFIGURED = 0;
   public static final int CERT_PATH_VAL_ALWAYS = 1;
   public static final int CERT_PATH_VAL_NEVER = 2;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static boolean running = false;
   private static LoggerWrapper log = LoggerWrapper.getInstance("SecurityCertPath");
   private static String CLIENT_CERTS_ENFORCED = "weblogic.security.ClientCertificatesEnforced";

   public static synchronized void start() {
      running = true;
   }

   public static synchronized void stop() {
      running = false;
   }

   public static synchronized void halt() {
      running = false;
   }

   private static boolean isDebug() {
      return log.isDebugEnabled();
   }

   private static void debug(String var0, String var1) {
      String var2 = "CertPathTrustManagerUtils." + var0 + ": " + var1;
      if (log.isDebugEnabled()) {
         log.debug(var2);
      }

      System.out.println(var2);
   }

   public static boolean certificateCallback(int var0, X509Certificate[] var1, int var2) {
      String var3 = "certificateCallback";
      if (isDebug()) {
         debug(var3, "certPathValStype = " + var0);
         debug(var3, "validateErr = " + var2);

         for(int var4 = 0; var1 != null && var4 < var1.length; ++var4) {
            debug(var3, "chain[" + var4 + "] = " + var1[var4]);
         }
      }

      boolean var7 = false;
      var7 = Boolean.getBoolean("weblogic.security.dontValidateIfSSLErrors");
      if (var7 && var2 != 0) {
         if (isDebug()) {
            debug(var3, "returning false because of built-in SSL validation errors");
         }

         return false;
      } else if (0 != var2 || var1 != null && var1.length >= 1) {
         boolean var5;
         if (!doCertPathValidation(var0)) {
            var5 = var2 == 0;
            if (isDebug()) {
               debug(var3, "returning " + var5 + " because the CertPathValidators should not be called");
            }

            return var5;
         } else {
            var5 = false;
            if (!var7) {
               SSLMBean var6 = ManagementService.getRuntimeAccess(kernelId).getServer().getSSL();
               if (var6.isClientCertificateEnforced()) {
                  var5 = true;
               }
            }

            boolean var8 = performCertPathValidation(var1, var2, var5);
            if (isDebug()) {
               debug(var3, "returning results of CertPathValidators = " + var8);
            }

            return var8;
         }
      } else {
         if (isDebug()) {
            debug(var3, "returning true because there is no chain and the chain is not required");
         }

         return true;
      }
   }

   private static boolean doCertPathValidation(int var0) {
      String var1 = "doCertPathValidation";
      if (isDebug()) {
         debug(var1, "");
      }

      if (!running) {
         if (isDebug()) {
            debug(var1, "returning false because cert path validation is not yet available in this server");
         }

         return false;
      } else if (var0 == 1) {
         if (isDebug()) {
            debug(var1, "returning true because configured to always call the cert path validators");
         }

         return true;
      } else if (var0 == 2) {
         if (isDebug()) {
            debug(var1, "returning false because configured to never call the cert path validators");
         }

         return false;
      } else {
         if (isDebug()) {
            debug(var1, "configured to defer to the admin");
         }

         boolean var2 = TrustManagerEnvironment.getSSLSocket().getUseClientMode();
         if (isDebug()) {
            debug(var1, "outbound = " + var2);
         }

         SSLMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServer().getSSL();
         String var4 = var2 ? var3.getOutboundCertificateValidation() : var3.getInboundCertificateValidation();
         if (isDebug()) {
            debug(var1, "style = " + var4);
         }

         boolean var5 = "BuiltinSSLValidationAndCertPathValidators".equals(var4);
         if (isDebug()) {
            debug(var1, "returning " + var5);
         }

         return var5;
      }
   }

   private static boolean performCertPathValidation(X509Certificate[] var0, int var1, boolean var2) {
      String var3 = "performCertPathValidation";
      if (isDebug()) {
         debug(var3, "");
      }

      try {
         int var4 = null == var0 ? 0 : var0.length;
         ArrayList var5 = new ArrayList(var4);

         for(int var6 = 0; var6 < var4; ++var6) {
            var5.add(var0[var6]);
         }

         CertPath var14 = CertificateFactory.getInstance("X509").generateCertPath(var5);
         SSLSocket var7 = TrustManagerEnvironment.getSSLSocket();
         CertPathValidatorParameters var8 = new CertPathValidatorParameters(SecurityServiceManager.getDefaultRealmName(), TrustManagerEnvironment.getTrustedCAs(), new SSLPrevalidationContextParams(var7.getPort(), var7.getInetAddress().toString(), var1, var2));
         CertPathValidator var9 = CertPathValidator.getInstance("WLSCertPathValidator");

         try {
            var9.validate(var14, var8);
            if (isDebug()) {
               debug(var3, "the chain was validated by the cert path validators");
            }

            return true;
         } catch (CertPathValidatorException var11) {
            SecurityLogger.logSSLCertPathNotValidated(var14.toString(), var11);
            if (isDebug()) {
               debug(var3, "the chain was not validated by the cert path validators:" + var11);
            }

            return false;
         } catch (IllegalArgumentException var12) {
            if (isDebug()) {
               debug(var3, "the chain was not validated by the cert path validators:" + var12);
            }

            return false;
         }
      } catch (Exception var13) {
         if (isDebug()) {
            debug(var3, "unexpected exception: " + var13);
         }

         throw new CertPathTrustManagerRuntimeException(var13);
      }
   }

   private static class SSLPrevalidationContextParams implements ContextHandler {
      ContextElement[] ctxElements = new ContextElement[4];

      public SSLPrevalidationContextParams(int var1, String var2, int var3, boolean var4) {
         this.ctxElements[0] = new ContextElement("com.bea.contextelement.security.ChainPrevailidatedBySSL", new Boolean(var3 == 0));
         this.ctxElements[1] = new ContextElement("com.bea.contextelement.channel.RemotePort", new Integer(var1));
         this.ctxElements[2] = new ContextElement("com.bea.contextelement.channel.RemoteAddress", var2);
         this.ctxElements[3] = new ContextElement(CertPathTrustManagerUtils.CLIENT_CERTS_ENFORCED, new Boolean(var4));
      }

      public int size() {
         return this.ctxElements.length;
      }

      public String[] getNames() {
         String[] var1 = new String[this.ctxElements.length];

         for(int var2 = 0; var2 < this.ctxElements.length; ++var2) {
            var1[var2] = this.ctxElements[var2].getName();
         }

         return var1;
      }

      public Object getValue(String var1) {
         for(int var2 = 0; var2 < this.ctxElements.length; ++var2) {
            if (this.ctxElements[var2].getName().equals(var1)) {
               return this.ctxElements[var2].getValue();
            }
         }

         return null;
      }

      public ContextElement[] getValues(String[] var1) {
         ArrayList var2 = new ArrayList(this.ctxElements.length);

         for(int var3 = 0; var1 != null && var3 < var1.length; ++var3) {
            for(int var4 = 0; var4 < this.ctxElements.length; ++var4) {
               if (this.ctxElements[var4].getName().equals(var1[var3])) {
                  var2.add(this.ctxElements[var4]);
                  break;
               }
            }
         }

         return (ContextElement[])((ContextElement[])var2.toArray(new ContextElement[var2.size()]));
      }
   }

   private static final class CertPathTrustManagerRuntimeException extends RuntimeException {
      public CertPathTrustManagerRuntimeException(Throwable var1) {
         super(var1);
      }
   }
}
