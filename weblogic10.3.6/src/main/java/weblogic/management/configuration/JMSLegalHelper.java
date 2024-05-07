package weblogic.management.configuration;

import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.management.internal.ManagementTextTextFormatter;

public final class JMSLegalHelper {
   private static final int DIABLO_VERSION = 9;

   private static boolean targetMatchStoreTarget(DeploymentMBean var0, DeploymentMBean var1) {
      TargetMBean[] var2 = var0.getTargets();
      TargetMBean[] var3 = var1.getTargets();
      if (var2 != null && var2.length != 0 && var2[0] != null) {
         if (var3 != null && var3.length != 0 && var3[0] != null) {
            return var2[0].getName().equals(var3[0].getName());
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   private static boolean usesMigratableTarget(DeploymentMBean var0) {
      TargetMBean[] var1 = var0.getTargets();
      if (var1 != null && var1.length != 0) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] instanceof MigratableTargetMBean) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static int getConfigMajorVersion(DomainMBean var0) {
      if (var0 == null) {
         return 9;
      } else {
         String var1 = var0.getConfigurationVersion();
         if (var1 != null && var1.length() != 0) {
            int var2 = var1.indexOf(46);
            String var3;
            if (var2 <= 0) {
               var3 = var1;
            } else {
               var3 = var1.substring(0, var2);
            }

            try {
               return Integer.parseInt(var3);
            } catch (NumberFormatException var5) {
               return 9;
            }
         } else {
            return 9;
         }
      }
   }

   public static void validateJMSServer(JMSServerMBean var0) throws IllegalArgumentException {
      if (getConfigMajorVersion((DomainMBean)var0.getParent()) >= 9 && var0.getStore() == null && var0.getPersistentStore() == null && var0.getStoreEnabled() && usesMigratableTarget(var0)) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getJMSServerMigratableStore(var0.getName()));
      } else if (var0.getPersistentStore() != null && var0.getStoreEnabled() && !targetMatchStoreTarget(var0, var0.getPersistentStore())) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getStoreTargetMismatch(var0.getName()));
      } else if (!var0.isHostingTemporaryDestinations() && (var0.getTemporaryTemplateResource() != null || var0.getTemporaryTemplateName() != null)) {
         throw new IllegalArgumentException("The JMSServer " + var0.getName() + " does not allow temporary destinations to be hosted, but " + " has either the temporary-template-resource or temporary-template-name defined");
      } else {
         if (var0.isHostingTemporaryDestinations()) {
            String var1 = var0.getTemporaryTemplateResource();
            String var2 = var0.getTemporaryTemplateName();
            if (var1 != null && var2 == null) {
               throw new IllegalArgumentException("A temporary template resource was specified (" + var1 + ") but no temporary template name was given");
            }

            if (var2 != null && var1 == null) {
               throw new IllegalArgumentException("A temporary template name was specified (" + var2 + ") but no temporary template resource was given");
            }

            if (var2 != null) {
               DomainMBean var3 = JMSBeanHelper.getDomain(var0);
               Object var4 = null;
               if (var1.equals("interop-jms")) {
                  JMSInteropModuleMBean[] var5 = var3.getJMSInteropModules();
                  if (var5.length > 0) {
                     var4 = var5[0];
                  }
               }

               if (var4 == null) {
                  var4 = var3.lookupJMSSystemResource(var1);
               }

               if (var4 == null) {
                  throw new IllegalArgumentException(JMSExceptionLogger.logNoTemporaryTemplateLoggable(var0.getName(), var1, var2).getMessage());
               }

               JMSBean var7 = ((JMSSystemResourceMBean)var4).getJMSResource();
               if (var7 == null) {
                  return;
               }

               TemplateBean var6 = var7.lookupTemplate(var2);
               if (var6 == null) {
                  throw new IllegalArgumentException("The JMSServer " + var0.getName() + " has a temporary resource of " + var1 + " and a temporary template name of " + var2 + " but not template of that name can be found in the given resource");
               }
            }
         }

      }
   }

   public static void validateSAFAgent(SAFAgentMBean var0) throws IllegalArgumentException {
      if (var0.getStore() == null && usesMigratableTarget(var0)) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getJMSServerMigratableStore(var0.getName()));
      } else if (var0.getStore() == null || var0.getTargets().length <= 1 && (var0.getTargets().length != 1 || !(var0.getTargets()[0] instanceof ClusterMBean))) {
         if (var0.getStore() != null && !targetMatchStoreTarget(var0, var0.getStore())) {
            throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getStoreTargetMismatch(var0.getName()));
         } else if (var0.getTargets().length == 1 && var0.getTargets()[0] instanceof MigratableTargetMBean && "Receiving-only".equals(var0.getServiceType())) {
            throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getReceivingAgentInvlid4MT(var0.getName()));
         }
      } else {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getSAFAgentSingleTarget(var0.getName()));
      }
   }

   public static void validateServerBytesValues(JMSServerMBean var0) throws IllegalArgumentException {
      if (var0.getBytesMaximum() >= 0L && var0.getBytesThresholdHigh() > var0.getBytesMaximum()) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getJMSBytesMaxOverThreshold(var0.getName()));
      } else if (var0.getBytesThresholdHigh() >= 0L && var0.getBytesThresholdHigh() < var0.getBytesThresholdLow()) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getJMSBytesThresholdsReversed(var0.getName()));
      }
   }

   public static void validateServerBytesValues(SAFAgentMBean var0) throws IllegalArgumentException {
      if (var0.getBytesMaximum() >= 0L && var0.getBytesThresholdHigh() > var0.getBytesMaximum()) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getJMSBytesMaxOverThreshold(var0.getName()));
      } else if (var0.getBytesThresholdHigh() >= 0L && var0.getBytesThresholdHigh() < var0.getBytesThresholdLow()) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getJMSBytesThresholdsReversed(var0.getName()));
      }
   }

   public static void validateServerMessagesValues(JMSServerMBean var0) throws IllegalArgumentException {
      if (var0.getMessagesMaximum() >= 0L && var0.getMessagesThresholdHigh() > var0.getMessagesMaximum()) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getJMSMessagesMaxOverThreshold(var0.getName()));
      } else if (var0.getMessagesThresholdHigh() >= 0L && var0.getMessagesThresholdHigh() < var0.getMessagesThresholdLow()) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getJMSMessagesThresholdsReversed(var0.getName()));
      }
   }

   public static void validateServerMessagesValues(SAFAgentMBean var0) throws IllegalArgumentException {
      if (var0.getMessagesMaximum() >= 0L && var0.getMessagesThresholdHigh() > var0.getMessagesMaximum()) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getJMSMessagesMaxOverThreshold(var0.getName()));
      } else if (var0.getMessagesThresholdHigh() >= 0L && var0.getMessagesThresholdHigh() < var0.getMessagesThresholdLow()) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getJMSMessagesThresholdsReversed(var0.getName()));
      }
   }

   public static void validateSingleServerTargets(TargetMBean var0) throws IllegalArgumentException {
      if (var0 != null) {
         if (!(var0 instanceof ServerMBean) && !(var0 instanceof MigratableTargetMBean)) {
            throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getSingleTargetRequired() + ": " + var0);
         }
      }
   }

   public static void validateSingleServerTargets(TargetMBean[] var0) throws IllegalArgumentException {
      if (var0 != null && var0.length != 0) {
         if (var0.length > 1) {
            StringBuffer var1 = new StringBuffer("[");

            for(int var2 = 0; var2 < var0.length; ++var2) {
               TargetMBean var3 = var0[var2];
               var1.append(var3.getName());
               if (var2 < var0.length - 1) {
                  var1.append(",");
               }
            }

            var1.append("]");
            throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getSingleTargetRequired() + ": " + var1);
         } else {
            validateSingleServerTargets(var0[0]);
         }
      }
   }

   public static void validateStoreTargets(DomainMBean var0) throws IllegalArgumentException {
      JMSServerMBean[] var1 = var0.getJMSServers();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         JMSServerMBean var3 = var1[var2];
         if (var3.getPersistentStore() != null && !targetMatchStoreTarget(var3, var3.getPersistentStore())) {
            throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getStoreTargetMismatch(var3.getName()));
         }
      }

      SAFAgentMBean[] var6 = var0.getSAFAgents();

      for(int var7 = 0; var7 < var6.length; ++var7) {
         SAFAgentMBean var4 = var6[var7];
         if (var4.getStore() != null && !targetMatchStoreTarget(var4, var4.getStore())) {
            throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getStoreTargetMismatch(var4.getName()));
         }
      }

      PathServiceMBean[] var8 = var0.getPathServices();

      for(int var9 = 0; var9 < var8.length; ++var9) {
         PathServiceMBean var5 = var8[var9];
         if (var5.getPersistentStore() != null && !targetMatchStoreTarget(var5, var5.getPersistentStore())) {
            throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getPathServiceStoreTargetMismatch(var5.getName()));
         }
      }

   }

   public static void validateStoreParams(DomainMBean var0) throws IllegalArgumentException {
      FileStoreMBean[] var1 = var0.getFileStores();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         FileStoreMBean var3 = var1[var2];
         TargetMBean[] var4 = var3.getTargets();
         if (var4.length != 0) {
            TargetMBean var5 = var3.getTargets()[0];
            if (var5 != null && var5 instanceof MigratableTargetMBean && (var3.getDirectory() == null || var3.getDirectory().trim().equals(""))) {
               throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getMigratableFileStoreDirectoryMissing(var3.getName(), var5.getName()));
            }
         }

         validateStoreConfig(var3);
      }

      ServerMBean[] var8 = var0.getServers();
      ServerMBean[] var9 = var8;
      int var11 = var8.length;

      int var13;
      for(var13 = 0; var13 < var11; ++var13) {
         ServerMBean var6 = var9[var13];
         validateStoreConfig(var6.getDefaultFileStore());
         validateDiagnosticStoreConfig(var6.getServerDiagnosticConfig());
      }

      JMSServerMBean[] var10 = var0.getJMSServers();
      JMSServerMBean[] var12 = var10;
      var13 = var10.length;

      for(int var14 = 0; var14 < var13; ++var14) {
         JMSServerMBean var7 = var12[var14];
         validatePagingConfig(var7);
      }

   }

   private static void invalidNumberRange(Object var0, String var1, Object var2, Object var3, Object var4) {
      throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getInvalidNumberRange(String.valueOf(var0), var1, String.valueOf(var2), String.valueOf(var3), String.valueOf(var4)));
   }

   private static void validateStoreTuning(int var0, int var1, int var2, long var3, int var5) {
      if (var0 != -1 && var0 < 65536) {
         invalidNumberRange(var0, "MinWindowBufferSize", -1, 65536, 1073741824);
      }

      if (var1 != -1 && (var1 < 65536 || var1 < var0)) {
         Object var6 = var0 == -1 ? 65536 : "MinWindowBufferSize";
         invalidNumberRange(var1, "MaxWindowBufferSize", -1, var6, 1073741824);
      }

      if (var2 != -1 && var2 < 1048576) {
         invalidNumberRange(var2, "IoBufferSize", -1, 1048576, 67108864);
      }

      if (var3 != 0L && var3 < 1048576L) {
         invalidNumberRange(var3, "InitialSize", 0, 1048576, 4503599627370496L);
      }

      if (var5 != -1 && var5 < 512) {
         invalidNumberRange(var5, "BlockSize", -1, 512, 8192);
      }

   }

   private static void validateDiagnosticStoreConfig(WLDFServerDiagnosticMBean var0) throws IllegalArgumentException {
      int var1 = var0.getDiagnosticStoreMinWindowBufferSize();
      int var2 = var0.getDiagnosticStoreMaxWindowBufferSize();
      int var3 = var0.getDiagnosticStoreIoBufferSize();
      int var4 = var0.getDiagnosticStoreBlockSize();
      validateStoreTuning(var1, var2, var3, 0L, var4);
   }

   private static void validatePagingConfig(JMSServerMBean var0) throws IllegalArgumentException {
      int var1 = var0.getPagingMinWindowBufferSize();
      int var2 = var0.getPagingMaxWindowBufferSize();
      int var3 = var0.getPagingIoBufferSize();
      int var4 = var0.getPagingBlockSize();
      validateStoreTuning(var1, var2, var3, 0L, var4);
   }

   private static void validateStoreConfig(GenericFileStoreMBean var0) throws IllegalArgumentException {
      int var1 = var0.getMinWindowBufferSize();
      int var2 = var0.getMaxWindowBufferSize();
      int var3 = var0.getIoBufferSize();
      long var4 = var0.getInitialSize();
      int var6 = var0.getBlockSize();
      validateStoreTuning(var1, var2, var3, var4, var6);
   }

   public static void validateJDBCPrefix(String var0) throws IllegalArgumentException {
      if (var0 != null && var0.length() != 0) {
         if (var0.equalsIgnoreCase("jmsmsg") || var0.equalsIgnoreCase("jmsstore") || var0.equalsIgnoreCase("jmsstate")) {
            throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getIllegalJMSJDBCPrefix());
         }
      }
   }

   public static void validateRetryBaseAndMax(SAFAgentMBean var0) {
      if (var0.getDefaultRetryDelayMultiplier() > 1.0 && var0.getDefaultRetryDelayBase() > var0.getDefaultRetryDelayMaximum()) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getIllegalRetryDelayBaseAndMax(var0.getName(), var0.getDefaultRetryDelayBase(), var0.getDefaultRetryDelayMaximum()));
      }
   }

   public static void validateSAFAgentTargets(TargetMBean[] var0) throws IllegalArgumentException {
      if (var0 != null && var0.length != 0) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            TargetMBean var2 = var0[var1];
            if (!(var2 instanceof ServerMBean) && !(var2 instanceof ClusterMBean) && !(var2 instanceof MigratableTargetMBean)) {
               throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getIllegalSAFAgentTargets(var2.getType()));
            }

            if (var2 instanceof MigratableTargetMBean && var0.length > 1) {
               throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getIllegalSAFAgentMigratableTargets());
            }
         }

      }
   }

   public static void validateAcknowledgeIntervalValue(long var0) throws IllegalArgumentException {
      if (var0 <= 0L && var0 != -1L) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getAcknowledgeIntervalNotValid(var0));
      }
   }
}
