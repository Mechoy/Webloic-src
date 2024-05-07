package weblogic.diagnostics.archive.filestore;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;

final class AccessLogRecordParser implements RecordParser {
   private int timestampColumnIndex;
   private String dateFormatString;

   AccessLogRecordParser() {
   }

   AccessLogRecordParser(String var1, int var2) {
      this.timestampColumnIndex = var2;
      this.dateFormatString = var1;
   }

   public DataRecord parseRecord(byte[] var1, int var2, int var3) {
      DataRecord var4 = null;
      InputStreamReader var5 = null;

      try {
         var5 = new InputStreamReader(new ByteArrayInputStream(var1, var2, var3));
         AccessLogLexer var6 = new AccessLogLexer(var5);
         AccessLogFileParser var7 = new AccessLogFileParser(var6);
         var4 = var7.getNextAccessLogEntry();
      } catch (Exception var16) {
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
      if (var1 != null && this.dateFormatString != null) {
         try {
            ParsePosition var4 = new ParsePosition(0);
            SimpleDateFormat var5 = new SimpleDateFormat(this.dateFormatString);
            String var6 = var1.get(this.timestampColumnIndex).toString();
            Date var7 = var5.parse(var6, var4);
            if (var7 != null) {
               var2 = var7.getTime();
            }
         } catch (Throwable var8) {
         }

         return var2;
      } else {
         return 0L;
      }
   }
}
