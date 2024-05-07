package weblogic.entitlement.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TextFilter {
   public static final char ALL_CHAR = '*';
   public static final char ESCAPE_CHAR = '\\';
   public static final Token ALL_STRINGS_TOKEN = new SpecialToken('*');
   private List mTokens = null;

   public TextFilter() {
      this.mTokens = new ArrayList();
   }

   public TextFilter(String var1) {
      char[] var2 = var1.toCharArray();
      this.mTokens = new ArrayList(countTokens(var2));
      StringBuffer var3 = new StringBuffer(var2.length);

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (var2[var4] == '*') {
            if (var3.length() > 0) {
               this.mTokens.add(new Token(var3.toString()));
            }

            this.mTokens.add(ALL_STRINGS_TOKEN);
            var3.setLength(0);
         } else if (var2[var4] == '\\') {
            ++var4;
            if (var4 < var2.length) {
               var3.append(var2[var4]);
            }
         } else {
            var3.append(var2[var4]);
         }
      }

      if (var3.length() > 0) {
         this.mTokens.add(new Token(var3.toString()));
      }

   }

   public int getTokenCount() {
      return this.mTokens.size();
   }

   public Token getToken(int var1) {
      return (Token)this.mTokens.get(var1);
   }

   public void add(Token var1) {
      this.mTokens.add(var1);
   }

   public void add(String var1) {
      this.add(new Token(var1));
   }

   public Iterator tokens() {
      return this.mTokens.iterator();
   }

   public Token remove(int var1) {
      return (Token)this.mTokens.remove(var1);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      int var2 = 0;

      for(int var3 = this.getTokenCount(); var2 < var3; ++var2) {
         var1.append(this.getToken(var2));
      }

      return var1.toString();
   }

   public String toString(Escaping var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      int var4 = 0;

      for(int var5 = this.getTokenCount(); var4 < var5; ++var4) {
         Token var6 = this.getToken(var4);
         if (var6 == ALL_STRINGS_TOKEN) {
            var3.append(var2);
         } else {
            var3.append(var1.escapeString(var6.getText()));
         }
      }

      return var3.toString();
   }

   public boolean accept(String var1) {
      return this.accept(var1, 0, this.getTokenCount());
   }

   private boolean accept(String var1, int var2, int var3) {
      for(int var4 = var2; var4 < var3; ++var4) {
         Token var5 = this.getToken(var4);
         if (var5.isSpecial()) {
            if (var5 == ALL_STRINGS_TOKEN) {
               int var9 = var4 + 1;
               int var7 = 0;

               for(int var8 = var1.length(); var7 < var8; ++var7) {
                  if (this.accept(var1.substring(var7), var9, var3)) {
                     return true;
                  }
               }

               return this.accept("", var9, var3);
            }

            throw new RuntimeException("Unknown token");
         }

         String var6 = var5.getText();
         if (!var1.startsWith(var6)) {
            return false;
         }

         var1 = var1.substring(var6.length());
      }

      return var1.length() == 0;
   }

   private static int countTokens(char[] var0) {
      int var1 = 0;
      boolean var2 = false;

      for(int var3 = 0; var3 < var0.length; ++var3) {
         if (var0[var3] == '*') {
            ++var1;
            var2 = false;
         } else {
            if (var0[var3] == '\\') {
               ++var3;
               if (var0.length >= var3) {
                  break;
               }
            }

            if (!var2) {
               ++var1;
               var2 = true;
            }
         }
      }

      return var1;
   }

   private static class SpecialToken extends Token {
      private SpecialToken(char var1) {
         super(String.valueOf(var1));
      }

      public boolean isSpecial() {
         return true;
      }

      public boolean equals(Object var1) {
         return this == var1;
      }

      public String toString() {
         return this.getText();
      }

      // $FF: synthetic method
      SpecialToken(char var1, Object var2) {
         this(var1);
      }
   }

   public static class Token {
      private String text = null;

      public Token(String var1) {
         if (var1.length() == 0) {
            throw new IllegalArgumentException("no text");
         } else {
            this.text = var1;
         }
      }

      public String getText() {
         return this.text;
      }

      public boolean isSpecial() {
         return false;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof Token)) {
            return false;
         } else {
            Token var2 = (Token)var1;
            return !var2.isSpecial() && this.text.equals(var2.getText());
         }
      }

      public String toString() {
         StringBuffer var1 = null;
         char[] var2 = this.text.toCharArray();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] == '*' || var2[var3] == '\\') {
               if (var1 == null) {
                  var1 = new StringBuffer(var2.length * 2 - var3);
                  var1.append(this.text.substring(0, var3));
               }

               var1.append('\\');
            }

            if (var1 != null) {
               var1.append(var2[var3]);
            }
         }

         return var1 != null ? var1.toString() : this.text;
      }
   }
}
