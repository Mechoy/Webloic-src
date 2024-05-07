package weblogic.diagnostics.archive.filestore;

import weblogic.diagnostics.accessor.DataRecord;

public class UnformattedLogRecordParser implements RecordParser {
   public DataRecord parseRecord(byte[] var1, int var2, int var3) {
      if (var3 <= 0) {
         return null;
      } else {
         byte var4 = var1[var2 + var3 - 1];
         if (var4 != 13 && var4 != 10) {
            return null;
         } else {
            String var5 = (new String(var1, var2, var3)).trim();
            if (var5.length() == 0) {
               return null;
            } else {
               Object var6 = null;
               Object[] var7 = new Object[]{var6, var5};
               return new DataRecord(var7);
            }
         }
      }
   }

   public long getTimestamp(DataRecord var1) {
      return 0L;
   }
}
