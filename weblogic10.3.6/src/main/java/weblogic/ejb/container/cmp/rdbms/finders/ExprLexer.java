package weblogic.ejb.container.cmp.rdbms.finders;

import antlr.ANTLRHashString;
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

public class ExprLexer extends CharScanner implements ExprParserTokenTypes, TokenStream {
   public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
   public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
   public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

   public ExprLexer(InputStream var1) {
      this((InputBuffer)(new ByteBuffer(var1)));
   }

   public ExprLexer(Reader var1) {
      this((InputBuffer)(new CharBuffer(var1)));
   }

   public ExprLexer(InputBuffer var1) {
      this(new LexerSharedInputState(var1));
   }

   public ExprLexer(LexerSharedInputState var1) {
      super(var1);
      this.caseSensitiveLiterals = false;
      this.setCaseSensitive(true);
      this.literals = new Hashtable();
      this.literals.put(new ANTLRHashString("all", this), new Integer(16));
      this.literals.put(new ANTLRHashString("sqrt", this), new Integer(65));
      this.literals.put(new ANTLRHashString("count", this), new Integer(15));
      this.literals.put(new ANTLRHashString("sum", this), new Integer(14));
      this.literals.put(new ANTLRHashString("for", this), new Integer(21));
      this.literals.put(new ANTLRHashString("orderby", this), new Integer(23));
      this.literals.put(new ANTLRHashString("min", this), new Integer(11));
      this.literals.put(new ANTLRHashString("lower", this), new Integer(63));
      this.literals.put(new ANTLRHashString("upper", this), new Integer(62));
      this.literals.put(new ANTLRHashString("false", this), new Integer(56));
      this.literals.put(new ANTLRHashString("abs", this), new Integer(64));
      this.literals.put(new ANTLRHashString("true", this), new Integer(55));
      this.literals.put(new ANTLRHashString("substring", this), new Integer(59));
      this.literals.put(new ANTLRHashString("and", this), new Integer(30));
      this.literals.put(new ANTLRHashString("concat", this), new Integer(58));
      this.literals.put(new ANTLRHashString("asc", this), new Integer(45));
      this.literals.put(new ANTLRHashString("desc", this), new Integer(46));
      this.literals.put(new ANTLRHashString("select", this), new Integer(4));
      this.literals.put(new ANTLRHashString("member", this), new Integer(47));
      this.literals.put(new ANTLRHashString("exists", this), new Integer(32));
      this.literals.put(new ANTLRHashString("distinct", this), new Integer(5));
      this.literals.put(new ANTLRHashString("group", this), new Integer(26));
      this.literals.put(new ANTLRHashString("where", this), new Integer(22));
      this.literals.put(new ANTLRHashString("avg", this), new Integer(13));
      this.literals.put(new ANTLRHashString("order", this), new Integer(24));
      this.literals.put(new ANTLRHashString("mod", this), new Integer(66));
      this.literals.put(new ANTLRHashString("in", this), new Integer(19));
      this.literals.put(new ANTLRHashString("null", this), new Integer(40));
      this.literals.put(new ANTLRHashString("locate", this), new Integer(61));
      this.literals.put(new ANTLRHashString("empty", this), new Integer(41));
      this.literals.put(new ANTLRHashString("escape", this), new Integer(44));
      this.literals.put(new ANTLRHashString("length", this), new Integer(60));
      this.literals.put(new ANTLRHashString("having", this), new Integer(27));
      this.literals.put(new ANTLRHashString("of", this), new Integer(48));
      this.literals.put(new ANTLRHashString("or", this), new Integer(29));
      this.literals.put(new ANTLRHashString("between", this), new Integer(42));
      this.literals.put(new ANTLRHashString("max", this), new Integer(12));
      this.literals.put(new ANTLRHashString("from", this), new Integer(18));
      this.literals.put(new ANTLRHashString("is", this), new Integer(39));
      this.literals.put(new ANTLRHashString("like", this), new Integer(43));
      this.literals.put(new ANTLRHashString("any", this), new Integer(52));
      this.literals.put(new ANTLRHashString("select_hint", this), new Integer(28));
      this.literals.put(new ANTLRHashString("object", this), new Integer(7));
      this.literals.put(new ANTLRHashString("not", this), new Integer(31));
      this.literals.put(new ANTLRHashString("by", this), new Integer(25));
      this.literals.put(new ANTLRHashString("as", this), new Integer(20));
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
                  case '\u000b':
                  case '\f':
                  case '\u000e':
                  case '\u000f':
                  case '\u0010':
                  case '\u0011':
                  case '\u0012':
                  case '\u0013':
                  case '\u0014':
                  case '\u0015':
                  case '\u0016':
                  case '\u0017':
                  case '\u0018':
                  case '\u0019':
                  case '\u001a':
                  case '\u001b':
                  case '\u001c':
                  case '\u001d':
                  case '\u001e':
                  case '\u001f':
                  case '!':
                  case '"':
                  case '#':
                  case '$':
                  case '%':
                  case '&':
                  case '.':
                  case ':':
                  case ';':
                  case '<':
                  case '>':
                  default:
                     if (this.LA(1) == '<' && this.LA(2) == '>') {
                        this.mNTEQ(true);
                        var1 = this._returnToken;
                     } else if (this.LA(1) == '<' && this.LA(2) == '=') {
                        this.mLTEQ(true);
                        var1 = this._returnToken;
                     } else if (this.LA(1) == '>' && this.LA(2) == '=') {
                        this.mGTEQ(true);
                        var1 = this._returnToken;
                     } else if (this.LA(1) == '<') {
                        this.mLT(true);
                        var1 = this._returnToken;
                     } else if (this.LA(1) == '>') {
                        this.mGT(true);
                        var1 = this._returnToken;
                     } else if (_tokenSet_0.member(this.LA(1))) {
                        this.mID(true);
                        var1 = this._returnToken;
                     } else {
                        if (this.LA(1) != '\uffff') {
                           throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                        }

                        this.uponEOF();
                        this._returnToken = this.makeToken(1);
                     }
                     break;
                  case '\'':
                     this.mSTRING(true);
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
                     this.mTIMES(true);
                     var1 = this._returnToken;
                     break;
                  case '+':
                     this.mPLUS(true);
                     var1 = this._returnToken;
                     break;
                  case ',':
                     this.mCOMMA(true);
                     var1 = this._returnToken;
                     break;
                  case '-':
                     this.mMINUS(true);
                     var1 = this._returnToken;
                     break;
                  case '/':
                     this.mDIV(true);
                     var1 = this._returnToken;
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
                     this.mNUMBER(true);
                     var1 = this._returnToken;
                     break;
                  case '=':
                     this.mEQ(true);
                     var1 = this._returnToken;
                     break;
                  case '?':
                     this.mVARIABLE(true);
                     var1 = this._returnToken;
               }

               if (this._returnToken != null) {
                  int var7 = this._returnToken.getType();
                  var7 = this.testLiteralsTable(var7);
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

   public final void mLPAREN(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 8;
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
      byte var2 = 10;
      this.match(')');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mEQ(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 34;
      this.match('=');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mLT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 35;
      this.match('<');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mGT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 36;
      this.match('>');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mNTEQ(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 33;
      this.match("<>");
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mLTEQ(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 37;
      this.match("<=");
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mGTEQ(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 38;
      this.match(">=");
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mDIV(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 51;
      this.match('/');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mTIMES(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 17;
      this.match('*');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mMINUS(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 50;
      this.match('-');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mPLUS(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 49;
      this.match('+');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mCOMMA(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 6;
      this.match(',');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mDIGIT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 67;
      this.matchRange('0', '9');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mLETTER(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 68;
      switch (this.LA(1)) {
         case '$':
            this.match('$');
            break;
         case '%':
         case '&':
         case '\'':
         case '(':
         case ')':
         case '*':
         case '+':
         case ',':
         case '-':
         case '.':
         case '/':
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
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
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

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mDOT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 69;
      this.match('.');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mAT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 70;
      this.match('@');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mDASH(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 71;
      this.match('-');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mUNICODE_RANGE(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 72;
      this.matchRange('\u0081', '\ufffe');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mVARIABLE(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 53;
      int var5 = this.text.length();
      this.match('?');
      this.text.setLength(var5);
      this.mINT(false);
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mINT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 73;

      int var6;
      for(var6 = 0; this.LA(1) >= '0' && this.LA(1) <= '9'; ++var6) {
         this.mDIGIT(false);
      }

      if (var6 >= 1) {
         if (var1 && var3 == null && var2 != -1) {
            var3 = this.makeToken(var2);
            var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
         }

         this._returnToken = var3;
      } else {
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }
   }

   public final void mID(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 9;
      switch (this.LA(1)) {
         case '$':
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
         case '_':
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
            this.mLETTER(false);
            break;
         case '%':
         case '&':
         case '\'':
         case '(':
         case ')':
         case '*':
         case '+':
         case ',':
         case '-':
         case '.':
         case '/':
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
         case ':':
         case ';':
         case '<':
         case '=':
         case '>':
         case '?':
         case '[':
         case '\\':
         case ']':
         case '^':
         case '`':
         default:
            if (this.LA(1) < 129 || this.LA(1) > '\ufffe') {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            this.mUNICODE_RANGE(false);
            break;
         case '@':
            this.mAT(false);
      }

      while(true) {
         while(true) {
            switch (this.LA(1)) {
               case '$':
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
               case '_':
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
                  this.mLETTER(false);
                  break;
               case '%':
               case '&':
               case '\'':
               case '(':
               case ')':
               case '*':
               case '+':
               case ',':
               case '/':
               case ':':
               case ';':
               case '<':
               case '=':
               case '>':
               case '?':
               case '[':
               case '\\':
               case ']':
               case '^':
               case '`':
               default:
                  if (this.LA(1) < 129 || this.LA(1) > '\ufffe') {
                     if (var1 && var3 == null && var2 != -1) {
                        var3 = this.makeToken(var2);
                        var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
                     }

                     this._returnToken = var3;
                     return;
                  }

                  this.mUNICODE_RANGE(false);
                  break;
               case '-':
                  this.mDASH(false);
                  break;
               case '.':
                  this.mDOT(false);
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
                  this.mDIGIT(false);
                  break;
               case '@':
                  this.mAT(false);
            }
         }
      }
   }

   public final void mSTRING(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 54;
      this.match('\'');

      while(true) {
         while(this.LA(1) != '\'' || this.LA(2) != '\'') {
            if (!_tokenSet_1.member(this.LA(1))) {
               this.match('\'');
               if (var1 && var3 == null && var2 != -1) {
                  var3 = this.makeToken(var2);
                  var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
               }

               this._returnToken = var3;
               return;
            }

            this.matchNot('\'');
         }

         this.match('\'');
         int var5 = this.text.length();
         this.match('\'');
         this.text.setLength(var5);
      }
   }

   public final void mNUMBER(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 57;
      boolean var6 = false;
      if (this.LA(1) >= '0' && this.LA(1) <= '9' && _tokenSet_2.member(this.LA(2))) {
         int var7 = this.mark();
         var6 = true;
         ++this.inputState.guessing;

         try {
            int var8;
            for(var8 = 0; this.LA(1) >= '0' && this.LA(1) <= '9'; ++var8) {
               this.mDIGIT(false);
            }

            if (var8 < 1) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            switch (this.LA(1)) {
               case '.':
                  this.mDOT(false);
                  break;
               case 'E':
               case 'e':
                  this.mE(false);
                  break;
               default:
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         } catch (RecognitionException var9) {
            var6 = false;
         }

         this.rewind(var7);
         --this.inputState.guessing;
      }

      if (var6) {
         this.mREAL(false);
      } else {
         if (this.LA(1) < '0' || this.LA(1) > '9') {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         this.mINT(false);
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mE(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 75;
      switch (this.LA(1)) {
         case 'E':
            this.match('E');
            break;
         case 'e':
            this.match('e');
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

   protected final void mREAL(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 74;

      int var6;
      for(var6 = 0; this.LA(1) >= '0' && this.LA(1) <= '9'; ++var6) {
         this.mDIGIT(false);
      }

      if (var6 < 1) {
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      } else {
         if (this.LA(1) == '.' && this.LA(2) >= '0' && this.LA(2) <= '9') {
            this.mDOT(false);

            for(var6 = 0; this.LA(1) >= '0' && this.LA(1) <= '9'; ++var6) {
               this.mDIGIT(false);
            }

            if (var6 < 1) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            if (this.LA(1) == 'E' || this.LA(1) == 'e') {
               this.mE(false);
               switch (this.LA(1)) {
                  case '-':
                     this.mMINUS(false);
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
                     for(var6 = 0; this.LA(1) >= '0' && this.LA(1) <= '9'; ++var6) {
                        this.mDIGIT(false);
                     }

                     if (var6 < 1) {
                        throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                     }
                     break;
                  case '.':
                  case '/':
                  default:
                     throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }
            }
         } else if (this.LA(1) != 'E' && this.LA(1) != 'e') {
            if (this.LA(1) != '.') {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            this.mDOT(false);
         } else {
            this.mE(false);
            switch (this.LA(1)) {
               case '-':
                  this.mMINUS(false);
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
                  for(var6 = 0; this.LA(1) >= '0' && this.LA(1) <= '9'; ++var6) {
                     this.mDIGIT(false);
                  }

                  if (var6 < 1) {
                     throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                  }
                  break;
               case '.':
               case '/':
               default:
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         }

         if (var1 && var3 == null && var2 != -1) {
            var3 = this.makeToken(var2);
            var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
         }

         this._returnToken = var3;
      }
   }

   public final void mWS(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 76;
      switch (this.LA(1)) {
         case '\t':
            this.match('\t');
            break;
         case '\n':
            this.match('\n');
            if (this.inputState.guessing == 0) {
               this.newline();
            }
            break;
         case '\r':
            this.match('\r');
            break;
         case ' ':
            this.match(' ');
            break;
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }

      if (this.inputState.guessing == 0) {
         var2 = -1;
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   private static final long[] mk_tokenSet_0() {
      long[] var0 = new long[3072];
      var0[0] = 68719476736L;
      var0[1] = 576460745995190271L;
      var0[2] = -2L;

      for(int var1 = 3; var1 <= 1022; ++var1) {
         var0[var1] = -1L;
      }

      var0[1023] = Long.MAX_VALUE;
      return var0;
   }

   private static final long[] mk_tokenSet_1() {
      long[] var0 = new long[2048];
      var0[0] = -549755813889L;

      for(int var1 = 1; var1 <= 1022; ++var1) {
         var0[var1] = -1L;
      }

      var0[1023] = Long.MAX_VALUE;
      return var0;
   }

   private static final long[] mk_tokenSet_2() {
      long[] var0 = new long[1025];
      var0[0] = 288019269919178752L;
      var0[1] = 137438953504L;
      return var0;
   }
}
