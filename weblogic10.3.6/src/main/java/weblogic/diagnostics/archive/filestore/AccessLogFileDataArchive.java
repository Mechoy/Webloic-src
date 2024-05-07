package weblogic.diagnostics.archive.filestore;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import weblogic.diagnostics.accessor.ColumnInfo;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;

public final class AccessLogFileDataArchive extends FileDataArchive {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticArchive");
   private int timestampColumnIndex;
   private static final byte[] RECORD_MARKER = "".getBytes();

   public AccessLogFileDataArchive(String var1, File var2, File var3, File var4, String var5, ColumnInfo[] var6, int var7, int var8, boolean var9) throws IOException, ManagementException {
      super(var1, var6, var2, var3, var4, new AccessLogRecordParser(var5, var8), RECORD_MARKER, true, var9);
      this.timestampColumnIndex = 4;
      this.timestampColumnIndex = var8;
   }

   public AccessLogFileDataArchive(String var1, File var2, File var3, File var4, String var5, ColumnInfo[] var6, int var7, boolean var8) throws IOException, ManagementException {
      this(var1, var2, var3, var4, var5, var6, -1, var7, var8);
   }

   public AccessLogFileDataArchive(String var1, File var2, File var3, File var4, String var5, boolean var6) throws IOException, ManagementException {
      this(var1, var2, var3, var4, var5, ArchiveConstants.getColumns(4), 4, var6);
   }

   public AccessLogFileDataArchive(String var1, File var2, File var3, File var4, boolean var5) throws IOException, ManagementException {
      this(var1, var2, var3, var4, "dd/MMM/yyyy:HH:mm:ss Z", var5);
   }

   public AccessLogFileDataArchive(String var1, File var2, File var3, File var4, String[] var5, boolean var6) throws IOException, ManagementException {
      super(var1, getCols(var5), var2, var3, var4, new AccessLogRecordParser(), RECORD_MARKER, false, var6);
      this.timestampColumnIndex = 4;
   }

   private static ColumnInfo[] getCols(String[] var0) {
      int var1 = (var0 != null ? var0.length : 0) + 1;
      ColumnInfo[] var2 = new ColumnInfo[var1];
      var2[0] = new ColumnInfo("RECORDID", 2);

      for(int var3 = 1; var3 < var1; ++var3) {
         var2[var3] = new ColumnInfo(var0[var3 - 1], 5);
      }

      return var2;
   }

   public String getDescription() {
      return "HTTP Access Log";
   }

   private static void usage() {
      System.out.println("java [-Dverbose=true] " + AccessLogFileDataArchive.class.getName() + " logFile indexStoreDirectory lowTimestamp highTimestamp dateDormat [queryString]");
      System.out.println("For example:");
      System.out.println("java [-Dverbose=true] " + AccessLogFileDataArchive.class.getName() + " c:/mydomain/myserver/access.log" + " c:/mydomain/servers/myserver/data/store/diagnostics 0 99999999999999" + " \"dd/MMM/yyyy:HH:mm:ss Z\"" + " \"STATUS = 200\"");
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 5) {
         usage();
      } else {
         File var1 = new File(var0[0]);
         File var2 = new File(var0[1]);
         long var3 = Long.parseLong(var0[2]);
         long var5 = Long.parseLong(var0[3]);
         String var7 = var0[4];
         String var8 = var0.length > 5 ? var0[5] : null;
         boolean var9 = Boolean.getBoolean("verbose");
         AccessLogFileDataArchive var10 = new AccessLogFileDataArchive("HTTPAccessLog", var1, var1.getParentFile(), var2, var7, true);
         int var11 = 0;
         long var12 = System.currentTimeMillis();
         Iterator var14 = var10.getDataRecords(var3, var5, var8);

         while(var14.hasNext()) {
            DataRecord var15 = (DataRecord)var14.next();
            ++var11;
            if (var9) {
               System.out.println(var15.toString());
            }
         }

         long var16 = System.currentTimeMillis();
         System.out.println("Found " + var11 + " record(s) in " + (var16 - var12) + " ms");
         var10.close();
      }
   }
}
