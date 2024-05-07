package weblogic.jms.common;

import java.util.Random;

public final class RandomLoadBalancer implements LoadBalancer {
   private DistributedDestinationImpl[] dests = null;
   private int size;
   private int unit;
   private int[] upper;
   private int[] lower;
   private int[] rate;
   private Random random;

   public RandomLoadBalancer() {
   }

   public RandomLoadBalancer(DistributedDestinationImpl[] var1) {
      this.refresh(var1);
   }

   public void refresh(DistributedDestinationImpl[] var1) {
      if (var1 != null && var1.length != 0 && var1[0] != null) {
         this.dests = var1;
         this.random = new Random();

         int var3;
         for(var3 = 0; var3 < var1.length && var1[var3] != null; ++var3) {
         }

         this.size = var3;
         int[] var2 = new int[this.size];
         this.upper = new int[this.size];
         this.lower = new int[this.size];
         this.rate = new int[this.size];
         this.unit = 0;

         for(var3 = 0; var3 < this.size; ++var3) {
            var2[var3] = var1[var3].getWeight();
            this.unit += var2[var3];
            var2[var3] *= this.size;
         }

         for(var3 = 0; var3 < this.size; ++var3) {
            int var4 = Integer.MAX_VALUE;
            int var6 = 0;
            int var5 = 0;
            int var7 = 0;

            for(int var8 = 0; var8 < this.size; ++var8) {
               if (var2[var8] > 0 && var2[var8] < var4) {
                  var4 = var2[var8];
                  var6 = var8;
               }

               if (var2[var8] > var5) {
                  var5 = var2[var8];
                  var7 = var8;
               }
            }

            this.upper[var3] = var6;
            this.lower[var3] = var7;
            if (var4 >= this.unit) {
               this.rate[var3] = this.unit;
            } else {
               this.rate[var3] = var4;
            }

            var2[var6] = 0;
            var2[var7] -= this.unit - var4;
         }

      } else {
         this.dests = null;
      }
   }

   public DistributedDestinationImpl getNext(DDTxLoadBalancingOptimizer var1) {
      if (this.dests == null) {
         return null;
      } else {
         int var2 = this.random.nextInt(this.size);
         int var3 = this.random.nextInt(this.unit);
         DistributedDestinationImpl var4;
         if (var3 < this.rate[var2]) {
            var4 = this.dests[this.upper[var2]];
         } else {
            var4 = this.dests[this.lower[var2]];
         }

         if (var1 != null && !var4.isLocal() && !var1.visited(var4)) {
            int var5;
            for(var5 = 0; var5 < this.size && !var1.visited(this.dests[var2]); ++var5) {
               ++var2;
               if (var2 >= this.size) {
                  var2 = 0;
               }
            }

            if (var5 < this.size) {
               var4 = this.dests[var2];
            } else {
               var1.addVisitedDispatcher(var4);
            }
         }

         return var4;
      }
   }

   public DistributedDestinationImpl getNext(int var1) {
      return this.dests[var1];
   }
}
