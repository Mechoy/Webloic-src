package weblogic.diagnostics.archive.dbstore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import javax.naming.NamingException;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.archive.DataWriter;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;

public class HarvestedJdbcDataArchive extends JdbcDataArchive implements DataWriter {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticArchive");

   public HarvestedJdbcDataArchive(String var1, String var2) throws NamingException, ManagementException, SQLException {
      this(var1, var2, (String)null, (String)null, (String)null);
   }

   public HarvestedJdbcDataArchive(String var1, String var2, String var3, String var4, String var5) throws NamingException, ManagementException, SQLException {
      super(var1, var2, "WLS_HVST", ArchiveConstants.getColumns(2), var3, var4, var5);
   }

   public String getDescription() {
      return "Harvested diagnostic data";
   }

   protected DataRecord getDataRecord(ResultSet var1) throws SQLException {
      int var2 = ArchiveConstants.HARVESTER_ARCHIVE_COLUMNS_COUNT;
      Object[] var3 = new Object[var2];
      int var4 = 0;
      var3[var4] = new Long(var1.getLong(var4 + 1));
      ++var4;
      var3[var4] = new Long(var1.getLong(var4 + 1));
      ++var4;
      var3[var4] = var1.getString(var4 + 1);
      ++var4;
      var3[var4] = var1.getString(var4 + 1);
      ++var4;
      var3[var4] = var1.getString(var4 + 1);
      ++var4;
      var3[var4] = var1.getString(var4 + 1);
      ++var4;
      var3[var4] = var1.getString(var4 + 1);
      ++var4;
      var3[var4] = new Integer(var1.getString(var4 + 1));
      ++var4;
      var3[var4] = var1.getString(var4 + 1);
      ++var4;
      return new DataRecord(var3);
   }

   protected void insertDataRecord(PreparedStatement var1, Object var2) throws SQLException {
      DataRecord var3 = (DataRecord)var2;
      int var4 = 1;
      var1.setLong(var4, (Long)var3.get(var4));
      ++var4;
      var1.setString(var4, var3.get(var4).toString());
      ++var4;
      var1.setString(var4, var3.get(var4).toString());
      ++var4;
      var1.setString(var4, var3.get(var4).toString());
      ++var4;
      var1.setString(var4, var3.get(var4).toString());
      ++var4;
      var1.setString(var4, var3.get(var4).toString());
      ++var4;
      var1.setInt(var4, (Integer)var3.get(var4));
      ++var4;
      var1.setString(var4, var3.get(var4).toString());
      ++var4;
      var1.executeUpdate();
   }

   public static void main(String[] var0) throws Exception {
      HarvestedJdbcDataArchive var1 = new HarvestedJdbcDataArchive("HarvestedDataArchive", var0[0]);
      String var2 = var0.length > 1 ? var0[1] : null;
      int var3 = 0;
      long var4 = System.currentTimeMillis();
      Iterator var6 = var1.getDataRecords(var2);

      while(var6.hasNext()) {
         DataRecord var7 = (DataRecord)var6.next();
         ++var3;
         System.out.println(var7.toString());
      }

      long var8 = System.currentTimeMillis();
      System.out.println("Found " + var3 + " record(s) in " + (var8 - var4) + " ms");
   }
}
