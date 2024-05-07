package weblogic.diagnostics.accessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.stream.XMLStreamException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.query.QueryException;

public final class XMLExporter implements AccessorConstants {
   private static final DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticAccessor");
   private static final String SEP = "/";

   public static void exportDiagnosticData(String var0, String var1, String var2, String var3, String var4, String var5, String var6, long var7, long var9) throws DiagnosticDataAccessException {
      String[] var11 = var0.split("/");
      String var12 = var11[0];
      debugLogger.debug("LogType = " + var12);
      debugLogger.debug("LogName = " + var1);
      HashMap var13 = new HashMap();
      if (var12.equals("ServerLog") || var12.equals("DomainLog") || var12.equals("HTTPAccessLog") || var12.equals("WebAppLog") || var12.equals("ConnectorLog") || var12.equals("JMSMessageLog") || var12.equals("JMSSAFMessageLog")) {
         var13.put("logFilePath", var1);
         var13.put("logRotationDir", var2);
      }

      var13.put("storeDir", var3);
      if (var6 != null && var6.length() > 0) {
         var13.put("elfFields", var6);
      }

      DiagnosticDataAccessService var14 = null;

      try {
         var14 = DiagnosticDataAccessServiceFactory.createDiagnosticDataAccessService(var0, var12, var13);
      } catch (UnknownLogTypeException var32) {
         throw new DiagnosticDataAccessException(var32);
      } catch (DataAccessServiceCreateException var33) {
         throw new DiagnosticDataAccessException(var33);
      }

      try {
         ColumnInfo[] var15 = var14.getColumns();
         Iterator var16 = null;

         try {
            var16 = var14.getDataRecords(var7, var9, var4);
         } catch (QueryException var31) {
            throw new DiagnosticDataAccessException(var31);
         }

         FileOutputStream var17 = null;

         try {
            File var18 = new File(var5);
            var17 = new FileOutputStream(var18.getCanonicalFile());
         } catch (IOException var30) {
            throw new DiagnosticDataAccessException(var30);
         }

         try {
            XMLDataWriter var36 = new XMLDataWriter(var17);
            var36.exportDiagnosticDataToXML(var15, var16);
         } catch (XMLStreamException var29) {
            throw new DiagnosticDataAccessException(var29);
         }
      } finally {
         try {
            if (var14 != null) {
               var14.close();
            }
         } catch (Exception var34) {
            if (debugLogger.isDebugEnabled()) {
               var34.printStackTrace();
            }
         }

      }

   }
}
