package weblogic.diagnostics.instrumentation.action;

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
import java.util.List;

public class MethodDescriptorParser extends LLkParser implements MethodDescriptorParserTokenTypes {
   private List inputParameters;
   public static final String[] _tokenNames = new String[]{"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "WS", "LPAREN", "RPAREN", "BYTE", "BOOLEAN", "CHAR", "DOUBLE", "FLOAT", "INT", "LONG", "SHORT", "VOID", "ARRAY_PREFIX", "REFERENCE_TYPE_PREFIX", "SEMI_COLON", "CLASS_NAME"};
   public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

   public List getInputParameters() {
      return this.inputParameters;
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length != 1) {
         System.err.println("Invalid number of arguments");
         System.exit(1);
      }

      MethodDescriptorLexer var1 = new MethodDescriptorLexer(new StringReader(var0[0]));
      MethodDescriptorParser var2 = new MethodDescriptorParser(var1);
      var2.methodDescriptor();
      System.out.println("Parsed method descriptor: " + var2.inputParameters);
   }

   protected MethodDescriptorParser(TokenBuffer var1, int var2) {
      super(var1, var2);
      this.inputParameters = new ArrayList();
      this.tokenNames = _tokenNames;
   }

   public MethodDescriptorParser(TokenBuffer var1) {
      this((TokenBuffer)var1, 1);
   }

   protected MethodDescriptorParser(TokenStream var1, int var2) {
      super(var1, var2);
      this.inputParameters = new ArrayList();
      this.tokenNames = _tokenNames;
   }

   public MethodDescriptorParser(TokenStream var1) {
      this((TokenStream)var1, 1);
   }

   public MethodDescriptorParser(ParserSharedInputState var1) {
      super(var1, 1);
      this.inputParameters = new ArrayList();
      this.tokenNames = _tokenNames;
   }

   public final void methodDescriptor() throws RecognitionException, TokenStreamException {
      this.match(5);
      this.inputParams();
      this.match(6);
      this.anyType();
   }

   public final void inputParams() throws RecognitionException, TokenStreamException {
      while(_tokenSet_0.member(this.LA(1))) {
         String var1 = this.arrayType();
         this.inputParameters.add(var1);
      }

   }

   public final void anyType() throws RecognitionException, TokenStreamException {
      switch (this.LA(1)) {
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 16:
         case 19:
            this.arrayType();
            break;
         case 15:
            this.match(15);
            break;
         case 17:
         case 18:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

   }

   public final String arrayType() throws RecognitionException, TokenStreamException {
      String var1 = null;
      Token var2 = null;

      String var4;
      for(var4 = ""; this.LA(1) == 16; var4 = var4 + "[]") {
         var2 = this.LT(1);
         this.match(16);
      }

      String var3 = this.inputArgType();
      var1 = var3 + var4;
      return var1;
   }

   public final String inputArgType() throws RecognitionException, TokenStreamException {
      String var1 = null;
      switch (this.LA(1)) {
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
            var1 = this.primitiveType();
            break;
         case 15:
         case 16:
         case 17:
         case 18:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 19:
            var1 = this.referenceType();
      }

      return var1;
   }

   public final String primitiveType() throws RecognitionException, TokenStreamException {
      String var1 = null;
      switch (this.LA(1)) {
         case 7:
            this.match(7);
            var1 = "byte";
            break;
         case 8:
            this.match(8);
            var1 = "boolean";
            break;
         case 9:
            this.match(9);
            var1 = "char";
            break;
         case 10:
            this.match(10);
            var1 = "double";
            break;
         case 11:
            this.match(11);
            var1 = "float";
            break;
         case 12:
            this.match(12);
            var1 = "int";
            break;
         case 13:
            this.match(13);
            var1 = "long";
            break;
         case 14:
            this.match(14);
            var1 = "short";
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      return var1;
   }

   public final String referenceType() throws RecognitionException, TokenStreamException {
      String var1 = null;
      Token var2 = null;
      var2 = this.LT(1);
      this.match(19);
      var1 = var2.getText().replace('/', '.');
      return var1;
   }

   private static final long[] mk_tokenSet_0() {
      long[] var0 = new long[]{622464L, 0L};
      return var0;
   }
}
