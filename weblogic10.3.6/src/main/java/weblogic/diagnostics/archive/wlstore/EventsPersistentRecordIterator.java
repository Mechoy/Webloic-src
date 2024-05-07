package weblogic.diagnostics.archive.wlstore;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.query.QueryException;

public final class EventsPersistentRecordIterator extends PersistentRecordIterator {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticArchive");

   EventsPersistentRecordIterator(EventsPersistentStoreDataArchive var1, long var2, long var4, String var6) throws QueryException {
      super(var1, var2, var4, var6);
   }

   EventsPersistentRecordIterator(EventsPersistentStoreDataArchive var1, long var2, long var4, long var6, String var8) throws QueryException {
      super(var1, var2, var4, var6, var8);
   }
}
