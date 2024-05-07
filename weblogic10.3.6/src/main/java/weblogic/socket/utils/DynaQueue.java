package weblogic.socket.utils;

public final class DynaQueue implements QueueDef {
   private static final boolean DEBUG = false;
   private boolean verbose = false;
   private Object[] q;
   private int count = 0;
   private int getPos = 0;
   private int putPos = 0;
   private Object[] qput;
   private Object[] qlast;
   private int blockCount = 0;
   private int maxBlockCount = 0;
   private int blockSize = 256;
   private boolean cancelled = false;
   private String name = "(unknown)";
   private int departures = 0;

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public String toString() {
      return this.name;
   }

   public DynaQueue(String var1, int var2) {
      this.q = new Object[var2 + 1];
      this.name = var1;
      this.blockSize = var2;
      this.qput = this.qlast = this.q;
      this.q[var2] = this.q;
      this.blockCount = 1;
      this.resize(1);
   }

   public int count() {
      return this.count;
   }

   public int size() {
      return this.maxBlockCount == 0 ? Integer.MAX_VALUE : this.maxBlockCount * this.blockSize;
   }

   public int departures() {
      return this.departures;
   }

   public synchronized void put(Object var1) throws QueueFullException {
      if (this.putPos == this.blockSize) {
         if (this.qput == this.qlast) {
            if (this.blockCount == this.maxBlockCount) {
               throw new QueueFullException();
            }

            this.resize(1);
         }

         this.qput = (Object[])((Object[])this.qput[this.blockSize]);
         this.putPos = 0;
      }

      ++this.count;
      this.qput[this.putPos++] = var1;
      this.notify();
   }

   private final boolean full() {
      return this.blockCount == this.maxBlockCount && this.putPos == this.blockSize;
   }

   public synchronized void putW(Object var1) {
      while(this.full() && !this.cancelled) {
         try {
            this.wait();
         } catch (InterruptedException var3) {
         }
      }

      if (!this.full()) {
         try {
            this.put(var1);
         } catch (QueueFullException var4) {
         }

      } else if (this.cancelled) {
         if (!this.full()) {
            System.out.println(this + "******!!!!!!!!!! QUEUE2 " + this.cancelled + " count = " + this.count);
         }

         this.cancelled = false;
      } else {
         throw new AssertionError("Queue invariant failed count=" + this.count + "; cancel = " + this.cancelled);
      }
   }

   public synchronized void cancelWait() {
      this.cancelled = true;
      this.notifyAll();
   }

   public void resetCancel() {
      this.cancelled = false;
   }

   public synchronized boolean empty() {
      return this.count == 0;
   }

   public Object peek() {
      return this.count <= 0 ? null : this.q[this.getPos];
   }

   public synchronized Object get() {
      Object var1 = null;
      if (this.count <= 0) {
         return null;
      } else {
         --this.count;
         var1 = this.q[this.getPos];
         this.q[this.getPos] = null;
         if (++this.getPos == this.blockSize) {
            if (this.qput == this.q) {
               this.qput = (Object[])((Object[])this.q[this.blockSize]);
               this.putPos = 0;
            }

            if (this.count >= (this.blockCount - 2) * this.blockSize) {
               this.qlast = this.q;
               this.q = (Object[])((Object[])this.q[this.blockSize]);
            } else {
               --this.blockCount;
               Object[] var2 = this.q;
               this.q = (Object[])((Object[])var2[this.blockSize]);
               var2[this.blockSize] = null;
               this.qlast[this.blockSize] = this.q;
            }

            this.getPos = 0;
         }

         ++this.departures;
         return var1;
      }
   }

   public synchronized Object getW() {
      while(this.count <= 0 && !this.cancelled) {
         try {
            this.wait();
         } catch (InterruptedException var2) {
         }
      }

      if (this.count > 0) {
         return this.get();
      } else if (this.cancelled) {
         if (this.count != 0) {
            System.out.println(this + "******!!!!!!!!!! QUEUE2 " + this.cancelled + " count = " + this.count);
         }

         this.cancelled = false;
         return null;
      } else {
         throw new AssertionError("Queue invarient failed count=" + this.count + "; cancel = " + this.cancelled);
      }
   }

   public synchronized Object getW(int var1) {
      long var2 = System.currentTimeMillis();
      long var4 = (long)var1;

      while(this.count <= 0 && !this.cancelled) {
         long var6 = System.currentTimeMillis();
         var4 -= var6 - var2;
         if (var4 <= 0L) {
            break;
         }

         var2 = var6;

         try {
            this.wait(var4);
         } catch (InterruptedException var9) {
         }
      }

      if (this.count > 0) {
         return this.get();
      } else {
         if (this.cancelled) {
            if (this.count != 0) {
               System.out.println(this + "******!!!!!!!!!! QUEUE2 " + this.cancelled + " count = " + this.count);
            }

            this.cancelled = false;
         }

         return null;
      }
   }

   public synchronized void resize(int var1) {
      Object[] var2;
      for(this.blockCount += var1; var1 > 0; --var1) {
         var2 = new Object[this.blockSize + 1];
         this.qlast[this.blockSize] = var2;
         var2[this.blockSize] = this.q;
         this.qlast = var2;
      }

      while(var1 < 0) {
         var2 = (Object[])((Object[])this.qput[this.blockSize]);
         this.qput[this.blockSize] = var2[this.blockSize];
         var2[this.blockSize] = null;
         ++var1;
      }

   }
}
