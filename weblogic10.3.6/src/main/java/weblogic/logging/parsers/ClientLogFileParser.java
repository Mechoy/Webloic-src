package weblogic.logging.parsers;

import antlr.LLkParser;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import java.util.Locale;
import weblogic.logging.LogEntry;
import weblogic.logging.SeverityI18N;
import weblogic.logging.WLLevel;
import weblogic.logging.WLLogRecord;

public class ClientLogFileParser extends LLkParser implements ClientLogFileParserTokenTypes {
   public static final String[] _tokenNames = new String[]{"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "BEGIN_LOG_RECORD", "LOGFIELD", "NEWLINE", "ESCAPED_START", "ESCAPED_END", "START_DELIMITER", "END_DELIMITER"};

   protected ClientLogFileParser(TokenBuffer var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public ClientLogFileParser(TokenBuffer var1) {
      this((TokenBuffer)var1, 1);
   }

   protected ClientLogFileParser(TokenStream var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public ClientLogFileParser(TokenStream var1) {
      this((TokenStream)var1, 1);
   }

   public ClientLogFileParser(ParserSharedInputState var1) {
      super(var1, 1);
      this.tokenNames = _tokenNames;
   }

   public final LogEntry getNextClientLogEntry() throws RecognitionException, TokenStreamException {
      WLLogRecord var1 = null;
      Token var2 = null;
      Token var3 = null;
      Token var4 = null;
      Token var5 = null;
      Token var6 = null;

      try {
         var2 = this.LT(1);
         this.match(5);
         if (var2.getColumn() != 1) {
            throw new SemanticException("a.getColumn() == 1");
         }

         var3 = this.LT(1);
         this.match(5);
         var4 = this.LT(1);
         this.match(5);
         var5 = this.LT(1);
         this.match(5);
         var6 = this.LT(1);
         this.match(5);
         String var7 = var2.getText();
         String var8 = var3.getText();
         String var9 = var4.getText();
         String var10 = var5.getText();
         String var11 = var6.getText();
         int var12 = SeverityI18N.severityStringToNum(var8, Locale.getDefault());
         WLLogRecord var13 = new WLLogRecord(WLLevel.getLevel(var12), var11);
         var13.setId(var10);
         var13.setLoggerName(var9);
         var1 = var13;
      } catch (SemanticException var14) {
         this.consumeUntil(5);
      } catch (RecognitionException var15) {
         this.reportError(var15.toString());
         this.consumeUntil(5);
      }

      return var1;
   }
}
