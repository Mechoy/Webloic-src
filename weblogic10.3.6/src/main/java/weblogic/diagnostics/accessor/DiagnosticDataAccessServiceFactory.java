package weblogic.diagnostics.accessor;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.naming.NamingException;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.archive.dbstore.EventsJdbcDataArchive;
import weblogic.diagnostics.archive.dbstore.HarvestedJdbcDataArchive;
import weblogic.diagnostics.archive.filestore.AccessLogFileDataArchive;
import weblogic.diagnostics.archive.filestore.DataSourceLogFileDataArchive;
import weblogic.diagnostics.archive.filestore.JMSLogFileDataArchive;
import weblogic.diagnostics.archive.filestore.ServerLogFileDataArchive;
import weblogic.diagnostics.archive.filestore.UnformattedLogFileDataArchive;
import weblogic.diagnostics.archive.wlstore.EventsPersistentStoreDataArchive;
import weblogic.diagnostics.archive.wlstore.GenericPersistentStoreDataArchive;
import weblogic.diagnostics.archive.wlstore.HarvestedPersistentStoreDataArchive;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.kernel.Kernel;
import weblogic.management.ManagementException;
import weblogic.store.PersistentStoreException;

public final class DiagnosticDataAccessServiceFactory implements AccessorConstants {
   public static DiagnosticDataAccessService createDiagnosticDataAccessService(String var0, String var1, Map var2) throws UnknownLogTypeException, DataAccessServiceCreateException {
      return createDiagnosticDataAccessService(var0, var1, (ColumnInfo[])null, var2);
   }

   public static DiagnosticDataAccessService createDiagnosticDataAccessService(String var0, String var1, ColumnInfo[] var2, Map var3) throws UnknownLogTypeException, DataAccessServiceCreateException {
      try {
         if (var1.equals("ServerLog")) {
            return createServerLogDataAccessService(var0, var3);
         } else if (var1.equals("DomainLog")) {
            return createDomainLogDataAccessService(var0, var3);
         } else if (var1.equals("DataSourceLog")) {
            return createDataSourceLogDataAccessService(var0, var3);
         } else if (var1.equals("HarvestedDataArchive")) {
            return createHarvestedDataAccessService(var0, var3);
         } else if (var1.equals("EventsDataArchive")) {
            return createEventsDataAccessService(var0, var3);
         } else if (var1.equals("HTTPAccessLog")) {
            return createHTTPAccessLogDataAccessService(var0, var3);
         } else if (var1.equals("WebAppLog")) {
            return createWebAppLogDataAccessService(var0, var3);
         } else if (var1.equals("ConnectorLog")) {
            return createConnectorLogDataAccessService(var0, var3);
         } else if (!var1.equals("JMSMessageLog") && !var1.equals("JMSSAFMessageLog")) {
            if (var1.equals("CUSTOM")) {
               return createGenericDataAccessService(var0, var2, var3);
            } else {
               throw new UnknownLogTypeException(var1);
            }
         } else {
            return createJMSMessageLogDataAccessService(var0, var3);
         }
      } catch (Exception var5) {
         throw new DataAccessServiceCreateException(var5);
      }
   }

   private static DiagnosticDataAccessService createServerLogDataAccessService(String var0, Map var1) throws IOException, ManagementException {
      String var2 = (String)var1.get("logFilePath");
      File var3 = new File(var2);
      String var4 = (String)var1.get("storeDir");
      File var5 = var4 != null ? new File(var4) : null;
      File var6 = var3.getParentFile();
      String var7 = (String)var1.get("logRotationDir");
      if (var7 != null && var7.length() > 0) {
         var6 = new File(var7);
      }

      return new ServerLogFileDataArchive(var0, new File(var2), var6, var5, false);
   }

   private static DiagnosticDataAccessService createDomainLogDataAccessService(String var0, Map var1) throws IOException, ManagementException {
      String var2 = (String)var1.get("logFilePath");
      File var3 = new File(var2);
      String var4 = (String)var1.get("storeDir");
      File var5 = var4 != null ? new File(var4) : null;
      File var6 = var3.getParentFile();
      String var7 = (String)var1.get("logRotationDir");
      if (var7 != null && var7.length() > 0) {
         var6 = new File(var7);
      }

      return new ServerLogFileDataArchive(var0, new File(var2), var6, var5, !Kernel.isServer());
   }

   private static DiagnosticDataAccessService createDataSourceLogDataAccessService(String var0, Map var1) throws IOException, ManagementException {
      String var2 = (String)var1.get("logFilePath");
      File var3 = new File(var2);
      String var4 = (String)var1.get("storeDir");
      File var5 = var4 != null ? new File(var4) : null;
      File var6 = var3.getParentFile();
      String var7 = (String)var1.get("logRotationDir");
      if (var7 != null && var7.length() > 0) {
         var6 = new File(var7);
      }

      return new DataSourceLogFileDataArchive(var0, new File(var2), var6, var5, !Kernel.isServer());
   }

   private static DiagnosticDataAccessService createHTTPAccessLogDataAccessService(String var0, Map var1) throws IOException, ManagementException {
      String var2 = (String)var1.get("logFilePath");
      File var3 = new File(var2);
      String var4 = (String)var1.get("storeDir");
      File var5 = var4 != null ? new File(var4) : null;
      File var6 = var3.getParentFile();
      String var7 = (String)var1.get("logRotationDir");
      if (var7 != null && var7.length() > 0) {
         var6 = new File(var7);
      }

      ColumnInfo[] var8 = ArchiveConstants.getColumns(4);
      byte var9 = -1;
      byte var10 = 4;
      boolean var11 = true;
      if (var1.containsKey("elfFields")) {
         boolean var14 = true;
         String var12 = (String)var1.get("elfFields");
         String[] var13 = var12.trim().split("\\s+");
         return new AccessLogFileDataArchive(var0, new File(var2), var6, var5, var13, !Kernel.isServer());
      } else {
         return new AccessLogFileDataArchive(var0, new File(var2), var6, var5, "dd/MMM/yyyy:HH:mm:ss Z", var8, var9, var10, !Kernel.isServer());
      }
   }

   private static DiagnosticDataAccessService createHarvestedDataAccessService(String var0, Map var1) throws PersistentStoreException, NamingException, ManagementException {
      String var2;
      if (var1.containsKey("jndiName")) {
         var2 = (String)var1.get("jndiName");

         try {
            return new HarvestedJdbcDataArchive(var0, var2);
         } catch (Exception var4) {
            DiagnosticsLogger.logErrorInitializingJDBCArchive(var0, var4);
         }
      }

      var2 = (String)var1.get("storeDir");
      return new HarvestedPersistentStoreDataArchive(var0, var2, !Kernel.isServer());
   }

   private static DiagnosticDataAccessService createEventsDataAccessService(String var0, Map var1) throws PersistentStoreException, NamingException, ManagementException {
      String var2;
      if (var1.containsKey("jndiName")) {
         var2 = (String)var1.get("jndiName");

         try {
            return new EventsJdbcDataArchive(var0, var2);
         } catch (Exception var4) {
            DiagnosticsLogger.logErrorInitializingJDBCArchive(var0, var4);
         }
      }

      var2 = (String)var1.get("storeDir");
      return new EventsPersistentStoreDataArchive(var0, var2, !Kernel.isServer());
   }

   private static DiagnosticDataAccessService createGenericDataAccessService(String var0, ColumnInfo[] var1, Map var2) throws PersistentStoreException, ManagementException {
      String var3 = (String)var2.get("storeDir");
      if (var3 == null) {
         throw new PersistentStoreException("Missing diagnostic store directory");
      } else {
         int var4 = var1 != null ? var1.length : 0;
         if (var4 > 0) {
            ColumnInfo var5 = var1[0];
            if (!var5.getColumnName().equals("RECORDID") || var5.getColumnType() != 2) {
               throw new PersistentStoreException("Missing first column RECORDID of type COLTYPE_LONG");
            }

            var5 = var4 > 1 ? var1[1] : null;
            if (var5 == null || !var5.getColumnName().equals("TIMESTAMP") || var5.getColumnType() != 2) {
               throw new PersistentStoreException("Missing second column TIMESTAMP of type COLTYPE_LONG");
            }
         } else {
            var1 = null;
         }

         return new GenericPersistentStoreDataArchive(var0, var1, var3, !Kernel.isServer());
      }
   }

   private static DiagnosticDataAccessService createWebAppLogDataAccessService(String var0, Map var1) throws IOException, ManagementException {
      String var2 = (String)var1.get("logFilePath");
      File var3 = new File(var2);
      String var4 = (String)var1.get("storeDir");
      File var5 = var4 != null ? new File(var4) : null;
      File var6 = var3.getParentFile();
      String var7 = (String)var1.get("logRotationDir");
      if (var7 != null && var7.length() > 0) {
         var6 = new File(var7);
      }

      return new ServerLogFileDataArchive(var0, new File(var2), var6, var5, !Kernel.isServer());
   }

   private static DiagnosticDataAccessService createConnectorLogDataAccessService(String var0, Map var1) throws IOException, ManagementException {
      String var2 = (String)var1.get("logFilePath");
      File var3 = new File(var2);
      String var4 = (String)var1.get("storeDir");
      File var5 = var4 != null ? new File(var4) : null;
      File var6 = var3.getParentFile();
      String var7 = (String)var1.get("logRotationDir");
      if (var7 != null && var7.length() > 0) {
         var6 = new File(var7);
      }

      return new UnformattedLogFileDataArchive(var0, new File(var2), var6, var5, !Kernel.isServer());
   }

   private static DiagnosticDataAccessService createJMSMessageLogDataAccessService(String var0, Map var1) throws IOException, ManagementException {
      String var2 = (String)var1.get("logFilePath");
      File var3 = new File(var2);
      File var4 = var3.getParentFile();
      String var5 = (String)var1.get("logRotationDir");
      if (var5 != null && var5.length() > 0) {
         new File(var5);
      }

      String var6 = (String)var1.get("storeDir");
      File var7 = var6 != null ? new File(var6) : null;
      return new JMSLogFileDataArchive(var0, new File(var2), var7, !Kernel.isServer());
   }
}
