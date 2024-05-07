package weblogic.management.configuration;

import java.util.Arrays;
import weblogic.common.internal.VersionInfo;
import weblogic.common.internal.VersioningError;
import weblogic.jms.module.validators.JMSModuleValidator;

public class DomainValidator {
   public static void validateDomain(DomainMBean var0) throws IllegalArgumentException {
      WLDFValidator.validateWLDFSystemResources(var0);
      WLDFValidator.validateWLDFServerDiagnosticConfiguration(var0);
      JMSModuleValidator.validateJMSDomain(var0);
      JMSLegalHelper.validateStoreTargets(var0);
      JMSLegalHelper.validateStoreParams(var0);
      SNMPValidator.validateSNMPAgentDeployments(var0);
      validateName(var0.getName());
      validateSystemResources(var0.getSystemResources());
   }

   public static void validateName(String var0) throws IllegalArgumentException {
      if (var0 == null || var0.length() == 0) {
         throw new IllegalArgumentException("Name may not be null or empty string");
      }
   }

   private static void validateSystemResources(SystemResourceMBean[] var0) throws IllegalArgumentException {
      String[] var1 = new String[var0.length];

      int var2;
      for(var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = var0[var2].getName();
      }

      Arrays.sort(var1);

      for(var2 = 0; var2 < var1.length - 1; ++var2) {
         if (var1[var2].equals(var1[var2 + 1])) {
            String var3 = "Multiple system resources with same name: " + var1[var2];
            throw new IllegalArgumentException(var3);
         }
      }

   }

   public static void validateVersionString(String var0) throws IllegalArgumentException {
      try {
         new VersionInfo(var0);
      } catch (Exception var2) {
         throw new IllegalArgumentException("Invalid version string: " + var0, var2);
      } catch (VersioningError var3) {
         throw new IllegalArgumentException("Invalid version string: " + var0, var3);
      }
   }
}
