package weblogic.logging.parsers;

import antlr.LLkParser;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import java.util.Locale;
import weblogic.logging.LogEntry;
import weblogic.logging.SeverityI18N;
import weblogic.logging.WLLevel;
import weblogic.logging.WLLogRecord;

public class ServerLogFileParser extends LLkParser implements ServerLogFileParserTokenTypes {
   public static final String[] _tokenNames = new String[]{"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "BEGIN_LOG_RECORD", "LOGFIELD", "NEWLINE", "ESCAPED_START", "ESCAPED_END", "START_DELIMITER", "END_DELIMITER"};

   protected ServerLogFileParser(TokenBuffer var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public ServerLogFileParser(TokenBuffer var1) {
      this((TokenBuffer)var1, 1);
   }

   protected ServerLogFileParser(TokenStream var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public ServerLogFileParser(TokenStream var1) {
      this((TokenStream)var1, 1);
   }

   public ServerLogFileParser(ParserSharedInputState var1) {
      super(var1, 1);
      this.tokenNames = _tokenNames;
   }

   public final LogEntry getNextServerLogEntry() throws RecognitionException, TokenStreamException {
      WLLogRecord var1 = null;
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
         String var15 = var3.getText();
         String var16 = var4.getText();
         String var17 = var5.getText();
         String var18 = var6.getText();
         String var19 = var7.getText();
         String var20 = var8.getText();
         String var21 = var9.getText();
         String var22 = var10.getText();
         String var23 = var11.getText();
         long var24 = Long.parseLong(var12.getText());
         String var26 = var13.getText();
         String var27 = var14.getText();
         int var28 = SeverityI18N.severityStringToNum(var16, Locale.getDefault());
         WLLogRecord var29 = new WLLogRecord(WLLevel.getLevel(var28), var27);
         var29.setId(var26);
         var29.setLoggerName(var17);
         var29.setMachineName(var18);
         var29.setServerName(var19);
         var29.setThreadName(var20);
         var29.setTransactionId(var22);
         var29.setUserId(var21);
         var29.setDiagnosticContextId(var23);
         var29.setMillis(var24);
         var1 = var29;
      } catch (RecognitionException var30) {
         this.reportError(var30.toString());
         this.consume();

         while(this.LA(1) != 4 && this.LA(1) != 1) {
            this.consume();
         }
      }

      return var1;
   }
}
