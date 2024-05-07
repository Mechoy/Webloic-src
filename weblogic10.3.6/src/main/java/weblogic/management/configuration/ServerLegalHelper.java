package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.logging.Loggable;
import weblogic.security.SecurityLogger;
import weblogic.utils.Debug;

public final class ServerLegalHelper {
   public static final int DEFAULT_THREAD_POOL_SIZE = 15;
   public static final int PRODUCTION_MODE_THREAD_POOL_SIZE = 25;

   public static boolean isSSLListenPortEnabled(ServerMBean var0) {
      if (Boolean.getBoolean("weblogic.mbeanLegalClause.ByPass")) {
         return true;
      } else if (var0.getSSL().isEnabled()) {
         return true;
      } else {
         Debug.assertion(var0.getParent() != null);
         if (((DomainMBean)var0.getParent()).isAdministrationPortEnabled()) {
            return true;
         } else {
            NetworkAccessPointMBean[] var1 = var0.getNetworkAccessPoints();

            for(int var2 = 0; var2 < var1.length; ++var2) {
               if (var1[var2].isEnabled()) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public static boolean isListenPortEnabled(SSLMBean var0) {
      if (Boolean.getBoolean("weblogic.mbeanLegalClause.ByPass")) {
         return true;
      } else {
         ServerMBean var1 = (ServerMBean)var0.getParent();
         Debug.assertion(var1 != null);
         return var1.isListenPortEnabled() ? true : isSSLListenPortEnabled(var1);
      }
   }

   public static void validateSSL(SSLMBean var0) throws IllegalArgumentException {
      if (!var0.isEnabled() && !isListenPortEnabled(var0)) {
         throw new IllegalArgumentException("Either ListenPort or SSLListenPort must be enabled");
      } else {
         if (var0.isEnabled()) {
            ServerMBean var1 = (ServerMBean)var0.getParent();
            if (var1 != null) {
               int var2 = var1.getListenPort();
               if (var2 == var0.getListenPort()) {
                  Loggable var3 = SecurityLogger.logSSLListenPortSameAsServerListenPortLoggable(Integer.toString(var2));
                  var3.log();
                  throw new IllegalArgumentException(var3.getMessage());
               }
            }
         }

      }
   }

   public static void validateServer(ServerMBean var0) throws IllegalArgumentException {
      if (!var0.isListenPortEnabled() && !isSSLListenPortEnabled(var0)) {
         throw new IllegalArgumentException("Either ListenPort or SSLListenPort must be enabled");
      } else {
         String var1 = var0.getName();

         try {
            if (!LegalHelper.serverMBeanSetNameLegalCheck(var1, var0)) {
               throw new IllegalArgumentException("ServerName " + var1 + " is invalid");
            }
         } catch (InvalidAttributeValueException var3) {
            throw new IllegalArgumentException(var3.getMessage());
         }
      }
   }

   /** @deprecated */
   public static void checkListenAddress(ServerMBean var0, Object var1) throws InvalidAttributeValueException {
      if (var1 == null) {
         throw new InvalidAttributeValueException("null port");
      } else if (!(var1 instanceof Integer)) {
         throw new InvalidAttributeValueException("port not integer:" + var1);
      } else {
         int var2 = (Integer)var1;
         if (var0.getSSL().getListenPort() == var2) {
            throw new InvalidAttributeValueException("Listen port cannot be the same as SSL port");
         }
      }
   }

   public static void validateFederationServices(FederationServicesMBean var0) throws IllegalArgumentException {
   }

   public static void validateSingleSignOnServices(SingleSignOnServicesMBean var0) throws IllegalArgumentException {
   }
}
