package weblogic.ejb.container.ejbc.codegen;

import java.util.ArrayList;
import weblogic.utils.RecursiveDescentParser;

class MethodSignatureParser extends RecursiveDescentParser {
   private static final boolean debug = false;
   private static final boolean verbose = false;

   public MethodSignatureParser(String var1) {
      this.setSkipWhiteSpace(true);
      this.buf = var1.toCharArray();
   }

   public boolean matchSignature(MethodSignature var1) {
      this.startMatch();
      if (this.matchModifiers()) {
         var1.setModifiers((Integer)this.oval);
      }

      if (this.matchType()) {
         var1.setReturnType((Class)this.oval);
      }

      if (!this.matchID()) {
         return this.failure();
      } else {
         var1.setName(this.sval);
         if (!this.match('(')) {
            return this.failure();
         } else {
            ArrayList var2 = new ArrayList();
            ArrayList var3 = new ArrayList();

            do {
               String var5 = null;
               if (!this.matchType()) {
                  break;
               }

               Class var4 = (Class)this.oval;
               if (!this.matchID()) {
                  return this.failure();
               }

               var5 = this.sval;
               var2.add(var4);
               var3.add(var5);
            } while(this.match(','));

            int var6 = var2.size();
            var1.setParameterTypes((Class[])((Class[])var2.toArray(new Class[var6])));
            var1.setParameterNames((String[])((String[])var3.toArray(new String[var6])));
            if (!this.match(')')) {
               return this.failure();
            } else {
               if (this.matchThrows()) {
                  var1.setExceptionTypes((Class[])((Class[])this.oval));
               }

               return this.success();
            }
         }
      }
   }

   public boolean matchID() {
      this.startMatch();
      int var1 = this.peek;
      if (Character.isJavaIdentifierStart(this.buf[this.peek])) {
         ++this.peek;

         for(int var2 = this.buf.length; this.peek < var2 && Character.isJavaIdentifierPart(this.buf[this.peek]); ++this.peek) {
         }
      }

      return this.peek > var1 ? this.success() : this.failure();
   }

   public boolean matchType() {
      this.startMatch();
      StringBuffer var1 = new StringBuffer();
      if (this.matchPrimitiveType()) {
         return this.success();
      } else {
         if (this.matchID()) {
            var1.append(this.sval);
         }

         while(true) {
            this.startMatch();
            if (!this.match('.') || !this.matchID()) {
               this.failure();
               if (var1.length() == 0) {
                  return this.failure();
               } else {
                  int var2;
                  for(var2 = 0; this.match("[]"); ++var2) {
                  }

                  Class var3 = this.getType(var1.toString(), var2);
                  if (var3 != null) {
                     this.oval = var3;
                     return this.success();
                  } else {
                     return this.failure();
                  }
               }
            }

            this.success();
            var1.append(this.sval);
         }
      }
   }

   private Class getType(String var1, int var2) {
      String var3 = "";
      String var4 = "";

      try {
         if (var2 > 0) {
            StringBuffer var5 = new StringBuffer();

            for(int var6 = 0; var6 < var2; ++var6) {
               var5.append("[");
            }

            var5.append("L");
            var3 = var5.toString();
            var4 = ";";
         }

         try {
            return Class.forName(var3 + var1 + var4);
         } catch (ClassNotFoundException var7) {
            return Class.forName(var3 + "java.lang." + var1 + var4);
         }
      } catch (ClassNotFoundException var8) {
         return null;
      }
   }

   public boolean matchPrimitiveType() {
      this.startMatch();
      if (this.match("boolean")) {
         this.oval = Boolean.TYPE;
      } else if (this.match("byte")) {
         this.oval = Byte.TYPE;
      } else if (this.match("char")) {
         this.oval = Character.TYPE;
      } else if (this.match("double")) {
         this.oval = Double.TYPE;
      } else if (this.match("float")) {
         this.oval = Float.TYPE;
      } else if (this.match("int")) {
         this.oval = Integer.TYPE;
      } else if (this.match("long")) {
         this.oval = Long.TYPE;
      } else if (this.match("short")) {
         this.oval = Short.TYPE;
      } else {
         if (!this.match("void")) {
            return this.failure();
         }

         this.oval = Void.TYPE;
      }

      int var1;
      for(var1 = 0; this.match("[]"); ++var1) {
      }

      if (var1 > 0) {
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < var1; ++var3) {
            var2.append("[");
         }

         if (this.oval == Boolean.TYPE) {
            var2.append("Z");
         } else if (this.oval == Byte.TYPE) {
            var2.append("B");
         } else if (this.oval == Character.TYPE) {
            var2.append("C");
         } else if (this.oval == Double.TYPE) {
            var2.append("D");
         } else if (this.oval == Float.TYPE) {
            var2.append("F");
         } else if (this.oval == Integer.TYPE) {
            var2.append("I");
         } else if (this.oval == Long.TYPE) {
            var2.append("J");
         } else if (this.oval == Short.TYPE) {
            var2.append("S");
         }

         try {
            this.oval = Class.forName(var2.toString());
         } catch (ClassNotFoundException var4) {
            return this.failure();
         }
      }

      return this.success();
   }

   public boolean matchModifiers() {
      this.startMatch();
      int var1 = 0;
      int var2 = this.peek;

      while(true) {
         while(!this.match("public")) {
            if (this.match("protected")) {
               var1 ^= 4;
            } else if (this.match("private")) {
               var1 ^= 2;
            } else if (this.match("abstract")) {
               var1 ^= 1024;
            } else if (this.match("static")) {
               var1 ^= 8;
            } else if (this.match("final")) {
               var1 ^= 16;
            } else if (this.match("synchronized")) {
               var1 ^= 32;
            } else {
               if (!this.match("native")) {
                  if (this.peek > var2) {
                     this.oval = new Integer(var1);
                     return this.success();
                  }

                  return this.failure();
               }

               var1 ^= 256;
            }
         }

         var1 ^= 1;
      }
   }

   public boolean matchThrows() {
      this.startMatch();
      if (this.match("throws")) {
         ArrayList var1 = new ArrayList();

         while(this.matchType()) {
            var1.add(this.oval);
            if (!this.match(',')) {
               break;
            }
         }

         int var2 = var1.size();
         if (var2 > 0) {
            this.oval = var1.toArray(new Class[var2]);
            return this.success();
         }
      }

      return this.failure();
   }
}
