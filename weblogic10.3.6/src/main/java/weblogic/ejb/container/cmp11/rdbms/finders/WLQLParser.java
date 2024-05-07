package weblogic.ejb.container.cmp11.rdbms.finders;

import antlr.LLkParser;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import weblogic.ejb.container.ejbc.EJBCException;

public class WLQLParser extends LLkParser implements WLQLParserTokenTypes {
   public static final String[] _tokenNames = new String[]{"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "LPAREN", "RPAREN", "AND", "OR", "NOT", "EQUALS", "LT", "GT", "LTEQ", "GTEQ", "\"like\"", "\"isNull\"", "\"isNotNull\"", "\"orderBy\"", "VARIABLE", "SPECIAL", "ID", "STRING", "BACKSTRING", "NUMBER", "SLASH", "BACKTICK", "SSTRING", "DSTRING", "DASH", "DOT", "INT", "REAL", "UNICODE_RANGE", "WS", "COMMENT", "DIGIT", "LETTER"};
   public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

   public WLQLParser() {
      super(2);
   }

   public WLQLExpression parse(String var1) throws EJBCException {
      try {
         byte[] var2 = var1.getBytes();
         ByteArrayInputStream var3 = new ByteArrayInputStream(var2);
         WLQLLexer var4 = new WLQLLexer(var3);
         TokenBuffer var5 = new TokenBuffer(var4);
         this.setTokenBuffer(var5);
         return this.expression();
      } catch (Exception var6) {
         throw new EJBCException("Couldn't parse '" + var1 + "' into WLQLExpression.", var6);
      }
   }

   protected WLQLParser(TokenBuffer var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public WLQLParser(TokenBuffer var1) {
      this((TokenBuffer)var1, 2);
   }

   protected WLQLParser(TokenStream var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public WLQLParser(TokenStream var1) {
      this((TokenStream)var1, 2);
   }

   public WLQLParser(ParserSharedInputState var1) {
      super(var1, 2);
      this.tokenNames = _tokenNames;
   }

   public final WLQLExpression expression() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      switch (this.LA(1)) {
         case 4:
            this.match(4);
            switch (this.LA(1)) {
               case 6:
                  var1 = this.and();
                  break;
               case 7:
                  var1 = this.or();
                  break;
               case 8:
                  var1 = this.not();
                  break;
               case 9:
                  var1 = this.eq();
                  break;
               case 10:
                  var1 = this.less_than();
                  break;
               case 11:
                  var1 = this.greater_than();
                  break;
               case 12:
                  var1 = this.less_than_or_equal();
                  break;
               case 13:
                  var1 = this.greater_than_or_equal();
                  break;
               case 14:
                  var1 = this.like();
                  break;
               case 15:
                  var1 = this.isnull();
                  break;
               case 16:
                  var1 = this.isnotnull();
                  break;
               case 17:
                  var1 = this.orderby();
                  break;
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            this.match(5);
            break;
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 18:
            var1 = this.variable();
            break;
         case 19:
            var1 = this.special();
            break;
         case 20:
            var1 = this.id();
            break;
         case 21:
         case 22:
            var1 = this.string();
            break;
         case 23:
            var1 = this.number();
      }

      return var1;
   }

   public final WLQLExpression and() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      new ArrayList();
      this.match(6);
      List var2 = this.expression_list();
      var1 = new WLQLExpression(0, var2);
      return var1;
   }

   public final WLQLExpression or() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      new ArrayList();
      this.match(7);
      List var2 = this.expression_list();
      var1 = new WLQLExpression(1, var2);
      return var1;
   }

   public final WLQLExpression not() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      WLQLExpression var2 = null;
      this.match(8);
      var2 = this.expression();
      var1 = new WLQLExpression(2, var2);
      return var1;
   }

   public final WLQLExpression eq() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      new ArrayList();
      this.match(9);
      List var2 = this.expression_list();
      var1 = new WLQLExpression(3, var2);
      return var1;
   }

   public final WLQLExpression less_than() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      WLQLExpression var2 = null;
      WLQLExpression var3 = null;
      this.match(10);
      var2 = this.expression();
      var3 = this.expression();
      var1 = new WLQLExpression(4, var2, var3);
      return var1;
   }

   public final WLQLExpression greater_than() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      WLQLExpression var2 = null;
      WLQLExpression var3 = null;
      this.match(11);
      var2 = this.expression();
      var3 = this.expression();
      var1 = new WLQLExpression(5, var2, var3);
      return var1;
   }

   public final WLQLExpression less_than_or_equal() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      WLQLExpression var2 = null;
      WLQLExpression var3 = null;
      this.match(12);
      var2 = this.expression();
      var3 = this.expression();
      var1 = new WLQLExpression(6, var2, var3);
      return var1;
   }

   public final WLQLExpression greater_than_or_equal() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      WLQLExpression var2 = null;
      WLQLExpression var3 = null;
      this.match(13);
      var2 = this.expression();
      var3 = this.expression();
      var1 = new WLQLExpression(7, var2, var3);
      return var1;
   }

   public final WLQLExpression like() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      WLQLExpression var2 = null;
      WLQLExpression var3 = null;
      this.match(14);
      var2 = this.expression();
      var3 = this.expression();
      var1 = new WLQLExpression(8, var2, var3);
      return var1;
   }

   public final WLQLExpression isnull() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      WLQLExpression var2 = null;
      this.match(15);
      var2 = this.expression();
      var1 = new WLQLExpression(14, var2);
      return var1;
   }

   public final WLQLExpression isnotnull() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      WLQLExpression var2 = null;
      this.match(16);
      var2 = this.expression();
      var1 = new WLQLExpression(15, var2);
      return var1;
   }

   public final WLQLExpression orderby() throws RecognitionException, TokenStreamException {
      WLQLExpression var1 = null;
      WLQLExpression var2 = null;
      WLQLExpression var3 = null;
      this.match(17);
      var2 = this.string();
      var3 = this.expression();
      var1 = new WLQLExpression(16, var2, var3);
      return var1;
   }

   public final WLQLExpression variable() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      WLQLExpression var1 = null;
      var2 = this.LT(1);
      this.match(18);
      var1 = new WLQLExpression(13, var2.getText());
      return var1;
   }

   public final WLQLExpression special() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      WLQLExpression var1 = null;
      WLQLExpression var3 = null;
      ArrayList var4 = new ArrayList();
      var2 = this.LT(1);
      this.match(19);
      var3 = this.string();
      var4.add(var3);
      var1 = new WLQLExpression(var2.getText(), var4);
      return var1;
   }

   public final WLQLExpression id() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      WLQLExpression var1 = null;
      var2 = this.LT(1);
      this.match(20);
      var1 = new WLQLExpression(9, var2.getText());
      return var1;
   }

   public final WLQLExpression string() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      Token var3 = null;
      WLQLExpression var1 = null;
      switch (this.LA(1)) {
         case 21:
            var2 = this.LT(1);
            this.match(21);
            var1 = new WLQLExpression(10, var2.getText());
            break;
         case 22:
            var3 = this.LT(1);
            this.match(22);
            var1 = new WLQLExpression(10, var3.getText());
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      return var1;
   }

   public final WLQLExpression number() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      WLQLExpression var1 = null;
      var2 = this.LT(1);
      this.match(23);
      var1 = new WLQLExpression(11, var2.getText());
      return var1;
   }

   public final List expression_list() throws RecognitionException, TokenStreamException {
      ArrayList var1 = new ArrayList();
      WLQLExpression var2 = null;

      int var3;
      for(var3 = 0; _tokenSet_0.member(this.LA(1)); ++var3) {
         var2 = this.expression();
         var1.add(var2);
      }

      if (var3 >= 1) {
         return var1;
      } else {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   private static final long[] mk_tokenSet_0() {
      long[] var0 = new long[]{16515088L, 0L};
      return var0;
   }
}
