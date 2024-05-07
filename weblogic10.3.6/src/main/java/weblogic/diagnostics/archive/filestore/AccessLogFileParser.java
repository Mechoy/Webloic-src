package weblogic.diagnostics.archive.filestore;

import antlr.LLkParser;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import java.util.ArrayList;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.debug.DebugLogger;

public class AccessLogFileParser extends LLkParser implements AccessLogFileParserTokenTypes {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticArchive");
   public static final String[] _tokenNames = new String[]{"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "NEWLINE", "SPECIAL", "WS", "NOT_WS", "LOGFIELD"};

   public void reportError(RecognitionException var1) {
      if (DEBUG.isDebugEnabled()) {
         super.reportError(var1);
      }

   }

   protected AccessLogFileParser(TokenBuffer var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public AccessLogFileParser(TokenBuffer var1) {
      this((TokenBuffer)var1, 1);
   }

   protected AccessLogFileParser(TokenStream var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public AccessLogFileParser(TokenStream var1) {
      this((TokenStream)var1, 1);
   }

   public AccessLogFileParser(ParserSharedInputState var1) {
      super(var1, 1);
      this.tokenNames = _tokenNames;
   }

   public final DataRecord getNextAccessLogEntry() throws RecognitionException, TokenStreamException {
      DataRecord var1 = null;
      Token var2 = null;
      ArrayList var3 = new ArrayList();
      var3.add(new Long(0L));

      while(this.LA(1) == 8) {
         var2 = this.LT(1);
         this.match(8);
         var3.add(var2.getText());
      }

      this.match(4);
      int var4 = var3.size();
      if (var4 > 1) {
         Object[] var5 = new Object[var4];
         var5 = (Object[])var3.toArray(var5);
         var1 = new DataRecord(var5);
      }

      return var1;
   }
}
