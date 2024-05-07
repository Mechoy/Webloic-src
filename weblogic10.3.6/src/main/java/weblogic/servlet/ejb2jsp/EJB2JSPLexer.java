package weblogic.servlet.ejb2jsp;

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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import weblogic.servlet.ejb2jsp.dd.BeanDescriptor;

public class EJB2JSPLexer extends CharScanner implements EJB2JSPLexerTokenTypes, TokenStream {
   private static final int NO_COMMENT = 0;
   private static final int SLASH_COMMENT = 1;
   private static final int STD_COMMENT = 2;
   BeanDescriptor desc;
   public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
   public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
   public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
   public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
   public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
   public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
   public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
   public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
   public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
   public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());

   public void setDescriptor(BeanDescriptor var1) {
      this.desc = var1;
   }

   private String showLA(int var1) throws CharStreamException {
      StringBuffer var2 = new StringBuffer();
      var2.append("LA(1,");
      var2.append(var1);
      var2.append(")=>#");

      for(int var3 = 1; var3 <= var1; ++var3) {
         var2.append(this.LA(var3));
      }

      var2.append('#');
      return var2.toString();
   }

   void p(String var1) {
      System.err.println("[ejb2jsp @line " + this.getLine() + "]: " + var1);
   }

   public void parse() throws TokenStreamException {
      long var1 = System.currentTimeMillis();

      while(this.nextToken().getType() != 1) {
      }

   }

   public EJB2JSPLexer(InputStream var1) {
      this((InputBuffer)(new ByteBuffer(var1)));
   }

   public EJB2JSPLexer(Reader var1) {
      this((InputBuffer)(new CharBuffer(var1)));
   }

   public EJB2JSPLexer(InputBuffer var1) {
      this(new LexerSharedInputState(var1));
   }

   public EJB2JSPLexer(LexerSharedInputState var1) {
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
         this.setCommitToPath(false);
         int var4 = this.mark();
         this.resetText();

         try {
            try {
               if (_tokenSet_0.member(this.LA(1)) && _tokenSet_1.member(this.LA(2)) && this.LA(3) >= 3 && this.LA(3) <= 255) {
                  this.mTOKEN(true);
                  var1 = this._returnToken;
               } else if (_tokenSet_2.member(this.LA(1)) && _tokenSet_3.member(this.LA(2)) && _tokenSet_4.member(this.LA(3))) {
                  this.mMETHOD_DECLARATION(true);
                  var1 = this._returnToken;
               } else {
                  if (this.LA(1) != '\uffff') {
                     this.commit();

                     try {
                        this.mCODE(false);
                     } catch (RecognitionException var8) {
                        this.reportError(var8);
                        this.consume();
                     }
                     continue;
                  }

                  this.uponEOF();
                  this._returnToken = this.makeToken(1);
               }

               this.commit();
               if (this._returnToken != null) {
                  int var11 = this._returnToken.getType();
                  var11 = this.testLiteralsTable(var11);
                  this._returnToken.setType(var11);
                  return this._returnToken;
               }
            } catch (RecognitionException var9) {
               if (this.getCommitToPath()) {
                  throw new TokenStreamRecognitionException(var9);
               }

               this.rewind(var4);
               this.resetText();

               try {
                  this.mCODE(false);
               } catch (RecognitionException var7) {
                  this.reportError(var7);
                  this.consume();
               }
            }
         } catch (CharStreamException var10) {
            if (var10 instanceof CharStreamIOException) {
               throw new TokenStreamIOException(((CharStreamIOException)var10).io);
            }

            throw new TokenStreamException(var10.getMessage());
         }
      }
   }

   public final void mTOKEN(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 4;
      boolean var6 = false;
      if (this.LA(1) == '/' && this.LA(2) == '/') {
         int var7 = this.mark();
         var6 = true;
         ++this.inputState.guessing;

         try {
            this.match("//");
         } catch (RecognitionException var11) {
            var6 = false;
         }

         this.rewind(var7);
         --this.inputState.guessing;
      }

      if (var6) {
         this.mSLASH_COMMENT(false);
      } else {
         boolean var12 = false;
         if (this.LA(1) == '/' && this.LA(2) == '*') {
            int var8 = this.mark();
            var12 = true;
            ++this.inputState.guessing;

            try {
               this.match("/*");
            } catch (RecognitionException var10) {
               var12 = false;
            }

            this.rewind(var8);
            --this.inputState.guessing;
         }

         if (var12) {
            this.mSTANDARD_COMMENT(false);
         } else {
            if (!_tokenSet_2.member(this.LA(1))) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            this.mMETHOD_DECLARATION(false);
         }
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mSLASH_COMMENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 1;
      Token var6 = null;
      this.match("//");
      this.mSLASH_COMMENT_CONTENT(true);
      var6 = this._returnToken;
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

      if (this.inputState.guessing == 0) {
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mSTANDARD_COMMENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 9;
      Token var6 = null;
      this.match("/*");
      this.mSTANDARD_COMMENT_CONTENT(true);
      var6 = this._returnToken;
      this.match("*/");
      if (this.inputState.guessing == 0) {
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   public final void mMETHOD_DECLARATION(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 5;
      Token var6 = null;
      Token var7 = null;
      ArrayList var8 = new ArrayList();
      ArrayList var9 = new ArrayList();
      switch (this.LA(1)) {
         case 'p':
            this.match("public");
         case '\t':
         case '\n':
         case '\r':
         case ' ':
            int var10;
            for(var10 = 0; _tokenSet_5.member(this.LA(1)); ++var10) {
               this.mWS(false);
            }

            if (var10 < 1) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            } else {
               this.mWORD(true);
               var6 = this._returnToken;

               while(_tokenSet_5.member(this.LA(1))) {
                  this.mWS(false);
               }

               this.mWORD(true);
               var7 = this._returnToken;

               while(_tokenSet_5.member(this.LA(1))) {
                  this.mWS(false);
               }

               this.match("(");
               if (this.inputState.guessing == 0) {
               }

               this.mARGLIST(false, var8, var9);

               while(_tokenSet_5.member(this.LA(1))) {
                  this.mWS(false);
               }

               this.match(")");
               if (this.inputState.guessing == 0) {
                  SourceMethodInfo var11 = new SourceMethodInfo(var7.getText(), var6.getText(), var8, var9);
                  this.desc.resolveSource(var11);
               }

               if (var1 && var3 == null && var2 != -1) {
                  var3 = this.makeToken(var2);
                  var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
               }

               this._returnToken = var3;
               return;
            }
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }
   }

   protected final void mWS(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 13;
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

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mWORD(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 21;
      this.mLETTER(false);

      while(true) {
         while(true) {
            switch (this.LA(1)) {
               case '.':
                  this.mDOT(false);
                  break;
               case '/':
               case ':':
               case ';':
               case '<':
               case '=':
               case '>':
               case '?':
               case '@':
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
               case '\\':
               default:
                  if (!_tokenSet_6.member(this.LA(1)) || !_tokenSet_7.member(this.LA(2))) {
                     if (var1 && var3 == null && var2 != -1) {
                        var3 = this.makeToken(var2);
                        var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
                     }

                     this._returnToken = var3;
                     return;
                  }

                  this.mLETTER(false);
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
               case '[':
               case ']':
                  this.mBRACE(false);
            }
         }
      }
   }

   protected final void mARGLIST(boolean var1, List var2, List var3) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var5 = null;
      int var6 = this.text.length();
      byte var4 = 6;

      while(_tokenSet_3.member(this.LA(1)) && _tokenSet_4.member(this.LA(2)) && _tokenSet_4.member(this.LA(3))) {
         while(_tokenSet_5.member(this.LA(1))) {
            this.mWS(false);
         }

         this.mARG(false, var2, var3);

         while(_tokenSet_5.member(this.LA(1)) && _tokenSet_8.member(this.LA(2))) {
            this.mWS(false);
         }

         switch (this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
            case ')':
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
            case '\'':
            case '(':
            case '*':
            case '+':
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
            case ',':
               this.mCOMMA(false);
         }
      }

      if (var1 && var5 == null && var4 != -1) {
         var5 = this.makeToken(var4);
         var5.setText(new String(this.text.getBuffer(), var6, this.text.length() - var6));
      }

      this._returnToken = var5;
   }

   protected final void mARG(boolean var1, List var2, List var3) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var5 = null;
      int var6 = this.text.length();
      byte var4 = 7;
      Token var8 = null;
      Token var9 = null;
      this.mWORD(true);
      var8 = this._returnToken;

      int var10;
      for(var10 = 0; _tokenSet_5.member(this.LA(1)); ++var10) {
         this.mWS(false);
      }

      if (var10 >= 1) {
         this.mWORD(true);
         var9 = this._returnToken;
         if (this.inputState.guessing == 0) {
            String var12 = var8.getText();
            String var11 = var9.getText();
            var2.add(var12);
            var3.add(var11);
         }

         if (var1 && var5 == null && var4 != -1) {
            var5 = this.makeToken(var4);
            var5.setText(new String(this.text.getBuffer(), var6, this.text.length() - var6));
         }

         this._returnToken = var5;
      } else {
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }
   }

   protected final void mCOMMA(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 14;
      this.match(',');
      if (this.inputState.guessing == 0) {
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mCOMMENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 8;

      while(true) {
         while(this.LA(1) == '/' && this.LA(2) == '*') {
            this.mSTANDARD_COMMENT(false);
         }

         if (this.LA(1) != '/' || this.LA(2) != '/') {
            if (var1 && var3 == null && var2 != -1) {
               var3 = this.makeToken(var2);
               var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
            }

            this._returnToken = var3;
            return;
         }

         this.mSLASH_COMMENT(false);
      }
   }

   protected final void mSTANDARD_COMMENT_CONTENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 10;

      while((this.LA(1) != '*' || this.LA(2) != '/') && this.LA(1) >= 3 && this.LA(1) <= 255 && this.LA(2) >= 3 && this.LA(2) <= 255 && this.LA(3) >= 3 && this.LA(3) <= 255) {
         this.matchNot('\uffff');
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mSLASH_COMMENT_CONTENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 12;

      while(_tokenSet_9.member(this.LA(1))) {
         this.match(_tokenSet_9);
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
      byte var2 = 15;
      this.match('.');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mDASH(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 16;
      this.match('-');
      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   protected final void mSTAR(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
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

   protected final void mDIGIT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 18;
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
      byte var2 = 19;
      switch (this.LA(1)) {
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
         case '[':
         case '\\':
         case ']':
         case '^':
         case '`':
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
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

   protected final void mBRACE(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 20;
      switch (this.LA(1)) {
         case '[':
            this.match('[');
            break;
         case ']':
            this.match(']');
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

   protected final void mIMPORT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 22;
      this.mLETTER(false);

      while(true) {
         switch (this.LA(1)) {
            case '*':
               this.mSTAR(false);
               break;
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
               if (var1 && var3 == null && var2 != -1) {
                  var3 = this.makeToken(var2);
                  var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
               }

               this._returnToken = var3;
               return;
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
         }
      }
   }

   protected final void mCODE(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var3 = null;
      int var4 = this.text.length();
      byte var2 = 23;
      boolean var6 = false;
      char var7 = this.LA(1);
      this.matchNot('\uffff');
      if (this.inputState.guessing == 0 && var7 == '\n') {
      }

      if (var1 && var3 == null && var2 != -1) {
         var3 = this.makeToken(var2);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
   }

   private static final long[] mk_tokenSet_0() {
      long[] var0 = new long[]{140741783332352L, 281474976710656L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_1() {
      long[] var0 = new long[]{145139829843456L, 576460745995190270L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_2() {
      long[] var0 = new long[]{4294977024L, 281474976710656L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_3() {
      long[] var0 = new long[]{4294977024L, 576460745995190270L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_4() {
      long[] var0 = new long[]{288019274214155776L, 576460746666278910L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_5() {
      long[] var0 = new long[]{4294977024L, 0L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_6() {
      long[] var0 = new long[]{0L, 576460745995190270L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_7() {
      long[] var0 = new long[]{288040164935083520L, 576460746666278910L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_8() {
      long[] var0 = new long[]{19795504276992L, 576460745995190270L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_9() {
      long[] var0 = new long[8];
      var0[0] = -9224L;

      for(int var1 = 1; var1 <= 3; ++var1) {
         var0[var1] = -1L;
      }

      return var0;
   }
}
