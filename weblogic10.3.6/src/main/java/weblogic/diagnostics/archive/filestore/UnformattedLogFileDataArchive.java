package weblogic.diagnostics.archive.filestore;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;

public class UnformattedLogFileDataArchive extends FileDataArchive {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticArchive");
   private static final byte[] RECORD_MARKER = "".getBytes();
   private static final RecordParser RECORD_PARSER = new UnformattedLogRecordParser();

   public UnformattedLogFileDataArchive(String var1, File var2, File var3, File var4, boolean var5) throws IOException, ManagementException {
      super(var1, ArchiveConstants.getColumns(5), var2, var3, var4, RECORD_PARSER, RECORD_MARKER, false, var5);
   }

   public UnformattedLogFileDataArchive(String var1, File var2, File var3, boolean var4) throws IOException, ManagementException {
      this(var1, var2, var2.getParentFile(), var3, var4);
   }

   public String getDescription() {
      return "Unformatted Log";
   }

   private static void usage() {
      System.out.println("java [-Dverbose=true] " + UnformattedLogFileDataArchive.class.getName() + " logName logFile indexStoreDirectory lowRecordId highRecordId [queryString]");
      System.out.println("For example:");
      System.out.println("java [-Dverbose=true] " + UnformattedLogFileDataArchive.class.getName() + " WebAppLog c:/mydomain/myserver/webapp.log" + " c:/mydomain/servers/myserver/data/store/diagnostics 0 99999999999999" + " \"LINE LIKE '%Diagnostics%'\"");
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 5) {
         usage();
      } else {
         boolean var1 = Boolean.getBoolean("verbose");
         String var2 = var0[0];
         File var3 = new File(var0[1]);
         File var4 = new File(var0[2]);
         long var5 = Long.parseLong(var0[3]);
         long var7 = Long.parseLong(var0[4]);
         String var9 = var0.length > 5 ? var0[5] : null;
         UnformattedLogFileDataArchive var10 = new UnformattedLogFileDataArchive(var2, var3, var3.getParentFile(), var4, true);
         int var11 = 0;
         long var12 = System.currentTimeMillis();
         Iterator var14 = var10.getDataRecords(var5, var7, Long.MAX_VALUE, var9);

         while(var14.hasNext()) {
            DataRecord var15 = (DataRecord)var14.next();
            ++var11;
            if (var1) {
               System.out.println(var15.toString());
            }
         }

         long var16 = System.currentTimeMillis();
         System.out.println("Found " + var11 + " record(s) in " + (var16 - var12) + " ms");
         var10.close();
      }
   }
}
