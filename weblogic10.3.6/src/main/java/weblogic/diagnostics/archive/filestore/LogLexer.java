package weblogic.diagnostics.archive.filestore;

import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.CharScanner;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.InputBuffer;
import antlr.LexerSharedInputState;
import antlr.NoViableAltForCharException;
import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;

public class LogLexer extends CharScanner implements LogLexerTokenTypes, TokenStream {
   private static final boolean DEBUG = false;
   private boolean hitEOF;

   public void uponEOF() throws TokenStreamException, CharStreamException {
      this.hitEOF = true;
      super.uponEOF();
   }

   public boolean isEOFReached() {
      return this.hitEOF;
   }

   public LogLexer(InputStream var1) {
      this((InputBuffer)(new ByteBuffer(var1)));
   }

   public LogLexer(Reader var1) {
      this((InputBuffer)(new CharBuffer(var1)));
   }

   public LogLexer(InputBuffer var1) {
      this(new LexerSharedInputState(var1));
   }

   public LogLexer(LexerSharedInputState var1) {
      super(var1);
      this.hitEOF = false;
      this.caseSensitiveLiterals = true;
      this.setCaseSensitive(true);
      this.literals = new Hashtable();
   }

   public Token nextToken() throws TokenStreamException {
      Token var1 = null;

      while(true) {
         Object var2 = null;
         boolean var3 = false;
         this.setCommitToPath(false);
         this.resetText();

         try {
            try {
               if (this.LA(1) == '#' && this.getColumn() == 1) {
                  this.mBEGIN_LOG_RECORD(true);
                  var1 = this._returnToken;
               } else if (this.LA(1) == '<') {
                  this.mLOGFIELD(true);
                  var1 = this._returnToken;
               } else {
                  if (this.LA(1) != '\uffff') {
                     this.consume();
                     continue;
                  }

                  this.uponEOF();
                  this._returnToken = this.makeToken(1);
               }

               if (this._returnToken != null) {
                  int var7 = this._returnToken.getType();
                  var7 = this.testLiteralsTable(var7);
                  this._returnToken.setType(var7);
                  return this._returnToken;
               }
            } catch (RecognitionException var5) {
               if (this.getCommitToPath()) {
                  throw new TokenStreamRecognitionException(var5);
               }

               this.consume();
            }
         } catch (CharStreamException var6) {
            if (var6 instanceof CharStreamIOException) {
               throw new TokenStreamIOException(((CharStreamIOException)var6).io);
            }

            throw new TokenStreamException(var6.getMessage());
         }
      }
   }

   public final void mBEGIN_LOG_RECORD(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 4;
      if (this.getColumn() != 1) {
         throw new SemanticException("getColumn()==1");
      } else {
         this.match("####");
         if (var1 && var3 == null && var2 != -1) {
            var3 = this.makeToken(var2);
            var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
         }

         this._returnToken = var3;
      }
   }

   public final void mLOGFIELD(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 5;
      int var5 = this.text.length();
      this.mSTART_DELIMITER(false);
      this.text.setLength(var5);

      while(this.LA(1) != '>' || this.LA(2) != ' ') {
         if ((this.LA(1) == '\n' || this.LA(1) == '\r') && this.LA(2) >= 0 && this.LA(2) <= '\ufffe' && this.LA(3) >= 0 && this.LA(3) <= '\ufffe') {
            this.mNEWLINE(false);
         } else if (this.LA(1) == '&' && this.LA(2) == 'l' && this.LA(3) == 't') {
            this.mESCAPED_START(false);
         } else if (this.LA(1) == '&' && this.LA(2) == 'g' && this.LA(3) == 't') {
            this.mESCAPED_END(false);
         } else {
            if (this.LA(1) < 0 || this.LA(1) > '\ufffe' || this.LA(2) < 0 || this.LA(2) > '\ufffe' || this.LA(3) < 0 || this.LA(3) > '\ufffe') {
               break;
            }

            this.matchNot('\uffff');
         }
      }

      var5 = this.text.length();
      this.mEND_DELIMITER(false);
      this.text.setLength(var5);
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mSTART_DELIMITER(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 9;
      this.match("<");
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mNEWLINE(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 6;
      if (this.LA(1) == '\r' && this.LA(2) == '\n') {
         this.match('\r');
         this.match('\n');
         this.newline();
      } else if (this.LA(1) == '\r') {
         this.match('\r');
         this.newline();
      } else {
         if (this.LA(1) != '\n') {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         this.match('\n');
         this.newline();
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mESCAPED_START(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 7;
      this.match("&lt;");
      this.text.setLength(var4);
      this.text.append("<");
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mESCAPED_END(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 8;
      this.match("&gt;");
      this.text.setLength(var4);
      this.text.append(">");
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mEND_DELIMITER(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 10;
      this.match("> ");
      if (this.LA(1) == '\n' || this.LA(1) == '\r') {
         this.mNEWLINE(false);
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }
}
