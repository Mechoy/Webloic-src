package weblogic.diagnostics.harvester.internal;

import antlr.LLkParser;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class HarvesterDefaultAttributeNormalizerParser extends LLkParser implements HarvesterDefaultAttributeNormalizerParserTokenTypes {
   private static final String REGEX_METACHARS = ".#^$\\?+*|[]()";
   private static final String INVALID_IDENT_CHARS = ".#^$\\?+*|[]()%";
   private static final char WILD_CARD_CHAR = '%';
   public static final String[] _tokenNames = new String[]{"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "WS", "LSQPAREN", "RSQPAREN", "COMMA", "PERIOD", "SEMICOLON", "DIGIT", "LETTER", "INTEGER", "IDENTIFIER", "STAR_WILDCARD", "KEYSPEC"};

   private static void validateCharacters(String var0) throws RecognitionException {
      int var1 = var0.length();

      for(int var2 = 0; var2 < var1; ++var2) {
         char var3 = var0.charAt(var2);
         if (".#^$\\?+*|[]()%".indexOf(var3) >= 0) {
            throw new RecognitionException("Unexpected character '" + var3 + "' in IDENTIFIER " + var0);
         }
      }

   }

   private static String normalizeKey(String var0) {
      int var1 = var0.length();
      ArrayList var2 = new ArrayList();
      StringBuilder var4 = new StringBuilder();
      boolean var5 = false;
      boolean var6 = false;

      for(int var7 = 0; var7 < var1; ++var7) {
         char var8 = var0.charAt(var7);
         if (var8 == '\\') {
            ++var7;
            if (var7 < var1) {
               var8 = var0.charAt(var7);
               if (var8 == '%') {
                  var4.append('%');
               }

               var4.append(var8);
            }
         } else if (var8 == ',') {
            var6 |= addKeySpec(var2, var4);
            var4 = new StringBuilder();
         } else {
            if (var8 == '%') {
               var5 = true;
            }

            var4.append(var8);
         }
      }

      var6 |= addKeySpec(var2, var4);
      if (var6) {
         return "(*)";
      } else {
         return normalizeKey(var2, var5);
      }
   }

   private static boolean addKeySpec(List var0, StringBuilder var1) {
      boolean var2 = false;
      if (var1.length() > 0) {
         String var3 = var1.toString().trim();
         var0.add(var3);
         if ("*".equals(var3)) {
            var2 = true;
         }
      }

      return var2;
   }

   private static String normalizeKey(List var0, boolean var1) {
      int var2 = var0.size();
      StringBuilder var3 = new StringBuilder();
      int var4;
      if (var1) {
         var3.append("{");

         for(var4 = 0; var4 < var2; ++var4) {
            if (var4 > 0) {
               var3.append("|");
            }

            String var5 = (String)var0.get(var4);
            escapeRegexMetaChars(var5, var3);
         }

         var3.append("}");
      } else {
         var3.append("(");

         for(var4 = 0; var4 < var2; ++var4) {
            if (var4 > 0) {
               var3.append(",");
            }

            unescapeWildcard((String)var0.get(var4), var3);
         }

         var3.append(")");
      }

      return var3.toString();
   }

   private static void unescapeWildcard(String var0, StringBuilder var1) {
      int var2 = var0.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         char var4 = var0.charAt(var3);
         if (var4 == '%') {
            char var5 = var3 < var2 - 1 ? var0.charAt(var3 + 1) : 0;
            if (var5 == '%') {
               ++var3;
            }
         } else if (var4 == ',') {
            var1.append('\\');
         }

         var1.append(var4);
      }

   }

   private static void escapeRegexMetaChars(String var0, StringBuilder var1) {
      int var2 = var0.length();
      boolean var3 = var2 > 0 && var0.charAt(0) == '%';
      boolean var4 = var2 > 0 && var0.charAt(var2 - 1) == '%';
      if (!var3) {
         var1.append("^");
      }

      for(int var5 = 0; var5 < var2; ++var5) {
         char var6 = var0.charAt(var5);
         if (var6 == '%') {
            char var7 = var5 < var2 - 1 ? var0.charAt(var5 + 1) : 0;
            if (var7 == '%') {
               var1.append('%');
               ++var5;
            } else {
               var1.append(".*?");
            }
         } else if (".#^$\\?+*|[]()".indexOf(var6) >= 0) {
            var1.append("\\");
            var1.append(var6);
         } else {
            var1.append(var6);
         }
      }

      if (!var4) {
         var1.append("$");
      }

   }

   public static void main(String[] var0) throws Exception {
      if (var0.length != 1) {
         System.err.println("Invalid number of arguments");
         System.exit(1);
      }

      System.out.println("Normalizing: " + var0[0]);
      HarvesterDefaultAttributeNormalizerLexer var1 = new HarvesterDefaultAttributeNormalizerLexer(new StringReader(var0[0]));
      HarvesterDefaultAttributeNormalizerParser var2 = new HarvesterDefaultAttributeNormalizerParser(var1);
      String var3 = var2.normalizeAttributeSpec();
      System.out.println("Normalized attribute spec: " + var3);
   }

   protected HarvesterDefaultAttributeNormalizerParser(TokenBuffer var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public HarvesterDefaultAttributeNormalizerParser(TokenBuffer var1) {
      this((TokenBuffer)var1, 1);
   }

   protected HarvesterDefaultAttributeNormalizerParser(TokenStream var1, int var2) {
      super(var1, var2);
      this.tokenNames = _tokenNames;
   }

   public HarvesterDefaultAttributeNormalizerParser(TokenStream var1) {
      this((TokenStream)var1, 1);
   }

   public HarvesterDefaultAttributeNormalizerParser(ParserSharedInputState var1) {
      super(var1, 1);
      this.tokenNames = _tokenNames;
   }

   public final String normalizeAttributeSpec() throws RecognitionException, TokenStreamException {
      String var1 = null;
      String var2 = this.attributeNameSpec();
      String var3 = this.remainderSpec();
      var1 = var2 + var3;
      return var1;
   }

   public final String attributeNameSpec() throws RecognitionException, TokenStreamException {
      String var1 = null;
      Token var2 = null;
      var2 = this.LT(1);
      this.match(13);
      var1 = var2.getText();
      validateCharacters(var1);
      return var1;
   }

   public final String remainderSpec() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      String var3 = null;
      String var4 = null;
      Object var5 = null;
      new ArrayList();
      String var1 = "";
      switch (this.LA(1)) {
         case 1:
            this.match(1);
            var1 = "";
            break;
         case 5:
            this.match(5);
            var3 = this.indexSpec();
            this.match(6);
            var4 = this.remainderSpec();
            var1 = "[" + var3 + "]" + var4;
            break;
         case 8:
            this.match(8);
            var1 = this.normalizeAttributeSpec();
            var1 = "." + var1;
            break;
         case 15:
            var2 = this.LT(1);
            this.match(15);
            var4 = this.remainderSpec();
            var1 = normalizeKey(var2.getText()) + var4;
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      return var1;
   }

   public final String indexSpec() throws RecognitionException, TokenStreamException {
      Token var2 = null;
      String var1;
      switch (this.LA(1)) {
         case 12:
            var2 = this.LT(1);
            this.match(12);
            var1 = var2.getText();
            break;
         case 14:
            this.match(14);
            var1 = "*";
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      return var1;
   }
}
