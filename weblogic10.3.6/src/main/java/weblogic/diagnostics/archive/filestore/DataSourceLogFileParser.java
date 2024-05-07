package weblogic.diagnostics.archive.filestore;

import antlr.LLkParser;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;
import java.util.ArrayList;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.debug.DebugLogger;

public class DataSourceLogFileParser extends LLkParser implements DataSourceLogFileParserTokenTypes {
   private static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugDiagnosticArchive");
   private static final boolean DEBUG = false;
   private static final int SUPP_ATTR_INDEX = 11;
   public static final String[] _tokenNames = new String[]{"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "BEGIN_LOG_RECORD", "LOGFIELD", "NEWLINE", "ESCAPED_START", "ESCAPED_END", "START_DELIMITER", "END_DELIMITER"};
   public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

   public void reportError(RecognitionException var1) {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         super.reportError(var1);
      }

   }

   protected DataSourceLogFileParser(TokenBuffer var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public DataSourceLogFileParser(TokenBuffer var1) {
      this((TokenBuffer)var1, 1);
   }

   protected DataSourceLogFileParser(TokenStream var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public DataSourceLogFileParser(TokenStream var1) {
      this((TokenStream)var1, 1);
   }

   public DataSourceLogFileParser(ParserSharedInputState var1) {
      super(var1, 1);
      this.tokenNames = _tokenNames;
   }

   public final DataRecord getNextLogEntry() throws RecognitionException, TokenStreamException {
      DataRecord var1 = null;
      Token var2 = null;
      Token var3 = null;
      ArrayList var4 = new ArrayList();

      try {
         var2 = this.LT(1);
         this.match(4);

         while(this.LA(1) == 5) {
            var3 = this.LT(1);
            this.match(5);
            var4.add(var3.getText());
         }

         Object[] var5 = new Object[ArchiveConstants.DATASOURCELOG_ARCHIVE_COLUMNS_COUNT];
         boolean var6 = false;

         for(int var8 = 1; var8 < ArchiveConstants.DATASOURCELOG_ARCHIVE_COLUMNS_COUNT; ++var8) {
            var5[var8] = var4.get(var8 - 1);
         }

         var1 = new DataRecord(var5);
      } catch (RecognitionException var7) {
         this.reportError(var7);
         this.recover(var7, _tokenSet_0);
      }

      return var1;
   }

   private static final long[] mk_tokenSet_0() {
      long[] var0 = new long[]{2L, 0L};
      return var0;
   }
}
