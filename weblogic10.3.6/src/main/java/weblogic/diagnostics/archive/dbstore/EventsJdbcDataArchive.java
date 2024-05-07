package weblogic.diagnostics.archive.dbstore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import javax.naming.NamingException;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.archive.DataWriter;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;
import weblogic.management.ManagementException;

public class EventsJdbcDataArchive extends JdbcDataArchive implements DataWriter {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticArchive");

   public EventsJdbcDataArchive(String var1, String var2) throws NamingException, ManagementException, SQLException {
      this(var1, var2, (String)null, (String)null, (String)null);
   }

   public EventsJdbcDataArchive(String var1, String var2, String var3, String var4, String var5) throws NamingException, ManagementException, SQLException {
      super(var1, var2, "WLS_EVENTS", ArchiveConstants.getColumns(1), var3, var4, var5);
   }

   public String getDescription() {
      return "Diagnostic Events";
   }

   protected DataRecord getDataRecord(ResultSet var1) throws SQLException {
      int var2 = ArchiveConstants.EVENTS_ARCHIVE_COLUMNS_COUNT;
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
      var3[var4] = new Integer(var1.getInt(var4 + 1));
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
      var3[var4] = this.getPayloadObject(var1.getBytes(var4 + 1));
      ++var4;
      var3[var4] = var1.getString(var4 + 1);
      ++var4;
      var3[var4] = new Long(var1.getLong(var4 + 1));
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
      var1.setString(var4, var3.get(var4).toString());
      ++var4;
      var1.setString(var4, var3.get(var4).toString());
      ++var4;
      var1.setString(var4, var3.get(var4).toString());
      ++var4;
      var1.setString(var4, var3.get(var4).toString());
      ++var4;
      var1.setBytes(var4, this.getPayloadBytes(var3.get(var4)));
      ++var4;
      var1.setString(var4, var3.get(var4).toString());
      ++var4;
      var1.setLong(var4, (Long)var3.get(var4));
      ++var4;
      var1.setString(var4, var3.get(var4).toString());
      ++var4;
      var1.executeUpdate();
   }

   private byte[] getPayloadBytes(Object var1) {
      byte[] var2 = new byte[0];
      ByteArrayOutputStream var3 = null;
      ObjectOutputStream var4 = null;

      try {
         var3 = new ByteArrayOutputStream();
         var4 = new ObjectOutputStream(var3);
         var4.writeObject(var1);
         var2 = var3.toByteArray();
         var4.close();
      } catch (Exception var14) {
         UnexpectedExceptionHandler.handle("Could not persist payload", var14);
      } finally {
         if (var4 != null) {
            try {
               var4.close();
            } catch (Exception var13) {
            }
         }

      }

      return var2;
   }

   private Object getPayloadObject(byte[] var1) {
      ByteArrayInputStream var2 = null;
      ObjectInputStream var3 = null;
      Object var4 = null;

      try {
         if (var1 != null && var1.length > 0) {
            var2 = new ByteArrayInputStream(var1);
            var3 = new ObjectInputStream(var2);
            var4 = var3.readObject();
         }
      } catch (Exception var14) {
         UnexpectedExceptionHandler.handle("Could not reconstruct payload", var14);
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (Exception var13) {
            }
         }

      }

      return var4;
   }

   public static void main(String[] var0) throws Exception {
      EventsJdbcDataArchive var1 = new EventsJdbcDataArchive("EventsDataArchive", var0[0]);
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
