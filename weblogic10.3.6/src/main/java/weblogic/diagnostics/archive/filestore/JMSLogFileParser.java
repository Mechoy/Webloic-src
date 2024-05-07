package weblogic.diagnostics.archive.filestore;

import antlr.LLkParser;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.debug.DebugLogger;

public class JMSLogFileParser extends LLkParser implements JMSLogFileParserTokenTypes {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticArchive");
   public static final String[] _tokenNames = new String[]{"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "BEGIN_LOG_RECORD", "LOGFIELD", "NEWLINE", "ESCAPED_START", "ESCAPED_END", "START_DELIMITER", "END_DELIMITER"};
   public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

   public void reportError(RecognitionException var1) {
      if (DEBUG.isDebugEnabled()) {
         super.reportError(var1);
      }

   }

   protected JMSLogFileParser(TokenBuffer var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public JMSLogFileParser(TokenBuffer var1) {
      this((TokenBuffer)var1, 1);
   }

   protected JMSLogFileParser(TokenStream var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public JMSLogFileParser(TokenStream var1) {
      this((TokenStream)var1, 1);
   }

   public JMSLogFileParser(ParserSharedInputState var1) {
      super(var1, 1);
      this.tokenNames = _tokenNames;
   }

   public final DataRecord getNextLogEntry() throws RecognitionException, TokenStreamException {
      DataRecord var1 = null;
      Token var2 = null;
      Token var3 = null;
      Token var4 = null;
      Token var5 = null;
      Token var6 = null;
      Token var7 = null;
      Token var8 = null;
      Token var9 = null;
      Token var10 = null;
      Token var11 = null;
      Token var12 = null;
      Token var13 = null;
      Token var14 = null;
      Token var15 = null;

      try {
         var2 = this.LT(1);
         this.match(4);
         var3 = this.LT(1);
         this.match(5);
         var4 = this.LT(1);
         this.match(5);
         var5 = this.LT(1);
         this.match(5);
         var6 = this.LT(1);
         this.match(5);
         var7 = this.LT(1);
         this.match(5);
         var8 = this.LT(1);
         this.match(5);
         var9 = this.LT(1);
         this.match(5);
         var10 = this.LT(1);
         this.match(5);
         var11 = this.LT(1);
         this.match(5);
         var12 = this.LT(1);
         this.match(5);
         var13 = this.LT(1);
         this.match(5);
         var14 = this.LT(1);
         this.match(5);
         var15 = this.LT(1);
         this.match(5);
         Object[] var16 = new Object[ArchiveConstants.JMSLOG_ARCHIVE_COLUMNS_COUNT];
         int var17 = 1;
         var16[var17++] = var3.getText();
         var16[var17++] = var4.getText();
         var16[var17++] = var5.getText();
         var16[var17++] = var6.getText();
         var16[var17++] = var7.getText();
         var16[var17++] = var8.getText();
         var16[var17++] = var9.getText();
         var16[var17++] = var10.getText();
         var16[var17++] = var11.getText();
         var16[var17++] = var12.getText();
         var16[var17++] = var13.getText();
         var16[var17++] = var14.getText();
         var16[var17++] = var15.getText();
         var1 = new DataRecord(var16);
      } catch (RecognitionException var18) {
         this.reportError(var18);
         this.recover(var18, _tokenSet_0);
      }

      return var1;
   }

   private static final long[] mk_tokenSet_0() {
      long[] var0 = new long[]{2L, 0L};
      return var0;
   }
}
