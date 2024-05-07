package weblogic.jms.common;

public final class RRLoadBalancer implements LoadBalancer {
   private DistributedDestinationImpl[] dests = null;
   private int current;
   private int pass;
   private int min;
   private int max;
   private int size;
   private int count;

   public RRLoadBalancer() {
   }

   public RRLoadBalancer(DistributedDestinationImpl[] var1) {
      if (var1 != null && var1.length != 0 && var1[0] != null) {
         this.refresh(var1);
         this.current = 0;
         this.pass = 0;
         this.count = -1;
      } else {
         this.dests = null;
      }
   }

   public void refresh(DistributedDestinationImpl[] var1) {
      if (var1 != null && var1.length != 0 && var1[0] != null) {
         this.min = var1[0].getWeight();
         this.max = var1[0].getWeight();

         int var3;
         for(var3 = 1; var3 < var1.length && var1[var3] != null; ++var3) {
            int var2 = var1[var3].getWeight();
            if (var2 < this.min) {
               this.min = var2;
            } else if (var2 > this.max) {
               this.max = var2;
            }
         }

         if (this.dests != null && this.size > var3) {
            while(this.current < var3 && this.current >= 0 && this.dests[this.current] != var1[this.current]) {
               --this.current;
            }
         }

         this.size = var3;
         this.dests = var1;
      } else {
         this.dests = null;
      }
   }

   public DistributedDestinationImpl getNext(DDTxLoadBalancingOptimizer var1) {
      if (this.dests == null) {
         return null;
      } else {
         if (++this.current >= this.size) {
            this.current = 0;
         }

         if (++this.count >= this.size) {
            this.count = 0;
            if (++this.pass >= this.max) {
               this.pass = 0;
            }
         }

         while(this.pass >= this.min && this.dests[this.current].getWeight() <= this.pass) {
            if (++this.current >= this.size) {
               this.current = 0;
            }

            if (++this.count >= this.size) {
               this.count = 0;
               if (++this.pass >= this.max) {
                  this.pass = 0;
               }
            }
         }

         int var2 = this.current;
         if (var1 != null && !this.dests[var2].isLocal() && !var1.visited(this.dests[var2])) {
            int var3;
            for(var3 = 0; var3 < this.size && !var1.visited(this.dests[var2]); ++var3) {
               ++var2;
               if (var2 >= this.size) {
                  var2 = 0;
               }
            }

            if (var3 >= this.size) {
               var1.addVisitedDispatcher(this.dests[var2]);
            }
         }

         return this.dests[var2];
      }
   }

   public DistributedDestinationImpl getNext(int var1) {
      return this.dests[var1];
   }
}
