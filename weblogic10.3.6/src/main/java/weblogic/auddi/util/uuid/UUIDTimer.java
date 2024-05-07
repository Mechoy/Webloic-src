package weblogic.auddi.util.uuid;

import java.security.SecureRandom;

public class UUIDTimer {
   private static final long clockOffset = 122192928000000000L;
   private static final long clockMultiplier = 10000L;
   private static final long clockMultiplierL = 10000L;
   private final SecureRandom rnd;
   private final byte[] clockSequence = new byte[2];
   private long lastTimeStamp = 0L;
   private int clockCounter = 0;

   public UUIDTimer(SecureRandom var1) {
      this.rnd = var1;
      this.initTimeStamps();
   }

   private void initTimeStamps() {
      this.rnd.nextBytes(this.clockSequence);
      byte[] var1 = new byte[1];
      this.rnd.nextBytes(var1);
      this.clockCounter = var1[0] & 255;
      this.lastTimeStamp = 0L;
   }

   public void getTimestamp(byte[] var1) {
      var1[8] = this.clockSequence[0];
      var1[9] = this.clockSequence[1];
      long var2 = System.currentTimeMillis();
      if (var2 < this.lastTimeStamp) {
         this.initTimeStamps();
      } else if (var2 == this.lastTimeStamp) {
         if ((long)this.clockCounter == 10000L) {
            byte[] var4 = new byte[1];
            this.rnd.nextBytes(var4);
            this.clockCounter = var4[0] & 255;

            do {
               try {
                  Thread.sleep(1L);
               } catch (InterruptedException var6) {
               }

               var2 = System.currentTimeMillis();
            } while(var2 == this.lastTimeStamp);

            this.lastTimeStamp = var2;
         }
      } else {
         this.clockCounter &= 255;
         this.lastTimeStamp = var2;
      }

      var2 *= 10000L;
      var2 += 122192928000000000L;
      var2 += (long)this.clockCounter;
      int var7 = (int)(var2 >>> 32);
      int var5 = (int)var2;
      var1[0] = (byte)(var5 >>> 24);
      var1[1] = (byte)(var5 >>> 16);
      var1[2] = (byte)(var5 >>> 8);
      var1[3] = (byte)var5;
      var1[4] = (byte)(var7 >>> 8);
      var1[5] = (byte)var7;
      var1[6] = (byte)(var7 >>> 24);
      var1[7] = (byte)(var7 >>> 16);
      ++this.clockCounter;
   }
}
