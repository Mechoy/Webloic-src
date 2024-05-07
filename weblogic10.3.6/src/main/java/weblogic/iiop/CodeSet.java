package weblogic.iiop;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;

public final class CodeSet extends ServiceContext {
   private int char_data;
   private int wchar_data;
   public static final int ISO_8859_1 = 65537;
   public static final String ISO_8859_1_ENC = "iso-8859-1";
   public static final int US_ASCII = 65568;
   public static final String US_ASCII_ENC = "us-ascii";
   public static final int UTF_8 = 83951617;
   public static final String UTF_8_ENC = "utf-8";
   public static int DEFAULT_CHAR_NATIVE_CODE_SET = 65568;
   public static final int UTF_16 = 65801;
   public static final String UTF_16_ENC = "utf-16";
   public static final int UCS_2 = 65792;
   public static final String UCS_2_ENC = "ucs-2";
   public static final String UTF_16LE_ENC = "utf-16le";
   public static final String UTF_16BE_ENC = "utf-16be";
   public static final int UCS_4 = 65796;
   public static int DEFAULT_WCHAR_NATIVE_CODE_SET = 65792;
   public static final HashMap codeSetTable = new HashMap();
   private static final String[] charsets = new String[]{"iso-8859-1", "us-ascii", "utf-8", "utf-16", "utf-16le", "utf-16be"};
   private static Charset[] charsetTable;

   public CodeSet() {
      super(1);
      this.char_data = DEFAULT_CHAR_NATIVE_CODE_SET;
      this.wchar_data = DEFAULT_WCHAR_NATIVE_CODE_SET;
   }

   public CodeSet(int var1, int var2) {
      super(1);
      this.char_data = var1;
      this.wchar_data = var2;
   }

   protected CodeSet(IIOPInputStream var1) {
      super(1);
      this.readEncapsulatedContext(var1);
   }

   public int getCharCodeSet() {
      return this.char_data;
   }

   public int getWcharCodeSet() {
      return this.wchar_data;
   }

   public static final int getDefaultCharCodeSet() {
      return DEFAULT_CHAR_NATIVE_CODE_SET;
   }

   public static final int getDefaultWcharCodeSet() {
      return DEFAULT_WCHAR_NATIVE_CODE_SET;
   }

   public static boolean supportedCharCodeSet(int var0) {
      return var0 == 83951617 || var0 == 65568 || var0 == 65537;
   }

   public static boolean supportedWcharCodeSet(int var0) {
      return var0 == 83951617 || var0 == 65801 || var0 == 65792;
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      this.char_data = var1.read_ulong();
      this.wchar_data = var1.read_ulong();
   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
      var1.write_ulong(this.char_data);
      var1.write_ulong(this.wchar_data);
   }

   public static final void setDefaults(int var0, int var1) {
      DEFAULT_CHAR_NATIVE_CODE_SET = var0;
      DEFAULT_WCHAR_NATIVE_CODE_SET = var1;
   }

   public static final int getOSFCodeset(String var0) {
      Integer var1 = (Integer)codeSetTable.get(var0);
      return var1 != null ? var1 : 0;
   }

   public String toString() {
      return "CodeSet Context (char_data = " + Integer.toHexString(this.char_data) + ", wchar_data = " + Integer.toHexString(this.wchar_data) + ")";
   }

   static {
      try {
         codeSetTable.put("iso-8859-1", new Integer(65537));
         codeSetTable.put("us-ascii", new Integer(65568));
         codeSetTable.put("utf-8", new Integer(83951617));
         codeSetTable.put("utf-16", new Integer(65801));
         codeSetTable.put("ucs-2", new Integer(65792));
         codeSetTable.put("iso-8859-1".toUpperCase(), new Integer(65537));
         codeSetTable.put("us-ascii".toUpperCase(), new Integer(65568));
         codeSetTable.put("utf-8".toUpperCase(), new Integer(83951617));
         codeSetTable.put("utf-16".toUpperCase(), new Integer(65801));
         codeSetTable.put("ucs-2".toUpperCase(), new Integer(65792));
         charsetTable = new Charset[codeSetTable.size()];
         codeSetTable.put("iso-8859-1", new Integer(65537));
         codeSetTable.put("us-ascii", new Integer(65568));
         codeSetTable.put("utf-8", new Integer(83951617));
         codeSetTable.put("utf-16", new Integer(65801));
         codeSetTable.put("ucs-2", new Integer(65792));

         for(int var0 = 0; var0 < charsets.length; ++var0) {
            charsetTable[var0] = Charset.forName(charsets[var0]);
         }
      } catch (UnsupportedCharsetException var1) {
      }

   }
}
