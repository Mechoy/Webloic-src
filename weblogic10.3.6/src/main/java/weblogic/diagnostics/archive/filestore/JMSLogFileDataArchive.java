package weblogic.diagnostics.archive.filestore;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;

public class JMSLogFileDataArchive extends FileDataArchive {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticArchive");
   private static final byte[] RECORD_MARKER = "####".getBytes();
   private static final RecordParser RECORD_PARSER = new JMSLogRecordParser();

   public JMSLogFileDataArchive(String var1, File var2, File var3, File var4, boolean var5) throws IOException, ManagementException {
      super(var1, ArchiveConstants.getColumns(6), var2, var3, var4, RECORD_PARSER, RECORD_MARKER, true, var5);
   }

   public JMSLogFileDataArchive(String var1, File var2, File var3, boolean var4) throws IOException, ManagementException {
      this(var1, var2, var2.getParentFile(), var3, var4);
   }

   public String getDescription() {
      return "JMS Log";
   }

   private static void usage() {
      System.out.println("java [-Dverbose=true] " + JMSLogFileDataArchive.class.getName() + " logFile indexStoreDirectory lowTimestamp highTimestamp [queryString]");
      System.out.println("For example:");
      System.out.println("java [-Dverbose=true] " + JMSLogFileDataArchive.class.getName() + " c:/mydomain/myserver/jms.log" + " c:/mydomain/servers/myserver/data/store/diagnostics 0 99999999999999" + " \"DESTINATION = 'myDestonation'\"");
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 4) {
         usage();
      } else {
         File var1 = new File(var0[0]);
         File var2 = new File(var0[1]);
         JMSLogFileDataArchive var3 = new JMSLogFileDataArchive("JMSMessageLog", var1, var2, true);
         long var4 = Long.parseLong(var0[2]);
         long var6 = Long.parseLong(var0[3]);
         String var8 = var0.length > 4 ? var0[4] : null;
         boolean var9 = Boolean.getBoolean("verbose");
         int var10 = 0;
         long var11 = System.currentTimeMillis();
         Iterator var13 = var3.getDataRecords(var4, var6, var8);

         while(var13.hasNext()) {
            DataRecord var14 = (DataRecord)var13.next();
            ++var10;
            if (var9) {
               System.out.println(var14.toString());
            }
         }

         long var15 = System.currentTimeMillis();
         System.out.println("Found " + var10 + " record(s) in " + (var15 - var11) + " ms");
         var3.close();
      }
   }
}
