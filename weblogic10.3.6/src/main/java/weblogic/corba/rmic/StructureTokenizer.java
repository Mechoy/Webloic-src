package weblogic.corba.rmic;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import weblogic.rmi.utils.Utilities;

final class StructureTokenizer extends StreamTokenizer {
   boolean eofOK;
   String currFile;
   int nestingLevel;
   final boolean verbose;
   int errors;

   StructureTokenizer(Reader var1) {
      super(var1);
      this.eofOK = false;
      this.currFile = "[none]";
      this.nestingLevel = 0;
      this.verbose = false;
      this.errors = 0;
      this.init();
   }

   StructureTokenizer(Reader var1, String var2) {
      this(var1);
      this.currFile = var2;
   }

   void init() {
      this.resetSyntax();
      this.whitespaceChars(0, 32);
      this.wordChars(33, 126);
      this.eolIsSignificant(false);
      this.commentChar(59);
      this.ordinaryChar(40);
      this.ordinaryChar(41);
      this.ordinaryChar(91);
      this.ordinaryChar(93);
      this.quoteChar(34);
   }

   void aargh(String var1) throws Exception {
      ++this.errors;
      System.err.println(this.currFile + ": " + var1);
      if (this.errors > 20) {
         throw new ParseException("Exceeded 20 errors. Aborted.");
      }
   }

   Structure parseStructure() throws Exception {
      this.match('(');
      this.eofOK = false;
      ++this.nestingLevel;
      Structure var1 = new Structure();
      var1.name = this.nextWord();
      var1.elements = this.parseElements();
      this.match(')');
      --this.nestingLevel;
      if (this.nestingLevel == 0) {
         this.eofOK = true;
      }

      return var1;
   }

   public int nextToken() throws IOException {
      int var1 = super.nextToken();
      if (var1 == 34) {
         var1 = -3;
      }

      if (var1 == -1 && !this.eofOK) {
         System.err.println("Unexpected end of file in '" + this.currFile + "'");
         Runtime.getRuntime().exit(-1);
      }

      return var1;
   }

   Hashtable parseElements() throws Exception {
      Hashtable var1 = new Hashtable();

      while(true) {
         int var4 = this.nextToken();
         char var5 = (char)var4;
         if (var5 == ')') {
            this.putBack();
            return var1;
         }

         String var2;
         Object var3;
         if (var5 == '(') {
            this.putBack();
            Structure var6 = this.parseStructure();
            var2 = var6.name;
            var3 = var6.elements;
         } else {
            var2 = this.sval;
            var3 = this.parseValue();
         }

         var1.put(var2, var3);
      }
   }

   Object parseValue() throws Exception {
      int var1 = this.nextToken();
      char var2 = (char)var1;
      if (var2 == '[') {
         this.putBack();
         return this.parseStringVector();
      } else if (var1 == -3) {
         return this.sval;
      } else {
         this.fatalError("Expected word or '[' words ']'. Got " + this.interpret());
         return this.sval;
      }
   }

   Vector parseStringVector() throws Exception {
      Vector var1 = new Vector();
      this.match('[');

      while(true) {
         int var2 = this.nextToken();
         char var3 = (char)var2;
         if (var3 == ']') {
            return var1;
         }

         if (var2 == -3) {
            var1.addElement(this.sval);
         } else {
            this.fatalError("Expected string or ']'. Got " + this.interpret());
         }
      }
   }

   void match(char var1) throws Exception {
      int var2 = this.nextToken();
      if ((char)var2 != var1) {
         this.fatalError("Expected '" + var1 + "'. Got " + this.interpret());
      }
   }

   void fatalError(String var1) throws ParseException {
      throw new ParseException(this.lineno(), var1);
   }

   String nextWord() throws Exception {
      int var1 = this.nextToken();
      if (var1 == -3) {
         return this.sval;
      } else {
         this.fatalError("Expected a word. Got " + this.interpret());
         return null;
      }
   }

   String interpret() {
      switch (this.ttype) {
         case -3:
         case 34:
            return "word (" + this.sval + ")";
         case -2:
            return "number (" + this.nval + ")";
         case 10:
            return "end of line";
         default:
            return "character '" + (char)this.ttype + "'";
      }
   }

   void putBack() throws Exception {
      this.pushBack();
   }

   static Class[] getParameterTypes(String var0) throws ClassNotFoundException {
      if (var0 == null) {
         return new Class[0];
      } else if (var0.equals("")) {
         return new Class[0];
      } else {
         int var1 = var0.indexOf(44);
         if (var1 == -1) {
            return new Class[]{getParameterType(var0)};
         } else {
            StringTokenizer var2 = new StringTokenizer(var0, ",");
            int var3 = var2.countTokens();
            Class[] var4 = new Class[var3];

            for(int var5 = 0; var5 < var3; ++var5) {
               var4[var5] = getParameterType(var2.nextToken());
            }

            return var4;
         }
      }
   }

   private static Class getParameterType(String var0) throws ClassNotFoundException {
      if (var0.charAt(var0.length() - 1) == ']') {
         int var1 = var0.lastIndexOf(91);
         int var2 = var0.indexOf(91);
         int[] var3 = new int[(var1 - var2) / 2 + 1];
         var0 = var0.substring(0, var2);
         return Array.newInstance(getParameterType(var0), var3).getClass();
      } else if (var0.equals("boolean")) {
         return Boolean.TYPE;
      } else if (var0.equals("int")) {
         return Integer.TYPE;
      } else if (var0.equals("short")) {
         return Short.TYPE;
      } else if (var0.equals("long")) {
         return Long.TYPE;
      } else if (var0.equals("double")) {
         return Double.TYPE;
      } else if (var0.equals("float")) {
         return Float.TYPE;
      } else if (var0.equals("char")) {
         return Character.TYPE;
      } else {
         return var0.equals("byte") ? Byte.TYPE : Utilities.classForName(var0);
      }
   }

   private static Method getMethod(Class var0, String var1, Class[] var2) throws NoSuchMethodException {
      StringBuffer var3 = new StringBuffer();
      int var4 = 0;

      for(int var5 = var1.length(); var4 < var5; ++var4) {
         char var6 = var1.charAt(var4);
         if (!Character.isWhitespace(var6)) {
            var3.append(var6);
         }
      }

      var1 = var3.toString();

      try {
         return var0.getMethod(var1, var2);
      } catch (NoSuchMethodException var7) {
         System.err.println("Method " + var1 + " in " + var0 + " not found. Skipping.");
         throw var7;
      }
   }

   static Method getMethod(Class var0, String var1, String var2) throws NoSuchMethodException, ClassNotFoundException {
      return getMethod(var0, var1, getParameterTypes(var2));
   }
}
