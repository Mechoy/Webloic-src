package weblogic.diagnostics.watch;

import java.util.HashMap;
import java.util.Map;
import weblogic.diagnostics.accessor.ColumnInfo;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.archive.EventDataResolver;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsTextTextFormatter;

public class EventDataVariablesImpl extends EventDataResolver {
   private static final ColumnInfo[] columns = ArchiveConstants.getColumns(1);
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private static final DiagnosticsTextTextFormatter DTF = DiagnosticsTextTextFormatter.getInstance();

   public EventDataVariablesImpl() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Created EventDataVariableImpl " + this);
      }

   }

   Map getWatchData() {
      HashMap var1 = new HashMap();

      for(int var2 = 0; var2 < columns.length; ++var2) {
         String var3 = "";

         try {
            var3 = var3 + this.resolveVariable(var2);
         } catch (Exception var5) {
         }

         var1.put(columns[var2].getColumnName(), var3);
      }

      return var1;
   }
}
