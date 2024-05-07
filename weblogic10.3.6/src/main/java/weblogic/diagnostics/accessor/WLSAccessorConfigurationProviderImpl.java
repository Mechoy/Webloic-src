package weblogic.diagnostics.accessor;

import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import weblogic.diagnostics.accessor.parser.LogRecordParser;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.WLDFDataRetirementByAgeMBean;
import weblogic.management.configuration.WLDFServerDiagnosticMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class WLSAccessorConfigurationProviderImpl implements AccessorConfigurationProvider, AccessorConstants {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticAccessor");
   private static final AuthenticatedSubject KERNELID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final RuntimeAccess runtimeAccess;
   private AccessorMBeanFactory accessorMBeanFactory;
   private Map accessorMap = new HashMap();
   private static final String[] EDITABLE_ACCESSOR_TYPES;
   private static final String[] SIZE_BASED_RETIREMENT_PARTICIPANTS;

   private static WLDFServerDiagnosticMBean getWLDFConfiguration() {
      ServerMBean var0 = runtimeAccess.getServer();
      return var0.getServerDiagnosticConfig();
   }

   public WLSAccessorConfigurationProviderImpl(AccessorMBeanFactory var1) {
      this.accessorMBeanFactory = var1;
   }

   public boolean isDataRetirementTestModeEnabled() {
      return getWLDFConfiguration().isDataRetirementTestModeEnabled();
   }

   public String getStoreDirectory() {
      return AccessorUtils.getDiagnosticStoreDirectory();
   }

   public boolean isDataRetirementEnabled() {
      return getWLDFConfiguration().isDataRetirementEnabled();
   }

   public int getPreferredStoreSizeLimit() {
      return getWLDFConfiguration().getPreferredStoreSizeLimit();
   }

   public int getStoreSizeCheckPeriod() {
      return getWLDFConfiguration().getStoreSizeCheckPeriod();
   }

   public AccessorConfiguration getAccessorConfiguration(String var1) throws UnknownLogTypeException {
      Map var2 = this.getAccessorProperties(var1);
      if (var2.get("logFilePath") != null) {
         return new LogAccessorConfigurationImpl(var1, var2);
      } else {
         return (AccessorConfiguration)(isEditableAccessor(var1) ? new EditableAccessorConfigurationImpl(var1, var2) : new AccessorConfigurationImpl(var1, var2));
      }
   }

   public synchronized String[] getAccessorNames() {
      HashSet var1 = new HashSet();

      String[] var2;
      try {
         var2 = this.accessorMBeanFactory.getAvailableDiagnosticDataAccessorNames();
         int var3 = var2 != null ? var2.length : 0;

         for(int var4 = 0; var4 < var3; ++var4) {
            var1.add(var2[var4]);
         }
      } catch (ManagementException var5) {
         UnexpectedExceptionHandler.handle(var5.getMessage(), var5);
      }

      var1.addAll(this.accessorMap.keySet());
      var2 = new String[var1.size()];
      var2 = (String[])((String[])var1.toArray(var2));
      return var2;
   }

   public synchronized void addAccessor(AccessorConfiguration var1) {
      String var2 = var1.getName();
      if (this.accessorMap.get(var2) == null) {
         this.accessorMap.put(var2, var1);
      }

   }

   public synchronized void removeAccessor(AccessorConfiguration var1) {
      String var2 = var1.getName();
      if (this.accessorMap.get(var2) != null) {
         this.accessorMap.remove(var2);
      }

   }

   private static String getAccessorType(String var0) {
      String[] var1 = var0.split("/");
      return var1[0];
   }

   private static boolean isEditableAccessor(String var0) {
      String var1 = getAccessorType(var0);

      for(int var2 = 0; var2 < EDITABLE_ACCESSOR_TYPES.length; ++var2) {
         if (var1.equals(EDITABLE_ACCESSOR_TYPES[var2])) {
            return true;
         }
      }

      return false;
   }

   private Map getAccessorProperties(String var1) throws UnknownLogTypeException {
      String[] var2 = var1.split("/");
      String var3 = var2[0];
      Map var4 = null;
      boolean var5 = false;
      boolean var6 = AccessorUtils.isAdminServer();
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Admin server = " + var6);
      }

      if (var3.equals("ServerLog")) {
         var4 = AccessorUtils.getParamsForServerLog();
      } else if (var3.equals("DomainLog") && var6) {
         var4 = AccessorUtils.getParamsForDomainLog();
      } else if (var3.equals("HarvestedDataArchive")) {
         var4 = AccessorUtils.getParamsForDiagnosticDataArchive();
      } else if (var3.equals("EventsDataArchive")) {
         var4 = AccessorUtils.getParamsForDiagnosticDataArchive();
      } else if (var3.equals("DataSourceLog")) {
         var4 = AccessorUtils.getParamsForDataSourceLog();
      } else if (var3.equals("HTTPAccessLog")) {
         var4 = AccessorUtils.getParamsForHTTPAccessLog(var2);
      } else if (var3.equals("WebAppLog")) {
         var4 = AccessorUtils.getParamsForWebAppLog(var2);
      } else if (var3.equals("ConnectorLog")) {
         var4 = AccessorUtils.getParamsForConnectorLog(var1);
      } else if (var3.equals("JMSMessageLog")) {
         var4 = AccessorUtils.getParamsForJMSMessageLog(var1);
      } else if (var3.equals("JMSSAFMessageLog")) {
         var4 = AccessorUtils.getParamsForJMSSAFMessageLog(var1);
      } else {
         if (!var3.equals("CUSTOM")) {
            Loggable var7 = DiagnosticsLogger.logUnknownLogTypeLoggable(var3);
            throw new UnknownLogTypeException(var7.getMessageBody());
         }

         var5 = true;
         var4 = AccessorUtils.getParamsForGenericDataArchive();
      }

      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Config parameters for accessor " + var1 + ":" + this.getConfigurationParamString(var4));
      }

      return var4;
   }

   private String getConfigurationParamString(Map var1) {
      StringBuffer var2 = new StringBuffer();
      Iterator var3 = var1.keySet().iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         Object var5 = var1.get(var4);
         var2.append("\n   " + var4 + "=" + var5);
      }

      return var2.toString();
   }

   static {
      runtimeAccess = ManagementService.getRuntimeAccess(KERNELID);
      EDITABLE_ACCESSOR_TYPES = new String[]{"CUSTOM", "EventsDataArchive", "HarvestedDataArchive"};
      SIZE_BASED_RETIREMENT_PARTICIPANTS = new String[]{"EventsDataArchive", "HarvestedDataArchive"};
   }

   private static class EditableAccessorConfigurationImpl extends AccessorConfigurationImpl implements EditableAccessorConfiguration {
      EditableAccessorConfigurationImpl(String var1, Map var2) {
         super(var1, var2);
      }

      private WLDFDataRetirementByAgeMBean lookupWLDFDataRetirementByAge() {
         WLDFServerDiagnosticMBean var1 = WLSAccessorConfigurationProviderImpl.getWLDFConfiguration();
         String var2 = this.getName();
         WLDFDataRetirementByAgeMBean[] var3 = var1.getWLDFDataRetirementByAges();
         int var4 = var3 != null ? var3.length : 0;

         for(int var5 = 0; var5 < var4; ++var5) {
            WLDFDataRetirementByAgeMBean var6 = var3[var5];
            if (var2.equals(var6.getArchiveName())) {
               return var6;
            }
         }

         return null;
      }

      public int getRetirementAge() {
         WLDFDataRetirementByAgeMBean var1 = this.lookupWLDFDataRetirementByAge();
         return var1 != null ? var1.getRetirementAge() : 72;
      }

      public int getRetirementPeriod() {
         WLDFDataRetirementByAgeMBean var1 = this.lookupWLDFDataRetirementByAge();
         return var1 != null ? var1.getRetirementPeriod() : 24;
      }

      public int getRetirementTime() {
         WLDFDataRetirementByAgeMBean var1 = this.lookupWLDFDataRetirementByAge();
         return var1 != null ? var1.getRetirementTime() : 0;
      }

      public boolean isAgeBasedDataRetirementEnabled() {
         WLDFDataRetirementByAgeMBean var1 = this.lookupWLDFDataRetirementByAge();
         return var1 != null && var1.isEnabled();
      }

      public boolean isParticipantInSizeBasedDataRetirement() {
         WLDFServerDiagnosticMBean var1 = WLSAccessorConfigurationProviderImpl.getWLDFConfiguration();
         String var2 = var1.getDiagnosticDataArchiveType();
         if ("FileStoreArchive".equals(var2)) {
            for(int var3 = 0; var3 < WLSAccessorConfigurationProviderImpl.SIZE_BASED_RETIREMENT_PARTICIPANTS.length; ++var3) {
               if (WLSAccessorConfigurationProviderImpl.SIZE_BASED_RETIREMENT_PARTICIPANTS[var3].equals(this.getName())) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private static class LogAccessorConfigurationImpl extends AccessorConfigurationImpl implements LogAccessorConfiguration {
      private String logFile;
      private String rotationDir;

      LogAccessorConfigurationImpl(String var1, Map var2) {
         super(var1, var2);
         this.logFile = (String)var2.get("logFilePath");
         this.rotationDir = (String)var2.get("logRotationDir");
      }

      public LogRecordParser getRecordParser() {
         return null;
      }

      public String getLogFilePath() {
         return this.logFile;
      }

      public String getLogFileRotationDirectory() {
         return this.rotationDir;
      }
   }

   private static class AccessorConfigurationImpl implements AccessorConfiguration {
      private String accessorName;
      private Map params;
      private boolean isModifiable;

      AccessorConfigurationImpl(String var1, Map var2) {
         this.accessorName = var1;
         this.params = var2;
         String var3 = WLSAccessorConfigurationProviderImpl.getAccessorType(var1);
         if (var3.equals("WebAppLog")) {
            this.isModifiable = true;
         } else if (var3.equals("ConnectorLog")) {
            this.isModifiable = true;
         } else if (var3.equals("JMSMessageLog") || var3.equals("JMSSAFMessageLog")) {
            this.isModifiable = true;
         }

      }

      public String getName() {
         return this.accessorName;
      }

      public boolean isModifiableConfiguration() {
         return this.isModifiable;
      }

      public ColumnInfo[] getColumns() {
         return null;
      }

      public Map getAccessorParameters() {
         return this.params;
      }
   }
}
