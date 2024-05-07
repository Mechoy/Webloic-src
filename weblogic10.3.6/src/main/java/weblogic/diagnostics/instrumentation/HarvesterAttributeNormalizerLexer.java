package weblogic.diagnostics.instrumentation;

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

public class HarvesterAttributeNormalizerLexer extends CharScanner implements HarvesterAttributeNormalizerLexerTokenTypes, TokenStream {
   public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

   public HarvesterAttributeNormalizerLexer(InputStream var1) {
      this((InputBuffer)(new ByteBuffer(var1)));
   }

   public HarvesterAttributeNormalizerLexer(Reader var1) {
      this((InputBuffer)(new CharBuffer(var1)));
   }

   public HarvesterAttributeNormalizerLexer(InputBuffer var1) {
      this(new LexerSharedInputState(var1));
   }

   public HarvesterAttributeNormalizerLexer(LexerSharedInputState var1) {
      super(var1);
      this.caseSensitiveLiterals = true;
      this.setCaseSensitive(true);
      this.literals = new Hashtable();
   }

   public Token nextToken() throws TokenStreamException {
      Token var1 = null;

      while(true) {
         Object var2 = null;
         boolean var3 = false;
         this.resetText();

         try {
            try {
               switch (this.LA(1)) {
                  case '\t':
                  case '\n':
                  case '\r':
                  case ' ':
                     this.mWS(true);
                     var1 = this._returnToken;
                     break;
                  case '(':
                     this.mLPAREN(true);
                     var1 = this._returnToken;
                     break;
                  case ')':
                     this.mRPAREN(true);
                     var1 = this._returnToken;
                     break;
                  case '*':
                     this.mSTAR_WILDCARD(true);
                     var1 = this._returnToken;
                     break;
                  case ',':
                     this.mCOMMA(true);
                     var1 = this._returnToken;
                     break;
                  case '?':
                     this.mPOSITIONAL_WILDCARD(true);
                     var1 = this._returnToken;
                     break;
                  default:
                     if (_tokenSet_0.member(this.LA(1))) {
                        this.mTYPE_NAME(true);
                        var1 = this._returnToken;
                     } else {
                        if (this.LA(1) != '\uffff') {
                           throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                        }

                        this.uponEOF();
                        this._returnToken = this.makeToken(1);
                     }
               }

               if (this._returnToken != null) {
                  int var7 = this._returnToken.getType();
                  this._returnToken.setType(var7);
                  return this._returnToken;
               }
            } catch (RecognitionException var5) {
               throw new TokenStreamRecognitionException(var5);
            }
         } catch (CharStreamException var6) {
            if (var6 instanceof CharStreamIOException) {
               throw new TokenStreamIOException(((CharStreamIOException)var6).io);
            }

            throw new TokenStreamException(var6.getMessage());
         }
      }
   }

   public final void mWS(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
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

   public final void mLPAREN(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 5;
      this.match('(');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mRPAREN(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 6;
      this.match(')');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mCOMMA(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 7;
      this.match(',');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mTYPE_NAME(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 8;
      int var6 = 0;

      while(true) {
         switch (this.LA(1)) {
            case '$':
               this.match('$');
               break;
            case '%':
               this.match('%');
               break;
            case '&':
            case '\'':
            case '(':
            case ')':
            case '*':
            case '+':
            case ',':
            case '-':
            case '/':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '`':
            default:
               if (this.LA(1) < 128 || this.LA(1) > '\ufffe') {
                  if (var6 >= 1) {
                     if (var1 && var3 == null && var2 != -1) {
                        var3 = this.makeToken(var2);
                        var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
                     }

                     this._returnToken = var3;
                     return;
                  } else {
                     throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                  }
               }

               this.matchRange('\u0080', '\ufffe');
               break;
            case '.':
               this.match('.');
               break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
               this.matchRange('0', '9');
               break;
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
               this.matchRange('A', 'Z');
               break;
            case '_':
               this.match('_');
               break;
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
               this.matchRange('a', 'z');
         }

         ++var6;
      }
   }

   public final void mSTAR_WILDCARD(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 9;
      this.match('*');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mPOSITIONAL_WILDCARD(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 10;
      this.match('?');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   private static final long[] mk_tokenSet_0() {
      long[] var0 = new long[3072];
      var0[0] = 288019476077608960L;
      var0[1] = 576460745995190270L;

      for(int var1 = 2; var1 <= 1022; ++var1) {
         var0[var1] = -1L;
      }

      var0[1023] = Long.MAX_VALUE;
      return var0;
   }
}
