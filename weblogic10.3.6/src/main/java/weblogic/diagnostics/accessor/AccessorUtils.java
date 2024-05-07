package weblogic.diagnostics.accessor;

import java.io.File;
import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.connector.external.RAUtil;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBean;
import weblogic.logging.Loggable;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DataSourceLogFileMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.JMSMessageLogFileMBean;
import weblogic.management.configuration.JMSSAFMessageLogFileMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.LogMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.configuration.WLDFServerDiagnosticMBean;
import weblogic.management.configuration.WebServerLogMBean;
import weblogic.management.configuration.WebServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.WLDFArchiveRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AdminResource;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.servlet.utils.ServletAccessorHelper;

public final class AccessorUtils implements AccessorConstants {
   private static final DebugLogger ACCESSOR_DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticAccessor");
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static AuthorizationManager am = null;
   private static final AdminResource LOG_VIEW_RESOURCE = new AdminResource("ViewLog", (String)null, (String)null);
   private static final RuntimeAccess runtimeAccess;

   public static void ensureUserAuthorized() throws ManagementException {
      if (am == null) {
         am = (AuthorizationManager)SecurityServiceManager.getSecurityService(KERNEL_ID, "weblogicDEFAULT", ServiceType.AUTHORIZE);
      }

      AuthenticatedSubject var0 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      if (!am.isAccessAllowed(var0, LOG_VIEW_RESOURCE, (ContextHandler)null)) {
         Loggable var1 = DiagnosticsLogger.logUserNotAuthorizedToViewLogsLoggable(var0.toString());
         var1.log();
         throw new ManagementException(var1.getMessage());
      }
   }

   public static boolean isAdminServer() {
      return runtimeAccess.isAdminServer();
   }

   public static Set getAvailableVirtualHosts() {
      VirtualHostMBean[] var0 = runtimeAccess.getDomain().getVirtualHosts();
      HashSet var1 = new HashSet();
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (isDeploymentAvailableOnTarget(var0[var2])) {
               var1.add(var0[var2]);
            }
         }
      }

      return var1;
   }

   public static Set getAvailableJMSServers() {
      JMSServerMBean[] var0 = runtimeAccess.getDomain().getJMSServers();
      HashSet var1 = new HashSet();
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (isDeploymentAvailableOnTarget(var0[var2])) {
               var1.add(var0[var2]);
            }
         }
      }

      return var1;
   }

   public static Set getAvailableSAFAgents() {
      SAFAgentMBean[] var0 = runtimeAccess.getDomain().getSAFAgents();
      HashSet var1 = new HashSet();
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (isDeploymentAvailableOnTarget(var0[var2])) {
               var1.add(var0[var2]);
            }
         }
      }

      return var1;
   }

   public static Set getAvailableCustomArchives() {
      WLDFArchiveRuntimeMBean[] var0 = runtimeAccess.getServerRuntime().getWLDFRuntime().getWLDFArchiveRuntimes();
      HashSet var1 = new HashSet();
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2].getName().startsWith("CUSTOM/")) {
               var1.add(var0[var2]);
            }
         }
      }

      return var1;
   }

   private static boolean isDeploymentAvailableOnTarget(DeploymentMBean var0) {
      TargetMBean[] var1 = var0.getTargets();
      if (var1 == null) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            TargetMBean var3 = var1[var2];
            String var4 = var1[var2].getName();
            if (var3 instanceof ServerMBean && var4.equals(runtimeAccess.getServerName())) {
               return true;
            }

            if (var3 instanceof ClusterMBean) {
               ClusterMBean var5 = runtimeAccess.getServer().getCluster();
               if (var5 != null && var5.getName().equals(var4)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static boolean compareMaps(Map var0, Map var1) {
      if (var0.size() != var1.size()) {
         return false;
      } else {
         Set var2 = var0.keySet();
         Iterator var3 = var2.iterator();

         Object var5;
         Object var6;
         label34:
         do {
            do {
               if (!var3.hasNext()) {
                  return true;
               }

               Object var4 = var3.next();
               var5 = var0.get(var4);
               var6 = var1.get(var4);
               if (var5 == null || var6 == null) {
                  continue label34;
               }
            } while(var5.equals(var6));

            return false;
         } while(var5 == null && var6 == null);

         return false;
      }
   }

   public static String getDiagnosticStoreDirectory() {
      ServerMBean var0 = runtimeAccess.getServer();
      WLDFServerDiagnosticMBean var1 = var0.getServerDiagnosticConfig();
      String var2 = var1.getDiagnosticStoreDir();
      File var3 = new File(var2);
      if (!var3.isAbsolute()) {
         var2 = DomainDir.getPathRelativeServerDir(runtimeAccess.getServerName(), var2);
         var3 = new File(var2);
      }

      if (!var3.exists()) {
         var3.mkdirs();
      }

      var2 = var3.getAbsolutePath();
      return var2;
   }

   static Map getParamsForServerLog() {
      LogMBean var0 = runtimeAccess.getServer().getLog();
      String var1 = var0.computeLogFilePath();
      String var2 = var0.getLogFileRotationDir();
      HashMap var3 = new HashMap();
      var3.put("logFilePath", var1);
      var3.put("storeDir", getDiagnosticStoreDirectory());
      var3.put("logRotationDir", var2);
      return var3;
   }

   static Map getParamsForDataSourceLog() {
      DataSourceLogFileMBean var0 = runtimeAccess.getServer().getDataSource().getDataSourceLogFile();
      String var1 = var0.computeLogFilePath();
      String var2 = var0.getLogFileRotationDir();
      HashMap var3 = new HashMap();
      var3.put("logFilePath", var1);
      var3.put("storeDir", getDiagnosticStoreDirectory());
      var3.put("logRotationDir", var2);
      return var3;
   }

   static Map getParamsForDomainLog() {
      LogMBean var0 = runtimeAccess.getDomain().getLog();
      String var1 = var0.computeLogFilePath();
      String var2 = var0.getLogFileRotationDir();
      HashMap var3 = new HashMap();
      var3.put("logFilePath", var1);
      var3.put("storeDir", getDiagnosticStoreDirectory());
      var3.put("logRotationDir", var2);
      return var3;
   }

   static Map getParamsForHTTPAccessLog(String[] var0) throws UnknownLogTypeException {
      WebServerMBean var1 = runtimeAccess.getServer().getWebServer();
      String var2 = var0.length == 2 ? var0[1] : null;
      if (var2 != null && var2.length() > 0) {
         Iterator var3 = getAvailableVirtualHosts().iterator();

         while(var3.hasNext()) {
            WebServerMBean var4 = (WebServerMBean)var3.next();
            if (var4.getName().equals(var2)) {
               var1 = var4;
               break;
            }
         }
      }

      WebServerLogMBean var7 = var1.getWebServerLog();
      String var8 = var7.computeLogFilePath();
      String var5 = var7.getLogFileRotationDir();
      HashMap var6 = new HashMap();
      var6.put("logFilePath", var8);
      var6.put("storeDir", getDiagnosticStoreDirectory());
      var6.put("logRotationDir", var5);
      if (var7.getLogFileFormat().equals("extended")) {
         var6.put("elfFields", var7.getELFFields());
      }

      return var6;
   }

   static Map getParamsForDiagnosticDataArchive() {
      HashMap var0 = new HashMap();
      WLDFServerDiagnosticMBean var1 = runtimeAccess.getServer().getServerDiagnosticConfig();
      String var2 = var1.getDiagnosticDataArchiveType();
      var0.put("storeDir", getDiagnosticStoreDirectory());
      if (var2.equals("JDBCArchive")) {
         JDBCSystemResourceMBean var3 = var1.getDiagnosticJDBCResource();
         String var4 = null;
         if (var3 != null) {
            JDBCDataSourceBean var5 = var3.getJDBCResource();
            JDBCDataSourceParamsBean var6 = var5.getJDBCDataSourceParams();
            String[] var7 = var6.getJNDINames();
            if (var7 != null && var7.length > 0) {
               var4 = var7[0];
            }
         }

         if (var4 != null) {
            var0.put("jndiName", var4);
         } else {
            DiagnosticsLogger.logIncompleteJDBCArchiveConfiguration();
         }
      }

      return var0;
   }

   static Map getParamsForGenericDataArchive() {
      HashMap var0 = new HashMap();
      var0.put("storeDir", getDiagnosticStoreDirectory());
      return var0;
   }

   static Map getParamsForWebAppLog(String[] var0) throws UnknownLogTypeException {
      HashMap var1 = new HashMap();
      int var2 = var0.length;
      String var3 = var2 >= 2 ? var0[1] : null;
      String var4 = "/";
      if (var2 >= 3) {
         var4 = var4 + var0[2];
      }

      String var5 = ServletAccessorHelper.getLogFileName(var3, var4);
      if (var5 == null) {
         throw new UnknownLogTypeException("Logs dont exist for the webapp");
      } else {
         String var6 = ServletAccessorHelper.getLogFileRotationDir(var3, var4);
         var1.put("logFilePath", var5);
         var1.put("storeDir", getDiagnosticStoreDirectory());
         var1.put("logRotationDir", var6);
         return var1;
      }
   }

   static Map getParamsForConnectorLog(String var0) throws UnknownLogTypeException {
      HashMap var1 = new HashMap();
      int var2 = "ConnectorLog".length() + 1;
      String var3 = var0.substring(var2);
      String var4 = RAUtil.getLogFileName(var3);
      if (var4 == null) {
         throw new UnknownLogTypeException("Logs dont exist for the outbound connection " + var3);
      } else {
         String var5 = RAUtil.getLogFileRotationDir(var3);
         var1.put("logFilePath", var4);
         var1.put("storeDir", getDiagnosticStoreDirectory());
         var1.put("logRotationDir", var5);
         return var1;
      }
   }

   static Map getParamsForJMSMessageLog(String var0) throws UnknownLogTypeException {
      HashMap var1 = new HashMap();
      int var2 = "JMSMessageLog".length() + 1;
      String var3 = var0.substring(var2);
      JMSServerMBean var4 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().lookupJMSServer(var3);
      if (var4 == null) {
         throw new UnknownLogTypeException("JMS Server does not exist " + var3);
      } else {
         JMSMessageLogFileMBean var5 = var4.getJMSMessageLogFile();
         String var6 = var5.computeLogFilePath();
         String var7 = var5.getLogFileRotationDir();
         var1.put("logFilePath", var6);
         var1.put("logRotationDir", var7);
         var1.put("storeDir", getDiagnosticStoreDirectory());
         return var1;
      }
   }

   static Map getParamsForJMSSAFMessageLog(String var0) throws UnknownLogTypeException {
      HashMap var1 = new HashMap();
      int var2 = "JMSSAFMessageLog".length() + 1;
      String var3 = var0.substring(var2);
      SAFAgentMBean var4 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().lookupSAFAgent(var3);
      if (var4 == null) {
         throw new UnknownLogTypeException("SAF Agent does not exist " + var4);
      } else {
         JMSSAFMessageLogFileMBean var5 = var4.getJMSSAFMessageLogFile();
         String var6 = var5.computeLogFilePath();
         String var7 = var5.getLogFileRotationDir();
         var1.put("logFilePath", var6);
         var1.put("logRotationDir", var7);
         var1.put("storeDir", getDiagnosticStoreDirectory());
         return var1;
      }
   }

   static {
      runtimeAccess = ManagementService.getRuntimeAccess(KERNEL_ID);
   }
}
