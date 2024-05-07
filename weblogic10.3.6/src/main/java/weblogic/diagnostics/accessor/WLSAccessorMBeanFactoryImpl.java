package weblogic.diagnostics.accessor;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.connector.external.RAUtil;
import weblogic.diagnostics.accessor.runtime.AccessRuntimeMBean;
import weblogic.diagnostics.accessor.runtime.ArchiveRuntimeMBean;
import weblogic.diagnostics.accessor.runtime.DataAccessRuntimeMBean;
import weblogic.diagnostics.accessor.runtime.DataRetirementTaskRuntimeMBean;
import weblogic.diagnostics.archive.DiagnosticArchiveRuntime;
import weblogic.diagnostics.archive.EditableDataArchive;
import weblogic.diagnostics.archive.WLDFDataRetirementByAgeTaskImpl;
import weblogic.diagnostics.archive.WLDFDiagnosticDbstoreArchiveRuntime;
import weblogic.diagnostics.archive.WLDFDiagnosticFileArchiveRuntime;
import weblogic.diagnostics.archive.WLDFDiagnosticWlstoreArchiveRuntime;
import weblogic.diagnostics.archive.dbstore.JdbcDataArchive;
import weblogic.diagnostics.archive.filestore.FileDataArchive;
import weblogic.diagnostics.archive.wlstore.PersistentStoreDataArchive;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.configuration.WebServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WLDFAccessRuntimeMBean;
import weblogic.management.runtime.WLDFArchiveRuntimeMBean;
import weblogic.management.runtime.WLDFDataAccessRuntimeMBean;
import weblogic.management.runtime.WLDFRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.utils.ServletAccessorHelper;

public class WLSAccessorMBeanFactoryImpl implements AccessorMBeanFactory, AccessorConstants {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticAccessor");
   private static AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final RuntimeAccess runtimeAccess;

   public String[] getAvailableDiagnosticDataAccessorNames() throws ManagementException {
      AccessorUtils.ensureUserAuthorized();
      HashSet var1 = new HashSet();
      var1.add("ServerLog");
      if (runtimeAccess.isAdminServer()) {
         var1.add("DomainLog");
      }

      var1.add("HarvestedDataArchive");
      var1.add("EventsDataArchive");
      var1.add("HTTPAccessLog");
      var1.add("DataSourceLog");
      Iterator var2 = AccessorUtils.getAvailableVirtualHosts().iterator();

      while(var2.hasNext()) {
         VirtualHostMBean var3 = (VirtualHostMBean)var2.next();
         var1.add("HTTPAccessLog/" + var3.getName());
      }

      Set var9 = this.getLogicalNamesForAvailableWebAppLogs();
      if (var9.size() > 0) {
         var1.addAll(var9);
      }

      Set var4 = RAUtil.getAvailableConnectorLogNames();
      if (var4.size() > 0) {
         var1.addAll(var4);
      }

      Iterator var5 = AccessorUtils.getAvailableJMSServers().iterator();

      while(var5.hasNext()) {
         JMSServerMBean var6 = (JMSServerMBean)var5.next();
         var1.add("JMSMessageLog/" + var6.getName());
      }

      Iterator var10 = AccessorUtils.getAvailableSAFAgents().iterator();

      while(var10.hasNext()) {
         SAFAgentMBean var7 = (SAFAgentMBean)var10.next();
         var1.add("JMSSAFMessageLog/" + var7.getName());
      }

      Iterator var11 = AccessorUtils.getAvailableCustomArchives().iterator();

      while(var11.hasNext()) {
         WLDFArchiveRuntimeMBean var8 = (WLDFArchiveRuntimeMBean)var11.next();
         var1.add(var8.getName());
      }

      String[] var12 = new String[var1.size()];
      var1.toArray(var12);
      return var12;
   }

   private Set getLogicalNamesForAvailableWebAppLogs() {
      HashSet var1 = new HashSet();
      Set var2 = AccessorUtils.getAvailableVirtualHosts();
      var2.add(runtimeAccess.getServer().getWebServer());
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         WebServerMBean var4 = (WebServerMBean)var3.next();
         Set var5 = ServletAccessorHelper.getLogicalNamesForWebApps(var4);
         if (!var5.isEmpty()) {
            var1.addAll(var5);
         }
      }

      return var1;
   }

   public AccessRuntimeMBean createDiagnosticAccessRuntime(AccessorConfigurationProvider var1, AccessorSecurityProvider var2, RuntimeMBean var3) throws ManagementException {
      return new DiagnosticAccessRuntime(this, var1, var2, var3);
   }

   public DataAccessRuntimeMBean createDiagnosticDataAccessRuntime(final String var1, final ColumnInfo[] var2, final AccessRuntimeMBean var3) throws ManagementException {
      try {
         WLDFDataAccessRuntimeMBean var4 = (WLDFDataAccessRuntimeMBean)SecurityServiceManager.runAs(KERNEL_ID, SecurityServiceManager.getCurrentSubject(KERNEL_ID), new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return WLSAccessorMBeanFactoryImpl.this.createDataAccessRuntime(var1, var2, (WLDFAccessRuntimeMBean)var3);
            }
         });
         return var4;
      } catch (PrivilegedActionException var5) {
         throw new ManagementException((Throwable)(var5.getCause() == null ? var5 : var5.getCause()));
      }
   }

   private WLDFDataAccessRuntimeMBean createDataAccessRuntime(String var1, ColumnInfo[] var2, WLDFAccessRuntimeMBean var3) throws ManagementException {
      try {
         DataAccessRuntime.DiagnosticDataAccessServiceStruct var4 = DataAccessRuntime.createDiagnosticDataAccessService(var1, var2);
         DiagnosticDataAccessService var8 = var4.getDiagnosticDataAccessService();
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Creating DiagnosticDataAccessRuntime with Name=" + var1);
         }

         DiagnosticDataAccessRuntime var6 = new DiagnosticDataAccessRuntime(var1, var3, var8);
         var6.setDataArchiveParameters(var4.getCreateParams());
         return var6;
      } catch (Exception var7) {
         Loggable var5 = DiagnosticsLogger.logErrorCreatingDiagnosticDataRuntimeLoggable(var1, var7);
         var5.log();
         throw new ManagementException(var5.getMessageBody());
      }
   }

   public DiagnosticDataAccessService createDiagnosticDataAccessService(String var1, String var2, ColumnInfo[] var3, Map var4) throws UnknownLogTypeException, DataAccessServiceCreateException {
      if (!var2.equals("CUSTOM")) {
         var3 = null;
      }

      DiagnosticDataAccessService var5 = DiagnosticDataAccessServiceFactory.createDiagnosticDataAccessService(var1, var2, var3, var4);
      return var5;
   }

   public ArchiveRuntimeMBean createDiagnosticArchiveRuntime(DiagnosticDataAccessService var1) throws ManagementException {
      WLDFRuntimeMBean var2 = runtimeAccess.getServerRuntime().getWLDFRuntime();
      Object var3 = null;
      if (var1 instanceof FileDataArchive) {
         var3 = new WLDFDiagnosticFileArchiveRuntime((FileDataArchive)var1, var2);
      } else if (var1 instanceof JdbcDataArchive) {
         var3 = new WLDFDiagnosticDbstoreArchiveRuntime((JdbcDataArchive)var1, var2);
      } else {
         if (!(var1 instanceof PersistentStoreDataArchive)) {
            throw new ManagementException("Unknown archive type: " + var1);
         }

         var3 = new WLDFDiagnosticWlstoreArchiveRuntime((PersistentStoreDataArchive)var1, var2);
      }

      var2.addWLDFArchiveRuntime((WLDFArchiveRuntimeMBean)var3);
      return (ArchiveRuntimeMBean)var3;
   }

   public void destroyDiagnosticArchiveRuntime(ArchiveRuntimeMBean var1) throws ManagementException {
      if (var1 instanceof WLDFArchiveRuntimeMBean) {
         WLDFRuntimeMBean var2 = runtimeAccess.getServerRuntime().getWLDFRuntime();
         var2.removeWLDFArchiveRuntime((WLDFArchiveRuntimeMBean)var1);
      }

      DiagnosticArchiveRuntime var3 = (DiagnosticArchiveRuntime)var1;
      var3.unregister();
   }

   public DataRetirementTaskRuntimeMBean createRetirementByAgeTask(DiagnosticDataAccessService var1, long var2) throws ManagementException {
      WLDFDataRetirementByAgeTaskImpl var4 = null;
      if (var1 instanceof EditableDataArchive) {
         var4 = new WLDFDataRetirementByAgeTaskImpl((EditableDataArchive)var1, var2);
         return var4;
      } else {
         throw new ManagementException("Can not create retirement task for non-editable archive " + var1.getName());
      }
   }

   static {
      runtimeAccess = ManagementService.getRuntimeAccess(KERNEL_ID);
   }
}
