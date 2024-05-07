package weblogic.diagnostics.archive.wlstore;

import java.util.Iterator;
import weblogic.diagnostics.accessor.DiagnosticDataAccessException;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.archive.DiagnosticStoreRepository;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.query.QueryException;
import weblogic.management.ManagementException;
import weblogic.store.PersistentStoreException;

public final class HarvestedPersistentStoreDataArchive extends PersistentStoreDataArchive {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticArchive");

   public HarvestedPersistentStoreDataArchive(String var1, String var2, boolean var3) throws PersistentStoreException, ManagementException {
      super(var1, ArchiveConstants.getColumns(2), "WLS_HVST", var2, var3);
   }

   public String getDescription() {
      return "Harvested diagnostic data";
   }

   public Iterator getDataRecords(String var1) throws QueryException, DiagnosticDataAccessException {
      return this.getDataRecords(0L, Long.MAX_VALUE, var1);
   }

   public Iterator getDataRecords(long var1, long var3, String var5) throws QueryException, DiagnosticDataAccessException, UnsupportedOperationException {
      return new HarvesterPersistentRecordIterator(this, var1, var3, var5);
   }

   public Iterator getDataRecords(long var1, long var3, long var5, String var7) throws QueryException, DiagnosticDataAccessException, UnsupportedOperationException {
      return new HarvesterPersistentRecordIterator(this, var1, var3, var5, var7);
   }

   private static void usage() {
      System.out.println("java [-Dverbose=true] " + HarvestedPersistentStoreDataArchive.class.getName() + "storeDirectory lowTimestamp highTimestamp [queryString]");
      System.out.println("For example:");
      System.out.println("java [-Dverbose=true] " + HarvestedPersistentStoreDataArchive.class.getName() + " c:/mydomain/servers/myserver/data/store/diagnostics 0 99999999999999" + " \"TYPE LIKE '%JDBC%'\"");
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
            HarvestedPersistentStoreDataArchive var10 = new HarvestedPersistentStoreDataArchive("HarvestedDataArchive", var1, !var8);
            int var11 = 0;
            long var12 = System.currentTimeMillis();
            if (var8) {
               var11 = var10.deleteDataRecords(var2, var4, var6);
            } else {
               Iterator var14;
               if (var9) {
                  var14 = var10.getDataRecords(var2, var4, Long.MAX_VALUE, var6);
               } else {
                  var14 = var10.getDataRecords(var2, var4, var6);
               }

               for(; var14.hasNext(); ++var11) {
                  Object var15 = var14.next();
                  if (var7) {
                     System.out.println("RECORD>>>> " + var15);
                  }
               }
            }

            long var16 = System.currentTimeMillis();
            if (var8) {
               System.out.println("Deleted " + var11 + " matching records in " + (var16 - var12) + " ms");
            } else {
               System.out.println("Found " + var11 + " matches in " + (var16 - var12) + " ms");
            }

            var10.close();
         }
      }
   }
}
