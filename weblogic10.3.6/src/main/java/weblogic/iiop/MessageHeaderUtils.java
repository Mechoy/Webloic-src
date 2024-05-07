package weblogic.iiop;

public class MessageHeaderUtils implements MessageHeaderConstants {
   public static final int getIntBigEndian(byte[] var0, int var1) {
      int var2 = var0[var1] << 24 & -16777216;
      int var3 = var0[var1 + 1] << 16 & 16711680;
      int var4 = var0[var1 + 2] << 8 & '\uff00';
      int var5 = var0[var1 + 3] & 255;
      return var2 | var3 | var4 | var5;
   }

   public static final int getIntLittleEndian(byte[] var0, int var1) {
      int var2 = var0[var1 + 3] << 24 & -16777216;
      int var3 = var0[var1 + 2] << 16 & 16711680;
      int var4 = var0[var1 + 1] << 8 & '\uff00';
      int var5 = var0[var1] & 255;
      return var2 | var3 | var4 | var5;
   }

   public static final int getShortBigEndian(byte[] var0, int var1) {
      int var2 = var0[var1] << 8 & '\uff00';
      int var3 = var0[var1 + 1] & 255;
      return var2 | var3;
   }

   public static final int getShortLittleEndian(byte[] var0, int var1) {
      int var2 = var0[var1 + 1] << 8 & '\uff00';
      int var3 = var0[var1] & 255;
      return var2 | var3;
   }

   public static final int getMsgLength(byte[] var0) {
      byte var1 = (byte)(var0[6] & 1);
      return var1 == 0 ? getIntBigEndian(var0, 8) + 12 : getIntLittleEndian(var0, 8) + 12;
   }
}
