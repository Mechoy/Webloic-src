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
import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.collections.impl.BitSet;
import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;

public class AccessLogLexer extends CharScanner implements AccessLogLexerTokenTypes, TokenStream {
   private static final boolean DEBUG = false;
   private boolean hitEOF;
   public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
   public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
   public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
   public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());

   public void uponEOF() throws TokenStreamException, CharStreamException {
      this.hitEOF = true;
      super.uponEOF();
   }

   public boolean isEOFReached() {
      return this.hitEOF;
   }

   public AccessLogLexer(InputStream var1) {
      this((InputBuffer)(new ByteBuffer(var1)));
   }

   public AccessLogLexer(Reader var1) {
      this((InputBuffer)(new CharBuffer(var1)));
   }

   public AccessLogLexer(InputBuffer var1) {
      this(new LexerSharedInputState(var1));
   }

   public AccessLogLexer(LexerSharedInputState var1) {
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
               if (this.LA(1) != '\n' && this.LA(1) != '\r') {
                  if (_tokenSet_0.member(this.LA(1))) {
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
               } else {
                  this.mNEWLINE(true);
                  var1 = this._returnToken;
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

   public final void mNEWLINE(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 4;
      switch (this.LA(1)) {
         case '\n':
            this.match('\n');
            break;
         case '\r':
            this.match('\r');
            break;
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mSPECIAL(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 5;
      switch (this.LA(1)) {
         case '"':
            this.match('"');
            break;
         case '[':
            this.match('[');
            break;
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mWS(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      boolean var2 = true;
      switch (this.LA(1)) {
         case '\t':
            this.match('\t');
            break;
         case '\n':
            this.match('\n');
            break;
         case '\r':
            this.match('\r');
            this.match('\n');
            break;
         case ' ':
            this.match(' ');
            break;
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }

      byte var6 = -1;
      if (var1 && var3 == null && var6 != -1) {
         var3 = this.makeToken(var6);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mNOT_WS(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 7;
      this.match(_tokenSet_0);
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mLOGFIELD(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 8;
      int var5;
      switch (this.LA(1)) {
         case '"':
            var5 = this.text.length();
            this.match('"');
            this.text.setLength(var5);

            while(_tokenSet_2.member(this.LA(1))) {
               this.matchNot('"');
            }

            var5 = this.text.length();
            this.match('"');
            this.text.setLength(var5);
            break;
         case '[':
            var5 = this.text.length();
            this.match('[');
            this.text.setLength(var5);

            while(_tokenSet_1.member(this.LA(1))) {
               this.matchNot(']');
            }

            var5 = this.text.length();
            this.match(']');
            this.text.setLength(var5);
            break;
         default:
            if (!_tokenSet_3.member(this.LA(1))) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            this.match(_tokenSet_3);

            while(_tokenSet_0.member(this.LA(1))) {
               this.mNOT_WS(false);
            }
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   private static final long[] mk_tokenSet_0() {
      long[] var0 = new long[2048];
      var0[0] = -4294977025L;

      for(int var1 = 1; var1 <= 1022; ++var1) {
         var0[var1] = -1L;
      }

      var0[1023] = Long.MAX_VALUE;
      return var0;
   }

   private static final long[] mk_tokenSet_1() {
      long[] var0 = new long[2048];
      var0[0] = -1L;
      var0[1] = -536870913L;

      for(int var1 = 2; var1 <= 1022; ++var1) {
         var0[var1] = -1L;
      }

      var0[1023] = Long.MAX_VALUE;
      return var0;
   }

   private static final long[] mk_tokenSet_2() {
      long[] var0 = new long[2048];
      var0[0] = -17179869185L;

      for(int var1 = 1; var1 <= 1022; ++var1) {
         var0[var1] = -1L;
      }

      var0[1023] = Long.MAX_VALUE;
      return var0;
   }

   private static final long[] mk_tokenSet_3() {
      long[] var0 = new long[2048];
      var0[0] = -21474846209L;
      var0[1] = -134217729L;

      for(int var1 = 2; var1 <= 1022; ++var1) {
         var0[var1] = -1L;
      }

      var0[1023] = Long.MAX_VALUE;
      return var0;
   }
}
