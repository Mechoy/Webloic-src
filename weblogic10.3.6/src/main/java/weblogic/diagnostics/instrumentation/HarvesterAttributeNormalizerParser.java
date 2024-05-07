package weblogic.diagnostics.instrumentation;

import antlr.LLkParser;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class HarvesterAttributeNormalizerParser extends LLkParser implements HarvesterAttributeNormalizerParserTokenTypes {
   public static final String[] _tokenNames = new String[]{"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "WS", "LPAREN", "RPAREN", "COMMA", "TYPE_NAME", "STAR_WILDCARD", "POSITIONAL_WILDCARD"};

   private static Set createValidStatsKeySet() {
      HashSet var0 = new HashSet();
      var0.add("min");
      var0.add("max");
      var0.add("avg");
      var0.add("count");
      var0.add("sum");
      var0.add("sum_of_squares");
      var0.add("std_deviation");
      return var0;
   }

   private static String normalizeKey(String var0) {
      boolean var2 = isRegex(var0);
      int var3 = var0.length();
      String var1;
      if (!var2) {
         var1 = "(" + var0 + ")";
      } else {
         var1 = "{" + escapeRegexMetaChars(var0) + "}";
      }

      return var1;
   }

   private static boolean isRegex(String var0) {
      if (var0.equals("*")) {
         return false;
      } else {
         int var1 = var0.length();
         if (var1 == 0) {
            return false;
         } else {
            boolean var2 = var0.indexOf("*") >= 0;
            var2 = var2 || var0.indexOf("?") >= 0;
            var2 = var2 || var0.indexOf("%") >= 0;
            return var2;
         }
      }
   }

   private static String escapeRegexMetaChars(String var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var3 = var0.charAt(var2);
         switch (var3) {
            case '$':
               var1.append("\\$");
               break;
            case '%':
            case '*':
               var1.append(".*?");
               break;
            case '.':
               var1.append("\\.");
               break;
            case '?':
               var1.append("[^,]*?");
               break;
            default:
               var1.append(var3);
         }
      }

      return var1.toString();
   }

   private static void validateStatsKey(String var0) {
      if (!HarvesterAttributeNormalizerParser.ValidStatsKeySetInitializer.VALID_STATS_KEYS.contains(var0)) {
         throw new IllegalArgumentException("Invalid statistics key " + var0);
      }
   }

   private static String getFullyQualifiedTypeName(String var0) {
      int var1 = var0.indexOf(".");
      if (var1 == -1) {
         try {
            return Class.forName("java.lang." + var0).getName();
         } catch (Exception var3) {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length != 1) {
         System.err.println("Invalid number of arguments");
         System.exit(1);
      }

      HarvesterAttributeNormalizerLexer var1 = new HarvesterAttributeNormalizerLexer(new StringReader(var0[0]));
      HarvesterAttributeNormalizerParser var2 = new HarvesterAttributeNormalizerParser(var1);
      String var3 = var2.normalizeAttributeSpec();
      System.out.println("Normalized attribute spec: " + var3);
   }

   protected HarvesterAttributeNormalizerParser(TokenBuffer var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public HarvesterAttributeNormalizerParser(TokenBuffer var1) {
      this((TokenBuffer)var1, 1);
   }

   protected HarvesterAttributeNormalizerParser(TokenStream var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public HarvesterAttributeNormalizerParser(TokenStream var1) {
      this((TokenStream)var1, 1);
   }

   public HarvesterAttributeNormalizerParser(ParserSharedInputState var1) {
      super(var1, 1);
      this.tokenNames = _tokenNames;
   }

   public final String normalizeAttributeSpec() throws RecognitionException, TokenStreamException {
      String var1 = null;
      String var2 = "";
      String var3 = "";
      String var4 = "";
      String var5 = "";
      var2 = this.classNameKey();
      var3 = this.methodNameKey();
      var4 = this.methodParamsKey();
      var5 = this.methodStatsKey();
      var1 = var2 + var3 + var4 + var5;
      return var1;
   }

   public final String classNameKey() throws RecognitionException, TokenStreamException {
      String var1 = null;
      Token var2 = null;
      Token var3 = null;
      this.match(5);
      String var4;
      switch (this.LA(1)) {
         case 8:
            var2 = this.LT(1);
            this.match(8);
            var4 = var2.getText();
            break;
         case 9:
            var3 = this.LT(1);
            this.match(9);
            var4 = var3.getText();
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.match(6);
      var1 = normalizeKey(var4);
      return var1;
   }

   public final String methodNameKey() throws RecognitionException, TokenStreamException {
      String var1 = null;
      Token var2 = null;
      Token var3 = null;
      this.match(5);
      String var4;
      switch (this.LA(1)) {
         case 8:
            var2 = this.LT(1);
            this.match(8);
            var4 = var2.getText();
            break;
         case 9:
            var3 = this.LT(1);
            this.match(9);
            var4 = var3.getText();
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.match(6);
      var1 = normalizeKey(var4);
      return var1;
   }

   public final String methodParamsKey() throws RecognitionException, TokenStreamException {
      String var1 = "";
      Token var2 = null;
      Token var3 = null;
      this.match(5);
      switch (this.LA(1)) {
         case 7:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 8:
         case 9:
         case 10:
            switch (this.LA(1)) {
               case 8:
                  var2 = this.LT(1);
                  this.match(8);
                  var1 = getFullyQualifiedTypeName(var2.getText());
                  break;
               case 9:
                  this.match(9);
                  var1 = "*";
                  break;
               case 10:
                  this.match(10);
                  var1 = "?";
                  break;
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            while(this.LA(1) == 7) {
               this.match(7);
               switch (this.LA(1)) {
                  case 8:
                     var3 = this.LT(1);
                     this.match(8);
                     var1 = var1 + "," + getFullyQualifiedTypeName(var3.getText());
                     break;
                  case 9:
                     this.match(9);
                     var1 = var1 + ",*";
                     break;
                  case 10:
                     this.match(10);
                     var1 = var1 + ",?";
                     break;
                  default:
                     throw new NoViableAltException(this.LT(1), this.getFilename());
               }
            }
         case 6:
            this.match(6);
            if (!isRegex(var1)) {
               var1 = var1.replace(",", "\\,");
            }

            var1 = normalizeKey(var1);
            return var1;
      }
   }

   public final String methodStatsKey() throws RecognitionException, TokenStreamException {
      String var1;
      var1 = "";
      Token var2 = null;
      Token var3 = null;
      Token var4 = null;
      this.match(5);
      label16:
      switch (this.LA(1)) {
         case 8:
            var2 = this.LT(1);
            this.match(8);
            var1 = var2.getText();
            validateStatsKey(var1);

            while(true) {
               if (this.LA(1) != 7) {
                  break label16;
               }

               this.match(7);
               var3 = this.LT(1);
               this.match(8);
               String var5 = var3.getText();
               validateStatsKey(var5);
               var1 = var1 + "," + var5;
            }
         case 9:
            var4 = this.LT(1);
            this.match(9);
            var1 = var4.getText();
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.match(6);
      var1 = normalizeKey(var1);
      return var1;
   }

   private static class ValidStatsKeySetInitializer {
      private static final Set VALID_STATS_KEYS = HarvesterAttributeNormalizerParser.createValidStatsKeySet();
   }
}
