package weblogic.diagnostics.archive.wlstore;

import java.util.Iterator;
import weblogic.diagnostics.accessor.DiagnosticDataAccessException;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.archive.DiagnosticStoreRepository;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.query.QueryException;
import weblogic.management.ManagementException;
import weblogic.store.PersistentStoreException;

public class EventsPersistentStoreDataArchive extends PersistentStoreDataArchive {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticArchive");

   public EventsPersistentStoreDataArchive(String var1, String var2, boolean var3) throws PersistentStoreException, ManagementException {
      super(var1, ArchiveConstants.getColumns(1), "WLS_EVENTS", var2, var3);
   }

   public String getDescription() {
      return "Diagnostic Events";
   }

   public Iterator getDataRecords(String var1) throws QueryException, DiagnosticDataAccessException {
      return this.getDataRecords(0L, Long.MAX_VALUE, var1);
   }

   public Iterator getDataRecords(long var1, long var3, String var5) throws QueryException, DiagnosticDataAccessException, UnsupportedOperationException {
      return new EventsPersistentRecordIterator(this, var1, var3, var5);
   }

   public Iterator getDataRecords(long var1, long var3, long var5, String var7) throws QueryException, DiagnosticDataAccessException, UnsupportedOperationException {
      return new EventsPersistentRecordIterator(this, var1, var3, var5, var7);
   }

   private static void usage() {
      System.out.println("java [-Dverbose=true] " + EventsPersistentStoreDataArchive.class.getName() + "storeDirectory lowTimestamp highTimestamp [queryString]");
      System.out.println("For example:");
      System.out.println("java [-Dverbose=true] " + EventsPersistentStoreDataArchive.class.getName() + " c:/mydomain/servers/myserver/data/store/diagnostics 0 99999999999999" + " \"MONITOR LIKE '%Before%'\"");
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 3) {
         usage();
      } else {
         String var1 = var0[0];
         long var2 = Long.parseLong(var0[1]);
         long var4 = Long.parseLong(var0[2]);
         String var6 = var0.length > 3 ? var0[3] : null;
         if (!DiagnosticStoreRepository.storeFileExists(var1)) {
            System.out.println("Specified store directory " + var1 + " does not exist or does not contain the diagnostic store file.");
         } else {
            boolean var7 = Boolean.getBoolean("verbose");
            boolean var8 = Boolean.getBoolean("doDelete");
            boolean var9 = Boolean.getBoolean("byID");
            String var10 = System.getProperty("Name");
            if (var10 == null) {
               var10 = "EventsDataArchive";
            }

            EventsPersistentStoreDataArchive var11 = new EventsPersistentStoreDataArchive(var10, var1, !var8);
            int var12 = 0;
            long var13 = System.currentTimeMillis();
            if (var8) {
               var12 = var11.deleteDataRecords(var2, var4, var6);
            } else {
               Iterator var15;
               if (var9) {
                  var15 = var11.getDataRecords(var2, var4, Long.MAX_VALUE, var6);
               } else {
                  var15 = var11.getDataRecords(var2, var4, var6);
               }

               for(; var15.hasNext(); ++var12) {
                  Object var16 = var15.next();
                  if (var7) {
                     System.out.println("RECORD>>>> " + var16);
                  }
               }
            }

            long var17 = System.currentTimeMillis();
            if (var8) {
               System.out.println("Deleted " + var12 + " matching records in " + (var17 - var13) + " ms");
            } else {
               System.out.println("Found " + var12 + " matches in " + (var17 - var13) + " ms");
            }

            var11.close();
         }
      }
   }
}
