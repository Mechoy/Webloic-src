package weblogic.iiop;

import java.io.UnsupportedEncodingException;

final class CodeSetsComponent extends TaggedComponent {
   private static final int[] DEFAULT_CHAR_CONVERSION_CODE_SETS = new int[]{65568, 65537, 83951617};
   private static final int[] DEFAULT_WCHAR_CONVERSION_CODE_SETS = new int[]{65792, 65801, 83951617};
   private static CodeSetsComponent DEFAULT;
   private int charNativeCodeSet;
   private int[] charConversionCodeSets;
   private int wcharNativeCodeSet;
   private int[] wcharConversionCodeSets;

   public int negotiatedCharCodeSet() throws UnsupportedEncodingException {
      if (CodeSet.supportedCharCodeSet(this.charNativeCodeSet)) {
         return this.charNativeCodeSet;
      } else {
         for(int var1 = 0; var1 < this.charConversionCodeSets.length; ++var1) {
            if (CodeSet.supportedCharCodeSet(this.charConversionCodeSets[var1])) {
               return this.charConversionCodeSets[var1];
            }
         }

         throw new UnsupportedEncodingException();
      }
   }

   public int negotiatedWcharCodeSet() throws UnsupportedEncodingException {
      if (this.supportedWcharCodeSet(83951617)) {
         return 83951617;
      } else if (CodeSet.supportedWcharCodeSet(this.wcharNativeCodeSet)) {
         return this.wcharNativeCodeSet;
      } else {
         for(int var1 = 0; var1 < this.wcharConversionCodeSets.length; ++var1) {
            if (CodeSet.supportedWcharCodeSet(this.wcharConversionCodeSets[var1])) {
               return this.wcharConversionCodeSets[var1];
            }
         }

         throw new UnsupportedEncodingException();
      }
   }

   private CodeSetsComponent() {
      super(1);
      this.charNativeCodeSet = CodeSet.DEFAULT_CHAR_NATIVE_CODE_SET;
      this.charConversionCodeSets = DEFAULT_CHAR_CONVERSION_CODE_SETS;
      this.wcharNativeCodeSet = CodeSet.DEFAULT_WCHAR_NATIVE_CODE_SET;
      this.wcharConversionCodeSets = DEFAULT_WCHAR_CONVERSION_CODE_SETS;
   }

   public static final CodeSetsComponent getDefault() {
      if (DEFAULT == null) {
         Class var0 = CodeSetsComponent.class;
         synchronized(CodeSetsComponent.class) {
            DEFAULT = new CodeSetsComponent();
         }
      }

      return DEFAULT;
   }

   public static final void resetDefault() {
      Class var0 = CodeSetsComponent.class;
      synchronized(CodeSetsComponent.class) {
         DEFAULT = null;
      }
   }

   public CodeSetsComponent(IIOPInputStream var1) {
      super(1);
      this.read(var1);
   }

   public final boolean supportedCharCodeSet(int var1) {
      if (this.charNativeCodeSet == var1) {
         return true;
      } else {
         for(int var2 = 0; var2 < this.charConversionCodeSets.length; ++var2) {
            if (this.charConversionCodeSets[var2] == var1) {
               return true;
            }
         }

         return false;
      }
   }

   public final boolean supportedWcharCodeSet(int var1) {
      if (this.wcharNativeCodeSet == var1) {
         return true;
      } else {
         for(int var2 = 0; var2 < this.wcharConversionCodeSets.length; ++var2) {
            if (this.wcharConversionCodeSets[var2] == var1) {
               return true;
            }
         }

         return false;
      }
   }

   public final void read(IIOPInputStream var1) {
      long var2 = var1.startEncapsulation();
      this.charNativeCodeSet = var1.read_long();
      int var4 = var1.read_long();
      this.charConversionCodeSets = new int[var4];

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         this.charConversionCodeSets[var5] = var1.read_long();
      }

      this.wcharNativeCodeSet = var1.read_long();
      var4 = var1.read_long();
      this.wcharConversionCodeSets = new int[var4];

      for(var5 = 0; var5 < var4; ++var5) {
         this.wcharConversionCodeSets[var5] = var1.read_long();
      }

      var1.endEncapsulation(var2);
   }

   public final void write(IIOPOutputStream var1) {
      var1.write_long(this.tag);
      long var2 = var1.startEncapsulation();
      var1.write_long(this.charNativeCodeSet);
      var1.write_long(this.charConversionCodeSets.length);

      int var4;
      for(var4 = 0; var4 < this.charConversionCodeSets.length; ++var4) {
         var1.write_long(this.charConversionCodeSets[var4]);
      }

      var1.write_long(this.wcharNativeCodeSet);
      var1.write_long(this.wcharConversionCodeSets.length);

      for(var4 = 0; var4 < this.wcharConversionCodeSets.length; ++var4) {
         var1.write_long(this.wcharConversionCodeSets[var4]);
      }

      var1.endEncapsulation(var2);
   }
}
