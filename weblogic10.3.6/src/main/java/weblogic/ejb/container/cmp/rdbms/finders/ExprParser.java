package weblogic.ejb.container.cmp.rdbms.finders;

import antlr.LLkParser;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import weblogic.ejb20.cmp.rdbms.finders.EJBQLCompilerException;
import weblogic.ejb20.cmp.rdbms.finders.EJBQLToken;

public class ExprParser extends LLkParser implements ExprParserTokenTypes {
   EjbqlFinder finder;
   Expr exprNOOP;
   int nextSubQuery;
   Vector exprVector;
   static String[] keywords = new String[]{"abs", "all", "and", "any", "as", "asc", "avg", "between", "by", "concat", "count", "desc", "distinct", "empty", "escape", "exists", "false", "for", "from", "group", "having", "in", "is", "length", "like", "locate", "max", "member", "min", "mod", "new", "not", "null", "object", "of", "or", "order", "orderby", "select", "select_hint", "sqrt", "substring", "sum", "true", "where", "upper", "lower"};
   StringBuffer exceptionBuff;
   List ejbqlParserTokenList;
   boolean parseExceptionErrorHandled;
   String lastIdValue;
   public static final String[] _tokenNames = new String[]{"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "\"select\"", "\"distinct\"", "COMMA", "\"object\"", "LPAREN", "ID", "RPAREN", "\"min\"", "\"max\"", "\"avg\"", "\"sum\"", "\"count\"", "\"all\"", "TIMES", "\"from\"", "\"in\"", "\"as\"", "\"for\"", "\"where\"", "\"orderby\"", "\"order\"", "\"by\"", "\"group\"", "\"having\"", "\"select_hint\"", "\"or\"", "\"and\"", "\"not\"", "\"exists\"", "NTEQ", "EQ", "LT", "GT", "LTEQ", "GTEQ", "\"is\"", "\"null\"", "\"empty\"", "\"between\"", "\"like\"", "\"escape\"", "\"asc\"", "\"desc\"", "\"member\"", "\"of\"", "PLUS", "MINUS", "DIV", "\"any\"", "VARIABLE", "STRING", "\"true\"", "\"false\"", "NUMBER", "\"concat\"", "\"substring\"", "\"length\"", "\"locate\"", "\"upper\"", "\"lower\"", "\"abs\"", "\"sqrt\"", "\"mod\"", "DIGIT", "LETTER", "DOT", "AT", "DASH", "UNICODE_RANGE", "INT", "REAL", "E", "WS"};
   public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
   public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
   public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
   public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
   public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());

   public ExprParser(EjbqlFinder var1) {
      super(2);
      this.exprNOOP = new ExprNOOP(72);
      this.nextSubQuery = 1;
      this.exprVector = new Vector();
      this.exceptionBuff = new StringBuffer();
      this.parseExceptionErrorHandled = false;
      this.lastIdValue = null;
      this.finder = var1;
   }

   public Expr parse(String var1) throws EJBQLCompilerException {
      try {
         StringReader var7 = new StringReader(var1);
         ExprLexer var8 = new ExprLexer(var7);
         TokenBuffer var9 = new TokenBuffer(var8);
         this.setTokenBuffer(var9);
         this.ejbqlParserTokenList = new ArrayList();
         return this.rootExpr();
      } catch (Exception var6) {
         try {
            this.handleParserEjbqlTokensAfterFailure();
         } catch (TokenStreamException var5) {
         }

         String var3 = var6.toString();
         if (var3.indexOf("java.lang.NullPointerException") != -1) {
            var3 = "";
         } else {
            var3 = this.removeANTRLine(var3) + "\n\n";
         }

         Exception var2 = new Exception("\nEJB QL Parser Error.\n\n" + var3 + this.exceptionBuff.toString() + "\n");
         EJBQLCompilerException var4 = this.finder.newEJBQLCompilerException(var2, this.ejbqlParserTokenList);
         throw var4;
      }
   }

   private String getTokenText() throws TokenStreamException {
      return this.LT(0).getText();
   }

   private EJBQLToken addToParserTokenList() throws TokenStreamException {
      String var1 = this.getTokenText();
      return this.addToParserTokenList(var1, true);
   }

   private EJBQLToken addToParserTokenList(boolean var1) throws TokenStreamException {
      String var2 = this.getTokenText();
      return this.addToParserTokenList(var2, var1);
   }

   private EJBQLToken addToParserTokenList(String var1) throws TokenStreamException {
      return this.addToParserTokenList(var1, true);
   }

   private EJBQLToken addToParserTokenList(String var1, boolean var2) throws TokenStreamException {
      if (var2) {
         var1 = var1 + " ";
      }

      EJBQLToken var3 = new EJBQLToken(var1);
      this.ejbqlParserTokenList.add(var3);
      return var3;
   }

   private void handleParserEjbqlTokensAfterFailure() throws TokenStreamException {
      if (!this.parseExceptionErrorHandled) {
         this.parseExceptionErrorHandled = true;
         String var1 = "?";
         Token var2 = this.LT(1);
         if (var2 != null) {
            var1 = var2.getText();
         } else {
            var2 = this.LT(0);
            if (var2 != null) {
               var1 = var2.getText();
            }
         }

         EJBQLToken var3 = this.addToParserTokenList(var1);
         var3.setHadException(true);
         int var4 = 2;

         while(true) {
            try {
               int var5 = this.LA(var4);
               if (var5 == 1) {
                  break;
               }

               var2 = this.LT(var4++);
               this.addToParserTokenList(var2.getText());
            } catch (Exception var6) {
               break;
            }
         }

      }
   }

   private void appendCurrTokenIsKeywordMaybe() {
      boolean var1 = false;

      String var2;
      try {
         var2 = this.LT(1).getText();
      } catch (Throwable var4) {
         return;
      }

      if (var2 != null) {
         if (var2.length() != 0) {
            for(int var3 = 0; var3 < keywords.length; ++var3) {
               if (keywords[var3].equalsIgnoreCase(var2)) {
                  var1 = true;
                  break;
               }
            }

            if (var1) {
               this.exceptionBuff.append("Note: '" + var2 + "' is a Reserved Keyword.\n\n");
            }

         }
      }
   }

   private String removeANTRLine(String var1) {
      String var2 = "line 1:";
      int var3 = var1.indexOf(var2);
      return var3 == 0 ? var1.substring(var3 + var2.length()) : var1;
   }

   protected ExprParser(TokenBuffer var1, int var2) {
      super(var1, var2);
      this.exprNOOP = new ExprNOOP(72);
      this.nextSubQuery = 1;
      this.exprVector = new Vector();
      this.exceptionBuff = new StringBuffer();
      this.parseExceptionErrorHandled = false;
      this.lastIdValue = null;
      this.tokenNames = _tokenNames;
   }

   public ExprParser(TokenBuffer var1) {
      this((TokenBuffer)var1, 2);
   }

   protected ExprParser(TokenStream var1, int var2) {
      super(var1, var2);
      this.exprNOOP = new ExprNOOP(72);
      this.nextSubQuery = 1;
      this.exprVector = new Vector();
      this.exceptionBuff = new StringBuffer();
      this.parseExceptionErrorHandled = false;
      this.lastIdValue = null;
      this.tokenNames = _tokenNames;
   }

   public ExprParser(TokenStream var1) {
      this((TokenStream)var1, 2);
   }

   public ExprParser(ParserSharedInputState var1) {
      super(var1, 2);
      this.exprNOOP = new ExprNOOP(72);
      this.nextSubQuery = 1;
      this.exprVector = new Vector();
      this.exceptionBuff = new StringBuffer();
      this.parseExceptionErrorHandled = false;
      this.lastIdValue = null;
      this.tokenNames = _tokenNames;
   }

   public final ExprROOT rootExpr() throws RecognitionException, TokenStreamException {
      ExprROOT var1 = null;
      Object var2 = null;
      this.getExprs();
      var1 = this.getExprFromVector();
      var1.appendMainEJBQL(" ");
      return var1;
   }

   public final void getExprs() throws RecognitionException, TokenStreamException {
      ExprSELECT var1 = null;
      Expr var2 = null;
      Expr var3 = null;
      Expr var4 = null;
      Expr var5 = null;
      ExprFROM var6 = null;
      var1 = this.selectExpr();
      if (var1 != null) {
         this.exprVector.addElement(var1);
      }

      var6 = this.fromExpr();
      if (var6 != null) {
         this.exprVector.addElement(var6);
      }

      var2 = this.whereExpr();
      if (var2 != null) {
         this.exprVector.addElement(var2);
      }

      var3 = this.groupByExpr();
      if (var3 != null) {
         this.exprVector.addElement(var3);
      }

      var4 = this.orderByExpr();
      if (var4 != null) {
         this.exprVector.addElement(var4);
      }

      var5 = this.selectHintExpr();
      if (var5 != null) {
         this.exprVector.addElement(var5);
      }

   }

   public final ExprROOT getExprFromVector() throws RecognitionException, TokenStreamException {
      ExprROOT var1 = null;
      int var2 = this.exprVector.size();
      Enumeration var3 = this.exprVector.elements();
      if (var2 == 5) {
         var1 = new ExprROOT(33, (Expr)var3.nextElement(), (Expr)var3.nextElement(), (Expr)var3.nextElement(), (Expr)var3.nextElement(), (Expr)var3.nextElement());
      } else if (var2 == 4) {
         var1 = new ExprROOT(33, (Expr)var3.nextElement(), (Expr)var3.nextElement(), (Expr)var3.nextElement(), (Expr)var3.nextElement());
      } else if (var2 == 3) {
         var1 = new ExprROOT(33, (Expr)var3.nextElement(), (Expr)var3.nextElement(), (Expr)var3.nextElement());
      } else if (var2 == 2) {
         var1 = new ExprROOT(33, (Expr)var3.nextElement(), (Expr)var3.nextElement());
      } else if (var2 == 1) {
         var1 = new ExprROOT(33, (Expr)var3.nextElement());
      } else {
         this.reportError("Expected 2-5 EJB-QL Elements: SELECT, FROM, [WHERE, ORDER_BY, SELECT_HINT] instead got: " + var2);
      }

      return var1;
   }

   public final ExprSELECT selectExpr() throws RecognitionException, TokenStreamException {
      ExprSELECT var1 = null;
      EJBQLToken var2 = null;
      switch (this.LA(1)) {
         case 1:
         case 18:
         case 22:
         case 23:
         case 24:
         case 26:
         case 28:
            var1 = null;
            break;
         case 2:
         case 3:
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
         case 19:
         case 20:
         case 21:
         case 25:
         case 27:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 4:
            this.match(4);
            var2 = this.addToParserTokenList();
            var1 = this.selectBody();
            var1.appendMainEJBQL(var2);
      }

      return var1;
   }

   public final ExprFROM fromExpr() throws RecognitionException, TokenStreamException {
      ExprFROM var1 = null;
      Expr var2 = null;
      Vector var4 = new Vector();
      EJBQLToken var5 = null;
      String var6 = "";

      try {
         switch (this.LA(1)) {
            case 1:
            case 10:
            case 22:
            case 23:
            case 24:
            case 26:
            case 28:
               var1 = null;
               break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 19:
            case 20:
            case 21:
            case 25:
            case 27:
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            case 18:
               this.match(18);
               var5 = this.addToParserTokenList();
               var6 = var5.getTokenText();
               var2 = this.fromTarget();
               var4.addElement(var2);

               while(this.LA(1) == 6) {
                  this.match(6);
                  this.addToParserTokenList();
                  var2 = this.fromTarget();
                  var4.addElement(var2);
               }

               var1 = new ExprFROM(27, this.exprNOOP, var4, var6);
               var1.appendMainEJBQL(var6 + " ");
         }
      } catch (Exception var8) {
         this.appendCurrTokenIsKeywordMaybe();
         this.exceptionBuff.append(" Error in FROM clause. \n\n Check that the Range Variable Declarations and the Collection Member Declarations are correct \n  and that no EJB QL keywords are being used as: \n\n      Range Variable names,\n\n   or Collection Member names,\n\n   or Abstract Schema names. \n\n");
      }

      return var1;
   }

   public final Expr whereExpr() throws RecognitionException, TokenStreamException {
      ExprWHERE var1 = null;
      Expr var2 = null;
      boolean var3 = false;
      EJBQLToken var4 = null;

      try {
         switch (this.LA(1)) {
            case 1:
            case 10:
            case 23:
            case 24:
            case 26:
            case 28:
               var1 = null;
               break;
            case 22:
               this.match(22);
               var4 = this.addToParserTokenList();
               var3 = true;
               var2 = this.conditionalExpr((Expr)null, false);
               var1 = new ExprWHERE(26, var2);
               var1.appendMainEJBQL(var4);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      } catch (Exception var6) {
         if (var3) {
            this.appendCurrTokenIsKeywordMaybe();
            this.exceptionBuff.append("Error in WHERE clause. \nCheck that no EJB QL keywords are being used as: \n   variable names. \n\n\n");
         }
      }

      return var1;
   }

   public final Expr groupByExpr() throws RecognitionException, TokenStreamException {
      ExprGROUPBY var1 = null;
      Expr var2 = null;
      ExprHAVING var3 = null;
      Vector var4 = null;
      EJBQLToken var5 = null;
      String var6 = null;
      String var7 = null;
      boolean var8 = false;

      try {
         if (this.LA(1) == 26 && this.LA(2) == 25) {
            this.match(26);
            var5 = this.addToParserTokenList(true);
            var6 = var5.getTokenText();
            this.match(25);
            var5 = this.addToParserTokenList(true);
            var6 = var6 + var5.getTokenText();
            var4 = this.groupby_ident_list();
            switch (this.LA(1)) {
               case 27:
                  this.match(27);
                  var5 = this.addToParserTokenList();
                  var7 = var5.getTokenText();
                  var8 = true;
                  var2 = this.logicalOrExpr(true);
                  var3 = new ExprHAVING(69, var2);
                  var3.appendMainEJBQL(var7);
               case 1:
               case 10:
               case 23:
               case 24:
               case 26:
               case 28:
                  var1 = new ExprGROUPBY(68, var3, var4);
                  var1.appendMainEJBQL(var6);
                  break;
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
            }
         } else {
            if (!_tokenSet_0.member(this.LA(1)) || !_tokenSet_1.member(this.LA(2))) {
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            var1 = null;
         }
      } catch (Exception var10) {
         if (var8) {
            this.appendCurrTokenIsKeywordMaybe();
            this.exceptionBuff.append("Error in the HAVING clause. \n   check that no EJB QL keywords are being used as variable names. \n\n");
         }
      }

      return var1;
   }

   public final Expr orderByExpr() throws RecognitionException, TokenStreamException {
      ExprORDERBY var1 = null;
      ExprSIMPLE_QUALIFIER var2 = null;
      Vector var3 = null;
      EJBQLToken var4 = null;
      boolean var5 = false;
      String var6 = null;

      try {
         switch (this.LA(1)) {
            case 1:
            case 28:
               var1 = null;
               break;
            case 23:
               this.match(23);
               var4 = this.addToParserTokenList();
               var5 = true;
               var3 = this.orderby_ident_list();
               var2 = new ExprSIMPLE_QUALIFIER(36, var4.getTokenText());
               var1 = new ExprORDERBY(36, var2, var3);
               var1.appendMainEJBQL(var4);
               break;
            case 24:
               this.match(24);
               var4 = this.addToParserTokenList(true);
               var6 = var4.getTokenText();
               this.match(25);
               var4 = this.addToParserTokenList(true);
               var6 = var6 + var4.getTokenText();
               var3 = this.orderby_ident_list();
               var2 = new ExprSIMPLE_QUALIFIER(36, var4.getTokenText());
               var1 = new ExprORDERBY(36, var2, var3);
               var1.appendMainEJBQL(var6);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      } catch (Exception var8) {
         if (var5) {
            this.exceptionBuff.append(" Possible Error in ORDER BY clause. \n Check that the ORDER BY arguments consists of:\n   pathExprs terminating in a cmp-field name, \n   numeric literals. \n Check that no EJB QL keywords are being used as: \n   arguments. \n\n");
            this.appendCurrTokenIsKeywordMaybe();
         }
      }

      return var1;
   }

   public final Expr selectHintExpr() throws RecognitionException, TokenStreamException {
      Expr var1 = null;
      Object var2 = null;
      EJBQLToken var3 = null;
      ExprSELECT_HINT var4;
      switch (this.LA(1)) {
         case 1:
            var4 = null;
            break;
         case 28:
            this.match(28);
            var3 = this.addToParserTokenList();
            var1 = this.string();
            var4 = new ExprSELECT_HINT(60, (ExprSTRING)var1);
            var4.appendMainEJBQL(var3);
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      return var4;
   }

   public final ExprSELECT selectBody() throws RecognitionException, TokenStreamException {
      ExprSELECT var1 = null;
      Expr var2 = null;
      Vector var3 = new Vector();
      boolean var4 = false;
      EJBQLToken var5 = null;
      String var6 = "";

      try {
         switch (this.LA(1)) {
            case 5:
               this.match(5);
               var5 = this.addToParserTokenList();
               var6 = var5.getTokenText();
               var4 = true;
            case 7:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 62:
            case 63:
               var2 = this.selectTarget();
               var3.addElement(var2);

               while(this.LA(1) == 6) {
                  this.match(6);
                  this.addToParserTokenList();
                  var2 = this.selectTarget();
                  var3.addElement(var2);
               }

               if (var4) {
                  ExprSIMPLE_QUALIFIER var7 = new ExprSIMPLE_QUALIFIER(51, var6);
                  var7.appendMainEJBQL(var6);
                  var1 = new ExprSELECT(34, var7, var3);
               } else {
                  var1 = new ExprSELECT(34, this.exprNOOP, var3);
               }
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      } catch (Exception var8) {
         this.appendCurrTokenIsKeywordMaybe();
         this.exceptionBuff.append("Error detected in SELECT clause.\n\nCheck that no arguments in SELECT clause, or SELECT clause Operators are keywords.\n");
         this.exceptionBuff.append("\nSELECT Targets must be:\n      AGGREGATE(cmp-field or pathExpr terminating in cmp-field): \n          { MIN(f), MAX(f), AVG(f), SUM(f), COUNT(f) },\n\n or   single valued path expression terminating in cmp-field or cmr-field, \n\n or   OBJECT(Identification Variable declared in the FROM Clause). \n\n");
      }

      return var1;
   }

   public final Expr selectTarget() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      Token var3 = null;
      Object var1 = null;
      ExprID var4 = null;
      EJBQLToken var5 = null;
      String var7;
      switch (this.LA(1)) {
         case 7:
            this.match(7);
            var5 = this.addToParserTokenList(false);
            var7 = var5.getTokenText();
            this.match(8);
            this.addToParserTokenList("(", false);
            var2 = this.LT(1);
            this.match(9);
            this.addToParserTokenList(false);
            this.match(10);
            this.addToParserTokenList(")", true);
            var4 = new ExprID(17, var2.getText());
            this.lastIdValue = var2.getText();
            var4.appendMainEJBQL(var2.getText() + " ");
            var1 = new ExprOBJECT(61, var4);
            ((Expr)var1).appendMainEJBQL(var7);
            break;
         case 9:
            var3 = this.LT(1);
            this.match(9);
            this.addToParserTokenList();
            int var6 = BaseExpr.finderStringOrId(var3.getText());
            if (var6 != 17 && var6 != 40) {
               this.reportError(" Error SELECT expected an ID or '@@', but instead got: " + var3.getText());
            }

            if (var6 == 40) {
               var7 = BaseExpr.getSelectCast(var3.getText());
               var4 = new ExprID(17, var7);
               this.lastIdValue = var7;
               var4.appendMainEJBQL(var7 + " ");
               var1 = new ExprOBJECT(61, var4);
               ((Expr)var1).appendMainEJBQL("@@");
            } else {
               var1 = new ExprID(17, var3.getText());
               this.lastIdValue = var3.getText();
               ((Expr)var1).appendMainEJBQL(var3.getText() + " ");
            }
            break;
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
            var1 = this.aggregateTarget();
            break;
         case 62:
         case 63:
            var1 = this.case_function(false);
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      return (Expr)var1;
   }

   public final ExprAGGREGATE aggregateTarget() throws RecognitionException, TokenStreamException {
      ExprAGGREGATE var1 = null;
      switch (this.LA(1)) {
         case 11:
            this.match(11);
            this.addToParserTokenList(false);
            var1 = this.aggregateWrap(44, this.getTokenText());
            break;
         case 12:
            this.match(12);
            this.addToParserTokenList(false);
            var1 = this.aggregateWrap(45, this.getTokenText());
            break;
         case 13:
            this.match(13);
            this.addToParserTokenList(false);
            var1 = this.aggregateWrap(46, this.getTokenText());
            break;
         case 14:
            this.match(14);
            this.addToParserTokenList(false);
            var1 = this.aggregateWrap(47, this.getTokenText());
            break;
         case 15:
            this.match(15);
            this.addToParserTokenList(false);
            var1 = this.aggregateCountWrap(48, this.getTokenText());
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      return var1;
   }

   public final Expr case_function(boolean var1) throws RecognitionException, TokenStreamException {
      ExprCASE var2 = null;
      String var3 = null;
      EJBQLToken var4 = null;

      try {
         Expr var7;
         switch (this.LA(1)) {
            case 62:
               this.match(62);
               var3 = "UPPER";
               var4 = this.addToParserTokenList();
               this.match(8);
               this.addToParserTokenList();
               if (var1) {
                  var7 = this.case_value_order_group_by();
               } else {
                  var7 = this.case_value();
               }

               this.match(10);
               this.addToParserTokenList();
               var2 = new ExprCASE(70, var7);
               var2.appendMainEJBQL(var4);
               break;
            case 63:
               this.match(63);
               var3 = "LOWER";
               var4 = this.addToParserTokenList();
               this.match(8);
               this.addToParserTokenList();
               if (var1) {
                  var7 = this.case_value_order_group_by();
               } else {
                  var7 = this.case_value();
               }

               this.match(10);
               this.addToParserTokenList();
               var2 = new ExprCASE(71, var7);
               var2.appendMainEJBQL(var4);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      } catch (Exception var6) {
         if (var3.equals("UPPER")) {
            this.exceptionBuff.append(" Error in UPPER(String) function. \n");
         } else if (var3.equals("LOWER")) {
            this.exceptionBuff.append(" Error in LOWER(String) function. \n");
         }

         this.exceptionBuff.append(" Check for proper syntax. \n Check that none of the arguments are EJB QL keywords. \n");
      }

      return var2;
   }

   public final ExprAGGREGATE aggregateWrap(int var1, String var2) throws RecognitionException, TokenStreamException {
      ExprAGGREGATE var3 = null;
      Expr var4 = null;
      ExprSIMPLE_QUALIFIER var5 = null;
      ExprID var6 = null;
      boolean var7 = false;
      boolean var8 = false;
      EJBQLToken var9 = null;
      String var10 = "";
      this.match(8);
      this.addToParserTokenList();
      switch (this.LA(1)) {
         case 5:
            this.match(5);
            var9 = this.addToParserTokenList();
            var10 = var9.getTokenText();
            var7 = true;
         case 9:
         case 16:
            switch (this.LA(1)) {
               case 16:
                  this.match(16);
                  var9 = this.addToParserTokenList();
                  var10 = var9.getTokenText();
                  var8 = true;
               case 9:
                  var4 = this.id();
                  this.match(10);
                  this.addToParserTokenList();
                  var6 = (ExprID)var4;
                  if (var7) {
                     var5 = new ExprSIMPLE_QUALIFIER(51, var10);
                     var5.appendMainEJBQL(var10);
                     var3 = new ExprAGGREGATE(var1, var6, var5);
                  } else if (var8) {
                     var5 = new ExprSIMPLE_QUALIFIER(49, var10);
                     var5.appendMainEJBQL(var10);
                     var3 = new ExprAGGREGATE(var1, var6, var5);
                  } else {
                     var3 = new ExprAGGREGATE(var1, var6);
                  }

                  var3.appendMainEJBQL(var2);
                  return var3;
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
            }
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final ExprAGGREGATE aggregateCountWrap(int var1, String var2) throws RecognitionException, TokenStreamException {
      ExprAGGREGATE var3 = null;
      ExprSIMPLE_QUALIFIER var4 = null;
      ExprSIMPLE_QUALIFIER var5 = null;
      Expr var6 = null;
      ExprID var7 = null;
      EJBQLToken var8 = null;
      boolean var9 = false;
      boolean var10 = false;
      boolean var11 = false;
      String var12 = "";
      this.match(8);
      this.addToParserTokenList();
      switch (this.LA(1)) {
         case 5:
            this.match(5);
            var8 = this.addToParserTokenList();
            var12 = var8.getTokenText();
            var9 = true;
            var5 = new ExprSIMPLE_QUALIFIER(51, var12);
            var5.appendMainEJBQL(var12);
         case 9:
         case 16:
         case 17:
            switch (this.LA(1)) {
               case 16:
                  this.match(16);
                  var8 = this.addToParserTokenList();
                  var12 = var8.getTokenText();
                  var10 = true;
                  var5 = new ExprSIMPLE_QUALIFIER(49, var12);
                  var5.appendMainEJBQL(var12);
               case 9:
               case 17:
                  switch (this.LA(1)) {
                     case 9:
                        var6 = this.id();
                        var7 = (ExprID)var6;
                        break;
                     case 17:
                        this.match(17);
                        this.addToParserTokenList();
                        var4 = new ExprSIMPLE_QUALIFIER(50, "*");
                        var4.appendMainEJBQL("*");
                        var11 = true;
                        break;
                     default:
                        throw new NoViableAltException(this.LT(1), this.getFilename());
                  }

                  this.match(10);
                  this.addToParserTokenList();
                  if (var11) {
                     var3 = new ExprAGGREGATE(var1, var7);
                  } else if (var9) {
                     var3 = new ExprAGGREGATE(var1, var7, var5);
                  } else if (var10) {
                     var3 = new ExprAGGREGATE(var1, var7, var5);
                  } else {
                     var3 = new ExprAGGREGATE(var1, var7);
                  }

                  var3.appendMainEJBQL(var2);
                  return var3;
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
            }
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final Expr id() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      ExprID var1 = null;
      EJBQLToken var3 = null;
      var2 = this.LT(1);
      this.match(9);
      var3 = this.addToParserTokenList(false);
      String var4 = var3.getTokenText();
      int var5 = BaseExpr.finderStringOrId(var4);
      if (var5 != 17) {
         this.reportError(" Error expected an ID, but instead got: " + var4);
      }

      var1 = new ExprID(17, var4);
      this.lastIdValue = var4;
      var1.appendMainEJBQL(var4 + " ");
      return var1;
   }

   public final Expr fromTarget() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      Token var3 = null;
      Token var4 = null;
      Token var5 = null;
      Token var6 = null;
      Token var7 = null;
      Token var8 = null;
      Object var1 = null;
      EJBQLToken var11 = null;
      String var13 = "";
      String var14 = null;
      ExprID var9;
      ExprID var10;
      int var12;
      switch (this.LA(1)) {
         case 9:
            var4 = this.LT(1);
            this.match(9);
            var11 = this.addToParserTokenList();
            var9 = new ExprID(17, var4.getText());
            this.lastIdValue = var4.getText();
            var9.appendMainEJBQL(var11);
            var12 = BaseExpr.finderStringOrId(var4.getText());
            if (var12 != 17) {
               this.reportError(" Error FOR expected an ID, but instead got: " + var4.getText());
            }

            switch (this.LA(1)) {
               case 9:
                  var8 = this.LT(1);
                  this.match(9);
                  var11 = this.addToParserTokenList();
                  var12 = BaseExpr.finderStringOrId(var8.getText());
                  if (var12 == 35) {
                     this.reportError(" Error FOR expected an ID, but instead got: " + var8.getText());
                  }

                  var10 = new ExprID(17, var8.getText());
                  this.lastIdValue = var8.getText();
                  var10.appendMainEJBQL(var11);
                  var1 = new ExprRANGE_VARIABLE(74, var9, var10);
                  return (Expr)var1;
               case 19:
                  this.match(19);
                  var11 = this.addToParserTokenList();
                  var13 = var11.getTokenText();
                  var6 = this.LT(1);
                  this.match(9);
                  var11 = this.addToParserTokenList();
                  var12 = BaseExpr.finderStringOrId(var6.getText());
                  if (var12 != 17) {
                     this.reportError(" Error FOR expected an ID, but instead got: " + var6.getText());
                  }

                  var10 = new ExprID(17, var6.getText());
                  this.lastIdValue = var6.getText();
                  var10.appendMainEJBQL(var11);
                  var1 = new ExprCORR_IN(28, var9, var10, var13);
                  ((Expr)var1).appendMainEJBQL(var13);
                  return (Expr)var1;
               case 20:
                  this.match(20);
                  var11 = this.addToParserTokenList();
                  var13 = var11.getTokenText();
                  var7 = this.LT(1);
                  this.match(9);
                  var11 = this.addToParserTokenList();
                  var12 = BaseExpr.finderStringOrId(var7.getText());
                  if (var12 != 17) {
                     this.reportError(" Error FOR expected an ID, but instead got: " + var7.getText());
                  }

                  var10 = new ExprID(17, var7.getText());
                  this.lastIdValue = var7.getText();
                  var10.appendMainEJBQL(var11);
                  var1 = new ExprRANGE_VARIABLE(74, var9, var10);
                  ((Expr)var1).appendMainEJBQL(var13);
                  return (Expr)var1;
               case 21:
                  this.match(21);
                  var11 = this.addToParserTokenList();
                  var13 = var11.getTokenText();
                  var5 = this.LT(1);
                  this.match(9);
                  var11 = this.addToParserTokenList();
                  var12 = BaseExpr.finderStringOrId(var5.getText());
                  if (var12 != 17) {
                     this.reportError(" Error FOR expected an ID, but instead got: " + var5.getText());
                  }

                  var10 = new ExprID(17, var5.getText());
                  this.lastIdValue = var5.getText();
                  var10.appendMainEJBQL(var11);
                  var1 = new ExprCORR_FOR(29, var9, var10, var13);
                  ((Expr)var1).appendMainEJBQL(var13);
                  return (Expr)var1;
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
            }
         case 19:
            this.match(19);
            var11 = this.addToParserTokenList();
            var13 = var11.getTokenText();
            this.match(8);
            this.addToParserTokenList();
            var2 = this.LT(1);
            this.match(9);
            this.addToParserTokenList();
            this.match(10);
            this.addToParserTokenList();
            switch (this.LA(1)) {
               case 20:
                  this.match(20);
                  var11 = this.addToParserTokenList();
                  var14 = var11.getTokenText();
               case 9:
                  var3 = this.LT(1);
                  this.match(9);
                  this.addToParserTokenList();
                  var12 = BaseExpr.finderStringOrId(var2.getText());
                  if (var12 != 17) {
                     this.reportError(" Error FOR expected an ID, but instead got: " + var2.getText());
                  }

                  var12 = BaseExpr.finderStringOrId(var3.getText());
                  if (var12 != 17) {
                     this.reportError(" Error FOR expected an ID, but instead got: " + var3.getText());
                  }

                  var9 = new ExprID(17, var2.getText());
                  this.lastIdValue = var2.getText();
                  var9.prependPreEJBQL("(");
                  var9.appendMainEJBQL(var2.getText());
                  var9.appendPostEJBQL(")");
                  var10 = new ExprID(17, var3.getText());
                  this.lastIdValue = var2.getText();
                  if (var14 != null) {
                     var10.prependPreEJBQL(" " + var14 + " ");
                  }

                  var10.appendMainEJBQL(var3.getText() + " ");
                  var1 = new ExprCOLLECTION_MEMBER(73, var9, var10, var13);
                  ((Expr)var1).appendMainEJBQL(var13);
                  return (Expr)var1;
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
            }
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final Expr conditionalExpr(Expr var1, boolean var2) throws RecognitionException, TokenStreamException {
      Expr var3 = null;
      Object var4 = null;
      EJBQLToken var5 = null;
      switch (this.LA(1)) {
         case 1:
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
         case 19:
         case 23:
         case 24:
         case 26:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 42:
         case 43:
         case 47:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
            var3 = this.logicalOrExpr(var2);
            if (var1 != null) {
               this.exceptionBuff.append(" The qualifiers: ANY, ALL, EXISTS  may only be used with a SubQuery\n\n");
            }
            break;
         case 2:
         case 3:
         case 5:
         case 6:
         case 7:
         case 18:
         case 20:
         case 21:
         case 22:
         case 25:
         case 27:
         case 40:
         case 41:
         case 44:
         case 45:
         case 46:
         case 48:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 4:
            this.match(4);
            var5 = this.addToParserTokenList();
            String var6 = var5.getTokenText();
            var3 = this.subqueryBody(var1, var6);
      }

      return var3;
   }

   public final Vector orderby_ident_list() throws RecognitionException, TokenStreamException {
      Vector var1 = new Vector();
      Expr var2 = null;
      EJBQLToken var3 = null;
      ExprSIMPLE_QUALIFIER var4 = null;
      switch (this.LA(1)) {
         case 9:
            var2 = this.id();
            break;
         case 57:
            var2 = this.number();
            break;
         case 62:
         case 63:
            var2 = this.case_function(true);
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      String var5;
      switch (this.LA(1)) {
         case 45:
            this.match(45);
            var3 = this.addToParserTokenList(false);
            var5 = var3.getTokenText();
            var4 = new ExprSIMPLE_QUALIFIER(66, var5);
            var4.appendMainEJBQL(var5);
         case 1:
         case 6:
         case 28:
         case 46:
            switch (this.LA(1)) {
               case 46:
                  this.match(46);
                  var3 = this.addToParserTokenList();
                  var5 = var3.getTokenText();
                  var4 = new ExprSIMPLE_QUALIFIER(67, var5);
                  var4.appendMainEJBQL(var5);
               case 1:
               case 6:
               case 28:
                  ExprORDERBY_ELEMENT var6 = new ExprORDERBY_ELEMENT(52, var2, var4);
                  var1.addElement(var6);
                  var4 = null;

                  while(this.LA(1) == 6) {
                     this.match(6);
                     this.addToParserTokenList();
                     switch (this.LA(1)) {
                        case 9:
                           var2 = this.id();
                           break;
                        case 57:
                           var2 = this.number();
                           break;
                        case 62:
                        case 63:
                           var2 = this.case_function(true);
                           break;
                        default:
                           throw new NoViableAltException(this.LT(1), this.getFilename());
                     }

                     switch (this.LA(1)) {
                        case 1:
                        case 6:
                        case 28:
                        case 46:
                           break;
                        case 45:
                           this.match(45);
                           var3 = this.addToParserTokenList();
                           var5 = var3.getTokenText();
                           var4 = new ExprSIMPLE_QUALIFIER(66, var5);
                           var4.appendMainEJBQL(var5);
                           break;
                        default:
                           throw new NoViableAltException(this.LT(1), this.getFilename());
                     }

                     switch (this.LA(1)) {
                        case 46:
                           this.match(46);
                           var3 = this.addToParserTokenList();
                           var5 = var3.getTokenText();
                           var4 = new ExprSIMPLE_QUALIFIER(67, var5);
                           var4.appendMainEJBQL(var5);
                        case 1:
                        case 6:
                        case 28:
                           var6 = new ExprORDERBY_ELEMENT(52, var2, var4);
                           var1.addElement(var6);
                           var4 = null;
                           break;
                        default:
                           throw new NoViableAltException(this.LT(1), this.getFilename());
                     }
                  }

                  return var1;
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
            }
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final Vector groupby_ident_list() throws RecognitionException, TokenStreamException {
      Vector var1 = new Vector();
      Expr var2 = null;

      try {
         switch (this.LA(1)) {
            case 9:
               var2 = this.id();
               break;
            case 57:
               var2 = this.number();
               break;
            case 62:
            case 63:
               var2 = this.case_function(true);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         var1.addElement(var2);

         for(; this.LA(1) == 6; var1.addElement(var2)) {
            this.match(6);
            this.addToParserTokenList();
            switch (this.LA(1)) {
               case 9:
                  var2 = this.id();
                  break;
               case 57:
                  var2 = this.number();
                  break;
               case 62:
               case 63:
                  var2 = this.case_function(true);
                  break;
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
            }
         }
      } catch (Exception var4) {
         this.appendCurrTokenIsKeywordMaybe();
         this.exceptionBuff.append(" Error in GROUP BY expression. \n Check that the GROUP BY arguments are: \n   numeric literals, \n   pathExprs terminating in cmp-fields \n  \n Check that none of the GROUP BY arguments are EJB QL keywords \n\n");
         this.appendCurrTokenIsKeywordMaybe();
      }

      return var1;
   }

   public final Expr logicalOrExpr(boolean var1) throws RecognitionException, TokenStreamException {
      Object var2 = null;
      Expr var3 = null;
      EJBQLToken var4 = null;
      var2 = this.logicalAndExpr(var1);

      while(this.LA(1) == 29) {
         this.match(29);
         var4 = this.addToParserTokenList();
         var3 = this.logicalAndExpr(var1);
         var2 = new ExprOR(1, (Expr)var2, var3);
         ((Expr)var2).appendMainEJBQL(var4);
      }

      return (Expr)var2;
   }

   public final Expr string() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      ExprSTRING var1 = null;
      EJBQLToken var3 = null;
      var2 = this.LT(1);
      this.match(54);
      var3 = this.addToParserTokenList(false);
      String var4 = var3.getTokenText();
      var1 = new ExprSTRING(18, var4);
      var1.appendMainEJBQL(var4 + " ");
      return var1;
   }

   public final Expr subqueryBody(Expr var1, String var2) throws RecognitionException, TokenStreamException {
      Object var3 = null;

      try {
         var3 = this.selectBody();
         ((Expr)var3).appendMainEJBQL(var2);
         Expr var4 = null;
         Expr var5 = null;
         ExprFROM var6 = null;
         Object var7 = null;
         Vector var8 = new Vector();
         if (var1 != null) {
            var8.addElement(var1);
         }

         var8.addElement(var3);
         var6 = this.fromExpr();
         if (var6 != null) {
            var8.addElement(var6);
         }

         var4 = this.whereExpr();
         if (var4 != null) {
            var8.addElement(var4);
         }

         var5 = this.groupByExpr();
         if (var5 != null) {
            var8.addElement(var5);
         }

         int var9 = var8.size();
         Enumeration var10 = var8.elements();
         if (var9 == 5) {
            var3 = new ExprSUBQUERY(43, new ExprINTEGER(19, Integer.toString(this.nextSubQuery++)), (Expr)var10.nextElement(), (Expr)var10.nextElement(), (Expr)var10.nextElement(), (Expr)var10.nextElement(), (Expr)var10.nextElement());
         } else if (var9 == 4) {
            var3 = new ExprSUBQUERY(43, new ExprINTEGER(19, Integer.toString(this.nextSubQuery++)), (Expr)var10.nextElement(), (Expr)var10.nextElement(), (Expr)var10.nextElement(), (Expr)var10.nextElement());
         } else if (var9 == 3) {
            var3 = new ExprSUBQUERY(43, new ExprINTEGER(19, Integer.toString(this.nextSubQuery++)), (Expr)var10.nextElement(), (Expr)var10.nextElement(), (Expr)var10.nextElement());
         } else if (var9 == 2) {
            var3 = new ExprSUBQUERY(43, new ExprINTEGER(19, Integer.toString(this.nextSubQuery++)), (Expr)var10.nextElement(), (Expr)var10.nextElement());
         } else if (var9 == 1) {
            var3 = new ExprSUBQUERY(43, new ExprINTEGER(19, Integer.toString(this.nextSubQuery++)), (Expr)var10.nextElement());
         } else {
            this.reportError("Expected 1-5 EJB QL SubQuery Elements: [{ANY, ALL, EXISTS}] SELECT, FROM, [WHERE] [GROUP BY] instead got: " + var9);
         }
      } catch (Exception var11) {
         this.exceptionBuff.append(" Error in Subquery expression. \n\n");
         this.appendCurrTokenIsKeywordMaybe();
      }

      return (Expr)var3;
   }

   public final Expr subqueryExpr(Expr var1) throws RecognitionException, TokenStreamException {
      Expr var2 = null;
      EJBQLToken var3 = null;
      this.match(4);
      var3 = this.addToParserTokenList();
      String var4 = var3.getTokenText();
      var2 = this.subqueryBody(var1, var4);
      return var2;
   }

   public final Expr logicalAndExpr(boolean var1) throws RecognitionException, TokenStreamException {
      Object var2 = null;
      Expr var3 = null;
      EJBQLToken var4 = null;
      var2 = this.logicalNotExpr(var1);

      while(this.LA(1) == 30) {
         this.match(30);
         var4 = this.addToParserTokenList();
         var3 = this.logicalNotExpr(var1);
         var2 = new ExprAND(0, (Expr)var2, var3);
         ((Expr)var2).appendMainEJBQL(var4);
      }

      return (Expr)var2;
   }

   public final Expr logicalNotExpr(boolean var1) throws RecognitionException, TokenStreamException {
      Object var2 = null;
      ExprEXISTS var3 = null;
      byte var4 = 0;
      EJBQLToken var5 = null;
      EJBQLToken var6 = null;
      Object var7 = null;
      String var8 = null;
      if (this.LA(1) == 31 && _tokenSet_2.member(this.LA(2))) {
         this.match(31);
         var5 = this.addToParserTokenList();
         var4 = 2;
      } else if (!_tokenSet_2.member(this.LA(1)) || !_tokenSet_3.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (this.LA(1) == 32 && this.LA(2) == 8) {
         this.match(32);
         var6 = this.addToParserTokenList();
         var8 = var6.getTokenText();
         this.match(8);
         this.addToParserTokenList();
         var6 = this.addToParserTokenList();
         boolean var9 = false;
         if (var4 == 2) {
            var9 = true;
         }

         var3 = new ExprEXISTS(65, var9);
         var3.appendMainEJBQL(var5 + " " + var8);
         var2 = this.subqueryExpr(var3);
         this.match(10);
         this.addToParserTokenList();
         ((Expr)var2).prependPreEJBQL("( ");
         ((Expr)var2).appendPostEJBQL(") ");
      } else {
         if (!_tokenSet_2.member(this.LA(1)) || !_tokenSet_3.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         var2 = this.relationalExpr(var1);
         if (var4 != 0) {
            var2 = new ExprNOT(var4, (Expr)var2);
            ((Expr)var2).appendMainEJBQL(var5);
         }
      }

      return (Expr)var2;
   }

   public final Expr relationalExpr(boolean var1) throws RecognitionException, TokenStreamException {
      Object var2 = null;
      Expr var3 = null;
      Object var4 = null;
      boolean var5 = false;
      EJBQLToken var7 = null;
      String var8 = null;

      try {
         var2 = this.additiveExpr(var1);
         if (var2 == null) {
            throw new Exception("An unexpected variable or operator was encountered in the EJB QL Query");
         } else {
            while(true) {
               while(true) {
                  switch (this.LA(1)) {
                     case 19:
                     case 31:
                     case 42:
                     case 43:
                     case 47:
                        switch (this.LA(1)) {
                           case 31:
                              this.match(31);
                              this.addToParserTokenList();
                              var5 = true;
                           case 19:
                           case 42:
                           case 43:
                           case 47:
                              switch (this.LA(1)) {
                                 case 19:
                                    var2 = this.inExpr((Expr)var2, var5);
                                    continue;
                                 case 42:
                                    var2 = this.betweenExpr((Expr)var2, var5, var1);
                                    continue;
                                 case 43:
                                    var2 = this.likeExpr((Expr)var2, var5);
                                    continue;
                                 case 47:
                                    var2 = this.memberExpr((Expr)var2, var5);
                                    continue;
                                 default:
                                    throw new NoViableAltException(this.LT(1), this.getFilename());
                              }
                           default:
                              throw new NoViableAltException(this.LT(1), this.getFilename());
                        }
                     case 20:
                     case 21:
                     case 22:
                     case 23:
                     case 24:
                     case 25:
                     case 26:
                     case 27:
                     case 28:
                     case 29:
                     case 30:
                     case 32:
                     case 40:
                     case 41:
                     case 44:
                     case 45:
                     case 46:
                     default:
                        return (Expr)var2;
                     case 33:
                     case 34:
                     case 35:
                     case 36:
                     case 37:
                     case 38:
                        byte var6;
                        switch (this.LA(1)) {
                           case 33:
                              this.match(33);
                              var7 = this.addToParserTokenList();
                              var8 = var7.getTokenText();
                              var6 = 10;
                              break;
                           case 34:
                              this.match(34);
                              var7 = this.addToParserTokenList();
                              var8 = var7.getTokenText();
                              var6 = 5;
                              break;
                           case 35:
                              this.match(35);
                              var7 = this.addToParserTokenList();
                              var8 = var7.getTokenText();
                              var6 = 6;
                              break;
                           case 36:
                              this.match(36);
                              var7 = this.addToParserTokenList();
                              var8 = var7.getTokenText();
                              var6 = 7;
                              break;
                           case 37:
                              this.match(37);
                              var7 = this.addToParserTokenList();
                              var8 = var7.getTokenText();
                              var6 = 8;
                              break;
                           case 38:
                              this.match(38);
                              var7 = this.addToParserTokenList();
                              var8 = var7.getTokenText();
                              var6 = 9;
                              break;
                           default:
                              throw new NoViableAltException(this.LT(1), this.getFilename());
                        }

                        var3 = this.additiveExpr(var1);
                        if (var3 == null) {
                           throw new Exception("An unexpected variable or operator was encountered in the EJB QL Query");
                        }

                        if (var6 == 10) {
                           var2 = new ExprNOT_EQUAL(var6, (Expr)var2, var3);
                        } else if (var6 == 5) {
                           var2 = new ExprEQUAL(var6, (Expr)var2, var3);
                        } else {
                           var2 = new ExprSIMPLE_TWO_TERM(var6, (Expr)var2, var3);
                        }

                        ((Expr)var2).appendMainEJBQL(var8);
                        break;
                     case 39:
                        this.match(39);
                        var7 = this.addToParserTokenList();
                        var8 = this.getTokenText();
                        switch (this.LA(1)) {
                           case 31:
                              this.match(31);
                              var7 = this.addToParserTokenList();
                              var8 = var8 + " " + var7.getTokenText();
                              var5 = true;
                           case 40:
                           case 41:
                              switch (this.LA(1)) {
                                 case 40:
                                    this.match(40);
                                    var7 = this.addToParserTokenList();
                                    var8 = var8 + " " + var7.getTokenText();
                                    if (var5) {
                                       var2 = new ExprISNULL(4, (Expr)var2, false);
                                       ((Expr)var2).appendMainEJBQL(var8);
                                    } else {
                                       var2 = new ExprISNULL(3, (Expr)var2, true);
                                       ((Expr)var2).appendMainEJBQL(var8);
                                    }
                                    continue;
                                 case 41:
                                    this.match(41);
                                    var7 = this.addToParserTokenList();
                                    var8 = var8 + " " + var7.getTokenText();
                                    if (var5) {
                                       var2 = new ExprISEMPTY(42, (Expr)var2, false);
                                       ((Expr)var2).appendMainEJBQL(var8);
                                    } else {
                                       var2 = new ExprISEMPTY(41, (Expr)var2, true);
                                       ((Expr)var2).appendMainEJBQL(var8);
                                    }
                                    continue;
                                 default:
                                    throw new NoViableAltException(this.LT(1), this.getFilename());
                              }
                           default:
                              throw new NoViableAltException(this.LT(1), this.getFilename());
                        }
                  }
               }
            }
         }
      } catch (Exception var10) {
         this.exceptionBuff.append(var10.getMessage());
         return (Expr)var2;
      }
   }

   public final Expr additiveExpr(boolean var1) throws RecognitionException, TokenStreamException {
      Object var2 = null;
      Expr var3 = null;
      String var5 = "";
      var2 = this.multiplicativeExpr(var1);

      while(this.LA(1) == 49 || this.LA(1) == 50) {
         byte var4;
         switch (this.LA(1)) {
            case 49:
               this.match(49);
               this.addToParserTokenList();
               var4 = 22;
               var5 = "+";
               break;
            case 50:
               this.match(50);
               this.addToParserTokenList();
               var4 = 21;
               var5 = "-";
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         var3 = this.multiplicativeExpr(var1);
         var2 = new ExprSIMPLE_ARITH(var4, (Expr)var2, var3);
         ((Expr)var2).appendMainEJBQL(var5 + " ");
      }

      return (Expr)var2;
   }

   public final Expr betweenExpr(Expr var1, boolean var2, boolean var3) throws RecognitionException, TokenStreamException {
      Object var4 = null;
      Object var5 = null;
      Expr var6 = null;
      Expr var7 = null;
      EJBQLToken var8 = null;
      EJBQLToken var9 = null;

      try {
         this.match(42);
         var8 = this.addToParserTokenList();
         var6 = this.additiveExpr(var3);
         this.match(30);
         var9 = this.addToParserTokenList();
         var7 = this.additiveExpr(var3);
         if (var2) {
            var4 = new ExprNOT_BETWEEN(77, var1, var6, var7);
            ((Expr)var4).appendMainEJBQL(var8.getTokenText());
         } else {
            var4 = new ExprBETWEEN(12, var1, var6, var7);
            ((Expr)var4).appendMainEJBQL(var8.getTokenText());
         }

         ((Expr)var4).appendPostEJBQL(var9.getTokenText());
      } catch (Exception var11) {
         this.exceptionBuff.append("Error in 'BETWEEN' expression. \n  Check the BETWEEN syntax:  expr1 [NOT] BETWEEN expr2 AND expr3 \n  Check that no EJB QL keywords are being used as arguments: expr1, expr2 or expr3. \n\n");
         this.appendCurrTokenIsKeywordMaybe();
      }

      return (Expr)var4;
   }

   public final Expr likeExpr(Expr var1, boolean var2) throws RecognitionException, TokenStreamException {
      ExprLIKE var3 = null;
      Expr var4 = null;
      Expr var5 = null;
      EJBQLToken var6 = null;
      boolean var7 = false;
      String var8 = "";

      try {
         this.match(43);
         var6 = this.addToParserTokenList();
         var7 = true;
         var4 = this.like_value();
         var5 = this.escapeExpr();
         var3 = new ExprLIKE(11, var1, var4, var5, var2);
         if (var2) {
            var3.appendMainEJBQL("NOT ");
         }

         var3.appendMainEJBQL(var6);
      } catch (Exception var10) {
         if (var7) {
            this.appendCurrTokenIsKeywordMaybe();
            this.exceptionBuff.append("Error in 'LIKE' expression. \nCheck the arguments to the 'LIKE' expression.  \n  The arguments are allowed to be: \n     String Literal. \n     Input Parameter. \nCheck that none of the arguments are EJB QL keywords. \n\n");
         }
      }

      return var3;
   }

   public final Expr inExpr(Expr var1, boolean var2) throws RecognitionException, TokenStreamException {
      Object var3 = null;
      EJBQLToken var4 = null;
      Vector var5 = null;
      String var6 = null;
      String var7 = null;

      try {
         this.match(19);
         var4 = this.addToParserTokenList();
         var6 = var4.getTokenText();
         this.match(8);
         this.addToParserTokenList();
         switch (this.LA(1)) {
            case 4:
               this.match(4);
               var4 = this.addToParserTokenList();
               var7 = var4.getTokenText();
               Expr var10 = this.subqueryBody((Expr)null, var7);
               var3 = new ExprIN_SUBQUERY(13, var1, var10, var2);
               if (var2) {
                  ((Expr)var3).appendMainEJBQL("NOT ");
               }

               ((Expr)var3).appendMainEJBQL(var6);
               break;
            case 49:
            case 50:
            case 53:
            case 54:
            case 57:
               var5 = this.in_rhs();
               var3 = new ExprIN(13, var1, var5, var2);
               if (var2) {
                  ((Expr)var3).appendMainEJBQL("NOT ");
               }

               ((Expr)var3).appendMainEJBQL(var6);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.match(10);
         this.addToParserTokenList();
      } catch (Exception var9) {
         this.appendCurrTokenIsKeywordMaybe();
         this.exceptionBuff.append("Error in 'IN' expression. \n\n");
      }

      return (Expr)var3;
   }

   public final Expr memberExpr(Expr var1, boolean var2) throws RecognitionException, TokenStreamException {
      Token var4 = null;
      ExprISMEMBER var3 = null;
      ExprID var5 = null;
      EJBQLToken var6 = null;
      String var7 = null;
      String var8 = "";
      this.match(47);
      var6 = this.addToParserTokenList();
      var7 = var6.getTokenText();
      switch (this.LA(1)) {
         case 48:
            this.match(48);
            var6 = this.addToParserTokenList();
            var8 = var6.getTokenText();
         case 9:
            try {
               var4 = this.LT(1);
               this.match(9);
            } catch (Exception var10) {
               this.appendCurrTokenIsKeywordMaybe();
               this.exceptionBuff.append(" Error in [NOT] MEMBER OF expression. \n Check that the MEMBER OF target is a Collection Valued Path Expr \n  and that no EJB QL keywords are being used as: \n      arguments. \n\n");
            }

            var6 = this.addToParserTokenList();
            int var9 = BaseExpr.finderStringOrId(var4.getText());
            if (var9 != 17) {
               this.reportError(" Error MEMBER [OF] expected an ID, but instead got: " + var4.getText());
            }

            var5 = new ExprID(17, var4.getText());
            this.lastIdValue = var4.getText();
            var5.appendMainEJBQL(var6);
            if (var2) {
               var3 = new ExprISMEMBER(63, var1, var5, false);
               var3.appendMainEJBQL("NOT " + var7 + var8);
            } else {
               var3 = new ExprISMEMBER(62, var1, var5, true);
               var3.appendMainEJBQL(var7 + var8);
            }

            return var3;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final Expr like_value() throws RecognitionException, TokenStreamException {
      Expr var1 = null;
      switch (this.LA(1)) {
         case 1:
         case 10:
         case 19:
         case 23:
         case 24:
         case 26:
         case 28:
         case 29:
         case 30:
         case 31:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 42:
         case 43:
         case 44:
         case 47:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
            var1 = this.function();
            break;
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 20:
         case 21:
         case 22:
         case 25:
         case 27:
         case 32:
         case 40:
         case 41:
         case 45:
         case 46:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 55:
         case 56:
         case 57:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 53:
            var1 = this.variable();
            break;
         case 54:
            var1 = this.string();
      }

      return var1;
   }

   public final Expr escapeExpr() throws RecognitionException, TokenStreamException {
      ExprESCAPE var1 = null;
      EJBQLToken var2 = null;
      boolean var3 = false;

      try {
         switch (this.LA(1)) {
            case 1:
            case 10:
            case 19:
            case 23:
            case 24:
            case 26:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 42:
            case 43:
            case 47:
               var1 = null;
               break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 20:
            case 21:
            case 22:
            case 25:
            case 27:
            case 32:
            case 40:
            case 41:
            case 45:
            case 46:
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            case 44:
               this.match(44);
               var2 = this.addToParserTokenList();
               var3 = true;
               Expr var6 = this.like_value();
               var1 = new ExprESCAPE(75, var6);
               var1.appendMainEJBQL(var2);
         }
      } catch (Exception var5) {
         if (var3) {
            this.appendCurrTokenIsKeywordMaybe();
            this.exceptionBuff.append("Error in 'ESCAPE' expression. \nCheck the arguments to the 'ESCAPE' expression.  \n  The arguments are allowed to be: \n     String Literal. \n     Input Parameter. \nCheck that none of the arguments are EJB QL keywords. \n\n");
         }
      }

      return var1;
   }

   public final Vector in_rhs() throws RecognitionException, TokenStreamException {
      Object var2 = null;
      Object var3 = null;
      new Vector();
      Vector var1 = this.in_list();
      return var1;
   }

   public final Vector in_list() throws RecognitionException, TokenStreamException {
      Vector var1 = new Vector();
      Expr var2 = null;

      try {
         var2 = this.in_value();
         var1.addElement(var2);

         while(this.LA(1) == 6) {
            this.match(6);
            this.addToParserTokenList();
            var2 = this.in_value();
            var1.addElement(var2);
         }
      } catch (Exception var4) {
         this.appendCurrTokenIsKeywordMaybe();
         this.exceptionBuff.append(" Error in the IN expression. \n Check that the IN (   ) arguments are: \n   String Literals \n   Input Parameters \n  \n Check that none of the IN (  ) arguments are EJB QL keywords \n\n");
      }

      return var1;
   }

   public final Expr number() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      Object var1 = null;
      EJBQLToken var4 = null;
      var2 = this.LT(1);
      this.match(57);
      var4 = this.addToParserTokenList(false);
      String var5 = var4.getTokenText();
      int var3 = BaseExpr.numType(var5);
      if (var3 == 19) {
         var1 = new ExprINTEGER(var3, var5);
         ((Expr)var1).appendMainEJBQL(var5 + " ");
      } else if (var3 == 20) {
         var1 = new ExprFLOAT(var3, var5);
         ((Expr)var1).appendMainEJBQL(var5 + " ");
      } else {
         this.reportError(" Error expected expected INTEGER or FLOAT, instead got: " + BaseExpr.getTypeName(var3));
      }

      return (Expr)var1;
   }

   public final Expr in_value() throws RecognitionException, TokenStreamException {
      Expr var1 = null;
      switch (this.LA(1)) {
         case 49:
         case 50:
         case 57:
            var1 = this.numeric_literal();
            break;
         case 51:
         case 52:
         case 55:
         case 56:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 53:
            var1 = this.variable();
            break;
         case 54:
            var1 = this.string();
      }

      return var1;
   }

   public final Expr multiplicativeExpr(boolean var1) throws RecognitionException, TokenStreamException {
      Object var2 = null;
      Expr var3 = null;
      EJBQLToken var5 = null;
      String var6 = "";
      var2 = this.prefixExpr(var1);

      while(this.LA(1) == 17 || this.LA(1) == 51) {
         byte var4;
         switch (this.LA(1)) {
            case 17:
               this.match(17);
               var5 = this.addToParserTokenList();
               var4 = 23;
               break;
            case 51:
               this.match(51);
               var5 = this.addToParserTokenList();
               var4 = 24;
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         var3 = this.prefixExpr(var1);
         var2 = new ExprSIMPLE_ARITH(var4, (Expr)var2, var3);
         ((Expr)var2).appendMainEJBQL(var5);
      }

      return (Expr)var2;
   }

   public final Expr prefixExpr(boolean var1) throws RecognitionException, TokenStreamException {
      Object var2 = null;
      if (this.LA(1) == 50 && _tokenSet_4.member(this.LA(2))) {
         this.match(50);
         this.addToParserTokenList();
         Expr var4 = this.prefixExpr(var1);
         ExprINTEGER var3 = new ExprINTEGER(19, "-1");
         var2 = new ExprTIMES(23, var3, var4);
         ((Expr)var2).appendMainEJBQL("-");
      } else if (this.LA(1) == 49 && _tokenSet_4.member(this.LA(2))) {
         this.match(49);
         this.addToParserTokenList();
         var2 = this.prefixExpr(var1);
      } else {
         if (!_tokenSet_4.member(this.LA(1)) || !_tokenSet_3.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         var2 = this.primaryExpr(var1);
      }

      return (Expr)var2;
   }

   public final Expr primaryExpr(boolean var1) throws RecognitionException, TokenStreamException {
      Expr var2 = null;
      Object var3 = null;
      EJBQLToken var4 = null;
      String var5 = null;
      switch (this.LA(1)) {
         case 1:
         case 6:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 17:
         case 19:
         case 23:
         case 24:
         case 26:
         case 28:
         case 29:
         case 30:
         case 31:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 42:
         case 43:
         case 47:
         case 49:
         case 50:
         case 51:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
            var2 = this.value(var1);
            return var2;
         case 2:
         case 3:
         case 4:
         case 5:
         case 7:
         case 18:
         case 20:
         case 21:
         case 22:
         case 25:
         case 27:
         case 40:
         case 41:
         case 44:
         case 45:
         case 46:
         case 48:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 8:
         case 16:
         case 32:
         case 52:
            switch (this.LA(1)) {
               case 16:
                  this.match(16);
                  var4 = this.addToParserTokenList();
                  var5 = var4.getTokenText();
                  var3 = new ExprSIMPLE_QUALIFIER(49, var5);
                  ((Expr)var3).appendMainEJBQL(var5);
               case 8:
               case 32:
               case 52:
                  switch (this.LA(1)) {
                     case 52:
                        this.match(52);
                        var4 = this.addToParserTokenList();
                        var5 = var4.getTokenText();
                        var3 = new ExprSIMPLE_QUALIFIER(64, var5);
                        ((Expr)var3).appendMainEJBQL(var5);
                     case 8:
                     case 32:
                        switch (this.LA(1)) {
                           case 32:
                              this.match(32);
                              var4 = this.addToParserTokenList();
                              var5 = var4.getTokenText();
                              var3 = new ExprEXISTS(65, false);
                              ((Expr)var3).appendMainEJBQL(var5);
                           case 8:
                              this.match(8);
                              this.addToParserTokenList();
                              var2 = this.conditionalExpr((Expr)var3, var1);
                              this.match(10);
                              this.addToParserTokenList();
                              var2.prependPreEJBQL("( ");
                              var2.appendPostEJBQL(") ");
                              return var2;
                           default:
                              throw new NoViableAltException(this.LT(1), this.getFilename());
                        }
                     default:
                        throw new NoViableAltException(this.LT(1), this.getFilename());
                  }
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
            }
      }
   }

   public final Expr value(boolean var1) throws RecognitionException, TokenStreamException {
      Object var2 = null;
      Object var3 = null;
      Object var4 = null;
      switch (this.LA(1)) {
         case 1:
         case 6:
         case 10:
         case 17:
         case 19:
         case 23:
         case 24:
         case 26:
         case 28:
         case 29:
         case 30:
         case 31:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 42:
         case 43:
         case 47:
         case 49:
         case 50:
         case 51:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
            var2 = this.function();
            break;
         case 2:
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         case 16:
         case 18:
         case 20:
         case 21:
         case 22:
         case 25:
         case 27:
         case 32:
         case 40:
         case 41:
         case 44:
         case 45:
         case 46:
         case 48:
         case 52:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 9:
            var2 = this.id();
            break;
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
            if (var1) {
               var2 = this.aggregateTarget();
            }
            break;
         case 53:
            var2 = this.variable();
            break;
         case 54:
            var2 = this.string();
            break;
         case 55:
         case 56:
            var2 = this.bool();
            break;
         case 57:
            var2 = this.number();
      }

      return (Expr)var2;
   }

   public final Expr function() throws RecognitionException, TokenStreamException {
      Expr var1 = null;
      switch (this.LA(1)) {
         case 1:
         case 6:
         case 10:
         case 17:
         case 19:
         case 23:
         case 24:
         case 26:
         case 28:
         case 29:
         case 30:
         case 31:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 42:
         case 43:
         case 44:
         case 47:
         case 49:
         case 50:
         case 51:
         case 64:
         case 65:
         case 66:
            var1 = this.arith_function();
            break;
         case 2:
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         case 9:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 18:
         case 20:
         case 21:
         case 22:
         case 25:
         case 27:
         case 32:
         case 40:
         case 41:
         case 45:
         case 46:
         case 48:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
            var1 = this.string_function();
      }

      return var1;
   }

   public final Expr variable() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      ExprVARIABLE var1 = null;
      EJBQLToken var3 = null;
      var2 = this.LT(1);
      this.match(53);
      this.addToParserTokenList("?", false);
      var3 = this.addToParserTokenList();
      var1 = new ExprVARIABLE(25, var2.getText(), this.lastIdValue);
      var1.appendMainEJBQL("?");
      var1.appendMainEJBQL(var3);
      return var1;
   }

   public final Expr bool() throws RecognitionException, TokenStreamException {
      ExprBOOLEAN var1 = null;
      EJBQLToken var2 = null;
      switch (this.LA(1)) {
         case 55:
            this.match(55);
            var2 = this.addToParserTokenList();
            var1 = new ExprBOOLEAN(14);
            var1.appendMainEJBQL(var2);
            break;
         case 56:
            this.match(56);
            var2 = this.addToParserTokenList();
            var1 = new ExprBOOLEAN(15);
            var1.appendMainEJBQL(var2);
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      return var1;
   }

   public final Expr numeric_literal() throws RecognitionException, TokenStreamException {
      Object var1 = null;
      switch (this.LA(1)) {
         case 49:
            this.match(49);
            this.addToParserTokenList();
            var1 = this.number();
            break;
         case 50:
            this.match(50);
            this.addToParserTokenList();
            Expr var3 = this.number();
            ExprINTEGER var2 = new ExprINTEGER(19, "-1");
            var1 = new ExprTIMES(23, var2, var3);
            ((Expr)var1).appendMainEJBQL("-");
            break;
         case 57:
            var1 = this.number();
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      return (Expr)var1;
   }

   public final Expr case_value() throws RecognitionException, TokenStreamException {
      Expr var1 = null;
      switch (this.LA(1)) {
         case 9:
            var1 = this.id();
            break;
         case 53:
            var1 = this.variable();
            break;
         case 54:
            var1 = this.string();
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      return var1;
   }

   public final Expr case_value_order_group_by() throws RecognitionException, TokenStreamException {
      Expr var1 = null;
      switch (this.LA(1)) {
         case 9:
            var1 = this.id();
            break;
         case 57:
            var1 = this.number();
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      return var1;
   }

   public final Expr string_function() throws RecognitionException, TokenStreamException {
      Expr var2 = null;
      Expr var3 = null;
      Object var1 = null;
      String var4 = null;
      EJBQLToken var5 = null;

      try {
         Expr var8;
         switch (this.LA(1)) {
            case 58:
               this.match(58);
               var5 = this.addToParserTokenList();
               var4 = "CONCAT";
               this.match(8);
               this.addToParserTokenList();
               var8 = this.value(false);
               this.match(6);
               this.addToParserTokenList();
               var2 = this.value(false);
               this.match(10);
               this.addToParserTokenList();
               var1 = new ExprSTRING_FUNCTION(54, var8, var2);
               ((Expr)var1).appendMainEJBQL(var5);
               break;
            case 59:
               this.match(59);
               this.addToParserTokenList();
               var4 = "SUBSTRING";
               this.match(8);
               var5 = this.addToParserTokenList();
               var8 = this.value(false);
               this.match(6);
               this.addToParserTokenList();
               var2 = this.additiveExpr(false);
               this.match(6);
               this.addToParserTokenList();
               var3 = this.additiveExpr(false);
               this.match(10);
               this.addToParserTokenList();
               var1 = new ExprSTRING_FUNCTION(55, var8, var2, var3);
               ((Expr)var1).appendMainEJBQL(var5);
               break;
            case 60:
               this.match(60);
               var5 = this.addToParserTokenList();
               var4 = "LENGTH";
               this.match(8);
               this.addToParserTokenList();
               var8 = this.value(false);
               this.match(10);
               this.addToParserTokenList();
               var1 = new ExprSTRING_FUNCTION(57, var8);
               ((Expr)var1).appendMainEJBQL(var5);
               break;
            case 61:
               this.match(61);
               var5 = this.addToParserTokenList();
               var4 = "LOCATE";
               this.match(8);
               this.addToParserTokenList();
               var8 = this.value(false);
               this.match(6);
               this.addToParserTokenList();
               var2 = this.value(false);
               switch (this.LA(1)) {
                  case 6:
                     this.match(6);
                     this.addToParserTokenList();
                     var3 = this.additiveExpr(false);
                  case 10:
                     this.match(10);
                     var1 = new ExprSTRING_FUNCTION(56, var8, var2, var3);
                     ((Expr)var1).appendMainEJBQL(var5);
                     return (Expr)var1;
                  default:
                     throw new NoViableAltException(this.LT(1), this.getFilename());
               }
            case 62:
            case 63:
               var1 = this.case_function(false);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      } catch (Exception var7) {
         this.appendCurrTokenIsKeywordMaybe();
         if (var4.equals("CONCAT")) {
            this.exceptionBuff.append(" Error in CONCAT(String, String) function. \n");
         } else if (var4.equals("SUBSTRING")) {
            this.exceptionBuff.append(" Error in SUBSTRING(String, start, length) function. \n");
         } else if (var4.equals("LENGTH")) {
            this.exceptionBuff.append(" Error in LENGTH(String) function. \n");
         } else if (var4.equals("LOCATE")) {
            this.exceptionBuff.append(" Error in LOCATE(String, String, [,start]) function. \n");
         }

         this.exceptionBuff.append(" Check for proper syntax. \n Check that none of the arguments are EJB QL keywords. \n\n");
      }

      return (Expr)var1;
   }

   public final Expr arith_function() throws RecognitionException, TokenStreamException {
      Expr var2 = null;
      Object var3 = null;
      ExprARITH_FUNCTION var1 = null;
      String var4 = null;
      EJBQLToken var5 = null;

      try {
         Expr var8;
         switch (this.LA(1)) {
            case 1:
            case 6:
            case 10:
            case 17:
            case 19:
            case 23:
            case 24:
            case 26:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 42:
            case 43:
            case 44:
            case 47:
            case 49:
            case 50:
            case 51:
               break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 20:
            case 21:
            case 22:
            case 25:
            case 27:
            case 32:
            case 40:
            case 41:
            case 45:
            case 46:
            case 48:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            case 64:
               this.match(64);
               var5 = this.addToParserTokenList();
               var4 = "ABS";
               this.match(8);
               this.addToParserTokenList();
               var8 = this.additiveExpr(false);
               this.match(10);
               this.addToParserTokenList();
               var1 = new ExprARITH_FUNCTION(58, var8);
               var1.appendMainEJBQL(var5);
               break;
            case 65:
               this.match(65);
               var5 = this.addToParserTokenList();
               var4 = "SQRT";
               this.match(8);
               this.addToParserTokenList();
               var8 = this.additiveExpr(false);
               this.match(10);
               this.addToParserTokenList();
               var1 = new ExprARITH_FUNCTION(59, var8);
               var1.appendMainEJBQL(var5);
               break;
            case 66:
               this.match(66);
               var5 = this.addToParserTokenList();
               var4 = "MOD";
               this.match(8);
               this.addToParserTokenList();
               var8 = this.additiveExpr(false);
               this.match(6);
               this.addToParserTokenList();
               var2 = this.additiveExpr(false);
               this.match(10);
               this.addToParserTokenList();
               var1 = new ExprARITH_FUNCTION(76, var8, var2);
               var1.appendMainEJBQL(var5);
         }
      } catch (Exception var7) {
         this.appendCurrTokenIsKeywordMaybe();
         if (var4.equals("ABS")) {
            this.exceptionBuff.append(" Error in ABS(number) function. \n");
         } else if (var4.equals("SQRT")) {
            this.exceptionBuff.append(" Error in SQRT(double) function. \n");
         } else if (var4.equals("MOD")) {
            this.exceptionBuff.append(" Error in MOD(int, int) function. \n");
         }

         this.exceptionBuff.append(" Check for proper syntax. \n Check that none of the arguments are EJB QL keywords. \n\n");
      }

      return var1;
   }

   private static final long[] mk_tokenSet_0() {
      long[] var0 = new long[]{360711170L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_1() {
      long[] var0 = new long[]{-4445460755465501118L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_2() {
      long[] var0 = new long[]{-407918989279486L, 7L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_3() {
      long[] var0 = new long[]{-105553258086574L, 7L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_4() {
      long[] var0 = new long[]{-407918989279422L, 7L, 0L, 0L};
      return var0;
   }
}
