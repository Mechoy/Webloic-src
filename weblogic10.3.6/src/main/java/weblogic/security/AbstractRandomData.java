package weblogic.security;

import java.security.ProviderException;
import java.security.SecureRandom;

/** @deprecated */
public abstract class AbstractRandomData {
   private String provider = null;
   private String algorithm = null;
   private int initialSeedSize = 0;
   private int incrementalSeedSize = 0;
   private int seedingIntervalMillis = 0;
   private SecureRandom random = null;
   private long lastSeedTime = 0L;

   private AbstractRandomData() {
   }

   protected AbstractRandomData(String var1, String var2, int var3, int var4, int var5) {
      this.provider = var1;
      this.algorithm = var2;
      this.initialSeedSize = var3;
      this.incrementalSeedSize = var4;
      this.seedingIntervalMillis = var5;
   }

   private final synchronized void ensureInittedAndSeeded() {
      int var1 = this.incrementalSeedSize;
      if (this.random == null) {
         try {
            if (this.algorithm != null && this.provider != null) {
               this.random = SecureRandom.getInstance(this.algorithm, this.provider);
            } else if (this.algorithm != null) {
               this.random = SecureRandom.getInstance(this.algorithm);
            } else {
               this.random = new SecureRandom();
            }
         } catch (Exception var5) {
            this.random = null;
            throw new ProviderException("AbstractRandomData: Unable to instantiate SecureRandom");
         }

         var1 = this.initialSeedSize;
         this.lastSeedTime = 0L;
      }

      if (var1 > 0) {
         long var2 = System.currentTimeMillis();
         if (var2 >= this.lastSeedTime + (long)this.seedingIntervalMillis) {
            byte[] var4 = this.random.generateSeed(var1);
            this.random.setSeed(var4);
            this.lastSeedTime = var2;
         }
      }

   }

   public final byte[] getRandomBytes(int var1) {
      byte[] var2 = new byte[var1];
      this.getRandomBytes(var2);
      return var2;
   }

   public final synchronized void getRandomBytes(byte[] var1) {
      this.ensureInittedAndSeeded();
      this.random.nextBytes(var1);
   }
}
