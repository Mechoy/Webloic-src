package weblogic.io.common.internal;

import java.util.Hashtable;

class WaitHashtable extends Hashtable {
   boolean cancelled = false;
   private String error = "";
   int numPut = 0;
   private static final int CLIENT_TIMEOUT_MILLSECS = 120000;

   public WaitHashtable(int var1) {
      super(var1);
   }

   public WaitHashtable(int var1, float var2) {
      super(var1, var2);
   }

   public synchronized Object put(Object var1, Object var2) {
      Object var3 = super.put(var1, var2);
      ++this.numPut;
      this.notifyAll();
      return var3;
   }

   public synchronized void cancel(String var1) {
      this.cancelled = true;
      this.error = var1;
      this.notifyAll();
   }

   public synchronized String getError() {
      return this.error;
   }

   public synchronized Object getWait(Object var1) {
      if (this.cancelled) {
         return null;
      } else {
         Object var2;
         for(var2 = this.get(var1); var2 == null && !this.cancelled; var2 = this.get(var1)) {
            int var3 = this.numPut;
            boolean var4 = false;

            try {
               this.wait(120000L);
            } catch (InterruptedException var6) {
               var4 = true;
            }

            if (var3 == this.numPut && !this.cancelled) {
               if (var4) {
                  this.cancel("T3RemoteInputStream was interrupted before numPut could increase from " + this.numPut);
               }

               this.cancel("Supplier to T3RemoteInputStream made no progress in 120000 ms, numPut=" + this.numPut);
            }
         }

         return var2;
      }
   }

   public synchronized Object removeWait(Object var1) {
      if (this.cancelled) {
         return null;
      } else {
         Object var2;
         for(var2 = this.remove(var1); var2 == null && !this.cancelled; var2 = this.remove(var1)) {
            int var3 = this.numPut;
            boolean var4 = false;

            try {
               this.wait(120000L);
            } catch (InterruptedException var6) {
               var4 = true;
            }

            if (var3 == this.numPut && !this.cancelled) {
               if (var4) {
                  this.cancel("T3RemoteInputStream was interrupted before numPut could increase from " + this.numPut);
               }

               this.cancel("Supplier to T3RemoteInputStream made no progress in 120000 ms, numPut=" + this.numPut);
            }
         }

         return var2;
      }
   }
}
