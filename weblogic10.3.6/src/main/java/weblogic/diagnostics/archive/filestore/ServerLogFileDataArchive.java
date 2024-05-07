package weblogic.diagnostics.archive.filestore;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;

public class ServerLogFileDataArchive extends FileDataArchive {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticArchive");
   private static final byte[] RECORD_MARKER = "####".getBytes();
   private static final RecordParser RECORD_PARSER = new ServerLogRecordParser();

   public ServerLogFileDataArchive(String var1, File var2, File var3, File var4, boolean var5) throws IOException, ManagementException {
      super(var1, ArchiveConstants.getColumns(3), var2, var3, var4, RECORD_PARSER, RECORD_MARKER, true, var5);
   }

   public ServerLogFileDataArchive(String var1, File var2, File var3, boolean var4) throws IOException, ManagementException {
      this(var1, var2, var2.getParentFile(), var3, var4);
   }

   public String getDescription() {
      return "Server Log";
   }

   private static void usage() {
      System.out.println("java [-Dverbose=true] " + ServerLogFileDataArchive.class.getName() + " logFile indexStoreDirectory lowTimestamp highTimestamp [queryString]");
      System.out.println("For example:");
      System.out.println("java [-Dverbose=true] " + ServerLogFileDataArchive.class.getName() + " c:/mydomain/myserver/myserver.log" + " c:/mydomain/servers/myserver/data/store/diagnostics 0 99999999999999" + " \"SUBSYSTEM LIKE '%Diagnostics%'\"");
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 4) {
         usage();
      } else {
         boolean var1 = Boolean.getBoolean("verbose");
         boolean var2 = Boolean.getBoolean("buildIndex");
         boolean var3 = Boolean.getBoolean("byID");
         boolean var4 = Boolean.getBoolean("printIndex");
         System.setProperty("_Offline_FileDataArchive", "true");
         File var5 = new File(var0[0]);
         File var6 = new File(var0[1]);
         ServerLogFileDataArchive var7 = new ServerLogFileDataArchive("ServerLog", var5, var6, !var2);
         long var8 = Long.parseLong(var0[2]);
         long var10 = Long.parseLong(var0[3]);
         String var12 = var0.length > 4 ? var0[4] : null;
         int var13 = 0;
         long var14 = System.currentTimeMillis();
         Iterator var16;
         if (var3) {
            var16 = var7.getDataRecords(var8, var10, Long.MAX_VALUE, var12);
         } else {
            var16 = var7.getDataRecords(var8, var10, var12);
         }

         while(var16.hasNext()) {
            DataRecord var17 = (DataRecord)var16.next();
            ++var13;
            if (var1) {
               System.out.println(var17.toString());
            }
         }

         long var19 = System.currentTimeMillis();
         System.out.println("Found " + var13 + " record(s) in " + (var19 - var14) + " ms");
         if (var4) {
            var7.printIndex();
         }

         var7.close();
      }
   }
}
