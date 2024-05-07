package weblogic.diagnostics.archive.filestore;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;

public class JMSLogRecordParser implements RecordParser {
   public DataRecord parseRecord(byte[] var1, int var2, int var3) {
      DataRecord var4 = null;
      InputStreamReader var5 = null;

      try {
         var5 = new InputStreamReader(new ByteArrayInputStream(var1, var2, var3));
         LogLexer var6 = new LogLexer(var5);
         JMSLogFileParser var7 = new JMSLogFileParser(var6);
         var4 = var7.getNextLogEntry();
      } catch (Exception var16) {
         DiagnosticsLogger.logLogRecordParseError(new String(var1, var2, var3));
      } finally {
         if (var5 != null) {
            try {
               var5.close();
            } catch (Exception var15) {
               UnexpectedExceptionHandler.handle("Could not close stream", var15);
            }
         }

      }

      return var4;
   }

   public long getTimestamp(DataRecord var1) {
      long var2 = 0L;
      if (var1 == null) {
         return 0L;
      } else {
         try {
            var2 = Long.parseLong(var1.get(4).toString());
         } catch (Exception var5) {
         }

         return var2;
      }
   }
}
