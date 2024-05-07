package weblogic.auddi.util.uuid;

import java.security.SecureRandom;

public class UUIDGen {
   private final SecureRandom rnd = new SecureRandom();
   private UUIDTimer timer;
   private final String hexChars;
   private final Object lock;

   public UUIDGen() {
      this.timer = new UUIDTimer(this.rnd);
      this.hexChars = "0123456789abcdef";
      this.lock = new Object();
   }

   public static void main(String[] var0) {
      int var1 = Integer.parseInt(var0[0]);
      System.out.println("here 1");
      UUIDGen var2 = new UUIDGen();
      System.out.println("here 2");
      System.out.println("==========TimeBasedUUIDWithRandomNode============");

      int var3;
      String var4;
      for(var3 = 0; var3 < var1; ++var3) {
         var4 = var2.generateTimeBasedUUIDWithRandomNode();
         System.out.print("UUID: ");
         System.out.println(var4);
      }

      System.out.println("here 3");
      System.out.println("==========RandomBasedUUID============");

      for(var3 = 0; var3 < var1; ++var3) {
         var4 = var2.generateRandomBasedUUID();
         System.out.print("UUID: ");
         System.out.println(var4);
      }

      System.out.println("here 4");
      System.out.println("==========TimeBasedUUID============");

      for(var3 = 0; var3 < var1; ++var3) {
         var4 = var2.generateTimeBasedUUID("00ab3fd234f3");
         System.out.print("UUID: ");
         System.out.println(var4);
      }

      System.out.println("here 5");
   }

   private boolean isValid(String var1) {
      boolean var2 = false;
      byte[] var3 = var1.getBytes();
      if (var3.length == 12) {
         for(int var4 = 0; var4 < 12; ++var4) {
            if (var3[var4] > 47 && var3[var4] < 58 || var3[var4] > 96 && var3[var4] < 123) {
               var2 = true;
            }
         }
      }

      return var2;
   }

   private String generateRandomNodeNumber() {
      byte[] var1 = new byte[6];
      this.rnd.nextBytes(var1);
      var1[0] = (byte)(var1[0] | 128);
      StringBuffer var2 = new StringBuffer(12);

      for(int var3 = 0; var3 < 6; ++var3) {
         int var4 = var1[var3] & 255;
         var2.append("0123456789abcdef".charAt(var4 >> 4));
         var2.append("0123456789abcdef".charAt(var4 & 15));
      }

      return var2.toString();
   }

   public String generateRandomBasedUUID() {
      byte[] var1 = new byte[16];
      this.rnd.nextBytes(var1);
      var1[6] = (byte)(var1[6] & 15);
      var1[6] = (byte)(var1[6] | 64);
      var1[8] = (byte)(var1[8] & 63);
      var1[8] |= -128;
      StringBuffer var2 = new StringBuffer(36);
      int var3 = 0;

      while(var3 < 16) {
         switch (var3) {
            case 4:
            case 6:
            case 8:
            case 10:
               var2.append('-');
            case 5:
            case 7:
            case 9:
            default:
               int var4 = var1[var3] & 255;
               var2.append("0123456789abcdef".charAt(var4 >> 4));
               var2.append("0123456789abcdef".charAt(var4 & 15));
               ++var3;
         }
      }

      return var2.toString();
   }

   public String generateTimeBasedUUIDWithRandomNode() {
      String var1 = this.generateRandomNodeNumber();
      return this.generateTimeBasedUUID(var1);
   }

   public String generateTimeBasedUUID(String var1) {
      if (!this.isValid(var1)) {
         var1 = this.generateRandomNodeNumber();
      }

      byte[] var2 = new byte[10];
      synchronized(this.lock) {
         this.timer.getTimestamp(var2);
      }

      var2[6] = (byte)(var2[6] & 15);
      var2[6] = (byte)(var2[6] | 16);
      var2[8] = (byte)(var2[8] & 63);
      var2[8] |= -128;
      StringBuffer var3 = new StringBuffer(23);
      int var4 = 0;

      while(var4 < 10) {
         switch (var4) {
            case 4:
            case 6:
            case 8:
               var3.append('-');
            case 5:
            case 7:
            default:
               int var5 = var2[var4] & 255;
               var3.append("0123456789abcdef".charAt(var5 >> 4));
               var3.append("0123456789abcdef".charAt(var5 & 15));
               ++var4;
         }
      }

      return var3.toString() + "-" + var1;
   }
}
