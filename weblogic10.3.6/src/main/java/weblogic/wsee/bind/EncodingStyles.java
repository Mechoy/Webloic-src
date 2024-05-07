package weblogic.wsee.bind;

public class EncodingStyles {
   public static final int STYLE_LITERAL = 1;
   public static final int STYLE_ENCODED = 2;
   public static final int STYLE_RPC = 4;
   public static final int STYLE_DOCUMENT = 8;
   public static final int STYLE_SOAP11 = 16;
   public static final int STYLE_SOAP12 = 32;
   public static final int STYLE_WRAPPED = 64;
   public static final int STYLE_BARE = 128;
   public static final int STYLE_DOCUMENT_LITERAL_BARE = 137;
   public static final int STYLE_DOCUMENT_LITERAL_WRAPPED = 73;
   public static final int STYLE_DOCUMENT_ENCODED = 10;
   public static final int STYLE_RPC_LITERAL = 5;
   public static final int STYLE_RPC_ENCODED = 6;
   public static final int STYLE_RPC_ENCODED11 = 22;

   public static boolean isLiteral(int var0) {
      return (1 & var0) != 0;
   }

   public static boolean isRpcLiteralExactly(int var0) {
      return 5 - var0 == 0;
   }

   public static boolean isEncoded(int var0) {
      return (2 & var0) != 0;
   }

   public static void main(String[] var0) {
      System.out.println(" test  DOCUMENT_LITERAL_BARE IS DOCUMENT: ");
      System.out.println(" YES DOCUMENT_LITERAL_BARE IS DOCUMENT");
      System.out.println(" test  DOCUMENT_LITERAL_WRAPPED IS NOT RPC: ");
      System.out.println(" YES DOCUMENT_LITERAL_WRAPPED IS NOT RPC");
   }
}
